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

import org.apache.hadoop.io.WritableComparable;

/**
 * A general identifier, which internally stores the id
 * as an integer. This is the super class of {@link JobID}, 
 * {@link TaskID} and {@link TaskAttemptID}.
 * 
 * @see JobID
 * @see TaskID
 * @see TaskAttemptID
 */
public abstract class ID implements WritableComparable<ID> {
  protected static final char SEPARATOR = '_';
  protected int id;

  /** constructs an ID object from the given int */
  public ID(int id) {
    this.id = id;
  }

  protected ID() {
  }

  /** returns the int which represents the identifier */
  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return String.valueOf(id);
  }

  @Override
  public int hashCode() {
    return Integer.valueOf(id).hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if(o == null)
      return false;
    if (o.getClass() == this.getClass()) {
      ID that = (ID) o;
      return this.id == that.id;
    }
    else
      return false;
  }

  /** Compare IDs by associated numbers */
  public int compareTo(ID that) {
    return this.id - that.id;
  }

  public void readFields(DataInput in) throws IOException {
    this.id = in.readInt();
  }

  public void write(DataOutput out) throws IOException {
    out.writeInt(id);
  }
  
}
