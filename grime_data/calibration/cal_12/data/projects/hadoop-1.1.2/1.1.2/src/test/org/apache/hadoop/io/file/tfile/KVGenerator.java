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

import java.util.Random;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.io.file.tfile.RandomDistribution.DiscreteRNG;

/**
 * Generate random <key, value> pairs.
 */
class KVGenerator {
  private final Random random;
  private final byte[][] dict;
  private final boolean sorted;
  private final DiscreteRNG keyLenRNG, valLenRNG;
  private BytesWritable lastKey;
  private static final int MIN_KEY_LEN = 4;
  private final byte prefix[] = new byte[MIN_KEY_LEN];

  public KVGenerator(Random random, boolean sorted, DiscreteRNG keyLenRNG,
      DiscreteRNG valLenRNG, DiscreteRNG wordLenRNG, int dictSize) {
    this.random = random;
    dict = new byte[dictSize][];
    this.sorted = sorted;
    this.keyLenRNG = keyLenRNG;
    this.valLenRNG = valLenRNG;
    for (int i = 0; i < dictSize; ++i) {
      int wordLen = wordLenRNG.nextInt();
      dict[i] = new byte[wordLen];
      random.nextBytes(dict[i]);
    }
    lastKey = new BytesWritable();
    fillKey(lastKey);
  }
  
  private void fillKey(BytesWritable o) {
    int len = keyLenRNG.nextInt();
    if (len < MIN_KEY_LEN) len = MIN_KEY_LEN;
    o.setSize(len);
    int n = MIN_KEY_LEN;
    while (n < len) {
      byte[] word = dict[random.nextInt(dict.length)];
      int l = Math.min(word.length, len - n);
      System.arraycopy(word, 0, o.get(), n, l);
      n += l;
    }
    if (sorted
        && WritableComparator.compareBytes(lastKey.get(), MIN_KEY_LEN, lastKey
            .getSize()
            - MIN_KEY_LEN, o.get(), MIN_KEY_LEN, o.getSize() - MIN_KEY_LEN) > 0) {
      incrementPrefix();
    }

    System.arraycopy(prefix, 0, o.get(), 0, MIN_KEY_LEN);
    lastKey.set(o);
  }

  private void fillValue(BytesWritable o) {
    int len = valLenRNG.nextInt();
    o.setSize(len);
    int n = 0;
    while (n < len) {
      byte[] word = dict[random.nextInt(dict.length)];
      int l = Math.min(word.length, len - n);
      System.arraycopy(word, 0, o.get(), n, l);
      n += l;
    }
  }
  
  private void incrementPrefix() {
    for (int i = MIN_KEY_LEN - 1; i >= 0; --i) {
      ++prefix[i];
      if (prefix[i] != 0) return;
    }
    
    throw new RuntimeException("Prefix overflown");
  }
  
  public void next(BytesWritable key, BytesWritable value, boolean dupKey) {
    if (dupKey) {
      key.set(lastKey);
    }
    else {
      fillKey(key);
    }
    fillValue(value);
  }
}
