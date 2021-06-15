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

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

class LogRecordType {
  static Map<String, LogRecordType> internees = new HashMap<String, LogRecordType>();

  final String name;

  final int index;

  private LogRecordType(String name) {
    super();

    this.name = name;

    index = internees.size();
  }

  static LogRecordType intern(String typeName) {
    LogRecordType result = internees.get(typeName);

    if (result == null) {
      result = new LogRecordType(typeName);

      internees.put(typeName, result);
    }

    return result;
  }

  static LogRecordType internSoft(String typeName) {
    return internees.get(typeName);
  }

  @Override
  public String toString() {
    return name;
  }

  static String[] lineTypes() {
    Iterator<Map.Entry<String, LogRecordType>> iter = internees.entrySet()
        .iterator();

    String[] result = new String[internees.size()];

    for (int i = 0; i < internees.size(); ++i) {
      result[i] = iter.next().getKey();
    }

    return result;
  }
}
