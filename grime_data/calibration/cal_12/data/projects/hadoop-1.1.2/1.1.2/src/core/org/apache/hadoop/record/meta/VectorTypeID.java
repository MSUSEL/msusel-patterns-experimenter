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
 * Represents typeID for vector. 
 */
public class VectorTypeID extends TypeID {
  private TypeID typeIDElement; 
  
  public VectorTypeID(TypeID typeIDElement) {
    super(RIOType.VECTOR);
    this.typeIDElement = typeIDElement;
  }
  
  public TypeID getElementTypeID() {
    return this.typeIDElement;
  }
  
  void write(RecordOutput rout, String tag) throws IOException {
    rout.writeByte(typeVal, tag);
    typeIDElement.write(rout, tag);
  }
  
  /**
   * Two vector typeIDs are equal if their constituent elements have the 
   * same type
   */
  public boolean equals(Object o) {
    if (!super.equals (o))
      return false;

    VectorTypeID vti = (VectorTypeID) o;
    return this.typeIDElement.equals(vti.typeIDElement);
  }
  
  /**
   * We use a basic hashcode implementation, since this class will likely not
   * be used as a hashmap key 
   */
  public int hashCode() {
    return 37*17+typeIDElement.hashCode();
  }
  
}
