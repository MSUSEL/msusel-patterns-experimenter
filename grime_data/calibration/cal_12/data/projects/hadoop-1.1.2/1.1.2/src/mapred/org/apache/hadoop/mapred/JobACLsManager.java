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

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.mapreduce.JobACL;
import org.apache.hadoop.security.AccessControlException;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.security.authorize.AccessControlList;

class JobACLsManager {

	  JobConf conf;
	  
	  public JobACLsManager(JobConf conf) {
        this.conf = conf;
      }

	  boolean areACLsEnabled() {
	    return conf.getBoolean(JobConf.MR_ACLS_ENABLED, false);
	  }

	  /**
	   * Construct the jobACLs from the configuration so that they can be kept in
	   * the memory. If authorization is disabled on the JT, nothing is constructed
	   * and an empty map is returned.
	   * 
	   * @return JobACL to AccessControlList map.
	   */
	  Map<JobACL, AccessControlList> constructJobACLs(JobConf conf) {
	    
	    Map<JobACL, AccessControlList> acls =
	      new HashMap<JobACL, AccessControlList>();

	    // Don't construct anything if authorization is disabled.
	    if (!areACLsEnabled()) {
	      return acls;
	    }

	    for (JobACL aclName : JobACL.values()) {
	      String aclConfigName = aclName.getAclName();
	      String aclConfigured = conf.get(aclConfigName);
	      if (aclConfigured == null) {
	        // If ACLs are not configured at all, we grant no access to anyone. So
	        // jobOwner and cluster administrators _only_ can do 'stuff'
	        aclConfigured = "";
	      }
	      acls.put(aclName, new AccessControlList(aclConfigured));
	    }
	    return acls;
	  }

	  /**
	   * If authorization is enabled, checks whether the user (in the callerUGI)
	   * is authorized to perform the operation specified by 'jobOperation' on
	   * the job by checking if the user is jobOwner or part of job ACL for the
	   * specific job operation.
	   * <ul>
	   * <li>The owner of the job can do any operation on the job</li>
	   * <li>For all other users/groups job-acls are checked</li>
	   * </ul>
	   * @param callerUGI
	   * @param jobOperation
	   * @param jobOwner
	   * @param jobACL
	   * @throws AccessControlException
	   */
	  boolean checkAccess(UserGroupInformation callerUGI,
	      JobACL jobOperation, String jobOwner, AccessControlList jobACL)
	      throws AccessControlException {

	    String user = callerUGI.getShortUserName();
	    if (!areACLsEnabled()) {
	      return true;
	    }

	    // Allow Job-owner for any operation on the job
	    if (user.equals(jobOwner) 
	        || jobACL.isUserAllowed(callerUGI)) {
	      return true;
	    }

	    return false;
	  }
	}
