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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Properties;

import org.apache.hadoop.examples.SleepJob;
import org.junit.Test;


public class TestCapacitySchedulerWithJobTracker extends
    ClusterWithCapacityScheduler {

  /**
   * Test case which checks if the jobs which fail initialization are removed
   * from the {@link CapacityTaskScheduler} waiting queue.
   * 
   * @throws Exception
   */
  @Test
  public void testFailingJobInitalization() throws Exception {
    Properties schedulerProps = new Properties();
    schedulerProps.put("mapred.capacity-scheduler.queue.default.capacity",
        "100");
    Properties clusterProps = new Properties();
    clusterProps.put("mapred.tasktracker.map.tasks.maximum", String.valueOf(1));
    clusterProps.put("mapred.tasktracker.reduce.tasks.maximum", String
        .valueOf(1));
    clusterProps.put("mapred.jobtracker.maxtasks.per.job", String.valueOf(1));
    // cluster capacity 1 maps, 1 reduces
    startCluster(1, clusterProps, schedulerProps);
    JobConf conf = getJobConf();
    conf.setSpeculativeExecution(false);
    conf.set("mapred.committer.job.setup.cleanup.needed", "false");
    conf.setNumTasksToExecutePerJvm(-1);
    SleepJob sleepJob = new SleepJob();
    sleepJob.setConf(conf);
    JobConf job = sleepJob.setupJobConf(3, 3, 1, 1, 1, 1);
    RunningJob rjob;
    try {
      rjob = runJob(job, false);
      fail("The job should have thrown Exception");
    } catch (Exception e) {
      CapacityTaskScheduler scheduler = (CapacityTaskScheduler) getJobTracker()
          .getTaskScheduler();
      JobQueuesManager mgr = scheduler.jobQueuesManager;
      assertEquals("Failed job present in Waiting queue", 0, mgr
          .getQueue("default").getNumWaitingJobs());
    }
  }

  /**
   * Test case which checks {@link JobTracker} and {@link CapacityTaskScheduler}
   * 
   * Test case submits 2 jobs in two different capacity scheduler queues. And
   * checks if the jobs successfully complete.
   * 
   * @throws Exception
   */
  @Test
  public void testJobTrackerIntegration() throws Exception {

    Properties schedulerProps = new Properties();
    String[] queues = new String[] { "Q1", "Q2" };
    RunningJob jobs[] = new RunningJob[2];
    for (String q : queues) {
      schedulerProps.put(CapacitySchedulerConf
          .toFullPropertyName(q, "capacity"), "50");
      schedulerProps.put(CapacitySchedulerConf.toFullPropertyName(q,
          "minimum-user-limit-percent"), "100");
    }

    Properties clusterProps = new Properties();
    clusterProps.put("mapred.tasktracker.map.tasks.maximum", String.valueOf(2));
    clusterProps.put("mapred.tasktracker.reduce.tasks.maximum", String
        .valueOf(2));
    clusterProps.put("mapred.queue.names", queues[0] + "," + queues[1]);
    startCluster(2, clusterProps, schedulerProps);

    JobConf conf = getJobConf();
    conf.setSpeculativeExecution(false);
    conf.set("mapred.committer.job.setup.cleanup.needed", "false");
    conf.setNumTasksToExecutePerJvm(-1);
    conf.setQueueName(queues[0]);
    SleepJob sleepJob1 = new SleepJob();
    sleepJob1.setConf(conf);
    JobConf sleepJobConf = sleepJob1.setupJobConf(1, 1, 1, 1, 1, 1);
    jobs[0] = runJob(sleepJobConf, true);

    JobConf conf2 = getJobConf();
    conf2.setSpeculativeExecution(false);
    conf2.set("mapred.committer.job.setup.cleanup.needed", "false");
    conf2.setNumTasksToExecutePerJvm(-1);
    conf2.setQueueName(queues[1]);
    SleepJob sleepJob2 = new SleepJob();
    sleepJob2.setConf(conf2);
    JobConf sleep2 = sleepJob2.setupJobConf(3, 3, 5, 3, 5, 3);
    jobs[1] = runJob(sleep2, false);
    assertTrue("Sleep job submitted to queue 1 is not successful", jobs[0]
        .isSuccessful());
    assertTrue("Sleep job submitted to queue 2 is not successful", jobs[1]
        .isSuccessful());
  }

  private RunningJob runJob(JobConf conf, boolean inBackGround)
      throws IOException {
    if (!inBackGround) {
      RunningJob rjob = JobClient.runJob(conf);
      return rjob;
    } else {
      RunningJob rJob = new JobClient(conf).submitJob(conf);
      return rJob;
    }
  }
}
