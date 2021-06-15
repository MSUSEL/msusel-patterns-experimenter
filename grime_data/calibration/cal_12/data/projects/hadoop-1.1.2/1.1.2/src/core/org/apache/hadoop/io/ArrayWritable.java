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

/** 
 * A Writable for arrays containing instances of a class. The elements of this
 * writable must all be instances of the same class. If this writable will be
 * the input for a Reducer, you will need to create a subclass that sets the
 * value to be of the proper type.
 *
 * For example:
 * <code>
 * public class IntArrayWritable extends ArrayWritable {
 *   public IntArrayWritable() { 
 *     super(IntWritable.class); 
 *   }	
 * }
 * </code>
 */
public class ArrayWritable implements Writable {
  private Class<? extends Writable> valueClass;
  private Writable[] values;

  public ArrayWritable(Class<? extends Writable> valueClass) {
    if (valueClass == null) { 
      throw new IllegalArgumentException("null valueClass"); 
    }    
    this.valueClass = valueClass;
  }

  public ArrayWritable(Class<? extends Writable> valueClass, Writable[] values) {
    this(valueClass);
    this.values = values;
  }

  public ArrayWritable(String[] strings) {
    this(UTF8.class, new Writable[strings.length]);
    for (int i = 0; i < strings.length; i++) {
      values[i] = new UTF8(strings[i]);
    }
  }

  public Class getValueClass() {
    return valueClass;
  }

  public String[] toStrings() {
    String[] strings = new String[values.length];
    for (int i = 0; i < values.length; i++) {
      strings[i] = values[i].toString();
    }
    return strings;
  }

  public Object toArray() {
    Object result = Array.newInstance(valueClass, values.length);
    for (int i = 0; i < values.length; i++) {
      Array.set(result, i, values[i]);
    }
    return result;
  }

  public void set(Writable[] values) { this.values = values; }

  public Writable[] get() { return values; }

  public void readFields(DataInput in) throws IOException {
    values = new Writable[in.readInt()];          // construct values
    for (int i = 0; i < values.length; i++) {
      Writable value = WritableFactories.newInstance(valueClass);
      value.readFields(in);                       // read a value
      values[i] = value;                          // store it in values
    }
  }

  public void write(DataOutput out) throws IOException {
    out.writeInt(values.length);                 // write values
    for (int i = 0; i < values.length; i++) {
      values[i].write(out);
    }
  }

}

