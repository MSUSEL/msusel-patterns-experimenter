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
<%@page contentType="text/html;charset=UTF-8"%>

<%@page import="org.apache.struts.taglib.TagUtils" %>

<%@page import='com.ivata.mask.web.javascript.JavaScriptWindow' %>
<%@page import='java.util.HashMap'%>
<!--
////////////////////////////////////////////////////////////////////////////////
// $Id: keepAlive.jsp,v 1.2 2005/04/09 17:19:40 colinmacleod Exp $
//
// Lightweight JSP to keep the seession alive as long as the browser is open.
//
// Since: ivata groupware 0.9 (2003-01-25)
// Author: Colin MacLeod <colin.macleod@ivata.com>
// $Revision: 1.2 $
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2001 - 2005 ivata limited.
// All rights reserved.
// =========================================================
// ivata groupware may be redistributed under the GNU General Public
// License as published by the Free Software Foundation;
// version 2 of the License.
//
// These programs are free software; you can redistribute them and/or
// modify them under the terms of the GNU General Public License
// as published by the Free Software Foundation; version 2 of the License.
//
// These programs are distributed in the hope that they will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
//
// See the GNU General Public License in the file LICENSE.txt for more
// details.
//
// If you would like a copy of the GNU General Public License write to
//
// Free Software Foundation, Inc.
// 59 Temple Place - Suite 330
// Boston, MA 02111-1307, USA.
//
//
// To arrange commercial support and licensing, contact ivata at
//                  http://www.ivata.com/contact.jsp
//
////////////////////////////////////////////////////////////////////////////////
//
// $Log: keepAlive.jsp,v $
// Revision 1.2  2005/04/09 17:19:40  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1  2005/03/10 19:24:02  colinmacleod
// Moved to ivata groupware.
//
// Revision 1.6  2004/12/31 18:48:08  colinmacleod
// Fixed typos in TagUtils conversion from RequestUtils.
//
// Revision 1.5  2004/12/27 14:52:01  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.4  2004/11/03 15:53:20  colinmacleod
// Changed todo comments to TODO: all caps.
//
// Revision 1.3  2004/07/13 19:42:46  colinmacleod
// Moved project to POJOs from EJBs.
// Applied PicoContainer to services layer (replacing session EJBs).
// Applied Hibernate to persistence layer (replacing entity EJBs).
//
// Revision 1.2  2004/03/21 21:16:24  colinmacleod
// Shortened name to ivata op.
//
// Revision 1.1  2004/03/03 21:32:35  colinmacleod
// Moved core files to core subproject
//
// Revision 1.1.1.1  2004/01/27 20:58:18  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.3  2004/01/20 15:11:39  jano
// fixing problems with new sslext
//
// Revision 1.2  2003/10/17 12:36:13  jano
// fixing problems with building
// converting intranet -> portal
// Eclipse building
//
// Revision 1.1.1.1  2003/10/13 20:50:50  colin
// Restructured portal into subprojects
//
// Revision 1.3  2003/04/09 08:48:09  jano
// session timeOut is 17 minutes
// so we need to refres after 15 minutes
//
// Revision 1.2  2003/02/25 07:31:16  colin
// restructured java file paths
//
// Revision 1.1  2003/01/27 07:10:40  colin
// first version of keepAlive
//
////////////////////////////////////////////////////////////////////////////////
-->

<%!
TagUtils tagUtils = TagUtils.getInstance();
%>

<html>
  <head>
    <title>keeping your connection open</title>
    <%-- TODO: make the interval a setting --%>
    <meta http-equiv='Refresh' content='<%="150;" +
                                          tagUtils.computeURL(pageContext,
                                            null, null, "/util/keepAlive.jsp",
                                            null, null, null, null, true)%>'/>
  </head>
  <body>
    <%--
      didn't use custom tags to keep this as lightweight as poss. this should be
      put in a servlet, really
      anyhow, with this page the session _shouldn't_ time-out, but if the server
      is reset, then a login screen is displayed
    --%>
    <%
      if (session.getAttribute("securitySession") == null) {
          HashMap params = new HashMap();
          params.put("reset", "1");
    %>
      <script>window.location.href = "<%=tagUtils.computeURL(pageContext,
                                        "login", null, null, null, null,
                                        params, null, true)%>";</script>
    <%
      }
    %>
  </body>
</html>
