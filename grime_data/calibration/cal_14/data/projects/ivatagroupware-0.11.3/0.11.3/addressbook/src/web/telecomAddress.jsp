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

<%@page import='com.ivata.groupware.business.addressbook.address.country.CountryDO' %>
<%@page import='com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressConstants' %>
<%@page import='com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressDO' %>
<%@page import='com.ivata.groupware.business.addressbook.person.PersonDO' %>
<%@page import='com.ivata.mask.web.format.HTMLFormatter' %>
<%@page import='com.ivata.mask.util.StringHandling' %>

<%@page import='java.util.Collection' %>
<%@page import='java.util.Iterator' %>

<%@page import='org.apache.struts.Globals' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: telecomAddress.jsp,v 1.2 2005/04/09 17:19:12 colinmacleod Exp $
//
// Let's you enter/modify a person's phone, fax and email addresses. Split off
// from personDetails.jsp.
//
// Since: ivata groupware 0.9 (2003-02-14)
// Author: Colin MacLeod <colin.macleod@ivata.com>
// $Revision: 1.2 $
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
// $Log: telecomAddress.jsp,v $
// Revision 1.2  2005/04/09 17:19:12  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:50:45  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.7  2004/12/27 14:51:59  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.6  2004/11/03 15:45:09  colinmacleod
// Changed fieldPath to resourceFieldPath.
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
// Revision 1.4  2003/03/03 18:15:47  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.3  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.2  2003/02/25 07:28:54  colin
// restructured java file paths
//
// Revision 1.1  2003/02/18 10:45:19  colin
// first version
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<%@include file='/include/theme.jspf'%>

<igw:bean id='addressBookPersonForm' scope='session' type='com.ivata.groupware.business.addressbook.struts.PersonForm'/>
<igw:bean id='person' name='addressBookPersonForm' property='person' type='com.ivata.groupware.business.addressbook.person.PersonDO'/>

<imhtml:form refersTo='personFormTag' resourceFieldPath='telecomAddress'>
  <imtheme:framePane style='padding: 0px 0px 0px 0px;' styleClass='hilight'>
    <%
      // can't use addresses for size - see comment in PersonAction.java
      int totalTelecomAddresses = addressBookPersonForm.getTelecomAddresses().size();
      int lastIndex = totalTelecomAddresses - 1;
    %>
    <imhtml:iframe style='border: 0px;'
                 styleId='stretchIFrame'
                formName='addressBookPersonForm'
              formTarget='self'
               frameName='ivataTelecomAddressIFrame'
                   width='100%'
                  height='100%'
                   align='block'>
      <table width='100%'>
        <c:forEach begin='0' end='<%=lastIndex%>' var='telecomAddressIndex'>
          <igw:bean id='telecomAddressIndex' scope='page' type='java.lang.Integer'/>
          <tr>
            <td class='fieldCaption'>
              <imhtml:select
                  fieldName='<%="telecomAddressType" + telecomAddressIndex%>'
                  property='<%="telecomAddresses[" + telecomAddressIndex + "].type"%>'>
                <c:forEach begin='0' end='<%=TelecomAddressConstants.countTypes() - 1%>' var='typeCounter'>
                  <igw:bean id='typeCounter' scope='page' type='java.lang.Integer'/>
                  <imhtml:option value='<%=typeCounter.toString()%>'><bean:message bundle='addressBook' key='<%=TelecomAddressConstants.getTypeName(typeCounter.intValue())%>'/></imhtml:option>
                </c:forEach>
              </imhtml:select>
            </td>
            <td class='field'>
              <imhtml:text fieldName='<%="telecomAddress" + telecomAddressIndex%>'
                         maxlength='100'
                         property='<%="telecomAddresses[" + telecomAddressIndex + "].address"%>'/>
            </td>
            <c:if test='<%=telecomAddressIndex.intValue() == lastIndex%>'>
              <td>
                <imhtml:button onclick='parent.document.addressBookPersonForm.moreTelecomAddresses.value=1;parent.document.addressBookPersonForm.submit();'
                                fieldName='moreTelecomAddresses'/></td>
            </c:if>
          </tr>
        </c:forEach>
        <tr height='100%'><td>&nbsp;</td></tr>
      </table>
    </imhtml:iframe>
    <html:hidden property='moreTelecomAddresses' value=''/>
    <%-- keep the hidden fields outside the iframe --%>
    <% int telecomAddressIdIndex = 0;%>
    <c:forEach var='telecomAddressForId' items='<%=addressBookPersonForm.getTelecomAddresses()%>'>
      <igw:bean id='telecomAddressForId' type='com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressDO'/>
      <%-- if the id is null, we don't want to create a field for it at all --%>
      <c:if test='<%=telecomAddressForId.getId() != null%>'>
        <html:hidden property='<%="telecomAddresses[" + telecomAddressIdIndex + "].id"%>'/>
      </c:if>
      <%telecomAddressIdIndex++;%>
    </c:forEach>
  </imtheme:framePane>
</imhtml:form>
