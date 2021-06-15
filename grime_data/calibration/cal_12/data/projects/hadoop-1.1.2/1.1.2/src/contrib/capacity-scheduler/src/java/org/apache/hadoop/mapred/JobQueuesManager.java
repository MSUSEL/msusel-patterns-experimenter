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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.mapred.JobQueueJobInProgressListener.JobSchedulingInfo;
import org.apache.hadoop.mapred.JobStatusChangeEvent.EventType;

/**
 * A {@link JobInProgressListener} that maintains the jobs being managed in
 * one or more queues. 
 */
class JobQueuesManager extends JobInProgressListener {
  
  private static final Log LOG = LogFactory.getLog(JobQueuesManager.class);
  private CapacityTaskScheduler scheduler;
  // Queues in the system
  private Collection<String> jobQueueNames;
  private Map<String, CapacitySchedulerQueue> jobQueues = 
    new HashMap<String, CapacitySchedulerQueue>();

  
  JobQueuesManager(CapacityTaskScheduler s) {
    this.scheduler = s;
  }
  
  void setQueues(Map<String, CapacitySchedulerQueue> queues) {
    this.jobQueues = queues;
    this.jobQueueNames = new ArrayList<String>(queues.keySet());
  }
  
  @Override
  public void jobAdded(JobInProgress job) throws IOException {
    LOG.info("Job " + job.getJobID() + " submitted to queue " + 
        job.getProfile().getQueueName());
    
    // add job to the right queue
    CapacitySchedulerQueue queue = getQueue(job.getProfile().getQueueName());
    if (null == queue) {
      // job was submitted to a queue we're not aware of
      LOG.warn("Invalid queue " + job.getProfile().getQueueName() + 
          " specified for job" + job.getProfile().getJobID() + 
          ". Ignoring job.");
      return;
    }
    // add job to waiting queue. It will end up in the right place, 
    // based on priority. 
    queue.addWaitingJob(job);
    // let scheduler know. 
    scheduler.jobAdded(job);
  }

  /*
   * Method removes the jobs from both running and waiting job queue in 
   * job queue manager.
   */
  private void jobCompleted(JobInProgress job, JobSchedulingInfo oldInfo, 
      CapacitySchedulerQueue queue, int runState) {
    LOG.info("Job " + job.getJobID().toString() + " submitted to queue " 
        + job.getProfile().getQueueName() + " has completed");
    //remove jobs from both queue's a job can be in
    //running and waiting queue at the same time.
    JobInProgress waitingJob = queue.removeWaitingJob(oldInfo, runState);
    JobInProgress initializingJob = 
      queue.removeInitializingJob(oldInfo, runState);
    JobInProgress runningJob = queue.removeRunningJob(oldInfo, runState);
    
    // let scheduler know if necessary
    // sometimes this isn't necessary if the job was rejected during submission
    if (runningJob != null || initializingJob != null || waitingJob != null) {
      scheduler.jobCompleted(job);
    }
  }
  
  // Note that job is removed when the job completes i.e in jobUpated()
  @Override
  public void jobRemoved(JobInProgress job) {}
  
  // This is used to reposition a job in the queue. A job can get repositioned 
  // because of the change in the job priority or job start-time.
  private void reorderJobs(JobInProgress job, JobSchedulingInfo oldInfo, 
      CapacitySchedulerQueue queue, int runState) {
    if(queue.removeWaitingJob(oldInfo, runState) != null) {
      try {
        queue.addWaitingJob(job);
      } catch (IOException ioe) {
        // Ignore, cannot happen
        LOG.warn("Couldn't change priority!");
        return;
      }
    }
    if (queue.removeInitializingJob(oldInfo, runState) != null) {
      queue.addInitializingJob(job);
    }
    if(queue.removeRunningJob(oldInfo, runState) != null) {
      queue.addRunningJob(job);
    }
  }
  
  // This is used to move a job from the waiting queue to the running queue.
  private void makeJobRunning(JobInProgress job, JobSchedulingInfo oldInfo, 
                              CapacitySchedulerQueue queue) {
    // Removing of the job from job list is responsibility of the
    //initialization poller.
    // Add the job to the running queue
    queue.addRunningJob(job);
  }
  
  // Update the scheduler as job's state has changed
  private void jobStateChanged(JobStatusChangeEvent event, 
                               CapacitySchedulerQueue queue) {
    JobInProgress job = event.getJobInProgress();
    JobSchedulingInfo oldJobStateInfo = 
      new JobSchedulingInfo(event.getOldStatus());
    // Check if the ordering of the job has changed
    // For now priority and start-time can change the job ordering
    if (event.getEventType() == EventType.PRIORITY_CHANGED 
        || event.getEventType() == EventType.START_TIME_CHANGED) {
      // Make a priority change
      int runState = job.getStatus().getRunState();
      reorderJobs(job, oldJobStateInfo, queue, runState);
    } else if (event.getEventType() == EventType.RUN_STATE_CHANGED) {
      // Check if the job is complete
      int runState = job.getStatus().getRunState();
      if (runState == JobStatus.SUCCEEDED
          || runState == JobStatus.FAILED
          || runState == JobStatus.KILLED) {
        jobCompleted(job, oldJobStateInfo, queue, runState);
      } else if (runState == JobStatus.RUNNING) {
        makeJobRunning(job, oldJobStateInfo, queue);
      }
    }
  }
  
  @Override
  public void jobUpdated(JobChangeEvent event) {
    JobInProgress job = event.getJobInProgress();
    CapacitySchedulerQueue queue = getQueue(job.getProfile().getQueueName());
    if (null == queue) {
      // can't find queue for job. Shouldn't happen. 
      LOG.warn("Could not find queue " + job.getProfile().getQueueName() + 
          " when updating job " + job.getProfile().getJobID());
      return;
    }
    
    // Check if this is the status change
    if (event instanceof JobStatusChangeEvent) {
      jobStateChanged((JobStatusChangeEvent)event, queue);
    }
  }
  
  CapacitySchedulerQueue getQueue(String queue) {
    return jobQueues.get(queue);
  }
  
  Collection<String> getAllQueues() {
    return Collections.unmodifiableCollection(jobQueueNames);
  }
}
