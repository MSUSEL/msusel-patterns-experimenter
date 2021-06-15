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

import java.util.regex.PatternSyntaxException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for glob patterns
 */
public class TestGlobPattern {
  private void assertMatch(boolean yes, String glob, String...input) {
    GlobPattern pattern = new GlobPattern(glob);

    for (String s : input) {
      boolean result = pattern.matches(s);
      assertTrue(glob +" should"+ (yes ? "" : " not") +" match "+ s,
                 yes ? result : !result);
    }
  }

  private void shouldThrow(String... globs) {
    for (String glob : globs) {
      try {
        GlobPattern.compile(glob);
      }
      catch (PatternSyntaxException e) {
        e.printStackTrace();
        continue;
      }
      assertTrue("glob "+ glob +" should throw", false);
    }
  }

  @Test public void testValidPatterns() {
    assertMatch(true, "*", "^$", "foo", "bar");
    assertMatch(true, "?", "?", "^", "[", "]", "$");
    assertMatch(true, "foo*", "foo", "food", "fool");
    assertMatch(true, "f*d", "fud", "food");
    assertMatch(true, "*d", "good", "bad");
    assertMatch(true, "\\*\\?\\[\\{\\\\", "*?[{\\");
    assertMatch(true, "[]^-]", "]", "-", "^");
    assertMatch(true, "]", "]");
    assertMatch(true, "^.$()|+", "^.$()|+");
    assertMatch(true, "[^^]", ".", "$", "[", "]");
    assertMatch(false, "[^^]", "^");
    assertMatch(true, "[!!-]", "^", "?");
    assertMatch(false, "[!!-]", "!", "-");
    assertMatch(true, "{[12]*,[45]*,[78]*}", "1", "2!", "4", "42", "7", "7$");
    assertMatch(false, "{[12]*,[45]*,[78]*}", "3", "6", "9ß");
    assertMatch(true, "}", "}");
  }

  @Test public void testInvalidPatterns() {
    shouldThrow("[", "[[]]", "[][]", "{", "\\");
  }
}
