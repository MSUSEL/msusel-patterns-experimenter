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
import javax.security.auth.login.LoginException;
import junit.framework.TestCase;

import org.apache.hadoop.mapred.QueueManager.QueueACL;
import org.apache.hadoop.security.UserGroupInformation;

/**
 * Unit test class to test queue acls
 *
 */
public class TestQueueAclsForCurrentUser extends TestCase {

  private QueueManager queueManager;
  private JobConf conf = null;
  UserGroupInformation currentUGI = null;
  String submitAcl = QueueACL.SUBMIT_JOB.getAclName();
  String adminAcl  = QueueACL.ADMINISTER_JOBS.getAclName();

  private void setupConfForNoAccess() throws IOException,LoginException {
    currentUGI = UserGroupInformation.getLoginUser();
    String userName = currentUGI.getUserName();
    conf = new JobConf();

    conf.setBoolean(JobConf.MR_ACLS_ENABLED,true);

    conf.set("mapred.queue.names", "qu1,qu2");
    //Only user u1 has access
    conf.set(QueueManager.toFullPropertyName("qu1", submitAcl), "u1");
    conf.set(QueueManager.toFullPropertyName("qu1", adminAcl), "u1");
    //q2 only group g2 has acls for the queues
    conf.set(QueueManager.toFullPropertyName("qu2", submitAcl), " g2");
    conf.set(QueueManager.toFullPropertyName("qu2", adminAcl), " g2");
    queueManager = new QueueManager(conf);

  }

  /**
   *  sets up configuration for acls test.
   * @return
   */
  private void setupConf(boolean aclSwitch) throws IOException,LoginException{
    currentUGI = UserGroupInformation.getLoginUser();
    String userName = currentUGI.getUserName();
    conf = new JobConf();

    conf.setBoolean(JobConf.MR_ACLS_ENABLED, aclSwitch);

    conf.set("mapred.queue.names", "qu1,qu2,qu3,qu4,qu5,qu6,qu7");
    //q1 Has acls for all the users, supports both submit and administer
    conf.set(QueueManager.toFullPropertyName("qu1", submitAcl), "*");
    conf.set(QueueManager.toFullPropertyName("qu1", adminAcl), "*");
    //q2 only u2 has acls for the queues
    conf.set(QueueManager.toFullPropertyName("qu2", submitAcl), "u2");
    conf.set(QueueManager.toFullPropertyName("qu2", adminAcl), "u2");
    //q3  Only u2 has submit operation access rest all have administer access
    conf.set(QueueManager.toFullPropertyName("qu3", submitAcl), "u2");
    conf.set(QueueManager.toFullPropertyName("qu3", adminAcl), "*");
    //q4 Only u2 has administer access , anyone can do submit
    conf.set(QueueManager.toFullPropertyName("qu4", submitAcl), "*");
    conf.set(QueueManager.toFullPropertyName("qu4", adminAcl), "u2");
    //qu6 only current user has submit access
    conf.set(QueueManager.toFullPropertyName("qu6", submitAcl),userName);
    conf.set(QueueManager.toFullPropertyName("qu6", adminAcl),"u2");
    //qu7 only current user has administrator access
    conf.set(QueueManager.toFullPropertyName("qu7", submitAcl),"u2");
    conf.set(QueueManager.toFullPropertyName("qu7", adminAcl),userName);
    //qu8 only current group has access
    StringBuilder groupNames = new StringBuilder("");
    String[] ugiGroupNames = currentUGI.getGroupNames();
    int max = ugiGroupNames.length-1;
    for(int j=0;j< ugiGroupNames.length;j++) {
      groupNames.append(ugiGroupNames[j]);
      if(j<max) {
        groupNames.append(",");
      }
    }
    conf.set(QueueManager.toFullPropertyName("qu5", submitAcl),
        " " + groupNames.toString());
    conf.set(QueueManager.toFullPropertyName("qu5", adminAcl),
        " " + groupNames.toString());

    queueManager = new QueueManager(conf);
  }

  public void testQueueAclsForCurrentuser() throws IOException,LoginException {
    setupConf(true);
    QueueAclsInfo[] queueAclsInfoList =
            queueManager.getQueueAcls(currentUGI);
    checkQueueAclsInfo(queueAclsInfoList);
  }

  public void testQueueAclsForCurrentUserAclsDisabled() throws IOException,
          LoginException {
    setupConf(false);
    //fetch the acls info for current user.
    QueueAclsInfo[] queueAclsInfoList = queueManager.
            getQueueAcls(currentUGI);
    checkQueueAclsInfo(queueAclsInfoList);
  }

  public void testQueueAclsForNoAccess() throws IOException,LoginException {
    setupConfForNoAccess();
    QueueAclsInfo[] queueAclsInfoList = queueManager.
            getQueueAcls(currentUGI);
    assertTrue(queueAclsInfoList.length == 0);
  }

  private void checkQueueAclsInfo(QueueAclsInfo[] queueAclsInfoList)
          throws IOException {
    if (conf.get(JobConf.MR_ACLS_ENABLED).equalsIgnoreCase("true")) {
      for (int i = 0; i < queueAclsInfoList.length; i++) {
        QueueAclsInfo acls = queueAclsInfoList[i];
        String queueName = acls.getQueueName();
        assertFalse(queueName.contains("qu2"));
        if (queueName.equals("qu1")) {
          assertTrue(acls.getOperations().length == 2);
          assertTrue(checkAll(acls.getOperations()));
        } else if (queueName.equals("qu3")) {
          assertTrue(acls.getOperations().length == 1);
          assertTrue(acls.getOperations()[0].equalsIgnoreCase(adminAcl));
        } else if (queueName.equals("qu4")) {
          assertTrue(acls.getOperations().length == 1);
          assertTrue(acls.getOperations()[0].equalsIgnoreCase(submitAcl));
        } else if (queueName.equals("qu5")) {
          assertTrue(acls.getOperations().length == 2);
          assertTrue(checkAll(acls.getOperations()));
        } else if(queueName.equals("qu6")) {
          assertTrue(acls.getOperations()[0].equals(submitAcl));
        } else if(queueName.equals("qu7")) {
          assertTrue(acls.getOperations()[0].equals(adminAcl));
        } 
      }
    } else {
      for (int i = 0; i < queueAclsInfoList.length; i++) {
        QueueAclsInfo acls = queueAclsInfoList[i];
        String queueName = acls.getQueueName();
        assertTrue(acls.getOperations().length == 2);
        assertTrue(checkAll(acls.getOperations()));
      }
    }
  }

  private boolean checkAll(String[] operations){
    boolean submit = false;
    boolean admin = false;

    for(String val: operations){
      if(val.equalsIgnoreCase(submitAcl))
        submit = true;
      else if(val.equalsIgnoreCase(adminAcl))
        admin = true;
    }
    if(submit && admin) return true;
    return false;
  }
}
