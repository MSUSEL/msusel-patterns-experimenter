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

import java.io.DataOutputStream;
import java.io.IOException;
import java.security.PrivilegedExceptionAction;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.security.token.delegation.DelegationTokenIdentifier;
import org.apache.hadoop.hdfs.security.token.delegation.DelegationTokenSecretManager;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.security.Credentials;
import org.apache.hadoop.security.SecurityUtil;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.security.token.Token;

/**
 * Serve delegation tokens over http for use in hftp.
 */
@SuppressWarnings("serial")
public class GetDelegationTokenServlet extends DfsServlet {
  private static final Log LOG = LogFactory.getLog(GetDelegationTokenServlet.class);
  public static final String PATH_SPEC = "/getDelegationToken";
  public static final String RENEWER = "renewer";
  
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
    LOG.info("Sending token: {" + ugi.getUserName() + "," + req.getRemoteAddr() +"}");
    final NameNode nn = (NameNode) context.getAttribute("name.node");
    String renewer = req.getParameter(RENEWER);
    final String renewerFinal = (renewer == null) ? 
        req.getUserPrincipal().getName() : renewer;
    
    DataOutputStream dos = null;
    try {
      dos = new DataOutputStream(resp.getOutputStream());
      final DataOutputStream dosFinal = dos; // for doAs block
      ugi.doAs(new PrivilegedExceptionAction<Void>() {
        @Override
        public Void run() throws IOException {
          final Credentials ts = DelegationTokenSecretManager.createCredentials(
              nn, ugi, renewerFinal);
          ts.write(dosFinal);
          dosFinal.close();
          return null;
        }
      });

    } catch(Exception e) {
      LOG.info("Exception while sending token. Re-throwing. ", e);
      resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    } finally {
      if(dos != null) dos.close();
    }
  }
}
