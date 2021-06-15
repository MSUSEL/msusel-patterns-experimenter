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
package org.apache.hadoop.io.compress.bzip2;

import java.io.IOException;

import org.apache.hadoop.io.compress.Decompressor;

/**
 * This is a dummy decompressor for BZip2.
 */
public class BZip2DummyDecompressor implements Decompressor {

  @Override
  public int decompress(byte[] b, int off, int len) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void end() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean finished() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean needsDictionary() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean needsInput() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getRemaining() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void reset() {
    // do nothing
  }

  @Override
  public void setDictionary(byte[] b, int off, int len) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setInput(byte[] b, int off, int len) {
    throw new UnsupportedOperationException();
  }

}
