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
package org.apache.hadoop.hdfs;

import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.server.datanode.DataNode;
import org.apache.hadoop.hdfs.server.namenode.FSNamesystem;
import org.apache.hadoop.hdfs.server.namenode.LeaseManager;
import org.apache.hadoop.hdfs.server.protocol.InterDatanodeProtocol;
import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.Level;

/**
 * This class tests that a file need not be closed before its
 * data can be read by another client.
 */
public class TestFileCreationClient extends junit.framework.TestCase {
  static final String DIR = "/" + TestFileCreationClient.class.getSimpleName() + "/";

  {
    ((Log4JLogger)DataNode.LOG).getLogger().setLevel(Level.ALL);
    ((Log4JLogger)LeaseManager.LOG).getLogger().setLevel(Level.ALL);
    ((Log4JLogger)FSNamesystem.LOG).getLogger().setLevel(Level.ALL);
    ((Log4JLogger)InterDatanodeProtocol.LOG).getLogger().setLevel(Level.ALL);
  }

  /** Test lease recovery Triggered by DFSClient. */
  public void testClientTriggeredLeaseRecovery() throws Exception {
    final int REPLICATION = 3;
    Configuration conf = new Configuration();
    conf.setInt("dfs.datanode.handler.count", 1);
    conf.setInt("dfs.replication", REPLICATION);
    MiniDFSCluster cluster = new MiniDFSCluster(conf, REPLICATION, true, null);

    try {
      final FileSystem fs = cluster.getFileSystem();
      final Path dir = new Path("/wrwelkj");
      
      SlowWriter[] slowwriters = new SlowWriter[10];
      for(int i = 0; i < slowwriters.length; i++) {
        slowwriters[i] = new SlowWriter(fs, new Path(dir, "file" + i));
      }

      try {
        for(int i = 0; i < slowwriters.length; i++) {
          slowwriters[i].start();
        }

        Thread.sleep(1000);                       // let writers get started

        //stop a datanode, it should have least recover.
        cluster.stopDataNode(AppendTestUtil.nextInt(REPLICATION));
        
        //let the slow writer writes a few more seconds
        System.out.println("Wait a few seconds");
        Thread.sleep(5000);
      }
      finally {
        for(int i = 0; i < slowwriters.length; i++) {
          if (slowwriters[i] != null) {
            slowwriters[i].running = false;
            slowwriters[i].interrupt();
          }
        }
        for(int i = 0; i < slowwriters.length; i++) {
          if (slowwriters[i] != null) {
            slowwriters[i].join();
          }
        }
      }

      //Verify the file
      System.out.println("Verify the file");
      for(int i = 0; i < slowwriters.length; i++) {
        System.out.println(slowwriters[i].filepath + ": length="
            + fs.getFileStatus(slowwriters[i].filepath).getLen());
        FSDataInputStream in = null;
        try {
          in = fs.open(slowwriters[i].filepath);
          for(int j = 0, x; (x = in.read()) != -1; j++) {
            assertEquals(j, x);
          }
        }
        finally {
          IOUtils.closeStream(in);
        }
      }
    } finally {
      if (cluster != null) {cluster.shutdown();}
    }
  }

  static class SlowWriter extends Thread {
    final FileSystem fs;
    final Path filepath;
    boolean running = true;
    
    SlowWriter(FileSystem fs, Path filepath) {
      super(SlowWriter.class.getSimpleName() + ":" + filepath);
      this.fs = fs;
      this.filepath = filepath;
    }

    public void run() {
      FSDataOutputStream out = null;
      int i = 0;
      try {
        out = fs.create(filepath);
        for(; running; i++) {
          System.out.println(getName() + " writes " + i);
          out.write(i);
          out.sync();
          sleep(100);
        }
      }
      catch(Exception e) {
        System.out.println(getName() + " dies: e=" + e);
      }
      finally {
        System.out.println(getName() + ": i=" + i);
        IOUtils.closeStream(out);
      }
    }        
  }
}
