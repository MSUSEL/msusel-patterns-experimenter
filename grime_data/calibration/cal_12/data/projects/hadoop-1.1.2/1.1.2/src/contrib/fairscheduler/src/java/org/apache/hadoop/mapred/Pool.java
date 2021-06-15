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

import java.util.ArrayList;
import java.util.Collection;

import org.apache.hadoop.mapreduce.TaskType;

/**
 * A schedulable pool of jobs.
 */
public class Pool {
  /** Name of the default pool, where jobs with no pool parameter go. */
  public static final String DEFAULT_POOL_NAME = "default";
  
  /** Pool name. */
  private String name;
  
  /** Jobs in this specific pool; does not include children pools' jobs. */
  private Collection<JobInProgress> jobs = new ArrayList<JobInProgress>();
  
  /** Scheduling mode for jobs inside the pool (fair or FIFO) */
  private SchedulingMode schedulingMode;

  private PoolSchedulable mapSchedulable;
  private PoolSchedulable reduceSchedulable;

  public Pool(FairScheduler scheduler, String name) {
    if (name == null) {
      throw new IllegalArgumentException("Passed pool name was null.");
    }
    this.name = name;
    mapSchedulable = new PoolSchedulable(scheduler, this, TaskType.MAP);
    reduceSchedulable = new PoolSchedulable(scheduler, this, TaskType.REDUCE);
  }
  
  public Collection<JobInProgress> getJobs() {
    return jobs;
  }
  
  public void addJob(JobInProgress job) {
    jobs.add(job);
    mapSchedulable.addJob(job);
    reduceSchedulable.addJob(job);
  }
  
  public void removeJob(JobInProgress job) {
    jobs.remove(job);
    mapSchedulable.removeJob(job);
    reduceSchedulable.removeJob(job);
  }
  
  public String getName() {
    return name;
  }

  public SchedulingMode getSchedulingMode() {
    return schedulingMode;
  }
  
  public void setSchedulingMode(SchedulingMode schedulingMode) {
    this.schedulingMode = schedulingMode;
  }

  public boolean isDefaultPool() {
    return Pool.DEFAULT_POOL_NAME.equals(name);
  }
  
  public PoolSchedulable getMapSchedulable() {
    return mapSchedulable;
  }
  
  public PoolSchedulable getReduceSchedulable() {
    return reduceSchedulable;
  }
  
  public PoolSchedulable getSchedulable(TaskType type) {
    return type == TaskType.MAP ? mapSchedulable : reduceSchedulable;
  }

  public void updateMetrics() {
    mapSchedulable.updateMetrics();
    reduceSchedulable.updateMetrics();
  }
}
