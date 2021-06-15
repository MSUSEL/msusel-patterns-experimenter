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
package org.apache.hadoop.io.file.tfile;

import java.io.IOException;
import java.util.Random;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.file.tfile.RandomDistribution.DiscreteRNG;

class KeySampler {
  Random random;
  int min, max;
  DiscreteRNG keyLenRNG;
  private static final int MIN_KEY_LEN = 4;

  public KeySampler(Random random, RawComparable first, RawComparable last,
      DiscreteRNG keyLenRNG) throws IOException {
    this.random = random;
    min = keyPrefixToInt(first);
    max = keyPrefixToInt(last);
    this.keyLenRNG = keyLenRNG;
  }

  private int keyPrefixToInt(RawComparable key) throws IOException {
    byte[] b = key.buffer();
    int o = key.offset();
    return (b[o] & 0xff) << 24 | (b[o + 1] & 0xff) << 16
        | (b[o + 2] & 0xff) << 8 | (b[o + 3] & 0xff);
  }
  
  public void next(BytesWritable key) {
    key.setSize(Math.max(MIN_KEY_LEN, keyLenRNG.nextInt()));
    random.nextBytes(key.get());
    int n = random.nextInt(max - min) + min;
    byte[] b = key.get();
    b[0] = (byte) (n >> 24);
    b[1] = (byte) (n >> 16);
    b[2] = (byte) (n >> 8);
    b[3] = (byte) n;
  }
}
