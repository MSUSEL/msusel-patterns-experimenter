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

<%@page import='com.ivata.groupware.business.addressbook.address.AddressDO' %>
<%@page import='com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressConstants' %>
<%@page import='com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressDO' %>
<%@page import='com.ivata.mask.util.StringHandling' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: personSummary.jsp,v 1.3 2005/04/28 18:47:09 colinmacleod Exp $
//
// Display all known details about a person which the current user is allowed
// to see.
//
// Since: ivata groupware 0.9 (2003-02-15)
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
// $Log: personSummary.jsp,v $
// Revision 1.3  2005/04/28 18:47:09  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.2  2005/04/09 17:19:12  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:51:01  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.7  2004/11/12 15:57:09  colinmacleod
// Removed dependencies on SSLEXT.
// Moved Persistence classes to ivata masks.
//
// Revision 1.6  2004/11/03 15:44:21  colinmacleod
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
// Revision 1.5  2003/12/12 11:08:58  jano
// fixing aaddressbook functionaality
//
// Revision 1.4  2003/11/13 16:03:16  jano
// commitng everything to CVS
// can deploy and application is ruuning, can login into
//
// Revision 1.3  2003/11/07 14:54:15  jano
// commitng after fixing some bugs
//
// Revision 1.12  2003/08/25 08:35:58  jano
// fixing bug with new contact
//
// Revision 1.11  2003/08/22 15:48:37  jano
// again, fixing bug for new contact
//
// Revision 1.10  2003/08/22 15:45:18  jano
// fixing bug
//
// Revision 1.9  2003/08/21 11:14:13  jano
// fixing bug
//
// Revision 1.8  2003/07/25 11:45:40  jano
// adding functionality for addressBook extension
//
// Revision 1.7  2003/04/02 09:02:47  jano
// again, fixing bug with wrong keys
//
// Revision 1.6  2003/03/04 16:09:24  colin
// removed streetAddress bug (was displayed twice, if you specify region code)
//
// Revision 1.5  2003/03/04 00:26:28  colin
// fixed key address -> streetAddress
//
// Revision 1.4  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.3  2003/02/25 07:28:54  colin
// restructured java file paths
//
// Revision 1.2  2003/02/20 07:45:07  colin
// fixed bug where name of main group doesnt show in personSummary.jsp
//
// Revision 1.1  2003/02/18 10:45:19  colin
// first version
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<igw:checkLogin />
<%@include file='/include/theme.jspf'%>



<igw:bean id='addressBookPersonForm' scope='session' type='com.ivata.groupware.business.addressbook.struts.PersonForm'/>
<igw:bean id='person' name='addressBookPersonForm' property='person' type='com.ivata.groupware.business.addressbook.person.PersonDO'/>

<igw:bean id='addressFormatter' type='com.ivata.mask.web.format.HTMLFormatter'/>
<igw:bean id='lineBreaks' type='com.ivata.mask.web.format.LineBreakFormat'/>
<jsp:setProperty name='lineBreaks' property='convertLineBreaks' value='true'/>
<imformat:addFormat formatter='<%=addressFormatter%>' format='<%=lineBreaks%>'/>

<igw:bean id='emailAddressFormatter' scope='page' type='com.ivata.groupware.web.format.EmailAddressFormatter'/>
<jsp:setProperty name='emailAddressFormatter' property='pageContext' value='<%=pageContext%>'/>
<jsp:setProperty name='emailAddressFormatter' property='showAddress' value='true'/>


<imhtml:html locale='true'>
  <igw:head topLevel='true'
              keepOnTop='false'
               titleKey='<%=addressBookPersonForm.getTitleKey()%>'>
    <imhtml:base/>
  </igw:head>

  <body class='normal'>
    <h2><bean:write name='person' property='fileAs'/></h2>
    <table border='0'>
