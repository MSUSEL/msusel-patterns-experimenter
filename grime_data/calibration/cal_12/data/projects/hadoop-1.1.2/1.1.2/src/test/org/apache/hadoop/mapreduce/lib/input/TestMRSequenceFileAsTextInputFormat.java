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

import java.util.*;
import junit.framework.TestCase;

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

public class TestMRSequenceFileAsTextInputFormat extends TestCase {
  private static int MAX_LENGTH = 10000;
  private static Configuration conf = new Configuration();

  public void testFormat() throws Exception {
    Job job = new Job(conf);
    FileSystem fs = FileSystem.getLocal(conf);
    Path dir = new Path(System.getProperty("test.build.data",".") + "/mapred");
    Path file = new Path(dir, "test.seq");
    
    int seed = new Random().nextInt();
    Random random = new Random(seed);

    fs.delete(dir, true);

    FileInputFormat.setInputPaths(job, dir);

    // for a variety of lengths
    for (int length = 0; length < MAX_LENGTH;
         length += random.nextInt(MAX_LENGTH / 10) + 1) {

      // create a file with length entries
      SequenceFile.Writer writer =
        SequenceFile.createWriter(fs, conf, file,
          IntWritable.class, LongWritable.class);
      try {
        for (int i = 0; i < length; i++) {
          IntWritable key = new IntWritable(i);
          LongWritable value = new LongWritable(10 * i);
          writer.append(key, value);
        }
      } finally {
        writer.close();
      }

      TaskAttemptContext context = MapReduceTestUtil.
        createDummyMapTaskAttemptContext(job.getConfiguration());
      // try splitting the file in a variety of sizes
      InputFormat<Text, Text> format =
        new SequenceFileAsTextInputFormat();
      
      for (int i = 0; i < 3; i++) {
        // check each split
        BitSet bits = new BitSet(length);
        int numSplits =
          random.nextInt(MAX_LENGTH / (SequenceFile.SYNC_INTERVAL / 20)) + 1;
        FileInputFormat.setMaxInputSplitSize(job, 
          fs.getFileStatus(file).getLen() / numSplits);
        for (InputSplit split : format.getSplits(job)) {
          RecordReader<Text, Text> reader =
            format.createRecordReader(split, context);
          MapContext<Text, Text, Text, Text> mcontext = 
            new MapContext<Text, Text, Text, Text>(job.getConfiguration(), 
            context.getTaskAttemptID(), reader, null, null, 
            MapReduceTestUtil.createDummyReporter(), 
            split);
          reader.initialize(split, mcontext);
          Class<?> readerClass = reader.getClass();
          assertEquals("reader class is SequenceFileAsTextRecordReader.",
            SequenceFileAsTextRecordReader.class, readerClass);        
          Text key;
          try {
            int count = 0;
            while (reader.nextKeyValue()) {
              key = reader.getCurrentKey();
              int keyInt = Integer.parseInt(key.toString());
              assertFalse("Key in multiple partitions.", bits.get(keyInt));
              bits.set(keyInt);
              count++;
            }
          } finally {
            reader.close();
          }
        }
        assertEquals("Some keys in no partition.", length, bits.cardinality());
      }

    }
  }

  public static void main(String[] args) throws Exception {
    new TestMRSequenceFileAsTextInputFormat().testFormat();
  }
}
