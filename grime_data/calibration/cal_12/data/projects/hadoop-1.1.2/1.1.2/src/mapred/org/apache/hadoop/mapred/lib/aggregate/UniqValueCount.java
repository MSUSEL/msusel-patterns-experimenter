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
import java.util.Set;
import java.util.TreeMap;

/**
 * This class implements a value aggregator that dedupes a sequence of objects.
 * 
 */
public class UniqValueCount implements ValueAggregator {

  private TreeMap<Object, Object> uniqItems = null;

  private long numItems = 0;
  
  private long maxNumItems = Long.MAX_VALUE;

  /**
   * the default constructor
   * 
   */
  public UniqValueCount() {
    this(Long.MAX_VALUE);
  }
  
  /**
   * constructor
   * @param maxNum the limit in the number of unique values to keep.
   *  
   */
  public UniqValueCount(long maxNum) {
    uniqItems = new TreeMap<Object, Object>();
    this.numItems = 0;
    maxNumItems = Long.MAX_VALUE;
    if (maxNum > 0 ) {
      this.maxNumItems = maxNum;
    }
  }

  /**
   * Set the limit on the number of unique values
   * @param n the desired limit on the number of unique values
   * @return the new limit on the number of unique values
   */
  public long setMaxItems(long n) {
    if (n >= numItems) {
      this.maxNumItems = n;
    } else if (this.maxNumItems >= this.numItems) {
      this.maxNumItems = this.numItems;
    }
    return this.maxNumItems;
  }
  
  /**
   * add a value to the aggregator
   * 
   * @param val
   *          an object.
   * 
   */
  public void addNextValue(Object val) {
    if (this.numItems <= this.maxNumItems) {
      uniqItems.put(val.toString(), "1");
      this.numItems = this.uniqItems.size();
    }
  }

  /**
   * @return return the number of unique objects aggregated
   */
  public String getReport() {
    return "" + uniqItems.size();
  }

  /**
   * 
   * @return the set of the unique objects
   */
  public Set getUniqueItems() {
    return uniqItems.keySet();
  }

  /**
   * reset the aggregator
   */
  public void reset() {
    uniqItems = new TreeMap<Object, Object>();
  }

  /**
   * @return return an array of the unique objects. The return value is
   *         expected to be used by the a combiner.
   */
  public ArrayList getCombinerOutput() {
    Object key = null;
    Iterator iter = uniqItems.keySet().iterator();
    ArrayList<Object> retv = new ArrayList<Object>();

    while (iter.hasNext()) {
      key = iter.next();
      retv.add(key);
    }
    return retv;
  }
}
