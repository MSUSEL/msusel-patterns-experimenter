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
package org.apache.hadoop.hdfs.server.namenode.web.resources;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DFSConfigKeys;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.hdfs.protocol.LocatedBlock;
import org.apache.hadoop.hdfs.protocol.LocatedBlocks;
import org.apache.hadoop.hdfs.server.datanode.DataNode;
import org.apache.hadoop.hdfs.server.namenode.FSNamesystem;
import org.apache.hadoop.hdfs.server.namenode.LeaseManager;
import org.apache.hadoop.hdfs.server.namenode.NameNode;
import org.apache.hadoop.hdfs.server.namenode.NameNodeAdapter;
import org.apache.hadoop.hdfs.web.WebHdfsTestUtil;
import org.apache.hadoop.hdfs.web.resources.GetOpParam;
import org.apache.hadoop.hdfs.web.resources.PostOpParam;
import org.apache.hadoop.hdfs.web.resources.PutOpParam;
import org.apache.log4j.Level;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test WebHDFS which provides data locality using HTTP redirection.
 */
public class TestWebHdfsDataLocality {
  static final Log LOG = LogFactory.getLog(TestWebHdfsDataLocality.class);
  {
    ((Log4JLogger)NameNode.stateChangeLog).getLogger().setLevel(Level.OFF);
    ((Log4JLogger)LeaseManager.LOG).getLogger().setLevel(Level.OFF);
    ((Log4JLogger)LogFactory.getLog(FSNamesystem.class)).getLogger().setLevel(Level.OFF);
  }
  
  private static final String RACK0 = "/rack0";
  private static final String RACK1 = "/rack1";
  private static final String RACK2 = "/rack2";

  @Test
  public void testDataLocality() throws Exception {
    final Configuration conf = WebHdfsTestUtil.createConf();
    final String[] racks = {RACK0, RACK0, RACK1, RACK1, RACK2, RACK2};
    final int nDataNodes = racks.length;
    LOG.info("nDataNodes=" + nDataNodes + ", racks=" + Arrays.asList(racks));

    final MiniDFSCluster cluster = new MiniDFSCluster(
        conf, nDataNodes, true, racks);
    try {
      cluster.waitActive();

      final DistributedFileSystem dfs = (DistributedFileSystem)cluster.getFileSystem();
      final NameNode namenode = cluster.getNameNode();
      final FSNamesystem ns = namenode.getNamesystem();
      LOG.info("ns=" + ns);
  
      final long blocksize = DFSConfigKeys.DFS_BLOCK_SIZE_DEFAULT;
      final String f = "/foo";

      { //test CREATE
        for(int i = 0; i < nDataNodes; i++) {
          //set client address to a particular datanode
          final DataNode dn = cluster.getDataNodes().get(i);
          final String host = ns.getDatanode(dn.dnRegistration).getHost();
          NamenodeWebHdfsMethods.setRemoteAddress(host);

          //The chosen datanode must be the same as the client address
          final DatanodeInfo chosen = NamenodeWebHdfsMethods.chooseDatanode(
              namenode, f, PutOpParam.Op.CREATE, -1L, blocksize);
          Assert.assertEquals(host, chosen.getHost());
        }
      }
  
      //create a file with one replica.
      final Path p = new Path(f);
      final FSDataOutputStream out = dfs.create(p, (short)1);
      out.write(1);
      out.close();
  
      //get replica location.
      final LocatedBlocks locatedblocks = NameNodeAdapter.getBlockLocations(
          namenode, f, 0, 1);
      final List<LocatedBlock> lb = locatedblocks.getLocatedBlocks();
      Assert.assertEquals(1, lb.size());
      final DatanodeInfo[] locations = lb.get(0).getLocations();
      Assert.assertEquals(1, locations.length);
      final DatanodeInfo expected = locations[0];
      
      //For GETFILECHECKSUM, OPEN and APPEND,
      //the chosen datanode must be the same as the replica location.

      { //test GETFILECHECKSUM
        final DatanodeInfo chosen = NamenodeWebHdfsMethods.chooseDatanode(
            namenode, f, GetOpParam.Op.GETFILECHECKSUM, -1L, blocksize);
        Assert.assertEquals(expected, chosen);
      }
  
      { //test OPEN
        final DatanodeInfo chosen = NamenodeWebHdfsMethods.chooseDatanode(
            namenode, f, GetOpParam.Op.OPEN, 0, blocksize);
        Assert.assertEquals(expected, chosen);
      }

      { //test APPEND
        final DatanodeInfo chosen = NamenodeWebHdfsMethods.chooseDatanode(
            namenode, f, PostOpParam.Op.APPEND, -1L, blocksize);
        Assert.assertEquals(expected, chosen);
      }
    } finally {
      cluster.shutdown();
    }
  }
}