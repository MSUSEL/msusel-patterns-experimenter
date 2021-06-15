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
package org.apache.hadoop.mapreduce.lib.output;

import java.io.IOException;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.util.ReflectionUtils;

/**
 * A Convenience class that creates output lazily.  
 */
@InterfaceAudience.Public
@InterfaceStability.Stable
public class LazyOutputFormat <K,V> extends FilterOutputFormat<K, V> {
  public static String OUTPUT_FORMAT = 
    "mapreduce.output.lazyoutputformat.outputformat";
  /**
   * Set the underlying output format for LazyOutputFormat.
   * @param job the {@link Job} to modify
   * @param theClass the underlying class
   */
  @SuppressWarnings("unchecked")
  public static void  setOutputFormatClass(Job job, 
                                     Class<? extends OutputFormat> theClass) {
      job.setOutputFormatClass(LazyOutputFormat.class);
      job.getConfiguration().setClass(OUTPUT_FORMAT, 
          theClass, OutputFormat.class);
  }

  @SuppressWarnings("unchecked")
  private void getBaseOutputFormat(Configuration conf) 
  throws IOException {
    baseOut =  ((OutputFormat<K, V>) ReflectionUtils.newInstance(
      conf.getClass(OUTPUT_FORMAT, null), conf));
    if (baseOut == null) {
      throw new IOException("Output Format not set for LazyOutputFormat");
    }
  }

  @Override
  public RecordWriter<K, V> getRecordWriter(TaskAttemptContext context)
  throws IOException, InterruptedException {
    if (baseOut == null) {
      getBaseOutputFormat(context.getConfiguration());
    }
    return new LazyRecordWriter<K, V>(baseOut, context);
  }
  
  @Override
  public void checkOutputSpecs(JobContext context) 
  throws IOException, InterruptedException {
    if (baseOut == null) {
      getBaseOutputFormat(context.getConfiguration());
    }
   super.checkOutputSpecs(context);
  }
  
  @Override
  public OutputCommitter getOutputCommitter(TaskAttemptContext context) 
  throws IOException, InterruptedException {
    if (baseOut == null) {
      getBaseOutputFormat(context.getConfiguration());
    }
    return super.getOutputCommitter(context);
  }
  
  /**
   * A convenience class to be used with LazyOutputFormat
   */
  private static class LazyRecordWriter<K,V> extends FilterRecordWriter<K,V> {

    final OutputFormat<K,V> outputFormat;
    final TaskAttemptContext taskContext;

    public LazyRecordWriter(OutputFormat<K,V> out, 
                            TaskAttemptContext taskContext)
    throws IOException, InterruptedException {
      this.outputFormat = out;
      this.taskContext = taskContext;
    }

    @Override
    public void write(K key, V value) throws IOException, InterruptedException {
      if (rawWriter == null) {
        rawWriter = outputFormat.getRecordWriter(taskContext);
      }
      rawWriter.write(key, value);
    }

    @Override
    public void close(TaskAttemptContext context) 
    throws IOException, InterruptedException {
      if (rawWriter != null) {
        rawWriter.close(context);
      }
    }

  }
}
