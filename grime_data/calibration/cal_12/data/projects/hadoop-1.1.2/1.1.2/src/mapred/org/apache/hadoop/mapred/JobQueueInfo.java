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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

/**
 * Class that contains the information regarding the Job Queues which are 
 * maintained by the Hadoop Map/Reduce framework.
 * 
 */

public class JobQueueInfo implements Writable {

  /**
   * String used for empty (null) scheduling information.
   */
  static final String EMPTY_INFO = "N/A";

  private String queueName = "";
  private String queueState = Queue.QueueState.RUNNING.getStateName();
  //The scheduling Information object is read back as String.
  //Once the scheduling information is set there is no way to recover it.
  private String schedulingInfo = EMPTY_INFO;

  public JobQueueInfo() { }

  /**
   * Construct a new JobQueueInfo object using the queue name and the
   * scheduling information passed.
   * 
   * @param queueName Name of the job queue
   * @param schedulingInfo Scheduling Information associated with the job
   * queue
   */
  public JobQueueInfo(String queueName, String schedulingInfo) {
    this.queueName = queueName;
    this.schedulingInfo = schedulingInfo;
  }
  
  
  /**
   * Set the queue name of the JobQueueInfo
   * 
   * @param queueName Name of the job queue.
   */
  public void setQueueName(String queueName) {
    this.queueName = queueName;
  }

  /**
   * Get the queue name from JobQueueInfo
   * 
   * @return queue name
   */
  public String getQueueName() {
    return queueName;
  }

  /**
   * Set the scheduling information associated to particular job queue
   * 
   * @param schedulingInfo
   */
  public void setSchedulingInfo(String schedulingInfo) {
    this.schedulingInfo = (schedulingInfo != null)
      ? schedulingInfo
      : EMPTY_INFO;
  }

  /**
   * Gets the scheduling information associated to particular job queue.
   * If nothing is set would return <b>"N/A"</b>
   * 
   * @return Scheduling information associated to particular Job Queue
   */
  public String getSchedulingInfo() {
    return schedulingInfo;
  }
  
  /**
   * Set the state of the queue
   * @param state state of the queue.
   */
  public void setQueueState(String state) {
    queueState = state;
  }

  /**
   * Return the queue state
   * @return the queue state.
   */
  public String getQueueState() {
    return queueState;
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    queueName = Text.readString(in);
    queueState = Text.readString(in);
    schedulingInfo = Text.readString(in);
  }

  @Override
  public void write(DataOutput out) throws IOException {
    Text.writeString(out, queueName);
    Text.writeString(out, queueState);
    Text.writeString(out, schedulingInfo);
  }
}
