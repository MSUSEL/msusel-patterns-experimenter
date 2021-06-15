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
package org.apache.hadoop.hdfs.server.namenode;

import java.io.IOException;
import java.io.PrintStream;
import java.security.PrivilegedExceptionAction;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.security.token.delegation.DelegationTokenIdentifier;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.security.token.Token;

/**
 * Renew delegation tokens over http for use in hftp.
 */
@SuppressWarnings("serial")
public class RenewDelegationTokenServlet extends DfsServlet {
  private static final Log LOG = LogFactory.getLog(RenewDelegationTokenServlet.class);
  public static final String PATH_SPEC = "/renewDelegationToken";
  public static final String TOKEN = "token";
  
  @Override
  protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    final UserGroupInformation ugi;
    final ServletContext context = getServletContext();
    final Configuration conf = 
      (Configuration) context.getAttribute(JspHelper.CURRENT_CONF);
    try {
      ugi = getUGI(req, conf);
    } catch(IOException ioe) {
      LOG.info("Request for token received with no authentication from "
          + req.getRemoteAddr(), ioe);
      resp.sendError(HttpServletResponse.SC_FORBIDDEN, 
          "Unable to identify or authenticate user");
      return;
    }
    final NameNode nn = (NameNode) context.getAttribute("name.node");
    String tokenString = req.getParameter(TOKEN);
    if (tokenString == null) {
      resp.sendError(HttpServletResponse.SC_MULTIPLE_CHOICES,
                     "Token to renew not specified");
    }
    final Token<DelegationTokenIdentifier> token = 
      new Token<DelegationTokenIdentifier>();
    token.decodeFromUrlString(tokenString);
    
    try {
      long result = ugi.doAs(new PrivilegedExceptionAction<Long>() {
        public Long run() throws Exception {
          return nn.renewDelegationToken(token);
        }
      });
      PrintStream os = new PrintStream(resp.getOutputStream());
      os.println(result);
      os.close();
    } catch(Exception e) {
      // transfer exception over the http
      String exceptionClass = e.getClass().getCanonicalName();
      String exceptionMsg = e.getLocalizedMessage();
      String strException = exceptionClass + ";" + exceptionMsg;
      LOG.info("Exception while renewing token. Re-throwing. s=" + strException, e);
      
      resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                     strException);
    }
  }
}
