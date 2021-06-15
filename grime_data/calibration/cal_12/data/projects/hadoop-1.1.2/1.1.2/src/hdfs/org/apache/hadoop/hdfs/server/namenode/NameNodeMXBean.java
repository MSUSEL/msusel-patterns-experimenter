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

/**
 * 
 * This is the JMX management interface for namenode information
 */
public interface NameNodeMXBean {

  /**
   * @return the host name
   */
  public String getHostName();
  
  /**
   * Gets the version of Hadoop.
   * 
   * @return the version
   */
  public String getVersion();
  
  /**
   * Gets the used space by data nodes.
   * 
   * @return the used space by data nodes
   */
  public long getUsed();
  
  /**
   * Gets total non-used raw bytes.
   * 
   * @return total non-used raw bytes
   */
  public long getFree();
  
  /**
   * Gets total raw bytes including non-dfs used space.
   * 
   * @return the total raw bytes including non-dfs used space
   */
  public long getTotal();
  
  /**
   * Gets the safemode status
   * 
   * @return the safemode status
   * 
   */
  public String getSafemode();
  
  /**
   * Checks if upgrade is finalized.
   * 
   * @return true, if upgrade is finalized
   */
  public boolean isUpgradeFinalized();
  
  /**
   * Gets total used space by data nodes for non DFS purposes such as storing
   * temporary files on the local file system
   * 
   * @return the non dfs space of the cluster
   */
  public long getNonDfsUsedSpace();
  
  /**
   * Gets the total used space by data nodes as percentage of total capacity
   * 
   * @return the percentage of used space on the cluster.
   */
  public float getPercentUsed();
  
  /**
   * Gets the total remaining space by data nodes as percentage of total 
   * capacity
   * 
   * @return the percentage of the remaining space on the cluster
   */
  public float getPercentRemaining();
  
  /**
   * Gets the total numbers of blocks on the cluster.
   * 
   * @return the total number of blocks of the cluster
   */
  public long getTotalBlocks();
  
  /**
   * Gets the total number of files on the cluster
   * 
   * @return the total number of files on the cluster
   */
  public long getTotalFiles();
  
  /**
   * Gets the number of threads.
   * 
   * @return the number of threads
   */
  public int getThreads();

  /**
   * Gets the live node information of the cluster.
   * 
   * @return the live node information
   */
  public String getLiveNodes();
  
  /**
   * Gets the dead node information of the cluster.
   * 
   * @return the dead node information
   */
  public String getDeadNodes();
  
  /**
   * Gets the decommissioning node information of the cluster.
   * 
   * @return the decommissioning node information
   */
  public String getDecomNodes();

  /**
   * Get status information about the directories storing image and edits logs
   * of the NN.
   * 
   * @return the name dir status information, as a JSON string.
   */
  public String getNameDirStatuses();
}
