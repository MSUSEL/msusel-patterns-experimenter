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

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.hadoop.mapred.TestJobQueueTaskScheduler.FakeTaskTrackerManager;

public class TestLimitTasksPerJobTaskScheduler extends TestCase {
  protected JobConf jobConf;
  protected TaskScheduler scheduler;
  private FakeTaskTrackerManager taskTrackerManager;

  @Override
  protected void setUp() throws Exception {
    TestJobQueueTaskScheduler.resetCounters();
    jobConf = new JobConf();
    jobConf.setNumMapTasks(10);
    jobConf.setNumReduceTasks(10);
    taskTrackerManager = new FakeTaskTrackerManager();
    scheduler = createTaskScheduler();
    scheduler.setConf(jobConf);
    scheduler.setTaskTrackerManager(taskTrackerManager);
    scheduler.start();
  }
  
  @Override
  protected void tearDown() throws Exception {
    if (scheduler != null) {
      scheduler.terminate();
    }
  }

  protected TaskScheduler createTaskScheduler() {
    return new LimitTasksPerJobTaskScheduler();
  }

  public void testMaxRunningTasksPerJob() throws IOException {
    jobConf.setLong(LimitTasksPerJobTaskScheduler.MAX_TASKS_PER_JOB_PROPERTY,
        4L);
    scheduler.setConf(jobConf);
    TestJobQueueTaskScheduler.submitJobs(taskTrackerManager, jobConf, 
                                         2, JobStatus.RUNNING);
    
    // First 4 slots are filled with job 1, second 4 with job 2
    TestJobQueueTaskScheduler.checkAssignment(
        scheduler, TestJobQueueTaskScheduler.tracker(taskTrackerManager, "tt1"), 
        new String[] {"attempt_test_0001_m_000001_0 on tt1"});
    TestJobQueueTaskScheduler.checkAssignment(
        scheduler, TestJobQueueTaskScheduler.tracker(taskTrackerManager, "tt1"), 
        new String[] {"attempt_test_0001_m_000002_0 on tt1"});
    TestJobQueueTaskScheduler.checkAssignment(
        scheduler, TestJobQueueTaskScheduler.tracker(taskTrackerManager, "tt1"), 
        new String[] {"attempt_test_0001_r_000003_0 on tt1"});
    TestJobQueueTaskScheduler.checkAssignment(
        scheduler, TestJobQueueTaskScheduler.tracker(taskTrackerManager, "tt1"), 
        new String[] {"attempt_test_0001_r_000004_0 on tt1"});
    TestJobQueueTaskScheduler.checkAssignment(
        scheduler, TestJobQueueTaskScheduler.tracker(taskTrackerManager, "tt2"), 
        new String[] {"attempt_test_0002_m_000005_0 on tt2"});
    TestJobQueueTaskScheduler.checkAssignment(
        scheduler, TestJobQueueTaskScheduler.tracker(taskTrackerManager, "tt2"), 
        new String[] {"attempt_test_0002_m_000006_0 on tt2"});
    TestJobQueueTaskScheduler.checkAssignment(
        scheduler, TestJobQueueTaskScheduler.tracker(taskTrackerManager, "tt2"), 
        new String[] {"attempt_test_0002_r_000007_0 on tt2"});
    TestJobQueueTaskScheduler.checkAssignment(
        scheduler, TestJobQueueTaskScheduler.tracker(taskTrackerManager, "tt2"), 
        new String[] {"attempt_test_0002_r_000008_0 on tt2"});
  }
  
  public void testMaxRunningTasksPerJobWithInterleavedTrackers()
      throws IOException {
    jobConf.setLong(LimitTasksPerJobTaskScheduler.MAX_TASKS_PER_JOB_PROPERTY,
        4L);
    scheduler.setConf(jobConf);
    TestJobQueueTaskScheduler.submitJobs(taskTrackerManager, jobConf, 2, JobStatus.RUNNING);
    
    // First 4 slots are filled with job 1, second 4 with job 2
    // even when tracker requests are interleaved
    TestJobQueueTaskScheduler.checkAssignment(
        scheduler, TestJobQueueTaskScheduler.tracker(taskTrackerManager, "tt1"), 
        new String[] {"attempt_test_0001_m_000001_0 on tt1"});
    TestJobQueueTaskScheduler.checkAssignment(
        scheduler, TestJobQueueTaskScheduler.tracker(taskTrackerManager, "tt1"), 
        new String[] {"attempt_test_0001_m_000002_0 on tt1"});
    TestJobQueueTaskScheduler.checkAssignment(
        scheduler, TestJobQueueTaskScheduler.tracker(taskTrackerManager, "tt2"), 
        new String[] {"attempt_test_0001_m_000003_0 on tt2"});
    TestJobQueueTaskScheduler.checkAssignment(
        scheduler, TestJobQueueTaskScheduler.tracker(taskTrackerManager, "tt1"), 
        new String[] {"attempt_test_0001_r_000004_0 on tt1"});
    TestJobQueueTaskScheduler.checkAssignment(
        scheduler, TestJobQueueTaskScheduler.tracker(taskTrackerManager, "tt2"), 
        new String[] {"attempt_test_0002_m_000005_0 on tt2"});
    TestJobQueueTaskScheduler.checkAssignment(
        scheduler, TestJobQueueTaskScheduler.tracker(taskTrackerManager, "tt1"), 
        new String[] {"attempt_test_0002_r_000006_0 on tt1"});
    TestJobQueueTaskScheduler.checkAssignment(
        scheduler, TestJobQueueTaskScheduler.tracker(taskTrackerManager, "tt2"), 
        new String[] {"attempt_test_0002_r_000007_0 on tt2"});
    TestJobQueueTaskScheduler.checkAssignment(
        scheduler, TestJobQueueTaskScheduler.tracker(taskTrackerManager, "tt2"), 
        new String[] {"attempt_test_0002_r_000008_0 on tt2"});
  }
  
}
