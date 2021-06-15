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
package org.apache.hadoop.mapred.lib;

import java.io.*;
import java.util.*;
import junit.framework.TestCase;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

public class TestLineInputFormat extends TestCase {
  private static int MAX_LENGTH = 200;
  
  private static JobConf defaultConf = new JobConf();
  private static FileSystem localFs = null; 

  static {
    try {
      localFs = FileSystem.getLocal(defaultConf);
    } catch (IOException e) {
      throw new RuntimeException("init failure", e);
    }
  }

  private static Path workDir = 
    new Path(new Path(System.getProperty("test.build.data", "."), "data"),
             "TestLineInputFormat");
  
  public void testFormat() throws Exception {
    JobConf job = new JobConf();
    Path file = new Path(workDir, "test.txt");

    localFs.delete(workDir, true);
    FileInputFormat.setInputPaths(job, workDir);
    int numLinesPerMap = 5;
    job.setInt("mapred.line.input.format.linespermap", numLinesPerMap);

    // for a variety of lengths
    for (int length = 0; length < MAX_LENGTH;
         length += 1) {
      System.out.println("Processing file of length "+length);
      // create a file with length entries
      Writer writer = new OutputStreamWriter(localFs.create(file));
      try {
        for (int i = 0; i < length; i++) {
          writer.write(Integer.toString(i));
          writer.write("\n");
        }
      } finally {
        writer.close();
      }
      int lastN = 0;
      if (length != 0) {
        lastN = length % numLinesPerMap;
        if (lastN == 0) {
          lastN = numLinesPerMap;
        }
      }
      checkFormat(job, numLinesPerMap, lastN);
    }
  }

  // A reporter that does nothing
  private static final Reporter voidReporter = Reporter.NULL;
  
  void checkFormat(JobConf job, int expectedN, int lastN) throws IOException{
    NLineInputFormat format = new NLineInputFormat();
    format.configure(job);
    int ignoredNumSplits = 1;
    InputSplit[] splits = format.getSplits(job, ignoredNumSplits);

    // check all splits except last one
    int count = 0;
    for (int j = 0; j < splits.length; j++) {
      System.out.println("Processing split "+splits[j]);
      assertEquals("There are no split locations", 0,
                   splits[j].getLocations().length);
      RecordReader<LongWritable, Text> reader =
        format.getRecordReader(splits[j], job, voidReporter);
      Class readerClass = reader.getClass();
      assertEquals("reader class is LineRecordReader.",
                   LineRecordReader.class, readerClass);        
      LongWritable key = reader.createKey();
      Class keyClass = key.getClass();
      assertEquals("Key class is LongWritable.", LongWritable.class, keyClass);
      Text value = reader.createValue();
      Class valueClass = value.getClass();
      assertEquals("Value class is Text.", Text.class, valueClass);
         
      try {
        count = 0;
        while (reader.next(key, value)) {
          System.out.println("Got "+key+" "+value+" at count "+count+" of split "+j);
          count++;
        }
      } finally {
        reader.close();
      }
      if ( j == splits.length - 1) {
        assertEquals("number of lines in split(" + j + ") is wrong" ,
                     lastN, count);
      } else {
        assertEquals("number of lines in split(" + j + ") is wrong" ,
                     expectedN, count);
      }
    }
  }

  public static void main(String[] args) throws Exception {
    new TestLineInputFormat().testFormat();
  }
}
