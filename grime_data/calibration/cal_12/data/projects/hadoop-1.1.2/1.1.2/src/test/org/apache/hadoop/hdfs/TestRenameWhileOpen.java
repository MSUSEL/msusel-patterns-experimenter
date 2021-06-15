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

import java.io.IOException;

import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.server.namenode.FSNamesystem;
import org.apache.hadoop.hdfs.server.namenode.LeaseManager;
import org.apache.hadoop.hdfs.server.namenode.NameNode;
import org.apache.log4j.Level;

public class TestRenameWhileOpen extends junit.framework.TestCase {
  {
    ((Log4JLogger)NameNode.stateChangeLog).getLogger().setLevel(Level.ALL);
    ((Log4JLogger)LeaseManager.LOG).getLogger().setLevel(Level.ALL);
    ((Log4JLogger)FSNamesystem.LOG).getLogger().setLevel(Level.ALL);
  }

  /**
   * open /user/dir1/file1 /user/dir2/file2
   * mkdir /user/dir3
   * move /user/dir1 /user/dir3
   */
  public void testWhileOpenRenameParent() throws IOException {
    Configuration conf = new Configuration();
    final int MAX_IDLE_TIME = 2000; // 2s
    conf.setInt("ipc.client.connection.maxidletime", MAX_IDLE_TIME);
    conf.setInt("heartbeat.recheck.interval", 1000);
    conf.setInt("dfs.heartbeat.interval", 1);
    conf.setInt("dfs.safemode.threshold.pct", 1);

    // create cluster
    System.out.println("Test 1*****************************");
    MiniDFSCluster cluster = new MiniDFSCluster(conf, 1, true, null);
    FileSystem fs = null;
    try {
      cluster.waitActive();
      fs = cluster.getFileSystem();
      final int nnport = cluster.getNameNodePort();

      // create file1.
      Path dir1 = new Path("/user/a+b/dir1");
      Path file1 = new Path(dir1, "file1");
      FSDataOutputStream stm1 = TestFileCreation.createFile(fs, file1, 1);
      System.out.println("testFileCreationDeleteParent: "
          + "Created file " + file1);
      TestFileCreation.writeFile(stm1);
      stm1.sync();

      // create file2.
      Path dir2 = new Path("/user/dir2");
      Path file2 = new Path(dir2, "file2");
      FSDataOutputStream stm2 = TestFileCreation.createFile(fs, file2, 1);
      System.out.println("testFileCreationDeleteParent: "
          + "Created file " + file2);
      TestFileCreation.writeFile(stm2);
      stm2.sync();

      // move dir1 while file1 is open
      Path dir3 = new Path("/user/dir3");
      fs.mkdirs(dir3);
      fs.rename(dir1, dir3);

      // create file3
      Path file3 = new Path(dir3, "file3");
      FSDataOutputStream stm3 = TestFileCreation.createFile(fs, file3, 1);
      TestFileCreation.writeFile(stm3);
      // rename file3 to some bad name
      try {
        fs.rename(file3, new Path(dir3, "$ "));
      } catch(Exception e) {
        e.printStackTrace();
      }
      
      // restart cluster with the same namenode port as before.
      // This ensures that leases are persisted in fsimage.
      cluster.shutdown();
      try {Thread.sleep(2*MAX_IDLE_TIME);} catch (InterruptedException e) {}
      cluster = new MiniDFSCluster(nnport, conf, 1, false, true, 
                                   null, null, null);
      cluster.waitActive();

      // restart cluster yet again. This triggers the code to read in
      // persistent leases from fsimage.
      cluster.shutdown();
      try {Thread.sleep(5000);} catch (InterruptedException e) {}
      cluster = new MiniDFSCluster(nnport, conf, 1, false, true, 
                                   null, null, null);
      cluster.waitActive();
      fs = cluster.getFileSystem();

      Path newfile = new Path("/user/dir3/dir1", "file1");
      assertTrue(!fs.exists(file1));
      assertTrue(fs.exists(file2));
      assertTrue(fs.exists(newfile));
      TestFileCreation.checkFullFile(fs, newfile);
    } finally {
      fs.close();
      cluster.shutdown();
    }
  }

