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
package org.ws2ten1.chunks;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;

import org.ws2ten1.chunkrequests.Chunkable;
import org.ws2ten1.chunkrequests.DefaultIdExtractor;
import org.ws2ten1.chunkrequests.PaginationTokenEncoder;
import org.ws2ten1.chunkrequests.SimplePaginationTokenEncoder;

/**
 * Factory to create {@link Chunk} from list and {@link Chunkable}.
 *
 * @param <E> type of entity
 * @param <ID> type of identifier
 */
@RequiredArgsConstructor
public class ChunkFactory<E, ID extends Serializable & Comparable<ID>> {
	
	/**
	 * Function to extract ID from entity.
	 */
	private final Function<E, ID> idExtractor;
	
	private final PaginationTokenEncoder encoder;
	
	
	public ChunkFactory() {
		this(new DefaultIdExtractor<>(), new SimplePaginationTokenEncoder());
	}
	
	public Chunk<E> createChunk(List<E> content, Chunkable chunkable) {
		String paginationToken = null;
		if (content.isEmpty() == false) {
			Object firstKey = null;
			if (chunkable.getPaginationToken() != null && content.isEmpty() == false) {
				firstKey = idExtractor.apply(content.get(0));
			}
			Object lastKey = null;
			if (content.isEmpty() == false) {
				lastKey = idExtractor.apply(content.get(content.size() - 1));
			}
			paginationToken = encoder.encode(firstKey, lastKey);
		}
		return new ChunkImpl<>(content, paginationToken, chunkable);
	}
}
