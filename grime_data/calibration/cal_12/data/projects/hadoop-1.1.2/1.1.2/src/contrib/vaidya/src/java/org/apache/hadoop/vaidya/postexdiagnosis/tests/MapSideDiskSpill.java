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
import org.apache.hadoop.vaidya.statistics.job.JobStatisticsInterface.MapTaskKeys;
import org.apache.hadoop.vaidya.statistics.job.JobStatisticsInterface.ReduceTaskKeys;
import org.apache.hadoop.vaidya.statistics.job.MapTaskStatistics;
import org.apache.hadoop.vaidya.DiagnosticTest;
import org.w3c.dom.Element;
import java.util.Hashtable;
import java.util.List;

/**
 *
 */
public class MapSideDiskSpill extends DiagnosticTest {

  private double _impact;
  private JobStatistics _job;
  private long _numLocalBytesWrittenByMaps;
  
  /**            
   * 
   */
  public MapSideDiskSpill() {
  }

  /* 
   *  
   */
  @Override
  public double evaluate(JobStatistics job) {
  
    /*
     * Set the this._job
     */
    this._job = job;
      
    /*
     * Read the Normalization Factor
     */
    double normF = getInputElementDoubleValue("NormalizationFactor", 3.0);
    
    /*
     * Get the sorted map task list by number MapTaskKeys.OUTPUT_BYTES
     */
    List<MapTaskStatistics> smTaskList = job.getMapTaskList(MapTaskKeys.FILE_BYTES_WRITTEN, KeyDataType.LONG);
    int size = smTaskList.size();
    long numLocalBytesWrittenByMaps = 0;
    for (int i=0; i<size; i++) {
      numLocalBytesWrittenByMaps += smTaskList.get(i).getLongValue(MapTaskKeys.FILE_BYTES_WRITTEN);
    }
    this._numLocalBytesWrittenByMaps = numLocalBytesWrittenByMaps;
    
    /*
     * Map only job vs. map reduce job
     * For MapReduce job MAP_OUTPUT_BYTES are normally written by maps on local disk, so they are subtracted
     * from the localBytesWrittenByMaps.
     */
    if (job.getLongValue(JobKeys.TOTAL_REDUCES) > 0) {
      this._impact = (this._numLocalBytesWrittenByMaps - job.getLongValue(JobKeys.MAP_OUTPUT_BYTES))/job.getLongValue(JobKeys.MAP_OUTPUT_BYTES);
    } else {
      this._impact = this._numLocalBytesWrittenByMaps/job.getLongValue(JobKeys.MAP_OUTPUT_BYTES);
    }
    
    if (this._impact > normF) {
      this._impact = 1.0;
    } else {
      this._impact = this._impact/normF;
    }
    
    return this._impact;
    
  }
  
  /* (non-Javadoc)
   * @see org.apache.hadoop.contrib.utils.perfadvisor.diagnostic_rules.DiagnosticRule#getAdvice()
   */
  @Override
  public String getPrescription() {
    return 
    "* Use combiner to lower the map output size.\n" +
      "* Increase map side sort buffer size (io.sort.mb:"+this._job.getJobConf().getInt("io.sort.mb", 0) + ").\n" +
      "* Increase index buffer size (io.sort.record.percent:"+ this._job.getJobConf().getInt("io.sort.record.percent", 0) + ") if number of Map Output Records are large. \n" +
      "* Increase (io.sort.spill.percent:"+ this._job.getJobConf().getInt("io.sort.spill.percent", 0) + "), default 0.80 i.e. 80% of sort buffer size and index buffer size. \n";
  }

  /* (non-Javadoc)
   * @see org.apache.hadoop.contrib.utils.perfadvisor.diagnostic_rules.DiagnosticRule#getReferenceDetails()
   */
  @Override
  public String getReferenceDetails() {
    String ref = 
    "* TotalMapOutputBytes: "+this._job.getLongValue(JobKeys.MAP_OUTPUT_BYTES)+"\n"+
    "* Total Local Bytes Written by Maps: "+this._numLocalBytesWrittenByMaps+"\n"+
    "* Impact: "+ truncate(this._impact);
    return ref;
  }
}
