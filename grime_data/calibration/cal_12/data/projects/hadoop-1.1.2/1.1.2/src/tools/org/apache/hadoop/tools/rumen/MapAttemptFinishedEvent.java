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

import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.TaskAttemptID;
import org.apache.hadoop.mapreduce.TaskID;
import org.apache.hadoop.mapreduce.TaskType;

/**
 * Event to record successful completion of a map attempt
 *
 */
public class MapAttemptFinishedEvent  implements HistoryEvent {
  private TaskID taskId;
  private TaskAttemptID attemptId;
  private TaskType taskType;
  private String taskStatus;
  private long mapFinishTime;
  private long finishTime;
  private String hostname;
  private String state;
  private JhCounters counters;
  
  /** 
   * Create an event for successful completion of map attempts
   * @param id Task Attempt ID
   * @param taskType Type of the task
   * @param taskStatus Status of the task
   * @param mapFinishTime Finish time of the map phase
   * @param finishTime Finish time of the attempt
   * @param hostname Name of the host where the map executed
   * @param state State string for the attempt
   * @param counters Counters for the attempt
   */
  public MapAttemptFinishedEvent(TaskAttemptID id, 
      TaskType taskType, String taskStatus, 
      long mapFinishTime, long finishTime,
      String hostname, String state, Counters counters) {
    this.taskId = id.getTaskID();
    this.attemptId = id;
    this.taskType = taskType;
    this.taskStatus = taskStatus;
    this.mapFinishTime = mapFinishTime;
    this.finishTime = finishTime;
    this.hostname = hostname;
    this.state = state;
    this.counters = new JhCounters(counters, "COUNTERS");
  }

  /** Get the task ID */
  public TaskID getTaskId() { return taskId; }
  /** Get the attempt id */
  public TaskAttemptID getAttemptId() {
    return attemptId;
  }
  /** Get the task type */
  public TaskType getTaskType() {
    return taskType;
  }
  /** Get the task status */
  public String getTaskStatus() { return taskStatus; }
  /** Get the map phase finish time */
  public long getMapFinishTime() { return mapFinishTime; }
  /** Get the attempt finish time */
  public long getFinishTime() { return finishTime; }
  /** Get the host name */
  public String getHostname() { return hostname; }
  /** Get the state string */
  public String getState() { return state; }
  /** Get the counters */
  public JhCounters getCounters() { return counters; }
  /** Get the event type */
   public EventType getEventType() {
    return EventType.MAP_ATTEMPT_FINISHED;
  }
  
}
