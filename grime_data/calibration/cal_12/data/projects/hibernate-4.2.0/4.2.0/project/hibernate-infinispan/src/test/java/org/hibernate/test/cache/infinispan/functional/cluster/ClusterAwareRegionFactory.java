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
package org.hibernate.test.cache.infinispan.functional.cluster;

import java.util.Hashtable;
import java.util.Properties;

import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.util.logging.Log;
import org.infinispan.util.logging.LogFactory;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.infinispan.InfinispanRegionFactory;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.QueryResultsRegion;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.cache.spi.TimestampsRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cfg.Settings;

/**
 * ClusterAwareRegionFactory.
 * 
 * @author Galder Zamarreño
 * @since 3.5
 */
public class ClusterAwareRegionFactory implements RegionFactory {
   
   private static final Log log = LogFactory.getLog(ClusterAwareRegionFactory.class);
   private static final Hashtable<String, EmbeddedCacheManager> cacheManagers = new Hashtable<String, EmbeddedCacheManager>();

   private final InfinispanRegionFactory delegate = new InfinispanRegionFactory();
   private String cacheManagerName;
   private boolean locallyAdded;
   
   public ClusterAwareRegionFactory(Properties props) {
   }
   
   public static EmbeddedCacheManager getCacheManager(String name) {
      return cacheManagers.get(name);
   }
   
   public static void addCacheManager(String name, EmbeddedCacheManager manager) {
      cacheManagers.put(name, manager);
   }
   
   public static void clearCacheManagers() {
      for (EmbeddedCacheManager manager : cacheManagers.values()) {
         try {
            manager.stop();
         } catch (Exception e) {
            log.error("Exception cleaning up CacheManager " + manager, e);
         }
      }
      cacheManagers.clear();      
   }

   public void start(Settings settings, Properties properties) throws CacheException {
      cacheManagerName = properties.getProperty(DualNodeTestCase.NODE_ID_PROP);
      
      EmbeddedCacheManager existing = getCacheManager(cacheManagerName);
      locallyAdded = (existing == null);
      
      if (locallyAdded) {
         delegate.start(settings, properties);
         cacheManagers.put(cacheManagerName, delegate.getCacheManager());
      } else {
         delegate.setCacheManager(existing);
      }      
   }

   public void stop() {
      if (locallyAdded) cacheManagers.remove(cacheManagerName);     
      delegate.stop();
   }

   public CollectionRegion buildCollectionRegion(String regionName, Properties properties,
            CacheDataDescription metadata) throws CacheException {
      return delegate.buildCollectionRegion(regionName, properties, metadata);
   }

   public EntityRegion buildEntityRegion(String regionName, Properties properties,
            CacheDataDescription metadata) throws CacheException {
      return delegate.buildEntityRegion(regionName, properties, metadata);
   }
   
   @Override
	public NaturalIdRegion buildNaturalIdRegion(String regionName, Properties properties, CacheDataDescription metadata)
			throws CacheException {
		return delegate.buildNaturalIdRegion( regionName, properties, metadata );
	}

   public QueryResultsRegion buildQueryResultsRegion(String regionName, Properties properties)
            throws CacheException {
      return delegate.buildQueryResultsRegion(regionName, properties);
   }

   public TimestampsRegion buildTimestampsRegion(String regionName, Properties properties)
            throws CacheException {
      return delegate.buildTimestampsRegion(regionName, properties);
   }

   public boolean isMinimalPutsEnabledByDefault() {
      return delegate.isMinimalPutsEnabledByDefault();
   }

	@Override
	public AccessType getDefaultAccessType() {
		return AccessType.TRANSACTIONAL;
	}

	public long nextTimestamp() {
      return delegate.nextTimestamp();
   }
}
