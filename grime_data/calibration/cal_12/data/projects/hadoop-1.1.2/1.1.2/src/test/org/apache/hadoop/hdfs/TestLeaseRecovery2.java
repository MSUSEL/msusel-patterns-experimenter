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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.protocol.AlreadyBeingCreatedException;
import org.apache.hadoop.hdfs.server.datanode.DataNode;
import org.apache.hadoop.hdfs.server.namenode.FSNamesystem;
import org.apache.hadoop.hdfs.server.namenode.LeaseManager;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.log4j.Level;

public class TestLeaseRecovery2 extends junit.framework.TestCase {
  {
    ((Log4JLogger)DataNode.LOG).getLogger().setLevel(Level.ALL);
    ((Log4JLogger)LeaseManager.LOG).getLogger().setLevel(Level.ALL);
    ((Log4JLogger)FSNamesystem.LOG).getLogger().setLevel(Level.ALL);
  }

  static final long BLOCK_SIZE = 1024;
  static final int FILE_SIZE = 1024*16;
  static final short REPLICATION_NUM = (short)3;
  private static byte[] buffer = new byte[FILE_SIZE];
  private final Configuration conf = new Configuration();
  private final int bufferSize = conf.getInt("io.file.buffer.size", 4096);
  
  static private String fakeUsername = "fakeUser1";
  static private String fakeGroup = "supergroup";

  public void testBlockSynchronization() throws Exception {
    final long softLease = 1000;
    final long hardLease = 60 * 60 *1000;
    conf.setLong("dfs.block.size", BLOCK_SIZE);
    conf.setInt("dfs.heartbeat.interval", 1);
  //  conf.setInt("io.bytes.per.checksum", 16);

    // create fake mapping user to group and set it to the conf
    // NOTE. this must be done at the beginning, before first call to mapping
    // functions
    Map<String, String []> u2g_map = new HashMap<String, String []>(1);
    u2g_map.put(fakeUsername, new String[] {fakeGroup});
    DFSTestUtil.updateConfWithFakeGroupMapping(conf, u2g_map);

    MiniDFSCluster cluster = null;
    byte[] actual = new byte[FILE_SIZE];

    try {
      cluster = new MiniDFSCluster(conf, 5, true, null);
      cluster.waitActive();

      //create a file
      DistributedFileSystem dfs = (DistributedFileSystem)cluster.getFileSystem();
      int size = AppendTestUtil.nextInt(FILE_SIZE);
      Path filepath = createFile(dfs, size, true);

      // set the soft limit to be 1 second so that the
      // namenode triggers lease recovery on next attempt to write-for-open.
      cluster.setLeasePeriod(softLease, hardLease);

      recoverLeaseUsingCreate(filepath);
      verifyFile(dfs, filepath, actual, size);

      //test recoverLease
      // set the soft limit to be 1 hour but recoverLease should
      // close the file immediately
      cluster.setLeasePeriod(hardLease, hardLease);
      size = AppendTestUtil.nextInt(FILE_SIZE);
      filepath = createFile(dfs, size, false);

      // test recoverLese from a different client
      recoverLease(filepath, null);
      verifyFile(dfs, filepath, actual, size);

      // test recoverlease from the same client
      size = AppendTestUtil.nextInt(FILE_SIZE);
      filepath = createFile(dfs, size, false);
      
      // create another file using the same client
      Path filepath1 = new Path("/foo" + AppendTestUtil.nextInt());
      FSDataOutputStream stm = dfs.create(filepath1, true,
          bufferSize, REPLICATION_NUM, BLOCK_SIZE);
      
      // recover the first file
      recoverLease(filepath, dfs);
      verifyFile(dfs, filepath, actual, size);

      // continue to write to the second file
      stm.write(buffer, 0, size);
      stm.close();
      verifyFile(dfs, filepath1, actual, size);
    }
    finally {
      try {
        if (cluster != null) {cluster.getFileSystem().close();cluster.shutdown();}
      } catch (Exception e) {
        // ignore
      }
    }
  }
  
  private void recoverLease(Path filepath, DistributedFileSystem dfs2) throws Exception {
    if (dfs2==null) {
      UserGroupInformation ugi = 
        UserGroupInformation.createUserForTesting(fakeUsername, 
                                                  new String [] { fakeGroup});
      dfs2 = (DistributedFileSystem) DFSTestUtil.getFileSystemAs(ugi, conf);
    }
    
    while (!dfs2.recoverLease(filepath)) {
      AppendTestUtil.LOG.info("sleep " + 5000 + "ms");
      Thread.sleep(5000);
    }
  }
  
  // try to re-open the file before closing the previous handle. This
  // should fail but will trigger lease recovery.
  private Path createFile(DistributedFileSystem dfs, int size,
      boolean triggerSoftLease) throws IOException, InterruptedException {
    // create a random file name
    String filestr = "/foo" + AppendTestUtil.nextInt();
    System.out.println("filestr=" + filestr);
    Path filepath = new Path(filestr);
    FSDataOutputStream stm = dfs.create(filepath, true,
        bufferSize, REPLICATION_NUM, BLOCK_SIZE);
    assertTrue(dfs.dfs.exists(filestr));

    // write random number of bytes into it.
    System.out.println("size=" + size);
    stm.write(buffer, 0, size);

    // sync file
    AppendTestUtil.LOG.info("sync");
    stm.sync();
    if (triggerSoftLease) {
      AppendTestUtil.LOG.info("leasechecker.interruptAndJoin()");
      dfs.dfs.getLeaseRenewer().interruptAndJoin();
    }
    return filepath;
  }
  
  private void recoverLeaseUsingCreate(Path filepath)
  throws IOException, InterruptedException {
    UserGroupInformation ugi = 
      UserGroupInformation.createUserForTesting(fakeUsername, 
                                                new String [] { fakeGroup});
    FileSystem dfs2 = DFSTestUtil.getFileSystemAs(ugi, conf);

    boolean done = false;
    for(int i = 0; i < 10 && !done; i++) {
      AppendTestUtil.LOG.info("i=" + i);
      try {
        dfs2.create(filepath, false, bufferSize, (short)1, BLOCK_SIZE);
        fail("Creation of an existing file should never succeed.");
      } catch (IOException ioe) {
        final String message = ioe.getMessage();
        if (message.contains("file exists")) {
          AppendTestUtil.LOG.info("done", ioe);
          done = true;
        }
        else if (message.contains(AlreadyBeingCreatedException.class.getSimpleName())) {
          AppendTestUtil.LOG.info("GOOD! got " + message);
        }
        else {
          AppendTestUtil.LOG.warn("UNEXPECTED IOException", ioe);
        }
      }

      if (!done) {
        AppendTestUtil.LOG.info("sleep " + 5000 + "ms");
        try {Thread.sleep(5000);} catch (InterruptedException e) {}
      }
    }
    assertTrue(done);

  }
  
  private void verifyFile(FileSystem dfs, Path filepath, byte[] actual,
      int size) throws IOException {
    AppendTestUtil.LOG.info("Lease for file " +  filepath + " is recovered. "
        + "Validating its contents now...");

    // verify that file-size matches
    assertTrue("File should be " + size + " bytes, but is actually " +
               " found to be " + dfs.getFileStatus(filepath).getLen() +
               " bytes",
               dfs.getFileStatus(filepath).getLen() == size);

    // verify that there is enough data to read.
    System.out.println("File size is good. Now validating sizes from datanodes...");
    FSDataInputStream stmin = dfs.open(filepath);
    stmin.readFully(0, actual, 0, size);
    stmin.close();
  }
}
