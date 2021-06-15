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
package org.apache.hadoop.security.authentication.server;

import org.apache.hadoop.security.authentication.client.AuthenticationException;
import junit.framework.TestCase;

public class TestAuthenticationToken extends TestCase {

  public void testAnonymous() {
    assertNotNull(AuthenticationToken.ANONYMOUS);
    assertEquals(null, AuthenticationToken.ANONYMOUS.getUserName());
    assertEquals(null, AuthenticationToken.ANONYMOUS.getName());
    assertEquals(null, AuthenticationToken.ANONYMOUS.getType());
    assertEquals(-1, AuthenticationToken.ANONYMOUS.getExpires());
    assertFalse(AuthenticationToken.ANONYMOUS.isExpired());
  }

  public void testConstructor() throws Exception {
    try {
      new AuthenticationToken(null, "p", "t");
      fail();
    } catch (IllegalArgumentException ex) {
      // Expected
    } catch (Throwable ex) {
      fail();
    }
    try {
      new AuthenticationToken("", "p", "t");
      fail();
    } catch (IllegalArgumentException ex) {
      // Expected
    } catch (Throwable ex) {
      fail();
    }
    try {
      new AuthenticationToken("u", null, "t");
      fail();
    } catch (IllegalArgumentException ex) {
      // Expected
    } catch (Throwable ex) {
      fail();
    }
    try {
      new AuthenticationToken("u", "", "t");
      fail();
    } catch (IllegalArgumentException ex) {
      // Expected
    } catch (Throwable ex) {
      fail();
    }
    try {
      new AuthenticationToken("u", "p", null);
      fail();
    } catch (IllegalArgumentException ex) {
      // Expected
    } catch (Throwable ex) {
      fail();
    }
    try {
      new AuthenticationToken("u", "p", "");
      fail();
    } catch (IllegalArgumentException ex) {
      // Expected
    } catch (Throwable ex) {
      fail();
    }
    new AuthenticationToken("u", "p", "t");
  }

  public void testGetters() throws Exception {
    long expires = System.currentTimeMillis() + 50;
    AuthenticationToken token = new AuthenticationToken("u", "p", "t");
    token.setExpires(expires);
    assertEquals("u", token.getUserName());
    assertEquals("p", token.getName());
    assertEquals("t", token.getType());
    assertEquals(expires, token.getExpires());
    assertFalse(token.isExpired());
    Thread.sleep(51);
    assertTrue(token.isExpired());
  }

  public void testToStringAndParse() throws Exception {
    long expires = System.currentTimeMillis() + 50;
    AuthenticationToken token = new AuthenticationToken("u", "p", "t");
    token.setExpires(expires);
    String str = token.toString();
    token = AuthenticationToken.parse(str);
    assertEquals("p", token.getName());
    assertEquals("t", token.getType());
    assertEquals(expires, token.getExpires());
    assertFalse(token.isExpired());
    Thread.sleep(51);
    assertTrue(token.isExpired());
  }

  public void testParseInvalid() throws Exception {
    long expires = System.currentTimeMillis() + 50;
    AuthenticationToken token = new AuthenticationToken("u", "p", "t");
    token.setExpires(expires);
    String str = token.toString();
    str = str.substring(0, str.indexOf("e="));
    try {
      AuthenticationToken.parse(str);
      fail();
    } catch (AuthenticationException ex) {
      // Expected
    } catch (Exception ex) {
      fail();
    }
  }
}
