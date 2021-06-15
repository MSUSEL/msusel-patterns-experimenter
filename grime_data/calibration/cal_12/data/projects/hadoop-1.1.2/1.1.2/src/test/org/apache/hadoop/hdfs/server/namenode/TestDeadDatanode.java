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

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DFSConfigKeys;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.protocol.Block;
import org.apache.hadoop.hdfs.protocol.DatanodeID;
import org.apache.hadoop.hdfs.server.datanode.DataNode;
import org.apache.hadoop.hdfs.server.namenode.FSNamesystem;
import org.apache.hadoop.hdfs.server.protocol.DatanodeCommand;
import org.apache.hadoop.hdfs.server.protocol.DatanodeProtocol;
import org.apache.hadoop.hdfs.server.protocol.DatanodeRegistration;
import org.junit.After;
import org.junit.Test;

/**
 * Test to ensure requests from dead datnodes are rejected by namenode with
 * appropriate exceptions/failure response
 */
public class TestDeadDatanode {
  private static final Log LOG = LogFactory.getLog(TestDeadDatanode.class);
  private MiniDFSCluster cluster;

  @After
  public void cleanup() {
    cluster.shutdown();
  }

  /**
   * wait for datanode to reach alive or dead state for waitTime given in
   * milliseconds.
   */
  private void waitForDatanodeState(DatanodeID nodeID, boolean alive, int waitTime)
      throws TimeoutException, InterruptedException, IOException {
    long stopTime = System.currentTimeMillis() + waitTime;
    FSNamesystem namesystem = cluster.getNameNode().getNamesystem();
    String state = alive ? "alive" : "dead";
    while (System.currentTimeMillis() < stopTime) {
      if (namesystem.getDatanode(nodeID).isAlive == alive) {
        LOG.info("datanode " + nodeID + " is " + state);
        return;
      }
      LOG.info("Waiting for datanode " + nodeID + " to become " + state);
      Thread.sleep(1000);
    }
    throw new TimeoutException("Timedout waiting for datanode reach state "
        + state);
  }

  /**
   * Test to ensure namenode rejects request from dead datanode
   * - Start a cluster
   * - Shutdown the datanode and wait for it to be marked dead at the namenode
   * - Send datanode requests to Namenode and make sure it is rejected 
   *   appropriately.
   */
  @Test
  public void testDeadDatanode() throws Exception {
    Configuration conf = new Configuration();
    conf.setInt("heartbeat.recheck.interval", 500);
    conf.setLong(DFSConfigKeys.DFS_HEARTBEAT_INTERVAL_KEY, 1L);
    cluster = new MiniDFSCluster(conf, 1, true, null);
    cluster.waitActive();

    // wait for datanode to be marked live
    DataNode dn = cluster.getDataNodes().get(0);
    DatanodeRegistration reg = cluster.getDataNodes().get(0)
        .dnRegistration;
    waitForDatanodeState(reg, true, 20000);

    // Shutdown and wait for datanode to be marked dead
    dn.shutdown();
    waitForDatanodeState(reg, false, 20000);

    DatanodeProtocol dnp = cluster.getNameNode();
    Block block = new Block(0);
    Block[] blocks = new Block[] { block };
    String[] delHints = new String[] { "" };
    
    // Ensure blockReceived call from dead datanode is rejected with IOException
    try {
      dnp.blockReceived(reg, blocks, delHints);
      Assert.fail("Expected IOException is not thrown");
    } catch (IOException ex) {
      // Expected
    }

    // Ensure blockReport from dead datanode is rejected with IOException
    long[] blockReport = new long[] { 0L, 0L, 0L };
    try {
      dnp.blockReport(reg, blockReport);
      Assert.fail("Expected IOException is not thrown");
    } catch (IOException ex) {
      // Expected
    }

    // Ensure heartbeat from dead datanode is rejected with a command
    // that asks datanode to register again
    DatanodeCommand[] cmd = dnp.sendHeartbeat(reg, 0, 0, 0, 0, 0);
    Assert.assertEquals(1, cmd.length);
    Assert.assertEquals(cmd[0].getAction(), DatanodeCommand.REGISTER
        .getAction());
  }
}
