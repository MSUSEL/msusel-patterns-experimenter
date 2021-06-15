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
import java.util.Iterator;
import org.apache.hadoop.record.RecordInput;

/**
 * Various utility functions for Hadooop record I/O platform.
 */
public class Utils {
  
  /** Cannot create a new instance of Utils */
  private Utils() {
  }
  
  /**
   * read/skip bytes from stream based on a type
   */
  public static void skip(RecordInput rin, String tag, TypeID typeID) throws IOException {
    switch (typeID.typeVal) {
    case TypeID.RIOType.BOOL: 
      rin.readBool(tag);
      break;
    case TypeID.RIOType.BUFFER: 
      rin.readBuffer(tag);
      break;
    case TypeID.RIOType.BYTE: 
      rin.readByte(tag);
      break;
    case TypeID.RIOType.DOUBLE: 
      rin.readDouble(tag);
      break;
    case TypeID.RIOType.FLOAT: 
      rin.readFloat(tag);
      break;
    case TypeID.RIOType.INT: 
      rin.readInt(tag);
      break;
    case TypeID.RIOType.LONG: 
      rin.readLong(tag);
      break;
    case TypeID.RIOType.MAP: 
      org.apache.hadoop.record.Index midx1 = rin.startMap(tag);
      MapTypeID mtID = (MapTypeID) typeID;
      for (; !midx1.done(); midx1.incr()) {
        skip(rin, tag, mtID.getKeyTypeID());
        skip(rin, tag, mtID.getValueTypeID());
      }
      rin.endMap(tag);
      break;
    case TypeID.RIOType.STRING: 
      rin.readString(tag);
      break;
    case TypeID.RIOType.STRUCT:
      rin.startRecord(tag);
      // read past each field in the struct
      StructTypeID stID = (StructTypeID) typeID;
      Iterator<FieldTypeInfo> it = stID.getFieldTypeInfos().iterator();
      while (it.hasNext()) {
        FieldTypeInfo tInfo = it.next();
        skip(rin, tag, tInfo.getTypeID());
      }
      rin.endRecord(tag);
      break;
    case TypeID.RIOType.VECTOR: 
      org.apache.hadoop.record.Index vidx1 = rin.startVector(tag);
      VectorTypeID vtID = (VectorTypeID) typeID;
      for (; !vidx1.done(); vidx1.incr()) {
        skip(rin, tag, vtID.getElementTypeID());
      }
      rin.endVector(tag);
      break;
    default: 
      // shouldn't be here
      throw new IOException("Unknown typeID when skipping bytes");
    }
  }
}
