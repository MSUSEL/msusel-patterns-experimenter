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

import java.lang.management.ManagementFactory;
import java.util.List;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.conf.Configuration;
import org.junit.Test;
import junit.framework.Assert;

/**
 * Class for testing {@link DataNodeMXBean} implementation
 */
public class TestDataNodeMXBean {
  @Test
  public void testDataNodeMXBean() throws Exception {
    Configuration conf = new Configuration();
    MiniDFSCluster cluster = new MiniDFSCluster(conf, 1, true, null);

    try {
      List<DataNode> datanodes = cluster.getDataNodes();
      Assert.assertEquals(datanodes.size(), 1);
      DataNode datanode = datanodes.get(0);

      MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
      ObjectName mxbeanName = new ObjectName(
          "Hadoop:service=DataNode,name=DataNodeInfo");
      // get attribute "HostName"
      String hostname = (String) mbs.getAttribute(mxbeanName, "HostName");
      Assert.assertEquals(datanode.getHostName(), hostname);
      // get attribute "Version"
      String version = (String)mbs.getAttribute(mxbeanName, "Version");
      Assert.assertEquals(datanode.getVersion(),version);
      // get attribute "RpcPort"
      String rpcPort = (String)mbs.getAttribute(mxbeanName, "RpcPort");
      Assert.assertEquals(datanode.getRpcPort(),rpcPort);
      // get attribute "HttpPort"
      String httpPort = (String)mbs.getAttribute(mxbeanName, "HttpPort");
      Assert.assertEquals(datanode.getHttpPort(),httpPort);
      // get attribute "NamenodeAddress"
      String namenodeAddress = (String)mbs.getAttribute(mxbeanName, 
          "NamenodeAddress");
      Assert.assertEquals(datanode.getNamenodeAddress(),namenodeAddress);
      // get attribute "getVolumeInfo"
      String volumeInfo = (String)mbs.getAttribute(mxbeanName, "VolumeInfo");
      Assert.assertEquals(datanode.getVolumeInfo(),volumeInfo);
    } finally {
      if (cluster != null) {cluster.shutdown();}
    }
  }
}
