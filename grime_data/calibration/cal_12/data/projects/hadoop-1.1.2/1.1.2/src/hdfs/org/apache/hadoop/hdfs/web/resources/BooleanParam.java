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
package org.apache.hadoop.hdfs.web.resources;

/** Boolean parameter. */
abstract class BooleanParam extends Param<Boolean, BooleanParam.Domain> {
  static final String TRUE = "true";
  static final String FALSE = "false";

  BooleanParam(final Domain domain, final Boolean value) {
    super(domain, value);
  }

  /** The domain of the parameter. */
  static final class Domain extends Param.Domain<Boolean> {
    Domain(final String paramName) {
      super(paramName);
    }

    @Override
    public String getDomain() {
      return "<" + NULL + " | boolean>";
    }

    @Override
    Boolean parse(final String str) {
      if (TRUE.equalsIgnoreCase(str)) {
        return true;
      } else if (FALSE.equalsIgnoreCase(str)) {
        return false;
      }
      throw new IllegalArgumentException("Failed to parse \"" + str
          + "\" to Boolean.");
    }
  }
}
