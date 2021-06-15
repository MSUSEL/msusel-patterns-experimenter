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
package org.apache.hadoop.io.compress.zlib;

import java.io.IOException;
import java.util.zip.Deflater;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.compress.Compressor;
import org.mortbay.log.Log;

/**
 * A wrapper around java.util.zip.Deflater to make it conform 
 * to org.apache.hadoop.io.compress.Compressor interface.
 * 
 */
public class BuiltInZlibDeflater extends Deflater implements Compressor {

  public BuiltInZlibDeflater(int level, boolean nowrap) {
    super(level, nowrap);
  }

  BuiltInZlibDeflater(Configuration conf) {
    this(null == conf
        ? DEFAULT_COMPRESSION
        : ZlibFactory.getCompressionLevel(conf).compressionLevel());
    if (conf != null) {
      final ZlibCompressor.CompressionStrategy strategy =
        ZlibFactory.getCompressionStrategy(conf);
      try {
        setStrategy(strategy.compressionStrategy());
      } catch (IllegalArgumentException ill) {
        Log.warn(strategy + " not supported by BuiltInZlibDeflater.");
        setStrategy(DEFAULT_STRATEGY);
      }
    }
  }

  public BuiltInZlibDeflater(int level) {
    super(level);
  }

  public BuiltInZlibDeflater() {
    super();
  }

  public synchronized int compress(byte[] b, int off, int len) 
    throws IOException {
    return super.deflate(b, off, len);
  }

  /**
   * reinit the compressor with the given configuration. It will reset the
   * compressor's compression level and compression strategy. Different from
   * <tt>ZlibCompressor</tt>, <tt>BuiltInZlibDeflater</tt> only support three
   * kind of compression strategy: FILTERED, HUFFMAN_ONLY and DEFAULT_STRATEGY.
   * It will use DEFAULT_STRATEGY as default if the configured compression
   * strategy is not supported.
   */
  @Override
  public void reinit(Configuration conf) {
    reset();
    if (conf == null) {
      return;
    }
    setLevel(ZlibFactory.getCompressionLevel(conf).compressionLevel());
    final ZlibCompressor.CompressionStrategy strategy =
      ZlibFactory.getCompressionStrategy(conf);
    try {
      setStrategy(strategy.compressionStrategy());
    } catch (IllegalArgumentException ill) {
      Log.warn(strategy + " not supported by BuiltInZlibDeflater.");
      setStrategy(DEFAULT_STRATEGY);
    }
    Log.debug("Reinit compressor with new compression configuration");
  }
}
