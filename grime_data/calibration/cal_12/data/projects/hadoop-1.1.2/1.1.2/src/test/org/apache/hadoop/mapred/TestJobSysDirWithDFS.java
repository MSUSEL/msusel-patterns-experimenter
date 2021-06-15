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

import java.io.DataOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

/**
 * A JUnit test to test Job System Directory with Mini-DFS.
 */
public class TestJobSysDirWithDFS extends TestCase {
  private static final Log LOG =
    LogFactory.getLog(TestJobSysDirWithDFS.class.getName());
  
  static final int NUM_MAPS = 10;
  static final int NUM_SAMPLES = 100000;
  
  public static class TestResult {
    public String output;
    public RunningJob job;
    TestResult(RunningJob job, String output) {
      this.job = job;
      this.output = output;
    }
  }

  public static TestResult launchWordCount(JobConf conf,
                                           Path inDir,
                                           Path outDir,
                                           String input,
                                           int numMaps,
                                           int numReduces,
                                           String sysDir) throws IOException {
    FileSystem inFs = inDir.getFileSystem(conf);
    FileSystem outFs = outDir.getFileSystem(conf);
    outFs.delete(outDir, true);
    if (!inFs.mkdirs(inDir)) {
      throw new IOException("Mkdirs failed to create " + inDir.toString());
    }
    {
      DataOutputStream file = inFs.create(new Path(inDir, "part-0"));
      file.writeBytes(input);
      file.close();
    }
    conf.setJobName("wordcount");
    conf.setInputFormat(TextInputFormat.class);
    
    // the keys are words (strings)
    conf.setOutputKeyClass(Text.class);
    // the values are counts (ints)
    conf.setOutputValueClass(IntWritable.class);
    
    conf.setMapperClass(WordCount.MapClass.class);        
    conf.setCombinerClass(WordCount.Reduce.class);
    conf.setReducerClass(WordCount.Reduce.class);
    FileInputFormat.setInputPaths(conf, inDir);
    FileOutputFormat.setOutputPath(conf, outDir);
    conf.setNumMapTasks(numMaps);
    conf.setNumReduceTasks(numReduces);
    conf.set("mapred.system.dir", "/tmp/subru/mapred/system");
    JobClient jobClient = new JobClient(conf);
    RunningJob job = jobClient.runJob(conf);
    // Checking that the Job Client system dir is not used
    assertFalse(FileSystem.get(conf).exists(new Path(conf.get("mapred.system.dir")))); 
    // Check if the Job Tracker system dir is propogated to client
    sysDir = jobClient.getSystemDir().toString();
    System.out.println("Job sys dir -->" + sysDir);
    assertFalse(sysDir.contains("/tmp/subru/mapred/system"));
    assertTrue(sysDir.contains("custom"));
    return new TestResult(job, TestMiniMRWithDFS.readOutput(outDir, conf));
  }

 static void runWordCount(MiniMRCluster mr, JobConf jobConf, String sysDir)
 throws IOException {
    LOG.info("runWordCount");
    // Run a word count example
    // Keeping tasks that match this pattern
    TestResult result;
    final Path inDir = new Path("./wc/input");
    final Path outDir = new Path("./wc/output");
    result = launchWordCount(jobConf, inDir, outDir,
                             "The quick brown fox\nhas many silly\n" + 
                             "red fox sox\n",
                             3, 1, sysDir);
    assertEquals("The\t1\nbrown\t1\nfox\t2\nhas\t1\nmany\t1\n" +
                 "quick\t1\nred\t1\nsilly\t1\nsox\t1\n", result.output);
    // Checking if the Job ran successfully in spite of different system dir config
    //  between Job Client & Job Tracker
    assertTrue(result.job.isSuccessful());
  }

  public void testWithDFS() throws IOException {
    MiniDFSCluster dfs = null;
    MiniMRCluster mr = null;
    FileSystem fileSys = null;
    try {
      final int taskTrackers = 4;

      JobConf conf = new JobConf();
      conf.set("mapred.system.dir", "/tmp/custom/mapred/system");
      dfs = new MiniDFSCluster(conf, 4, true, null);
      fileSys = dfs.getFileSystem();
      mr = new MiniMRCluster(taskTrackers, fileSys.getUri().toString(), 1, null, null, conf);

      runWordCount(mr, mr.createJobConf(), conf.get("mapred.system.dir"));
    } finally {
      if (dfs != null) { dfs.shutdown(); }
      if (mr != null) { mr.shutdown();
      }
    }
  }
  
}
