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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobID;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.test.system.JTProtocol;
import org.apache.hadoop.mapreduce.test.system.JobInfo;
import org.apache.hadoop.mapreduce.test.system.MRCluster;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import testjar.JobKillCommitter;

public class TestJobKill {
  private static final Log LOG = LogFactory.getLog(TestJobKill.class);
  private JTProtocol wovenClient = null;
  private static Path outDir = new Path("output");
  private static Path inDir = new Path("input");
  private static FileSystem fs = null;
  private static MRCluster cluster;

  @BeforeClass
  public static void setUp() throws Exception {
    cluster = MRCluster.createCluster(new Configuration());
    cluster.setUp();
    fs = inDir.getFileSystem(cluster.getJTClient().getConf());
    if(!fs.exists(inDir)){
      fs.create(inDir);
    }
    if (fs.exists(outDir)) {
      fs.delete(outDir,true);
    }
  }

  @AfterClass
  public static void tearDown() throws Exception {
    if(fs.exists(inDir)) {
      fs.delete(inDir,true);
    }    
    if (fs.exists(outDir)) {
      fs.delete(outDir,true);
    }
    cluster.tearDown();
  }

  /*
   * The test case intention is to test the job failure due to system
   * exceptions, so the exceptions are thrown intentionally and the job is
   * verified for failure. At the end of the test, the verification is made
   * that the success file is not present in the hdfs location. This is because
   * the success file only should exist if the actual job had succeeded. 
   * 
   * @throws Exception in a case of test errors
   */
  @Test
  public void testSystemJobKill() throws Exception {
    wovenClient = cluster.getJTClient().getProxy();
    Configuration conf = new Configuration(cluster.getConf());
    conf.set("mapred.map.max.attempts", "1");
    conf.set("mapred.reduce.max.attempts", "1");
    // fail the mapper job
    failJob(conf, JobKillCommitter.CommitterWithNoError.class, "JobMapperFail",
        JobKillCommitter.MapperFail.class, JobKillCommitter.ReducerPass.class,
        false);
    // fail the reducer job
    failJob(conf, JobKillCommitter.CommitterWithNoError.class,
        "JobReducerFail", JobKillCommitter.MapperPass.class,
        JobKillCommitter.ReducerFail.class,false);
    // fail the set up job
    failJob(conf, JobKillCommitter.CommitterWithFailSetup.class,
        "JobSetupFail", JobKillCommitter.MapperPass.class,
        JobKillCommitter.ReducerPass.class,false);
    // fail the clean up job
    failJob(conf, JobKillCommitter.CommitterWithFailCleanup.class,
        "JobCleanupFail", JobKillCommitter.MapperPass.class,
        JobKillCommitter.ReducerPass.class,false);
  }

  private void failJob(Configuration conf,
      Class<? extends OutputCommitter> theClass, String confName,
      Class<? extends Mapper> mapClass, Class<? extends Reducer> redClass,
      boolean isUserKill)
      throws Exception {
    Job job = new Job(conf, confName);
    job.setJarByClass(JobKillCommitter.class);
    job.setMapperClass(mapClass);
    job.setCombinerClass(redClass);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(Text.class);
    job.setReducerClass(redClass);
    job.setNumReduceTasks(1);
    FileInputFormat.addInputPath(job, inDir);
    FileOutputFormat.setOutputPath(job, outDir);
    JobConf jconf = new JobConf(job.getConfiguration(), JobKillCommitter.class);
    jconf.setOutputCommitter(theClass);
    if(!isUserKill)
    {  
      RunningJob rJob = cluster.getJTClient().getClient().submitJob(jconf);
      JobID id = rJob.getID();
      JobInfo jInfo = wovenClient.getJobInfo(id);
      Assert.assertTrue("Job is not in PREP state",
          jInfo.getStatus().getRunState() == JobStatus.PREP);
    }
    else
    {
      //user kill job
      RunningJob rJob = cluster.getJTClient().getClient().submitJob(jconf);
      JobInfo info = wovenClient.getJobInfo(rJob.getID());
      Assert.assertNotNull("Job Info is null",info);
      JobID id = rJob.getID();      
      Assert.assertTrue("Failed to start the job",
          cluster.getJTClient().isJobStarted(id));
      rJob.killJob();
      Assert.assertTrue("Failed to kill the job",
          cluster.getJTClient().isJobStopped(id));
    }
    checkCleanup(jconf);
    deleteOutputDir();
  }
  
  /**
   * This test is used to kill the job by explicity calling the kill api
   * and making sure the clean up happens
   * @throws Exception
   */
  @Test
  public void testUserJobKill() throws Exception{
    wovenClient = cluster.getJTClient().getProxy();
    Configuration conf = new Configuration(cluster.getConf());
    conf.set("mapred.map.max.attempts", "1");
    conf.set("mapred.reduce.max.attempts", "1");
    // fail the mapper job
    failJob(conf, JobKillCommitter.CommitterWithNoError.class, "JobUserKill",
        JobKillCommitter.MapperPassSleep.class, 
        JobKillCommitter.ReducerPass.class,true);    
  }

  private void checkCleanup(JobConf conf) throws Exception {
    if (outDir != null) {
      if (fs.exists(outDir)) {
        Path filePath = new Path(outDir,
            FileOutputCommitter.SUCCEEDED_FILE_NAME);
        // check to make sure the success file is not there since the job
        // failed.
        Assert.assertTrue("The success file is present when the job failed",
            !fs.exists(filePath));
      }
    }
  }

  private void deleteOutputDir() throws Exception {
    if (fs != null) {
      fs.delete(outDir, true);
    }
  }
}
