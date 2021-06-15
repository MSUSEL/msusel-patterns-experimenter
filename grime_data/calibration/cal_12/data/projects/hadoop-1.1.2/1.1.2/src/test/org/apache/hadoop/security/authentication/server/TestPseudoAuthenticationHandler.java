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
import org.apache.hadoop.security.authentication.client.PseudoAuthenticator;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

public class TestPseudoAuthenticationHandler extends TestCase {

  public void testInit() throws Exception {
    PseudoAuthenticationHandler handler = new PseudoAuthenticationHandler();
    try {
      Properties props = new Properties();
      props.setProperty(PseudoAuthenticationHandler.ANONYMOUS_ALLOWED, "false");
      handler.init(props);
      assertEquals(false, handler.getAcceptAnonymous());
    } finally {
      handler.destroy();
    }
  }

  public void testType() throws Exception {
    PseudoAuthenticationHandler handler = new PseudoAuthenticationHandler();
    assertEquals(PseudoAuthenticationHandler.TYPE, handler.getType());
  }

  public void testAnonymousOn() throws Exception {
    PseudoAuthenticationHandler handler = new PseudoAuthenticationHandler();
    try {
      Properties props = new Properties();
      props.setProperty(PseudoAuthenticationHandler.ANONYMOUS_ALLOWED, "true");
      handler.init(props);

      HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
      HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

      AuthenticationToken token = handler.authenticate(request, response);

      assertEquals(AuthenticationToken.ANONYMOUS, token);
    } finally {
      handler.destroy();
    }
  }

  public void testAnonymousOff() throws Exception {
    PseudoAuthenticationHandler handler = new PseudoAuthenticationHandler();
    try {
      Properties props = new Properties();
      props.setProperty(PseudoAuthenticationHandler.ANONYMOUS_ALLOWED, "false");
      handler.init(props);

      HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
      HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

      handler.authenticate(request, response);
      fail();
    } catch (AuthenticationException ex) {
      // Expected
    } catch (Exception ex) {
      fail();
    } finally {
      handler.destroy();
    }
  }

  private void _testUserName(boolean anonymous) throws Exception {
    PseudoAuthenticationHandler handler = new PseudoAuthenticationHandler();
    try {
      Properties props = new Properties();
      props.setProperty(PseudoAuthenticationHandler.ANONYMOUS_ALLOWED, Boolean.toString(anonymous));
      handler.init(props);

      HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
      HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
      Mockito.when(request.getParameter(PseudoAuthenticator.USER_NAME)).thenReturn("user");

      AuthenticationToken token = handler.authenticate(request, response);

      assertNotNull(token);
      assertEquals("user", token.getUserName());
      assertEquals("user", token.getName());
      assertEquals(PseudoAuthenticationHandler.TYPE, token.getType());
    } finally {
      handler.destroy();
    }
  }

  public void testUserNameAnonymousOff() throws Exception {
    _testUserName(false);
  }

  public void testUserNameAnonymousOn() throws Exception {
    _testUserName(true);
  }

}
