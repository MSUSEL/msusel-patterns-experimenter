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
 * An implementation of the core algorithm of QuickSort.
 */
public final class QuickSort implements IndexedSorter {

  private static final IndexedSorter alt = new HeapSort();

  public QuickSort() { }

  private static void fix(IndexedSortable s, int p, int r) {
    if (s.compare(p, r) > 0) {
      s.swap(p, r);
    }
  }

  /**
   * Deepest recursion before giving up and doing a heapsort.
   * Returns 2 * ceil(log(n)).
   */
  protected static int getMaxDepth(int x) {
    if (x <= 0)
      throw new IllegalArgumentException("Undefined for " + x);
    return (32 - Integer.numberOfLeadingZeros(x - 1)) << 2;
  }

  /**
   * Sort the given range of items using quick sort.
   * {@inheritDoc} If the recursion depth falls below {@link #getMaxDepth},
   * then switch to {@link HeapSort}.
   */
  public void sort(IndexedSortable s, int p, int r) {
    sort(s, p, r, null);
  }

  /**
   * {@inheritDoc}
   */
  public void sort(final IndexedSortable s, int p, int r,
      final Progressable rep) {
    sortInternal(s, p, r, rep, getMaxDepth(r - p));
  }

  private static void sortInternal(final IndexedSortable s, int p, int r,
      final Progressable rep, int depth) {
    if (null != rep) {
      rep.progress();
    }
    while (true) {
    if (r-p < 13) {
      for (int i = p; i < r; ++i) {
        for (int j = i; j > p && s.compare(j-1, j) > 0; --j) {
          s.swap(j, j-1);
        }
      }
      return;
    }
    if (--depth < 0) {
      // give up
      alt.sort(s, p, r, rep);
      return;
    }

    // select, move pivot into first position
    fix(s, (p+r) >>> 1, p);
    fix(s, (p+r) >>> 1, r - 1);
    fix(s, p, r-1);

    // Divide
    int i = p;
    int j = r;
    int ll = p;
    int rr = r;
    int cr;
    while(true) {
      while (++i < j) {
        if ((cr = s.compare(i, p)) > 0) break;
        if (0 == cr && ++ll != i) {
          s.swap(ll, i);
        }
      }
      while (--j > i) {
        if ((cr = s.compare(p, j)) > 0) break;
        if (0 == cr && --rr != j) {
          s.swap(rr, j);
        }
      }
      if (i < j) s.swap(i, j);
      else break;
    }
    j = i;
    // swap pivot- and all eq values- into position
    while (ll >= p) {
      s.swap(ll--, --i);
    }
    while (rr < r) {
      s.swap(rr++, j++);
    }

    // Conquer
    // Recurse on smaller interval first to keep stack shallow
    assert i != j;
    if (i - p < r - j) {
      sortInternal(s, p, i, rep, depth);
      p = j;
    } else {
      sortInternal(s, j, r, rep, depth);
      r = i;
    }
    }
  }

}
