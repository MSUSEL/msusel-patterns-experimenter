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
package org.apache.hadoop.mapreduce.lib.input;

import java.io.IOException;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

/**
 * This class converts the input keys and values to their String forms by
 * calling toString() method. This class to SequenceFileAsTextInputFormat
 * class is as LineRecordReader class to TextInputFormat class.
 */
@InterfaceAudience.Public
@InterfaceStability.Stable
public class SequenceFileAsTextRecordReader
  extends RecordReader<Text, Text> {
  
  private final SequenceFileRecordReader<WritableComparable<?>, Writable>
    sequenceFileRecordReader;

  private Text key;
  private Text value;

  public SequenceFileAsTextRecordReader()
    throws IOException {
    sequenceFileRecordReader =
      new SequenceFileRecordReader<WritableComparable<?>, Writable>();
  }

  public void initialize(InputSplit split, TaskAttemptContext context)
      throws IOException, InterruptedException {
    sequenceFileRecordReader.initialize(split, context);
  }

  @Override
  public Text getCurrentKey() 
      throws IOException, InterruptedException {
    return key;
  }
  
  @Override
  public Text getCurrentValue() 
      throws IOException, InterruptedException {
    return value;
  }
  
  /** Read key/value pair in a line. */
  public synchronized boolean nextKeyValue() 
      throws IOException, InterruptedException {
    if (!sequenceFileRecordReader.nextKeyValue()) {
      return false;
    }
    if (key == null) {
      key = new Text(); 
    }
    if (value == null) {
      value = new Text(); 
    }
    key.set(sequenceFileRecordReader.getCurrentKey().toString());
    value.set(sequenceFileRecordReader.getCurrentValue().toString());
    return true;
  }
  
  public float getProgress() throws IOException,  InterruptedException {
    return sequenceFileRecordReader.getProgress();
  }
  
  public synchronized void close() throws IOException {
    sequenceFileRecordReader.close();
  }
}
