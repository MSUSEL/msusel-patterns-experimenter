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

import java.util.ArrayList;
import java.util.Map.Entry;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;

/**
 * This interface defines the contract a value aggregator descriptor must
 * support. Such a descriptor can be configured with a JobConf object. Its main
 * function is to generate a list of aggregation-id/value pairs. An aggregation
 * id encodes an aggregation type which is used to guide the way to aggregate
 * the value in the reduce/combiner phrase of an Aggregate based job.The mapper in
 * an Aggregate based map/reduce job may create one or more of
 * ValueAggregatorDescriptor objects at configuration time. For each input
 * key/value pair, the mapper will use those objects to create aggregation
 * id/value pairs.
 * 
 */
public interface ValueAggregatorDescriptor {

  public static final String TYPE_SEPARATOR = ":";

  public static final Text ONE = new Text("1");

  /**
   * Generate a list of aggregation-id/value pairs for the given key/value pair.
   * This function is usually called by the mapper of an Aggregate based job.
   * 
   * @param key
   *          input key
   * @param val
   *          input value
   * @return a list of aggregation id/value pairs. An aggregation id encodes an
   *         aggregation type which is used to guide the way to aggregate the
   *         value in the reduce/combiner phrase of an Aggregate based job.
   */
  public ArrayList<Entry<Text, Text>> generateKeyValPairs(Object key,
                                                          Object val);

  /**
   * Configure the object
   * 
   * @param job
   *          a JobConf object that may contain the information that can be used
   *          to configure the object.
   */
  public void configure(JobConf job);
}
