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

import org.apache.hadoop.mapred.TaskStatus;
import org.apache.hadoop.mapreduce.TaskID;
import org.apache.hadoop.mapreduce.test.system.TaskInfo;

/**
 * Concrete class to expose out the task related information to the Clients from
 * the JobTracker.
 * Look at {@link TaskInfo} for further details.
 */
class TaskInfoImpl implements TaskInfo {

  private double progress;
  private TaskID taskID;
  private int killedAttempts;
  private int failedAttempts;
  private int runningAttempts;
  private TaskStatus[] taskStatus;
  private boolean setupOrCleanup;
  private String[] taskTrackers;

  public TaskInfoImpl() {
    taskID = new TaskID();
  }

  public TaskInfoImpl(
      TaskID taskID, double progress, int runningAttempts, int killedAttempts,
      int failedAttempts, TaskStatus[] taskStatus,
      boolean setupOrCleanup, String[] taskTrackers) {
    this.progress = progress;
    this.taskID = taskID;
    this.killedAttempts = killedAttempts;
    this.failedAttempts = failedAttempts;
    this.runningAttempts = runningAttempts;
    if (taskStatus != null) {
      this.taskStatus = taskStatus;
    } else {
      if (taskID.isMap()) {
        this.taskStatus = new MapTaskStatus[] {};
      } else {
        this.taskStatus = new ReduceTaskStatus[] {};
      }
    }
    this.setupOrCleanup = setupOrCleanup;
    this.taskTrackers = taskTrackers;
  }

  @Override
  public double getProgress() {
    return progress;
  }

  @Override
  public TaskID getTaskID() {
    return taskID;
  }

  @Override
  public int numKilledAttempts() {
    return killedAttempts;
  }

  @Override
  public int numFailedAttempts() {
    return failedAttempts;
  }

  @Override
  public int numRunningAttempts() {
    return runningAttempts;
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    taskID.readFields(in);
    progress = in.readDouble();
    runningAttempts = in.readInt();
    killedAttempts = in.readInt();
    failedAttempts = in.readInt();
    int size = in.readInt();
    if (taskID.isMap()) {
      taskStatus = new MapTaskStatus[size];
    }
    else {
      taskStatus = new ReduceTaskStatus[size];
    }
    for (int i = 0; i < size; i++) {
      if (taskID.isMap()) {
        taskStatus[i] = new MapTaskStatus();
      }
      else {
        taskStatus[i] = new ReduceTaskStatus();
      }
      taskStatus[i].readFields(in);
      taskStatus[i].setTaskTracker(in.readUTF());
    }
    setupOrCleanup = in.readBoolean();
    size = in.readInt();
    taskTrackers = new String[size];
    for(int i=0; i < size ; i++) {
      taskTrackers[i] = in.readUTF();
    }
  }

  @Override
  public void write(DataOutput out) throws IOException {
    taskID.write(out);
    out.writeDouble(progress);
    out.writeInt(runningAttempts);
    out.writeInt(killedAttempts);
    out.writeInt(failedAttempts);
    out.writeInt(taskStatus.length);
    for (TaskStatus t : taskStatus) {
      t.write(out);
      out.writeUTF(t.getTaskTracker());
    }
    out.writeBoolean(setupOrCleanup);
    out.writeInt(taskTrackers.length);
    for(String tt : taskTrackers) {
      out.writeUTF(tt);
    }
  }

  @Override
  public TaskStatus[] getTaskStatus() {
    return taskStatus;
  }

  @Override
  public boolean isSetupOrCleanup() {
    return setupOrCleanup;
  }

  @Override
  public String[] getTaskTrackers() {
    return taskTrackers;
  }
}
