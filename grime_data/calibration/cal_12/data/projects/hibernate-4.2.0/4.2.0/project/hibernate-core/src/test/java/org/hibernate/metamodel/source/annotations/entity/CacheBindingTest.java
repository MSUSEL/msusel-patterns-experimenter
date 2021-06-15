/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.hibernate.metamodel.source.annotations.entity;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SharedCacheMode;

import org.junit.Test;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.metamodel.binding.Caching;
import org.hibernate.metamodel.binding.EntityBinding;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Tests for {@code o.h.a.Cache} and {@code j.p.Cacheable}.
 *
 * @author Hardy Ferentschik
 */
public class CacheBindingTest extends BaseAnnotationBindingTestCase {
	@Test
	@Resources(annotatedClasses = HibernateCacheEntity.class, cacheMode = SharedCacheMode.ALL)
	public void testHibernateCaching() {
		EntityBinding binding = getEntityBinding( HibernateCacheEntity.class );
		assertNotNull( "There should be a cache binding", binding.getHierarchyDetails().getCaching() );
		Caching caching = binding.getHierarchyDetails().getCaching();
		assertEquals( "Wrong region", "foo", caching.getRegion() );
		assertEquals( "Wrong strategy", AccessType.READ_WRITE, caching.getAccessType() );
		assertEquals( "Wrong lazy properties configuration", false, caching.isCacheLazyProperties() );
	}

	@Test
	@Resources(annotatedClasses = JpaCacheEntity.class, cacheMode = SharedCacheMode.ALL)
	public void testJpaCaching() {
		EntityBinding binding = getEntityBinding( JpaCacheEntity.class );
		assertNotNull( "There should be a cache binding", binding.getHierarchyDetails().getCaching() );
		Caching caching = binding.getHierarchyDetails().getCaching();
		assertEquals(
				"Wrong region",
				this.getClass().getName() + "$" + JpaCacheEntity.class.getSimpleName(),
				caching.getRegion()
		);
		assertEquals( "Wrong lazy properties configuration", true, caching.isCacheLazyProperties() );
	}

	@Test
	@Resources(annotatedClasses = NoCacheEntity.class, cacheMode = SharedCacheMode.NONE)
	public void testNoCaching() {
		EntityBinding binding = getEntityBinding( NoCacheEntity.class );
		assertNull( "There should be no cache binding", binding.getHierarchyDetails().getCaching() );
	}

	@Entity
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "foo", include = "non-lazy")
	class HibernateCacheEntity {
		@Id
		private int id;
	}

	@Entity
	@Cacheable
	class JpaCacheEntity {
		@Id
		private int id;
	}

	@Entity
	@Cacheable
	class NoCacheEntity {
		@Id
		private int id;
	}
}


