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
<%@page import='org.apache.struts.Globals' %>

<!--
////////////////////////////////////////////////////////////////////////////////
// $Id: closePopUp.jsp,v 1.3 2005/04/28 18:47:07 colinmacleod Exp $
//
// Lightweight HTML-JavaScript file to close pop-up windows.
//
// If you specify the request attribute "refreshOpener" to be a non-null value,
// it will also reload the window which opened this one.
//
// If you specify the request attribute "openerPage" to be a non-null value, it
// is taken as the page to refresh the opener to.
//
// Since: ivata groupware 0.9 (2003-01-25)
// Author: Colin MacLeod <colin.macleod@ivata.com>
// $Revision: 1.3 $
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
// $Log: closePopUp.jsp,v $
// Revision 1.3  2005/04/28 18:47:07  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.2  2005/04/09 17:19:40  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1  2005/03/10 19:24:02  colinmacleod
// Moved to ivata groupware.
//
// Revision 1.5  2004/12/27 14:52:01  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.4  2004/11/12 15:57:12  colinmacleod
// Removed dependencies on SSLEXT.
// Moved Persistence classes to ivata masks.
//
// Revision 1.3  2004/07/14 22:50:23  colinmacleod
// Replaced bean:define with extended igw:bean.
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
// Revision 1.2  2003/10/17 12:36:13  jano
// fixing problems with building
// converting intranet -> portal
// Eclipse building
//
// Revision 1.1.1.1  2003/10/13 20:50:50  colin
// Restructured portal into subprojects
//
// Revision 1.12  2003/03/06 17:30:08  peter
// fixed js frame references and location updates
//
// Revision 1.11  2003/03/03 16:57:14  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.10  2003/02/28 11:59:39  colin
// added reload to openerPage
//
// Revision 1.9  2003/02/28 08:56:32  colin
// bugfixes - removed meeting-specific stuff
//
// Revision 1.8  2003/02/27 16:56:52  colin
// bugfixes
//
// Revision 1.6  2003/02/25 07:31:16  colin
// restructured java file paths
//
// Revision 1.5  2003/02/21 10:16:03  colin
// made openerPage now work via loading.jsp
//
// Revision 1.4  2003/02/18 10:53:09  colin
// tried using javascriptWindow to change the value of the opener page
//
// Revision 1.3  2003/02/11 07:18:05  colin
// added openerPage attribute
//
// Revision 1.2  2003/02/06 13:37:48  colin
// added refreshOpener request attribute
//
// Revision 1.1  2003/02/06 13:36:42  colin
// moved from plain html to jsp file extension
//
// Revision 1.1  2003/01/27 07:10:57  colin
// first version of closePopUp
//
////////////////////////////////////////////////////////////////////////////////
-->
<%@include file='/include/tags.jspf'%>
<html>
  <head>
    <title>closing your window</title>
    <script>
      <!--
        <%if (request.getAttribute("refreshOpener") != null) {%>
          <%--
            // note: this isn't as silly as it looks - if you don't _change_
            // the URL, then the browser won't refresh the page
          --%>
          var thisLocation = window.top.opener.location.href;
          window.top.opener.location.href = "/";
          window.top.opener.location.href = thisLocation;
        <%}%>
        <%if (request.getAttribute("openerPage") != null) { %>
          <igw:bean id='openerNewLocation'><imhtml:rewrite page='<%="/util/loading.jsp?page=" + request.getAttribute("openerPage").toString()%>' /></igw:bean>
          <%--
            // note: this isn't as silly as it looks - if you don't _change_
            // the URL, then the browser won't refresh the page
          --%>
          window.top.opener.parent.ivataMain.location.href="/";
          window.top.opener.parent.ivataMain.location.href="<%=openerNewLocation%>";
        <%}%>
        window.top.close();
      // -->
    </script>
  </head>
  <body>
  </body>
</html>