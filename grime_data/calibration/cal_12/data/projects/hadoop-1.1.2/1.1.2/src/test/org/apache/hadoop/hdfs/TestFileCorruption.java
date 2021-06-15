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

import java.io.*;
import java.util.ArrayList;

import junit.framework.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.ChecksumException;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.protocol.Block;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.hdfs.server.common.GenerationStamp;
import org.apache.hadoop.hdfs.server.datanode.DataNode;

/**
 * A JUnit test for corrupted file handling.
 */
public class TestFileCorruption extends TestCase {
  
  public TestFileCorruption(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  /** check if DFS can handle corrupted blocks properly */
  public void testFileCorruption() throws Exception {
    MiniDFSCluster cluster = null;
    DFSTestUtil util = new DFSTestUtil("TestFileCorruption", 20, 3, 8*1024);
    try {
      Configuration conf = new Configuration();
      cluster = new MiniDFSCluster(conf, 3, true, null);
      FileSystem fs = cluster.getFileSystem();
      util.createFiles(fs, "/srcdat");
      // Now deliberately remove the blocks
      File data_dir = new File(System.getProperty("test.build.data"),
                               "dfs/data/data5/current");
      assertTrue("data directory does not exist", data_dir.exists());
      File[] blocks = data_dir.listFiles();
      assertTrue("Blocks do not exist in data-dir", (blocks != null) && (blocks.length > 0));
      for (int idx = 0; idx < blocks.length; idx++) {
        if (!blocks[idx].getName().startsWith("blk_")) {
          continue;
        }
        System.out.println("Deliberately removing file "+blocks[idx].getName());
        assertTrue("Cannot remove file.", blocks[idx].delete());
      }
      assertTrue("Corrupted replicas not handled properly.",
                 util.checkFiles(fs, "/srcdat"));
      util.cleanup(fs, "/srcdat");
    } finally {
      if (cluster != null) { cluster.shutdown(); }
    }
  }

  /** check if local FS can handle corrupted blocks properly */
  public void testLocalFileCorruption() throws Exception {
    Configuration conf = new Configuration();
    Path file = new Path(System.getProperty("test.build.data"), "corruptFile");
    FileSystem fs = FileSystem.getLocal(conf);
    DataOutputStream dos = fs.create(file);
    dos.writeBytes("original bytes");
    dos.close();
    // Now deliberately corrupt the file
    dos = new DataOutputStream(new FileOutputStream(file.toString()));
    dos.writeBytes("corruption");
    dos.close();
    // Now attempt to read the file
    DataInputStream dis = fs.open(file, 512);
    try {
      System.out.println("A ChecksumException is expected to be logged.");
      dis.readByte();
    } catch (ChecksumException ignore) {
      //expect this exception but let any NPE get thrown
    }
    fs.delete(file, true);
  }
  
  /** Test the case that a replica is reported corrupt while it is not
   * in blocksMap. Make sure that ArrayIndexOutOfBounds does not thrown.
   * See Hadoop-4351.
   */
  public void testArrayOutOfBoundsException() throws Exception {
    MiniDFSCluster cluster = null;
    try {
      Configuration conf = new Configuration();
      cluster = new MiniDFSCluster(conf, 2, true, null);
      cluster.waitActive();
      
      FileSystem fs = cluster.getFileSystem();
      final Path FILE_PATH = new Path("/tmp.txt");
      final long FILE_LEN = 1L;
      DFSTestUtil.createFile(fs, FILE_PATH, FILE_LEN, (short)2, 1L);
      
      // get the block
      File dataDir = new File(cluster.getDataDirectory(),
          "data1/current");
      Block blk = getBlock(dataDir);
      if (blk == null) {
        blk = getBlock(new File(cluster.getDataDirectory(),
          "dfs/data/data2/current"));
      }
      assertFalse(blk==null);

      // start a third datanode
      cluster.startDataNodes(conf, 1, true, null, null);
      ArrayList<DataNode> datanodes = cluster.getDataNodes();
      assertEquals(datanodes.size(), 3);
      DataNode dataNode = datanodes.get(2);
      
      // report corrupted block by the third datanode
      cluster.getNameNode().namesystem.markBlockAsCorrupt(blk, 
          new DatanodeInfo(dataNode.dnRegistration ));
      
      // open the file
      fs.open(FILE_PATH);
      
      //clean up
      fs.delete(FILE_PATH, false);
    } finally {
      if (cluster != null) { cluster.shutdown(); }
    }
    
  }
  
  private Block getBlock(File dataDir) {
    assertTrue("data directory does not exist", dataDir.exists());
    File[] blocks = dataDir.listFiles();
    assertTrue("Blocks do not exist in dataDir", (blocks != null) && (blocks.length > 0));

    int idx = 0;
    String blockFileName = null;
    for (; idx < blocks.length; idx++) {
      blockFileName = blocks[idx].getName();
      if (blockFileName.startsWith("blk_") && !blockFileName.endsWith(".meta")) {
        break;
      }
    }
    if (blockFileName == null) {
      return null;
    }
    long blockId = Long.parseLong(blockFileName.substring("blk_".length()));
    long blockTimeStamp = GenerationStamp.WILDCARD_STAMP;
    for (idx=0; idx < blocks.length; idx++) {
      String fileName = blocks[idx].getName();
      if (fileName.startsWith(blockFileName) && fileName.endsWith(".meta")) {
        int startIndex = blockFileName.length()+1;
        int endIndex = fileName.length() - ".meta".length();
        blockTimeStamp = Long.parseLong(fileName.substring(startIndex, endIndex));
        break;
      }
    }
    return new Block(blockId, blocks[idx].length(), blockTimeStamp);
  }
}
