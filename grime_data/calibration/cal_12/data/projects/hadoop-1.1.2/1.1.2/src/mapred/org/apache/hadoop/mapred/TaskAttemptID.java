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
 * TaskAttemptID represents the immutable and unique identifier for 
 * a task attempt. Each task attempt is one particular instance of a Map or
 * Reduce Task identified by its TaskID. 
 * 
 * TaskAttemptID consists of 2 parts. First part is the 
 * {@link TaskID}, that this TaskAttemptID belongs to.
 * Second part is the task attempt number. <br> 
 * An example TaskAttemptID is : 
 * <code>attempt_200707121733_0003_m_000005_0</code> , which represents the
 * zeroth task attempt for the fifth map task in the third job 
 * running at the jobtracker started at <code>200707121733</code>.
 * <p>
 * Applications should never construct or parse TaskAttemptID strings
 * , but rather use appropriate constructors or {@link #forName(String)} 
 * method. 
 * 
 * @see JobID
 * @see TaskID
 */
public class TaskAttemptID extends org.apache.hadoop.mapreduce.TaskAttemptID {
  
  /**
   * Constructs a TaskAttemptID object from given {@link TaskID}.  
   * @param taskId TaskID that this task belongs to  
   * @param id the task attempt number
   */
  public TaskAttemptID(TaskID taskId, int id) {
    super(taskId, id);
  }
  
  /**
   * Constructs a TaskId object from given parts.
   * @param jtIdentifier jobTracker identifier
   * @param jobId job number 
   * @param isMap whether the tip is a map 
   * @param taskId taskId number
   * @param id the task attempt number
   */
  public TaskAttemptID(String jtIdentifier, int jobId, boolean isMap, 
                       int taskId, int id) {
    this(new TaskID(jtIdentifier, jobId, isMap, taskId), id);
  }
  
  public TaskAttemptID() { 
    super(new TaskID(), 0);
  }

  /**
   * Downgrade a new TaskAttemptID to an old one
   * @param old the new id
   * @return either old or a new TaskAttemptID constructed to match old
   */
  public static 
  TaskAttemptID downgrade(org.apache.hadoop.mapreduce.TaskAttemptID old) {
    if (old instanceof TaskAttemptID) {
      return (TaskAttemptID) old;
    } else {
      return new TaskAttemptID(TaskID.downgrade(old.getTaskID()), old.getId());
    }
  }

  public TaskID getTaskID() {
    return (TaskID) super.getTaskID();
  }

  public JobID getJobID() {
    return (JobID) super.getJobID();
  }

  @Deprecated
  public static TaskAttemptID read(DataInput in) throws IOException {
    TaskAttemptID taskId = new TaskAttemptID();
    taskId.readFields(in);
    return taskId;
  }
  
  /** Construct a TaskAttemptID object from given string 
   * @return constructed TaskAttemptID object or null if the given String is null
   * @throws IllegalArgumentException if the given string is malformed
   */
  public static TaskAttemptID forName(String str
                                      ) throws IllegalArgumentException {
    return (TaskAttemptID) 
             org.apache.hadoop.mapreduce.TaskAttemptID.forName(str);
  }
  
  /** 
   * Returns a regex pattern which matches task attempt IDs. Arguments can 
   * be given null, in which case that part of the regex will be generic.  
   * For example to obtain a regex matching <i>all task attempt IDs</i> 
   * of <i>any jobtracker</i>, in <i>any job</i>, of the <i>first 
   * map task</i>, we would use :
   * <pre> 
   * TaskAttemptID.getTaskAttemptIDsPattern(null, null, true, 1, null);
   * </pre>
   * which will return :
   * <pre> "attempt_[^_]*_[0-9]*_m_000001_[0-9]*" </pre> 
   * @param jtIdentifier jobTracker identifier, or null
   * @param jobId job number, or null
   * @param isMap whether the tip is a map, or null 
   * @param taskId taskId number, or null
   * @param attemptId the task attempt number, or null
   * @return a regex pattern matching TaskAttemptIDs
   */
  @Deprecated
  public static String getTaskAttemptIDsPattern(String jtIdentifier,
      Integer jobId, Boolean isMap, Integer taskId, Integer attemptId) {
    StringBuilder builder = new StringBuilder(ATTEMPT).append(SEPARATOR);
    builder.append(getTaskAttemptIDsPatternWOPrefix(jtIdentifier, jobId,
        isMap, taskId, attemptId));
    return builder.toString();
  }
  
  @Deprecated
  static StringBuilder getTaskAttemptIDsPatternWOPrefix(String jtIdentifier
      , Integer jobId, Boolean isMap, Integer taskId, Integer attemptId) {
    StringBuilder builder = new StringBuilder();
    builder.append(TaskID.getTaskIDsPatternWOPrefix(jtIdentifier
        , jobId, isMap, taskId))
        .append(SEPARATOR)
        .append(attemptId != null ? attemptId : "[0-9]*");
    return builder;
  }
}
