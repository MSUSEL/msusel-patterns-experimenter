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
 * This class implements the common functionalities of 
 * the subclasses of ValueAggregatorDescriptor class.
 */
public class ValueAggregatorBaseDescriptor implements ValueAggregatorDescriptor {

  static public final String UNIQ_VALUE_COUNT = "UniqValueCount";

  static public final String LONG_VALUE_SUM = "LongValueSum";

  static public final String DOUBLE_VALUE_SUM = "DoubleValueSum";

  static public final String VALUE_HISTOGRAM = "ValueHistogram";
  
  static public final String LONG_VALUE_MAX = "LongValueMax";
  
  static public final String LONG_VALUE_MIN = "LongValueMin";
  
  static public final String STRING_VALUE_MAX = "StringValueMax";
  
  static public final String STRING_VALUE_MIN = "StringValueMin";
  
  private static long maxNumItems = Long.MAX_VALUE;
  
  public String inputFile = null;

  private static class MyEntry implements Entry<Text, Text> {
    Text key;

    Text val;

    public Text getKey() {
      return key;
    }

    public Text getValue() {
      return val;
    }

    public Text setValue(Text val) {
      this.val = val;
      return val;
    }

    public MyEntry(Text key, Text val) {
      this.key = key;
      this.val = val;
    }
  }

  /**
   * 
   * @param type the aggregation type
   * @param id the aggregation id
   * @param val the val associated with the id to be aggregated
   * @return an Entry whose key is the aggregation id prefixed with 
   * the aggregation type.
   */
  public static Entry<Text, Text> generateEntry(String type, String id, Text val) {
    Text key = new Text(type + TYPE_SEPARATOR + id);
    return new MyEntry(key, val);
  }

  /**
   * 
   * @param type the aggregation type
   * @return a value aggregator of the given type.
   */
  static public ValueAggregator generateValueAggregator(String type) {
    ValueAggregator retv = null;
    if (type.compareToIgnoreCase(LONG_VALUE_SUM) == 0) {
      retv = new LongValueSum();
    } if (type.compareToIgnoreCase(LONG_VALUE_MAX) == 0) {
      retv = new LongValueMax();
    } else if (type.compareToIgnoreCase(LONG_VALUE_MIN) == 0) {
      retv = new LongValueMin();
    } else if (type.compareToIgnoreCase(STRING_VALUE_MAX) == 0) {
      retv = new StringValueMax();
    } else if (type.compareToIgnoreCase(STRING_VALUE_MIN) == 0) {
      retv = new StringValueMin();
    } else if (type.compareToIgnoreCase(DOUBLE_VALUE_SUM) == 0) {
      retv = new DoubleValueSum();
    } else if (type.compareToIgnoreCase(UNIQ_VALUE_COUNT) == 0) {
      retv = new UniqValueCount(maxNumItems);
    } else if (type.compareToIgnoreCase(VALUE_HISTOGRAM) == 0) {
      retv = new ValueHistogram();
    }
    return retv;
  }

  /**
   * Generate 1 or 2 aggregation-id/value pairs for the given key/value pair.
   * The first id will be of type LONG_VALUE_SUM, with "record_count" as
   * its aggregation id. If the input is a file split,
   * the second id of the same type will be generated too, with the file name 
   * as its aggregation id. This achieves the behavior of counting the total number
   * of records in the input data, and the number of records in each input file.
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
                                                          Object val) {
    ArrayList<Entry<Text, Text>> retv = new ArrayList<Entry<Text, Text>>();
    String countType = LONG_VALUE_SUM;
    String id = "record_count";
    Entry<Text, Text> e = generateEntry(countType, id, ONE);
    if (e != null) {
      retv.add(e);
    }
    if (this.inputFile != null) {
      e = generateEntry(countType, this.inputFile, ONE);
      if (e != null) {
        retv.add(e);
      }
    }
    return retv;
  }

  /**
   * get the input file name.
   * 
   * @param job a job configuration object
   */
  public void configure(JobConf job) {
    this.inputFile = job.get("map.input.file");
    maxNumItems = job.getLong("aggregate.max.num.unique.values",
                              Long.MAX_VALUE);
  }
}
