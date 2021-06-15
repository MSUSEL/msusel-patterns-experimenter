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

import java.util.Date;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.IdentityReducer;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.taglibs.standard.extra.spath.Predicate;

import org.mortbay.log.Log;

import junit.framework.TestCase;
import org.apache.hadoop.fs.Path;

/**
 * Test {@link JobTracker} w.r.t config parameters.
 */
public class TestJobHistoryConfig extends TestCase {
  // private MiniMRCluster mr = null;
  private MiniDFSCluster mdfs = null;
  private String namenode = null;
  FileSystem fileSys = null;
  final Path inDir = new Path("./input");
  final Path outDir = new Path("./output");

  private void setUpCluster(JobConf conf) throws IOException,
      InterruptedException {
    mdfs = new MiniDFSCluster(conf, 1, true, null);
    fileSys = mdfs.getFileSystem();
    namenode = fileSys.getUri().toString();
  }

  /**
   * Test case to make sure that JobTracker will start and JobHistory enabled
   * <ol>
   * <li>Run a job with valid jobhistory configuration</li>
   * <li>Check if JobTracker can start</li>
   * </ol>
   * 
   * @throws Exception
   */

  public void testJobHistoryWithValidConfiguration() throws Exception {
    try {
      JobConf conf = new JobConf();
      setUpCluster(conf);
      conf.set("hadoop.job.history.location", "/hadoop/history");
      conf = MiniMRCluster.configureJobConf(conf, namenode, 0, 0, null);
      boolean started = canStartJobTracker(conf);
      assertTrue(started);
    } finally {
      if (mdfs != null) {
        try {
          mdfs.shutdown();
        } catch (Exception e) {
        }
      }
    }
  }

  public static class MapperClass extends MapReduceBase implements
      Mapper<LongWritable, Text, Text, IntWritable> {
    public void configure(JobConf job) {
    }

    public void map(LongWritable key, Text value,
        OutputCollector<Text, IntWritable> output, Reporter reporter)
        throws IOException {
      throw new IOException();
    }
  }

  public void testJobHistoryLogging() throws Exception {
    JobConf conf = new JobConf();
    setUpCluster(conf);
    conf.setMapperClass(MapperClass.class);
    conf.setReducerClass(IdentityReducer.class);
    conf.setNumReduceTasks(0);
    JobClient jc = new JobClient(conf);
    conf.set("hadoop.job.history.location", "/hadoop/history");
    conf = MiniMRCluster.configureJobConf(conf, namenode, 0, 0, null);
    FileSystem inFs = inDir.getFileSystem(conf);
    if (!inFs.mkdirs(inDir)) {
      throw new IOException("Mkdirs failed to create " + inDir.toString());
    }
    FileInputFormat.setInputPaths(conf, inDir);
    FileOutputFormat.setOutputPath(conf, outDir);
    conf.setSpeculativeExecution(false);
    conf.setJobName("test");
    conf.setUser("testuser");
    conf.setQueueName("default");
    String TEST_ROOT_DIR = new Path(System.getProperty("test.build.data",
        "/tmp")).toString().replace(' ', '+');
    String uniqid = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
    JobTracker jt = JobTracker.startTracker(conf, uniqid, true);
    assertTrue(jt != null);
    JobInProgress jip = new JobInProgress(new JobID("jt", 1),
        new JobConf(conf), jt);
    assertTrue(jip != null);
    jip.jobFile = "testfile";
    String historyFile = JobHistory.getHistoryFilePath(jip.getJobID());
    JobHistory.JobInfo.logSubmitted(jip.getJobID(), jip.getJobConf(),
        jip.jobFile, jip.startTime);
  }

  /**
   * Check whether the JobTracker can be started.
   * 
   * @throws IOException
   */
  private boolean canStartJobTracker(JobConf conf) throws InterruptedException,
      IOException {
    JobTracker jt = null;
    String uniqid = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
    try {
      jt = JobTracker.startTracker(conf, uniqid, true);
      Log.info("Started JobTracker");
    } catch (IOException e) {
      Log.info("Can not Start JobTracker", e.getLocalizedMessage());
      return false;
    }
    if (jt != null) {
      jt.fs.close();
      jt.stopTracker();
    }
    return true;
  }
}
