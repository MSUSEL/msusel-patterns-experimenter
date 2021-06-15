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
package org.apache.hadoop.jmx;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.http.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestJMXJsonServlet {
  private static final Log LOG = LogFactory.getLog(TestJMXJsonServlet.class);
  private static HttpServer server;
  private static URL baseUrl;
  
  private String readOutput(URL url) throws IOException {
    StringBuilder out = new StringBuilder();
    InputStream in = url.openConnection().getInputStream();
    byte[] buffer = new byte[64 * 1024];
    int len = in.read(buffer);
    while (len > 0) {
      out.append(new String(buffer, 0, len));
      len = in.read(buffer);
    }
    return out.toString();
  }
  
  @BeforeClass public static void setup() throws Exception {
    new File(System.getProperty("build.webapps", "build/webapps") + "/test"
             ).mkdirs();
    server = new HttpServer("test", "0.0.0.0", 0, true);
    server.start();
    int port = server.getPort();
    baseUrl = new URL("http://localhost:" + port + "/");
  }
  
  @AfterClass public static void cleanup() throws Exception {
    server.stop();
  }
  
  public static void assertReFind(String re, String value) {
    Pattern p = Pattern.compile(re);
    Matcher m = p.matcher(value);
    assertTrue("'"+p+"' does not match "+value, m.find());
  }
  
  @Test public void testQury() throws Exception {
    String result = readOutput(new URL(baseUrl, "/jmx?qry=java.lang:type=Runtime"));
    LOG.info("/jmx?qry=java.lang:type=Runtime RESULT: "+result);
    assertReFind("\"name\"\\s*:\\s*\"java.lang:type=Runtime\"", result);
    assertReFind("\"modelerType\"", result);
    
    result = readOutput(new URL(baseUrl, "/jmx?qry=java.lang:type=Memory"));
    LOG.info("/jmx?qry=java.lang:type=Memory RESULT: "+result);
    assertReFind("\"name\"\\s*:\\s*\"java.lang:type=Memory\"", result);
    assertReFind("\"modelerType\"", result);
    
    result = readOutput(new URL(baseUrl, "/jmx"));
    LOG.info("/jmx RESULT: "+result);
    assertReFind("\"name\"\\s*:\\s*\"java.lang:type=Memory\"", result);
  }
}
