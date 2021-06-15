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
package org.apache.hadoop.mapreduce.test.system;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.TaskStatus;
import org.apache.hadoop.mapred.TaskTracker;

/**
 * Task state information as seen by the TT.
 */
public interface TTTaskInfo extends Writable {

  /**
   * Has task occupied a slot? A task occupies a slot once it starts localizing
   * on the {@link TaskTracker} <br/>
   * 
   * @return true if task has started occupying a slot.
   */
  boolean slotTaken();

  /**
   * Has the task been killed? <br/>
   * 
   * @return true, if task has been killed.
   */
  boolean wasKilled();

  /**
   * Gets the task status associated with the particular task trackers task 
   * view.<br/>
   * 
   * @return status of the particular task
   */
  TaskStatus getTaskStatus();
  
  /**
   * Gets the configuration object of the task.
   * @return
   */
  Configuration getConf();
  
  /**
   * Gets the user of the task.
   * @return
   */
  String getUser();
  
  /**
   * Provides information as to whether the task is a cleanup of task.
   * @return true if it is a clean up of task.
   */
  boolean isTaskCleanupTask();

  /**
   * Gets the pid of the running task on the task-tracker.
   * 
   * @return pid of the task.
   */
  String getPid();
}
