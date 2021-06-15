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

import org.apache.hadoop.mapreduce.TaskType;
import org.apache.hadoop.conf.Configuration;

/**
 * A {@link LoadManager} for use by the {@link FairScheduler} that allocates
 * tasks evenly across nodes up to their per-node maximum, using the default
 * load management algorithm in Hadoop.
 */
public class CapBasedLoadManager extends LoadManager {
  
  float maxDiff = 0.0f;
  
  public void setConf(Configuration conf) {
    super.setConf(conf);
    maxDiff = conf.getFloat("mapred.fairscheduler.load.max.diff", 0.0f);
  }
  
  /**
   * Determine how many tasks of a given type we want to run on a TaskTracker. 
   * This cap is chosen based on how many tasks of that type are outstanding in
   * total, so that when the cluster is used below capacity, tasks are spread
   * out uniformly across the nodes rather than being clumped up on whichever
   * machines sent out heartbeats earliest.
   */
  int getCap(int totalRunnableTasks, int localMaxTasks, int totalSlots) {
    double load = maxDiff + ((double)totalRunnableTasks) / totalSlots;
    return (int) Math.ceil(localMaxTasks * Math.min(1.0, load));
  }

  @Override
  public boolean canAssignMap(TaskTrackerStatus tracker,
      int totalRunnableMaps, int totalMapSlots, int alreadyAssigned) {
    int cap = getCap(totalRunnableMaps, tracker.getMaxMapSlots(), totalMapSlots);
    return tracker.countMapTasks() + alreadyAssigned < cap;
  }

  @Override
  public boolean canAssignReduce(TaskTrackerStatus tracker,
      int totalRunnableReduces, int totalReduceSlots, int alreadyAssigned) {
    int cap = getCap(totalRunnableReduces, tracker.getMaxReduceSlots(),
        totalReduceSlots); 
    return tracker.countReduceTasks() + alreadyAssigned < cap;
  }

  @Override
  public boolean canLaunchTask(TaskTrackerStatus tracker,
      JobInProgress job,  TaskType type) {
    return true;
  }
}
