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

<%@page import='com.ivata.groupware.business.addressbook.person.*' %>
<%@page import='com.ivata.mask.util.StringHandling' %>
<%@page import='com.ivata.groupware.business.addressbook.person.group.right.RightConstants' %>

<%@page import='java.util.Arrays' %>
<%@page import='java.util.ArrayList' %>
<%@page import='java.util.Collection' %>
<%@page import='java.util.List' %>
<%@page import='java.util.Vector' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: member.jsp,v 1.3 2005/04/10 20:10:09 colinmacleod Exp $
//
// Lets you change the groups a person is a member of.
//
// Since: ivata groupware 0.9 (2002-01-01)
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
// $Log: member.jsp,v $
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
// Revision 1.1.1.1  2004/01/27 20:58:01  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.4  2003/11/13 16:03:16  jano
// commitng everything to CVS
// can deploy and application is ruuning, can login into
//
// Revision 1.3  2003/11/07 14:54:15  jano
// commitng after fixing some bugs
//
// Revision 1.18  2003/08/21 09:42:34  jano
// fixing for new addressBook extension
//
// Revision 1.17  2003/07/25 11:45:40  jano
// adding functionality for addressBook extension
//
// Revision 1.16  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.15  2003/02/25 07:28:54  colin
// restructured java file paths
//
// Revision 1.14  2003/02/20 14:40:49  colin
// removed default caption from other groups
// no exclude branch in other groups (even the group is shown)
//
// Revision 1.13  2003/02/18 10:38:26  colin
// conversion to Struts and pop-ups
//
// Revision 1.12  2003/01/14 10:38:56  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.11  2003/01/10 07:48:12  jano
// show only those groups where user can add
// if user created contact -> he can amend if he has not AMEND right
//
// Revision 1.10  2002/12/19 12:53:54  jano
// contact should be readOnly
//
// Revision 1.9  2002/12/12 15:07:31  jano
// filter of group
//
// Revision 1.8  2002/11/21 14:49:26  peter
// localisation
//
// Revision 1.7  2002/10/15 10:00:05  colin
// fixed bug when no groups are chosen
//
// Revision 1.6  2002/09/19 14:37:57  colin
// made into a tab instead of a popup
// added userName bean
// fixed selected id handling
//
// Revision 1.5  2002/07/01 08:07:56  colin
// moved SetPropertyTag to util from webgui
//
// Revision 1.4  2002/06/28 13:15:58  colin
// first addressbook release
//
// Revision 1.3  2002/06/17 07:35:06  colin
// improved and extended documentation
//
// Revision 1.2  2002/06/13 15:45:21  peter
// brought over to peter, fixed bugs in webgui property-settings
//
// Revision 1.1  2002/06/13 11:10:59  colin
// first version with rose model and jboss  integration
//
// Revision 1.4  2002/02/10 14:33:24  colin
// corrections made whilst adding topic group rights
//
// Revision 1.3  2002/01/22 21:55:06  colin
// changed the object model path from com.ivata.groupware.address to com.ivata.groupware.business.addressbook.person
//
// Revision 1.2  2002/01/20 12:22:45  colin
// reformatted addressbook without frames
//
// Revision 1.1  2002/01/03 12:57:47  colin
// added group membership functionality
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<%@include file='/include/theme.jspf'%>

<%-- get the addressBook object --%>
<%@include file='/addressBook/include/addressBook.jspf'%>
<%-- only show groups we can change --%>

<igw:bean id='addressBookPersonForm' scope='session' type='com.ivata.groupware.business.addressbook.struts.PersonForm'/>
<igw:bean id='userGroups' name='addressBookPersonForm' property='userGroups' type='java.util.List'/>

<igw:bean id='person' name='addressBookPersonForm' property='person' type='com.ivata.groupware.business.addressbook.person.PersonDO'/>

<imhtml:form refersTo='personFormTag' resourceFieldPath='member'>
  <imtheme:framePane>
    <table border='0' cellpadding='4' cellspacing='0' align='center' height='100%'>
      <tr height='100%'>
        <td class='fieldCaption'><imhtml:label fieldName='memberOf'/></td>
        <td>

          <select name='userGroups' id='userGroups' disabled style='width: 100%; height: 100%;' multiple='multiple'>
            <c:forEach var='group' items='<%=userGroups%>'>
              <igw:bean id='group' type='com.ivata.groupware.business.addressbook.person.group.GroupDO'/>
              <option value='<%=group.getId()%>' <%=person.getUser().getGroups().contains(group) ? "selected='selected'" : ""%> >
                <%=group.getName()%>
              </option>
            </c:forEach>
          </select>

        </td>
      </tr>
    </table>
  </imtheme:framePane>
</imhtml:form>
