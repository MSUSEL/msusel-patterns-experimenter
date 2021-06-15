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
package org.apache.hadoop.ipc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import junit.framework.TestCase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.JobStatus;
import org.apache.hadoop.mapred.MiniMRCluster;
import org.apache.hadoop.net.StandardSocketFactory;

/**
 * This class checks that RPCs can use specialized socket factories.
 */
public class TestSocketFactory extends TestCase {

  /**
   * Check that we can reach a NameNode or a JobTracker using a specific
   * socket factory
   */
  public void testSocketFactory() throws IOException {
    // Create a standard mini-cluster
    Configuration sconf = new Configuration();
    MiniDFSCluster cluster = new MiniDFSCluster(sconf, 1, true, null);
    final int nameNodePort = cluster.getNameNodePort();

    // Get a reference to its DFS directly
    FileSystem fs = cluster.getFileSystem();
    assertTrue(fs instanceof DistributedFileSystem);
    DistributedFileSystem directDfs = (DistributedFileSystem) fs;

    // Get another reference via network using a specific socket factory
    Configuration cconf = new Configuration();
    FileSystem.setDefaultUri(cconf, String.format("hdfs://localhost:%s/",
        nameNodePort + 10));
    cconf.set("hadoop.rpc.socket.factory.class.default",
        "org.apache.hadoop.ipc.DummySocketFactory");
    cconf.set("hadoop.rpc.socket.factory.class.ClientProtocol",
        "org.apache.hadoop.ipc.DummySocketFactory");
    cconf.set("hadoop.rpc.socket.factory.class.JobSubmissionProtocol",
        "org.apache.hadoop.ipc.DummySocketFactory");

    fs = FileSystem.get(cconf);
    assertTrue(fs instanceof DistributedFileSystem);
    DistributedFileSystem dfs = (DistributedFileSystem) fs;

    JobClient client = null;
    MiniMRCluster mr = null;
    try {
      // This will test RPC to the NameNode only.
      // could we test Client-DataNode connections?
      Path filePath = new Path("/dir");

      assertFalse(directDfs.exists(filePath));
      assertFalse(dfs.exists(filePath));

      directDfs.mkdirs(filePath);
      assertTrue(directDfs.exists(filePath));
      assertTrue(dfs.exists(filePath));

      // This will test TPC to a JobTracker
      fs = FileSystem.get(sconf);
      mr = new MiniMRCluster(1, fs.getUri().toString(), 1);
      final int jobTrackerPort = mr.getJobTrackerPort();

      JobConf jconf = new JobConf(cconf);
      jconf.set("mapred.job.tracker", String.format("localhost:%d",
          jobTrackerPort + 10));
      client = new JobClient(jconf);

      JobStatus[] jobs = client.jobsToComplete();
      assertTrue(jobs.length == 0);

    } finally {
      try {
        if (client != null)
          client.close();
      } catch (Exception ignored) {
        // nothing we can do
        ignored.printStackTrace();
      }
      try {
        if (dfs != null)
          dfs.close();

      } catch (Exception ignored) {
        // nothing we can do
        ignored.printStackTrace();
      }
      try {
        if (directDfs != null)
          directDfs.close();

      } catch (Exception ignored) {
        // nothing we can do
        ignored.printStackTrace();
      }
      try {
        if (cluster != null)
          cluster.shutdown();

      } catch (Exception ignored) {
        // nothing we can do
        ignored.printStackTrace();
      }
      if (mr != null) {
        try {
          mr.shutdown();
        } catch (Exception ignored) {
          ignored.printStackTrace();
        }
      }
    }
  }
}

/**
 * Dummy socket factory which shift TPC ports by subtracting 10 when
 * establishing a connection
 */
class DummySocketFactory extends StandardSocketFactory {
  /**
   * Default empty constructor (for use with the reflection API).
   */
  public DummySocketFactory() {
  }

  /* @inheritDoc */
  @Override
  public Socket createSocket() throws IOException {
    return new Socket() {
      @Override
      public void connect(SocketAddress addr, int timeout)
          throws IOException {

        assert (addr instanceof InetSocketAddress);
        InetSocketAddress iaddr = (InetSocketAddress) addr;
        SocketAddress newAddr = null;
        if (iaddr.isUnresolved())
          newAddr =
              new InetSocketAddress(iaddr.getHostName(),
                  iaddr.getPort() - 10);
        else
          newAddr =
              new InetSocketAddress(iaddr.getAddress(), iaddr.getPort() - 10);
        System.out.printf("Test socket: rerouting %s to %s\n", iaddr,
            newAddr);
        super.connect(newAddr, timeout);
      }
    };
  }

  /* @inheritDoc */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof DummySocketFactory))
      return false;
    return true;
  }

  /* @inheritDoc */
  @Override
  public int hashCode() {
    // Dummy hash code (to make find bugs happy)
    return 53;
  }
}
