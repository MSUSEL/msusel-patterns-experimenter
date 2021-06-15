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

import java.io.EOFException;
import java.io.IOException;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputFormat;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.SequenceFileInputFormat;
import org.apache.hadoop.mapred.TextInputFormat;

/**
 * An {@link InputFormat} that tries to deduce the types of the input files
 * automatically. It can currently handle text and sequence files.
 */
public class AutoInputFormat extends FileInputFormat {

  private TextInputFormat textInputFormat = new TextInputFormat();

  private SequenceFileInputFormat seqFileInputFormat = 
    new SequenceFileInputFormat();

  public void configure(JobConf job) {
    textInputFormat.configure(job);
    // SequenceFileInputFormat has no configure() method
  }

  public RecordReader getRecordReader(InputSplit split, JobConf job,
    Reporter reporter) throws IOException {
    FileSplit fileSplit = (FileSplit) split;
    FileSystem fs = FileSystem.get(job);
    FSDataInputStream is = fs.open(fileSplit.getPath());
    byte[] header = new byte[3];
    RecordReader reader = null;
    try {
      is.readFully(header);
    } catch (EOFException eof) {
      reader = textInputFormat.getRecordReader(split, job, reporter);
    } finally {
      is.close();
    }
    if (header[0] == 'S' && header[1] == 'E' && header[2] == 'Q') {
      reader = seqFileInputFormat.getRecordReader(split, job, reporter);
    } else {
      reader = textInputFormat.getRecordReader(split, job, reporter);
    }
    return reader;
  }

}
