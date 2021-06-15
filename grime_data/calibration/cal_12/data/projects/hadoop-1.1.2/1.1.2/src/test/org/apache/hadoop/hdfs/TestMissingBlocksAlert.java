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
import java.net.InetSocketAddress;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.ChecksumException;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import junit.framework.TestCase;

/**
 * The test makes sure that NameNode detects presense blocks that do not have
 * any valid replicas. In addition, it verifies that HDFS front page displays
 * a warning in such a case.
 */
public class TestMissingBlocksAlert extends TestCase {
  
  private static final Log LOG = 
                           LogFactory.getLog(TestMissingBlocksAlert.class);
  
  public void testMissingBlocksAlert() throws IOException, 
                                       InterruptedException {
    
    MiniDFSCluster cluster = null;
    
    try {
      Configuration conf = new Configuration();
      //minimize test delay
      conf.setInt("dfs.replication.interval", 0);
      int fileLen = 10*1024;

      //start a cluster with single datanode
      cluster = new MiniDFSCluster(conf, 1, true, null);
      cluster.waitActive();

      DistributedFileSystem dfs = 
                            (DistributedFileSystem) cluster.getFileSystem();

      // create a normal file
      DFSTestUtil.createFile(dfs, new Path("/testMissingBlocksAlert/file1"), 
                             fileLen, (short)3, 0);

      Path corruptFile = new Path("/testMissingBlocks/corruptFile");
      DFSTestUtil.createFile(dfs, corruptFile, fileLen, (short)3, 0);


      // Corrupt the block
      String block = DFSTestUtil.getFirstBlock(dfs, corruptFile).getBlockName();
      TestDatanodeBlockScanner.corruptReplica(block, 0);

      // read the file so that the corrupt block is reported to NN
      FSDataInputStream in = dfs.open(corruptFile); 
      try {
        in.readFully(new byte[fileLen]);
      } catch (ChecksumException ignored) { // checksum error is expected.      
      }
      in.close();

      LOG.info("Waiting for missing blocks count to increase...");

      while (dfs.getMissingBlocksCount() <= 0) {
        Thread.sleep(100);
      }
      assertTrue(dfs.getMissingBlocksCount() == 1);


      // Now verify that it shows up on webui
      URL url = new URL("http://" + conf.get("dfs.http.address") + 
                        "/dfshealth.jsp");
      String dfsFrontPage = DFSTestUtil.urlGet(url);
      String warnStr = "WARNING : There are about ";
      assertTrue("HDFS Front page does not contain expected warning", 
                 dfsFrontPage.contains(warnStr + "1 missing blocks"));

      // now do the reverse : remove the file expect the number of missing 
      // blocks to go to zero

      dfs.delete(corruptFile, true);

      LOG.info("Waiting for missing blocks count to be zero...");
      while (dfs.getMissingBlocksCount() > 0) {
        Thread.sleep(100);
      }

      // and make sure WARNING disappears
      // Now verify that it shows up on webui
      dfsFrontPage = DFSTestUtil.urlGet(url);
      assertFalse("HDFS Front page contains unexpected warning", 
                  dfsFrontPage.contains(warnStr));
    } finally {
      if (cluster != null) {
        cluster.shutdown();
      }
    }
  }
}
