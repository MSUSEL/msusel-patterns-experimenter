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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.protocol.Block;
import org.apache.hadoop.hdfs.server.common.GenerationStamp;

import junit.framework.TestCase;

/**
 * Test if FSNamesystem handles heartbeat right
 */
public class TestComputeInvalidateWork extends TestCase {
  /**
   * Test if {@link FSNamesystem#computeInvalidateWork(int)}
   * can schedule invalidate work correctly 
   */
  public void testCompInvalidate() throws Exception {
    final Configuration conf = new Configuration();
    final int NUM_OF_DATANODES = 3;
    final MiniDFSCluster cluster = new MiniDFSCluster(conf, NUM_OF_DATANODES, true, null);
    try {
      cluster.waitActive();
      final FSNamesystem namesystem = cluster.getNameNode().getNamesystem();
      DatanodeDescriptor[] nodes =
        namesystem.heartbeats.toArray(new DatanodeDescriptor[NUM_OF_DATANODES]);
      assertEquals(nodes.length, NUM_OF_DATANODES);
      
      synchronized (namesystem) {
      for (int i=0; i<nodes.length; i++) {
        for(int j=0; j<3*namesystem.blockInvalidateLimit+1; j++) {
          Block block = new Block(i*(namesystem.blockInvalidateLimit+1)+j, 0, 
              GenerationStamp.FIRST_VALID_STAMP);
          namesystem.addToInvalidatesNoLog(block, nodes[i]);
        }
      }
      
      assertEquals(namesystem.blockInvalidateLimit*NUM_OF_DATANODES, 
          namesystem.computeInvalidateWork(NUM_OF_DATANODES+1));
      assertEquals(namesystem.blockInvalidateLimit*NUM_OF_DATANODES, 
          namesystem.computeInvalidateWork(NUM_OF_DATANODES));
      assertEquals(namesystem.blockInvalidateLimit*(NUM_OF_DATANODES-1), 
          namesystem.computeInvalidateWork(NUM_OF_DATANODES-1));
      int workCount = namesystem.computeInvalidateWork(1);
      if (workCount == 1) {
        assertEquals(namesystem.blockInvalidateLimit+1, 
            namesystem.computeInvalidateWork(2));        
      } else {
        assertEquals(workCount, namesystem.blockInvalidateLimit);
        assertEquals(2, namesystem.computeInvalidateWork(2));
      }
      }
    } finally {
      cluster.shutdown();
    }
  }
}
