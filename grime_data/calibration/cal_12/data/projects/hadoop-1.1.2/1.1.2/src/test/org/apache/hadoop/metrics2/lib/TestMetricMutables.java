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
package org.apache.hadoop.metrics2.lib;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.mockito.AdditionalMatchers.*;

import org.apache.hadoop.metrics2.MetricsRecordBuilder;


/**
 * Test metrics record builder interface and mutable metrics
 */
public class TestMetricMutables {

  private final double EPSILON = 1e-42;

  /**
   * Test the snapshot method
   */
  @Test public void testSnapshot() {
    MetricsRecordBuilder mb = mock(MetricsRecordBuilder.class);
    MetricMutableStat stat =
        new MetricMutableStat("s1", "stat", "ops", "time", true);
    stat.add(0);
    MetricMutableStat stat2 =
        new MetricMutableStat("s2", "stat", "ops", "time");
    stat2.add(0);
    List<MetricMutable> metrics = Arrays.asList(
        new MetricMutableCounterInt("c1", "int counter", 1),
        new MetricMutableCounterLong("c2", "long counter", 2L),
        new MetricMutableGaugeInt("g1", "int gauge", 3),
        new MetricMutableGaugeLong("g2", "long gauge", 4L),
        stat, stat2);

    for (MetricMutable metric : metrics) {
      metric.snapshot(mb, true);
    }
    stat2.snapshot(mb, true); // should get the same back.
    stat2.add(1);
    stat2.snapshot(mb, true); // should get new interval values back

    verify(mb).addCounter("c1", "int counter", 1);
    verify(mb).addCounter("c2", "long counter", 2L);
    verify(mb).addGauge("g1", "int gauge", 3);
    verify(mb).addGauge("g2", "long gauge", 4L);
    verify(mb).addCounter("s1_num_ops", "Number of ops for stat", 1L);
    verify(mb).addGauge(eq("s1_avg_time"), eq("Average time for stat"),
                           eq(0.0, EPSILON));
    verify(mb).addGauge(eq("s1_stdev_time"),
                           eq("Standard deviation of time for stat"),
                           eq(0.0, EPSILON));
    verify(mb).addGauge(eq("s1_imin_time"),
                           eq("Interval min time for stat"),
                           eq(0.0, EPSILON));
    verify(mb).addGauge(eq("s1_imax_time"),
                           eq("Interval max time for stat"),
                           eq(0.0, EPSILON));
    verify(mb).addGauge(eq("s1_min_time"), eq("Min time for stat"),
                           eq(0.0, EPSILON));
    verify(mb).addGauge(eq("s1_max_time"), eq("Max time for stat"),
                           eq(0.0, EPSILON));
    verify(mb, times(2)).addCounter("s2_num_ops", "Number of ops for stat", 1L);
    verify(mb, times(2)).addGauge(eq("s2_avg_time"),
                                  eq("Average time for stat"),
                                  eq(0.0, EPSILON));
    verify(mb).addCounter("s2_num_ops", "Number of ops for stat", 2L);
    verify(mb).addGauge(eq("s2_avg_time"), eq("Average time for stat"),
                           eq(1.0, EPSILON));
  }

}
