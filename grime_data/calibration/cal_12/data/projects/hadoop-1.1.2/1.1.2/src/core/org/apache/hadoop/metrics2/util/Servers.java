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

package org.apache.hadoop.metrics2.util;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Helpers to handle server addresses
 */
public class Servers {

  /**
   * This class is not intended to be instantiated
   */
  private Servers() {}

  /**
   * Parses a space and/or comma separated sequence of server specifications
   * of the form <i>hostname</i> or <i>hostname:port</i>.  If
   * the specs string is null, defaults to localhost:defaultPort.
   *
   * @param specs   server specs (see description)
   * @param defaultPort the default port if not specified
   * @return a list of InetSocketAddress objects.
   */
  public static List<InetSocketAddress> parse(String specs, int defaultPort) {
    List<InetSocketAddress> result = new ArrayList<InetSocketAddress>(1);
    if (specs == null) {
      result.add(new InetSocketAddress("localhost", defaultPort));
    }
    else {
      String[] specStrings = specs.split("[ ,]+");
      for (String specString : specStrings) {
        int colon = specString.indexOf(':');
        if (colon < 0 || colon == specString.length() - 1) {
          result.add(new InetSocketAddress(specString, defaultPort));
        } else {
          String hostname = specString.substring(0, colon);
          int port = Integer.parseInt(specString.substring(colon+1));
          result.add(new InetSocketAddress(hostname, port));
        }
      }
    }
    return result;
  }

}
