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
package org.hibernate.test.cache.infinispan.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.hibernate.cache.infinispan.InfinispanRegionFactory;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Configuration;
import org.hibernate.test.cache.infinispan.AbstractNonFunctionalTestCase;
import org.hibernate.test.cache.infinispan.NodeEnvironment;
import org.hibernate.test.cache.infinispan.util.CacheTestUtil;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * Tests for the "extra API" in EntityRegionAccessStrategy;.
 * <p>
 * By "extra API" we mean those methods that are superfluous to the 
 * function of the JBC integration, where the impl is a no-op or a static
 * false return value, UnsupportedOperationException, etc.
 * 
 * @author Galder Zamarreño
 * @since 3.5
 */
public class TransactionalExtraAPITestCase extends AbstractNonFunctionalTestCase {
	public static final String REGION_NAME = "test/com.foo.test";
	public static final String KEY = "KEY";
	public static final String VALUE1 = "VALUE1";
	public static final String VALUE2 = "VALUE2";

	private NodeEnvironment environment;
	private EntityRegionAccessStrategy accessStrategy;

	@Before
	public final void prepareLocalAccessStrategy() throws Exception {
		environment = new NodeEnvironment( createConfiguration() );
		environment.prepare();

		// Sleep a bit to avoid concurrent FLUSH problem
		avoidConcurrentFlush();

		accessStrategy = environment.getEntityRegion( REGION_NAME, null ).buildAccessStrategy( getAccessType() );
   }

	protected Configuration createConfiguration() {
		Configuration cfg = CacheTestUtil.buildConfiguration(REGION_PREFIX, InfinispanRegionFactory.class, true, false);
		cfg.setProperty(InfinispanRegionFactory.ENTITY_CACHE_RESOURCE_PROP, getCacheConfigName());
		return cfg;
	}

	@After
	public final void releaseLocalAccessStrategy() throws Exception {
		if ( environment != null ) {
			environment.release();
		}
	}

	protected final EntityRegionAccessStrategy getEntityAccessStrategy() {
		return accessStrategy;
	}

	protected String getCacheConfigName() {
		return "entity";
	}

	protected AccessType getAccessType() {
		return AccessType.TRANSACTIONAL;
	}

	@Test
	@SuppressWarnings( {"UnnecessaryBoxing"})
	public void testLockItem() {
		assertNull( getEntityAccessStrategy().lockItem( KEY, Integer.valueOf( 1 ) ) );
	}

	@Test
	public void testLockRegion() {
		assertNull( getEntityAccessStrategy().lockRegion() );
	}

	@Test
	public void testUnlockItem() {
		getEntityAccessStrategy().unlockItem( KEY, new MockSoftLock() );
	}

	@Test
	public void testUnlockRegion() {
		getEntityAccessStrategy().unlockItem( KEY, new MockSoftLock() );
	}

	@Test
	@SuppressWarnings( {"UnnecessaryBoxing"})
	public void testAfterInsert() {
		assertFalse(
				"afterInsert always returns false",
				getEntityAccessStrategy().afterInsert(
						KEY,
						VALUE1,
						Integer.valueOf( 1 )
				)
		);
	}

	@Test
	@SuppressWarnings( {"UnnecessaryBoxing"})
	public void testAfterUpdate() {
		assertFalse(
				"afterInsert always returns false",
				getEntityAccessStrategy().afterUpdate(
						KEY,
						VALUE2,
						Integer.valueOf( 1 ),
						Integer.valueOf( 2 ),
						new MockSoftLock()
				)
		);
	}

	public static class MockSoftLock implements SoftLock {
	}
}
