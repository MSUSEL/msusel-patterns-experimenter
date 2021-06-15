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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.infinispan.commands.ReplicableCommand;
import org.infinispan.commands.module.ExtendedModuleCommandFactory;
import org.infinispan.commands.remote.CacheRpcCommand;

import org.hibernate.cache.infinispan.impl.BaseRegion;

/**
 * Command factory
 *
 * @author Galder Zamarreño
 * @since 4.0
 */
public class CacheCommandFactory implements ExtendedModuleCommandFactory {

   private ConcurrentMap<String, BaseRegion> allRegions =
         new ConcurrentHashMap<String, BaseRegion>();

   public void addRegion(String regionName, BaseRegion region) {
      allRegions.put(regionName, region);
   }

   public void clearRegions(List<String> regionNames) {
      for (String regionName : regionNames)
         allRegions.remove(regionName);
   }

   @Override
   public Map<Byte, Class<? extends ReplicableCommand>> getModuleCommands() {
      Map<Byte, Class<? extends ReplicableCommand>> map = new HashMap<Byte, Class<? extends ReplicableCommand>>(3);
      map.put(CacheCommandIds.EVICT_ALL, EvictAllCommand.class);
      return map;
   }

   @Override
   public CacheRpcCommand fromStream(byte commandId, Object[] args, String cacheName) {
      CacheRpcCommand c;
      switch (commandId) {
         case CacheCommandIds.EVICT_ALL:
            c = new EvictAllCommand(cacheName, allRegions.get(cacheName));
            break;
         default:
            throw new IllegalArgumentException("Not registered to handle command id " + commandId);
      }
      c.setParameters(commandId, args);
      return c;
   }

   @Override
   public ReplicableCommand fromStream(byte commandId, Object[] args) {
      // Should not be called while this factory only
      // provides cache specific replicable commands.
      return null;
   }

}
