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
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Arrays;


/**
 * This class implements a value aggregator that computes the 
 * histogram of a sequence of strings.
 * 
 */
public class ValueHistogram implements ValueAggregator {

  TreeMap<Object, Object> items = null;

  public ValueHistogram() {
    items = new TreeMap<Object, Object>();
  }

  /**
   * add the given val to the aggregator.
   * 
   * @param val the value to be added. It is expected to be a string
   * in the form of xxxx\tnum, meaning xxxx has num occurrences.
   */
  public void addNextValue(Object val) {
    String valCountStr = val.toString();
    int pos = valCountStr.lastIndexOf("\t");
    String valStr = valCountStr;
    String countStr = "1";
    if (pos >= 0) {
      valStr = valCountStr.substring(0, pos);
      countStr = valCountStr.substring(pos + 1);
    }
    
    Long count = (Long) this.items.get(valStr);
    long inc = Long.parseLong(countStr);

    if (count == null) {
      count = inc;
    } else {
      count = count.longValue() + inc;
    }
    items.put(valStr, count);
  }

  /**
   * @return the string representation of this aggregator.
   * It includes the following basic statistics of the histogram:
   *    the number of unique values
   *    the minimum value
   *    the media value
   *    the maximum value
   *    the average value
   *    the standard deviation
   */
  public String getReport() {
    long[] counts = new long[items.size()];

    StringBuffer sb = new StringBuffer();
    Iterator iter = items.values().iterator();
    int i = 0;
    while (iter.hasNext()) {
      Long count = (Long) iter.next();
      counts[i] = count.longValue();
      i += 1;
    }
    Arrays.sort(counts);
    sb.append(counts.length);
    i = 0;
    long acc = 0;
    while (i < counts.length) {
      long nextVal = counts[i];
      int j = i + 1;
      while (j < counts.length && counts[j] == nextVal) {
        j++;
      }
      acc += nextVal * (j - i);
      //sbVal.append("\t").append(nextVal).append("\t").append(j - i)
      //.append("\n");
      i = j;
    }
    double average = 0.0;
    double sd = 0.0;
    if (counts.length > 0) {
      sb.append("\t").append(counts[0]);
      sb.append("\t").append(counts[counts.length / 2]);
      sb.append("\t").append(counts[counts.length - 1]);

      average = acc * 1.0 / counts.length;
      sb.append("\t").append(average);

      i = 0;
      while (i < counts.length) {
        double nextDiff = counts[i] - average;
        sd += nextDiff * nextDiff;
        i += 1;
      }
      sd = Math.sqrt(sd / counts.length);

      sb.append("\t").append(sd);

    }
    //sb.append("\n").append(sbVal.toString());
    return sb.toString();
  }

  /** 
   * 
   * @return a string representation of the list of value/frequence pairs of 
   * the histogram
   */
  public String getReportDetails() {
    StringBuffer sb = new StringBuffer();
    Iterator iter = items.entrySet().iterator();
    while (iter.hasNext()) {
      Entry en = (Entry) iter.next();
      Object val = en.getKey();
      Long count = (Long) en.getValue();
      sb.append("\t").append(val.toString()).append("\t").append(
                                                                 count.longValue()).append("\n");
    }
    return sb.toString();
  }

  /**
   *  @return a list value/frequence pairs.
   *  The return value is expected to be used by the reducer.
   */
  public ArrayList getCombinerOutput() {
    ArrayList<String> retv = new ArrayList<String>();
    Iterator iter = items.entrySet().iterator();

    while (iter.hasNext()) {
      Entry en = (Entry) iter.next();
      Object val = en.getKey();
      Long count = (Long) en.getValue();
      retv.add(val.toString() + "\t" + count.longValue());
    }
    return retv;
  }

  /** 
   * 
   * @return a TreeMap representation of the histogram
   */
  public TreeMap getReportItems() {
    return items;
  }

  /** 
   * reset the aggregator
   */
  public void reset() {
    items = new TreeMap<Object, Object>();
  }

}
