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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class responsible for modeling the resource consumption of running tasks.
 * 
 * For now, we just do temp space for maps
 * 
 * There is one ResourceEstimator per JobInProgress
 *
 */
class ResourceEstimator {

  //Log with JobInProgress
  private static final Log LOG = LogFactory.getLog(
      "org.apache.hadoop.mapred.ResourceEstimator");

  private long completedMapsInputSize;
  private long completedMapsOutputSize;

  private int completedMapsUpdates;
  final private JobInProgress job;
  private int threshholdToUse;

  public ResourceEstimator(JobInProgress job) {
    this.job = job;
    threshholdToUse = job.desiredMaps()/ 10;
  }

  protected synchronized void updateWithCompletedTask(TaskStatus ts, 
      TaskInProgress tip) {

    //-1 indicates error, which we don't average in.
    if(tip.isMapTask() &&  ts.getOutputSize() != -1)  {
      completedMapsUpdates++;

      completedMapsInputSize+=(tip.getMapInputSize()+1);
      completedMapsOutputSize+=ts.getOutputSize();

      if(LOG.isDebugEnabled()) {
        LOG.debug("completedMapsUpdates:"+completedMapsUpdates+"  "+
                  "completedMapsInputSize:"+completedMapsInputSize+"  " +
                  "completedMapsOutputSize:"+completedMapsOutputSize);
      }
    }
  }

  /**
   * @return estimated length of this job's total map output
   */
  protected synchronized long getEstimatedTotalMapOutputSize()  {
    if(completedMapsUpdates < threshholdToUse) {
      return 0;
    } else {
      long inputSize = job.getInputLength() + job.desiredMaps(); 
      //add desiredMaps() so that randomwriter case doesn't blow up
      //the multiplication might lead to overflow, casting it with
      //double prevents it
      long estimate = Math.round(((double)inputSize * 
          completedMapsOutputSize * 2.0)/completedMapsInputSize);
      if (LOG.isDebugEnabled()) {
        LOG.debug("estimate total map output will be " + estimate);
      }
      return estimate;
    }
  }
  
  /**
   * @return estimated length of this job's average map output
   */
  long getEstimatedMapOutputSize() {
    long estimate = 0L;
    if (job.desiredMaps() > 0) {
      estimate = getEstimatedTotalMapOutputSize()  / job.desiredMaps();
    }
    return estimate;
  }

  /**
   * 
   * @return estimated length of this job's average reduce input
   */
  long getEstimatedReduceInputSize() {
    if(job.desiredReduces() == 0) {//no reduce output, so no size
      return 0;
    } else {
      return getEstimatedTotalMapOutputSize() / job.desiredReduces();
      //estimate that each reduce gets an equal share of total map output
    }
  }
  
  /**
   * the number of maps after which reduce starts launching
   * @param numMaps the number of maps after which reduce starts
   * launching. It acts as the upper bound for the threshhold, so
   * that we can get right estimates before we reach these number
   * of maps.
   */
  void setThreshhold(int numMaps) {
    threshholdToUse = Math.min(threshholdToUse, numMaps);
  }
}
