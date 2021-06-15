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
package org.apache.hadoop.mapred.join;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashSet;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableUtils;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.util.ReflectionUtils;

/**
 * This InputSplit contains a set of child InputSplits. Any InputSplit inserted
 * into this collection must have a public default constructor.
 */
public class CompositeInputSplit implements InputSplit {

  private int fill = 0;
  private long totsize = 0L;
  private InputSplit[] splits;

  public CompositeInputSplit() { }

  public CompositeInputSplit(int capacity) {
    splits = new InputSplit[capacity];
  }

  /**
   * Add an InputSplit to this collection.
   * @throws IOException If capacity was not specified during construction
   *                     or if capacity has been reached.
   */
  public void add(InputSplit s) throws IOException {
    if (null == splits) {
      throw new IOException("Uninitialized InputSplit");
    }
    if (fill == splits.length) {
      throw new IOException("Too many splits");
    }
    splits[fill++] = s;
    totsize += s.getLength();
  }

  /**
   * Get ith child InputSplit.
   */
  public InputSplit get(int i) {
    return splits[i];
  }

  /**
   * Return the aggregate length of all child InputSplits currently added.
   */
  public long getLength() throws IOException {
    return totsize;
  }

  /**
   * Get the length of ith child InputSplit.
   */
  public long getLength(int i) throws IOException {
    return splits[i].getLength();
  }

  /**
   * Collect a set of hosts from all child InputSplits.
   */
  public String[] getLocations() throws IOException {
    HashSet<String> hosts = new HashSet<String>();
    for (InputSplit s : splits) {
      String[] hints = s.getLocations();
      if (hints != null && hints.length > 0) {
        for (String host : hints) {
          hosts.add(host);
        }
      }
    }
    return hosts.toArray(new String[hosts.size()]);
  }

  /**
   * getLocations from ith InputSplit.
   */
  public String[] getLocation(int i) throws IOException {
    return splits[i].getLocations();
  }

  /**
   * Write splits in the following format.
   * {@code
   * <count><class1><class2>...<classn><split1><split2>...<splitn>
   * }
   */
  public void write(DataOutput out) throws IOException {
    WritableUtils.writeVInt(out, splits.length);
    for (InputSplit s : splits) {
      Text.writeString(out, s.getClass().getName());
    }
    for (InputSplit s : splits) {
      s.write(out);
    }
  }

  /**
   * {@inheritDoc}
   * @throws IOException If the child InputSplit cannot be read, typically
   *                     for faliing access checks.
   */
  @SuppressWarnings("unchecked")  // Generic array assignment
  public void readFields(DataInput in) throws IOException {
    int card = WritableUtils.readVInt(in);
    if (splits == null || splits.length != card) {
      splits = new InputSplit[card];
    }
    Class<? extends InputSplit>[] cls = new Class[card];
    try {
      for (int i = 0; i < card; ++i) {
        cls[i] =
          Class.forName(Text.readString(in)).asSubclass(InputSplit.class);
      }
      for (int i = 0; i < card; ++i) {
        splits[i] = ReflectionUtils.newInstance(cls[i], null);
        splits[i].readFields(in);
      }
    } catch (ClassNotFoundException e) {
      throw (IOException)new IOException("Failed split init").initCause(e);
    }
  }

}
