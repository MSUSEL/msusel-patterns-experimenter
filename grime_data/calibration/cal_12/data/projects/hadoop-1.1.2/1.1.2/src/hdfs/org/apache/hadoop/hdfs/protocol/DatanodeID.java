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
package org.apache.hadoop.hdfs.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.UTF8;
import org.apache.hadoop.io.WritableComparable;

/**
 * DatanodeID is composed of the data node 
 * name (hostname:portNumber) and the data storage ID, 
 * which it currently represents.
 * 
 */
public class DatanodeID implements WritableComparable<DatanodeID> {
  public static final DatanodeID[] EMPTY_ARRAY = {}; 

  public String name;       // hostname:port (data transfer port)
  public String storageID;  // unique per cluster storageID
  protected int infoPort;   // info server port
  public int ipcPort;       // ipc server port

  /** Equivalent to DatanodeID(""). */
  public DatanodeID() {this("");}

  /** Equivalent to DatanodeID(nodeName, "", -1, -1). */
  public DatanodeID(String nodeName) {this(nodeName, "", -1, -1);}

  /**
   * DatanodeID copy constructor
   * 
   * @param from
   */
  public DatanodeID(DatanodeID from) {
    this(from.getName(),
        from.getStorageID(),
        from.getInfoPort(),
        from.getIpcPort());
  }
  
  /**
   * Create DatanodeID
   * @param nodeName (hostname:portNumber) 
   * @param storageID data storage ID
   * @param infoPort info server port 
   * @param ipcPort ipc server port
   */
  public DatanodeID(String nodeName, String storageID,
      int infoPort, int ipcPort) {
    this.name = nodeName;
    this.storageID = storageID;
    this.infoPort = infoPort;
    this.ipcPort = ipcPort;
  }
  
  /**
   * @return hostname:portNumber.
   */
  public String getName() {
    return name;
  }
  
  /**
   * @return data storage ID.
   */
  public String getStorageID() {
    return this.storageID;
  }

  /**
   * @return infoPort (the port at which the HTTP server bound to)
   */
  public int getInfoPort() {
    return infoPort;
  }

  /**
   * @return ipcPort (the port at which the IPC server bound to)
   */
  public int getIpcPort() {
    return ipcPort;
  }

  /**
   * sets the data storage ID.
   */
  public void setStorageID(String storageID) {
    this.storageID = storageID;
  }

  /**
   * @return hostname and no :portNumber.
   */
  public String getHost() {
    int colon = name.indexOf(":");
    if (colon < 0) {
      return name;
    } else {
      return name.substring(0, colon);
    }
  }
  
  public int getPort() {
    int colon = name.indexOf(":");
    if (colon < 0) {
      return 50010; // default port.
    }
    return Integer.parseInt(name.substring(colon+1));
  }

  public boolean equals(Object to) {
    if (this == to) {
      return true;
    }
    if (!(to instanceof DatanodeID)) {
      return false;
    }
    return (name.equals(((DatanodeID)to).getName()) &&
            storageID.equals(((DatanodeID)to).getStorageID()));
  }
  
  public int hashCode() {
    return name.hashCode()^ storageID.hashCode();
  }
  
  public String toString() {
    return name;
  }
  
  /**
   * Update fields when a new registration request comes in.
   * Note that this does not update storageID.
   */
  public void updateRegInfo(DatanodeID nodeReg) {
    name = nodeReg.getName();
    infoPort = nodeReg.getInfoPort();
    ipcPort = nodeReg.getIpcPort();
    // update any more fields added in future.
  }
    
  /** Comparable.
   * Basis of compare is the String name (host:portNumber) only.
   * @param that
   * @return as specified by Comparable.
   */
  public int compareTo(DatanodeID that) {
    return name.compareTo(that.getName());
  }

  /////////////////////////////////////////////////
  // Writable
  /////////////////////////////////////////////////
  /** {@inheritDoc} */
  public void write(DataOutput out) throws IOException {
    UTF8.writeString(out, name);
    UTF8.writeString(out, storageID);
    out.writeShort(infoPort);
  }

  /** {@inheritDoc} */
  public void readFields(DataInput in) throws IOException {
    name = UTF8.readString(in);
    storageID = UTF8.readString(in);
    // the infoPort read could be negative, if the port is a large number (more
    // than 15 bits in storage size (but less than 16 bits).
    // So chop off the first two bytes (and hence the signed bits) before 
    // setting the field.
    this.infoPort = in.readShort() & 0x0000ffff;
  }
}
