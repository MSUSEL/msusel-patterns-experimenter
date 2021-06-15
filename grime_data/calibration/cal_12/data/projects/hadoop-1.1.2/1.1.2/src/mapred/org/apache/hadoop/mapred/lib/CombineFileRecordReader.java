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
package org.apache.hadoop.mapred.lib;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import org.apache.hadoop.mapred.*;
import org.apache.hadoop.conf.Configuration;

/**
 * A generic RecordReader that can hand out different recordReaders
 * for each chunk in a {@link CombineFileSplit}.
 * A CombineFileSplit can combine data chunks from multiple files. 
 * This class allows using different RecordReaders for processing
 * these data chunks from different files.
 * @see CombineFileSplit
 */

public class CombineFileRecordReader<K, V> implements RecordReader<K, V> {

  static final Class [] constructorSignature = new Class [] 
                                         {CombineFileSplit.class, 
                                          Configuration.class, 
                                          Reporter.class,
                                          Integer.class};

  protected CombineFileSplit split;
  protected JobConf jc;
  protected Reporter reporter;
  protected Class<RecordReader<K, V>> rrClass;
  protected Constructor<RecordReader<K, V>> rrConstructor;
  protected FileSystem fs;
  
  protected int idx;
  protected long progress;
  protected RecordReader<K, V> curReader;
  
  public boolean next(K key, V value) throws IOException {

    while ((curReader == null) || !curReader.next(key, value)) {
      if (!initNextRecordReader()) {
        return false;
      }
    }
    return true;
  }

  public K createKey() {
    return curReader.createKey();
  }
  
  public V createValue() {
    return curReader.createValue();
  }
  
  /**
   * return the amount of data processed
   */
  public long getPos() throws IOException {
    return progress;
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
  public float getProgress() throws IOException {
    return Math.min(1.0f,  progress/(float)(split.getLength()));
  }
  
  /**
   * A generic RecordReader that can hand out different recordReaders
   * for each chunk in the CombineFileSplit.
   */
  public CombineFileRecordReader(JobConf job, CombineFileSplit split, 
                                 Reporter reporter,
                                 Class<RecordReader<K, V>> rrClass)
    throws IOException {
    this.split = split;
    this.jc = job;
    this.rrClass = rrClass;
    this.reporter = reporter;
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
      curReader =  rrConstructor.newInstance(new Object [] 
                            {split, jc, reporter, Integer.valueOf(idx)});

      // setup some helper config variables.
      jc.set("map.input.file", split.getPath(idx).toString());
      jc.setLong("map.input.start", split.getOffset(idx));
      jc.setLong("map.input.length", split.getLength(idx));
    } catch (Exception e) {
      throw new RuntimeException (e);
    }
    idx++;
    return true;
  }
}
