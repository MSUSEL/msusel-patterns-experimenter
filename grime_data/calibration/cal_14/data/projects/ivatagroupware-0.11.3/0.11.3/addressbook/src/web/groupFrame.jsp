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
// $Id: groupFrame.jsp,v 1.3 2005/04/28 18:47:09 colinmacleod Exp $
//
// <p>Contains the group entry/modification pane, and the group list.</p>
//
// Since: ivata groupware 0.9 (2001-12-12)
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
// $Log: groupFrame.jsp,v $
// Revision 1.3  2005/04/28 18:47:09  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.2  2005/04/09 17:19:12  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:50:47  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.6  2004/12/31 18:33:04  colinmacleod
// Changes to apply ivata masks instead of hard-coded mask.
// Currently displays value objects for addressbooks.
//
// Revision 1.5  2004/11/12 15:57:09  colinmacleod
// Removed dependencies on SSLEXT.
// Moved Persistence classes to ivata masks.
//
// Revision 1.4  2004/07/14 22:50:20  colinmacleod
// Replaced bean:define with extended igw:bean.
//
// Revision 1.3  2004/07/14 20:59:44  colinmacleod
// Changed all occurrences of jsp:useBean to igw:bean (better tomcat compatibility).
//
// Revision 1.2  2004/03/21 21:16:20  colinmacleod
// Shortened name to ivata op.
//
// Revision 1.1  2004/03/03 20:27:38  colinmacleod
// Updated webapp structure. Moved core items to core subproject.
//
// Revision 1.1.1.1  2004/01/27 20:58:01  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.1.1.1  2003/10/13 20:50:22  colin
// Restructured portal into subprojects
//
// Revision 1.12  2003/06/10 05:57:38  peter
// img and html:img replaced with size-aware imhtml:img
//
// Revision 1.11  2003/02/18 10:38:26  colin
// conversion to Struts and pop-ups
//
// Revision 1.10  2003/01/30 08:56:53  colin
// improvements for struts/sslext
//
// Revision 1.9  2003/01/28 07:19:41  colin
// re-added the group tree path
// both trees now load at the same time again
//
// Revision 1.8  2003/01/23 16:52:21  peter
// links and forms changed to sslext, checkLogin added
//
// Revision 1.7  2003/01/14 10:38:56  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.6  2002/12/12 15:06:48  jano
// right frame is refreshing left frame on start
//
// Revision 1.5  2002/11/21 10:08:51  peter
// localisation
//
// Revision 1.4  2002/10/08 16:21:08  colin
// fixed topLevel handling for pop-ups
//
// Revision 1.3  2002/08/26 09:21:33  colin
// refined group implementation with server-side tree model
//
// Revision 1.2  2002/06/18 08:42:51  colin
// first basic working version of library
//
// Revision 1.1  2002/06/13 11:10:59  colin
// first version with rose model and jboss  integration
//
// Revision 1.1  2002/01/24 13:54:40  colin
// renamed:
// group_list.jsp to groupList.jsp
// group_frame.jsp to groupFrame.jsp
// person_list.jsp to personList.jsp
// person_list_item.jsp to personListItem.jsp
//
// Revision 1.3  2002/01/20 12:22:45  colin
// reformatted addressbook without frames
//
// Revision 1.2  2002/01/03 12:57:47  colin
// added group membership functionality
//
// Revision 1.1  2001/12/26 21:27:27  colin
// first working version of groups
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@ include file='/include/tags.jspf'%>
<%@ include file='/include/theme.jspf'%>

<igw:bean id='addressBookGroupFrameForm'
    name='addressBookGroupFrameForm'
    scope='request'
    type='org.apache.struts.action.DynaActionForm'/>

<igw:checkLogin />


<imhtml:html locale='true'>
  <imhtml:base/>
  <igw:head bundle='addressBook' titleKey='group.title' topLevel='true'>
    <script language="JavaScript" type="text/javascript">
      <!-- // this little script ensures we stay on top :-)
          if (window != top) {
              top.location.href = location.href;
          }
          window.focus();
      // -->
    </script>
  </igw:head>

  <imutil:map id='loadingParams'>
    <imutil:mapEntry name='page'><bean:write name="addressBookGroupFrameForm" property="menuPage"/></imutil:mapEntry>
  </imutil:map>
  <igw:bean id='menuFrameName' name="addressBookGroupFrameForm" property="menuFrameName"/>
  <igw:bean id='menuUri'><imhtml:rewrite page='/util/loading.jsp' name='loadingParams'/></igw:bean>

  <imutil:mapEntry mapName='loadingParams' name='page'><bean:write name="addressBookGroupFrameForm" property="contentPage"/></imutil:mapEntry>
  <igw:bean id='contentFrameName' name="addressBookGroupFrameForm" property="contentFrameName"/>
  <igw:bean id='contentUri'><imhtml:rewrite page='/util/loading.jsp' name='loadingParams'/></igw:bean>

  <frameset cols="260,*">
    <frame src="<%=menuUri%>" name="<%=menuFrameName%>" border='0' marginheight='0' marginwidth='0' frameborder='yes' scrolling='no'/>
    <frame src='<%=contentUri %>' name='<%=contentFrameName%>' border='0' marginheight='0' marginwidth='10' frameborder='no' scrolling='no'/>
    <noframes>
      <bean:message key='alert.noframes'/>
    </noframes>
  </frameset>
</imhtml:html>

