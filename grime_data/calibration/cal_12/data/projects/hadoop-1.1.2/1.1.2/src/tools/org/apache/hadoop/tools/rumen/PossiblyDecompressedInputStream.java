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
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.compress.CodecPool;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.Decompressor;

class PossiblyDecompressedInputStream extends InputStream {
  private final Decompressor decompressor;
  private final InputStream coreInputStream;

  public PossiblyDecompressedInputStream(Path inputPath, Configuration conf)
      throws IOException {
    CompressionCodecFactory codecs = new CompressionCodecFactory(conf);
    CompressionCodec inputCodec = codecs.getCodec(inputPath);

    FileSystem ifs = inputPath.getFileSystem(conf);
    FSDataInputStream fileIn = ifs.open(inputPath);

    if (inputCodec == null) {
      decompressor = null;
      coreInputStream = fileIn;
    } else {
      decompressor = CodecPool.getDecompressor(inputCodec);
      coreInputStream = inputCodec.createInputStream(fileIn, decompressor);
    }
  }

  @Override
  public int read() throws IOException {
    return coreInputStream.read();
  }

  @Override
  public int read(byte[] buffer, int offset, int length) throws IOException {
    return coreInputStream.read(buffer, offset, length);
  }

  @Override
  public void close() throws IOException {
    if (decompressor != null) {
      CodecPool.returnDecompressor(decompressor);
    }

    coreInputStream.close();
  }

}
