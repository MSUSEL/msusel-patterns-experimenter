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
 * Event to record the initialization of a job
 *
 */
public class JobInitedEvent implements HistoryEvent {
  private JobID jobId;
  private long launchTime;
  private int totalMaps;
  private int totalReduces;
  private String jobStatus;

  /**
   * Create an event to record job initialization
   * @param id
   * @param launchTime
   * @param totalMaps
   * @param totalReduces
   * @param jobStatus
   */
  public JobInitedEvent(JobID id, long launchTime, int totalMaps,
                        int totalReduces, String jobStatus) {
    this.jobId = id;
    this.launchTime = launchTime;
    this.totalMaps = totalMaps;
    this.totalReduces = totalReduces;
    this.jobStatus = jobStatus;
  }

  /** Get the job ID */
  public JobID getJobId() { return jobId; }
  /** Get the launch time */
  public long getLaunchTime() { return launchTime; }
  /** Get the total number of maps */
  public int getTotalMaps() { return totalMaps; }
  /** Get the total number of reduces */
  public int getTotalReduces() { return totalReduces; }
  /** Get the status */
  public String getStatus() { return jobStatus; }
 /** Get the event type */
  public EventType getEventType() {
    return EventType.JOB_INITED;
  }

}
