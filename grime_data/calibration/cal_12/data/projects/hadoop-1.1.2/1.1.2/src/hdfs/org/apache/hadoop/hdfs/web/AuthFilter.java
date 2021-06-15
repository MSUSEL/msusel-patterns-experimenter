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
package org.apache.hadoop.hdfs.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.hadoop.hdfs.web.resources.DelegationParam;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.security.authentication.server.AuthenticationFilter;
import org.apache.hadoop.security.authentication.server.KerberosAuthenticationHandler;
import org.apache.hadoop.security.authentication.server.PseudoAuthenticationHandler;

/**
 * Subclass of {@link AuthenticationFilter} that
 * obtains Hadoop-Auth configuration for webhdfs.
 */
public class AuthFilter extends AuthenticationFilter {
  private static final String CONF_PREFIX = "dfs.web.authentication.";

  /**
   * Returns the filter configuration properties,
   * including the ones prefixed with {@link #CONF_PREFIX}.
   * The prefix is removed from the returned property names.
   *
   * @param prefix parameter not used.
   * @param config parameter contains the initialization values.
   * @return Hadoop-Auth configuration properties.
   * @throws ServletException 
   */
  @Override
  protected Properties getConfiguration(String prefix, FilterConfig config)
      throws ServletException {
    final Properties p = super.getConfiguration(CONF_PREFIX, config);
    // set authentication type
    p.setProperty(AUTH_TYPE, UserGroupInformation.isSecurityEnabled()?
        KerberosAuthenticationHandler.TYPE: PseudoAuthenticationHandler.TYPE);
    //For Pseudo Authentication, allow anonymous.
    p.setProperty(PseudoAuthenticationHandler.ANONYMOUS_ALLOWED, "true");
    //set cookie path
    p.setProperty(COOKIE_PATH, "/");
    return p;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {
    final HttpServletRequest httpRequest = toLowerCase((HttpServletRequest)request);
    final String tokenString = httpRequest.getParameter(DelegationParam.NAME);
    if (tokenString != null) {
      //Token is present in the url, therefore token will be used for
      //authentication, bypass kerberos authentication.
      filterChain.doFilter(httpRequest, response);
      return;
    }
    super.doFilter(httpRequest, response, filterChain);
  }

  private static HttpServletRequest toLowerCase(final HttpServletRequest request) {
    @SuppressWarnings("unchecked")
    final Map<String, String[]> original = (Map<String, String[]>)request.getParameterMap();
    if (!ParamFilter.containsUpperCase(original.keySet())) {
      return request;
    }

    final Map<String, List<String>> m = new HashMap<String, List<String>>();
    for(Map.Entry<String, String[]> entry : original.entrySet()) {
      final String key = entry.getKey().toLowerCase();
      List<String> strings = m.get(key);
      if (strings == null) {
        strings = new ArrayList<String>();
        m.put(key, strings);
      }
      for(String v : entry.getValue()) {
        strings.add(v);
      }
    }

    return new HttpServletRequestWrapper(request) {
      private Map<String, String[]> parameters = null;

      @Override
      public Map<String, String[]> getParameterMap() {
        if (parameters == null) {
          parameters = new HashMap<String, String[]>();
          for(Map.Entry<String, List<String>> entry : m.entrySet()) {
            final List<String> a = entry.getValue();
            parameters.put(entry.getKey(), a.toArray(new String[a.size()]));
          }
        }
       return parameters;
      }

      @Override
      public String getParameter(String name) {
        final List<String> a = m.get(name);
        return a == null? null: a.get(0);
      }
      
      @Override
      public String[] getParameterValues(String name) {
        return getParameterMap().get(name);
      }

      @Override
      public Enumeration<String> getParameterNames() {
        final Iterator<String> i = m.keySet().iterator();
        return new Enumeration<String>() {
          @Override
          public boolean hasMoreElements() {
            return i.hasNext();
          }
          @Override
          public String nextElement() {
            return i.next();
          }
        };
      }
    };
  }
}