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
import java.net.URI;
import java.net.URISyntaxException;
import java.security.PrivilegedExceptionAction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.protocol.ClientProtocol;
import org.apache.hadoop.hdfs.protocol.DatanodeID;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.hdfs.protocol.HdfsFileStatus;
import org.apache.hadoop.hdfs.protocol.LocatedBlocks;
import org.apache.hadoop.security.UserGroupInformation;

/** Redirect queries about the hosted filesystem to an appropriate datanode.
 * @see org.apache.hadoop.hdfs.HftpFileSystem
 */
public class FileDataServlet extends DfsServlet {

  /** Create a redirection URI */
  protected URI createUri(String parent, HdfsFileStatus i, UserGroupInformation ugi,
      ClientProtocol nnproxy, HttpServletRequest request, String dt)
      throws IOException, URISyntaxException {
    String scheme = request.getScheme();
    final DatanodeID host = pickSrcDatanode(parent, i, nnproxy);
    final String hostname;
    if (host instanceof DatanodeInfo) {
      hostname = ((DatanodeInfo)host).getHostName();
    } else {
      hostname = host.getHost();
    }
    
    String dtParam="";
    if (dt != null) {
      dtParam = JspHelper.getDelegationTokenUrlParam(dt);
    }
    
    return new URI(scheme, null, hostname,
        "https".equals(scheme)
          ? (Integer)getServletContext().getAttribute("datanode.https.port")
          : host.getInfoPort(),
        "/streamFile" + i.getFullName(parent), 
        "ugi=" + ugi.getShortUserName() + dtParam, null);
  }

  private static JspHelper jspHelper = null;

  /** Select a datanode to service this request.
   * Currently, this looks at no more than the first five blocks of a file,
   * selecting a datanode randomly from the most represented.
   */
  private static DatanodeID pickSrcDatanode(String parent, HdfsFileStatus i,
      ClientProtocol nnproxy) throws IOException {
    // a race condition can happen by initializing a static member this way.
    // A proper fix should make JspHelper a singleton. Since it doesn't affect 
    // correctness, we leave it as is for now.
    if (jspHelper == null)
      jspHelper = new JspHelper();
    final LocatedBlocks blks = nnproxy.getBlockLocations(
        i.getFullPath(new Path(parent)).toUri().getPath(), 0, 1);
    if (i.getLen() == 0 || blks.getLocatedBlocks().size() <= 0) {
      // pick a random datanode
      return jspHelper.randomNode();
    }
    return JspHelper.bestNode(blks.get(0));
  }

  /**
   * Service a GET request as described below.
   * Request:
   * {@code
   * GET http://<nn>:<port>/data[/<path>] HTTP/1.1
   * }
   */
  public void doGet(final HttpServletRequest request,
                    final HttpServletResponse response)
    throws IOException {
    Configuration conf =
	(Configuration) getServletContext().getAttribute(JspHelper.CURRENT_CONF);
    final UserGroupInformation ugi = getUGI(request, conf);

    try {
      ugi.doAs(new PrivilegedExceptionAction<Void>() {
            @Override
            public Void run() throws IOException {
              ClientProtocol nn = createNameNodeProxy();
              final String path = 
                request.getPathInfo() != null ? request.getPathInfo() : "/";
              
              String delegationToken = 
                request.getParameter(JspHelper.DELEGATION_PARAMETER_NAME);
              
              HdfsFileStatus info = nn.getFileInfo(path);
              if ((info != null) && !info.isDir()) {
                try {
                  response.sendRedirect(createUri(path, info, ugi, nn,
                        request, delegationToken).toURL().toString());
                } catch (URISyntaxException e) {
                  response.getWriter().println(e.toString());
                }
              } else if (info == null){
                response.sendError(400, "File not found " + path);
              } else {
                response.sendError(400, path + " is a directory");
              }
              return null;
            }
          });

    } catch (IOException e) {
      response.sendError(400, e.getMessage());
    } catch (InterruptedException e) {
      response.sendError(400, e.getMessage());
    }
  }

}

