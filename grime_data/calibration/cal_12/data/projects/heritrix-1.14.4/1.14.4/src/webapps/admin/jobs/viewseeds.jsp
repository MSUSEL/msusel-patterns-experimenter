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
<%@include file="/include/handler.jsp"%>

<%@ page import="org.archive.crawler.admin.CrawlJob,org.archive.crawler.admin.StatisticsTracker,java.util.*,java.io.*" %>
<%@ page import="org.archive.crawler.settings.ComplexType"%>
<%@ page import="org.archive.crawler.admin.ui.JobConfigureUtils"%>
<%
    String job = request.getParameter("job");
    CrawlJob cjob = null;

    if(job != null)
    {
        cjob = handler.getJob(job);
    }
    
    String title = "View seeds";
    int tab = 1;
%>

<%@include file="/include/head.jsp"%>
    
    <body>
        <%
            if(cjob == null)
            {
                // NO JOB SELECTED - ERROR
        %>
                <b>Invalid job selected</b>
        <%
            }
            else
            {
        %>
                <fieldset style="width: 600px">
                    <legend>Seed file for '<%=cjob.getJobName()%>'</legend>
                    <pre><%
                    JobConfigureUtils.printOutSeeds(cjob.getSettingsHandler(),
                        out); 
        %></pre>
                </fieldset>
        <%
            } // End if(cjob==null)else clause
        %>
        <a href="javascript:history.back()">Back</a>

<%@include file="/include/foot.jsp"%>
