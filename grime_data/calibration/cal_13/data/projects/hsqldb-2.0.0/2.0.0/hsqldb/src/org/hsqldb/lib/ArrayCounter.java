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

package org.hsqldb.lib;

/**
 * Collection of routines for counting the distribution of the values
 * in an int[] array.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.7.2
 * @since 1.7.2
 */
public class ArrayCounter {

    /**
     * Returns an int[] array of length segments containing the distribution
     * count of the elements in unsorted int[] array with values between min
     * and max (range). Values outside the min-max range are ignored<p>
     *
     * A usage example is determining the count of people of each age group
     * in a large int[] array containing the age of each person. Called with
     * (array, 16,0,79), it will return an int[16] with the first element
     * the count of people aged 0-4, the second element the count of those
     * aged 5-9, and so on. People above the age of 79 are excluded. If the
     * range is not a multiple of segments, the last segment will be cover a
     * smaller sub-range than the rest.
     *
     */
    public static int[] countSegments(int[] array, int elements,
                                      int segments, int start, int limit) {

        int[] counts   = new int[segments];
        long  interval = calcInterval(segments, start, limit);
        int   index    = 0;
        int   element  = 0;

        if (interval <= 0) {
            return counts;
        }

        for (int i = 0; i < elements; i++) {
            element = array[i];

            if (element < start || element >= limit) {
                continue;
            }

            index = (int) ((element - start) / interval);

            counts[index]++;
        }

        return counts;
    }

    /**
     * With an unsorted int[] array and with target a positive integer in the
     * range (1,array.length), finds the value in the range (start,limit) of the
     * largest element (rank) where the count of all smaller elements in that
     * range is less than or equals target. Parameter margin indicates the
     * margin of error in target<p>
     *
     * In statistics, this can be used to calculate a median or quadrile value.
     * A usage example applied to an array of age values is to determine
     * the maximum age of a given number of people. With the example array
     * given in countSegments, rank(array, c, 6000, 18, 65, 0) will return an age
     * value between 18-64 (inclusive) and the count of all people aged between
     * 18 and the returned value(exclusive) will be less than or equal 6000.
     *
     */
    public static int rank(int[] array, int elements, int target, int start,
                           int limit, int margin) {

        final int segments     = 256;
        int       elementCount = 0;
        int       currentLimit = limit;

        for (;;) {
            long interval = calcInterval(segments, start, currentLimit);
            int[] counts = countSegments(array, elements, segments, start,
                                         currentLimit);

            for (int i = 0; i < counts.length; i++) {
                if (elementCount + counts[i] < target) {
                    elementCount += counts[i];
                    start        += interval;
                } else {
                    break;
                }
            }

            if (elementCount + margin >= target) {
                return start;
            }

            if (interval <= 1) {
                return start;
            }

            currentLimit = start + interval < limit ? (int) (start + interval)
                                                    : limit;
        }
    }

    /**
     * Helper method to calculate the span of the sub-interval. Simply returns
     * the cieling of ((limit - start) / segments) and accounts for invalid
     * start and limit combinations.
     */
    static long calcInterval(int segments, int start, int limit) {

        long range = limit - start;

        if (range < 0) {
            return 0;
        }

        int partSegment = (range % segments) == 0 ? 0
                                                  : 1;

        return (range / segments) + partSegment;
    }
}
