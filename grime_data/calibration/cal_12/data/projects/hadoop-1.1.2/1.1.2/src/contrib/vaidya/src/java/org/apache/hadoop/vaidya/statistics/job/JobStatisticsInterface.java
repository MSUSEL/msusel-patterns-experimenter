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
package org.apache.hadoop.vaidya.statistics.job;

import java.util.ArrayList;

import org.apache.hadoop.mapred.JobConf;

public interface JobStatisticsInterface {
  
  /**
   * Get job configuration (job.xml) values
   */
  public JobConf getJobConf();
  
  /*
   * Get Job Counters of type long
   */
  public long getLongValue(Enum key);
  
  /*
   * Get job Counters of type Double
   */
  public double getDoubleValue(Enum key);
  
  /* 
   * Get Job Counters of type String
   */
  public String getStringValue(Enum key);
  
  /*
   * Set key value of type long
   */
  public void setValue(Enum key, long value);
  
  /*
   * Set key value of type double
   */
  public void setValue(Enum key, double valye);
  
  /*
   * Set key value of type String
   */
  public void setValue(Enum key, String value);
  
  /**
   * @return mapTaskList : ArrayList of MapTaskStatistics
   * @param mapTaskSortKey : Specific counter key used for sorting the task list
   * @param datatype : indicates the data type of the counter key used for sorting
   * If sort key is null then by default map tasks are sorted using map task ids.
   */
  public ArrayList<MapTaskStatistics> getMapTaskList(Enum mapTaskSortKey, KeyDataType dataType);
  
  /**
   * @return reduceTaskList : ArrayList of ReduceTaskStatistics
   * @param reduceTaskSortKey : Specific counter key used for sorting the task list
   * @param dataType : indicates the data type of the counter key used for sorting
   * If sort key is null then, by default reduce tasks are sorted using task ids.
   */
  public ArrayList<ReduceTaskStatistics> getReduceTaskList(Enum reduceTaskSortKey, KeyDataType dataType);
  
  
  /*
   * Print the Job Execution Statistics
   */
  public void printJobExecutionStatistics();
  
  
  /*
   * Job and Task statistics Key data types
   */
  public static enum KeyDataType {
    STRING, LONG, DOUBLE
  }
  
  /**
   * Job Keys
   */
  public static enum JobKeys {
    JOBTRACKERID, JOBID, JOBNAME, JOBTYPE, USER, SUBMIT_TIME, CONF_PATH, LAUNCH_TIME, TOTAL_MAPS, TOTAL_REDUCES,
    STATUS, FINISH_TIME, FINISHED_MAPS, FINISHED_REDUCES, FAILED_MAPS, FAILED_REDUCES, 
    LAUNCHED_MAPS, LAUNCHED_REDUCES, RACKLOCAL_MAPS, DATALOCAL_MAPS, HDFS_BYTES_READ,
    HDFS_BYTES_WRITTEN, FILE_BYTES_READ, FILE_BYTES_WRITTEN, COMBINE_OUTPUT_RECORDS,
    COMBINE_INPUT_RECORDS, REDUCE_INPUT_GROUPS, REDUCE_INPUT_RECORDS, REDUCE_OUTPUT_RECORDS,
    MAP_INPUT_RECORDS, MAP_OUTPUT_RECORDS, MAP_INPUT_BYTES, MAP_OUTPUT_BYTES, MAP_HDFS_BYTES_WRITTEN,
    JOBCONF, JOB_PRIORITY, SHUFFLE_BYTES, SPILLED_RECORDS
  }
  
  /**
   * Map Task Keys
   */
  public static enum MapTaskKeys {
    TASK_ID, TASK_TYPE, START_TIME, STATUS, FINISH_TIME, HDFS_BYTES_READ, HDFS_BYTES_WRITTEN,
    FILE_BYTES_READ, FILE_BYTES_WRITTEN, COMBINE_OUTPUT_RECORDS, COMBINE_INPUT_RECORDS, 
    OUTPUT_RECORDS, INPUT_RECORDS, INPUT_BYTES, OUTPUT_BYTES, NUM_ATTEMPTS, ATTEMPT_ID,
    HOSTNAME, SPLITS, SPILLED_RECORDS, TRACKER_NAME, STATE_STRING, HTTP_PORT, ERROR, EXECUTION_TIME
  }
  
  /**
   * Reduce Task Keys
   */
  public static enum ReduceTaskKeys {
    
    TASK_ID, TASK_TYPE, START_TIME, STATUS, FINISH_TIME, HDFS_BYTES_READ, HDFS_BYTES_WRITTEN,
    FILE_BYTES_READ, FILE_BYTES_WRITTEN, COMBINE_OUTPUT_RECORDS, COMBINE_INPUT_RECORDS, 
    OUTPUT_RECORDS, INPUT_RECORDS, NUM_ATTEMPTS, ATTEMPT_ID, HOSTNAME, SHUFFLE_FINISH_TIME,
    SORT_FINISH_TIME, INPUT_GROUPS, TRACKER_NAME, STATE_STRING, HTTP_PORT, SPLITS, SHUFFLE_BYTES, 
    SPILLED_RECORDS, EXECUTION_TIME
  }
}
