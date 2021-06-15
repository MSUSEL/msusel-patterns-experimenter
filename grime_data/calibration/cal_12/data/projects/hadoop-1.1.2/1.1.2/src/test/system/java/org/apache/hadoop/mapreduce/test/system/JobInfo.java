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
package org.apache.hadoop.mapreduce.test.system;

import java.util.List;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.JobStatus;
import org.apache.hadoop.mapreduce.JobID;

/**
 * Job state information as seen by the JobTracker.
 */
public interface JobInfo extends Writable {
  /**
   * Gets the JobId of the job.<br/>
   * 
   * @return id of the job.
   */
  JobID getID();

  /**
   * Gets the current status of the job.<br/>
   * 
   * @return status.
   */
  JobStatus getStatus();

  /**
   * Gets the history location of the job.<br/>
   * 
   * @return the path to the history file.
   */
  String getHistoryUrl();

  /**
   * Gets the number of maps which are currently running for the job. <br/>
   * 
   * @return number of running for the job.
   */
  int runningMaps();

  /**
   * Gets the number of reduces currently running for the job. <br/>
   * 
   * @return number of reduces running for the job.
   */
  int runningReduces();

  /**
   * Gets the number of maps to be scheduled for the job. <br/>
   * 
   * @return number of waiting maps.
   */
  int waitingMaps();

  /**
   * Gets the number of reduces to be scheduled for the job. <br/>
   * 
   * @return number of waiting reduces.
   */
  int waitingReduces();
  
  /**
   * Gets the number of maps that are finished. <br/>
   * @return the number of finished maps.
   */
  int finishedMaps();
  
  /**
   * Gets the number of map tasks that are to be spawned for the job <br/>
   * @return
   */
  int numMaps();
  
  /**
   * Gets the number of reduce tasks that are to be spawned for the job <br/>
   * @return
   */
  int numReduces();
  
  /**
   * Gets the number of reduces that are finished. <br/>
   * @return the number of finished reduces.
   */
  int finishedReduces();

  /**
   * Gets if cleanup for the job has been launched.<br/>
   * 
   * @return true if cleanup task has been launched.
   */
  boolean isCleanupLaunched();

  /**
   * Gets if the setup for the job has been launched.<br/>
   * 
   * @return true if setup task has been launched.
   */
  boolean isSetupLaunched();

  /**
   * Gets if the setup for the job has been completed.<br/>
   * 
   * @return true if the setup task for the job has completed.
   */
  boolean isSetupFinished();

  /**
   * Gets list of blacklisted trackers for the particular job. <br/>
   * 
   * @return list of blacklisted tracker name.
   */
  List<String> getBlackListedTrackers();
  
  /**
   * Get the launch time of a job.
   * @return long - launch time for a job.
   */
  long getLaunchTime();
  /**
   * Get the finish time of a job
   * @return long - finish time for a job
   */
  long getFinishTime();
  /**
   * Get the number of slots per map.
   * @return int - number of slots per map.
   */
  int getNumSlotsPerMap();
  /**
   * Get the number of slots per reduce.
   * @return int - number of slots per reduce.
   */
  int getNumSlotsPerReduce();
}
