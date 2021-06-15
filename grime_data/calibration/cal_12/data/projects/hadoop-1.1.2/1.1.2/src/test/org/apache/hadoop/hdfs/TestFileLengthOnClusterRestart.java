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

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DFSClient.DFSDataInputStream;
import org.apache.hadoop.hdfs.protocol.FSConstants.SafeModeAction;
import org.junit.Assert;
import org.junit.Test;

/** Test the fileLength on cluster restarts */
public class TestFileLengthOnClusterRestart {
  /**
   * Tests the fileLength when we sync the file and restart the cluster and
   * Datanodes not report to Namenode yet.
   */
  @Test(timeout = 60000)
  public void testFileLengthWithHSyncAndClusterRestartWithOutDNsRegister()
      throws Exception {
    final Configuration conf = new Configuration();
    // create cluster
    conf.setInt(DFSConfigKeys.DFS_BLOCK_SIZE_KEY, 512);

    final MiniDFSCluster cluster = new MiniDFSCluster(conf, 2, true, null);
    DFSDataInputStream in = null;
    FSDataOutputStream out = null;
    DistributedFileSystem dfs = null;
    try {
      Path path = new Path(MiniDFSCluster.getBaseDir().getPath(), "test");
      dfs = (DistributedFileSystem) cluster.getFileSystem();
      out = dfs.create(path);
      int fileLength = 1030;
      out.write(new byte[fileLength]);
      out.sync();
      cluster.restartNameNode();
      cluster.waitActive();
      in = (DFSDataInputStream) dfs.open(path, 1024);
      // Verify the length when we just restart NN. DNs will register
      // immediately.
      Assert.assertEquals(fileLength, in.getVisibleLength());
      cluster.shutdownDataNodes();
      cluster.restartNameNode(false);
      // This is just for ensuring NN started.
      verifyNNIsInSafeMode(dfs);

      try {
        in = (DFSDataInputStream) dfs.open(path);
        Assert.fail("Expected IOException");
      } catch (IOException e) {
        Assert.assertTrue(e.getLocalizedMessage().indexOf(
            "Name node is in safe mode") >= 0);
      }

    } finally {
      if (null != in) {
        in.close();
      }
      if (null != dfs) {
        dfs.dfs.clientRunning = false;
      }
      cluster.shutdown();
    }
  }

  private void verifyNNIsInSafeMode(DistributedFileSystem dfs)
      throws IOException {
    while (true) {
      try {
        if (dfs.dfs.namenode.setSafeMode(SafeModeAction.SAFEMODE_GET)) {
          return;
        } else {
          throw new IOException("Expected to be in SafeMode");
        }
      } catch (IOException e) {
        // NN might not started completely Ignore
      }
    }
  }
}
