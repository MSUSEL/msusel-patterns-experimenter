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

<%@ page import="org.archive.crawler.admin.CrawlJob" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>

<%!
    public String printJobList(List jobs, boolean isJobs,
            HttpServletRequest request) {
        if(jobs==null) {
            return null;
        }
        StringBuffer ret = new StringBuffer();
        for(int i = jobs.size()-1 ; i >= 0 ; i--) {
            CrawlJob tmp = (CrawlJob)jobs.get(i);
            ret.append("<li><a href=\"");
            ret.append(request.getContextPath());
            ret.append("/jobs/new.jsp?job=" + tmp.getUID() + "\">" +
                tmp.getJobName());
            if(isJobs){
                ret.append(" ["+tmp.getUID()+"]");
            }
            ret.append("</a>");
        }
        return ret.toString();
    }
%>
<%
    boolean isJobs = request.getParameter("type")!=null&&request.getParameter("type").equals("jobs");
    String title = "New via "+(isJobs?"an existing job":"a profile");
    int tab = 1;
%>

<%@include file="/include/head.jsp"%>
<p>
    <b>Select <%=isJobs?"job":"profile"%> to base new job on:</b>
<p>
    <ul>
<%
    if(isJobs){
        out.println(printJobList(handler.getPendingJobs(), true, request));
        if(handler.getCurrentJob()!=null){
            out.println("<li><a href=\"");
            out.println(request.getContextPath());
            out.println("/jobs/new.jsp?job=" +
                handler.getCurrentJob().getUID() + "\">" +
                handler.getCurrentJob().getJobName() +
                " [" + handler.getCurrentJob().getUID() + "]</a>");
        }
        out.println(printJobList(handler.getCompletedJobs(),true, request));
    } else {
        out.println(printJobList(handler.getProfiles(),false, request));
    }
%>    
    </ul>

        
<%@include file="/include/foot.jsp"%>
