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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.mapreduce.JobACL;
import org.apache.hadoop.mapreduce.JobID;
import org.apache.hadoop.security.authorize.AccessControlList;

/**
 * Event to record the submission of a job
 *
 */
public class JobSubmittedEvent implements HistoryEvent {
  private JobID jobId;
  private String jobName;
  private String userName;
  private long submitTime;
  private String jobConfPath;
  private Map<JobACL, AccessControlList> jobAcls;
  private String queue;

  /**
   * @deprecated Use
   *             {@link #JobSubmittedEvent(JobID, String, String, long, String,
   *             Map, String)}
   *             instead.
   */
  @Deprecated
  public JobSubmittedEvent(JobID id, String jobName, String userName,
      long submitTime, String jobConfPath) {
    this(id, jobName, userName, submitTime, jobConfPath,
        new HashMap<JobACL, AccessControlList>(), null);
  }

  /**
   * @deprecated Use
   *             {@link #JobSubmittedEvent(JobID, String, String, long, String,
   *             Map, String)}
   *             instead.
   */
  @Deprecated
  public JobSubmittedEvent(JobID id, String jobName, String userName,
      long submitTime, String jobConfPath,
      Map<JobACL, AccessControlList> jobACLs) {
    this(id, jobName, userName, submitTime, jobConfPath, jobACLs, null);
  }

  /**
   * Create an event to record job submission
   * @param id The job Id of the job
   * @param jobName Name of the job
   * @param userName Name of the user who submitted the job
   * @param submitTime Time of submission
   * @param jobConfPath Path of the Job Configuration file
   * @param jobACLs The configured acls for the job.
   * @param queue job queue name
   */
  public JobSubmittedEvent(JobID id, String jobName, String userName,
      long submitTime, String jobConfPath,
      Map<JobACL, AccessControlList> jobACLs, String queue) {
    this.jobId = id;
    this.jobName = jobName;
    this.userName = userName;
    this.submitTime = submitTime;
    this.jobConfPath = jobConfPath;
    this.jobAcls = jobACLs;
    this.queue = queue;
  }

  /** Get the Job Id */
  public JobID getJobId() { return jobId; }
  /** Get the Job name */
  public String getJobName() { return jobName; }
  /** Get the user name */
  public String getUserName() { return userName; }
  /** Get the submit time */
  public long getSubmitTime() { return submitTime; }
  /** Get the Path for the Job Configuration file */
  public String getJobConfPath() { return jobConfPath; }
  /** Get the acls configured for the job **/
  public Map<JobACL, AccessControlList> getJobAcls() {
    return jobAcls;
  }

  public String getJobQueueName() {
    return queue;
  }

  /** Get the event type */
  public EventType getEventType() { return EventType.JOB_SUBMITTED; }

}
