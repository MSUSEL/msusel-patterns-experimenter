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

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;

/**
 * 
 *
 */
public class ProxyForwardServlet extends HttpServlet {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private static Configuration configuration = null;
  public static final Log LOG = LogFactory.getLog(ProxyForwardServlet.class);

  /** {@inheritDoc} */
  @Override
  public void init() throws ServletException {
    ServletContext context = getServletContext();
    configuration = (Configuration) context
        .getAttribute("org.apache.hadoop.hdfsproxy.conf");
  }

  /** {@inheritDoc} */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String hostname = request.getServerName();

    String version = configuration.get(hostname);
    if (version == null) {
      // extract from hostname directly
      String[] strs = hostname.split("[-\\.]");
      version = "/" + strs[0];
    }

    ServletContext curContext = getServletContext();
    ServletContext dstContext = curContext.getContext(version);

    // avoid infinite forwarding.
    if (dstContext == null
        || "HDFS Proxy Forward".equals(dstContext.getServletContextName())) {
      LOG.error("Context (" + version
          + ".war) non-exist or restricted from access");
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
    LOG.debug("Request to " + hostname + " is forwarded to version " + version);
    forwardRequest(request, response, dstContext, request.getServletPath());

  }

  /** {@inheritDoc} */
  public void forwardRequest(HttpServletRequest request,
      HttpServletResponse response, ServletContext context, String pathInfo)
      throws IOException, ServletException {
    String path = buildForwardPath(request, pathInfo);
    RequestDispatcher dispatcher = context.getRequestDispatcher(path);
    if (dispatcher == null) {
      LOG.info("There was no such dispatcher: " + path);
      response.sendError(HttpServletResponse.SC_NO_CONTENT);
      return;
    }
    dispatcher.forward(request, response);
  }

  /** {@inheritDoc} */
  protected String buildForwardPath(HttpServletRequest request, String pathInfo) {
    String path = pathInfo;
    if (request.getPathInfo() != null) {
      path += request.getPathInfo();
    }
    if (request.getQueryString() != null) {
      path += "?" + request.getQueryString();
    }
    return path;
  }
}
