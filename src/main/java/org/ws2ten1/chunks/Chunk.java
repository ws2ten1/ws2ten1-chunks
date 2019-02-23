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

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort.Direction;

/**
 * A part of item set.
 *
 * @param <T> Type of item
 */
public interface Chunk<T>extends Collection<T> {
	
	/**
	 * Returns the chunk content as {@link List}.
	 *
	 * @return chunk content list
	 */
	List<T> getContent();
	
	/**
	 * Returns stream of content.
	 *
	 * @return stream
	 */
	@Override
	Stream<T> stream();
	
	/**
	 * Returns pagination token.
	 *
	 * @return token
	 */
	String getPaginationToken();
	
	/**
	 * Returns the sorting direction for the {@link Chunk}.
	 *
	 * @return direction
	 */
	Direction getDirection();
	
	/**
	 * Returns whether the {@link Chunk} has content at all.
	 *
	 * @return {@code true} if this chunk contains content
	 */
	boolean hasContent();
	
	/**
	 * Returns if there is a next {@link Chunk}.
	 *
	 * @return if there is a next {@link Chunk}.
	 */
	boolean hasNext();
	
	/**
	 * Returns if there is a previous {@link Chunk}.
	 *
	 * @return if there is a previous {@link Chunk}.
	 */
	boolean hasPrev();
	
	/**
	 * Returns whether the current {@link Chunk} is the last one.
	 *
	 * @return {@code true} if this chunk is the last one
	 */
	boolean isLast();
	
	/**
	 * Returns whether the current {@link Chunk} is the first one.
	 *
	 * @return {@code true} if this chunk is the first one
	 */
	boolean isFirst();
	
	/**
	 * Returns the {@link Chunkable} to request the next {@link Chunk}. Can be {@literal null} in case the current
	 * {@link Chunk} is already the last one. Clients should check {@link #hasNext()} before calling this method to make
	 * sure they receive a non-{@literal null} value.
	 *
	 * @return {@link Chunkable}
	 */
	Chunkable nextChunkable();
	
	/**
	 * Returns the {@link Chunkable} to request the previous {@link Chunk}. Can be {@literal null} in case the current
	 * {@link Chunk} is already the first one. Clients should check {@link #hasNext()} before calling this method to make
	 * sure they receive a non-{@literal null} value.
	 *
	 * @return {@link Chunkable}
	 */
	Chunkable prevChunkable();
	
	/**
	 * Returns a new {@link Chunk} with the content of the current one mapped by the given {@link Converter}.
	 *
	 * @param converter must not be {@literal null}.
	 * @return a new {@link Chunk} with the content of the current one mapped by the given {@link Converter}.
	 */
	<S> Chunk<S> map(Converter<? super T, ? extends S> converter);
	
	/**
	 * Returns the {@link Chunkable} used to retrieve current{@link Chunk}.
	 *
	 * @return {@link Chunkable}
	 */
	Chunkable getChunkable();
}
