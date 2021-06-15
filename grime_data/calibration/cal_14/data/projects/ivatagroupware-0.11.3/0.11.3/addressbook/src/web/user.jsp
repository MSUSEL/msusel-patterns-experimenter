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

<%@page import='com.ivata.groupware.business.addressbook.*' %>
<%@page import='com.ivata.groupware.business.addressbook.telecomaddress.*' %>
<%@page import='com.ivata.groupware.business.addressbook.person.*' %>
<%@page import='com.ivata.mask.web.format.HTMLFormatter' %>
<%@page import='com.ivata.mask.util.CollectionHandling' %>
<%@page import='com.ivata.mask.util.StringHandling' %>

<%@page import='java.util.Collection' %>
<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: user.jsp,v 1.4 2005/04/30 13:04:13 colinmacleod Exp $
//
// Lets you enter/modify a user's details, including the user name, the
// person associated with this user and the user's password.
//
// Since: ivata groupware 0.9 (2002-08-11)
// Author: Colin MacLeod <colin.macleod@ivata.com>
// $Revision: 1.4 $
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
// $Log: user.jsp,v $
// Revision 1.4  2005/04/30 13:04:13  colinmacleod
// Fixes reverting id type from String to Integer.
//
// Revision 1.3  2005/04/11 09:17:03  colinmacleod
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
// Revision 1.6  2004/11/03 15:47:04  colinmacleod
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
// Revision 1.4  2003/11/13 16:03:16  jano
// commitng everything to CVS
// can deploy and application is ruuning, can login into
//
// Revision 1.3  2003/11/07 14:54:15  jano
// commitng after fixing some bugs
//
// Revision 1.17  2003/09/11 13:09:03  jano
// fixing bugs
//
// Revision 1.16  2003/08/21 11:14:13  jano
// fixing bug
//
// Revision 1.15  2003/03/20 15:20:50  jano
// fixing textarea size for IE
//
// Revision 1.14  2003/03/03 18:44:51  colin
// added i18n for user aliases
//
// Revision 1.13  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.12  2003/02/25 07:28:54  colin
// restructured java file paths
//
// Revision 1.11  2003/02/18 13:13:55  colin
// reverted to single quotes for popups
//
// Revision 1.10  2003/02/18 10:38:26  colin
// conversion to Struts and pop-ups
//
// Revision 1.9  2003/01/14 10:38:57  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.8  2002/11/22 16:44:38  peter
// localisation
//
// Revision 1.7  2002/11/20 16:59:37  jano
// defalut addrese for ivata op is : @www.ivata.com
//
// Revision 1.6  2002/11/13 17:01:58  jano
// displaing mail subdomain
//
// Revision 1.5  2002/10/18 08:39:21  colin
// split off password functionality from user.jsp to password.jsp
//
// Revision 1.4  2002/09/30 15:31:45  colin
// remmed out forwarding & vacation messages
//
// Revision 1.3  2002/09/25 16:25:35  colin
// added more title/tootips to submit buttons
//
// Revision 1.2  2002/09/19 14:40:56  colin
// added user aliases and vacation message fields
//
// Revision 1.1  2002/08/26 09:20:36  colin
// implemented tabs on person mask for user and person details
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<%@include file='/include/theme.jspf'%>

<igw:bean id='addressBookPersonForm' scope='session' type='com.ivata.groupware.business.addressbook.struts.PersonForm'/>
<igw:bean id='person' name='addressBookPersonForm' property='person' type='com.ivata.groupware.business.addressbook.person.PersonDO'/>
<igw:bean id='currentUserName' name='securitySession' property='user.name' type='java.lang.String'/>

<igw:getSetting id='emailAddressHost' setting='emailAddressHost' type='java.lang.String'/>
<igw:getSetting id='demoVersion' setting='demoVersion' type='java.lang.Boolean'/>

<%
boolean readOnly = addressBookPersonForm.isUserTabReadOnly();
%>

