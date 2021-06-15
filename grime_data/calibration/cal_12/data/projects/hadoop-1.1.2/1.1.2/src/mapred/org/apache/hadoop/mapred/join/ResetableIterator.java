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
package org.apache.hadoop.mapred.join;

import java.io.IOException;

import org.apache.hadoop.io.Writable;

/**
 * This defines an interface to a stateful Iterator that can replay elements
 * added to it directly.
 * Note that this does not extend {@link java.util.Iterator}.
 */
public interface ResetableIterator<T extends Writable> {

  public static class EMPTY<U extends Writable>
    implements ResetableIterator<U> {
    public boolean hasNext() { return false; }
    public void reset() { }
    public void close() throws IOException { }
    public void clear() { }
    public boolean next(U val) throws IOException {
      return false;
    }
    public boolean replay(U val) throws IOException {
      return false;
    }
    public void add(U item) throws IOException {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * True if a call to next may return a value. This is permitted false
   * positives, but not false negatives.
   */
  public boolean hasNext();

  /**
   * Assign next value to actual.
   * It is required that elements added to a ResetableIterator be returned in
   * the same order after a call to {@link #reset} (FIFO).
   *
   * Note that a call to this may fail for nested joins (i.e. more elements
   * available, but none satisfying the constraints of the join)
   */
  public boolean next(T val) throws IOException;

  /**
   * Assign last value returned to actual.
   */
  public boolean replay(T val) throws IOException;

  /**
   * Set iterator to return to the start of its range. Must be called after
   * calling {@link #add} to avoid a ConcurrentModificationException.
   */
  public void reset();

  /**
   * Add an element to the collection of elements to iterate over.
   */
  public void add(T item) throws IOException;

  /**
   * Close datasources and release resources. Calling methods on the iterator
   * after calling close has undefined behavior.
   */
  // XXX is this necessary?
  public void close() throws IOException;

  /**
   * Close datasources, but do not release internal resources. Calling this
   * method should permit the object to be reused with a different datasource.
   */
  public void clear();

}
