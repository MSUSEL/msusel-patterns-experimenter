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

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Checksum;

import org.apache.hadoop.fs.ChecksumException;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SecureIOUtils;
import org.apache.hadoop.util.PureJavaCrc32;

import static org.apache.hadoop.mapred.MapTask.MAP_OUTPUT_INDEX_RECORD_LENGTH;

class SpillRecord {

  /** Backing store */
  private final ByteBuffer buf;
  /** View of backing storage as longs */
  private final LongBuffer entries;

  public SpillRecord(int numPartitions) {
    buf = ByteBuffer.allocate(
        numPartitions * MapTask.MAP_OUTPUT_INDEX_RECORD_LENGTH);
    entries = buf.asLongBuffer();
  }

  public SpillRecord(Path indexFileName, JobConf job, String expectedIndexOwner)
  throws IOException {
    this(indexFileName, job, new PureJavaCrc32(), expectedIndexOwner);
  }

  public SpillRecord(Path indexFileName, JobConf job, Checksum crc, 
      String expectedIndexOwner) throws IOException {

    final FileSystem rfs = FileSystem.getLocal(job).getRaw();
    final DataInputStream in =
      new DataInputStream(SecureIOUtils.openForRead(
         new File(indexFileName.toUri().getPath()), expectedIndexOwner));
    try {
      final long length = rfs.getFileStatus(indexFileName).getLen();
      final int partitions = (int) length / MAP_OUTPUT_INDEX_RECORD_LENGTH;
      final int size = partitions * MAP_OUTPUT_INDEX_RECORD_LENGTH;

      buf = ByteBuffer.allocate(size);
      if (crc != null) {
        crc.reset();
        CheckedInputStream chk = new CheckedInputStream(in, crc);
        IOUtils.readFully(chk, buf.array(), 0, size);
        if (chk.getChecksum().getValue() != in.readLong()) {
          throw new ChecksumException("Checksum error reading spill index: " +
                                indexFileName, -1);
        }
      } else {
        IOUtils.readFully(in, buf.array(), 0, size);
      }
      entries = buf.asLongBuffer();
    } finally {
      in.close();
    }
  }

  /**
   * Return number of IndexRecord entries in this spill.
   */
  public int size() {
    return entries.capacity() / (MapTask.MAP_OUTPUT_INDEX_RECORD_LENGTH / 8);
  }

  /**
   * Get spill offsets for given partition.
   */
  public IndexRecord getIndex(int partition) {
    final int pos = partition * MapTask.MAP_OUTPUT_INDEX_RECORD_LENGTH / 8;
    return new IndexRecord(entries.get(pos), entries.get(pos + 1),
                           entries.get(pos + 2));
  }

  /**
   * Set spill offsets for given partition.
   */
  public void putIndex(IndexRecord rec, int partition) {
    final int pos = partition * MapTask.MAP_OUTPUT_INDEX_RECORD_LENGTH / 8;
    entries.put(pos, rec.startOffset);
    entries.put(pos + 1, rec.rawLength);
    entries.put(pos + 2, rec.partLength);
  }

  /**
   * Write this spill record to the location provided.
   */
  public void writeToFile(Path loc, JobConf job)
      throws IOException {
    writeToFile(loc, job, new PureJavaCrc32());
  }

  public void writeToFile(Path loc, JobConf job, Checksum crc)
      throws IOException {
    final FileSystem rfs = FileSystem.getLocal(job).getRaw();
    CheckedOutputStream chk = null;
    final FSDataOutputStream out = rfs.create(loc);
    try {
      if (crc != null) {
        crc.reset();
        chk = new CheckedOutputStream(out, crc);
        chk.write(buf.array());
        out.writeLong(chk.getChecksum().getValue());
      } else {
        out.write(buf.array());
      }
    } finally {
      if (chk != null) {
        chk.close();
      } else {
        out.close();
      }
    }
  }

}

class IndexRecord {
  long startOffset;
  long rawLength;
  long partLength;

  public IndexRecord() { }

  public IndexRecord(long startOffset, long rawLength, long partLength) {
    this.startOffset = startOffset;
    this.rawLength = rawLength;
    this.partLength = partLength;
  }
}
