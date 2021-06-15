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
package org.apache.hadoop.hdfs.server.common;

import java.io.IOException;
import java.util.SortedSet;

import org.apache.hadoop.hdfs.protocol.FSConstants;
import org.apache.hadoop.hdfs.server.protocol.UpgradeCommand;

/**
 * Generic upgrade manager.
 * 
 * {@link #broadcastCommand} is the command that should be 
 *
 */
public abstract class UpgradeManager {
  protected SortedSet<Upgradeable> currentUpgrades = null;
  protected boolean upgradeState = false; // true if upgrade is in progress
  protected int upgradeVersion = 0;
  protected UpgradeCommand broadcastCommand = null;

  public synchronized UpgradeCommand getBroadcastCommand() {
    return this.broadcastCommand;
  }

  public boolean getUpgradeState() {
    return this.upgradeState;
  }

  public int getUpgradeVersion(){
    return this.upgradeVersion;
  }

  public void setUpgradeState(boolean uState, int uVersion) {
    this.upgradeState = uState;
    this.upgradeVersion = uVersion;
  }

  public SortedSet<Upgradeable> getDistributedUpgrades() throws IOException {
    return UpgradeObjectCollection.getDistributedUpgrades(
                                            getUpgradeVersion(), getType());
  }

  public short getUpgradeStatus() {
    if(currentUpgrades == null)
      return 100;
    return currentUpgrades.first().getUpgradeStatus();
  }

  public boolean initializeUpgrade() throws IOException {
    currentUpgrades = getDistributedUpgrades();
    if(currentUpgrades == null) {
      // set new upgrade state
      setUpgradeState(false, FSConstants.LAYOUT_VERSION);
      return false;
    }
    Upgradeable curUO = currentUpgrades.first();
    // set and write new upgrade state into disk
    setUpgradeState(true, curUO.getVersion());
    return true;
  }

  public boolean isUpgradeCompleted() {
    if (currentUpgrades == null) {
      return true;
    }
    return false;
  }

  public abstract HdfsConstants.NodeType getType();
  public abstract boolean startUpgrade() throws IOException;
  public abstract void completeUpgrade() throws IOException;
}
