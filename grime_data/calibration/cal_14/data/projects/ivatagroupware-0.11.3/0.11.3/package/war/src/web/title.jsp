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
<%@page import="com.ivata.mask.util.StringHandling" %>
<%@page import="com.ivata.mask.web.RewriteHandling"%>
<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: title.jsp,v 1.5 2005/04/28 18:47:06 colinmacleod Exp $
//
// <p>Title frame: displays your logo, the user information and the search
// box.</p>
//
// Since: ivata groupware 0.9 (2001-09-09)
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
// $Log: title.jsp,v $
// Revision 1.5  2005/04/28 18:47:06  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.4  2005/04/10 20:10:11  colinmacleod
// Added new themes.
// Changed id type to String.
// Changed i tag to em and b tag to strong.
// Improved PicoContainerFactory with NanoContainer scripts.
//
// Revision 1.3  2005/04/09 17:19:54  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.2  2005/03/16 21:32:00  colinmacleod
// Fixed link to groupware.ivata.org.
//
// Revision 1.1.1.1  2005/03/10 17:49:21  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.7  2004/11/12 16:03:00  colinmacleod
// Removed dependencies on SSLEXT.
// Moved Persistence classes to ivata masks.
//
// Revision 1.6  2004/07/14 22:50:26  colinmacleod
// Replaced bean:define with extended igw:bean.
//
// Revision 1.5  2004/07/13 19:48:09  colinmacleod
// Moved project to POJOs from EJBs.
// Applied PicoContainer to services layer (replacing session EJBs).
// Applied Hibernate to persistence layer (replacing entity EJBs).
//
// Revision 1.4  2004/03/21 21:16:34  colinmacleod
// Shortened name to ivata op.
//
// Revision 1.3  2004/02/10 22:09:29  colinmacleod
// Turned off SSL
//
// Revision 1.2  2004/02/04 23:52:12  colinmacleod
// Changed URL to ivata.org.
//
// Revision 1.1.1.1  2004/01/27 20:59:20  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.2  2003/10/17 14:14:22  jano
// changed names intranet -> portal
//
// Revision 1.1.1.1  2003/10/13 20:47:37  colin
// Restructured portal into subprojects
//
// Revision 1.27  2003/07/06 20:37:05  colin
// Made main pages secure to stop IE warning
//
// Revision 1.26  2003/07/04 06:59:58  peter
// the query input field closer to search icon
//
// Revision 1.25  2003/06/10 11:30:58  jano
// still fixing image size
//
// Revision 1.24  2003/06/10 09:07:01  jano
// fixing search inputBox size for opera
//
// Revision 1.23  2003/06/10 08:27:27  jano
// fixing size of image
//
// Revision 1.22  2003/06/09 12:12:04  peter
// img and html:img replaced with size-aware imhtml:img
//
// Revision 1.21  2003/06/05 11:00:29  peter
// fixed apostrophe error in favorite title
//
// Revision 1.20  2003/03/27 20:42:20  peter
// favorites javascript changed, works with https now
//
// Revision 1.19  2003/03/05 15:21:57  colin
// added parent target to getitnow
//
// Revision 1.18  2003/03/04 14:28:40  colin
// moved favorite adding to struts
//
// Revision 1.17  2003/02/25 07:32:11  colin
// restructured java file paths
//
// Revision 1.16  2003/01/31 09:35:28  colin
// changed the style class of the search input box to mandatory
//
// Revision 1.15  2003/01/27 16:10:32  peter
// fixed targets and javascript
//
// Revision 1.14  2003/01/23 13:57:19  colin
// modifications to allow URL rewriting sessions (instead of cookie)
//
// Revision 1.13  2003/01/14 10:38:00  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.12  2003/01/09 09:45:28  jano
// fixing bug
//
// Revision 1.11  2003/01/07 15:59:19  jano
// fixing bug
//
// Revision 1.10  2003/01/07 14:55:22  colin
// changed taglibs over to include
//
// Revision 1.9  2002/11/28 16:56:26  peter
// i18n finished
//
// Revision 1.8  2002/11/12 10:28:31  colin
// moved tag libraries to /WEB-INF/tag
//
// Revision 1.7  2002/10/11 13:49:23  peter
// javascript exception catched when try to add an external page to favorites
//
// Revision 1.6  2002/09/30 06:03:39  peter
// add to favorites implemented
//
// Revision 1.5  2002/09/26 08:25:10  colin
// fixed tooltip/title attributes
//
// Revision 1.4  2002/09/09 12:49:14  colin
// added favorite handling and removed siteMotto
//
// Revision 1.3  2002/08/19 07:45:05  colin
// moved over to resin for development
//
// Revision 1.2  2002/06/17 07:35:09  colin
// improved and extended documentation
//
// Revision 1.1  2002/06/13 11:10:59  colin
// first version with rose model and jboss  integration
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>


