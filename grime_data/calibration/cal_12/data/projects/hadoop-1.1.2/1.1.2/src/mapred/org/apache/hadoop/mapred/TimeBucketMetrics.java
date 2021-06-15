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
package org.apache.hadoop.mapred;

import java.util.HashMap;

/**
 * Create a set of buckets that hold key-time pairs. When the values of the 
 * buckets is queried, the number of objects with time differences in the
 * different buckets is returned.
 */
class TimeBucketMetrics<OBJ> {

  private final HashMap<OBJ, Long> map = new HashMap<OBJ, Long>();
  private final int[] counts;
  private final long[] cuts;

  /**
   * Create a set of buckets based on a set of time points. The number of 
   * buckets is one more than the number of points.
   */
  TimeBucketMetrics(long[] cuts) {
    this.cuts = cuts;
    counts = new int[cuts.length + 1];
  }

  /**
   * Add an object to be counted
   */
  synchronized void add(OBJ key, long time) {
    map.put(key, time);
  }

  /**
   * Remove an object to be counted
   */
  synchronized void remove(OBJ key) {
    map.remove(key);
  }

  /**
   * Find the bucket based on the cut points.
   */
  private int findBucket(long val) {
    for(int i=0; i < cuts.length; ++i) {
      if (val < cuts[i]) {
	return i;
      }
    }
    return cuts.length;
  }

  /**
   * Get the counts of how many keys are in each bucket. The same array is
   * returned by each call to this method.
   */
  synchronized int[] getBucketCounts(long now) {
    for(int i=0; i < counts.length; ++i) {
      counts[i] = 0;
    }
    for(Long time: map.values()) {
      counts[findBucket(now - time)] += 1;
    }
    return counts;
  }
}