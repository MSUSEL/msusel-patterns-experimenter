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
package org.apache.hadoop.hdfs.server.namenode;

import junit.framework.TestCase;
import java.io.*;
import java.net.*;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FsShell;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.StringUtils;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.protocol.FSConstants.DatanodeReportType;
import org.apache.hadoop.hdfs.server.datanode.SimulatedFSDataset;


/**
 * This class tests that a file system adheres to the limit of
 * maximum number of files that is configured.
 */
public class TestFileLimit extends TestCase {
  static final long seed = 0xDEADBEEFL;
  static final int blockSize = 8192;
  boolean simulatedStorage = false;

  // The test file is 2 times the blocksize plus one. This means that when the
  // entire file is written, the first two blocks definitely get flushed to
  // the datanodes.

  private static String TEST_ROOT_DIR =
    new Path(System.getProperty("test.build.data","/tmp"))
    .toString().replace(' ', '+');
  
  //
  // creates a zero file.
  //
  private void createFile(FileSystem fileSys, Path name)
    throws IOException {
    FSDataOutputStream stm = fileSys.create(name, true,
                                            fileSys.getConf().getInt("io.file.buffer.size", 4096),
                                            (short)1, (long)blockSize);
    byte[] buffer = new byte[1024];
    Random rand = new Random(seed);
    rand.nextBytes(buffer);
    stm.write(buffer);
    stm.close();
  }

  private void waitForLimit(FSNamesystem namesys, long num)
  {
    // wait for number of blocks to decrease
    while (true) {
      long total = namesys.getBlocksTotal() + namesys.dir.totalInodes();
      System.out.println("Comparing current nodes " + total +
                         " to become " + num);
      if (total == num) {
        break;
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
      }
    }
  }

  /**
   * Test that file data becomes available before file is closed.
   */
  public void testFileLimit() throws IOException {
    Configuration conf = new Configuration();
    int maxObjects = 5;
    conf.setLong("dfs.max.objects", maxObjects);
    conf.setLong("dfs.blockreport.intervalMsec", 1000L);
    conf.setInt("dfs.heartbeat.interval", 1);
    int currentNodes = 0;
    
    if (simulatedStorage) {
      conf.setBoolean(SimulatedFSDataset.CONFIG_PROPERTY_SIMULATED, true);
    }
    MiniDFSCluster cluster = new MiniDFSCluster(conf, 1, true, null);
    FileSystem fs = cluster.getFileSystem();
    FSNamesystem namesys = FSNamesystem.fsNamesystemObject;
    NameNode namenode = cluster.getNameNode();
    try {

      //
      // check that / exists
      //
      Path path = new Path("/");
      assertTrue("/ should be a directory", 
                 fs.getFileStatus(path).isDir() == true);
      currentNodes = 1;          // root inode

      // verify that we can create the specified number of files. We leave
      // one for the "/". Each file takes an inode and a block.
      //
      for (int i = 0; i < maxObjects/2; i++) {
        Path file = new Path("/filestatus" + i);
        createFile(fs, file);
        System.out.println("Created file " + file);
        currentNodes += 2;      // two more objects for this creation.
      }

      // verify that creating another file fails
      boolean hitException = false;
      try {
        Path file = new Path("/filestatus");
        createFile(fs, file);
        System.out.println("Created file " + file);
      } catch (IOException e) {
        hitException = true;
      }
      assertTrue("Was able to exceed file limit", hitException);

      // delete one file
      Path file0 = new Path("/filestatus0");
      fs.delete(file0, true);
      System.out.println("Deleted file " + file0);
      currentNodes -= 2;

      // wait for number of blocks to decrease
      waitForLimit(namesys, currentNodes);

      // now, we shud be able to create a new file
      createFile(fs, file0);
      System.out.println("Created file " + file0 + " again.");
      currentNodes += 2;

      // delete the file again
      file0 = new Path("/filestatus0");
      fs.delete(file0, true);
      System.out.println("Deleted file " + file0 + " again.");
      currentNodes -= 2;

      // wait for number of blocks to decrease
      waitForLimit(namesys, currentNodes);

      // create two directories in place of the file that we deleted
      Path dir = new Path("/dir0/dir1");
      fs.mkdirs(dir);
      System.out.println("Created directories " + dir);
      currentNodes += 2;
      waitForLimit(namesys, currentNodes);

      // verify that creating another directory fails
      hitException = false;
      try {
        fs.mkdirs(new Path("dir.fail"));
        System.out.println("Created directory should not have succeeded.");
      } catch (IOException e) {
        hitException = true;
      }
      assertTrue("Was able to exceed dir limit", hitException);

    } finally {
      fs.close();
      cluster.shutdown();
    }
  }

  public void testFileLimitSimulated() throws IOException {
    simulatedStorage = true;
    testFileLimit();
    simulatedStorage = false;
  }
}