  /**
   * open /user/dir1/file1 /user/dir2/file2
   * move /user/dir1 /user/dir3
   */
  public void testWhileOpenRenameParentToNonexistentDir() throws IOException {
    Configuration conf = new Configuration();
    final int MAX_IDLE_TIME = 2000; // 2s
    conf.setInt("ipc.client.connection.maxidletime", MAX_IDLE_TIME);
    conf.setInt("heartbeat.recheck.interval", 1000);
    conf.setInt("dfs.heartbeat.interval", 1);
    conf.setInt("dfs.safemode.threshold.pct", 1);
    System.out.println("Test 2************************************");

    // create cluster
    MiniDFSCluster cluster = new MiniDFSCluster(conf, 1, true, null);
    FileSystem fs = null;
    try {
      cluster.waitActive();
      fs = cluster.getFileSystem();
      final int nnport = cluster.getNameNodePort();

      // create file1.
      Path dir1 = new Path("/user/dir1");
      Path file1 = new Path(dir1, "file1");
      FSDataOutputStream stm1 = TestFileCreation.createFile(fs, file1, 1);
      System.out.println("testFileCreationDeleteParent: "
          + "Created file " + file1);
      TestFileCreation.writeFile(stm1);
      stm1.sync();

      // create file2.
      Path dir2 = new Path("/user/dir2");
      Path file2 = new Path(dir2, "file2");
      FSDataOutputStream stm2 = TestFileCreation.createFile(fs, file2, 1);
      System.out.println("testFileCreationDeleteParent: "
          + "Created file " + file2);
      TestFileCreation.writeFile(stm2);
      stm2.sync();

      // move dir1 while file1 is open
      Path dir3 = new Path("/user/dir3");
      fs.rename(dir1, dir3);

      // restart cluster with the same namenode port as before.
      // This ensures that leases are persisted in fsimage.
      cluster.shutdown();
      try {Thread.sleep(2*MAX_IDLE_TIME);} catch (InterruptedException e) {}
      cluster = new MiniDFSCluster(nnport, conf, 1, false, true, 
                                   null, null, null);
      cluster.waitActive();

      // restart cluster yet again. This triggers the code to read in
      // persistent leases from fsimage.
      cluster.shutdown();
      try {Thread.sleep(5000);} catch (InterruptedException e) {}
      cluster = new MiniDFSCluster(nnport, conf, 1, false, true, 
                                   null, null, null);
      cluster.waitActive();
      fs = cluster.getFileSystem();

      Path newfile = new Path("/user/dir3", "file1");
      assertTrue(!fs.exists(file1));
      assertTrue(fs.exists(file2));
      assertTrue(fs.exists(newfile));
      TestFileCreation.checkFullFile(fs, newfile);
    } finally {
      fs.close();
      cluster.shutdown();
    }
  }

