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
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

/**
 * This class treats a line in the input as a key/value pair separated by a 
 * separator character. The separator can be specified in config file 
 * under the attribute name key.value.separator.in.input.line. The default
 * separator is the tab character ('\t').
 */
public class KeyValueLineRecordReader implements RecordReader<Text, Text> {
  
  private final LineRecordReader lineRecordReader;

  private byte separator = (byte) '\t';

  private LongWritable dummyKey;

  private Text innerValue;

  public Class getKeyClass() { return Text.class; }
  
  public Text createKey() {
    return new Text();
  }
  
  public Text createValue() {
    return new Text();
  }

  public KeyValueLineRecordReader(Configuration job, FileSplit split)
    throws IOException {
    
    lineRecordReader = new LineRecordReader(job, split);
    dummyKey = lineRecordReader.createKey();
    innerValue = lineRecordReader.createValue();
    String sepStr = job.get("key.value.separator.in.input.line", "\t");
    this.separator = (byte) sepStr.charAt(0);
  }

  public static int findSeparator(byte[] utf, int start, int length, byte sep) {
    for (int i = start; i < (start + length); i++) {
      if (utf[i] == sep) {
        return i;
      }
    }
    return -1;
  }

  /** Read key/value pair in a line. */
  public synchronized boolean next(Text key, Text value)
    throws IOException {
    Text tKey = key;
    Text tValue = value;
    byte[] line = null;
    int lineLen = -1;
    if (lineRecordReader.next(dummyKey, innerValue)) {
      line = innerValue.getBytes();
      lineLen = innerValue.getLength();
    } else {
      return false;
    }
    if (line == null)
      return false;
    int pos = findSeparator(line, 0, lineLen, this.separator);
    if (pos == -1) {
      tKey.set(line, 0, lineLen);
      tValue.set("");
    } else {
      int keyLen = pos;
      byte[] keyBytes = new byte[keyLen];
      System.arraycopy(line, 0, keyBytes, 0, keyLen);
      int valLen = lineLen - keyLen - 1;
      byte[] valBytes = new byte[valLen];
      System.arraycopy(line, pos + 1, valBytes, 0, valLen);
      tKey.set(keyBytes);
      tValue.set(valBytes);
    }
    return true;
  }
  
  public float getProgress() throws IOException {
    return lineRecordReader.getProgress();
  }
  
  public synchronized long getPos() throws IOException {
    return lineRecordReader.getPos();
  }

  public synchronized void close() throws IOException { 
    lineRecordReader.close();
  }
}
