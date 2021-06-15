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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.protocol.FSConstants;
import org.apache.hadoop.hdfs.protocol.LocatedBlock;
import org.apache.hadoop.hdfs.protocol.LocatedBlocks;
import org.apache.hadoop.hdfs.protocol.QuotaExceededException;
import org.apache.hadoop.hdfs.server.namenode.NameNode;
import org.apache.hadoop.util.StringUtils;

import static org.junit.Assert.*;

public class TestAbandonBlock extends junit.framework.TestCase {
  public static final Log LOG = LogFactory.getLog(TestAbandonBlock.class);
  
  private static final Configuration CONF = new Configuration();
  static final String FILE_NAME_PREFIX
      = "/" + TestAbandonBlock.class.getSimpleName() + "_"; 

  public void testAbandonBlock() throws IOException {
    MiniDFSCluster cluster = new MiniDFSCluster(CONF, 2, true, null);
    FileSystem fs = cluster.getFileSystem();

    String src = FILE_NAME_PREFIX + "foo";
    FSDataOutputStream fout = null;
    try {
      //start writing a a file but not close it
      fout = fs.create(new Path(src), true, 4096, (short)1, 512L);
      for(int i = 0; i < 1024; i++) {
        fout.write(123);
      }
      fout.sync();
  
      //try reading the block by someone
      final DFSClient dfsclient = new DFSClient(NameNode.getAddress(CONF), CONF);
      LocatedBlocks blocks = dfsclient.namenode.getBlockLocations(src, 0, 1);
      LocatedBlock b = blocks.get(0); 
      try {
        dfsclient.namenode.abandonBlock(b.getBlock(), src, "someone");
        //previous line should throw an exception.
        assertTrue(false);
      }
      catch(IOException ioe) {
        LOG.info("GREAT! " + StringUtils.stringifyException(ioe));
      }
    }
    finally {
      try{fout.close();} catch(Exception e) {}
      try{fs.close();} catch(Exception e) {}
      try{cluster.shutdown();} catch(Exception e) {}
    }
  }

  /** Make sure that the quota is decremented correctly when a block is abandoned */
  public void testQuotaUpdatedWhenBlockAbandoned() throws IOException {
    MiniDFSCluster cluster = new MiniDFSCluster(CONF, 2, true, null);
    FileSystem fs = cluster.getFileSystem();
    DistributedFileSystem dfs = (DistributedFileSystem)fs;

    try {
      // Setting diskspace quota to 3MB
      dfs.setQuota(new Path("/"), FSConstants.QUOTA_DONT_SET, 3 * 1024 * 1024);

      // Start writing a file with 2 replicas to ensure each datanode has one.
      // Block Size is 1MB.
      String src = FILE_NAME_PREFIX + "test_quota1";
      FSDataOutputStream fout = fs.create(new Path(src), true, 4096, (short)2, 1024 * 1024);
      for (int i = 0; i < 1024; i++) {
        fout.writeByte(123);
      }

      // Shutdown one datanode, causing the block abandonment.
      cluster.getDataNodes().get(0).shutdown();

      // Close the file, new block will be allocated with 2MB pending size.
      try {
        fout.close();
      } catch (QuotaExceededException e) {
        fail("Unexpected quota exception when closing fout");
      }
    } finally {
      try{fs.close();} catch(Exception e) {}
      try{cluster.shutdown();} catch(Exception e) {}
    }
  }
}
