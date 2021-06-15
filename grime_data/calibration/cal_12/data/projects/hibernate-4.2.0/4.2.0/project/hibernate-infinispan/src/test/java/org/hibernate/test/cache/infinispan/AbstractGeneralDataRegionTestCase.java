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

import java.util.Set;

import org.infinispan.AdvancedCache;
import org.infinispan.transaction.tm.BatchModeTransactionManager;
import org.jboss.logging.Logger;
import org.junit.Test;

import org.hibernate.cache.infinispan.InfinispanRegionFactory;
import org.hibernate.cache.spi.GeneralDataRegion;
import org.hibernate.cache.spi.QueryResultsRegion;
import org.hibernate.cache.spi.Region;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.test.cache.infinispan.util.CacheTestUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Base class for tests of QueryResultsRegion and TimestampsRegion.
 *
 * @author Galder Zamarreño
 * @since 3.5
 */
public abstract class AbstractGeneralDataRegionTestCase extends AbstractRegionImplTestCase {
	private static final Logger log = Logger.getLogger( AbstractGeneralDataRegionTestCase.class );

	protected static final String KEY = "Key";

	protected static final String VALUE1 = "value1";
	protected static final String VALUE2 = "value2";

	protected Configuration createConfiguration() {
		return CacheTestUtil.buildConfiguration( "test", InfinispanRegionFactory.class, false, true );
	}

	@Override
	protected void putInRegion(Region region, Object key, Object value) {
		((GeneralDataRegion) region).put( key, value );
	}

	@Override
	protected void removeFromRegion(Region region, Object key) {
		((GeneralDataRegion) region).evict( key );
	}

	@Test
	public void testEvict() throws Exception {
		evictOrRemoveTest();
	}

	private void evictOrRemoveTest() throws Exception {
		Configuration cfg = createConfiguration();
		InfinispanRegionFactory regionFactory = CacheTestUtil.startRegionFactory(
				new ServiceRegistryBuilder().applySettings( cfg.getProperties() ).buildServiceRegistry(),
				cfg,
				getCacheTestSupport()
		);
		boolean invalidation = false;

		// Sleep a bit to avoid concurrent FLUSH problem
		avoidConcurrentFlush();

		GeneralDataRegion localRegion = (GeneralDataRegion) createRegion(
				regionFactory,
				getStandardRegionName( REGION_PREFIX ), cfg.getProperties(), null
		);

		cfg = createConfiguration();
		regionFactory = CacheTestUtil.startRegionFactory(
				new ServiceRegistryBuilder().applySettings( cfg.getProperties() ).buildServiceRegistry(),
				cfg,
				getCacheTestSupport()
		);

		GeneralDataRegion remoteRegion = (GeneralDataRegion) createRegion(
				regionFactory,
				getStandardRegionName( REGION_PREFIX ),
				cfg.getProperties(),
				null
		);

		assertNull( "local is clean", localRegion.get( KEY ) );
		assertNull( "remote is clean", remoteRegion.get( KEY ) );

      regionPut(localRegion);
      assertEquals( VALUE1, localRegion.get( KEY ) );

		// allow async propagation
		sleep( 250 );
		Object expected = invalidation ? null : VALUE1;
		assertEquals( expected, remoteRegion.get( KEY ) );

      regionEvict(localRegion);

      // allow async propagation
		sleep( 250 );
		assertEquals( null, localRegion.get( KEY ) );
		assertEquals( null, remoteRegion.get( KEY ) );
	}

   protected void regionEvict(GeneralDataRegion region) throws Exception {
      region.evict(KEY);
   }

   protected void regionPut(GeneralDataRegion region) throws Exception {
      region.put(KEY, VALUE1);
   }

   protected abstract String getStandardRegionName(String regionPrefix);

	/**
	 * Test method for {@link QueryResultsRegion#evictAll()}.
	 * <p/>
	 * FIXME add testing of the "immediately without regard for transaction isolation" bit in the
	 * CollectionRegionAccessStrategy API.
	 */
	public void testEvictAll() throws Exception {
		evictOrRemoveAllTest( "entity" );
	}

	private void evictOrRemoveAllTest(String configName) throws Exception {
		Configuration cfg = createConfiguration();
		InfinispanRegionFactory regionFactory = CacheTestUtil.startRegionFactory(
				new ServiceRegistryBuilder().applySettings( cfg.getProperties() ).buildServiceRegistry(),
				cfg,
				getCacheTestSupport()
		);
		AdvancedCache localCache = getInfinispanCache( regionFactory );

		// Sleep a bit to avoid concurrent FLUSH problem
		avoidConcurrentFlush();

		GeneralDataRegion localRegion = (GeneralDataRegion) createRegion(
				regionFactory,
				getStandardRegionName( REGION_PREFIX ),
				cfg.getProperties(),
				null
		);

		cfg = createConfiguration();
		regionFactory = CacheTestUtil.startRegionFactory(
				new ServiceRegistryBuilder().applySettings( cfg.getProperties() ).buildServiceRegistry(),
				cfg,
				getCacheTestSupport()
		);
      AdvancedCache remoteCache = getInfinispanCache( regionFactory );

		// Sleep a bit to avoid concurrent FLUSH problem
		avoidConcurrentFlush();

		GeneralDataRegion remoteRegion = (GeneralDataRegion) createRegion(
				regionFactory,
				getStandardRegionName( REGION_PREFIX ),
				cfg.getProperties(),
				null
		);

		Set keys = localCache.keySet();
		assertEquals( "No valid children in " + keys, 0, getValidKeyCount( keys ) );

		keys = remoteCache.keySet();
		assertEquals( "No valid children in " + keys, 0, getValidKeyCount( keys ) );

		assertNull( "local is clean", localRegion.get( KEY ) );
		assertNull( "remote is clean", remoteRegion.get( KEY ) );

      regionPut(localRegion);
      assertEquals( VALUE1, localRegion.get( KEY ) );

		// Allow async propagation
		sleep( 250 );

      regionPut(remoteRegion);
      assertEquals( VALUE1, remoteRegion.get( KEY ) );

		// Allow async propagation
		sleep( 250 );

		localRegion.evictAll();

		// allow async propagation
		sleep( 250 );
		// This should re-establish the region root node in the optimistic case
		assertNull( localRegion.get( KEY ) );
		assertEquals( "No valid children in " + keys, 0, getValidKeyCount( localCache.keySet() ) );

		// Re-establishing the region root on the local node doesn't
		// propagate it to other nodes. Do a get on the remote node to re-establish
		// This only adds a node in the case of optimistic locking
		assertEquals( null, remoteRegion.get( KEY ) );
		assertEquals( "No valid children in " + keys, 0, getValidKeyCount( remoteCache.keySet() ) );

		assertEquals( "local is clean", null, localRegion.get( KEY ) );
		assertEquals( "remote is clean", null, remoteRegion.get( KEY ) );
	}

	protected void rollback() {
		try {
			BatchModeTransactionManager.getInstance().rollback();
		}
		catch (Exception e) {
			log.error( e.getMessage(), e );
		}
	}
}