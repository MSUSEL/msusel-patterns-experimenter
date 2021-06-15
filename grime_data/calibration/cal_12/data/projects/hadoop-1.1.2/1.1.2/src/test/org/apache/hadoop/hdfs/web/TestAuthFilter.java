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

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.hadoop.hdfs.DFSConfigKeys;
import org.apache.hadoop.security.authentication.server.PseudoAuthenticationHandler;
import org.junit.Assert;
import org.junit.Test;

public class TestAuthFilter {
  
  private static class DummyFilterConfig implements FilterConfig {
    final Map<String, String> map;
    
    DummyFilterConfig(Map<String,String> map) {
      this.map = map;
    }
    
    @Override
    public String getFilterName() {
      return "dummy";
    }
    @Override
    public String getInitParameter(String arg0) {
      return map.get(arg0);
    }
    @Override
    public Enumeration<String> getInitParameterNames() {
      return Collections.enumeration(map.keySet());
    }
    @Override
    public ServletContext getServletContext() {
      return null;
    }
  }
  
  @Test
  public void testGetConfiguration() throws ServletException {
    AuthFilter filter = new AuthFilter();
    Map<String, String> m = new HashMap<String,String>();
    m.put(DFSConfigKeys.DFS_WEB_AUTHENTICATION_KERBEROS_PRINCIPAL_KEY,
        "xyz/thehost@REALM");
    m.put(DFSConfigKeys.DFS_WEB_AUTHENTICATION_KERBEROS_KEYTAB_KEY,
        "thekeytab");
    FilterConfig config = new DummyFilterConfig(m);
    Properties p = filter.getConfiguration("random", config);
    Assert.assertEquals("xyz/thehost@REALM",
        p.getProperty("kerberos.principal"));
    Assert.assertEquals("thekeytab", p.getProperty("kerberos.keytab"));
    Assert.assertEquals("true",
        p.getProperty(PseudoAuthenticationHandler.ANONYMOUS_ALLOWED));
  }
}
