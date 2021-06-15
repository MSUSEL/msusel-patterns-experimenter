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
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.hdfs.server.datanode.SimulatedFSDataset;

/**
 * This class tests the creation of files with block-size
 * smaller than the default buffer size of 4K.
 */
public class TestSmallBlock extends TestCase {
  static final long seed = 0xDEADBEEFL;
  static final int blockSize = 1;
  static final int fileSize = 20;
  boolean simulatedStorage = false;

  private void writeFile(FileSystem fileSys, Path name) throws IOException {
    // create and write a file that contains three blocks of data
    FSDataOutputStream stm = fileSys.create(name, true, 
                                            fileSys.getConf().getInt("io.file.buffer.size", 4096),
                                            (short)1, (long)blockSize);
    byte[] buffer = new byte[fileSize];
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
  
  private void checkFile(FileSystem fileSys, Path name) throws IOException {
    BlockLocation[] locations = fileSys.getFileBlockLocations(
        fileSys.getFileStatus(name), 0, fileSize);
    assertEquals("Number of blocks", fileSize, locations.length);
    FSDataInputStream stm = fileSys.open(name);
    byte[] expected = new byte[fileSize];
    if (simulatedStorage) {
      for (int i = 0; i < expected.length; ++i) {  
        expected[i] = SimulatedFSDataset.DEFAULT_DATABYTE;
      }
    } else {
      Random rand = new Random(seed);
      rand.nextBytes(expected);
    }
    // do a sanity check. Read the file
    byte[] actual = new byte[fileSize];
    stm.readFully(0, actual);
    checkAndEraseData(actual, 0, expected, "Read Sanity Test");
    stm.close();
  }
  
  private void cleanupFile(FileSystem fileSys, Path name) throws IOException {
    assertTrue(fileSys.exists(name));
    fileSys.delete(name, true);
    assertTrue(!fileSys.exists(name));
  }
  
  /**
   * Tests small block size in in DFS.
   */
  public void testSmallBlock() throws IOException {
    Configuration conf = new Configuration();
    if (simulatedStorage) {
      conf.setBoolean("dfs.datanode.simulateddatastorage", true);
    }
    conf.set("io.bytes.per.checksum", "1");
    MiniDFSCluster cluster = new MiniDFSCluster(conf, 1, true, null);
    FileSystem fileSys = cluster.getFileSystem();
    try {
      Path file1 = new Path("smallblocktest.dat");
      writeFile(fileSys, file1);
      checkFile(fileSys, file1);
      cleanupFile(fileSys, file1);
    } finally {
      fileSys.close();
      cluster.shutdown();
    }
  }
  public void testSmallBlockSimulatedStorage() throws IOException {
    simulatedStorage = true;
    testSmallBlock();
    simulatedStorage = false;
  }
}
