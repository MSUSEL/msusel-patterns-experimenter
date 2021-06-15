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

<%@page import="java.util.List"%>
<%@page import="org.archive.crawler.admin.CrawlJob"%>
<%@page import="org.archive.crawler.settings.XMLSettingsHandler"%>

<%
    if(request.getParameter("default") != null) {
        CrawlJob defaultJob = handler.getJob(request.getParameter("default"));
        if(defaultJob != null && defaultJob.isProfile()){
            handler.setDefaultProfile(defaultJob);
        }
    }
    if(request.getParameter("delete") != null) {
        CrawlJob defaultJob = handler.getJob(request.getParameter("delete"));
        if(defaultJob != null && defaultJob.isProfile()) {
            handler.deleteProfile(defaultJob);
        }
    }

    String title = "Profiles";
    int tab = 2;
%>

<%@include file="/include/head.jsp"%>
<% if(request.getParameter("message")!=null && request.getParameter("message").length() >0){ %>
    <p>
        <span class="flashMessage"><b><%=request.getParameter("message")%></b></span>
<% } %>
<table border="0" cellspacing="0" cellpadding="1">
    <tr>
        <th>
            Profile name
        </th>
        <th>
            Actions
        </th>
    </tr>
    <%
        List profiles = handler.getProfiles();
        CrawlJob defaultProfile = handler.getDefaultProfile();
        
        boolean alt = true;
        for(int i=0 ; i<profiles.size() ; i++){
            CrawlJob profile = (CrawlJob)profiles.get(i);
    %>
            <tr <%=alt?"bgcolor='#EEEEFF'":""%>>
                <td width="150">
                    <%if(defaultProfile.getJobName().equals(profile.getJobName())){out.println("<b>");}%>
                    <%=profile.getJobName()%>&nbsp;&nbsp;
                    <%if(defaultProfile.getJobName().equals(profile.getJobName())){out.println("</b>");}%>
                </td>
                <td>
                    <a href="<%=request.getContextPath()%>/jobs/configure.jsp?job=<%=profile.getUID()%>" style="color: #003399;" class="underLineOnHover">Edit</a>
                    &nbsp;
                    <a href="<%=request.getContextPath()%>/jobs/new.jsp?job=<%=profile.getUID()%>" style="color: #003399;" class="underLineOnHover">New job based on it</a>
                    &nbsp;
                    <a href="<%=request.getContextPath()%>/jobs/new.jsp?job=<%=profile.getUID()%>&profile=true" style="color: #003399;" class="underLineOnHover">New profile based on it</a>
                    &nbsp;
                    <%if(defaultProfile.getJobName().equals(profile.getJobName())==false){%>
                        <a href="<%=request.getContextPath()%>/profiles.jsp?default=<%=profile.getUID()%>" style="color: #003399;" class="underLineOnHover">Set as default</a>
                        &nbsp;
                        <a href="<%=request.getContextPath()%>/profiles.jsp?delete=<%=profile.getUID()%>" style="color: #003399;" class="underLineOnHover">Delete</a>
                    <%}%>
                </td>
            </tr>
    <%
            alt = !alt;
        }
    %>
</table>
<%@include file="/include/foot.jsp"%>
