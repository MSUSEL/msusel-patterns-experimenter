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

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.mapred.ControlledMapReduceJob.ControlledMapReduceJobRunner;

/**
 * Test to verify the controlled behavior of a ControlledMapReduceJob.
 * 
 */
public class TestControlledMapReduceJob extends ClusterMapReduceTestCase {
  static final Log LOG = LogFactory.getLog(TestControlledMapReduceJob.class);

  /**
   * Starts a job with 5 maps and 5 reduces. Then controls the finishing of
   * tasks. Signals finishing tasks in batches and then verifies their
   * completion.
   * 
   * @throws Exception
   */
  public void testControlledMapReduceJob()
      throws Exception {

    Properties props = new Properties();
    props.setProperty("mapred.tasktracker.map.tasks.maximum", "2");
    props.setProperty("mapred.tasktracker.reduce.tasks.maximum", "2");
    startCluster(true, props);
    LOG.info("Started the cluster");

    ControlledMapReduceJobRunner jobRunner =
        ControlledMapReduceJobRunner
            .getControlledMapReduceJobRunner(createJobConf(), 7, 6);
    jobRunner.start();
    ControlledMapReduceJob controlledJob = jobRunner.getJob();
    JobInProgress jip =
        getMRCluster().getJobTrackerRunner().getJobTracker().getJob(
            jobRunner.getJobID());

    ControlledMapReduceJob.waitTillNTasksStartRunning(jip, true, 4);
    LOG.info("Finishing 3 maps");
    controlledJob.finishNTasks(true, 3);
    ControlledMapReduceJob.waitTillNTotalTasksFinish(jip, true, 3);

    ControlledMapReduceJob.waitTillNTasksStartRunning(jip, true, 4);
    LOG.info("Finishing 4 more maps");
    controlledJob.finishNTasks(true, 4);
    ControlledMapReduceJob.waitTillNTotalTasksFinish(jip, true, 7);

    ControlledMapReduceJob.waitTillNTasksStartRunning(jip, false, 4);
    LOG.info("Finishing 2 reduces");
    controlledJob.finishNTasks(false, 2);
    ControlledMapReduceJob.waitTillNTotalTasksFinish(jip, false, 2);

    ControlledMapReduceJob.waitTillNTasksStartRunning(jip, false, 4);
    LOG.info("Finishing 4 more reduces");
    controlledJob.finishNTasks(false, 4);
    ControlledMapReduceJob.waitTillNTotalTasksFinish(jip, false, 6);

    jobRunner.join();
  }
}
