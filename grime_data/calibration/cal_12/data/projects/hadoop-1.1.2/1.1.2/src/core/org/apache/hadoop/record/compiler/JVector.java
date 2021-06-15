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

import java.util.Map;

/**
 */
public class JVector extends JCompType {
  
  static private int level = 0;
  
  static private String getId(String id) { return id+getLevel(); }
  
  static private String getLevel() { return Integer.toString(level); }
  
  static private void incrLevel() { level++; }
  
  static private void decrLevel() { level--; }
  
  private JType type;
  
  class JavaVector extends JavaCompType {
    
    private JType.JavaType element;
    
    JavaVector(JType.JavaType t) {
      super("java.util.ArrayList<"+t.getWrapperType()+">",
            "Vector", "java.util.ArrayList<"+t.getWrapperType()+">",
            "TypeID.RIOType.VECTOR");
      element = t;
    }
    
    String getTypeIDObjectString() {
      return "new org.apache.hadoop.record.meta.VectorTypeID(" + 
      element.getTypeIDObjectString() + ")";
    }

    void genSetRTIFilter(CodeBuffer cb, Map<String, Integer> nestedStructMap) {
      element.genSetRTIFilter(cb, nestedStructMap);
    }

    void genCompareTo(CodeBuffer cb, String fname, String other) {
      cb.append("{\n");
      incrLevel();
      cb.append("int "+getId(Consts.RIO_PREFIX + "len1")+" = "+fname+
          ".size();\n");
      cb.append("int "+getId(Consts.RIO_PREFIX + "len2")+" = "+other+
          ".size();\n");
      cb.append("for(int "+getId(Consts.RIO_PREFIX + "vidx")+" = 0; "+
          getId(Consts.RIO_PREFIX + "vidx")+"<"+getId(Consts.RIO_PREFIX + "len1")+
          " && "+getId(Consts.RIO_PREFIX + "vidx")+"<"+
          getId(Consts.RIO_PREFIX + "len2")+"; "+
          getId(Consts.RIO_PREFIX + "vidx")+"++) {\n");
      cb.append(element.getType()+" "+getId(Consts.RIO_PREFIX + "e1")+
                " = "+fname+
                ".get("+getId(Consts.RIO_PREFIX + "vidx")+");\n");
      cb.append(element.getType()+" "+getId(Consts.RIO_PREFIX + "e2")+
                " = "+other+
                ".get("+getId(Consts.RIO_PREFIX + "vidx")+");\n");
      element.genCompareTo(cb, getId(Consts.RIO_PREFIX + "e1"), 
          getId(Consts.RIO_PREFIX + "e2"));
      cb.append("if (" + Consts.RIO_PREFIX + "ret != 0) { return " +
          Consts.RIO_PREFIX + "ret; }\n");
      cb.append("}\n");
      cb.append(Consts.RIO_PREFIX + "ret = ("+getId(Consts.RIO_PREFIX + "len1")+
          " - "+getId(Consts.RIO_PREFIX + "len2")+");\n");
      decrLevel();
      cb.append("}\n");
    }
    
    void genReadMethod(CodeBuffer cb, String fname, String tag, boolean decl) {
      if (decl) {
        cb.append(getType()+" "+fname+";\n");
      }
      cb.append("{\n");
      incrLevel();
      cb.append("org.apache.hadoop.record.Index "+
          getId(Consts.RIO_PREFIX + "vidx")+" = " + 
          Consts.RECORD_INPUT + ".startVector(\""+tag+"\");\n");
      cb.append(fname+"=new "+getType()+"();\n");
      cb.append("for (; !"+getId(Consts.RIO_PREFIX + "vidx")+".done(); " + 
          getId(Consts.RIO_PREFIX + "vidx")+".incr()) {\n");
      element.genReadMethod(cb, getId(Consts.RIO_PREFIX + "e"), 
          getId(Consts.RIO_PREFIX + "e"), true);
      cb.append(fname+".add("+getId(Consts.RIO_PREFIX + "e")+");\n");
      cb.append("}\n");
      cb.append(Consts.RECORD_INPUT + ".endVector(\""+tag+"\");\n");
      decrLevel();
      cb.append("}\n");
    }
    
