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

import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobID;
import org.apache.hadoop.mapreduce.TaskType;
import org.apache.hadoop.tools.rumen.Pre21JobHistoryConstants.Values;

/**
 * {@link JobStory} represents the runtime information available for a
 * completed Map-Reduce job.
 */
public interface JobStory {
  
  /**
   * Get the {@link JobConf} for the job.
   * @return the <code>JobConf</code> for the job
   */
  public JobConf getJobConf();
  
  /**
   * Get the job name.
   * @return the job name
   */
  public String getName();
  
  /**
   * Get the job ID
   * @return the job ID
   */
  public JobID getJobID();
  
  /**
   * Get the user who ran the job.
   * @return the user who ran the job
   */
  public String getUser();
  
  /**
   * Get the job submission time.
   * @return the job submission time
   */
  public long getSubmissionTime();
  
  /**
   * Get the number of maps in the {@link JobStory}.
   * @return the number of maps in the <code>Job</code>
   */
  public int getNumberMaps();
  
  /**
   * Get the number of reduce in the {@link JobStory}.
   * @return the number of reduces in the <code>Job</code>
   */
  public int getNumberReduces();

  /**
   * Get the input splits for the job.
   * @return the input splits for the job
   */
  public InputSplit[] getInputSplits();
  
  /**
   * Get {@link TaskInfo} for a given task.
   * @param taskType {@link TaskType} of the task
   * @param taskNumber Partition number of the task
   * @return the <code>TaskInfo</code> for the given task
   */
  public TaskInfo getTaskInfo(TaskType taskType, int taskNumber);
  
  /**
   * Get {@link TaskAttemptInfo} for a given task-attempt, without regard to
   * impact of locality (e.g. not needed to make scheduling decisions).
   * @param taskType {@link TaskType} of the task-attempt
   * @param taskNumber Partition number of the task-attempt
   * @param taskAttemptNumber Attempt number of the task
   * @return the <code>TaskAttemptInfo</code> for the given task-attempt
   */
  public TaskAttemptInfo getTaskAttemptInfo(TaskType taskType, 
                                            int taskNumber, 
                                            int taskAttemptNumber);
  
  /**
   * Get {@link TaskAttemptInfo} for a given task-attempt, considering impact
   * of locality.
   * @param taskNumber Partition number of the task-attempt
   * @param taskAttemptNumber Attempt number of the task
   * @param locality Data locality of the task as scheduled in simulation
   * @return the <code>TaskAttemptInfo</code> for the given task-attempt
   */
  public TaskAttemptInfo
    getMapTaskAttemptInfoAdjusted(int taskNumber,
                                  int taskAttemptNumber,
                                  int locality);
  
  /**
   * Get the outcome of the job execution.
   * @return The outcome of the job execution.
   */
  public Values getOutcome();
  
  /**
   * Get the queue where the job is submitted.
   * @return the queue where the job is submitted.
   */
  public String getQueueName();
}
