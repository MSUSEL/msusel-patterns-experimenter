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
<%@page import="com.ivata.groupware.admin.setting.Settings" %>
<%@page import="com.ivata.groupware.admin.security.server.SecuritySession" %>
<%@page import="com.ivata.mask.util.StringHandling" %>
<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: index.jsp,v 1.10 2005/04/28 18:47:06 colinmacleod Exp $
//
// <p>This is the frames page which creates all the others.</p>
//
// <p>In the future, is planned to have several different layouts so that,
// in addition to this frames appearance you could have a version
// without frames too.</p>
//
// Since: ivata groupware 0.9 (2001-09-08)
// Author: Colin MacLeod <colin.macleod@ivata.com>
// $Revision: 1.10 $
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
// $Log: index.jsp,v $
// Revision 1.10  2005/04/28 18:47:06  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.9  2005/04/22 10:16:12  colinmacleod
// Made the border 3 instead of 8.
//
// Revision 1.8  2005/04/10 20:18:43  colinmacleod
// Updated frames for new themes.
//
// Revision 1.6  2005/03/18 09:25:28  colinmacleod
// Resorted to scriptlet to fix securitySession initilization.
//
// Revision 1.3  2005/03/17 11:31:49  colinmacleod
// Now forwards to login guest even without javascript.
//
// Revision 1.2  2005/03/17 10:56:42  colinmacleod
// Added checkLogin.
//
// Revision 1.1.1.1  2005/03/10 17:49:26  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.8  2004/11/12 16:02:49  colinmacleod
// Removed dependencies on SSLEXT.
// Moved Persistence classes to ivata masks.
//
// Revision 1.7  2004/07/14 22:50:26  colinmacleod
// Replaced bean:define with extended igw:bean.
//
// Revision 1.6  2004/07/14 20:59:54  colinmacleod
// Changed all occurrences of jsp:useBean to igw:bean (better tomcat compatibility).
//
// Revision 1.5  2004/07/13 19:48:09  colinmacleod
// Moved project to POJOs from EJBs.
// Applied PicoContainer to services layer (replacing session EJBs).
// Applied Hibernate to persistence layer (replacing entity EJBs).
//
// Revision 1.4  2004/03/27 11:06:52  colinmacleod
// Reduced title frame height.
//
// Revision 1.3  2004/03/21 21:16:33  colinmacleod
// Shortened name to ivata op.
//
// Revision 1.2  2004/02/10 22:09:29  colinmacleod
// Turned off SSL
//
// Revision 1.1.1.1  2004/01/27 20:59:19  colinmacleod
// Moved ivata op to sourceforge.
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
// Revision 1.19  2003/03/13 14:55:52  jano
// fixing usage ling in index.jsp
// fixing usage link in usage.jsp
//
// Revision 1.18  2003/03/12 14:04:49  jano
// fixing width of left.jsp
//
// Revision 1.17  2003/02/25 07:32:10  colin
// restructured java file paths
//
// Revision 1.16  2003/02/18 19:20:51  colin
// added checking for a null username or settings in the session
//
// Revision 1.15  2003/01/27 07:17:27  colin
// update copyright notice
// added keepAlive frame
//
// Revision 1.14  2003/01/23 17:47:11  colin
// made the quota thing optional, depending on a boolean setting siteCheckQuota
//
// Revision 1.13  2003/01/23 16:06:52  peter
// fixed frameset sources
//
// Revision 1.12  2003/01/23 13:57:18  colin
// modifications to allow URL rewriting sessions (instead of cookie)
//
// Revision 1.11  2003/01/14 10:31:29  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.10  2002/11/28 16:56:26  peter
// i18n finished
//
// Revision 1.9  2002/09/30 15:26:03  colin
// added topLevel handling
//
// Revision 1.8  2002/09/26 08:17:17  colin
// improved check that the user is logged in
//
// Revision 1.7  2002/09/25 15:29:00  colin
// added title/tooltip attributes to buttons/submits
//
// Revision 1.4  2002/09/23 12:07:26  jano
// we remove right frame
//
// Revision 1.3  2002/07/01 08:06:20  colin
// cosmetic changes/forwarded to the library
//
// Revision 1.2  2002/06/21 12:06:20  colin
// tidied up documentation
//
// Revision 1.1  2002/04/30 16:23:25  colin
// moved over to EJB/JBuilder project
//
// Revision 1.4  2002/01/24 15:57:55  colin
// renaming and restructuring
//
// Revision 1.3  2002/01/20 12:29:25  colin
// moved properties over to separate jsp
//
// Revision 1.2  2002/01/03 12:56:11  colin
// changes during implementation of address book person_member
//
// Revision 1.1  2001/12/12 20:13:27  colin
// initial import into CVS
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<%-- can't use checkLogin here to avoid going round in circles --%>
<%
SecuritySession securitySession = (SecuritySession)session.getAttribute("securitySession");
%>
<c:if test='<%=(securitySession == null) || securitySession.isGuest()%>'>
  <logic:forward name='loginGuestAction'/>
</c:if>
<%
pageContext.setAttribute("securitySession", securitySession);
%>
<igw:bean id='user' name='securitySession' property="user" type='com.ivata.groupware.admin.security.user.UserDO'/>
<igw:bean id='container' name='securitySession' property='container' type='org.picocontainer.PicoContainer'/>
<%
Settings settings = (Settings) container.getComponentInstance(Settings.class);
%>
<igw:bean id='mainURI'><c:choose>
  <c:when test='<%=request.getParameter("uri") != null%>'><imhtml:rewrite page='<%=request.getParameter("uri")%>'/></c:when>
  <c:otherwise><imhtml:rewrite forward='<%=settings.getStringSetting(securitySession, "siteDefaultForward", user)%>'/></c:otherwise>
</c:choose></igw:bean>

<%-- if we're told to save this new uri as the default, do that
NOTE: since we've moved to forward handling, this isn't working any more
<c:if test='<%=request.getParameter("saveURI") != null%>'>
  <igw:setSetting setting='siteDefaultURL' userName='<%=userName%>' value='<%=uri%>'/>
</c:if>
--%>
<igw:bean id='titleURI'><imhtml:rewrite page='/title.jsp'/></igw:bean>
<igw:bean id='usageURI'><imhtml:rewrite page='/usage.jsp'/></igw:bean>
<igw:bean id='leftURI'><imhtml:rewrite page='/left.jsp'/></igw:bean>
<igw:bean id='keepAliveURI'><imhtml:rewrite page='/util/keepAlive.jsp'/></igw:bean>

<!DOCTYPE html
  PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html:html xhtml='true' locale='true'>
  <igw:head topLevel='true' frames='true'><imhtml:base/></igw:head>
  <frameset rows='115,*' frameborder='0'>
    <frameset cols='*,1' frameborder='0'>
      <frame src='<%=titleURI%>' name='ivataTitle' scrolling='no' frameborder='1'  framespacing='8'/>
      <frame src='<%=keepAliveURI%>' name='ivataKeepAlive' scrolling='no' frameborder='1'  framespacing='8'/>
    </frameset>
    <frameset cols='215,*' frameborder="1" border="3" bordercolor="#eee">
      <frame src='<%=leftURI%>' name='ivataLeft'/>
      <frame src='<%=mainURI%>' name='ivataMain'/>
    </frameset>
    <noframes>
      <bean:message key='alert.noframes' />
    </noframes>
  </frameset>
</html:html>
