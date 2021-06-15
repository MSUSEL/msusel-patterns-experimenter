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
package org.apache.hadoop.tools.rumen;

import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

/**
 * {@link DefaultInputDemuxer} acts as a pass-through demuxer. It just opens
 * each file and returns back the input stream. If the input is compressed, it
 * would return a decompression stream.
 */
public class DefaultInputDemuxer implements InputDemuxer {
  String name;
  InputStream input;

  @Override
  public void bindTo(Path path, Configuration conf) throws IOException {
    if (name != null) { // re-binding before the previous one was consumed.
      close();
    }
    name = path.getName();

    input = new PossiblyDecompressedInputStream(path, conf);

    return;
  }

  @Override
  public Pair<String, InputStream> getNext() throws IOException {
    if (name != null) {
      Pair<String, InputStream> ret =
          new Pair<String, InputStream>(name, input);
      name = null;
      input = null;
      return ret;
    }
    return null;
  }

  @Override
  public void close() throws IOException {
    try {
      if (input != null) {
        input.close();
      }
    } finally {
      name = null;
      input = null;
    }
  }
}
