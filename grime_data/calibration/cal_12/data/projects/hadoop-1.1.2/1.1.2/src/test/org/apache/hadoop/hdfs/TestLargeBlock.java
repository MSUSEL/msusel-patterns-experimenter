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

import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.server.datanode.SimulatedFSDataset;
import org.junit.Test;

/**
 * This class tests that blocks can be larger than 2GB
 */
public class TestLargeBlock extends junit.framework.TestCase {
  static final String DIR = "/" + TestLargeBlock.class.getSimpleName() + "/";

  //should we verify the data read back from the file? (slow)
  static final boolean verifyData = true;

  static final byte[] pattern = { 'D', 'E', 'A', 'D', 'B', 'E', 'E', 'F'};
  static final boolean simulatedStorage = false;

  // creates a file
  static FSDataOutputStream createFile(FileSystem fileSys, Path name, int repl, final long blockSize)
    throws IOException {
    FSDataOutputStream stm = fileSys.create(name, true,
                                            fileSys.getConf().getInt("io.file.buffer.size", 4096),
                                            (short)repl, blockSize);
    System.out.println("createFile: Created " + name + " with " + repl + " replica.");
    return stm;
  }


  /**
   * Writes pattern to file
   */
  static void writeFile(FSDataOutputStream stm, final long fileSize) throws IOException {
    final int writeSize = pattern.length * 8 * 1024 * 1024; // write in chunks of 64 MB

    if (writeSize > Integer.MAX_VALUE) {
      throw new IOException("A single write is too large " + writeSize);
    }

    long bytesToWrite = fileSize;
    byte[] b = new byte[writeSize];

    // initialize buffer
    for (int j = 0; j < writeSize; j++) {
      b[j] = pattern[j % pattern.length];
    }

    while (bytesToWrite > 0) {
      // how many bytes we are writing in this iteration
      int thiswrite = (int) Math.min(writeSize, bytesToWrite);

      stm.write(b, 0, thiswrite);
      bytesToWrite -= thiswrite;
    }
  }

  /**
   * Reads from file and makes sure that it matches the pattern
   */
  static void checkFullFile(FileSystem fs, Path name, final long fileSize) throws IOException {
    final int readSize = pattern.length * 16 * 1024 * 1024; // read in chunks of 128 MB

    if (readSize > Integer.MAX_VALUE) {
      throw new IOException("A single read is too large " + readSize);
    }

    byte[] b = new byte[readSize];
    long bytesToRead = fileSize;

    byte[] compb = new byte[readSize]; // buffer with correct data for comparison

    if (verifyData) {
      // initialize compare buffer
      for (int j = 0; j < readSize; j++) {
        compb[j] = pattern[j % pattern.length];
      }
    }


    FSDataInputStream stm = fs.open(name);

    while (bytesToRead > 0) {
      int thisread = (int) Math.min(readSize, bytesToRead); // how many bytes we are reading in this iteration

      stm.readFully(b, 0, thisread);

      if (verifyData) {
        // verify data read

        if (thisread == readSize) {
          assertTrue("file corrupted at or after byte " + (fileSize - bytesToRead), Arrays.equals(b, compb));
        } else {
          // b was only partially filled by last read
          for (int k = 0; k < thisread; k++) {
            assertTrue("file corrupted at or after byte " + (fileSize - bytesToRead), b[k] == compb[k]);
          }
        }
      }

      // System.out.println("Read[" + i + "/" + readCount + "] " + thisread + " bytes.");

      bytesToRead -= thisread;
    }
    stm.close();
  }

  /**
   * Test for block size of 2GB + 512B
   */
  @Test
  public void testLargeBlockSize() throws IOException {
    final long blockSize = 2L * 1024L * 1024L * 1024L + 512L; // 2GB + 512B
    runTest(blockSize);
  }

  /**
   * Test that we can write to and read from large blocks
   */
  public void runTest(final long blockSize) throws IOException {

    // write a file that is slightly larger than 1 block
    final long fileSize = blockSize + 1L;

    Configuration conf = new Configuration();
    if (simulatedStorage) {
      conf.setBoolean(SimulatedFSDataset.CONFIG_PROPERTY_SIMULATED, true);
    }
    MiniDFSCluster cluster = new MiniDFSCluster(conf, 1, true, null);
    FileSystem fs = cluster.getFileSystem();
    try {

      // create a new file in test data directory
      Path file1 = new Path(System.getProperty("test.build.data") + "/" + Long.toString(blockSize) + ".dat");
      FSDataOutputStream stm = createFile(fs, file1, 1, blockSize);
      System.out.println("File " + file1 + " created with file size " +
                         fileSize +
                         " blocksize " + blockSize);

      // verify that file exists in FS namespace
      assertTrue(file1 + " should be a file and not a dir",
                  !fs.getFileStatus(file1).isDir());

      // write to file
      writeFile(stm, fileSize);
      System.out.println("File " + file1 + " written to.");

      // close file
      stm.close();
      System.out.println("File " + file1 + " closed.");

      // Make sure a client can read it
      checkFullFile(fs, file1, fileSize);

      // verify that file size has changed
      long len = fs.getFileStatus(file1).getLen();
      assertTrue(file1 + " should be of size " +  fileSize +
                 " but found to be of size " + len,
                  len == fileSize);

    } finally {
      cluster.shutdown();
    }
  }
}
