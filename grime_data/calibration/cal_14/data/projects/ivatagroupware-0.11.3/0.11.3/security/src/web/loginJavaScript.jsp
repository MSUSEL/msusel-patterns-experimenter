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

<%@page import="com.ivata.mask.util.StringHandling"%>
<%@page import="com.ivata.mask.web.RewriteHandling"%>
<%@page import="java.util.*"%>
<%@page import="javax.servlet.http.*"%>

<%@page import='org.apache.struts.Globals' %>
<%@page import="org.apache.struts.taglib.TagUtils" %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: loginJavaScript.jsp,v 1.5 2005/04/28 18:47:05 colinmacleod Exp $
//
// This page is displayed when you try to login without javascript. It checks to
// see if javascript really doesn't work and if it doesn't it sends you back to
// the login page.
//
// Since: ivata groupware 0.9 (2003-01-18)
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
// $Log: loginJavaScript.jsp,v $
// Revision 1.5  2005/04/28 18:47:05  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.4  2005/04/10 19:35:57  colinmacleod
// Updated login pages to change theme.
//
// Revision 1.3  2005/04/09 17:19:59  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.2  2005/03/16 18:49:27  colinmacleod
// Changed debug logging for no java script to info.
//
// Revision 1.1.1.1  2005/03/10 17:51:45  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.8  2004/12/27 14:52:02  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.7  2004/11/03 16:02:24  colinmacleod
// Changed fieldPath to resourceFieldPath.
//
// Revision 1.6  2004/07/20 23:48:17  colinmacleod
// Replace invalid SettingsRemote calls.
//
// Revision 1.5  2004/07/18 16:38:42  colinmacleod
// Changed DynaValidatorForm to DynaActionForm.
//
// Revision 1.4  2004/07/14 22:50:26  colinmacleod
// Replaced bean:define with extended igw:bean.
//
// Revision 1.3  2004/07/14 20:59:54  colinmacleod
// Changed all occurrences of jsp:useBean to igw:bean (better tomcat compatibility).
//
// Revision 1.2  2004/03/21 21:16:34  colinmacleod
// Shortened name to ivata op.
//
// Revision 1.1.1.1  2004/01/27 20:59:20  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.2  2003/10/17 14:14:21  jano
// changed names intranet -> portal
//
// Revision 1.1.1.1  2003/10/13 20:47:25  colin
// Restructured portal into subprojects
//
// Revision 1.3  2003/03/03 16:57:12  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.2  2003/02/25 07:32:10  colin
// restructured java file paths
//
// Revision 1.1  2003/01/27 07:19:16  colin
// renamed from loginErrors
//
// Revision 1.2  2003/01/20 17:09:02  colin
// fixed bug where the help always comes up if you press it once
//
// Revision 1.1  2003/01/18 20:05:47  colin
// converted login to struts, added javascript checking, help button
//
////////////////////////////////////////////////////////////////////////////////
--%>

<%@include file='/include/tags.jspf'%>
<c:catch>
  <%@include file='/include/theme.jspf'%>
</c:catch>

<imhtml:html locale='true'>
  <%-- this little form is submitted by javascript, if you have it --%>
  <igw:head title='<%= TagUtils.getInstance().message(pageContext, null, Globals.LOCALE_KEY, "login.title")%>' login='true' topLevel='true'>
    <imhtml:base/>
    <link rel='stylesheet' href='<%=RewriteHandling.getContextPath(request)%>/style/login.css' type='text/css' />
  </igw:head>
  <body>
    <igw:bean id='errorWindowTitle'><bean:message key='error.title.window'/></igw:bean>
    <html:form action='/javaScript'>
      <html:hidden property='javaScriptVersion' value='1.0'/>
    </html:form>
    <script LANGUAGE="JavaScript1.1">
      <!--
      document.javaScriptForm.javaScriptVersion.value = "1.1";
      // -->
    </script>
    <script LANGUAGE="JavaScript1.2">
      <!--
      document.javaScriptForm.javaScriptVersion.value = "1.2";
      // -->
    </script>
    <script LANGUAGE="JavaScript1.3">
      <!--
      document.javaScriptForm.javaScriptVersion.value = "1.3";
      // -->
    </script>
    <%--
      if the person submitted the form, and we _thought_ there was no javascript
      submit the formusing javascript to make certain - if (s)he really doesn't
      have javascript, then this just does nothing.
      the alert is a cheat - it prevents the rest of the page from loading till
      the person has clicked on it, by which time the page should have refreshed
      anyway. this prevents the login form from ever appearing (which would be
      confusing).
    --%>
    <igw:bean id='loginForm' name='loginForm' type='org.apache.struts.action.DynaActionForm'/>
    <igw:bean id='javaScriptAlert'><bean:message key='login.label.javascript'/></igw:bean>
    <c:if test='<%=!StringHandling.isNullOrEmpty((String) loginForm.get("login"))
                      && StringHandling.isNullOrEmpty((String) loginForm.get("javaScriptVersion"))%>'>
      <script>
        <!--
          document.javaScriptForm.submit();
          alert("<%=javaScriptAlert%>");
        // -->
      </script>
    </c:if>

    <igw:getSetting id='siteName' setting='siteName' type='java.lang.String'/>
    <div align='center'>
      <imhtml:form action='/login' resourceFieldPath='login' onsubmit='if(!document.loginForm.javaScriptVersion.value) {document.loginForm.javaScriptVersion.value = 1}'>
        <html:hidden property='javaScriptVersion'/>
        <igw:bean id='errorWindowTitle'><bean:message key='error.title.window'/></igw:bean>
        <imtheme:window
            styleClass='loginWindow'
            title='<%=errorWindowTitle%>'>
          <imtheme:frame>
            <imtheme:framePane>
              <imhtml:errors/>
            </imtheme:framePane>
          </imtheme:frame>
        </imtheme:window>
        <igw:bean id='windowTitle'><bean:message key='login.title.window' arg0='<%=siteName%>'/></igw:bean>
        <imtheme:window
            styleClass='loginWindow'
            title='<%=windowTitle%>'>
          <imtheme:frame>
            <imtheme:framePane styleClass='hilight'>
              <%@include file='/loginMask.jspf'%>
            </imtheme:framePane>
          </imtheme:frame>
          <imtheme:buttonFrame>
            <imhtml:submit fieldName='login' onclick="if(!document.loginForm.javaScriptVersion.value) {document.loginForm.javaScriptVersion.value = 1}"/>
            <%--
              use a hidden field so that login is always set now (help is a
              javascript button. prevents problems with javascript submits
            --%>
            <html:hidden property='login' value='1'/>

            <%-- enable javascript so the help button will display --%>
            <igw:bean id='browser' scope='session' type='com.ivata.mask.web.browser.Browser'/>
            <jsp:setProperty name='browser' property='javaScriptVersion' value='1'/>
            <imhtml:help key='login'/>
            <html:hidden property='theme'/>
            <%-- if the person pressed help, show the help window --%>
            <c:if test='<%=!StringHandling.isNullOrEmpty((String) loginForm.get("help"))%>'>
              <%-- clear the help button --%>
              <%loginForm.set("help", null);%>
              <script type='text/javascript'>
                <!--
                  document.loginForm.help.click();
                // -->
              </script>
            </c:if>
          </imtheme:buttonFrame>
        </imtheme:window>
      </imhtml:form>
    </div>
  </body>
</imhtml:html>