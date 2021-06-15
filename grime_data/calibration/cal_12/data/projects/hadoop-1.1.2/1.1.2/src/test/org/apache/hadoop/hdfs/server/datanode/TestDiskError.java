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

import java.io.DataOutputStream;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hdfs.DFSTestUtil;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.protocol.DataTransferProtocol;
import org.apache.hadoop.hdfs.protocol.LocatedBlock;
import org.apache.hadoop.hdfs.protocol.LocatedBlocks;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.DiskChecker;
import org.apache.hadoop.hdfs.security.token.block.BlockTokenSecretManager;

import junit.framework.TestCase;

/** Test if a datanode can correctly handle errors during block read/write*/
public class TestDiskError extends TestCase {
  public void testShutdown() throws Exception {
    if (System.getProperty("os.name").startsWith("Windows")) {
      /**
       * This test depends on OS not allowing file creations on a directory
       * that does not have write permissions for the user. Apparently it is 
       * not the case on Windows (at least under Cygwin), and possibly AIX.
       * This is disabled on Windows.
       */
      return;
    }
    // bring up a cluster of 3
    Configuration conf = new Configuration();
    conf.setLong("dfs.block.size", 512L);
    MiniDFSCluster cluster = new MiniDFSCluster(conf, 3, true, null);
    cluster.waitActive();
    FileSystem fs = cluster.getFileSystem();
    final int dnIndex = 0;
    String dataDir = cluster.getDataDirectory();
    File dir1 = new File(new File(dataDir, "data"+(2*dnIndex+1)), "blocksBeingWritten");
    File dir2 = new File(new File(dataDir, "data"+(2*dnIndex+2)), "blocksBeingWritten");
    try {
      // make the data directory of the first datanode to be readonly
      assertTrue(dir1.setReadOnly());
      assertTrue(dir2.setReadOnly());

      // create files and make sure that first datanode will be down
      DataNode dn = cluster.getDataNodes().get(dnIndex);
      for (int i=0; DataNode.isDatanodeUp(dn); i++) {
        Path fileName = new Path("/test.txt"+i);
        DFSTestUtil.createFile(fs, fileName, 1024, (short)2, 1L);
        DFSTestUtil.waitReplication(fs, fileName, (short)2);
        fs.delete(fileName, true);
      }
    } finally {
      // restore its old permission
      dir1.setWritable(true);
      dir2.setWritable(true);
      cluster.shutdown();
    }
  }
  
  public void testReplicationError() throws Exception {
    // bring up a cluster of 1
    Configuration conf = new Configuration();
    MiniDFSCluster cluster = new MiniDFSCluster(conf, 1, true, null);
    cluster.waitActive();
    FileSystem fs = cluster.getFileSystem();
    
    try {
      // create a file of replication factor of 1
      final Path fileName = new Path("/test.txt");
      final int fileLen = 1;
      DFSTestUtil.createFile(fs, fileName, 1, (short)1, 1L);
      DFSTestUtil.waitReplication(fs, fileName, (short)1);

      // get the block belonged to the created file
      LocatedBlocks blocks = cluster.getNameNode().namesystem.getBlockLocations(
          fileName.toString(), 0, (long)fileLen);
      assertEquals(blocks.locatedBlockCount(), 1);
      LocatedBlock block = blocks.get(0);
      
      // bring up a second datanode
      cluster.startDataNodes(conf, 1, true, null, null);
      cluster.waitActive();
      final int sndNode = 1;
      DataNode datanode = cluster.getDataNodes().get(sndNode);
      
      // replicate the block to the second datanode
      InetSocketAddress target = datanode.getSelfAddr();
      Socket s = new Socket(target.getAddress(), target.getPort());
        //write the header.
      DataOutputStream out = new DataOutputStream(
          s.getOutputStream());

      out.writeShort( DataTransferProtocol.DATA_TRANSFER_VERSION );
      out.write( DataTransferProtocol.OP_WRITE_BLOCK );
      out.writeLong( block.getBlock().getBlockId());
      out.writeLong( block.getBlock().getGenerationStamp() );
      out.writeInt(1);
      out.writeBoolean( false );       // recovery flag
      Text.writeString( out, "" );
      out.writeBoolean(false); // Not sending src node information
      out.writeInt(0);
      BlockTokenSecretManager.DUMMY_TOKEN.write(out);
      
      // write check header
      out.writeByte( 1 );
      out.writeInt( 512 );

      out.flush();

      // close the connection before sending the content of the block
      out.close();
      
      // the temporary block & meta files should be deleted
      String dataDir = cluster.getDataDirectory();
      File dir1 = new File(new File(dataDir, "data"+(2*sndNode+1)), "tmp");
      File dir2 = new File(new File(dataDir, "data"+(2*sndNode+2)), "tmp");
      while (dir1.listFiles().length != 0 || dir2.listFiles().length != 0) {
        Thread.sleep(100);
      }
      
      // then increase the file's replication factor
      fs.setReplication(fileName, (short)2);
      // replication should succeed
      DFSTestUtil.waitReplication(fs, fileName, (short)1);
      
      // clean up the file
      fs.delete(fileName, false);
    } finally {
      cluster.shutdown();
    }
  }
  
  public void testLocalDirs() throws Exception {
    Configuration conf = new Configuration();
    final String permStr = "755";
    FsPermission expected = new FsPermission(permStr);
    conf.set(DataNode.DATA_DIR_PERMISSION_KEY, permStr);
    MiniDFSCluster cluster = null; 
    
    try {
      // Start the cluster
      cluster = new MiniDFSCluster(conf, 1, true, null);
      cluster.waitActive();
      
      // Check permissions on directories in 'dfs.data.dir'
      FileSystem localFS = FileSystem.getLocal(conf);
      for (DataNode dn : cluster.getDataNodes()) {
        String[] dataDirs = dn.getConf().getStrings(DataNode.DATA_DIR_KEY);
        for (String dir : dataDirs) {
          Path dataDir = new Path(dir);
          FsPermission actual = localFS.getFileStatus(dataDir).getPermission();
          assertEquals("Permission for dir: " + dataDir, expected, actual);
        }
      }
    } finally {
      if (cluster != null)
        cluster.shutdown();
    }
    
  }
}
