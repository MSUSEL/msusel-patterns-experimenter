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

/**
 * A {@link TaskSelector} implementation that wraps around the default
 * {@link JobInProgress#obtainNewMapTask(TaskTrackerStatus, int)} and
 * {@link JobInProgress#obtainNewReduceTask(TaskTrackerStatus, int)} methods
 * in {@link JobInProgress}, using the default Hadoop locality and speculative
 * threshold algorithms.
 */
public class DefaultTaskSelector extends TaskSelector {

  @Override
  public int neededSpeculativeMaps(JobInProgress job) {
    int count = 0;
    long time = System.currentTimeMillis();
    double avgProgress = job.getStatus().mapProgress();
    for (TaskInProgress tip: job.maps) {
      if (tip.isRunning() && tip.hasSpeculativeTask(time, avgProgress)) {
        count++;
      }
    }
    return count;
  }

  @Override
  public int neededSpeculativeReduces(JobInProgress job) {
    int count = 0;
    long time = System.currentTimeMillis();
    double avgProgress = job.getStatus().reduceProgress();
    for (TaskInProgress tip: job.reduces) {
      if (tip.isRunning() && tip.hasSpeculativeTask(time, avgProgress)) {
        count++;
      }
    }
    return count;
  }

  @Override
  public Task obtainNewMapTask(TaskTrackerStatus taskTracker, JobInProgress job,
      int localityLevel) throws IOException {
    ClusterStatus clusterStatus = taskTrackerManager.getClusterStatus();
    int numTaskTrackers = clusterStatus.getTaskTrackers();
    switch (localityLevel) {
      case 1:
        return job.obtainNewNodeLocalMapTask(taskTracker, numTaskTrackers,
          taskTrackerManager.getNumberOfUniqueHosts());
      case 2:
        return job.obtainNewNodeOrRackLocalMapTask(taskTracker, numTaskTrackers,
          taskTrackerManager.getNumberOfUniqueHosts());
      default:
        return job.obtainNewMapTask(taskTracker, numTaskTrackers,
          taskTrackerManager.getNumberOfUniqueHosts());
    }
  }

  @Override
  public Task obtainNewReduceTask(TaskTrackerStatus taskTracker, JobInProgress job)
      throws IOException {
    ClusterStatus clusterStatus = taskTrackerManager.getClusterStatus();
    int numTaskTrackers = clusterStatus.getTaskTrackers();
    return job.obtainNewReduceTask(taskTracker, numTaskTrackers,
        taskTrackerManager.getNumberOfUniqueHosts());
  }

}
