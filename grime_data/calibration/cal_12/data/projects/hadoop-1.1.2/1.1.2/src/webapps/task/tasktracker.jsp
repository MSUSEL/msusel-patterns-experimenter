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
<%@ page
  contentType="text/html; charset=UTF-8"
  import="javax.servlet.*"
  import="javax.servlet.http.*"
  import="java.io.*"
  import="java.util.*"
  import="java.text.DecimalFormat"
  import="org.apache.hadoop.http.HtmlQuoting"
  import="org.apache.hadoop.mapred.*"
  import="org.apache.hadoop.util.*"
%>
<%!	private static final long serialVersionUID = 1L;
%>
<%
  TaskTracker tracker = (TaskTracker) application.getAttribute("task.tracker");
  String trackerName = tracker.getName();
%>

<html>

<title><%= trackerName %> Task Tracker Status</title>

<body>
<h1><%= trackerName %> Task Tracker Status</h1>
<img src="/static/hadoop-logo.jpg"/><br>
<b>Version:</b> <%= VersionInfo.getVersion()%>,
                r<%= VersionInfo.getRevision()%><br>
<b>Compiled:</b> <%= VersionInfo.getDate()%> by 
                 <%= VersionInfo.getUser()%><br>

<h2>Running tasks</h2>
<center>
<table border=2 cellpadding="5" cellspacing="2">
<tr><td align="center">Task Attempts</td><td>Status</td>
    <td>Progress</td><td>Errors</td></tr>

  <%
     Iterator itr = tracker.getRunningTaskStatuses().iterator();
     while (itr.hasNext()) {
       TaskStatus status = (TaskStatus) itr.next();
       out.print("<tr><td>" + status.getTaskID());
       out.print("</td><td>" + status.getRunState()); 
       out.print("</td><td>" + 
                 StringUtils.formatPercent(status.getProgress(), 2));
       out.print("</td><td><pre>" +
           HtmlQuoting.quoteHtmlChars(status.getDiagnosticInfo()) +
           "</pre></td>");
       out.print("</tr>\n");
     }
  %>
</table>
</center>

<h2>Non-Running Tasks</h2>
<table border=2 cellpadding="5" cellspacing="2">
<tr><td align="center">Task Attempts</td><td>Status</td>
  <%
    for(TaskStatus status: tracker.getNonRunningTasks()) {
      out.print("<tr><td>" + status.getTaskID() + "</td>");
      out.print("<td>" + status.getRunState() + "</td></tr>\n");
    }
  %>
</table>


<h2>Tasks from Running Jobs</h2>
<center>
<table border=2 cellpadding="5" cellspacing="2">
<tr><td align="center">Task Attempts</td><td>Status</td>
    <td>Progress</td><td>Errors</td></tr>

  <%
     itr = tracker.getTasksFromRunningJobs().iterator();
     while (itr.hasNext()) {
       TaskStatus status = (TaskStatus) itr.next();
       out.print("<tr><td>" + status.getTaskID());
       out.print("</td><td>" + status.getRunState()); 
       out.print("</td><td>" + 
                 StringUtils.formatPercent(status.getProgress(), 2));
       out.print("</td><td><pre>" +
           HtmlQuoting.quoteHtmlChars(status.getDiagnosticInfo()) +
           "</pre></td>");
       out.print("</tr>\n");
     }
  %>
</table>
</center>


<h2>Local Logs</h2>
<a href="/logs/">Log</a> directory

<%
out.println(ServletUtil.htmlFooter());
%>
