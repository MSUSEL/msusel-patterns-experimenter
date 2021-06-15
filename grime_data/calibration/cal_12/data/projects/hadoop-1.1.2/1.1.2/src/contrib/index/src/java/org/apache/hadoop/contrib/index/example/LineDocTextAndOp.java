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
package org.apache.hadoop.contrib.index.example;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.contrib.index.mapred.DocumentAndOp;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

/**
 * This class represents an operation. The operation can be an insert, a delete
 * or an update. If the operation is an insert or an update, a (new) document,
 * which is in the form of text, is specified.
 */
public class LineDocTextAndOp implements Writable {
  private DocumentAndOp.Op op;
  private Text doc;

  /**
   * Constructor
   */
  public LineDocTextAndOp() {
    doc = new Text();
  }

  /**
   * Set the type of the operation.
   * @param op  the type of the operation
   */
  public void setOp(DocumentAndOp.Op op) {
    this.op = op;
  }

  /**
   * Get the type of the operation.
   * @return the type of the operation
   */
  public DocumentAndOp.Op getOp() {
    return op;
  }

  /**
   * Get the text that represents a document.
   * @return the text that represents a document
   */
  public Text getText() {
    return doc;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return this.getClass().getName() + "[op=" + op + ", text=" + doc + "]";
  }

  /* (non-Javadoc)
   * @see org.apache.hadoop.io.Writable#write(java.io.DataOutput)
   */
  public void write(DataOutput out) throws IOException {
    throw new IOException(this.getClass().getName()
        + ".write should never be called");
  }

  /* (non-Javadoc)
   * @see org.apache.hadoop.io.Writable#readFields(java.io.DataInput)
   */
  public void readFields(DataInput in) throws IOException {
    throw new IOException(this.getClass().getName()
        + ".readFields should never be called");
  }

}
