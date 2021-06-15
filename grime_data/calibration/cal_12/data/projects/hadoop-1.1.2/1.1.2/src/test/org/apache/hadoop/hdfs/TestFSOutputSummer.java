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

/**
 * This class tests if FSOutputSummer works correctly.
 */
public class TestFSOutputSummer extends TestCase {
  private static final long seed = 0xDEADBEEFL;
  private static final int BYTES_PER_CHECKSUM = 10;
  private static final int BLOCK_SIZE = 2*BYTES_PER_CHECKSUM;
  private static final int HALF_CHUNK_SIZE = BYTES_PER_CHECKSUM/2;
  private static final int FILE_SIZE = 2*BLOCK_SIZE-1;
  private static final short NUM_OF_DATANODES = 2;
  private byte[] expected = new byte[FILE_SIZE];
  private byte[] actual = new byte[FILE_SIZE];
  private FileSystem fileSys;

  /* create a file, write all data at once */
  private void writeFile1(Path name) throws Exception {
    FSDataOutputStream stm = fileSys.create(name, true, 
               fileSys.getConf().getInt("io.file.buffer.size", 4096),
               NUM_OF_DATANODES, BLOCK_SIZE);
    stm.write(expected);
    stm.close();
    checkFile(name);
    cleanupFile(name);
  }
  
  /* create a file, write data chunk by chunk */
  private void writeFile2(Path name) throws Exception {
    FSDataOutputStream stm = fileSys.create(name, true, 
               fileSys.getConf().getInt("io.file.buffer.size", 4096),
               NUM_OF_DATANODES, BLOCK_SIZE);
    int i=0;
    for( ;i<FILE_SIZE-BYTES_PER_CHECKSUM; i+=BYTES_PER_CHECKSUM) {
      stm.write(expected, i, BYTES_PER_CHECKSUM);
    }
    stm.write(expected, i, FILE_SIZE-3*BYTES_PER_CHECKSUM);
    stm.close();
    checkFile(name);
    cleanupFile(name);
  }
  
  /* create a file, write data with vairable amount of data */
  private void writeFile3(Path name) throws Exception {
    FSDataOutputStream stm = fileSys.create(name, true, 
        fileSys.getConf().getInt("io.file.buffer.size", 4096),
        NUM_OF_DATANODES, BLOCK_SIZE);
    stm.write(expected, 0, HALF_CHUNK_SIZE);
    stm.write(expected, HALF_CHUNK_SIZE, BYTES_PER_CHECKSUM+2);
    stm.write(expected, HALF_CHUNK_SIZE+BYTES_PER_CHECKSUM+2, 2);
    stm.write(expected, HALF_CHUNK_SIZE+BYTES_PER_CHECKSUM+4, HALF_CHUNK_SIZE);
    stm.write(expected, BLOCK_SIZE+4, BYTES_PER_CHECKSUM-4);
    stm.write(expected, BLOCK_SIZE+BYTES_PER_CHECKSUM, 
        FILE_SIZE-3*BYTES_PER_CHECKSUM);
    stm.close();
    checkFile(name);
    cleanupFile(name);
  }
  private void checkAndEraseData(byte[] actual, int from, byte[] expected,
      String message) throws Exception {
    for (int idx = 0; idx < actual.length; idx++) {
      assertEquals(message+" byte "+(from+idx)+" differs. expected "+
                        expected[from+idx]+" actual "+actual[idx],
                        actual[idx], expected[from+idx]);
      actual[idx] = 0;
    }
  }
  
  private void checkFile(Path name) throws Exception {
    FSDataInputStream stm = fileSys.open(name);
    // do a sanity check. Read the file
    stm.readFully(0, actual);
    checkAndEraseData(actual, 0, expected, "Read Sanity Test");
    stm.close();
  }

  private void cleanupFile(Path name) throws IOException {
    assertTrue(fileSys.exists(name));
    fileSys.delete(name, true);
    assertTrue(!fileSys.exists(name));
  }
  
  /**
   * Test write opeation for output stream in DFS.
   */
  public void testFSOutputSummer() throws Exception {
    Configuration conf = new Configuration();
    conf.setLong("dfs.block.size", BLOCK_SIZE);
    conf.setInt("io.bytes.per.checksum", BYTES_PER_CHECKSUM);
    conf.set("fs.hdfs.impl",
             "org.apache.hadoop.hdfs.ChecksumDistributedFileSystem");      
    MiniDFSCluster cluster = new MiniDFSCluster(
        conf, NUM_OF_DATANODES, true, null);
    fileSys = cluster.getFileSystem();
    try {
      Path file = new Path("try.dat");
      Random rand = new Random(seed);
      rand.nextBytes(expected);
      writeFile1(file);
      writeFile2(file);
      writeFile3(file);
    } finally {
      fileSys.close();
      cluster.shutdown();
    }
  }
}
