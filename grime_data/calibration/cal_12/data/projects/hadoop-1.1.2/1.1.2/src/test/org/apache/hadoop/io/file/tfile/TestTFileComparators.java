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
package org.apache.hadoop.io.file.tfile;

import java.io.IOException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.file.tfile.TFile.Writer;

/**
 * 
 * Byte arrays test case class using GZ compression codec, base class of none
 * and LZO compression classes.
 * 
 */
public class TestTFileComparators extends TestCase {
  private static String ROOT =
      System.getProperty("test.build.data", "/tmp/tfile-test");

  private final static int BLOCK_SIZE = 512;
  private FileSystem fs;
  private Configuration conf;
  private Path path;
  private FSDataOutputStream out;
  private Writer writer;

  private String compression = Compression.Algorithm.GZ.getName();
  private String outputFile = "TFileTestComparators";
  /*
   * pre-sampled numbers of records in one block, based on the given the
   * generated key and value strings
   */
  // private int records1stBlock = 4314;
  // private int records2ndBlock = 4108;
  private int records1stBlock = 4480;
  private int records2ndBlock = 4263;

  @Override
  public void setUp() throws IOException {
    conf = new Configuration();
    path = new Path(ROOT, outputFile);
    fs = path.getFileSystem(conf);
    out = fs.create(path);
  }

  @Override
  public void tearDown() throws IOException {
    fs.delete(path, true);
  }

  // bad comparator format
  public void testFailureBadComparatorNames() throws IOException {
    try {
      writer = new Writer(out, BLOCK_SIZE, compression, "badcmp", conf);
      Assert.fail("Failed to catch unsupported comparator names");
    }
    catch (Exception e) {
      // noop, expecting exceptions
      e.printStackTrace();
    }
  }

  // jclass that doesn't exist
  public void testFailureBadJClassNames() throws IOException {
    try {
      writer =
          new Writer(out, BLOCK_SIZE, compression,
              "jclass: some.non.existence.clazz", conf);
      Assert.fail("Failed to catch unsupported comparator names");
    }
    catch (Exception e) {
      // noop, expecting exceptions
      e.printStackTrace();
    }
  }

  // class exists but not a RawComparator
  public void testFailureBadJClasses() throws IOException {
    try {
      writer =
          new Writer(out, BLOCK_SIZE, compression,
              "jclass:org.apache.hadoop.io.file.tfile.Chunk", conf);
      Assert.fail("Failed to catch unsupported comparator names");
    }
    catch (Exception e) {
      // noop, expecting exceptions
      e.printStackTrace();
    }
  }

  private void closeOutput() throws IOException {
    if (writer != null) {
      writer.close();
      writer = null;
    }
    if (out != null) {
      out.close();
      out = null;
    }
  }
}
