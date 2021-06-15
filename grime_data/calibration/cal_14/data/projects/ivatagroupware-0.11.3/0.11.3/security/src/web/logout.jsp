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

<%@page import='com.ivata.mask.util.StringHandling'%>

<%@page import='org.apache.struts.Globals'%>
<%@page import="org.apache.struts.taglib.TagUtils"%>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: logout.jsp,v 1.4 2005/04/28 18:47:05 colinmacleod Exp $
//
// <p>Leave our illustrious intranet.</p>
//
// Since: ivata groupware 0.9 (2001-09-29)
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
// $Log: logout.jsp,v $
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
// Revision 1.6  2004/12/27 14:52:02  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.5  2004/07/31 10:46:08  colinmacleod
// Removed reference to ApplicationServer class.
// Removed securitySession from the session (rather than username/settings).
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
// Revision 1.2  2003/10/17 14:14:22  jano
// changed names intranet -> portal
//
// Revision 1.1.1.1  2003/10/13 20:47:29  colin
// Restructured portal into subprojects
//
// Revision 1.21  2003/07/05 16:49:36  colin
// fixed context path bugs and path for login
//
// Revision 1.20  2003/06/11 10:00:45  peter
// imhtml:img replaed with hardcoded img, we don't have locale after session end...
//
// Revision 1.19  2003/06/09 12:11:23  peter
// img and html:img replaced with size-aware imhtml:img
//
// Revision 1.18  2003/06/06 13:35:25  jano
// fixing image size
//
// Revision 1.17  2003/03/13 07:19:52  peter
// try to kill the entire session by logout
//
// Revision 1.16  2003/03/11 18:27:34  colin
// changes to allow just one webapp
//
// Revision 1.15  2003/03/04 19:36:10  colin
// used a simple html head tag
//
// Revision 1.14  2003/03/04 14:42:01  colin
// added session.ivalidate
//
// Revision 1.13  2003/01/27 07:16:51  colin
// update copyright notice
// changed styleClass
//
// Revision 1.12  2003/01/14 10:31:46  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.11  2002/12/17 12:02:48  peter
// fixed i18n
//
// Revision 1.10  2002/11/12 10:28:03  colin
// moved tag libraries to /WEB-INF/tag
//
// Revision 1.9  2002/09/30 15:26:59  colin
// changes for new checkLogin tag
//
// Revision 1.8  2002/09/16 14:31:20  colin
// moved the window down a bit by adding br tags
//
// Revision 1.7  2002/09/16 14:30:55  jano
// *** empty log message ***
//
// Revision 1.5  2002/08/27 08:43:40  colin
// split tags and themes into two separate includes
//
// Revision 1.4  2002/07/01 08:07:33  colin
// moved SetPropertyTag to util from webgui
//
// Revision 1.3  2002/06/18 08:42:50  colin
// first basic working version of library
//
// Revision 1.2  2002/06/13 11:10:59  colin
// first version with rose model and jboss  integration
//
// Revision 1.1  2002/04/30 15:12:47  colin
// first version in JBuilder/EJB project
//
// Revision 1.8  2002/02/05 19:49:03  colin
// updated for changes to settings class
//
// Revision 1.7  2002/01/20 23:47:29  colin
// changes coding style to match core product
// added toolbar
//
// Revision 1.6  2002/01/20 20:38:31  colin
// updated theme changes from core project
// removed _ from tag names and changed capitalisation
// updated the tag libraries to the latest versions
//
// Revision 1.5  2002/01/20 19:44:59  colin
// consolidated work done on www.ivata.com and on the laptop
//
// Revision 1.4  2001/11/08 19:40:03  colin
// added code to remove the user object from the session, thus logging out
//
// Revision 1.3  2001/11/05 22:10:19  colin
// first version with working themes, imutil:setProperty tag
//
// Revision 1.2  2001/10/28 20:20:14  colin
// first working version with theme support
//
// Revision 1.1.1.1  2001/10/06 15:48:04  colin
// initial import of jsp version into cvs
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%-- can't use /include/tags.jspf as we don't want to be sent to login page --%>
<%@taglib uri='/WEB-INF/tag/ivata/ivatatheme.tld' prefix='imtheme'%>
<%@taglib uri='/WEB-INF/tag/ivata/ivatathemedef.tld' prefix='imthemedef'%>
<%@taglib uri='/WEB-INF/tag/ivata/ivatautil.tld' prefix='imutil'%>
<%@taglib uri='/WEB-INF/tag/ivata/ivatagroupware.tld' prefix='igw'%>
<%@taglib uri='/WEB-INF/tag/ivata/ivatahtml.tld' prefix='imhtml'%>

<%@taglib uri='/WEB-INF/tag/jstl/c-rt.tld' prefix='c'%>

<%@taglib uri='/WEB-INF/tag/struts/struts-bean.tld' prefix='bean'%>
<%@taglib uri='/WEB-INF/tag/struts/struts-html.tld' prefix='html'%>

<c:catch>
  <%@include file='/include/theme.jspf'%>
</c:catch>

<imhtml:html locale='true'>
<%-- login=true is set because otherwise it would forward to login.jsp (after the second request made by 'topLevel' javaScript) --%>
<igw:head topLevel='true' keepOnTop='true' titleKey='logout.title'>
  <imhtml:base/>
</igw:head>
<body>
<div align='center'><br />
<br />
<br />
<br />
<imtheme:window
    styleClass='loginWindow'
    titleKey='logout.title'>
  <imtheme:frame>
    <%-- we only want to show the error pane if there really is an error--%>
    <imtheme:framePane styleClass='hilight'>
      <table cellpadding='0' cellspacing='0' width='100%' height='100%'>
        <tr height='100%'>
          <td style='vertical-align: middle;'><img align='left'
            src='images/logout.gif' width='150' height='170' /></td>
          <td><img align='left' src='images/empty.gif' width='20'
            height='1' /></td>
          <td style='vertical-align: middle;'><igw:bean id='path'>
            <imhtml:rewrite page='/login.jsp' />
          </igw:bean> <bean:message key='logout.label.logBack'
            arg0='<%=path%>' /></td>
        </tr>
      </table>
    </imtheme:framePane>
  </imtheme:frame>
</imtheme:window></div>
</body>
</imhtml:html>
<%
  // to log-out, remove essentials and try kill the session
  session.removeAttribute( "securitySession" );
  session.invalidate();
%>
