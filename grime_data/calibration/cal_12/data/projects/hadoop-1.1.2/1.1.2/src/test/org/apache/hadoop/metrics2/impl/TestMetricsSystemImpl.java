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

import org.apache.commons.configuration.SubsetConfiguration;
import org.apache.hadoop.metrics2.lib.MetricMutableCounterLong;
import org.apache.hadoop.metrics2.lib.MetricMutableStat;
import org.apache.hadoop.metrics2.lib.MetricMutableGaugeLong;
import org.apache.hadoop.metrics2.lib.AbstractMetricsSource;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import static org.apache.hadoop.test.MoreAsserts.*;
import org.apache.hadoop.metrics2.Metric;
import org.apache.hadoop.metrics2.MetricsRecord;
import org.apache.hadoop.metrics2.MetricsSink;
import org.apache.hadoop.metrics2.MetricsTag;

/**
 * Test the MetricsSystemImpl class
 */
@RunWith(MockitoJUnitRunner.class)
public class TestMetricsSystemImpl {
  private static final Log LOG = LogFactory.getLog(TestMetricsSystemImpl.class);
  @Captor private ArgumentCaptor<MetricsRecord> r1;
  @Captor private ArgumentCaptor<MetricsRecord> r2;
  @Captor private ArgumentCaptor<MetricsRecord> r3;
  private static String hostname = MetricsSystemImpl.getHostname();

  @Test public void testInitFirst() throws Exception {
    ConfigBuilder cb = new ConfigBuilder().add("default.period", 8)
        .add("source.filter.class",
             "org.apache.hadoop.metrics2.filter.GlobFilter")
        .add("test.*.source.filter.class", "${source.filter.class}")
        .add("test.*.source.filter.exclude", "s1*")
        .add("test.sink.sink3.source.filter.class", "${source.filter.class}")
        .add("test.sink.sink3.source.filter.exclude", "s2*")
        .save(TestMetricsConfig.getTestFilename("hadoop-metrics2-test"));
    MetricsSystemImpl ms = new MetricsSystemImpl("Test");
    ms.start();
    TestSource s1 = ms.register("s1", "s1 desc", new TestSource("s1rec"));
    TestSource s2 = ms.register("s2", "s2 desc", new TestSource("s2rec"));
    TestSource s3 = ms.register("s3", "s3 desc", new TestSource("s3rec"));
    s1.s1.add(0);
    s2.s1.add(0);
    s3.s1.add(0);
    MetricsSink sink1 = mock(MetricsSink.class);
    MetricsSink sink2 = mock(MetricsSink.class);
    MetricsSink sink3 = mock(MetricsSink.class);
    ms.register("sink1", "sink1 desc", sink1);
    ms.register("sink2", "sink2 desc", sink2);
    ms.register("sink3", "sink3 desc", sink3);
    ms.onTimerEvent();  // trigger something interesting
    ms.stop();

    verify(sink1, times(3)).putMetrics(r1.capture()); // 2 + 1 sys source
    List<MetricsRecord> mr1 = r1.getAllValues();
    verify(sink2, times(3)).putMetrics(r2.capture()); // ditto
    List<MetricsRecord> mr2 = r2.getAllValues();
    verify(sink3, times(2)).putMetrics(r3.capture()); // 1 + 1 (s1, s2 filtered)
    List<MetricsRecord> mr3 = r3.getAllValues();
    checkMetricsRecords(mr1, "s2rec");
    assertEquals("output", mr1, mr2);
    checkMetricsRecords(mr3, "s3rec");
  }

  static void checkMetricsRecords(List<MetricsRecord> recs, String expected) {
    LOG.debug(recs);
    MetricsRecord r = recs.get(0);
    assertEquals("name", expected, r.name());
    assertEquals("tags", new MetricsTag[] {
      new MetricsTag("context", "Metrics context", "test"),
      new MetricsTag("hostName", "Local hostname", hostname)}, r.tags());
    assertEquals("metrics", new Metric[] {
      new MetricCounterLong("c1", "c1 desc", 1),
      new MetricGaugeLong("g1", "g1 desc", 2),
      new MetricCounterLong("s1_num_ops", "Number of ops for s1 desc", 1),
      new MetricGaugeDouble("s1_avg_time", "Average time for s1 desc", 0)},
      r.metrics());

    // Skip the system metrics for now.
    // MetricsRecord r1 = recs.get(1);
  }

  private static class TestSource extends AbstractMetricsSource {
    final MetricMutableCounterLong c1;
    final MetricMutableGaugeLong g1;
    final MetricMutableStat s1;

    TestSource(String name) {
      super(name);
      registry.setContext("test");
      c1 = registry.newCounter("c1", "c1 desc", 1L);
      g1 = registry.newGauge("g1", "g1 desc", 2L);
      s1 = registry.newStat("s1", "s1 desc", "ops", "time");
    }
  }

}
