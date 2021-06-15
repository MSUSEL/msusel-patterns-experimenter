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
package org.apache.hadoop.record;

import java.io.*;
import java.util.*;
import junit.framework.TestCase;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.conf.*;
import org.apache.commons.logging.*;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.InputFormat;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.SequenceFileInputFormat;

public class TestRecordWritable extends TestCase {
  private static final Log LOG = FileInputFormat.LOG;

  private static int MAX_LENGTH = 10000;
  private static Configuration conf = new Configuration();

  public void testFormat() throws Exception {
    JobConf job = new JobConf(conf);
    FileSystem fs = FileSystem.getLocal(conf);
    Path dir = new Path(System.getProperty("test.build.data",".") + "/mapred");
    Path file = new Path(dir, "test.seq");
    
    int seed = new Random().nextInt();
    //LOG.info("seed = "+seed);
    Random random = new Random(seed);

    fs.delete(dir, true);

    FileInputFormat.setInputPaths(job, dir);

    // for a variety of lengths
    for (int length = 0; length < MAX_LENGTH;
         length+= random.nextInt(MAX_LENGTH/10)+1) {

      // create a file with length entries
      SequenceFile.Writer writer =
        new SequenceFile.Writer(fs, conf, file,
                                RecInt.class, RecBuffer.class);
      try {
        for (int i = 0; i < length; i++) {
          RecInt key = new RecInt();
          key.setData(i);
          byte[] data = new byte[random.nextInt(10)];
          random.nextBytes(data);
          RecBuffer value = new RecBuffer();
          value.setData(new Buffer(data));
          writer.append(key, value);
        }
      } finally {
        writer.close();
      }

      // try splitting the file in a variety of sizes
      InputFormat<RecInt, RecBuffer> format =
        new SequenceFileInputFormat<RecInt, RecBuffer>();
      RecInt key = new RecInt();
      RecBuffer value = new RecBuffer();
      for (int i = 0; i < 3; i++) {
        int numSplits =
          random.nextInt(MAX_LENGTH/(SequenceFile.SYNC_INTERVAL/20))+1;
        InputSplit[] splits = format.getSplits(job, numSplits);

        // check each split
        BitSet bits = new BitSet(length);
        for (int j = 0; j < splits.length; j++) {
          RecordReader<RecInt, RecBuffer> reader =
            format.getRecordReader(splits[j], job, Reporter.NULL);
          try {
            int count = 0;
            while (reader.next(key, value)) {
              assertFalse("Key in multiple partitions.", bits.get(key.getData()));
              bits.set(key.getData());
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
    new TestRecordWritable().testFormat();
  }
}
