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
package org.apache.hadoop.metrics2.filter;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.SubsetConfiguration;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.hadoop.metrics2.MetricsTag;
import org.apache.hadoop.metrics2.impl.ConfigBuilder;

public class TestPatternFilter {

  /**
   * Filters should default to accept
   */
  @Test public void emptyConfigShouldAccept() {
    SubsetConfiguration empty = new ConfigBuilder().subset("");
    shouldAccept(empty, "anything");
    shouldAccept(empty, Arrays.asList(new MetricsTag("key", "desc", "value")));
  }

  /**
   * Filters should handle white-listing correctly
   */
  @Test public void includeOnlyShouldOnlyIncludeMatched() {
    SubsetConfiguration wl = new ConfigBuilder()
        .add("p.include", "foo")
        .add("p.include.tags", "foo:f").subset("p");
    shouldAccept(wl, "foo");
    shouldAccept(wl, Arrays.asList(new MetricsTag("bar", "", ""),
                                   new MetricsTag("foo", "", "f")));
    shouldReject(wl, "bar");
    shouldReject(wl, Arrays.asList(new MetricsTag("bar", "", "")));
    shouldReject(wl, Arrays.asList(new MetricsTag("foo", "", "boo")));
  }

  /**
   * Filters should handle black-listing correctly
   */
  @Test public void excludeOnlyShouldOnlyExcludeMatched() {
    SubsetConfiguration bl = new ConfigBuilder()
        .add("p.exclude", "foo")
        .add("p.exclude.tags", "foo:f").subset("p");
    shouldAccept(bl, "bar");
    shouldAccept(bl, Arrays.asList(new MetricsTag("bar", "", "")));
    shouldReject(bl, "foo");
    shouldReject(bl, Arrays.asList(new MetricsTag("bar", "", ""),
                                   new MetricsTag("foo", "", "f")));
  }

  /**
   * Filters should accepts unmatched item when both include and
   * exclude patterns are present.
   */
  @Test public void shouldAcceptUnmatchedWhenBothAreConfigured() {
    SubsetConfiguration c = new ConfigBuilder()
        .add("p.include", "foo")
        .add("p.include.tags", "foo:f")
        .add("p.exclude", "bar")
        .add("p.exclude.tags", "bar:b").subset("p");
    shouldAccept(c, "foo");
    shouldAccept(c, Arrays.asList(new MetricsTag("foo", "", "f")));
    shouldReject(c, "bar");
    shouldReject(c, Arrays.asList(new MetricsTag("bar", "", "b")));
    shouldAccept(c, "foobar");
    shouldAccept(c, Arrays.asList(new MetricsTag("foobar", "", "")));
  }

  /**
   * Include patterns should take precedence over exclude patterns
   */
  @Test public void includeShouldOverrideExclude() {
    SubsetConfiguration c = new ConfigBuilder()
        .add("p.include", "foo")
        .add("p.include.tags", "foo:f")
        .add("p.exclude", "foo")
        .add("p.exclude.tags", "foo:f").subset("p");
    shouldAccept(c, "foo");
    shouldAccept(c, Arrays.asList(new MetricsTag("foo", "", "f")));
  }

  static void shouldAccept(SubsetConfiguration conf, String s) {
    assertTrue("accepts "+ s, newGlobFilter(conf).accepts(s));
    assertTrue("accepts "+ s, newRegexFilter(conf).accepts(s));
  }

  static void shouldAccept(SubsetConfiguration conf, List<MetricsTag> tags) {
    assertTrue("accepts "+ tags, newGlobFilter(conf).accepts(tags));
    assertTrue("accepts "+ tags, newRegexFilter(conf).accepts(tags));
  }

  static void shouldReject(SubsetConfiguration conf, String s) {
    assertTrue("rejects "+ s, !newGlobFilter(conf).accepts(s));
    assertTrue("rejects "+ s, !newRegexFilter(conf).accepts(s));
  }

  static void shouldReject(SubsetConfiguration conf, List<MetricsTag> tags) {
    assertTrue("rejects "+ tags, !newGlobFilter(conf).accepts(tags));
    assertTrue("rejects "+ tags, !newRegexFilter(conf).accepts(tags));
  }

  /**
   * Create a new glob filter with a config object
   * @param conf  the config object
   * @return the filter
   */
  public static GlobFilter newGlobFilter(SubsetConfiguration conf) {
    GlobFilter f = new GlobFilter();
    f.init(conf);
    return f;
  }

  /**
   * Create a new regex filter with a config object
   * @param conf  the config object
   * @return the filter
   */
  public static RegexFilter newRegexFilter(SubsetConfiguration conf) {
    RegexFilter f = new RegexFilter();
    f.init(conf);
    return f;
  }
}
