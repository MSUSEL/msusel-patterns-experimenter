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
package org.apache.hadoop.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.mockito.Mockito.*;
import org.mockito.stubbing.Answer;
import org.mockito.invocation.InvocationOnMock;
import static org.mockito.AdditionalMatchers.*;

import org.apache.hadoop.metrics2.MetricsBuilder;
import org.apache.hadoop.metrics2.MetricsSource;
import org.apache.hadoop.metrics2.MetricsRecordBuilder;
import org.apache.hadoop.util.StringUtils;

/**
 * Helpers for metrics source tests
 */
public class MetricsAsserts {

  final static Log LOG = LogFactory.getLog(MetricsAsserts.class);

  /**
   * Call getMetrics on source and get a record builder mock to verify
   * @param source  the metrics source
   * @return the record builder mock to verify
   */
  public static MetricsRecordBuilder getMetrics(MetricsSource source) {
    MetricsBuilder mb = mock(MetricsBuilder.class);
    final MetricsRecordBuilder rb = mock(MetricsRecordBuilder.class,
        new Answer<Object>() {
      @Override
      public Object answer(InvocationOnMock invocation) {
        Object[] args = invocation.getArguments();
        StringBuilder sb = new StringBuilder();
        for (Object o : args) {
          if (sb.length() > 0) sb.append(", ");
          sb.append(String.valueOf(o));
        }
        LOG.debug(invocation.getMethod().getName() +": "+ sb);
        return invocation.getMock();
      }
    });
    when(mb.addRecord(anyString())).thenReturn(rb);
    source.getMetrics(mb, true);
    return rb;
  }

  /**
   * Assert an int gauge metric as expected
   * @param name  of the metric
   * @param expected  value of the metric
   * @param rb  the record builder mock used to getMetrics
   */
  public static void assertGauge(String name, int expected,
                                 MetricsRecordBuilder rb) {
    verify(rb).addGauge(eq(name), anyString(), eq(expected));
  }

  /**
   * Assert an int counter metric as expected
   * @param name  of the metric
   * @param expected  value of the metric
   * @param rb  the record builder mock used to getMetrics
   */
  public static void assertCounter(String name, int expected,
                                   MetricsRecordBuilder rb) {
    verify(rb).addCounter(eq(name), anyString(), eq(expected));
  }

  /**
   * Assert a long gauge metric as expected
   * @param name  of the metric
   * @param expected  value of the metric
   * @param rb  the record builder mock used to getMetrics
   */
  public static void assertGauge(String name, long expected,
                                 MetricsRecordBuilder rb) {
    verify(rb).addGauge(eq(name), anyString(), eq(expected));
  }

  /**
   * Assert a long counter metric as expected
   * @param name  of the metric
   * @param expected  value of the metric
   * @param rb  the record builder mock used to getMetrics
   */
  public static void assertCounter(String name, long expected,
                                   MetricsRecordBuilder rb) {
    verify(rb).addCounter(eq(name), anyString(), eq(expected));
  }

  /**
   * Assert an int gauge metric as expected
   * @param name  of the metric
   * @param expected  value of the metric
   * @param source  to get metrics from
   */
  public static void assertGauge(String name, int expected,
                                 MetricsSource source) {
    assertGauge(name, expected, getMetrics(source));
  }

  /**
   * Assert an int counter metric as expected
   * @param name  of the metric
   * @param expected  value of the metric
   * @param source  to get metrics from
   */
  public static void assertCounter(String name, int expected,
                                   MetricsSource source) {
    assertCounter(name, expected, getMetrics(source));
  }

  /**
   * Assert a long gauge metric as expected
   * @param name  of the metric
   * @param expected  value of the metric
   * @param source  to get metrics from
   */
  public static void assertGauge(String name, long expected,
                                 MetricsSource source) {
    assertGauge(name, expected, getMetrics(source));
  }

  /**
   * Assert a long counter metric as expected
   * @param name  of the metric
   * @param expected  value of the metric
   * @param source  to get metrics from
   */
  public static void assertCounter(String name, long expected,
                                   MetricsSource source) {
    assertCounter(name, expected, getMetrics(source));
  }

  /**
   * Assert that a long counter metric is greater than a value
   * @param name  of the metric
   * @param greater value of the metric should be greater than this
   * @param rb  the record builder mock used to getMetrics
   */
  public static void assertCounterGt(String name, long greater,
                                     MetricsRecordBuilder rb) {
    verify(rb).addCounter(eq(name), anyString(), gt(greater));
  }

  /**
   * Assert that a long counter metric is greater than a value
   * @param name  of the metric
   * @param greater value of the metric should be greater than this
   * @param source  the metrics source
   */
  public static void assertCounterGt(String name, long greater,
                                     MetricsSource source) {
    assertCounterGt(name, greater, getMetrics(source));
  }

  /**
   * Assert that a double gauge metric is greater than a value
   * @param name  of the metric
   * @param greater value of the metric should be greater than this
   * @param rb  the record builder mock used to getMetrics
   */
  public static void assertGaugeGt(String name, double greater,
                                   MetricsRecordBuilder rb) {
    verify(rb).addGauge(eq(name), anyString(), gt(greater));
  }

  /**
   * Assert that a double gauge metric is greater than a value
   * @param name  of the metric
   * @param greater value of the metric should be greater than this
   * @param source  the metrics source
   */
  public static void assertGaugeGt(String name, double greater,
                                   MetricsSource source) {
    assertGaugeGt(name, greater, getMetrics(source));
  }

}
