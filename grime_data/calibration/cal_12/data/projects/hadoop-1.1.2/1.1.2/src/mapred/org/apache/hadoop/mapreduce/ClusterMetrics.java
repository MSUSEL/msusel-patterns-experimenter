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
package org.apache.hadoop.mapreduce;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

/**
 * Status information on the current state of the Map-Reduce cluster.
 * 
 * <p><code>ClusterMetrics</code> provides clients with information such as:
 * <ol>
 *   <li>
 *   Size of the cluster.  
 *   </li>
 *   <li>
 *   Number of blacklisted and decommissioned trackers.  
 *   </li>
 *   <li>
 *   Slot capacity of the cluster. 
 *   </li>
 *   <li>
 *   The number of currently occupied/reserved map & reduce slots.
 *   </li>
 *   <li>
 *   The number of currently running map & reduce tasks.
 *   </li>
 *   <li>
 *   The number of job submissions.
 *   </li>
 * </ol></p>
 * 
 */
public class ClusterMetrics implements Writable {
  private int runningMaps;
  private int runningReduces;
  private int occupiedMapSlots;
  private int occupiedReduceSlots;
  private int reservedMapSlots;
  private int reservedReduceSlots;
  private int totalMapSlots;
  private int totalReduceSlots;
  private int totalJobSubmissions;
  private int numTrackers;
  private int numBlacklistedTrackers;
  private int numGraylistedTrackers;
  private int numDecommissionedTrackers;

  public ClusterMetrics() {
  }
  
  public ClusterMetrics(int runningMaps, int runningReduces,
      int occupiedMapSlots, int occupiedReduceSlots,
      int reservedMapSlots, int reservedReduceSlots,
      int mapSlots, int reduceSlots, 
      int totalJobSubmissions,
      int numTrackers, int numBlacklistedTrackers, int numGraylistedTrackers,
      int numDecommissionedNodes) {
    this.runningMaps = runningMaps;
    this.runningReduces = runningReduces;
    this.occupiedMapSlots = occupiedMapSlots;
    this.occupiedReduceSlots = occupiedReduceSlots;
    this.reservedMapSlots = reservedMapSlots;
    this.reservedReduceSlots = reservedReduceSlots;
    this.totalMapSlots = mapSlots;
    this.totalReduceSlots = reduceSlots;
    this.totalJobSubmissions = totalJobSubmissions;
    this.numTrackers = numTrackers;
    this.numBlacklistedTrackers = numBlacklistedTrackers;
    this.numGraylistedTrackers = numGraylistedTrackers;
    this.numDecommissionedTrackers = numDecommissionedNodes;
  }

  /**
   * Get the number of running map tasks in the cluster.
   * 
   * @return running maps
   */
  public int getRunningMaps() {
    return runningMaps;
  }
  
  /**
   * Get the number of running reduce tasks in the cluster.
   * 
   * @return running reduces
   */
  public int getRunningReduces() {
    return runningReduces;
  }
  
  /**
   * Get number of occupied map slots in the cluster.
   * 
   * @return occupied map slot count
   */
  public int getOccupiedMapSlots() { 
    return occupiedMapSlots;
  }
  
  /**
   * Get the number of occupied reduce slots in the cluster.
   * 
   * @return occupied reduce slot count
   */
  public int getOccupiedReduceSlots() { 
    return occupiedReduceSlots; 
  }

  /**
   * Get number of reserved map slots in the cluster.
   * 
   * @return reserved map slot count
   */
  public int getReservedMapSlots() { 
    return reservedMapSlots;
  }
  
  /**
   * Get the number of reserved reduce slots in the cluster.
   * 
   * @return reserved reduce slot count
   */
  public int getReservedReduceSlots() { 
    return reservedReduceSlots; 
  }

  /**
   * Get the total number of map slots in the cluster.
   * 
   * @return map slot capacity
   */
  public int getMapSlotCapacity() {
    return totalMapSlots;
  }
  
  /**
   * Get the total number of reduce slots in the cluster.
   * 
   * @return reduce slot capacity
   */
  public int getReduceSlotCapacity() {
    return totalReduceSlots;
  }
  
  /**
   * Get the total number of job submissions in the cluster.
   * 
   * @return total number of job submissions
   */
  public int getTotalJobSubmissions() {
    return totalJobSubmissions;
  }
  
  /**
   * Get the number of active trackers in the cluster.
   * 
   * @return active tracker count.
   */
  public int getTaskTrackerCount() {
    return numTrackers;
  }
  
  /**
   * Get the number of blacklisted trackers in the cluster.
   * 
   * @return blacklisted tracker count
   */
  public int getBlackListedTaskTrackerCount() {
    return numBlacklistedTrackers;
  }
  
  /**
   * Get the number of graylisted trackers in the cluster.
   * 
   * @return graylisted tracker count
   */
  public int getGrayListedTaskTrackerCount() {
    return numGraylistedTrackers;
  }
  
  /**
   * Get the number of decommissioned trackers in the cluster.
   * 
   * @return decommissioned tracker count
   */
  public int getDecommissionedTaskTrackerCount() {
    return numDecommissionedTrackers;
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    runningMaps = in.readInt();
    runningReduces = in.readInt();
    occupiedMapSlots = in.readInt();
    occupiedReduceSlots = in.readInt();
    reservedMapSlots = in.readInt();
    reservedReduceSlots = in.readInt();
    totalMapSlots = in.readInt();
    totalReduceSlots = in.readInt();
    totalJobSubmissions = in.readInt();
    numTrackers = in.readInt();
    numBlacklistedTrackers = in.readInt();
    numDecommissionedTrackers = in.readInt();
  }

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeInt(runningMaps);
    out.writeInt(runningReduces);
    out.writeInt(occupiedMapSlots);
    out.writeInt(occupiedReduceSlots);
    out.writeInt(reservedMapSlots);
    out.writeInt(reservedReduceSlots);
    out.writeInt(totalMapSlots);
    out.writeInt(totalReduceSlots);
    out.writeInt(totalJobSubmissions);
    out.writeInt(numTrackers);
    out.writeInt(numBlacklistedTrackers);
    out.writeInt(numDecommissionedTrackers);
  }

}
