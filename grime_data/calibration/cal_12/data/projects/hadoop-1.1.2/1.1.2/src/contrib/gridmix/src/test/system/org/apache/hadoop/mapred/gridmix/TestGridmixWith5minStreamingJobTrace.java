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
import org.junit.Test;
import org.junit.Assert;

/**
 * Run the Gridmix with 5 minutes job trace which has been generated with 
 * streaming jobs histories and verify each job history against 
 * corresponding job story in a given trace file.
 */
public class TestGridmixWith5minStreamingJobTrace 
    extends GridmixSystemTestCase {
  private static final Log LOG = 
      LogFactory.getLog("TestGridmixWith5minStreamingJobTrace.class");

  /**
   * Generate input data and run gridmix by load job with SERIAL submission 
   * policy in a SubmitterUserResolver mode against 5 minutes job trace file 
   * of streaming job. Verify each gridmix job history with a corresponding 
   * job story in a trace file after completion of all the jobs execution.
   * @throws Exception - if an error occurs.
   */
  @Test
  public void testGridmixWith5minStreamJobTrace() throws Exception {
    String tracePath = getTraceFile("5m_stream");
    Assert.assertNotNull("Trace file has not found.", tracePath);
    final long inputSizeInMB = cSize * 200;
    final long bytesPerFile = 150 * 1024 * 1024;
    String [] runtimeValues = {"LOADJOB", 
                              SubmitterUserResolver.class.getName(), 
                              "SERIAL", 
                              inputSizeInMB + "m",
                              tracePath};

    String [] otherArgs = {
        "-D", GridMixConfig.GRIDMIX_KEY_FRC + "=0.5f",
        "-D", GridMixConfig.GRIDMIX_BYTES_PER_FILE + "=" + bytesPerFile,
        "-D", GridMixConfig.GRIDMIX_DISTCACHE_ENABLE + "=false",
        "-D", GridmixJob.GRIDMIX_HIGHRAM_EMULATION_ENABLE + "=false",
        "-D", GridMixConfig.GRIDMIX_COMPRESSION_ENABLE + "=false"
    };

    runGridmixAndVerify(runtimeValues, otherArgs, tracePath);
  }
}
