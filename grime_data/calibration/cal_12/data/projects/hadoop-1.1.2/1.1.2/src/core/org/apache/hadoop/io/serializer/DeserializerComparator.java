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
package org.apache.hadoop.io.serializer;

import java.io.IOException;
import java.util.Comparator;

import org.apache.hadoop.io.InputBuffer;
import org.apache.hadoop.io.RawComparator;

/**
 * <p>
 * A {@link RawComparator} that uses a {@link Deserializer} to deserialize
 * the objects to be compared so that the standard {@link Comparator} can
 * be used to compare them.
 * </p>
 * <p>
 * One may optimize compare-intensive operations by using a custom
 * implementation of {@link RawComparator} that operates directly
 * on byte representations.
 * </p>
 * @param <T>
 */
public abstract class DeserializerComparator<T> implements RawComparator<T> {
  
  private InputBuffer buffer = new InputBuffer();
  private Deserializer<T> deserializer;
  
  private T key1;
  private T key2;

  protected DeserializerComparator(Deserializer<T> deserializer)
    throws IOException {
    
    this.deserializer = deserializer;
    this.deserializer.open(buffer);
  }

  public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
    try {
      
      buffer.reset(b1, s1, l1);
      key1 = deserializer.deserialize(key1);
      
      buffer.reset(b2, s2, l2);
      key2 = deserializer.deserialize(key2);
      
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return compare(key1, key2);
  }

}
