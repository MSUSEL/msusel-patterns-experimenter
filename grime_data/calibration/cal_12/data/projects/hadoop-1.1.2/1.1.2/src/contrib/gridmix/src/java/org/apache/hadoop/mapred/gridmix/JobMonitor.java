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
package org.apache.hadoop.mapred.gridmix;

import java.io.IOException;
import java.nio.channels.ClosedByInterruptException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.hadoop.mapreduce.Job;

/**
 * Component accepting submitted, running jobs and responsible for
 * monitoring jobs for success and failure. Once a job is submitted, it is
 * polled for status until complete. If a job is complete, then the monitor
 * thread returns immediately to the queue. If not, the monitor will sleep
 * for some duration.
 */
class JobMonitor implements Gridmix.Component<Job> {

  public static final Log LOG = LogFactory.getLog(JobMonitor.class);

  private final Queue<Job> mJobs;
  private final MonitorThread mThread;
  private final BlockingQueue<Job> runningJobs;
  private final long pollDelayMillis;
  private Statistics statistics;
  private boolean graceful = false;
  private boolean shutdown = false;

  public JobMonitor(Statistics statistics) {
    this(5,TimeUnit.SECONDS, statistics);
  }

  /**
   * Create a JobMonitor that sleeps for the specified duration after
   * polling a still-running job.
   * @param pollDelay Delay after polling a running job
   * @param unit Time unit for pollDelaySec (rounded to milliseconds)
   * @param statistics StatCollector , listener to job completion.
   */
  public JobMonitor(int pollDelay, TimeUnit unit, Statistics statistics) {
    mThread = new MonitorThread();
    runningJobs = new LinkedBlockingQueue<Job>();
    mJobs = new LinkedList<Job>();
    this.pollDelayMillis = TimeUnit.MILLISECONDS.convert(pollDelay, unit);
    this.statistics = statistics;
  }

  /**
   * Add a job to the polling queue.
   */
  public void add(Job job) throws InterruptedException {
    runningJobs.put(job);
  }

  /**
   * Add a submission failed job , such that it can be communicated
   * back to serial.
   * TODO: Cleaner solution for this problem
   * @param job
   */
  public void submissionFailed(Job job) {
    LOG.info("Job submission failed notification for job " + job.getJobID());
    this.statistics.add(job);
  }

  /**
   * Temporary hook for recording job success.
   */
  protected void onSuccess(Job job) {
    LOG.info(job.getJobName() + " (" + job.getJobID() + ")" + " success");
  }

  /**
   * Temporary hook for recording job failure.
   */
  protected void onFailure(Job job) {
    LOG.info(job.getJobName() + " (" + job.getJobID() + ")" + " failure");
  }

  /**
   * If shutdown before all jobs have completed, any still-running jobs
   * may be extracted from the component.
   * @throws IllegalStateException If monitoring thread is still running.
   * @return Any jobs submitted and not known to have completed.
   */
  List<Job> getRemainingJobs() {
    if (mThread.isAlive()) {
      LOG.warn("Internal error: Polling running monitor for jobs");
    }
    synchronized (mJobs) {
      return new ArrayList<Job>(mJobs);
    }
  }

  /**
   * Monitoring thread pulling running jobs from the component and into
   * a queue to be polled for status.
   */
  private class MonitorThread extends Thread {

    public MonitorThread() {
      super("GridmixJobMonitor");
    }

    /**
     * Check a job for success or failure.
     */
    public void process(Job job) throws IOException, InterruptedException {
      if (job.isSuccessful()) {
        onSuccess(job);
      } else {
        onFailure(job);
      }
    }

    @Override
    public void run() {
      boolean graceful;
      boolean shutdown;
      while (true) {
        try {
          synchronized (mJobs) {
            graceful = JobMonitor.this.graceful;
            shutdown = JobMonitor.this.shutdown;
            runningJobs.drainTo(mJobs);
          }

          // shutdown conditions; either shutdown requested and all jobs
          // have completed or abort requested and there are recently
          // submitted jobs not in the monitored set
          if (shutdown) {
            if (!graceful) {
              while (!runningJobs.isEmpty()) {
                synchronized (mJobs) {
                  runningJobs.drainTo(mJobs);
                }
              }
              break;
            } else if (mJobs.isEmpty()) {
              break;
            }
          }
          while (!mJobs.isEmpty()) {
            Job job;
            synchronized (mJobs) {
              job = mJobs.poll();
            }
            try {
              if (job.isComplete()) {
                process(job);
                statistics.add(job);
                continue;
              }
            } catch (IOException e) {
              if (e.getCause() instanceof ClosedByInterruptException) {
                // Job doesn't throw InterruptedException, but RPC socket layer
                // is blocking and may throw a wrapped Exception if this thread
                // is interrupted. Since the lower level cleared the flag,
                // reset it here
                Thread.currentThread().interrupt();
              } else {
                LOG.warn("Lost job " + (null == job.getJobName()
                     ? "<unknown>" : job.getJobName()), e);
                continue;
              }
            }
            synchronized (mJobs) {
              if (!mJobs.offer(job)) {
                LOG.error("Lost job " + (null == job.getJobName()
                     ? "<unknown>" : job.getJobName())); // should never
                                                         // happen
              }
            }
            break;
          }
          try {
            TimeUnit.MILLISECONDS.sleep(pollDelayMillis);
          } catch (InterruptedException e) {
            shutdown = true;
            continue;
          }
        } catch (Throwable e) {
          LOG.warn("Unexpected exception: ", e);
        }
      }
    }
  }

  /**
   * Start the internal, monitoring thread.
   */
  public void start() {
    mThread.start();
  }

  /**
   * Wait for the monitor to halt, assuming shutdown or abort have been
   * called. Note that, since submission may be sporatic, this will hang
   * if no form of shutdown has been requested.
   */
  public void join(long millis) throws InterruptedException {
    mThread.join(millis);
  }

  /**
   * Drain all submitted jobs to a queue and stop the monitoring thread.
   * Upstream submitter is assumed dead.
   */
  public void abort() {
    synchronized (mJobs) {
      graceful = false;
      shutdown = true;
    }
    mThread.interrupt();
  }

  /**
   * When all monitored jobs have completed, stop the monitoring thread.
   * Upstream submitter is assumed dead.
   */
  public void shutdown() {
    synchronized (mJobs) {
      graceful = true;
      shutdown = true;
    }
    mThread.interrupt();
  }
}


