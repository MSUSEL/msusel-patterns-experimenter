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

import org.apache.hadoop.hdfs.protocol.Block;
import org.apache.hadoop.ipc.Server;

import java.util.*;

/**
 * Stores information about all corrupt blocks in the File System.
 * A Block is considered corrupt only if all of its replicas are
 * corrupt. While reporting replicas of a Block, we hide any corrupt
 * copies. These copies are removed once Block is found to have 
 * expected number of good replicas.
 * Mapping: Block -> TreeSet<DatanodeDescriptor> 
 */

public class CorruptReplicasMap{

  private Map<Block, Collection<DatanodeDescriptor>> corruptReplicasMap =
    new TreeMap<Block, Collection<DatanodeDescriptor>>();
  
  /**
   * Mark the block belonging to datanode as corrupt.
   *
   * @param blk Block to be added to CorruptReplicasMap
   * @param dn DatanodeDescriptor which holds the corrupt replica
   */
  public void addToCorruptReplicasMap(Block blk, DatanodeDescriptor dn) {
    Collection<DatanodeDescriptor> nodes = getNodes(blk);
    if (nodes == null) {
      nodes = new TreeSet<DatanodeDescriptor>();
      corruptReplicasMap.put(blk, nodes);
    }
    if (!nodes.contains(dn)) {
      nodes.add(dn);
      NameNode.stateChangeLog.info("BLOCK NameSystem.addToCorruptReplicasMap: "+
                                   blk.getBlockName() +
                                   " added as corrupt on " + dn.getName() +
                                   " by " + Server.getRemoteIp());
    } else {
      NameNode.stateChangeLog.info("BLOCK NameSystem.addToCorruptReplicasMap: "+
                                   "duplicate requested for " + 
                                   blk.getBlockName() + " to add as corrupt " +
                                   "on " + dn.getName() +
                                   " by " + Server.getRemoteIp());
    }
  }

  /**
   * Remove Block from CorruptBlocksMap
   *
   * @param blk Block to be removed
   */
  void removeFromCorruptReplicasMap(Block blk) {
    if (corruptReplicasMap != null) {
      corruptReplicasMap.remove(blk);
    }
  }

  /**
   * Remove the block at the given datanode from CorruptBlockMap
   * @param blk block to be removed
   * @param datanode datanode where the block is located
   * @return true if the removal is successful; 
             false if the replica is not in the map
   */ 
  boolean removeFromCorruptReplicasMap(Block blk, DatanodeDescriptor datanode) {
    Collection<DatanodeDescriptor> datanodes = corruptReplicasMap.get(blk);
    if (datanodes==null)
      return false;
    if (datanodes.remove(datanode)) { // remove the replicas
      if (datanodes.isEmpty()) {
        // remove the block if there is no more corrupted replicas
        corruptReplicasMap.remove(blk);
      }
      return true;
    }
    return false;
  }
    

  /**
   * Get Nodes which have corrupt replicas of Block
   * 
   * @param blk Block for which nodes are requested
   * @return collection of nodes. Null if does not exists
   */
  Collection<DatanodeDescriptor> getNodes(Block blk) {
    return corruptReplicasMap.get(blk);
  }

  /**
   * Check if replica belonging to Datanode is corrupt
   *
   * @param blk Block to check
   * @param node DatanodeDescriptor which holds the replica
   * @return true if replica is corrupt, false if does not exists in this map
   */
  boolean isReplicaCorrupt(Block blk, DatanodeDescriptor node) {
    Collection<DatanodeDescriptor> nodes = getNodes(blk);
    return ((nodes != null) && (nodes.contains(node)));
  }

  public int numCorruptReplicas(Block blk) {
    Collection<DatanodeDescriptor> nodes = getNodes(blk);
    return (nodes == null) ? 0 : nodes.size();
  }
  
  public int size() {
    return corruptReplicasMap.size();
  }
}
