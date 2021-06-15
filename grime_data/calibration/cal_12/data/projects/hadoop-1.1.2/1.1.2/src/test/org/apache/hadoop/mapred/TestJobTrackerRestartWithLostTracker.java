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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.junit.Test;


import java.io.*;


/** 
 * This test checks if the jobtracker can detect and recover a tracker that was
 * lost while the jobtracker was down.
 */

public class TestJobTrackerRestartWithLostTracker {
  final Path testDir = new Path("/jt-restart-lost-tt-testing");
  final Path inDir = new Path(testDir, "input");
  final Path shareDir = new Path(testDir, "share");
  final Path outputDir = new Path(testDir, "output");
  
  private JobConf configureJob(JobConf conf, int maps, int reduces,
                               String mapSignal, String redSignal) 
  throws IOException {
    UtilsForTests.configureWaitingJobConf(conf, inDir, outputDir, 
        maps, reduces, "test-jobtracker-restart-with-lost-tt", 
        mapSignal, redSignal);
    return conf;
  }
  
  public void testRecoveryWithLostTracker(MiniDFSCluster dfs,
                                          MiniMRCluster mr) 
  throws IOException {
    FileSystem fileSys = dfs.getFileSystem();
    JobConf jobConf = mr.createJobConf();
    int numMaps = 2;
    int numReds = 1;
    String mapSignalFile = UtilsForTests.getMapSignalFile(shareDir);
    String redSignalFile = UtilsForTests.getReduceSignalFile(shareDir);

    // Enable recovery on restart
    mr.getJobTrackerConf()
        .setBoolean("mapred.jobtracker.restart.recover", true);
    // Configure the jobs
    JobConf job = configureJob(jobConf, numMaps, numReds, 
                               mapSignalFile, redSignalFile);
      
    fileSys.delete(shareDir, true);
    
    // Submit a master job   
    JobClient jobClient = new JobClient(job);
    RunningJob rJob = jobClient.submitJob(job);
    JobID id = rJob.getID();
    
    // wait for the job to be inited
    mr.initializeJob(id);
    
    // Make sure that the master job is 50% completed
    while (UtilsForTests.getJobStatus(jobClient, id).mapProgress() 
           < 0.5f) {
      UtilsForTests.waitFor(100);
    }

    // Kill the jobtracker
    mr.stopJobTracker();

    // Signal the maps to complete
    UtilsForTests.signalTasks(dfs, fileSys, true, mapSignalFile, redSignalFile);
    
    // Kill the 2nd tasktracker
    mr.stopTaskTracker(1);
    
    // Wait for a minute before submitting a job
    UtilsForTests.waitFor(60 * 1000);
    
    // Restart the jobtracker
    mr.startJobTracker();

    // Check if the jobs are still running
    
    // Wait for the JT to be ready
    UtilsForTests.waitForJobTracker(jobClient);

    // Signal the maps to complete
    UtilsForTests.signalTasks(dfs, fileSys, true, mapSignalFile, redSignalFile);
    // Signal the reducers to complete
    UtilsForTests.signalTasks(dfs, fileSys, false, mapSignalFile, 
                              redSignalFile);
    
    UtilsForTests.waitTillDone(jobClient);

    // Check if the tasks on the lost tracker got re-executed
    assertEquals("Tracker killed while the jobtracker was down did not get lost "
                 + "upon restart", 
                 jobClient.getClusterStatus().getTaskTrackers(), 1);

    assertTrue("Job should be successful", rJob.isSuccessful());
  }
  @Test
  public void testRestartWithLostTracker() throws IOException {
    String namenode = null;
    MiniDFSCluster dfs = null;
    MiniMRCluster mr = null;
    FileSystem fileSys = null;

    try {
      Configuration conf = new Configuration();
      conf.setBoolean("dfs.replication.considerLoad", false);
      dfs = new MiniDFSCluster(conf, 1, true, null, null);
      dfs.waitActive();
      fileSys = dfs.getFileSystem();
      
      // clean up
      fileSys.delete(testDir, true);
      
      if (!fileSys.mkdirs(inDir)) {
        throw new IOException("Mkdirs failed to create " + inDir.toString());
      }

      // Write the input file
      UtilsForTests.writeFile(dfs.getNameNode(), conf, 
                              new Path(inDir + "/file"), (short)1);

      dfs.startDataNodes(conf, 1, true, null, null, null, null);
      dfs.waitActive();

      namenode = (dfs.getFileSystem()).getUri().getHost() + ":" 
                 + (dfs.getFileSystem()).getUri().getPort();

      // Make sure that jobhistory leads to a proper job restart
      // So keep the blocksize and the buffer size small
      JobConf jtConf = new JobConf();
      jtConf.set("mapred.jobtracker.job.history.block.size", "1024");
      jtConf.set("mapred.jobtracker.job.history.buffer.size", "1024");
      jtConf.setInt("mapred.tasktracker.reduce.tasks.maximum", 1);
      jtConf.setLong("mapred.tasktracker.expiry.interval", 25 * 1000);
      jtConf.setInt("mapred.reduce.copy.backoff", 4);
      
      mr = new MiniMRCluster(2, namenode, 1, null, null, jtConf);
      
      // Test Lost tracker case
      testRecoveryWithLostTracker(dfs, mr);
    } finally {
      if (mr != null) {
        try {
          mr.shutdown();
        } catch (Exception e) {}
      }
      if (dfs != null) {
        try {
          dfs.shutdown();
        } catch (Exception e) {}
      }
    }
  }

  public static void main(String[] args) throws IOException {
    new TestJobTrackerRestartWithLostTracker().testRestartWithLostTracker();
  }
}
