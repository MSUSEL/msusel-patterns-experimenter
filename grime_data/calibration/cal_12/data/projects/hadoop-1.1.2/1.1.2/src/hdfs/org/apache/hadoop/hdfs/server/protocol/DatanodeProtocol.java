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

import java.io.*;

import org.apache.hadoop.hdfs.DFSConfigKeys;
import org.apache.hadoop.hdfs.protocol.Block;
import org.apache.hadoop.hdfs.protocol.DatanodeID;
import org.apache.hadoop.hdfs.protocol.LocatedBlock;
import org.apache.hadoop.ipc.VersionedProtocol;

import org.apache.hadoop.security.KerberosInfo;

/**********************************************************************
 * Protocol that a DFS datanode uses to communicate with the NameNode.
 * It's used to upload current load information and block reports.
 *
 * The only way a NameNode can communicate with a DataNode is by
 * returning values from these functions.
 *
 **********************************************************************/
@KerberosInfo(
    serverPrincipal = DFSConfigKeys.DFS_NAMENODE_USER_NAME_KEY, 
    clientPrincipal = DFSConfigKeys.DFS_DATANODE_USER_NAME_KEY)
public interface DatanodeProtocol extends VersionedProtocol {
  /**
   * 26: Added an additional member to NamespaceInfo
   */
  public static final long versionID = 26L;
  
  // error code
  final static int NOTIFY = 0;
  final static int DISK_ERROR = 1; // there are still valid volumes on DN
  final static int INVALID_BLOCK = 2;
  final static int FATAL_DISK_ERROR = 3; // no valid volumes left on DN

  /**
   * Determines actions that data node should perform 
   * when receiving a datanode command. 
   */
  final static int DNA_UNKNOWN = 0;    // unknown action   
  final static int DNA_TRANSFER = 1;   // transfer blocks to another datanode
  final static int DNA_INVALIDATE = 2; // invalidate blocks
  final static int DNA_SHUTDOWN = 3;   // shutdown node
  final static int DNA_REGISTER = 4;   // re-register
  final static int DNA_FINALIZE = 5;   // finalize previous upgrade
  final static int DNA_RECOVERBLOCK = 6;  // request a block recovery
  final static int DNA_ACCESSKEYUPDATE = 7;  // update access key
  final static int DNA_BALANCERBANDWIDTHUPDATE = 8; // update balancer bandwidth

  /** 
   * Register Datanode.
   *
   * @see org.apache.hadoop.hdfs.server.datanode.DataNode#dnRegistration
   * @see org.apache.hadoop.hdfs.server.namenode.FSNamesystem#registerDatanode(DatanodeRegistration)
   * 
   * @return updated {@link org.apache.hadoop.hdfs.server.protocol.DatanodeRegistration}, which contains 
   * new storageID if the datanode did not have one and
   * registration ID for further communication.
   */
  public DatanodeRegistration register(DatanodeRegistration registration
                                       ) throws IOException;
  /**
   * sendHeartbeat() tells the NameNode that the DataNode is still
   * alive and well.  Includes some status info, too. 
   * It also gives the NameNode a chance to return 
   * an array of "DatanodeCommand" objects.
   * A DatanodeCommand tells the DataNode to invalidate local block(s), 
   * or to copy them to other DataNodes, etc.
   */
  public DatanodeCommand[] sendHeartbeat(DatanodeRegistration registration,
                                       long capacity,
                                       long dfsUsed, long remaining,
                                       int xmitsInProgress,
                                       int xceiverCount) throws IOException;

  /**
   * blockReport() tells the NameNode about all the locally-stored blocks.
   * The NameNode returns an array of Blocks that have become obsolete
   * and should be deleted.  This function is meant to upload *all*
   * the locally-stored blocks.  It's invoked upon startup and then
   * infrequently afterwards.
   * @param registration
   * @param blocks - the block list as an array of longs.
   *     Each block is represented as 2 longs.
   *     This is done instead of Block[] to reduce memory used by block reports.
   *     
   * @return - the next command for DN to process.
   * @throws IOException
   */
  public DatanodeCommand blockReport(DatanodeRegistration registration,
                                     long[] blocks) throws IOException;
  
  /**
   * blocksBeingWrittenReport() tells the NameNode about the blocks-being-
   * written information
   * 
   * @param registration
   * @param blocks
   * @throws IOException
   */
  public void blocksBeingWrittenReport(DatanodeRegistration registration,
      long[] blocks) throws IOException;
    
  /**
   * blockReceived() allows the DataNode to tell the NameNode about
   * recently-received block data, with a hint for pereferred replica
   * to be deleted when there is any excessive blocks.
   * For example, whenever client code
   * writes a new Block here, or another DataNode copies a Block to
   * this DataNode, it will call blockReceived().
   */
  public void blockReceived(DatanodeRegistration registration,
                            Block blocks[],
                            String[] delHints) throws IOException;

  /**
   * errorReport() tells the NameNode about something that has gone
   * awry.  Useful for debugging.
   */
  public void errorReport(DatanodeRegistration registration,
                          int errorCode, 
                          String msg) throws IOException;
    
  public NamespaceInfo versionRequest() throws IOException;

  /**
   * This is a very general way to send a command to the name-node during
   * distributed upgrade process.
   * 
   * The generosity is because the variety of upgrade commands is unpredictable.
   * The reply from the name-node is also received in the form of an upgrade 
   * command. 
   * 
   * @return a reply in the form of an upgrade command
   */
  UpgradeCommand processUpgradeCommand(UpgradeCommand comm) throws IOException;
  
  /**
   * same as {@link org.apache.hadoop.hdfs.protocol.ClientProtocol#reportBadBlocks(LocatedBlock[])}
   * }
   */
  public void reportBadBlocks(LocatedBlock[] blocks) throws IOException;
  
  /**
   * Get the next GenerationStamp to be associated with the specified
   * block.
   * 
   * @param block block
   * @param fromNN if it is for lease recovery initiated by NameNode
   * @return a new generation stamp
   */
  public long nextGenerationStamp(Block block, boolean fromNN) throws IOException;

  /**
   * Commit block synchronization in lease recovery
   */
  public void commitBlockSynchronization(Block block,
      long newgenerationstamp, long newlength,
      boolean closeFile, boolean deleteblock, DatanodeID[] newtargets
      ) throws IOException;
}
