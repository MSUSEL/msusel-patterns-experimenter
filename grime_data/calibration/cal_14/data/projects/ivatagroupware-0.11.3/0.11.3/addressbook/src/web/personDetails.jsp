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
<%@page import='com.ivata.groupware.business.addressbook.address.country.*' %>
<%@page import='com.ivata.groupware.business.addressbook.telecomaddress.*' %>
<%@page import='com.ivata.groupware.business.addressbook.person.*' %>
<%@page import='com.ivata.mask.web.format.HTMLFormatter' %>
<%@page import='com.ivata.mask.util.StringHandling' %>

<%@page import='java.util.Collection' %>
<%@page import='java.util.Map' %>

<%@page import='org.apache.struts.Globals' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: personDetails.jsp,v 1.6 2005/04/30 13:04:12 colinmacleod Exp $
//
// <p>Lets you enter/modify a person's details.</p>
//
// Since: ivata groupware 0.9 (2001-12-12)
// Author: Colin MacLeod <colin.macleod@ivata.com>
// $Revision: 1.6 $
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
// $Log: personDetails.jsp,v $
// Revision 1.6  2005/04/30 13:04:12  colinmacleod
// Fixes reverting id type from String to Integer.
//
// Revision 1.5  2005/04/29 02:48:15  colinmacleod
// Data bugfixes.
// Changed primary key back to Integer.
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
// Revision 1.1.1.1  2005/03/10 17:50:47  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.8  2004/12/27 14:51:59  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
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
// Revision 1.4  2003/11/13 16:03:16  jano
// commitng everything to CVS
// can deploy and application is ruuning, can login into
//
// Revision 1.3  2003/11/07 14:54:15  jano
// commitng after fixing some bugs
//
// Revision 1.20  2003/08/21 09:47:48  jano
// fixing for new addressBook extension
//
// Revision 1.19  2003/07/25 11:45:40  jano
// adding functionality for addressBook extension
//
// Revision 1.18  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.17  2003/02/25 07:28:54  colin
// restructured java file paths
//
// Revision 1.16  2003/02/21 18:30:46  peter
// birthDate fixed
//
// Revision 1.15  2003/02/20 13:06:45  colin
// removed date format hint text from dob
//
// Revision 1.14  2003/02/20 07:45:07  colin
// fixed bug where name of main group doesnt show in personSummary.jsp
//
// Revision 1.13  2003/02/18 10:38:26  colin
// conversion to Struts and pop-ups
//
// Revision 1.12  2003/01/14 10:38:56  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.11  2003/01/13 09:58:37  jano
// fixing coutry in diffrent language
//
// Revision 1.10  2002/12/19 12:55:26  jano
// if contact is new you can add it only to group where you have right
//
// Revision 1.9  2002/12/06 09:34:07  jano
// fixing null pointer bug
//
// Revision 1.8  2002/12/06 09:15:58  jano
// resolving
//
// Revision 1.7  2002/12/06 08:54:51  jano
// Formating DATE OF BIRTH is wrong, so I had to hard coding, because I didn't change PersonDO
//
// Revision 1.6  2002/11/22 16:44:38  peter
// localisation
//
// Revision 1.5  2002/10/08 16:21:08  colin
// fixed topLevel handling for pop-ups
//
// Revision 1.4  2002/10/03 13:02:37  colin
// bugfixes when Clear/Delete buttons are pressed
//
// Revision 1.3  2002/09/25 11:43:27  colin
// improved telecom address field handling (More button)
//
// Revision 1.2  2002/09/19 14:39:06  colin
// fixed country display
//
// Revision 1.1  2002/08/26 09:20:36  colin
// implemented tabs on person mask for user and person details
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
// Revision 1.5  2002/01/22 21:55:06  colin
// changed the object model path from com.ivata.groupware.address to com.ivata.groupware.business.addressbook.person
//
// Revision 1.4  2002/01/20 12:22:45  colin
// reformatted addressbook without frames
//
// Revision 1.3  2002/01/03 12:57:47  colin
// added group membership functionality
//
// Revision 1.2  2001/12/26 21:34:35  colin
// first working version
//
// Revision 1.1  2001/12/12 20:13:27  colin
// initial import into CVS
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<%@include file='/include/theme.jspf'%>

<%-- get the addressBook tree --%>
<%@include file='/addressBook/include/tree.jspf'%>


<%-- get the addressBook object --%>
<%@include file='/addressBook/include/addressBook.jspf'%>

