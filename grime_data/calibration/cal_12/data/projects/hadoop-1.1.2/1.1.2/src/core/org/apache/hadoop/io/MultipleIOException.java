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
package org.apache.hadoop.io;

import java.io.IOException;
import java.util.List;

/** Encapsulate a list of {@link IOException} into an {@link IOException} */
public class MultipleIOException extends IOException {
  /** Require by {@link java.io.Serializable} */
  private static final long serialVersionUID = 1L;
  
  private final List<IOException> exceptions;
  
  /** Constructor is private, use {@link #createIOException(List)}. */
  private MultipleIOException(List<IOException> exceptions) {
    super(exceptions.size() + " exceptions " + exceptions);
    this.exceptions = exceptions;
  }

  /** @return the underlying exceptions */
  public List<IOException> getExceptions() {return exceptions;}

  /** A convenient method to create an {@link IOException}. */
  public static IOException createIOException(List<IOException> exceptions) {
    if (exceptions == null || exceptions.isEmpty()) {
      return null;
    }
    if (exceptions.size() == 1) {
      return exceptions.get(0);
    }
    return new MultipleIOException(exceptions);
  }
}
