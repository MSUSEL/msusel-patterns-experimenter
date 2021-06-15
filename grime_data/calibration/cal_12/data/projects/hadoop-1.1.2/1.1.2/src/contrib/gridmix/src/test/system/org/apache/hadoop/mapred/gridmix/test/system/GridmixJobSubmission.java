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
package org.apache.hadoop.mapred.gridmix.test.system;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.test.system.JTClient;
import org.junit.Assert;

/**
 * Submit the gridmix jobs. 
 */
public class GridmixJobSubmission {
  private static final Log LOG = 
      LogFactory.getLog(GridmixJobSubmission.class);
  private int gridmixJobCount;
  private Configuration conf;
  private Path gridmixDir;
  private JTClient jtClient;

  public GridmixJobSubmission(Configuration conf, JTClient jtClient , 
                              Path gridmixDir) { 
    this.conf = conf;
    this.jtClient = jtClient;
    this.gridmixDir = gridmixDir;
  }
  
  /**
   * Submit the gridmix jobs.
   * @param runtimeArgs - gridmix common runtime arguments.
   * @param otherArgs - gridmix other runtime arguments.
   * @param traceInterval - trace time interval.
   * @throws Exception
   */
  public void submitJobs(String [] runtimeArgs, 
                         String [] otherArgs, int mode) throws Exception {
    int prvJobCount = jtClient.getClient().getAllJobs().length;
    int exitCode = -1;
    if (otherArgs == null) {
      exitCode = UtilsForGridmix.runGridmixJob(gridmixDir, conf, 
                                               mode, runtimeArgs);
    } else {
      exitCode = UtilsForGridmix.runGridmixJob(gridmixDir, conf, mode,
                                               runtimeArgs, otherArgs);
    }
    Assert.assertEquals("Gridmix jobs have failed.", 0 , exitCode);
    gridmixJobCount = jtClient.getClient().getAllJobs().length - prvJobCount;
  }

  /**
   * Get the submitted jobs count.
   * @return count of no. of jobs submitted for a trace.
   */
  public int getGridmixJobCount() {
     return gridmixJobCount;
  }

  /**
   * Get the job configuration.
   * @return Configuration of a submitted job.
   */
  public Configuration getJobConf() {
    return conf;
  }
}
