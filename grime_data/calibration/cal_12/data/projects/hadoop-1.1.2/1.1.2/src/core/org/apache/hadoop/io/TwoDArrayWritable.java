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

import java.io.*;
import java.lang.reflect.Array;

/** A Writable for 2D arrays containing a matrix of instances of a class. */
public class TwoDArrayWritable implements Writable {
  private Class valueClass;
  private Writable[][] values;

  public TwoDArrayWritable(Class valueClass) {
    this.valueClass = valueClass;
  }

  public TwoDArrayWritable(Class valueClass, Writable[][] values) {
    this(valueClass);
    this.values = values;
  }

  public Object toArray() {
    int dimensions[] = {values.length, 0};
    Object result = Array.newInstance(valueClass, dimensions);
    for (int i = 0; i < values.length; i++) {
      Object resultRow = Array.newInstance(valueClass, values[i].length);
      Array.set(result, i, resultRow);
      for (int j = 0; j < values[i].length; j++) {
        Array.set(resultRow, j, values[i][j]);
      }
    }
    return result;
  }

  public void set(Writable[][] values) { this.values = values; }

  public Writable[][] get() { return values; }

  public void readFields(DataInput in) throws IOException {
    // construct matrix
    values = new Writable[in.readInt()][];          
    for (int i = 0; i < values.length; i++) {
      values[i] = new Writable[in.readInt()];
    }

    // construct values
    for (int i = 0; i < values.length; i++) {
      for (int j = 0; j < values[i].length; j++) {
        Writable value;                             // construct value
        try {
          value = (Writable)valueClass.newInstance();
        } catch (InstantiationException e) {
          throw new RuntimeException(e.toString());
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e.toString());
        }
        value.readFields(in);                       // read a value
        values[i][j] = value;                       // store it in values
      }
    }
  }

  public void write(DataOutput out) throws IOException {
    out.writeInt(values.length);                 // write values
    for (int i = 0; i < values.length; i++) {
      out.writeInt(values[i].length);
    }
    for (int i = 0; i < values.length; i++) {
      for (int j = 0; j < values[i].length; j++) {
        values[i][j].write(out);
      }
    }
  }
}

