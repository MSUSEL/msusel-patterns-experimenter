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


import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.examples.SleepJob;
import org.apache.hadoop.mapreduce.JobID;
import org.apache.hadoop.mapreduce.test.system.FinishTaskControlAction;
import org.apache.hadoop.mapreduce.test.system.JTProtocol;
import org.apache.hadoop.mapreduce.test.system.JobInfo;
import org.apache.hadoop.mapreduce.test.system.MRCluster;
import org.apache.hadoop.mapreduce.test.system.TTClient;
import org.apache.hadoop.mapreduce.test.system.TaskInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestControlledJob {
  private MRCluster cluster;

  private static final Log LOG = LogFactory.getLog(TestControlledJob.class);

  public TestControlledJob() throws Exception {
    cluster = MRCluster.createCluster(new Configuration());
  }

  @Before
  public void before() throws Exception {
    cluster.setUp();
  }

  @After
  public void after() throws Exception {
    cluster.tearDown();
  }
  
  @Test
  public void testControlledJob() throws Exception {
    Configuration conf = new Configuration(cluster.getConf());
    JTProtocol wovenClient = cluster.getJTClient().getProxy();
    FinishTaskControlAction.configureControlActionForJob(conf);
    SleepJob job = new SleepJob();
    job.setConf(conf);
    
    conf = job.setupJobConf(1, 0, 100, 100, 100, 100);
    JobClient client = cluster.getJTClient().getClient();
    
    RunningJob rJob = client.submitJob(new JobConf(conf));
    JobID id = rJob.getID();
    
    JobInfo jInfo = wovenClient.getJobInfo(id);
    
    while (jInfo.getStatus().getRunState() != JobStatus.RUNNING) {
      Thread.sleep(1000);
      jInfo = wovenClient.getJobInfo(id);
    }
    
    LOG.info("Waiting till job starts running one map");
    jInfo = wovenClient.getJobInfo(id);
    Assert.assertEquals(jInfo.runningMaps(), 1);
    
    LOG.info("waiting for another cycle to " +
    		"check if the maps dont finish off");
    Thread.sleep(1000);
    jInfo = wovenClient.getJobInfo(id);
    Assert.assertEquals(jInfo.runningMaps(), 1);
    
    TaskInfo[] taskInfos = wovenClient.getTaskInfo(id);
    
    for(TaskInfo info : taskInfos) {
      LOG.info("constructing control action to signal task to finish");
      FinishTaskControlAction action = new FinishTaskControlAction(
          TaskID.downgrade(info.getTaskID()));
      for(TTClient cli : cluster.getTTClients()) {
        cli.getProxy().sendAction(action);
      }
    }
    
    jInfo = wovenClient.getJobInfo(id);
    int i = 1;
    if (jInfo != null) {
      while (!jInfo.getStatus().isJobComplete()) {
        Thread.sleep(1000);
        jInfo = wovenClient.getJobInfo(id);
        if (jInfo == null) {
          break;
        }
        if(i > 40) {
          Assert.fail("Controlled Job with ID : "
              + jInfo.getID()
              + " has not completed in 40 seconds after signalling.");
        }
        i++;
      }
    }
    LOG.info("Job sucessfully completed after signalling!!!!");
  }
}
