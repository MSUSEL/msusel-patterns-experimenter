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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
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
@InterfaceAudience.Public
@InterfaceStability.Stable
public class NLineInputFormat extends FileInputFormat<LongWritable, Text> { 
  public static final String LINES_PER_MAP = 
    "mapreduce.input.lineinputformat.linespermap";

  public RecordReader<LongWritable, Text> createRecordReader(
      InputSplit genericSplit, TaskAttemptContext context) 
      throws IOException {
    context.setStatus(genericSplit.toString());
    return new LineRecordReader();
  }

  /** 
   * Logically splits the set of input files for the job, splits N lines
   * of the input as one split.
   * 
   * @see FileInputFormat#getSplits(JobContext)
   */
  public List<InputSplit> getSplits(JobContext job)
  throws IOException {
    List<InputSplit> splits = new ArrayList<InputSplit>();
    int numLinesPerSplit = getNumLinesPerSplit(job);
    for (FileStatus status : listStatus(job)) {
      splits.addAll(getSplitsForFile(status,
        job.getConfiguration(), numLinesPerSplit));
    }
    return splits;
  }
  
  public static List<FileSplit> getSplitsForFile(FileStatus status,
      Configuration conf, int numLinesPerSplit) throws IOException {
    List<FileSplit> splits = new ArrayList<FileSplit> ();
    Path fileName = status.getPath();
    if (status.isDir()) {
      throw new IOException("Not a file: " + fileName);
    }
    FileSystem  fs = fileName.getFileSystem(conf);
    LineReader lr = null;
    try {
      FSDataInputStream in  = fs.open(fileName);
      lr = new LineReader(in, conf);
      Text line = new Text();
      int numLines = 0;
      long begin = 0;
      long length = 0;
      int num = -1;
      while ((num = lr.readLine(line)) > 0) {
        numLines++;
        length += num;
        if (numLines == numLinesPerSplit) {
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
    return splits; 
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
  
  /**
   * Set the number of lines per split
   * @param job the job to modify
   * @param numLines the number of lines per split
   */
  public static void setNumLinesPerSplit(Job job, int numLines) {
    job.getConfiguration().setInt(LINES_PER_MAP, numLines);
  }

  /**
   * Get the number of lines per split
   * @param job the job
   * @return the number of lines per split
   */
  public static int getNumLinesPerSplit(JobContext job) {
    return job.getConfiguration().getInt(LINES_PER_MAP, 1);
  }
}
