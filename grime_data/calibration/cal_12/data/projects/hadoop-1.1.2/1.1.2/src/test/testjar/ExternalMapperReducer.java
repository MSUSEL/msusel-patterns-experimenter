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
package testjar;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class ExternalMapperReducer
  implements Mapper<WritableComparable, Writable,
                    ExternalWritable, IntWritable>,
             Reducer<WritableComparable, Writable,
                     WritableComparable, IntWritable> {

  public void configure(JobConf job) {

  }

  public void close()
    throws IOException {

  }

  public void map(WritableComparable key, Writable value,
                  OutputCollector<ExternalWritable, IntWritable> output,
                  Reporter reporter)
    throws IOException {
    
    if (value instanceof Text) {
      Text text = (Text)value;
      ExternalWritable ext = new ExternalWritable(text.toString());
      output.collect(ext, new IntWritable(1));
    }
  }

  public void reduce(WritableComparable key, Iterator<Writable> values,
                     OutputCollector<WritableComparable, IntWritable> output,
                     Reporter reporter)
    throws IOException {
    
    int count = 0;
    while (values.hasNext()) {
      count++;
      values.next();
    }
    output.collect(key, new IntWritable(count));
  }
}
