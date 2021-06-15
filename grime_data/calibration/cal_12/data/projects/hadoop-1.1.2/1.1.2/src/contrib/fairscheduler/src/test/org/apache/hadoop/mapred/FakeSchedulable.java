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

import org.apache.hadoop.mapreduce.TaskType;

/**
 * Dummy implementation of Schedulable for unit testing.
 */
public class FakeSchedulable extends Schedulable {
  private int demand;
  private int runningTasks;
  private int minShare;
  private double weight;
  private JobPriority priority;
  private long startTime;
  
  public FakeSchedulable() {
    this(0, 0, 1, 0, 0, JobPriority.NORMAL, 0);
  }
  
  public FakeSchedulable(int demand) {
    this(demand, 0, 1, 0, 0, JobPriority.NORMAL, 0);
  }
  
  public FakeSchedulable(int demand, int minShare) {
    this(demand, minShare, 1, 0, 0, JobPriority.NORMAL, 0);
  }
  
  public FakeSchedulable(int demand, int minShare, double weight) {
    this(demand, minShare, weight, 0, 0, JobPriority.NORMAL, 0);
  }
  
  public FakeSchedulable(int demand, int minShare, double weight, int fairShare,
      int runningTasks, JobPriority priority, long startTime) {
    this.demand = demand;
    this.minShare = minShare;
    this.weight = weight;
    setFairShare(fairShare);
    this.runningTasks = runningTasks;
    this.priority = priority;
    this.startTime = startTime;
  }
  
  @Override
  public Task assignTask(TaskTrackerStatus tts, long currentTime,
      Collection<JobInProgress> visited) throws IOException {
    return null;
  }

  @Override
  public int getDemand() {
    return demand;
  }

  @Override
  public String getName() {
    return "FakeSchedulable" + this.hashCode();
  }

  @Override
  public JobPriority getPriority() {
    return priority;
  }

  @Override
  public int getRunningTasks() {
    return runningTasks;
  }

  @Override
  public long getStartTime() {
    return startTime;
  }
  
  @Override
  public double getWeight() {
    return weight;
  }
  
  @Override
  public int getMinShare() {
    return minShare;
  }

  @Override
  public void redistributeShare() {}

  @Override
  public void updateDemand() {}

  @Override
  public TaskType getTaskType() {
    return TaskType.MAP;
  }

  @Override
  protected String getMetricsContextName() {
    return "fake";
  }

  @Override
  void updateMetrics() {
  }
}
