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
package org.apache.hadoop.hdfsproxy;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CommonConfigurationKeys;
import org.apache.hadoop.security.UserGroupInformation;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import java.io.IOException;
import java.util.Arrays;

/**
 * This filter is required for hdfsproxies connecting to HDFS
 * with kerberos authentication. Keytab file and principal to
 * use for proxy user is retrieved from a configuration file.
 * If user attribute in ldap doesn't kerberos realm, the 
 * default realm is picked up from configuration. 
 */
public class KerberosAuthorizationFilter extends AuthorizationFilter {

  private String defaultRealm;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    super.init(filterConfig);
    Configuration conf = new Configuration(false);
    conf.addResource("hdfsproxy-default.xml");
    conf.addResource("hdfsproxy-site.xml");
    initializeUGI(conf);
    initDefaultRealm(conf);
  }

  private void initializeUGI(Configuration conf) {
    try {
      conf.set(CommonConfigurationKeys.HADOOP_SECURITY_AUTHENTICATION,
          "kerberos");

      UserGroupInformation.setConfiguration(conf);
      UserGroupInformation.loginUserFromKeytab(
          conf.get("hdfsproxy.kerberos.principal"),
          conf.get("hdfsproxy.kerberos.keytab"));

      LOG.info(contextPath + " :: Logged in user: " +
          UserGroupInformation.getLoginUser().getUserName() +
          ", Current User: " + UserGroupInformation.getCurrentUser().getUserName());

    } catch (IOException e) {
      throw new RuntimeException("Unable to initialize credentials", e);
    }
  }

  private void initDefaultRealm(Configuration conf) {
    defaultRealm = conf.get("hdfsproxy.kerberos.default.realm","");
  }

  @Override
  /** If the userid does not have realm, add the default realm */
  protected String getUserId(ServletRequest request) {
    String userId =  super.getUserId(request);
    return userId +
        (userId.indexOf('@') > 0 ? "" : defaultRealm);
  }

  @Override
  protected String getGroups(ServletRequest request) {
    UserGroupInformation ugi = UserGroupInformation.
        createRemoteUser(getUserId(request));
    return Arrays.toString(ugi.getGroupNames());
  }
}
