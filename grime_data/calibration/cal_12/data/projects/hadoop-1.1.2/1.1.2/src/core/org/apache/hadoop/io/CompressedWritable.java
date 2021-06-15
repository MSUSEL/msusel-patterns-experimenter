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

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/** A base-class for Writables which store themselves compressed and lazily
 * inflate on field access.  This is useful for large objects whose fields are
 * not be altered during a map or reduce operation: leaving the field data
 * compressed makes copying the instance from one file to another much
 * faster. */
public abstract class CompressedWritable implements Writable {
  // if non-null, the compressed field data of this instance.
  private byte[] compressed;

  public CompressedWritable() {}

  public final void readFields(DataInput in) throws IOException {
    compressed = new byte[in.readInt()];
    in.readFully(compressed, 0, compressed.length);
  }

  /** Must be called by all methods which access fields to ensure that the data
   * has been uncompressed. */
  protected void ensureInflated() {
    if (compressed != null) {
      try {
        ByteArrayInputStream deflated = new ByteArrayInputStream(compressed);
        DataInput inflater =
          new DataInputStream(new InflaterInputStream(deflated));
        readFieldsCompressed(inflater);
        compressed = null;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  /** Subclasses implement this instead of {@link #readFields(DataInput)}. */
  protected abstract void readFieldsCompressed(DataInput in)
    throws IOException;

  public final void write(DataOutput out) throws IOException {
    if (compressed == null) {
      ByteArrayOutputStream deflated = new ByteArrayOutputStream();
      Deflater deflater = new Deflater(Deflater.BEST_SPEED);
      DataOutputStream dout =
        new DataOutputStream(new DeflaterOutputStream(deflated, deflater));
      writeCompressed(dout);
      dout.close();
      deflater.end();
      compressed = deflated.toByteArray();
    }
    out.writeInt(compressed.length);
    out.write(compressed);
  }

  /** Subclasses implement this instead of {@link #write(DataOutput)}. */
  protected abstract void writeCompressed(DataOutput out) throws IOException;

}
