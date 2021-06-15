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
<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: frame.jsp,v 1.3 2005/04/28 18:47:07 colinmacleod Exp $
//
// <p>Create a page with a single frame in it, using the URL provided by the
// URL parameter. This is useful for keeping javascript values throughout
// multiple refreshes of a page.</p>
//
// Since: ivata groupware 0.9 (2002-05-22)
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
// $Log: frame.jsp,v $
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
// Revision 1.1.1.1  2003/10/13 20:50:50  colin
// Restructured portal into subprojects
//
// Revision 1.2  2003/02/13 10:54:17  colin
// added sslext page handling
//
// Revision 1.1  2003/01/27 07:10:00  colin
// moved from root dir
//
// Revision 1.6  2003/01/14 10:31:29  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.5  2002/11/28 16:56:26  peter
// i18n finished
//
// Revision 1.4  2002/11/15 09:17:46  jano
// tags are now in another directory
//
// Revision 1.3  2002/10/08 16:23:52  colin
// fixed top-level handling
//
// Revision 1.2  2002/06/21 12:06:20  colin
// tidied up documentation
//
// Revision 1.1  2002/06/13 11:10:59  colin
// first version with rose model and jboss  integration
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<%
  String requestURL = request.getParameter("URL");
  String requestPage = request.getParameter("page");
  String title = request.getParameter("title");
%>
<igw:bean id='URL'><c:choose>
  <c:when test='<%=requestPage != null%>'><imhtml:rewrite page='<%=requestPage%>'/></c:when>
  <c:otherwise><%=requestURL%></c:otherwise>
</c:choose></igw:bean>

<imhtml:html locale='true'>
  <igw:head topLevel='true' title='<%=title%>'><imhtml:base/></igw:head>
  <frameset>
    <%-- need two frames to keep mozilla happy --%>
    <frame src="<%=URL%>" name="nothing" scrolling='false' marginheight='0' marginwidth='0' frameborder='0'/>
    <noframes>
      <bean:message key='alert.noframes' />
    </noframes>
  </frameset>
</imhtml:html>
