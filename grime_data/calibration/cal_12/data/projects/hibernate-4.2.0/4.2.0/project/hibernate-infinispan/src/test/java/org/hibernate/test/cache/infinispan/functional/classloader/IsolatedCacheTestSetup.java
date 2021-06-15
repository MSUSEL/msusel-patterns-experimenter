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
package org.hibernate.test.cache.infinispan.functional.classloader;

import junit.framework.Test;

import org.hibernate.test.cache.infinispan.functional.cluster.ClusterAwareRegionFactory;
import org.hibernate.test.cache.infinispan.functional.cluster.DualNodeJtaTransactionManagerImpl;

/**
 * A TestSetup that uses SelectedClassnameClassLoader to ensure that certain classes are not visible
 * to Infinispan or JGroups' classloader.
 * 
 * @author Galder Zamarreño
 */
public class IsolatedCacheTestSetup extends SelectedClassnameClassLoaderTestSetup {

   private String[] isolatedClasses;
   private String cacheConfig;

   /**
    * Create a new IsolatedCacheTestSetup.
    */
   public IsolatedCacheTestSetup(Test test, String[] isolatedClasses) {
      super(test, null, null, isolatedClasses);
      this.isolatedClasses = isolatedClasses;
   }

   @Override
   protected void setUp() throws Exception {
      super.setUp();

      // At this point the TCCL cannot see the isolatedClasses
      // We want the caches to use this CL as their default classloader
      ClassLoader tccl = Thread.currentThread().getContextClassLoader();

      // Now make the isolatedClasses visible to the test driver itself
      SelectedClassnameClassLoader visible = new SelectedClassnameClassLoader(isolatedClasses, null, null, tccl);
      Thread.currentThread().setContextClassLoader(visible);
   }

   @Override
   protected void tearDown() throws Exception {
      try {
         super.tearDown();
      } finally {
         ClusterAwareRegionFactory.clearCacheManagers();
         DualNodeJtaTransactionManagerImpl.cleanupTransactions();
         DualNodeJtaTransactionManagerImpl.cleanupTransactionManagers();
      }
   }

}
