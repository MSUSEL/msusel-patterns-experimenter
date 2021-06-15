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
import java.io.OutputStream;
import java.io.InputStream;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.compress.zlib.*;

public class DefaultCodec implements Configurable, CompressionCodec {
  
  Configuration conf;

  public void setConf(Configuration conf) {
    this.conf = conf;
  }
  
  public Configuration getConf() {
    return conf;
  }
  
  public CompressionOutputStream createOutputStream(OutputStream out) 
  throws IOException {
    return new CompressorStream(out, createCompressor(), 
                                conf.getInt("io.file.buffer.size", 4*1024));
  }

  public CompressionOutputStream createOutputStream(OutputStream out, 
                                                    Compressor compressor) 
  throws IOException {
    return new CompressorStream(out, compressor, 
                                conf.getInt("io.file.buffer.size", 4*1024));
  }

  public Class<? extends Compressor> getCompressorType() {
    return ZlibFactory.getZlibCompressorType(conf);
  }

  public Compressor createCompressor() {
    return ZlibFactory.getZlibCompressor(conf);
  }

  public CompressionInputStream createInputStream(InputStream in) 
  throws IOException {
    return new DecompressorStream(in, createDecompressor(),
                                  conf.getInt("io.file.buffer.size", 4*1024));
  }

  public CompressionInputStream createInputStream(InputStream in, 
                                                  Decompressor decompressor) 
  throws IOException {
    return new DecompressorStream(in, decompressor, 
                                  conf.getInt("io.file.buffer.size", 4*1024));
  }

  public Class<? extends Decompressor> getDecompressorType() {
    return ZlibFactory.getZlibDecompressorType(conf);
  }

  public Decompressor createDecompressor() {
    return ZlibFactory.getZlibDecompressor(conf);
  }
  
  public String getDefaultExtension() {
    return ".deflate";
  }

}
