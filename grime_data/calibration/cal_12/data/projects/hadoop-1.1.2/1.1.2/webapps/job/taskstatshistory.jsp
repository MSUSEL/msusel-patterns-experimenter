<%--

    The MIT License (MIT)

    MSUSEL Arc Framework
    Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
    Software Engineering Laboratory and Idaho State University, Informatics and
    Computer Science, Empirical Software Engineering Laboratory

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.

--%>
<%
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file 
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
%>
<%@ page
  contentType="text/html; charset=UTF-8"
  import="javax.servlet.http.*"
  import="java.io.*"
  import="java.util.*"
  import="org.apache.hadoop.http.HtmlQuoting"
  import="org.apache.hadoop.mapred.*"
  import="org.apache.hadoop.fs.*"
  import="org.apache.hadoop.util.*"
  import="java.text.*"
  import="org.apache.hadoop.mapred.JobHistory.*" 
%>
<%! private static SimpleDateFormat dateFormat = new SimpleDateFormat("d/MM HH:mm:ss") ;
    private static final long serialVersionUID = 1L;
%>

<%
  String attemptid = request.getParameter("attemptid");
  if(attemptid == null) {
    out.println("No attemptid found! Pass a 'attemptid' parameter in the request.");
    return;
  }
  TaskID tipid = TaskAttemptID.forName(attemptid).getTaskID();
  String logFile = request.getParameter("logFile");
  String encodedLogFileName = 
    JobHistory.JobInfo.encodeJobHistoryFilePath(logFile);
  String jobid = JSPUtil.getJobID(new Path(encodedLogFileName).getName());
  Format decimal = new DecimalFormat();

  FileSystem fs = (FileSystem) application.getAttribute("fileSys");
  JobConf jobConf = (JobConf) application.getAttribute("jobConf");
  ACLsManager aclsManager = (ACLsManager) application.getAttribute("aclManager");
  JobHistory.JobInfo job = JSPUtil.checkAccessAndGetJobInfo(request,
      response, jobConf, aclsManager, fs, new Path(logFile));
  if (job == null) {
    return;
  }

  JobHistory.Task task = job.getAllTasks().get(tipid.toString());
  JobHistory.TaskAttempt attempt = task.getTaskAttempts().get(attemptid);

  Counters counters = 
    Counters.fromEscapedCompactString(attempt.get(Keys.COUNTERS));
%>

<html>
  <head>
    <title>Counters for <%=attemptid%></title>
  </head>
<body>
<h1>Counters for <%=attemptid%></h1>

<hr>

<%
  if (counters == null) {
%>
    <h3>No counter information found for this attempt</h3>
<%
  } else {    
%>
    <table>
<%
      for (String groupName : counters.getGroupNames()) {
        Counters.Group group = counters.getGroup(groupName);
        String displayGroupName = group.getDisplayName();
%>
        <tr>
          <td colspan="3"><br/><b>
          <%=HtmlQuoting.quoteHtmlChars(displayGroupName)%></b></td>
        </tr>
<%
        Iterator<Counters.Counter> ctrItr = group.iterator();
        while(ctrItr.hasNext()) {
          Counters.Counter counter = ctrItr.next();
          String displayCounterName = counter.getDisplayName();
          long value = counter.getCounter();
%>
          <tr>
            <td width="50"></td>
            <td><%=HtmlQuoting.quoteHtmlChars(displayCounterName)%></td>
            <td align="right"><%=decimal.format(value)%></td>
          </tr>
<%
        }
      }
%>
    </table>
<%
  }
%>

<hr>
<a href="jobdetailshistory.jsp?logFile=<%=encodedLogFileName%>">Go back to the job</a><br>
<a href="jobhistoryhome.jsp">Go back to Job History Viewer</a><br>
<%
out.println(ServletUtil.htmlFooter());
%>
