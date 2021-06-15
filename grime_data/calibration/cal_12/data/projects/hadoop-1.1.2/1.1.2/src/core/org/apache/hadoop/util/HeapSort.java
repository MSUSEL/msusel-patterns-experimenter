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
package org.apache.hadoop.util;

/**
 * An implementation of the core algorithm of HeapSort.
 */
public final class HeapSort implements IndexedSorter {

  public HeapSort() { }

  private static void downHeap(final IndexedSortable s, final int b,
      int i, final int N) {
    for (int idx = i << 1; idx < N; idx = i << 1) {
      if (idx + 1 < N && s.compare(b + idx, b + idx + 1) < 0) {
        if (s.compare(b + i, b + idx + 1) < 0) {
          s.swap(b + i, b + idx + 1);
        } else return;
        i = idx + 1;
      } else if (s.compare(b + i, b + idx) < 0) {
        s.swap(b + i, b + idx);
        i = idx;
      } else return;
    }
  }

  /**
   * Sort the given range of items using heap sort.
   * {@inheritDoc}
   */
  public void sort(IndexedSortable s, int p, int r) {
    sort(s, p, r, null);
  }

  /**
   * {@inheritDoc}
   */
  public void sort(final IndexedSortable s, final int p, final int r,
      final Progressable rep) {
    final int N = r - p;
    // build heap w/ reverse comparator, then write in-place from end
    final int t = Integer.highestOneBit(N);
    for (int i = t; i > 1; i >>>= 1) {
      for (int j = i >>> 1; j < i; ++j) {
        downHeap(s, p-1, j, N + 1);
      }
      if (null != rep) {
        rep.progress();
      }
    }
    for (int i = r - 1; i > p; --i) {
      s.swap(p, i);
      downHeap(s, p - 1, 1, i - p + 1);
    }
  }
}
