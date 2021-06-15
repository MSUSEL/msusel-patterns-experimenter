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
/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. See accompanying LICENSE file.
 */
package org.apache.hadoop.security.authentication.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Properties;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.security.authentication.server.AuthenticationFilter;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.FilterHolder;
import org.mortbay.jetty.servlet.ServletHolder;

public abstract class AuthenticatorTestCase {
  private Server server;
  private String host = null;
  private int port = -1;
  Context context;

  private static Properties authenticatorConfig;

  protected static void setAuthenticationHandlerConfig(Properties config) {
    authenticatorConfig = config;
  }

  public static class TestFilter extends AuthenticationFilter {

    @Override
    protected Properties getConfiguration(String configPrefix, FilterConfig filterConfig) throws ServletException {
      return authenticatorConfig;
    }
  }

  @SuppressWarnings("serial")
  public static class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      InputStream is = req.getInputStream();
      OutputStream os = resp.getOutputStream();
      int c = is.read();
      while (c > -1) {
        os.write(c);
        c = is.read();
      }
      is.close();
      os.close();
      resp.setStatus(HttpServletResponse.SC_OK);
    }
  }

  protected void start() throws Exception {
    server = new Server(0);
    context = new Context();
    context.setContextPath("/foo");
    server.setHandler(context);
    context.addFilter(new FilterHolder(TestFilter.class), "/*", 0);
    context.addServlet(new ServletHolder(TestServlet.class), "/bar");
    host = "localhost";
    ServerSocket ss = new ServerSocket(0);
    port = ss.getLocalPort();
    ss.close();
    server.getConnectors()[0].setHost(host);
    server.getConnectors()[0].setPort(port);
    server.start();
    System.out.println("Running embedded servlet container at: http://" + host + ":" + port);
  }

  protected void stop() throws Exception {
    try {
      server.stop();
    } catch (Exception e) {
    }

    try {
      server.destroy();
    } catch (Exception e) {
    }
  }

  protected String getBaseURL() {
    return "http://" + host + ":" + port + "/foo/bar";
  }

  private String POST = "test";

  protected void _testAuthentication(Authenticator authenticator, boolean doPost) throws Exception {
    start();
    try {
      URL url = new URL(getBaseURL());
      AuthenticatedURL.Token token = new AuthenticatedURL.Token();
      AuthenticatedURL aUrl = new AuthenticatedURL(authenticator);
      HttpURLConnection conn = aUrl.openConnection(url, token);
      String tokenStr = token.toString();
      if (doPost) {
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
      }
      conn.connect();
      if (doPost) {
        Writer writer = new OutputStreamWriter(conn.getOutputStream());
        writer.write(POST);
        writer.close();
      }
      assertEquals(HttpURLConnection.HTTP_OK, conn.getResponseCode());
      if (doPost) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String echo = reader.readLine();
        assertEquals(POST, echo);
        assertNull(reader.readLine());
      }
      aUrl = new AuthenticatedURL();
      conn = aUrl.openConnection(url, token);
      conn.connect();
      assertEquals(HttpURLConnection.HTTP_OK, conn.getResponseCode());
      assertEquals(tokenStr, token.toString());
    } finally {
      stop();
    }
  }

}
