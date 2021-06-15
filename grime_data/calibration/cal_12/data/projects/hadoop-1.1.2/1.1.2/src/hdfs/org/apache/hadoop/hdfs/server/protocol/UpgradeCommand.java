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
package org.apache.hadoop.hdfs.server.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableFactories;
import org.apache.hadoop.io.WritableFactory;

/**
 * This as a generic distributed upgrade command.
 * 
 * During the upgrade cluster components send upgrade commands to each other
 * in order to obtain or share information with them.
 * It is supposed that each upgrade defines specific upgrade command by
 * deriving them from this class.
 * The upgrade command contains version of the upgrade, which is verified 
 * on the receiving side and current status of the upgrade.
 */
public class UpgradeCommand extends DatanodeCommand {
  final static int UC_ACTION_UNKNOWN = DatanodeProtocol.DNA_UNKNOWN;
  public final static int UC_ACTION_REPORT_STATUS = 100; // report upgrade status
  public final static int UC_ACTION_START_UPGRADE = 101; // start upgrade

  private int version;
  private short upgradeStatus;

  public UpgradeCommand() {
    super(UC_ACTION_UNKNOWN);
    this.version = 0;
    this.upgradeStatus = 0;
  }

  public UpgradeCommand(int action, int version, short status) {
    super(action);
    this.version = version;
    this.upgradeStatus = status;
  }

  public int getVersion() {
    return this.version;
  }

  public short getCurrentStatus() {
    return this.upgradeStatus;
  }

  /////////////////////////////////////////////////
  // Writable
  /////////////////////////////////////////////////
  static {                                      // register a ctor
    WritableFactories.setFactory
      (UpgradeCommand.class,
       new WritableFactory() {
         public Writable newInstance() { return new UpgradeCommand(); }
       });
  }

  /**
   */
  public void write(DataOutput out) throws IOException {
    super.write(out);
    out.writeInt(this.version);
    out.writeShort(this.upgradeStatus);
  }

  /**
   */
  public void readFields(DataInput in) throws IOException {
    super.readFields(in);
    this.version = in.readInt();
    this.upgradeStatus = in.readShort();
  }
}