<%-- this is superfluous because of the title
      <tr>
        <td class='fieldCaption'><bean:message bundle='addressBook' key='personDetails.prompt.firstNames'/></td>
        <td class='field'><bean:write name='person' property='firstNames'/></td>
      </tr>
      <tr>
        <td class='fieldCaption'><bean:message bundle='addressBook' key='personDetails.prompt.lastName'/></td>
        <td class='field'><bean:write name='person' property='lastName'/></td>
      </tr>
      <c:if test='<%=!StringHandling.isNullOrEmpty(person.getSalutation())%>'>
        <tr>
          <td class='fieldCaption'><bean:message bundle='addressBook' key='personDetails.prompt.salutation'/></td>
          <td class='field'><bean:message key='<%="salutation." + person.getSalutation().toLowerCase()%>'/></td>
        </tr>
      </c:if>
--%>
<%-- TODO
      <c:if test='<%=!StringHandling.isNullOrEmpty(addressBookPersonForm.getAddressBookName())%>'>
        <tr>
          <td class='fieldCaption'>
            <bean:message bundle='addressBook' key='personDetails.field.addressBookName' />
          </td>
          <td class='field'><bean:write name='addressBookPersonForm' property='addressBookName'/></td>
        </tr>
      </c:if>
      <c:if test='<%=person.getGroup().getId() != null &&
                     !person.getGroup().getId().equals(addressBookPersonForm.getAddressBookId()) &&
                     !person.getGroup().getId().equals(new Integer(0)) %>'>
        <tr>
          <td class='fieldCaption'>
            <bean:message bundle='addressBook' key='personDetails.field.group' />
          </td>
          <td class='field'><bean:write name='person' property='groupName'/></td>
        </tr>
      </c:if>
--%>
      <tr>
        <td class='fieldCaption' colspan='2'><br/></td>
      </tr>

      <logic:notEmpty name='person' property='jobTitle'>
        <tr>
          <td class='fieldCaption'><bean:message bundle='addressBook' key='personDetails.field.jobTitle' /></td>
          <td class='field'><bean:write name='person' property='jobTitle'/></td>
        </tr>
      </logic:notEmpty>
      <logic:notEmpty name='person' property='company'>
        <tr>
          <td class='fieldCaption'><bean:message bundle='addressBook' key='personDetails.field.company'/></td>
          <td class='field'><bean:write name='person' property='company'/></td>
        </tr>
      </logic:notEmpty>
      <tr>
        <td class='fieldCaption' colspan='2'><br/><br/></td>
      </tr>
      <logic:present name='person' property='address.country.name'>
        <tr height='100%'>
          <td class='fieldCaption'><bean:message bundle='addressBook' key='person.address.field.streetAddress' /></td>
          <td class='field'>
            <imformat:format formatter='<%=addressFormatter%>'><bean:write name='person' property='address.streetAddress'/></imformat:format><br/>
            <%-- TODO: some country put the postcode after the town --%>
            <bean:write name='person' property='address.postCode'/> <bean:write name='person' property='address.town'/> <br/>
            <logic:present name='person' property='address.region'>
              <bean:write name='person' property='address.region'/><br/>
            </logic:present>
            <igw:bean id='countryKey' name='person' property='address.country.name' type='java.lang.String'/>
            <bean:message bundle='addressBook' key='<%=countryKey%>'/>
          </td>
        </tr>
      </logic:present>
      <c:forEach var='telecomAddress' items='<%=addressBookPersonForm.getTelecomAddresses()%>'>
        <igw:bean id='telecomAddress' type='com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressDO'/>
        <logic:notEmpty name='telecomAddress' property='address'>
          <tr>
            <td class='fieldCaption'><bean:message bundle='addressBook' key='<%=TelecomAddressConstants.getTypeName(telecomAddress.getType())%>'/>:</td>
            <%-- if it is an email address, show it as a link to compose --%>
            <c:choose>
              <c:when test='<%=telecomAddress.getType() == TelecomAddressConstants.TYPE_EMAIL%>'>
                <td class='field'><%=emailAddressFormatter.format(person.getFileAs()
                  + " <"
                  + telecomAddress.getAddress()
                  + ">")%></td>
              </c:when>
              <c:otherwise>
                <td class='field'><%=telecomAddress.getAddress()%></td>
              </c:otherwise>
            </c:choose>
          </tr>
        </logic:notEmpty>
      </c:forEach>
      <%-- TODO: add user, employee information, checking the view rights --%>
    </table>
  </body>
</imhtml:html>
