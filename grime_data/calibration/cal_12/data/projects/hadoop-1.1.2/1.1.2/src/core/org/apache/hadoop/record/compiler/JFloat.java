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
public class JFloat extends JType {
  
  class JavaFloat extends JavaType {
    
    JavaFloat() {
      super("float", "Float", "Float", "TypeID.RIOType.FLOAT");
    }
    
    String getTypeIDObjectString() {
      return "org.apache.hadoop.record.meta.TypeID.FloatTypeID";
    }

    void genHashCode(CodeBuffer cb, String fname) {
      cb.append(Consts.RIO_PREFIX + "ret = Float.floatToIntBits("+fname+");\n");
    }
    
    void genSlurpBytes(CodeBuffer cb, String b, String s, String l) {
      cb.append("{\n");
      cb.append("if ("+l+"<4) {\n");
      cb.append("throw new java.io.IOException(\"Float is exactly 4 bytes."+
                " Provided buffer is smaller.\");\n");
      cb.append("}\n");
      cb.append(s+"+=4; "+l+"-=4;\n");
      cb.append("}\n");
    }
    
    void genCompareBytes(CodeBuffer cb) {
      cb.append("{\n");
      cb.append("if (l1<4 || l2<4) {\n");
      cb.append("throw new java.io.IOException(\"Float is exactly 4 bytes."+
                " Provided buffer is smaller.\");\n");
      cb.append("}\n");
      cb.append("float f1 = org.apache.hadoop.record.Utils.readFloat(b1, s1);\n");
      cb.append("float f2 = org.apache.hadoop.record.Utils.readFloat(b2, s2);\n");
      cb.append("if (f1 != f2) {\n");
      cb.append("return ((f1-f2) < 0) ? -1 : 0;\n");
      cb.append("}\n");
      cb.append("s1+=4; s2+=4; l1-=4; l2-=4;\n");
      cb.append("}\n");
    }
  }

  class CppFloat extends CppType {
    
    CppFloat() {
      super("float");
    }
    
    String getTypeIDObjectString() {
      return "new ::hadoop::TypeID(::hadoop::RIOTYPE_FLOAT)";
    }
  }

  /** Creates a new instance of JFloat */
  public JFloat() {
    setJavaType(new JavaFloat());
    setCppType(new CppFloat());
    setCType(new CType());
  }
  
  String getSignature() {
    return "f";
  }
}
