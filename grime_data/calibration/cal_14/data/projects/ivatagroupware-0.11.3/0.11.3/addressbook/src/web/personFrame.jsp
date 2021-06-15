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
<%@page import='com.ivata.mask.util.StringHandling' %>
<%@page import='java.util.Arrays' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: personFrame.jsp,v 1.3 2005/04/28 18:47:09 colinmacleod Exp $
//
// Display a frame around the person editing fields and the person summary.
//
// Since: ivata groupware 0.9 (2003-02-15)
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
// $Log: personFrame.jsp,v $
// Revision 1.3  2005/04/28 18:47:09  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.2  2005/04/09 17:19:12  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:50:45  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.6  2004/11/12 15:57:09  colinmacleod
// Removed dependencies on SSLEXT.
// Moved Persistence classes to ivata masks.
//
// Revision 1.5  2004/07/14 22:50:21  colinmacleod
// Replaced bean:define with extended igw:bean.
//
// Revision 1.4  2004/07/14 20:59:44  colinmacleod
// Changed all occurrences of jsp:useBean to igw:bean (better tomcat compatibility).
//
// Revision 1.3  2004/07/13 19:41:18  colinmacleod
// Moved project to POJOs from EJBs.
// Applied PicoContainer to services layer (replacing session EJBs).
// Applied Hibernate to persistence layer (replacing entity EJBs).
//
// Revision 1.2  2004/03/21 21:16:20  colinmacleod
// Shortened name to ivata op.
//
// Revision 1.1  2004/03/03 20:27:38  colinmacleod
// Updated webapp structure. Moved core items to core subproject.
//
// Revision 1.2  2004/02/10 22:09:28  colinmacleod
// Turned off SSL
//
// Revision 1.1.1.1  2004/01/27 20:58:02  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.2  2003/10/17 12:36:13  jano
// fixing problems with building
// converting intranet -> portal
// Eclipse building
//
// Revision 1.1.1.1  2003/10/13 20:50:24  colin
// Restructured portal into subprojects
//
// Revision 1.2  2003/02/25 07:28:54  colin
// restructured java file paths
//
// Revision 1.1  2003/02/18 10:45:19  colin
// first version
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<igw:checkLogin />
<%@include file='/include/theme.jspf'%>



<igw:bean id='addressBookPersonForm' scope='session' type='com.ivata.groupware.business.addressbook.struts.PersonForm'/>
<igw:bean id='person' name='addressBookPersonForm' property='person' type='com.ivata.groupware.business.addressbook.person.PersonDO'/>

<igw:bean id='fileAs'><c:choose>
<c:when test='<%=StringHandling.isNullOrEmpty(person.getFileAs())%>'>new user</c:when>
<c:otherwise><%=person.getFileAs()%></c:otherwise>
</c:choose></igw:bean>

<imhtml:html locale='true'>
  <imhtml:base/>
  <igw:head bundle='addressBook'
             titleKey='<%=addressBookPersonForm.getTitleKey()%>'
             titleArgs='<%=Arrays.asList(new Object[] {fileAs})%>'
             topLevel='true'>
    <imhtml:base/>
  </igw:head>
  <igw:bean id='personUri'><imhtml:rewrite page='/addressBook/person.jsp'/></igw:bean>
  <igw:bean id='personSummaryUri'><imhtml:rewrite page='/addressBook/personSummary.jsp'/></igw:bean>
  <frameset cols="*, 300">
    <frame src="<%=personUri%>" name="ivataPerson" border='0' marginheight='0' marginwidth='0' frameborder='yes' scrolling='no'/>
    <frame src='<%=personSummaryUri%>' name='ivataPersonSummary' border='0' marginheight='0' marginwidth='10' frameborder='no' scrolling='yes'/>
    <noframes>
      <bean:message key='alert.noframes' />
    </noframes>
  </frameset>
</imhtml:html>
