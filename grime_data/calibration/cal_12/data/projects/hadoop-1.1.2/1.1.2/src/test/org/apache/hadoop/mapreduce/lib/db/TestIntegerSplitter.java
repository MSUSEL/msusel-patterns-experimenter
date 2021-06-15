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
package org.apache.hadoop.mapreduce.lib.db;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class TestIntegerSplitter extends TestCase {
  private long [] toLongArray(List<Long> in) {
    long [] out = new long[in.size()];
    for (int i = 0; i < in.size(); i++) {
      out[i] = in.get(i).longValue();
    }

    return out;
  }

  public String formatLongArray(long [] ar) {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    boolean first = true;
    for (long val : ar) {
      if (!first) {
        sb.append(", ");
      }

      sb.append(Long.toString(val));
      first = false;
    }

    sb.append("]");
    return sb.toString();
  }

  public void assertLongArrayEquals(long [] expected, long [] actual) {
    for (int i = 0; i < expected.length; i++) {
      try {
        assertEquals("Failure at position " + i + "; got " + actual[i]
            + " instead of " + expected[i] + "; actual array is " + formatLongArray(actual),
            expected[i], actual[i]);
      } catch (ArrayIndexOutOfBoundsException oob) {
        fail("Expected array with " + expected.length + " elements; got " + actual.length
            + ". Actual array is " + formatLongArray(actual));
      }
    }

    if (actual.length > expected.length) {
      fail("Actual array has " + actual.length + " elements; expected " + expected.length
          + ". ACtual array is " + formatLongArray(actual));
    }
  }

  public void testEvenSplits() throws SQLException {
    List<Long> splits = new IntegerSplitter().split(10, 0, 100);
    long [] expected = { 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 };
    assertLongArrayEquals(expected, toLongArray(splits));
  }

  public void testOddSplits() throws SQLException {
    List<Long> splits = new IntegerSplitter().split(10, 0, 95);
    long [] expected = { 0, 9, 18, 27, 36, 45, 54, 63, 72, 81, 90, 95 };
    assertLongArrayEquals(expected, toLongArray(splits));

  }

  public void testSingletonSplit() throws SQLException {
    List<Long> splits = new IntegerSplitter().split(1, 5, 5);
    long [] expected = { 5, 5 };
    assertLongArrayEquals(expected, toLongArray(splits));
  }

  public void testSingletonSplit2() throws SQLException {
    // Same test, but overly-high numSplits
    List<Long> splits = new IntegerSplitter().split(5, 5, 5);
    long [] expected = { 5, 5 };
    assertLongArrayEquals(expected, toLongArray(splits));
  }

  public void testTooManySplits() throws SQLException {
    List<Long> splits = new IntegerSplitter().split(5, 3, 5);
    long [] expected = { 3, 4, 5 };
    assertLongArrayEquals(expected, toLongArray(splits));
  }

}

