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
import java.io.DataInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.streaming.DumpTypedBytes;
import org.apache.hadoop.typedbytes.TypedBytesInput;

import junit.framework.TestCase;

public class TestDumpTypedBytes extends TestCase {

  public void testDumping() throws Exception {
    Configuration conf = new Configuration();
    MiniDFSCluster cluster = new MiniDFSCluster(conf, 2, true, null);
    FileSystem fs = cluster.getFileSystem();
    PrintStream psBackup = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintStream psOut = new PrintStream(out);
    System.setOut(psOut);
    DumpTypedBytes dumptb = new DumpTypedBytes(conf);

    try {
      Path root = new Path("/typedbytestest");
      assertTrue(fs.mkdirs(root));
      assertTrue(fs.exists(root));
      OutputStreamWriter writer = new OutputStreamWriter(fs.create(new Path(
        root, "test.txt")));
      try {
        for (int i = 0; i < 100; i++) {
          writer.write("" + (10 * i) + "\n");
        }
      } finally {
        writer.close();
      }

      String[] args = new String[1];
      args[0] = "/typedbytestest";
      int ret = dumptb.run(args);
      assertEquals("Return value != 0.", 0, ret);

      ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
      TypedBytesInput tbinput = new TypedBytesInput(new DataInputStream(in));
      int counter = 0;
      Object key = tbinput.read();
      while (key != null) {
        assertEquals(Long.class, key.getClass()); // offset
        Object value = tbinput.read();
        assertEquals(String.class, value.getClass());
        assertTrue("Invalid output.",
          Integer.parseInt(value.toString()) % 10 == 0);
        counter++;
        key = tbinput.read();
      }
      assertEquals("Wrong number of outputs.", 100, counter);
    } finally {
      try {
        fs.close();
      } catch (Exception e) {
      }
      System.setOut(psBackup);
      cluster.shutdown();
    }
  }

}
