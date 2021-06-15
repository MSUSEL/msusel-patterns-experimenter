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

import org.apache.hadoop.metrics2.MetricsRecordBuilder;

/**
 * A mutable long gauge
 */
public class MetricMutableGaugeLong extends MetricMutableGauge<Long> {

  private volatile long value;

  /**
   * Construct a mutable long gauge metric
   * @param name  of the gauge
   * @param description of the gauge
   * @param initValue the initial value of the gauge
   */
  public MetricMutableGaugeLong(String name, String description,
                                long initValue) {
    super(name, description);
    this.value = initValue;
  }

  public synchronized void incr() {
    ++value;
    setChanged();
  }

  /**
   * Increment by delta
   * @param delta of the increment
   */
  public synchronized void incr(long delta) {
    value += delta;
    setChanged();
  }

  public synchronized void decr() {
    --value;
    setChanged();
  }

  /**
   * decrement by delta
   * @param delta of the decrement
   */
  public synchronized void decr(long delta) {
    value -= delta;
    setChanged();
  }

  /**
   * Set the value of the metric
   * @param value to set
   */
  public void set(long value) {
    this.value = value;
    setChanged();
  }

  public void snapshot(MetricsRecordBuilder builder, boolean all) {
    if (all || changed()) {
      builder.addGauge(name, description, value);
      clearChanged();
    }
  }

}
