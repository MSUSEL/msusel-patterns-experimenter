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

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.IOException;
import java.util.Properties;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

public class TestJobStatusPersistency extends ClusterMapReduceTestCase {
  static final Path TEST_DIR = 
    new Path(System.getProperty("test.build.data","/tmp"), 
             "job-status-persistence");

  @Override
  protected void setUp() throws Exception {
    // Don't start anything by default
  }

  private JobID runJob() throws Exception {
    OutputStream os = getFileSystem().create(new Path(getInputDir(), "text.txt"));
    Writer wr = new OutputStreamWriter(os);
    wr.write("hello1\n");
    wr.write("hello2\n");
    wr.write("hello3\n");
    wr.write("hello4\n");
    wr.close();

    JobConf conf = createJobConf();
    conf.setJobName("mr");

    conf.setInputFormat(TextInputFormat.class);

    conf.setMapOutputKeyClass(LongWritable.class);
    conf.setMapOutputValueClass(Text.class);

    conf.setOutputFormat(TextOutputFormat.class);
    conf.setOutputKeyClass(LongWritable.class);
    conf.setOutputValueClass(Text.class);

    conf.setMapperClass(org.apache.hadoop.mapred.lib.IdentityMapper.class);
    conf.setReducerClass(org.apache.hadoop.mapred.lib.IdentityReducer.class);

    FileInputFormat.setInputPaths(conf, getInputDir());

    FileOutputFormat.setOutputPath(conf, getOutputDir());

    return JobClient.runJob(conf).getID();
  }

  public void testNonPersistency() throws Exception {
    startCluster(true, null);
    JobID jobId = runJob();
    JobClient jc = new JobClient(createJobConf());
    RunningJob rj = jc.getJob(jobId);
    assertNotNull(rj);
    stopCluster();
    startCluster(true, null);
    jc = new JobClient(createJobConf());
    rj = jc.getJob(jobId);
    assertNull(rj);
  }

  public void testPersistency() throws Exception {
    Properties config = new Properties();
    config.setProperty("mapred.job.tracker.persist.jobstatus.active", "true");
    config.setProperty("mapred.job.tracker.persist.jobstatus.hours", "1");
    startCluster(false, config);
    JobID jobId = runJob();
    JobClient jc = new JobClient(createJobConf());
    RunningJob rj0 = jc.getJob(jobId);
    assertNotNull(rj0);
    boolean sucessfull0 = rj0.isSuccessful();
    String jobName0 = rj0.getJobName();
    Counters counters0 = rj0.getCounters();
    TaskCompletionEvent[] events0 = rj0.getTaskCompletionEvents(0);

    stopCluster();
    startCluster(false, config);
     
    jc = new JobClient(createJobConf());
    RunningJob rj1 = jc.getJob(jobId);
    assertNotNull(rj1);
    assertEquals(sucessfull0, rj1.isSuccessful());
    assertEquals(jobName0, rj0.getJobName());
    assertEquals(counters0.size(), rj1.getCounters().size());

    TaskCompletionEvent[] events1 = rj1.getTaskCompletionEvents(0);
    assertEquals(events0.length, events1.length);    
    for (int i = 0; i < events0.length; i++) {
      assertEquals(events0[i].getTaskAttemptId(), events1[i].getTaskAttemptId());
      assertEquals(events0[i].getTaskStatus(), events1[i].getTaskStatus());
    }
  }

  /**
   * Test if the completed job status is persisted to localfs.
   */
  public void testLocalPersistency() throws Exception {
    FileSystem fs = FileSystem.getLocal(new JobConf());
    
    fs.delete(TEST_DIR, true);
    
    Properties config = new Properties();
    config.setProperty("mapred.job.tracker.persist.jobstatus.active", "true");
    config.setProperty("mapred.job.tracker.persist.jobstatus.hours", "1");
    config.setProperty("mapred.job.tracker.persist.jobstatus.dir", 
                       fs.makeQualified(TEST_DIR).toString());
    startCluster(true, config);
    JobID jobId = runJob();
    JobClient jc = new JobClient(createJobConf());
    RunningJob rj = jc.getJob(jobId);
    assertNotNull(rj);
    
    // check if the local fs has the data
    Path jobInfo = new Path(TEST_DIR, rj.getID() + ".info");
    assertTrue("Missing job info from the local fs", fs.exists(jobInfo));
    fs.delete(TEST_DIR, true);
  }

  /**
   * Verify that completed-job store is inactive if the jobinfo path is not
   * writable.
   * 
   * @throws Exception
   */
  public void testJobStoreDisablingWithInvalidPath() throws Exception {
    MiniMRCluster mr = null;
    Path parent = new Path(TEST_DIR, "parent");
    try {
      FileSystem fs = FileSystem.getLocal(new JobConf());

      if (fs.exists(TEST_DIR) && !fs.delete(TEST_DIR, true)) {
        fail("Cannot delete TEST_DIR!");
      }

      if (fs.mkdirs(new Path(TEST_DIR, parent))) {
        if (!(new File(parent.toUri().getPath()).setWritable(false, false))) {
          fail("Cannot chmod parent!");
        }
      } else {
        fail("Cannot create parent dir!");
      }
      JobConf config = new JobConf();
      config.set("mapred.job.tracker.persist.jobstatus.active", "true");
      config.set("mapred.job.tracker.persist.jobstatus.hours", "1");
      config.set("mapred.job.tracker.persist.jobstatus.dir", new Path(parent,
          "child").toUri().getPath());
      boolean started = true;
      JobConf conf = MiniMRCluster.configureJobConf(config, "file:///", 0, 0, null);
      try {
        JobTracker jt = JobTracker.startTracker(conf);
      } catch (IOException ex) {
        started = false;
      }
    } finally {
      new File(parent.toUri().getPath()).setWritable(true, false);
    }
  }
}
