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
package org.hibernate.cache.infinispan;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.infinispan.config.Configuration;
import org.infinispan.eviction.EvictionStrategy;

import org.hibernate.cache.CacheException;

/**
 * This class represents Infinispan cache parameters that can be configured via hibernate configuration properties 
 * for either general entity/collection/query/timestamp data type caches and overrides for individual entity or 
 * collection caches. Configuration these properties override previously defined properties in XML file.
 * 
 * @author Galder Zamarreño
 * @since 3.5
 */
public class TypeOverrides {

   private final Set<String> overridden = new HashSet<String>();

   private String cacheName;

   private EvictionStrategy evictionStrategy;

   private long evictionWakeUpInterval;

   private int evictionMaxEntries;

   private long expirationLifespan;

   private long expirationMaxIdle;

   private boolean isExposeStatistics;

   public String getCacheName() {
      return cacheName;
   }

   public void setCacheName(String cacheName) {
      this.cacheName = cacheName;
   }

   public EvictionStrategy getEvictionStrategy() {
      return evictionStrategy;
   }

   public void setEvictionStrategy(String evictionStrategy) {
      markAsOverriden("evictionStrategy");
      this.evictionStrategy = EvictionStrategy.valueOf(uc(evictionStrategy));
   }

   public long getEvictionWakeUpInterval() {
      return evictionWakeUpInterval;
   }

   public void setEvictionWakeUpInterval(long evictionWakeUpInterval) {
      markAsOverriden("evictionWakeUpInterval");
      this.evictionWakeUpInterval = evictionWakeUpInterval;
   }

   public int getEvictionMaxEntries() {
      return evictionMaxEntries;
   }

   public void setEvictionMaxEntries(int evictionMaxEntries) {
      markAsOverriden("evictionMaxEntries");
      this.evictionMaxEntries = evictionMaxEntries;
   }

   public long getExpirationLifespan() {
      return expirationLifespan;
   }

   public void setExpirationLifespan(long expirationLifespan) {
      markAsOverriden("expirationLifespan");
      this.expirationLifespan = expirationLifespan;
   }

   public long getExpirationMaxIdle() {
      return expirationMaxIdle;
   }

   public void setExpirationMaxIdle(long expirationMaxIdle) {
      markAsOverriden("expirationMaxIdle");
      this.expirationMaxIdle = expirationMaxIdle;
   }

   public boolean isExposeStatistics() {
      return isExposeStatistics;
   }

   public void setExposeStatistics(boolean isExposeStatistics) {
      markAsOverriden("isExposeStatistics");
      this.isExposeStatistics = isExposeStatistics;
   }

   public Configuration createInfinispanConfiguration() {
      Configuration cacheCfg = new Configuration();
      if (overridden.contains("evictionStrategy"))
         cacheCfg.fluent().eviction().strategy(evictionStrategy);
      if (overridden.contains("evictionWakeUpInterval"))
         cacheCfg.fluent().expiration().wakeUpInterval(evictionWakeUpInterval);
      if (overridden.contains("evictionMaxEntries"))
         cacheCfg.fluent().eviction().maxEntries(evictionMaxEntries);
      if (overridden.contains("expirationLifespan"))
         cacheCfg.fluent().expiration().lifespan(expirationLifespan);
      if (overridden.contains("expirationMaxIdle"))
         cacheCfg.fluent().expiration().maxIdle(expirationMaxIdle);
      if (overridden.contains("isExposeStatistics") && isExposeStatistics)
         cacheCfg.fluent().jmxStatistics();
      return cacheCfg;
   }

   public void validateInfinispanConfiguration(Configuration configuration) throws CacheException {
      // no-op
   }

   @Override
   public String toString() {
      return new StringBuilder().append(getClass().getSimpleName()).append('{')
         .append("cache=").append(cacheName)
         .append(", strategy=").append(evictionStrategy)
         .append(", wakeUpInterval=").append(evictionWakeUpInterval)
         .append(", maxEntries=").append(evictionMaxEntries)
         .append(", lifespan=").append(expirationLifespan)
         .append(", maxIdle=").append(expirationMaxIdle)
         .append('}').toString();
   }

   private String uc(String s) {
      return s == null ? null : s.toUpperCase(Locale.ENGLISH);
   }

   private void markAsOverriden(String fieldName) {
      overridden.add(fieldName);
   }
}