<igw:bean id='addressBookPersonForm' scope='session' type='com.ivata.groupware.business.addressbook.struts.PersonForm'/>
<igw:bean id='person' name='addressBookPersonForm' property='person' type='com.ivata.groupware.business.addressbook.person.PersonDO'/>

<igw:bean id='addressBookGroupId' name='addressBookPersonForm' property='addressBookGroupId' type='java.lang.Integer'/>
<%-- only show this addressbook --%>
<jsp:setProperty name='personTreeModel' property='rootId' value='<%=addressBookGroupId%>'/>

<% boolean readOnly =  addressBookPersonForm.getReadOnly(); %>
<% boolean canRemove =  addressBookPersonForm.getCanRemove(); %>
<% boolean newContact =  (person.getId()==null); %>

<%-- get the salutation; we'll need this for the select below --%>
<%String salutation = StringHandling.getNotNull(person.getSalutation()).toLowerCase();%>
<igw:getSetting id='dateInputFormatString' setting='i18nDateInput' type='java.lang.String'/>
<igw:getSetting id='dateDisplayFormatString' setting='i18nDateInputDisplay' type='java.lang.String'/>
<igw:bean id='alertBirthDate'><bean:message bundle='addressBook' key='personDetails.alert.birthDate' arg0='<%=dateInputFormatString%>'/></igw:bean>

<igw:bean id='dateScript'><imhtml:rewrite page='/include/script/date.jsp'/></igw:bean>
<igw:bean id='trimScript'><imhtml:rewrite page='/include/script/trim.jsp'/></igw:bean>
<script language='javascript' src='<%=dateScript%>'></script>
<script language='javascript' src='<%=trimScript%>'></script>

<script language='javascript'>
  <!--
  <%String personForm = "document.addressBookPersonForm";%>
  <%@include file='/addressBook/include/script/fileAs.jspf'%>

  function refreshAddressBoookCombo() {
    document.addressBookPersonForm.changedAddressBook.value = 'true';
    document.addressBookPersonForm.submit();
  }

  function onBirthDate() {
    var dateInputFormat = '<%=dateInputFormatString%>';
    var dateDisplayFormat = '<%=dateDisplayFormatString%>';

    var birthDate = trimElementById("dateOfBirthString");

    if (birthDate.value.length == 0) {
        return;
    }

    <%--
    // check this is a valid date
    --%>
    if (!isValidDate(birthDate.value, dateInputFormat)) {
        alert("<%=alertBirthDate%>");
        return;
    } else {
        reformatTimeField(birthDate, dateInputFormat, dateDisplayFormat);
    }
  }
  // -->
