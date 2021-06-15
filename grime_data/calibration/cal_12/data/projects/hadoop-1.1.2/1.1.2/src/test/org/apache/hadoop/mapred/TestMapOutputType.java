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

import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.lib.*;
import junit.framework.TestCase;
import java.io.*;
import java.util.*;

/** 
 * TestMapOutputType checks whether the Map task handles type mismatch
 * between mapper output and the type specified in
 * JobConf.MapOutputKeyType and JobConf.MapOutputValueType.
 */
public class TestMapOutputType extends TestCase 
{
  JobConf conf = new JobConf(TestMapOutputType.class);
  JobClient jc;
  /** 
   * TextGen is a Mapper that generates a Text key-value pair. The
   * type specified in conf will be anything but.
   */
   
  static class TextGen
    implements Mapper<WritableComparable, Writable, Text, Text> {
    
    public void configure(JobConf job) {
    }
    
    public void map(WritableComparable key, Writable val,
                    OutputCollector<Text, Text> out,
                    Reporter reporter) throws IOException {
      out.collect(new Text("Hello"), new Text("World"));
    }
    
    public void close() {
    }
  }
  
  /** A do-nothing reducer class. We won't get this far, really.
   *
   */
  static class TextReduce
    implements Reducer<Text, Text, Text, Text> {
    
    public void configure(JobConf job) {
    }

    public void reduce(Text key,
                       Iterator<Text> values,
                       OutputCollector<Text, Text> out,
                       Reporter reporter) throws IOException {
      out.collect(new Text("Test"), new Text("Me"));
    }

    public void close() {
    }
  }


  public void configure() throws Exception {
    Path testdir = new Path("build/test/test.mapred.spill");
    Path inDir = new Path(testdir, "in");
    Path outDir = new Path(testdir, "out");
    FileSystem fs = FileSystem.get(conf);
    fs.delete(testdir, true);
    conf.setInt("io.sort.mb", 1);
    conf.setInputFormat(SequenceFileInputFormat.class);
    FileInputFormat.setInputPaths(conf, inDir);
    FileOutputFormat.setOutputPath(conf, outDir);
    conf.setMapperClass(TextGen.class);
    conf.setReducerClass(TextReduce.class);
    conf.setOutputKeyClass(Text.class);
    conf.setOutputValueClass(Text.class); 
    
    conf.setOutputFormat(SequenceFileOutputFormat.class);
    if (!fs.mkdirs(testdir)) {
      throw new IOException("Mkdirs failed to create " + testdir.toString());
    }
    if (!fs.mkdirs(inDir)) {
      throw new IOException("Mkdirs failed to create " + inDir.toString());
    }
    Path inFile = new Path(inDir, "part0");
    SequenceFile.Writer writer = SequenceFile.createWriter(fs, conf, inFile, 
                                                           Text.class, Text.class);
    writer.append(new Text("rec: 1"), new Text("Hello"));
    writer.close();
    
    jc = new JobClient(conf);
  }
  
  public void testKeyMismatch() throws Exception {
    configure();
    
    //  Set bad MapOutputKeyClass and MapOutputValueClass
    conf.setMapOutputKeyClass(IntWritable.class);
    conf.setMapOutputValueClass(IntWritable.class);
    
    RunningJob r_job = jc.submitJob(conf);
    while (!r_job.isComplete()) {
      Thread.sleep(1000);
    }
    
    if (r_job.isSuccessful()) {
      fail("Oops! The job was supposed to break due to an exception");
    }
  }
  
  public void testValueMismatch() throws Exception {
    configure();
  
    // Set good MapOutputKeyClass, bad MapOutputValueClass    
    conf.setMapOutputKeyClass(Text.class);
    conf.setMapOutputValueClass(IntWritable.class);
    
    RunningJob r_job = jc.submitJob(conf);
    while (!r_job.isComplete()) {
      Thread.sleep(1000);
    }
    
    if (r_job.isSuccessful()) {
      fail("Oops! The job was supposed to break due to an exception");
    }
  }
  
  public void testNoMismatch() throws Exception{ 
    configure();
    
    //  Set good MapOutputKeyClass and MapOutputValueClass    
    conf.setMapOutputKeyClass(Text.class);
    conf.setMapOutputValueClass(Text.class);
     
    RunningJob r_job = jc.submitJob(conf);
    while (!r_job.isComplete()) {
      Thread.sleep(1000);
    }
     
    if (!r_job.isSuccessful()) {
      fail("Oops! The job broke due to an unexpected error");
    }
  }
}
