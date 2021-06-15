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
package org.apache.hadoop.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.Progressable;

/**
 * A context object that allows input and output from the task. It is only
 * supplied to the {@link Mapper} or {@link Reducer}.
 * @param <KEYIN> the input key type for the task
 * @param <VALUEIN> the input value type for the task
 * @param <KEYOUT> the output key type for the task
 * @param <VALUEOUT> the output value type for the task
 */
public abstract class TaskInputOutputContext<KEYIN,VALUEIN,KEYOUT,VALUEOUT> 
       extends TaskAttemptContext implements Progressable {
  private RecordWriter<KEYOUT,VALUEOUT> output;
  private StatusReporter reporter;
  private OutputCommitter committer;

  public TaskInputOutputContext(Configuration conf, TaskAttemptID taskid,
                                RecordWriter<KEYOUT,VALUEOUT> output,
                                OutputCommitter committer,
                                StatusReporter reporter) {
    super(conf, taskid);
    this.output = output;
    this.reporter = reporter;
    this.committer = committer;
  }

  /**
   * Advance to the next key, value pair, returning null if at end.
   * @return the key object that was read into, or null if no more
   */
  public abstract 
  boolean nextKeyValue() throws IOException, InterruptedException;
 
  /**
   * Get the current key.
   * @return the current key object or null if there isn't one
   * @throws IOException
   * @throws InterruptedException
   */
  public abstract 
  KEYIN getCurrentKey() throws IOException, InterruptedException;

  /**
   * Get the current value.
   * @return the value object that was read into
   * @throws IOException
   * @throws InterruptedException
   */
  public abstract VALUEIN getCurrentValue() throws IOException, 
                                                   InterruptedException;

  /**
   * Generate an output key/value pair.
   */
  public void write(KEYOUT key, VALUEOUT value
                    ) throws IOException, InterruptedException {
    output.write(key, value);
  }

  public Counter getCounter(Enum<?> counterName) {
    return reporter.getCounter(counterName);
  }

  public Counter getCounter(String groupName, String counterName) {
    return reporter.getCounter(groupName, counterName);
  }

  @Override
  public void progress() {
    reporter.progress();
  }

  public float getProgress() {
    return reporter.getProgress();
  }

  @Override
  public void setStatus(String status) {
    reporter.setStatus(status);
  }
  
  public OutputCommitter getOutputCommitter() {
    return committer;
  }
}
