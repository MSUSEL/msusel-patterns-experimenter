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

import junit.framework.TestCase;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.AppendTestUtil;
import org.apache.hadoop.hdfs.DFSTestUtil;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.protocol.Block;
import org.apache.hadoop.hdfs.protocol.LocatedBlocks;

import java.io.IOException;

public class TestDFSConcurrentFileOperations extends TestCase {

  MiniDFSCluster cluster;
  FileSystem fs;
  private int writeSize;
  private long blockSize;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    writeSize = 64 * 1024;
    blockSize = 2 * writeSize;
  }

  private void init() throws IOException {
    init(new Configuration());
  }

  private void init(Configuration conf) throws IOException {
    cluster = new MiniDFSCluster(conf, 3, true, new String[]{"/rack1", "/rack2", "/rack1"});
    cluster.waitClusterUp();
    fs = cluster.getFileSystem();
  }

  @Override
  protected void tearDown() throws Exception {
    fs.close();
    cluster.shutdown();
    super.tearDown();
  }

  /*
   * test case: 
   * 1. file is opened
   * 2. file is moved while being written to (including move to trash on delete)
   * 3. blocks complete and are finalized
   * 4. close fails
   * 5. lease recovery tries to finalize blocks and should succeed
   */
  public void testLeaseRecoveryOnTrashedFile() throws Exception {
    Configuration conf = new Configuration();
    
    conf.setLong("dfs.block.size", blockSize);
    conf.setBoolean("dfs.support.broken.append", true);
    
    init(conf);
    
    String src = "/file-1";
    String dst = "/file-2";
    Path srcPath = new Path(src);
    Path dstPath = new Path(dst);
    FSDataOutputStream fos = fs.create(srcPath);
   
    AppendTestUtil.write(fos, 0, writeSize);
    fos.sync();
    
    // renaming a file out from under a client will cause close to fail
    // and result in the lease remaining while the blocks are finalized on
    // the DNs
    fs.rename(srcPath, dstPath);

    try {
      fos.close();
      fail("expected IOException");
    } catch (IOException e) {
      //expected
    }

    FileSystem fs2 = AppendTestUtil.createHdfsWithDifferentUsername(conf);
    AppendTestUtil.recoverFile(cluster, fs2, dstPath);
    AppendTestUtil.check(fs2, dstPath, writeSize);
  }
}
