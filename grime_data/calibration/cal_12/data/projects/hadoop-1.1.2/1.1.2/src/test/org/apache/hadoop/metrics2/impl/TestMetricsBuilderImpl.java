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

import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.configuration.SubsetConfiguration;
import static org.apache.hadoop.metrics2.filter.TestPatternFilter.*;

public class TestMetricsBuilderImpl {

  @Test public void recordBuilderShouldNoOpIfFiltered() {
    SubsetConfiguration fc = new ConfigBuilder()
        .add("p.exclude", "foo").subset("p");
    MetricsBuilderImpl mb = new MetricsBuilderImpl();
    mb.setRecordFilter(newGlobFilter(fc));
    MetricsRecordBuilderImpl rb = mb.addRecord("foo");
    rb.tag("foo", "", "value").addGauge("g0", "", 1);
    assertEquals("no tags", 0, rb.tags().size());
    assertEquals("no metrics", 0, rb.metrics().size());
    assertNull("null record", rb.getRecord());
    assertEquals("no records", 0, mb.getRecords().size());
  }

  @Test public void testPerMetricFiltering() {
    SubsetConfiguration fc = new ConfigBuilder()
        .add("p.exclude", "foo").subset("p");
    MetricsBuilderImpl mb = new MetricsBuilderImpl();
    mb.setMetricFilter(newGlobFilter(fc));
    MetricsRecordBuilderImpl rb = mb.addRecord("foo");
    rb.tag("foo", "", "").addCounter("c0", "", 0).addGauge("foo", "", 1);
    assertEquals("1 tag", 1, rb.tags().size());
    assertEquals("1 metric", 1, rb.metrics().size());
    assertEquals("expect foo tag", "foo", rb.tags().get(0).name());
    assertEquals("expect c0", "c0", rb.metrics().get(0).name());
  }
}
