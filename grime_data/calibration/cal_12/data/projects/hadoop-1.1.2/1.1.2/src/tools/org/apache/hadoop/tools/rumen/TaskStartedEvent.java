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
package org.apache.hadoop.tools.rumen;

import org.apache.hadoop.mapreduce.TaskID;
import org.apache.hadoop.mapreduce.TaskType;

/**
 * Event to record the start of a task
 *
 */
public class TaskStartedEvent implements HistoryEvent {
  private TaskID taskId;
  private String splitLocations;
  private long startTime;
  private TaskType taskType;

  /**
   * Create an event to record start of a task
   * @param id Task Id
   * @param startTime Start time of the task
   * @param taskType Type of the task
   * @param splitLocations Split locations, applicable for map tasks
   */
  public TaskStartedEvent(TaskID id, long startTime, 
      TaskType taskType, String splitLocations) {
    this.taskId = id;
    this.splitLocations = splitLocations;
    this.startTime = startTime;
    this.taskType = taskType;
  }

  /** Get the task id */
  public TaskID getTaskId() { return taskId; }
  /** Get the split locations, applicable for map tasks */
  public String getSplitLocations() { return splitLocations; }
  /** Get the start time of the task */
  public long getStartTime() { return startTime; }
  /** Get the task type */
  public TaskType getTaskType() {
    return taskType;
  }
  /** Get the event type */
  public EventType getEventType() {
    return EventType.TASK_STARTED;
  }

}
