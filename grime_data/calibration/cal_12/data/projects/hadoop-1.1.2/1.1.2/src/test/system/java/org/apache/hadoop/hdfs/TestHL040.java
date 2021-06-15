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
import java.util.Collection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.test.system.DNClient;
import org.apache.hadoop.hdfs.test.system.HDFSCluster;
import org.apache.hadoop.hdfs.test.system.NNClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.util.ajax.JSON;

public class TestHL040 {
  private HDFSCluster cluster = null;
  private static final Log LOG = LogFactory.getLog(TestHL040.class);

  public TestHL040() throws Exception {
  }

  @Before
  public void setupUp() throws Exception {
    cluster = HDFSCluster.createCluster(new Configuration());
    cluster.setUp();
  }

  @After
  public void tearDown() throws Exception {
    cluster.tearDown();
  }

  @Test
  public void testConnect() throws IOException {
    LOG.info("Staring TestHL040: connecting to the HDFSCluster ");
    LOG.info("================ Getting namenode info ================");
    NNClient dfsMaster = cluster.getNNClient();
    LOG.info("Process info of namenode " + dfsMaster.getHostName() + " is: " +
        dfsMaster.getProcessInfo());
    LOG.info("================ Getting datanode info ================");
    Collection<DNClient> clients = cluster.getDNClients();
    for (DNClient dnC : clients) {
      LOG.info("Process info of datanode " + dnC.getHostName() + " is: " +
          dnC.getProcessInfo());
      Assert.assertNotNull("Datanode process info isn't suppose to be null",
          dnC.getProcessInfo());
      LOG.info("Free space " + readAttr(dnC));
    }
  }

  private long readAttr(DNClient dnC) throws IOException {
    Object volObj = dnC.getDaemonAttribute("VolumeInfo");
    Assert.assertNotNull("Attribute value is expected to be not null", volObj);
    LOG.debug("Got object: " + volObj);
    Map volInfoMap = (Map) JSON.parse(volObj.toString());
    long totalFreeSpace = 0L;
    for (Object key : volInfoMap.keySet()) {
      Map attrMap = (Map) volInfoMap.get(key);
      long freeSpace = (Long) attrMap.get("freeSpace");
      totalFreeSpace += freeSpace;
    }
    return totalFreeSpace;
  }
}
