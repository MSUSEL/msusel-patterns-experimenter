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
package org.apache.hadoop.metrics2.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.metrics2.Metric;
import org.apache.hadoop.metrics2.MetricsRecord;
import org.apache.hadoop.metrics2.MetricsTag;

/**
 * A metrics cache for sinks that don't support sparse updates.
 */
public class MetricsCache {

  private static final long serialVersionUID = 1L;
  private final Map<String, RecMap> map = new HashMap<String, RecMap>();

  static class RecMap extends HashMap<Collection<MetricsTag>, Record> {
    private static final long serialVersionUID = 1L;
  }

  /**
   * Cached record
   */
  public static class Record {
    final Map<String, String> tags = new LinkedHashMap<String, String>();
    final Map<String, Metric> metrics = new LinkedHashMap<String, Metric>();

    /**
     * Get the tag value
     * @param key name of the tag
     * @return the tag value
     */
    public String getTag(String key) {
      return tags.get(key);
    }

    /**
     * Get the metric value
     * @param key name of the metric
     * @return the metric value
     */
    public Number getMetric(String key) {
      Metric metric = metrics.get(key);
      return metric != null ? metric.value() : null;
    }

    /**
     * Get the metric value
     * @param key name of the metric
     * @return the metric value
     */
    public Metric getMetricInstance(String key) {
      return metrics.get(key);
    }

    /**
     * @return entry set of metrics
     */
    public Set<Map.Entry<String, Number>> metrics() {
      Map<String, Number> map =
        new LinkedHashMap<String,Number>(metrics.size());
      for (Map.Entry<String, Metric> mapEntry : metrics.entrySet()) {
        map.put(mapEntry.getKey(), mapEntry.getValue().value());
      }
      return map.entrySet();
    }

    /**
     * @return entry set of metrics
     */
    public Set<Map.Entry<String, Metric>> metricsEntrySet() {
      return metrics.entrySet();
    }
  }

  /**
   * Update the cache and return the cached record
   * @param mr the update record
   * @param includingTags cache tag values (for later lookup by name) if true
   * @return the updated cached record
   */
  public Record update(MetricsRecord mr, boolean includingTags) {
    String name = mr.name();
    RecMap recMap = map.get(name);
    if (recMap == null) {
      recMap = new RecMap();
      map.put(name, recMap);
    }
    Collection<MetricsTag> tags = (Collection<MetricsTag>)mr.tags();
    Record rec = recMap.get(tags);
    if (rec == null) {
      rec = new Record();
      recMap.put(tags, rec);
    }
    for (Metric m : mr.metrics()) {
      rec.metrics.put(m.name(), m);
    }
    if (includingTags) {
      // mostly for some sinks that include tags as part of a dense schema
      for (MetricsTag t : mr.tags()) {
        rec.tags.put(t.name(), t.value());
      }
    }
    return rec;
  }

  public Record update(MetricsRecord mr) {
    return update(mr, false);
  }

  /**
   * Get the cached record
   * @param name of the record
   * @param tags of the record
   * @return the cached record or null
   */
  public Record get(String name, Collection<MetricsTag> tags) {
    RecMap tmap = map.get(name);
    if (tmap == null) return null;
    return tmap.get(tags);
  }
}
