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
package org.apache.hadoop.contrib.failmon;

import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;

/**********************************************************
 * This class executes monitoring jobs on all nodes of the
 * cluster, on which we intend to gather failure metrics. 
 * It is basically a thread that sleeps and periodically wakes
 * up to execute monitoring jobs and ship all gathered data to 
 * a "safe" location, which in most cases will be the HDFS 
 * filesystem of the monitored cluster.
 * 
 **********************************************************/

public class Executor implements Runnable {

  public static final int DEFAULT_LOG_INTERVAL = 3600;

  public static final int DEFAULT_POLL_INTERVAL = 360;

  public static int MIN_INTERVAL = 5;

  public static int instances = 0;

  LocalStore lstore;

  ArrayList<MonitorJob> monitors;
  
  int interval;

  int upload_interval;
  int upload_counter;
  
  /**
   * Create an instance of the class and read the configuration
   * file to determine the set of jobs that will be run and the 
   * maximum interval for which the thread can sleep before it 
   * wakes up to execute a monitoring job on the node.
   * 
   */ 

  public Executor(Configuration conf) {
    
    Environment.prepare("conf/failmon.properties");
    
    String localTmpDir;
    
    if (conf == null) {
      // running as a stand-alone application
      localTmpDir = System.getProperty("java.io.tmpdir");
      Environment.setProperty("local.tmp.dir", localTmpDir);
    } else {
      // running from within Hadoop
      localTmpDir = conf.get("hadoop.tmp.dir");
      String hadoopLogPath = System.getProperty("hadoop.log.dir") + "/" + System.getProperty("hadoop.log.file");
      Environment.setProperty("hadoop.log.file", hadoopLogPath);
      Environment.setProperty("local.tmp.dir", localTmpDir);
    }
    
    monitors = Environment.getJobs();
    interval = Environment.getInterval(monitors);
    upload_interval = LocalStore.UPLOAD_INTERVAL;
    lstore = new LocalStore();
    
    if (Environment.getProperty("local.upload.interval") != null) 
     upload_interval = Integer.parseInt(Environment.getProperty("local.upload.interval"));

    instances++;
  }

  public void run() {
    upload_counter = upload_interval;

    Environment.logInfo("Failmon Executor thread started successfully.");
    while (true) {
      try {
        Thread.sleep(interval * 1000);
        for (int i = 0; i < monitors.size(); i++) {
          monitors.get(i).counter -= interval;
          if (monitors.get(i).counter <= 0) {
            monitors.get(i).reset();
            Environment.logInfo("Calling " + monitors.get(i).job.getInfo() + "...\t");
            monitors.get(i).job.monitor(lstore);
          }
        }
        upload_counter -= interval;
        if (upload_counter <= 0) {
          lstore.upload();
          upload_counter = upload_interval;
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void cleanup() {
    instances--;   
  }
}
