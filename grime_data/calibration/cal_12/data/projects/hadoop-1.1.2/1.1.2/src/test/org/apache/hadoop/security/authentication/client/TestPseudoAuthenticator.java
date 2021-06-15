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
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. See accompanying LICENSE file.
 */
package org.apache.hadoop.security.authentication.client;

import static org.junit.Assert.*;

import org.apache.hadoop.security.authentication.server.AuthenticationFilter;
import org.apache.hadoop.security.authentication.server.PseudoAuthenticationHandler;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class TestPseudoAuthenticator extends AuthenticatorTestCase {

  private Properties getAuthenticationHandlerConfiguration(boolean anonymousAllowed) {
    Properties props = new Properties();
    props.setProperty(AuthenticationFilter.AUTH_TYPE, "simple");
    props.setProperty(PseudoAuthenticationHandler.ANONYMOUS_ALLOWED, Boolean.toString(anonymousAllowed));
    return props;
  }

  @Test
  public void testGetUserName() throws Exception {
    PseudoAuthenticator authenticator = new PseudoAuthenticator();
    assertEquals(System.getProperty("user.name"), authenticator.getUserName());
  }

  @Test
  public void testAnonymousAllowed() throws Exception {
    setAuthenticationHandlerConfig(getAuthenticationHandlerConfiguration(true));
    start();
    try {
      URL url = new URL(getBaseURL());
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.connect();
      assertEquals(HttpURLConnection.HTTP_OK, conn.getResponseCode());
    } finally {
      stop();
    }
  }

  @Test
  public void testAnonymousDisallowed() throws Exception {
    setAuthenticationHandlerConfig(getAuthenticationHandlerConfiguration(false));
    start();
    try {
      URL url = new URL(getBaseURL());
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.connect();
      assertEquals(HttpURLConnection.HTTP_UNAUTHORIZED, conn.getResponseCode());
    } finally {
      stop();
    }
  }

  @Test
  public void testAuthenticationAnonymousAllowed() throws Exception {
    setAuthenticationHandlerConfig(getAuthenticationHandlerConfiguration(true));
    _testAuthentication(new PseudoAuthenticator(), false);
  }

  @Test
  public void testAuthenticationAnonymousDisallowed() throws Exception {
    setAuthenticationHandlerConfig(getAuthenticationHandlerConfiguration(false));
    _testAuthentication(new PseudoAuthenticator(), false);
  }

  @Test
  public void testAuthenticationAnonymousAllowedWithPost() throws Exception {
    setAuthenticationHandlerConfig(getAuthenticationHandlerConfiguration(true));
    _testAuthentication(new PseudoAuthenticator(), true);
  }

  @Test
  public void testAuthenticationAnonymousDisallowedWithPost() throws Exception {
    setAuthenticationHandlerConfig(getAuthenticationHandlerConfiguration(false));
    _testAuthentication(new PseudoAuthenticator(), true);
  }

}
