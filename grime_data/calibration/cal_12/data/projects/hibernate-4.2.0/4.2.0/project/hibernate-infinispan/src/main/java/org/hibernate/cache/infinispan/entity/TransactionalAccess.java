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
package org.hibernate.cache.infinispan.entity;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.infinispan.access.TransactionalAccessDelegate;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;

/**
 * Transactional entity region access for Infinispan.
 * 
 * @author Chris Bredesen
 * @author Galder Zamarreño
 * @since 3.5
 */
class TransactionalAccess implements EntityRegionAccessStrategy {
 
   private final EntityRegionImpl region;
   
   private final TransactionalAccessDelegate delegate;

   TransactionalAccess(EntityRegionImpl region) {
      this.region = region;
      this.delegate = new TransactionalAccessDelegate(region, region.getPutFromLoadValidator());
   }

   public void evict(Object key) throws CacheException {
      delegate.evict(key);
   }

   public void evictAll() throws CacheException {
      delegate.evictAll();
   }

   public Object get(Object key, long txTimestamp) throws CacheException {
      return delegate.get(key, txTimestamp);
   }

   public EntityRegion getRegion() {
      return this.region;
   }

   public boolean insert(Object key, Object value, Object version) throws CacheException {
      return delegate.insert(key, value, version);
   }

   public boolean putFromLoad(Object key, Object value, long txTimestamp, Object version) throws CacheException {
      return delegate.putFromLoad(key, value, txTimestamp, version);
   }

   public boolean putFromLoad(Object key, Object value, long txTimestamp, Object version, boolean minimalPutOverride) throws CacheException {
      return delegate.putFromLoad(key, value, txTimestamp, version, minimalPutOverride);
   }

   public void remove(Object key) throws CacheException {
      delegate.remove(key);
   }

   public void removeAll() throws CacheException {
      delegate.removeAll();
   }

   public boolean update(Object key, Object value, Object currentVersion, Object previousVersion) throws CacheException {
      return delegate.update(key, value, currentVersion, previousVersion);
   }

   public SoftLock lockItem(Object key, Object version) throws CacheException {
      return null;
   }

   public SoftLock lockRegion() throws CacheException {
      return null;
   }

   public void unlockItem(Object key, SoftLock lock) throws CacheException {
   }

   public void unlockRegion(SoftLock lock) throws CacheException {
   }

   public boolean afterInsert(Object key, Object value, Object version) throws CacheException {
      return false;
   }

   public boolean afterUpdate(Object key, Object value, Object currentVersion, Object previousVersion, SoftLock lock) throws CacheException {
      return false;
   }
}