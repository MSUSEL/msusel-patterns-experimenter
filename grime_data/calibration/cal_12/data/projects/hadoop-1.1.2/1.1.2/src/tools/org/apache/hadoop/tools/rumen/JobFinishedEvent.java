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

import java.io.IOException;

import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.JobID;

/**
 * Event to record successful completion of job
 *
 */
public class JobFinishedEvent  implements HistoryEvent {
  private JobID jobId;
  private long finishTime;
  private int finishedMaps;
  private int finishedReduces;
  private int failedMaps;
  private int failedReduces;
  private Counters mapCounters;
  private Counters reduceCounters;
  private Counters totalCounters;

  /** 
   * Create an event to record successful job completion
   * @param id Job ID
   * @param finishTime Finish time of the job
   * @param finishedMaps The number of finished maps
   * @param finishedReduces The number of finished reduces
   * @param failedMaps The number of failed maps
   * @param failedReduces The number of failed reduces
   * @param mapCounters Map Counters for the job
   * @param reduceCounters Reduce Counters for the job
   * @param totalCounters Total Counters for the job
   */
  public JobFinishedEvent(JobID id, long finishTime,
      int finishedMaps, int finishedReduces,
      int failedMaps, int failedReduces,
      Counters mapCounters, Counters reduceCounters,
      Counters totalCounters) {
    this.jobId = id;
    this.finishTime = finishTime;
    this.finishedMaps = finishedMaps;
    this.finishedReduces = finishedReduces;
    this.failedMaps = failedMaps;
    this.failedReduces = failedReduces;
    this.mapCounters = mapCounters;
    this.reduceCounters = reduceCounters;
    this.totalCounters = totalCounters;
  }

  public EventType getEventType() {
    return EventType.JOB_FINISHED;
  }

  /** Get the Job ID */
  public JobID getJobid() { return jobId; }
  /** Get the job finish time */
  public long getFinishTime() { return finishTime; }
  /** Get the number of finished maps for the job */
  public int getFinishedMaps() { return finishedMaps; }
  /** Get the number of finished reducers for the job */
  public int getFinishedReduces() { return finishedReduces; }
  /** Get the number of failed maps for the job */
  public int getFailedMaps() { return failedMaps; }
  /** Get the number of failed reducers for the job */
  public int getFailedReduces() { return failedReduces; }
  /** Get the counters for the job */
  public Counters getTotalCounters() {
    return totalCounters;
  }
  /** Get the Map counters for the job */
  public Counters getMapCounters() {
    return mapCounters;
  }
  /** Get the reduce counters for the job */
  public Counters getReduceCounters() {
    return reduceCounters;
  }
}
