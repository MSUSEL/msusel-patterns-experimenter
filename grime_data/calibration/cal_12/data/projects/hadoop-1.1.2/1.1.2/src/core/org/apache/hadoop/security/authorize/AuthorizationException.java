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
package org.apache.hadoop.security.authorize;

import java.io.PrintStream;
import java.io.PrintWriter;

import org.apache.hadoop.security.AccessControlException;

/**
 * An exception class for authorization-related issues.
 * 
 * This class <em>does not</em> provide the stack trace for security purposes.
 */
public class AuthorizationException extends AccessControlException {
  private static final long serialVersionUID = 1L;

  public AuthorizationException() {
    super();
  }

  public AuthorizationException(String message) {
    super(message);
  }
  
  /**
   * Constructs a new exception with the specified cause and a detail
   * message of <tt>(cause==null ? null : cause.toString())</tt> (which
   * typically contains the class and detail message of <tt>cause</tt>).
   * @param  cause the cause (which is saved for later retrieval by the
   *         {@link #getCause()} method).  (A <tt>null</tt> value is
   *         permitted, and indicates that the cause is nonexistent or
   *         unknown.)
   */
  public AuthorizationException(Throwable cause) {
    super(cause);
  }
  
  private static StackTraceElement[] stackTrace = new StackTraceElement[0];
  @Override
  public StackTraceElement[] getStackTrace() {
    // Do not provide the stack-trace
    return stackTrace;
  }

  @Override
  public void printStackTrace() {
    // Do not provide the stack-trace
  }

  @Override
  public void printStackTrace(PrintStream s) {
    // Do not provide the stack-trace
  }

  @Override
  public void printStackTrace(PrintWriter s) {
    // Do not provide the stack-trace
  }
  
}
