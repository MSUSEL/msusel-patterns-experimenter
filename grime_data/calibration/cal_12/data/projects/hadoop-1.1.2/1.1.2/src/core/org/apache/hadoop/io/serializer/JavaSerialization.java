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
package org.apache.hadoop.io.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * <p>
 * An experimental {@link Serialization} for Java {@link Serializable} classes.
 * </p>
 * @see JavaSerializationComparator
 */
public class JavaSerialization implements Serialization<Serializable> {
  
  static class JavaSerializationDeserializer<T extends Serializable>
    implements Deserializer<T> {

    private ObjectInputStream ois;

    public void open(InputStream in) throws IOException {
      ois = new ObjectInputStream(in) {
        @Override protected void readStreamHeader() {
          // no header
        }
      };
    }
    
    @SuppressWarnings("unchecked")
    public T deserialize(T object) throws IOException {
      try {
        // ignore passed-in object
        return (T) ois.readObject();
      } catch (ClassNotFoundException e) {
        throw new IOException(e.toString());
      }
    }

    public void close() throws IOException {
      ois.close();
    }

  }
  
  static class JavaSerializationSerializer
    implements Serializer<Serializable> {

    private ObjectOutputStream oos;

    public void open(OutputStream out) throws IOException {
      oos = new ObjectOutputStream(out) {
        @Override protected void writeStreamHeader() {
          // no header
        }
      };
    }

    public void serialize(Serializable object) throws IOException {
      oos.reset(); // clear (class) back-references
      oos.writeObject(object);
    }

    public void close() throws IOException {
      oos.close();
    }

  }

  public boolean accept(Class<?> c) {
    return Serializable.class.isAssignableFrom(c);
  }

  public Deserializer<Serializable> getDeserializer(Class<Serializable> c) {
    return new JavaSerializationDeserializer<Serializable>();
  }

  public Serializer<Serializable> getSerializer(Class<Serializable> c) {
    return new JavaSerializationSerializer();
  }

}
