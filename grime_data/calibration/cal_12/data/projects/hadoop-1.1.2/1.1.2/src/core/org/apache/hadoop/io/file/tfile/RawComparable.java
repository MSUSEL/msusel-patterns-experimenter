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

import java.util.Collections;
import java.util.Comparator;

import org.apache.hadoop.io.RawComparator;

/**
 * Interface for objects that can be compared through {@link RawComparator}.
 * This is useful in places where we need a single object reference to specify a
 * range of bytes in a byte array, such as {@link Comparable} or
 * {@link Collections#binarySearch(java.util.List, Object, Comparator)}
 * 
 * The actual comparison among RawComparable's requires an external
 * RawComparator and it is applications' responsibility to ensure two
 * RawComparable are supposed to be semantically comparable with the same
 * RawComparator.
 */
public interface RawComparable {
  /**
   * Get the underlying byte array.
   * 
   * @return The underlying byte array.
   */
  abstract byte[] buffer();

  /**
   * Get the offset of the first byte in the byte array.
   * 
   * @return The offset of the first byte in the byte array.
   */
  abstract int offset();

  /**
   * Get the size of the byte range in the byte array.
   * 
   * @return The size of the byte range in the byte array.
   */
  abstract int size();
}
