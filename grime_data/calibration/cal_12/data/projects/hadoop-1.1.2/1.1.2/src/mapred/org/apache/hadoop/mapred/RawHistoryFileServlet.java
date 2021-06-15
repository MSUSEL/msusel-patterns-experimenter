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
package org.apache.hadoop.mapred;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class RawHistoryFileServlet extends HttpServlet {

  private ServletContext servletContext;

  @Override
  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    servletContext = servletConfig.getServletContext();
  }

  @Override
  protected void doGet(HttpServletRequest request,
                       HttpServletResponse response)
      throws ServletException, IOException {

    String logFile = request.getParameter("logFile");

    if (logFile == null) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "Invalid log file name");
      return;
    }

    FileSystem fs = (FileSystem) servletContext.getAttribute("fileSys");
    JobConf jobConf = (JobConf) servletContext.getAttribute("jobConf");
    ACLsManager aclsManager = (ACLsManager) servletContext.getAttribute("aclManager");
    Path logFilePath = new Path(logFile);
    JobHistory.JobInfo job = null;
    try {
      job = JSPUtil.checkAccessAndGetJobInfo(request,
        response, jobConf, aclsManager, fs, logFilePath);
    } catch (InterruptedException e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          "Request interrupted");
    }

    if (job == null) {
      response.sendError(HttpServletResponse.SC_MOVED_PERMANENTLY,
                "Job details doesn't exist");
      return;
    }

    InputStream in = fs.open(logFilePath);
    try {
      IOUtils.copyBytes(in, response.getOutputStream(), 8192, false);
    } finally {
      in.close();
    }
  }
}
