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

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

/**
 * This class converts the input keys and values to their String forms by calling toString()
 * method. This class to SequenceFileAsTextInputFormat class is as LineRecordReader
 * class to TextInputFormat class.
 */
public class SequenceFileAsTextRecordReader
  implements RecordReader<Text, Text> {
  
  private final SequenceFileRecordReader<WritableComparable, Writable>
  sequenceFileRecordReader;

  private WritableComparable innerKey;
  private Writable innerValue;

  public SequenceFileAsTextRecordReader(Configuration conf, FileSplit split)
    throws IOException {
    sequenceFileRecordReader =
      new SequenceFileRecordReader<WritableComparable, Writable>(conf, split);
    innerKey = sequenceFileRecordReader.createKey();
    innerValue = sequenceFileRecordReader.createValue();
  }

  public Text createKey() {
    return new Text();
  }
  
  public Text createValue() {
    return new Text();
  }

  /** Read key/value pair in a line. */
  public synchronized boolean next(Text key, Text value) throws IOException {
    Text tKey = key;
    Text tValue = value;
    if (!sequenceFileRecordReader.next(innerKey, innerValue)) {
      return false;
    }
    tKey.set(innerKey.toString());
    tValue.set(innerValue.toString());
    return true;
  }
  
  public float getProgress() throws IOException {
    return sequenceFileRecordReader.getProgress();
  }
  
  public synchronized long getPos() throws IOException {
    return sequenceFileRecordReader.getPos();
  }
  
  public synchronized void close() throws IOException {
    sequenceFileRecordReader.close();
  }
  
}
