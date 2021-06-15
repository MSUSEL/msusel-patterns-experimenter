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
package org.apache.hadoop.mapred.lib.aggregate;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.Reducer;

/**
 * This abstract class implements some common functionalities of the
 * the generic mapper, reducer and combiner classes of Aggregate.
 */
public abstract class ValueAggregatorJobBase<K1 extends WritableComparable,
                                             V1 extends Writable>
  implements Mapper<K1, V1, Text, Text>, Reducer<Text, Text, Text, Text> {

  protected ArrayList<ValueAggregatorDescriptor> aggregatorDescriptorList = null;

  public void configure(JobConf job) {
    this.initializeMySpec(job);
    this.logSpec();
  }

  private static ValueAggregatorDescriptor getValueAggregatorDescriptor(
      String spec, JobConf job) {
    if (spec == null)
      return null;
    String[] segments = spec.split(",", -1);
    String type = segments[0];
    if (type.compareToIgnoreCase("UserDefined") == 0) {
      String className = segments[1];
      return new UserDefinedValueAggregatorDescriptor(className, job);
    }
    return null;
  }

  private static ArrayList<ValueAggregatorDescriptor> getAggregatorDescriptors(JobConf job) {
    String advn = "aggregator.descriptor";
    int num = job.getInt(advn + ".num", 0);
    ArrayList<ValueAggregatorDescriptor> retv = new ArrayList<ValueAggregatorDescriptor>(num);
    for (int i = 0; i < num; i++) {
      String spec = job.get(advn + "." + i);
      ValueAggregatorDescriptor ad = getValueAggregatorDescriptor(spec, job);
      if (ad != null) {
        retv.add(ad);
      }
    }
    return retv;
  }

  private void initializeMySpec(JobConf job) {
    this.aggregatorDescriptorList = getAggregatorDescriptors(job);
    if (this.aggregatorDescriptorList.size() == 0) {
      this.aggregatorDescriptorList
          .add(new UserDefinedValueAggregatorDescriptor(
              ValueAggregatorBaseDescriptor.class.getCanonicalName(), job));
    }
  }

  protected void logSpec() {

  }

  public void close() throws IOException {
  }
}
