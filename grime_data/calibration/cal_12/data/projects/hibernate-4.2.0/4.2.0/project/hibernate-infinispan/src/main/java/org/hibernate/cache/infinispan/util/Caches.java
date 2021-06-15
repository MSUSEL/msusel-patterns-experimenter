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
package org.hibernate.cache.infinispan.util;

import org.infinispan.AdvancedCache;
import org.infinispan.context.Flag;
import org.infinispan.remoting.rpc.RpcManager;

import javax.transaction.Status;
import javax.transaction.TransactionManager;
import java.util.concurrent.Callable;

/**
 * Helper for dealing with Infinispan cache instances.
 *
 * @author Galder Zamarreño
 * @since 4.1
 */
public class Caches {

   private Caches() {
      // Suppresses default constructor, ensuring non-instantiability.
   }

   public static <T> T withinTx(AdvancedCache cache,
         Callable<T> c) throws Exception {
      // Retrieve transaction manager
      return withinTx(cache.getTransactionManager(), c);
   }

   public static <T> T withinTx(TransactionManager tm,
         Callable<T> c) throws Exception {
      tm.begin();
      try {
         return c.call();
      } catch (Exception e) {
         tm.setRollbackOnly();
         throw e;
      } finally {
         if (tm.getStatus() == Status.STATUS_ACTIVE) tm.commit();
         else tm.rollback();
      }
   }

   public static AdvancedCache localCache(AdvancedCache cache) {
      return cache.withFlags(Flag.CACHE_MODE_LOCAL);
   }

   public static AdvancedCache ignoreReturnValuesCache(AdvancedCache cache) {
      return cache.withFlags(Flag.SKIP_CACHE_LOAD, Flag.SKIP_REMOTE_LOOKUP);
   }

   public static AdvancedCache ignoreReturnValuesCache(
         AdvancedCache cache, Flag extraFlag) {
      return cache.withFlags(
            Flag.SKIP_CACHE_LOAD, Flag.SKIP_REMOTE_LOOKUP, extraFlag);
   }

   public static AdvancedCache asyncWriteCache(AdvancedCache cache,
         Flag extraFlag) {
      return cache.withFlags(
            Flag.SKIP_CACHE_LOAD,
            Flag.SKIP_REMOTE_LOOKUP,
            Flag.FORCE_ASYNCHRONOUS,
            extraFlag);
   }

   public static AdvancedCache failSilentWriteCache(AdvancedCache cache) {
      return cache.withFlags(
            Flag.FAIL_SILENTLY,
            Flag.ZERO_LOCK_ACQUISITION_TIMEOUT,
            Flag.SKIP_CACHE_LOAD,
            Flag.SKIP_REMOTE_LOOKUP);
   }

   public static AdvancedCache failSilentWriteCache(AdvancedCache cache,
         Flag extraFlag) {
      return cache.withFlags(
            Flag.FAIL_SILENTLY,
            Flag.ZERO_LOCK_ACQUISITION_TIMEOUT,
            Flag.SKIP_CACHE_LOAD,
            Flag.SKIP_REMOTE_LOOKUP,
            extraFlag);
   }

   public static AdvancedCache failSilentReadCache(AdvancedCache cache) {
      return cache.withFlags(
            Flag.FAIL_SILENTLY,
            Flag.ZERO_LOCK_ACQUISITION_TIMEOUT);
   }

   public static void broadcastEvictAll(AdvancedCache cache) {
      RpcManager rpcManager = cache.getRpcManager();
      if (rpcManager != null) {
         // Only broadcast evict all if it's clustered
         CacheCommandInitializer factory = cache.getComponentRegistry()
               .getComponent(CacheCommandInitializer.class);
         boolean isSync = isSynchronousCache(cache);

         EvictAllCommand cmd = factory.buildEvictAllCommand(cache.getName());
         rpcManager.broadcastRpcCommand(cmd, isSync);
      }
   }

   public static boolean isInvalidationCache(AdvancedCache cache) {
      return cache.getCacheConfiguration()
            .clustering().cacheMode().isInvalidation();
   }

   public static boolean isSynchronousCache(AdvancedCache cache) {
      return cache.getCacheConfiguration()
            .clustering().cacheMode().isSynchronous();
   }

   public static boolean isClustered(AdvancedCache cache) {
      return cache.getCacheConfiguration()
            .clustering().cacheMode().isClustered();
   }

}
