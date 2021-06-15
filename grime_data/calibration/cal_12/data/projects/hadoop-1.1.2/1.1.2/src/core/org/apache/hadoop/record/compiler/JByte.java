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
package org.apache.hadoop.record.compiler;

/**
 * Code generator for "byte" type.
 */
public class JByte extends JType {
  
  class JavaByte extends JavaType {
    
    JavaByte() {
      super("byte", "Byte", "Byte", "TypeID.RIOType.BYTE");
    }
    
    String getTypeIDObjectString() {
      return "org.apache.hadoop.record.meta.TypeID.ByteTypeID";
    }

    void genSlurpBytes(CodeBuffer cb, String b, String s, String l) {
      cb.append("{\n");
      cb.append("if ("+l+"<1) {\n");
      cb.append("throw new java.io.IOException(\"Byte is exactly 1 byte."+
                " Provided buffer is smaller.\");\n");
      cb.append("}\n");
      cb.append(s+"++; "+l+"--;\n");
      cb.append("}\n");
    }
    
    void genCompareBytes(CodeBuffer cb) {
      cb.append("{\n");
      cb.append("if (l1<1 || l2<1) {\n");
      cb.append("throw new java.io.IOException(\"Byte is exactly 1 byte."+
                " Provided buffer is smaller.\");\n");
      cb.append("}\n");
      cb.append("if (b1[s1] != b2[s2]) {\n");
      cb.append("return (b1[s1]<b2[s2])?-1:0;\n");
      cb.append("}\n");
      cb.append("s1++; s2++; l1--; l2--;\n");
      cb.append("}\n");
    }
  }
  
  class CppByte extends CppType {
    
    CppByte() {
      super("int8_t");
    }
    
    String getTypeIDObjectString() {
      return "new ::hadoop::TypeID(::hadoop::RIOTYPE_BYTE)";
    }
  }

  public JByte() {
    setJavaType(new JavaByte());
    setCppType(new CppByte());
    setCType(new CType());
  }
  
  String getSignature() {
    return "b";
  }
}
