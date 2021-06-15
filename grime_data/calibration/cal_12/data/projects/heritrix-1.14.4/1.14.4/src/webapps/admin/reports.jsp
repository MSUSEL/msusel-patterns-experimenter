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
<%@ include file="/include/handler.jsp" %>
<%@ page import="org.archive.crawler.admin.CrawlJob,java.util.List" %>
<%
    String title = "Reports";
    int tab = 4;
%>
<%@include file="/include/head.jsp"%>
<%
    String message = null;
    boolean crawling = handler.isCrawling();
    List jobs = handler.getCompletedJobs();
    final String FORCE = "force";
    final String ACTION = "action";
    String action = request.getParameter(ACTION);
    if (action != null && action.equals(FORCE)) {
        // Force generation of end-of-crawl reports.
        if (!crawling) {
            message = "Cannot force generation of reports if no running job.";
        } else {
            if (handler.getCurrentJob() == null) {
                message = "Current job is null.";
            } else {
                stats.dumpReports();
                message = "Forced generation of end-of-crawl reports.";
            }
       }
    }
%>

<p>
<% if(message != null ){ %>
<b><font color=red><%=message%></font></b>
<%}%>
</p>
<p>
<b>Reports on ongoing crawl/current status</b><br>
<ul>
	<% if(handler.getCurrentJob() != null) { %>
	<li><a href="<%=request.getContextPath()%>/reports/crawljob.jsp">Crawl report</a></li>
    <li><a href="<%=request.getContextPath()%>/reports/seeds.jsp?job=<%=handler.getCurrentJob().getUID()%>">Seed report</a></li>
    <% } else { %>
    <li>Crawl report (unavailable)</li>
    <li>Seed report (unavailable)</li>
    <% } %>
	<% if(handler.getCurrentJob() != null) { %>
    <li><a href="<%=request.getContextPath()%>/reports/frontier.jsp">Frontier report</a><br>
     <%=handler.getCurrentJob() != null?
        handler.getCurrentJob().getFrontierOneLine(): ""%></li>
    <% } else { %>
    <li>Frontier report (unavailable)</li>
    <% } %>
    <% if(handler.isCrawling()) { %>
    <li><a href="<%=request.getContextPath()%>/reports/processors.jsp">Processors report</a></li>
    <% } else { %>
    <li>Processors report (unavailable)</li>
    <% } %>
    <% if(handler.isCrawling()) { %>
    <li><a href="<%=request.getContextPath()%>/reports/threads.jsp">ToeThread report</a><br>
     <%=(handler.getCurrentJob() != null)?
        handler.getCurrentJob().getThreadOneLine():"" %></li>
    <% } else { %>
    <li>ToeThread report (unavailable)</li>
    <% } %>
</ul>
<% if(handler.getCurrentJob() != null) { %>
<p>The crawler generates reports when it finishes a job.  Clicking here on <a href="<%=request.getContextPath()%>/reports.jsp?<%=ACTION%>=<%=FORCE%>">Force generation of end-of-crawl Reports</a> will force the writing of reports to disk.  Clicking this link will return you to this page. Look to the disk for the generated reports.  Each click overwrites previously generated reports. Use this facility when the crawler has hung threads that can't be interrupted.</p>
<% } %>

<table border="0" cellspacing="0" cellpadding="1">
    <tr>
        <td colspan="3">
            <b>Started crawl jobs</b> (newest to oldest)
        </td>
    <% if (crawling == false && jobs.size() == 0) { %>
        <tr>
            <td>&nbsp;No crawl jobs have been started</td>
        </tr>
    <% } else { %>
        <%  if(crawling){ %>
                <tr bgcolor="#EEEEFF">
                    <td>
                       <%=handler.getCurrentJob().getJobName()%>&nbsp;
                    </td>
                    <td>
                       <i><%=handler.getCurrentJob().getStatus()%></i>&nbsp;&nbsp;
                    </td>
                </tr>
        <% 
            }
            boolean alt = !crawling;
            for(int i=jobs.size()-1; i>=0; i--){ 
                CrawlJob job = (CrawlJob)jobs.get(i);
        %>
                <tr <%=alt?"bgcolor='#EEEEFF'":""%>>
                    <td>
                        <%=job.getJobName()%>&nbsp;&nbsp;
                    </td>
                    <td>
                    	<a href="<%=request.getContextPath()%>/reports/crawljob.jsp?job=<%=job.getUID()%>">Crawl report</a> &nbsp;
                    </td>
                    <td>
                    	<a href="<%=request.getContextPath()%>/reports/seeds.jsp?job=<%=job.getUID()%>">Seed report</a> &nbsp;
                    </td>
                    <td>
                        <i><%=job.getStatus()%></i>&nbsp;&nbsp;
                    </td>
                </tr>
        <%
                alt = !alt; 
            } 
        %>
    <% } %>
</table>

<%@include file="/include/foot.jsp"%>
