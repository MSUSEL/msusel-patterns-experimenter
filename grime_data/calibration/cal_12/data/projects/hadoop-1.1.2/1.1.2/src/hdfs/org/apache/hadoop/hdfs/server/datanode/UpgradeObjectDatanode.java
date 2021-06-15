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

import org.apache.hadoop.hdfs.protocol.FSConstants;
import org.apache.hadoop.hdfs.server.common.HdfsConstants;
import org.apache.hadoop.hdfs.server.common.UpgradeObject;
import org.apache.hadoop.hdfs.server.protocol.DatanodeProtocol;
import org.apache.hadoop.hdfs.server.protocol.NamespaceInfo;
import org.apache.hadoop.hdfs.server.protocol.UpgradeCommand;
import org.apache.hadoop.util.StringUtils;
import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Base class for data-node upgrade objects.
 * Data-node upgrades are run in separate threads.
 */
public abstract class UpgradeObjectDatanode extends UpgradeObject implements Runnable {
  private DataNode dataNode = null;

  public HdfsConstants.NodeType getType() {
    return HdfsConstants.NodeType.DATA_NODE;
  }

  protected DataNode getDatanode() {
    return dataNode;
  }

  void setDatanode(DataNode dataNode) {
    this.dataNode = dataNode;
  }

  /**
   * Specifies how the upgrade is performed. 
   * @throws IOException
   */
  public abstract void doUpgrade() throws IOException;

  /**
   * Specifies what to do before the upgrade is started.
   * 
   * The default implementation checks whether the data-node missed the upgrade
   * and throws an exception if it did. This leads to the data-node shutdown.
   * 
   * Data-nodes usually start distributed upgrade when the name-node replies
   * to its heartbeat with a start upgrade command.
   * Sometimes though, e.g. when a data-node missed the upgrade and wants to
   * catchup with the rest of the cluster, it is necessary to initiate the 
   * upgrade directly on the data-node, since the name-node might not ever 
   * start it. An override of this method should then return true.
   * And the upgrade will start after data-ndoe registration but before sending
   * its first heartbeat.
   * 
   * @param nsInfo name-node versions, verify that the upgrade
   * object can talk to this name-node version if necessary.
   * 
   * @throws IOException
   * @return true if data-node itself should start the upgrade or 
   * false if it should wait until the name-node starts the upgrade.
   */
  boolean preUpgradeAction(NamespaceInfo nsInfo) throws IOException {
    int nsUpgradeVersion = nsInfo.getDistributedUpgradeVersion();
    if(nsUpgradeVersion >= getVersion())
      return false; // name-node will perform the upgrade
    // Missed the upgrade. Report problem to the name-node and throw exception
    String errorMsg = 
              "\n   Data-node missed a distributed upgrade and will shutdown."
            + "\n   " + getDescription() + "."
            + " Name-node version = " + nsInfo.getLayoutVersion() + ".";
    DataNode.LOG.fatal( errorMsg );
    try {
      dataNode.namenode.errorReport(dataNode.dnRegistration,
                                    DatanodeProtocol.NOTIFY, errorMsg);
    } catch(SocketTimeoutException e) {  // namenode is busy
      DataNode.LOG.info("Problem connecting to server: " 
                        + dataNode.getNameNodeAddr());
    }
    throw new IOException(errorMsg);
  }

  public void run() {
    assert dataNode != null : "UpgradeObjectDatanode.dataNode is null";
    while(dataNode.shouldRun) {
      try {
        doUpgrade();
      } catch(Exception e) {
        DataNode.LOG.error(StringUtils.stringifyException(e));
      }
      break;
    }

    // report results
    if(getUpgradeStatus() < 100) {
      DataNode.LOG.info("\n   Distributed upgrade for DataNode version " 
          + getVersion() + " to current LV " 
          + FSConstants.LAYOUT_VERSION + " cannot be completed.");
    }

    // Complete the upgrade by calling the manager method
    try {
      dataNode.upgradeManager.completeUpgrade();
    } catch(IOException e) {
      DataNode.LOG.error(StringUtils.stringifyException(e));
    }
  }

  /**
   * Complete upgrade and return a status complete command for broadcasting.
   * 
   * Data-nodes finish upgrade at different times.
   * The data-node needs to re-confirm with the name-node that the upgrade
   * is complete while other nodes are still upgrading.
   */
  public UpgradeCommand completeUpgrade() throws IOException {
    return new UpgradeCommand(UpgradeCommand.UC_ACTION_REPORT_STATUS,
                              getVersion(), (short)100);
  }
}
