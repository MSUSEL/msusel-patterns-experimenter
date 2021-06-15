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
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

/**
 * FilterOutputFormat is a convenience class that wraps OutputFormat. 
 */
@InterfaceAudience.Public
@InterfaceStability.Stable
public class FilterOutputFormat <K,V> extends OutputFormat<K, V> {

  protected OutputFormat<K,V> baseOut;

  public FilterOutputFormat() {
    this.baseOut = null;
  }
  
  /**
   * Create a FilterOutputFormat based on the underlying output format.
   * @param baseOut the underlying OutputFormat
   */
  public FilterOutputFormat(OutputFormat<K,V> baseOut) {
    this.baseOut = baseOut;
  }

  @Override
  public RecordWriter<K, V> getRecordWriter(TaskAttemptContext context) 
  throws IOException, InterruptedException {
    return getBaseOut().getRecordWriter(context);
  }

  @Override
  public void checkOutputSpecs(JobContext context) 
  throws IOException, InterruptedException {
    getBaseOut().checkOutputSpecs(context);
  }

  @Override
  public OutputCommitter getOutputCommitter(TaskAttemptContext context) 
  throws IOException, InterruptedException {
    return getBaseOut().getOutputCommitter(context);
  }

  private OutputFormat<K,V> getBaseOut() throws IOException {
    if (baseOut == null) {
      throw new IOException("OutputFormat not set for FilterOutputFormat");
    }
    return baseOut;
  }
  /**
   * <code>FilterRecordWriter</code> is a convenience wrapper
   * class that extends the {@link RecordWriter}.
   */

  public static class FilterRecordWriter<K,V> extends RecordWriter<K,V> {

    protected RecordWriter<K,V> rawWriter = null;

    public FilterRecordWriter() {
      rawWriter = null;
    }
    
    public FilterRecordWriter(RecordWriter<K,V> rwriter) {
      this.rawWriter = rwriter;
    }
    
    @Override
    public void write(K key, V value) throws IOException, InterruptedException {
      getRawWriter().write(key, value);
    }

    @Override
    public void close(TaskAttemptContext context) 
    throws IOException, InterruptedException {
      getRawWriter().close(context);
    }
    
    private RecordWriter<K,V> getRawWriter() throws IOException {
      if (rawWriter == null) {
        throw new IOException("Record Writer not set for FilterRecordWriter");
      }
      return rawWriter;
    }
  }
}
