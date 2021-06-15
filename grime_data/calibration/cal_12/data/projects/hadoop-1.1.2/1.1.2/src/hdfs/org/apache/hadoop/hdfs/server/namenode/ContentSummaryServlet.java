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
import java.security.PrivilegedExceptionAction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.hdfs.protocol.ClientProtocol;
import org.apache.hadoop.ipc.RemoteException;
import org.apache.hadoop.security.UserGroupInformation;
import org.znerd.xmlenc.XMLOutputter;

/** Servlets for file checksum */
public class ContentSummaryServlet extends DfsServlet {
  /** For java.io.Serializable */
  private static final long serialVersionUID = 1L;
  
  /** {@inheritDoc} */
  public void doGet(final HttpServletRequest request,
      final HttpServletResponse response) throws ServletException, IOException {
    final Configuration conf = 
      (Configuration) getServletContext().getAttribute(JspHelper.CURRENT_CONF);
    final UserGroupInformation ugi = getUGI(request, conf);
    try {
      ugi.doAs(new PrivilegedExceptionAction<Void>() {
        @Override
        public Void run() throws Exception {
          final String path = request.getPathInfo();

          final PrintWriter out = response.getWriter();
          final XMLOutputter xml = new XMLOutputter(out, "UTF-8");
          xml.declaration();
          try {
            //get content summary
            final ClientProtocol nnproxy = createNameNodeProxy();
            final ContentSummary cs = nnproxy.getContentSummary(path);

            //write xml
            xml.startTag(ContentSummary.class.getName());
            if (cs != null) {
              xml.attribute("length"        , "" + cs.getLength());
              xml.attribute("fileCount"     , "" + cs.getFileCount());
              xml.attribute("directoryCount", "" + cs.getDirectoryCount());
              xml.attribute("quota"         , "" + cs.getQuota());
              xml.attribute("spaceConsumed" , "" + cs.getSpaceConsumed());
              xml.attribute("spaceQuota"    , "" + cs.getSpaceQuota());
            }
            xml.endTag();
          } catch(IOException ioe) {
            writeXml(ioe, path, xml);
          }
          xml.endDocument();
          return null;
        }
      });
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
  }
}
