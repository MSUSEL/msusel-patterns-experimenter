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

import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

/**
 * Consume all outputs and put them in /dev/null. 
 */
public class NullOutputFormat<K, V> extends OutputFormat<K, V> {
  
  @Override
  public RecordWriter<K, V> 
         getRecordWriter(TaskAttemptContext context) {
    return new RecordWriter<K, V>(){
        public void write(K key, V value) { }
        public void close(TaskAttemptContext context) { }
      };
  }
  
  @Override
  public void checkOutputSpecs(JobContext context) { }
  
  @Override
  public OutputCommitter getOutputCommitter(TaskAttemptContext context) {
    return new OutputCommitter() {
      public void abortTask(TaskAttemptContext taskContext) { }
      public void cleanupJob(JobContext jobContext) { }
      public void commitJob(JobContext jobContext) { }
      public void commitTask(TaskAttemptContext taskContext) { }
      public boolean needsTaskCommit(TaskAttemptContext taskContext) {
        return false;
      }
      public void setupJob(JobContext jobContext) { }
      public void setupTask(TaskAttemptContext taskContext) { }
    };
  }
}
