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
package org.apache.hadoop.mapreduce;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TreeMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;

/**
 * A group of {@link Counter}s that logically belong together. Typically,
 * it is an {@link Enum} subclass and the counters are the values.
 */
public class CounterGroup implements Writable, Iterable<Counter> {
  private String name;
  private String displayName;
  private TreeMap<String, Counter> counters = new TreeMap<String, Counter>();
  // Optional ResourceBundle for localization of group and counter names.
  private ResourceBundle bundle = null;    
  
  /**
   * Returns the specified resource bundle, or throws an exception.
   * @throws MissingResourceException if the bundle isn't found
   */
  private static ResourceBundle getResourceBundle(String enumClassName) {
    String bundleName = enumClassName.replace('$','_');
    return ResourceBundle.getBundle(bundleName);
  }

  protected CounterGroup(String name) {
    this.name = name;
    try {
      bundle = getResourceBundle(name);
    }
    catch (MissingResourceException neverMind) {
    }
    displayName = localize("CounterGroupName", name);
  }
  
  protected CounterGroup(String name, String displayName) {
    this.name = name;
    this.displayName = displayName;
  }
 
  /**
   * Get the internal name of the group
   * @return the internal name
   */
  public synchronized String getName() {
    return name;
  }
  
  /**
   * Get the display name of the group.
   * @return the human readable name
   */
  public synchronized String getDisplayName() {
    return displayName;
  }

  synchronized void addCounter(Counter counter) {
    counters.put(counter.getName(), counter);
  }

  /**
   * Internal to find a counter in a group.
   * @param counterName the name of the counter
   * @param displayName the display name of the counter
   * @return the counter that was found or added
   */
  protected Counter findCounter(String counterName, String displayName) {
    Counter result = counters.get(counterName);
    if (result == null) {
      result = new Counter(counterName, displayName);
      counters.put(counterName, result);
    }
    return result;
  }

  public synchronized Counter findCounter(String counterName) {
    Counter result = counters.get(counterName);
    if (result == null) {
      String displayName = localize(counterName, counterName);
      result = new Counter(counterName, displayName);
      counters.put(counterName, result);
    }
    return result;
  }
  
  public synchronized Iterator<Counter> iterator() {
    return counters.values().iterator();
  }

  public synchronized void write(DataOutput out) throws IOException {
    Text.writeString(out, displayName);
    WritableUtils.writeVInt(out, counters.size());
    for(Counter counter: counters.values()) {
      counter.write(out);
    }
  }
  
  public synchronized void readFields(DataInput in) throws IOException {
    displayName = Text.readString(in);
    counters.clear();
    int size = WritableUtils.readVInt(in);
    for(int i=0; i < size; i++) {
      Counter counter = new Counter();
      counter.readFields(in);
      counters.put(counter.getName(), counter);
    }
  }

  /**
   * Looks up key in the ResourceBundle and returns the corresponding value.
   * If the bundle or the key doesn't exist, returns the default value.
   */
  private String localize(String key, String defaultValue) {
    String result = defaultValue;
    if (bundle != null) {
      try {
        result = bundle.getString(key);
      }
      catch (MissingResourceException mre) {
      }
    }
    return result;
  }

  /**
   * Returns the number of counters in this group.
   */
  public synchronized int size() {
    return counters.size();
  }

  public synchronized boolean equals(Object genericRight) {
    if (genericRight instanceof CounterGroup) {
      Iterator<Counter> right = ((CounterGroup) genericRight).counters.
                                       values().iterator();
      Iterator<Counter> left = counters.values().iterator();
      while (left.hasNext()) {
        if (!right.hasNext() || !left.next().equals(right.next())) {
          return false;
        }
      }
      return !right.hasNext();
    }
    return false;
  }

  public synchronized int hashCode() {
    return counters.hashCode();
  }
  
  public synchronized void incrAllCounters(CounterGroup rightGroup) {
    for(Counter right: rightGroup.counters.values()) {
      Counter left = findCounter(right.getName(), right.getDisplayName());
      left.increment(right.getValue());
    }
  }
}
