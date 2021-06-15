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
import junit.extensions.TestSetup;
import junit.framework.Test;

/**
 * A TestSetup that makes SelectedClassnameClassLoader the thread context classloader for the
 * duration of the test.
 * 
 * @author <a href="brian.stansberry@jboss.com">Brian Stansberry</a>
 * @version $Revision: 1 $
 */
public class SelectedClassnameClassLoaderTestSetup extends TestSetup {
   private ClassLoader originalTCCL;
   private String[] includedClasses;
   private String[] excludedClasses;
   private String[] notFoundClasses;

   /**
    * Create a new SelectedClassnameClassLoaderTestSetup.
    * 
    * @param test
    */
   public SelectedClassnameClassLoaderTestSetup(Test test, String[] includedClasses, String[] excludedClasses, String[] notFoundClasses) {
      super(test);
      this.includedClasses = includedClasses;
      this.excludedClasses = excludedClasses;
      this.notFoundClasses = notFoundClasses;
   }

   @Override
   protected void setUp() throws Exception {
      super.setUp();

      originalTCCL = Thread.currentThread().getContextClassLoader();
      ClassLoader parent = originalTCCL == null ? getClass().getClassLoader() : originalTCCL;
      ClassLoader selectedTCCL = new SelectedClassnameClassLoader(includedClasses, excludedClasses, notFoundClasses, parent);
      Thread.currentThread().setContextClassLoader(selectedTCCL);
   }

   @Override
   protected void tearDown() throws Exception {
      Thread.currentThread().setContextClassLoader(originalTCCL);
      super.tearDown();
   }

}
