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
package org.apache.hadoop.hdfs.server.datanode;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CommonConfigurationKeys;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.server.protocol.NamespaceInfo;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the version check the DN performs when connecting to the NN
 */
public class TestDataNodeVersionCheck {

  /**
   * Test the default DN version checking
   */
  @Test
  public void testDefaultVersionCheck() throws IOException {
    MiniDFSCluster cluster = null;
    try {
      Configuration conf = new Configuration();
      cluster = new MiniDFSCluster(conf, 1, true, null);

      DataNode dn = cluster.getDataNodes().get(0);
    
      final NamespaceInfo currInfo = new NamespaceInfo(0, 0, 0);
      assertTrue(dn.isPermittedVersion(currInfo));

      // Different revisions are not permitted
      NamespaceInfo infoDiffRev = new NamespaceInfo(0, 0, 0) {
                @Override public String getRevision() { return "bogus"; }
      };      
      assertFalse("Different revision is not permitted",
          dn.isPermittedVersion(infoDiffRev));

      // Different versions are not permitted
      NamespaceInfo infoDiffVersion = new NamespaceInfo(0, 0, 0) {
        @Override public String getVersion() { return "bogus"; }
        @Override public String getRevision() { return "bogus"; }
      };
      assertFalse("Different version is not permitted",
          dn.isPermittedVersion(infoDiffVersion));

      // A bogus version (matching revision but not version)
      NamespaceInfo bogusVersion = new NamespaceInfo(0, 0, 0) {
        @Override public String getVersion() { return "bogus"; }
      };
      try {
        dn.isPermittedVersion(bogusVersion);
        fail("Matched revision with mismatched version");
      } catch (AssertionError ae) {
        // Expected
      }
    } finally {
      if (cluster != null) {
        cluster.shutdown();
      }
    }
  }
  
  /**
   * Test the "relaxed" DN version checking
   */
  @Test
  public void testRelaxedVersionCheck() throws IOException {
    MiniDFSCluster cluster = null;
    try {
      Configuration conf = new Configuration();
      conf.setBoolean(
          CommonConfigurationKeys.HADOOP_RELAXED_VERSION_CHECK_KEY, true);
      cluster = new MiniDFSCluster(conf, 1, true, null);
      
      DataNode dn = cluster.getDataNodes().get(0);
    
      final NamespaceInfo currInfo = new NamespaceInfo(0, 0, 0);
      assertTrue(dn.isPermittedVersion(currInfo));

      // Different revisions are permitted
      NamespaceInfo infoDiffRev = new NamespaceInfo(0, 0, 0) {
        @Override public String getRevision() { return "bogus"; }
      };      
      assertTrue("Different revisions should be permitted",
          dn.isPermittedVersion(infoDiffRev));

      // Different versions are not permitted
      NamespaceInfo infoDiffVersion = new NamespaceInfo(0, 0, 0) {
        @Override public String getVersion() { return "bogus"; }
        @Override public String getRevision() { return "bogus"; }
      };
      assertFalse("Different version is not permitted",
          dn.isPermittedVersion(infoDiffVersion));
    } finally {
      if (cluster != null) {
        cluster.shutdown();
      }
    }
  }
}