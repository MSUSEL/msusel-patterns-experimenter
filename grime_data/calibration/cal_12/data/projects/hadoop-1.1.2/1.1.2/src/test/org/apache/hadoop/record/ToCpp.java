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
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 */
public class ToCpp extends TestCase {
    
  public ToCpp(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
    
  public void testBinary() {
    File tmpfile;
    try {
      tmpfile = new File("/tmp/hadooptemp.dat");
      FileOutputStream ostream = new FileOutputStream(tmpfile);
      BinaryRecordOutput out = new BinaryRecordOutput(ostream);
      RecRecord1 r1 = new RecRecord1();
      r1.setBoolVal(true);
      r1.setByteVal((byte)0x66);
      r1.setFloatVal(3.145F);
      r1.setDoubleVal(1.5234);
      r1.setIntVal(4567);
      r1.setLongVal(0x5a5a5a5a5a5aL);
      r1.setStringVal("random text");
      r1.setBufferVal(new Buffer());
      r1.setVectorVal(new ArrayList<String>());
      r1.setMapVal(new TreeMap<String,String>());
      r1.serialize(out, "");
      ostream.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    } 
  }
    
  public void testCsv() {
    File tmpfile;
    try {
      tmpfile = new File("/tmp/hadooptemp.txt");
      FileOutputStream ostream = new FileOutputStream(tmpfile);
      CsvRecordOutput out = new CsvRecordOutput(ostream);
      RecRecord1 r1 = new RecRecord1();
      r1.setBoolVal(true);
      r1.setByteVal((byte)0x66);
      r1.setFloatVal(3.145F);
      r1.setDoubleVal(1.5234);
      r1.setIntVal(4567);
      r1.setLongVal(0x5a5a5a5a5a5aL);
      r1.setStringVal("random text");
      r1.setBufferVal(new Buffer());
      r1.setVectorVal(new ArrayList<String>());
      r1.setMapVal(new TreeMap<String,String>());
      r1.serialize(out, "");
      ostream.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    } 
  }

  public void testXml() {
    File tmpfile;
    try {
      tmpfile = new File("/tmp/hadooptemp.xml");
      FileOutputStream ostream = new FileOutputStream(tmpfile);
      XmlRecordOutput out = new XmlRecordOutput(ostream);
      RecRecord1 r1 = new RecRecord1();
      r1.setBoolVal(true);
      r1.setByteVal((byte)0x66);
      r1.setFloatVal(3.145F);
      r1.setDoubleVal(1.5234);
      r1.setIntVal(4567);
      r1.setLongVal(0x5a5a5a5a5a5aL);
      r1.setStringVal("random text");
      r1.setBufferVal(new Buffer());
      r1.setVectorVal(new ArrayList<String>());
      r1.setMapVal(new TreeMap<String,String>());
      r1.serialize(out, "");
      ostream.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    } 
  }
}
