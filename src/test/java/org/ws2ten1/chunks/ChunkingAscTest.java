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

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.data.domain.Sort.Direction;

import org.junit.Test;

import org.ws2ten1.chunks.Chunkable.PaginationRelation;

/**
 * Test for chunking.
 */
public class ChunkingAscTest {
	
	private ExampleRepository repo = new ExampleRepository();
	
	
	@Test
	public void testChunkASC_Under26() {
		// ASC chunk1 by first
		Chunkable request = new ChunkRequest(10, Direction.ASC);
		assertThat(request.getPaginationToken()).isNull();
		assertThat(request.getPaginationRelation()).isNull();
		assertThat(request.getMaxPageSize()).isEqualTo(10);
		assertThat(request.getDirection()).isEqualTo(Direction.ASC);
		
		Chunk<String> chunk = repo.findAll(request);
		assertThat(chunk.getContent()).containsExactly("aa", "bb", "cc", "dd", "ee", "ff", "gg", "hh", "ii", "jj");
		assertThat(chunk.isFirst()).isTrue();
		assertThat(chunk.isLast()).isFalse();
		assertThat(chunk.hasPrevious()).isFalse();
		assertThat(chunk.previousChunkable()).isNull();
		assertThat(chunk.hasNext()).isTrue();
		assertThat(chunk.nextChunkable()).isNotNull();
		
		// ASC chunk2 by next from chunk1
		request = chunk.nextChunkable();
		assertThat(request.getPaginationToken()).isEqualTo(chunk.getPaginationToken());
		assertThat(request.getPaginationRelation()).isEqualTo(PaginationRelation.NEXT);
		assertThat(request.getMaxPageSize()).isEqualTo(10);
		assertThat(request.getDirection()).isEqualTo(Direction.ASC);
		
		chunk = repo.findAll(request);
		assertThat(chunk.getContent()).containsExactly("kk", "ll", "mm", "nn", "oo", "pp", "qq", "rr", "ss", "tt");
		assertThat(chunk.isFirst()).isFalse();
		assertThat(chunk.isLast()).isFalse();
		assertThat(chunk.hasPrevious()).isTrue();
		assertThat(chunk.previousChunkable()).isNotNull();
		assertThat(chunk.hasNext()).isTrue();
		assertThat(chunk.nextChunkable()).isNotNull();
		
		// ASC chunk3 by next from chunk2
		request = chunk.nextChunkable();
		assertThat(request.getPaginationToken()).isEqualTo(chunk.getPaginationToken());
		assertThat(request.getPaginationRelation()).isEqualTo(PaginationRelation.NEXT);
		assertThat(request.getMaxPageSize()).isEqualTo(10);
		assertThat(request.getDirection()).isEqualTo(Direction.ASC);
		
		chunk = repo.findAll(request);
		assertThat(chunk.getContent()).containsExactly("uu", "vv", "ww", "xx", "yy", "zz");
		assertThat(chunk.isFirst()).isFalse();
		assertThat(chunk.isLast()).isTrue();
		assertThat(chunk.hasPrevious()).isTrue();
		assertThat(chunk.previousChunkable()).isNotNull();
		assertThat(chunk.hasNext()).isFalse();
		assertThat(chunk.nextChunkable()).isNull();
		
		// ASC chunk2 by prev from chunk3
		request = chunk.previousChunkable();
		assertThat(request.getPaginationToken()).isEqualTo(chunk.getPaginationToken());
		assertThat(request.getPaginationRelation()).isEqualTo(PaginationRelation.PREV);
		assertThat(request.getMaxPageSize()).isEqualTo(10);
		assertThat(request.getDirection()).isEqualTo(Direction.ASC);
		
		chunk = repo.findAll(request);
		assertThat(chunk.getContent()).containsExactly("kk", "ll", "mm", "nn", "oo", "pp", "qq", "rr", "ss", "tt");
		assertThat(chunk.isFirst()).isFalse();
		assertThat(chunk.isLast()).isFalse();
		assertThat(chunk.hasPrevious()).isTrue();
		assertThat(chunk.previousChunkable()).isNotNull();
		assertThat(chunk.hasNext()).isTrue();
		assertThat(chunk.nextChunkable()).isNotNull();
		
		// ASC chunk1 by prev from chunk2
		request = chunk.previousChunkable();
		assertThat(request.getPaginationToken()).isEqualTo(chunk.getPaginationToken());
		assertThat(request.getPaginationRelation()).isEqualTo(PaginationRelation.PREV);
		assertThat(request.getMaxPageSize()).isEqualTo(10);
		assertThat(request.getDirection()).isEqualTo(Direction.ASC);
		
		chunk = repo.findAll(request);
		assertThat(chunk.getContent()).containsExactly("aa", "bb", "cc", "dd", "ee", "ff", "gg", "hh", "ii", "jj");
		assertThat(chunk.isFirst()).isFalse(); // unknown
		assertThat(chunk.isLast()).isFalse();
		assertThat(chunk.hasPrevious()).isTrue(); // unknown
		assertThat(chunk.previousChunkable()).isNotNull();
		assertThat(chunk.hasNext()).isTrue();
		assertThat(chunk.nextChunkable()).isNotNull();
		
		// ASC chunk0 by prev from chunk1
		request = chunk.previousChunkable();
		assertThat(request.getPaginationToken()).isEqualTo(chunk.getPaginationToken());
		assertThat(request.getPaginationRelation()).isEqualTo(PaginationRelation.PREV);
		assertThat(request.getMaxPageSize()).isEqualTo(10);
		assertThat(request.getDirection()).isEqualTo(Direction.ASC);
		
		chunk = repo.findAll(request);
		assertThat(chunk.getContent()).isEmpty();
		assertThat(chunk.isFirst()).isFalse();
		assertThat(chunk.isLast()).isTrue();
		assertThat(chunk.hasPrevious()).isFalse();
		assertThat(chunk.previousChunkable()).isNull();
		assertThat(chunk.hasNext()).isTrue();
		assertThat(chunk.nextChunkable()).isNotNull();
		
		// ASC chunk1 by next from chunk0
		request = chunk.nextChunkable();
		assertThat(request.getPaginationToken()).isNotNull();
		assertThat(request.getPaginationRelation()).isEqualTo(PaginationRelation.NEXT);
		assertThat(request.getMaxPageSize()).isEqualTo(10);
		assertThat(request.getDirection()).isEqualTo(Direction.ASC);
		
		chunk = repo.findAll(request);
		assertThat(chunk.getContent()).containsExactly("aa", "bb", "cc", "dd", "ee", "ff", "gg", "hh", "ii", "jj");
		assertThat(chunk.isFirst()).isFalse();
		assertThat(chunk.isLast()).isFalse();
		assertThat(chunk.hasPrevious()).isTrue();
		assertThat(chunk.previousChunkable()).isNotNull();
		assertThat(chunk.hasNext()).isTrue();
		assertThat(chunk.nextChunkable()).isNotNull();
	}
	
