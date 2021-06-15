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

import junit.framework.TestCase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.mapred.JobConf;

public class TestMapredHeartbeat extends TestCase {
  public void testJobDirCleanup() throws IOException {
    MiniMRCluster mr = null;
    try {
      // test the default heartbeat interval
      int taskTrackers = 2;
      JobConf conf = new JobConf();
      mr = new MiniMRCluster(taskTrackers, "file:///", 3, 
          null, null, conf);
      JobClient jc = new JobClient(mr.createJobConf());
      while(jc.getClusterStatus().getTaskTrackers() != taskTrackers) {
        UtilsForTests.waitFor(100);
      }
      assertEquals(MRConstants.HEARTBEAT_INTERVAL_MIN, 
        mr.getJobTrackerRunner().getJobTracker().getNextHeartbeatInterval());
      mr.shutdown(); 
      
      // test configured heartbeat interval
      taskTrackers = 5;
      conf.setInt(JobTracker.JT_HEARTBEATS_IN_SECOND, 1);
      mr = new MiniMRCluster(taskTrackers, "file:///", 3, 
          null, null, conf);
      jc = new JobClient(mr.createJobConf());
      while(jc.getClusterStatus().getTaskTrackers() != taskTrackers) {
        UtilsForTests.waitFor(100);
      }
      assertEquals(taskTrackers * 1000, 
        mr.getJobTrackerRunner().getJobTracker().getNextHeartbeatInterval());
      mr.shutdown(); 
      
      // test configured heartbeat interval is capped with min value
      taskTrackers = 5;
      conf.setInt(JobTracker.JT_HEARTBEATS_IN_SECOND, 
          (int)Math.ceil((taskTrackers * 1000.0) / MRConstants.HEARTBEAT_INTERVAL_MIN) );
      mr = new MiniMRCluster(taskTrackers, "file:///", 3, 
          null, null, conf);
      jc = new JobClient(mr.createJobConf());
      while(jc.getClusterStatus().getTaskTrackers() != taskTrackers) {
        UtilsForTests.waitFor(100);
      }
      assertEquals(MRConstants.HEARTBEAT_INTERVAL_MIN, 
        mr.getJobTrackerRunner().getJobTracker().getNextHeartbeatInterval());
    } finally {
      if (mr != null) { mr.shutdown(); }
    }
  }
  
  public void testOutOfBandHeartbeats() throws Exception {
    MiniDFSCluster dfs = null;
    MiniMRCluster mr = null;
    try {
      Configuration conf = new Configuration();
      dfs = new MiniDFSCluster(conf, 4, true, null);
      
      int taskTrackers = 1;
      JobConf jobConf = new JobConf();
      jobConf.setFloat(JobTracker.JT_HEARTBEATS_SCALING_FACTOR, 30.0f);
      jobConf.setBoolean(TaskTracker.TT_OUTOFBAND_HEARBEAT, true);
      mr = new MiniMRCluster(taskTrackers, 
                             dfs.getFileSystem().getUri().toString(), 3, 
                             null, null, jobConf);
      long start = System.currentTimeMillis();
      TestMiniMRDFSSort.runRandomWriter(mr.createJobConf(), new Path("rw"));
      long end = System.currentTimeMillis();
      
      final int expectedRuntimeSecs = 120;
      final int runTimeSecs = (int)((end-start) / 1000); 
      System.err.println("Runtime is " + runTimeSecs);
      assertEquals("Actual runtime " + runTimeSecs + "s not less than expected " +
                   "runtime of " + expectedRuntimeSecs + "s!", 
                   true, (runTimeSecs <= 120));
    } finally {
      if (mr != null) { mr.shutdown(); }
      if (dfs != null) { dfs.shutdown(); }
    }
  }

}


