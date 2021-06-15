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
<%@include file="/include/nocache.jsp"%>
<%@ page errorPage="/error.jsp" %>
<%@page import="java.net.URLDecoder" %>

<%  String sMessage = null;%>

<html>
    <head>
        <title>Heritrix: Login</title>
        <link rel="stylesheet" 
            href="<%=request.getContextPath()%>/css/heritrix.css">
    </head>

    <body onload='document.loginForm.j_username.focus()'>
        <table border="0" cellspacing="0" cellpadding="0" height="100%">
            <tr>
                <td width="155" height="60" valign="top" nowrap>
                    <table border="0" cellspacing="0" cellpadding="0"
                            width="100%" height="100%">
                        <tr>
                            <td align="center" height="40" valign="bottom">
                                <a border="0" 
                                href="<%=request.getContextPath()%>/"><img border="0" src="<%=request.getContextPath()%>/images/logo.gif" width="145"></a>
                            </td>
                        </tr>
                        <tr>
                            <td class="subheading">
                                Login
                            </td>
                        </tr>
                    </table>
                </td>
                <td width="100%">&nbsp;</td>
            </tr>
            <tr>
                <td bgcolor="#0000FF" height="1" colspan="2">
                </td>
            </tr>
            <tr>
                <td colspan="2" height="100%" valign="top" class="main">
                    <form method="post" 
                        action='<%= response.encodeURL("j_security_check") %>'
                            name="loginForm">
                        <input type="hidden" name="action" value="login">
                        <input type="hidden" name="redirect" 
                            value="<%=request.getParameter("back")%>">
                        <table border="0">
                            <% if(sMessage != null ){ %>
                                <tr>
                                    <td colspan="2" align="left">
                                    <b><font color=red><%=sMessage%></font></b>
                                    </td>
                                </tr>
                            <%}%>
                            <tr>
                                <td class="dataheader">
                                    Username:
                                </td>
                                <td> 
                                    <input name="j_username">
                                </td>
                            </tr>
                            <tr>
                                <td class="dataheader">
                                    Password:
                                </td>
                                <td>
                                    <input type="password" name="j_password">
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" align="center">
                                    <input type="submit" value="Login">
                                </td>
                            </tr>
                    </form>
                </td>
            </tr>
        </table>
    </body>
</HTML>
