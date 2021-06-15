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

import org.junit.Test;
import static org.junit.Assert.*;

public class TestTryIterator {

  /**
   * Test a common use case
   */
  @Test public void testCommonIteration() {
    Iterator<Integer> it = new TryIterator<Integer>() {
      private int count = 0;
      @Override protected Integer tryNext() {
        switch (count++) {
          case 0: return 0;
          case 1: return 1;
          case 2: return done();
          default: fail("Should not reach here");
        }
        return null;
      }
    };

    assertTrue("has next", it.hasNext());
    assertEquals("next", 0, (int) it.next());

    assertTrue("has next", it.hasNext());
    assertTrue("has next", it.hasNext()); // should be idempotent

    assertEquals("current", 1, (int) ((TryIterator<Integer>) it).current());
    assertEquals("current 1", 1, (int) ((TryIterator<Integer>) it).current());
    assertEquals("next", 1, (int) it.next());

    assertTrue("no next", !it.hasNext());
    assertTrue("no next", !it.hasNext()); // ditto

    try {
      it.next();
      fail("Should throw exception");
    }
    catch (NoSuchElementException expected) {
      expected.getCause();
    }
  }

  /**
   * Test empty conditions
   */
  @Test public void testEmptyIteration() {
    TryIterator<Integer> it = new TryIterator<Integer>() {
      private boolean doneDone = false;
      @Override public Integer tryNext() {
        if (doneDone) {
          fail("Should not be called again");
        }
        doneDone = true;
        return done();
      }
    };

    assertTrue("should not has next", !it.hasNext());

    try {
      it.current();
      fail("should throw");
    }
    catch (NoSuchElementException expected) {
      expected.getCause();
    }

    try {
      it.next();
      fail("should throw");
    }
    catch (NoSuchElementException expected) {
      expected.getCause();
    }
  }

  /**
   * Test tryNext throwing exceptions
   */
  @Test public void testExceptionInTryNext() {
    final RuntimeException exception = new RuntimeException("expected");

    Iterator<Integer> it = new TryIterator<Integer>() {
      @Override public Integer tryNext() {
        throw exception;
      }
    };

    try {
      it.hasNext();
      fail("should throw");
    }
    catch (Exception expected) {
      assertSame(exception, expected);
    }
  }

  /**
   * Test remove method on the iterator, which should throw
   */
  @Test public void testRemove() {
    Iterator<Integer> it = new TryIterator<Integer>() {
      private boolean called = false;
      @Override public Integer tryNext() {
        if (called) {
          return done();
        }
        called = true;
        return 0;
      }
    };

    assertEquals("should be 0", 0, (int) it.next());

    try {
      it.remove();
    }
    catch (UnsupportedOperationException expected) {
      expected.getCause();
    }
  }

  /**
   * Test calling hasNext in tryNext, which is illegal
   */
  @Test public void testHasNextInTryNext() {
    Iterator<Integer> it = new TryIterator<Integer>() {
      @Override public Integer tryNext() {
        hasNext();
        return null;
      }
    };

    try {
      it.hasNext();
      fail("should throw");
    } catch (IllegalStateException expected) {
      expected.getCause();
    }
  }
}
