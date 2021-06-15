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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

/**
 * {@link InputDemuxer} dem-ultiplexes the input files into individual input
 * streams.
 */
public interface InputDemuxer extends Closeable {
  /**
   * Bind the {@link InputDemuxer} to a particular file.
   * 
   * @param path
   *          The path to the find it should bind to.
   * @param conf
   *          Configuration
   * @throws IOException
   * 
   *           Returns true when the binding succeeds. If the file can be read
   *           but is in the wrong format, returns false. IOException is
   *           reserved for read errors.
   */
  public void bindTo(Path path, Configuration conf) throws IOException;

  /**
   * Get the next <name, input> pair. The name should preserve the original job
   * history file or job conf file name. The input object should be closed
   * before calling getNext() again. The old input object would be invalid after
   * calling getNext() again.
   * 
   * @return the next <name, input> pair.
   */
  public Pair<String, InputStream> getNext() throws IOException;
}
