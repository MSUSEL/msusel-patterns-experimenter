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

import org.apache.hadoop.hdfs.protocol.FSConstants;
import org.apache.hadoop.hdfs.server.common.HdfsConstants;
import org.apache.hadoop.hdfs.server.common.UpgradeObject;
import org.apache.hadoop.hdfs.server.protocol.UpgradeCommand;

/**
 * Base class for name-node upgrade objects.
 * Data-node upgrades are run in separate threads.
 */
public abstract class UpgradeObjectNamenode extends UpgradeObject {

  /**
   * Process an upgrade command.
   * RPC has only one very generic command for all upgrade related inter 
   * component communications. 
   * The actual command recognition and execution should be handled here.
   * The reply is sent back also as an UpgradeCommand.
   * 
   * @param command
   * @return the reply command which is analyzed on the client side.
   */
  public abstract UpgradeCommand processUpgradeCommand(UpgradeCommand command
                                               ) throws IOException;

  public HdfsConstants.NodeType getType() {
    return HdfsConstants.NodeType.NAME_NODE;
  }

  /**
   */
  public UpgradeCommand startUpgrade() throws IOException {
    // broadcast that data-nodes must start the upgrade
    return new UpgradeCommand(UpgradeCommand.UC_ACTION_START_UPGRADE,
                              getVersion(), (short)0);
  }

  protected FSNamesystem getFSNamesystem() {
    return FSNamesystem.getFSNamesystem();
  }

  public void forceProceed() throws IOException {
    // do nothing by default
    NameNode.LOG.info("forceProceed() is not defined for the upgrade. " 
        + getDescription());
  }
}
