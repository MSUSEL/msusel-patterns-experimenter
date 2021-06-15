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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.test.system.TTTaskInfo;
/**
 * Abstract class which passes the Task view of the TaskTracker to the client.
 * See {@link TTInfoImpl} for further details.
 *
 */
abstract class TTTaskInfoImpl implements TTTaskInfo {

  private boolean slotTaken;
  private boolean wasKilled;
  TaskStatus status;
  Configuration conf;
  String user;
  boolean isTaskCleanupTask;
  private String pid;

  public TTTaskInfoImpl() {
  }

  public TTTaskInfoImpl(boolean slotTaken, boolean wasKilled,
      TaskStatus status, Configuration conf, String user,
      boolean isTaskCleanupTask, String pid) {
    super();
    this.slotTaken = slotTaken;
    this.wasKilled = wasKilled;
    this.status = status;
    this.conf = conf;
    this.user = user;
    this.isTaskCleanupTask = isTaskCleanupTask;
    this.pid = pid;
  }

  @Override
  public boolean slotTaken() {
    return slotTaken;
  }

  @Override
  public boolean wasKilled() {
    return wasKilled;
  }

  @Override
  public abstract TaskStatus getTaskStatus();

  @Override
  public Configuration getConf() {
    return conf;
  }
  
  @Override
  public String getUser() {
    return user;
  }
  
  @Override
  public boolean isTaskCleanupTask() {
    return isTaskCleanupTask;
  }
  
  @Override
  public String getPid() {
    return pid;
  }
  
  @Override
  public void readFields(DataInput in) throws IOException {
    slotTaken = in.readBoolean();
    wasKilled = in.readBoolean();
    conf = new Configuration();
    conf.readFields(in);
    user = in.readUTF();
    isTaskCleanupTask = in.readBoolean();
    pid = in.readUTF();
  }

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeBoolean(slotTaken);
    out.writeBoolean(wasKilled);
    conf.write(out);
    out.writeUTF(user);
    out.writeBoolean(isTaskCleanupTask);
    if (pid != null) {
      out.writeUTF(pid);
    } else {
      out.writeUTF("");
    }
    status.write(out);
  }

  static class MapTTTaskInfo extends TTTaskInfoImpl {

    public MapTTTaskInfo() {
      super();
    }

    public MapTTTaskInfo(boolean slotTaken, boolean wasKilled,
        MapTaskStatus status, Configuration conf, String user,
        boolean isTaskCleanup,String pid) {
      super(slotTaken, wasKilled, status, conf, user, isTaskCleanup, pid);
    }

    @Override
    public TaskStatus getTaskStatus() {
      return status;
    }
    
    public void readFields(DataInput in) throws IOException {
      super.readFields(in);
      status = new MapTaskStatus();
      status.readFields(in);
    }
  }

  static class ReduceTTTaskInfo extends TTTaskInfoImpl {

    public ReduceTTTaskInfo() {
      super();
    }

    public ReduceTTTaskInfo(boolean slotTaken, boolean wasKilled,
        ReduceTaskStatus status, Configuration conf, String user,
        boolean isTaskCleanup, String pid) {
      super(slotTaken, wasKilled, status, conf, user, isTaskCleanup, pid);
    }

    @Override
    public TaskStatus getTaskStatus() {
      return status;
    }
    
    public void readFields(DataInput in) throws IOException {
      super.readFields(in);
      status = new ReduceTaskStatus();
      status.readFields(in);
    }
  }

}
