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
package org.apache.hadoop.security.authorize;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.PrivilegedExceptionAction;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.HDFSPolicyProvider;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.tools.DFSAdmin;
import org.apache.hadoop.ipc.RemoteException;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MiniMRCluster;
import org.apache.hadoop.mapred.TestMiniMRWithDFS;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.util.StringUtils;

import junit.framework.TestCase;

public class TestServiceLevelAuthorization extends TestCase {
  public void testServiceLevelAuthorization() throws Exception {
    MiniDFSCluster dfs = null;
    MiniMRCluster mr = null;
    FileSystem fileSys = null;
    try {
      final int slaves = 4;

      // Turn on service-level authorization
      Configuration conf = new Configuration();
      conf.setClass(PolicyProvider.POLICY_PROVIDER_CONFIG, 
                    HadoopPolicyProvider.class, PolicyProvider.class);
      conf.setBoolean(ServiceAuthorizationManager.SERVICE_AUTHORIZATION_CONFIG, 
                      true);
      
      // Start the mini clusters
      dfs = new MiniDFSCluster(conf, slaves, true, null);
      fileSys = dfs.getFileSystem();
      JobConf mrConf = new JobConf(conf);
      mr = new MiniMRCluster(slaves, fileSys.getUri().toString(), 1, 
                             null, null, mrConf);
      // make cleanup inline sothat validation of existence of these directories
      // can be done
      mr.setInlineCleanupThreads();
      
      // Run examples
      TestMiniMRWithDFS.runPI(mr, mr.createJobConf(mrConf));
      TestMiniMRWithDFS.runWordCount(mr, mr.createJobConf(mrConf));
    } finally {
      if (dfs != null) { dfs.shutdown(); }
      if (mr != null) { mr.shutdown();
      }
    }
  }
  
  private static final String DUMMY_ACL = "nouser nogroup";
  private static final String UNKNOWN_USER = "dev,null";
  
  private void rewriteHadoopPolicyFile(File policyFile) throws IOException {
    FileWriter fos = new FileWriter(policyFile);
    PolicyProvider policyProvider = new HDFSPolicyProvider();
    fos.write("<configuration>\n");
    for (Service service : policyProvider.getServices()) {
      String key = service.getServiceKey();
      String value ="*";
      if (key.equals("security.refresh.policy.protocol.acl")) {
        value = DUMMY_ACL;
      }
      fos.write("<property><name>"+ key + "</name><value>" + value + 
                "</value></property>\n");
      System.err.println("<property><name>"+ key + "</name><value>" + value + 
          "</value></property>\n");
    }
    fos.write("</configuration>\n");
    fos.close();
  }
  
  private void refreshPolicy(Configuration conf)  throws IOException {
    DFSAdmin dfsAdmin = new DFSAdmin(conf);
    dfsAdmin.refreshServiceAcl();
  }
  
  public void testRefresh() throws Exception {
    MiniDFSCluster dfs = null;
    try {
      final int slaves = 4;

      // Turn on service-level authorization
      final Configuration conf = new Configuration();
      conf.setClass(PolicyProvider.POLICY_PROVIDER_CONFIG, 
                    HDFSPolicyProvider.class, PolicyProvider.class);
      conf.setBoolean(ServiceAuthorizationManager.SERVICE_AUTHORIZATION_CONFIG, 
                      true);
      
      // Start the mini dfs cluster
      dfs = new MiniDFSCluster(conf, slaves, true, null);

      // Refresh the service level authorization policy
      refreshPolicy(conf);
      
      // Simulate an 'edit' of hadoop-policy.xml
      String confDir = System.getProperty("test.build.extraconf", 
                                          "build/test/extraconf");
      String HADOOP_POLICY_FILE = System.getProperty("hadoop.policy.file");
      File policyFile = new File(confDir, HADOOP_POLICY_FILE);
      String policyFileCopy = HADOOP_POLICY_FILE + ".orig";
      FileUtil.copy(policyFile, FileSystem.getLocal(conf),   // first save original 
                    new Path(confDir, policyFileCopy), false, conf);
      rewriteHadoopPolicyFile(                               // rewrite the file
          new File(confDir, HADOOP_POLICY_FILE));
      
      // Refresh the service level authorization policy
      refreshPolicy(conf);
      
      // Refresh the service level authorization policy once again, 
      // this time it should fail!
      try {
        // Note: hadoop-policy.xml for tests has 
        // security.refresh.policy.protocol.acl = ${user.name}
        UserGroupInformation unknownUser = 
          UserGroupInformation.createRemoteUser("unknown");
        unknownUser.doAs(new PrivilegedExceptionAction<Void>() {
          public Void run() throws IOException {
            refreshPolicy(conf);
            return null;
          }
        });
        fail("Refresh of NameNode's policy file cannot be successful!");
      } catch (Exception re) {
        System.out.println("Good, refresh worked... refresh failed with: " + 
                           StringUtils.stringifyException(re));
      } finally {
        // Reset to original hadoop-policy.xml
        FileUtil.fullyDelete(new File(confDir, 
            HADOOP_POLICY_FILE));
        FileUtil.replaceFile(new File(confDir, policyFileCopy), new File(confDir, HADOOP_POLICY_FILE));
      }
    } finally {
      if (dfs != null) { dfs.shutdown(); }
    }
  }

}
