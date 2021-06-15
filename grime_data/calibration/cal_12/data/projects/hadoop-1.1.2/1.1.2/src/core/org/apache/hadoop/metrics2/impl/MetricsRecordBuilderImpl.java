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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.hadoop.metrics2.Metric;
import org.apache.hadoop.metrics2.MetricsFilter;
import org.apache.hadoop.metrics2.MetricsRecordBuilder;
import org.apache.hadoop.metrics2.MetricsTag;
import static org.apache.hadoop.metrics2.lib.MetricsRegistry.*;

class MetricsRecordBuilderImpl extends MetricsRecordBuilder {
  private final long timestamp;
  private final String name;
  private final List<Metric> metrics;
  private final List<MetricsTag> tags;
  private final MetricsFilter recordFilter, metricFilter;
  private final boolean acceptable;

  MetricsRecordBuilderImpl(String name, MetricsFilter rf, MetricsFilter mf,
                           boolean acceptable) {
    timestamp = System.currentTimeMillis();
    this.name = name;
    metrics = new ArrayList<Metric>();
    tags = new ArrayList<MetricsTag>();
    recordFilter = rf;
    metricFilter = mf;
    this.acceptable = acceptable;
  }

  @Override
  public MetricsRecordBuilder tag(String name, String description,
                                  String value) {
    if (acceptable) {
      tags.add(new MetricsTag(name, description, value));
    }
    return this;
  }

  @Override
  public MetricsRecordBuilder addCounter(String name, String description,
                                         int value) {
    if (acceptable && (metricFilter == null || metricFilter.accepts(name))) {
      metrics.add(new MetricCounterInt(name, description, value));
    }
    return this;
  }

  @Override
  public MetricsRecordBuilder addCounter(String name, String description,
                                         long value) {
    if (acceptable && (metricFilter == null || metricFilter.accepts(name))) {
      metrics.add(new MetricCounterLong(name, description, value));
    }
    return this;
  }

  @Override
  public MetricsRecordBuilder addGauge(String name, String description,
                                       int value) {
    if (acceptable && (metricFilter == null || metricFilter.accepts(name))) {
      metrics.add(new MetricGaugeInt(name, description, value));
    }
    return this;
  }

  @Override
  public MetricsRecordBuilder addGauge(String name, String description,
                                       long value) {
    if (acceptable && (metricFilter == null || metricFilter.accepts(name))) {
      metrics.add(new MetricGaugeLong(name, description, value));
    }
    return this;
  }

  @Override
  public MetricsRecordBuilder addGauge(String name, String description,
                                       float value) {
    if (acceptable && (metricFilter == null || metricFilter.accepts(name))) {
      metrics.add(new MetricGaugeFloat(name, description, value));
    }
    return this;
  }

  @Override
  public MetricsRecordBuilder addGauge(String name, String description,
                                       double value) {
    if (acceptable && (metricFilter == null || metricFilter.accepts(name))) {
      metrics.add(new MetricGaugeDouble(name, description, value));
    }
    return this;
  }

  @Override
  public MetricsRecordBuilder add(MetricsTag tag) {
    tags.add(tag);
    return this;
  }

  @Override
  public MetricsRecordBuilder add(Metric metric) {
    metrics.add(metric);
    return this;
  }

  @Override
  public MetricsRecordBuilder setContext(String value) {
    return tag(CONTEXT_KEY, CONTEXT_DESC, value);
  }

  public MetricsRecordImpl getRecord() {
    if (acceptable && (recordFilter == null || recordFilter.accepts(tags))) {
      return new MetricsRecordImpl(name, timestamp, tags(), metrics());
    }
    return null;
  }

  List<MetricsTag> tags() {
    return Collections.unmodifiableList(tags);
  }

  List<Metric> metrics() {
    return Collections.unmodifiableList(metrics);
  }

}
