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
package org.apache.hadoop.metrics2.impl;

import org.apache.hadoop.metrics2.Metric;
import org.apache.hadoop.metrics2.MetricsRecord;
import org.apache.hadoop.metrics2.MetricsTag;
import org.apache.hadoop.metrics2.util.Contracts;

public class MetricsRecordImpl implements MetricsRecord {

  protected static final String CONTEXT_KEY = "context";
  protected static final String DEFAULT_CONTEXT = "default";

  private final long timestamp;
  private final String name;
  private final Iterable<MetricsTag> tags;
  private final Iterable<Metric> metrics;

  /**
   * Construct a metrics record
   * @param name  of the record
   * @param timestamp of the record
   * @param tags  of the record
   * @param metrics of the record
   */
  public MetricsRecordImpl(String name, long timestamp,
                           Iterable<MetricsTag> tags,
                           Iterable<Metric> metrics) {
    this.timestamp = Contracts.checkArg(timestamp, timestamp > 0, "timestamp");
    this.name = Contracts.checkNotNull(name, "name");
    this.tags = Contracts.checkNotNull(tags, "tags");
    this.metrics = Contracts.checkNotNull(metrics, "metrics");
  }

  public long timestamp() {
    return timestamp;
  }

  public String name() {
    return name;
  }

  public String context() {
    // usually the first tag
    for (MetricsTag t : tags) {
      if (t.name().equals(CONTEXT_KEY)) {
        return String.valueOf(t.value());
      }
    }
    return DEFAULT_CONTEXT;
  }

  public Iterable<MetricsTag> tags() {
    return tags;
  }

  public Iterable<Metric> metrics() {
    return metrics;
  }

  // Mostly for testing
  @Override public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final MetricsRecordImpl other = (MetricsRecordImpl) obj;
    if (this.timestamp != other.timestamp()) {
      return false;
    }
    if (!this.name.equals(other.name())) {
      return false;
    }
    if (!this.tags.equals(other.tags())) {
      return false;
    }
    if (!this.metrics.equals(other.metrics())) {
      return false;
    }
    return true;
  }

  @Override public int hashCode() {
    return name.hashCode();
  }

  @Override public String toString() {
    return "MetricsRecordImpl{" + "timestamp=" + timestamp + " name='" + name +
        "' tags=" + tags + " metrics=" + metrics + "}\n";
  }

}
