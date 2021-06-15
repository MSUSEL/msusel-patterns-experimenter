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
package org.apache.hadoop.mapred;


import java.util.Hashtable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.test.system.MRCluster;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestPushConfig {
  private static MRCluster cluster;
  private static final Log LOG = LogFactory.getLog(
      TestPushConfig.class.getName());
  
  @BeforeClass
  public static void before() throws Exception {
    String [] expExcludeList = new String[2];
    expExcludeList[0] = "java.net.ConnectException";
    expExcludeList[1] = "java.io.IOException";
    
    cluster = MRCluster.createCluster(new Configuration());
    cluster.setExcludeExpList(expExcludeList);
    cluster.setUp();
  }

  @AfterClass
  public static void after() throws Exception {
    cluster.tearDown();
  }
  
  /**
   * This test about testing the pushConfig feature. The pushConfig functionality
   * available as part of the cluster process manager. The functionality takes
   * in local input directory and pushes all the files from the local to the 
   * remote conf directory. This functionality is required is change the config
   * on the fly and restart the cluster which will be used by other test cases
   * @throws Exception if an I/O error occurs.
   */
  @Test
  public void testPushConfig() throws Exception {
    final String DUMMY_CONFIG_STRING = "mapred.newdummy.conf";
    String confFile = "mapred-site.xml";
    Hashtable<String,Long> prop = new Hashtable<String,Long>();
    prop.put(DUMMY_CONFIG_STRING, 1L);
    Configuration daemonConf =  cluster.getJTClient().getProxy().getDaemonConf();
    Assert.assertTrue("Dummy varialble is expected to be null before restart.",
        daemonConf.get(DUMMY_CONFIG_STRING) == null);
    cluster.restartClusterWithNewConfig(prop, confFile);    
    Configuration newconf = cluster.getJTClient().getProxy().getDaemonConf();
    Assert.assertTrue("Extra varialble is expected to be set",
        newconf.get(DUMMY_CONFIG_STRING).equals("1"));
    cluster.restart();
    daemonConf = cluster.getJTClient().getProxy().getDaemonConf();
    Assert.assertTrue("Dummy variable is expected to be null after restart.",
        daemonConf.get(DUMMY_CONFIG_STRING) == null);   
  }  
 }
