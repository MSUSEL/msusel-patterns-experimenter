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
 * Interface for sort algorithms accepting {@link IndexedSortable} items.
 *
 * A sort algorithm implementing this interface may only
 * {@link IndexedSortable#compare} and {@link IndexedSortable#swap} items
 * for a range of indices to effect a sort across that range.
 */
public interface IndexedSorter {

  /**
   * Sort the items accessed through the given IndexedSortable over the given
   * range of logical indices. From the perspective of the sort algorithm,
   * each index between l (inclusive) and r (exclusive) is an addressable
   * entry.
   * @see IndexedSortable#compare
   * @see IndexedSortable#swap
   */
  void sort(IndexedSortable s, int l, int r);

  /**
   * Same as {@link #sort(IndexedSortable,int,int)}, but indicate progress
   * periodically.
   * @see #sort(IndexedSortable,int,int)
   */
  void sort(IndexedSortable s, int l, int r, Progressable rep);

}
