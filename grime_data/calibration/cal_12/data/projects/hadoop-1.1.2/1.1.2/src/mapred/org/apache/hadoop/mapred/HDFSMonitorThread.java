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

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.mapred.JobTracker.SafeModeAction;

public class HDFSMonitorThread extends Thread {
  
  public static final Log LOG = LogFactory.getLog(HDFSMonitorThread.class);
  
  private final JobTracker jt;
  private final FileSystem fs;
  
  private final int hdfsMonitorInterval;
  
  public HDFSMonitorThread(Configuration conf, JobTracker jt, FileSystem fs) {
    super("JT-HDFS-Monitor-Thread");
    this.jt = jt;
    this.fs = fs;
    this.hdfsMonitorInterval = 
        conf.getInt(
            JobTracker.JT_HDFS_MONITOR_THREAD_INTERVAL, 
            JobTracker.DEFAULT_JT_HDFS_MONITOR_THREAD_INTERVAL_MS);
    setDaemon(true);
  }

  @Override
  public void run() {
    
    LOG.info("Starting HDFS Health Monitoring...");
    
    boolean previouslyHealthy = true;
    boolean done = false;
    
    while (!done && !isInterrupted()) {
      
      boolean currentlyHealthy = DistributedFileSystem.isHealthy(fs.getUri());
      if (currentlyHealthy != previouslyHealthy) {
        
        JobTracker.SafeModeAction action; 
        if (currentlyHealthy) {
          action = SafeModeAction.SAFEMODE_LEAVE;
          LOG.info("HDFS healthy again, instructing JobTracker to leave " +
              "'safemode' ...");
        } else {
          action = SafeModeAction.SAFEMODE_ENTER;
          LOG.info("HDFS is unhealthy, instructing JobTracker to enter " +
              "'safemode' ...");
        }
        
        try {
          if (jt.isInAdminSafeMode()) {
            // Don't override admin-set safemode
            LOG.info("JobTracker is in admin-set safemode, not overriding " +
            		"through " + action);
           previouslyHealthy = currentlyHealthy; 
          } else {
            previouslyHealthy = !(jt.setSafeModeInternal(action)); 
                                                         //safemode => !healthy
          }
        } catch (IOException ioe) {
          LOG.info("Failed to setSafeMode with action " + action, ioe);
        }
      }
      
      try {
        Thread.sleep(hdfsMonitorInterval);
      } catch (InterruptedException e) {
        done = true;
      }
    }
    
    LOG.info("Stoping HDFS Health Monitoring...");
  }
  
}
