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
import java.util.HashSet;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.lib.IdentityReducer;

/**
 * Test node blacklisting. This testcase tests
 *   - node blacklisting along with node refresh 
 */
public class TestNodeBlacklisting extends TestCase {
  public static final Log LOG = LogFactory.getLog(TestNodeBlacklisting.class);
  private static final Path TEST_DIR = 
    new Path(System.getProperty("test.build.data", "/tmp"), "node-bklisting");

  // Mapper that fails once for the first time
  static class FailOnceMapper extends MapReduceBase implements
      Mapper<WritableComparable, Writable, WritableComparable, Writable> {

    private boolean shouldFail = false;
    public void map(WritableComparable key, Writable value,
        OutputCollector<WritableComparable, Writable> out, Reporter reporter)
        throws IOException {

      if (shouldFail) {
        throw new RuntimeException("failing map");
      }
    }
     
    @Override
    public void configure(JobConf conf) {
      TaskAttemptID id = TaskAttemptID.forName(conf.get("mapred.task.id"));
      shouldFail = id.getId() == 0 && id.getTaskID().getId() == 0; 
    }
  }
   
  /**
   * Check refreshNodes for decommissioning blacklisted nodes. 
   */
  public void testBlacklistedNodeDecommissioning() throws Exception {
    LOG.info("Testing blacklisted node decommissioning");
    MiniMRCluster mr = null;
    JobTracker jt = null;
     
    try {
      // start mini mr
      JobConf jtConf = new JobConf();
      jtConf.set("mapred.max.tracker.blacklists", "1");
      mr = new MiniMRCluster(0, 0, 2, "file:///", 1, null, null, null, jtConf);
      jt = mr.getJobTrackerRunner().getJobTracker();

      assertEquals("Trackers not up", 2, jt.taskTrackers().size());
      // validate the total tracker count
      assertEquals("Active tracker count mismatch", 
                   2, jt.getClusterStatus(false).getTaskTrackers());
      // validate blacklisted count
      assertEquals("Blacklisted tracker count mismatch", 
                   0, jt.getClusterStatus(false).getBlacklistedTrackers());
      
      // run a failing job to blacklist the tracker
      JobConf jConf = mr.createJobConf();
      jConf.set("mapred.max.tracker.failures", "1");
      jConf.setJobName("test-job-fail-once");
      jConf.setMapperClass(FailOnceMapper.class);
      jConf.setReducerClass(IdentityReducer.class);
      jConf.setNumMapTasks(1);
      jConf.setNumReduceTasks(0);

      RunningJob job = 
        UtilsForTests.runJob(jConf, new Path(TEST_DIR, "in"), 
                             new Path(TEST_DIR, "out"));
      job.waitForCompletion();

      // validate the total tracker count
      // (graylisted trackers remain active, unlike blacklisted ones)
      assertEquals("Active tracker count mismatch",
                   2, jt.getClusterStatus(false).getTaskTrackers());
      // validate graylisted count
      assertEquals("Graylisted tracker count mismatch",
                   1, jt.getClusterStatus(false).getGraylistedTrackers());

      // find the graylisted tracker
      String trackerName = null;
      for (TaskTrackerStatus status : jt.taskTrackers()) {
        if (jt.isGraylisted(status.getTrackerName())) {
          trackerName = status.getTrackerName();
          break;
        }
      }
      // get the hostname
      String hostToDecommission = 
        JobInProgress.convertTrackerNameToHostName(trackerName);
      LOG.info("Decommissioning tracker " + hostToDecommission);

      // decommission the node
      HashSet<String> decom = new HashSet<String>(1);
      decom.add(hostToDecommission);
      jt.decommissionNodes(decom);

      // validate
      // check the cluster status and tracker size
      assertEquals("Tracker is not lost upon host decommissioning", 
                   1, jt.getClusterStatus(false).getTaskTrackers());
      assertEquals("Graylisted tracker count incorrect in cluster status "
                   + "after decommissioning", 
                   0, jt.getClusterStatus(false).getGraylistedTrackers());
      assertEquals("Tracker is not lost upon host decommissioning", 
                   1, jt.taskTrackers().size());
    } finally {
      if (mr != null) {
        mr.shutdown();
        mr = null;
        jt = null;
        FileUtil.fullyDelete(new File(TEST_DIR.toString()));
      }
    }
  }
}
