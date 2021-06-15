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
package org.apache.hadoop.metrics.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.classification.InterfaceAudience;

/**
 * 
 * This is the registry for metrics.
 * Related set of metrics should be declared in a holding class and registered
 * in a registry for those metrics which is also stored in the the holding class.
 *
 * @deprecated in favor of {@link org.apache.hadoop.metrics2.lib.MetricsRegistry}.
 */
@Deprecated
@InterfaceAudience.LimitedPrivate({"HDFS", "MapReduce"})
public class MetricsRegistry {
  private Map<String, MetricsBase> metricsList = new HashMap<String, MetricsBase>();

  public MetricsRegistry() {
  }
  
  /**
   * 
   * @return number of metrics in the registry
   */
  public int size() {
    return metricsList.size();
  }
  
  /**
   * Add a new metrics to the registry
   * @param metricsName - the name
   * @param theMetricsObj - the metrics
   * @throws IllegalArgumentException if a name is already registered
   */
  public synchronized void add(final String metricsName, final MetricsBase theMetricsObj) {
    if (metricsList.containsKey(metricsName)) {
      throw new IllegalArgumentException("Duplicate metricsName:" + metricsName);
    }
    metricsList.put(metricsName, theMetricsObj);
  }

  
  /**
   * 
   * @param metricsName
   * @return the metrics if there is one registered by the supplied name.
   *         Returns null if none is registered
   */
  public synchronized MetricsBase get(final String metricsName) {
    return metricsList.get(metricsName);
  }
  
  
  /**
   * 
   * @return the list of metrics names
   */
  public synchronized Collection<String> getKeyList() {
    return metricsList.keySet();
  }
  
  /**
   * 
   * @return the list of metrics
   */
  public synchronized Collection<MetricsBase> getMetricsList() {
    return metricsList.values();
  }
}
