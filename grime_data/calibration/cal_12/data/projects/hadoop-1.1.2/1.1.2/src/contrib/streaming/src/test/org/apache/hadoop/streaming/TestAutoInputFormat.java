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
package org.apache.hadoop.streaming;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.streaming.AutoInputFormat;

import junit.framework.TestCase;

public class TestAutoInputFormat extends TestCase {

  private static Configuration conf = new Configuration();

  private static final int LINES_COUNT = 3;

  private static final int RECORDS_COUNT = 3;

  private static final int SPLITS_COUNT = 2;

  @SuppressWarnings( { "unchecked", "deprecation" })
  public void testFormat() throws IOException {
    JobConf job = new JobConf(conf);
    FileSystem fs = FileSystem.getLocal(conf);
    Path dir = new Path(System.getProperty("test.build.data", ".") + "/mapred");
    Path txtFile = new Path(dir, "auto.txt");
    Path seqFile = new Path(dir, "auto.seq");

    fs.delete(dir, true);

    FileInputFormat.setInputPaths(job, dir);

    Writer txtWriter = new OutputStreamWriter(fs.create(txtFile));
    try {
      for (int i = 0; i < LINES_COUNT; i++) {
        txtWriter.write("" + (10 * i));
        txtWriter.write("\n");
      }
    } finally {
      txtWriter.close();
    }

    SequenceFile.Writer seqWriter = SequenceFile.createWriter(fs, conf,
      seqFile, IntWritable.class, LongWritable.class);
    try {
      for (int i = 0; i < RECORDS_COUNT; i++) {
        IntWritable key = new IntWritable(11 * i);
        LongWritable value = new LongWritable(12 * i);
        seqWriter.append(key, value);
      }
    } finally {
      seqWriter.close();
    }

    AutoInputFormat format = new AutoInputFormat();
    InputSplit[] splits = format.getSplits(job, SPLITS_COUNT);
    for (InputSplit split : splits) {
      RecordReader reader = format.getRecordReader(split, job, Reporter.NULL);
      Object key = reader.createKey();
      Object value = reader.createValue();
      try {
        while (reader.next(key, value)) {
          if (key instanceof LongWritable) {
            assertEquals("Wrong value class.", Text.class, value.getClass());
            assertTrue("Invalid value", Integer.parseInt(((Text) value)
              .toString()) % 10 == 0);
          } else {
            assertEquals("Wrong key class.", IntWritable.class, key.getClass());
            assertEquals("Wrong value class.", LongWritable.class, value
              .getClass());
            assertTrue("Invalid key.", ((IntWritable) key).get() % 11 == 0);
            assertTrue("Invalid value.", ((LongWritable) value).get() % 12 == 0);
          }
        }
      } finally {
        reader.close();
      }
    }
  }

}
