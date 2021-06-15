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

import java.util.ConcurrentModificationException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.server.namenode.FSNamesystem;

/**
 * Test empty file creation.
 */
public class TestFileCreationEmpty extends junit.framework.TestCase {
  private boolean isConcurrentModificationException = false;

  /**
   * This test creates three empty files and lets their leases expire.
   * This triggers release of the leases. 
   * The empty files are supposed to be closed by that 
   * without causing ConcurrentModificationException.
   */
  public void testLeaseExpireEmptyFiles() throws Exception {
    final Thread.UncaughtExceptionHandler oldUEH = Thread.getDefaultUncaughtExceptionHandler();
    Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
      public void uncaughtException(Thread t, Throwable e) {
        if (e instanceof ConcurrentModificationException) {
          FSNamesystem.LOG.error("t=" + t, e);
          isConcurrentModificationException = true;
        }
      }
    });

    System.out.println("testLeaseExpireEmptyFiles start");
    final long leasePeriod = 1000;
    final int DATANODE_NUM = 3;

    final Configuration conf = new Configuration();
    conf.setInt("heartbeat.recheck.interval", 1000);
    conf.setInt("dfs.heartbeat.interval", 1);

    // create cluster
    MiniDFSCluster cluster = new MiniDFSCluster(conf, DATANODE_NUM, true, null);
    try {
      cluster.waitActive();
      DistributedFileSystem dfs = (DistributedFileSystem)cluster.getFileSystem();

      // create a new file.
      TestFileCreation.createFile(dfs, new Path("/foo"), DATANODE_NUM);
      TestFileCreation.createFile(dfs, new Path("/foo2"), DATANODE_NUM);
      TestFileCreation.createFile(dfs, new Path("/foo3"), DATANODE_NUM);

      // set the soft and hard limit to be 1 second so that the
      // namenode triggers lease recovery
      cluster.setLeasePeriod(leasePeriod, leasePeriod);
      // wait for the lease to expire
      try {Thread.sleep(5 * leasePeriod);} catch (InterruptedException e) {}

      assertFalse(isConcurrentModificationException);
    } finally {
      Thread.setDefaultUncaughtExceptionHandler(oldUEH);
      cluster.shutdown();
    }
  }
}
