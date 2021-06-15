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
package org.apache.hadoop.hdfs.server.namenode;

import junit.framework.TestCase;
import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.permission.*;

import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.server.namenode.FSEditLog.EditLogFileInputStream;
import org.apache.hadoop.hdfs.server.common.Storage;
import org.apache.hadoop.hdfs.server.common.Storage.StorageDirectory;
import org.apache.hadoop.hdfs.server.namenode.FSImage.NameNodeDirType;
import org.apache.hadoop.hdfs.server.namenode.FSImage.NameNodeFile;

/**
 * This class tests the creation and validation of a checkpoint.
 */
public class TestEditLog extends TestCase {
  static final int numDatanodes = 1;

  // This test creates numThreads threads and each thread does
  // 2 * numberTransactions Transactions concurrently.
  int numberTransactions = 100;
  int numThreads = 100;

  //
  // an object that does a bunch of transactions
  //
  static class Transactions implements Runnable {
    FSEditLog editLog;
    int numTransactions;
    short replication = 3;
    long blockSize = 64;

    Transactions(FSEditLog editlog, int num) {
      editLog = editlog;
      numTransactions = num;
    }

    // add a bunch of transactions.
    public void run() {
      PermissionStatus p = FSNamesystem.getFSNamesystem(
          ).createFsOwnerPermissions(new FsPermission((short)0777));

      for (int i = 0; i < numTransactions; i++) {
        try {
          INodeFileUnderConstruction inode = new INodeFileUnderConstruction(
                              p, replication, blockSize, 0, "", "", null);
          editLog.logOpenFile("/filename" + i, inode);
          editLog.logCloseFile("/filename" + i, inode);
          editLog.logSync();
        } catch (IOException e) {
          System.out.println("Transaction " + i + " encountered exception " +
                             e);
        }
      }
    }
  }
  
  public void testEditLogPreallocation() throws IOException {
    final File TEST_DIR =
        new File(System.getProperty("test.build.data", "/tmp"));
    final File TEST_EDITS = new File(TEST_DIR, "edit_log");
    
    FSEditLog.EditLogFileOutputStream elfos = null;
    try {
      elfos = new FSEditLog.EditLogFileOutputStream(TEST_EDITS);
      byte b[] = new byte[1024];
      for (int i = 0; i < b.length; i++) {
        b[i] = 0;
      }
      elfos.write(b);
      elfos.setReadyToFlush();
      elfos.flushAndSync();
      assertEquals(FSEditLog.MIN_PREALLOCATION_LENGTH,
          elfos.getFile().length());
      for (int i = 0;
          i < 2 * FSEditLog.MIN_PREALLOCATION_LENGTH / b.length; i++) {
        elfos.write(b);
        elfos.setReadyToFlush();
        elfos.flushAndSync();
      }
      assertEquals(3 * FSEditLog.MIN_PREALLOCATION_LENGTH, elfos.getFile().length());
    } finally {
      if (elfos != null) elfos.close();
      if (TEST_EDITS.exists()) TEST_EDITS.delete();
    }
  }

  /**
   * Tests transaction logging in dfs.
   */
  public void testEditLog() throws IOException {

    // start a cluster 

    Collection<File> namedirs = null;
    Collection<File> editsdirs = null;
    Configuration conf = new Configuration();
    MiniDFSCluster cluster = new MiniDFSCluster(0, conf, numDatanodes, 
                                                true, true, null, null);
    cluster.waitActive();
    FileSystem fileSys = cluster.getFileSystem();
    int numdirs = 0;

    try {
      namedirs = cluster.getNameDirs();
      editsdirs = cluster.getNameEditsDirs();
    } finally {
      fileSys.close();
      cluster.shutdown();
    }

    for (Iterator it = namedirs.iterator(); it.hasNext(); ) {
      File dir = (File)it.next();
      System.out.println(dir);
      numdirs++;
    }

    FSImage fsimage = new FSImage(namedirs, editsdirs);
    FSEditLog editLog = fsimage.getEditLog();

    // set small size of flush buffer
    editLog.setBufferCapacity(2048);
    editLog.close();
    editLog.open();
  
    // Create threads and make them run transactions concurrently.
    Thread threadId[] = new Thread[numThreads];
    for (int i = 0; i < numThreads; i++) {
      Transactions trans = new Transactions(editLog, numberTransactions);
      threadId[i] = new Thread(trans, "TransactionThread-" + i);
      threadId[i].start();
    }

    // wait for all transactions to get over
    for (int i = 0; i < numThreads; i++) {
      try {
        threadId[i].join();
      } catch (InterruptedException e) {
        i--;      // retry 
      }
    } 
    
    editLog.close();

    // Verify that we can read in all the transactions that we have written.
    // If there were any corruptions, it is likely that the reading in
    // of these transactions will throw an exception.
    //
    for (Iterator<StorageDirectory> it = 
            fsimage.dirIterator(NameNodeDirType.EDITS); it.hasNext();) {
      File editFile = FSImage.getImageFile(it.next(), NameNodeFile.EDITS);
      System.out.println("Verifying file: " + editFile);
      int numEdits = FSEditLog.loadFSEdits(
          new EditLogFileInputStream(editFile), null);
      int numLeases = FSNamesystem.getFSNamesystem().leaseManager.countLease();
      System.out.println("Number of outstanding leases " + numLeases);
      assertEquals(0, numLeases);
      assertTrue("Verification for " + editFile + " failed. " +
                 "Expected " + (numThreads * 2 * numberTransactions) + " transactions. "+
                 "Found " + numEdits + " transactions.",
                 numEdits == numThreads * 2 * numberTransactions);

    }
  }
 
 public void test203LayoutVersion() {
   for (int lv : Storage.LAYOUT_VERSIONS_203) {
     assertTrue(Storage.is203LayoutVersion(lv));
   }
 }
}
