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
<%@ page import="org.archive.io.SinkHandlerLogRecord" %>
<%@ page import="java.util.logging.Level" %>
<%
    SinkHandlerLogRecord alert =
        heritrix.getAlert(request.getParameter("alert"));
    String title = "Read alert";
    int tab = 0;
%>

<%@include file="/include/head.jsp"%>
<p>
<% if(alert == null) { %>
    <b> No matching alert found </b>
<% } else { 
    alert.setRead();
%>
    <table>
        <tr>
            <td>
                <b>Time:</b>&nbsp;
            </td>
            <td>
                <%=sdf.format(alert.getCreationTime())%> GMT
            </td>
        </tr>
        <tr>
            <td>
                <b>Level:</b>&nbsp;
            </td>
            <td>
                <%=alert.getLevel().getName()%>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <b>Message:</b>&nbsp;
            </td>
            <td>
                <pre><%=alert.getMessage()%></pre>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <b>Exception:</b>&nbsp;
            </td>
            <td>
                <pre><%=alert.getThrownToString()%></pre>
            </td>
        </tr>
    </table>
<% } %>
    <p>
        <a href="<%=request.getContextPath()%>/console/alerts.jsp">Back to alerts</a>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <% if(alert != null) { %>
             <a href="<%=request.getContextPath()%>/console/alerts.jsp?alerts=<%=alert.getSequenceNumber()%>&action=delete">Delete this alert</a>
        <% } %>
<%@include file="/include/foot.jsp"%>
