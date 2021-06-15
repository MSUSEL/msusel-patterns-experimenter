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

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.hadoop.fs.Path;

/**
 * A JUnit test to test Kill Job & Fail Job functionality with local file
 * system.
 */
public class TestJobKillAndFail extends TestCase {

  private static String TEST_ROOT_DIR = new File(System.getProperty(
      "test.build.data", "/tmp")).toURI().toString().replace(' ', '+');

  public void testJobFailAndKill() throws IOException {
    MiniMRCluster mr = null;
    try {
      JobConf jtConf = new JobConf();
      jtConf.set("mapred.jobtracker.instrumentation", 
          JTInstrumentation.class.getName());
      mr = new MiniMRCluster(2, "file:///", 3, null, null, jtConf);
      JTInstrumentation instr = (JTInstrumentation) 
        mr.getJobTrackerRunner().getJobTracker().getInstrumentation();

      // run the TCs
      JobConf conf = mr.createJobConf();
      
      Path inDir = new Path(TEST_ROOT_DIR + "/failkilljob/input");
      Path outDir = new Path(TEST_ROOT_DIR + "/failkilljob/output");
      RunningJob job = UtilsForTests.runJobFail(conf, inDir, outDir);
      // Checking that the Job got failed
      assertEquals(job.getJobState(), JobStatus.FAILED);
      assertTrue(instr.verifyJob());
      assertEquals(1, instr.failed);
      instr.reset();

      
      job = UtilsForTests.runJobKill(conf, inDir, outDir);
      // Checking that the Job got killed
      assertTrue(job.isComplete());
      assertEquals(job.getJobState(), JobStatus.KILLED);
      assertTrue(instr.verifyJob());
      assertEquals(1, instr.killed);
    } finally {
      if (mr != null) {
        mr.shutdown();
      }
    }
  }
  
  static class JTInstrumentation extends JobTrackerInstrumentation {
    volatile int failed;
    volatile int killed;
    volatile int addPrep;
    volatile int decPrep;
    volatile int addRunning;
    volatile int decRunning;

    void reset() {
      failed = 0;
      killed = 0;
      addPrep = 0;
      decPrep = 0;
      addRunning = 0;
      decRunning = 0;
    }

    boolean verifyJob() {
      return addPrep==1 && decPrep==1 && addRunning==1 && decRunning==1;
    }

    public JTInstrumentation(JobTracker jt, JobConf conf) {
      super(jt, conf);
    }

    public synchronized void addPrepJob(JobConf conf, JobID id) 
    {
      addPrep++;
    }
    
    public synchronized void decPrepJob(JobConf conf, JobID id) 
    {
      decPrep++;
    }

    public synchronized void addRunningJob(JobConf conf, JobID id) 
    {
      addRunning++;
    }

    public synchronized void decRunningJob(JobConf conf, JobID id) 
    {
      decRunning++;
    }
    
    public synchronized void failedJob(JobConf conf, JobID id) 
    {
      failed++;
    }

    public synchronized void killedJob(JobConf conf, JobID id) 
    {
      killed++;
    }
  }
  
}
