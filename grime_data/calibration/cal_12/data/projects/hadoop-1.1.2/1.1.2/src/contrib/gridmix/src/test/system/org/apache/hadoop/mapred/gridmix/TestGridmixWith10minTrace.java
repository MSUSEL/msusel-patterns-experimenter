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
import org.apache.hadoop.mapred.gridmix.test.system.UtilsForGridmix;
import org.apache.hadoop.mapred.gridmix.test.system.GridMixConfig;
import org.junit.Test;

/**
 * Run the Gridmix with 10 minutes MR jobs trace and 
 * verify each job history against the corresponding job story 
 * in a given trace file.
 */
public class TestGridmixWith10minTrace extends GridmixSystemTestCase {
  private static final Log LOG = 
      LogFactory.getLog(TestGridmixWith10minTrace.class);

  /**
   * Generate data and run gridmix by sleep jobs with STRESS submission 
   * policy in a RoundRobinUserResolver mode against 10 minutes trace file.
   * Verify each Gridmix job history with a corresponding job story 
   * in a trace file after completion of all the jobs execution.
   * @throws Exception - if an error occurs.
   */
  @Test
  public void testGridmixWith10minTrace() throws Exception {
    final long inputSizeInMB = cSize * 250;
    final long minFileSize = 200 * 1024 * 1024;
    String [] runtimeValues =
               {"SLEEPJOB",
                RoundRobinUserResolver.class.getName(),
                "SERIAL",
                inputSizeInMB + "m",
                "file://" + UtilsForGridmix.getProxyUsersFile(conf),
                map.get("10m")};

    String [] otherArgs = {
        "-D", GridMixConfig.GRIDMIX_DISTCACHE_ENABLE + "=false",
        "-D", GridMixConfig.GRIDMIX_COMPRESSION_ENABLE + "=false",
        "-D", GridmixJob.GRIDMIX_HIGHRAM_EMULATION_ENABLE + "=false",
        "-D", GridMixConfig.GRIDMIX_MINIMUM_FILE_SIZE + "=" + minFileSize,
        "-D", GridMixConfig.GRIDMIX_JOB_SUBMISSION_QUEUE_IN_TRACE + "=false",
        "-D", GridMixConfig.GRIDMIX_SLEEPJOB_MAPTASK_ONLY + "=true",
        "-D", GridMixConfig.GRIDMIX_SLEEP_MAP_MAX_TIME + "=10"
    };
    String tracePath = map.get("10m");
    runGridmixAndVerify(runtimeValues, otherArgs,tracePath);
  }
}
