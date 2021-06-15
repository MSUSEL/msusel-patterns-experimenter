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

import junit.framework.TestCase;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.examples.PiEstimator;
import org.apache.hadoop.fs.FileSystem;

import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.Level;

/**
 * A JUnit test to test configired task limits.
 */
public class TestTaskLimits extends TestCase {

  {     
    ((Log4JLogger)JobInProgress.LOG).getLogger().setLevel(Level.ALL);
  }     

  private static final Log LOG =
    LogFactory.getLog(TestMiniMRWithDFS.class.getName());
  
  static final int NUM_MAPS = 5;
  static final int NUM_SAMPLES = 100;
  
  public static class TestResult {
    public String output;
    public RunningJob job;
    TestResult(RunningJob job, String output) {
      this.job = job;
      this.output = output;
    }
  }
  
  static void runPI(MiniMRCluster mr, JobConf jobconf) throws IOException {
    LOG.info("runPI");
    double estimate = PiEstimator.estimate(NUM_MAPS, NUM_SAMPLES, jobconf).doubleValue();
    double error = Math.abs(Math.PI - estimate);
    System.out.println("PI estimation " + error);
  }

  /**
   * check with a reduce limit of 10 bytes for input to reduce.
   * This should fail since input to reduce estimate is greater
   * than that!
   * @return true on failing the job, false
   * @throws IOException
   */
  private boolean runReduceLimitCheck() throws IOException {
    MiniDFSCluster dfs = null;
    MiniMRCluster mr = null;
    FileSystem fileSys = null;
    boolean success = false;
    try {
      final int taskTrackers = 2;
   
      Configuration conf = new Configuration();
      conf.setInt("mapred.jobtracker.maxtasks.per.job", -1);
      dfs = new MiniDFSCluster(conf, 4, true, null);
      fileSys = dfs.getFileSystem();
      JobConf jconf = new JobConf(conf);
      mr = new MiniMRCluster(0, 0, taskTrackers, fileSys.getUri().toString(), 1,
                             null, null, null, jconf);
      
      JobConf jc = mr.createJobConf();
      jc.setLong("mapreduce.reduce.input.limit", 10L);
      try {
        runPI(mr, jc);
        success = false;
      } catch (IOException e) {
        success = true;
      }
    } finally {
      if (dfs != null) { dfs.shutdown(); }
      if (mr != null) { mr.shutdown(); }
    }
    return success;
  }
  
  /**
   * Run the pi test with a specifix value of 
   * mapred.jobtracker.maxtasks.per.job. Returns true if the job succeeded.
   */
  private boolean runOneTest(int maxTasks) throws IOException {
    MiniDFSCluster dfs = null;
    MiniMRCluster mr = null;
    FileSystem fileSys = null;
    boolean success = false;
    try {
      final int taskTrackers = 2;
   
      Configuration conf = new Configuration();
      conf.setInt("mapred.jobtracker.maxtasks.per.job", maxTasks);
      dfs = new MiniDFSCluster(conf, 4, true, null);
      fileSys = dfs.getFileSystem();
      JobConf jconf = new JobConf(conf);
      mr = new MiniMRCluster(0, 0, taskTrackers, fileSys.getUri().toString(), 1,
                             null, null, null, jconf);
      
      JobConf jc = mr.createJobConf();
      try {
        runPI(mr, jc);
        success = true;
      } catch (IOException e) {
        success = false;
      }
    } finally {
      if (dfs != null) { dfs.shutdown(); }
      if (mr != null) { mr.shutdown(); }
    }
    return success;
  }

  public void testTaskLimits() throws IOException {

    System.out.println("Job 1 running with max set to 2");
    boolean status = runOneTest(2);
    assertTrue(status == false);
    System.out.println("Job 1 failed as expected.");

    // verify that checking this limit works well. The job
    // needs 5 mappers and we set the limit to 7.
    System.out.println("Job 2 running with max set to 7.");
    status = runOneTest(7);
    assertTrue(status == true);
    System.out.println("Job 2 succeeded as expected.");

    System.out.println("Job 3 running with max disabled.");
    status = runOneTest(-1);
    assertTrue(status == true);
    System.out.println("Job 3 succeeded as expected.");
    status = runReduceLimitCheck();
    assertTrue(status == true);
    System.out.println("Success: Reduce limit as expected");
  }
}
