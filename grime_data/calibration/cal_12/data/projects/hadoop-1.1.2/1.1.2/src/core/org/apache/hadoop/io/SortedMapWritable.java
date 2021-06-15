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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.hadoop.util.ReflectionUtils;

/**
 * A Writable SortedMap.
 */
public class SortedMapWritable extends AbstractMapWritable
  implements SortedMap<WritableComparable, Writable> {
  
  private SortedMap<WritableComparable, Writable> instance;
  
  /** default constructor. */
  public SortedMapWritable() {
    super();
    this.instance = new TreeMap<WritableComparable, Writable>();
  }
  
  /**
   * Copy constructor.
   * 
   * @param other the map to copy from
   */
  public SortedMapWritable(SortedMapWritable other) {
    this();
    copy(other);
  }

  /** {@inheritDoc} */
  public Comparator<? super WritableComparable> comparator() {
    // Returning null means we use the natural ordering of the keys
    return null;
  }

  /** {@inheritDoc} */
  public WritableComparable firstKey() {
    return instance.firstKey();
  }

  /** {@inheritDoc} */
  public SortedMap<WritableComparable, Writable>
  headMap(WritableComparable toKey) {
    
    return instance.headMap(toKey);
  }

  /** {@inheritDoc} */
  public WritableComparable lastKey() {
    return instance.lastKey();
  }

  /** {@inheritDoc} */
  public SortedMap<WritableComparable, Writable>
  subMap(WritableComparable fromKey, WritableComparable toKey) {
    
    return instance.subMap(fromKey, toKey);
  }

  /** {@inheritDoc} */
  public SortedMap<WritableComparable, Writable>
  tailMap(WritableComparable fromKey) {
    
    return instance.tailMap(fromKey);
  }

  /** {@inheritDoc} */
  public void clear() {
    instance.clear();
  }

  /** {@inheritDoc} */
  public boolean containsKey(Object key) {
    return instance.containsKey(key);
  }

  /** {@inheritDoc} */
  public boolean containsValue(Object value) {
    return instance.containsValue(value);
  }

  /** {@inheritDoc} */
  public Set<java.util.Map.Entry<WritableComparable, Writable>> entrySet() {
    return instance.entrySet();
  }

  /** {@inheritDoc} */
  public Writable get(Object key) {
    return instance.get(key);
  }

  /** {@inheritDoc} */
  public boolean isEmpty() {
    return instance.isEmpty();
  }

  /** {@inheritDoc} */
  public Set<WritableComparable> keySet() {
    return instance.keySet();
  }

  /** {@inheritDoc} */
  public Writable put(WritableComparable key, Writable value) {
    addToMap(key.getClass());
    addToMap(value.getClass());
    return instance.put(key, value);
  }

  /** {@inheritDoc} */
  public void putAll(Map<? extends WritableComparable, ? extends Writable> t) {
    for (Map.Entry<? extends WritableComparable, ? extends Writable> e:
      t.entrySet()) {
      
      instance.put(e.getKey(), e.getValue());
    }
  }

  /** {@inheritDoc} */
  public Writable remove(Object key) {
    return instance.remove(key);
  }

  /** {@inheritDoc} */
  public int size() {
    return instance.size();
  }

  /** {@inheritDoc} */
  public Collection<Writable> values() {
    return instance.values();
  }

  /** {@inheritDoc} */
  @SuppressWarnings("unchecked")
  @Override
  public void readFields(DataInput in) throws IOException {
    super.readFields(in);
    
    // Read the number of entries in the map
    
    int entries = in.readInt();
    
    // Then read each key/value pair
    
    for (int i = 0; i < entries; i++) {
      WritableComparable key =
        (WritableComparable) ReflectionUtils.newInstance(getClass(
            in.readByte()), getConf());
      
      key.readFields(in);
      
      Writable value = (Writable) ReflectionUtils.newInstance(getClass(
          in.readByte()), getConf());
      
      value.readFields(in);
      instance.put(key, value);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void write(DataOutput out) throws IOException {
    super.write(out);
    
    // Write out the number of entries in the map
    
    out.writeInt(instance.size());
    
    // Then write out each key/value pair
    
    for (Map.Entry<WritableComparable, Writable> e: instance.entrySet()) {
      out.writeByte(getId(e.getKey().getClass()));
      e.getKey().write(out);
      out.writeByte(getId(e.getValue().getClass()));
      e.getValue().write(out);
    }
  }
}
