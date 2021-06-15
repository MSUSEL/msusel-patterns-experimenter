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
public class MapsReExecutionImpact extends DiagnosticTest {

  private double _impact;
  private JobStatistics _job;
  private long _percentMapsReExecuted;
  
  /**
   * 
   */
  public MapsReExecutionImpact() {
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
     * Calculate and return the impact
     */
    this._impact = ((job.getLongValue(JobKeys.LAUNCHED_MAPS) - job.getLongValue(JobKeys.TOTAL_MAPS))/job.getLongValue(JobKeys.TOTAL_MAPS));
    this._percentMapsReExecuted = Math.round(this._impact * 100);
    return this._impact;
  }

  
  /* (non-Javadoc)
   * @see org.apache.hadoop.contrib.utils.perfadvisor.diagnostic_rules.DiagnosticRule#getAdvice()
   */
  @Override
  public String getPrescription() {
    return 
    "* Need careful evaluation of why maps are re-executed. \n" +
      "  * It could be due to some set of unstable cluster nodes.\n" +
      "  * It could be due application specific failures.";
  }

  /* (non-Javadoc)
   * @see org.apache.hadoop.contrib.utils.perfadvisor.diagnostic_rules.DiagnosticRule#getReferenceDetails()
   */
  @Override
  public String getReferenceDetails() {
    String ref = "* Total Map Tasks: "+this._job.getLongValue(JobKeys.TOTAL_MAPS)+"\n"+
                 "* Launched Map Tasks: "+this._job.getLongValue(JobKeys.LAUNCHED_MAPS)+"\n"+
                 "* Percent Maps ReExecuted: "+this._percentMapsReExecuted+"\n"+
                 "* Impact: "+truncate(this._impact);
    return ref;
  }
}