  /**
   * open /user/dir1/file1 
   * mkdir /user/dir2
   * move /user/dir1/file1 /user/dir2/
   */
  public void testWhileOpenRenameToExistentDirectory() throws IOException {
    Configuration conf = new Configuration();
    final int MAX_IDLE_TIME = 2000; // 2s
    conf.setInt("ipc.client.connection.maxidletime", MAX_IDLE_TIME);
    conf.setInt("heartbeat.recheck.interval", 1000);
    conf.setInt("dfs.heartbeat.interval", 1);
    conf.setInt("dfs.safemode.threshold.pct", 1);
    System.out.println("Test 3************************************");

    // create cluster
    MiniDFSCluster cluster = new MiniDFSCluster(conf, 1, true, null);
    FileSystem fs = null;
    try {
      cluster.waitActive();
      fs = cluster.getFileSystem();
      final int nnport = cluster.getNameNodePort();

      // create file1.
      Path dir1 = new Path("/user/dir1");
      Path file1 = new Path(dir1, "file1");
      FSDataOutputStream stm1 = TestFileCreation.createFile(fs, file1, 1);
      System.out.println("testFileCreationDeleteParent: " +
                         "Created file " + file1);
      TestFileCreation.writeFile(stm1);
      stm1.sync();

      Path dir2 = new Path("/user/dir2");
      fs.mkdirs(dir2);

      fs.rename(file1, dir2);

      // restart cluster with the same namenode port as before.
      // This ensures that leases are persisted in fsimage.
      cluster.shutdown();
      try {Thread.sleep(2*MAX_IDLE_TIME);} catch (InterruptedException e) {}
      cluster = new MiniDFSCluster(nnport, conf, 1, false, true, 
                                   null, null, null);
      cluster.waitActive();

      // restart cluster yet again. This triggers the code to read in
      // persistent leases from fsimage.
      cluster.shutdown();
      try {Thread.sleep(5000);} catch (InterruptedException e) {}
      cluster = new MiniDFSCluster(nnport, conf, 1, false, true, 
                                   null, null, null);
      cluster.waitActive();
      fs = cluster.getFileSystem();

      Path newfile = new Path("/user/dir2", "file1");
      assertTrue(!fs.exists(file1));
      assertTrue(fs.exists(newfile));
      TestFileCreation.checkFullFile(fs, newfile);
    } finally {
      fs.close();
      cluster.shutdown();
    }
  }

  /**
   * open /user/dir1/file1 
   * move /user/dir1/file1 /user/dir2/
   */
  public void testWhileOpenRenameToNonExistentDirectory() throws IOException {
    Configuration conf = new Configuration();
    final int MAX_IDLE_TIME = 2000; // 2s
    conf.setInt("ipc.client.connection.maxidletime", MAX_IDLE_TIME);
    conf.setInt("heartbeat.recheck.interval", 1000);
    conf.setInt("dfs.heartbeat.interval", 1);
    conf.setInt("dfs.safemode.threshold.pct", 1);
    System.out.println("Test 4************************************");

    // create cluster
    MiniDFSCluster cluster = new MiniDFSCluster(conf, 1, true, null);
    FileSystem fs = null;
    try {
      cluster.waitActive();
      fs = cluster.getFileSystem();
      final int nnport = cluster.getNameNodePort();

      // create file1.
      Path dir1 = new Path("/user/dir1");
      Path file1 = new Path(dir1, "file1");
      FSDataOutputStream stm1 = TestFileCreation.createFile(fs, file1, 1);
      System.out.println("testFileCreationDeleteParent: "
          + "Created file " + file1);
      TestFileCreation.writeFile(stm1);
      stm1.sync();

      Path dir2 = new Path("/user/dir2");

      fs.rename(file1, dir2);

      // restart cluster with the same namenode port as before.
      // This ensures that leases are persisted in fsimage.
      cluster.shutdown();
      try {Thread.sleep(2*MAX_IDLE_TIME);} catch (InterruptedException e) {}
      cluster = new MiniDFSCluster(nnport, conf, 1, false, true, 
                                   null, null, null);
      cluster.waitActive();

      // restart cluster yet again. This triggers the code to read in
      // persistent leases from fsimage.
      cluster.shutdown();
      try {Thread.sleep(5000);} catch (InterruptedException e) {}
      cluster = new MiniDFSCluster(nnport, conf, 1, false, true, 
                                   null, null, null);
      cluster.waitActive();
      fs = cluster.getFileSystem();

      Path newfile = new Path("/user", "dir2");
      assertTrue(!fs.exists(file1));
      assertTrue(fs.exists(newfile));
      TestFileCreation.checkFullFile(fs, newfile);
    } finally {
      fs.close();
      cluster.shutdown();
    }
  }
}
