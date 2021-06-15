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
package org.apache.hadoop.vaidya.postexdiagnosis.tests;

import org.apache.hadoop.vaidya.statistics.job.JobStatistics;
import org.apache.hadoop.vaidya.statistics.job.JobStatisticsInterface.JobKeys;
import org.apache.hadoop.vaidya.statistics.job.JobStatisticsInterface.KeyDataType;
import org.apache.hadoop.vaidya.statistics.job.JobStatisticsInterface.ReduceTaskKeys;
import org.apache.hadoop.vaidya.statistics.job.ReduceTaskStatistics;
import org.apache.hadoop.vaidya.DiagnosticTest;
import org.w3c.dom.Element;
import java.util.Hashtable;
import java.util.List;

/**
 *
 */
public class BalancedReducePartitioning extends DiagnosticTest {

  private long totalReduces;
  private long busyReducers;
  private long percentReduceRecordsSize;
  private double percent;
  private double impact;
  private JobStatistics _job;
  
  /**
   * 
   */
  public BalancedReducePartitioning() {
  }

  /*    
   */
  @Override
  public double evaluate(JobStatistics jobExecutionStats) {
    
    /* Set the global job variable */
    this._job = jobExecutionStats;

    /* If Map only job then impact is zero */
    if (jobExecutionStats.getStringValue(JobKeys.JOBTYPE).equals("MAP_ONLY")) {
      this.impact = 0;
      return this.impact;
    }

    /*
     * Read this rule specific input PercentReduceRecords
     */
    this.percent = getInputElementDoubleValue("PercentReduceRecords", 0.90);
    
    /*
     * Get the sorted reduce task list by number of INPUT_RECORDS (ascending) 
     */
    List<ReduceTaskStatistics> srTaskList = 
                            jobExecutionStats.getReduceTaskList(ReduceTaskKeys.INPUT_RECORDS, KeyDataType.LONG);
    this.percentReduceRecordsSize = (long) (this.percent * jobExecutionStats.getLongValue(JobKeys.REDUCE_INPUT_RECORDS));
    this.totalReduces = jobExecutionStats.getLongValue(JobKeys.TOTAL_REDUCES);
    long tempReduceRecordsCount = 0;
    this.busyReducers = 0;
    for (int i=srTaskList.size()-1; i>-1; i--) {
      tempReduceRecordsCount += srTaskList.get(i).getLongValue(ReduceTaskKeys.INPUT_RECORDS);
      this.busyReducers++;
      if (tempReduceRecordsCount >= this.percentReduceRecordsSize) {
        break;
      }
    }
    
    // Calculate Impact
    return this.impact = (1 - (double)this.busyReducers/(double)this.totalReduces);
  }

  /*
   * helper function to print specific reduce counter for all reduce tasks
   */
  public void printReduceCounters (List<Hashtable<ReduceTaskKeys, String>> x, ReduceTaskKeys key) {
    for (int i=0; i<x.size(); i++) {
      System.out.println("ind:"+i+", Value:"+x.get(i).get(key)+":");
    }
  }
  
  /* 
   * 
   */
  @Override
  public String getPrescription() {
    return 
    "* Use the appropriate partitioning function"+ "\n" +
    "* For streaming job consider following partitioner and hadoop config parameters\n"+
    "  * org.apache.hadoop.mapred.lib.KeyFieldBasedPartitioner\n" +
    "  * -jobconf stream.map.output.field.separator, -jobconf stream.num.map.output.key.fields";
  }

  /* 
   */
  @Override
  public String getReferenceDetails() {
    String ref = 
    "* TotalReduceTasks: "+this.totalReduces+"\n"+
    "* BusyReduceTasks processing "+this.percent+ "% of total records: " +this.busyReducers+"\n"+
    "* Impact: "+truncate(this.impact);
    return ref;
  }
}
