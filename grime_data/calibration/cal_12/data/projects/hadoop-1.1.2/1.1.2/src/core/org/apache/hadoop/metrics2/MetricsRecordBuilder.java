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
 * The metrics record builder interface
 */
public abstract class MetricsRecordBuilder {

  /**
   * Add a metrics tag
   * @param name  of the tag
   * @param description of the tag
   * @param value of the tag
   * @return  self
   */
  public abstract MetricsRecordBuilder tag(String name, String description,
                                           String value);

  /**
   * Add an immutable metrics tag object
   * @param tag a pre-made tag object (potentially save an object construction)
   * @return  self
   */
  public abstract MetricsRecordBuilder add(MetricsTag tag);

  /**
   * Set the context tag
   * @param value of the context
   * @return  self
   */
  public abstract MetricsRecordBuilder setContext(String value);

  /**
   * Add an int counter metric
   * @param name  of the metric
   * @param description of the metric
   * @param value of the metric
   * @return  self
   */
  public abstract MetricsRecordBuilder addCounter(String name,
                                                  String description,
                                                  int value);

  /**
   * Add an long counter metric
   * @param name  of the metric
   * @param description of the metric
   * @param value of the metric
   * @return  self
   */
  public abstract MetricsRecordBuilder addCounter(String name,
                                                  String description,
                                                  long value);

  /**
   * Add a int gauge metric
   * @param name  of the metric
   * @param description of the metric
   * @param value of the metric
   * @return  self
   */
  public abstract MetricsRecordBuilder addGauge(String name,
                                                String description, int value);

  /**
   * Add a long gauge metric
   * @param name  of the metric
   * @param description of the metric
   * @param value of the metric
   * @return  self
   */
  public abstract MetricsRecordBuilder addGauge(String name,
                                                String description, long value);

  /**
   * Add a float gauge metric
   * @param name  of the metric
   * @param description of the metric
   * @param value of the metric
   * @return  self
   */
  public abstract MetricsRecordBuilder addGauge(String name,
                                                String description,
                                                float value);

  /**
   * Add a double gauge metric
   * @param name  of the metric
   * @param description of the metric
   * @param value of the metric
   * @return  self
   */
  public abstract MetricsRecordBuilder addGauge(String name,
                                                String description,
                                                double value);

  /**
   * Add a pre-made immutable metric object
   * @param metric  the pre-made metric to save an object construction
   * @return  self
   */
  public abstract MetricsRecordBuilder add(Metric metric);

}
