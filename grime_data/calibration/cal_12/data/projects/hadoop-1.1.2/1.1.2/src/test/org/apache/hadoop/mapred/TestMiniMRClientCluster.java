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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.Job;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Basic testing for the MiniMRClientCluster. This test shows an example class
 * that can be used in MR1 or MR2, without any change to the test. The test will
 * use MiniMRYarnCluster in MR2, and MiniMRCluster in MR1.
 */
public class TestMiniMRClientCluster {

  private static Path inDir = null;
  private static Path outDir = null;
  private static Path testdir = null;
  private static Path[] inFiles = new Path[5];
  private static MiniMRClientCluster mrCluster;

  @BeforeClass
  public static void setup() throws IOException {
    final Configuration conf = new Configuration();
    final Path TEST_ROOT_DIR = new Path(System.getProperty("test.build.data",
        "/tmp"));
    testdir = new Path(TEST_ROOT_DIR, "TestMiniMRClientCluster");
    inDir = new Path(testdir, "in");
    outDir = new Path(testdir, "out");

    FileSystem fs = FileSystem.getLocal(conf);
    if (fs.exists(testdir) && !fs.delete(testdir, true)) {
      throw new IOException("Could not delete " + testdir);
    }
    if (!fs.mkdirs(inDir)) {
      throw new IOException("Mkdirs failed to create " + inDir);
    }

    for (int i = 0; i < inFiles.length; i++) {
      inFiles[i] = new Path(inDir, "part_" + i);
      createFile(inFiles[i], conf);
    }

    // create the mini cluster to be used for the tests
    mrCluster = MiniMRClientClusterFactory.create(
        TestMiniMRClientCluster.class, 1, new Configuration());
  }

  @AfterClass
  public static void cleanup() throws IOException {
    // clean up the input and output files
    final Configuration conf = new Configuration();
    final FileSystem fs = testdir.getFileSystem(conf);
    if (fs.exists(testdir)) {
      fs.delete(testdir, true);
    }
    // stopping the mini cluster
    mrCluster.stop();
  }

  @Test
  public void testJob() throws Exception {
    final Job job = createJob();
    org.apache.hadoop.mapreduce.lib.input.FileInputFormat.setInputPaths(job,
        inDir);
    org.apache.hadoop.mapreduce.lib.output.FileOutputFormat.setOutputPath(job,
        new Path(outDir, "testJob"));
    assertTrue(job.waitForCompletion(true));
    validateCounters(job.getCounters(), 5, 25, 5, 5);
  }

  private void validateCounters(Counters counters, long mapInputRecords,
      long mapOutputRecords, long reduceInputGroups, long reduceOutputRecords) {
    assertEquals("MapInputRecords", mapInputRecords, counters.findCounter(
        "MyCounterGroup", "MAP_INPUT_RECORDS").getValue());
    assertEquals("MapOutputRecords", mapOutputRecords, counters.findCounter(
        "MyCounterGroup", "MAP_OUTPUT_RECORDS").getValue());
    assertEquals("ReduceInputGroups", reduceInputGroups, counters.findCounter(
        "MyCounterGroup", "REDUCE_INPUT_GROUPS").getValue());
    assertEquals("ReduceOutputRecords", reduceOutputRecords, counters
        .findCounter("MyCounterGroup", "REDUCE_OUTPUT_RECORDS").getValue());
  }

  private static void createFile(Path inFile, Configuration conf)
      throws IOException {
    final FileSystem fs = inFile.getFileSystem(conf);
    if (fs.exists(inFile)) {
      return;
    }
    FSDataOutputStream out = fs.create(inFile);
    out.writeBytes("This is a test file");
    out.close();
  }

  public static Job createJob() throws IOException {
    final Job baseJob = new Job(mrCluster.getConfig());
    baseJob.setOutputKeyClass(Text.class);
    baseJob.setOutputValueClass(IntWritable.class);
    baseJob.setMapperClass(MyMapper.class);
    baseJob.setReducerClass(MyReducer.class);
    baseJob.setNumReduceTasks(1);
    return baseJob;
  }

  public static class MyMapper extends
      org.apache.hadoop.mapreduce.Mapper<Object, Text, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(Object key, Text value, Context context)
        throws IOException, InterruptedException {
      context.getCounter("MyCounterGroup", "MAP_INPUT_RECORDS").increment(1);
      StringTokenizer iter = new StringTokenizer(value.toString());
      while (iter.hasMoreTokens()) {
        word.set(iter.nextToken());
        context.write(word, one);
        context.getCounter("MyCounterGroup", "MAP_OUTPUT_RECORDS").increment(1);
      }
    }
  }

  public static class MyReducer extends
      org.apache.hadoop.mapreduce.Reducer<Text, IntWritable, Text, IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values, Context context)
        throws IOException, InterruptedException {
      context.getCounter("MyCounterGroup", "REDUCE_INPUT_GROUPS").increment(1);
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
      context.getCounter("MyCounterGroup", "REDUCE_OUTPUT_RECORDS")
          .increment(1);
    }
  }

}
