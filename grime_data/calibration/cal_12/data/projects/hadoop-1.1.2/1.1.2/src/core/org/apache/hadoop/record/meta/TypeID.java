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
package org.apache.hadoop.record.meta;

import java.io.IOException;
import org.apache.hadoop.record.RecordOutput;

/** 
 * Represents typeID for basic types. 
 */
public class TypeID {

  /**
   * constants representing the IDL types we support
   */
  public static final class RIOType {
    public static final byte BOOL   = 1;
    public static final byte BUFFER = 2;
    public static final byte BYTE   = 3;
    public static final byte DOUBLE = 4;
    public static final byte FLOAT  = 5;
    public static final byte INT    = 6;
    public static final byte LONG   = 7;
    public static final byte MAP    = 8;
    public static final byte STRING = 9;
    public static final byte STRUCT = 10;
    public static final byte VECTOR = 11;
  }

  /**
   * Constant classes for the basic types, so we can share them.
   */
  public static final TypeID BoolTypeID = new TypeID(RIOType.BOOL);
  public static final TypeID BufferTypeID = new TypeID(RIOType.BUFFER);
  public static final TypeID ByteTypeID = new TypeID(RIOType.BYTE);
  public static final TypeID DoubleTypeID = new TypeID(RIOType.DOUBLE);
  public static final TypeID FloatTypeID = new TypeID(RIOType.FLOAT);
  public static final TypeID IntTypeID = new TypeID(RIOType.INT);
  public static final TypeID LongTypeID = new TypeID(RIOType.LONG);
  public static final TypeID StringTypeID = new TypeID(RIOType.STRING);
  
  protected byte typeVal;

  /**
   * Create a TypeID object 
   */
  TypeID(byte typeVal) {
    this.typeVal = typeVal;
  }

  /**
   * Get the type value. One of the constants in RIOType.
   */
  public byte getTypeVal() {
    return typeVal;
  }

  /**
   * Serialize the TypeID object
   */
  void write(RecordOutput rout, String tag) throws IOException {
    rout.writeByte(typeVal, tag);
  }
  
  /**
   * Two base typeIDs are equal if they refer to the same type
   */
  public boolean equals(Object o) {
    if (this == o) 
      return true;

    if (o == null)
      return false;

    if (this.getClass() != o.getClass())
      return false;

    TypeID oTypeID = (TypeID) o;
    return (this.typeVal == oTypeID.typeVal);
  }
  
  /**
   * We use a basic hashcode implementation, since this class will likely not
   * be used as a hashmap key 
   */
  public int hashCode() {
    // See 'Effectve Java' by Joshua Bloch
    return 37*17+(int)typeVal;
  }
}

