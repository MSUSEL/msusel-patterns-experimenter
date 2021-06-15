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
package org.apache.hadoop.mapreduce.split;

import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.WritableUtils;
import org.apache.hadoop.mapreduce.JobID;
import org.apache.hadoop.mapreduce.JobSubmissionFiles;

/**
 * A (internal) utility that reads the split meta info and creates
 * split meta info objects
 */

public class SplitMetaInfoReader {
  
  public static JobSplit.TaskSplitMetaInfo[] readSplitMetaInfo(
      JobID jobId, FileSystem fs, Configuration conf, Path jobSubmitDir) 
  throws IOException {
    long maxMetaInfoSize = conf.getLong("mapreduce.jobtracker.split.metainfo.maxsize", 
        10000000L);
    Path metaSplitFile = JobSubmissionFiles.getJobSplitMetaFile(jobSubmitDir);
    FileStatus fStatus = fs.getFileStatus(metaSplitFile);
    if (maxMetaInfoSize > 0 && fStatus.getLen() > maxMetaInfoSize) {
      throw new IOException("Split metadata size exceeded " +
          maxMetaInfoSize +". Aborting job " + jobId);
    }
    FSDataInputStream in = fs.open(metaSplitFile);
    byte[] header = new byte[JobSplit.META_SPLIT_FILE_HEADER.length];
    in.readFully(header);
    if (!Arrays.equals(JobSplit.META_SPLIT_FILE_HEADER, header)) {
      throw new IOException("Invalid header on split file");
    }
    int vers = WritableUtils.readVInt(in);
    if (vers != JobSplit.META_SPLIT_VERSION) {
      in.close();
      throw new IOException("Unsupported split version " + vers);
    }
    int numSplits = WritableUtils.readVInt(in); //TODO: check for insane values
    JobSplit.TaskSplitMetaInfo[] allSplitMetaInfo = 
      new JobSplit.TaskSplitMetaInfo[numSplits];
    final int maxLocations =
      conf.getInt(JobSplitWriter.MAX_SPLIT_LOCATIONS, Integer.MAX_VALUE);
    for (int i = 0; i < numSplits; i++) {
      JobSplit.SplitMetaInfo splitMetaInfo = new JobSplit.SplitMetaInfo();
      splitMetaInfo.readFields(in);
      final int numLocations = splitMetaInfo.getLocations().length;
      if (numLocations > maxLocations) {
        throw new IOException("Max block location exceeded for split: #"  + i +
              " splitsize: " + numLocations + " maxsize: " + maxLocations);
      }
      JobSplit.TaskSplitIndex splitIndex = new JobSplit.TaskSplitIndex(
          JobSubmissionFiles.getJobSplitFile(jobSubmitDir).toString(), 
          splitMetaInfo.getStartOffset());
      allSplitMetaInfo[i] = new JobSplit.TaskSplitMetaInfo(splitIndex, 
          splitMetaInfo.getLocations(), 
          splitMetaInfo.getInputDataLength());
    }
    in.close();
    return allSplitMetaInfo;
  }

}
