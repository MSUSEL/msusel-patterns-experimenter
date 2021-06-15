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

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.hadoop.metrics2.Metric;
import org.apache.hadoop.metrics2.MetricsRecord;
import org.apache.hadoop.metrics2.MetricsTag;

public class TestMetricsCache {
  private static final Log LOG = LogFactory.getLog(TestMetricsCache.class);

  @Test public void testUpdate() {
    MetricsCache cache = new MetricsCache();
    MetricsRecord mr = makeRecord("r",
        Arrays.asList(makeTag("t", "tv")),
        Arrays.asList(makeMetric("m", 0), makeMetric("m1", 1)));

    MetricsCache.Record cr = cache.update(mr);
    verify(mr).name();
    verify(mr).tags();
    verify(mr).metrics();
    assertEquals("same record size", cr.metrics.size(),
                 ((Collection<Metric>)mr.metrics()).size());
    assertEquals("same metric value", 0, cr.getMetric("m"));
    assertNotNull("metric not null", cr.getMetricInstance("m"));
    assertEquals("new metric value", 0, cr.getMetricInstance("m").value());


    MetricsRecord mr2 = makeRecord("r",
        Arrays.asList(makeTag("t", "tv")),
        Arrays.asList(makeMetric("m", 2), makeMetric("m2", 42)));
    cr = cache.update(mr2);
    assertEquals("contains 3 metric", 3, cr.metrics.size());
    assertEquals("updated metric value", 2, cr.getMetric("m"));
    assertNotNull("metric not null", cr.getMetricInstance("m"));
    assertEquals("new metric value", 2, cr.getMetricInstance("m").value());

    assertEquals("old metric value", 1, cr.getMetric("m1"));
    assertNotNull("metric not null", cr.getMetricInstance("m1"));
    assertEquals("new metric value", 1, cr.getMetricInstance("m1").value());

    assertEquals("new metric value", 42, cr.getMetric("m2"));
    assertNotNull("metric not null", cr.getMetricInstance("m2"));
    assertEquals("new metric value", 42, cr.getMetricInstance("m2").value());

    MetricsRecord mr3 = makeRecord("r",
        Arrays.asList(makeTag("t", "tv3")), // different tag value
        Arrays.asList(makeMetric("m3", 3)));
    cr = cache.update(mr3); // should get a new record
    assertEquals("contains 1 metric", 1, cr.metrics.size());
    assertEquals("updated metric value", 3, cr.getMetric("m3"));
    assertNotNull("metric not null", cr.getMetricInstance("m3"));
    assertEquals("new metric value", 3, cr.getMetricInstance("m3").value());

    // tags cache should be empty so far
    assertEquals("no tags", 0, cr.tags.size());
    // until now
    cr = cache.update(mr3, true);
    assertEquals("Got 1 tag", 1, cr.tags.size());
    assertEquals("Tag value", "tv3", cr.getTag("t"));
    assertEquals("Metric value", 3, cr.getMetric("m3"));
    assertNotNull("metric not null", cr.getMetricInstance("m3"));
    assertEquals("new metric value", 3, cr.getMetricInstance("m3").value());
  }

  @Test public void testGet() {
    MetricsCache cache = new MetricsCache();
    assertNull("empty", cache.get("r", Arrays.asList(makeTag("t", "t"))));
    MetricsRecord mr = makeRecord("r",
        Arrays.asList(makeTag("t", "t")),
        Arrays.asList(makeMetric("m", 1)));
    cache.update(mr);
    MetricsCache.Record cr = cache.get("r", (Collection<MetricsTag>)mr.tags());
    LOG.debug("tags="+ (Collection<MetricsTag>)mr.tags() +" cr="+ cr);

    assertNotNull("Got record", cr);
    assertEquals("contains 1 metric", 1, cr.metrics.size());
    assertEquals("new metric value", 1, cr.getMetric("m"));
    assertNotNull("metric not null", cr.getMetricInstance("m"));
    assertEquals("new metric value", 1, cr.getMetricInstance("m").value());
  }

  private MetricsRecord makeRecord(String name, Collection<MetricsTag> tags,
                                   Collection<Metric> metrics) {
    MetricsRecord mr = mock(MetricsRecord.class);
    when(mr.name()).thenReturn(name);
    when(mr.tags()).thenReturn(tags);
    when(mr.metrics()).thenReturn(metrics);
    return mr;
  }

  private MetricsTag makeTag(String name, String value) {
    return new MetricsTag(name, "", value);
  }

  private Metric makeMetric(String name, Number value) {
    Metric metric = mock(Metric.class);
    when(metric.name()).thenReturn(name);
    when(metric.value()).thenReturn(value);
    return metric;
  }
}
