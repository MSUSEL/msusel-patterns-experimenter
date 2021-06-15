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
import java.util.List;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import org.apache.hadoop.metrics2.Metric;
import org.apache.hadoop.metrics2.MetricCounter;
import org.apache.hadoop.metrics2.MetricGauge;
import org.apache.hadoop.metrics2.MetricsTag;

import org.apache.hadoop.metrics2.MetricsVisitor;

/**
 * Helper class to build MBeanInfo from metrics records
 */
class MBeanInfoBuilder implements MetricsVisitor {

  private final String name, description;
  private List<MBeanAttributeInfo> attrs;
  private Iterable<MetricsRecordImpl> recs;
  private int curRecNo;

  MBeanInfoBuilder(String name, String desc) {
    this.name = name;
    description = desc;
    attrs = new ArrayList<MBeanAttributeInfo>();
  }

  MBeanInfoBuilder reset(Iterable<MetricsRecordImpl> recs) {
    this.recs = recs;
    attrs.clear();
    return this;
  }

  MBeanAttributeInfo newAttrInfo(String name, String desc, String type) {
    return new MBeanAttributeInfo(getAttrName(name), type, desc,
                                  true, false, false); // read-only, non-is
  }

  MBeanAttributeInfo newAttrInfo(Metric m, String type) {
    return newAttrInfo(m.name(), m.description(), type);
  }

  public void gauge(MetricGauge<Integer> metric, int value) {
    attrs.add(newAttrInfo(metric, "java.lang.Integer"));
  }

  public void gauge(MetricGauge<Long> metric, long value) {
    attrs.add(newAttrInfo(metric, "java.lang.Long"));
  }

  public void gauge(MetricGauge<Float> metric, float value) {
    attrs.add(newAttrInfo(metric, "java.lang.Float"));
  }

  public void gauge(MetricGauge<Double> metric, double value) {
    attrs.add(newAttrInfo(metric, "java.lang.Double"));
  }

  public void counter(MetricCounter<Integer> metric, int value) {
    attrs.add(newAttrInfo(metric, "java.lang.Integer"));
  }

  public void counter(MetricCounter<Long> metric, long value) {
    attrs.add(newAttrInfo(metric, "java.lang.Long"));
  }

  String getAttrName(String name) {
    return curRecNo > 0 ? name +"."+ curRecNo : name;
  }

  MBeanInfo get() {
    curRecNo = 0;
    for (MetricsRecordImpl rec : recs) {
      for (MetricsTag t : rec.tags()) {
        attrs.add(newAttrInfo("tag."+ t.name(), t.description(),
                  "java.lang.String"));
      }
      for (Metric m : rec.metrics()) {
        m.visit(this);
      }
      ++curRecNo;
    }
    MBeanAttributeInfo[] attrsArray = new MBeanAttributeInfo[attrs.size()];
    return new MBeanInfo(name, description, attrs.toArray(attrsArray),
                         null, null, null); // no ops/ctors/notifications
  }
}
