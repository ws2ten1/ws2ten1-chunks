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

/**
 * Factory interface of {@link Chunk}.
 *
 * @param <E> type of entity
 * @param <ID> type of identifier
 */
public interface ChunkFactory<E, ID extends Serializable> { // -@cs[InterfaceTypeParameterName]
	
	/**
	 * Create new {@link Chunk} from content list and requested {@link Chunkable}.
	 *
	 * @param content content list
	 * @param chunkable requested {@link Chunkable}
	 * @return created {@link Chunk}
	 */
	Chunk<E> createChunk(List<E> content, Chunkable chunkable);
	
}
