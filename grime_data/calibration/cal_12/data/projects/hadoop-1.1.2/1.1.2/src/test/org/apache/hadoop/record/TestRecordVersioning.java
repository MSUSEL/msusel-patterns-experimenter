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
import junit.framework.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.TreeMap;
import org.apache.hadoop.record.meta.RecordTypeInfo;

/**
 */
public class TestRecordVersioning extends TestCase {
    
  public TestRecordVersioning(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
    
  /* 
   * basic versioning
   * write out a record and its type info, read it back using its typeinfo
   */
  public void testBasic() {
    File tmpfile, tmpRTIfile;
    try {
      tmpfile = File.createTempFile("hadooprec", ".dat");
      tmpRTIfile = File.createTempFile("hadooprti", ".dat");
      FileOutputStream ostream = new FileOutputStream(tmpfile);
      BinaryRecordOutput out = new BinaryRecordOutput(ostream);
      FileOutputStream oRTIstream = new FileOutputStream(tmpRTIfile);
      BinaryRecordOutput outRTI = new BinaryRecordOutput(oRTIstream);
      RecRecord1 r1 = new RecRecord1();
      r1.setBoolVal(true);
      r1.setByteVal((byte)0x66);
      r1.setFloatVal(3.145F);
      r1.setDoubleVal(1.5234);
      r1.setIntVal(-4567);
      r1.setLongVal(-2367L);
      r1.setStringVal("random text");
      r1.setBufferVal(new Buffer());
      r1.setVectorVal(new ArrayList<String>());
      r1.setMapVal(new TreeMap<String,String>());
      RecRecord0 r0 = new RecRecord0();
      r0.setStringVal("other random text");
      r1.setRecordVal(r0);
      r1.serialize(out, "");
      ostream.close();
      // write out the type info
      RecRecord1.getTypeInfo().serialize(outRTI);
      oRTIstream.close();
      
      // read
      FileInputStream istream = new FileInputStream(tmpfile);
      BinaryRecordInput in = new BinaryRecordInput(istream);
      FileInputStream iRTIstream = new FileInputStream(tmpRTIfile);
      BinaryRecordInput inRTI = new BinaryRecordInput(iRTIstream);
      RecordTypeInfo rti = new RecordTypeInfo();
      rti.deserialize(inRTI);
      iRTIstream.close();
      RecRecord1.setTypeFilter(rti);
      RecRecord1 r2 = new RecRecord1();
      r2.deserialize(in, "");
      istream.close();
      tmpfile.delete();
      tmpRTIfile.delete();
      assertTrue("Serialized and deserialized versioned records do not match.", r1.equals(r2));
    } catch (IOException ex) {
      ex.printStackTrace();
    } 
  }
    
  /* 
   * versioning
   * write out a record and its type info, read back a similar record using the written record's typeinfo
   */
  public void testVersioning() {
    File tmpfile, tmpRTIfile;
    try {
      tmpfile = File.createTempFile("hadooprec", ".dat");
      tmpRTIfile = File.createTempFile("hadooprti", ".dat");
      FileOutputStream ostream = new FileOutputStream(tmpfile);
      BinaryRecordOutput out = new BinaryRecordOutput(ostream);
      FileOutputStream oRTIstream = new FileOutputStream(tmpRTIfile);
      BinaryRecordOutput outRTI = new BinaryRecordOutput(oRTIstream);

      // we create an array of records to write
      ArrayList<RecRecordOld> recsWrite = new ArrayList<RecRecordOld>();
      int i, j, k, l;
      for (i=0; i<5; i++) {
        RecRecordOld s1Rec = new RecRecordOld();

        s1Rec.setName("This is record s1: " + i);

        ArrayList<Long> iA = new ArrayList<Long>();
        for (j=0; j<3; j++) {
          iA.add(new Long(i+j));
        }
        s1Rec.setIvec(iA);

        ArrayList<ArrayList<RecRecord0>> ssVec = new ArrayList<ArrayList<RecRecord0>>();
        for (j=0; j<2; j++) {
          ArrayList<RecRecord0> sVec = new ArrayList<RecRecord0>();
          for (k=0; k<3; k++) {
            RecRecord0 sRec = new RecRecord0("This is record s: ("+j+": "+k+")");
            sVec.add(sRec);
          }
          ssVec.add(sVec);
        }
        s1Rec.setSvec(ssVec);

        s1Rec.setInner(new RecRecord0("This is record s: " + i));

        ArrayList<ArrayList<ArrayList<String>>> aaaVec = new ArrayList<ArrayList<ArrayList<String>>>();
        for (l=0; l<2; l++) {
          ArrayList<ArrayList<String>> aaVec = new ArrayList<ArrayList<String>>();
          for (j=0; j<2; j++) {
            ArrayList<String> aVec = new ArrayList<String>();
            for (k=0; k<3; k++) {
              aVec.add(new String("THis is a nested string: (" + l + ": " + j + ": " + k + ")"));
            }
            aaVec.add(aVec);
          }
          aaaVec.add(aaVec);
        }
        s1Rec.setStrvec(aaaVec);

        s1Rec.setI1(100+i);

        java.util.TreeMap<Byte,String> map1 = new java.util.TreeMap<Byte,String>();
        map1.put(new Byte("23"), "23");
        map1.put(new Byte("11"), "11");
        s1Rec.setMap1(map1);

        java.util.TreeMap<Integer,Long> m1 = new java.util.TreeMap<Integer,Long>();
        java.util.TreeMap<Integer,Long> m2 = new java.util.TreeMap<Integer,Long>();
        m1.put(new Integer(5), 5L);
        m1.put(new Integer(10), 10L);
        m2.put(new Integer(15), 15L);
        m2.put(new Integer(20), 20L);
        java.util.ArrayList<java.util.TreeMap<Integer,Long>> vm1 = new java.util.ArrayList<java.util.TreeMap<Integer,Long>>();
        vm1.add(m1);
        vm1.add(m2);
        s1Rec.setMvec1(vm1);
        java.util.ArrayList<java.util.TreeMap<Integer,Long>> vm2 = new java.util.ArrayList<java.util.TreeMap<Integer,Long>>();
        vm2.add(m1);
        s1Rec.setMvec2(vm2);

        // add to our list
        recsWrite.add(s1Rec);
      }

      // write out to file
      for (RecRecordOld rec: recsWrite) {
        rec.serialize(out);
      }
      ostream.close();
      // write out the type info
      RecRecordOld.getTypeInfo().serialize(outRTI);
      oRTIstream.close();

      // read
      FileInputStream istream = new FileInputStream(tmpfile);
      BinaryRecordInput in = new BinaryRecordInput(istream);
      FileInputStream iRTIstream = new FileInputStream(tmpRTIfile);
      BinaryRecordInput inRTI = new BinaryRecordInput(iRTIstream);
      RecordTypeInfo rti = new RecordTypeInfo();

      // read type info
      rti.deserialize(inRTI);
      iRTIstream.close();
      RecRecordNew.setTypeFilter(rti);

      // read records
      ArrayList<RecRecordNew> recsRead = new ArrayList<RecRecordNew>();
      for (i=0; i<recsWrite.size(); i++) {
        RecRecordNew s2Rec = new RecRecordNew();
        s2Rec.deserialize(in);
        recsRead.add(s2Rec);
      }
      istream.close();
      tmpfile.delete();
      tmpRTIfile.delete();

      // compare
      for (i=0; i<recsRead.size(); i++) {
        RecRecordOld s1Out = recsWrite.get(i);
        RecRecordNew s2In = recsRead.get(i);
        assertTrue("Incorrectly read name2 field", null == s2In.getName2());
        assertTrue("Error comparing inner fields", (0 == s1Out.getInner().compareTo(s2In.getInner())));
        assertTrue("Incorrectly read ivec field", null == s2In.getIvec());
        assertTrue("Incorrectly read svec field", null == s2In.getSvec());
        for (j=0; j<s2In.getStrvec().size(); j++) {
          ArrayList<ArrayList<String>> ss2Vec = s2In.getStrvec().get(j);
          ArrayList<ArrayList<String>> ss1Vec = s1Out.getStrvec().get(j);
          for (k=0; k<ss2Vec.size(); k++) {
            ArrayList<String> s2Vec = ss2Vec.get(k);
            ArrayList<String> s1Vec = ss1Vec.get(k);
            for (l=0; l<s2Vec.size(); l++) {
              assertTrue("Error comparing strVec fields", (0 == s2Vec.get(l).compareTo(s1Vec.get(l))));
            }
          }
        }
        assertTrue("Incorrectly read map1 field", null == s2In.getMap1());
        for (j=0; j<s2In.getMvec2().size(); j++) {
          assertTrue("Error comparing mvec2 fields", (s2In.getMvec2().get(j).equals(s1Out.getMvec2().get(j))));
        }
      }

    } catch (IOException ex) {
      ex.printStackTrace();
    } 
  }

}
