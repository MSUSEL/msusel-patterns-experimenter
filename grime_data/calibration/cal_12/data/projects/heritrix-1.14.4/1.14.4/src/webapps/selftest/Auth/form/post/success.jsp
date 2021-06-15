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
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>

<%
    // This define is all that differs from the post page.
    final String METHOD = "POST";

    final String COOKIE_NAME = "selftest-login-" + METHOD;
    final String LOGIN = "login";
    final String PASSWORD = "password";

    String method = request.getMethod();
    String login = request.getParameter(LOGIN);
    String password = request.getParameter(PASSWORD);
    Cookie [] cookies = request.getCookies();
    boolean loggedIn = false;
    if (cookies != null) {
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(COOKIE_NAME)) {
                loggedIn = true;
                break;
            }
        }
    }

    // If logged in, let them through, else see what parameters are 
    // available.
    if (!loggedIn) {
        if (login == null && password == null ) {
            // Needs to login first.
            response.sendRedirect("index.html");
        } else if (login == null || !login.equals(LOGIN) ||
            password == null || !password.equals(PASSWORD) ||
            method == null || !method.equals(METHOD)) {
            // Add the query string to aid debugging.
            response.sendRedirect("error.html?method=" + method +
                '&' + "login=" + login +
                '&' + "password=" + password);
        } else {
            Cookie cookie = new Cookie("selftest-login-get","successful");
            response.addCookie(cookie);
        }
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>Successful <%=METHOD%> Login Page</title>
        <meta name="author" content="Debian User,,," >
        <meta name="generator" content="screem 0.8.2" >
        <meta name="keywords" content="" >
        <meta http-equiv="content-type" content="text/html; charset=UTF-8" >
        <meta http-equiv="Content-Script-Type" content="text/javascript" >
        <meta http-equiv="Content-Style-Type" content="text/css" >
    </head>
    <body>
            <h1>Successful <%=METHOD%> Login Page</h1>
            <p>You get this page if a successful login.</p>
            <p><a href="post-loggedin.html">Page crawler can get only if it
            successfully negotiated login.</p>
    </body>
</html>
