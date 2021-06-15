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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.examples.SleepJob;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.util.ToolRunner;

public class TestJobDirCleanup extends TestCase {
  //The testcase brings up a cluster with many trackers, and
  //runs a job with a single map and many reduces. The check is 
  //to see whether the job directories are cleaned up at the
  //end of the job (indirectly testing whether all tasktrackers
  //got a KillJobAction).
  private static final Log LOG =
        LogFactory.getLog(TestEmptyJob.class.getName());
  private void runSleepJob(JobConf conf) throws Exception {
    String[] args = { "-m", "1", "-r", "10", "-mt", "1000", "-rt", "10000" };
    ToolRunner.run(conf, new SleepJob(), args);
  }
  public void testJobDirCleanup() throws IOException {
    String namenode = null;
    MiniDFSCluster dfs = null;
    MiniMRCluster mr = null;
    FileSystem fileSys = null;
    try {
      final int taskTrackers = 10;
      final int jobTrackerPort = 60050;
      Configuration conf = new Configuration();
      JobConf mrConf = new JobConf();
      mrConf.set("mapred.tasktracker.reduce.tasks.maximum", "1");
      dfs = new MiniDFSCluster(conf, 1, true, null);
      fileSys = dfs.getFileSystem();
      namenode = fileSys.getUri().toString();
      mr = new MiniMRCluster(10, namenode, 3, 
          null, null, mrConf);
      // make cleanup inline sothat validation of existence of these directories
      // can be done
      mr.setInlineCleanupThreads();
      final String jobTrackerName = "localhost:" + mr.getJobTrackerPort();
      JobConf jobConf = mr.createJobConf();
      runSleepJob(jobConf);
      for(int i=0; i < taskTrackers; ++i) {
        String jobDirStr = mr.getTaskTrackerLocalDir(i)+
                           "/taskTracker/jobcache";
        File jobDir = new File(jobDirStr);
        String[] contents = jobDir.list();
        assertTrue("Contents of " + jobDir + " not cleanup.",
                   (contents == null || contents.length == 0));
      }
    } catch (Exception ee){
    } finally {
      if (fileSys != null) { fileSys.close(); }
      if (dfs != null) { dfs.shutdown(); }
      if (mr != null) { mr.shutdown(); }
    }
  }
}


