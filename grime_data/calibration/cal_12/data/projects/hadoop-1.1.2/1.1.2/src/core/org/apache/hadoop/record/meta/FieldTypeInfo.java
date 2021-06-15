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
 * Represents a type information for a field, which is made up of its 
 * ID (name) and its type (a TypeID object).
 */
public class FieldTypeInfo
{

  private String fieldID;
  private TypeID typeID;

  /**
   * Construct a FiledTypeInfo with the given field name and the type
   */
  FieldTypeInfo(String fieldID, TypeID typeID) {
    this.fieldID = fieldID;
    this.typeID = typeID;
  }

  /**
   * get the field's TypeID object
   */
  public TypeID getTypeID() {
    return typeID;
  }
  
  /**
   * get the field's id (name)
   */
  public String getFieldID() {
    return fieldID;
  }
  
  void write(RecordOutput rout, String tag) throws IOException {
    rout.writeString(fieldID, tag);
    typeID.write(rout, tag);
  }
  
  /**
   * Two FieldTypeInfos are equal if ach of their fields matches
   */
  public boolean equals(Object o) {
    if (this == o) 
      return true;
    if (!(o instanceof FieldTypeInfo))
      return false;
    FieldTypeInfo fti = (FieldTypeInfo) o;
    // first check if fieldID matches
    if (!this.fieldID.equals(fti.fieldID)) {
      return false;
    }
    // now see if typeID matches
    return (this.typeID.equals(fti.typeID));
  }
  
  /**
   * We use a basic hashcode implementation, since this class will likely not
   * be used as a hashmap key 
   */
  public int hashCode() {
    return 37*17+typeID.hashCode() + 37*17+fieldID.hashCode();
  }
  

  public boolean equals(FieldTypeInfo ti) {
    // first check if fieldID matches
    if (!this.fieldID.equals(ti.fieldID)) {
      return false;
    }
    // now see if typeID matches
    return (this.typeID.equals(ti.typeID));
  }

}

