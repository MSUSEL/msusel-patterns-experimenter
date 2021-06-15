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

import java.io.IOException;

import org.apache.hadoop.hdfs.DFSConfigKeys;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.hdfs.security.token.block.ExportedBlockKeys;
import org.apache.hadoop.hdfs.server.namenode.CheckpointSignature;
import org.apache.hadoop.ipc.VersionedProtocol;
import org.apache.hadoop.security.KerberosInfo;

/*****************************************************************************
 * Protocol that a secondary NameNode uses to communicate with the NameNode.
 * It's used to get part of the name node state
 *****************************************************************************/
@KerberosInfo(
    serverPrincipal = DFSConfigKeys.DFS_NAMENODE_USER_NAME_KEY,
    clientPrincipal = DFSConfigKeys.DFS_NAMENODE_USER_NAME_KEY)
public interface NamenodeProtocol extends VersionedProtocol {
  /**
   * 3: new method added: getAccessKeys()
   */
  public static final long versionID = 3L;

  /** Get a list of blocks belonged to <code>datanode</code>
    * whose total size is equal to <code>size</code>
   * @param datanode  a data node
   * @param size      requested size
   * @return          a list of blocks & their locations
   * @throws RemoteException if size is less than or equal to 0 or
                                   datanode does not exist
   */
  public BlocksWithLocations getBlocks(DatanodeInfo datanode, long size)
  throws IOException;

  /**
   * Get the current block keys
   * 
   * @return ExportedBlockKeys containing current block keys
   * @throws IOException 
   */
  public ExportedBlockKeys getBlockKeys() throws IOException;

  /**
   * Get the size of the current edit log (in bytes).
   * @return The number of bytes in the current edit log.
   * @throws IOException
   */
  public long getEditLogSize() throws IOException;

  /**
   * Closes the current edit log and opens a new one. The 
   * call fails if the file system is in SafeMode.
   * @throws IOException
   * @return a unique token to identify this transaction.
   */
  public CheckpointSignature rollEditLog() throws IOException;

  /**
   * Rolls the fsImage log. It removes the old fsImage, copies the
   * new image to fsImage, removes the old edits and renames edits.new 
   * to edits. The call fails if any of the four files are missing.
   * @throws IOException
   */
  public void rollFsImage() throws IOException;
}
