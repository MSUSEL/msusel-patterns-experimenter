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

/**
 * {@link JobStatusChangeEvent} tracks the change in job's status. Job's 
 * status can change w.r.t 
 *   - run state i.e PREP, RUNNING, FAILED, KILLED, SUCCEEDED
 *   - start time
 *   - priority
 * Note that job times can change as the job can get restarted.
 */
class JobStatusChangeEvent extends JobChangeEvent {
  // Events in job status that can lead to a job-status change
  static enum EventType {RUN_STATE_CHANGED, START_TIME_CHANGED, PRIORITY_CHANGED}
  
  private JobStatus oldStatus;
  private JobStatus newStatus;
  private EventType eventType;
   
  JobStatusChangeEvent(JobInProgress jip, EventType eventType, 
                       JobStatus oldStatus, JobStatus newStatus) {
    super(jip);
    this.oldStatus = oldStatus;
    this.newStatus = newStatus;
    this.eventType = eventType;
  }
  
  /**
   * Create a {@link JobStatusChangeEvent} indicating the state has changed. 
   * Note that here we assume that the state change doesnt care about the old
   * state.
   */
  JobStatusChangeEvent(JobInProgress jip, EventType eventType, JobStatus status)
  {
    this(jip, eventType, status, status);
  }
  
  /**
   * Returns a event-type that caused the state change
   */
  EventType getEventType() {
    return eventType;
  }
  
  /**
   * Get the old job status
   */
  JobStatus getOldStatus() {
    return oldStatus;
  }
  
  /**
   * Get the new job status as a result of the events
   */
  JobStatus getNewStatus() {
    return newStatus;
  }
}