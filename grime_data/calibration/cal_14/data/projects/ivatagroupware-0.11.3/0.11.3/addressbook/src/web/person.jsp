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
<%@page import='com.ivata.groupware.business.addressbook.person.PersonDO' %>
<%@page import='com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressConstants' %>
<%@page import='com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressDO' %>
<%@page import='com.ivata.mask.web.format.HTMLFormatter' %>
<%@page import='com.ivata.mask.util.StringHandling' %>
<%@page import='com.ivata.mask.util.CollectionHandling' %>

<%@page import='java.util.Arrays' %>
<%@page import='java.util.Collection' %>

<%@page import='javax.servlet.http.HttpServletResponse' %>

<%@page import='org.apache.struts.Globals' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: person.jsp,v 1.5 2005/04/30 13:04:12 colinmacleod Exp $
//
// Enter/modify a person's details. This page includes other pages
// which handle the individual aspects of the user.
//
// Since: ivata groupware 0.9 (2001-12-12)
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
// $Log: person.jsp,v $
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
// Revision 1.1.1.1  2005/03/10 17:50:46  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.8  2004/12/31 18:39:42  colinmacleod
// Cosmetic indentation improvements.
//
// Revision 1.7  2004/12/27 14:51:59  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.6  2004/11/12 15:57:09  colinmacleod
// Removed dependencies on SSLEXT.
// Moved Persistence classes to ivata masks.
//
// Revision 1.5  2004/11/03 15:43:46  colinmacleod
// Fixed deleteKey.
// Fixed title (was null).
// Changed tabs to read the keys from the form.
//
// Revision 1.4  2004/07/14 22:50:21  colinmacleod
// Replaced bean:define with extended igw  .
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
// Revision 1.38  2003/09/11 13:09:03  jano
// fixing bugs
//
// Revision 1.37  2003/08/21 09:42:44  jano
// fixing for new addressBook extension
//
// Revision 1.36  2003/07/25 11:45:40  jano
// adding functionality for addressBook extension
//
// Revision 1.35  2003/06/19 14:12:31  jano
// clicking on "new" or "clear" go to contact tab
//
// Revision 1.34  2003/06/10 05:57:38  peter
// img and html:img replaced with size-aware imhtml:img
//
// Revision 1.33  2003/04/14 07:29:16  peter
// helpKey is taken from form
//
// Revision 1.32  2003/03/06 17:29:00  peter
// fixed js frame references and location updates
//
// Revision 1.31  2003/03/04 18:28:21  colin
// newContacts are never read only
//
// Revision 1.30  2003/03/03 19:10:57  colin
// removed debugging alerts
//
// Revision 1.29  2003/03/03 17:49:20  colin
// removed misplaced bundle from html:html
//
// Revision 1.28  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.27  2003/02/25 17:13:40  colin
// fixed display, recipients of mail subsystem
//
// Revision 1.26  2003/02/25 07:28:54  colin
// restructured java file paths
//
// Revision 1.25  2003/02/18 17:26:14  colin
// renamed new button property to clear
//
// Revision 1.24  2003/02/18 13:40:14  colin
// added condition not to show the delete button for a new user
//
// Revision 1.23  2003/02/18 10:38:26  colin
// conversion to Struts and pop-ups
//
// Revision 1.22  2003/01/23 16:52:21  peter
// links and forms changed to sslext, checkLogin added
//
// Revision 1.21  2003/01/18 20:38:58  colin
// added help buttons
//
// Revision 1.20  2003/01/14 10:38:56  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.19  2003/01/10 07:48:12  jano
// show only those groups where user can add
// if user created contact -> he can amend if he has not AMEND right
//
// Revision 1.18  2003/01/09 13:30:27  jano
// for new contact set up createdBy user name
//
// Revision 1.17  2002/12/19 12:54:21  jano
// contact should be read Only
//
// Revision 1.16  2002/11/22 16:44:38  peter
// localisation
//
// Revision 1.15  2002/11/21 08:48:26  jano
// we have to correct logic for deleteing person with user at same time
//
// Revision 1.14  2002/11/20 16:58:17  jano
// fixinf adding new contact and add new user ..
//
// Revision 1.13  2002/11/15 09:13:03  colin
// added temporary workaround to disable apply button for new contacts
//
// Revision 1.12  2002/11/12 10:20:51  colin
// added topLevel handling to head tag
//
// Revision 1.11  2002/10/18 08:38:59  colin
// bugfix - the userAliases were not set the first time
//
// Revision 1.10  2002/10/15 10:00:05  colin
// fixed bug when no groups are chosen
//
// Revision 1.9  2002/10/08 16:21:08  colin
// fixed topLevel handling for pop-ups
//
// Revision 1.8  2002/10/03 13:02:36  colin
// bugfixes when Clear/Delete buttons are pressed
//
// Revision 1.7  2002/09/24 09:30:15  colin
// added tooltips (title attribute) to submit buttons
//
// Revision 1.6  2002/09/19 14:38:38  colin
// added employee handling
// fixed user handling
//
// Revision 1.5  2002/08/26 09:20:36  colin
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
<igw:checkLogin />
<%@include file='/include/theme.jspf'%>

