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
import java.util.TreeMap;
import java.util.ArrayList;

/**
 * Interface that alll the serializers have to implement.
 */
public interface RecordOutput {
  /**
   * Write a byte to serialized record.
   * @param b Byte to be serialized
   * @param tag Used by tagged serialization formats (such as XML)
   * @throws IOException Indicates error in serialization
   */
  public void writeByte(byte b, String tag) throws IOException;
  
  /**
   * Write a boolean to serialized record.
   * @param b Boolean to be serialized
   * @param tag Used by tagged serialization formats (such as XML)
   * @throws IOException Indicates error in serialization
   */
  public void writeBool(boolean b, String tag) throws IOException;
  
  /**
   * Write an integer to serialized record.
   * @param i Integer to be serialized
   * @param tag Used by tagged serialization formats (such as XML)
   * @throws IOException Indicates error in serialization
   */
  public void writeInt(int i, String tag) throws IOException;
  
  /**
   * Write a long integer to serialized record.
   * @param l Long to be serialized
   * @param tag Used by tagged serialization formats (such as XML)
   * @throws IOException Indicates error in serialization
   */
  public void writeLong(long l, String tag) throws IOException;
  
  /**
   * Write a single-precision float to serialized record.
   * @param f Float to be serialized
   * @param tag Used by tagged serialization formats (such as XML)
   * @throws IOException Indicates error in serialization
   */
  public void writeFloat(float f, String tag) throws IOException;
  
  /**
   * Write a double precision floating point number to serialized record.
   * @param d Double to be serialized
   * @param tag Used by tagged serialization formats (such as XML)
   * @throws IOException Indicates error in serialization
   */
  public void writeDouble(double d, String tag) throws IOException;
  
  /**
   * Write a unicode string to serialized record.
   * @param s String to be serialized
   * @param tag Used by tagged serialization formats (such as XML)
   * @throws IOException Indicates error in serialization
   */
  public void writeString(String s, String tag) throws IOException;
  
  /**
   * Write a buffer to serialized record.
   * @param buf Buffer to be serialized
   * @param tag Used by tagged serialization formats (such as XML)
   * @throws IOException Indicates error in serialization
   */
  public void writeBuffer(Buffer buf, String tag)
    throws IOException;
  
  /**
   * Mark the start of a record to be serialized.
   * @param r Record to be serialized
   * @param tag Used by tagged serialization formats (such as XML)
   * @throws IOException Indicates error in serialization
   */
  public void startRecord(Record r, String tag) throws IOException;
  
  /**
   * Mark the end of a serialized record.
   * @param r Record to be serialized
   * @param tag Used by tagged serialization formats (such as XML)
   * @throws IOException Indicates error in serialization
   */
  public void endRecord(Record r, String tag) throws IOException;
  
  /**
   * Mark the start of a vector to be serialized.
   * @param v Vector to be serialized
   * @param tag Used by tagged serialization formats (such as XML)
   * @throws IOException Indicates error in serialization
   */
  public void startVector(ArrayList v, String tag) throws IOException;
  
  /**
   * Mark the end of a serialized vector.
   * @param v Vector to be serialized
   * @param tag Used by tagged serialization formats (such as XML)
   * @throws IOException Indicates error in serialization
   */
  public void endVector(ArrayList v, String tag) throws IOException;
  
  /**
   * Mark the start of a map to be serialized.
   * @param m Map to be serialized
   * @param tag Used by tagged serialization formats (such as XML)
   * @throws IOException Indicates error in serialization
   */
  public void startMap(TreeMap m, String tag) throws IOException;
  
  /**
   * Mark the end of a serialized map.
   * @param m Map to be serialized
   * @param tag Used by tagged serialization formats (such as XML)
   * @throws IOException Indicates error in serialization
   */
  public void endMap(TreeMap m, String tag) throws IOException;
}
