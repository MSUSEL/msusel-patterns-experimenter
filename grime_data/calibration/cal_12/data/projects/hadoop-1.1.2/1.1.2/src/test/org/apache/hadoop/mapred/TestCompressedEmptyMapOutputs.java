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
import org.apache.hadoop.examples.RandomWriter;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.mapred.lib.IdentityReducer;
import org.apache.hadoop.util.ToolRunner;

import junit.framework.TestCase;

/**
 * A JUnit test to test the Map-Reduce framework's sort in presence of 
 * null intermediate map-outputs, when compression is enabled for intermediate
 * map-outputs. 
 */
public class TestCompressedEmptyMapOutputs extends TestCase {
  // Input/Output paths for sort
  private static final Path SORT_INPUT_PATH = new Path("/sort/input");
  private static final Path SORT_OUTPUT_PATH = new Path("/sort/output");

  // Knobs to control randomwriter; and hence sort
  private static final int NUM_HADOOP_SLAVES = 3;
  private static final int RW_BYTES_PER_MAP = 50000;
  private static final int RW_MAPS_PER_HOST = 5;
  
  private static void runRandomWriter(JobConf job, Path sortInput) 
  throws Exception {
    // Scale down the default settings for RandomWriter for the test-case
    // Generates NUM_HADOOP_SLAVES * RW_MAPS_PER_HOST * RW_BYTES_PER_MAP -> 1MB
    job.setInt("test.randomwrite.bytes_per_map", RW_BYTES_PER_MAP);
    job.setInt("test.randomwriter.maps_per_host", RW_MAPS_PER_HOST);
    String[] rwArgs = {sortInput.toString()};
    
    // Run RandomWriter
    assertEquals(ToolRunner.run(job, new RandomWriter(), rwArgs), 0);
  }


  static class SinkMapper<K, V>
  extends MapReduceBase implements Mapper<K, V, K, V> {

    public void map(K key, V val,
        OutputCollector<K, V> output, Reporter reporter)
    throws IOException {
      // Don't output anything!
      if (false) output.collect(key, val);
    }
  }

  private static void runSort(JobConf jobConf, Path sortInput, Path sortOutput) 
  throws Exception {
    // Set up the job
    jobConf.setJobName("null-sorter");
    
    jobConf.setMapperClass(SinkMapper.class);
    jobConf.setReducerClass(IdentityReducer.class);

    jobConf.setNumReduceTasks(2);

    jobConf.setInputFormat(SequenceFileInputFormat.class);
    jobConf.setOutputFormat(SequenceFileOutputFormat.class);

    jobConf.setOutputKeyClass(BytesWritable.class);
    jobConf.setOutputValueClass(BytesWritable.class);
    
    FileInputFormat.setInputPaths(jobConf, sortInput);
    FileOutputFormat.setOutputPath(jobConf, sortOutput);

    // Compress the intermediate map-outputs!
    jobConf.setCompressMapOutput(true);

    // Run the job
    JobClient.runJob(jobConf);
  }
  
  public void testMapReduceSortWithCompressedEmptyMapOutputs() 
  throws Exception {
    MiniDFSCluster dfs = null;
    MiniMRCluster mr = null;
    FileSystem fileSys = null;
    try {
      Configuration conf = new Configuration();

      // Start the mini-MR and mini-DFS clusters
      dfs = new MiniDFSCluster(conf, NUM_HADOOP_SLAVES, true, null);
      fileSys = dfs.getFileSystem();
      mr = new MiniMRCluster(NUM_HADOOP_SLAVES, fileSys.getUri().toString(), 1);

      // Run randomwriter to generate input for 'sort'
      runRandomWriter(mr.createJobConf(), SORT_INPUT_PATH);
      
      // Run sort
      runSort(mr.createJobConf(), SORT_INPUT_PATH, SORT_OUTPUT_PATH);
    } finally {
      if (dfs != null) { dfs.shutdown(); }
      if (mr != null) { mr.shutdown();
      }
    }
  }

}
