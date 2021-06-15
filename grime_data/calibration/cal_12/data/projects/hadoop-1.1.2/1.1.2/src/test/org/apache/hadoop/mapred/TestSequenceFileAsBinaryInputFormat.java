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

import java.io.IOException;
import java.util.Random;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;

import junit.framework.TestCase;
import org.apache.commons.logging.*;

public class TestSequenceFileAsBinaryInputFormat extends TestCase {
  private static final Log LOG = FileInputFormat.LOG;
  private static final int RECORDS = 10000;

  public void testBinary() throws IOException {
    JobConf job = new JobConf();
    FileSystem fs = FileSystem.getLocal(job);
    Path dir = new Path(System.getProperty("test.build.data",".") + "/mapred");
    Path file = new Path(dir, "testbinary.seq");
    Random r = new Random();
    long seed = r.nextLong();
    r.setSeed(seed);

    fs.delete(dir, true);
    FileInputFormat.setInputPaths(job, dir);

    Text tkey = new Text();
    Text tval = new Text();

    SequenceFile.Writer writer =
      new SequenceFile.Writer(fs, job, file, Text.class, Text.class);
    try {
      for (int i = 0; i < RECORDS; ++i) {
        tkey.set(Integer.toString(r.nextInt(), 36));
        tval.set(Long.toString(r.nextLong(), 36));
        writer.append(tkey, tval);
      }
    } finally {
      writer.close();
    }

    InputFormat<BytesWritable,BytesWritable> bformat =
      new SequenceFileAsBinaryInputFormat();

    int count = 0;
    r.setSeed(seed);
    BytesWritable bkey = new BytesWritable();
    BytesWritable bval = new BytesWritable();
    Text cmpkey = new Text();
    Text cmpval = new Text();
    DataInputBuffer buf = new DataInputBuffer();
    final int NUM_SPLITS = 3;
    FileInputFormat.setInputPaths(job, file);
    for (InputSplit split : bformat.getSplits(job, NUM_SPLITS)) {
      RecordReader<BytesWritable,BytesWritable> reader =
        bformat.getRecordReader(split, job, Reporter.NULL);
      try {
        while (reader.next(bkey, bval)) {
          tkey.set(Integer.toString(r.nextInt(), 36));
          tval.set(Long.toString(r.nextLong(), 36));
          buf.reset(bkey.getBytes(), bkey.getLength());
          cmpkey.readFields(buf);
          buf.reset(bval.getBytes(), bval.getLength());
          cmpval.readFields(buf);
          assertTrue(
              "Keys don't match: " + "*" + cmpkey.toString() + ":" +
                                           tkey.toString() + "*",
              cmpkey.toString().equals(tkey.toString()));
          assertTrue(
              "Vals don't match: " + "*" + cmpval.toString() + ":" +
                                           tval.toString() + "*",
              cmpval.toString().equals(tval.toString()));
          ++count;
        }
      } finally {
        reader.close();
      }
    }
    assertEquals("Some records not found", RECORDS, count);
  }

}
