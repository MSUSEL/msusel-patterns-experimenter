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
package org.apache.hadoop.security.token.delegation;

//import org.apache.hadoop.classification.InterfaceAudience;
//import static org.apache.hadoop.classification.InterfaceAudience.LimitedPrivate.Project.HDFS;
//import static org.apache.hadoop.classification.InterfaceAudience.LimitedPrivate.Project.MAPREDUCE;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableUtils;
import org.apache.hadoop.security.KerberosName;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.security.token.TokenIdentifier;

//@InterfaceAudience.LimitedPrivate({HDFS, MAPREDUCE})
public abstract class AbstractDelegationTokenIdentifier 
extends TokenIdentifier {
  private static final byte VERSION = 0;

  private Text owner;
  private Text renewer;
  private Text realUser;
  private long issueDate;
  private long maxDate;
  private int sequenceNumber;
  private int masterKeyId = 0;
  
  public AbstractDelegationTokenIdentifier() {
    this(new Text(), new Text(), new Text());
  }
  
  public AbstractDelegationTokenIdentifier(Text owner, Text renewer, Text realUser) {
    if (owner == null) {
      this.owner = new Text();
    } else {
      this.owner = owner;
    }
    if (renewer == null) {
      this.renewer = new Text();
    } else {
      KerberosName renewerKrbName = new KerberosName(renewer.toString());
      try {
        this.renewer = new Text(renewerKrbName.getShortName());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    if (realUser == null) {
      this.realUser = new Text();
    } else {
      this.realUser = realUser;
    }
    issueDate = 0;
    maxDate = 0;
  }

  @Override
  public abstract Text getKind();
  
  /**
   * Get the username encoded in the token identifier
   * 
   * @return the username or owner
   */
  public UserGroupInformation getUser() {
    if ( (owner == null) || ("".equals(owner.toString()))) {
      return null;
    }
    if ((realUser == null) || ("".equals(realUser.toString()))
        || realUser.equals(owner)) {
      return UserGroupInformation.createRemoteUser(owner.toString());
    } else {
      UserGroupInformation realUgi = UserGroupInformation
          .createRemoteUser(realUser.toString());
      return UserGroupInformation.createProxyUser(owner.toString(), realUgi);
    }
  }

  public Text getRenewer() {
    return renewer;
  }
  
  public void setIssueDate(long issueDate) {
    this.issueDate = issueDate;
  }
  
  public long getIssueDate() {
    return issueDate;
  }
  
  public void setMaxDate(long maxDate) {
    this.maxDate = maxDate;
  }
  
  public long getMaxDate() {
    return maxDate;
  }

  public void setSequenceNumber(int seqNum) {
    this.sequenceNumber = seqNum;
  }
  
  public int getSequenceNumber() {
    return sequenceNumber;
  }

  public void setMasterKeyId(int newId) {
    masterKeyId = newId;
  }

  public int getMasterKeyId() {
    return masterKeyId;
  }

  static boolean isEqual(Object a, Object b) {
    return a == null ? b == null : a.equals(b);
  }
  
  /** {@inheritDoc} */
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof AbstractDelegationTokenIdentifier) {
      AbstractDelegationTokenIdentifier that = (AbstractDelegationTokenIdentifier) obj;
      return this.sequenceNumber == that.sequenceNumber 
          && this.issueDate == that.issueDate 
          && this.maxDate == that.maxDate
          && this.masterKeyId == that.masterKeyId
          && isEqual(this.owner, that.owner) 
          && isEqual(this.renewer, that.renewer)
          && isEqual(this.realUser, that.realUser);
    }
    return false;
  }

  /** {@inheritDoc} */
  public int hashCode() {
    return this.sequenceNumber;
  }
  
  public void readFields(DataInput in) throws IOException {
    byte version = in.readByte();
    if (version != VERSION) {
	throw new IOException("Unknown version of delegation token " + 
                              version);
    }
    owner.readFields(in);
    renewer.readFields(in);
    realUser.readFields(in);
    issueDate = WritableUtils.readVLong(in);
    maxDate = WritableUtils.readVLong(in);
    sequenceNumber = WritableUtils.readVInt(in);
    masterKeyId = WritableUtils.readVInt(in);
  }

  public void write(DataOutput out) throws IOException {
    out.writeByte(VERSION);
    owner.write(out);
    renewer.write(out);
    realUser.write(out);
    WritableUtils.writeVLong(out, issueDate);
    WritableUtils.writeVLong(out, maxDate);
    WritableUtils.writeVInt(out, sequenceNumber);
    WritableUtils.writeVInt(out, masterKeyId);
  }
  
  public String toString() {
    StringBuilder buffer = new StringBuilder();
    buffer
        .append("owner=" + owner + ", renewer=" + renewer + ", realUser="
            + realUser + ", issueDate=" + issueDate + ", maxDate=" + maxDate
            + ", sequenceNumber=" + sequenceNumber + ", masterKeyId="
            + masterKeyId);
    return buffer.toString();
  }
}
