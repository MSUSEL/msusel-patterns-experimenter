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
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.serializer.Deserializer;
import org.apache.hadoop.io.serializer.Serialization;
import org.apache.hadoop.io.serializer.SerializationFactory;
import org.apache.hadoop.io.serializer.Serializer;
import org.apache.hadoop.util.GenericsUtil;

/**
 * DefaultStringifier is the default implementation of the {@link Stringifier}
 * interface which stringifies the objects using base64 encoding of the
 * serialized version of the objects. The {@link Serializer} and
 * {@link Deserializer} are obtained from the {@link SerializationFactory}.
 * <br>
 * DefaultStringifier offers convenience methods to store/load objects to/from
 * the configuration.
 * 
 * @param <T> the class of the objects to stringify
 */
public class DefaultStringifier<T> implements Stringifier<T> {

  private static final String SEPARATOR = ",";

  private Serializer<T> serializer;

  private Deserializer<T> deserializer;

  private DataInputBuffer inBuf;

  private DataOutputBuffer outBuf;

  public DefaultStringifier(Configuration conf, Class<T> c) {

    SerializationFactory factory = new SerializationFactory(conf);
    this.serializer = factory.getSerializer(c);
    this.deserializer = factory.getDeserializer(c);
    this.inBuf = new DataInputBuffer();
    this.outBuf = new DataOutputBuffer();
    try {
      serializer.open(outBuf);
      deserializer.open(inBuf);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  public T fromString(String str) throws IOException {
    try {
      byte[] bytes = Base64.decodeBase64(str.getBytes("UTF-8"));
      inBuf.reset(bytes, bytes.length);
      T restored = deserializer.deserialize(null);
      return restored;
    } catch (UnsupportedCharsetException ex) {
      throw new IOException(ex.toString());
    }
  }

  public String toString(T obj) throws IOException {
    outBuf.reset();
    serializer.serialize(obj);
    byte[] buf = new byte[outBuf.getLength()];
    System.arraycopy(outBuf.getData(), 0, buf, 0, buf.length);
    return new String(Base64.encodeBase64(buf));
  }

  public void close() throws IOException {
    inBuf.close();
    outBuf.close();
    deserializer.close();
    serializer.close();
  }

  /**
   * Stores the item in the configuration with the given keyName.
   * 
   * @param <K>  the class of the item
   * @param conf the configuration to store
   * @param item the object to be stored
   * @param keyName the name of the key to use
   * @throws IOException : forwards Exceptions from the underlying 
   * {@link Serialization} classes. 
   */
  public static <K> void store(Configuration conf, K item, String keyName)
  throws IOException {

    DefaultStringifier<K> stringifier = new DefaultStringifier<K>(conf,
        GenericsUtil.getClass(item));
    conf.set(keyName, stringifier.toString(item));
    stringifier.close();
  }

  /**
   * Restores the object from the configuration.
   * 
   * @param <K> the class of the item
   * @param conf the configuration to use
   * @param keyName the name of the key to use
   * @param itemClass the class of the item
   * @return restored object
   * @throws IOException : forwards Exceptions from the underlying 
   * {@link Serialization} classes.
   */
  public static <K> K load(Configuration conf, String keyName,
      Class<K> itemClass) throws IOException {
    DefaultStringifier<K> stringifier = new DefaultStringifier<K>(conf,
        itemClass);
    try {
      String itemStr = conf.get(keyName);
      return stringifier.fromString(itemStr);
    } finally {
      stringifier.close();
    }
  }

  /**
   * Stores the array of items in the configuration with the given keyName.
   * 
   * @param <K> the class of the item
   * @param conf the configuration to use 
   * @param items the objects to be stored
   * @param keyName the name of the key to use
   * @throws IndexOutOfBoundsException if the items array is empty
   * @throws IOException : forwards Exceptions from the underlying 
   * {@link Serialization} classes.         
   */
  public static <K> void storeArray(Configuration conf, K[] items,
      String keyName) throws IOException {

    DefaultStringifier<K> stringifier = new DefaultStringifier<K>(conf, 
        GenericsUtil.getClass(items[0]));
    try {
      StringBuilder builder = new StringBuilder();
      for (K item : items) {
        builder.append(stringifier.toString(item)).append(SEPARATOR);
      }
      conf.set(keyName, builder.toString());
    }
    finally {
      stringifier.close();
    }
  }

  /**
   * Restores the array of objects from the configuration.
   * 
   * @param <K> the class of the item
   * @param conf the configuration to use
   * @param keyName the name of the key to use
   * @param itemClass the class of the item
   * @return restored object
   * @throws IOException : forwards Exceptions from the underlying 
   * {@link Serialization} classes.
   */
  public static <K> K[] loadArray(Configuration conf, String keyName,
      Class<K> itemClass) throws IOException {
    DefaultStringifier<K> stringifier = new DefaultStringifier<K>(conf,
        itemClass);
    try {
      String itemStr = conf.get(keyName);
      ArrayList<K> list = new ArrayList<K>();
      String[] parts = itemStr.split(SEPARATOR);

      for (String part : parts) {
        if (!part.equals(""))
          list.add(stringifier.fromString(part));
      }

      return GenericsUtil.toArray(itemClass, list);
    }
    finally {
      stringifier.close();
    }
  }

}
