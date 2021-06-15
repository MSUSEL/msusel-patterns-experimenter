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
import java.io.IOException;

 // A class that could decide if a string matches the glob or not
class GlobFilter implements PathFilter {
  private final static PathFilter DEFAULT_FILTER = new PathFilter() {
      public boolean accept(Path file) {
        return true;
      }
    };

  private PathFilter userFilter = DEFAULT_FILTER;
  private GlobPattern pattern;

  GlobFilter(String filePattern) throws IOException {
    init(filePattern, DEFAULT_FILTER);
  }

  GlobFilter(String filePattern, PathFilter filter) throws IOException {
    init(filePattern, filter);
  }

  void init(String filePattern, PathFilter filter) throws IOException {
    try {
      userFilter = filter;
      pattern = new GlobPattern(filePattern);
    }
    catch (PatternSyntaxException e) {
      // Existing code expects IOException startWith("Illegal file pattern")
      throw new IOException("Illegal file pattern: "+ e.getMessage(), e);
    }
  }

  boolean hasPattern() {
    return pattern.hasWildcard();
  }

  public boolean accept(Path path) {
    return pattern.matches(path.getName()) && userFilter.accept(path);
  }
}
