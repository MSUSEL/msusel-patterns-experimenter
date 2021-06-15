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

import junit.framework.TestCase;
import org.mockito.Mockito;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestAuthenticatedURL extends TestCase {

  public void testToken() throws Exception {
    AuthenticatedURL.Token token = new AuthenticatedURL.Token();
    assertFalse(token.isSet());
    token = new AuthenticatedURL.Token("foo");
    assertTrue(token.isSet());
    assertEquals("foo", token.toString());

    AuthenticatedURL.Token token1 = new AuthenticatedURL.Token();
    AuthenticatedURL.Token token2 = new AuthenticatedURL.Token();
    assertEquals(token1.hashCode(), token2.hashCode());
    assertTrue(token1.equals(token2));

    token1 = new AuthenticatedURL.Token();
    token2 = new AuthenticatedURL.Token("foo");
    assertNotSame(token1.hashCode(), token2.hashCode());
    assertFalse(token1.equals(token2));

    token1 = new AuthenticatedURL.Token("foo");
    token2 = new AuthenticatedURL.Token();
    assertNotSame(token1.hashCode(), token2.hashCode());
    assertFalse(token1.equals(token2));

    token1 = new AuthenticatedURL.Token("foo");
    token2 = new AuthenticatedURL.Token("foo");
    assertEquals(token1.hashCode(), token2.hashCode());
    assertTrue(token1.equals(token2));

    token1 = new AuthenticatedURL.Token("bar");
    token2 = new AuthenticatedURL.Token("foo");
    assertNotSame(token1.hashCode(), token2.hashCode());
    assertFalse(token1.equals(token2));

    token1 = new AuthenticatedURL.Token("foo");
    token2 = new AuthenticatedURL.Token("bar");
    assertNotSame(token1.hashCode(), token2.hashCode());
    assertFalse(token1.equals(token2));
  }

  public void testInjectToken() throws Exception {
    HttpURLConnection conn = Mockito.mock(HttpURLConnection.class);
    AuthenticatedURL.Token token = new AuthenticatedURL.Token();
    token.set("foo");
    AuthenticatedURL.injectToken(conn, token);
    Mockito.verify(conn).addRequestProperty(Mockito.eq("Cookie"), Mockito.anyString());
  }

  public void testExtractTokenOK() throws Exception {
    HttpURLConnection conn = Mockito.mock(HttpURLConnection.class);

    Mockito.when(conn.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

    String tokenStr = "foo";
    Map<String, List<String>> headers = new HashMap<String, List<String>>();
    List<String> cookies = new ArrayList<String>();
    cookies.add(AuthenticatedURL.AUTH_COOKIE + "=" + tokenStr);
    headers.put("Set-Cookie", cookies);
    Mockito.when(conn.getHeaderFields()).thenReturn(headers);

    AuthenticatedURL.Token token = new AuthenticatedURL.Token();
    AuthenticatedURL.extractToken(conn, token);

    assertEquals(tokenStr, token.toString());
  }

  public void testExtractTokenFail() throws Exception {
    HttpURLConnection conn = Mockito.mock(HttpURLConnection.class);

    Mockito.when(conn.getResponseCode()).thenReturn(HttpURLConnection.HTTP_UNAUTHORIZED);

    String tokenStr = "foo";
    Map<String, List<String>> headers = new HashMap<String, List<String>>();
    List<String> cookies = new ArrayList<String>();
    cookies.add(AuthenticatedURL.AUTH_COOKIE + "=" + tokenStr);
    headers.put("Set-Cookie", cookies);
    Mockito.when(conn.getHeaderFields()).thenReturn(headers);

    try {
      AuthenticatedURL.extractToken(conn, new AuthenticatedURL.Token());
      fail();
    } catch (AuthenticationException ex) {
      // Expected
    } catch (Exception ex) {
      fail();
    }
  }

}
