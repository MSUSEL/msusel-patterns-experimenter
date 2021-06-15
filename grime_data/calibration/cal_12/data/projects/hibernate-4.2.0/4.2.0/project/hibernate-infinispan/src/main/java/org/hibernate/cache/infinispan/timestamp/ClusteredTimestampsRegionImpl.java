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
package org.hibernate.cache.infinispan.timestamp;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.infinispan.util.Caches;
import org.hibernate.cache.spi.RegionFactory;
import org.infinispan.AdvancedCache;
import org.infinispan.context.Flag;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.event.CacheEntryModifiedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;

import javax.transaction.Transaction;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Timestamp cache region for clustered environments.
 *
 * @author Galder Zamarreño
 * @since 4.1
 */
@Listener
public class ClusteredTimestampsRegionImpl extends TimestampsRegionImpl {

   /**
    * Maintains a local (authoritative) cache of timestamps along with the
    * replicated cache held in Infinispan. It listens for changes in the
    * cache and updates the local cache accordingly. This approach allows
    * timestamp changes to be replicated asynchronously.
    */
   private final Map localCache = new ConcurrentHashMap();

   public ClusteredTimestampsRegionImpl(AdvancedCache cache,
         String name, RegionFactory factory) {
      super(cache, name, factory);
      cache.addListener(this);
      populateLocalCache();
   }

   @Override
   protected AdvancedCache getTimestampsPutCache(AdvancedCache cache) {
      return Caches.asyncWriteCache(cache, Flag.SKIP_LOCKING);
   }

   @Override
   public Object get(Object key) throws CacheException {
      Object value = localCache.get(key);

      // If the region is not valid, skip cache store to avoid going remote to retrieve the query.
      // The aim of this is to maintain same logic/semantics as when state transfer was configured.
      // TODO: Once https://issues.jboss.org/browse/ISPN-835 has been resolved, revert to state transfer and remove workaround
      boolean skipCacheStore = false;
      if (!isValid())
         skipCacheStore = true;

      if (value == null && checkValid()) {
         if (skipCacheStore)
            value = cache.withFlags(Flag.SKIP_CACHE_STORE).get(key);
         else
            value = cache.get(key);

         if (value != null)
            localCache.put(key, value);
      }
      return value;
   }

   @Override
   public void evictAll() throws CacheException {
      // TODO Is this a valid operation on a timestamps cache?
      Transaction tx = suspend();
      try {
         invalidateRegion(); // Invalidate the local region and then go remote
         Caches.broadcastEvictAll(cache);
      } finally {
         resume(tx);
      }
   }

   @Override
   public void invalidateRegion() {
      super.invalidateRegion(); // Invalidate first
      localCache.clear();
   }

   @Override
   public void destroy() throws CacheException {
      localCache.clear();
      cache.removeListener(this);
      super.destroy();
   }

   /**
    * Brings all data from the distributed cache into our local cache.
    */
   private void populateLocalCache() {
      Set children = cache.keySet();
      for (Object key : children)
         get(key);
   }

   /**
    * Monitors cache events and updates the local cache
    *
    * @param event
    */
   @CacheEntryModified
   @SuppressWarnings("unused")
   public void nodeModified(CacheEntryModifiedEvent event) {
      if (!event.isPre())
         localCache.put(event.getKey(), event.getValue());
   }

   /**
    * Monitors cache events and updates the local cache
    *
    * @param event
    */
   @CacheEntryRemoved
   @SuppressWarnings("unused")
   public void nodeRemoved(CacheEntryRemovedEvent event) {
      if (event.isPre()) return;
      localCache.remove(event.getKey());
   }

}
