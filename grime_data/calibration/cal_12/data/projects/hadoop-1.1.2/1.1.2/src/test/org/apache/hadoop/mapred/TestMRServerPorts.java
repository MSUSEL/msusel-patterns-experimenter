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
import java.util.Date;
import java.text.SimpleDateFormat;
import junit.framework.TestCase;
import org.apache.hadoop.hdfs.TestHDFSServerPorts;
import org.apache.hadoop.hdfs.server.datanode.DataNode;
import org.apache.hadoop.hdfs.server.namenode.NameNode;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

/**
 * This test checks correctness of port usage by mapreduce components:
 * JobTracker, and TaskTracker.
 * 
 * The correct behavior is:<br> 
 * - when a specific port is provided the server must either start on that port 
 * or fail by throwing {@link java.net.BindException}.<br>
 * - if the port = 0 (ephemeral) then the server should choose 
 * a free port and start on it.
 */
public class TestMRServerPorts extends TestCase {
  TestHDFSServerPorts hdfs = new TestHDFSServerPorts();

  // Runs the JT in a separate thread
  private static class JTRunner extends Thread {
    JobTracker jt;
    void setJobTracker(JobTracker jt) {
      this.jt = jt;
    }

    public void run() {
      if (jt != null) {
        try {
          jt.offerService();
        } catch (Exception ioe) {}
      }
    }
  }
  /**
   * Check whether the JobTracker can be started.
   */
  private JobTracker startJobTracker(JobConf conf, JTRunner runner) 
  throws IOException {
    conf.set("mapred.job.tracker", "localhost:0");
    conf.set("mapred.job.tracker.http.address", "0.0.0.0:0");
    JobTracker jt = null;
    try {
      String uniqid = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
      jt = JobTracker.startTracker(conf, uniqid, true);
      runner.setJobTracker(jt);
      runner.start();
      conf.set("mapred.job.tracker", "localhost:" + jt.getTrackerPort());
      conf.set("mapred.job.tracker.http.address", 
                            "0.0.0.0:" + jt.getInfoPort());
    } catch(InterruptedException e) {
      throw new IOException(e.getLocalizedMessage());
    }
    return jt;
  }
  
  private void setDataNodePorts(Configuration conf) {
    conf.set("dfs.datanode.address", 
        TestHDFSServerPorts.NAME_NODE_HOST + "0");
    conf.set("dfs.datanode.http.address", 
        TestHDFSServerPorts.NAME_NODE_HTTP_HOST + "0");
    conf.set("dfs.datanode.ipc.address", 
        TestHDFSServerPorts.NAME_NODE_HOST + "0");
  }

  /**
   * Check whether the JobTracker can be started.
   */
  private boolean canStartJobTracker(JobConf conf) 
  throws IOException, InterruptedException {
    JobTracker jt = null;
    try {
      String uniqid = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
      jt = JobTracker.startTracker(conf, uniqid, true);
    } catch(IOException e) {
      if (e instanceof java.net.BindException)
        return false;
      throw e;
    }
    jt.fs.close();
    jt.stopTracker();
    return true;
  }

  /**
   * Check whether the TaskTracker can be started.
   */
  private boolean canStartTaskTracker(JobConf conf) 
  throws IOException, InterruptedException {
    TaskTracker tt = null;
    try {
      tt = new TaskTracker(conf);
    } catch(IOException e) {
      if (e instanceof java.net.BindException)
        return false;
      throw e;
    }
    tt.shutdown();
    return true;
  }

  /**
   * Verify JobTracker port usage.
   */
  public void testJobTrackerPorts() throws Exception {
    NameNode nn = null;
    DataNode dn = null;
    try {
      nn = hdfs.startNameNode();
      setDataNodePorts(hdfs.getConfig());
      dn = hdfs.startDataNode(1, hdfs.getConfig());

      // start job tracker on the same port as name-node
      JobConf conf2 = new JobConf(hdfs.getConfig());
      conf2.set("mapred.job.tracker",
                FileSystem.getDefaultUri(hdfs.getConfig()).toString());
      conf2.set("mapred.job.tracker.http.address",
        TestHDFSServerPorts.NAME_NODE_HTTP_HOST + 0);
      boolean started = canStartJobTracker(conf2);
      assertFalse(started); // should fail

      // bind http server to the same port as name-node
      conf2.set("mapred.job.tracker", TestHDFSServerPorts.NAME_NODE_HOST + 0);
      conf2.set("mapred.job.tracker.http.address",
        hdfs.getConfig().get("dfs.http.address"));
      started = canStartJobTracker(conf2);
      assertFalse(started); // should fail again

      // both ports are different from the name-node ones
      conf2.set("mapred.job.tracker", TestHDFSServerPorts.NAME_NODE_HOST + 0);
      conf2.set("mapred.job.tracker.http.address",
        TestHDFSServerPorts.NAME_NODE_HTTP_HOST + 0);
      started = canStartJobTracker(conf2);
      assertTrue(started); // should start now

    } finally {
      hdfs.stopDataNode(dn);
      hdfs.stopNameNode(nn);
    }
  }

  /**
   * Verify JobTracker port usage.
   */
  public void testTaskTrackerPorts() throws Exception {
    NameNode nn = null;
    DataNode dn = null;
    JobTracker jt = null;
    JTRunner runner = null;
    try {
      nn = hdfs.startNameNode();
      setDataNodePorts(hdfs.getConfig());
      dn = hdfs.startDataNode(2, hdfs.getConfig());

      JobConf conf2 = new JobConf(hdfs.getConfig());
      runner = new JTRunner();
      jt = startJobTracker(conf2, runner);

      // start job tracker on the same port as name-node
      conf2.set("mapred.task.tracker.report.address",
                FileSystem.getDefaultUri(hdfs.getConfig()).toString());
      conf2.set("mapred.task.tracker.http.address",
        TestHDFSServerPorts.NAME_NODE_HTTP_HOST + 0);
      boolean started = canStartTaskTracker(conf2);
      assertFalse(started); // should fail

      // bind http server to the same port as name-node
      conf2.set("mapred.task.tracker.report.address",
        TestHDFSServerPorts.NAME_NODE_HOST + 0);
      conf2.set("mapred.task.tracker.http.address",
        hdfs.getConfig().get("dfs.http.address"));
      started = canStartTaskTracker(conf2);
      assertFalse(started); // should fail again

      // both ports are different from the name-node ones
      conf2.set("mapred.task.tracker.report.address",
        TestHDFSServerPorts.NAME_NODE_HOST + 0);
      conf2.set("mapred.task.tracker.http.address",
        TestHDFSServerPorts.NAME_NODE_HTTP_HOST + 0);
      started = canStartTaskTracker(conf2);
      assertTrue(started); // should start now
    } finally {
      if (jt != null) {
        jt.fs.close();
        jt.stopTracker();
        runner.interrupt();
        runner.join();
      }
      hdfs.stopDataNode(dn);
      hdfs.stopNameNode(nn);
    }
  }
}
