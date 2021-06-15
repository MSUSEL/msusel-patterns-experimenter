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
package org.apache.hadoop.mapred.gridmix;

import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IOUtils;

/**
 * For every record consumed, read key + val bytes from the stream provided.
 */
class ReadRecordFactory extends RecordFactory {

  /**
   * Size of internal, scratch buffer to read from internal stream.
   */
  public static final String GRIDMIX_READ_BUF_SIZE = "gridmix.read.buffer.size";

  private final byte[] buf;
  private final InputStream src;
  private final RecordFactory factory;

  /**
   * @param targetBytes Expected byte count.
   * @param targetRecords Expected record count.
   * @param src Stream to read bytes.
   * @param conf Used to establish read buffer size. @see #GRIDMIX_READ_BUF_SIZE
   */
  public ReadRecordFactory(long targetBytes, long targetRecords,
      InputStream src, Configuration conf) {
    this(new AvgRecordFactory(targetBytes, targetRecords, conf), src, conf);
  }

  /**
   * @param factory Factory to draw record sizes.
   * @param src Stream to read bytes.
   * @param conf Used to establish read buffer size. @see #GRIDMIX_READ_BUF_SIZE
   */
  public ReadRecordFactory(RecordFactory factory, InputStream src,
      Configuration conf) {
    this.src = src;
    this.factory = factory;
    buf = new byte[conf.getInt(GRIDMIX_READ_BUF_SIZE, 64 * 1024)];
  }

  @Override
  public boolean next(GridmixKey key, GridmixRecord val) throws IOException {
    if (!factory.next(key, val)) {
      return false;
    }
    for (int len = (null == key ? 0 : key.getSize()) + val.getSize();
         len > 0; len -= buf.length) {
      IOUtils.readFully(src, buf, 0, Math.min(buf.length, len));
    }
    return true;
  }

  @Override
  public float getProgress() throws IOException {
    return factory.getProgress();
  }

  @Override
  public void close() throws IOException {
    IOUtils.cleanup(null, src);
    factory.close();
  }
}
