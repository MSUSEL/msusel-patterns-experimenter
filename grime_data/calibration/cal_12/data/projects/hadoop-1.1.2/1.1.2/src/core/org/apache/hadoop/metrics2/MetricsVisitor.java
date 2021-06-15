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
package org.apache.hadoop.metrics2;

/**
 * A visitor interface for metrics
 */
public interface MetricsVisitor {

  /**
   * Callback for int value gauges
   * @param metric the metric object
   * @param value of the metric
   */
  public void gauge(MetricGauge<Integer> metric, int value);

  /**
   * Callback for long value gauges
   * @param metric the metric object
   * @param value of the metric
   */
  public void gauge(MetricGauge<Long> metric, long value);

  /**
   * Callback for float value gauges
   * @param metric the metric object
   * @param value of the metric
   */
  public void gauge(MetricGauge<Float> metric, float value);

  /**
   * Callback for double value gauges
   * @param metric the metric object
   * @param value of the metric
   */
  public void gauge(MetricGauge<Double> metric, double value);

  /**
   * Callback for integer value counters
   * @param metric the metric object
   * @param value of the metric
   */
  public void counter(MetricCounter<Integer> metric, int value);

  /**
   * Callback for long value counters
   * @param metric the metric object
   * @param value of the metric
   */
  public void counter(MetricCounter<Long> metric, long value);

}
