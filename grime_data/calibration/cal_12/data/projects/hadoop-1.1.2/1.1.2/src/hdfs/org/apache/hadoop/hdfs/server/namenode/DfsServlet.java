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
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.protocol.ClientProtocol;
import org.apache.hadoop.hdfs.protocol.DatanodeID;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.ipc.RemoteException;
import org.apache.hadoop.security.UserGroupInformation;
import org.znerd.xmlenc.XMLOutputter;

/**
 * A base class for the servlets in DFS.
 */
abstract class DfsServlet extends HttpServlet {
  /** For java.io.Serializable */
  private static final long serialVersionUID = 1L;

  static final Log LOG = LogFactory.getLog(DfsServlet.class.getCanonicalName());

  /** Write the object to XML format */
  protected void writeXml(Exception except,
                          String path, XMLOutputter doc) throws IOException {
    doc.startTag(RemoteException.class.getSimpleName());
    doc.attribute("path", path);
    if (except instanceof RemoteException) {
      doc.attribute("class", ((RemoteException) except).getClassName());
    } else {
      doc.attribute("class", except.getClass().getName());      
    }
    String msg = except.getLocalizedMessage();
    int i = msg.indexOf("\n");
    if (i >= 0) {
      msg = msg.substring(0, i);
    }
    doc.attribute("message", msg.substring(msg.indexOf(":") + 1).trim());
    doc.endTag();
  }

  /** Get {@link UserGroupInformation} from request 
   *    * @throws IOException */
  protected UserGroupInformation getUGI(HttpServletRequest request,
                                        Configuration conf
					) throws IOException {
    return JspHelper.getUGI(getServletContext(), request, conf);
  }

  /**
   * Create a {@link NameNode} proxy from the current {@link ServletContext}. 
   */
  protected ClientProtocol createNameNodeProxy() throws IOException {
    ServletContext context = getServletContext();
    // if we are running in the Name Node, use it directly rather than via 
    // rpc
    NameNode nn = (NameNode) context.getAttribute("name.node");
    if (nn != null) {
      return nn;
    }
    InetSocketAddress nnAddr = (InetSocketAddress)context.getAttribute("name.node.address");
    Configuration conf = new Configuration(
        (Configuration)context.getAttribute(JspHelper.CURRENT_CONF));
    return DFSClient.createNamenode(nnAddr, conf);
  }

  /** Create a URI for redirecting request */
  protected URI createRedirectUri(
      String servletpath, UserGroupInformation ugi,
      DatanodeID host, HttpServletRequest request, 
      String tokenString)  throws URISyntaxException {
    final String hostname = host instanceof DatanodeInfo?
        ((DatanodeInfo)host).getHostName(): host.getHost();
    final String scheme = request.getScheme();
    final int port = "https".equals(scheme)?
        (Integer)getServletContext().getAttribute("datanode.https.port")
        : host.getInfoPort();
    final String filename = request.getPathInfo();
    String dt="";
    if(tokenString!=null) {
      dt = JspHelper.getDelegationTokenUrlParam(tokenString);
    }
    return new URI(scheme, null, hostname, port, servletpath,
        "filename=" + filename + "&ugi=" + ugi.getShortUserName() + dt, null);
  }

  /** Get filename from the request */
  protected String getFilename(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    final String filename = request.getParameter("filename");
    if (filename == null || filename.length() == 0) {
      throw new IOException("Invalid filename");
    }
    return filename;
  }
}
