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
 */
public class JDouble extends JType {
  
  class JavaDouble extends JavaType {
    
    JavaDouble() {
      super("double", "Double", "Double", "TypeID.RIOType.DOUBLE");
    }
    
    String getTypeIDObjectString() {
      return "org.apache.hadoop.record.meta.TypeID.DoubleTypeID";
    }

    void genHashCode(CodeBuffer cb, String fname) {
      String tmp = "Double.doubleToLongBits("+fname+")";
      cb.append(Consts.RIO_PREFIX + "ret = (int)("+tmp+"^("+tmp+">>>32));\n");
    }
    
    void genSlurpBytes(CodeBuffer cb, String b, String s, String l) {
      cb.append("{\n");
      cb.append("if ("+l+"<8) {\n");
      cb.append("throw new java.io.IOException(\"Double is exactly 8 bytes."+
                " Provided buffer is smaller.\");\n");
      cb.append("}\n");
      cb.append(s+"+=8; "+l+"-=8;\n");
      cb.append("}\n");
    }
    
    void genCompareBytes(CodeBuffer cb) {
      cb.append("{\n");
      cb.append("if (l1<8 || l2<8) {\n");
      cb.append("throw new java.io.IOException(\"Double is exactly 8 bytes."+
                " Provided buffer is smaller.\");\n");
      cb.append("}\n");
      cb.append("double d1 = org.apache.hadoop.record.Utils.readDouble(b1, s1);\n");
      cb.append("double d2 = org.apache.hadoop.record.Utils.readDouble(b2, s2);\n");
      cb.append("if (d1 != d2) {\n");
      cb.append("return ((d1-d2) < 0) ? -1 : 0;\n");
      cb.append("}\n");
      cb.append("s1+=8; s2+=8; l1-=8; l2-=8;\n");
      cb.append("}\n");
    }
  }

  class CppDouble extends CppType {
    
    CppDouble() {
      super("double");
    }
    
    String getTypeIDObjectString() {
      return "new ::hadoop::TypeID(::hadoop::RIOTYPE_DOUBLE)";
    }
  }

  
  /** Creates a new instance of JDouble */
  public JDouble() {
    setJavaType(new JavaDouble());
    setCppType(new CppDouble());
    setCType(new CType());
  }
  
  String getSignature() {
    return "d";
  }
}
