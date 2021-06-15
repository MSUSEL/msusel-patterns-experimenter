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

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.test.system.TTProtocol;
import org.apache.hadoop.mapreduce.test.system.TTTaskInfo;
import org.apache.hadoop.mapred.TTTaskInfoImpl.MapTTTaskInfo;
import org.apache.hadoop.mapred.TTTaskInfoImpl.ReduceTTTaskInfo;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.test.system.DaemonProtocol;
import org.apache.hadoop.util.Shell;
import org.apache.hadoop.util.Shell.ShellCommandExecutor;
import org.apache.hadoop.mapred.TaskTracker.TaskInProgress;
import org.apache.hadoop.mapreduce.TaskAttemptID;

public privileged aspect TaskTrackerAspect {

  declare parents : TaskTracker implements TTProtocol;

  // Add a last sent status field to the Tasktracker class.
  TaskTrackerStatus TaskTracker.lastSentStatus = null;
  static String TaskTracker.TASKJARDIR = TaskTracker.JARSDIR;

  public synchronized TaskTrackerStatus TaskTracker.getStatus()
      throws IOException {
    return lastSentStatus;
  }

  public Configuration TaskTracker.getDaemonConf() throws IOException {
    return fConf;
  }

  public TTTaskInfo[] TaskTracker.getTasks() throws IOException {
    List<TTTaskInfo> infoList = new ArrayList<TTTaskInfo>();
    synchronized (tasks) {
      for (TaskInProgress tip : tasks.values()) {
        TTTaskInfo info = getTTTaskInfo(tip);
        infoList.add(info);
      }
    }
    return (TTTaskInfo[]) infoList.toArray(new TTTaskInfo[infoList.size()]);
  }

  public TTTaskInfo TaskTracker.getTask(org.apache.hadoop.mapreduce.TaskID id) 
      throws IOException {
    TaskID old = org.apache.hadoop.mapred.TaskID.downgrade(id);
    synchronized (tasks) {
      for(TaskAttemptID ta : tasks.keySet()) {
        if(old.equals(ta.getTaskID())) {
          return getTTTaskInfo(tasks.get(ta));
        }
      }
    }
    return null;
  }

  private TTTaskInfo TaskTracker.getTTTaskInfo(TaskInProgress tip) {
    TTTaskInfo info;
    if (tip.task.isMapTask()) {
      info = new MapTTTaskInfo(tip.slotTaken, tip.wasKilled,
          (MapTaskStatus) tip.getStatus(), tip.getJobConf(), tip.getTask()
              .getUser(), tip.getTask().isTaskCleanupTask(), getPid(tip.getTask().getTaskID()));
    } else {
      info = new ReduceTTTaskInfo(tip.slotTaken, tip.wasKilled,
          (ReduceTaskStatus) tip.getStatus(), tip.getJobConf(), tip.getTask()
              .getUser(), tip.getTask().isTaskCleanupTask(),getPid(tip.getTask().getTaskID()));
    }
    return info;
  }

  before(TaskTrackerStatus newStatus, TaskTracker tracker) : 
    set(TaskTrackerStatus TaskTracker.status) 
    && args(newStatus) && this(tracker) {
    if (newStatus == null) {
      tracker.lastSentStatus = tracker.status;
    }
  }

  pointcut ttConstructorPointCut(JobConf conf) : 
    call(TaskTracker.new(JobConf)) 
    && args(conf);

  after(JobConf conf) returning (TaskTracker tracker): 
    ttConstructorPointCut(conf) {
    try {
      UserGroupInformation ugi = UserGroupInformation.getCurrentUser();
      tracker.setUser(ugi.getShortUserName());
    } catch (IOException e) {
      tracker.LOG.warn("Unable to get the user information for the " +
          "Jobtracker");
    }
    tracker.setReady(true);
  }
  
  pointcut getVersionAspect(String protocol, long clientVersion) : 
    execution(public long TaskTracker.getProtocolVersion(String , 
      long) throws IOException) && args(protocol, clientVersion);

  long around(String protocol, long clientVersion) :  
    getVersionAspect(protocol, clientVersion) {
    if(protocol.equals(DaemonProtocol.class.getName())) {
      return DaemonProtocol.versionID;
    } else if(protocol.equals(TTProtocol.class.getName())) {
      return TTProtocol.versionID;
    } else {
      return proceed(protocol, clientVersion);
    }
  }  

  public boolean TaskTracker.isProcessTreeAlive(String pid) throws IOException {
    // Command to be executed is as follows :
    // ps -o pid,ppid,sid,command -e | grep -v ps | grep -v grep | grep
    // "$pid"
    String checkerCommand =
        getDaemonConf().get(
            "test.system.processgroup_checker_command",
            "ps -o pid,ppid,sid,command -e "
                + "| grep -v ps | grep -v grep | grep \"$");
    String[] command =
        new String[] { "bash", "-c", checkerCommand + pid + "\"" };
    ShellCommandExecutor shexec = new ShellCommandExecutor(command);
    try {
      shexec.execute();
    } catch (Shell.ExitCodeException e) {
      TaskTracker.LOG
          .info("The process tree grep threw a exitcode exception pointing "
              + "to process tree not being alive.");
      return false;
    }
    TaskTracker.LOG.info("The task grep command is : "
        + shexec.toString() + " the output from command is : "
        + shexec.getOutput());
    return true;
  }
}
