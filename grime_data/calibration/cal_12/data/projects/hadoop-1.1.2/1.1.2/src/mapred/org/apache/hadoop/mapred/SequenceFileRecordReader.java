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
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.ReflectionUtils;

/** An {@link RecordReader} for {@link SequenceFile}s. */
public class SequenceFileRecordReader<K, V> implements RecordReader<K, V> {
  
  private SequenceFile.Reader in;
  private long start;
  private long end;
  private boolean more = true;
  protected Configuration conf;

  public SequenceFileRecordReader(Configuration conf, FileSplit split)
    throws IOException {
    Path path = split.getPath();
    FileSystem fs = path.getFileSystem(conf);
    this.in = new SequenceFile.Reader(fs, path, conf);
    this.end = split.getStart() + split.getLength();
    this.conf = conf;

    if (split.getStart() > in.getPosition())
      in.sync(split.getStart());                  // sync to start

    this.start = in.getPosition();
    more = start < end;
  }


  /** The class of key that must be passed to {@link
   * #next(Object, Object)}.. */
  public Class getKeyClass() { return in.getKeyClass(); }

  /** The class of value that must be passed to {@link
   * #next(Object, Object)}.. */
  public Class getValueClass() { return in.getValueClass(); }
  
  @SuppressWarnings("unchecked")
  public K createKey() {
    return (K) ReflectionUtils.newInstance(getKeyClass(), conf);
  }
  
  @SuppressWarnings("unchecked")
  public V createValue() {
    return (V) ReflectionUtils.newInstance(getValueClass(), conf);
  }
    
  public synchronized boolean next(K key, V value) throws IOException {
    if (!more) return false;
    long pos = in.getPosition();
    boolean remaining = (in.next(key) != null);
    if (remaining) {
      getCurrentValue(value);
    }
    if (pos >= end && in.syncSeen()) {
      more = false;
    } else {
      more = remaining;
    }
    return more;
  }
  
  protected synchronized boolean next(K key)
    throws IOException {
    if (!more) return false;
    long pos = in.getPosition();
    boolean remaining = (in.next(key) != null);
    if (pos >= end && in.syncSeen()) {
      more = false;
    } else {
      more = remaining;
    }
    return more;
  }
  
  protected synchronized void getCurrentValue(V value)
    throws IOException {
    in.getCurrentValue(value);
  }
  
  /**
   * Return the progress within the input split
   * @return 0.0 to 1.0 of the input byte range
   */
  public float getProgress() throws IOException {
    if (end == start) {
      return 0.0f;
    } else {
      return Math.min(1.0f, (in.getPosition() - start) / (float)(end - start));
    }
  }
  
  public synchronized long getPos() throws IOException {
    return in.getPosition();
  }
  
  protected synchronized void seek(long pos) throws IOException {
    in.seek(pos);
  }
  public synchronized void close() throws IOException { in.close(); }
  
}

