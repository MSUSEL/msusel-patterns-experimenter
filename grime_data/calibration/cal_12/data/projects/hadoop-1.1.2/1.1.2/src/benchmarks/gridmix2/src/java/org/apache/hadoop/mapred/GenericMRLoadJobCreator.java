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

import java.util.Random;
import java.util.Stack;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.GenericMRLoadGenerator;
import org.apache.hadoop.mapred.lib.NullOutputFormat;
import org.apache.hadoop.mapred.JobConf;

public class GenericMRLoadJobCreator extends GenericMRLoadGenerator {

  public static JobConf createJob(String[] argv, boolean mapoutputCompressed,
      boolean outputCompressed) throws Exception {

    JobConf job = new JobConf();
    job.setJarByClass(GenericMRLoadGenerator.class);
    job.setMapperClass(SampleMapper.class);
    job.setReducerClass(SampleReducer.class);
    if (!parseArgs(argv, job)) {
      return null;
    }

    if (null == FileOutputFormat.getOutputPath(job)) {
      // No output dir? No writes
      job.setOutputFormat(NullOutputFormat.class);
    }

    if (0 == FileInputFormat.getInputPaths(job).length) {
      // No input dir? Generate random data
      System.err.println("No input path; ignoring InputFormat");
      confRandom(job);
    } else if (null != job.getClass("mapred.indirect.input.format", null)) {
      // specified IndirectInputFormat? Build src list
      JobClient jClient = new JobClient(job);
      Path sysdir = jClient.getSystemDir();
      Random r = new Random();
      Path indirInputFile = new Path(sysdir, Integer.toString(r
          .nextInt(Integer.MAX_VALUE), 36)
          + "_files");
      job.set("mapred.indirect.input.file", indirInputFile.toString());
      SequenceFile.Writer writer = SequenceFile.createWriter(sysdir
          .getFileSystem(job), job, indirInputFile, LongWritable.class,
          Text.class, SequenceFile.CompressionType.NONE);
      try {
        for (Path p : FileInputFormat.getInputPaths(job)) {
          FileSystem fs = p.getFileSystem(job);
          Stack<Path> pathstack = new Stack<Path>();
          pathstack.push(p);
          while (!pathstack.empty()) {
            for (FileStatus stat : fs.listStatus(pathstack.pop())) {
              if (stat.isDir()) {
                if (!stat.getPath().getName().startsWith("_")) {
                  pathstack.push(stat.getPath());
                }
              } else {
                writer.sync();
                writer.append(new LongWritable(stat.getLen()), new Text(stat
                    .getPath().toUri().toString()));
              }
            }
          }
        }
      } finally {
        writer.close();
      }
    }

    job.setCompressMapOutput(mapoutputCompressed);
    job.setBoolean("mapred.output.compress", outputCompressed);
    return job;

  }

}
