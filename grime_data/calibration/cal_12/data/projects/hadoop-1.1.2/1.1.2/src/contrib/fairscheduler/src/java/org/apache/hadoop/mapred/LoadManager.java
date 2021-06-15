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

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.TaskType;

/**
 * A pluggable object that manages the load on each {@link TaskTracker}, telling
 * the {@link TaskScheduler} when it can launch new tasks. 
 */
public abstract class LoadManager implements Configurable {
  protected Configuration conf;
  protected TaskTrackerManager taskTrackerManager;
  protected FairSchedulerEventLog schedulingLog;
  
  public Configuration getConf() {
    return conf;
  }

  public void setConf(Configuration conf) {
    this.conf = conf;
  }

  public synchronized void setTaskTrackerManager(
      TaskTrackerManager taskTrackerManager) {
    this.taskTrackerManager = taskTrackerManager;
  }

  public void setEventLog(FairSchedulerEventLog schedulingLog) {
    this.schedulingLog = schedulingLog;
  }
  
  /**
   * Lifecycle method to allow the LoadManager to start any work in separate
   * threads.
   */
  public void start() throws IOException {
    // do nothing
  }
  
  /**
   * Lifecycle method to allow the LoadManager to stop any work it is doing.
   */
  public void terminate() throws IOException {
    // do nothing
  }
  
  /**
   * Can a given {@link TaskTracker} run another map task?
   * This method may check whether the specified tracker has
   * enough resources to run another map task.
   * @param tracker The machine we wish to run a new map on
   * @param totalRunnableMaps Set of running jobs in the cluster
   * @param totalMapSlots The total number of map slots in the cluster
   * @param alreadyAssigned the number of maps already assigned to
   *        this tracker during this heartbeat
   * @return true if another map can be launched on <code>tracker</code>
   */
  public abstract boolean canAssignMap(TaskTrackerStatus tracker,
      int totalRunnableMaps, int totalMapSlots, int alreadyAssigned);

  /**
   * Can a given {@link TaskTracker} run another reduce task?
   * This method may check whether the specified tracker has
   * enough resources to run another reduce task.
   * @param tracker The machine we wish to run a new map on
   * @param totalRunnableReduces Set of running jobs in the cluster
   * @param totalReduceSlots The total number of reduce slots in the cluster
   * @param alreadyAssigned the number of reduces already assigned to
   *        this tracker during this heartbeat
   * @return true if another reduce can be launched on <code>tracker</code>
   */
  public abstract boolean canAssignReduce(TaskTrackerStatus tracker,
      int totalRunnableReduces, int totalReduceSlots, int alreadyAssigned);

  /**
   * Can a given {@link TaskTracker} run another new task from a given job? 
   * This method is provided for use by LoadManagers that take into 
   * account jobs' individual resource needs when placing tasks.
   * @param tracker The machine we wish to run a new map on
   * @param job The job from which we want to run a task on this machine
   * @param type The type of task that we want to run on
   * @return true if this task can be launched on <code>tracker</code>
   */
  public abstract boolean canLaunchTask(TaskTrackerStatus tracker,
      JobInProgress job,  TaskType type);
}
