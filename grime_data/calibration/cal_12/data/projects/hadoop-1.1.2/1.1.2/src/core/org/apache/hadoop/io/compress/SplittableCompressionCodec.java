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
package org.apache.hadoop.io.compress;

import java.io.IOException;
import java.io.InputStream;

/**
 * This interface is meant to be implemented by those compression codecs
 * which are capable to compress / decompress a stream starting at any
 * arbitrary position.
 *
 * Especially the process of decompressing a stream starting at some arbitrary
 * position is challenging.  Most of the codecs are only able to successfully
 * decompress a stream, if they start from the very beginning till the end.
 * One of the reasons is the stored state at the beginning of the stream which
 * is crucial for decompression.
 *
 * Yet there are few codecs which do not save the whole state at the beginning
 * of the stream and hence can be used to de-compress stream starting at any
 * arbitrary points.  This interface is meant to be used by such codecs.  Such
 * codecs are highly valuable, especially in the context of Hadoop, because
 * an input compressed file can be split and hence can be worked on by multiple
 * machines in parallel.
 */
public interface SplittableCompressionCodec extends CompressionCodec {

  /**
   * During decompression, data can be read off from the decompressor in two
   * modes, namely continuous and blocked.  Few codecs (e.g. BZip2) are capable
   * of compressing data in blocks and then decompressing the blocks.  In
   * Blocked reading mode codecs inform 'end of block' events to its caller.
   * While in continuous mode, the caller of codecs is unaware about the blocks
   * and uncompressed data is spilled out like a continuous stream.
   */
  public enum READ_MODE {CONTINUOUS, BYBLOCK};

  /**
   * Create a stream as dictated by the readMode.  This method is used when
   * the codecs wants the ability to work with the underlying stream positions.
   *
   * @param seekableIn  The seekable input stream (seeks in compressed data)
   * @param start The start offset into the compressed stream. May be changed
   *              by the underlying codec.
   * @param end The end offset into the compressed stream. May be changed by
   *            the underlying codec.
   * @param readMode Controls whether stream position is reported continuously
   *                 from the compressed stream only only at block boundaries.
   * @return  a stream to read uncompressed bytes from
   */
  SplitCompressionInputStream createInputStream(InputStream seekableIn,
      Decompressor decompressor, long start, long end, READ_MODE readMode)
      throws IOException;

}