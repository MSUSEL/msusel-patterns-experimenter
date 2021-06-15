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

public class TestTextSplitter extends TestCase {

  public String formatArray(Object [] ar) {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    boolean first = true;
    for (Object val : ar) {
      if (!first) {
        sb.append(", ");
      }

      sb.append(val.toString());
      first = false;
    }

    sb.append("]");
    return sb.toString();
  }

  public void assertArrayEquals(Object [] expected, Object [] actual) {
    for (int i = 0; i < expected.length; i++) {
      try {
        assertEquals("Failure at position " + i + "; got " + actual[i]
            + " instead of " + expected[i] + "; actual array is " + formatArray(actual),
            expected[i], actual[i]);
      } catch (ArrayIndexOutOfBoundsException oob) {
        fail("Expected array with " + expected.length + " elements; got " + actual.length
            + ". Actual array is " + formatArray(actual));
      }
    }

    if (actual.length > expected.length) {
      fail("Actual array has " + actual.length + " elements; expected " + expected.length
          + ". Actual array is " + formatArray(actual));
    }
  }

  public void testStringConvertEmpty() {
    TextSplitter splitter = new TextSplitter();
    BigDecimal emptyBigDec = splitter.stringToBigDecimal("");
    assertEquals(BigDecimal.ZERO, emptyBigDec);
  }

  public void testBigDecConvertEmpty() {
    TextSplitter splitter = new TextSplitter();
    String emptyStr = splitter.bigDecimalToString(BigDecimal.ZERO);
    assertEquals("", emptyStr);
  }

  public void testConvertA() {
    TextSplitter splitter = new TextSplitter();
    String out = splitter.bigDecimalToString(splitter.stringToBigDecimal("A"));
    assertEquals("A", out);
  }

  public void testConvertZ() {
    TextSplitter splitter = new TextSplitter();
    String out = splitter.bigDecimalToString(splitter.stringToBigDecimal("Z"));
    assertEquals("Z", out);
  }

  public void testConvertThreeChars() {
    TextSplitter splitter = new TextSplitter();
    String out = splitter.bigDecimalToString(splitter.stringToBigDecimal("abc"));
    assertEquals("abc", out);
  }

  public void testConvertStr() {
    TextSplitter splitter = new TextSplitter();
    String out = splitter.bigDecimalToString(splitter.stringToBigDecimal("big str"));
    assertEquals("big str", out);
  }

  public void testConvertChomped() {
    TextSplitter splitter = new TextSplitter();
    String out = splitter.bigDecimalToString(splitter.stringToBigDecimal("AVeryLongStringIndeed"));
    assertEquals("AVeryLon", out);
  }

  public void testAlphabetSplit() throws SQLException {
    // This should give us 25 splits, one per letter.
    TextSplitter splitter = new TextSplitter();
    List<String> splits = splitter.split(25, "A", "Z", "");
    String [] expected = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
        "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
    assertArrayEquals(expected, splits.toArray(new String [0]));
  }

  public void testCommonPrefix() throws SQLException {
    // Splits between 'Hand' and 'Hardy'
    TextSplitter splitter = new TextSplitter();
    List<String> splits = splitter.split(5, "nd", "rdy", "Ha");
    // Don't check for exact values in the middle, because the splitter generates some
    // ugly Unicode-isms. But do check that we get multiple splits and that it starts
    // and ends on the correct points.
    assertEquals("Hand", splits.get(0));
    assertEquals("Hardy", splits.get(splits.size() -1));
    assertEquals(6, splits.size());
  }
}

