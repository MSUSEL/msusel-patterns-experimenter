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
package org.apache.hadoop.metrics2.util;

/**
 * Utilities for programming by contract (preconditions, postconditions etc.)
 */
public class Contracts {

  private Contracts() {}

  /**
   * Check that a reference is not null.
   * @param <T> type of the reference
   * @param ref the reference to check
   * @param msg the error message
   * @throws NullPointerException if {@code ref} is null
   * @return the checked reference for convenience
   */
  public static <T> T checkNotNull(T ref, Object msg) {
    if (ref == null) {
      throw new NullPointerException(String.valueOf(msg) +": "+
                                     ref.getClass().getName());
    }
    return ref;
  }

  /**
   * Check the state expression for false conditions
   * @param expression  the boolean expression to check
   * @param msg the error message if {@code expression} is false
   * @throws IllegalStateException if {@code expression} is false
   */
  public static void checkState(boolean expression, Object msg) {
    if (!expression) {
      throw new IllegalStateException(String.valueOf(msg));
    }
  }

  /**
   * Check an argument for false conditions
   * @param <T> type of the argument
   * @param arg the argument to check
   * @param expression  the boolean expression for the condition
   * @param msg the error message if {@code expression} is false
   * @return the argument for convenience
   */
  public static <T> T checkArg(T arg, boolean expression, Object msg) {
    if (!expression) {
      throw new IllegalArgumentException(String.valueOf(msg) +": "+ arg);
    }
    return arg;
  }

  /**
   * Check an argument for false conditions
   * @param arg the argument to check
   * @param expression  the boolean expression for the condition
   * @param msg the error message if {@code expression} is false
   * @return the argument for convenience
   */
  public static int checkArg(int arg, boolean expression, Object msg) {
    if (!expression) {
      throw new IllegalArgumentException(String.valueOf(msg)+ ": "+arg);
    }
    return arg;
  }

  /**
   * Check an argument for false conditions
   * @param arg the argument to check
   * @param expression  the boolean expression for the condition
   * @param msg the error message if {@code expression} is false
   * @return the argument for convenience
   */
  public static long checkArg(long arg, boolean expression, Object msg) {
    if (!expression) {
      throw new IllegalArgumentException(String.valueOf(msg));
    }
    return arg;
  }

  /**
   * Check an argument for false conditions
   * @param arg the argument to check
   * @param expression  the boolean expression for the condition
   * @param msg the error message if {@code expression} is false
   * @return the argument for convenience
   */
  public static float checkArg(float arg, boolean expression, Object msg) {
    if (!expression) {
      throw new IllegalArgumentException(String.valueOf(msg) +": "+ arg);
    }
    return arg;
  }

  /**
   * Check an argument for false conditions
   * @param arg the argument to check
   * @param expression  the boolean expression for the condition
   * @param msg the error message if {@code expression} is false
   * @return the argument for convenience
   */
  public static double checkArg(double arg, boolean expression, Object msg) {
    if (!expression) {
      throw new IllegalArgumentException(String.valueOf(msg) +": "+ arg);
    }
    return arg;
  }

}