    void genWriteMethod(CodeBuffer cb, String fname, String tag) {
      cb.append("{\n");
      incrLevel();
      cb.append(Consts.RECORD_OUTPUT + ".startVector("+fname+",\""+tag+"\");\n");
      cb.append("int "+getId(Consts.RIO_PREFIX + "len")+" = "+fname+".size();\n");
      cb.append("for(int "+getId(Consts.RIO_PREFIX + "vidx")+" = 0; " + 
          getId(Consts.RIO_PREFIX + "vidx")+"<"+getId(Consts.RIO_PREFIX + "len")+
          "; "+getId(Consts.RIO_PREFIX + "vidx")+"++) {\n");
      cb.append(element.getType()+" "+getId(Consts.RIO_PREFIX + "e")+" = "+
          fname+".get("+getId(Consts.RIO_PREFIX + "vidx")+");\n");
      element.genWriteMethod(cb, getId(Consts.RIO_PREFIX + "e"), 
          getId(Consts.RIO_PREFIX + "e"));
      cb.append("}\n");
      cb.append(Consts.RECORD_OUTPUT + ".endVector("+fname+",\""+tag+"\");\n");
      cb.append("}\n");
      decrLevel();
    }
    
    void genSlurpBytes(CodeBuffer cb, String b, String s, String l) {
      cb.append("{\n");
      incrLevel();
      cb.append("int "+getId("vi")+
                " = org.apache.hadoop.record.Utils.readVInt("+b+", "+s+");\n");
      cb.append("int "+getId("vz")+
                " = org.apache.hadoop.record.Utils.getVIntSize("+getId("vi")+");\n");
      cb.append(s+"+="+getId("vz")+"; "+l+"-="+getId("vz")+";\n");
      cb.append("for (int "+getId("vidx")+" = 0; "+getId("vidx")+
                " < "+getId("vi")+"; "+getId("vidx")+"++)");
      element.genSlurpBytes(cb, b, s, l);
      decrLevel();
      cb.append("}\n");
    }
    
    void genCompareBytes(CodeBuffer cb) {
      cb.append("{\n");
      incrLevel();
      cb.append("int "+getId("vi1")+
                " = org.apache.hadoop.record.Utils.readVInt(b1, s1);\n");
      cb.append("int "+getId("vi2")+
                " = org.apache.hadoop.record.Utils.readVInt(b2, s2);\n");
      cb.append("int "+getId("vz1")+
                " = org.apache.hadoop.record.Utils.getVIntSize("+getId("vi1")+");\n");
      cb.append("int "+getId("vz2")+
                " = org.apache.hadoop.record.Utils.getVIntSize("+getId("vi2")+");\n");
      cb.append("s1+="+getId("vz1")+"; s2+="+getId("vz2")+
                "; l1-="+getId("vz1")+"; l2-="+getId("vz2")+";\n");
      cb.append("for (int "+getId("vidx")+" = 0; "+getId("vidx")+
                " < "+getId("vi1")+" && "+getId("vidx")+" < "+getId("vi2")+
                "; "+getId("vidx")+"++)");
      element.genCompareBytes(cb);
      cb.append("if ("+getId("vi1")+" != "+getId("vi2")+
                ") { return ("+getId("vi1")+"<"+getId("vi2")+")?-1:0; }\n");
      decrLevel();
      cb.append("}\n");
    }
  }
  
  class CppVector extends CppCompType {
    
    private JType.CppType element;
    
    CppVector(JType.CppType t) {
      super("::std::vector< "+t.getType()+" >");
      element = t;
    }
    
    String getTypeIDObjectString() {
      return "new ::hadoop::VectorTypeID(" +    
      element.getTypeIDObjectString() + ")";
    }

    void genSetRTIFilter(CodeBuffer cb) {
      element.genSetRTIFilter(cb);
    }

  }
  
  /** Creates a new instance of JVector */
  public JVector(JType t) {
    type = t;
    setJavaType(new JavaVector(t.getJavaType()));
    setCppType(new CppVector(t.getCppType()));
    setCType(new CCompType());
  }
  
  String getSignature() {
    return "[" + type.getSignature() + "]";
  }
}
