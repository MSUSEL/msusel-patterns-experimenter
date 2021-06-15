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

import java.io.*;
import java.util.*;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

/**
 * This is a support class to test Hadoop Pipes when using C++ RecordReaders.
 * It defines an InputFormat with InputSplits that are just strings. The
 * RecordReaders are not implemented in Java, naturally...
 */
public class WordCountInputFormat
  extends FileInputFormat<IntWritable, Text> {
  
  static class WordCountInputSplit implements InputSplit  {
    private String filename;
    WordCountInputSplit() { }
    WordCountInputSplit(Path filename) {
      this.filename = filename.toUri().getPath();
    }
    public void write(DataOutput out) throws IOException { 
      Text.writeString(out, filename); 
    }
    public void readFields(DataInput in) throws IOException { 
      filename = Text.readString(in); 
    }
    public long getLength() { return 0L; }
    public String[] getLocations() { return new String[0]; }
  }

  public InputSplit[] getSplits(JobConf conf, 
                                int numSplits) throws IOException {
    ArrayList<InputSplit> result = new ArrayList<InputSplit>();
    FileSystem local = FileSystem.getLocal(conf);
    for(Path dir: getInputPaths(conf)) {
      for(FileStatus file: local.listStatus(dir)) {
        result.add(new WordCountInputSplit(file.getPath()));
      }
    }
    return result.toArray(new InputSplit[result.size()]);
  }
  public RecordReader<IntWritable, Text> getRecordReader(InputSplit split,
                                                         JobConf conf, 
                                                         Reporter reporter) {
    return new RecordReader<IntWritable, Text>(){
      public boolean next(IntWritable key, Text value) throws IOException {
        return false;
      }
      public IntWritable createKey() {
        return new IntWritable();
      }
      public Text createValue() {
        return new Text();
      }
      public long getPos() {
        return 0;
      }
      public void close() { }
      public float getProgress() { 
        return 0.0f;
      }
    };
  }
}
