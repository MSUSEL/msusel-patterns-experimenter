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
 * JobID represents the immutable and unique identifier for 
 * the job. JobID consists of two parts. First part 
 * represents the jobtracker identifier, so that jobID to jobtracker map 
 * is defined. For cluster setup this string is the jobtracker 
 * start time, for local setting, it is "local".
 * Second part of the JobID is the job number. <br> 
 * An example JobID is : 
 * <code>job_200707121733_0003</code> , which represents the third job 
 * running at the jobtracker started at <code>200707121733</code>. 
 * <p>
 * Applications should never construct or parse JobID strings, but rather 
 * use appropriate constructors or {@link #forName(String)} method. 
 * 
 * @see TaskID
 * @see TaskAttemptID
 */
public class JobID extends org.apache.hadoop.mapreduce.JobID {
  /**
   * Constructs a JobID object 
   * @param jtIdentifier jobTracker identifier
   * @param id job number
   */
  public JobID(String jtIdentifier, int id) {
    super(jtIdentifier, id);
  }
  
  public JobID() { }

  /**
   * Downgrade a new JobID to an old one
   * @param old a new or old JobID
   * @return either old or a new JobID build to match old
   */
  public static JobID downgrade(org.apache.hadoop.mapreduce.JobID old) {
    if (old instanceof JobID) {
      return (JobID) old;
    } else {
      return new JobID(old.getJtIdentifier(), old.getId());
    }
  }

  @Deprecated
  public static JobID read(DataInput in) throws IOException {
    JobID jobId = new JobID();
    jobId.readFields(in);
    return jobId;
  }

  /** Construct a JobId object from given string 
   * @return constructed JobId object or null if the given String is null
   * @throws IllegalArgumentException if the given string is malformed
   */
  public static JobID forName(String str) throws IllegalArgumentException {
    return (JobID) org.apache.hadoop.mapreduce.JobID.forName(str);
  }
  
  /** 
   * Returns a regex pattern which matches task IDs. Arguments can 
   * be given null, in which case that part of the regex will be generic.  
   * For example to obtain a regex matching <i>any job</i> 
   * run on the jobtracker started at <i>200707121733</i>, we would use :
   * <pre> 
   * JobID.getTaskIDsPattern("200707121733", null);
   * </pre>
   * which will return :
   * <pre> "job_200707121733_[0-9]*" </pre> 
   * @param jtIdentifier jobTracker identifier, or null
   * @param jobId job number, or null
   * @return a regex pattern matching JobIDs
   */
  @Deprecated
  public static String getJobIDsPattern(String jtIdentifier, Integer jobId) {
    StringBuilder builder = new StringBuilder(JOB).append(SEPARATOR);
    builder.append(getJobIDsPatternWOPrefix(jtIdentifier, jobId));
    return builder.toString();
  }
  
  @Deprecated
  static StringBuilder getJobIDsPatternWOPrefix(String jtIdentifier,
                                                Integer jobId) {
    StringBuilder builder = new StringBuilder();
    if (jtIdentifier != null) {
      builder.append(jtIdentifier);
    } else {
      builder.append("[^").append(SEPARATOR).append("]*");
    }
    builder.append(SEPARATOR)
      .append(jobId != null ? idFormat.format(jobId) : "[0-9]*");
    return builder;
  }

}
