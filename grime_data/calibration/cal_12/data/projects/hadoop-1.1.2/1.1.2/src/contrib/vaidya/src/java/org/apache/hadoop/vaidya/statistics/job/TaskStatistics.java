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
package org.apache.hadoop.vaidya.statistics.job;

import java.util.Hashtable;
import java.util.Map;

/**
 *
 */
public class TaskStatistics {
  
  /*
   * Stores task statistics as Enum/String key,value pairs.
   */
  private Hashtable<Enum, String>  _task = new Hashtable<Enum, String>();
  
  /* 
   * Get Long key value
   */
  public long getLongValue(Enum key) {
    if (this._task.get(key) == null) {
      return (long)0;
    }
    else {
      return Long.parseLong(this._task.get(key));
    }
  } 

  /*
   * Get key type Double
   */
  public double getDoubleValue(Enum key) {
    if (this._task.get(key) == null) {
      return (double)0;
    } else {
      return Double.parseDouble(this._task.get(key));
    }
  }
 
  /*
   * Get key of type String
   */
  public String getStringValue(Enum key) {
    if (this._task.get(key) == null) {
      return "";
    } else {
     return this._task.get(key);
    }
  }

  /*
   * Set long key value 
   */
  public void setValue(Enum key, long value) {
    this._task.put(key, Long.toString(value));
  }
  
  /*
   * Set double key value
   */
  public void setValue(Enum key, double value) {
    this._task.put(key, Double.toString(value));
  }
  
  /*
   * Set String key value
   */
  public void setValue(Enum key, String value) {
    this._task.put(key, value);
  }
  
  /*
   * Print the key/values pairs for a task 
   */
  public void  printKeys () {
    java.util.Set<Map.Entry<Enum, String>> task = this._task.entrySet();
    int size = task.size();
    java.util.Iterator<Map.Entry<Enum, String>> kv = task.iterator();
    for (int i = 0; i < size; i++)
    {
      Map.Entry<Enum, String> entry = (Map.Entry<Enum, String>) kv.next();
      Enum key = entry.getKey();
      String value = entry.getValue();
      System.out.println("Key:<" + key.name() + ">, value:<"+ value +">"); 
    }
  }
}
