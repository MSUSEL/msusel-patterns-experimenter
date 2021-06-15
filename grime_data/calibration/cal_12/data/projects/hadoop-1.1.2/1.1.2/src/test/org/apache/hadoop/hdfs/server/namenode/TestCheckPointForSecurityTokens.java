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
package org.apache.hadoop.hdfs.server.namenode;

import junit.framework.Assert;
import java.io.*;
import java.util.Collection;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.protocol.FSConstants.SafeModeAction;
import org.apache.hadoop.hdfs.security.token.delegation.DelegationTokenIdentifier;
import org.apache.hadoop.hdfs.server.common.HdfsConstants.StartupOption;
import org.apache.hadoop.hdfs.tools.DFSAdmin;
import org.apache.hadoop.hdfs.DFSConfigKeys;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.security.token.Token;
import org.junit.Test;

/**
 * This class tests the creation and validation of a checkpoint.
 */
public class TestCheckPointForSecurityTokens {
  static final long seed = 0xDEADBEEFL;
  static final int blockSize = 4096;
  static final int fileSize = 8192;
  static final int numDatanodes = 3;
  short replication = 3;
  MiniDFSCluster cluster = null;

  NameNode startNameNode( Configuration conf,
                          String imageDirs,
                          String editsDirs,
                          StartupOption start) throws IOException {
    conf.set(DFSConfigKeys.FS_DEFAULT_NAME_KEY, "hdfs://localhost:0");
    conf.set(DFSConfigKeys.DFS_NAMENODE_HTTP_ADDRESS_KEY, "0.0.0.0:0");  
    conf.set(DFSConfigKeys.DFS_NAMENODE_NAME_DIR_KEY, imageDirs);
    conf.set(DFSConfigKeys.DFS_NAMENODE_EDITS_DIR_KEY, editsDirs);
    String[] args = new String[]{start.getName()};
    NameNode nn = NameNode.createNameNode(args, conf);
    Assert.assertTrue(nn.isInSafeMode());
    return nn;
  }
  
  private void cancelToken(Token<DelegationTokenIdentifier> token)
      throws IOException {
    cluster.getNameNode().getNamesystem().cancelDelegationToken(token);
  }
  
  private void renewToken(Token<DelegationTokenIdentifier> token)
      throws IOException {
      cluster.getNameNode().getNamesystem().renewDelegationToken(token);
  }
  
  /**
   * Tests save namepsace.
   */
  @Test
  public void testSaveNamespace() throws IOException {
    DistributedFileSystem fs = null;
    try {
      Configuration conf = new Configuration();
      cluster = new MiniDFSCluster(conf, numDatanodes, true, null);
      cluster.waitActive();
      fs = (DistributedFileSystem)(cluster.getFileSystem());
      FSNamesystem namesystem = cluster.getNameNode().getNamesystem();
      namesystem.getDelegationTokenSecretManager().startThreads();
      String renewer = UserGroupInformation.getLoginUser().getUserName();
      Token<DelegationTokenIdentifier> token1 = namesystem
          .getDelegationToken(new Text(renewer)); 
      Token<DelegationTokenIdentifier> token2 = namesystem
          .getDelegationToken(new Text(renewer));
      
      // Saving image without safe mode should fail
      DFSAdmin admin = new DFSAdmin(conf);
      String[] args = new String[]{"-saveNamespace"};

      // verify that the edits file is NOT empty
      Collection<File> editsDirs = cluster.getNameEditsDirs();
      for(File ed : editsDirs) {
        Assert.assertTrue(new File(ed, "current/edits").length() > Integer.SIZE/Byte.SIZE);
      }

      // Saving image in safe mode should succeed
      fs.setSafeMode(SafeModeAction.SAFEMODE_ENTER);
      try {
        admin.run(args);
      } catch(Exception e) {
        throw new IOException(e.getMessage());
      }
      // verify that the edits file is empty
      for(File ed : editsDirs) {
        Assert.assertTrue(new File(ed, "current/edits").length() == Integer.SIZE/Byte.SIZE);
      }
      // restart cluster
      cluster.shutdown();
      cluster = null;
      
      cluster = new MiniDFSCluster(conf, numDatanodes, false, null);
      cluster.waitActive();
      //Should be able to renew & cancel the delegation token after cluster restart
      try {
        renewToken(token1);
        renewToken(token2);
      } catch (IOException e) {
        Assert.fail("Could not renew or cancel the token");
      }
      
      namesystem = cluster.getNameNode().getNamesystem();
      namesystem.getDelegationTokenSecretManager().startThreads();
      Token<DelegationTokenIdentifier> token3 = namesystem
          .getDelegationToken(new Text(renewer));
      Token<DelegationTokenIdentifier> token4 = namesystem
          .getDelegationToken(new Text(renewer));
      
      // restart cluster again
      cluster.shutdown();
      cluster = null;
      
      cluster = new MiniDFSCluster(conf, numDatanodes, false, null);
      cluster.waitActive();
      
      namesystem = cluster.getNameNode().getNamesystem();
      namesystem.getDelegationTokenSecretManager().startThreads();
      Token<DelegationTokenIdentifier> token5 = namesystem
      .getDelegationToken(new Text(renewer));

      try {
        renewToken(token1);
        renewToken(token2);
        renewToken(token3);
        renewToken(token4);
        renewToken(token5);
      } catch (IOException e) {
        Assert.fail("Could not renew or cancel the token");
      }
      
      // restart cluster again
      cluster.shutdown();
      cluster = null;
      
      cluster = new MiniDFSCluster(conf, numDatanodes, false, null);
      cluster.waitActive();
      
      namesystem = cluster.getNameNode().getNamesystem();
      namesystem.getDelegationTokenSecretManager().startThreads();
      try {
        renewToken(token1);
        cancelToken(token1);
        renewToken(token2);
        cancelToken(token2);
        renewToken(token3);
        cancelToken(token3);
        renewToken(token4);
        cancelToken(token4);
        renewToken(token5);
        cancelToken(token5);
      } catch (IOException e) {
        Assert.fail("Could not renew or cancel the token");
      }
      
    } finally {
      if(fs != null) fs.close();
      if(cluster!= null) cluster.shutdown();
    }
  }
}
