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

import static org.junit.Assert.assertTrue;

import java.util.Properties;
import org.apache.hadoop.mapred.ControlledMapReduceJob.ControlledMapReduceJobRunner;
import org.junit.*;

/**UNTIL MAPREDUCE-873 is backported, we will not run recovery manager tests
 */
@Ignore
public class TestJobTrackerRestartWithCS extends ClusterWithCapacityScheduler {

  /**
   * Test single queue.
   *
   * <p>
   *
   * Submit a job with more M/R tasks than total capacity. Full queue capacity
   * should be utilized and remaining M/R tasks should wait for slots to be
   * available.
   *
   * @throws Exception
   */
  @Test
  public void testJobTrackerRestartWithCS()
          throws Exception {
    try {
      Properties schedulerProps = new Properties();
      schedulerProps.put(
              "mapred.capacity-scheduler.queue.default.guaranteed-capacity", "100");
      Properties clusterProps = new Properties();
      clusterProps.put("mapred.tasktracker.map.tasks.maximum", String.valueOf(2));
      clusterProps.put("mapred.tasktracker.reduce.tasks.maximum", String.valueOf(0));

      // cluster capacity 2 maps, 0 reduces
      startCluster(1, clusterProps, schedulerProps);

      ControlledMapReduceJobRunner jobRunner =
              ControlledMapReduceJobRunner.getControlledMapReduceJobRunner(
              getJobConf(), 4, 0);
      jobRunner.start();
      ControlledMapReduceJob controlledJob = jobRunner.getJob();
      JobID myJobID = jobRunner.getJobID();
      JobInProgress myJob = getJobTracker().getJob(myJobID);
      ControlledMapReduceJob.waitTillNTasksStartRunning(myJob, true, 2);

      LOG.info("Trying to finish 2 maps");
      controlledJob.finishNTasks(true, 2);
      ControlledMapReduceJob.waitTillNTotalTasksFinish(myJob, true, 2);
      assertTrue("Number of maps finished", myJob.finishedMaps() == 2);

      JobClient jobClient = new JobClient(getMrCluster().createJobConf());
      getMrCluster().stopJobTracker();

      getMrCluster().getJobTrackerConf().setBoolean("mapred.jobtracker.restart.recover",
              true);
      getMrCluster().startJobTracker();

      UtilsForTests.waitForJobTracker(jobClient);
      ControlledMapReduceJob.waitTillNTasksStartRunning(myJob, true, 1);

      controlledJob.finishNTasks(true, 2);
      ControlledMapReduceJob.waitTillNTotalTasksFinish(myJob, true, 2);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      tearDown();
    }
  }
}
