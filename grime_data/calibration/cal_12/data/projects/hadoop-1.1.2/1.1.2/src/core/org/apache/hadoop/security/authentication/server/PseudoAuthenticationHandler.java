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
import org.apache.hadoop.security.authentication.client.PseudoAuthenticator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

/**
 * The <code>PseudoAuthenticationHandler</code> provides a pseudo authentication mechanism that accepts
 * the user name specified as a query string parameter.
 * <p/>
 * This mimics the model of Hadoop Simple authentication which trust the 'user.name' property provided in
 * the configuration object.
 * <p/>
 * This handler can be configured to support anonymous users.
 * <p/>
 * The only supported configuration property is:
 * <ul>
 * <li>simple.anonymous.allowed: <code>true|false</code>, default value is <code>false</code></li>
 * </ul>
 */
public class PseudoAuthenticationHandler implements AuthenticationHandler {

  /**
   * Constant that identifies the authentication mechanism.
   */
  public static final String TYPE = "simple";

  /**
   * Constant for the configuration property that indicates if anonymous users are allowed.
   */
  public static final String ANONYMOUS_ALLOWED = TYPE + ".anonymous.allowed";

  private boolean acceptAnonymous;

  /**
   * Initializes the authentication handler instance.
   * <p/>
   * This method is invoked by the {@link AuthenticationFilter#init} method.
   *
   * @param config configuration properties to initialize the handler.
   *
   * @throws ServletException thrown if the handler could not be initialized.
   */
  @Override
  public void init(Properties config) throws ServletException {
    acceptAnonymous = Boolean.parseBoolean(config.getProperty(ANONYMOUS_ALLOWED, "false"));
  }

  /**
   * Returns if the handler is configured to support anonymous users.
   *
   * @return if the handler is configured to support anonymous users.
   */
  protected boolean getAcceptAnonymous() {
    return acceptAnonymous;
  }

  /**
   * Releases any resources initialized by the authentication handler.
   * <p/>
   * This implementation does a NOP.
   */
  @Override
  public void destroy() {
  }

  /**
   * Returns the authentication type of the authentication handler, 'simple'.
   * <p/>
   *
   * @return the authentication type of the authentication handler, 'simple'.
   */
  @Override
  public String getType() {
    return TYPE;
  }

  /**
   * Authenticates an HTTP client request.
   * <p/>
   * It extracts the {@link PseudoAuthenticator#USER_NAME} parameter from the query string and creates
   * an {@link AuthenticationToken} with it.
   * <p/>
   * If the HTTP client request does not contain the {@link PseudoAuthenticator#USER_NAME} parameter and
   * the handler is configured to allow anonymous users it returns the {@link AuthenticationToken#ANONYMOUS}
   * token.
   * <p/>
   * If the HTTP client request does not contain the {@link PseudoAuthenticator#USER_NAME} parameter and
   * the handler is configured to disallow anonymous users it throws an {@link AuthenticationException}.
   *
   * @param request the HTTP client request.
   * @param response the HTTP client response.
   *
   * @return an authentication token if the HTTP client request is accepted and credentials are valid.
   *
   * @throws IOException thrown if an IO error occurred.
   * @throws AuthenticationException thrown if HTTP client request was not accepted as an authentication request.
   */
  @Override
  public AuthenticationToken authenticate(HttpServletRequest request, HttpServletResponse response)
    throws IOException, AuthenticationException {
    AuthenticationToken token;
    String userName = request.getParameter(PseudoAuthenticator.USER_NAME);
    if (userName == null) {
      if (getAcceptAnonymous()) {
        token = AuthenticationToken.ANONYMOUS;
      } else {
        throw new AuthenticationException("Anonymous requests are disallowed");
      }
    } else {
      token = new AuthenticationToken(userName, userName, TYPE);
    }
    return token;
  }

}
