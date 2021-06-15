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
package org.apache.hadoop.mapreduce.lib.map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.HadoopTestCase;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.DataOutputStream;
import java.io.IOException;

public class TestMultithreadedMapper extends HadoopTestCase {

  public TestMultithreadedMapper() throws IOException {
    super(HadoopTestCase.LOCAL_MR, HadoopTestCase.LOCAL_FS, 1, 1);
  }

  public void testOKRun() throws Exception {
    run(false, false);
  }

  public void testIOExRun() throws Exception {
    run(true, false);
  }
  public void testRuntimeExRun() throws Exception {
    run(false, true);
  }

  private void run(boolean ioEx, boolean rtEx) throws Exception {
    String localPathRoot = System.getProperty("test.build.data", "/tmp");
    Path inDir = new Path(localPathRoot, "testing/mt/input");
    Path outDir = new Path(localPathRoot, "testing/mt/output");


    Configuration conf = createJobConf();
    if (ioEx) {
      conf.setBoolean("multithreaded.ioException", true);
    }
    if (rtEx) {
      conf.setBoolean("multithreaded.runtimeException", true);
    }

    Job job = new Job(conf);
    FileSystem fs = FileSystem.get(conf);
    if (fs.exists(outDir)) {
      fs.delete(outDir, true);
    }
    if (fs.exists(inDir)) {
      fs.delete(inDir, true);
    }
    fs.mkdirs(inDir);
    String input = "The quick brown fox\n" + "has many silly\n"
      + "red fox sox\n";
    DataOutputStream file = fs.create(new Path(inDir, "part-" + 0));
    file.writeBytes(input);
    file.close();

    FileInputFormat.setInputPaths(job, inDir);
    FileOutputFormat.setOutputPath(job, outDir);
    job.setNumReduceTasks(1);
    job.setJobName("mt");

    job.setMapperClass(MultithreadedMapper.class);
    MultithreadedMapper.setMapperClass(job, IDMap.class);
    MultithreadedMapper.setNumberOfThreads(job, 2);
    job.setReducerClass(Reducer.class);

    job.waitForCompletion(true);

    if (job.isSuccessful()) {
      assertFalse(ioEx || rtEx);
    }
    else {
      assertTrue(ioEx || rtEx);
    }
  }

  public static class IDMap extends 
      Mapper<LongWritable, Text, LongWritable, Text> {
    private boolean ioEx = false;
    private boolean rtEx = false;

    public void setup(Context context) {
      ioEx = context.getConfiguration().
               getBoolean("multithreaded.ioException", false);
      rtEx = context.getConfiguration().
               getBoolean("multithreaded.runtimeException", false);
    }

    public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
      if (ioEx) {
        throw new IOException();
      }
      if (rtEx) {
        throw new RuntimeException();
      }
      super.map(key, value, context);
    }
  }
}
