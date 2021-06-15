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
import java.net.URI;
import java.net.URISyntaxException;
import java.security.PrivilegedExceptionAction;

import javax.net.SocketFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.MD5MD5CRC32FileChecksum;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.protocol.ClientProtocol;
import org.apache.hadoop.hdfs.protocol.DatanodeID;
import org.apache.hadoop.hdfs.protocol.FSConstants;
import org.apache.hadoop.hdfs.server.common.HdfsConstants;
import org.apache.hadoop.hdfs.server.datanode.DataNode;
import org.apache.hadoop.ipc.RemoteException;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.security.UserGroupInformation;
import org.znerd.xmlenc.XMLOutputter;

/** Servlets for file checksum */
public class FileChecksumServlets {
  /** Redirect file checksum queries to an appropriate datanode. */
  public static class RedirectServlet extends DfsServlet {
    /** For java.io.Serializable */
    private static final long serialVersionUID = 1L;
  
    /** {@inheritDoc} */
    public void doGet(HttpServletRequest request, HttpServletResponse response
        ) throws ServletException, IOException {
      final ServletContext context = getServletContext();
      Configuration conf = (Configuration) context.getAttribute(JspHelper.CURRENT_CONF);
      final UserGroupInformation ugi = getUGI(request, conf);
      String tokenString = request.getParameter(JspHelper.DELEGATION_PARAMETER_NAME);
      final NameNode namenode = (NameNode)context.getAttribute("name.node");
      final DatanodeID datanode = namenode.namesystem.getRandomDatanode();
      try {
        final URI uri = 
          createRedirectUri("/getFileChecksum", ugi, datanode, request, tokenString);
        response.sendRedirect(uri.toURL().toString());
      } catch(URISyntaxException e) {
        throw new ServletException(e); 
        //response.getWriter().println(e.toString());
      } catch (IOException e) {
        response.sendError(400, e.getMessage());
      }
    }
  }
  
  /** Get FileChecksum */
  public static class GetServlet extends DfsServlet {
    /** For java.io.Serializable */
    private static final long serialVersionUID = 1L;
    
    /** {@inheritDoc} */
    public void doGet(HttpServletRequest request, HttpServletResponse response
        ) throws ServletException, IOException {
      final PrintWriter out = response.getWriter();
      final String filename = getFilename(request, response);
      final XMLOutputter xml = new XMLOutputter(out, "UTF-8");
      xml.declaration();

      final Configuration conf = new Configuration(DataNode.getDataNode().getConf());
      final int socketTimeout = conf.getInt("dfs.socket.timeout", HdfsConstants.READ_TIMEOUT);
      final SocketFactory socketFactory = NetUtils.getSocketFactory(conf, ClientProtocol.class);

      try {
        ClientProtocol nnproxy = getUGI(request, conf).doAs
        (new PrivilegedExceptionAction<ClientProtocol>() {
          @Override
          public ClientProtocol run() throws IOException {
            return DFSClient.createNamenode(conf);
          }
        });
        
        final MD5MD5CRC32FileChecksum checksum = DFSClient.getFileChecksum(
            filename, nnproxy, socketFactory, socketTimeout);
        MD5MD5CRC32FileChecksum.write(xml, checksum);
      } catch(IOException ioe) {
        writeXml(ioe, filename, xml);
      } catch (InterruptedException e) {
        writeXml(e, filename, xml);
      }
      xml.endDocument();
    }
  }
}
