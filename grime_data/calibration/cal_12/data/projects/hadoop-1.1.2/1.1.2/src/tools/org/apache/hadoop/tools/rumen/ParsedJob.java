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
/**
 * 
 */
package org.apache.hadoop.tools.rumen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.mapreduce.JobACL;
import org.apache.hadoop.security.authorize.AccessControlList;

/**
 * This is a wrapper class around {@link LoggedJob}. This provides also the
 * extra information about the job obtained from job history which is not
 * written to the JSON trace file.
 */
public class ParsedJob extends LoggedJob {

  private static final Log LOG = LogFactory.getLog(ParsedJob.class);

  private Map<String, Long> totalCountersMap = new HashMap<String, Long>();
  private Map<String, Long> mapCountersMap = new HashMap<String, Long>();
  private Map<String, Long> reduceCountersMap = new HashMap<String, Long>();

  private String jobConfPath;
  private Map<JobACL, AccessControlList> jobAcls;

  ParsedJob() {

  }

  ParsedJob(String jobID) {
    super();

    setJobID(jobID);
  }

  /** Set the job total counters */
  void putTotalCounters(Map<String, Long> totalCounters) {
    this.totalCountersMap = totalCounters;
  }

  /**
   * @return the job total counters
   */
  public Map<String, Long> obtainTotalCounters() {
    return totalCountersMap;
  }

  /** Set the job level map tasks' counters */
  void putMapCounters(Map<String, Long> mapCounters) {
    this.mapCountersMap = mapCounters;
  }

  /**
   * @return the job level map tasks' counters
   */
  public Map<String, Long> obtainMapCounters() {
    return mapCountersMap;
  }

  /** Set the job level reduce tasks' counters */
  void putReduceCounters(Map<String, Long> reduceCounters) {
    this.reduceCountersMap = reduceCounters;
  }

  /**
   * @return the job level reduce tasks' counters
   */
  public Map<String, Long> obtainReduceCounters() {
    return reduceCountersMap;
  }

  /** Set the job conf path in staging dir on hdfs */
  void putJobConfPath(String confPath) {
    jobConfPath = confPath;
  }

  /**
   * @return the job conf path in staging dir on hdfs
   */
  public String obtainJobConfpath() {
    return jobConfPath;
  }

  /** Set the job acls */
  void putJobAcls(Map<JobACL, AccessControlList> acls) {
    jobAcls = acls;
  }

  /**
   * @return the job acls
   */
  public Map<JobACL, AccessControlList> obtainJobAcls() {
    return jobAcls;
  }

  /**
   * @return the list of map tasks of this job
   */
  public List<ParsedTask> obtainMapTasks() {
    List<LoggedTask> tasks = super.getMapTasks();
    return convertTasks(tasks);
  }

  /**
   * @return the list of reduce tasks of this job
   */
  public List<ParsedTask> obtainReduceTasks() {
    List<LoggedTask> tasks = super.getReduceTasks();
    return convertTasks(tasks);
  }

  /**
   * @return the list of other tasks of this job
   */
  public List<ParsedTask> obtainOtherTasks() {
    List<LoggedTask> tasks = super.getOtherTasks();
    return convertTasks(tasks);
  }

  /** As we know that this list of {@link LoggedTask} objects is actually a list
   * of {@link ParsedTask} objects, we go ahead and cast them.
   * @return the list of {@link ParsedTask} objects
   */
  private List<ParsedTask> convertTasks(List<LoggedTask> tasks) {
    List<ParsedTask> result = new ArrayList<ParsedTask>();

    for (LoggedTask t : tasks) {
      if (t instanceof ParsedTask) {
        result.add((ParsedTask)t);
      } else {
        throw new RuntimeException("Unexpected type of tasks in the list...");
      }
    }
    return result;
  }

  /** Dump the extra info of ParsedJob */
  void dumpParsedJob() {
    LOG.info("ParsedJob details:" + obtainTotalCounters() + ";"
        + obtainMapCounters() + ";" + obtainReduceCounters()
        + "\n" + obtainJobConfpath() + "\n" + obtainJobAcls()
        + ";Q=" + (getQueue() == null ? "null" : getQueue()));
    List<ParsedTask> maps = obtainMapTasks();
    for (ParsedTask task : maps) {
      task.dumpParsedTask();
    }
    List<ParsedTask> reduces = obtainReduceTasks();
    for (ParsedTask task : reduces) {
      task.dumpParsedTask();
    }
    List<ParsedTask> others = obtainOtherTasks();
    for (ParsedTask task : others) {
      task.dumpParsedTask();
    }
  }
}
