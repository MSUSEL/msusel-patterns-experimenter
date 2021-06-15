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

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ReflectionUtils;

import junit.framework.TestCase;

/** Unit tests for WritableName. */
public class TestWritableName extends TestCase {
  public TestWritableName(String name) { 
    super(name); 
  }

  /** Example class used in test cases below. */
  public static class SimpleWritable implements Writable {
    private static final Random RANDOM = new Random();

    int state = RANDOM.nextInt();

    public void write(DataOutput out) throws IOException {
      out.writeInt(state);
    }

    public void readFields(DataInput in) throws IOException {
      this.state = in.readInt();
    }

    public static SimpleWritable read(DataInput in) throws IOException {
      SimpleWritable result = new SimpleWritable();
      result.readFields(in);
      return result;
    }

    /** Required by test code, below. */
    public boolean equals(Object o) {
      if (!(o instanceof SimpleWritable))
        return false;
      SimpleWritable other = (SimpleWritable)o;
      return this.state == other.state;
    }
  }

  private static final String testName = "mystring";

  public void testGoodName() throws Exception {
    Configuration conf = new Configuration();
    Class<?> test = WritableName.getClass("long",conf);
    assertTrue(test != null);
  }

  public void testSetName() throws Exception {
    Configuration conf = new Configuration();
    WritableName.setName(SimpleWritable.class, testName);

    Class<?> test = WritableName.getClass(testName,conf);
    assertTrue(test.equals(SimpleWritable.class));
  }


  public void testAddName() throws Exception {
    Configuration conf = new Configuration();
    String altName = testName + ".alt";

    WritableName.addName(SimpleWritable.class, altName);

    Class<?> test = WritableName.getClass(altName, conf);
    assertTrue(test.equals(SimpleWritable.class));

    // check original name still works
    test = WritableName.getClass(testName, conf);
    assertTrue(test.equals(SimpleWritable.class));

  }

  public void testBadName() throws Exception {
    Configuration conf = new Configuration();
    try {
      Class<?> test = WritableName.getClass("unknown_junk",conf);
      assertTrue(false);
    } catch(IOException e) {
      assertTrue(e.getMessage().matches(".*unknown_junk.*"));
    }
  }
	
}
