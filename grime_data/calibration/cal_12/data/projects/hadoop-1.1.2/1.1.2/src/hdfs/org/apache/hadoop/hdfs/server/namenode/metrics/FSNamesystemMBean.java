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
package org.apache.hadoop.hdfs.server.namenode.metrics;

/**
 * 
 * This Interface defines the methods to get the status of a the FSNamesystem of
 * a name node.
 * It is also used for publishing via JMX (hence we follow the JMX naming
 * convention.)
 * 
 * Note we have not used the metrics system to implement this MBean,
 * because the interface for the FSNamesystemMBean is stable and should
 * be published as an interface.
 * 
 * <p>
 * Name Node runtime activity statistics is report in another MBean via the
 * metrics system.
 * @see org.apache.hadoop.hdfs.server.namenode.metrics.NameNodeInstrumentation
 *
 */
public interface FSNamesystemMBean {

  /**
   * The state of the file system: Safemode or Operational
   * @return the state
   */
  public String getFSState();
  
  
  /**
   * Number of allocated blocks in the system
   * @return -  number of allocated blocks
   */
  public long getBlocksTotal();

  /**
   * Total storage capacity
   * @return -  total capacity in bytes
   */
  public long getCapacityTotal();


  /**
   * Free (unused) storage capacity
   * @return -  free capacity in bytes
   */
  public long getCapacityRemaining();
 
  /**
   * Used storage capacity
   * @return -  used capacity in bytes
   */
  public long getCapacityUsed();
 

  /**
   * Total number of files and directories
   * @return -  num of files and directories
   */
  public long getFilesTotal();
 
  /**
   * Blocks pending to be replicated
   * @return -  num of blocks to be replicated
   */
  public long getPendingReplicationBlocks();
 
  /**
   * Blocks under replicated 
   * @return -  num of blocks under replicated
   */
  public long getUnderReplicatedBlocks();
 
  /**
   * Blocks scheduled for replication
   * @return -  num of blocks scheduled for replication
   */
  public long getScheduledReplicationBlocks();

  /**
   * Total Load on the FSNamesystem
   * @return -  total load of FSNamesystem
   */
  public int getTotalLoad();

  /**
   * Number of Live data nodes
   * @return number of live data nodes
   */
  public int numLiveDataNodes();
  
  /**
   * Number of dead data nodes
   * @return number of dead data nodes
   */
  public int numDeadDataNodes();
}
