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

import java.io.IOException;

import org.apache.hadoop.mapreduce.TaskAttemptID;
import org.apache.hadoop.mapreduce.TaskID;
import org.apache.hadoop.mapreduce.TaskType;

/**
 * Event to record start of a task attempt
 *
 */
public class TaskAttemptStartedEvent implements HistoryEvent {
  private TaskID taskId;
  private TaskAttemptID attemptId;
  private long startTime;
  private TaskType taskType;
  private String trackerName;
  private int httpPort;

  /**
   * Create an event to record the start of an attempt
   * @param attemptId Id of the attempt
   * @param taskType Type of task
   * @param startTime Start time of the attempt
   * @param trackerName Name of the Task Tracker where attempt is running
   * @param httpPort The port number of the tracker
   */
  public TaskAttemptStartedEvent( TaskAttemptID attemptId,  
      TaskType taskType, long startTime, String trackerName,
      int httpPort) {
    this.taskId = attemptId.getTaskID();
    this.attemptId = attemptId;
    this.startTime = startTime;
    this.taskType = taskType;
    this.trackerName = trackerName;
    this.httpPort = httpPort;
  }

  /** Get the task id */
  public TaskID getTaskId() { return taskId; }
  /** Get the tracker name */
  public String getTrackerName() { return trackerName; }
  /** Get the start time */
  public long getStartTime() { return startTime; }
  /** Get the task type */
  public TaskType getTaskType() {
    return taskType;
  }
  /** Get the HTTP port */
  public int getHttpPort() { return httpPort; }
  /** Get the attempt id */
  public TaskAttemptID getTaskAttemptId() {
    return attemptId;
  }
  /** Get the event type */
  public EventType getEventType() {
    return EventType.MAP_ATTEMPT_STARTED;
  }

}
