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

import java.util.Comparator;
import org.apache.hadoop.io.IntWritable;

/** An implementation of the core algorithm of MergeSort. */
public class MergeSort {
  //Reusable IntWritables
  IntWritable I = new IntWritable(0);
  IntWritable J = new IntWritable(0);
  
  //the comparator that the algo should use
  private Comparator<IntWritable> comparator;
  
  public MergeSort(Comparator<IntWritable> comparator) {
    this.comparator = comparator;
  }
  
  public void mergeSort(int src[], int dest[], int low, int high) {
    int length = high - low;

    // Insertion sort on smallest arrays
    if (length < 7) {
      for (int i=low; i<high; i++) {
        for (int j=i;j > low; j--) {
          I.set(dest[j-1]);
          J.set(dest[j]);
          if (comparator.compare(I, J)>0)
            swap(dest, j, j-1);
        }
      }
      return;
    }

    // Recursively sort halves of dest into src
    int mid = (low + high) >>> 1;
    mergeSort(dest, src, low, mid);
    mergeSort(dest, src, mid, high);

    I.set(src[mid-1]);
    J.set(src[mid]);
    // If list is already sorted, just copy from src to dest.  This is an
    // optimization that results in faster sorts for nearly ordered lists.
    if (comparator.compare(I, J) <= 0) {
      System.arraycopy(src, low, dest, low, length);
      return;
    }

    // Merge sorted halves (now in src) into dest
    for (int i = low, p = low, q = mid; i < high; i++) {
      if (q < high && p < mid) {
        I.set(src[p]);
        J.set(src[q]);
      }
      if (q>=high || p<mid && comparator.compare(I, J) <= 0)
        dest[i] = src[p++];
      else
        dest[i] = src[q++];
    }
  }

  private void swap(int x[], int a, int b) {
    int t = x[a];
    x[a] = x[b];
    x[b] = t;
  }
}
