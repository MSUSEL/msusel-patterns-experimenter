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
package org.apache.hadoop.fs;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

public class TestGlobExpander extends TestCase {

  public void testExpansionIsIdentical() throws IOException {
    checkExpansionIsIdentical("");
    checkExpansionIsIdentical("/}");
    checkExpansionIsIdentical("/}{a,b}");
    checkExpansionIsIdentical("{/");
    checkExpansionIsIdentical("{a}");
    checkExpansionIsIdentical("{a,b}/{b,c}");
    checkExpansionIsIdentical("p\\{a/b,c/d\\}s");
    checkExpansionIsIdentical("p{a\\/b,c\\/d}s");
  }

  public void testExpansion() throws IOException {
    checkExpansion("{a/b}", "a/b");
    checkExpansion("/}{a/b}", "/}a/b");
    checkExpansion("p{a/b,c/d}s", "pa/bs", "pc/ds");
    checkExpansion("{a/b,c/d,{e,f}}", "a/b", "c/d", "{e,f}");
    checkExpansion("{a/b,c/d}{e,f}", "a/b{e,f}", "c/d{e,f}");
    checkExpansion("{a,b}/{b,{c/d,e/f}}", "{a,b}/b", "{a,b}/c/d", "{a,b}/e/f");
    checkExpansion("{a,b}/{c/\\d}", "{a,b}/c/d");
  }

  private void checkExpansionIsIdentical(String filePattern) throws IOException {
    checkExpansion(filePattern, filePattern);
  }

  private void checkExpansion(String filePattern, String... expectedExpansions)
      throws IOException {
    List<String> actualExpansions = GlobExpander.expand(filePattern);
    assertEquals("Different number of expansions", expectedExpansions.length,
        actualExpansions.size());
    for (int i = 0; i < expectedExpansions.length; i++) {
      assertEquals("Expansion of " + filePattern, expectedExpansions[i],
          actualExpansions.get(i));
    }
  }
}
