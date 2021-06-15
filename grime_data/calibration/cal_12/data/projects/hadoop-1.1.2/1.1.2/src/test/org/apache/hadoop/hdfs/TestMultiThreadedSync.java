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

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.hdfs.server.datanode.DataNode;
import org.apache.hadoop.hdfs.server.namenode.FSNamesystem;
import org.apache.hadoop.hdfs.server.namenode.LeaseManager;
import org.apache.hadoop.hdfs.server.namenode.NameNode;
import org.apache.hadoop.hdfs.server.protocol.InterDatanodeProtocol;
import org.apache.log4j.Level;

/**
 * This class tests the building blocks that are needed to
 * support HDFS appends.
 */
public class TestMultiThreadedSync {
  static final int blockSize = 1024*1024;
  static final int numBlocks = 10;
  static final int fileSize = numBlocks * blockSize + 1;

  private static final int NUM_THREADS = 10;
  private static final int WRITE_SIZE = 517;
  private static final int NUM_WRITES_PER_THREAD = 1000;
  
  private byte[] toWrite = null;

  /*
   * creates a file but does not close it
   */ 
  private FSDataOutputStream createFile(FileSystem fileSys, Path name, int repl)
    throws IOException {
    FSDataOutputStream stm = fileSys.create(name, true,
                                            fileSys.getConf().getInt("io.file.buffer.size", 4096),
                                            (short)repl, (long)blockSize);
    return stm;
  }
  
  private void initBuffer(int size) {
    long seed = AppendTestUtil.nextLong();
    toWrite = AppendTestUtil.randomBytes(seed, size);
  }

  @Test
  public void testMultipleSyncers() throws Exception {
    Configuration conf = new Configuration();
    MiniDFSCluster cluster = new MiniDFSCluster(conf, 1, true, null);
    FileSystem fs = cluster.getFileSystem();
    Path p = new Path("/multiple-syncers.dat");
    try {
      doMultithreadedWrites(conf, p, NUM_THREADS, WRITE_SIZE, NUM_WRITES_PER_THREAD);
    } finally {
      fs.close();
      cluster.shutdown();
    }
  }

  /**
   * Test case where a bunch of threads are continuously calling sync() while another
   * thread appends some data and then closes the file.
   *
   * The syncing threads should eventually catch an IOException stating that the stream
   * was closed -- and not an NPE or anything like that.
   */
  @Test
  public void testSyncWhileClosing() throws Throwable {
    Configuration conf = new Configuration();
    MiniDFSCluster cluster = new MiniDFSCluster(conf, 1, true, null);
    FileSystem fs = cluster.getFileSystem();
    Path p = new Path("/sync-and-close.dat");

    final FSDataOutputStream stm = createFile(fs, p, 1);


    ArrayList<Thread> flushers = new ArrayList<Thread>();
    final AtomicReference<Throwable> thrown = new AtomicReference<Throwable>();
    try {
      for (int i = 0; i < 10; i++) {
        Thread flusher = new Thread() {
            public void run() {
              try {
                while (true) {
                  try {
                    stm.sync();
                  } catch (IOException ioe) {
                    if (!ioe.toString().contains("DFSOutputStream is closed")) {
                      throw ioe;
                    } else {
                      return;
                    }
                  }
                }
              } catch (Throwable t) {
                thrown.set(t);
              }
            }
          };
        flusher.start();
        flushers.add(flusher);
      }

      // Write some data
      for (int i = 0; i < 10000; i++) {
        stm.write(1);
      }

      // Close it while the flushing threads are still flushing
      stm.close();

      // Wait for the flushers to all die.
      for (Thread t : flushers) {
        t.join();
      }

      // They should have all gotten the expected exception, not anything
      // else.
      if (thrown.get() != null) {
        throw thrown.get();
      }

    } finally {
      fs.close();
      cluster.shutdown();
    }
  }

  public void doMultithreadedWrites(
    Configuration conf, Path p, int numThreads, int bufferSize, int numWrites)
    throws Exception {
    initBuffer(bufferSize);

    // create a new file.
    FileSystem fs = p.getFileSystem(conf);
    FSDataOutputStream stm = createFile(fs, p, 1);
    System.out.println("Created file simpleFlush.dat");

    // TODO move this bit to another test case
    // There have been a couple issues with flushing empty buffers, so do
    // some empty flushes first.
    stm.sync();
    stm.sync();
    stm.write(1);
    stm.sync();
    stm.sync();

    CountDownLatch countdown = new CountDownLatch(1);
    ArrayList<Thread> threads = new ArrayList<Thread>();
    AtomicReference<Throwable> thrown = new AtomicReference<Throwable>();
    for (int i = 0; i < numThreads; i++) {
      Thread t = new AppendTestUtil.WriterThread(stm, toWrite, thrown, countdown, numWrites);
      threads.add(t);
      t.start();
    }

    // Start all the threads at the same time for maximum raciness!
    countdown.countDown();
    
    for (Thread t : threads) {
      t.join();
    }
    if (thrown.get() != null) {
      
      throw new RuntimeException("Deferred", thrown.get());
    }
    stm.close();
    System.out.println("Closed file.");
  }

  public static void main(String args[]) throws Exception {
    TestMultiThreadedSync test = new TestMultiThreadedSync();
    Configuration conf = new Configuration();
    Path p = new Path("/user/todd/test.dat");
    long st = System.nanoTime();
    test.doMultithreadedWrites(conf, p, 10, 511, 50000);
    long et = System.nanoTime();

    System.out.println("Finished in " + ((et - st) / 1000000) + "ms");
  }

}
