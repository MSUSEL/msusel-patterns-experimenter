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
 * Classes that implement this interface can deep-compare [for equality only,
 * not order] with another instance. They do a deep compare. If there is any
 * semantically significant difference, an implementer throws an Exception to be
 * thrown with a chain of causes describing the chain of field references and
 * indices that get you to the miscompared point.
 * 
 */
public interface DeepCompare {
  /**
   * @param other
   *          the other comparand that's being compared to me
   * @param myLocation
   *          the path that got to me. In the root, myLocation is null. To
   *          process the scalar {@code foo} field of the root we will make a
   *          recursive call with a {@link TreePath} whose {@code fieldName} is
   *          {@code "bar"} and whose {@code index} is -1 and whose {@code
   *          parent} is {@code null}. To process the plural {@code bar} field
   *          of the root we will make a recursive call with a {@link TreePath}
   *          whose fieldName is {@code "foo"} and whose {@code index} is -1 and
   *          whose {@code parent} is also {@code null}.
   * @throws DeepInequalityException
   */
  public void deepCompare(DeepCompare other, TreePath myLocation)
      throws DeepInequalityException;
}
