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
package org.apache.hadoop.security;

import org.apache.hadoop.security.authentication.server.AuthenticationFilter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.http.FilterContainer;
import org.apache.hadoop.http.FilterInitializer;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * Initializes Alfredo AuthenticationFilter which provides support for
 * Kerberos HTTP SPENGO authentication.
 * <p/>
 * It enables anonymous access, simple/speudo and Kerberos HTTP SPNEGO
 * authentication  for Hadoop JobTracker, NameNode, DataNodes and
 * TaskTrackers.
 * <p/>
 * Refer to the <code>core-default.xml</code> file, after the comment
 * 'HTTP Authentication' for details on the configuration options.
 * All related configuration properties have 'hadoop.http.authentication.'
 * as prefix.
 */
public class AuthenticationFilterInitializer extends FilterInitializer {

  static final String PREFIX = "hadoop.http.authentication.";

  static final String SIGNATURE_SECRET_FILE = AuthenticationFilter.SIGNATURE_SECRET + ".file";

  /**
   * Initializes Alfredo AuthenticationFilter.
   * <p/>
   * Propagates to Alfredo AuthenticationFilter configuration all Hadoop
   * configuration properties prefixed with "hadoop.http.authentication."
   *
   * @param container The filter container
   * @param conf Configuration for run-time parameters
   */
  @Override
  public void initFilter(FilterContainer container, Configuration conf) {
    Map<String, String> filterConfig = new HashMap<String, String>();

    //setting the cookie path to root '/' so it is used for all resources.
    filterConfig.put(AuthenticationFilter.COOKIE_PATH, "/");

    for (Map.Entry<String, String> entry : conf) {
      String name = entry.getKey();
      if (name.startsWith(PREFIX)) {
        String value = conf.get(name);
        name = name.substring(PREFIX.length());
        filterConfig.put(name, value);
      }
    }

    String signatureSecretFile = filterConfig.get(SIGNATURE_SECRET_FILE);
    if (signatureSecretFile == null) {
      throw new RuntimeException("Undefined property: " + SIGNATURE_SECRET_FILE);
    }

    try {
      StringBuilder secret = new StringBuilder();
      Reader reader = new FileReader(signatureSecretFile);
      int c = reader.read();
      while (c > -1) {
        secret.append((char)c);
        c = reader.read();
      }
      reader.close();
      filterConfig.put(AuthenticationFilter.SIGNATURE_SECRET, secret.toString());
    } catch (IOException ex) {
      throw new RuntimeException("Could not read HTTP signature secret file: " + signatureSecretFile);
    }

    container.addFilter("authentication",
                        AuthenticationFilter.class.getName(),
                        filterConfig);
  }

}
