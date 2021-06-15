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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A base class for unmodifiable iterators (throws on remove)
 *
 * This class also makes writing filtering iterators easier, where the only
 * way to discover the end of data is by trying to read it. The same applies
 * to writing iterator wrappers around stream read calls.
 *
 * One only needs to implement the tryNext() method and call done() when done.
 *
 * @param <T> the type of the iterator
 */
public abstract class TryIterator<T> implements Iterator<T> {

  enum State {
    PENDING,  // Ready to tryNext().
    GOT_NEXT, // Got the next element from tryNext() and yet to return it.
    DONE,     // Done/finished.
    FAILED,   // An exception occurred in the last op.
  }

  private State state = State.PENDING;
  private T next;

  /**
   * Return the next element. Must call {@link #done()} when done, otherwise
   * infinite loop could occur. If this method throws an exception, any
   * further attempts to use the iterator would result in an
   * {@link IllegalStateException}.
   *
   * @return the next element if there is one or return {@link #done()}
   */
  protected abstract T tryNext();

  /**
   * Implementations of {@link #tryNext} <b>must</b> call this method
   * when there are no more elements left in the iteration.
   *
   * @return  null as a convenience to implement {@link #tryNext()}
   */
  protected final T done() {
    state = State.DONE;
    return null;
  }

  /**
   * @return  true if we have a next element or false otherwise.
   */
  public final boolean hasNext() {
    if (state == State.FAILED)
      throw new IllegalStateException();

    switch (state) {
      case DONE:      return false;
      case GOT_NEXT:  return true;
      default:
    }

    // handle tryNext
    state = State.FAILED; // just in case
    next = tryNext();

    if (state != State.DONE) {
      state = State.GOT_NEXT;
      return true;
    }
    return false;
  }

  /**
   * @return  the next element if we have one.
   */
  public final T next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    state = State.PENDING;
    return next;
  }

  /**
   * @return the current element without advancing the iterator
   */
  public final T current() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    return next;
  }

  /**
   * Guaranteed to throw UnsupportedOperationException
   */
  public final void remove() {
    throw new UnsupportedOperationException("Not allowed.");
  }

}
