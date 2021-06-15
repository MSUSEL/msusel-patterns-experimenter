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

import org.apache.hadoop.util.hash.Hash;

/**
 * Implements a hash object that returns a certain number of hashed values.
 * 
 * @see Key The general behavior of a key being stored in a filter
 * @see Filter The general behavior of a filter
 */
public final class HashFunction {
  /** The number of hashed values. */
  private int nbHash;

  /** The maximum highest returned value. */
  private int maxValue;

  /** Hashing algorithm to use. */
  private Hash hashFunction;
  
  /**
   * Constructor.
   * <p>
   * Builds a hash function that must obey to a given maximum number of returned values and a highest value.
   * @param maxValue The maximum highest returned value.
   * @param nbHash The number of resulting hashed values.
   * @param hashType type of the hashing function (see {@link Hash}).
   */
  public HashFunction(int maxValue, int nbHash, int hashType) {
    if (maxValue <= 0) {
      throw new IllegalArgumentException("maxValue must be > 0");
    }
    
    if (nbHash <= 0) {
      throw new IllegalArgumentException("nbHash must be > 0");
    }

    this.maxValue = maxValue;
    this.nbHash = nbHash;
    this.hashFunction = Hash.getInstance(hashType);
    if (this.hashFunction == null)
      throw new IllegalArgumentException("hashType must be known");
  }

  /** Clears <i>this</i> hash function. A NOOP */
  public void clear() {
  }

  /**
   * Hashes a specified key into several integers.
   * @param k The specified key.
   * @return The array of hashed values.
   */
  public int[] hash(Key k){
      byte[] b = k.getBytes();
      if (b == null) {
        throw new NullPointerException("buffer reference is null");
      }
      if (b.length == 0) {
        throw new IllegalArgumentException("key length must be > 0");
      }
      int[] result = new int[nbHash];
      for (int i = 0, initval = 0; i < nbHash; i++) {
	  initval = hashFunction.hash(b, initval);
	  result[i] = Math.abs(initval % maxValue);
      }
      return result;
  }
}