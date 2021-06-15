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
import java.util.*;

import org.apache.hadoop.record.RecordInput;
import org.apache.hadoop.record.RecordOutput;

/** 
 * Represents typeID for a struct 
 */
public class StructTypeID extends TypeID {
  private ArrayList<FieldTypeInfo> typeInfos = new ArrayList<FieldTypeInfo>();
  
  StructTypeID() {
    super(RIOType.STRUCT);
  }
  
  /**
   * Create a StructTypeID based on the RecordTypeInfo of some record
   */
  public StructTypeID(RecordTypeInfo rti) {
    super(RIOType.STRUCT);
    typeInfos.addAll(rti.getFieldTypeInfos());
  }

  void add (FieldTypeInfo ti) {
    typeInfos.add(ti);
  }
  
  public Collection<FieldTypeInfo> getFieldTypeInfos() {
    return typeInfos;
  }
  
  /* 
   * return the StructTypeiD, if any, of the given field 
   */
  StructTypeID findStruct(String name) {
    // walk through the list, searching. Not the most efficient way, but this
    // in intended to be used rarely, so we keep it simple. 
    // As an optimization, we can keep a hashmap of record name to its RTI, for later.
    for (FieldTypeInfo ti : typeInfos) {
      if ((0 == ti.getFieldID().compareTo(name)) && (ti.getTypeID().getTypeVal() == RIOType.STRUCT)) {
        return (StructTypeID) ti.getTypeID();
      }
    }
    return null;
  }
  
  void write(RecordOutput rout, String tag) throws IOException {
    rout.writeByte(typeVal, tag);
    writeRest(rout, tag);
  }

  /* 
   * Writes rest of the struct (excluding type value).
   * As an optimization, this method is directly called by RTI 
   * for the top level record so that we don't write out the byte
   * indicating that this is a struct (since top level records are
   * always structs).
   */
  void writeRest(RecordOutput rout, String tag) throws IOException {
    rout.writeInt(typeInfos.size(), tag);
    for (FieldTypeInfo ti : typeInfos) {
      ti.write(rout, tag);
    }
  }

  /* 
   * deserialize ourselves. Called by RTI. 
   */
  void read(RecordInput rin, String tag) throws IOException {
    // number of elements
    int numElems = rin.readInt(tag);
    for (int i=0; i<numElems; i++) {
      typeInfos.add(genericReadTypeInfo(rin, tag));
    }
  }
  
  // generic reader: reads the next TypeInfo object from stream and returns it
  private FieldTypeInfo genericReadTypeInfo(RecordInput rin, String tag) throws IOException {
    String fieldName = rin.readString(tag);
    TypeID id = genericReadTypeID(rin, tag);
    return new FieldTypeInfo(fieldName, id);
  }
  
  // generic reader: reads the next TypeID object from stream and returns it
  private TypeID genericReadTypeID(RecordInput rin, String tag) throws IOException {
    byte typeVal = rin.readByte(tag);
    switch (typeVal) {
    case TypeID.RIOType.BOOL: 
      return TypeID.BoolTypeID;
    case TypeID.RIOType.BUFFER: 
      return TypeID.BufferTypeID;
    case TypeID.RIOType.BYTE:
      return TypeID.ByteTypeID;
    case TypeID.RIOType.DOUBLE:
      return TypeID.DoubleTypeID;
    case TypeID.RIOType.FLOAT:
      return TypeID.FloatTypeID;
    case TypeID.RIOType.INT: 
      return TypeID.IntTypeID;
    case TypeID.RIOType.LONG:
      return TypeID.LongTypeID;
    case TypeID.RIOType.MAP:
    {
      TypeID tIDKey = genericReadTypeID(rin, tag);
      TypeID tIDValue = genericReadTypeID(rin, tag);
      return new MapTypeID(tIDKey, tIDValue);
    }
    case TypeID.RIOType.STRING: 
      return TypeID.StringTypeID;
    case TypeID.RIOType.STRUCT: 
    {
      StructTypeID stID = new StructTypeID();
      int numElems = rin.readInt(tag);
      for (int i=0; i<numElems; i++) {
        stID.add(genericReadTypeInfo(rin, tag));
      }
      return stID;
    }
    case TypeID.RIOType.VECTOR: 
    {
      TypeID tID = genericReadTypeID(rin, tag);
      return new VectorTypeID(tID);
    }
    default:
      // shouldn't be here
      throw new IOException("Unknown type read");
    }
  }

}
