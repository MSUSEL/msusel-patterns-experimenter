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
import java.util.List;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.server.jobtracker.TaskTracker;

/**
 * Used by a {@link JobTracker} to schedule {@link Task}s on
 * {@link TaskTracker}s.
 * <p>
 * {@link TaskScheduler}s typically use one or more
 * {@link JobInProgressListener}s to receive notifications about jobs.
 * <p>
 * It is the responsibility of the {@link TaskScheduler}
 * to initialize tasks for a job, by calling {@link JobInProgress#initTasks()}
 * between the job being added (when
 * {@link JobInProgressListener#jobAdded(JobInProgress)} is called)
 * and tasks for that job being assigned (by
 * {@link #assignTasks(TaskTracker)}).
 * @see EagerTaskInitializationListener
 */
abstract class TaskScheduler implements Configurable {

  protected Configuration conf;
  protected TaskTrackerManager taskTrackerManager;
  
  public Configuration getConf() {
    return conf;
  }

  public void setConf(Configuration conf) {
    this.conf = conf;
  }

  public synchronized void setTaskTrackerManager(
      TaskTrackerManager taskTrackerManager) {
    this.taskTrackerManager = taskTrackerManager;
  }
  
  /**
   * Lifecycle method to allow the scheduler to start any work in separate
   * threads.
   * @throws IOException
   */
  public void start() throws IOException {
    // do nothing
  }
  
  /**
   * Lifecycle method to allow the scheduler to stop any work it is doing.
   * @throws IOException
   */
  public void terminate() throws IOException {
    // do nothing
  }

  /**
   * Returns the tasks we'd like the TaskTracker to execute right now.
   * 
   * @param taskTracker The TaskTracker for which we're looking for tasks.
   * @return A list of tasks to run on that TaskTracker, possibly empty.
   */
  public abstract List<Task> assignTasks(TaskTracker taskTracker)
  throws IOException;

  /**
   * Returns a collection of jobs in an order which is specific to 
   * the particular scheduler.
   * @param queueName
   * @return
   */
  public abstract Collection<JobInProgress> getJobs(String queueName);

  /**
   * Refresh the configuration of the scheduler.
   */
  public void refresh() throws IOException {}


  /**
   * Subclasses can override to provide any scheduler-specific checking
   * mechanism for job submission.
   * @param job
   * @throws IOException
   */
  public void checkJobSubmission(JobInProgress job) throws IOException{
  }

}
