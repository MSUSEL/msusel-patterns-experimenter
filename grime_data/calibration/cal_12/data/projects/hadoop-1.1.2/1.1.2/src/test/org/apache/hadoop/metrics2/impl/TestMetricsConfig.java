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

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.configuration.Configuration;

import static org.apache.hadoop.metrics2.impl.ConfigUtil.*;

/**
 * Test metrics configuration
 */
public class TestMetricsConfig {

  static final Log LOG = LogFactory.getLog(TestMetricsConfig.class);

  /**
   * Common use cases
   * @throws Exception
   */
  @Test public void testCommon() throws Exception {
    String filename = getTestFilename("test-metrics2");
    new ConfigBuilder()
        .add("*.foo", "default foo")
        .add("p1.*.bar", "p1 default bar")
        .add("p1.t1.*.bar", "p1.t1 default bar")
        .add("p1.t1.i1.name", "p1.t1.i1.name")
        .add("p1.t1.42.bar", "p1.t1.42.bar")
        .add("p1.t2.i1.foo", "p1.t2.i1.foo")
        .add("p2.*.foo", "p2 default foo")
        .save(filename);

    MetricsConfig mc = MetricsConfig.create("p1", filename);
    dump("mc:", mc);

    Configuration expected = new ConfigBuilder()
        .add("*.bar", "p1 default bar")
        .add("t1.*.bar", "p1.t1 default bar")
        .add("t1.i1.name", "p1.t1.i1.name")
        .add("t1.42.bar", "p1.t1.42.bar")
        .add("t2.i1.foo", "p1.t2.i1.foo")
        .config;

    assertEq(expected, mc);

    testInstances(mc);
  }

  private void testInstances(MetricsConfig c) throws Exception {
    Map<String, MetricsConfig> map = c.getInstanceConfigs("t1");
    Map<String, MetricsConfig> map2 = c.getInstanceConfigs("t2");

    assertEquals("number of t1 instances", 2, map.size());
    assertEquals("number of t2 instances", 1, map2.size());
    assertTrue("contains t1 instance i1", map.containsKey("i1"));
    assertTrue("contains t1 instance 42", map.containsKey("42"));
    assertTrue("contains t2 instance i1", map2.containsKey("i1"));

    MetricsConfig t1i1 = map.get("i1");
    MetricsConfig t1i42 = map.get("42");
    MetricsConfig t2i1 = map2.get("i1");
    dump("--- t1 instance i1:", t1i1);
    dump("--- t1 instance 42:", t1i42);
    dump("--- t2 instance i1:", t2i1);

    Configuration t1expected1 = new ConfigBuilder()
        .add("name", "p1.t1.i1.name").config;
    Configuration t1expected42 = new ConfigBuilder()
         .add("bar", "p1.t1.42.bar").config;
    Configuration t2expected1 = new ConfigBuilder()
        .add("foo", "p1.t2.i1.foo").config;

    assertEq(t1expected1, t1i1);
    assertEq(t1expected42, t1i42);
    assertEq(t2expected1, t2i1);

    LOG.debug("asserting foo == default foo");
    // Check default lookups
    assertEquals("value of foo in t1 instance i1", "default foo",
                 t1i1.getString("foo"));
    assertEquals("value of bar in t1 instance i1", "p1.t1 default bar",
                 t1i1.getString("bar"));
    assertEquals("value of foo in t1 instance 42", "default foo",
                 t1i42.getString("foo"));
    assertEquals("value of foo in t2 instance i1", "p1.t2.i1.foo",
                 t2i1.getString("foo"));
    assertEquals("value of bar in t2 instance i1", "p1 default bar",
                 t2i1.getString("bar"));
  }

  /**
   * Test the config file load order
   * @throws Exception
   */
  @Test public void testLoadFirst() throws Exception {
    String filename = getTestFilename("hadoop-metrics2-p1");
    new ConfigBuilder().add("p1.foo", "p1foo").save(filename);

    MetricsConfig mc = MetricsConfig.create("p1");
    MetricsConfig mc2 = MetricsConfig.create("p1", "na1", "na2", filename);
    Configuration expected = new ConfigBuilder().add("foo", "p1foo").config;

    assertEq(expected, mc);
    assertEq(expected, mc2);
  }

  /**
   * Return a test filename in the class path
   * @param basename
   * @return the filename
   */
  public static String getTestFilename(String basename) {
    return "build/classes/"+ basename +".properties";
  }

}
