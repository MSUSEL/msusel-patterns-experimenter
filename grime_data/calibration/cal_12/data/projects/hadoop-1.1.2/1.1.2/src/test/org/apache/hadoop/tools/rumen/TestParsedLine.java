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
package org.apache.hadoop.tools.rumen;

import org.apache.hadoop.util.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestParsedLine {
  static final char[] CHARS_TO_ESCAPE = new char[]{'=', '"', '.'};
  
  String buildLine(String type, String[] kvseq) {
    StringBuilder sb = new StringBuilder();
    sb.append(type);
    for (int i=0; i<kvseq.length; ++i) {
      sb.append(" ");
      if (kvseq[i].equals(".") || kvseq[i].equals("\n")) {
        sb.append(kvseq[i]);
        continue;
      }
      if (i == kvseq.length-1) {
        fail("Incorrect input, expecting value.");
      }
      sb.append(kvseq[i++]);
      sb.append("=\"");
      sb.append(StringUtils.escapeString(kvseq[i], StringUtils.ESCAPE_CHAR,
          CHARS_TO_ESCAPE));
      sb.append("\"");
    }
    return sb.toString();
  }
  
  void testOneLine(String type, String... kvseq) {
    String line = buildLine(type, kvseq);
    ParsedLine pl = new ParsedLine(line, Hadoop20JHParser.internalVersion);
    assertEquals("Mismatching type", type, pl.getType().toString());
    for (int i = 0; i < kvseq.length; ++i) {
      if (kvseq[i].equals(".") || kvseq[i].equals("\n")) {
        continue;
      }

      assertEquals("Key mismatching for " + kvseq[i], kvseq[i + 1], StringUtils
          .unEscapeString(pl.get(kvseq[i]), StringUtils.ESCAPE_CHAR,
              CHARS_TO_ESCAPE));
      ++i;
    }
  }
  
  @Test
  public void testEscapedQuote() {
    testOneLine("REC", "A", "x", "B", "abc\"de", "C", "f");
    testOneLine("REC", "B", "abcde\"", "C", "f");
    testOneLine("REC", "A", "x", "B", "\"abcde");
  }

  @Test
  public void testEqualSign() {
    testOneLine("REC1", "A", "x", "B", "abc=de", "C", "f");
    testOneLine("REC2", "B", "=abcde", "C", "f");
    testOneLine("REC3", "A", "x", "B", "abcde=");
  }

  @Test
  public void testSpace() {
    testOneLine("REC1", "A", "x", "B", "abc de", "C", "f");
    testOneLine("REC2", "B", " ab c de", "C", "f");
    testOneLine("REC3", "A", "x", "B", "abc\t  de  ");
  }

  @Test
  public void testBackSlash() {
    testOneLine("REC1", "A", "x", "B", "abc\\de", "C", "f");
    testOneLine("REC2", "B", "\\ab\\c\\de", "C", "f");
    testOneLine("REC3", "A", "x", "B", "abc\\\\de\\");
    testOneLine("REC4", "A", "x", "B", "abc\\\"de\\\"", "C", "f");
  }

  @Test
  public void testLineDelimiter() {
    testOneLine("REC1", "A", "x", "B", "abc.de", "C", "f");
    testOneLine("REC2", "B", ".ab.de");
    testOneLine("REC3", "A", "x", "B", "abc.de.");
    testOneLine("REC4", "A", "x", "B", "abc.de", ".");
  }
  
  @Test
  public void testMultipleLines() {
    testOneLine("REC1", "A", "x", "\n", "B", "abc.de", "\n", "C", "f", "\n", ".");
  }
}
