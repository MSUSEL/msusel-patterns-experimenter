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
package org.apache.hadoop.mapreduce.lib.input;

import java.io.*;
import java.util.*;
import junit.framework.TestCase;

import org.apache.commons.logging.*;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MapContext;
import org.apache.hadoop.mapreduce.MapReduceTestUtil;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.conf.*;

public class TestMRSequenceFileInputFilter extends TestCase {
  private static final Log LOG = 
    LogFactory.getLog(TestMRSequenceFileInputFilter.class.getName());

  private static final int MAX_LENGTH = 15000;
  private static final Configuration conf = new Configuration();
  private static final Job job;
  private static final FileSystem fs;
  private static final Path inDir = 
    new Path(System.getProperty("test.build.data",".") + "/mapred");
  private static final Path inFile = new Path(inDir, "test.seq");
  private static final Random random = new Random(1);
  
  static {
    try {
      job = new Job(conf);
      FileInputFormat.setInputPaths(job, inDir);
      fs = FileSystem.getLocal(conf);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  private static void createSequenceFile(int numRecords) throws Exception {
    // create a file with length entries
    SequenceFile.Writer writer =
      SequenceFile.createWriter(fs, conf, inFile,
                                Text.class, BytesWritable.class);
    try {
      for (int i = 1; i <= numRecords; i++) {
        Text key = new Text(Integer.toString(i));
        byte[] data = new byte[random.nextInt(10)];
        random.nextBytes(data);
        BytesWritable value = new BytesWritable(data);
        writer.append(key, value);
      }
    } finally {
      writer.close();
    }
  }


  private int countRecords(int numSplits) 
      throws IOException, InterruptedException {
    InputFormat<Text, BytesWritable> format =
      new SequenceFileInputFilter<Text, BytesWritable>();
    if (numSplits == 0) {
      numSplits =
        random.nextInt(MAX_LENGTH / (SequenceFile.SYNC_INTERVAL / 20)) + 1;
    }
    FileInputFormat.setMaxInputSplitSize(job, 
      fs.getFileStatus(inFile).getLen() / numSplits);
    TaskAttemptContext context = MapReduceTestUtil.
      createDummyMapTaskAttemptContext(job.getConfiguration());
    // check each split
    int count = 0;
    for (InputSplit split : format.getSplits(job)) {
      RecordReader<Text, BytesWritable> reader =
        format.createRecordReader(split, context);
      MapContext<Text, BytesWritable, Text, BytesWritable> mcontext = 
        new MapContext<Text, BytesWritable, Text, BytesWritable>(
        job.getConfiguration(), 
        context.getTaskAttemptID(), reader, null, null, 
        MapReduceTestUtil.createDummyReporter(), split);
      reader.initialize(split, mcontext);
      try {
        while (reader.nextKeyValue()) {
          LOG.info("Accept record " + reader.getCurrentKey().toString());
          count++;
        }
      } finally {
        reader.close();
      }
    }
    return count;
  }
  
  public void testRegexFilter() throws Exception {
    // set the filter class
    LOG.info("Testing Regex Filter with patter: \\A10*");
    SequenceFileInputFilter.setFilterClass(job, 
      SequenceFileInputFilter.RegexFilter.class);
    SequenceFileInputFilter.RegexFilter.setPattern(
      job.getConfiguration(), "\\A10*");
    
    // clean input dir
    fs.delete(inDir, true);
  
    // for a variety of lengths
    for (int length = 1; length < MAX_LENGTH;
         length += random.nextInt(MAX_LENGTH / 10) + 1) {
      LOG.info("******Number of records: " + length);
      createSequenceFile(length);
      int count = countRecords(0);
      assertEquals(count, length==0 ? 0 : (int)Math.log10(length) + 1);
    }
    
    // clean up
    fs.delete(inDir, true);
  }

  public void testPercentFilter() throws Exception {
    LOG.info("Testing Percent Filter with frequency: 1000");
    // set the filter class
    SequenceFileInputFilter.setFilterClass(job, 
      SequenceFileInputFilter.PercentFilter.class);
    SequenceFileInputFilter.PercentFilter.setFrequency(
      job.getConfiguration(), 1000);
      
    // clean input dir
    fs.delete(inDir, true);
    
    // for a variety of lengths
    for (int length = 0; length < MAX_LENGTH;
         length += random.nextInt(MAX_LENGTH / 10) + 1) {
      LOG.info("******Number of records: "+length);
      createSequenceFile(length);
      int count = countRecords(1);
      LOG.info("Accepted " + count + " records");
      int expectedCount = length / 1000;
      if (expectedCount * 1000 != length)
        expectedCount++;
      assertEquals(count, expectedCount);
    }
      
    // clean up
    fs.delete(inDir, true);
  }
  
  public void testMD5Filter() throws Exception {
    // set the filter class
    LOG.info("Testing MD5 Filter with frequency: 1000");
    SequenceFileInputFilter.setFilterClass(job, 
      SequenceFileInputFilter.MD5Filter.class);
    SequenceFileInputFilter.MD5Filter.setFrequency(
      job.getConfiguration(), 1000);
      
    // clean input dir
    fs.delete(inDir, true);
    
    // for a variety of lengths
    for (int length = 0; length < MAX_LENGTH;
         length += random.nextInt(MAX_LENGTH / 10) + 1) {
      LOG.info("******Number of records: " + length);
      createSequenceFile(length);
      LOG.info("Accepted " + countRecords(0) + " records");
    }
    // clean up
    fs.delete(inDir, true);
  }

  public static void main(String[] args) throws Exception {
    TestMRSequenceFileInputFilter filter = new TestMRSequenceFileInputFilter();
    filter.testRegexFilter();
  }
}