<%--
  these are page scope since we want to reinitialize them from the request
  they are written to the session later
--%>
<igw:bean id='addressBookPersonForm' scope='session' type='com.ivata.groupware.business.addressbook.struts.PersonForm'/>
<igw:bean id='person' name='addressBookPersonForm' property='person' type='com.ivata.groupware.business.addressbook.person.PersonDO'/>

<%-- get the addressBook and mail objects --%>
<%@include file='/addressBook/include/addressBook.jspf'%>
<%@include file='/addressBook/include/tree.jspf'%>

<imhtml:html locale='true'>
  <igw:head   bundle='addressBook'
               topLevel='true'
              keepOnTop='false'
               titleKey='<%=addressBookPersonForm.getTitleKey()%>'
              titleArgs='<%=Arrays.asList(new Object[] {person.getFileAs()})%>'>
    <igw:bean id='framesUri'><imhtml:rewrite page='/addressBook/personFrame.jsp'/></igw:bean>
    <igw:bean id='summaryUri'><imhtml:rewrite page='/addressBook/personSummary.jsp'/></igw:bean>
    <script language='javascript'>
      if (window == parent) {
          window.location.href = "<%=framesUri%>";
      } else {
          window.open ("<%=summaryUri%>", "ivataPersonSummary");
      }
      <%--
        // On confirmation (ok/apply) see if the recipients window opened this
      --%>
      if (opener != null && opener.name=="ivataRecipients") {
          document.addressBookPersonForm.refreshOnExit.value = 'false';
      }
      function onConfirm() {
          var thisOpener = top.ivataRecipients;
          if (thisOpener != null) {
              <%--
                // work out whether or not an iframe was used on the opener
              --%>
              <igw:bean id='personTreeNewHref'>
                <imhtml:rewrite page='/util/loading.jsp?page=/addressBook/personTree.jsp' />
              </igw:bean>

              if(thisOpener.personTree != null) {
                  thisOpener.personTree.location.href = "<%= personTreeNewHref %>";
              } else {
                  thisOpener.document.recipientForm.submit();
              }
          }
      }
    </script>
    <imhtml:base/>
  </igw:head>

  <body class='dialog'>
    <imtheme:window>
      <%@include file='/include/errorFrame.jspf'%>
      <%-- delete key is different for users --%>
      <igw:bean id='deleteKey'><bean:write name='addressBookPersonForm' property='deleteKey'/></igw:bean>
      <imhtml:form action='/addressBook/person' referralName='personFormTag' bundle='addressBook' deleteKey='<%=deleteKey%>' resourceFieldPath='person'>
        <%-- only include the tab controls if this is not a new person --%>
        <imtheme:tabFrame>
          <imtheme:tabControl name='personTab' formName='document.addressBookPersonForm'>
            <logic:iterate indexId='tabKeyIndex' id='tabKey' name='addressBookPersonForm' property='tabKeys' type='java.lang.String'>
              <imtheme:tab bundle="addressBook" key='<%=tabKey%>'/>
            </logic:iterate>
          </imtheme:tabControl>
          <imtheme:tabContent>
            <imtheme:frame>
              <jsp:include page='<%=addressBookPersonForm.getTabPage()%>'/>
            </imtheme:frame>
          </imtheme:tabContent>
        </imtheme:tabFrame>

        <% boolean readOnly =  addressBookPersonForm.getReadOnly(); %>
        <% boolean canRemove =  addressBookPersonForm.getCanRemove(); %>
        <% boolean newContact =  (addressBookPersonForm.getPerson().getId()==null); %>
        <imtheme:buttonFrame>
          <imhtml:clear onclick='document.addressBookPersonForm.personTab_activeTab.value="0";' asNewButton='<%=person.getId() != null%>' />

          <c:if test='<%=canRemove%>'>
            <%-- give users the option to delete existing people --%>
            <imhtml:delete/>
          </c:if>
          <imtheme:buttonSpacer/>
          <c:if test='<%=newContact || !readOnly%>'>
            <imhtml:ok onclick='onConfirm(); return true'/>
          </c:if>
          <imhtml:cancel bundle="addressBook"/>
          <c:if test='<%=newContact || !readOnly%>'>
            <imhtml:apply onclick='onConfirm(); return true'/>
          </c:if>
          <imhtml:help />
          <%--
            if this is true (default) then the main window is refreshed with
            the address book contents when this pop up closes.
          --%>
          <html:hidden property='refreshOnExit'/>
        </imtheme:buttonFrame>
      </imhtml:form>
    </imtheme:window>
    <%@include file='/include/script/fixPopUp.jspf'%>
  </body>
</imhtml:html>
