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
package org.apache.hadoop.streaming;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.typedbytes.TypedBytesOutput;
import org.apache.hadoop.typedbytes.TypedBytesWritable;

import junit.framework.TestCase;

public class TestLoadTypedBytes extends TestCase {

  public void testLoading() throws Exception {
    Configuration conf = new Configuration();
    MiniDFSCluster cluster = new MiniDFSCluster(conf, 2, true, null);
    FileSystem fs = cluster.getFileSystem();
    
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    TypedBytesOutput tboutput = new TypedBytesOutput(new DataOutputStream(out));
    for (int i = 0; i < 100; i++) {
      tboutput.write(new Long(i)); // key
      tboutput.write("" + (10 * i)); // value
    }
    InputStream isBackup = System.in;
    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
    System.setIn(in);
    LoadTypedBytes loadtb = new LoadTypedBytes(conf);

    try {
      Path root = new Path("/typedbytestest");
      assertTrue(fs.mkdirs(root));
      assertTrue(fs.exists(root));
      
      String[] args = new String[1];
      args[0] = "/typedbytestest/test.seq";
      int ret = loadtb.run(args);
      assertEquals("Return value != 0.", 0, ret);

      Path file = new Path(root, "test.seq");
      assertTrue(fs.exists(file));
      SequenceFile.Reader reader = new SequenceFile.Reader(fs, file, conf);
      int counter = 0;
      TypedBytesWritable key = new TypedBytesWritable();
      TypedBytesWritable value = new TypedBytesWritable();
      while (reader.next(key, value)) {
        assertEquals(Long.class, key.getValue().getClass());
        assertEquals(String.class, value.getValue().getClass());
        assertTrue("Invalid record.",
          Integer.parseInt(value.toString()) % 10 == 0);
        counter++;
      }
      assertEquals("Wrong number of records.", 100, counter);
    } finally {
      try {
        fs.close();
      } catch (Exception e) {
      }
      System.setIn(isBackup);
      cluster.shutdown();
    }
  }
  
}
