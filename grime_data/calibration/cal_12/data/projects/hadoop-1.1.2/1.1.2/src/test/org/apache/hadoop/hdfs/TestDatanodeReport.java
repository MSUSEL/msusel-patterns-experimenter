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
/**
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.apache.hadoop.hdfs;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import junit.framework.TestCase;
import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.hdfs.protocol.FSConstants.DatanodeReportType;
import org.apache.hadoop.hdfs.server.datanode.DataNode;

/**
 * This test ensures the all types of data node report work correctly.
 */
public class TestDatanodeReport extends TestCase {
  final static private Configuration conf = new Configuration();
  final static private int NUM_OF_DATANODES = 4;
    
  /**
   * This test attempts to different types of datanode report.
   */
  public void testDatanodeReport() throws Exception {
    conf.setInt(
        "heartbeat.recheck.interval", 500); // 0.5s
    MiniDFSCluster cluster = 
      new MiniDFSCluster(conf, NUM_OF_DATANODES, true, null);
    try {
      //wait until the cluster is up
      cluster.waitActive();

      InetSocketAddress addr = new InetSocketAddress("localhost",
          cluster.getNameNodePort());
      DFSClient client = new DFSClient(addr, conf);

      assertEquals(client.datanodeReport(DatanodeReportType.ALL).length,
                   NUM_OF_DATANODES);
      assertEquals(client.datanodeReport(DatanodeReportType.LIVE).length,
                   NUM_OF_DATANODES);
      assertEquals(client.datanodeReport(DatanodeReportType.DEAD).length, 0);

      // bring down one datanode
      ArrayList<DataNode> datanodes = cluster.getDataNodes();
      datanodes.remove(datanodes.size()-1).shutdown();

      DatanodeInfo[] nodeInfo = client.datanodeReport(DatanodeReportType.DEAD);
      while (nodeInfo.length != 1) {
        try {
          Thread.sleep(500);
        } catch (Exception e) {
        }
        nodeInfo = client.datanodeReport(DatanodeReportType.DEAD);
      }

      assertEquals(client.datanodeReport(DatanodeReportType.LIVE).length,
                   NUM_OF_DATANODES-1);
      assertEquals(client.datanodeReport(DatanodeReportType.ALL).length,
                   NUM_OF_DATANODES);
    }finally {
      cluster.shutdown();
    }
  }
 
  public static void main(String[] args) throws Exception {
    new TestDatanodeReport().testDatanodeReport();
  }
  
}


