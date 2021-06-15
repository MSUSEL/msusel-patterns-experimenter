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
package org.apache.hadoop.tools.rumen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestConcurrentRead {
  static final List<LoggedJob> cachedTrace = new ArrayList<LoggedJob>();
  static final String traceFile = 
      "rumen/small-trace-test/job-tracker-logs-trace-output.gz";
  
  static Configuration conf;
  static FileSystem lfs;
  static Path path;
  
  @BeforeClass
  static public void globalSetUp() throws IOException {
    conf = new Configuration();
    lfs = FileSystem.getLocal(conf);
    Path rootInputDir = new Path(System.getProperty("test.tools.input.dir", ""))
        .makeQualified(lfs);
    path = new Path(rootInputDir, traceFile);
    JobTraceReader reader = new JobTraceReader(path, conf);
    try {
      LoggedJob job;
      while ((job = reader.getNext()) != null) {
        cachedTrace.add(job);
      }
    } finally {
      reader.close();
    }
  }

  void readAndCompare() throws IOException {
    JobTraceReader reader = new JobTraceReader(path, conf);
    try {
      for (Iterator<LoggedJob> it = cachedTrace.iterator(); it.hasNext();) {
        LoggedJob jobExpected = it.next();
        LoggedJob jobRead = reader.getNext();
        assertNotNull(jobRead);
        try {
          jobRead.deepCompare(jobExpected, null);
        } catch (DeepInequalityException e) {
          fail(e.toString());
        }
      }
      assertNull(reader.getNext());
    } finally {
      reader.close();
    }
  }

  class TestThread extends Thread {
    final int repeat;
    final CountDownLatch startSignal, doneSignal;
    final Map<String, Throwable> errors;

    TestThread(int id, int repeat, CountDownLatch startSignal, CountDownLatch doneSignal, Map<String, Throwable> errors) {
      super(String.format("TestThread-%d", id));
      this.repeat = repeat;
      this.startSignal = startSignal;
      this.doneSignal = doneSignal;
      this.errors = errors;
    }

    @Override
    public void run() {
      try {
        startSignal.await();
        for (int i = 0; i < repeat; ++i) {
          try {
            readAndCompare();
          } catch (Throwable e) {
            errors.put(getName(), e);
            break;
          }
        }
        doneSignal.countDown();
      } catch (Throwable e) {
        errors.put(getName(), e);
      }
    }
  }

  @Test
  public void testConcurrentRead() throws InterruptedException {
    int nThr = conf.getInt("test.rumen.concurrent-read.threads", 4);
    int repeat = conf.getInt("test.rumen.concurrent-read.repeat", 10);
    CountDownLatch startSignal = new CountDownLatch(1);
    CountDownLatch doneSignal = new CountDownLatch(nThr);
    Map<String, Throwable> errors = Collections
        .synchronizedMap(new TreeMap<String, Throwable>());
    for (int i = 0; i < nThr; ++i) {
      new TestThread(i, repeat, startSignal, doneSignal, errors).start();
    }
    startSignal.countDown();
    doneSignal.await();
    if (!errors.isEmpty()) {
      StringBuilder sb = new StringBuilder();
      for (Map.Entry<String, Throwable> e : errors.entrySet()) {
        sb.append(String.format("%s:\n%s\n", e.getKey(), e.getValue().toString()));
      }
      fail(sb.toString());
    }
  }
}
