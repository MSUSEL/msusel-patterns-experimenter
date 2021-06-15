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
package org.apache.hadoop.hdfs.server.datanode;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.DFSTestUtil;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.server.namenode.FSNamesystem;
import org.apache.hadoop.hdfs.server.namenode.LeaseManager;
import org.apache.log4j.Level;
import org.apache.commons.logging.impl.Log4JLogger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestDataBlockScanner {
  {
    ((Log4JLogger) LeaseManager.LOG).getLogger().setLevel(Level.ALL);
    ((Log4JLogger) FSNamesystem.LOG).getLogger().setLevel(Level.ALL);
    ((Log4JLogger) DFSClient.LOG).getLogger().setLevel(Level.ALL);
  }
  static final int blockSize = 8192;
  private MiniDFSCluster cluster;
  private FileSystem fileSystem;

  
  @Before
  public void setUp() throws Exception {
    final Configuration conf = new Configuration();
    init(conf);    
  }

  @After
  public void tearDown() throws Exception {
    cluster.shutdown();
  }

  private void init(Configuration conf) throws IOException {
    if (cluster != null) {
      cluster.shutdown();
    }
    cluster = new MiniDFSCluster(conf, 1, true, null);
    cluster.waitClusterUp();
    fileSystem = cluster.getFileSystem();
  }

  /**
   * This test reads an open files and tests that client verification does not
   * add a new block to the block-scanner map.
   * 
   * @throws IOException
   */
  @Test
  public void testPrematureDataBlockScannerAdd() throws IOException {
    // create a new file in the root, write data, do not close
    Path file1 = new Path("/unfinished-block");
    FSDataOutputStream out = fileSystem.create(file1);

    int writeSize = blockSize / 2;
    out.write(DFSTestUtil.generateSequentialBytes(0, writeSize));
    out.sync();
    
    FSDataInputStream in = fileSystem.open(file1);
    
    byte[] buf = new byte[4096];
    in.readFully(0, buf);
    in.close();

    waitForBlocks(fileSystem, file1, 1, writeSize);
    
    int blockMapSize = cluster.getDataNodes().get(0).blockScanner.blockMap.size();
    Assert.assertEquals(String.format(
        "%d entries in blockMap and it should be empty", blockMapSize), 0,
        blockMapSize);
    out.close();
  }
  
private void waitForBlocks(FileSystem fileSys, Path name, int blockCount, long length)
    throws IOException {
    // wait until we have at least one block in the file to read.
    boolean done = false;

    while (!done) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
      }
      done = true;
      BlockLocation[] locations = fileSys.getFileBlockLocations(
        fileSys.getFileStatus(name), 0, length);
      if (locations.length < blockCount) {
        done = false;
        continue;
      }
    }
  }
}