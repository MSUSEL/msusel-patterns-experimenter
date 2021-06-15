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
package org.apache.hadoop.tools.rumen;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

/**
 * Simple wrapper around {@link JsonGenerator} to write objects in JSON format.
 * @param <T> The type of the objects to be written.
 */
public class JsonObjectMapperWriter<T> implements Closeable {
  private JsonGenerator writer;
  
  public JsonObjectMapperWriter(OutputStream output, boolean prettyPrint) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(
        SerializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS, true);
    mapper.getJsonFactory();
    writer = mapper.getJsonFactory().createJsonGenerator(
        output, JsonEncoding.UTF8);
    if (prettyPrint) {
      writer.useDefaultPrettyPrinter();
    }
  }
  
  public void write(T object) throws IOException {
    writer.writeObject(object);
  }
  
  @Override
  public void close() throws IOException {
    writer.close();
  }
}
