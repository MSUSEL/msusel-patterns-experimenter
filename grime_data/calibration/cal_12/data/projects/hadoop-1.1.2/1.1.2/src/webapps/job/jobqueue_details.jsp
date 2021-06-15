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
  import="java.util.Vector"
  import="java.util.Collection"
  import="org.apache.hadoop.http.HtmlQuoting"
  import="org.apache.hadoop.mapred.*"
  import="org.apache.hadoop.util.StringUtils"
  import="org.apache.hadoop.util.ServletUtil"
%>
<%!
private static final long serialVersionUID = 526456771152222127L; 
%>
<%
  JobTracker tracker = 
    (JobTracker) application.getAttribute("job.tracker");
  String trackerName = 
    StringUtils.simpleHostname(tracker.getJobTrackerMachine());
  String queueName = request.getParameter("queueName");
  TaskScheduler scheduler = tracker.getTaskScheduler();
  Collection<JobInProgress> jobs = scheduler.getJobs(queueName);
  JobQueueInfo schedInfo = tracker.getQueueInfo(queueName);
%>
<html>
<head>
<title>Queue details for
<%=queueName!=null?queueName:"(Given queue name was 'null')"%> </title>
<link rel="stylesheet" type="text/css" href="/static/hadoop.css">
<script type="text/javascript" src="/static/jobtracker.js"></script>
</head>
<body>
<% JSPUtil.processButtons(request, response, tracker); %>
<%
  String schedulingInfoString = schedInfo.getSchedulingInfo();
%>
<h1>Hadoop Job Queue Scheduling Information on 
  <a href="jobtracker.jsp"><%=trackerName%></a>
</h1>
<div>
State : <%= schedInfo.getQueueState() %> <br/>
Scheduling Information :
<%= HtmlQuoting.quoteHtmlChars(schedulingInfoString).replaceAll("\n","<br/>") %>
</div>
<hr/>
<%
if(jobs == null || jobs.isEmpty()) {
%>
<center>
<h2> No Jobs found for the Queue ::
<%=queueName!=null?queueName:""%> </h2>
<hr/>
</center>
<%
}else {
%>
<center>
<h2> Job Summary for the Queue ::
<%=queueName!=null?queueName:"" %> </h2>
</center>
<div style="text-align: center;text-indent: center;font-style: italic;">
(In the order maintained by the scheduler)
</div>
<br/>
<hr/>
<%=
  JSPUtil.generateJobTable("Job List", jobs, 30, 0, tracker.conf)
%>
<hr>
<% } %>

<%
out.println(ServletUtil.htmlFooter());
%>

