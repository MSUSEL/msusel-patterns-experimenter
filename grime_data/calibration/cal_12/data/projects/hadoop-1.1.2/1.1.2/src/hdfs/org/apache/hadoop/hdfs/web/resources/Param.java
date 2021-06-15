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

import java.util.Arrays;
import java.util.Comparator;


/** Base class of parameters. */
public abstract class Param<T, D extends Param.Domain<T>> {
  static final String NULL = "null";
  
  static final Comparator<Param<?,?>> NAME_CMP = new Comparator<Param<?,?>>() {
    @Override
    public int compare(Param<?, ?> left, Param<?, ?> right) {
      return left.getName().compareTo(right.getName());
    }
  };

  /** Convert the parameters to a sorted String. */
  public static String toSortedString(final String separator,
      final Param<?, ?>... parameters) {
    Arrays.sort(parameters, NAME_CMP);
    final StringBuilder b = new StringBuilder();
    for(Param<?, ?> p : parameters) {
      if (p.getValue() != null) {
        b.append(separator).append(p);
      }
    }
    return b.toString();
  }

  /** The domain of the parameter. */
  final D domain;
  /** The actual parameter value. */
  final T value;

  Param(final D domain, final T value) {
    this.domain = domain;
    this.value = value;
  }

  /** @return the parameter value. */
  public final T getValue() {
    return value;
  }

  /** @return the parameter name. */
  public abstract String getName();

  @Override
  public String toString() {
    return getName() + "=" + value;
  }

  /** Base class of parameter domains. */
  static abstract class Domain<T> {
    /** Parameter name. */
    final String paramName;
    
    Domain(final String paramName) {
      this.paramName = paramName;
    }
 
    /** @return the parameter name. */
    public final String getParamName() {
      return paramName;
    }

    /** @return a string description of the domain of the parameter. */
    public abstract String getDomain();

    /** @return the parameter value represented by the string. */
    abstract T parse(String str);

    /** Parse the given string.
     * @return the parameter value represented by the string.
     */
    public final T parse(final String varName, final String str) {
      try {
        return str != null && str.trim().length() > 0 ? parse(str) : null;
      } catch(Exception e) {
        throw new IllegalArgumentException("Failed to parse \"" + str
            + "\" for the parameter " + varName
            + ".  The value must be in the domain " + getDomain(), e);
      }
    }
  }
}