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
package org.apache.hadoop.io;

import java.io.DataOutput;
import java.io.DataInput;
import java.io.IOException;

/** A base class for Writables that provides version checking.
 *
 * <p>This is useful when a class may evolve, so that instances written by the
 * old version of the class may still be processed by the new version.  To
 * handle this situation, {@link #readFields(DataInput)}
 * implementations should catch {@link VersionMismatchException}.
 */
public abstract class VersionedWritable implements Writable {

  /** Return the version number of the current implementation. */
  public abstract byte getVersion();
    
  // javadoc from Writable
  public void write(DataOutput out) throws IOException {
    out.writeByte(getVersion());                  // store version
  }

  // javadoc from Writable
  public void readFields(DataInput in) throws IOException {
    byte version = in.readByte();                 // read version
    if (version != getVersion())
      throw new VersionMismatchException(getVersion(), version);
  }

    
}
