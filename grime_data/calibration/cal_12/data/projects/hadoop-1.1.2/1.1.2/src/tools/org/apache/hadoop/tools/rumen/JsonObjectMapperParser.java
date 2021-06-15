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
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * A simple wrapper for parsing JSON-encoded data using ObjectMapper.
 * 
 * @param <T>
 *          The (base) type of the object(s) to be parsed by this parser.
 */
class JsonObjectMapperParser<T> implements Closeable {
  private final ObjectMapper mapper;
  private final Class<? extends T> clazz;
  private final JsonParser jsonParser;

  /**
   * Constructor.
   * 
   * @param path
   *          Path to the JSON data file, possibly compressed.
   * @param conf
   * @throws IOException
   */
  public JsonObjectMapperParser(Path path, Class<? extends T> clazz,
      Configuration conf) throws IOException {
    mapper = new ObjectMapper();
    mapper.configure(
        DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS, true);
    this.clazz = clazz;
    InputStream input = new PossiblyDecompressedInputStream(path, conf);
    jsonParser = mapper.getJsonFactory().createJsonParser(input);
  }

  /**
   * Constructor.
   * 
   * @param input
   *          The input stream for the JSON data.
   */
  public JsonObjectMapperParser(InputStream input, Class<? extends T> clazz)
      throws IOException {
    mapper = new ObjectMapper();
    mapper.configure(
        DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS, true);
    this.clazz = clazz;
    jsonParser = mapper.getJsonFactory().createJsonParser(input);
  }

  /**
   * Get the next object from the trace.
   * 
   * @return The next instance of the object. Or null if we reach the end of
   *         stream.
   * @throws IOException
   */
  public T getNext() throws IOException {
    try {
      return mapper.readValue(jsonParser, clazz);
    } catch (EOFException e) {
      return null;
    }
  }

  @Override
  public void close() throws IOException {
    jsonParser.close();
  }
}
