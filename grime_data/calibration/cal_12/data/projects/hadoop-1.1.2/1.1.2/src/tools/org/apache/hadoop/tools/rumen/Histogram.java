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
/**
 * 
 */
package org.apache.hadoop.tools.rumen;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * {@link Histogram} represents an ordered summary of a sequence of {@code long}
 * s which can be queried to produce a discrete approximation of its cumulative
 * distribution function
 * 
 */
class Histogram implements Iterable<Map.Entry<Long, Long>> {
  private TreeMap<Long, Long> content = new TreeMap<Long, Long>();

  private String name;

  private long totalCount;

  public Histogram() {
    this("(anonymous)");
  }

  public Histogram(String name) {
    super();

    this.name = name;

    totalCount = 0L;
  }

  public void dump(PrintStream stream) {
    stream.print("dumping Histogram " + name + ":\n");

    Iterator<Map.Entry<Long, Long>> iter = iterator();

    while (iter.hasNext()) {
      Map.Entry<Long, Long> ent = iter.next();

      stream.print("val/count pair: " + (long) ent.getKey() + ", "
          + (long) ent.getValue() + "\n");
    }

    stream.print("*** end *** \n");
  }

  public Iterator<Map.Entry<Long, Long>> iterator() {
    return content.entrySet().iterator();
  }

  public long get(long key) {
    Long result = content.get(key);

    return result == null ? 0 : result;
  }

  public long getTotalCount() {
    return totalCount;
  }

  public void enter(long value) {
    Long existingValue = content.get(value);

    if (existingValue == null) {
      content.put(value, 1L);
    } else {
      content.put(value, existingValue + 1L);
    }

    ++totalCount;
  }

  /**
   * Produces a discrete approximation of the CDF. The user provides the points
   * on the {@code Y} axis he wants, and we give the corresponding points on the
   * {@code X} axis, plus the minimum and maximum from the data.
   * 
   * @param scale
   *          the denominator applied to every element of buckets. For example,
   *          if {@code scale} is {@code 1000}, a {@code buckets} element of 500
   *          will specify the median in that output slot.
   * @param buckets
   *          an array of int, all less than scale and each strictly greater
   *          than its predecessor if any. We don't check these requirements.
   * @return a {@code long[]}, with two more elements than {@code buckets} has.
   *         The first resp. last element is the minimum resp. maximum value
   *         that was ever {@code enter}ed. The rest of the elements correspond
   *         to the elements of {@code buckets} and carry the first element
   *         whose rank is no less than {@code #content elements * scale /
   *         bucket}.
   * 
   */
  public long[] getCDF(int scale, int[] buckets) {
    if (totalCount == 0) {
      return null;
    }

    long[] result = new long[buckets.length + 2];

    // fill in the min and the max
    result[0] = content.firstEntry().getKey();

    result[buckets.length + 1] = content.lastEntry().getKey();

    Iterator<Map.Entry<Long, Long>> iter = content.entrySet().iterator();
    long cumulativeCount = 0;
    int bucketCursor = 0;

    
    // Loop invariant: the item at buckets[bucketCursor] can still be reached
    // from iter, and the number of logged elements no longer available from
    // iter is cumulativeCount.
    // 
    // cumulativeCount/totalCount is therefore strictly less than
    // buckets[bucketCursor]/scale .
     
    while (iter.hasNext()) {
      long targetCumulativeCount = buckets[bucketCursor] * totalCount / scale;

      Map.Entry<Long, Long> elt = iter.next();

      cumulativeCount += elt.getValue();

      while (cumulativeCount >= targetCumulativeCount) {
        result[bucketCursor + 1] = elt.getKey();

        ++bucketCursor;

        if (bucketCursor < buckets.length) {
          targetCumulativeCount = buckets[bucketCursor] * totalCount / scale;
        } else {
          break;
        }
      }

      if (bucketCursor == buckets.length) {
        break;
      }
    }

    return result;
  }
}
