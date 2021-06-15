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
package org.apache.hadoop.mapred.pipes;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapRunner;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.SkipBadRecords;

/**
 * An adaptor to run a C++ mapper.
 */
class PipesMapRunner<K1 extends WritableComparable, V1 extends Writable,
    K2 extends WritableComparable, V2 extends Writable>
    extends MapRunner<K1, V1, K2, V2> {
  private JobConf job;

  /**
   * Get the new configuration.
   * @param job the job's configuration
   */
  public void configure(JobConf job) {
    this.job = job;
    //disable the auto increment of the counter. For pipes, no of processed 
    //records could be different(equal or less) than the no of records input.
    SkipBadRecords.setAutoIncrMapperProcCount(job, false);
  }

  /**
   * Run the map task.
   * @param input the set of inputs
   * @param output the object to collect the outputs of the map
   * @param reporter the object to update with status
   */
  @SuppressWarnings("unchecked")
  public void run(RecordReader<K1, V1> input, OutputCollector<K2, V2> output,
                  Reporter reporter) throws IOException {
    Application<K1, V1, K2, V2> application = null;
    try {
      RecordReader<FloatWritable, NullWritable> fakeInput = 
        (!Submitter.getIsJavaRecordReader(job) && 
         !Submitter.getIsJavaMapper(job)) ? 
	  (RecordReader<FloatWritable, NullWritable>) input : null;
      application = new Application<K1, V1, K2, V2>(job, fakeInput, output, 
                                                    reporter,
          (Class<? extends K2>) job.getOutputKeyClass(), 
          (Class<? extends V2>) job.getOutputValueClass());
    } catch (InterruptedException ie) {
      throw new RuntimeException("interrupted", ie);
    }
    DownwardProtocol<K1, V1> downlink = application.getDownlink();
    boolean isJavaInput = Submitter.getIsJavaRecordReader(job);
    downlink.runMap(reporter.getInputSplit(), 
                    job.getNumReduceTasks(), isJavaInput);
    boolean skipping = job.getBoolean("mapred.skip.on", false);
    try {
      if (isJavaInput) {
        // allocate key & value instances that are re-used for all entries
        K1 key = input.createKey();
        V1 value = input.createValue();
        downlink.setInputTypes(key.getClass().getName(),
                               value.getClass().getName());
        
        while (input.next(key, value)) {
          // map pair to output
          downlink.mapItem(key, value);
          if(skipping) {
            //flush the streams on every record input if running in skip mode
            //so that we don't buffer other records surrounding a bad record.
            downlink.flush();
          }
        }
        downlink.endOfInput();
      }
      application.waitForFinish();
    } catch (Throwable t) {
      application.abort(t);
    } finally {
      application.cleanup();
    }
  }
  
}
