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
package org.apache.hadoop.security.token;

import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.io.DataOutputBuffer;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.security.UserGroupInformation;

/**
 * An identifier that identifies a token, may contain public information 
 * about a token, including its kind (or type).
 */
public abstract class TokenIdentifier implements Writable {
  /**
   * Get the token kind
   * @return the kind of the token
   */
  public abstract Text getKind();

  /**
   * Get the Ugi with the username encoded in the token identifier
   * 
   * @return the username. null is returned if username in the identifier is
   *         empty or null.
   */
  public abstract UserGroupInformation getUser();

  /**
   * Get the bytes for the token identifier
   * @return the bytes of the identifier
   */
  public byte[] getBytes() {
    DataOutputBuffer buf = new DataOutputBuffer(4096);
    try {
      this.write(buf);
    } catch (IOException ie) {
      throw new RuntimeException("i/o error in getBytes", ie);
    }
    return Arrays.copyOf(buf.getData(), buf.getLength());
  }
}
