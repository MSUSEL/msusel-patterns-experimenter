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
package org.apache.hadoop.mapred.jobcontrol;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

/**
 * Utility methods used in various Job Control unit tests.
 */
public class JobControlTestUtils {

  static private Random rand = new Random();

  private static NumberFormat idFormat = NumberFormat.getInstance();

  static {
    idFormat.setMinimumIntegerDigits(4);
    idFormat.setGroupingUsed(false);
  }

  /**
   * Cleans the data from the passed Path in the passed FileSystem.
   * 
   * @param fs FileSystem to delete data from.
   * @param dirPath Path to be deleted.
   * @throws IOException If an error occurs cleaning the data.
   */
  static void cleanData(FileSystem fs, Path dirPath) throws IOException {
    fs.delete(dirPath, true);
  }

  /**
   * Generates a string of random digits.
   * 
   * @return A random string.
   */
  private static String generateRandomWord() {
    return idFormat.format(rand.nextLong());
  }

  /**
   * Generates a line of random text.
   * 
   * @return A line of random text.
   */
  private static String generateRandomLine() {
    long r = rand.nextLong() % 7;
    long n = r + 20;
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < n; i++) {
      sb.append(generateRandomWord()).append(" ");
    }
    sb.append("\n");
    return sb.toString();
  }

  /**
   * Generates data that can be used for Job Control tests.
   * 
   * @param fs FileSystem to create data in.
   * @param dirPath Path to create the data in.
   * @throws IOException If an error occurs creating the data.
   */
  static void generateData(FileSystem fs, Path dirPath) throws IOException {
    FSDataOutputStream out = fs.create(new Path(dirPath, "data.txt"));
    for (int i = 0; i < 10000; i++) {
      String line = generateRandomLine();
      out.write(line.getBytes("UTF-8"));
    }
    out.close();
  }

  /**
   * Creates a simple copy job.
   * 
   * @param indirs List of input directories.
   * @param outdir Output directory.
   * @return JobConf initialised for a simple copy job.
   * @throws Exception If an error occurs creating job configuration.
   */
  static JobConf createCopyJob(List<Path> indirs, Path outdir) throws Exception {

    Configuration defaults = new Configuration();
    JobConf theJob = new JobConf(defaults, TestJobControl.class);
    theJob.setJobName("DataMoveJob");

    FileInputFormat.setInputPaths(theJob, indirs.toArray(new Path[0]));
    theJob.setMapperClass(DataCopy.class);
    FileOutputFormat.setOutputPath(theJob, outdir);
    theJob.setOutputKeyClass(Text.class);
    theJob.setOutputValueClass(Text.class);
    theJob.setReducerClass(DataCopy.class);
    theJob.setNumMapTasks(12);
    theJob.setNumReduceTasks(4);
    return theJob;
  }

  /**
   * Simple Mapper and Reducer implementation which copies data it reads in.
   */
  public static class DataCopy extends MapReduceBase implements
      Mapper<LongWritable, Text, Text, Text>, Reducer<Text, Text, Text, Text> {
    public void map(LongWritable key, Text value, OutputCollector<Text, Text> output,
        Reporter reporter) throws IOException {
      output.collect(new Text(key.toString()), value);
    }

    public void reduce(Text key, Iterator<Text> values,
        OutputCollector<Text, Text> output, Reporter reporter)
        throws IOException {
      Text dumbKey = new Text("");
      while (values.hasNext()) {
        Text data = (Text) values.next();
        output.collect(dumbKey, data);
      }
    }
  }

}
