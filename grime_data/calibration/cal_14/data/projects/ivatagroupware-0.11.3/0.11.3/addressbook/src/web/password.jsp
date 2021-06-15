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

<%@page import='com.ivata.mask.util.StringHandling' %>

<%@page import='org.apache.struts.Globals' %>
<%@page import='java.util.Arrays' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: password.jsp,v 1.4 2005/04/28 18:47:09 colinmacleod Exp $
//
// Lets you change the password for the current or (if you have the user rights)
// another user. Split off from original location  in user.jsp.
//
// Since: ivata groupware 0.9 (2002-09-17)
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
// $Log: password.jsp,v $
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
// Revision 1.1.1.1  2005/03/10 17:50:46  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.9  2004/12/31 18:38:01  colinmacleod
// Removed unused ExceptionHandling import.
//
// Revision 1.8  2004/12/27 14:51:59  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.7  2004/11/12 15:57:09  colinmacleod
// Removed dependencies on SSLEXT.
// Moved Persistence classes to ivata masks.
//
// Revision 1.6  2004/11/03 15:42:54  colinmacleod
// Updated to use pico/struts.
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
// Revision 1.2  2004/02/10 22:09:28  colinmacleod
// Turned off SSL
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
// Revision 1.10  2003/03/31 15:47:26  jano
// fixing message if old/yours password is wrong
//
// Revision 1.9  2003/03/04 01:29:01  colin
// fixed bugs in the imhtml tags
//
// Revision 1.8  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.7  2003/02/25 07:28:54  colin
// restructured java file paths
//
// Revision 1.6  2003/02/18 10:38:26  colin
// conversion to Struts and pop-ups
//
// Revision 1.5  2003/01/23 16:52:21  peter
// links and forms changed to sslext, checkLogin added
//
// Revision 1.4  2003/01/18 20:38:58  colin
// added help buttons
//
// Revision 1.3  2003/01/14 10:38:56  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.2  2002/11/21 14:49:26  peter
// localisation
//
// Revision 1.1  2002/10/18 08:39:21  colin
// split off password functionality from user.jsp to password.jsp
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<%@include file='/include/theme.jspf'%>
<igw:checkLogin />

<igw:bean id='securitySession' scope='session' type='com.ivata.groupware.admin.security.server.SecuritySession'/>
<igw:bean id='userName' name='securitySession' property="user.name" type='java.lang.String'/>

<igw:bean id='addressBookPersonForm' scope='session' type='com.ivata.groupware.business.addressbook.struts.PersonForm'/>
<igw:bean id='justClose' name='addressBookPasswordForm' property='justClose' type='java.lang.Boolean'/>
<igw:bean id='person' name='addressBookPersonForm' property='person' type='com.ivata.groupware.business.addressbook.person.PersonDO'/>

<imhtml:html locale='true'>
  <igw:head topLevel='true'
      bundle='addressBook'
      titleKey='password.title'
      titleArgs='<%=Arrays.asList(new Object[] {addressBookPersonForm.getUserName()})%>'>
    <imhtml:base/>
  </igw:head>

  <body>
    <imtheme:window>
      <%-- if there was an error message, just display that with a close button. --%>
      <%@include file='/include/errorFrame.jspf'%>
      <imhtml:form action='/addressBook/password' bundle='addressBook' resourceFieldPath='password'>
        <%-- if we should just close after the error, don't display the form at all! --%>
        <c:if test='<%=!justClose.booleanValue()%>'>
          <imtheme:frame>
            <imtheme:framePane styleClass='hilight'>
              <table width='100%'>
                <tr>
                  <%--
                    the text changes if this is the name user, or a different one
                    to the user whose password is being set
                  --%>
                  <c:choose>
                    <c:when test='<%=userName.equals(addressBookPersonForm.getUserName())%>'>
                      <td class='fieldCaption'><imhtml:label fieldName='oldPassword' keySuffix='current'/></td>
                    </c:when>
                    <c:otherwise>
                      <td class='fieldCaption'><imhtml:label fieldName='oldPassword' keySuffix='yours'/></td>
                    </c:otherwise>
                  </c:choose>
                  <td class='field'><imhtml:password fieldName='oldPassword'/></td>
                </tr>
                <tr><td><html:img src='/images/empty.gif' width='1' height='10'/></td></tr>
                <tr>
                  <%-- see comment above --%>
                  <c:choose>
                    <c:when test='<%=userName.equals(addressBookPersonForm.getUserName())%>'>
                      <td class='fieldCaption'><imhtml:label fieldName='newPassword' keySuffix='new'/></td>
                    </c:when>
                    <c:otherwise>
                      <td class='fieldCaption'><imhtml:label fieldName='newPassword' keySuffix='users'/></td>
                    </c:otherwise>
                  </c:choose>
                  <td class='field'><imhtml:password fieldName='newPassword'/></td>
                </tr>
                <tr>
                  <td class='fieldCaption'><imhtml:label fieldName='confirmPassword'/></td>
                  <td class='field'><imhtml:password fieldName='confirmPassword'/></td>
                </tr>
                <tr><td colspan='2'><br/><br/></td></tr>
              </table>
            </imtheme:framePane>
          </imtheme:frame>
        </c:if>
        <imtheme:buttonFrame>
          <%-- only display the submit if we are not just going to close --%>
          <c:choose>
            <c:when test='<%=!justClose.booleanValue()%>'>
              <imhtml:ok/>
              <imhtml:button onclick='window.close()'
                              bundle='addressBook'
                             property='cancel'
                            valueKey='submit.cancel.value'
                            titleKey='password.submit.cancel.title'/>
            </c:when>
            <c:otherwise>
              <imhtml:button onclick='window.close()'
                              bundle='addressBook'
                             property='close'
                            valueKey='submit.close.value'
                            titleKey='password.submit.cancel.title'/>
            </c:otherwise>
          </c:choose>
          <imhtml:help key='addressbook.password'/>
        </imtheme:buttonFrame>
      </imhtml:form>
    </imtheme:window>
    <%@include file='/include/script/fixPopUp.jspf'%>
  </body>
</imhtml:html>

