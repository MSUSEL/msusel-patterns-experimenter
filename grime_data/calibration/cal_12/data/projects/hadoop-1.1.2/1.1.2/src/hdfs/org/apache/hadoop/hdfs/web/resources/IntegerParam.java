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

/** Integer parameter. */
abstract class IntegerParam extends Param<Integer, IntegerParam.Domain> {
  IntegerParam(final Domain domain, final Integer value,
      final Integer min, final Integer max) {
    super(domain, value);
    checkRange(min, max);
  }

  private void checkRange(final Integer min, final Integer max) {
    if (value == null) {
      return;
    }
    if (min != null && value < min) {
      throw new IllegalArgumentException("Invalid parameter range: " + getName()
          + " = " + domain.toString(value) + " < " + domain.toString(min));
    }
    if (max != null && value > max) {
      throw new IllegalArgumentException("Invalid parameter range: " + getName()
          + " = " + domain.toString(value) + " > " + domain.toString(max));
    }
  }
  
  @Override
  public String toString() {
    return getName() + "=" + domain.toString(getValue());
  }

  /** The domain of the parameter. */
  static final class Domain extends Param.Domain<Integer> {
    /** The radix of the number. */
    final int radix;

    Domain(final String paramName) {
      this(paramName, 10);
    }

    Domain(final String paramName, final int radix) {
      super(paramName);
      this.radix = radix;
    }

    @Override
    public String getDomain() {
      return "<" + NULL + " | int in radix " + radix + ">";
    }

    @Override
    Integer parse(final String str) {
      try{
        return NULL.equals(str)? null: Integer.parseInt(str, radix);
      } catch(NumberFormatException e) {
        throw new IllegalArgumentException("Failed to parse \"" + str
            + "\" as a radix-" + radix + " integer.", e);
      }
    }

    /** Convert an Integer to a String. */ 
    String toString(final Integer n) {
      return n == null? NULL: Integer.toString(n, radix);
    }
  }
}
