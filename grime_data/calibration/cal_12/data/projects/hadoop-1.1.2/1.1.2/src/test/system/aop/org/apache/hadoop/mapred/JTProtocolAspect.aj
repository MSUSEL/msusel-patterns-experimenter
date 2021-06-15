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
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.JobID;
import org.apache.hadoop.mapreduce.TaskID;
import org.apache.hadoop.mapreduce.test.system.JTProtocol;
import org.apache.hadoop.mapreduce.test.system.JobInfo;
import org.apache.hadoop.mapreduce.test.system.TTInfo;
import org.apache.hadoop.mapreduce.test.system.TaskInfo;
import org.apache.hadoop.mapred.StatisticsCollectionHandler;

/**
 * Aspect which injects the basic protocol functionality which is to be
 * implemented by all the services which implement {@link ClientProtocol}
 * 
 * Aspect also injects default implementation for the {@link JTProtocol}
 */

public aspect JTProtocolAspect {

  // Make the ClientProtocl extend the JTprotocol
  declare parents : JobSubmissionProtocol extends JTProtocol;

  /*
   * Start of default implementation of the methods in JTProtocol
   */

  public Configuration JTProtocol.getDaemonConf() throws IOException {
    return null;
  }

  public JobInfo JTProtocol.getJobInfo(JobID jobID) throws IOException {
    return null;
  }

  public TaskInfo JTProtocol.getTaskInfo(TaskID taskID) throws IOException {
    return null;
  }

  public TTInfo JTProtocol.getTTInfo(String trackerName) throws IOException {
    return null;
  }

  public JobInfo[] JTProtocol.getAllJobInfo() throws IOException {
    return null;
  }

  public TaskInfo[] JTProtocol.getTaskInfo(JobID jobID) throws IOException {
    return null;
  }

  public TTInfo[] JTProtocol.getAllTTInfo() throws IOException {
    return null;
  }
  
  public boolean JTProtocol.isJobRetired(JobID jobID) throws IOException {
    return false;
  }
  
  public String JTProtocol.getJobHistoryLocationForRetiredJob(JobID jobID) throws IOException {
    return "";
  }
  
  public boolean JTProtocol.isBlackListed(String trackerID) throws IOException {
    return false;
  }
  
  public String JTProtocol.getJobSummaryFromLog(JobID jobId, 
      String filePattern) throws IOException {
    return null;
  }

  public String JTProtocol.getJobSummaryInfo(JobID jobId) throws IOException {
    return null;
  }
  
  public int JTProtocol.getTaskTrackerLevelStatistics(TaskTrackerStatus
      ttStatus, String timePeriod, String totalTasksOrSucceededTasks)
      throws IOException {
    return 0;
  }

  public int JTProtocol.getInfoFromAllClients(String timePeriod,
      String totalTasksOrSucceededTasks) throws IOException {
    return 0;
  }

  public StatisticsCollectionHandler JTProtocol.
      getInfoFromAllClientsForAllTaskType() throws Exception {
    return null;
  }

  public int JTProtocol.getTaskTrackerHeartbeatInterval()
      throws Exception {
    return -1;
  }
  
  public void JTProtocol.accessHistoryData(JobID jobId) throws Exception{
    
  }

  public boolean JTProtocol.isNodeDecommissioned(String ttClientHostName) 
       throws IOException {
   return false;
  }


}
