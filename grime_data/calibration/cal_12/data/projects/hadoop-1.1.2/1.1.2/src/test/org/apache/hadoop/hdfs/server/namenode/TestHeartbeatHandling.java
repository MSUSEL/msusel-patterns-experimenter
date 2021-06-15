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

import java.util.ArrayList;

import junit.framework.TestCase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DFSConfigKeys;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.protocol.Block;
import org.apache.hadoop.hdfs.server.common.GenerationStamp;
import org.apache.hadoop.hdfs.server.protocol.BlockCommand;
import org.apache.hadoop.hdfs.server.protocol.DatanodeCommand;
import org.apache.hadoop.hdfs.server.protocol.DatanodeProtocol;
import org.apache.hadoop.hdfs.server.protocol.DatanodeRegistration;

/**
 * Test if FSNamesystem handles heartbeat right
 */
public class TestHeartbeatHandling extends TestCase {
  /**
   * Test if {@link FSNamesystem#handleHeartbeat(DatanodeRegistration, long, long, long, int, int)}
   * can pick up replication and/or invalidate requests and 
   * observes the max limit
   */
  public void testHeartbeat() throws Exception {
    final Configuration conf = new Configuration();
    final MiniDFSCluster cluster = new MiniDFSCluster(conf, 1, true, null);
    try {
      cluster.waitActive();
      final FSNamesystem namesystem = cluster.getNameNode().getNamesystem();
      final DatanodeRegistration nodeReg = cluster.getDataNodes().get(0).dnRegistration;
      DatanodeDescriptor dd = namesystem.getDatanode(nodeReg);
      
      final int REMAINING_BLOCKS = 1;
      final int MAX_REPLICATE_LIMIT = conf.getInt("dfs.max-repl-streams", 2);
      final int MAX_INVALIDATE_LIMIT = DFSConfigKeys.DFS_BLOCK_INVALIDATE_LIMIT_DEFAULT;
      final int MAX_INVALIDATE_BLOCKS = 2*MAX_INVALIDATE_LIMIT+REMAINING_BLOCKS;
      final int MAX_REPLICATE_BLOCKS = 2*MAX_REPLICATE_LIMIT+REMAINING_BLOCKS;
      final DatanodeDescriptor[] ONE_TARGET = new DatanodeDescriptor[1];

      synchronized (namesystem.heartbeats) {
      for (int i=0; i<MAX_REPLICATE_BLOCKS; i++) {
        dd.addBlockToBeReplicated(
            new Block(i, 0, GenerationStamp.FIRST_VALID_STAMP), ONE_TARGET);
      }
      DatanodeCommand[] cmds = namesystem.handleHeartbeat(
          nodeReg, dd.getCapacity(), dd.getDfsUsed(), dd.getRemaining(), 0, 0);
      assertEquals(1, cmds.length);
      assertEquals(DatanodeProtocol.DNA_TRANSFER, cmds[0].getAction());
      assertEquals(MAX_REPLICATE_LIMIT, ((BlockCommand)cmds[0]).getBlocks().length);
      
      ArrayList<Block> blockList = new ArrayList<Block>(MAX_INVALIDATE_BLOCKS);
      for (int i=0; i<MAX_INVALIDATE_BLOCKS; i++) {
        blockList.add(new Block(i, 0, GenerationStamp.FIRST_VALID_STAMP));
      }
      dd.addBlocksToBeInvalidated(blockList);
           
      cmds = namesystem.handleHeartbeat(
          nodeReg, dd.getCapacity(), dd.getDfsUsed(), dd.getRemaining(), 0, 0);
      assertEquals(2, cmds.length);
      assertEquals(DatanodeProtocol.DNA_TRANSFER, cmds[0].getAction());
      assertEquals(MAX_REPLICATE_LIMIT, ((BlockCommand)cmds[0]).getBlocks().length);
      assertEquals(DatanodeProtocol.DNA_INVALIDATE, cmds[1].getAction());
      assertEquals(MAX_INVALIDATE_LIMIT, ((BlockCommand)cmds[1]).getBlocks().length);
      
      cmds = namesystem.handleHeartbeat(
          nodeReg, dd.getCapacity(), dd.getDfsUsed(), dd.getRemaining(), 0, 0);
      assertEquals(2, cmds.length);
      assertEquals(DatanodeProtocol.DNA_TRANSFER, cmds[0].getAction());
      assertEquals(REMAINING_BLOCKS, ((BlockCommand)cmds[0]).getBlocks().length);
      assertEquals(DatanodeProtocol.DNA_INVALIDATE, cmds[1].getAction());
      assertEquals(MAX_INVALIDATE_LIMIT, ((BlockCommand)cmds[1]).getBlocks().length);
      
      cmds = namesystem.handleHeartbeat(
          nodeReg, dd.getCapacity(), dd.getDfsUsed(), dd.getRemaining(), 0, 0);
      assertEquals(1, cmds.length);
      assertEquals(DatanodeProtocol.DNA_INVALIDATE, cmds[0].getAction());
      assertEquals(REMAINING_BLOCKS, ((BlockCommand)cmds[0]).getBlocks().length);

      cmds = namesystem.handleHeartbeat(
          nodeReg, dd.getCapacity(), dd.getDfsUsed(), dd.getRemaining(), 0, 0);
      assertEquals(null, cmds);
      }
    } finally {
      cluster.shutdown();
    }
  }
}
