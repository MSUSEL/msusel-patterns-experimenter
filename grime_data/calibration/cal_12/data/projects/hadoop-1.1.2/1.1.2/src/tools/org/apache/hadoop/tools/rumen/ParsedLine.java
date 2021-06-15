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

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ParsedLine {
  Properties content;
  LogRecordType type;

  static final String KEY = "(\\w+)";
  /**
   * The value string is enclosed in double quotation marks ('"') and
   * occurrences of '"' and '\' are escaped with a '\'. So the escaped value
   * string is essentially a string of escaped sequence ('\' followed by any
   * character) or any character other than '"' and '\'.
   * 
   * The straightforward REGEX to capture the above is "((?:[^\"\\\\]|\\\\.)*)".
   * Unfortunately Java's REGEX implementation is "broken" that it does not
   * perform the NFA-to-DFA conversion and such expressions would lead to
   * backtracking and stack overflow when matching with long strings. The
   * following is a manual "unfolding" of the REGEX to get rid of backtracking.
   */
  static final String VALUE = "([^\"\\\\]*+(?:\\\\.[^\"\\\\]*+)*+)";
  /**
   * REGEX to match the Key-Value pairs in an input line. Capture group 1
   * matches the key and capture group 2 matches the value (without quotation
   * marks).
   */
  static final Pattern keyValPair = Pattern.compile(KEY + "=" + "\"" + VALUE + "\"");

  @SuppressWarnings("unused")
  ParsedLine(String fullLine, int version) {
    super();

    content = new Properties();

    int firstSpace = fullLine.indexOf(" ");

    if (firstSpace < 0) {
      firstSpace = fullLine.length();
    }

    if (firstSpace == 0) {
      return; // This is a junk line of some sort
    }

    type = LogRecordType.intern(fullLine.substring(0, firstSpace));

    String propValPairs = fullLine.substring(firstSpace + 1);

    Matcher matcher = keyValPair.matcher(propValPairs);

    while(matcher.find()){
      String key = matcher.group(1);
      String value = matcher.group(2);
      content.setProperty(key, value);
    }
  }

  protected LogRecordType getType() {
    return type;
  }

  protected String get(String key) {
    return content.getProperty(key);
  }

  protected long getLong(String key) {
    String val = get(key);

    return Long.parseLong(val);
  }
}
