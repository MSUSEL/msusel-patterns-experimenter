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
/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.util.bloom;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

/**
 * The general behavior of a key that must be stored in a filter.
 * 
 * @see Filter The general behavior of a filter
 */
public class Key implements WritableComparable<Key> {
  /** Byte value of key */
  byte[] bytes;
  
  /**
   * The weight associated to <i>this</i> key.
   * <p>
   * <b>Invariant</b>: if it is not specified, each instance of 
   * <code>Key</code> will have a default weight of 1.0
   */
  double weight;

  /** default constructor - use with readFields */
  public Key() {}

  /**
   * Constructor.
   * <p>
   * Builds a key with a default weight.
   * @param value The byte value of <i>this</i> key.
   */
  public Key(byte[] value) {
    this(value, 1.0);
  }

  /**
   * Constructor.
   * <p>
   * Builds a key with a specified weight.
   * @param value The value of <i>this</i> key.
   * @param weight The weight associated to <i>this</i> key.
   */
  public Key(byte[] value, double weight) {
    set(value, weight);
  }

  /**
   * @param value
   * @param weight
   */
  public void set(byte[] value, double weight) {
    if (value == null) {
      throw new IllegalArgumentException("value can not be null");
    }
    this.bytes = value;
    this.weight = weight;
  }
  
  /** @return byte[] The value of <i>this</i> key. */
  public byte[] getBytes() {
    return this.bytes;
  }

  /** @return Returns the weight associated to <i>this</i> key. */
  public double getWeight() {
    return weight;
  }

  /**
   * Increments the weight of <i>this</i> key with a specified value. 
   * @param weight The increment.
   */
  public void incrementWeight(double weight) {
    this.weight += weight;
  }

  /** Increments the weight of <i>this</i> key by one. */
  public void incrementWeight() {
    this.weight++;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Key)) {
      return false;
    }
    return this.compareTo((Key)o) == 0;
  }
  
  @Override
  public int hashCode() {
    int result = 0;
    for (int i = 0; i < bytes.length; i++) {
      result ^= Byte.valueOf(bytes[i]).hashCode();
    }
    result ^= Double.valueOf(weight).hashCode();
    return result;
  }

  // Writable

  public void write(DataOutput out) throws IOException {
    out.writeInt(bytes.length);
    out.write(bytes);
    out.writeDouble(weight);
  }
  
  public void readFields(DataInput in) throws IOException {
    this.bytes = new byte[in.readInt()];
    in.readFully(this.bytes);
    weight = in.readDouble();
  }
  
  // Comparable
  
  public int compareTo(Key other) {
    int result = this.bytes.length - other.getBytes().length;
    for (int i = 0; result == 0 && i < bytes.length; i++) {
      result = this.bytes[i] - other.bytes[i];
    }
    
    if (result == 0) {
      result = Double.valueOf(this.weight - other.weight).intValue();
    }
    return result;
  }
}