	@Test
	public void testChunkASC_Exact26() {
		// ASC chunk1 by first
		Chunkable request = new ChunkRequest(26, Direction.ASC);
		assertThat(request.getPaginationToken()).isNull();
		assertThat(request.getPaginationRelation()).isNull();
		assertThat(request.getMaxPageSize()).isEqualTo(26);
		assertThat(request.getDirection()).isEqualTo(Direction.ASC);
		
		Chunk<String> chunk = repo.findAll(request);
		assertThat(chunk.getContent()).containsExactly("aa", "bb", "cc", "dd", "ee", "ff", "gg", "hh", "ii", "jj",
				"kk", "ll", "mm", "nn", "oo", "pp", "qq", "rr", "ss", "tt", "uu", "vv", "ww", "xx", "yy", "zz");
		assertThat(chunk.isFirst()).isTrue();
		assertThat(chunk.isLast()).isFalse();
		assertThat(chunk.hasPrevious()).isFalse();
		assertThat(chunk.previousChunkable()).isNull();
		assertThat(chunk.hasNext()).isTrue();
		assertThat(chunk.nextChunkable()).isNotNull();
		
		// ASC chunk2 by next from chunk1
		request = chunk.nextChunkable();
		chunk = repo.findAll(request);
		assertThat(chunk.getContent()).isEmpty();
		assertThat(chunk.isFirst()).isFalse();
		assertThat(chunk.isLast()).isTrue();
		assertThat(chunk.hasPrevious()).isTrue();
		assertThat(chunk.previousChunkable()).isNotNull();
		assertThat(chunk.hasNext()).isFalse();
		assertThat(chunk.nextChunkable()).isNull();
		
		// ASC chunk1 by prev from chunk2
		request = chunk.previousChunkable();
		chunk = repo.findAll(request);
		assertThat(chunk.getContent()).containsExactly("aa", "bb", "cc", "dd", "ee", "ff", "gg", "hh", "ii", "jj",
				"kk", "ll", "mm", "nn", "oo", "pp", "qq", "rr", "ss", "tt", "uu", "vv", "ww", "xx", "yy", "zz");
		assertThat(chunk.isFirst()).isFalse();
		assertThat(chunk.isLast()).isFalse();
		assertThat(chunk.hasPrevious()).isTrue();
		assertThat(chunk.previousChunkable()).isNotNull();
		assertThat(chunk.hasNext()).isTrue();
		assertThat(chunk.nextChunkable()).isNotNull();
	}
	
	@Test
	public void testChunkASC_Over26() {
		// ASC chunk1 by first
		Chunkable request = new ChunkRequest(30, Direction.ASC);
		assertThat(request.getPaginationToken()).isNull();
		assertThat(request.getPaginationRelation()).isNull();
		assertThat(request.getMaxPageSize()).isEqualTo(30);
		assertThat(request.getDirection()).isEqualTo(Direction.ASC);
		
		Chunk<String> chunk = repo.findAll(request);
		assertThat(chunk.getContent()).containsExactly("aa", "bb", "cc", "dd", "ee", "ff", "gg", "hh", "ii", "jj",
				"kk", "ll", "mm", "nn", "oo", "pp", "qq", "rr", "ss", "tt", "uu", "vv", "ww", "xx", "yy", "zz");
		assertThat(chunk.isFirst()).isTrue();
		assertThat(chunk.isLast()).isTrue();
		assertThat(chunk.hasPrevious()).isFalse();
		assertThat(chunk.previousChunkable()).isNull();
		assertThat(chunk.hasNext()).isFalse();
		assertThat(chunk.nextChunkable()).isNull();
	}
}
