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

import java.io.IOException;
import java.security.PrivilegedExceptionAction;

import org.apache.hadoop.examples.SleepJob;
import org.apache.hadoop.fs.FileSystem;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ToolRunner;

/**
 * Test a java-based mapred job with LinuxTaskController running the jobs as a
 * user different from the user running the cluster. See
 * {@link ClusterWithLinuxTaskController}
 */
public class TestJobExecutionAsDifferentUser extends
    ClusterWithLinuxTaskController {

  public void testJobExecution()
      throws Exception {
    if (!shouldRun()) {
      return;
    }
    startCluster();
    jobOwner.doAs(new PrivilegedExceptionAction<Object>() {
      public Object run() throws Exception {
        Path inDir = new Path("input");
        Path outDir = new Path("output");

        RunningJob job;

        // Run a job with zero maps/reduces
        job = UtilsForTests.runJob(getClusterConf(), inDir, outDir, 0, 0);
        job.waitForCompletion();
        assertTrue("Job failed", job.isSuccessful());
        assertOwnerShip(outDir);

        // Run a job with 1 map and zero reduces
        job = UtilsForTests.runJob(getClusterConf(), inDir, outDir, 1, 0);
        job.waitForCompletion();
        assertTrue("Job failed", job.isSuccessful());
        assertOwnerShip(outDir);

        // Run a normal job with maps/reduces
        job = UtilsForTests.runJob(getClusterConf(), inDir, outDir, 1, 1);
        job.waitForCompletion();
        assertTrue("Job failed", job.isSuccessful());
        assertOwnerShip(outDir);

        // Run a job with jvm reuse
        JobConf myConf = getClusterConf();
        myConf.set("mapred.job.reuse.jvm.num.tasks", "-1");
        String[] args = { "-m", "6", "-r", "3", "-mt", "1000", "-rt", "1000" };
        assertEquals(0, ToolRunner.run(myConf, new SleepJob(), args));
        return null;
      }
    });
  }

  public void testEnvironment() throws Exception {
    if (!shouldRun()) {
      return;
    }
    startCluster();
    jobOwner.doAs(new PrivilegedExceptionAction<Object>() {
      public Object run() throws Exception {

        TestMiniMRChildTask childTask = new TestMiniMRChildTask();
        Path inDir = new Path("input1");
        Path outDir = new Path("output1");
        try {
          childTask.runTestTaskEnv(getClusterConf(), inDir, outDir, false);
        } catch (IOException e) {
          fail("IOException thrown while running enviroment test."
              + e.getMessage());
        } finally {
          FileSystem outFs = outDir.getFileSystem(getClusterConf());
          if (outFs.exists(outDir)) {
            assertOwnerShip(outDir);
            outFs.delete(outDir, true);
          } else {
            fail("Output directory does not exist" + outDir.toString());
          }
          return null;
        }
      }
    });
  }
}
