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
import java.io.IOException;

/**
 * TaskID represents the immutable and unique identifier for 
 * a Map or Reduce Task. Each TaskID encompasses multiple attempts made to
 * execute the Map or Reduce Task, each of which are uniquely indentified by
 * their TaskAttemptID.
 * 
 * TaskID consists of 3 parts. First part is the {@link JobID}, that this 
 * TaskInProgress belongs to. Second part of the TaskID is either 'm' or 'r' 
 * representing whether the task is a map task or a reduce task. 
 * And the third part is the task number. <br> 
 * An example TaskID is : 
 * <code>task_200707121733_0003_m_000005</code> , which represents the
 * fifth map task in the third job running at the jobtracker 
 * started at <code>200707121733</code>. 
 * <p>
 * Applications should never construct or parse TaskID strings
 * , but rather use appropriate constructors or {@link #forName(String)} 
 * method. 
 * 
 * @see JobID
 * @see TaskAttemptID
 */
public class TaskID extends org.apache.hadoop.mapreduce.TaskID {

  /**
   * Constructs a TaskID object from given {@link JobID}.  
   * @param jobId JobID that this tip belongs to 
   * @param isMap whether the tip is a map 
   * @param id the tip number
   */
  public TaskID(org.apache.hadoop.mapreduce.JobID jobId, boolean isMap,int id) {
    super(jobId, isMap, id);
  }
  
  /**
   * Constructs a TaskInProgressId object from given parts.
   * @param jtIdentifier jobTracker identifier
   * @param jobId job number 
   * @param isMap whether the tip is a map 
   * @param id the tip number
   */
  public TaskID(String jtIdentifier, int jobId, boolean isMap, int id) {
    this(new JobID(jtIdentifier, jobId), isMap, id);
  }
  
  public TaskID() {
    super(new JobID(), false, 0);
  }
  
  /**
   * Downgrade a new TaskID to an old one
   * @param old a new or old TaskID
   * @return either old or a new TaskID build to match old
   */
  public static TaskID downgrade(org.apache.hadoop.mapreduce.TaskID old) {
    if (old instanceof TaskID) {
      return (TaskID) old;
    } else {
      return new TaskID(JobID.downgrade(old.getJobID()), old.isMap(), 
                        old.getId());
    }
  }

  @Deprecated
  public static TaskID read(DataInput in) throws IOException {
    TaskID tipId = new TaskID();
    tipId.readFields(in);
    return tipId;
  }
  
  public JobID getJobID() {
    return (JobID) super.getJobID();
  }

  /** 
   * Returns a regex pattern which matches task IDs. Arguments can 
   * be given null, in which case that part of the regex will be generic.  
   * For example to obtain a regex matching <i>the first map task</i> 
   * of <i>any jobtracker</i>, of <i>any job</i>, we would use :
   * <pre> 
   * TaskID.getTaskIDsPattern(null, null, true, 1);
   * </pre>
   * which will return :
   * <pre> "task_[^_]*_[0-9]*_m_000001*" </pre> 
   * @param jtIdentifier jobTracker identifier, or null
   * @param jobId job number, or null
   * @param isMap whether the tip is a map, or null 
   * @param taskId taskId number, or null
   * @return a regex pattern matching TaskIDs
   */
  @Deprecated
  public static String getTaskIDsPattern(String jtIdentifier, Integer jobId
      , Boolean isMap, Integer taskId) {
    StringBuilder builder = new StringBuilder(TASK).append(SEPARATOR)
      .append(getTaskIDsPatternWOPrefix(jtIdentifier, jobId, isMap, taskId));
    return builder.toString();
  }
  
  @Deprecated
  static StringBuilder getTaskIDsPatternWOPrefix(String jtIdentifier
      , Integer jobId, Boolean isMap, Integer taskId) {
    StringBuilder builder = new StringBuilder();
    builder.append(JobID.getJobIDsPatternWOPrefix(jtIdentifier, jobId))
      .append(SEPARATOR)
      .append(isMap != null ? (isMap ? "m" : "r") : "(m|r)").append(SEPARATOR)
      .append(taskId != null ? idFormat.format(taskId) : "[0-9]*");
    return builder;
  }

  public static TaskID forName(String str
                               ) throws IllegalArgumentException {
    return (TaskID) org.apache.hadoop.mapreduce.TaskID.forName(str);
  }

}
