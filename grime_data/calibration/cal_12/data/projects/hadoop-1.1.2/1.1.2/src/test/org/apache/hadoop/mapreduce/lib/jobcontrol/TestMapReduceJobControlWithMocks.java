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
package org.apache.hadoop.mapreduce.lib.jobcontrol;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.junit.Test;

/**
 * Tests the JobControl API using mock and stub Job instances.
 */
public class TestMapReduceJobControlWithMocks {

  @Test
  public void testSuccessfulJobs() throws Exception {
    JobControl jobControl = new JobControl("Test");
    
    ControlledJob job1 = createSuccessfulControlledJob(jobControl);
    ControlledJob job2 = createSuccessfulControlledJob(jobControl);
    ControlledJob job3 = createSuccessfulControlledJob(jobControl, job1, job2);
    ControlledJob job4 = createSuccessfulControlledJob(jobControl, job3);
    
    runJobControl(jobControl);
    
    assertEquals("Success list", 4, jobControl.getSuccessfulJobList().size());
    assertEquals("Failed list", 0, jobControl.getFailedJobList().size());
    
    assertTrue(job1.getJobState() == ControlledJob.State.SUCCESS);
    assertTrue(job2.getJobState() == ControlledJob.State.SUCCESS);
    assertTrue(job3.getJobState() == ControlledJob.State.SUCCESS);
    assertTrue(job4.getJobState() == ControlledJob.State.SUCCESS);
    
    jobControl.stop();
  }
  
  @Test
  public void testFailedJob() throws Exception {
    JobControl jobControl = new JobControl("Test");
    
    ControlledJob job1 = createFailedControlledJob(jobControl);
    ControlledJob job2 = createSuccessfulControlledJob(jobControl);
    ControlledJob job3 = createSuccessfulControlledJob(jobControl, job1, job2);
    ControlledJob job4 = createSuccessfulControlledJob(jobControl, job3);
    
    runJobControl(jobControl);
    
    assertEquals("Success list", 1, jobControl.getSuccessfulJobList().size());
    assertEquals("Failed list", 3, jobControl.getFailedJobList().size());

    assertTrue(job1.getJobState() == ControlledJob.State.FAILED);
    assertTrue(job2.getJobState() == ControlledJob.State.SUCCESS);
    assertTrue(job3.getJobState() == ControlledJob.State.DEPENDENT_FAILED);
    assertTrue(job4.getJobState() == ControlledJob.State.DEPENDENT_FAILED);
    
    jobControl.stop();
  }
  
  @Test
  public void testKillJob() throws Exception {
    JobControl jobControl = new JobControl("Test");
    
    ControlledJob job = createFailedControlledJob(jobControl);
    
    job.killJob();

    // Verify that killJob() was called on the mock Job
    verify(job.getJob()).killJob();
  }
  
  private Job createJob(boolean complete, boolean successful)
  	throws IOException, InterruptedException {
    // Create a stub Job that responds in a controlled way
    Job mockJob = mock(Job.class);
    when(mockJob.getConfiguration()).thenReturn(new Configuration());
    when(mockJob.isComplete()).thenReturn(complete);
    when(mockJob.isSuccessful()).thenReturn(successful);
    return mockJob;
  }
  
  private ControlledJob createControlledJob(JobControl jobControl,
      	boolean successful, ControlledJob... dependingJobs)
      	throws IOException, InterruptedException {
    List<ControlledJob> dependingJobsList = dependingJobs == null ? null :
      Arrays.asList(dependingJobs);
    ControlledJob job = new ControlledJob(createJob(true, successful),
	dependingJobsList);
    jobControl.addJob(job);
    return job;
  }
  
  private ControlledJob createSuccessfulControlledJob(JobControl jobControl,
      ControlledJob... dependingJobs) throws IOException, InterruptedException {
    return createControlledJob(jobControl, true, dependingJobs);
  }

  private ControlledJob createFailedControlledJob(JobControl jobControl,
      ControlledJob... dependingJobs) throws IOException, InterruptedException {
    return createControlledJob(jobControl, false, dependingJobs);
  }

  private void runJobControl(JobControl jobControl) {
    Thread controller = new Thread(jobControl);
    controller.start();
    waitTillAllFinished(jobControl);
  }

  private void waitTillAllFinished(JobControl jobControl) {
    while (!jobControl.allFinished()) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
	// ignore
      }
    }
  }
}
