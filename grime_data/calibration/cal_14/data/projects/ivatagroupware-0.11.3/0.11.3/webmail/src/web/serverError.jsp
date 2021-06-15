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
<%@page contentType='text/html;charset=UTF-8'%>
<%@page import="com.ivata.groupware.business.mail.server.NoMailServerException"%>
<%@page import="com.ivata.mask.web.RewriteHandling"%>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: serverError.jsp,v 1.5 2005/04/28 18:46:15 colinmacleod Exp $
//
// This is where you'll end up if the intranet can't locate the page you're
// looking for.
//
// Since: ivata groupware 0.9 (2002-09-25)
// Author: Colin MacLeod <colin.macleod@ivata.com>
// $Revision: 1.5 $
//
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
// $Log: serverError.jsp,v $
// Revision 1.5  2005/04/28 18:46:15  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.4  2005/04/22 11:01:26  colinmacleod
// Updated the message for a non-configured
// mail server to a link to the setup page.
//
// Revision 1.3  2005/04/10 19:46:08  colinmacleod
// Removed mail.jspf include.
// Removed nobr tags.
//
// Revision 1.2  2005/04/09 17:20:02  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:51:23  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.5  2004/07/14 22:50:27  colinmacleod
// Replaced bean:define with extended igw:bean.
//
// Revision 1.4  2004/07/13 19:48:13  colinmacleod
// Moved project to POJOs from EJBs.
// Applied PicoContainer to services layer (replacing session EJBs).
// Applied Hibernate to persistence layer (replacing entity EJBs).
//
// Revision 1.3  2004/03/21 21:16:39  colinmacleod
// Shortened name to ivata op.
//
// Revision 1.2  2004/03/21 20:52:10  colinmacleod
// Extra handling for when the mail server is not there.
//
// Revision 1.1  2004/03/10 22:42:54  colinmacleod
// First version.
//
// Revision 1.1.1.1  2004/01/27 20:59:19  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.4  2003/12/16 15:09:08  jano
// fixing functionaality in buil.xml
//
// Revision 1.3  2003/12/12 11:10:11  jano
// fixing functionaality
//
// Revision 1.2  2003/10/17 14:14:21  jano
// changed names intranet -> portal
//
// Revision 1.1.1.1  2003/10/13 20:47:24  colin
// Restructured portal into subprojects
//
// Revision 1.11  2003/06/10 05:56:38  peter
// img and html:img replaced with size-aware imhtml:img
//
// Revision 1.10  2003/02/20 10:20:45  colin
// now displays last URL
//
// Revision 1.9  2003/02/20 09:45:42  colin
// added checkSite tag
//
// Revision 1.8  2003/01/27 07:17:52  colin
// update copyright notice
// changed cssClass to styleClass
//
// Revision 1.7  2003/01/18 20:04:40  colin
// fixed page not found message
//
// Revision 1.6  2003/01/14 10:31:29  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.5  2003/01/08 20:21:12  colin
// brought over debugging improvements from ivataShop
//
// Revision 1.4  2002/11/28 16:56:26  peter
// i18n finished
//
// Revision 1.3  2002/11/17 19:21:22  colin
// cosmetic changes - improved appearance and warning message
//
// Revision 1.2  2002/11/12 10:27:38  colin
// moved tag libraries to /WEB-INF/tag
//
// Revision 1.1  2002/09/26 08:15:35  colin
// first version of error hadnling page with link to login
//
////////////////////////////////////////////////////////////////////////////////
--%>

<%@include file='/include/tags.jspf'%>
<igw:checkLogin forward='mailIndex'/>
<%@include file='/include/theme.jspf'%>
<%
  Throwable serverThrowable = (Throwable) session.getAttribute("mailServerException");
%>
<imhtml:html locale='true'>
  <igw:head bundle='mail' titleKey='error.title.window'>
    <link rel='stylesheet' href='<%=RewriteHandling.getContextPath(request)%>/style/login.css' type='text/css' />
  </igw:head>
  <body>
    <div align='center'>
      <br/><br/><br/><br/>
      <igw:bean id='windowTitle'><bean:message key='error.title.window'/></igw:bean>
      <imtheme:window title='<%=windowTitle%>'>
        <imtheme:frame>
          <imtheme:framePane styleClass='error'>
            <table cellpadding='0' cellspacing='0' width='100%'>
              <tr>
                <td rowspan='2'>
                  <imhtml:img align='left' page='/images/error.gif'/>
                </td>
                <td rowspan='2'><img align='left' width='20' height='1' page='/images/empty.gif' /></td>
                <td>
                  <c:choose>
                    <c:when test='<%=(serverThrowable != null) && (serverThrowable instanceof NoMailServerException)%>'>
                      <igw:bean id='setupAction'><imhtml:rewrite page='/setup.action'/></igw:bean>
                      <bean:message key='error.label.mailServer.missing' bundle='mail' arg0='<%=setupAction%>'/>
                    </c:when>
                    <c:otherwise>
                      <bean:message key='error.label.mailServer' bundle='mail'/>
                    </c:otherwise>
                  </c:choose>
                </td>
              </tr>
              <c:if test='<%=(serverThrowable != null) && !(serverThrowable instanceof NoMailServerException)%>'>
                <tr>
                  <td>
                    <bean:message
                        key='error.label.mailServer.exception'
                        arg0='<%=serverThrowable.getMessage()%>'
                        bundle='mail'/>
                  </td>
                </tr>
              </c:if>
            </table>
          </imtheme:framePane>
        </imtheme:frame>
      </imtheme:window>
    </div>
  </body>
</imhtml:html>

