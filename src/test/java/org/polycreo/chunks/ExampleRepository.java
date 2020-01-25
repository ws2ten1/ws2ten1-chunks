/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.polycreo.chunks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.polycreo.chunkrequests.Chunkable;
import org.polycreo.chunkrequests.Chunkable.PaginationRelation;
import org.polycreo.chunkrequests.Direction;
import org.polycreo.chunkrequests.PaginationTokenEncoder;
import org.polycreo.chunkrequests.SimplePaginationTokenEncoder;

/**
 * Example repository implementation.
 */
public class ExampleRepository { // NOPMD - cc
	
	private PaginationTokenEncoder encoder = new SimplePaginationTokenEncoder();
	
	static final List<String> DATA = IntStream.rangeClosed('a', 'z')
		.mapToObj(i -> (char) i)
		.map(String::valueOf)
		.map(s -> s + s)
		.collect(Collectors.toList());
	
	
	public Chunk<String> findAll(Chunkable chunkable) { // NOPMD -@cs[CyclomaticComplexity|NPathComplexity]
		List<String> source = DATA;
		
		Direction direction = chunkable.getDirection();
		PaginationRelation relation = chunkable.getPaginationRelation();
		if ((direction == Direction.ASC && relation == PaginationRelation.PREV)
				|| (direction != Direction.ASC && relation != PaginationRelation.PREV)) {
			source = new ArrayList<>(DATA); // copy
			Collections.reverse(source);
		}
		
		Integer size = Optional.ofNullable(chunkable.getMaxPageSize()).orElse(20);
		List<String> content;
		if (chunkable.getPaginationToken() == null) {
			content = source.stream().limit(size).collect(Collectors.toList());
		} else {
			String key;
			if (relation == PaginationRelation.NEXT) {
				key = encoder.extractLastKey(chunkable.getPaginationToken()).orElse(null);
			} else if (relation == PaginationRelation.PREV) {
				key = encoder.extractFirstKey(chunkable.getPaginationToken()).orElse(null);
			} else {
				throw new AssertionError();
			}
			if (key == null) {
				content = source.stream().limit(size).collect(Collectors.toList());
			} else {
				content = source.stream()
					.filter(keyFilter(key, relation, direction))
					.limit(size)
					.collect(Collectors.toList());
			}
		}
		
		if (relation == PaginationRelation.PREV) {
			Collections.reverse(content);
		}
		
		String paginationToken = encoder.computeToken(chunkable, content);
		return new ChunkImpl<>(content, paginationToken, chunkable);
	}
	
	private Predicate<? super String> keyFilter(String key, PaginationRelation relation, Direction direction) {
		if ((direction == Direction.ASC && relation == PaginationRelation.NEXT)
				|| (direction != Direction.ASC && relation != PaginationRelation.NEXT)) {
			return e -> e.compareTo(key) > 0;
		} else {
			return e -> e.compareTo(key) < 0;
		}
	}
}
