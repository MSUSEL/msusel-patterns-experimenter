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

import junit.framework.TestCase;

import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.Level;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.TestFileAppend4.DelayAnswer;
import org.apache.hadoop.hdfs.server.datanode.DataNode;
import org.apache.hadoop.hdfs.server.datanode.metrics.DataNodeInstrumentation;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

/**
 * This class tests the building blocks that are needed to
 * support HDFS appends.
 */
public class TestStuckDataNode extends TestCase {
  {
    ((Log4JLogger)DataNode.LOG).getLogger().setLevel(Level.ALL);
    ((Log4JLogger)DFSClient.LOG).getLogger().setLevel(Level.ALL);
  }

  /** This creates a slow writer and check to see
   * if pipeline heartbeats work fine
   */
  public void testStuckDataNode() throws Exception {
    final int DATANODE_NUM = 3;
    Configuration conf = new Configuration();
    final int timeout = 8000;
    conf.setInt("dfs.socket.timeout",timeout);

    final Path p = new Path("/pipelineHeartbeat/foo");
    System.out.println("p=" + p);

    MiniDFSCluster cluster = new MiniDFSCluster(conf, DATANODE_NUM, true, null);
    DistributedFileSystem fs = (DistributedFileSystem)cluster.getFileSystem();

    DataNodeInstrumentation metrics = spy(cluster.getDataNodes().get(0).myMetrics);    
    DelayAnswer delayAnswer = new DelayAnswer(); 
    doAnswer(delayAnswer).when(metrics).incrBytesWritten(anyInt());

    try {
    	// create a new file.
    	FSDataOutputStream stm = fs.create(p);
    	stm.write(1);
    	stm.sync();
    	stm.write(2);
    	stm.close();

    	// verify that entire file is good
    	FSDataInputStream in = fs.open(p);
    	assertEquals(1, in.read());
    	assertEquals(2, in.read());
    	in.close();
    } finally {
      fs.close();
      cluster.shutdown();
    }
  }

}
