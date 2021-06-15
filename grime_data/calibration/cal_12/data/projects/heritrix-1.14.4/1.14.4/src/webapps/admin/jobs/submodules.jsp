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
  /**
   * This pages allows the user to select all submodules which appear
   * in collections inside other modules 
   *
   * @author Kristinn Sigurdsson
   */
%>
<%@include file="/include/handler.jsp"%>
<%@include file="/include/modules.jsp"%>

<%@ page import="org.archive.crawler.admin.ui.JobConfigureUtils" %>
<%@ page import="org.archive.crawler.admin.CrawlJob" %>
<%@ page import="org.archive.crawler.url.canonicalize.BaseRule" %>

<%
    CrawlJob theJob = JobConfigureUtils.handleJobAction(handler, request,
            response, request.getContextPath() + "/jobs.jsp", null, null);
    int tab = theJob.isProfile()?2:1;
%>

<%
    // Set page header.
    String title = "Submodules";
    int jobtab = 7;
%>

<%@include file="/include/head.jsp"%>
<%@include file="/include/filters_js.jsp"%>
    <p>
        <%@include file="/include/jobnav.jsp"%>
    <p>
        <p>
            <b>Add/Remove/Order Submodules</b>
        <p>
        <p>Use this page to add/remove/order submodules. Go to the
        <a href="javascript:doGoto('<%=request.getContextPath()%>/jobs/configure.jsp?job=<%=theJob.getUID()%>')">Settings</a>
        page to complete configuration of added submodules (e.g. To
        add the particular regex to an added canonicalization RegexRule
        or to fill in the authentication information into an added
        RFC2617 credential).</p>

    <form name="frmFilters" method="post" 
            action="submodules.jsp">
        <input type="hidden" name="job" value="<%=theJob.getUID()%>">
        <input type="hidden" name="action" value="done">
        <input type="hidden" name="subaction" value="">
        <input type="hidden" name="map" value="">
        <input type="hidden" name="filter" value="">
        <%=printAllMaps(theJob.getSettingsHandler().getOrder(), null, false, true, null)%>
    </form>
    <p>
<%@include file="/include/jobnav.jsp"%>
<%@include file="/include/foot.jsp"%>


