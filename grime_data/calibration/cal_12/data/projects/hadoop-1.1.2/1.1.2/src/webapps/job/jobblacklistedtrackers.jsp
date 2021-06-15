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
  import="org.apache.hadoop.http.HtmlQuoting"
  import="org.apache.hadoop.mapred.*"
  import="org.apache.hadoop.mapred.JSPUtil.JobWithViewAccessCheck"
  import="org.apache.hadoop.util.*"
%>
<%!	private static final long serialVersionUID = 1L;
%>

<%
  JobTracker tracker = (JobTracker) application.getAttribute("job.tracker");
  String trackerName = 
           StringUtils.simpleHostname(tracker.getJobTrackerMachine());
%>
<%!       
  private void printBlackListedTrackers(JspWriter out, 
                             JobInProgress job) throws IOException {
    Map<String, Integer> trackerErrors = job.getTaskTrackerErrors();
    out.print("<table border=2 cellpadding=\"5\" cellspacing=\"2\">");
    out.print("<tr><th>TaskTracker</th><th>No. of Failures</th></tr>\n");
    int maxErrorsPerTracker = job.getJobConf().getMaxTaskFailuresPerTracker();
    for (Map.Entry<String,Integer> e : trackerErrors.entrySet()) {
      if (e.getValue().intValue() >= maxErrorsPerTracker) {
        out.print("<tr><td>" + HtmlQuoting.quoteHtmlChars(e.getKey()) +
            "</td><td>" + e.getValue() + "</td></tr>\n");
      }
    }
    out.print("</table>\n");
  }
%>

<%
    String jobId = request.getParameter("jobid");
    if (jobId == null) {
  	  out.println("<h2>Missing 'jobid' for fetching black-listed tasktrackers!</h2>");
  	  return;
    }
    
    JobWithViewAccessCheck myJob = JSPUtil.checkAccessAndGetJob(tracker,
        JobID.forName(jobId), request, response);
    if (!myJob.isViewJobAllowed()) {
      return; // user is not authorized to view this job
    }

    JobInProgress job = myJob.getJob();
    if (job == null) {
      out.print("<b>Job " + jobId + " not found.</b><br>\n");
      return;
    }
%>

<html>
<title>Hadoop <%=jobId%>'s black-listed tasktrackers</title>
<body>
<h1>Hadoop <a href="jobdetails.jsp?jobid=<%=jobId%>"><%=jobId%></a> - 
Black-listed task-trackers</h1>

<% 
    printBlackListedTrackers(out, job); 
%>

<hr>
<a href="jobdetails.jsp?jobid=<%=jobId%>">Go back to <%=jobId%></a><br>
<%
out.println(ServletUtil.htmlFooter());
%>
