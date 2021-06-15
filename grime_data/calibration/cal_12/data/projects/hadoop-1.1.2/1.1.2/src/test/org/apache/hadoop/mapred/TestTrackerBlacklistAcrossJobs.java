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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.examples.SleepJob.SleepInputFormat;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapred.lib.NullOutputFormat;

import junit.framework.TestCase;

public class TestTrackerBlacklistAcrossJobs extends TestCase {
  private static final String hosts[] = new String[] {
    "host1.rack.com", "host2.rack.com", "host3.rack.com"
  };
  final Path inDir = new Path("/testing");
  final Path outDir = new Path("/output");

  public static class SleepJobFailOnHost extends MapReduceBase
    implements Mapper<IntWritable, IntWritable, IntWritable, NullWritable> {
    String hostname = "";
    
    public void configure(JobConf job) {
      this.hostname = job.get("slave.host.name");
    }
    
    public void map(IntWritable key, IntWritable value,
                    OutputCollector<IntWritable, NullWritable> output,
                    Reporter reporter)
    throws IOException {
      if (this.hostname.equals(hosts[0])) {
        // fail here
        throw new IOException("failing on host: " + hosts[0]);
      }
    }
  }
  
  public void testBlacklistAcrossJobs() throws IOException {
    MiniDFSCluster dfs = null;
    MiniMRCluster mr = null;
    FileSystem fileSys = null;
    Configuration conf = new Configuration();
    // set up dfs and input
    dfs = new MiniDFSCluster(conf, 1, true, null, hosts);
    fileSys = dfs.getFileSystem();
    if (!fileSys.mkdirs(inDir)) {
      throw new IOException("Mkdirs failed to create " + inDir.toString());
    }
    UtilsForTests.writeFile(dfs.getNameNode(), conf, 
                                 new Path(inDir + "/file"), (short) 1);
    // start mr cluster
    JobConf jtConf = new JobConf();
    jtConf.setInt("mapred.max.tracker.blacklists", 1);
    mr = new MiniMRCluster(3, fileSys.getUri().toString(),
                           1, null, hosts, jtConf);

    // set up job configuration
    JobConf mrConf = mr.createJobConf();
    JobConf job = new JobConf(mrConf);
    job.setInt("mapred.max.tracker.failures", 1);
    job.setNumMapTasks(30);
    job.setNumReduceTasks(0);
    job.setMapperClass(SleepJobFailOnHost.class);
    job.setMapOutputKeyClass(IntWritable.class);
    job.setMapOutputValueClass(NullWritable.class);
    job.setOutputFormat(NullOutputFormat.class);
    job.setInputFormat(SleepInputFormat.class);
    FileInputFormat.setInputPaths(job, inDir);
    FileOutputFormat.setOutputPath(job, outDir);
    
    // run the job
    JobClient jc = new JobClient(mrConf);
    RunningJob running = JobClient.runJob(job);
    assertEquals("Job failed", JobStatus.SUCCEEDED, running.getJobState());
    // heuristic blacklisting is graylisting as of 0.20.Fred
    assertEquals("Blacklisted the host", 0,
      jc.getClusterStatus().getBlacklistedTrackers());
    assertEquals("Didn't graylist the host", 1,
      jc.getClusterStatus().getGraylistedTrackers());
    assertEquals("Fault count should be 1", 1, mr.getFaultCount(hosts[0]));

    // run the same job once again 
    // there should be no change in blacklist or graylist count, but fault
    // count (per-job blacklistings) should go up by one
    running = JobClient.runJob(job);
    assertEquals("Job failed", JobStatus.SUCCEEDED, running.getJobState());
    assertEquals("Blacklisted the host", 0,
      jc.getClusterStatus().getBlacklistedTrackers());
    assertEquals("Didn't graylist the host", 1,
      jc.getClusterStatus().getGraylistedTrackers());
    // previously this asserted 1, but makes no sense:  each per-job
    // blacklisting counts as a new fault, so 2 runs => 2 faults:
    assertEquals("Fault count should be 2", 2, mr.getFaultCount(hosts[0]));
  }
}
