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
import java.io.OutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.compress.CodecPool;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.Compressor;

/**
 * The default {@link Outputter} that outputs to a plain file. Compression
 * will be applied if the path has the right suffix.
 */
public class DefaultOutputter<T> implements Outputter<T> {
  JsonObjectMapperWriter<T> writer;
  Compressor compressor;
  
  @Override
  public void init(Path path, Configuration conf) throws IOException {
    FileSystem fs = path.getFileSystem(conf);
    CompressionCodec codec = new CompressionCodecFactory(conf).getCodec(path);
    OutputStream output;
    if (codec != null) {
      compressor = CodecPool.getCompressor(codec);
      output = codec.createOutputStream(fs.create(path), compressor);
    } else {
      output = fs.create(path);
    }
    writer = new JsonObjectMapperWriter<T>(output, 
        conf.getBoolean("rumen.output.pretty.print", true));
  }

  @Override
  public void output(T object) throws IOException {
    writer.write(object);
  }

  @Override
  public void close() throws IOException {
    try {
      writer.close();
    } finally {
      if (compressor != null) {
        CodecPool.returnCompressor(compressor);
      }
    }
  }
}
