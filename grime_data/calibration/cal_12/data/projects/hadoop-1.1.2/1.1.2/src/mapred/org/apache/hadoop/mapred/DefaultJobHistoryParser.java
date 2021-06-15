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

import java.util.*;
import java.io.*;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.mapred.JobHistory.Keys; 
import org.apache.hadoop.mapred.JobHistory.Values;

/**
 * Default parser for job history files. It creates object model from 
 * job history file. 
 * 
 */
public class DefaultJobHistoryParser {

  // This class is required to work around the Java compiler's lack of
  // run-time information on generic classes. In particular, we need to be able
  // to cast to this type without generating compiler warnings, which is only
  // possible if it is a non-generic class.

  /**
   * Populates a JobInfo object from the job's history log file. 
   * @param jobHistoryFile history file for this job. 
   * @param job a precreated JobInfo object, should be non-null. 
   * @param fs FileSystem where historyFile is present. 
   * @throws IOException
   */
  public static void parseJobTasks(String jobHistoryFile, 
                       JobHistory.JobInfo job, FileSystem fs)
    throws IOException {
    JobHistory.parseHistoryFromFS(jobHistoryFile, 
                            new JobTasksParseListener(job), fs);
  }
  
  /**
   * Listener for Job's history log file, it populates JobHistory.JobInfo 
   * object with data from log file. 
   */
  static class JobTasksParseListener
    implements JobHistory.Listener {
    JobHistory.JobInfo job;

    JobTasksParseListener(JobHistory.JobInfo job) {
      this.job = job;
    }

    private JobHistory.Task getTask(String taskId) {
      JobHistory.Task task = job.getAllTasks().get(taskId);
      if (null == task) {
        task = new JobHistory.Task();
        task.set(Keys.TASKID, taskId);
        job.getAllTasks().put(taskId, task);
      }
      return task;
    }

    private JobHistory.MapAttempt getMapAttempt(
                                                String jobid, String jobTrackerId, String taskId, String taskAttemptId) {

      JobHistory.Task task = getTask(taskId);
      JobHistory.MapAttempt mapAttempt = 
        (JobHistory.MapAttempt) task.getTaskAttempts().get(taskAttemptId);
      if (null == mapAttempt) {
        mapAttempt = new JobHistory.MapAttempt();
        mapAttempt.set(Keys.TASK_ATTEMPT_ID, taskAttemptId);
        task.getTaskAttempts().put(taskAttemptId, mapAttempt);
      }
      return mapAttempt;
    }

    private JobHistory.ReduceAttempt getReduceAttempt(
                                                      String jobid, String jobTrackerId, String taskId, String taskAttemptId) {

      JobHistory.Task task = getTask(taskId);
      JobHistory.ReduceAttempt reduceAttempt = 
        (JobHistory.ReduceAttempt) task.getTaskAttempts().get(taskAttemptId);
      if (null == reduceAttempt) {
        reduceAttempt = new JobHistory.ReduceAttempt();
        reduceAttempt.set(Keys.TASK_ATTEMPT_ID, taskAttemptId);
        task.getTaskAttempts().put(taskAttemptId, reduceAttempt);
      }
      return reduceAttempt;
    }

    // JobHistory.Listener implementation 
    public void handle(JobHistory.RecordTypes recType, Map<Keys, String> values)
      throws IOException {
      String jobTrackerId = values.get(JobHistory.Keys.JOBTRACKERID);
      String jobid = values.get(Keys.JOBID);
      
      if (recType == JobHistory.RecordTypes.Job) {
        job.handle(values);
      }if (recType.equals(JobHistory.RecordTypes.Task)) {
        String taskid = values.get(JobHistory.Keys.TASKID);
        getTask(taskid).handle(values);
      } else if (recType.equals(JobHistory.RecordTypes.MapAttempt)) {
        String taskid =  values.get(Keys.TASKID);
        String mapAttemptId = values.get(Keys.TASK_ATTEMPT_ID);

        getMapAttempt(jobid, jobTrackerId, taskid, mapAttemptId).handle(values);
      } else if (recType.equals(JobHistory.RecordTypes.ReduceAttempt)) {
        String taskid = values.get(Keys.TASKID);
        String reduceAttemptId = values.get(Keys.TASK_ATTEMPT_ID);

        getReduceAttempt(jobid, jobTrackerId, taskid, reduceAttemptId).handle(values);
      }
    }
  }

  // call this only for jobs that succeeded for better results. 
  abstract static class NodesFilter implements JobHistory.Listener {
    private Map<String, Set<String>> badNodesToNumFailedTasks =
      new HashMap<String, Set<String>>();
    
    Map<String, Set<String>> getValues(){
      return badNodesToNumFailedTasks; 
    }
    String failureType;
    
    public void handle(JobHistory.RecordTypes recType, Map<Keys, String> values)
      throws IOException {
      if (recType.equals(JobHistory.RecordTypes.MapAttempt) || 
          recType.equals(JobHistory.RecordTypes.ReduceAttempt)) {
        if (failureType.equals(values.get(Keys.TASK_STATUS)) ) {
          String hostName = values.get(Keys.HOSTNAME);
          String taskid = values.get(Keys.TASKID); 
          Set<String> tasks = badNodesToNumFailedTasks.get(hostName); 
          if (null == tasks ){
            tasks = new TreeSet<String>(); 
            tasks.add(taskid);
            badNodesToNumFailedTasks.put(hostName, tasks);
          }else{
            tasks.add(taskid);
          }
        }
      }      
    }
    abstract void setFailureType();
    String getFailureType() {
      return failureType;
    }
    NodesFilter() {
      setFailureType();
    }
  }
 
  static class FailedOnNodesFilter extends NodesFilter {
    void setFailureType() {
      failureType = Values.FAILED.name();
    }
  }
  static class KilledOnNodesFilter extends NodesFilter {
    void setFailureType() {
      failureType = Values.KILLED.name();
    }
  }
}
