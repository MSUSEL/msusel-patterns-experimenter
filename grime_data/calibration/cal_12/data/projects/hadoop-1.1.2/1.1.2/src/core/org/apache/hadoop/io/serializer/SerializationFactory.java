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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.hadoop.util.StringUtils;

/**
 * <p>
 * A factory for {@link Serialization}s.
 * </p>
 */
public class SerializationFactory extends Configured {
  
  private static final Log LOG =
    LogFactory.getLog(SerializationFactory.class.getName());

  private List<Serialization<?>> serializations = new ArrayList<Serialization<?>>();
  
  /**
   * <p>
   * Serializations are found by reading the <code>io.serializations</code>
   * property from <code>conf</code>, which is a comma-delimited list of
   * classnames. 
   * </p>
   */
  public SerializationFactory(Configuration conf) {
    super(conf);
    for (String serializerName : conf.getStrings("io.serializations", 
      new String[]{"org.apache.hadoop.io.serializer.WritableSerialization"})) {
      add(conf, serializerName);
    }
  }
  
  @SuppressWarnings("unchecked")
  private void add(Configuration conf, String serializationName) {
    try {
      
      Class<? extends Serialization> serializionClass =
        (Class<? extends Serialization>) conf.getClassByName(serializationName);
      serializations.add((Serialization)
          ReflectionUtils.newInstance(serializionClass, getConf()));
    } catch (ClassNotFoundException e) {
      LOG.warn("Serilization class not found: " +
          StringUtils.stringifyException(e));
    }
  }

  public <T> Serializer<T> getSerializer(Class<T> c) {
    return getSerialization(c).getSerializer(c);
  }

  public <T> Deserializer<T> getDeserializer(Class<T> c) {
    return getSerialization(c).getDeserializer(c);
  }

  @SuppressWarnings("unchecked")
  public <T> Serialization<T> getSerialization(Class<T> c) {
    for (Serialization serialization : serializations) {
      if (serialization.accept(c)) {
        return (Serialization<T>) serialization;
      }
    }
    return null;
  }
}
