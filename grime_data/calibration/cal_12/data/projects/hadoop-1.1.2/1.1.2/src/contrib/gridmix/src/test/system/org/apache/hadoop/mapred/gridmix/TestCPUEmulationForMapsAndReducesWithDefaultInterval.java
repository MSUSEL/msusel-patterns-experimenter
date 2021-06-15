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
import org.junit.Test;
import org.junit.Assert;

/**
 * Test cpu emulation with default interval for gridmix jobs 
 * against different input data, submission policies and user resolvers.
 * Verify the cpu resource metrics for both maps and reduces of
 * Gridmix jobs with their corresponding original job in the input trace.
 */
public class TestCPUEmulationForMapsAndReducesWithDefaultInterval 
                                            extends GridmixSystemTestCase { 
  private static final Log LOG = 
          LogFactory.getLog(
              "TestCPUEmulationForMapsAndReducesWithDefaultInterval.class");
  int execMode = GridMixRunMode.DATA_GENERATION_AND_RUN_GRIDMIX.getValue();

 /**
   * Generate compressed input and run {@link Gridmix} by turning on the 
   * cpu emulation feature with default setting. The {@link Gridmix} 
   * should use the following runtime parameters.
   * Submission Policy : REPLAY, UserResovler: RoundRobinUserResolver. 
   * Once the {@link Gridmix} run is complete, verify cpu resource metrics of 
   * {@link Gridmix} jobs with their corresponding original jobs in the trace.
   * @throws Exception - if an error occurs.
   */
  @Test
  public void  testCPUEmulationForMapsAndReducesWithCompressedInputCase5() 
      throws Exception {
    final long inputSizeInMB = 7168;
    String tracePath = getTraceFile("cpu_emul_case2");
    Assert.assertNotNull("Trace file not found!", tracePath);
    String [] runtimeValues = 
            { "LOADJOB",
              RoundRobinUserResolver.class.getName(),
              "REPLAY",
              inputSizeInMB + "m",
              "file://" + UtilsForGridmix.getProxyUsersFile(conf),
              tracePath};

    String [] otherArgs = { 
            "-D", GridMixConfig.GRIDMIX_DISTCACHE_ENABLE + "=false",
            "-D", JobContext.JOB_CANCEL_DELEGATION_TOKEN + "=false",
            "-D", GridMixConfig.GRIDMIX_CPU_EMULATION + "=" + 
                  GridMixConfig.GRIDMIX_CPU_EMULATION_PLUGIN};

    runGridmixAndVerify(runtimeValues, otherArgs, tracePath, execMode); 
  }
  
  /**
   * Generate uncompressed input and run {@link Gridmix} by turning on the 
   * cpu emulation feature with default settings. The {@link Gridmix} 
   * should use the following runtime parameters.
   * Submission Policy : STRESS, UserResovler: SubmitterUserResolver 
   * Once the Gridmix run is complete, verify cpu resource metrics of 
   * {@link Gridmix} jobs with their corresponding original jobs in the trace.
   * @throws Exception - if an error occurs.
   */
  @Test
  public void  testCPUEmulatonForMapsAndReducesWithUncompressedInputCase6() 
      throws Exception {
    final long inputSizeInMB = cSize * 400;
    String tracePath = getTraceFile("cpu_emul_case2");
    Assert.assertNotNull("Trace file not found!", tracePath);
    String [] runtimeValues = 
              { "LOADJOB", 
                SubmitterUserResolver.class.getName(), 
                "STRESS",
                inputSizeInMB + "m",
                tracePath};

    String [] otherArgs = {
            "-D", GridMixConfig.GRIDMIX_DISTCACHE_ENABLE + "=false",
            "-D", GridMixConfig.GRIDMIX_COMPRESSION_ENABLE + "=false",
            "-D", JobContext.JOB_CANCEL_DELEGATION_TOKEN + "=false",
            "-D", GridMixConfig.GRIDMIX_CPU_EMULATION + "=" + 
                  GridMixConfig.GRIDMIX_CPU_EMULATION_PLUGIN     };
    
    runGridmixAndVerify(runtimeValues, otherArgs, tracePath, execMode); 
  }
}

