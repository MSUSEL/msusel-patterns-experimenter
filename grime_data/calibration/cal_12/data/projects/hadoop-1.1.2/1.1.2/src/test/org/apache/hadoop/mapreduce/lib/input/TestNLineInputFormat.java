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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class TestNLineInputFormat extends TestCase {
  private static int MAX_LENGTH = 200;
  
  private static Configuration conf = new Configuration();
  private static FileSystem localFs = null; 

  static {
    try {
      localFs = FileSystem.getLocal(conf);
    } catch (IOException e) {
      throw new RuntimeException("init failure", e);
    }
  }

  private static Path workDir = 
    new Path(new Path(System.getProperty("test.build.data", "."), "data"),
             "TestNLineInputFormat");
  
  public void testFormat() throws Exception {
    Job job = new Job(conf);
    Path file = new Path(workDir, "test.txt");

    localFs.delete(workDir, true);
    FileInputFormat.setInputPaths(job, workDir);
    int numLinesPerMap = 5;
    NLineInputFormat.setNumLinesPerSplit(job, numLinesPerMap);
    for (int length = 0; length < MAX_LENGTH;
         length += 1) {
 
      // create a file with length entries
      Writer writer = new OutputStreamWriter(localFs.create(file));
      try {
        for (int i = 0; i < length; i++) {
          writer.write(Integer.toString(i)+" some more text");
          writer.write("\n");
        }
      } finally {
        writer.close();
      }
      int lastN = 0;
      if (length != 0) {
        lastN = length % 5;
        if (lastN == 0) {
          lastN = 5;
        }
      }
      checkFormat(job, numLinesPerMap, lastN);
    }
  }

  void checkFormat(Job job, int expectedN, int lastN) 
      throws IOException, InterruptedException {
    NLineInputFormat format = new NLineInputFormat();
    List<InputSplit> splits = format.getSplits(job);
    int count = 0;
    for (int i = 0; i < splits.size(); i++) {
      assertEquals("There are no split locations", 0,
                   splits.get(i).getLocations().length);
      TaskAttemptContext context = MapReduceTestUtil.
        createDummyMapTaskAttemptContext(job.getConfiguration());
      RecordReader<LongWritable, Text> reader = format.createRecordReader(
        splits.get(i), context);
      Class<?> clazz = reader.getClass();
      assertEquals("reader class is LineRecordReader.", 
        LineRecordReader.class, clazz);
      MapContext<LongWritable, Text, LongWritable, Text> mcontext = 
        new MapContext<LongWritable, Text, LongWritable, Text>(
          job.getConfiguration(), context.getTaskAttemptID(), reader, null,
          null, MapReduceTestUtil.createDummyReporter(), splits.get(i));
      reader.initialize(splits.get(i), mcontext);
         
      try {
        count = 0;
        while (reader.nextKeyValue()) {
          count++;
        }
      } finally {
        reader.close();
      }
      if ( i == splits.size() - 1) {
        assertEquals("number of lines in split(" + i + ") is wrong" ,
                     lastN, count);
      } else {
        assertEquals("number of lines in split(" + i + ") is wrong" ,
                     expectedN, count);
      }
    }
  }
  
  public static void main(String[] args) throws Exception {
    new TestNLineInputFormat().testFormat();
  }
}
