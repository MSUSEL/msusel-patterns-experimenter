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
<%@page import="com.ivata.mask.web.RewriteHandling"%>
<%@page import="com.ivata.mask.web.tag.theme.ThemeTag"%>
<%@page import="java.util.*"%>
<%@page import="javax.servlet.http.*"%>

<%@page import='org.apache.struts.Globals' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: login.jsp,v 1.4 2005/04/28 18:47:05 colinmacleod Exp $
//
// Login to our illustrious groupware. This page logs the user into ivata
// groupware and sets up her session.
//
// Since: ivata groupware 0.9 (2001-08-31)
// Author: Colin MacLeod <colin.macleod@ivata.com>
// $Revision: 1.4 $
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
// $Log: login.jsp,v $
// Revision 1.4  2005/04/28 18:47:05  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.3  2005/04/10 19:35:57  colinmacleod
// Updated login pages to change theme.
//
// Revision 1.2  2005/04/09 17:19:59  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:51:45  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.11  2004/12/31 19:19:45  colinmacleod
// Restructured to use loginGuest action.
//
// Revision 1.10  2004/12/27 14:52:02  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.9  2004/12/23 21:01:29  colinmacleod
// Updated Struts to v1.2.4.
// Changed base classes to use ivata masks.
//
// Revision 1.8  2004/11/03 16:00:35  colinmacleod
// Fixed generateCSS include.
//
// Revision 1.7  2004/07/18 16:38:42  colinmacleod
// Changed DynaValidatorForm to DynaActionForm.
//
// Revision 1.6  2004/07/14 22:50:26  colinmacleod
// Replaced bean:define with extended igw:bean.
//
// Revision 1.5  2004/07/14 20:59:54  colinmacleod
// Changed all occurrences of jsp:useBean to igw:bean (better tomcat compatibility).
//
// Revision 1.4  2004/07/13 19:48:09  colinmacleod
// Moved project to POJOs from EJBs.
// Applied PicoContainer to services layer (replacing session EJBs).
// Applied Hibernate to persistence layer (replacing entity EJBs).
//
// Revision 1.3  2004/03/21 21:16:34  colinmacleod
// Shortened name to ivata op.
//
// Revision 1.2  2004/02/04 23:35:59  colinmacleod
// Added generateCSS
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
// Revision 1.38  2003/04/14 06:28:54  peter
// help uncommented
//
// Revision 1.37  2003/03/25 09:42:05  peter
// remmed out help submit for 1.2.4
//
// Revision 1.36  2003/03/18 15:25:14  peter
// uncommented help
//
// Revision 1.35  2003/03/11 18:27:34  colin
// changes to allow just one webapp
//
// Revision 1.34  2003/03/06 09:48:24  peter
// fixed js warning: added notNull condition for opener
//
// Revision 1.33  2003/03/04 19:10:40  colin
// remmed out the help submit
//
// Revision 1.32  2003/03/03 16:57:12  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.31  2003/02/27 12:03:31  colin
// changed forward to redirect to avoid displaying the parameters in the browser
//
// Revision 1.30  2003/02/25 07:32:10  colin
// restructured java file paths
//
// Revision 1.29  2003/02/11 07:17:49  colin
// moved the window title to the window tag directly
//
// Revision 1.28  2003/01/27 07:18:32  colin
// major bug fixes
//
// Revision 1.27  2003/01/23 13:57:19  colin
// modifications to allow URL rewriting sessions (instead of cookie)
//
// Revision 1.26  2003/01/20 17:09:02  colin
// fixed bug where the help always comes up if you press it once
//
// Revision 1.25  2003/01/18 20:05:09  colin
// converted to struts, added javascript checking, help button
//
// Revision 1.24  2003/01/14 10:31:29  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.23  2002/12/19 16:34:45  peter
// i18n fixed
//
// Revision 1.22  2002/11/28 16:56:26  peter
// i18n finished
//
// Revision 1.21  2002/11/15 12:22:54  peter
// check for secure request added, as the first action in the page
//
// Revision 1.19  2002/11/12 10:27:58  colin
// moved tag libraries to /WEB-INF/tag
//
// Revision 1.18  2002/10/02 14:50:33  jano
// set up jndiPrefix to session
//
// Revision 1.17  2002/10/01 14:58:17  jano
// set jndiPrefix to session
//
// Revision 1.16  2002/09/30 15:26:44  colin
// corrections to radio button handling
// added topLevel handling and changes for checkLogin
//
// Revision 1.15  2002/09/26 08:17:49  colin
// improved checking that the user is logged in
// changed java code to jsp tags
//
// Revision 1.14  2002/09/25 15:29:00  colin
// added title/tooltip attributes to buttons/submits
//
// Revision 1.13  2002/09/25 08:34:22  peter
// login button tooltip
//
// Revision 1.12  2002/09/25 08:14:22  peter
// start page functionality implemented
//
// Revision 1.11  2002/09/16 14:19:36  colin
// added javascript checking
//
// Revision 1.10  2002/09/05 11:43:43  colin
// added katarina's login picture
// removed silly warning message
//
// Revision 1.9  2002/08/27 08:43:40  colin
// split tags and themes into two separate includes
//
// Revision 1.8  2002/08/11 11:59:51  colin
// Added code to remove the comment theme as well as the default theme.
//
// Revision 1.7  2002/07/04 16:23:06  colin
// changed welcome text to a database setting
//
// Revision 1.6  2002/07/01 08:06:56  colin
// bugfixes
//
// Revision 1.5  2002/06/18 08:42:50  colin
// first basic working version of library
//
// Revision 1.4  2002/06/13 11:10:59  colin
// first version with rose model and jboss  integration
//
// Revision 1.3  2002/04/30 16:23:00  colin
// changed first login page to the frames-based index.jsp (running mail subproject in the main frame)
//
// Revision 1.2  2002/04/30 15:14:55  colin
// improved integration into JBuilder environment
//
// Revision 1.1  2002/04/27 17:38:23  colin
// first compliling version in EJB/JBuilder project
//
// Revision 1.11  2002/02/05 19:49:03  colin
// updated for changes to settings class
//
// Revision 1.10  2002/01/20 23:47:29  colin
// changes coding style to match core product
// added toolbar
//
// Revision 1.9  2002/01/20 20:38:31  colin
// updated theme changes from core project
// removed _ from tag names and changed capitalisation
// updated the tag libraries to the latest versions
//
// Revision 1.8  2002/01/20 19:44:59  colin
// consolidated work done on www.ivata.com and on the laptop
//
// Revision 1.7  2001/12/12 20:26:25  colin
// fixed the following bugs:
// made display window same width as index
// made leading spaces non-breaking
// put a little header with from/to/subject information before forwarded and replied to mails
// stopped append re: to messages which are already re: (same applies to forward)
// convert people's email addresses into links (mailto)
// word wrap the messages (replies) in compose
// make the subject line spread right over the whole space in display
//
// Revision 1.6  2001/12/02 21:25:47  colin
// fixed bug when viewing the sent mail folder
// changed caveat on login screen
//
// Revision 1.5  2001/11/24 13:49:00  colin
// display, reply and forward all working
// caveat placed on the login page
//
// Revision 1.4  2001/11/05 22:10:19  colin
// first version with working themes, imutil:setProperty tag
//
// Revision 1.3  2001/10/28 20:20:14  colin
// first working version with theme support
//
// Revision 1.2  2001/10/09 20:04:18  colin
// adapted tag libraries for shop project
//
// Revision 1.1.1.1  2001/08/31 14:10:07  colin
// starting over with jsp
//
////////////////////////////////////////////////////////////////////////////////
--%>

