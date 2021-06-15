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
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;

/**
 * The response sent by the {@link JobTracker} to the hearbeat sent
 * periodically by the {@link TaskTracker}
 * 
 */
class HeartbeatResponse implements Writable, Configurable {
  Configuration conf = null;
  short responseId;
  int heartbeatInterval;
  TaskTrackerAction[] actions;
  Set<JobID> recoveredJobs = new HashSet<JobID>();

  HeartbeatResponse() {}
  
  HeartbeatResponse(short responseId, TaskTrackerAction[] actions) {
    this.responseId = responseId;
    this.actions = actions;
    this.heartbeatInterval = MRConstants.HEARTBEAT_INTERVAL_MIN;
  }
  
  public void setResponseId(short responseId) {
    this.responseId = responseId; 
  }
  
  public short getResponseId() {
    return responseId;
  }
  
  public void setRecoveredJobs(Set<JobID> ids) {
    recoveredJobs = ids; 
  }
  
  public Set<JobID> getRecoveredJobs() {
    return recoveredJobs;
  }
  
  public void setActions(TaskTrackerAction[] actions) {
    this.actions = actions;
  }
  
  public TaskTrackerAction[] getActions() {
    return actions;
  }
  
  public void setConf(Configuration conf) {
    this.conf = conf;
  }

  public Configuration getConf() {
    return conf;
  }

  public void setHeartbeatInterval(int interval) {
    this.heartbeatInterval = interval;
  }
  
  public int getHeartbeatInterval() {
    return heartbeatInterval;
  }
  
  public void write(DataOutput out) throws IOException {
    out.writeShort(responseId);
    out.writeInt(heartbeatInterval);
    if (actions == null) {
      WritableUtils.writeVInt(out, 0);
    } else {
      WritableUtils.writeVInt(out, actions.length);
      for (TaskTrackerAction action : actions) {
        WritableUtils.writeEnum(out, action.getActionId());
        action.write(out);
      }
    }
    // Write the job ids of the jobs that were recovered
    out.writeInt(recoveredJobs.size());
    for (JobID id : recoveredJobs) {
      id.write(out);
    }
  }
  
  public void readFields(DataInput in) throws IOException {
    this.responseId = in.readShort();
    this.heartbeatInterval = in.readInt();
    int length = WritableUtils.readVInt(in);
    if (length > 0) {
      actions = new TaskTrackerAction[length];
      for (int i=0; i < length; ++i) {
        TaskTrackerAction.ActionType actionType = 
          WritableUtils.readEnum(in, TaskTrackerAction.ActionType.class);
        actions[i] = TaskTrackerAction.createAction(actionType);
        actions[i].readFields(in);
      }
    } else {
      actions = null;
    }
    // Read the job ids of the jobs that were recovered
    int size = in.readInt();
    for (int i = 0; i < size; ++i) {
      JobID id = new JobID();
      id.readFields(in);
      recoveredJobs.add(id);
    }
  }
}
