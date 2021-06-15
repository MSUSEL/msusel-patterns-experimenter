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

import org.apache.hadoop.mapred.QueueManager.QueueACL;
import org.apache.hadoop.mapreduce.JobACL;

/**
 * Generic operation that maps to the dependent set of ACLs that drive the
 * authorization of the operation.
 */
public enum Operation {
  VIEW_JOB_COUNTERS(QueueACL.ADMINISTER_JOBS, JobACL.VIEW_JOB),
  VIEW_JOB_DETAILS(QueueACL.ADMINISTER_JOBS, JobACL.VIEW_JOB),
  VIEW_TASK_LOGS(QueueACL.ADMINISTER_JOBS, JobACL.VIEW_JOB),
  KILL_JOB(QueueACL.ADMINISTER_JOBS, JobACL.MODIFY_JOB),
  FAIL_TASK(QueueACL.ADMINISTER_JOBS, JobACL.MODIFY_JOB),
  KILL_TASK(QueueACL.ADMINISTER_JOBS, JobACL.MODIFY_JOB),
  SET_JOB_PRIORITY(QueueACL.ADMINISTER_JOBS, JobACL.MODIFY_JOB),
  SUBMIT_JOB(QueueACL.SUBMIT_JOB, null);
  
  public QueueACL qACLNeeded;
  public JobACL jobACLNeeded;
  
  Operation(QueueACL qACL, JobACL jobACL) {
    this.qACLNeeded = qACL;
    this.jobACLNeeded = jobACL;
  }
}