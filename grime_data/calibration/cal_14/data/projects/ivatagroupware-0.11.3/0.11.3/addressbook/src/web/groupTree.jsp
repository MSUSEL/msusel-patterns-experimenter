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

<%@page import="com.ivata.groupware.business.addressbook.person.group.GroupConstants" %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: groupTree.jsp,v 1.5 2005/04/30 13:04:12 colinmacleod Exp $
//
// <p>This page draws a tree giving access to all groups.</p>
//
// Since: ivata groupware 0.9 (2001-12-11)
// Author: Colin MacLeod <colin.macleod@ivata.com>
// $Revision: 1.5 $
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
// $Log: groupTree.jsp,v $
// Revision 1.5  2005/04/30 13:04:12  colinmacleod
// Fixes reverting id type from String to Integer.
//
// Revision 1.4  2005/04/28 18:47:09  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.3  2005/04/10 20:10:09  colinmacleod
// Added new themes.
// Changed id type to String.
// Changed i tag to em and b tag to strong.
// Improved PicoContainerFactory with NanoContainer scripts.
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
// Revision 1.1.1.1  2004/01/27 20:58:01  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.5  2003/11/18 09:56:52  jano
// commiting all forgoten staff
//
// Revision 1.4  2003/11/13 16:03:16  jano
// commitng everything to CVS
// can deploy and application is ruuning, can login into
//
// Revision 1.3  2003/11/07 14:54:15  jano
// commitng after fixing some bugs
//
// Revision 1.22  2003/08/05 14:54:26  jano
// addressBook extension
//
// Revision 1.21  2003/07/18 08:27:26  peter
// oscache disabled (time='0')
//
// Revision 1.20  2003/06/18 07:40:41  peter
// fixed: the cache was stupidly around the iframe
//
// Revision 1.19  2003/06/10 10:25:40  peter
// changed key naming to suit  multiapp
//
// Revision 1.18  2003/06/10 05:58:02  peter
// cache added, hardcoded keys for now
//
// Revision 1.17  2003/03/04 00:29:30  colin
// removed bundle from html:html tag, reverted to using bean:message
//
// Revision 1.16  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.15  2003/02/25 07:28:54  colin
// restructured java file paths
//
// Revision 1.14  2003/01/30 08:56:53  colin
// improvements for struts/sslext
//
// Revision 1.13  2003/01/23 16:52:21  peter
// links and forms changed to sslext, checkLogin added
//
// Revision 1.12  2003/01/14 10:38:56  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.11  2002/12/12 15:07:15  jano
// default filter is for ACCESS_VIEW
//
// Revision 1.10  2002/11/21 10:33:51  peter
// localisation
//
// Revision 1.9  2002/10/08 16:21:08  colin
// fixed topLevel handling for pop-ups
//
// Revision 1.8  2002/09/19 14:37:13  colin
// added userName bean
//
// Revision 1.7  2002/08/26 09:21:33  colin
// refined group implementation with server-side tree model
//
// Revision 1.6  2002/08/15 09:16:11  peter
// remmed out tree (wouldn't compile)
//
// Revision 1.5  2002/07/01 08:07:56  colin
// moved SetPropertyTag to util from webgui
//
// Revision 1.4  2002/06/28 13:15:58  colin
// first addressbook release
//
// Revision 1.3  2002/06/18 08:42:51  colin
// first basic working version of library
//
// Revision 1.2  2002/06/13 15:45:21  peter
// brought over to peter, fixed bugs in webgui property-settings
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

<%@include file='/include/tags.jspf'%>
<igw:checkLogin />

<%@include file='/addressBook/theme/default.jspf'%>
<%@include file='/include/theme.jspf'%>

<%-- only show groups we can see --%>
<%@include file='/addressBook/include/tree.jspf'%>
<%@include file='/addressBook/include/addressBook.jspf'%>

<igw:bean id='empty' name="addressBookGroupTreeForm" property="empty" type='java.lang.Boolean'/>
<igw:bean id='groupForm' name='addressBookGroupTreeForm'/>
<%@ include file="/addressBook/include/groupBeans.jspf"%>

<igw:bean id='personTreeNodeRenderer' scope='page' type='com.ivata.groupware.web.tree.person.PersonTreeNodeRenderer'/>

<imhtml:html locale='true'>
  <igw:head topLevel='true' keepOnTop='false' >
    <imhtml:base/>
    <script language="JavaScript" type="text/javascript">
      <!--
        function refreshAddressBookCombo() {
          document.addressBookForm.submit();
        }
      // -->
    </script>
  </igw:head>
  <body class='dialog'>
    <imtheme:window themeName='groupTheme'>

      <igw:bean id='comboAddressBookAction'><imhtml:rewrite page='/addressBook/groupTree.action'/></igw:bean>
      <form name='addressBookForm' action='<%=comboAddressBookAction%>'>
        <input name='mode' value='group' type='hidden'/>
        <input name='idIsAddressBook' value='true' type='hidden'/>
        <input name='openFolder' type='hidden'/>
        <input name='closeFolder' type='hidden'/>

        <igw:bean id='selectedId' name="addressBookGroupTreeForm" property="selectedId" type='java.lang.Integer'/>
        <imtheme:frame>
          <imtheme:framePane>
            <select onChange='refreshAddressBookCombo()'
                name='selectedId'
                id='comboAddressBook'
                style='width: 100%'>
              <logic:iterate id="element" name="groupMap">
                <igw:bean id='key' name="element" property="key" type='java.lang.String'/>
                <option value='<%=key%>' <%=selectedId.toString().equals(key) ? "selected='selected'" : ""%>>
                  <bean:write name="element" property="value"/>
                </option>
              </logic:iterate>
            </select>
          </imtheme:framePane>

          <imtheme:framePane>
            <imhtml:iframe
                styleId='stretchIFrame'
                styleClass="tree"
                frameName='ivataGroupTreeIFrame'>
              <%--
                don't show an empty tree - if there are no groups in this
                address book, just say that
              --%>
              <c:choose>
                <c:when test="<%=empty.booleanValue()%>">
                  <em>There are no groups in this address book yet.</em>
                </c:when>
                <c:otherwise>
                  <jsp:setProperty name='personTreeModel' property='filter' value='<%=null%>'/>
                  <jsp:setProperty name='personTreeModel' property='rootId' value='<%=selectedId%>'/>
                  <strong><bean:message key='groupTree.field.groups' bundle='addressBook'/></strong>
                  <igwtree:tree model='<%=personTreeModel%>'
                                 treeName='groupTree'
                                 defaultOpen='false'
                                 renderer='<%=personTreeNodeRenderer%>'
                                 formName='window.document.addressBookForm'/>
                </c:otherwise>
              </c:choose>
            </imhtml:iframe>
          </imtheme:framePane>
        </imtheme:frame>
      </form>
    </imtheme:window>
    <%--
      only refresh the right panel if the right panel has not just refreshed us!
      (don't want to go 'round in circles)
    --%>
    <logic:notPresent parameter="inputMaskInvoked">
      <script language="JavaScript" type="text/javascript">
        <!--
          window.top.<%=contentFrameName%>.location.href = "<%=newURL%>";
        // -->
      </script>
    </logic:notPresent>
    <%@include file='/include/script/fixPopUp.jspf'%>
  </body>
</imhtml:html>