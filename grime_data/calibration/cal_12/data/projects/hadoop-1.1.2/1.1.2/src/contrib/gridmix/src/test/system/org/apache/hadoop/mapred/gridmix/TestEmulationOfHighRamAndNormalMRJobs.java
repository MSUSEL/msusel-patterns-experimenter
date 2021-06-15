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
import org.apache.hadoop.mapred.gridmix.test.system.GridMixRunMode;
import org.apache.hadoop.mapred.gridmix.test.system.UtilsForGridmix;
import org.junit.Test;
import org.junit.Assert;

/**
 * Run the {@link Gridmix} with combination of high ram and normal jobs of
 * trace and verify whether high ram jobs{@link Gridmix} are honoring or not.
 * Normal MR jobs should not honors the high ram emulation.
 */
public class TestEmulationOfHighRamAndNormalMRJobs
    extends GridmixSystemTestCase { 
  private static final Log LOG = 
      LogFactory.getLog("TestEmulationOfHighRamAndNormalMRJobs.class");

  /**
   * Generate input data and run the combination normal and high ram 
   * {@link Gridmix} jobs as load job and STRESS submission policy 
   * in a SubmitterUserResolver mode. Verify whether each {@link Gridmix} 
   * job honors the high ram or not after completion of execution. 
   * @throws Exception - if an error occurs.
   */
  @Test
  public void testEmulationOfHighRamForReducersOfMRJobs() 
      throws Exception { 
    final long inputSizeInMB = cSize * 250;
    String tracePath = getTraceFile("highram_mr_jobs_case4");
    Assert.assertNotNull("Trace file has not found.", tracePath);
    String [] runtimeArgs = {"LOADJOB",
                               SubmitterUserResolver.class.getName(),
                               "SERIAL",
                               inputSizeInMB + "m",
                               tracePath};
    String [] otherArgs = {
            "-D", GridMixConfig.GRIDMIX_DISTCACHE_ENABLE + "=false", 
            "-D", GridMixConfig.GRIDMIX_COMPRESSION_ENABLE + "=false", 
            "-D", GridMixConfig.GRIDMIX_HIGH_RAM_JOB_ENABLE + "=true"};

    validateTaskMemoryParamters(tracePath, true);
    runGridmixAndVerify(runtimeArgs, otherArgs, tracePath);
  }
}
