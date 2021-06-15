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
package org.apache.hadoop.mapreduce;

/**
 * Job related ACLs
 */
public enum JobACL {

  /**
   * ACL for 'viewing' job. Dictates who can 'view' some or all of the job
   * related details.
   */
  VIEW_JOB(JobContext.JOB_ACL_VIEW_JOB),

  /**
   * ACL for 'modifying' job. Dictates who can 'modify' the job for e.g., by
   * killing the job, killing/failing a task of the job or setting priority of
   * the job.
   */
  MODIFY_JOB(JobContext.JOB_ACL_MODIFY_JOB);

  String aclName;

  JobACL(String name) {
    this.aclName = name;
  }

  /**
   * Get the name of the ACL. Here it is same as the name of the configuration
   * property for specifying the ACL for the job.
   * 
   * @return aclName
   */
  public String getAclName() {
    return aclName;
  }
}
