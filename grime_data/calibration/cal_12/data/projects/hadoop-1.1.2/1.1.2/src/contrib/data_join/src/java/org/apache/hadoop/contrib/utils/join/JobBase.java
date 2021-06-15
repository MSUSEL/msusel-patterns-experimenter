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
package org.apache.hadoop.contrib.utils.join;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.Reducer;

/**
 * A common base implementing some statics collecting mechanisms that are
 * commonly used in a typical map/reduce job.
 * 
 */
public abstract class JobBase implements Mapper, Reducer {

  public static final Log LOG = LogFactory.getLog("datajoin.job");

  private SortedMap<Object, Long> longCounters = null;

  private SortedMap<Object, Double> doubleCounters = null;

  /**
   * Set the given counter to the given value
   * 
   * @param name
   *          the counter name
   * @param value
   *          the value for the counter
   */
  protected void setLongValue(Object name, long value) {
    this.longCounters.put(name, new Long(value));
  }

  /**
   * Set the given counter to the given value
   * 
   * @param name
   *          the counter name
   * @param value
   *          the value for the counter
   */
  protected void setDoubleValue(Object name, double value) {
    this.doubleCounters.put(name, new Double(value));
  }

  /**
   * 
   * @param name
   *          the counter name
   * @return return the value of the given counter.
   */
  protected Long getLongValue(Object name) {
    return this.longCounters.get(name);
  }

  /**
   * 
   * @param name
   *          the counter name
   * @return return the value of the given counter.
   */
  protected Double getDoubleValue(Object name) {
    return this.doubleCounters.get(name);
  }

  /**
   * Increment the given counter by the given incremental value If the counter
   * does not exist, one is created with value 0.
   * 
   * @param name
   *          the counter name
   * @param inc
   *          the incremental value
   * @return the updated value.
   */
  protected Long addLongValue(Object name, long inc) {
    Long val = this.longCounters.get(name);
    Long retv = null;
    if (val == null) {
      retv = new Long(inc);
    } else {
      retv = new Long(val.longValue() + inc);
    }
    this.longCounters.put(name, retv);
    return retv;
  }

  /**
   * Increment the given counter by the given incremental value If the counter
   * does not exist, one is created with value 0.
   * 
   * @param name
   *          the counter name
   * @param inc
   *          the incremental value
   * @return the updated value.
   */
  protected Double addDoubleValue(Object name, double inc) {
    Double val = this.doubleCounters.get(name);
    Double retv = null;
    if (val == null) {
      retv = new Double(inc);
    } else {
      retv = new Double(val.doubleValue() + inc);
    }
    this.doubleCounters.put(name, retv);
    return retv;
  }

  /**
   * log the counters
   * 
   */
  protected void report() {
    LOG.info(getReport());
  }

  /**
   * log the counters
   * 
   */
  protected String getReport() {
    StringBuffer sb = new StringBuffer();

    Iterator iter = this.longCounters.entrySet().iterator();
    while (iter.hasNext()) {
      Entry e = (Entry) iter.next();
      sb.append(e.getKey().toString()).append("\t").append(e.getValue())
        .append("\n");
    }
    iter = this.doubleCounters.entrySet().iterator();
    while (iter.hasNext()) {
      Entry e = (Entry) iter.next();
      sb.append(e.getKey().toString()).append("\t").append(e.getValue())
        .append("\n");
    }
    return sb.toString();
  }

  /**
   * Initializes a new instance from a {@link JobConf}.
   * 
   * @param job
   *          the configuration
   */
  public void configure(JobConf job) {
    this.longCounters = new TreeMap<Object, Long>();
    this.doubleCounters = new TreeMap<Object, Double>();
  }
}
