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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.SkipBadRecords;

import java.io.IOException;
import java.util.Iterator;

/**
 * This class is used to talk to a C++ reduce task.
 */
class PipesReducer<K2 extends WritableComparable, V2 extends Writable,
    K3 extends WritableComparable, V3 extends Writable>
    implements Reducer<K2, V2, K3, V3> {
  private static final Log LOG= LogFactory.getLog(PipesReducer.class.getName());
  private JobConf job;
  private Application<K2, V2, K3, V3> application = null;
  private DownwardProtocol<K2, V2> downlink = null;
  private boolean isOk = true;
  private boolean skipping = false;

  public void configure(JobConf job) {
    this.job = job;
    //disable the auto increment of the counter. For pipes, no of processed 
    //records could be different(equal or less) than the no of records input.
    SkipBadRecords.setAutoIncrReducerProcCount(job, false);
    skipping = job.getBoolean("mapred.skip.on", false);
  }

  /**
   * Process all of the keys and values. Start up the application if we haven't
   * started it yet.
   */
  public void reduce(K2 key, Iterator<V2> values, 
                     OutputCollector<K3, V3> output, Reporter reporter
                     ) throws IOException {
    isOk = false;
    startApplication(output, reporter);
    downlink.reduceKey(key);
    while (values.hasNext()) {
      downlink.reduceValue(values.next());
    }
    if(skipping) {
      //flush the streams on every record input if running in skip mode
      //so that we don't buffer other records surrounding a bad record.
      downlink.flush();
    }
    isOk = true;
  }

  @SuppressWarnings("unchecked")
  private void startApplication(OutputCollector<K3, V3> output, Reporter reporter) throws IOException {
    if (application == null) {
      try {
        LOG.info("starting application");
        application = 
          new Application<K2, V2, K3, V3>(
              job, null, output, reporter, 
              (Class<? extends K3>) job.getOutputKeyClass(), 
              (Class<? extends V3>) job.getOutputValueClass());
        downlink = application.getDownlink();
      } catch (InterruptedException ie) {
        throw new RuntimeException("interrupted", ie);
      }
      int reduce=0;
      downlink.runReduce(reduce, Submitter.getIsJavaRecordWriter(job));
    }
  }

  /**
   * Handle the end of the input by closing down the application.
   */
  public void close() throws IOException {
    // if we haven't started the application, we have nothing to do
    if (isOk) {
      OutputCollector<K3, V3> nullCollector = new OutputCollector<K3, V3>() {
        public void collect(K3 key, 
                            V3 value) throws IOException {
          // NULL
        }
      };
      startApplication(nullCollector, Reporter.NULL);
    }
    try {
      if (isOk) {
        application.getDownlink().endOfInput();
      } else {
        // send the abort to the application and let it clean up
        application.getDownlink().abort();
      }
      LOG.info("waiting for finish");
      application.waitForFinish();
      LOG.info("got done");
    } catch (Throwable t) {
      application.abort(t);
    } finally {
      application.cleanup();
    }
  }
}
