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

import java.util.Collection;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.hadoop.mapreduce.test.system.JTProtocol;
import org.apache.hadoop.mapreduce.test.system.TTClient;
import org.apache.hadoop.mapreduce.test.system.JobInfo;
import org.apache.hadoop.mapreduce.test.system.TaskInfo;
import org.apache.hadoop.mapreduce.test.system.MRCluster;

import org.apache.hadoop.mapreduce.test.system.FinishTaskControlAction;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.examples.SleepJob;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * This tests when successful and failed jobs are retired,
 * their jobInProgress object are removed properly. 
 */

public class TestRetiredJobs {

  private static MRCluster cluster = null;
  private static JobClient jobClient = null;
  private static int retiredJobInterval = 0;
  static final Log LOG = LogFactory.getLog(TestRetiredJobs.class);

  public TestRetiredJobs() throws Exception {
  }

  @BeforeClass
  public static void setUp() throws Exception {
    cluster = MRCluster.createCluster(new Configuration());
    cluster.setUp();
    jobClient = cluster.getJTClient().getClient();
  }

  @AfterClass
  public static void tearDown() throws Exception {
    cluster.tearDown();
  }

  @Test
  /**
   * This tests when successful and failed jobs are retired, 
   * their jobInProgress object are removed properly.
   * This is verified by checking whether getJobInfo
   * method returns a JobInfo object when running,
   * and whether getJobInfo method returns null
   * after job is retired. 
   * @param none
   * @return void
   */
  public void testRetiredJobsSuccessful() throws Exception {
    Configuration conf = new Configuration(cluster.getConf());
    conf.setBoolean("mapreduce.job.complete.cancel.delegation.tokens", false);
    JTProtocol remoteJTClient = cluster.getJTClient().getProxy();
    int testLoopCount = 0;

    //First run a successful job and verify if JobInProgress
    //object is removed by checking the getJobInfo. In the
    //second iteration, verify if a killed job JobInProgress
    //is removed.
    do {
      testLoopCount++;
      SleepJob job = new SleepJob();
      job.setConf(conf);
      conf = job.setupJobConf(5, 1, 100, 100, 100, 100);
      //Get the value of mapred.jobtracker.retirejob.check. If not
      //found then use 60000 milliseconds, which is the application default.
      retiredJobInterval =
        conf.getInt("mapred.jobtracker.retirejob.check", 60000);
      //Assert if retiredJobInterval is 0
      if ( retiredJobInterval == 0 ) {
        Assert.fail("mapred.jobtracker.retirejob.check is 0");
      }

      JobConf jconf = new JobConf(conf);
      //Controls the job till all verification is done 
      FinishTaskControlAction.configureControlActionForJob(conf);
      //Submitting the job
      RunningJob rJob = cluster.getJTClient().getClient().submitJob(jconf);

      JobID jobID = rJob.getID();

      JobInfo jInfo = remoteJTClient.getJobInfo(jobID);
      LOG.info("jInfo is :" + jInfo);

      boolean jobStarted = cluster.getJTClient().isJobStarted(jobID);

      Assert.assertTrue("Job has not started even after a minute", 
          jobStarted );

      LOG.info("job id is :" + jobID.toString());

      TaskInfo[] taskInfos = cluster.getJTClient().getProxy()
          .getTaskInfo(jobID);

      // getJobInfo method should
      // return a JobInProgress object when running,
      JobInfo jobInfo = cluster.getJTClient().getProxy()
          .getJobInfo(jobID);

      Assert.assertNotNull("The Job information is not present ", jobInfo); 

      //Allow the job to continue through MR control job.
      for (TaskInfo taskInfoRemaining : taskInfos) {
        FinishTaskControlAction action = new FinishTaskControlAction(TaskID
            .downgrade(taskInfoRemaining.getTaskID()));
        Collection<TTClient> tts = cluster.getTTClients();
        for (TTClient cli : tts) {
          cli.getProxy().sendAction(action);
        }
      }

      //Killing this job will happen only in the second iteration.
      if (testLoopCount == 2) {
        //Killing the job because all the verification needed
        //for this testcase is completed.
        rJob.killJob();
      }

      //Making sure that the job is complete.
      int count = 0;
      while (jInfo != null && !jInfo.getStatus().isJobComplete()) {
        UtilsForTests.waitFor(10000);
        count++;
        jInfo = remoteJTClient.getJobInfo(rJob.getID());
        //If the count goes more than 100 seconds, then fail; This is to
        //avoid infinite loop
        if (count > 10) {
          Assert.fail("Since the job has not completed even after" +
              " 100 seconds, failing at this point");
        }
      }

      //Waiting for a specific period of time for retire thread
      //to be called taking into consideration the network issues
      if (retiredJobInterval > 40000) {
        UtilsForTests.waitFor(retiredJobInterval * 2);
      } else {
        UtilsForTests.waitFor(retiredJobInterval * 4);
      }

      jobInfo = null; 
      // getJobInfo method should return null
      // after job is retired. JobInProgress
      // object should not be present.
      jobInfo = cluster.getJTClient().getProxy()
          .getJobInfo(jobID);

      Assert.assertNull("Job information is still available " + 
          "after retirement of job ", jobInfo);

    } while (testLoopCount < 2);
  }
}
