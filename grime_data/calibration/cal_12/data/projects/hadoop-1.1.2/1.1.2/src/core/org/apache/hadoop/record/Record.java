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
package org.apache.hadoop.record;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.hadoop.io.WritableComparable;

/**
 * Abstract class that is extended by generated classes.
 * 
 */
public abstract class Record implements WritableComparable, Cloneable {
  
  /**
   * Serialize a record with tag (ususally field name)
   * @param rout Record output destination
   * @param tag record tag (Used only in tagged serialization e.g. XML)
   */
  public abstract void serialize(RecordOutput rout, String tag)
    throws IOException;
  
  /**
   * Deserialize a record with a tag (usually field name)
   * @param rin Record input source
   * @param tag Record tag (Used only in tagged serialization e.g. XML)
   */
  public abstract void deserialize(RecordInput rin, String tag)
    throws IOException;
  
  // inheric javadoc
  public abstract int compareTo (final Object peer) throws ClassCastException;
  
  /**
   * Serialize a record without a tag
   * @param rout Record output destination
   */
  public void serialize(RecordOutput rout) throws IOException {
    this.serialize(rout, "");
  }
  
  /**
   * Deserialize a record without a tag
   * @param rin Record input source
   */
  public void deserialize(RecordInput rin) throws IOException {
    this.deserialize(rin, "");
  }
  
  // inherit javadoc
  public void write(final DataOutput out) throws java.io.IOException {
    BinaryRecordOutput bout = BinaryRecordOutput.get(out);
    this.serialize(bout);
  }
  
  // inherit javadoc
  public void readFields(final DataInput din) throws java.io.IOException {
    BinaryRecordInput rin = BinaryRecordInput.get(din);
    this.deserialize(rin);
  }

  // inherit javadoc
  public String toString() {
    try {
      ByteArrayOutputStream s = new ByteArrayOutputStream();
      CsvRecordOutput a = new CsvRecordOutput(s);
      this.serialize(a);
      return new String(s.toByteArray(), "UTF-8");
    } catch (Throwable ex) {
      throw new RuntimeException(ex);
    }
  }
}
