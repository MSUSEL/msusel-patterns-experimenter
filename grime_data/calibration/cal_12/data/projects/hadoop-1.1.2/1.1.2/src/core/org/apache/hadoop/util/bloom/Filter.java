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
import java.util.Collection;
import java.util.List;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.hash.Hash;

/**
 * Defines the general behavior of a filter.
 * <p>
 * A filter is a data structure which aims at offering a lossy summary of a set <code>A</code>.  The
 * key idea is to map entries of <code>A</code> (also called <i>keys</i>) into several positions 
 * in a vector through the use of several hash functions.
 * <p>
 * Typically, a filter will be implemented as a Bloom filter (or a Bloom filter extension).
 * <p>
 * It must be extended in order to define the real behavior.
 * 
 * @see Key The general behavior of a key
 * @see HashFunction A hash function
 */
public abstract class Filter implements Writable {
  private static final int VERSION = -1; // negative to accommodate for old format 
  /** The vector size of <i>this</i> filter. */
  protected int vectorSize;

  /** The hash function used to map a key to several positions in the vector. */
  protected HashFunction hash;

  /** The number of hash function to consider. */
  protected int nbHash;
  
  /** Type of hashing function to use. */
  protected int hashType;

  protected Filter() {}
  
  /** 
   * Constructor.
   * @param vectorSize The vector size of <i>this</i> filter.
   * @param nbHash The number of hash functions to consider.
   * @param hashType type of the hashing function (see {@link Hash}).
   */
  protected Filter(int vectorSize, int nbHash, int hashType) {
    this.vectorSize = vectorSize;
    this.nbHash = nbHash;
    this.hashType = hashType;
    this.hash = new HashFunction(this.vectorSize, this.nbHash, this.hashType);
  }

  /**
   * Adds a key to <i>this</i> filter.
   * @param key The key to add.
   */
  public abstract void add(Key key);

  /**
   * Determines wether a specified key belongs to <i>this</i> filter.
   * @param key The key to test.
   * @return boolean True if the specified key belongs to <i>this</i> filter.
   * 		     False otherwise.
   */
  public abstract boolean membershipTest(Key key);

  /**
   * Peforms a logical AND between <i>this</i> filter and a specified filter.
   * <p>
   * <b>Invariant</b>: The result is assigned to <i>this</i> filter.
   * @param filter The filter to AND with.
   */
  public abstract void and(Filter filter);

  /**
   * Peforms a logical OR between <i>this</i> filter and a specified filter.
   * <p>
   * <b>Invariant</b>: The result is assigned to <i>this</i> filter.
   * @param filter The filter to OR with.
   */
  public abstract void or(Filter filter);

  /**
   * Peforms a logical XOR between <i>this</i> filter and a specified filter.
   * <p>
   * <b>Invariant</b>: The result is assigned to <i>this</i> filter.
   * @param filter The filter to XOR with.
   */
  public abstract void xor(Filter filter);

  /**
   * Performs a logical NOT on <i>this</i> filter.
   * <p>
   * The result is assigned to <i>this</i> filter.
   */
  public abstract void not();

  /**
   * Adds a list of keys to <i>this</i> filter.
   * @param keys The list of keys.
   */
  public void add(List<Key> keys){
    if(keys == null) {
      throw new IllegalArgumentException("ArrayList<Key> may not be null");
    }

    for(Key key: keys) {
      add(key);
    }
  }//end add()

  /**
   * Adds a collection of keys to <i>this</i> filter.
   * @param keys The collection of keys.
   */
  public void add(Collection<Key> keys){
    if(keys == null) {
      throw new IllegalArgumentException("Collection<Key> may not be null");
    }
    for(Key key: keys) {
      add(key);
    }
  }//end add()

  /**
   * Adds an array of keys to <i>this</i> filter.
   * @param keys The array of keys.
   */
  public void add(Key[] keys){
    if(keys == null) {
      throw new IllegalArgumentException("Key[] may not be null");
    }
    for(int i = 0; i < keys.length; i++) {
      add(keys[i]);
    }
  }//end add()
  
  // Writable interface
  
  public void write(DataOutput out) throws IOException {
    out.writeInt(VERSION);
    out.writeInt(this.nbHash);
    out.writeByte(this.hashType);
    out.writeInt(this.vectorSize);
  }

  public void readFields(DataInput in) throws IOException {
    int ver = in.readInt();
    if (ver > 0) { // old unversioned format
      this.nbHash = ver;
      this.hashType = Hash.JENKINS_HASH;
    } else if (ver == VERSION) {
      this.nbHash = in.readInt();
      this.hashType = in.readByte();
    } else {
      throw new IOException("Unsupported version: " + ver);
    }
    this.vectorSize = in.readInt();
    this.hash = new HashFunction(this.vectorSize, this.nbHash, this.hashType);
  }
}//end class
