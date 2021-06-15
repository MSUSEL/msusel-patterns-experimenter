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
import java.util.Collection;

/**
 * Manages information about the {@link TaskTracker}s running on a cluster.
 * This interface exits primarily to test the {@link JobTracker}, and is not
 * intended to be implemented by users.
 */
interface TaskTrackerManager {

  /**
   * @return A collection of the {@link TaskTrackerStatus} for the tasktrackers
   * being managed.
   */
  public Collection<TaskTrackerStatus> taskTrackers();
  
  /**
   * @return The number of unique hosts running tasktrackers.
   */
  public int getNumberOfUniqueHosts();
  
  /**
   * @return a summary of the cluster's status.
   */
  public ClusterStatus getClusterStatus();

  /**
   * Registers a {@link JobInProgressListener} for updates from this
   * {@link TaskTrackerManager}.
   * @param jobInProgressListener the {@link JobInProgressListener} to add
   */
  public void addJobInProgressListener(JobInProgressListener listener);

  /**
   * Unregisters a {@link JobInProgressListener} from this
   * {@link TaskTrackerManager}.
   * @param jobInProgressListener the {@link JobInProgressListener} to remove
   */
  public void removeJobInProgressListener(JobInProgressListener listener);

  /**
   * Return the {@link QueueManager} which manages the queues in this
   * {@link TaskTrackerManager}.
   *
   * @return the {@link QueueManager}
   */
  public QueueManager getQueueManager();
  
  /**
   * Return the current heartbeat interval that's used by {@link TaskTracker}s.
   *
   * @return the heartbeat interval used by {@link TaskTracker}s
   */
  public int getNextHeartbeatInterval();

  /**
   * Kill the job identified by jobid
   * 
   * @param jobid
   * @throws IOException
   */
  public void killJob(JobID jobid)
      throws IOException;

  /**
   * Obtain the job object identified by jobid
   * 
   * @param jobid
   * @return jobInProgress object
   */
  public JobInProgress getJob(JobID jobid);

  /**
   * Mark the task attempt identified by taskid to be killed
   * 
   * @param taskid task to kill
   * @param shouldFail whether to count the task as failed
   * @return true if the task was found and successfully marked to kill
   */
  public boolean killTask(TaskAttemptID taskid, boolean shouldFail)
      throws IOException;  

  /**
   * Initialize the Job
   * 
   * @param job JobInProgress object
   */
  public void initJob(JobInProgress job);
  
  /**
   * Fail a job.
   * 
   * @param job JobInProgress object
   */
  public void failJob(JobInProgress job);
  
  /**
   * Get safe mode.
   * @return
   */
  public boolean isInSafeMode();
}
