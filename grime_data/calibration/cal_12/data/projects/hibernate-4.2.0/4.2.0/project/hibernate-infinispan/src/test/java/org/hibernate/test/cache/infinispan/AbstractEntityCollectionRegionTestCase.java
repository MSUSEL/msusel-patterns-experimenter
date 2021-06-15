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
package org.hibernate.test.cache.infinispan;

import java.util.Properties;

import org.junit.Test;

import org.hibernate.cache.infinispan.InfinispanRegionFactory;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.cache.spi.TransactionalDataRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cfg.Configuration;
import org.hibernate.test.cache.infinispan.util.CacheTestUtil;
import org.hibernate.testing.ServiceRegistryBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Base class for tests of EntityRegion and CollectionRegion implementations.
 *
 * @author Galder Zamarreño
 * @since 3.5
 */
public abstract class AbstractEntityCollectionRegionTestCase extends AbstractRegionImplTestCase {
	@Test
	public void testSupportedAccessTypes() throws Exception {
		supportedAccessTypeTest();
	}

	private void supportedAccessTypeTest() throws Exception {
		Configuration cfg = CacheTestUtil.buildConfiguration( "test", InfinispanRegionFactory.class, true, false );
		String entityCfg = "entity";
		cfg.setProperty( InfinispanRegionFactory.ENTITY_CACHE_RESOURCE_PROP, entityCfg );
		InfinispanRegionFactory regionFactory = CacheTestUtil.startRegionFactory(
				ServiceRegistryBuilder.buildServiceRegistry( cfg.getProperties() ),
				cfg,
				getCacheTestSupport()
		);
		supportedAccessTypeTest( regionFactory, cfg.getProperties() );
	}

	/**
	 * Creates a Region using the given factory, and then ensure that it handles calls to
	 * buildAccessStrategy as expected when all the various {@link AccessType}s are passed as
	 * arguments.
	 */
	protected abstract void supportedAccessTypeTest(RegionFactory regionFactory, Properties properties);

	@Test
	public void testIsTransactionAware() throws Exception {
		Configuration cfg = CacheTestUtil.buildConfiguration( "test", InfinispanRegionFactory.class, true, false );
		InfinispanRegionFactory regionFactory = CacheTestUtil.startRegionFactory(
				ServiceRegistryBuilder.buildServiceRegistry( cfg.getProperties() ),
				cfg,
				getCacheTestSupport()
		);
		TransactionalDataRegion region = (TransactionalDataRegion) createRegion(
				regionFactory, "test/test", cfg.getProperties(), getCacheDataDescription()
		);
		assertTrue( "Region is transaction-aware", region.isTransactionAware() );
		CacheTestUtil.stopRegionFactory( regionFactory, getCacheTestSupport() );
//		cfg = CacheTestUtil.buildConfiguration( "test", InfinispanRegionFactory.class, true, false );
//		// Make it non-transactional
//		cfg.getProperties().remove( AvailableSettings.JTA_PLATFORM );
//		regionFactory = CacheTestUtil.startRegionFactory(
//				ServiceRegistryBuilder.buildServiceRegistry( cfg.getProperties() ),
//				cfg,
//				getCacheTestSupport()
//		);
//		region = (TransactionalDataRegion) createRegion(
//				regionFactory, "test/test", cfg.getProperties(), getCacheDataDescription()
//		);
//		assertFalse( "Region is not transaction-aware", region.isTransactionAware() );
//		CacheTestUtil.stopRegionFactory( regionFactory, getCacheTestSupport() );
	}

	@Test
	public void testGetCacheDataDescription() throws Exception {
		Configuration cfg = CacheTestUtil.buildConfiguration( "test", InfinispanRegionFactory.class, true, false );
		InfinispanRegionFactory regionFactory = CacheTestUtil.startRegionFactory(
				ServiceRegistryBuilder.buildServiceRegistry( cfg.getProperties() ),
				cfg,
				getCacheTestSupport()
		);
		TransactionalDataRegion region = (TransactionalDataRegion) createRegion(
				regionFactory, "test/test", cfg.getProperties(), getCacheDataDescription()
		);
		CacheDataDescription cdd = region.getCacheDataDescription();
		assertNotNull( cdd );
		CacheDataDescription expected = getCacheDataDescription();
		assertEquals( expected.isMutable(), cdd.isMutable() );
		assertEquals( expected.isVersioned(), cdd.isVersioned() );
		assertEquals( expected.getVersionComparator(), cdd.getVersionComparator() );
	}
}
