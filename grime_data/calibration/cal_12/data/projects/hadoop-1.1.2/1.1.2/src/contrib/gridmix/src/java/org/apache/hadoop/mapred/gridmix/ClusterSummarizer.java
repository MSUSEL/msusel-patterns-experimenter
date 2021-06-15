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
package org.apache.hadoop.mapred.gridmix;

import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CommonConfigurationKeys;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.mapred.JobTracker;
import org.apache.hadoop.mapred.gridmix.Statistics.ClusterStats;

/**
 * Summarizes the Hadoop cluster used in this {@link Gridmix} run. 
 * Statistics that are reported are
 * <ul>
 *   <li>Total number of active trackers in the cluster</li>
 *   <li>Total number of blacklisted trackers in the cluster</li>
 *   <li>Max map task capacity of the cluster</li>
 *   <li>Max reduce task capacity of the cluster</li>
 * </ul>
 * 
 * Apart from these statistics, {@link JobTracker} and {@link FileSystem} 
 * addresses are also recorded in the summary.
 */
class ClusterSummarizer implements StatListener<ClusterStats> {
  static final Log LOG = LogFactory.getLog(ClusterSummarizer.class);
  
  private int numBlacklistedTrackers;
  private int numActiveTrackers;
  private int maxMapTasks;
  private int maxReduceTasks;
  private String jobTrackerInfo = Summarizer.NA;
  private String namenodeInfo = Summarizer.NA;
  
  @Override
  @SuppressWarnings("deprecation")
  public void update(ClusterStats item) {
    try {
      numBlacklistedTrackers = item.getStatus().getBlacklistedTrackers();
      numActiveTrackers = item.getStatus().getTaskTrackers();
      maxMapTasks = item.getStatus().getMaxMapTasks();
      maxReduceTasks = item.getStatus().getMaxReduceTasks();
    } catch (Exception e) {
      long time = System.currentTimeMillis();
      LOG.info("Error in processing cluster status at " 
               + FastDateFormat.getInstance().format(time));
    }
  }
  
  /**
   * Summarizes the cluster used for this {@link Gridmix} run.
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Cluster Summary:-");
    builder.append("\nJobTracker: ").append(getJobTrackerInfo());
    builder.append("\nFileSystem: ").append(getNamenodeInfo());
    builder.append("\nNumber of blacklisted trackers: ")
           .append(getNumBlacklistedTrackers());
    builder.append("\nNumber of active trackers: ")
           .append(getNumActiveTrackers());
    builder.append("\nMax map task capacity: ")
           .append(getMaxMapTasks());
    builder.append("\nMax reduce task capacity: ").append(getMaxReduceTasks());
    builder.append("\n\n");
    return builder.toString();
  }
  
  void start(Configuration conf) {
    jobTrackerInfo = conf.get("mapred.job.tracker");
    namenodeInfo = conf.get(CommonConfigurationKeys.FS_DEFAULT_NAME_KEY);
  }
  
  // Getters
  protected int getNumBlacklistedTrackers() {
    return numBlacklistedTrackers;
  }
  
  protected int getNumActiveTrackers() {
    return numActiveTrackers;
  }
  
  protected int getMaxMapTasks() {
    return maxMapTasks;
  }
  
  protected int getMaxReduceTasks() {
    return maxReduceTasks;
  }
  
  protected String getJobTrackerInfo() {
    return jobTrackerInfo;
  }
  
  protected String getNamenodeInfo() {
    return namenodeInfo;
  }
}