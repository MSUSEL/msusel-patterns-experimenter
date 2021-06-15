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

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.http.HttpServer;
import org.junit.Test;


public class TestJettyBugMonitor {
  private final Configuration conf = new Configuration();
  
  /**
   * Test that it can detect a running Jetty selector.
   */
  @Test(timeout=20000)
  public void testGetJettyThreads() throws Exception {
    JettyBugMonitor monitor = new JettyBugMonitor(conf);
    
    new File(System.getProperty("build.webapps", "build/webapps") + "/test"
      ).mkdirs();
    HttpServer server = new HttpServer("test", "0.0.0.0", 0, true);
    server.start();
    try {
      List<Long> threads = monitor.waitForJettyThreads();
      assertEquals(1, threads.size());
    } finally {
      server.stop();
    }
  }
  
  /**
   * Test that the CPU monitoring can detect a spinning
   * thread.
   */
  @Test(timeout=5000)
  public void testMonitoring() throws Exception {
    // Start a thread which sucks up CPU
    BusyThread busyThread = new BusyThread();
    busyThread.start();
    final long tid = busyThread.getId();
    // Latch which will be triggered when the jetty monitor
    // wants to abort
    final CountDownLatch abortLatch = new CountDownLatch(1);

    conf.setLong(JettyBugMonitor.CHECK_INTERVAL_KEY, 1000);
    JettyBugMonitor monitor = null;
    try {
      monitor = new JettyBugMonitor(conf) {
        @Override
        protected List<Long> waitForJettyThreads() {
          return Collections.<Long>singletonList(tid);
        }
        @Override
        protected void doAbort() {
          abortLatch.countDown();
          // signal abort to main thread
        }
      };
      monitor.start();
      
      abortLatch.await();
    } finally {
      busyThread.done = true;
      busyThread.join();
      
      if (monitor != null) {
        monitor.shutdown();
      }
    }
  }
  
  private static class BusyThread extends Thread {
    private volatile boolean done = false;
    
    @Override
    public void run() {
      while (!done) {
        // spin using up CPU
      }
    }
  }
}
