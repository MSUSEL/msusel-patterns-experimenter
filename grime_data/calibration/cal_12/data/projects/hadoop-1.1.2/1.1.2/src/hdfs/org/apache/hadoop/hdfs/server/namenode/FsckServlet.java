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
import java.io.PrintWriter;
import java.net.InetAddress;
import java.security.PrivilegedExceptionAction;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.protocol.FSConstants.DatanodeReportType;
import org.apache.hadoop.security.UserGroupInformation;

/**
 * This class is used in Namesystem's web server to do fsck on namenode.
 */
public class FsckServlet extends DfsServlet {
  /** for java.io.Serializable */
  private static final long serialVersionUID = 1L;

  /** Handle fsck request */
  public void doGet(HttpServletRequest request, HttpServletResponse response
      ) throws IOException {
    @SuppressWarnings("unchecked")
    final Map<String,String[]> pmap = request.getParameterMap();
    final PrintWriter out = response.getWriter();
    final InetAddress remoteAddress = 
      InetAddress.getByName(request.getRemoteAddr());
    final ServletContext context = getServletContext();
    final Configuration conf = 
      (Configuration) context.getAttribute(JspHelper.CURRENT_CONF);
    final UserGroupInformation ugi = getUGI(request, conf);
    try {
      ugi.doAs(new PrivilegedExceptionAction<Object>() {
        @Override
        public Object run() throws Exception {
          final NameNode nn = (NameNode) context.getAttribute("name.node");
          final int totalDatanodes = nn.namesystem.getNumberOfDatanodes(DatanodeReportType.LIVE); 
          final short minReplication = nn.namesystem.getMinReplication();

          new NamenodeFsck(conf, nn, nn.getNetworkTopology(), pmap, out,
              totalDatanodes, minReplication, remoteAddress).fsck();
                    return null;
          }
      });
    } catch (InterruptedException e) {
        response.sendError(400, e.getMessage());
    }
  }
}
