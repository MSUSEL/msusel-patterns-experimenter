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
package org.apache.hadoop.mapred;

import java.io.IOException;
import org.apache.hadoop.mapreduce.test.system.JobInfo;

/**
 * Aspect to add a utility method in the JobInProgress for easing up the
 * construction of the JobInfo object.
 */
privileged aspect JobInProgressAspect {

  /**
   * Returns a read only view of the JobInProgress object which is used by the
   * client.
   * 
   * @return JobInfo of the current JobInProgress object
   */
  public JobInfo JobInProgress.getJobInfo() {
    String historyLoc = getHistoryPath();
    JobInfoImpl jobInfoImpl;
    if (tasksInited) {
      jobInfoImpl = new JobInfoImpl(
          this.getJobID(), this.isSetupLaunched(), this.isSetupFinished(), this
              .isCleanupLaunched(), this.runningMaps(), this.runningReduces(),
          this.pendingMaps(), this.pendingReduces(), this.finishedMaps(), this
              .finishedReduces(), this.getStatus(), historyLoc, this
              .getBlackListedTrackers(), false, this.numMapTasks,
          this.numReduceTasks);
    } else {
      jobInfoImpl = new JobInfoImpl(
          this.getJobID(), false, false, false, 0, 0, this.pendingMaps(), this
              .pendingReduces(), this.finishedMaps(), this.finishedReduces(),
          this.getStatus(), historyLoc, this.getBlackListedTrackers(), this
              .isComplete(), this.numMapTasks, this.numReduceTasks);
    }
    jobInfoImpl.setFinishTime(getJobFinishTime());
    jobInfoImpl.setLaunchTime(getJobLaunchTime());
    jobInfoImpl.setNumSlotsPerReduce(getJobNumSlotsPerReduce());
    jobInfoImpl.setNumSlotsPerMap(getJobNumSlotsPerMap());
    return jobInfoImpl;
  }
  
  private long JobInProgress.getJobFinishTime() {
    long finishTime = 0;
    if (this.isComplete()) {
      finishTime = this.getFinishTime();
    }
    return finishTime;
  }

  private long JobInProgress.getJobLaunchTime() {
    long LaunchTime = 0;
    if (this.isComplete()) {
      LaunchTime = this.getLaunchTime();
    }
    return LaunchTime;
  }

  private int JobInProgress.getJobNumSlotsPerReduce() {
    int numSlotsPerReduce = 0;
    if (this.isComplete()) {
      numSlotsPerReduce = this.getNumSlotsPerReduce();
    }
    return numSlotsPerReduce;
  }

  private int JobInProgress.getJobNumSlotsPerMap() {
    int numSlotsPerMap = 0;
    if (this.isComplete()) {
      numSlotsPerMap = this.getNumSlotsPerMap();
    }
    return numSlotsPerMap;
 }


  private String JobInProgress.getHistoryPath() {
    String historyLoc = "";
    if(this.isComplete()) {
      historyLoc = this.getHistoryFile();
    } else {
      String historyFileName = null;
      try {
        historyFileName  = JobHistory.JobInfo.getJobHistoryFileName(conf, 
            jobId);
      } catch(IOException e) {
      }
      if(historyFileName != null) {
        historyLoc = JobHistory.JobInfo.getJobHistoryLogLocation(
            historyFileName).toString();
      }
    }
    return historyLoc;
  }

}
