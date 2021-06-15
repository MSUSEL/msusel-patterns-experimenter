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
package org.apache.hadoop.record.compiler;

import java.util.ArrayList;

/**
 * A wrapper around StringBuffer that automatically does indentation
 */
public class CodeBuffer {
  
  static private ArrayList<Character> startMarkers = new ArrayList<Character>();
  static private ArrayList<Character> endMarkers = new ArrayList<Character>();
  
  static {
    addMarkers('{', '}');
    addMarkers('(', ')');
  }
  
  static void addMarkers(char ch1, char ch2) {
    startMarkers.add(ch1);
    endMarkers.add(ch2);
  }
  
  private int level = 0;
  private int numSpaces = 2;
  private boolean firstChar = true;
  private StringBuffer sb;
  
  /** Creates a new instance of CodeBuffer */
  CodeBuffer() {
    this(2, "");
  }
  
  CodeBuffer(String s) {
    this(2, s);
  }
  
  CodeBuffer(int numSpaces, String s) {
    sb = new StringBuffer();
    this.numSpaces = numSpaces;
    this.append(s);
  }
  
  void append(String s) {
    int length = s.length();
    for (int idx = 0; idx < length; idx++) {
      char ch = s.charAt(idx);
      append(ch);
    }
  }
  
  void append(char ch) {
    if (endMarkers.contains(ch)) {
      level--;
    }
    if (firstChar) {
      for (int idx = 0; idx < level; idx++) {
        for (int num = 0; num < numSpaces; num++) {
          rawAppend(' ');
        }
      }
    }
    rawAppend(ch);
    firstChar = false;
    if (startMarkers.contains(ch)) {
      level++;
    }
    if (ch == '\n') {
      firstChar = true;
    }
  }

  private void rawAppend(char ch) {
    sb.append(ch);
  }
  
  public String toString() {
    return sb.toString();
  }
}
