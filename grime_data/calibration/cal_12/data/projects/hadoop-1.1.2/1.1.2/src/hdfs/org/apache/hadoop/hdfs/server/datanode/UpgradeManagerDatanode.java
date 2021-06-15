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

import org.apache.hadoop.hdfs.protocol.FSConstants;
import org.apache.hadoop.hdfs.server.common.HdfsConstants;
import org.apache.hadoop.hdfs.server.common.UpgradeManager;
import org.apache.hadoop.hdfs.server.protocol.NamespaceInfo;
import org.apache.hadoop.hdfs.server.protocol.UpgradeCommand;
import org.apache.hadoop.util.Daemon;

/**
 * Upgrade manager for data-nodes.
 *
 * Distributed upgrades for a data-node are performed in a separate thread.
 * The upgrade starts when the data-node receives the start upgrade command
 * from the namenode. At that point the manager finds a respective upgrade
 * object and starts a daemon in order to perform the upgrade defined by the 
 * object.
 */
class UpgradeManagerDatanode extends UpgradeManager {
  DataNode dataNode = null;
  Daemon upgradeDaemon = null;

  UpgradeManagerDatanode(DataNode dataNode) {
    super();
    this.dataNode = dataNode;
  }

  public HdfsConstants.NodeType getType() {
    return HdfsConstants.NodeType.DATA_NODE;
  }

  synchronized void initializeUpgrade(NamespaceInfo nsInfo) throws IOException {
    if( ! super.initializeUpgrade())
      return; // distr upgrade is not needed
    DataNode.LOG.info("\n   Distributed upgrade for DataNode " 
        + dataNode.dnRegistration.getName() 
        + " version " + getUpgradeVersion() + " to current LV " 
        + FSConstants.LAYOUT_VERSION + " is initialized.");
    UpgradeObjectDatanode curUO = (UpgradeObjectDatanode)currentUpgrades.first();
    curUO.setDatanode(dataNode);
    upgradeState = curUO.preUpgradeAction(nsInfo);
    // upgradeState is true if the data-node should start the upgrade itself
  }

  /**
   * Start distributed upgrade.
   * Instantiates distributed upgrade objects.
   * 
   * @return true if distributed upgrade is required or false otherwise
   * @throws IOException
   */
  public synchronized boolean startUpgrade() throws IOException {
    if(upgradeState) {  // upgrade is already in progress
      assert currentUpgrades != null : 
        "UpgradeManagerDatanode.currentUpgrades is null.";
      UpgradeObjectDatanode curUO = (UpgradeObjectDatanode)currentUpgrades.first();
      curUO.startUpgrade();
      return true;
    }
    if(broadcastCommand != null) {
      if(broadcastCommand.getVersion() > this.getUpgradeVersion()) {
        // stop broadcasting, the cluster moved on
        // start upgrade for the next version
        broadcastCommand = null;
      } else {
        // the upgrade has been finished by this data-node,
        // but the cluster is still running it, 
        // reply with the broadcast command
        assert currentUpgrades == null : 
          "UpgradeManagerDatanode.currentUpgrades is not null.";
        assert upgradeDaemon == null : 
          "UpgradeManagerDatanode.upgradeDaemon is not null.";
        dataNode.namenode.processUpgradeCommand(broadcastCommand);
        return true;
      }
    }
    if(currentUpgrades == null)
      currentUpgrades = getDistributedUpgrades();
    if(currentUpgrades == null) {
      DataNode.LOG.info("\n   Distributed upgrade for DataNode version " 
          + getUpgradeVersion() + " to current LV " 
          + FSConstants.LAYOUT_VERSION + " cannot be started. "
          + "The upgrade object is not defined.");
      return false;
    }
    upgradeState = true;
    UpgradeObjectDatanode curUO = (UpgradeObjectDatanode)currentUpgrades.first();
    curUO.setDatanode(dataNode);
    curUO.startUpgrade();
    upgradeDaemon = new Daemon(curUO);
    upgradeDaemon.start();
    DataNode.LOG.info("\n   Distributed upgrade for DataNode " 
        + dataNode.dnRegistration.getName() 
        + " version " + getUpgradeVersion() + " to current LV " 
        + FSConstants.LAYOUT_VERSION + " is started.");
    return true;
  }

  synchronized void processUpgradeCommand(UpgradeCommand command
                                          ) throws IOException {
    assert command.getAction() == UpgradeCommand.UC_ACTION_START_UPGRADE :
      "Only start upgrade action can be processed at this time.";
    this.upgradeVersion = command.getVersion();
    // Start distributed upgrade
    if(startUpgrade()) // upgrade started
      return;
    throw new IOException(
        "Distributed upgrade for DataNode " + dataNode.dnRegistration.getName() 
        + " version " + getUpgradeVersion() + " to current LV " 
        + FSConstants.LAYOUT_VERSION + " cannot be started. "
        + "The upgrade object is not defined.");
  }

  public synchronized void completeUpgrade() throws IOException {
    assert currentUpgrades != null : 
      "UpgradeManagerDatanode.currentUpgrades is null.";
    UpgradeObjectDatanode curUO = (UpgradeObjectDatanode)currentUpgrades.first();
    broadcastCommand = curUO.completeUpgrade();
    upgradeState = false;
    currentUpgrades = null;
    upgradeDaemon = null;
    DataNode.LOG.info("\n   Distributed upgrade for DataNode " 
        + dataNode.dnRegistration.getName() 
        + " version " + getUpgradeVersion() + " to current LV " 
        + FSConstants.LAYOUT_VERSION + " is complete.");
  }

  synchronized void shutdownUpgrade() {
    if(upgradeDaemon != null)
      upgradeDaemon.interrupt();
  }
}
