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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.polycreo.chunkrequests.ChunkRequest;
import org.polycreo.chunkrequests.Chunkable;
import org.polycreo.chunkrequests.Chunkable.PaginationRelation;
import org.polycreo.chunkrequests.Direction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Default information of {@link Chunk}.
 *
 * @param <T> entity type
 */
@EqualsAndHashCode(of = {
	"content",
	"paginationToken"
})
public class ChunkImpl<T> implements Chunk<T> {
	
	@JsonProperty
	private final List<T> content = new ArrayList<>();
	
	@JsonProperty
	@Getter
	private final String paginationToken;
	
	@JsonIgnore
	@Getter
	private final Chunkable chunkable;
	
	
	/**
	 * Creates a new {@link Chunk} with the given content and the given governing
	 * {@code org.springframework.data.domain.Pageable}.
	 *
	 * @param content content, must not be {@literal null}.
	 * @param paginationToken token, can be {@literal null}.
	 * @param chunkable can be {@literal null}.
	 */
	public ChunkImpl(List<T> content, String paginationToken, Chunkable chunkable) {
		if (content == null) {
			throw new IllegalArgumentException("Content must not be null!");
		}
		this.content.addAll(content);
		this.paginationToken = paginationToken;
		this.chunkable = chunkable;
	}
	
	@Override
	public Iterator<T> iterator() {
		return content.iterator();
	}
	
	@Override
	public List<T> getContent() {
		return Collections.unmodifiableList(content);
	}
	
	@Override
	public Stream<T> stream() {
		return content.stream();
	}
	
	@Override
	public Direction getDirection() {
		return chunkable == null ? null : chunkable.getDirection();
	}
	
	@Override
	public boolean hasContent() {
		return content.isEmpty() == false;
	}
	
	@Override
	public boolean hasNext() {
		if (isForward()) {
			return isLast() == false;
		}
		return true;
	}
	
	@Override
	public boolean hasPrevious() {
		if (isForward()) {
			return isFirst() == false;
		}
		return hasContent();
	}
	
	@Override
	public boolean isLast() {
		Integer maxPageSize = chunkable.getMaxPageSize();
		if (maxPageSize == null) {
			return false;
		}
		return content.size() < maxPageSize;
	}
	
	@Override
	public boolean isFirst() {
		return chunkable.getPaginationToken() == null;
	}
	
	@Override
	public Chunkable nextChunkable() {
		if (hasNext() == false) {
			return null;
		}
		return new ChunkRequest(paginationToken, PaginationRelation.NEXT,
				chunkable.getMaxPageSize(), chunkable.getDirection());
	}
	
	@Override
	public Chunkable previousChunkable() {
		if (hasPrevious() == false) {
			return null;
		}
		return new ChunkRequest(paginationToken, PaginationRelation.PREV,
				chunkable.getMaxPageSize(), chunkable.getDirection());
	}
	
	@Override
	public <S> Chunk<S> map(Function<? super T, ? extends S> mapper) {
		return new ChunkImpl<>(getConvertedContent(mapper), paginationToken, chunkable);
	}
	
	private boolean isForward() {
		return Optional.ofNullable(chunkable.getPaginationRelation())
			.map(PaginationRelation.NEXT::equals)
			.orElse(true);
	}
	
	/**
	 * Applies the given {@link Function} to the content of the {@link Chunk}.
	 *
	 * @param mapper must not be {@literal null}.
	 * @return mapped content list
	 * @since 0.11
	 */
	protected <S> List<S> getConvertedContent(Function<? super T, ? extends S> mapper) {
		if (mapper == null) {
			throw new IllegalArgumentException("mapper must not be null!");
		}
		return content.stream().map(mapper).collect(Collectors.toList());
	}
	
	@Override
	public int size() {
		return content.size();
	}
	
	@Override
	public boolean isEmpty() {
		return content.isEmpty();
	}
	
	@Override
	public boolean contains(Object o) {
		return content.contains(o);
	}
	
	@Override
	public Object[] toArray() {
		return content.toArray();
	}
	
	@Override
	public <U> U[] toArray(U[] a) {
		return content.toArray(a);
	}
	
	@Override
	public boolean add(T t) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		return content.containsAll(c);
	}
	
	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		String contentType = "UNKNOWN";
		List<T> list = getContent();
		
		if (list.isEmpty() == false) {
			contentType = list.get(0).getClass().getName();
		}
		
		return String.format(Locale.ENGLISH, "Chunk containing %s instances", contentType);
	}
}
