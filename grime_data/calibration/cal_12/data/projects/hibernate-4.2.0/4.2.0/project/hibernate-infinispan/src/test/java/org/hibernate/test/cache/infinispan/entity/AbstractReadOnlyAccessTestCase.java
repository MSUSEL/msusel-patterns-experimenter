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
package org.hibernate.test.cache.infinispan.entity;

import org.infinispan.transaction.tm.BatchModeTransactionManager;
import org.junit.Test;

import org.hibernate.cache.spi.access.AccessType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Base class for tests of TRANSACTIONAL access.
 *
 * @author Galder Zamarreño
 * @since 3.5
 */
public abstract class AbstractReadOnlyAccessTestCase extends AbstractEntityRegionAccessStrategyTestCase {

   @Override
   protected AccessType getAccessType() {
      return AccessType.READ_ONLY;
   }

   @Test
   @Override
   public void testPutFromLoad() throws Exception {
      putFromLoadTest(false);
   }

   @Test
   @Override
   public void testPutFromLoadMinimal() throws Exception {
      putFromLoadTest(true);
   }

   private void putFromLoadTest(boolean minimal) throws Exception {

      final String KEY = KEY_BASE + testCount++;

      long txTimestamp = System.currentTimeMillis();
      BatchModeTransactionManager.getInstance().begin();
      assertNull(localAccessStrategy.get(KEY, System.currentTimeMillis()));
      if (minimal)
         localAccessStrategy.putFromLoad(KEY, VALUE1, txTimestamp, 1, true);
      else
         localAccessStrategy.putFromLoad(KEY, VALUE1, txTimestamp, 1);

      sleep(250);
      Object expected = isUsingInvalidation() ? null : VALUE1;
      assertEquals(expected, remoteAccessStrategy.get(KEY, System.currentTimeMillis()));

      BatchModeTransactionManager.getInstance().commit();
      assertEquals(VALUE1, localAccessStrategy.get(KEY, System.currentTimeMillis()));
      assertEquals(expected, remoteAccessStrategy.get(KEY, System.currentTimeMillis()));
   }

   @Test(expected = UnsupportedOperationException.class)
   @Override
   public void testUpdate() throws Exception {
      localAccessStrategy.update(KEY_BASE + testCount++,
            VALUE2, 2, 1);
   }

}
