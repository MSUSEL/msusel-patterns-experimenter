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
import java.util.ArrayList;
import java.util.List;

class GlobExpander {
  
  static class StringWithOffset {
    String string;
    int offset;
    public StringWithOffset(String string, int offset) {
      super();
      this.string = string;
      this.offset = offset;
    }
  }
  
  /**
   * Expand globs in the given <code>filePattern</code> into a collection of 
   * file patterns so that in the expanded set no file pattern has a
   * slash character ("/") in a curly bracket pair.
   * @param filePattern
   * @return expanded file patterns
   * @throws IOException 
   */
  public static List<String> expand(String filePattern) throws IOException {
    List<String> fullyExpanded = new ArrayList<String>();
    List<StringWithOffset> toExpand = new ArrayList<StringWithOffset>();
    toExpand.add(new StringWithOffset(filePattern, 0));
    while (!toExpand.isEmpty()) {
      StringWithOffset path = toExpand.remove(0);
      List<StringWithOffset> expanded = expandLeftmost(path);
      if (expanded == null) {
        fullyExpanded.add(path.string);
      } else {
        toExpand.addAll(0, expanded);
      }
    }
    return fullyExpanded;
  }
  
  /**
   * Expand the leftmost outer curly bracket pair containing a
   * slash character ("/") in <code>filePattern</code>.
   * @param filePattern
   * @return expanded file patterns
   * @throws IOException 
   */
  private static List<StringWithOffset> expandLeftmost(StringWithOffset
      filePatternWithOffset) throws IOException {
    
    String filePattern = filePatternWithOffset.string;
    int leftmost = leftmostOuterCurlyContainingSlash(filePattern,
        filePatternWithOffset.offset);
    if (leftmost == -1) {
      return null;
    }
    int curlyOpen = 0;
    StringBuilder prefix = new StringBuilder(filePattern.substring(0, leftmost));
    StringBuilder suffix = new StringBuilder();
    List<String> alts = new ArrayList<String>();
    StringBuilder alt = new StringBuilder();
    StringBuilder cur = prefix;
    for (int i = leftmost; i < filePattern.length(); i++) {
      char c = filePattern.charAt(i);
      if (cur == suffix) {
        cur.append(c);
      } else if (c == '\\') {
        i++;
        if (i >= filePattern.length()) {
          throw new IOException("Illegal file pattern: "
              + "An escaped character does not present for glob "
              + filePattern + " at " + i);
        }
        c = filePattern.charAt(i);
        cur.append(c);
      } else if (c == '{') {
        if (curlyOpen++ == 0) {
          alt.setLength(0);
          cur = alt;
        } else {
          cur.append(c);
        }

      } else if (c == '}' && curlyOpen > 0) {
        if (--curlyOpen == 0) {
          alts.add(alt.toString());
          alt.setLength(0);
          cur = suffix;
        } else {
          cur.append(c);
        }
      } else if (c == ',') {
        if (curlyOpen == 1) {
          alts.add(alt.toString());
          alt.setLength(0);
        } else {
          cur.append(c);
        }
      } else {
        cur.append(c);
      }
    }
    List<StringWithOffset> exp = new ArrayList<StringWithOffset>();
    for (String string : alts) {
      exp.add(new StringWithOffset(prefix + string + suffix, prefix.length()));
    }
    return exp;
  }
  
  /**
   * Finds the index of the leftmost opening curly bracket containing a
   * slash character ("/") in <code>filePattern</code>.
   * @param filePattern
   * @return the index of the leftmost opening curly bracket containing a
   * slash character ("/"), or -1 if there is no such bracket
   * @throws IOException 
   */
  private static int leftmostOuterCurlyContainingSlash(String filePattern,
      int offset) throws IOException {
    int curlyOpen = 0;
    int leftmost = -1;
    boolean seenSlash = false;
    for (int i = offset; i < filePattern.length(); i++) {
      char c = filePattern.charAt(i);
      if (c == '\\') {
        i++;
        if (i >= filePattern.length()) {
          throw new IOException("Illegal file pattern: "
              + "An escaped character does not present for glob "
              + filePattern + " at " + i);
        }
      } else if (c == '{') {
        if (curlyOpen++ == 0) {
          leftmost = i;
        }
      } else if (c == '}' && curlyOpen > 0) {
        if (--curlyOpen == 0 && leftmost != -1 && seenSlash) {
          return leftmost;
        }
      } else if (c == '/' && curlyOpen > 0) {
        seenSlash = true;
      }
    }
    return -1;
  }

}
