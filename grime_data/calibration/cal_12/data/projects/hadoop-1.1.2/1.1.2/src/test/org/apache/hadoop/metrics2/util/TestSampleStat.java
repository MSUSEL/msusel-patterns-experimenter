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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the running sample stat computation
 */
public class TestSampleStat {
  private static final double EPSILON = 1e-42;

  /**
   * Some simple use cases
   */
  @Test public void testSimple() {
    SampleStat stat = new SampleStat();
    assertEquals("num samples", 0, stat.numSamples());
    assertEquals("mean", 0.0, stat.mean(), EPSILON);
    assertEquals("variance", 0.0, stat.variance(), EPSILON);
    assertEquals("stddev", 0.0, stat.stddev(), EPSILON);
    assertEquals("min", SampleStat.MinMax.DEFAULT_MIN_VALUE, stat.min(), EPSILON);
    assertEquals("max", SampleStat.MinMax.DEFAULT_MAX_VALUE, stat.max(), EPSILON);

    stat.add(3);
    assertEquals("num samples", 1L, stat.numSamples());
    assertEquals("mean", 3.0, stat.mean(), EPSILON);
    assertEquals("variance", 0.0, stat.variance(), EPSILON);
    assertEquals("stddev", 0.0, stat.stddev(), EPSILON);
    assertEquals("min", 3.0, stat.min(), EPSILON);
    assertEquals("max", 3.0, stat.max(), EPSILON);

    stat.add(2).add(1);
    assertEquals("num samples", 3L, stat.numSamples());
    assertEquals("mean", 2.0, stat.mean(), EPSILON);
    assertEquals("variance", 1.0, stat.variance(), EPSILON);
    assertEquals("stddev", 1.0, stat.stddev(), EPSILON);
    assertEquals("min", 1.0, stat.min(), EPSILON);
    assertEquals("max", 3.0, stat.max(), EPSILON);

    stat.reset();
    assertEquals("num samples", 0, stat.numSamples());
    assertEquals("mean", 0.0, stat.mean(), EPSILON);
    assertEquals("variance", 0.0, stat.variance(), EPSILON);
    assertEquals("stddev", 0.0, stat.stddev(), EPSILON);
    assertEquals("min", SampleStat.MinMax.DEFAULT_MIN_VALUE, stat.min(), EPSILON);
    assertEquals("max", SampleStat.MinMax.DEFAULT_MAX_VALUE, stat.max(), EPSILON);
  }

}
