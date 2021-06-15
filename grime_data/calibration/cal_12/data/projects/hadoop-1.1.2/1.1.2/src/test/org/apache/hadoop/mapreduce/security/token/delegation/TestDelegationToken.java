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
package org.apache.hadoop.mapreduce.security.token.delegation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.security.PrivilegedExceptionAction;

import org.apache.hadoop.io.DataInputBuffer;
import org.apache.hadoop.io.DataOutputBuffer;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MiniMRCluster;
import org.apache.hadoop.security.AccessControlException;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.security.token.Token;
import org.apache.hadoop.security.token.SecretManager.InvalidToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestDelegationToken {
  private MiniMRCluster cluster;
  private UserGroupInformation user1;
  private UserGroupInformation user2;
  
  @Before
  public void setup() throws Exception {
    user1 = UserGroupInformation.createUserForTesting("alice", 
                                                      new String[]{"users"});
    user2 = UserGroupInformation.createUserForTesting("bob", 
                                                      new String[]{"users"});
    cluster = new MiniMRCluster(0,0,1,"file:///",1);
  }
  
  @Test
  public void testDelegationToken() throws Exception {
    
    JobClient client;
    client = user1.doAs(new PrivilegedExceptionAction<JobClient>(){

      @Override
      public JobClient run() throws Exception {
        return new JobClient(cluster.createJobConf());
      }});
    JobClient bobClient;
    bobClient = user2.doAs(new PrivilegedExceptionAction<JobClient>(){

      @Override
      public JobClient run() throws Exception {
        return new JobClient(cluster.createJobConf());
      }});
    
    Token<DelegationTokenIdentifier> token = 
      client.getDelegationToken(new Text(user1.getUserName()));
    
    DataInputBuffer inBuf = new DataInputBuffer();
    byte[] bytes = token.getIdentifier();
    inBuf.reset(bytes, bytes.length);
    DelegationTokenIdentifier ident = new DelegationTokenIdentifier();
    ident.readFields(inBuf);
    
    assertEquals("alice", ident.getUser().getUserName());
    long createTime = ident.getIssueDate();
    long maxTime = ident.getMaxDate();
    long currentTime = System.currentTimeMillis();
    System.out.println("create time: " + createTime);
    System.out.println("current time: " + currentTime);
    System.out.println("max time: " + maxTime);
    assertTrue("createTime < current", createTime < currentTime);
    assertTrue("current < maxTime", currentTime < maxTime);
    client.renewDelegationToken(token);
    client.renewDelegationToken(token);
    try {
      bobClient.renewDelegationToken(token);
      Assert.fail("bob renew");
    } catch (AccessControlException ace) {
      // PASS
    }
    try {
      bobClient.cancelDelegationToken(token);
      Assert.fail("bob renew");
    } catch (AccessControlException ace) {
      // PASS
    }
    client.cancelDelegationToken(token);
    try {
      client.cancelDelegationToken(token);
      Assert.fail("second alice cancel");
    } catch (InvalidToken it) {
      // PASS
    }
  }
}

