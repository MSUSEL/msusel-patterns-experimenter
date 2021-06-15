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
import java.io.OutputStream;

/**
 * This class encapsulates a streaming compression/decompression pair.
 */
public interface CompressionCodec {

  /**
   * Create a {@link CompressionOutputStream} that will write to the given 
   * {@link OutputStream}.
   * 
   * @param out the location for the final output stream
   * @return a stream the user can write uncompressed data to have it compressed
   * @throws IOException
   */
  CompressionOutputStream createOutputStream(OutputStream out) 
  throws IOException;
  
  /**
   * Create a {@link CompressionOutputStream} that will write to the given 
   * {@link OutputStream} with the given {@link Compressor}.
   * 
   * @param out the location for the final output stream
   * @param compressor compressor to use
   * @return a stream the user can write uncompressed data to have it compressed
   * @throws IOException
   */
  CompressionOutputStream createOutputStream(OutputStream out, 
                                             Compressor compressor) 
  throws IOException;

  /**
   * Get the type of {@link Compressor} needed by this {@link CompressionCodec}.
   * 
   * @return the type of compressor needed by this codec.
   */
  Class<? extends Compressor> getCompressorType();
  
  /**
   * Create a new {@link Compressor} for use by this {@link CompressionCodec}.
   * 
   * @return a new compressor for use by this codec
   */
  Compressor createCompressor();
  
  /**
   * Create a stream decompressor that will read from the given input stream.
   * 
   * @param in the stream to read compressed bytes from
   * @return a stream to read uncompressed bytes from
   * @throws IOException
   */
  CompressionInputStream createInputStream(InputStream in) throws IOException;
  
  /**
   * Create a {@link CompressionInputStream} that will read from the given 
   * {@link InputStream} with the given {@link Decompressor}.
   * 
   * @param in the stream to read compressed bytes from
   * @param decompressor decompressor to use
   * @return a stream to read uncompressed bytes from
   * @throws IOException
   */
  CompressionInputStream createInputStream(InputStream in, 
                                           Decompressor decompressor) 
  throws IOException;


  /**
   * Get the type of {@link Decompressor} needed by this {@link CompressionCodec}.
   * 
   * @return the type of decompressor needed by this codec.
   */
  Class<? extends Decompressor> getDecompressorType();
  
  /**
   * Create a new {@link Decompressor} for use by this {@link CompressionCodec}.
   * 
   * @return a new decompressor for use by this codec
   */
  Decompressor createDecompressor();
  
  /**
   * Get the default filename extension for this kind of compression.
   * @return the extension including the '.'
   */
  String getDefaultExtension();
}
