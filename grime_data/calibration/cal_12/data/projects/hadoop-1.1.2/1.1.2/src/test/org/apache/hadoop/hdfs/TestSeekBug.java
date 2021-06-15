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
package org.apache.hadoop.hdfs;

import junit.framework.TestCase;
import java.io.*;
import java.util.Random;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.ChecksumFileSystem;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * This class tests the presence of seek bug as described
 * in HADOOP-508 
 */
public class TestSeekBug extends TestCase {
  static final long seed = 0xDEADBEEFL;
  static final int ONEMB = 1 << 20;
  
  private void writeFile(FileSystem fileSys, Path name) throws IOException {
    // create and write a file that contains 1MB
    DataOutputStream stm = fileSys.create(name);
    byte[] buffer = new byte[ONEMB];
    Random rand = new Random(seed);
    rand.nextBytes(buffer);
    stm.write(buffer);
    stm.close();
  }
  
  private void checkAndEraseData(byte[] actual, int from, byte[] expected, String message) {
    for (int idx = 0; idx < actual.length; idx++) {
      this.assertEquals(message+" byte "+(from+idx)+" differs. expected "+
                        expected[from+idx]+" actual "+actual[idx],
                        actual[idx], expected[from+idx]);
      actual[idx] = 0;
    }
  }
  
  private void seekReadFile(FileSystem fileSys, Path name) throws IOException {
    FSDataInputStream stm = fileSys.open(name, 4096);
    byte[] expected = new byte[ONEMB];
    Random rand = new Random(seed);
    rand.nextBytes(expected);
    
    // First read 128 bytes to set count in BufferedInputStream
    byte[] actual = new byte[128];
    stm.read(actual, 0, actual.length);
    // Now read a byte array that is bigger than the internal buffer
    actual = new byte[100000];
    stm.read(actual, 0, actual.length);
    checkAndEraseData(actual, 128, expected, "First Read Test");
    // now do a small seek, within the range that is already read
    stm.seek(96036); // 4 byte seek
    actual = new byte[128];
    stm.read(actual, 0, actual.length);
    checkAndEraseData(actual, 96036, expected, "Seek Bug");
    // all done
    stm.close();
  }

  /*
   * Read some data, skip a few bytes and read more. HADOOP-922.
   */
  private void smallReadSeek(FileSystem fileSys, Path name) throws IOException {
    if (fileSys instanceof ChecksumFileSystem) {
      fileSys = ((ChecksumFileSystem)fileSys).getRawFileSystem();
    }
    // Make the buffer size small to trigger code for HADOOP-922
    FSDataInputStream stmRaw = fileSys.open(name, 1);
    byte[] expected = new byte[ONEMB];
    Random rand = new Random(seed);
    rand.nextBytes(expected);
    
    // Issue a simple read first.
    byte[] actual = new byte[128];
    stmRaw.seek(100000);
    stmRaw.read(actual, 0, actual.length);
    checkAndEraseData(actual, 100000, expected, "First Small Read Test");

    // now do a small seek of 4 bytes, within the same block.
    int newpos1 = 100000 + 128 + 4;
    stmRaw.seek(newpos1);
    stmRaw.read(actual, 0, actual.length);
    checkAndEraseData(actual, newpos1, expected, "Small Seek Bug 1");

    // seek another 256 bytes this time
    int newpos2 = newpos1 + 256;
    stmRaw.seek(newpos2);
    stmRaw.read(actual, 0, actual.length);
    checkAndEraseData(actual, newpos2, expected, "Small Seek Bug 2");

    // all done
    stmRaw.close();
  }
  
  private void cleanupFile(FileSystem fileSys, Path name) throws IOException {
    assertTrue(fileSys.exists(name));
    fileSys.delete(name, true);
    assertTrue(!fileSys.exists(name));
  }
  
  /**
   * Test if the seek bug exists in FSDataInputStream in DFS.
   */
  public void testSeekBugDFS() throws IOException {
    Configuration conf = new Configuration();
    MiniDFSCluster cluster = new MiniDFSCluster(conf, 1, true, null);
    FileSystem fileSys = cluster.getFileSystem();
    try {
      Path file1 = new Path("seektest.dat");
      writeFile(fileSys, file1);
      seekReadFile(fileSys, file1);
      smallReadSeek(fileSys, file1);
      cleanupFile(fileSys, file1);
    } finally {
      fileSys.close();
      cluster.shutdown();
    }
  }
  
  /**
   * Tests if the seek bug exists in FSDataInputStream in LocalFS.
   */
  public void testSeekBugLocalFS() throws IOException {
    Configuration conf = new Configuration();
    FileSystem fileSys = FileSystem.getLocal(conf);
    try {
      Path file1 = new Path("build/test/data", "seektest.dat");
      writeFile(fileSys, file1);
      seekReadFile(fileSys, file1);
      cleanupFile(fileSys, file1);
    } finally {
      fileSys.close();
    }
  }
}
