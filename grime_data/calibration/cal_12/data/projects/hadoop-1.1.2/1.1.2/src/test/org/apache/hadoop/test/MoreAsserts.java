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
package org.apache.hadoop.test;

import java.util.Iterator;
import org.junit.Assert;

/**
 * A few more asserts
 */
public class MoreAsserts {

  /**
   * Assert equivalence for array and iterable
   * @param <T> the type of the elements
   * @param s the name/message for the collection
   * @param expected  the expected array of elements
   * @param actual    the actual iterable of elements
   */
  public static <T> void assertEquals(String s, T[] expected,
                                      Iterable<T> actual) {
    Iterator<T> it = actual.iterator();
    int i = 0;
    for (; i < expected.length && it.hasNext(); ++i) {
      Assert.assertEquals("Element "+ i +" for "+ s, expected[i], it.next());
    }
    Assert.assertTrue("Expected more elements", i == expected.length);
    Assert.assertTrue("Expected less elements", !it.hasNext());
  }

  /**
   * Assert equality for two iterables
   * @param <T> the type of the elements
   * @param s
   * @param expected
   * @param actual
   */
  public static <T> void assertEquals(String s, Iterable<T> expected,
                                      Iterable<T> actual) {
    Iterator<T> ite = expected.iterator();
    Iterator<T> ita = actual.iterator();
    int i = 0;
    while (ite.hasNext() && ita.hasNext()) {
      Assert.assertEquals("Element "+ i +" for "+s, ite.next(), ita.next());
    }
    Assert.assertTrue("Expected more elements", !ite.hasNext());
    Assert.assertTrue("Expected less elements", !ita.hasNext());
  }

}