<igw:checkLogin />

<igw:getSetting id="demoVersion" setting="demoVersion" type='java.lang.Boolean'/>
<igw:getSetting id="siteDefaultForward" setting="siteDefaultForward" type="java.lang.String"/>
<igw:getSetting id="siteLogo" setting="siteLogo" type="java.lang.String"/>

<imhtml:html locale='true'>
  <igw:head>
    <imhtml:base/>
    <script language="JavaScript" type="text/javascript">
      <igw:bean id='alertExternal'><bean:message key='title.alert.externalPageToFavorites' /></igw:bean>
      <!--

      function setFavorites() {
        <logic:present parameter='favoriteURL'>
          document.navigationFavoriteForm.favorite.value = "<%= StringHandling.getNotNull(request.getParameter("favoriteTitle"),"") %>";
          document.navigationFavoriteForm.uRL.value = "<%= request.getParameter("favoriteURL") %>";
        </logic:present>
      }

      <%--
        // the time out gives the main page time to refresh. in the head tag,
        // we set the favorite and URL
      --%>

      function addToFavorites() {
          document.navigationFavoriteForm.uRL.value = top.ivataMain.location.href;
          document.navigationFavoriteForm.favorite.value = top.ivataMain.document.title;
          document.navigationFavoriteForm.submit();
      }
      // -->
    </script>
  </igw:head>
  <igw:bean id="homeTitle"><bean:message key="title.homeLink.title" /></igw:bean>
  <igw:bean id="favoriteAlt"><bean:message key="title.favoritesAdd.title" /></igw:bean>
  <igw:bean id="favoriteTitle"><bean:message key="title.favoritesAdd.title" /></igw:bean>
  <igw:bean id="searchAction"><imhtml:rewrite page="/search.jsp"/></igw:bean>
  <igw:bean id='searchTitle'><bean:message key="title.search.title" /></igw:bean>
  <%siteLogo = "/images/logo.gif";%>
  <body onLoad='setFavorites()'>
    <div class="siteLogo">
      <a target='_parent'
          title='go to ivata groupware site'
          href='http://groupware.ivata.org'><imhtml:img
            alt='ivata groupware'
            page='<%=siteLogo%>'/></a>
    </div>
    <form target='ivataMain' action='<%=searchAction%>' method='post'>
      <div class="links">
        <div class="link">
          <imhtml:link target='ivataMain' forward='<%=siteDefaultForward%>'>
            <imhtml:img
                alt='Home'
                title='<%=homeTitle%>'
                page='/images/menu/home.gif'/>
          </imhtml:link>
        </div>
        <div class="link">
          <a href='' onclick='addToFavorites();return false'><imhtml:img
                alt='<%=favoriteAlt%>'
                title='<%=favoriteTitle%>'
                page='/images/menu/favorite.gif'/></a>
        </div>
        <%-- for ivata groupware 0.10, search has been left out
        <div class="search">
          <div class="searchBox">
            <input class='mandatory' name='query' size='20' length='20'/>
          </div>
          <div class="searchButton" title='<%=searchTitle%>'>
            <input type='image' src='<%= RewriteHandling.getContextPath(request)
                + "/images/search.gif" %>' border='0' align='middle'>
          </div>
        </div>
        --%>
      </div>
    </form>
    <%-- this little hidden form is used to add favorites --%>
    <imhtml:form action='/navigation/addFavorite' target='ivataLeft'>
      <html:hidden property='favorite'/>
      <html:hidden property='uRL'/>
    </imhtml:form>
  </body>
</imhtml:html>
