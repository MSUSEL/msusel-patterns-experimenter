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
package org.apache.hadoop.io;

import java.io.*;
import java.util.Random;
import junit.framework.TestCase;

/** Unit tests for VersionedWritable. */

public class TestVersionedWritable extends TestCase {

  public TestVersionedWritable(String name) { super(name); }
	
	
  /** Example class used in test cases below. */
  public static class SimpleVersionedWritable extends VersionedWritable {
		
    private static final Random RANDOM = new Random();
    int state = RANDOM.nextInt();

		
    private static byte VERSION = 1;
    public byte getVersion() { 
      return VERSION; 
    }		
		

    public void write(DataOutput out) throws IOException {
      super.write(out); // version.
      out.writeInt(state);
    }
		
    public void readFields(DataInput in) throws IOException {
      super.readFields(in); // version
      this.state = in.readInt();
    }
		

    public static SimpleVersionedWritable read(DataInput in) throws IOException {
      SimpleVersionedWritable result = new SimpleVersionedWritable();
      result.readFields(in);
      return result;
    }
		

    /** Required by test code, below. */
    public boolean equals(Object o) {
      if (!(o instanceof SimpleVersionedWritable))
        return false;
      SimpleVersionedWritable other = (SimpleVersionedWritable)o;
      return this.state == other.state;
    }

  }


	
  public static class AdvancedVersionedWritable extends SimpleVersionedWritable {

    String shortTestString = "Now is the time for all good men to come to the aid of the Party";
    String longTestString = "Four score and twenty years ago. Blah. Blah. Blah. Blah. Blah. Blah. Blah. Blah.";
		
    String compressableTestString = 
      "Blah. Blah. Blah. Blah. Blah. Blah. Blah. Blah. Blah. Blah. Blah. Blah. " +
      "Blah. Blah. Blah. Blah. Blah. Blah. Blah. Blah. Blah. Blah. Blah. Blah. " +
      "Blah. Blah. Blah. Blah. Blah. Blah. Blah. Blah. Blah. Blah. Blah. Blah. ";

    SimpleVersionedWritable containedObject = new SimpleVersionedWritable();
    String[] testStringArray = {"The", "Quick", "Brown", "Fox", "Jumped", "Over", "The", "Lazy", "Dog"};

    public void write(DataOutput out) throws IOException {
      super.write(out);
      out.writeUTF(shortTestString); 
      WritableUtils.writeString(out, longTestString); 
      int comp = WritableUtils.writeCompressedString(out, compressableTestString); 
      System.out.println("Compression is " + comp + "%");
      containedObject.write(out); // Warning if this is a recursive call, you need a null value.
      WritableUtils.writeStringArray(out, testStringArray); 

    }
		
		
    public void readFields(DataInput in) throws IOException {
      super.readFields(in);
      shortTestString = in.readUTF();
      longTestString = WritableUtils.readString(in); 
      compressableTestString = WritableUtils.readCompressedString(in);
      containedObject.readFields(in); // Warning if this is a recursive call, you need a null value.
      testStringArray = WritableUtils.readStringArray(in); 
    }
			


    public boolean equals(Object o) {
      super.equals(o);

      if (!shortTestString.equals(((AdvancedVersionedWritable)o).shortTestString)) { return false;}
      if (!longTestString.equals(((AdvancedVersionedWritable)o).longTestString)) { return false;}
      if (!compressableTestString.equals(((AdvancedVersionedWritable)o).compressableTestString)) { return false;}
			
      if (testStringArray.length != ((AdvancedVersionedWritable)o).testStringArray.length) { return false;}
      for(int i=0;i< testStringArray.length;i++){
        if (!testStringArray[i].equals(((AdvancedVersionedWritable)o).testStringArray[i])) {
          return false;
        }
      }
			
      if (!containedObject.equals(((AdvancedVersionedWritable)o).containedObject)) { return false;}
			
      return true;
    }
		


  }

  /* This one checks that version mismatch is thrown... */
  public static class SimpleVersionedWritableV2 extends SimpleVersionedWritable {
    static byte VERSION = 2;
    public byte getVersion() { 
      return VERSION; 
    }		
  }


  /** Test 1: Check that SimpleVersionedWritable. */
  public void testSimpleVersionedWritable() throws Exception {
    TestWritable.testWritable(new SimpleVersionedWritable());
  }

  /** Test 2: Check that AdvancedVersionedWritable Works (well, why wouldn't it!). */
  public void testAdvancedVersionedWritable() throws Exception {
    TestWritable.testWritable(new AdvancedVersionedWritable());
  }

  /** Test 3: Check that SimpleVersionedWritable throws an Exception. */
  public void testSimpleVersionedWritableMismatch() throws Exception {
    TestVersionedWritable.testVersionedWritable(new SimpleVersionedWritable(), new SimpleVersionedWritableV2());
  }



	
  /** Utility method for testing VersionedWritables. */
  public static void testVersionedWritable(Writable before, Writable after) throws Exception {
    DataOutputBuffer dob = new DataOutputBuffer();
    before.write(dob);
	
    DataInputBuffer dib = new DataInputBuffer();
    dib.reset(dob.getData(), dob.getLength());

    try {
      after.readFields(dib);
    } catch (VersionMismatchException vmme) {
      System.out.println("Good, we expected this:" + vmme);
      return;
    }
	
    throw new Exception("A Version Mismatch Didn't Happen!");
  }
}

