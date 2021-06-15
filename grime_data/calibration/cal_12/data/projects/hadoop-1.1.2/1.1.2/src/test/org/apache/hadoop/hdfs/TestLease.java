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
package org.apache.hadoop.hdfs;

import java.io.DataOutputStream;
import java.io.IOException;
import java.security.PrivilegedExceptionAction;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.protocol.ClientProtocol;
import org.apache.hadoop.security.UserGroupInformation;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class TestLease {
  static boolean hasLease(MiniDFSCluster cluster, Path src) {
    return cluster.getNameNode().namesystem.leaseManager.getLeaseByPath(src.toString()) != null;
  }
  
  final Path dir = new Path("/test/lease/");

  @Test
  public void testLease() throws Exception {
    Configuration conf = new Configuration();
    MiniDFSCluster cluster = new MiniDFSCluster(conf, 2, true, null);
    try {
      FileSystem fs = cluster.getFileSystem();
      Assert.assertTrue(fs.mkdirs(dir));
      
      Path a = new Path(dir, "a");
      Path b = new Path(dir, "b");

      DataOutputStream a_out = fs.create(a);
      a_out.writeBytes("something");

      Assert.assertTrue(hasLease(cluster, a));
      Assert.assertTrue(!hasLease(cluster, b));
      
      DataOutputStream b_out = fs.create(b);
      b_out.writeBytes("something");

      Assert.assertTrue(hasLease(cluster, a));
      Assert.assertTrue(hasLease(cluster, b));

      a_out.close();
      b_out.close();

      Assert.assertTrue(!hasLease(cluster, a));
      Assert.assertTrue(!hasLease(cluster, b));
      
      fs.delete(dir, true);
    } finally {
      if (cluster != null) {cluster.shutdown();}
    }
  }

  @Test
  public void testFactory() throws Exception {
    final String[] groups = new String[]{"supergroup"};
    final UserGroupInformation[] ugi = new UserGroupInformation[3];
    for(int i = 0; i < ugi.length; i++) {
      ugi[i] = UserGroupInformation.createUserForTesting("user" + i, groups);
    }

    final Configuration conf = new Configuration();
    final DFSClient c1 = createDFSClientAs(ugi[0], conf);
    FSDataOutputStream out1 = createFsOut(c1, "/out1");
    final DFSClient c2 = createDFSClientAs(ugi[0], conf);
    FSDataOutputStream out2 = createFsOut(c2, "/out2");
    Assert.assertEquals(c1.getLeaseRenewer(), c2.getLeaseRenewer());
    final DFSClient c3 = createDFSClientAs(ugi[1], conf);
    FSDataOutputStream out3 = createFsOut(c3, "/out3");
    Assert.assertTrue(c1.getLeaseRenewer() != c3.getLeaseRenewer());
    final DFSClient c4 = createDFSClientAs(ugi[1], conf);
    FSDataOutputStream out4 = createFsOut(c4, "/out4");
    Assert.assertEquals(c3.getLeaseRenewer(), c4.getLeaseRenewer());
    final DFSClient c5 = createDFSClientAs(ugi[2], conf);
    FSDataOutputStream out5 = createFsOut(c5, "/out5");
    Assert.assertTrue(c1.getLeaseRenewer() != c5.getLeaseRenewer());
    Assert.assertTrue(c3.getLeaseRenewer() != c5.getLeaseRenewer());
  }

  private FSDataOutputStream createFsOut(DFSClient dfs, String path)
      throws IOException {
    return new FSDataOutputStream(dfs.create(path, true), null);
  }
  
  static final ClientProtocol mcp = Mockito.mock(ClientProtocol.class);
  static public DFSClient createDFSClientAs(UserGroupInformation ugi, 
      final Configuration conf) throws Exception {
    return ugi.doAs(new PrivilegedExceptionAction<DFSClient>() {
      @Override
      public DFSClient run() throws Exception {
        return new DFSClient(null, mcp, conf, null);
      }
    });
  }
}
