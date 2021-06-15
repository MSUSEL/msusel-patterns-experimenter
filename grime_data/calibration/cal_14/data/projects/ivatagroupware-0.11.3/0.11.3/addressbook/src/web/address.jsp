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
<%@page import='com.ivata.groupware.business.addressbook.address.AddressDO' %>
<%@page import='com.ivata.groupware.business.addressbook.address.country.CountryDO' %>
<%@page import='com.ivata.groupware.business.addressbook.person.*' %>
<%@page import='com.ivata.mask.web.format.HTMLFormatter' %>
<%@page import='com.ivata.mask.util.StringHandling' %>

<%@page import='java.util.Collection' %>

<%@page import='org.apache.struts.Globals' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: address.jsp,v 1.2 2005/04/09 17:19:12 colinmacleod Exp $
//
// Lets you enter/modify a person's postal address details. Split off from
// address.jsp.
//
// Since: ivata groupware 0.9 (2003-02-14)
// Author: Colin MacLeod <colin.macleod@ivata.com>
// $Revision: 1.2 $
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
// $Log: address.jsp,v $
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
// Revision 1.6  2004/11/03 15:40:59  colinmacleod
// Changed address... paths to person.address...
//
// Revision 1.5  2004/07/14 22:50:20  colinmacleod
// Replaced bean:define with extended igw:bean.
//
// Revision 1.4  2004/07/14 20:59:44  colinmacleod
// Changed all occurrences of jsp:useBean to igw:bean (better tomcat compatibility).
//
// Revision 1.3  2004/07/13 19:41:17  colinmacleod
// Moved project to POJOs from EJBs.
// Applied PicoContainer to services layer (replacing session EJBs).
// Applied Hibernate to persistence layer (replacing entity EJBs).
//
// Revision 1.2  2004/03/21 21:16:19  colinmacleod
// Shortened name to ivata op.
//
// Revision 1.1  2004/03/03 20:27:38  colinmacleod
// Updated webapp structure. Moved core items to core subproject.
//
// Revision 1.1.1.1  2004/01/27 20:58:00  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.2  2003/10/17 12:36:13  jano
// fixing problems with building
// converting intranet -> portal
// Eclipse building
//
// Revision 1.1.1.1  2003/10/13 20:50:20  colin
// Restructured portal into subprojects
//
// Revision 1.6  2003/04/09 14:13:01  jano
// moving text from restoreUser -> applicationResources.properties
//
// Revision 1.5  2003/04/03 12:38:14  jano
// using sorting JSP for sort Countries
//
// Revision 1.4  2003/03/20 15:20:50  jano
// fixing textarea size for IE
//
// Revision 1.3  2003/03/03 16:57:12  colin
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

<%-- get the addressBook object --%>
<%@include file='/addressBook/include/addressBook.jspf'%>

<%-- if any of the address fields have values, they all become mandatory --%>
<script>
  <%@include file='/include/script/trim.jsp'%>
  function checkAddressFields() {
    var className = "normal";
    var personForm = document.addressBookPersonForm;
    var country = document.getElementById("country");
    var streetAddress = document.getElementById("streetAddress");
    var town = document.getElementById("town");
    var postCode = document.getElementById("postCode");
    var region = document.getElementById("region");

    if ((trim(streetAddress.value).length > 0)
            || (trim(town.value).length > 0)
            || (trim(postCode.value).length > 0)
            || (trim(region.value).length > 0)) {
      className = "mandatory";
    }
    streetAddress.className = className;
    town.className = className;
    postCode.className = className;
    <%-- NOTE: region is not mandatory! --%>
    country.className = className;
  }
</script>

<imhtml:form refersTo='personFormTag' fieldPath='person.address'>
  <logic:present name="person" property="address.id">
    <html:hidden property="person.address.id"/>
  </logic:present>
  <imtheme:framePane styleClass='hilight'>
    <table border='0' cellpadding='0' cellspacing='0' width='100%' height='100%'>
      <tr>
        <td class='fieldCaption'><imhtml:label fieldName='streetAddress'/></td>
        <td class='field'><imhtml:textarea fieldName='streetAddress' onchange='checkAddressFields()'/></td>
      </tr>
      <tr>
        <td class='fieldCaption'><imhtml:label fieldName='town'/></td>
        <td class='field'><imhtml:text fieldName='town' onchange='checkAddressFields()' maxlength='50'/></td>
      </tr>
      <tr>
        <td class='fieldCaption'><imhtml:label fieldName='postCode'/></td>
        <td class='field'><imhtml:text fieldName='postCode' onchange='checkAddressFields()' maxlength='15'/></td>
      </tr>
      <tr>
        <td class='fieldCaption'><imhtml:label fieldName='region'/></td>
        <td class='field'><imhtml:text fieldName='region' onchange='checkAddressFields()' maxlength='50'/></td>
      </tr>
      <tr>
        <td class='fieldCaption'><imhtml:label fieldName='country'/></td>
        <td class='field'>
          <imhtml:select fieldName='country' property="person.address.country.code">
            <imhtml:option value=''><bean:message key='select.pleaseSelect' /></imhtml:option>
            <%--Collection countries = addressBook.findAllCountries();--%>


            <%@include file='/addressBook/include/sortCountryKeys.jspf'%>
            <% int i=0; %>
            <c:forEach var='country' items='<%=countryCodes%>'>
              <igw:bean id='country' type='java.lang.String'/>
              <imhtml:option value='<%=country%>'><bean:message bundle='addressBook' key='<%=(String) countryKeys.get(i)%>' /></imhtml:option>
              <% i++; %>
            </c:forEach>
          </imhtml:select>
        </td>
      </tr>
    </table>
  </imtheme:framePane>
</imhtml:form>
<%-- run the script to check the mandatory status of the address fields --%>
<script language='javascript'>
  <!--
  checkAddressFields()
  // -->
</script>
