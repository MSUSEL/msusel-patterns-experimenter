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
package org.apache.hadoop.mapred.gridmix;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.mapred.gridmix.test.system.GridMixConfig;
import org.apache.hadoop.mapred.gridmix.test.system.GridMixRunMode;
import org.apache.hadoop.mapred.gridmix.test.system.UtilsForGridmix;
import org.apache.hadoop.mapreduce.JobContext;
import org.junit.Assert;
import org.junit.Test;

/**
 *  Verify the Gridmix emulation of HDFS distributed cache files of 
 *  different visibilities. 
 */

public class TestEmulationOfHDFSDCFilesWithDifferentVisibilities 
    extends GridmixSystemTestCase {
  private static final Log LOG = 
     LogFactory.getLog(
         "TestEmulationOfHDFSDCFilesWithDifferentVisibilities.class");
  
  /**
   * Generate input data and HDFS distributed cache files of different
   * visibilities based on given input trace. Verify the Gridmix emulation 
   * of HDFS distributed cache files of different visibilities in 
   * RoundRobinUserResolver mode with SERIAL submission policy.
   * @throws Exception - if an error occurs.
   */
  @Test
  public void testGenerateAndEmulateOfHDFSDCFilesWithDiffVisibilities() 
     throws Exception {
    final long INPUT_SIZE = 1024 * 9;
    final String tracePath = getTraceFile("distcache_case5_trace");
    Assert.assertNotNull("Trace file was not found.", tracePath);
    final String [] runtimeValues = 
                     { "LOADJOB",
                       RoundRobinUserResolver.class.getName(),
                       "STRESS",
                       INPUT_SIZE+"m",
                       "file://" + UtilsForGridmix.getProxyUsersFile(conf),
                       tracePath};

    final String [] otherArgs = { 
        "-D", JobContext.JOB_CANCEL_DELEGATION_TOKEN + "=false",
        "-D", GridmixJob.GRIDMIX_HIGHRAM_EMULATION_ENABLE + "=false",
        "-D", GridMixConfig.GRIDMIX_DISTCACHE_ENABLE +  "=true"
    };
    runGridmixAndVerify(runtimeValues, otherArgs, tracePath, 
        GridMixRunMode.DATA_GENERATION_AND_RUN_GRIDMIX.getValue());
  }
  
  /**
   * Disable the distributed cache emulation and verify the Gridmix jobs
   * whether it emulates or not. 
   * @throws Exception
   */
  @Test
  public void testHDFSDCFilesWithoutEnableDCEmulation() 
     throws Exception {
    final String tracePath = getTraceFile("distcache_case6_trace");
    Assert.assertNotNull("Trace file was not found.", tracePath);
    final String [] runtimeValues ={ "LOADJOB",
                                     SubmitterUserResolver.class.getName(),
                                     "STRESS",
                                     tracePath};
    final String [] otherArgs = {
        "-D", GridmixJob.GRIDMIX_HIGHRAM_EMULATION_ENABLE + "=false",
        "-D", GridMixConfig.GRIDMIX_DISTCACHE_ENABLE + "=false"
    };
    runGridmixAndVerify(runtimeValues, otherArgs, tracePath, 
                        GridMixRunMode.RUN_GRIDMIX.getValue());
  }
}

