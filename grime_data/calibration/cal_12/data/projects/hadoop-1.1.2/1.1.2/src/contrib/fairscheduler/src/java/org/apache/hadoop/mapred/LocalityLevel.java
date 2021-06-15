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

/**
 * Represents the level of data-locality at which a job in the fair scheduler
 * is allowed to launch tasks. By default, jobs are not allowed to launch
 * non-data-local tasks until they have waited a small number of seconds to
 * find a slot on a node that they have data on. If a job has waited this
 * long, it is allowed to launch rack-local tasks as well (on nodes that may
 * not have the task's input data, but share a rack with a node that does).
 * Finally, after a further wait, jobs are allowed to launch tasks anywhere
 * in the cluster.
 * 
 * This enum defines three levels - NODE, RACK and ANY (for allowing tasks
 * to be launched on any node). A map task's level can be obtained from
 * its job through {@link #fromTask(JobInProgress, Task, TaskTrackerStatus)}. In
 * addition, for any locality level, it is possible to get a "level cap" to pass
 * to {@link JobInProgress#obtainNewMapTask(TaskTrackerStatus, int, int, int)}
 * to ensure that only tasks at this level or lower are launched, through
 * the {@link #toCacheLevelCap()} method.
 */
public enum LocalityLevel {
  NODE, RACK, ANY;
  
  public static LocalityLevel fromTask(JobInProgress job, Task mapTask,
      TaskTrackerStatus tracker) {
    TaskID tipID = mapTask.getTaskID().getTaskID();
    TaskInProgress tip = job.getTaskInProgress(tipID);
    switch (job.getLocalityLevel(tip, tracker)) {
    case 0: return LocalityLevel.NODE;
    case 1: return LocalityLevel.RACK;
    default: return LocalityLevel.ANY;
    }
  }
  
  /**
   * Obtain a JobInProgress cache level cap to pass to
   * {@link JobInProgress#obtainNewMapTask(TaskTrackerStatus, int, int, int)}
   * to ensure that only tasks of this locality level and lower are launched.
   */
  public int toCacheLevelCap() {
    switch(this) {
    case NODE: return 1;
    case RACK: return 2;
    default: return Integer.MAX_VALUE;
    }
  }
}
