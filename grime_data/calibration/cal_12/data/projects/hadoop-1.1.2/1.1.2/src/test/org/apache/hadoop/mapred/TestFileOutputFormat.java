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

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

public class TestFileOutputFormat extends HadoopTestCase {

  public TestFileOutputFormat() throws IOException {
    super(HadoopTestCase.CLUSTER_MR, HadoopTestCase.LOCAL_FS, 1, 1);
  }

  public void testCustomFile() throws Exception {
    Path inDir = new Path("testing/fileoutputformat/input");
    Path outDir = new Path("testing/fileoutputformat/output");

    // Hack for local FS that does not have the concept of a 'mounting point'
    if (isLocalFS()) {
      String localPathRoot = System.getProperty("test.build.data", "/tmp")
        .replace(' ', '+');
      inDir = new Path(localPathRoot, inDir);
      outDir = new Path(localPathRoot, outDir);
    }


    JobConf conf = createJobConf();
    FileSystem fs = FileSystem.get(conf);

    fs.delete(outDir, true);
    if (!fs.mkdirs(inDir)) {
      throw new IOException("Mkdirs failed to create " + inDir.toString());
    }

    DataOutputStream file = fs.create(new Path(inDir, "part-0"));
    file.writeBytes("a\nb\n\nc\nd\ne");
    file.close();

    file = fs.create(new Path(inDir, "part-1"));
    file.writeBytes("a\nb\n\nc\nd\ne");
    file.close();

    conf.setJobName("fof");
    conf.setInputFormat(TextInputFormat.class);

    conf.setOutputKeyClass(LongWritable.class);
    conf.setOutputValueClass(Text.class);

    conf.setMapOutputKeyClass(LongWritable.class);
    conf.setMapOutputValueClass(Text.class);

    conf.setOutputFormat(TextOutputFormat.class);
    conf.setOutputKeyClass(LongWritable.class);
    conf.setOutputValueClass(Text.class);

    conf.setMapperClass(TestMap.class);
    conf.setReducerClass(TestReduce.class);

    FileInputFormat.setInputPaths(conf, inDir);
    FileOutputFormat.setOutputPath(conf, outDir);

    JobClient jc = new JobClient(conf);
    RunningJob job = jc.submitJob(conf);
    while (!job.isComplete()) {
      Thread.sleep(100);
    }
    assertTrue(job.isSuccessful());

    boolean map0 = false;
    boolean map1 = false;
    boolean reduce = false;
    FileStatus[] statuses = fs.listStatus(outDir);
    for (FileStatus status : statuses) {
      map0 = map0 || status.getPath().getName().equals("test-m-00000");
      map1 = map1 || status.getPath().getName().equals("test-m-00001");
      reduce = reduce || status.getPath().getName().equals("test-r-00000");
    }

    assertTrue(map0);
    assertTrue(map1);
    assertTrue(reduce);
  }

  public static class TestMap implements Mapper<LongWritable, Text,
    LongWritable, Text> {

    public void configure(JobConf conf) {
      try {
        FileSystem fs = FileSystem.get(conf);
        OutputStream os =
          fs.create(FileOutputFormat.getPathForCustomFile(conf, "test"));
        os.write(1);
        os.close();
      }
      catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }

    public void map(LongWritable key, Text value,
                    OutputCollector<LongWritable, Text> output,
                    Reporter reporter) throws IOException {
      output.collect(key, value);
    }

    public void close() throws IOException {
    }
  }

  public static class TestReduce implements Reducer<LongWritable, Text,
    LongWritable, Text> {

    public void configure(JobConf conf) {
      try {
        FileSystem fs = FileSystem.get(conf);
        OutputStream os =
          fs.create(FileOutputFormat.getPathForCustomFile(conf, "test"));
        os.write(1);
        os.close();
      }
      catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }

    public void reduce(LongWritable key, Iterator<Text> values,
                       OutputCollector<LongWritable, Text> output,
                       Reporter reporter) throws IOException {
      while (values.hasNext()) {
        Text value = values.next();
        output.collect(key, value);
      }
    }

    public void close() throws IOException {
    }
  }

}
