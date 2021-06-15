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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.metrics2.MetricsSystem;
import org.apache.hadoop.metrics2.lib.DefaultMetricsSystem;

class JobTrackerInstrumentation {
  private static final Log LOG =
      LogFactory.getLog(JobTrackerInstrumentation.class);

  protected final JobTracker tracker;
  
  public JobTrackerInstrumentation(JobTracker jt, JobConf conf) {
    tracker = jt;
  }

  public void launchMap(TaskAttemptID taskAttemptID)
  { }

  public void completeMap(TaskAttemptID taskAttemptID)
  { }

  public void failedMap(TaskAttemptID taskAttemptID)
  { }

  public void launchReduce(TaskAttemptID taskAttemptID)
  { }

  public void completeReduce(TaskAttemptID taskAttemptID)
  { }
  
  public void failedReduce(TaskAttemptID taskAttemptID)
  { }

  public void submitJob(JobConf conf, JobID id) 
  { }
    
  public void completeJob(JobConf conf, JobID id) 
  { }

  public void terminateJob(JobConf conf, JobID id) 
  { }
  
  public void finalizeJob(JobConf conf, JobID id) 
  { }
  
  public void addWaitingMaps(JobID id, int task)
  { }
  
  public void decWaitingMaps(JobID id, int task) 
  { }
  
  public void addWaitingReduces(JobID id, int task)
  { }
  
  public void decWaitingReduces(JobID id, int task)
  { }

  public void setMapSlots(int slots)
  { }

  public void setReduceSlots(int slots)
  { }

  public void addBlackListedMapSlots(int slots)
  { }

  public void decBlackListedMapSlots(int slots)
  { }

  public void addBlackListedReduceSlots(int slots)
  { }

  public void decBlackListedReduceSlots(int slots)
  { }

  public void addReservedMapSlots(int slots)
  { }

  public void decReservedMapSlots(int slots)
  { }

  public void addReservedReduceSlots(int slots)
  { }

  public void decReservedReduceSlots(int slots)
  { }

  public void addOccupiedMapSlots(int slots)
  { }

  public void decOccupiedMapSlots(int slots)
  { }

  public void addOccupiedReduceSlots(int slots)
  { }

  public void decOccupiedReduceSlots(int slots)
  { }

  public void failedJob(JobConf conf, JobID id) 
  { }

  public void killedJob(JobConf conf, JobID id) 
  { }

  public void addPrepJob(JobConf conf, JobID id) 
  { }
  
  public void decPrepJob(JobConf conf, JobID id) 
  { }

  public void addRunningJob(JobConf conf, JobID id) 
  { }

  public void decRunningJob(JobConf conf, JobID id) 
  { }

  public void addRunningMaps(int tasks)
  { }

  public void decRunningMaps(int tasks) 
  { }

  public void addRunningReduces(int tasks)
  { }

  public void decRunningReduces(int tasks)
  { }

  public void killedMap(TaskAttemptID taskAttemptID)
  { }

  public void killedReduce(TaskAttemptID taskAttemptID)
  { }

  public void addTrackers(int trackers)
  { }

  public void decTrackers(int trackers)
  { }

  public void addBlackListedTrackers(int trackers)
  { }

  public void decBlackListedTrackers(int trackers)
  { }

  public void addGrayListedTrackers(int trackers)
  { }

  public void decGrayListedTrackers(int trackers)
  { }

  public void setDecommissionedTrackers(int trackers)
  { }   

  public void heartbeat() {
  }

  static JobTrackerInstrumentation create(JobTracker jt, JobConf conf) {
    return create(jt, conf, DefaultMetricsSystem.INSTANCE);
  }

  static JobTrackerInstrumentation create(JobTracker jt, JobConf conf,
                                          MetricsSystem ms) {
    return ms.register("JobTrackerMetrics", "JobTracker metrics",
                       new JobTrackerMetricsSource(jt, conf));
  }

}
