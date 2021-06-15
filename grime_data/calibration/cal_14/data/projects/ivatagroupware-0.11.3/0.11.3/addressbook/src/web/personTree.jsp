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
<%@page import='com.ivata.groupware.business.addressbook.person.group.GroupDO' %>
<%@page import='com.ivata.mask.util.StringHandling' %>
<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: personTree.jsp,v 1.5 2005/04/30 13:04:12 colinmacleod Exp $
//
// Display all people in the address book in a tree with a checkbox beside each
// one. This can then be included in a page directly, or displayed within an
// iframe.
//
// Since: ivata groupware 0.9 (2002-09-12)
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
// $Log: personTree.jsp,v $
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
// Revision 1.7  2004/11/12 15:57:09  colinmacleod
// Removed dependencies on SSLEXT.
// Moved Persistence classes to ivata masks.
//
// Revision 1.6  2004/11/03 15:40:25  colinmacleod
// Changed todo comments to all caps.
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
// Revision 1.1.1.1  2004/01/27 20:58:02  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.5  2003/11/18 09:57:11  jano
// commiting all forgoten staff
//
// Revision 1.4  2003/11/13 16:03:16  jano
// commitng everything to CVS
// can deploy and application is ruuning, can login into
//
// Revision 1.3  2003/11/07 14:54:15  jano
// commitng after fixing some bugs
//
// Revision 1.15  2003/08/22 09:23:52  jano
// people are only in addressBooks
//
// Revision 1.14  2003/07/18 08:27:32  peter
// oscache disabled (time='0')
//
// Revision 1.13  2003/06/10 10:25:40  peter
// changed key naming to suit  multiapp
//
// Revision 1.12  2003/06/10 05:58:02  peter
// cache added, hardcoded keys for now
//
// Revision 1.11  2003/06/09 12:26:31  peter
// the tree is wrapped in oscache
//
// Revision 1.10  2003/06/02 23:39:07  colin
// added onlyUsers stuff
//
// Revision 1.9  2003/04/14 13:09:58  peter
// fixed : link -> rewrite for form action
//
// Revision 1.8  2003/03/04 00:27:52  colin
// made label bold, left justified
//
// Revision 1.7  2003/02/25 17:13:40  colin
// fixed display, recipients of mail subsystem
//
// Revision 1.6  2003/02/25 07:28:54  colin
// restructured java file paths
//
// Revision 1.5  2003/01/28 15:17:27  colin
// tidied up sslext & checklogin
//
// Revision 1.4  2003/01/23 16:52:21  peter
// links and forms changed to sslext, checkLogin added
//
// Revision 1.3  2003/01/14 10:38:57  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.2  2002/11/22 16:44:38  peter
// localisation
//
// Revision 1.1  2002/10/15 15:53:39  colin
// first version. used in email contacts.
//
////////////////////////////////////////////////////////////////////////////////
--%>
<% // TODO: remove this when we're sure it is all working
session.removeAttribute("groupPersonTheme");%>
<%@include file='/include/tags.jspf'%>
<igw:checkLogin />

<%@include file='/addressBook/theme/default.jspf'%>
<%@include file='/include/theme.jspf'%>

<%-- if you specify embedded, then only the contents of the body are shown --%>
<imhtml:html locale='true'>
  <igw:head bundle='addressBook' titleKey='personTree.title'><imhtml:base/></igw:head>
  <body class='tree'>
    <igw:bean id='action'><imhtml:rewrite page='/addressBook/personTree.jsp'/></igw:bean>
    <form name='personTreeForm' action='<%=action%>' method='post'>

      <%-- only show groups we can see --%>
      <%@include file='/addressBook/include/tree.jspf'%>
      <jsp:setProperty name='filter' property='access' value='<%=com.ivata.groupware.business.addressbook.person.group.right.RightConstants.ACCESS_VIEW%>'/>
      <jsp:setProperty name='personTreeModel' property='filter' value='<%=filter%>'/>
      <jsp:setProperty name='personTreeModel' property='includePeople' value='true'/>
      <jsp:setProperty name='personTreeModel' property='rootId' value='<%=com.ivata.groupware.business.addressbook.person.group.GroupConstants.ADDRESS_BOOK%>'/>
      <%-- if there is a request parameter for onlyUsers, set that --%>
      <logic:present parameter='onlyUsers'>
        <jsp:setProperty name='personTreeModel' property='onlyUsers' value='true'/>
      </logic:present>

      <%--
        all ids are stored in one hidden field, separated by ";".
        this lets us use an iframe
      --%>
      <script language='javascript'>
        <!--
        function onCheckBox(checkBoxId) {
            var checkBox = document.getElementById(checkBoxId);
            if (checkBox.checked) {
                addId(checkBox.value);
            } else {
                removeId(checkBox.value);
            }
        }
        function addId(addValue) {
            var people = window.top.document.getElementById("people");
            if (people == null) {
                return;
            }
            <%--
            // see if the id is already there - if so, ignore it
            --%>
            if (people.value.indexOf(addValue) != -1) {
                return;
            }
            if (people.value.length != 0) {
              people.value += ";";
            }
            people.value += addValue;
        }
        function removeId(removeValue) {
            var people = window.top.document.getElementById("people");
            if (people == null) {
                return;
            }
            <%--
            // see if the id is already there - if so, remove it
            --%>
            var position = people.value.indexOf(removeValue);
            if (position != -1) {
                <%--
                // if this is the first one, there is no semi before it
                --%>
                var beforeThis = "";
                if (position > 0) {
                    beforeThis = people.value.substring(0, position - 1);
                }

                var positionSemi = people.value.indexOf(";", position);
                <%--
                // if there is no semi, cut to the end of the string
                --%>
                if (positionSemi == -1) {
                    people.value = beforeThis;
                } else {
                    people.value = beforeThis + people.value.substring(positionSemi);
                }
            }
        }
        // -->
      </script>
      <strong><bean:message bundle='addressBook' key='personTree.label.contacts'/></strong>
      <igw:bean id='personTreeNodeRenderer' scope='page' type='com.ivata.groupware.web.tree.person.PersonTreeNodeRenderer'/>
      <igwtree:tree
          model='<%=personTreeModel%>'
          renderer='<%=personTreeNodeRenderer%>'
          treeName='chairPerson'
          defaultOpen='true'
          themeName='groupPersonTheme'/>

      <%--
        this script does the opposite - it goes thro' all of the selected
        people and checks the appropriate check boxes
      --%>


      <script language='javascript'>
        <!--
          var people = window.top.document.getElementById("people");
          var position = 0;
          var positionNext;
          var value;
          <%--
          // we only want to do the loop if there _are_ people
          --%>
          while ((people != null) && (people.value.length > 0)) {
              positionNext = people.value.indexOf(";", position);
              <%--
              // last one
              --%>
              if (positionNext == -1) {
                  <%--
                  // there will be one more
                  --%>
                  value = people.value.substring(position);
              } else {
                  value = people.value.substring(position, positionNext);
              }
              var checkBox = document.getElementById("people" + value);
              checkBox.checked = true;
              if (positionNext == -1) {
                  break;
              }
              <%--
              // + 1 = skip semicolon
              --%>
              position = positionNext + 1;
          }
        -->
      </script>

    </form>
  </body>
</imhtml:html>
