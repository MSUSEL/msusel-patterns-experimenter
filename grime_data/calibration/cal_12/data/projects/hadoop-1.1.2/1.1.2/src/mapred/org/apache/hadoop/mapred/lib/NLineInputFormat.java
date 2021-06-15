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

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.JobConfigurable;
import org.apache.hadoop.mapred.LineRecordReader;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.util.LineReader;

/**
 * NLineInputFormat which splits N lines of input as one split.
 *
 * In many "pleasantly" parallel applications, each process/mapper 
 * processes the same input file (s), but with computations are 
 * controlled by different parameters.(Referred to as "parameter sweeps").
 * One way to achieve this, is to specify a set of parameters 
 * (one set per line) as input in a control file 
 * (which is the input path to the map-reduce application,
 * where as the input dataset is specified 
 * via a config variable in JobConf.).
 * 
 * The NLineInputFormat can be used in such applications, that splits 
 * the input file such that by default, one line is fed as
 * a value to one map task, and key is the offset.
 * i.e. (k,v) is (LongWritable, Text).
 * The location hints will span the whole mapred cluster.
 */

public class NLineInputFormat extends FileInputFormat<LongWritable, Text> 
                              implements JobConfigurable { 
  private int N = 1;

  public RecordReader<LongWritable, Text> getRecordReader(
                                            InputSplit genericSplit,
                                            JobConf job,
                                            Reporter reporter) 
  throws IOException {
    reporter.setStatus(genericSplit.toString());
    return new LineRecordReader(job, (FileSplit) genericSplit);
  }

  /** 
   * Logically splits the set of input files for the job, splits N lines
   * of the input as one split.
   * 
   * @see org.apache.hadoop.mapred.FileInputFormat#getSplits(JobConf, int)
   */
  public InputSplit[] getSplits(JobConf job, int numSplits)
  throws IOException {
    ArrayList<FileSplit> splits = new ArrayList<FileSplit>();
    for (FileStatus status : listStatus(job)) {
      Path fileName = status.getPath();
      if (status.isDir()) {
        throw new IOException("Not a file: " + fileName);
      }
      FileSystem  fs = fileName.getFileSystem(job);
      LineReader lr = null;
      try {
        FSDataInputStream in  = fs.open(fileName);
        lr = new LineReader(in, job);
        Text line = new Text();
        int numLines = 0;
        long begin = 0;
        long length = 0;
        int num = -1;
        while ((num = lr.readLine(line)) > 0) {
          numLines++;
          length += num;
          if (numLines == N) {
            splits.add(createFileSplit(fileName, begin, length));
            begin += length;
            length = 0;
            numLines = 0;
          }
        }
        if (numLines != 0) {
          splits.add(createFileSplit(fileName, begin, length));
        }
   
      } finally {
        if (lr != null) {
          lr.close();
        }
      }
    }
    return splits.toArray(new FileSplit[splits.size()]);
  }

  /**
   * NLineInputFormat uses LineRecordReader, which always reads
   * (and consumes) at least one character out of its upper split
   * boundary. So to make sure that each mapper gets N lines, we
   * move back the upper split limits of each split 
   * by one character here.
   * @param fileName  Path of file
   * @param begin  the position of the first byte in the file to process
   * @param length  number of bytes in InputSplit
   * @return  FileSplit
   */
  protected static FileSplit createFileSplit(Path fileName, long begin, long length) {
    return (begin == 0) 
    ? new FileSplit(fileName, begin, length - 1, new String[] {})
    : new FileSplit(fileName, begin - 1, length, new String[] {});
  }

  public void configure(JobConf conf) {
    N = conf.getInt("mapred.line.input.format.linespermap", 1);
  }
}
