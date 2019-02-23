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

import java.util.Collections;

import lombok.experimental.UtilityClass;

/**
 * Utilities about {@link Chunk}.
 */
@UtilityClass
public class Chunks {
	
	/**
	 * Empty chunk shared instance.
	 */
	@SuppressWarnings("rawtypes")
	public static final Chunk EMPTY_CHUNK = new ChunkImpl<>(Collections.emptyList(), null, null);
	
	
	/**
	 * Returns a empty chunk.
	 *
	 * @param <T> element type of chunk
	 * @return Empty chunk
	 */
	@SuppressWarnings("unchecked")
	public static final <T> Chunk<T> emptyChunk() {
		return EMPTY_CHUNK;
	}
}
