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
package org.apache.hadoop.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.SequenceFile.Sorter.RawKeyValueIterator;
import org.apache.hadoop.io.SequenceFile.Sorter.SegmentDescriptor;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.DefaultCodec;
import org.apache.hadoop.mapred.*;

import junit.framework.TestCase;
import org.apache.commons.logging.*;

public class TestSequenceFileMergeProgress extends TestCase {
  private static final Log LOG = FileInputFormat.LOG;
  private static final int RECORDS = 10000;
  
  public void testMergeProgressWithNoCompression() throws IOException {
    runTest(SequenceFile.CompressionType.NONE);
  }

  public void testMergeProgressWithRecordCompression() throws IOException {
    runTest(SequenceFile.CompressionType.RECORD);
  }

  public void testMergeProgressWithBlockCompression() throws IOException {
    runTest(SequenceFile.CompressionType.BLOCK);
  }

  public void runTest(CompressionType compressionType) throws IOException {
    JobConf job = new JobConf();
    FileSystem fs = FileSystem.getLocal(job);
    Path dir = new Path(System.getProperty("test.build.data",".") + "/mapred");
    Path file = new Path(dir, "test.seq");
    Path tempDir = new Path(dir, "tmp");

    fs.delete(dir, true);
    FileInputFormat.setInputPaths(job, dir);
    fs.mkdirs(tempDir);

    LongWritable tkey = new LongWritable();
    Text tval = new Text();

    SequenceFile.Writer writer =
      SequenceFile.createWriter(fs, job, file, LongWritable.class, Text.class,
        compressionType, new DefaultCodec());
    try {
      for (int i = 0; i < RECORDS; ++i) {
        tkey.set(1234);
        tval.set("valuevaluevaluevaluevaluevaluevaluevaluevaluevaluevalue");
        writer.append(tkey, tval);
      }
    } finally {
      writer.close();
    }
    
    long fileLength = fs.getFileStatus(file).getLen();
    LOG.info("With compression = " + compressionType + ": "
        + "compressed length = " + fileLength);
    
    SequenceFile.Sorter sorter = new SequenceFile.Sorter(fs, 
        job.getOutputKeyComparator(), job.getMapOutputKeyClass(),
        job.getMapOutputValueClass(), job);
    Path[] paths = new Path[] {file};
    RawKeyValueIterator rIter = sorter.merge(paths, tempDir, false);
    int count = 0;
    while (rIter.next()) {
      count++;
    }
    assertEquals(RECORDS, count);
    assertEquals(1.0f, rIter.getProgress().get());
  }

}
