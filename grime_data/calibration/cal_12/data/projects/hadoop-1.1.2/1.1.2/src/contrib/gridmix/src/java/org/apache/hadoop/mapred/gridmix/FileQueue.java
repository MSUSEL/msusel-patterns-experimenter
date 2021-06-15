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
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

/**
 * Given a {@link org.apache.hadoop.mapreduce.lib.input.CombineFileSplit},
 * circularly read through each input source.
 */
class FileQueue extends InputStream {

  private int idx = -1;
  private long curlen = -1L;
  private InputStream input;
  private final byte[] z = new byte[1];
  private final Path[] paths;
  private final long[] lengths;
  private final long[] startoffset;
  private final Configuration conf;

  /**
   * @param split Description of input sources.
   * @param conf Used to resolve FileSystem instances.
   */
  public FileQueue(CombineFileSplit split, Configuration conf)
      throws IOException {
    this.conf = conf;
    paths = split.getPaths();
    startoffset = split.getStartOffsets();
    lengths = split.getLengths();
    nextSource();
  }

  protected void nextSource() throws IOException {
    if (0 == paths.length) {
      return;
    }
    if (input != null) {
      input.close();
    }
    idx = (idx + 1) % paths.length;
    curlen = lengths[idx];
    final Path file = paths[idx];
    input = 
      CompressionEmulationUtil.getPossiblyDecompressedInputStream(file, 
                                 conf, startoffset[idx]);
  }

  @Override
  public int read() throws IOException {
    final int tmp = read(z);
    return tmp == -1 ? -1 : (0xFF & z[0]);
  }

  @Override
  public int read(byte[] b) throws IOException {
    return read(b, 0, b.length);
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    int kvread = 0;
    while (kvread < len) {
      if (curlen <= 0) {
        nextSource();
        continue;
      }
      final int srcRead = (int) Math.min(len - kvread, curlen);
      IOUtils.readFully(input, b, kvread, srcRead);
      curlen -= srcRead;
      kvread += srcRead;
    }
    return kvread;
  }

  @Override
  public void close() throws IOException {
    input.close();
  }

}
