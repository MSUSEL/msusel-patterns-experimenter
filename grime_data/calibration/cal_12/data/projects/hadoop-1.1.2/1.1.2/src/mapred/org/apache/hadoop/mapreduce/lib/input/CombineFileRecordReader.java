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

import java.io.*;
import java.lang.reflect.*;

import org.apache.hadoop.fs.FileSystem;

import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.conf.Configuration;

/**
 * A generic RecordReader that can hand out different recordReaders
 * for each chunk in a {@link CombineFileSplit}.
 * A CombineFileSplit can combine data chunks from multiple files. 
 * This class allows using different RecordReaders for processing
 * these data chunks from different files.
 * @see CombineFileSplit
 */
@InterfaceAudience.Public
@InterfaceStability.Stable
public class CombineFileRecordReader<K, V> extends RecordReader<K, V> {

  static final Class [] constructorSignature = new Class [] 
                                         {CombineFileSplit.class,
                                          TaskAttemptContext.class,
                                          Integer.class};

  protected CombineFileSplit split;
  protected Class<? extends RecordReader<K,V>> rrClass;
  protected Constructor<? extends RecordReader<K,V>> rrConstructor;
  protected FileSystem fs;
  protected TaskAttemptContext context;
  
  protected int idx;
  protected long progress;
  protected RecordReader<K, V> curReader;
  
  public void initialize(InputSplit split,
      TaskAttemptContext context) throws IOException, InterruptedException {
    this.split = (CombineFileSplit)split;
    this.context = context;
    if (null != this.curReader) {
      this.curReader.initialize(split, context);
    }
  }
  
  public boolean nextKeyValue() throws IOException, InterruptedException {

    while ((curReader == null) || !curReader.nextKeyValue()) {
      if (!initNextRecordReader()) {
        return false;
      }
    }
    return true;
  }

  public K getCurrentKey() throws IOException, InterruptedException {
    return curReader.getCurrentKey();
  }
  
  public V getCurrentValue() throws IOException, InterruptedException {
    return curReader.getCurrentValue();
  }
  
  public void close() throws IOException {
    if (curReader != null) {
      curReader.close();
      curReader = null;
    }
  }
  
  /**
   * return progress based on the amount of data processed so far.
   */
  public float getProgress() throws IOException, InterruptedException {
    long subprogress = 0;    // bytes processed in current split
    if (null != curReader) {
      // idx is always one past the current subsplit's true index.
      subprogress = (long)(curReader.getProgress() * split.getLength(idx - 1));
    }
    return Math.min(1.0f,  (progress + subprogress)/(float)(split.getLength()));
  }
  
  /**
   * A generic RecordReader that can hand out different recordReaders
   * for each chunk in the CombineFileSplit.
   */
  public CombineFileRecordReader(CombineFileSplit split,
                                 TaskAttemptContext context,
                                 Class<? extends RecordReader<K,V>> rrClass)
    throws IOException {
    this.split = split;
    this.context = context;
    this.rrClass = rrClass;
    this.idx = 0;
    this.curReader = null;
    this.progress = 0;

    try {
      rrConstructor = rrClass.getDeclaredConstructor(constructorSignature);
      rrConstructor.setAccessible(true);
    } catch (Exception e) {
      throw new RuntimeException(rrClass.getName() + 
                                 " does not have valid constructor", e);
    }
    initNextRecordReader();
  }
  
  /**
   * Get the record reader for the next chunk in this CombineFileSplit.
   */
  protected boolean initNextRecordReader() throws IOException {

    if (curReader != null) {
      curReader.close();
      curReader = null;
      if (idx > 0) {
        progress += split.getLength(idx-1);    // done processing so far
      }
    }

    // if all chunks have been processed, nothing more to do.
    if (idx == split.getNumPaths()) {
      return false;
    }

    // get a record reader for the idx-th chunk
    try {
      Configuration conf = context.getConfiguration();
      // setup some helper config variables.
      conf.set("map.input.file", split.getPath(idx).toString());
      conf.setLong("map.input.start", split.getOffset(idx));
      conf.setLong("map.input.length", split.getLength(idx));

      curReader =  rrConstructor.newInstance(new Object [] 
                            {split, context, Integer.valueOf(idx)});

      if (idx > 0) {
        // initialize() for the first RecordReader will be called by MapTask;
        // we're responsible for initializing subsequent RecordReaders.
        curReader.initialize(split, context);
      }
    } catch (Exception e) {
      throw new RuntimeException (e);
    }
    idx++;
    return true;
  }
}
