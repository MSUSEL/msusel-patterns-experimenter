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
package org.apache.hadoop.contrib.utils.join;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;
import org.apache.hadoop.mapred.JobConf;

/**
 * This abstract class serves as the base class for the values that 
 * flow from the mappers to the reducers in a data join job. 
 * Typically, in such a job, the mappers will compute the source
 * tag of an input record based on its attributes or based on the 
 * file name of the input file. This tag will be used by the reducers
 * to re-group the values of a given key according to their source tags.
 * 
 */
public abstract class TaggedMapOutput implements Writable {
  protected Text tag;

  public TaggedMapOutput() {
    this.tag = new Text("");
  }

  public Text getTag() {
    return tag;
  }

  public void setTag(Text tag) {
    this.tag = tag;
  }

  public abstract Writable getData();
  
  public TaggedMapOutput clone(JobConf job) {
    return (TaggedMapOutput) WritableUtils.clone(this, job);
  }

}