<imhtml:form refersTo='personFormTag' resourceFieldPath='user'>
  <imtheme:framePane>
    <%-- this section changes the user name --%>
    <table border='0' cellpadding='2' cellspacing='0' width='100%'>
      <%--
      Nasty condition here, but this is complicated. We don't store the
      password, so if the user name is changed, we can't automatically change
      the password of the new system user.
      FIXME: A nicer solution would be to automatically pop-up the password
      window when you are changing your own user name, store the new password,
      and then change both together.
      --%>
      <c:choose>
        <c:when test='<%=currentUserName.equals(person.getUser().getName()) || demoVersion.booleanValue()%>'>
          <tr>
            <td class='fieldCaption'><imhtml:label fieldName='userName'/></td>
            <td class='field' colspan='2'>
              <bean:write name='addressBookPersonForm' property='userName'/>
              <imhtml:hidden name='addressBookPersonForm' property='userName'/>
              <imhtml:hidden name='addressBookPersonForm' property='enableUser'/>
            </td>
          </tr>
          <c:if test='<%=!demoVersion.booleanValue()%>'>
            <tr>
              <td colspan='2'>
                <p><em>(To prevent lockouts, users cannot change their own user name.)</em></p>
              </td>
            </tr>
          </c:if>
        </c:when>
        <c:otherwise>
          <tr>
            <td class='fieldCaption'><imhtml:label fieldName='userName'/></td>
            <td class='field' colspan='2'>
              <imhtml:text fieldName='userName' disabled='<%=readOnly%>'/>
            </td>
          </tr>
          <tr>
            <td class='fieldCaption'><imhtml:label fieldName='enableUser'/></td>
            <td class='field' colspan='2'>
              <imhtml:select fieldName='enableUser' disabled='<%=readOnly%>'>
                <imhtml:option value='true'><bean:message bundle='addressBook' key='user.option.enableUser.enabled'/></imhtml:option>
                <imhtml:option value='false'><bean:message bundle='addressBook' key='user.option.enableUser.disabled'/></imhtml:option>
              </imhtml:select>
            </td>
          </tr>
        </c:otherwise>
      </c:choose>
      <%-- only the mail user action has user aliases --%>
      <c:catch>
	      <c:if test='<%=Class.forName("com.ivata.groupware.business.mail.struts.MailUserForm").isAssignableFrom(addressBookPersonForm.getClass())%>'>
		      <tr>
		        <td class='fieldCaption'><imhtml:label fieldName='userAliasesAsLines'/></td>
		        <td class='field' height='80'><imhtml:textarea fieldName='userAliasesAsLines' disabled='<%=readOnly%>'/></td>
		        <td valign='top' width='150'>@<%=emailAddressHost%></td>
		      </tr>
	      </c:if>
      </c:catch>
  <%-- TODO: need to implement forwarding addresses and vacation message
      <tr>
        <td class='fieldCaption'>Forward&nbsp;To:</td>
        <td class='field'>
          <textarea name='forwardingAddresses'>TODO: this is not active yet!!</textarea>
      </tr>
      <tr>
        <td class='fieldCaption'></td>
        <td valign='top'>
          <input type='checkBox' name='keepLocalCopy'/>keep local copy
        </td>
      </tr>
      <tr>
        <td class='fieldCaption' colspan='2'>Vacation&nbsp;Message:</td>
      </tr>
      <tr>
        <td class='field' colspan='2'><textarea name='vacationMessage' cols='40' rows='6'>TODO: this is not active yet!!<%=vacationMessage%></textarea></td>
      </tr>
  --%>

    <%-- this section changes the password --%>
    <c:if test='<%=!readOnly%>'>
      <tr>
        <td colspan='3' align='right'>
          <igw:bean id='passwordJavaScript'>
            <c:choose>
              <c:when test='<%=demoVersion.booleanValue()%>'>
                alert("The change password feature is disabled in the demo version.");
              </c:when>
              <c:otherwise>
                <igw:bean id='passwordPopUp' scope='page' type='com.ivata.mask.web.javascript.JavaScriptWindow'/>
                <jsp:setProperty name='passwordPopUp' property='windowName' value='passwordChooser'/>
                <jsp:setProperty name='passwordPopUp' property='frameName' value='passwordFrame'/>
                <jsp:setProperty name='passwordPopUp' property='width' value='400'/>
                <jsp:setProperty name='passwordPopUp' property='height' value='420'/>
                <jsp:setProperty name='passwordPopUp' property='page' value='/addressBook/password.action'/>
                <jsp:setProperty name='passwordPopUp' property='pageContext' value='<%=pageContext%>'/>
                <%=passwordPopUp.toString()%>
              </c:otherwise>
            </c:choose>
          </igw:bean>
          <imhtml:button onclick='<%=passwordJavaScript%>'
                      fieldName='password'/>
        </td>
      </tr>
    </c:if>
    </table>
  </imtheme:framePane>
</imhtml:form>
<c:if test='<%=demoVersion.booleanValue()%>'>
  <script language='JavaScript' type='text/JavaScript'>
    alert("The features on this page are disabled in the demo version.");
  </script>
</c:if>
