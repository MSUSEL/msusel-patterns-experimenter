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
import org.junit.Test;
import org.junit.Assert;

/**
 * Test the {@link Gridmix} cpu emulation with custom interval for 
 * gridmix jobs against different input data, submission policies and 
 * user resolvers. Verify the map phase cpu metrics of gridmix jobs 
 * against their original job in the trace. 
 */
public class TestCPUEmulationForMapsWithCustomInterval 
                                            extends GridmixSystemTestCase {
  private static final Log LOG = 
      LogFactory.getLog("TestCPUEmulationForMapsWithCustomInterval.class");
  int execMode = GridMixRunMode.DATA_GENERATION_AND_RUN_GRIDMIX.getValue();

  /**
   * Generate compressed input and run {@link Gridmix} by turning on 
   * cpu emulation feature with custom setting. The {@link Gridmix} should 
   * use the following runtime parameters while running gridmix jobs.
   * Submission Policy : STRESS, User Resolver Mode : SumitterUserResolver
   * Once {@link Gridmix} run is complete, verify maps phase cpu resource 
   * metrics of {@link Gridmix} jobs with their corresponding original
   * in the trace.
   * @throws Exception - if an error occurs.
   */
  @Test
  public void  testCPUEmulatonForMapsWithCompressedInputCase3() 
      throws Exception { 
    final long inputSizeInMB = 1024 * 7;
    String tracePath = getTraceFile("cpu_emul_case1");
    Assert.assertNotNull("Trace file not found!", tracePath);
    String [] runtimeValues = {"LOADJOB",
                               SubmitterUserResolver.class.getName(),
                               "STRESS",
                               inputSizeInMB + "m",
                               tracePath};

    String [] otherArgs = {
        "-D", GridMixConfig.GRIDMIX_CPU_EMULATION + "=" +
              GridMixConfig.GRIDMIX_CPU_EMULATION_PLUGIN,
        "-D", GridMixConfig.GRIDMIX_DISTCACHE_ENABLE + "=false",
        "-D", GridMixConfig.GRIDMIX_CPU_CUSTOM_INTERVAL + "=0.25F"};

    runGridmixAndVerify(runtimeValues, otherArgs, tracePath, execMode);
  }

  /**
   * Generate uncompressed input and run {@link Gridmix} by turning on 
   * cpu emulation feature with custom settings. The {@link Gridmix} 
   * should use the following runtime paramters while running gridmix jobs.
   * Submission Policy: REPLAY  User Resolver Mode: RoundRobinUserResolver
   * Once {@link Gridmix} run is complete, verify the map phase cpu resource 
   * metrics of {@link Gridmix} jobs with their corresponding jobs
   * in the original trace.
   * @throws Exception - if an error occurs.
   */
  @Test
  public void testCPUEmulatonForMapsUnCompressedInputCase4() 
      throws Exception { 
    final long inputSizeInMB = cSize * 200;
    String tracePath = getTraceFile("cpu_emul_case1");
    Assert.assertNotNull("Trace file not found!", tracePath);
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
        "-D", GridMixConfig.GRIDMIX_CPU_EMULATION + "=" + 
              GridMixConfig.GRIDMIX_CPU_EMULATION_PLUGIN,
        "-D", GridMixConfig.GRIDMIX_CPU_CUSTOM_INTERVAL + "=0.35F"};

    runGridmixAndVerify(runtimeValues, otherArgs, tracePath, execMode);
  }
}
