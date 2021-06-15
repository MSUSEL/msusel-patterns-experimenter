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

import org.apache.hadoop.mapreduce.TaskAttemptID;
import org.apache.hadoop.mapreduce.TaskID;
import org.apache.hadoop.mapreduce.TaskType;

/**
 * Event to record unsuccessful (Killed/Failed) completion of task attempts
 *
 */
public class TaskAttemptUnsuccessfulCompletionEvent implements HistoryEvent {
  private TaskID taskId;
  private TaskType taskType;
  private TaskAttemptID attemptId;
  private long finishTime;
  private String hostname;
  private String error;
  private String status;

  /** 
   * Create an event to record the unsuccessful completion of attempts
   * @param id Attempt ID
   * @param taskType Type of the task
   * @param status Status of the attempt
   * @param finishTime Finish time of the attempt
   * @param hostname Name of the host where the attempt executed
   * @param error Error string
   */
  public TaskAttemptUnsuccessfulCompletionEvent(TaskAttemptID id, 
      TaskType taskType,
      String status, long finishTime, 
      String hostname, String error) {
    this.taskId = id.getTaskID();
    this.taskType = taskType;
    this.attemptId = id;
    this.finishTime = finishTime;
    this.hostname = hostname;
    this.error = error;
    this.status = status;
  }

  /** Get the task id */
  public TaskID getTaskId() { return taskId; }
  /** Get the task type */
  public TaskType getTaskType() {
    return taskType;
  }
  /** Get the attempt id */
  public TaskAttemptID getTaskAttemptId() {
    return attemptId;
  }
  /** Get the finish time */
  public long getFinishTime() { return finishTime; }
  /** Get the name of the host where the attempt executed */
  public String getHostname() { return hostname; }
  /** Get the error string */
  public String getError() { return error; }
  /** Get the task status */
  public String getTaskStatus() { return status; }
  /** Get the event type */
  public EventType getEventType() {
    return EventType.MAP_ATTEMPT_KILLED;
  }

}