</script>
<imhtml:form refersTo='personFormTag' resourceFieldPath='personDetails'>
  <imtheme:framePane styleClass='hilight'>
    <table border='0' cellpadding='0' cellspacing='0' width='100%'>
      <tr>
        <td class='fieldCaption'><imhtml:label fieldName='firstNames'/></td>
        <td class='field'>
          <imhtml:text fieldName='firstNames' property='person.firstNames' maxlength='50'
                  onchange='onChangeName()' mandatory='true'/>
          <%-- only include the id field if it is non-null --%>
          <logic:present name='person' property='id'>
            <html:hidden property='person.id'/>
          </logic:present>
        </td>
      </tr>
      <tr>
        <td class='fieldCaption'><imhtml:label fieldName='lastName'/></td>
        <td class='field'><imhtml:text fieldName='lastName' property='person.lastName' maxlength='30'
                  onchange='onChangeName()' mandatory='true'/></td>
      </tr>
      <tr>
        <td class='fieldCaption'><imhtml:label fieldName='salutation'/></td>
        <td class='field'>
          <imhtml:select fieldName='salutation' property='person.salutation' mandatory='true'>
            <imhtml:option value='mr'><bean:message key="salutation.mr"/></imhtml:option>
            <imhtml:option value='ms'><bean:message key="salutation.ms"/></imhtml:option>
            <imhtml:option value='dr'><bean:message key="salutation.dr"/></imhtml:option>
            <imhtml:option value='mrs'><bean:message key="salutation.mrs"/></imhtml:option>
            <imhtml:option value='miss'><bean:message key="salutation.miss"/></imhtml:option>
          </imhtml:select>
        </td>
      </tr>
      <tr>
        <td class='fieldCaption'><imhtml:label fieldName='dateOfBirthString'/></td>
        <td class='field'>
          <imhtml:text fieldName='dateOfBirthString' property='dateOfBirthString' onchange='onBirthDate();' />
        </td>
      </tr>
      <tr>
        <td class='fieldCaption'>
      <tr>
        <td class='fieldCaption' colspan='2'><br/><br/></td>
      </tr>
      <tr>
        <td class='fieldCaption'>
          <imhtml:label fieldName='fileAs'/>
        </td>
        <td class='field'><imhtml:text fieldName='fileAs' mandatory='true' property='person.fileAs' maxlength='35'/></td>
      </tr>

      <tr>
        <td class='fieldCaption'><imhtml:label fieldName='addressBookName'/></td>
        <%--
          only include this field if the user has the right to remove from the parent group
          otherwise it is possible that the parent won't appear in the list, so we can't use
          a read-only control
        --%>
        <%-- addressBooks combo --%>
        <td class='field'>
          <html:hidden property='changedAddressBook' />
          <c:choose>
            <%-- parent group is read only, or this is a user --%>
            <c:when test='<%=!StringHandling.isNullOrEmpty(addressBookPersonForm.getUserName())%>'>
              <em>Users are always in address book <strong><%=addressBookPersonForm.getAddressBookGroupName()%></strong>.</em>
              <imhtml:hidden name='addressBookGroupId'/>
            </c:when>
            <c:when test='<%=!newContact && (readOnly
                || !canRemove)%>'>
              <%=addressBookPersonForm.getAddressBookGroupName()%>
              <imhtml:hidden name='addressBookGroupId'/>
            </c:when>
            <%--
              you right to amend in addressBook so you can amend the group and show addressBooks where you can add this midifing group
              or
              it's new group show addressBooks where you can add new group
            --%>
            <c:when test='<%=(newContact || !readOnly)%>'>
              <%--jsp:setProperty name='filter' property='access' value='<%=com.ivata.groupware.business.addressbook.person.group.right.RightConstants.ACCESS_ADD%>'/>
              <jsp:setProperty name='personTreeModel' property='filter' value='<%=filter%>'/--%>
              <%--igwtree:treeSelect model='<%=addressBookTreeModel%>'
                                   treeName='addressBook'
                                   onChange='refreshAddressBoookCombo()'
                                   selected='<%=addressBookGroupId.toString()%>'
                                   treeStyle='combo'
                                   controlName='addressBookGroupId'
                                   maxDepth='1'/--%>

              <%
                Map addressBookGroupNames = addressBookPersonForm.getAddressBookGroupNames();
              %>
              <select onChange='refreshAddressBoookCombo()' name='addressBookGroupId' class='mandatory'>
                <c:forEach var='item' items='<%=addressBookGroupNames.keySet()%>'>
                  <igw:bean id='item' type='java.lang.Integer'/>
                  <option value='<%=item%>' <%=addressBookGroupId.equals(item) ? "selected='selected'" : ""%>>
                    <%=addressBookGroupNames.get(item)%>
                  </option>
                </c:forEach>
              </select>
            </c:when>
          </c:choose>
        </td>
      </tr>

      <%-- file Under --%>
      <tr>
        <td class='fieldCaption'>
          <imhtml:label fieldName='group'/>
        </td>
        <td class='field'>
          <jsp:setProperty name='personTreeModel' property='filter' value='<%=null%>'/>
          <%--
          <jsp:setProperty name='personTreeModel' property='overRideRoot' value='<%=addressBookGroupId%>'/>
          --%>
          <igw:bean id='defaultCaption'><bean:message bundle='addressBook' key='personDetails.select.addressBook' arg0='<%=addressBookPersonForm.getAddressBookGroupName()%>'/></igw:bean>
          <igwtree:treeSelect model='<%=personTreeModel%>'
                               treeName='group'
                               defaultCaption='<%=defaultCaption%>'
                               defaultValue='<%=StringHandling.getNotNull(addressBookPersonForm.getAddressBookGroupId())%>'
                               treeStyle='combo'
                               controlName='person.group.id'
                               selected='<%=StringHandling.getNotNull(person.getGroup().getId())%>'/>
        </td>
      </tr>

      <tr>
        <td class='fieldCaption'><imhtml:label fieldName='jobTitle'/></td>
        <td class='field'><imhtml:text fieldName='jobTitle' maxlength='25' property='person.jobTitle'/></td>
      </tr>
      <tr>
        <td class='fieldCaption'><imhtml:label fieldName='company'/></td>
        <td class='field'><imhtml:text fieldName='company' maxlength='35' property='person.company'/></td>
      </tr>
    </table>
  </imtheme:framePane>
</imhtml:form>
