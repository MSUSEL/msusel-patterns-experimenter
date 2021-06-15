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
 * Test the {@link Gridmix} memory emulation feature for {@link Gridmix} jobs 
 * with default progress interval, custom heap memory ratio, different input 
 * data, submission policies and user resolver modes. Verify the total heap 
 * usage of map and reduce tasks of the jobs with corresponding the original job 
 * in the trace. 
 */
public class TestMemEmulForMapsWithCustomHeapMemoryRatio 
    extends GridmixSystemTestCase { 
  private static final Log LOG = 
      LogFactory.getLog("TestMemEmulForMapsWithCustomHeapMemoryRatio.class");

  /**
   * Generate compressed input and run {@link Gridmix} by turning on the
   * memory emulation. The {@link Gridmix} should use the following runtime 
   * parameters while running the jobs.
   * Submission Policy : STRESS, User Resolver Mode : SumitterUserResolver
   * Verify total heap memory usage of the tasks of {@link Gridmix} jobs with 
   * corresponding original job in the trace. 
   * @throws Exception - if an error occurs.
   */
  @Test
  public void testMemoryEmulationForMapsWithCompressedInputCase1() 
     throws Exception {
    final long inputSizeInMB = 1024 * 7;
    String tracePath = getTraceFile("mem_emul_case2");
    Assert.assertNotNull("Trace file has not found.", tracePath);
    String [] runtimeValues = 
            { "LOADJOB",
              SubmitterUserResolver.class.getName(),
              "STRESS",
              inputSizeInMB + "m",
              tracePath};

    String [] otherArgs = { 
            "-D", GridMixConfig.GRIDMIX_MEMORY_EMULATION + "="  +
                  GridMixConfig.GRIDMIX_MEMORY_EMULATION_PLUGIN,
            "-D", GridMixConfig.GRIDMIX_DISTCACHE_ENABLE + "=false",
            "-D", JobContext.JOB_CANCEL_DELEGATION_TOKEN + "=false",
            "-D", GridMixConfig.GRIDMIX_HEAP_FREE_MEMORY_RATIO + "=0.5F"};

    runGridmixAndVerify(runtimeValues, otherArgs, tracePath, 
           GridMixRunMode.DATA_GENERATION_AND_RUN_GRIDMIX.getValue());
  }
  
  /**
   * Generate uncompressed input and run {@link Gridmix} by turning on the
   * memory emulation. The {@link Gridmix} should use the following runtime 
   * parameters while running the jobs.
   *  Submission Policy : STRESS, User Resolver Mode : RoundRobinUserResolver
   * Verify total heap memory usage of tasks of {@link Gridmix} jobs with 
   * corresponding original job in the trace. 
   * @throws Exception - if an error occurs.
   */
  @Test
  public void testMemoryEmulationForMapsWithUncompressedInputCase2() 
      throws Exception {
    final long inputSizeInMB = cSize * 300;
    String tracePath = getTraceFile("mem_emul_case2");
    Assert.assertNotNull("Trace file has not found.", tracePath);
    String [] runtimeValues = 
              { "LOADJOB", 
                RoundRobinUserResolver.class.getName(), 
                "STRESS",
                inputSizeInMB + "m",
                "file://" + UtilsForGridmix.getProxyUsersFile(conf),
                tracePath};

    String [] otherArgs = {
            "-D", GridMixConfig.GRIDMIX_MEMORY_EMULATION + "=" +  
                  GridMixConfig.GRIDMIX_MEMORY_EMULATION_PLUGIN,
            "-D", GridMixConfig.GRIDMIX_DISTCACHE_ENABLE + "=false",
            "-D", GridMixConfig.GRIDMIX_COMPRESSION_ENABLE + "=false",
            "-D", JobContext.JOB_CANCEL_DELEGATION_TOKEN + "=false",
            "-D", GridMixConfig.GRIDMIX_HEAP_FREE_MEMORY_RATIO + "=0.4F"};

    runGridmixAndVerify(runtimeValues, otherArgs, tracePath, 
            GridMixRunMode.DATA_GENERATION_AND_RUN_GRIDMIX.getValue());
  }
}
