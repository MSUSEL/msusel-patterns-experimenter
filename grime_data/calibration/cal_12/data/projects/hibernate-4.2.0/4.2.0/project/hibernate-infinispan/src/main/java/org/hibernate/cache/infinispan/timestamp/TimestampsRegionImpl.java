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

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.transaction.Transaction;

import org.hibernate.cache.infinispan.util.Caches;
import org.infinispan.AdvancedCache;
import org.infinispan.context.Flag;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.event.CacheEntryModifiedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.infinispan.impl.BaseGeneralDataRegion;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.cache.spi.TimestampsRegion;

/**
 * Defines the behavior of the timestamps cache region for Infinispan.
 * 
 * @author Chris Bredesen
 * @author Galder Zamarreño
 * @since 3.5
 */
public class TimestampsRegionImpl extends BaseGeneralDataRegion implements TimestampsRegion {

   private final AdvancedCache removeCache;
   private final AdvancedCache timestampsPutCache;

   public TimestampsRegionImpl(AdvancedCache cache, String name,
         RegionFactory factory) {
      super(cache, name, factory);
      this.removeCache = Caches.ignoreReturnValuesCache(cache);

      // Skip locking when updating timestamps to provide better performance
      // under highly concurrent insert scenarios, where update timestamps
      // for an entity/collection type are constantly updated, creating
      // contention.
      //
      // The worst it can happen is that an earlier an earlier timestamp
      // (i.e. ts=1) will override a later on (i.e. ts=2), so it means that
      // in highly concurrent environments, queries might be considered stale
      // earlier in time. The upside is that inserts/updates are way faster
      // in local set ups.
      this.timestampsPutCache = getTimestampsPutCache(cache);
   }

   protected AdvancedCache getTimestampsPutCache(AdvancedCache cache) {
      return Caches.ignoreReturnValuesCache(cache, Flag.SKIP_LOCKING);
   }

   @Override
   public void evict(Object key) throws CacheException {
      // TODO Is this a valid operation on a timestamps cache?
      removeCache.remove(key);
   }

   public void evictAll() throws CacheException {
      // TODO Is this a valid operation on a timestamps cache?
      Transaction tx = suspend();
      try {
         invalidateRegion(); // Invalidate the local region
      } finally {
         resume(tx);
      }
   }

   public Object get(Object key) throws CacheException {
      if (checkValid())
         return cache.get(key);

      return null;
   }

   public void put(final Object key, final Object value) throws CacheException {
      try {
         // We ensure ASYNC semantics (JBCACHE-1175) and make sure previous
         // value is not loaded from cache store cos it's not needed.
         timestampsPutCache.put(key, value);
      } catch (Exception e) {
         throw new CacheException(e);
      }
   }

}