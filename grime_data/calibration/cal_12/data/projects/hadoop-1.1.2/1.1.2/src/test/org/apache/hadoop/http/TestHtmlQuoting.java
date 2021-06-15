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
package org.apache.hadoop.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestHtmlQuoting {

  @Test public void testNeedsQuoting() throws Exception {
    assertTrue(HtmlQuoting.needsQuoting("abcde>"));
    assertTrue(HtmlQuoting.needsQuoting("<abcde"));
    assertTrue(HtmlQuoting.needsQuoting("abc'de"));
    assertTrue(HtmlQuoting.needsQuoting("abcde\""));
    assertTrue(HtmlQuoting.needsQuoting("&"));
    assertFalse(HtmlQuoting.needsQuoting(""));
    assertFalse(HtmlQuoting.needsQuoting("ab\ncdef"));
    assertFalse(HtmlQuoting.needsQuoting(null));
  }

  @Test public void testQuoting() throws Exception {
    assertEquals("ab&lt;cd", HtmlQuoting.quoteHtmlChars("ab<cd"));
    assertEquals("ab&gt;", HtmlQuoting.quoteHtmlChars("ab>"));
    assertEquals("&amp;&amp;&amp;", HtmlQuoting.quoteHtmlChars("&&&"));
    assertEquals(" &apos;\n", HtmlQuoting.quoteHtmlChars(" '\n"));
    assertEquals("&quot;", HtmlQuoting.quoteHtmlChars("\""));
    assertEquals(null, HtmlQuoting.quoteHtmlChars(null));
  }

  private void runRoundTrip(String str) throws Exception {
    assertEquals(str, 
                 HtmlQuoting.unquoteHtmlChars(HtmlQuoting.quoteHtmlChars(str)));
  }
  
  @Test public void testRoundtrip() throws Exception {
    runRoundTrip("");
    runRoundTrip("<>&'\"");
    runRoundTrip("ab>cd<ef&ghi'\"");
    runRoundTrip("A string\n with no quotable chars in it!");
    runRoundTrip(null);
    StringBuilder buffer = new StringBuilder();
    for(char ch=0; ch < 127; ++ch) {
      buffer.append(ch);
    }
    runRoundTrip(buffer.toString());
  }
}
