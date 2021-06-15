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

/**
 * This class implements a value aggregator that maintain the maximum of 
 * a sequence of long values.
 * 
 */
public class LongValueMax implements ValueAggregator {

  long maxVal = Long.MIN_VALUE;
    
  /**
   *  the default constructor
   *
   */
  public LongValueMax() {
    reset();
  }

  /**
   * add a value to the aggregator
   * 
   * @param val
   *          an object whose string representation represents a long value.
   * 
   */
  public void addNextValue(Object val) {
    long newVal = Long.parseLong(val.toString());
    if (this.maxVal < newVal) {
      this.maxVal = newVal;
    }
  }
    
  /**
   * add a value to the aggregator
   * 
   * @param newVal
   *          a long value.
   * 
   */
  public void addNextValue(long newVal) {
    if (this.maxVal < newVal) {
      this.maxVal = newVal;
    };
  }
    
  /**
   * @return the aggregated value
   */
  public long getVal() {
    return this.maxVal;
  }
    
  /**
   * @return the string representation of the aggregated value
   */
  public String getReport() {
    return ""+maxVal;
  }

  /**
   * reset the aggregator
   */
  public void reset() {
    maxVal = Long.MIN_VALUE;
  }

  /**
   * @return return an array of one element. The element is a string
   *         representation of the aggregated value. The return value is
   *         expected to be used by the a combiner.
   */
  public ArrayList<String> getCombinerOutput() {
    ArrayList<String> retv = new ArrayList<String>(1);;
    retv.add(""+maxVal);
    return retv;
  }
}
