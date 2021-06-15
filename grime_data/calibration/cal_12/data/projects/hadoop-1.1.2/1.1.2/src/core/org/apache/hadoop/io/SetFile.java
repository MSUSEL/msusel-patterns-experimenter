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
package org.apache.hadoop.io;

import java.io.*;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;

/** A file-based set of keys. */
public class SetFile extends MapFile {

  protected SetFile() {}                            // no public ctor

  /** 
   * Write a new set file.
   */
  public static class Writer extends MapFile.Writer {

    /** Create the named set for keys of the named class. 
     *  @deprecated pass a Configuration too
     */
    public Writer(FileSystem fs, String dirName,
	Class<? extends WritableComparable> keyClass) throws IOException {
      super(new Configuration(), fs, dirName, keyClass, NullWritable.class);
    }

    /** Create a set naming the element class and compression type. */
    public Writer(Configuration conf, FileSystem fs, String dirName,
                  Class<? extends WritableComparable> keyClass,
                  SequenceFile.CompressionType compress)
      throws IOException {
      this(conf, fs, dirName, WritableComparator.get(keyClass), compress);
    }

    /** Create a set naming the element comparator and compression type. */
    public Writer(Configuration conf, FileSystem fs, String dirName,
                  WritableComparator comparator,
                  SequenceFile.CompressionType compress) throws IOException {
      super(conf, fs, dirName, comparator, NullWritable.class, compress);
    }

    /** Append a key to a set.  The key must be strictly greater than the
     * previous key added to the set. */
    public void append(WritableComparable key) throws IOException{
      append(key, NullWritable.get());
    }
  }

  /** Provide access to an existing set file. */
  public static class Reader extends MapFile.Reader {

    /** Construct a set reader for the named set.*/
    public Reader(FileSystem fs, String dirName, Configuration conf) throws IOException {
      super(fs, dirName, conf);
    }

    /** Construct a set reader for the named set using the named comparator.*/
    public Reader(FileSystem fs, String dirName, WritableComparator comparator, Configuration conf)
      throws IOException {
      super(fs, dirName, comparator, conf);
    }

    // javadoc inherited
    public boolean seek(WritableComparable key)
      throws IOException {
      return super.seek(key);
    }

    /** Read the next key in a set into <code>key</code>.  Returns
     * true if such a key exists and false when at the end of the set. */
    public boolean next(WritableComparable key)
      throws IOException {
      return next(key, NullWritable.get());
    }

    /** Read the matching key from a set into <code>key</code>.
     * Returns <code>key</code>, or null if no match exists. */
    public WritableComparable get(WritableComparable key)
      throws IOException {
      if (seek(key)) {
        next(key);
        return key;
      } else
        return null;
    }
  }

}
