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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthorizationFilter implements Filter {
  public static final Log LOG = LogFactory.getLog(AuthorizationFilter.class);

  private static final Pattern HDFS_PATH_PATTERN = Pattern
      .compile("(^hdfs://([\\w\\-]+(\\.)?)+:\\d+|^hdfs://([\\w\\-]+(\\.)?)+)");

  /** Pattern for a filter to find out if a request is HFTP/HSFTP request */
  protected static final Pattern HFTP_PATTERN = Pattern
      .compile("^(/listPaths|/data|/streamFile|/file)$");

  protected String contextPath;

  protected String namenode;

  /** {@inheritDoc} **/
  public void init(FilterConfig filterConfig) throws ServletException {
    contextPath = filterConfig.getServletContext().getContextPath();
    Configuration conf = new Configuration(false);
    conf.addResource("hdfsproxy-default.xml");
    conf.addResource("hdfsproxy-site.xml");
    namenode = conf.get("fs.default.name");
  }

  /** {@inheritDoc} **/
  @SuppressWarnings("unchecked")
  public void doFilter(ServletRequest request,
                       ServletResponse response,
                       FilterChain chain)
      throws IOException, ServletException {

    HttpServletResponse rsp = (HttpServletResponse) response;
    HttpServletRequest rqst = (HttpServletRequest) request;

    String userId = getUserId(request);
    String groups = getGroups(request);
    List<Path> allowedPaths = getAllowedPaths(request);

    UserGroupInformation ugi =
        UserGroupInformation.createRemoteUser(userId);

    String filePath = getPathFromRequest(rqst);

    if (filePath == null || !checkHdfsPath(filePath, allowedPaths)) {
      String msg = "User " + userId + " (" + groups
          + ") is not authorized to access path " + filePath;
      LOG.warn(msg);
      rsp.sendError(HttpServletResponse.SC_FORBIDDEN, msg);
      return;
    }
    request.setAttribute("authorized.ugi", ugi);

    chain.doFilter(request, response);
  }

  protected String getUserId(ServletRequest request) {
     return (String)request.
         getAttribute("org.apache.hadoop.hdfsproxy.authorized.userID");
  }

   protected String getGroups(ServletRequest request) {
     UserGroupInformation ugi = UserGroupInformation.
         createRemoteUser(getUserId(request));
     return Arrays.toString(ugi.getGroupNames());
   }

  protected List<Path> getAllowedPaths(ServletRequest request) {
     return (List<Path>)request.
         getAttribute("org.apache.hadoop.hdfsproxy.authorized.paths");
  }
  
   private String getPathFromRequest(HttpServletRequest rqst) {
    String filePath = null;
    // check request path
    String servletPath = rqst.getServletPath();
    if (HFTP_PATTERN.matcher(servletPath).matches()) {
        // file path as part of the URL
        filePath = rqst.getPathInfo() != null ? rqst.getPathInfo() : "/";
    }
    return filePath;
  }

  /** check that the requested path is listed in the ldap entry
   * @param pathInfo - Path to check access
   * @param ldapPaths - List of paths allowed access
   * @return true if access allowed, false otherwise */
  public boolean checkHdfsPath(String pathInfo,
                               List<Path> ldapPaths) {
    if (pathInfo == null || pathInfo.length() == 0) {
      LOG.info("Can't get file path from the request");
      return false;
    }
    for (Path ldapPathVar : ldapPaths) {
      String ldapPath = ldapPathVar.toString();
      if (isPathQualified(ldapPath) &&
          isPathAuthroized(ldapPath)) {
        String allowedPath = extractPath(ldapPath);
        if (pathInfo.startsWith(allowedPath))
          return true;
      } else {
        if (pathInfo.startsWith(ldapPath))
          return true;
      }
    }
    return false;
  }

  private String extractPath(String ldapPath) {
    return HDFS_PATH_PATTERN.split(ldapPath)[1];
  }

  private boolean isPathAuthroized(String pathStr) {
    Matcher namenodeMatcher = HDFS_PATH_PATTERN.matcher(pathStr);
    return namenodeMatcher.find() && namenodeMatcher.group().contains(namenode);
  }

  private boolean isPathQualified(String pathStr) {
    if (pathStr == null || pathStr.trim().isEmpty()) {
      return false;
    } else {
      return HDFS_PATH_PATTERN.matcher(pathStr).find();
    }
  }

  /** {@inheritDoc} **/
  public void destroy() {
  }
}
