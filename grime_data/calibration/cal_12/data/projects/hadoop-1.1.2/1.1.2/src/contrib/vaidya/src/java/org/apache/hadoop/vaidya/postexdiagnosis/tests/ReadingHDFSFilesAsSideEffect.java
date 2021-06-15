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
public class ReadingHDFSFilesAsSideEffect extends DiagnosticTest {

  private double _impact;
  private JobStatistics _job;
  
  /**
   * 
   */
  public ReadingHDFSFilesAsSideEffect() {
  }

  /*
   * Evaluate the test    
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
    double normF = getInputElementDoubleValue("NormalizationFactor", 2.0);
    
    
    /*
     * Calculate and return the impact
     * 
     * Check if job level aggregate bytes read from HDFS are more than map input bytes
     * Typically they should be same unless maps and/or reducers are reading some data
     * from HDFS as a side effect
     * 
     * If side effect HDFS bytes read are >= twice map input bytes impact is treated as
     * maximum.
     */
    if(job.getLongValue(JobKeys.MAP_INPUT_BYTES) == 0 && job.getLongValue(JobKeys.HDFS_BYTES_READ) != 0) {
      return (double)1;
    }

    if (job.getLongValue(JobKeys.HDFS_BYTES_READ) == 0) {
      return (double)0;
    }
    
    this._impact = (job.getLongValue(JobKeys.HDFS_BYTES_READ) / job.getLongValue(JobKeys.MAP_INPUT_BYTES));
    if (this._impact >= normF) {
      this._impact = 1;
    }
    else  {
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
    "Map and/or Reduce tasks are reading application specific files from HDFS. Make sure the replication factor\n" +
        "of these HDFS files is high enough to avoid the data reading bottleneck. Typically replication factor\n" +
        "can be square root of map/reduce tasks capacity of the allocated cluster.";
  }

  /* (non-Javadoc)
   * @see org.apache.hadoop.contrib.utils.perfadvisor.diagnostic_rules.DiagnosticRule#getReferenceDetails()
   */
  @Override
  public String getReferenceDetails() {
    String ref = "* Total HDFS Bytes read: "+this._job.getLongValue(JobKeys.HDFS_BYTES_READ)+"\n"+
                 "* Total Map Input Bytes read: "+this._job.getLongValue(JobKeys.MAP_INPUT_BYTES)+"\n"+
                 "* Impact: "+truncate(this._impact);
    return ref;
  }
}
