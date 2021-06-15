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
package org.apache.hadoop.io;

import java.io.*;

/** Singleton Writable with no data. */
public class NullWritable implements WritableComparable {

  private static final NullWritable THIS = new NullWritable();

  private NullWritable() {}                       // no public ctor

  /** Returns the single instance of this class. */
  public static NullWritable get() { return THIS; }
  
  public String toString() {
    return "(null)";
  }

  public int hashCode() { return 0; }
  public int compareTo(Object other) {
    if (!(other instanceof NullWritable)) {
      throw new ClassCastException("can't compare " + other.getClass().getName() 
                                   + " to NullWritable");
    }
    return 0;
  }
  public boolean equals(Object other) { return other instanceof NullWritable; }
  public void readFields(DataInput in) throws IOException {}
  public void write(DataOutput out) throws IOException {}

  /** A Comparator &quot;optimized&quot; for NullWritable. */
  public static class Comparator extends WritableComparator {
    public Comparator() {
      super(NullWritable.class);
    }

    /**
     * Compare the buffers in serialized form.
     */
    public int compare(byte[] b1, int s1, int l1,
                       byte[] b2, int s2, int l2) {
      assert 0 == l1;
      assert 0 == l2;
      return 0;
    }
  }

  static {                                        // register this comparator
    WritableComparator.define(NullWritable.class, new Comparator());
  }
}

