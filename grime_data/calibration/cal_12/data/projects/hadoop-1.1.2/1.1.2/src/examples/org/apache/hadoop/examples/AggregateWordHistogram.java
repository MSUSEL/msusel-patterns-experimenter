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
package org.apache.hadoop.examples;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.lib.aggregate.ValueAggregatorBaseDescriptor;
import org.apache.hadoop.mapred.lib.aggregate.ValueAggregatorJob;

/**
 * This is an example Aggregated Hadoop Map/Reduce application. Computes the
 * histogram of the words in the input texts.
 * 
 * To run: bin/hadoop jar hadoop-examples-*.jar aggregatewordhist <i>in-dir</i>
 * <i>out-dir</i> <i>numOfReducers</i> textinputformat
 * 
 */
public class AggregateWordHistogram {

  public static class AggregateWordHistogramPlugin 
    extends ValueAggregatorBaseDescriptor {
    
    /**
     * Parse the given value, generate an aggregation-id/value pair per word.
     * The ID is of type VALUE_HISTOGRAM, with WORD_HISTOGRAM as the real id.
     * The value is WORD\t1.
     *
     * @return a list of the generated pairs.
     */
    @Override
    public ArrayList<Entry<Text, Text>> generateKeyValPairs(Object key, Object val) {
      String words[] = val.toString().split(" |\t");
      ArrayList<Entry<Text, Text>> retv = new ArrayList<Entry<Text, Text>>();
      for (int i = 0; i < words.length; i++) {
        Text valCount = new Text(words[i] + "\t" + "1");
        Entry<Text, Text> en = generateEntry(VALUE_HISTOGRAM, "WORD_HISTOGRAM",
                                 valCount);
        retv.add(en);
      }
      return retv;
    }
    
  }
  
  /**
   * The main driver for word count map/reduce program. Invoke this method to
   * submit the map/reduce job.
   * 
   * @throws IOException
   *           When there is communication problems with the job tracker.
   */
  @SuppressWarnings("unchecked")
  public static void main(String[] args) throws IOException {
    JobConf conf = ValueAggregatorJob.createValueAggregatorJob(args
        , new Class[] {AggregateWordHistogramPlugin.class});
    
    JobClient.runJob(conf);
  }
  
}
