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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;

/** A report on the state of a task. */
public class TaskReport implements Writable {
  private TaskID taskid;
  private float progress;
  private String state;
  private String[] diagnostics;
  private long startTime; 
  private long finishTime; 
  private Counters counters;
  private TIPStatus currentStatus;
  
  private Collection<TaskAttemptID> runningAttempts = 
    new ArrayList<TaskAttemptID>();
  private TaskAttemptID successfulAttempt = new TaskAttemptID();
  public TaskReport() {
    taskid = new TaskID();
  }
  
  /**
   * Creates a new TaskReport object
   * @param taskid
   * @param progress
   * @param state
   * @param diagnostics
   * @param startTime
   * @param finishTime
   * @param counters
   * @deprecated
   */
  @Deprecated
  TaskReport(TaskID taskid, float progress, String state,
      String[] diagnostics, long startTime, long finishTime,
      Counters counters) {
    this(taskid, progress, state, diagnostics, null, startTime, finishTime, 
        counters);
  }
  
  /**
   * Creates a new TaskReport object
   * @param taskid
   * @param progress
   * @param state
   * @param diagnostics
   * @param currentStatus
   * @param startTime
   * @param finishTime
   * @param counters
   */
  TaskReport(TaskID taskid, float progress, String state,
             String[] diagnostics, TIPStatus currentStatus, 
             long startTime, long finishTime,
             Counters counters) {
    this.taskid = taskid;
    this.progress = progress;
    this.state = state;
    this.diagnostics = diagnostics;
    this.currentStatus = currentStatus;
    this.startTime = startTime; 
    this.finishTime = finishTime;
    this.counters = counters;
  }
    
  /** @deprecated use {@link #getTaskID()} instead */
  @Deprecated
  public String getTaskId() { return taskid.toString(); }
  /** The id of the task. */
  public TaskID getTaskID() { return taskid; }
  /** The amount completed, between zero and one. */
  public float getProgress() { return progress; }
  /** The most recent state, reported by a {@link Reporter}. */
  public String getState() { return state; }
  /** A list of error messages. */
  public String[] getDiagnostics() { return diagnostics; }
  /** A table of counters. */
  public Counters getCounters() { return counters; }
  /** The current status */
  public TIPStatus getCurrentStatus() {
    return currentStatus;
  }
  
  /**
   * Get finish time of task. 
   * @return 0, if finish time was not set else returns finish time.
   */
  public long getFinishTime() {
    return finishTime;
  }

  /** 
   * set finish time of task. 
   * @param finishTime finish time of task. 
   */
  void setFinishTime(long finishTime) {
    this.finishTime = finishTime;
  }

  /**
   * Get start time of task. 
   * @return 0 if start time was not set, else start time. 
   */
  public long getStartTime() {
    return startTime;
  }

  /** 
   * set start time of the task. 
   */ 
  void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  /** 
   * set successful attempt ID of the task. 
   */ 
  public void setSuccessfulAttempt(TaskAttemptID t) {
    successfulAttempt = t;
  }
  /**
   * Get the attempt ID that took this task to completion
   */
  public TaskAttemptID getSuccessfulTaskAttempt() {
    return successfulAttempt;
  }
  /** 
   * set running attempt(s) of the task. 
   */ 
  public void setRunningTaskAttempts(
      Collection<TaskAttemptID> runningAttempts) {
    this.runningAttempts = runningAttempts;
  }
  /**
   * Get the running task attempt IDs for this task
   */
  public Collection<TaskAttemptID> getRunningTaskAttempts() {
    return runningAttempts;
  }


  @Override
  public boolean equals(Object o) {
    if(o == null)
      return false;
    if(o.getClass().equals(TaskReport.class)) {
      TaskReport report = (TaskReport) o;
      return counters.equals(report.getCounters())
             && Arrays.toString(this.diagnostics)
                      .equals(Arrays.toString(report.getDiagnostics()))
             && this.finishTime == report.getFinishTime()
             && this.progress == report.getProgress()
             && this.startTime == report.getStartTime()
             && this.state.equals(report.getState())
             && this.taskid.equals(report.getTaskID());
    }
    return false; 
  }

  @Override
  public int hashCode() {
    return (counters.toString() + Arrays.toString(this.diagnostics) 
            + this.finishTime + this.progress + this.startTime + this.state 
            + this.taskid.toString()).hashCode();
  }
  //////////////////////////////////////////////
  // Writable
  //////////////////////////////////////////////
  public void write(DataOutput out) throws IOException {
    taskid.write(out);
    out.writeFloat(progress);
    Text.writeString(out, state);
    out.writeLong(startTime);
    out.writeLong(finishTime);
    WritableUtils.writeStringArray(out, diagnostics);
    counters.write(out);
    WritableUtils.writeEnum(out, currentStatus);
    if (currentStatus == TIPStatus.RUNNING) {
      WritableUtils.writeVInt(out, runningAttempts.size());
      TaskAttemptID t[] = new TaskAttemptID[0];
      t = runningAttempts.toArray(t);
      for (int i = 0; i < t.length; i++) {
        t[i].write(out);
      }
    } else if (currentStatus == TIPStatus.COMPLETE) {
      successfulAttempt.write(out);
    }
  }

  public void readFields(DataInput in) throws IOException {
    this.taskid.readFields(in);
    this.progress = in.readFloat();
    this.state = Text.readString(in);
    this.startTime = in.readLong(); 
    this.finishTime = in.readLong();
    
    diagnostics = WritableUtils.readStringArray(in);
    counters = new Counters();
    counters.readFields(in);
    currentStatus = WritableUtils.readEnum(in, TIPStatus.class);
    if (currentStatus == TIPStatus.RUNNING) {
      int num = WritableUtils.readVInt(in);    
      for (int i = 0; i < num; i++) {
        TaskAttemptID t = new TaskAttemptID();
        t.readFields(in);
        runningAttempts.add(t);
      }
    } else if (currentStatus == TIPStatus.COMPLETE) {
      successfulAttempt.readFields(in);
    }
  }
}
