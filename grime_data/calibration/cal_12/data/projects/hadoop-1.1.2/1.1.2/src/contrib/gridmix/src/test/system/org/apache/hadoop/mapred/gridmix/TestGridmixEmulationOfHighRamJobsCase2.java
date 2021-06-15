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

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.hadoop.mapred.gridmix.test.system.GridMixConfig;
import org.apache.hadoop.mapred.gridmix.GridmixJob;
import org.apache.hadoop.mapred.gridmix.test.system.UtilsForGridmix;
import org.junit.Test;
import org.junit.Assert;

/**
 * Run the {@link Gridmix} with a high ram jobs trace and 
 * verify each {@link Gridmix} job whether it honors the high ram or not.
 * In the trace the jobs should use the high ram only for maps.
 */
public class TestGridmixEmulationOfHighRamJobsCase2 
    extends GridmixSystemTestCase { 
  private static final Log LOG = 
      LogFactory.getLog("TestGridmixEmulationOfHighRamJobsCase2.class");

 /**
   * Generate input data and run {@link Gridmix} with a high ram jobs trace 
   * as a load job and REPALY submission policy in a RoundRobinUserResolver 
   * mode. Verify each {@link Gridmix} job whether it honors the high ram or not
   * after completion of execution. 
   * @throws Exception - if an error occurs.
   */
  @Test
  public void testEmulationOfHighRamForMapsOfMRJobs() 
      throws Exception { 
    final long inputSizeInMB = cSize * 300;
    String tracePath = getTraceFile("highram_mr_jobs_case2");
    Assert.assertNotNull("Trace file has not found.", tracePath);
    String [] runtimeValues = 
               {"LOADJOB",
                RoundRobinUserResolver.class.getName(),
                "REPLAY",
                inputSizeInMB + "m",
                "file://" + UtilsForGridmix.getProxyUsersFile(conf),
                tracePath};

    String [] otherArgs = {
               "-D", GridMixConfig.GRIDMIX_DISTCACHE_ENABLE + "=false", 
               "-D", GridMixConfig.GRIDMIX_COMPRESSION_ENABLE + "=false", 
               "-D", GridmixJob.GRIDMIX_HIGHRAM_EMULATION_ENABLE + "=true"};

    validateTaskMemoryParamters(tracePath, true);
    runGridmixAndVerify(runtimeValues, otherArgs, tracePath);
  }
}
