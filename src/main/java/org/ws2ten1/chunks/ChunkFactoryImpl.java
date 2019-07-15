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

/**
 * Default implementation of {@link ChunkFactory}.
 *
 * @param <E> type of entity
 * @param <ID> type of identifier
 */
@RequiredArgsConstructor
public class ChunkFactoryImpl<E, ID extends Serializable> implements ChunkFactory<E, ID> { // -@cs[ClassTypeParameterName]
	
	/**
	 * Function to extract ID from entity.
	 */
	private final Function<E, ID> idExtractor;
	
	private final PaginationTokenEncoder encoder;
	
	
	@Override
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
