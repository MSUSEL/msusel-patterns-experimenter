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

import org.apache.hadoop.metrics2.Metric;

/**
 * Factory class for mutable metrics
 */
public class MetricMutableFactory {

  static final String DEFAULT_SAMPLE_NAME = "ops";
  static final String DEFAULT_VALUE_NAME  = "time";

  /**
   * Create a new mutable metric by name
   * Usually overridden by app specific factory
   * @param name  of the metric
   * @return  a new metric object
   */
  public MetricMutable newMetric(String name) {
    return null;
  }

  /**
   * Create a mutable integer counter
   * @param name  of the metric
   * @param description of the metric
   * @param initValue of the metric
   * @return  a new metric object
   */
  public MetricMutableCounterInt newCounter(String name, String description,
                                            int initValue) {
    return new MetricMutableCounterInt(name, description, initValue);
  }

  /**
   * Create a mutable integer counter with name only.
   * Usually gets overridden.
   * @param name  of the metric
   * @return  a new metric object
   */
  public MetricMutableCounterInt newCounterInt(String name) {
    return new MetricMutableCounterInt(name, Metric.NO_DESCRIPTION, 0);
  }

  /**
   * Create a mutable long integer counter
   * @param name  of the metric
   * @param description of the metric
   * @param initValue of the metric
   * @return  a new metric object
   */
  public MetricMutableCounterLong newCounter(String name, String description,
                                             long initValue) {
    return new MetricMutableCounterLong(name, description, initValue);
  }

  /**
   * Create a mutable long integer counter with a name
   * Usually gets overridden.
   * @param name  of the metric
   * @return  a new metric object
   */
  public MetricMutableCounterLong newCounterLong(String name) {
    return new MetricMutableCounterLong(name, Metric.NO_DESCRIPTION, 0L);
  }

  /**
   * Create a mutable integer gauge
   * @param name  of the metric
   * @param description of the metric
   * @param initValue of the metric
   * @return  a new metric object
   */
  public MetricMutableGaugeInt newGauge(String name, String description,
                                        int initValue) {
    return new MetricMutableGaugeInt(name, description, initValue);
  }

  /**
   * Create a mutable integer gauge with name only.
   * Usually gets overridden.
   * @param name  of the metric
   * @return  a new metric object
   */
  public MetricMutableGaugeInt newGaugeInt(String name) {
    return new MetricMutableGaugeInt(name, Metric.NO_DESCRIPTION, 0);
  }

  /**
   * Create a mutable long integer gauge
   * @param name  of the metric
   * @param description of the metric
   * @param initValue of the metric
   * @return  a new metric object
   */
  public MetricMutableGaugeLong newGauge(String name, String description,
                                         long initValue) {
    return new MetricMutableGaugeLong(name, description, initValue);
  }

  /**
   * Create a mutable long integer gauge with name only.
   * Usually gets overridden.
   * @param name  of the metric
   * @return  a new metric object
   */
  public MetricMutableGaugeLong newGaugeLong(String name) {
    return new MetricMutableGaugeLong(name, Metric.NO_DESCRIPTION, 0L);
  }

  /**
   * Create a mutable stat metric
   * @param name  of the metric
   * @param description of the metric
   * @param sampleName  of the metric (e.g., ops)
   * @param valueName   of the metric (e.g., time or latency)
   * @param extended    if true, produces extended stat (stdev, min/max etc.)
   * @return  a new metric object
   */
  public MetricMutableStat newStat(String name, String description,
                                   String sampleName, String valueName,
                                   boolean extended) {
    return new MetricMutableStat(name, description, sampleName, valueName,
                                 extended);
  }

  /**
   * Create a mutable stat metric with name only.
   * Usually gets overridden.
   * @param name  of the metric
   * @return  a new metric object
   */
  public MetricMutableStat newStat(String name) {
    return new MetricMutableStat(name, name, DEFAULT_SAMPLE_NAME,
                                 DEFAULT_VALUE_NAME);
  }

}
