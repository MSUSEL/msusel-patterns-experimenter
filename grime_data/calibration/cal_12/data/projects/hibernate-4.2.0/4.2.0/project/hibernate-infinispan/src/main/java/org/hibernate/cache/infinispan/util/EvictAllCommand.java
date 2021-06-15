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

import org.infinispan.commands.remote.BaseRpcCommand;
import org.infinispan.context.InvocationContext;

import org.hibernate.cache.infinispan.impl.BaseRegion;

/**
 * Evict all command
 *
 * @author Galder Zamarreño
 * @since 4.0
 */
public class EvictAllCommand extends BaseRpcCommand {

   private final BaseRegion region;

   public EvictAllCommand(String regionName, BaseRegion region) {
      super(regionName); // region name and cache names are the same...
      this.region = region;
   }

   public EvictAllCommand(String regionName) {
      this(regionName, null);
   }

   @Override
   public Object perform(InvocationContext ctx) throws Throwable {
      region.invalidateRegion();
      return null;
   }

   @Override
   public byte getCommandId() {
      return CacheCommandIds.EVICT_ALL;
   }

   @Override
   public Object[] getParameters() {
      return new Object[0];
   }

   @Override
   public void setParameters(int commandId, Object[] parameters) {
      // No-op
   }

   @Override
   public boolean isReturnValueExpected() {
      return false;
   }

}
