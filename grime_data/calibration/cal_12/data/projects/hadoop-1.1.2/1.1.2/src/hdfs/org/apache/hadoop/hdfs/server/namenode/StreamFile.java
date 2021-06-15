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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.server.datanode.DataNode;
import org.apache.hadoop.security.UserGroupInformation;

public class StreamFile extends DfsServlet {
  /** for java.io.Serializable */
  private static final long serialVersionUID = 1L;

  public static final String CONTENT_LENGTH = "Content-Length";

  static InetSocketAddress nameNodeAddr;
  static DataNode datanode = null;
  static {
    if ((datanode = DataNode.getDataNode()) != null) {
      nameNodeAddr = datanode.getNameNodeAddr();
    }
  }
  
  /** getting a client for connecting to dfs */
  protected DFSClient getDFSClient(HttpServletRequest request)
      throws IOException, InterruptedException {

    Configuration conf =
      (Configuration) getServletContext().getAttribute(JspHelper.CURRENT_CONF);
    UserGroupInformation ugi = getUGI(request, conf);

    return JspHelper.getDFSClient(ugi, nameNodeAddr, conf);
  }
  
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    final String filename = request.getPathInfo() != null ?
        request.getPathInfo() : "/";
    if (filename == null || filename.length() == 0) {
      response.setContentType("text/plain");
      PrintWriter out = response.getWriter();
      out.print("Invalid input");
      return;
    }
    
    DFSClient dfs;
    try {
      dfs = getDFSClient(request);
    } catch (InterruptedException e) {
      response.sendError(400, e.getMessage());
      return;
    }
    
    final DFSClient.DFSInputStream in = dfs.open(filename);
    OutputStream os = response.getOutputStream();
    response.setHeader("Content-Disposition", "attachment; filename=\"" + 
                       filename + "\"");
    response.setContentType("application/octet-stream");
    response.setHeader(CONTENT_LENGTH, "" + in.getFileLength());
    byte buf[] = new byte[4096];
    try {
      int bytesRead;
      while ((bytesRead = in.read(buf)) != -1) {
        os.write(buf, 0, bytesRead);
      }
    } catch(IOException e) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("response.isCommitted()=" + response.isCommitted(), e);
      }
      throw e;
    } finally {
      try {
        in.close();
        os.close();
      } finally {
        dfs.close();
      }
    }
  }
}
