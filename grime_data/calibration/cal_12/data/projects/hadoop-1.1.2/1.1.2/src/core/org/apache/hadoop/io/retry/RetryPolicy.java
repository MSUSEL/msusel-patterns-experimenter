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
package org.apache.hadoop.io.retry;

/**
 * <p>
 * Specifies a policy for retrying method failures.
 * Implementations of this interface should be immutable.
 * </p>
 */
public interface RetryPolicy {
  /**
   * <p>
   * Determines whether the framework should retry a
   * method for the given exception, and the number
   * of retries that have been made for that operation
   * so far.
   * </p>
   * @param e The exception that caused the method to fail.
   * @param retries The number of times the method has been retried.
   * @return <code>true</code> if the method should be retried,
   *   <code>false</code> if the method should not be retried
   *   but shouldn't fail with an exception (only for void methods).
   * @throws Exception The re-thrown exception <code>e</code> indicating
   *   that the method failed and should not be retried further. 
   */
  public boolean shouldRetry(Exception e, int retries) throws Exception;
}
