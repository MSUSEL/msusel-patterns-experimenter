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

import java.io.*;
import java.lang.reflect.*;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapred.*;

/** An input format that selects a RecordReader based on a JobConf property.
 *  This should be used only for non-standard record reader such as 
 *  StreamXmlRecordReader. For all other standard 
 *  record readers, the appropriate input format classes should be used.
 */
public class StreamInputFormat extends KeyValueTextInputFormat {

  @SuppressWarnings("unchecked")
  public RecordReader<Text, Text> getRecordReader(final InputSplit genericSplit,
                                      JobConf job, Reporter reporter) throws IOException {
    String c = job.get("stream.recordreader.class");
    if (c == null || c.indexOf("LineRecordReader") >= 0) {
      return super.getRecordReader(genericSplit, job, reporter);
    }

    // handling non-standard record reader (likely StreamXmlRecordReader) 
    FileSplit split = (FileSplit) genericSplit;
    LOG.info("getRecordReader start.....split=" + split);
    reporter.setStatus(split.toString());

    // Open the file and seek to the start of the split
    FileSystem fs = split.getPath().getFileSystem(job);
    FSDataInputStream in = fs.open(split.getPath());

    // Factory dispatch based on available params..
    Class readerClass;

    {
      readerClass = StreamUtil.goodClassOrNull(job, c, null);
      if (readerClass == null) {
        throw new RuntimeException("Class not found: " + c);
      }
    }

    Constructor ctor;
    try {
      ctor = readerClass.getConstructor(new Class[] { FSDataInputStream.class,
                                                      FileSplit.class, Reporter.class, JobConf.class, FileSystem.class });
    } catch (NoSuchMethodException nsm) {
      throw new RuntimeException(nsm);
    }

    RecordReader<Text, Text> reader;
    try {
      reader = (RecordReader<Text, Text>) ctor.newInstance(new Object[] { in, split,
                                                              reporter, job, fs });
    } catch (Exception nsm) {
      throw new RuntimeException(nsm);
    }
    return reader;
  }

}
