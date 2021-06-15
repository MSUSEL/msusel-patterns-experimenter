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

import java.io.IOException;
import java.security.PrivilegedExceptionAction;

import javax.security.auth.login.LoginException;

import org.apache.hadoop.security.UserGroupInformation;

public class TestQueueManagerForJobKillAndNonDefaultQueue extends TestQueueManager {

  public void testDisabledACLForNonDefaultQueue() 
  throws IOException, InterruptedException {
    try {
      // allow everyone in default queue
      JobConf conf = setupConf(QueueManager.toFullPropertyName
                               ("default", submitAcl), "*");
      // setup a different queue
      conf.set("mapred.queue.names", "default,q1");
      // setup a different acl for this queue.
      conf.set(QueueManager.toFullPropertyName
               ("q1", submitAcl), "dummy-user");
      // verify job submission to other queue fails.
      verifyJobSubmission(conf, false, "user1,group1", "q1");
    } finally {
      tearDownCluster();
    }
  }

  public void testEnabledACLForNonDefaultQueue() 
  throws IOException, LoginException, InterruptedException {
    try {
      UserGroupInformation ugi = createNecessaryUsers();
      String[] groups = ugi.getGroupNames();
      String userName = ugi.getShortUserName();
      // allow everyone in default queue
      JobConf conf = setupConf(QueueManager.toFullPropertyName
                               ("default", submitAcl), "*");
      // setup a different queue
      conf.set("mapred.queue.names", "default,q2");
      // setup a different acl for this queue.
      conf.set(QueueManager.toFullPropertyName
               ("q2", submitAcl), userName);
      // verify job submission to other queue fails.
      verifyJobSubmission(conf, true,
                          userName + "," + groups[groups.length-1], "q2");
    } finally {
      tearDownCluster();
    }
  }

  public void testAllEnabledACLForJobKill() 
  throws IOException, InterruptedException {
    try {
      UserGroupInformation ugi = createNecessaryUsers();
      // create other user who will try to kill the job of ugi.
      final UserGroupInformation otherUGI = UserGroupInformation.
        createUserForTesting("user1", new String [] {"group1"});

      ugi.doAs(new PrivilegedExceptionAction<Object>() {

                 @Override
                   public Object run() throws Exception {
                   JobConf conf = setupConf(QueueManager.toFullPropertyName
                                            ("default", adminAcl), "*");
                   verifyJobKill(otherUGI, conf, true);
                   return null;
                 }
               });
    } finally {
      tearDownCluster();
    }
  }

  public void testAllDisabledACLForJobKill() 
  throws IOException, InterruptedException {
    try {
      // Create a fake superuser for all processes to execute within
      final UserGroupInformation ugi = createNecessaryUsers();

      // create other users who will try to kill the job of ugi.
      final UserGroupInformation otherUGI1 = UserGroupInformation.
        createUserForTesting("user1", new String [] {"group1"});
      final UserGroupInformation otherUGI2 = UserGroupInformation.
        createUserForTesting("user2", new String [] {"group2"});

      ugi.doAs(new PrivilegedExceptionAction<Object>() {

                 @Override
                   public Object run() throws Exception {
                   // No queue admins
                   JobConf conf = setupConf(QueueManager.toFullPropertyName
                                            ("default", adminAcl), " ");
                   // Run job as ugi and try to kill job as user1, who
                   // (obviously) should not be able to kill the job.
                   verifyJobKill(otherUGI1, conf, false);
                   verifyJobKill(otherUGI2, conf, false);

                   // Check if cluster administrator can kill job
                   conf.set(JobConf.MR_ADMINS, "user1 group2");
                   tearDownCluster();
                   verifyJobKill(otherUGI1, conf, true);
                   verifyJobKill(otherUGI2, conf, true);
        
                   // Check if MROwner(user who started
                   // the mapreduce cluster) can kill job
                   verifyJobKill(ugi, conf, true);

                   return null;
                 }
               });
    } finally {
      tearDownCluster();
    }
  }

  
}
