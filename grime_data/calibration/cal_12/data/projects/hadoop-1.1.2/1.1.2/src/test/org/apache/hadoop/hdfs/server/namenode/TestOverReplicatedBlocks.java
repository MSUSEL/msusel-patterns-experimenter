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

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DFSTestUtil;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.TestDatanodeBlockScanner;
import org.apache.hadoop.hdfs.MiniDFSCluster.DataNodeProperties;
import org.apache.hadoop.hdfs.protocol.Block;
import org.apache.hadoop.hdfs.protocol.DatanodeID;

import junit.framework.TestCase;

public class TestOverReplicatedBlocks extends TestCase {
  /** Test processOverReplicatedBlock can handle corrupt replicas fine.
   * It make sure that it won't treat corrupt replicas as valid ones 
   * thus prevents NN deleting valid replicas but keeping
   * corrupt ones.
   */
  public void testProcesOverReplicateBlock() throws IOException {
    Configuration conf = new Configuration();
    conf.setLong("dfs.blockreport.intervalMsec", 1000L);
    conf.set("dfs.replication.pending.timeout.sec", Integer.toString(2));
    MiniDFSCluster cluster = new MiniDFSCluster(conf, 3, true, null);
    FileSystem fs = cluster.getFileSystem();

    try {
      final Path fileName = new Path("/foo1");
      DFSTestUtil.createFile(fs, fileName, 2, (short)3, 0L);
      DFSTestUtil.waitReplication(fs, fileName, (short)3);
      
      // corrupt the block on datanode 0
      Block block = DFSTestUtil.getFirstBlock(fs, fileName);
      TestDatanodeBlockScanner.corruptReplica(block.getBlockName(), 0);
      DataNodeProperties dnProps = cluster.stopDataNode(0);
      // remove block scanner log to trigger block scanning
      File scanLog = new File(System.getProperty("test.build.data"),
          "dfs/data/data1/current/dncp_block_verification.log.curr");
      //wait for one minute for deletion to succeed;
      for(int i=0; !scanLog.delete(); i++) {
        assertTrue("Could not delete log file in one minute", i < 60);
        try {
          Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
      }
      
      // restart the datanode so the corrupt replica will be detected
      cluster.restartDataNode(dnProps);
      DFSTestUtil.waitReplication(fs, fileName, (short)2);
      
      final DatanodeID corruptDataNode = 
        cluster.getDataNodes().get(2).dnRegistration;
      final FSNamesystem namesystem = FSNamesystem.getFSNamesystem();
      synchronized (namesystem.heartbeats) {
        // set live datanode's remaining space to be 0 
        // so they will be chosen to be deleted when over-replication occurs
        for (DatanodeDescriptor datanode : namesystem.heartbeats) {
          if (!corruptDataNode.equals(datanode)) {
            datanode.updateHeartbeat(100L, 100L, 0L, 0);
          }
        }
        
        // decrease the replication factor to 1; 
        namesystem.setReplication(fileName.toString(), (short)1);

        // corrupt one won't be chosen to be excess one
        // without 4910 the number of live replicas would be 0: block gets lost
        assertEquals(1, namesystem.countNodes(block).liveReplicas());
      }
    } finally {
      cluster.shutdown();
    }
  }
}
