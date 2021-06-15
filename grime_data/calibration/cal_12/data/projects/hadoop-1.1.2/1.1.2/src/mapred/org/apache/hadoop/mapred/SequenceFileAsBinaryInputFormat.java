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
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.DataOutputBuffer;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.SequenceFileInputFormat;

/**
 * InputFormat reading keys, values from SequenceFiles in binary (raw)
 * format.
 */
public class SequenceFileAsBinaryInputFormat
    extends SequenceFileInputFormat<BytesWritable,BytesWritable> {

  public SequenceFileAsBinaryInputFormat() {
    super();
  }

  public RecordReader<BytesWritable,BytesWritable> getRecordReader(
      InputSplit split, JobConf job, Reporter reporter)
      throws IOException {
    return new SequenceFileAsBinaryRecordReader(job, (FileSplit)split);
  }

  /**
   * Read records from a SequenceFile as binary (raw) bytes.
   */
  public static class SequenceFileAsBinaryRecordReader
      implements RecordReader<BytesWritable,BytesWritable> {
    private SequenceFile.Reader in;
    private long start;
    private long end;
    private boolean done = false;
    private DataOutputBuffer buffer = new DataOutputBuffer();
    private SequenceFile.ValueBytes vbytes;

    public SequenceFileAsBinaryRecordReader(Configuration conf, FileSplit split)
        throws IOException {
      Path path = split.getPath();
      FileSystem fs = path.getFileSystem(conf);
      this.in = new SequenceFile.Reader(fs, path, conf);
      this.end = split.getStart() + split.getLength();
      if (split.getStart() > in.getPosition())
        in.sync(split.getStart());                  // sync to start
      this.start = in.getPosition();
      vbytes = in.createValueBytes();
      done = start >= end;
    }

    public BytesWritable createKey() {
      return new BytesWritable();
    }

    public BytesWritable createValue() {
      return new BytesWritable();
    }

    /**
     * Retrieve the name of the key class for this SequenceFile.
     * @see org.apache.hadoop.io.SequenceFile.Reader#getKeyClassName
     */
    public String getKeyClassName() {
      return in.getKeyClassName();
    }

    /**
     * Retrieve the name of the value class for this SequenceFile.
     * @see org.apache.hadoop.io.SequenceFile.Reader#getValueClassName
     */
    public String getValueClassName() {
      return in.getValueClassName();
    }

    /**
     * Read raw bytes from a SequenceFile.
     */
    public synchronized boolean next(BytesWritable key, BytesWritable val)
        throws IOException {
      if (done) return false;
      long pos = in.getPosition();
      boolean eof = -1 == in.nextRawKey(buffer);
      if (!eof) {
        key.set(buffer.getData(), 0, buffer.getLength());
        buffer.reset();
        in.nextRawValue(vbytes);
        vbytes.writeUncompressedBytes(buffer);
        val.set(buffer.getData(), 0, buffer.getLength());
        buffer.reset();
      }
      return !(done = (eof || (pos >= end && in.syncSeen())));
    }

    public long getPos() throws IOException {
      return in.getPosition();
    }

    public void close() throws IOException {
      in.close();
    }

    /**
     * Return the progress within the input split
     * @return 0.0 to 1.0 of the input byte range
     */
    public float getProgress() throws IOException {
      if (end == start) {
        return 0.0f;
      } else {
        return Math.min(1.0f, (float)((in.getPosition() - start) /
                                      (double)(end - start)));
      }
    }
  }
}
