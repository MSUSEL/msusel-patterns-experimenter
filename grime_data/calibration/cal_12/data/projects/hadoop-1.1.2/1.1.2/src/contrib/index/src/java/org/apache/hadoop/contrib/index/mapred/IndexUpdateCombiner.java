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
package org.apache.hadoop.contrib.index.mapred;

import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

/**
 * This combiner combines multiple intermediate forms into one intermediate
 * form. More specifically, the input intermediate forms are a single-document
 * ram index and/or a single delete term. An output intermediate form contains
 * a multi-document ram index and/or multiple delete terms.   
 */
public class IndexUpdateCombiner extends MapReduceBase implements
    Reducer<Shard, IntermediateForm, Shard, IntermediateForm> {
  static final Log LOG = LogFactory.getLog(IndexUpdateCombiner.class);

  IndexUpdateConfiguration iconf;

  /* (non-Javadoc)
   * @see org.apache.hadoop.mapred.Reducer#reduce(java.lang.Object, java.util.Iterator, org.apache.hadoop.mapred.OutputCollector, org.apache.hadoop.mapred.Reporter)
   */
  public void reduce(Shard key, Iterator<IntermediateForm> values,
      OutputCollector<Shard, IntermediateForm> output, Reporter reporter)
      throws IOException {

    LOG.info("Construct a form writer for " + key);
    IntermediateForm form = new IntermediateForm();
    form.configure(iconf);
    while (values.hasNext()) {
      IntermediateForm singleDocForm = values.next();
      form.process(singleDocForm);
    }
    form.closeWriter();
    LOG.info("Closed the form writer for " + key + ", form = " + form);

    output.collect(key, form);
  }

  /* (non-Javadoc)
   * @see org.apache.hadoop.mapred.MapReduceBase#configure(org.apache.hadoop.mapred.JobConf)
   */
  public void configure(JobConf job) {
    iconf = new IndexUpdateConfiguration(job);
  }

  /* (non-Javadoc)
   * @see org.apache.hadoop.mapred.MapReduceBase#close()
   */
  public void close() throws IOException {
  }

}
