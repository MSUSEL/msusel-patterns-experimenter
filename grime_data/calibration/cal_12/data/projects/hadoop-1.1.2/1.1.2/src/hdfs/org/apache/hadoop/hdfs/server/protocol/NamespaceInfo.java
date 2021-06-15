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

import org.apache.hadoop.hdfs.protocol.FSConstants;
import org.apache.hadoop.hdfs.server.common.StorageInfo;
import org.apache.hadoop.io.UTF8;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableFactories;
import org.apache.hadoop.io.WritableFactory;
import org.apache.hadoop.util.VersionInfo;

/**
 * NamespaceInfo is returned by the name-node in reply 
 * to a data-node handshake.
 * 
 */
public class NamespaceInfo extends StorageInfo implements Writable {
  String revision;
  String version;
  int distributedUpgradeVersion;

  public NamespaceInfo() {
    super();
  }
  
  public NamespaceInfo(int nsID, long cT, int duVersion) {
    super(FSConstants.LAYOUT_VERSION, nsID, cT); 
    version = VersionInfo.getVersion();
    revision = VersionInfo.getRevision();
    this.distributedUpgradeVersion = duVersion;
  }
  
  public String getVersion() {
    return version;
  }

  public String getRevision() {
    return revision;
  }

  public int getDistributedUpgradeVersion() {
    return distributedUpgradeVersion;
  }
  
  /////////////////////////////////////////////////
  // Writable
  /////////////////////////////////////////////////
  static {                                      // register a ctor
    WritableFactories.setFactory
      (NamespaceInfo.class,
       new WritableFactory() {
         public Writable newInstance() { return new NamespaceInfo(); }
       });
  }

  public void write(DataOutput out) throws IOException {
    UTF8.writeString(out, getVersion());
    UTF8.writeString(out, getRevision());
    out.writeInt(getLayoutVersion());
    out.writeInt(getNamespaceID());
    out.writeLong(getCTime());
    out.writeInt(getDistributedUpgradeVersion());
  }

  public void readFields(DataInput in) throws IOException {
    version = UTF8.readString(in);
    revision = UTF8.readString(in);
    layoutVersion = in.readInt();
    namespaceID = in.readInt();
    cTime = in.readLong();
    distributedUpgradeVersion = in.readInt();
  }
}
