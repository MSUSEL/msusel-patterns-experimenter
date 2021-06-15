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

import java.io.*;

import org.apache.hadoop.mapred.TaskTracker.TaskInProgress;

/** Runs a map task. */
class MapTaskRunner extends TaskRunner {
  
  public MapTaskRunner(TaskInProgress task, TaskTracker tracker, JobConf conf,
                       TaskTracker.RunningJob rjob) 
  throws IOException {
    super(task, tracker, conf, rjob);
  }
  
  /** Delete any temporary files from previous failed attempts. */
  public boolean prepare() throws IOException {
    if (!super.prepare()) {
      return false;
    }
    
    mapOutputFile.removeAll();
    return true;
  }

  /** Delete all of the temporary map output files. */
  public void close() throws IOException {
    LOG.info(getTask()+" done; removing files.");
    mapOutputFile.removeAll();
  }

  @Override
  public String getChildJavaOpts(JobConf jobConf, String defaultValue) {
    String user = 
      jobConf.get(JobConf.MAPRED_MAP_TASK_JAVA_OPTS, 
        super.getChildJavaOpts(jobConf, 
            JobConf.DEFAULT_MAPRED_TASK_JAVA_OPTS));
    String admin = 
      jobConf.get(TaskRunner.MAPRED_MAP_ADMIN_JAVA_OPTS,
        TaskRunner.DEFAULT_MAPRED_ADMIN_JAVA_OPTS);
    return user + " " + admin;
  }

  @Override
  public int getChildUlimit(JobConf jobConf) {
    return jobConf.getInt(JobConf.MAPRED_MAP_TASK_ULIMIT, 
                          super.getChildUlimit(jobConf));
  }

  @Override
  public String getChildEnv(JobConf jobConf) {
    return jobConf.get(JobConf.MAPRED_MAP_TASK_ENV, super.getChildEnv(jobConf));
  }

}
