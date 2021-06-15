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
package org.apache.hadoop.metrics.spi;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.metrics.spi.AbstractMetricsContext.MetricMap;
import org.apache.hadoop.metrics.spi.AbstractMetricsContext.TagMap;

/**
 * Represents a record of metric data to be sent to a metrics system.
 * @deprecated in favor of <code>org.apache.hadoop.metrics2</code> usage.
 */
@Deprecated
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class OutputRecord {
    
  private TagMap tagMap;
  private MetricMap metricMap;
    
  /** Creates a new instance of OutputRecord */
  OutputRecord(TagMap tagMap, MetricMap metricMap) {
    this.tagMap = tagMap;
    this.metricMap = metricMap;
  }
    
  /**
   * Returns the set of tag names
   */
  public Set<String> getTagNames() {
    return Collections.unmodifiableSet(tagMap.keySet());
  }
    
  /**
   * Returns a tag object which is can be a String, Integer, Short or Byte.
   *
   * @return the tag value, or null if there is no such tag
   */
  public Object getTag(String name) {
    return tagMap.get(name);
  }
    
  /**
   * Returns the set of metric names.
   */
  public Set<String> getMetricNames() {
    return Collections.unmodifiableSet(metricMap.keySet());
  }
    
  /**
   * Returns the metric object which can be a Float, Integer, Short or Byte.
   */
  public Number getMetric(String name) {
    return metricMap.get(name);
  }
  

  /**
   * Returns a copy of this record's tags.
   */
  public TagMap getTagsCopy() {
    return new TagMap(tagMap);
  }
  
  /**
   * Returns a copy of this record's metrics.
   */
  public MetricMap getMetricsCopy() {
    return new MetricMap(metricMap);
  }
}
