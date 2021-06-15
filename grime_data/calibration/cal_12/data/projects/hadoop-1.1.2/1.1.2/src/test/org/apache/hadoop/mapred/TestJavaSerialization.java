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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.serializer.JavaSerializationComparator;

public class TestJavaSerialization extends ClusterMapReduceTestCase {
  
  static class WordCountMapper extends MapReduceBase implements
      Mapper<LongWritable, Text, String, Long> {

    public void map(LongWritable key, Text value,
        OutputCollector<String, Long> output, Reporter reporter)
        throws IOException {
      StringTokenizer st = new StringTokenizer(value.toString());
      while (st.hasMoreTokens()) {
        output.collect(st.nextToken(), 1L);
      }
    }

  }
  
  static class SumReducer<K> extends MapReduceBase implements
      Reducer<K, Long, K, Long> {
    
    public void reduce(K key, Iterator<Long> values,
        OutputCollector<K, Long> output, Reporter reporter)
      throws IOException {

      long sum = 0;
      while (values.hasNext()) {
        sum += values.next();
      }
      output.collect(key, sum);
    }
    
  }
  
  public void testMapReduceJob() throws Exception {
    OutputStream os = getFileSystem().create(new Path(getInputDir(),
        "text.txt"));
    Writer wr = new OutputStreamWriter(os);
    wr.write("b a\n");
    wr.close();

    JobConf conf = createJobConf();
    conf.setJobName("JavaSerialization");
    
    conf.set("io.serializations",
    "org.apache.hadoop.io.serializer.JavaSerialization," +
    "org.apache.hadoop.io.serializer.WritableSerialization");

    conf.setInputFormat(TextInputFormat.class);

    conf.setOutputKeyClass(String.class);
    conf.setOutputValueClass(Long.class);
    conf.setOutputKeyComparatorClass(JavaSerializationComparator.class);

    conf.setMapperClass(WordCountMapper.class);
    conf.setReducerClass(SumReducer.class);

    FileInputFormat.setInputPaths(conf, getInputDir());

    FileOutputFormat.setOutputPath(conf, getOutputDir());

    JobClient.runJob(conf);

    Path[] outputFiles = FileUtil.stat2Paths(
                           getFileSystem().listStatus(getOutputDir(),
                           new Utils.OutputFileUtils.OutputFilesFilter()));
    assertEquals(1, outputFiles.length);
    InputStream is = getFileSystem().open(outputFiles[0]);
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    assertEquals("a\t1", reader.readLine());
    assertEquals("b\t1", reader.readLine());
    assertNull(reader.readLine());
    reader.close();
  }

  /**
   * HADOOP-4466:
   * This test verifies the JavSerialization impl can write to SequenceFiles. by virtue other
   * SequenceFileOutputFormat is not coupled to Writable types, if so, the job will fail.
   *
   */
  public void testWriteToSequencefile() throws Exception {
    OutputStream os = getFileSystem().create(new Path(getInputDir(),
        "text.txt"));
    Writer wr = new OutputStreamWriter(os);
    wr.write("b a\n");
    wr.close();

    JobConf conf = createJobConf();
    conf.setJobName("JavaSerialization");

    conf.set("io.serializations",
    "org.apache.hadoop.io.serializer.JavaSerialization," +
    "org.apache.hadoop.io.serializer.WritableSerialization");

    conf.setInputFormat(TextInputFormat.class);
    conf.setOutputFormat(SequenceFileOutputFormat.class); // test we can write to sequence files

    conf.setOutputKeyClass(String.class);
    conf.setOutputValueClass(Long.class);
    conf.setOutputKeyComparatorClass(JavaSerializationComparator.class);

    conf.setMapperClass(WordCountMapper.class);
    conf.setReducerClass(SumReducer.class);

    FileInputFormat.setInputPaths(conf, getInputDir());

    FileOutputFormat.setOutputPath(conf, getOutputDir());

    JobClient.runJob(conf);

    Path[] outputFiles = FileUtil.stat2Paths(
                           getFileSystem().listStatus(getOutputDir(),
                           new Utils.OutputFileUtils.OutputFilesFilter()));
    assertEquals(1, outputFiles.length);
}

}
