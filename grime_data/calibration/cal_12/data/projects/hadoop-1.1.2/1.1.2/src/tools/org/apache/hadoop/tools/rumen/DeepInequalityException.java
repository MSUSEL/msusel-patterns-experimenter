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

/**
 * We use this exception class in the unit test, and we do a deep comparison
 * when we run the
 * 
 */
public class DeepInequalityException extends Exception {

  static final long serialVersionUID = 1352469876;

  final TreePath path;

  /**
   * @param message
   *          an exception message
   * @param path
   *          the path that gets from the root to the inequality
   * 
   *          This is the constructor that I intend to have used for this
   *          exception.
   */
  public DeepInequalityException(String message, TreePath path,
      Throwable chainee) {
    super(message, chainee);

    this.path = path;
  }

  /**
   * @param message
   *          an exception message
   * @param path
   *          the path that gets from the root to the inequality
   * 
   *          This is the constructor that I intend to have used for this
   *          exception.
   */
  public DeepInequalityException(String message, TreePath path) {
    super(message);

    this.path = path;
  }
}