<%@include file='/include/tags.jspf'%>
<%-- if we have logged in, go whereever we are told to --%>
<logic:present name="loginForward">
  <igw:bean id='loginForward' type='java.lang.String'/>
  <logic:redirect forward='<%=loginForward%>'/>
</logic:present>

<%-- we need to have at least a guest session... --%>
<logic:notPresent name='securitySession'>
  <logic:redirect action='/loginGuest'/>
</logic:notPresent>
<logic:notPresent name='loginForm'>
  <logic:redirect action='/loginGuest'/>
</logic:notPresent>
<logic:notPresent name='loginForm' property='javaScriptVersion'>
  <logic:redirect action='/loginGuest'/>
</logic:notPresent>

<%--
  this regenerates CSS to match the current context path at every login
  - you could remove this line and call /generateCSS once yourself after
  deploying...
--%>
<jsp:include page="/generateCSS"/>
<c:catch>
  <%@include file='/include/theme.jspf'%>
</c:catch>

<igw:getSetting id='siteName' setting='siteName' type='java.lang.String'/>
<igw:bean id='loginForm' name='loginForm' scope='session' type='org.apache.struts.action.DynaActionForm'/>
<%-- if we're in pop-up mode, don't show a title --%>

<igw:bean id='windowWidth' name='windowWidth' type='java.lang.String'/>
<igw:bean id='windowHeight' name='windowHeight' type='java.lang.String'/>

