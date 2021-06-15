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
import java.net.*;
import junit.framework.TestCase;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;



/**
 * A JUnit test to test that killing completed jobs does not move them
 * to the failed sate - See JIRA HADOOP-2132
 */
public class TestKillCompletedJob extends TestCase {
  
  
  static Boolean launchWordCount(String fileSys,
                                String jobTracker,
                                JobConf conf,
                                String input,
                                int numMaps,
                                int numReduces) throws IOException {
    final Path inDir = new Path("/testing/wc/input");
    final Path outDir = new Path("/testing/wc/output");
    FileSystem fs = FileSystem.get(URI.create(fileSys), conf);
    fs.delete(outDir, true);
    if (!fs.mkdirs(inDir)) {
      throw new IOException("Mkdirs failed to create " + inDir.toString());
    }
    {
      DataOutputStream file = fs.create(new Path(inDir, "part-0"));
      file.writeBytes(input);
      file.close();
    }

    FileSystem.setDefaultUri(conf, fileSys);
    conf.set("mapred.job.tracker", jobTracker);
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

    RunningJob rj = JobClient.runJob(conf);
    JobID jobId = rj.getID();
    
    // Kill the job after it is successful
    if (rj.isSuccessful())
    {
      System.out.println("Job Id:" + jobId + 
        " completed successfully. Killing it now");
      rj.killJob();
    }
    
       
    return rj.isSuccessful();
      
  }

     
  public void testKillCompJob() throws IOException {
    String namenode = null;
    MiniDFSCluster dfs = null;
    MiniMRCluster mr = null;
    FileSystem fileSys = null;
    try {
      final int taskTrackers = 1;

      Configuration conf = new Configuration();
      dfs = new MiniDFSCluster(conf, 1, true, null);
      fileSys = dfs.getFileSystem();
      namenode = fileSys.getUri().toString();
      mr = new MiniMRCluster(taskTrackers, namenode, 3);
      JobConf jobConf = new JobConf();
    
      Boolean result;
      final String jobTrackerName = "localhost:" + mr.getJobTrackerPort();
      result = launchWordCount(namenode, jobTrackerName, jobConf, 
                               "Small text\n",
                               1, 0);
      assertTrue(result);
          
    } finally {
      if (dfs != null) { dfs.shutdown(); }
      if (mr != null) { mr.shutdown();
      }
    }
  }
  
}
