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
package org.apache.hadoop.metrics2.sink.ganglia;

import org.apache.hadoop.metrics2.MetricCounter;
import org.apache.hadoop.metrics2.MetricGauge;
import org.apache.hadoop.metrics2.MetricsVisitor;
import org.apache.hadoop.metrics2.sink.ganglia.AbstractGangliaSink.GangliaSlope;

/**
 * Since implementations of Metric are not public, hence use a visitor to
 * figure out the type and slope of the metric. Counters have "positive"
 * slope.
 */
class GangliaMetricVisitor implements MetricsVisitor {
  private static final String INT32 = "int32";
  private static final String FLOAT = "float";
  private static final String DOUBLE = "double";

  private String type;
  private GangliaSlope slope;

  /**
   * @return the type of a visited metric
   */
  String getType() {
    return type;
  }

  /**
   * @return the slope of a visited metric. Slope is positive for counters
   * and null for others
   */
  GangliaSlope getSlope() {
    return slope;
  }

  @Override
  public void gauge(MetricGauge<Integer> metric, int value) {
    // MetricGaugeInt.class ==> "int32"
    type = INT32;
    slope = null; // set to null as cannot figure out from Metric
  }

  @Override
  public void counter(MetricCounter<Integer> metric, int value) {
    // MetricCounterInt.class ==> "int32"
    type = INT32;

    // counters have positive slope
    slope = GangliaSlope.positive;
  }

  @Override
  public void gauge(MetricGauge<Long> metric, long value) {
    // MetricGaugeLong.class ==> "float"
    type = FLOAT;
    slope = null; // set to null as cannot figure out from Metric
  }

  @Override
  public void counter(MetricCounter<Long> metric, long value) {
    // MetricCounterLong.class ==> "float"
    type = FLOAT;

    // counters have positive slope
    slope = GangliaSlope.positive;
  }

  @Override
  public void gauge(MetricGauge<Float> metric, float value) {
    // MetricGaugeFloat.class ==> "float"
    type = FLOAT;
    slope = null; // set to null as cannot figure out from Metric
  }

  @Override
  public void gauge(MetricGauge<Double> metric, double value) {
    // MetricGaugeDouble.class ==> "double"
    type = DOUBLE;
    slope = null; // set to null as cannot figure out from Metric
  }
}
