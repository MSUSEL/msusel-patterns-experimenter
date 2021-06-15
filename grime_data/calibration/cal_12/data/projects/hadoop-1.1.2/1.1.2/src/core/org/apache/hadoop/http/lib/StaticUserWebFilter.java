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
package org.apache.hadoop.http.lib;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.http.FilterContainer;
import org.apache.hadoop.http.FilterInitializer;

import javax.servlet.Filter;

/**
 * Provides a servlet filter that pretends to authenticate a fake user (Dr.Who)
 * so that the web UI is usable for a secure cluster without authentication.
 */
public class StaticUserWebFilter extends FilterInitializer {
  private static final String WEB_USERNAME = "Dr.Who";
  private static final Principal WEB_USER = new User(WEB_USERNAME);

  static class User implements Principal {
    private final String name;
    public User(String name) {
      this.name = name;
    }
    @Override
    public String getName() {
      return name;
    }
    @Override
    public int hashCode() {
      return name.hashCode();
    }
    @Override
    public boolean equals(Object other) {
      if (other == this) {
        return true;
      } else if (other == null || other.getClass() != getClass()) {
        return false;
      }
      return ((User) other).name.equals(name);
    }
    @Override
    public String toString() {
      return name;
    }    
  }

  public static class StaticUserFilter implements Filter {

    @Override
    public void destroy() {
      // NOTHING
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain
                         ) throws IOException, ServletException {
      HttpServletRequest httpRequest = (HttpServletRequest) request;
      // if the user is already authenticated, don't override it
      if (httpRequest.getRemoteUser() != null) {
        chain.doFilter(request, response);
      } else {
        HttpServletRequestWrapper wrapper = 
            new HttpServletRequestWrapper(httpRequest) {
          @Override
          public Principal getUserPrincipal() {
            return WEB_USER;
          }
          @Override
          public String getRemoteUser() {
            return WEB_USERNAME;
          }
        };
        chain.doFilter(wrapper, response);
      }
    }

    @Override
    public void init(FilterConfig conf) throws ServletException {
      // NOTHING
    }
    
  }

  @Override
  public void initFilter(FilterContainer container, Configuration conf) {
    container.addFilter("static_user_filter", StaticUserFilter.class.getName(), 
                        new HashMap<String,String>());
  }
}
