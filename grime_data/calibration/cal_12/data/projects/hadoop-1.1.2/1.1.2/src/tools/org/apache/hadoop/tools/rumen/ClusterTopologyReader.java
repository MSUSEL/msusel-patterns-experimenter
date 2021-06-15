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

import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

/**
 * Reading JSON-encoded cluster topology and produce the parsed
 * {@link LoggedNetworkTopology} object.
 */
public class ClusterTopologyReader {
  private LoggedNetworkTopology topology;

  private void readTopology(JsonObjectMapperParser<LoggedNetworkTopology> parser)
      throws IOException {
    try {
      topology = parser.getNext();
      if (topology == null) {
        throw new IOException(
            "Input file does not contain valid topology data.");
      }
    } finally {
      parser.close();
    }
  }

  /**
   * Constructor.
   * 
   * @param path
   *          Path to the JSON-encoded topology file, possibly compressed.
   * @param conf
   * @throws IOException
   */
  public ClusterTopologyReader(Path path, Configuration conf)
      throws IOException {
    JsonObjectMapperParser<LoggedNetworkTopology> parser = new JsonObjectMapperParser<LoggedNetworkTopology>(
        path, LoggedNetworkTopology.class, conf);
    readTopology(parser);
  }

  /**
   * Constructor.
   * 
   * @param input
   *          The input stream for the JSON-encoded topology data.
   */
  public ClusterTopologyReader(InputStream input) throws IOException {
    JsonObjectMapperParser<LoggedNetworkTopology> parser = new JsonObjectMapperParser<LoggedNetworkTopology>(
        input, LoggedNetworkTopology.class);
    readTopology(parser);
  }

  /**
   * Get the {@link LoggedNetworkTopology} object.
   * 
   * @return The {@link LoggedNetworkTopology} object parsed from the input.
   */
  public LoggedNetworkTopology get() {
    return topology;
  }
}
