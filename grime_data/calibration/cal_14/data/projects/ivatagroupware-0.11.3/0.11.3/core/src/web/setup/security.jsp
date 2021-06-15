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

<%@page import="com.ivata.groupware.admin.setting.Settings" %>

<%@page import="com.ivata.mask.util.StringHandling"%>
<%@page import="com.ivata.mask.web.tag.theme.ThemeTag"%>
<%@page import="java.util.*"%>
<%@page import="javax.servlet.http.*"%>

<%@page import='org.apache.struts.Globals' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: security.jsp,v 1.1 2005/04/22 09:44:31 colinmacleod Exp $
//
// Displays the results of the setup process.
//
// Since: ivata groupware 0.11 (2005-03-29)
// Author: Colin MacLeod <colin.macleod@ivata.com>
// $Revision: 1.1 $
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
// $Log: security.jsp,v $
// Revision 1.1  2005/04/22 09:44:31  colinmacleod
// This page is shown when the user is
// not authorized to setup the app.
//
// Revision 1.1  2005/04/11 10:40:41  colinmacleod
// Added setup feature.
//
////////////////////////////////////////////////////////////////////////////////
--%>

<%@include file='/include/tags.jspf'%>
<logic:notPresent name='securitySession'>
  <logic:redirect action='/setup'/>
</logic:notPresent>
<logic:notPresent name='setupForm'>
  <logic:redirect action='/setup'/>
</logic:notPresent>
<jsp:include page="/generateCSS"/>
<c:catch>
  <%@include file='/include/theme.jspf'%>
</c:catch>

<igw:getSetting id='siteName' setting='siteName' type='java.lang.String'/>
<igw:bean id='setupForm' name='setupForm' scope='request' type='com.ivata.groupware.business.mail.struts.MailSetupForm'/>

<%-- if we're in pop-up mode, don't show a title --%>
<igw:bean id='title' type='java.lang.String'><bean:message key='setup.title.results'/></igw:bean>
<imhtml:html locale='true'>
  <igw:head title='<%=title%>' login='true' topLevel='true'>
    <link rel='stylesheet' href='<%=request.getContextPath()%>/style/login.css' type='text/css' />
    <html:base/>
  </igw:head>
  <body>
    <imtheme:window
        styleClass='loginWindow'
        titleKey='setup.title.results.window'
        titleArgs='<%=Arrays.asList(new Object[] {siteName})%>'>
      <%@include file='/include/errorFrame.jspf'%>
      <imhtml:form
          action='/loginGuest'
          resourceFieldPath='setup'>
        <imtheme:frame>
          <imtheme:framePane>
            <bean:message key="setup.label.security" arg0='<%=siteName%>'/>
          </imtheme:framePane>
        </imtheme:frame>
        <imtheme:buttonFrame>
          <imhtml:ok/>
        </imtheme:buttonFrame>
      </imhtml:form>
    </imtheme:window>
    <%@ include file='/theme/include/standards.jspf' %>
  </body>
</imhtml:html>
