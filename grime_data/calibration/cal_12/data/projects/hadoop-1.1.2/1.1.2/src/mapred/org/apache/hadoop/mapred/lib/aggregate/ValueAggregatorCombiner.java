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
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

/**
 * This class implements the generic combiner of Aggregate.
 */
public class ValueAggregatorCombiner<K1 extends WritableComparable,
                                     V1 extends Writable>
  extends ValueAggregatorJobBase<K1, V1> {

  /**
   * Combiner does not need to configure.
   */
  public void configure(JobConf job) {

  }

  /** Combines values for a given key.  
   * @param key the key is expected to be a Text object, whose prefix indicates
   * the type of aggregation to aggregate the values. 
   * @param values the values to combine
   * @param output to collect combined values
   */
  public void reduce(Text key, Iterator<Text> values,
                     OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
    String keyStr = key.toString();
    int pos = keyStr.indexOf(ValueAggregatorDescriptor.TYPE_SEPARATOR);
    String type = keyStr.substring(0, pos);
    ValueAggregator aggregator = ValueAggregatorBaseDescriptor
      .generateValueAggregator(type);
    while (values.hasNext()) {
      aggregator.addNextValue(values.next());
    }
    Iterator outputs = aggregator.getCombinerOutput().iterator();

    while (outputs.hasNext()) {
      Object v = outputs.next();
      if (v instanceof Text) {
        output.collect(key, (Text)v);
      } else {
        output.collect(key, new Text(v.toString()));
      }
    }
  }

  /** 
   * Do nothing. 
   *
   */
  public void close() throws IOException {

  }

  /** 
   * Do nothing. Should not be called. 
   *
   */
  public void map(K1 arg0, V1 arg1, OutputCollector<Text, Text> arg2,
                  Reporter arg3) throws IOException {
    throw new IOException ("should not be called\n");
  }
}
