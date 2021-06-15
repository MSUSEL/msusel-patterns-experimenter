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
package org.hibernate.test.cache.infinispan.timestamp;

import java.util.Properties;

import org.infinispan.AdvancedCache;
import org.infinispan.context.Flag;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryActivated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryEvicted;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryInvalidated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryLoaded;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryPassivated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryVisited;
import org.infinispan.notifications.cachelistener.event.Event;

import org.hibernate.cache.infinispan.InfinispanRegionFactory;
import org.hibernate.test.cache.infinispan.util.ClassLoaderAwareCache;
import org.hibernate.cache.infinispan.timestamp.TimestampsRegionImpl;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.Region;
import org.hibernate.cache.spi.UpdateTimestampsCache;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.test.cache.infinispan.AbstractGeneralDataRegionTestCase;
import org.hibernate.test.cache.infinispan.functional.classloader.Account;
import org.hibernate.test.cache.infinispan.functional.classloader.AccountHolder;
import org.hibernate.test.cache.infinispan.functional.classloader.SelectedClassnameClassLoader;
import org.hibernate.test.cache.infinispan.util.CacheTestUtil;

/**
 * Tests of TimestampsRegionImpl.
 * 
 * @author Galder Zamarreño
 * @since 3.5
 */
public class TimestampsRegionImplTestCase extends AbstractGeneralDataRegionTestCase {

    @Override
   protected String getStandardRegionName(String regionPrefix) {
      return regionPrefix + "/" + UpdateTimestampsCache.class.getName();
   }

   @Override
   protected Region createRegion(InfinispanRegionFactory regionFactory, String regionName, Properties properties, CacheDataDescription cdd) {
      return regionFactory.buildTimestampsRegion(regionName, properties);
   }

   @Override
   protected AdvancedCache getInfinispanCache(InfinispanRegionFactory regionFactory) {
      return regionFactory.getCacheManager().getCache("timestamps").getAdvancedCache();
   }

   public void testClearTimestampsRegionInIsolated() throws Exception {
      Configuration cfg = createConfiguration();
      InfinispanRegionFactory regionFactory = CacheTestUtil.startRegionFactory(
			  new ServiceRegistryBuilder().applySettings( cfg.getProperties() ).buildServiceRegistry(),
			  cfg,
			  getCacheTestSupport()
	  );
      // Sleep a bit to avoid concurrent FLUSH problem
      avoidConcurrentFlush();

      Configuration cfg2 = createConfiguration();
      InfinispanRegionFactory regionFactory2 = CacheTestUtil.startRegionFactory(
			  new ServiceRegistryBuilder().applySettings( cfg.getProperties() ).buildServiceRegistry(),
			  cfg2,
			  getCacheTestSupport()
	  );
      // Sleep a bit to avoid concurrent FLUSH problem
      avoidConcurrentFlush();

      TimestampsRegionImpl region = (TimestampsRegionImpl) regionFactory.buildTimestampsRegion(getStandardRegionName(REGION_PREFIX), cfg.getProperties());
      TimestampsRegionImpl region2 = (TimestampsRegionImpl) regionFactory2.buildTimestampsRegion(getStandardRegionName(REGION_PREFIX), cfg2.getProperties());
//      QueryResultsRegion region2 = regionFactory2.buildQueryResultsRegion(getStandardRegionName(REGION_PREFIX), cfg2.getProperties());

//      ClassLoader cl = Thread.currentThread().getContextClassLoader();
//      Thread.currentThread().setContextClassLoader(cl.getParent());
//      log.info("TCCL is " + cl.getParent());

      Account acct = new Account();
      acct.setAccountHolder(new AccountHolder());
      region.getCache().withFlags(Flag.FORCE_SYNCHRONOUS).put(acct, "boo");

//      region.put(acct, "boo");
//
//      region.evictAll();

//      Account acct = new Account();
//      acct.setAccountHolder(new AccountHolder());



   }

   @Override
   protected Configuration createConfiguration() {
      return CacheTestUtil.buildConfiguration("test", MockInfinispanRegionFactory.class, false, true);
   }

   public static class MockInfinispanRegionFactory extends InfinispanRegionFactory {

      public MockInfinispanRegionFactory() {
      }

      public MockInfinispanRegionFactory(Properties props) {
         super(props);
      }

//      @Override
//      protected TimestampsRegionImpl createTimestampsRegion(CacheAdapter cacheAdapter, String regionName) {
//         return new MockTimestampsRegionImpl(cacheAdapter, regionName, getTransactionManager(), this);
//      }

      @Override
      protected AdvancedCache createCacheWrapper(AdvancedCache cache) {
         return new ClassLoaderAwareCache(cache, Thread.currentThread().getContextClassLoader()) {
            @Override
            public void addListener(Object listener) {
               super.addListener(new MockClassLoaderAwareListener(listener, this));
            }
         };
      }

      //      @Override
//      protected EmbeddedCacheManager createCacheManager(Properties properties) throws CacheException {
//         try {
//            EmbeddedCacheManager manager = new DefaultCacheManager(InfinispanRegionFactory.DEF_INFINISPAN_CONFIG_RESOURCE);
//            org.infinispan.config.Configuration ispnCfg = new org.infinispan.config.Configuration();
//            ispnCfg.setCacheMode(org.infinispan.config.Configuration.CacheMode.REPL_SYNC);
//            manager.defineConfiguration("timestamps", ispnCfg);
//            return manager;
//         } catch (IOException e) {
//            throw new CacheException("Unable to create default cache manager", e);
//         }
//      }

      @Listener      
      public static class MockClassLoaderAwareListener extends ClassLoaderAwareCache.ClassLoaderAwareListener {
         MockClassLoaderAwareListener(Object listener, ClassLoaderAwareCache cache) {
            super(listener, cache);
         }

         @CacheEntryActivated
         @CacheEntryCreated
         @CacheEntryEvicted
         @CacheEntryInvalidated
         @CacheEntryLoaded
         @CacheEntryModified
         @CacheEntryPassivated
         @CacheEntryRemoved
         @CacheEntryVisited
         public void event(Event event) throws Throwable {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            String notFoundPackage = "org.hibernate.test.cache.infinispan.functional.classloader";
            String[] notFoundClasses = { notFoundPackage + ".Account", notFoundPackage + ".AccountHolder" };
            SelectedClassnameClassLoader visible = new SelectedClassnameClassLoader(null, null, notFoundClasses, cl);
            Thread.currentThread().setContextClassLoader(visible);
            super.event(event);
            Thread.currentThread().setContextClassLoader(cl);
         }
      }
   }

//   @Listener
//   public static class MockTimestampsRegionImpl extends TimestampsRegionImpl {
//
//      public MockTimestampsRegionImpl(CacheAdapter cacheAdapter, String name, TransactionManager transactionManager, RegionFactory factory) {
//         super(cacheAdapter, name, transactionManager, factory);
//      }
//
//      @CacheEntryModified
//      public void nodeModified(CacheEntryModifiedEvent event) {
////         ClassLoader cl = Thread.currentThread().getContextClassLoader();
////         String notFoundPackage = "org.hibernate.test.cache.infinispan.functional.classloader";
////         String[] notFoundClasses = { notFoundPackage + ".Account", notFoundPackage + ".AccountHolder" };
////         SelectedClassnameClassLoader visible = new SelectedClassnameClassLoader(null, null, notFoundClasses, cl);
////         Thread.currentThread().setContextClassLoader(visible);
//         super.nodeModified(event);
////         Thread.currentThread().setContextClassLoader(cl);
//      }
//   }

}
