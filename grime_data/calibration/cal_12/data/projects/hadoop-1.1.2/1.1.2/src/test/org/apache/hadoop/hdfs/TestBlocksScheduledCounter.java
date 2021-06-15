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
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DFSClient.DFSOutputStream;
import org.apache.hadoop.hdfs.server.namenode.DatanodeDescriptor;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import junit.framework.TestCase;

/**
 * This class tests DatanodeDescriptor.getBlocksScheduled() at the
 * NameNode. This counter is supposed to keep track of blocks currently
 * scheduled to a datanode.
 */
public class TestBlocksScheduledCounter extends TestCase {

  public void testBlocksScheduledCounter() throws IOException {
    
    MiniDFSCluster cluster = new MiniDFSCluster(new Configuration(), 1, 
                                                true, null);
    cluster.waitActive();
    FileSystem fs = cluster.getFileSystem();
    
    //open a file an write a few bytes:
    FSDataOutputStream out = fs.create(new Path("/testBlockScheduledCounter"));
    for (int i=0; i<1024; i++) {
      out.write(i);
    }
    // flush to make sure a block is allocated.
    ((DFSOutputStream)(out.getWrappedStream())).sync();
    
    ArrayList<DatanodeDescriptor> dnList = new ArrayList<DatanodeDescriptor>();
    cluster.getNameNode().namesystem.DFSNodesStatus(dnList, dnList);
    DatanodeDescriptor dn = dnList.get(0);
    
    assertEquals(1, dn.getBlocksScheduled());
   
    // close the file and the counter should go to zero.
    out.close();   
    assertEquals(0, dn.getBlocksScheduled());
  }
}
