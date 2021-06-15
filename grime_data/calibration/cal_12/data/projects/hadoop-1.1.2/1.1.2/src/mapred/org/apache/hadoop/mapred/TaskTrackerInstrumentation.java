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

import java.io.File;
import org.apache.hadoop.metrics2.MetricsSystem;
import org.apache.hadoop.metrics2.lib.DefaultMetricsSystem;

/**
 * TaskTrackerInstrumentation defines a number of instrumentation points
 * associated with TaskTrackers.  By default, the instrumentation points do
 * nothing, but subclasses can do arbitrary instrumentation and monitoring at
 * these points.
 * 
 * TaskTrackerInstrumentation interfaces are associated uniquely with a
 * TaskTracker.  We don't want an inner class here, because then subclasses
 * wouldn't have direct access to the associated TaskTracker.
 *  
 **/
class TaskTrackerInstrumentation  {

  protected final TaskTracker tt;
  
  public TaskTrackerInstrumentation(TaskTracker t) {
    tt = t;
  }
  
  /**
   * invoked when task attempt t succeeds
   * @param t
   */
  public void completeTask(TaskAttemptID t) { }
  
  public void timedoutTask(TaskAttemptID t) { }
  
  public void taskFailedPing(TaskAttemptID t) { }

  /**
   * Called just before task attempt t starts.
   * @param stdout the file containing standard out of the new task
   * @param stderr the file containing standard error of the new task 
   */
  public void reportTaskLaunch(TaskAttemptID t, File stdout, File stderr)  { }
  
  /**
   * called when task t has just finished.
   * @param t
   */
  public void reportTaskEnd(TaskAttemptID t) {}

  static TaskTrackerInstrumentation create(TaskTracker tt) {
    return create(tt, DefaultMetricsSystem.INSTANCE);
  }

  static TaskTrackerInstrumentation create(TaskTracker tt, MetricsSystem ms) {
    return ms.register("TaskTrackerMetrics", "TaskTracker metrics",
                       new TaskTrackerMetricsSource(tt));
  }

}
