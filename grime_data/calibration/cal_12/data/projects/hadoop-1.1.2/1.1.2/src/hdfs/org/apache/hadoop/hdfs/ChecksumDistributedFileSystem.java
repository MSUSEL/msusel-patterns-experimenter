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
package org.apache.hadoop.hdfs;

import java.io.*;
import java.net.*;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.hdfs.protocol.FSConstants;
import org.apache.hadoop.hdfs.protocol.FSConstants.UpgradeAction;
import org.apache.hadoop.hdfs.server.common.UpgradeStatusReport;
import org.apache.hadoop.conf.Configuration;

/**
 * An implementation of ChecksumFileSystem over DistributedFileSystem. 
 * Note that as of now (May 07), DistributedFileSystem natively checksums 
 * all of its data. Using this class is not be necessary in most cases.
 * Currently provided mainly for backward compatibility and testing.
 */
public class ChecksumDistributedFileSystem extends ChecksumFileSystem {
  
  public ChecksumDistributedFileSystem() {
    super( new DistributedFileSystem() );
  }

  /** @deprecated */
  public ChecksumDistributedFileSystem(InetSocketAddress namenode,
                                       Configuration conf) throws IOException {
    super( new DistributedFileSystem(namenode, conf) );
  }
  
  /** Any extra interface that DistributeFileSystem provides can be
   * accessed with this.*/
  DistributedFileSystem getDFS() {
    return (DistributedFileSystem)fs;
  }

  /** Return the total raw capacity of the filesystem, disregarding
   * replication .*/
  public long getRawCapacity() throws IOException{
    return getDFS().getRawCapacity();
  }

  /** Return the total raw used space in the filesystem, disregarding
   * replication .*/
  public long getRawUsed() throws IOException{
    return getDFS().getRawUsed();
  }

  /** Return statistics for each datanode. */
  public DatanodeInfo[] getDataNodeStats() throws IOException {
    return getDFS().getDataNodeStats();
  }
    
  /**
   * Enter, leave or get safe mode.
   *  
   * @see org.apache.hadoop.hdfs.protocol.ClientProtocol#setSafeMode(FSConstants.SafeModeAction)
   */
  public boolean setSafeMode(FSConstants.SafeModeAction action) 
    throws IOException {
    return getDFS().setSafeMode(action);
  }

  /*
   * Refreshes the list of hosts and excluded hosts from the configured 
   * files.  
   */
  public void refreshNodes() throws IOException {
    getDFS().refreshNodes();
  }

  /**
   * Finalize previously upgraded files system state.
   */
  public void finalizeUpgrade() throws IOException {
    getDFS().finalizeUpgrade();
  }

  public UpgradeStatusReport distributedUpgradeProgress(UpgradeAction action
                                                        ) throws IOException {
    return getDFS().distributedUpgradeProgress(action);
  }

  /*
   * Dumps dfs data structures into specified file.
   */
  public void metaSave(String pathname) throws IOException {
    getDFS().metaSave(pathname);
  }

  /**
   * We need to find the blocks that didn't match.  Likely only one 
   * is corrupt but we will report both to the namenode.  In the future,
   * we can consider figuring out exactly which block is corrupt.
   */
  public boolean reportChecksumFailure(Path f, 
                                       FSDataInputStream in, long inPos, 
                                       FSDataInputStream sums, long sumsPos) {
    return getDFS().reportChecksumFailure(f, in, inPos, sums, sumsPos);
  }
  
  
  /**
   * Returns the stat information about the file.
   */
  @Override
  public FileStatus getFileStatus(Path f) throws IOException {
    return getDFS().getFileStatus(f);
  }

}