<igw:bean id='title' type='java.lang.String'><bean:message key='login.title'/></igw:bean>
<imhtml:html locale='true'>
  <igw:head title='<%=title%>' login='true' topLevel='true'>
    <%-- this javascript ensures that this window is not within an HTML frame--%>
    <script type='text/javascript'>
      <!--
      function onChangeTheme() {
          document.loginForm.submit();
      }
      if (opener != null && opener != self ) {
          opener.location.href = self.location.href;
          self.close();
      }
      // -->
    </script>
    <link rel='stylesheet' href='<%=RewriteHandling.getContextPath(request)%>/style/login.css' type='text/css' />
    <imhtml:base/>
    <meta http-equiv='Content-Type' content='text/html;charset=UTF-8; charset=utf-8'/>
  </igw:head>
  <body>
    <div align='center'>
      <imhtml:form action='/login'
          resourceFieldPath='login'
          onsubmit='if(!document.loginForm.javaScriptVersion.value) {document.loginForm.javaScriptVersion.value = 1}'>
        <html:hidden property='javaScriptVersion'/>
        <imtheme:window
            styleClass='loginWindow'
            titleKey='login.title.window'
            titleArgs='<%=Arrays.asList(new Object[] {siteName})%>'>
          <%@include file='/include/errorFrame.jspf'%>
          <imtheme:frame>
            <imtheme:framePane styleClass='hilight'>
              <%@include file='/loginMask.jspf'%>
            </imtheme:framePane>
          </imtheme:frame>
          <script type='text/javascript'>
            <!--
            document.loginForm.javaScriptVersion.value = "1.0";
            // -->
          </script>
          <script language="JavaScript1.1" type='text/javascript'>
            <!--
            document.loginForm.javaScriptVersion.value = "1.1";
            // -->
          </script>
          <script language="JavaScript1.2" type='text/javascript'>
            <!--
            document.loginForm.javaScriptVersion.value = "1.2";
            // -->
          </script>
          <script language="JavaScript1.3" type='text/javascript'>
            <!--
            document.loginForm.javaScriptVersion.value = "1.3";
            // -->
          </script>

          <imtheme:buttonFrame>
            Theme:&nbsp;
            <html:select property='theme' onchange='onChangeTheme(); return true'>
              <html:option value='last'>Last Used</html:option>
              <html:option value='round'>Round</html:option>
              <html:option value='classic'>Classic</html:option>
              <html:option value='shadow'>Shadow</html:option>
            </html:select>
            <imtheme:buttonSpacer/>
            <imhtml:submit fieldName='login'/>
            <%-- if the person pressed help, show the help window --%>
            <c:choose>
              <c:when test='<%=!StringHandling.isNullOrEmpty((String) loginForm.get("help"))%>'>
                <%-- clear the help button --%>
                <%loginForm.set("help", null);%>
                <%-- enable javascript so the help button will display --%>
                <igw:bean id='browser' name='browser' scope='session' type='com.ivata.mask.web.browser.Browser'/>
                <jsp:setProperty name='browser' property='javaScriptVersion' value='1'/>
                <imhtml:help key='login'/>
                <script type='text/javascript'>
                  <!--
                    document.loginForm.help.click();
                  // -->
                </script>
              </c:when>
              <c:otherwise>
                <%-- no help in this release !
                <imhtml:submit fieldName='help' />
                --%>
              </c:otherwise>
            </c:choose>
          </imtheme:buttonFrame>
        </imtheme:window>
      </imhtml:form>
    </div>
    <%@ include file='/theme/include/standards.jspf' %>
  </body>
</imhtml:html>
