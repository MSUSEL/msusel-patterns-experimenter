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
import java.net.URL;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableFactories;
import org.apache.hadoop.io.WritableFactory;

/**************************************************
 * A JobProfile is a MapReduce primitive.  Tracks a job,
 * whether living or dead.
 *
 **************************************************/
public class JobProfile implements Writable {

  static {                                      // register a ctor
    WritableFactories.setFactory
      (JobProfile.class,
       new WritableFactory() {
         public Writable newInstance() { return new JobProfile(); }
       });
  }

  String user;
  final JobID jobid;
  String jobFile;
  String url;
  String name;
  String queueName;
  
  /**
   * Construct an empty {@link JobProfile}.
   */
  public JobProfile() {
    jobid = new JobID();
  }

  /**
   * Construct a {@link JobProfile} the userid, jobid, 
   * job config-file, job-details url and job name. 
   * 
   * @param user userid of the person who submitted the job.
   * @param jobid id of the job.
   * @param jobFile job configuration file. 
   * @param url link to the web-ui for details of the job.
   * @param name user-specified job name.
   */
  public JobProfile(String user, org.apache.hadoop.mapreduce.JobID jobid, 
                    String jobFile, String url,
                    String name) {
    this(user, jobid, jobFile, url, name, JobConf.DEFAULT_QUEUE_NAME);
  }

  /**
   * Construct a {@link JobProfile} the userid, jobid, 
   * job config-file, job-details url and job name. 
   * 
   * @param user userid of the person who submitted the job.
   * @param jobid id of the job.
   * @param jobFile job configuration file. 
   * @param url link to the web-ui for details of the job.
   * @param name user-specified job name.
   * @param queueName name of the queue to which the job is submitted
   */
  public JobProfile(String user, org.apache.hadoop.mapreduce.JobID jobid, 
                    String jobFile, String url,
                    String name, String queueName) {
    this.user = user;
    this.jobid = JobID.downgrade(jobid);
    this.jobFile = jobFile;
    this.url = url;
    this.name = name;
    this.queueName = queueName;
  }
  
  /**
   * @deprecated use JobProfile(String, JobID, String, String, String) instead
   */
  @Deprecated
  public JobProfile(String user, String jobid, String jobFile, String url,
      String name) {
    this(user, JobID.forName(jobid), jobFile, url, name);
  }
  
  /**
   * Get the user id.
   */
  public String getUser() {
    return user;
  }
    
  /**
   * Get the job id.
   */
  public JobID getJobID() {
    return jobid;
  }

  /**
   * @deprecated use getJobID() instead
   */
  @Deprecated
  public String getJobId() {
    return jobid.toString();
  }
  
  /**
   * Get the configuration file for the job.
   */
  public String getJobFile() {
    return jobFile;
  }

  /**
   * Get the link to the web-ui for details of the job.
   */
  public URL getURL() {
    try {
      return new URL(url);
    } catch (IOException ie) {
      return null;
    }
  }

  /**
   * Get the user-specified job name.
   */
  public String getJobName() {
    return name;
  }
  
  /**
   * Get the name of the queue to which the job is submitted.
   * @return name of the queue.
   */
  public String getQueueName() {
    return queueName;
  }
  
  ///////////////////////////////////////
  // Writable
  ///////////////////////////////////////
  public void write(DataOutput out) throws IOException {
    jobid.write(out);
    Text.writeString(out, jobFile);
    Text.writeString(out, url);
    Text.writeString(out, user);
    Text.writeString(out, name);
    Text.writeString(out, queueName);
  }

  public void readFields(DataInput in) throws IOException {
    jobid.readFields(in);
    this.jobFile = Text.readString(in);
    this.url = Text.readString(in);
    this.user = Text.readString(in);
    this.name = Text.readString(in);
    this.queueName = Text.readString(in);
  }
}


