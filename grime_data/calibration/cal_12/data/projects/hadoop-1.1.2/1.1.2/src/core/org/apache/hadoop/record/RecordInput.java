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
package org.apache.hadoop.record;

import java.io.IOException;

/**
 * Interface that all the Deserializers have to implement.
 */
public interface RecordInput {
  /**
   * Read a byte from serialized record.
   * @param tag Used by tagged serialization formats (such as XML)
   * @return value read from serialized record.
   */
  byte readByte(String tag) throws IOException;
  
  /**
   * Read a boolean from serialized record.
   * @param tag Used by tagged serialization formats (such as XML)
   * @return value read from serialized record.
   */
  boolean readBool(String tag) throws IOException;
  
  /**
   * Read an integer from serialized record.
   * @param tag Used by tagged serialization formats (such as XML)
   * @return value read from serialized record.
   */
  int readInt(String tag) throws IOException;
  
  /**
   * Read a long integer from serialized record.
   * @param tag Used by tagged serialization formats (such as XML)
   * @return value read from serialized record.
   */
  long readLong(String tag) throws IOException;
  
  /**
   * Read a single-precision float from serialized record.
   * @param tag Used by tagged serialization formats (such as XML)
   * @return value read from serialized record.
   */
  float readFloat(String tag) throws IOException;
  
  /**
   * Read a double-precision number from serialized record.
   * @param tag Used by tagged serialization formats (such as XML)
   * @return value read from serialized record.
   */
  double readDouble(String tag) throws IOException;
  
  /**
   * Read a UTF-8 encoded string from serialized record.
   * @param tag Used by tagged serialization formats (such as XML)
   * @return value read from serialized record.
   */
  String readString(String tag) throws IOException;
  
  /**
   * Read byte array from serialized record.
   * @param tag Used by tagged serialization formats (such as XML)
   * @return value read from serialized record.
   */
  Buffer readBuffer(String tag) throws IOException;
  
  /**
   * Check the mark for start of the serialized record.
   * @param tag Used by tagged serialization formats (such as XML)
   */
  void startRecord(String tag) throws IOException;
  
  /**
   * Check the mark for end of the serialized record.
   * @param tag Used by tagged serialization formats (such as XML)
   */
  void endRecord(String tag) throws IOException;
  
  /**
   * Check the mark for start of the serialized vector.
   * @param tag Used by tagged serialization formats (such as XML)
   * @return Index that is used to count the number of elements.
   */
  Index startVector(String tag) throws IOException;
  
  /**
   * Check the mark for end of the serialized vector.
   * @param tag Used by tagged serialization formats (such as XML)
   */
  void endVector(String tag) throws IOException;
  
  /**
   * Check the mark for start of the serialized map.
   * @param tag Used by tagged serialization formats (such as XML)
   * @return Index that is used to count the number of map entries.
   */
  Index startMap(String tag) throws IOException;
  
  /**
   * Check the mark for end of the serialized map.
   * @param tag Used by tagged serialization formats (such as XML)
   */
  void endMap(String tag) throws IOException;
}
