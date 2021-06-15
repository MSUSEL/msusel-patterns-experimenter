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
package org.apache.hadoop.tools.rumen;

import org.apache.hadoop.mapreduce.JobID;

/**
 * Event to record Failed and Killed completion of jobs
 *
 */
public class JobUnsuccessfulCompletionEvent implements HistoryEvent {
  private JobID jobId;
  private long finishTime;
  private int finishedMaps;
  private int finishedReduces;
  private String jobStatus;

  /**
   * Create an event to record unsuccessful completion (killed/failed) of jobs
   * @param id Job ID
   * @param finishTime Finish time of the job
   * @param finishedMaps Number of finished maps
   * @param finishedReduces Number of finished reduces
   * @param status Status of the job
   */
  public JobUnsuccessfulCompletionEvent(JobID id, long finishTime,
      int finishedMaps,
      int finishedReduces, String status) {
    this.jobId = id;
    this.finishTime = finishTime;
    this.finishedMaps = finishedMaps;
    this.finishedReduces = finishedReduces;
    this.jobStatus = status;
  }

  /** Get the Job ID */
  public JobID getJobId() { return jobId; }
  /** Get the job finish time */
  public long getFinishTime() { return finishTime; }
  /** Get the number of finished maps */
  public int getFinishedMaps() { return finishedMaps; }
  /** Get the number of finished reduces */
  public int getFinishedReduces() { return finishedReduces; }
  /** Get the status */
  public String getStatus() { return jobStatus; }
  /** Get the event type */
  public EventType getEventType() {
    if ("FAILED".equals(getStatus())) {
      return EventType.JOB_FAILED;
    } else
      return EventType.JOB_KILLED;
  }

}
