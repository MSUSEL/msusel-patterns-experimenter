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
<%@page import="com.ivata.groupware.business.mail.MailConstants"%>
<%@page import="com.ivata.mask.web.format.DateFormatterConstants"%>
<%@page import="com.ivata.mask.util.StringHandling"%>

<%@page import='java.util.Arrays' %>
<%@page import="java.util.List"%>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: index.jsp,v 1.4 2005/04/28 18:46:15 colinmacleod Exp $
//
// Main page of the intranet web mail.
// Displays a list of messages in the currently selected folder, defaulting to
// the inbox if no other folder was selected.
//
// Since: ivata groupware 0.9 (2001-09-29)
// Author: Colin MacLeod <colin.macleod@ivata.com>
// $Revision: 1.4 $
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
// $Log: index.jsp,v $
// Revision 1.4  2005/04/28 18:46:15  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.3  2005/04/10 19:46:08  colinmacleod
// Removed mail.jspf include.
// Removed nobr tags.
//
// Revision 1.2  2005/04/09 17:20:01  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:51:27  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.7  2005/01/01 12:48:48  colinmacleod
// Cosmetic indentation improvements.
//
// Revision 1.6  2004/11/03 17:16:13  colinmacleod
// Changed todo comments to TODO: all caps.
//
// Revision 1.5  2004/07/14 22:50:27  colinmacleod
// Replaced bean:define with extended igw:bean.
//
// Revision 1.4  2004/07/14 20:59:56  colinmacleod
// Changed all occurrences of jsp:useBean to igw:bean (better tomcat compatibility).
//
// Revision 1.3  2004/07/13 19:48:13  colinmacleod
// Moved project to POJOs from EJBs.
// Applied PicoContainer to services layer (replacing session EJBs).
// Applied Hibernate to persistence layer (replacing entity EJBs).
//
// Revision 1.2  2004/03/21 21:16:39  colinmacleod
// Shortened name to ivata op.
//
// Revision 1.1  2004/03/03 22:34:41  colinmacleod
// Updated webapp structure. Moved core files to core subproject.
//
// Revision 1.1.1.1  2004/01/27 21:00:01  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.5  2003/12/12 13:24:34  jano
// fixing webmail functionality
//
// Revision 1.4  2003/11/13 16:11:08  jano
// commitng everything to CVS
// can deploy and application is ruuning, can login into
//
// Revision 1.3  2003/10/28 13:27:51  jano
// commiting webmail,
// still fixing compile and building openPortal project
//
// Revision 1.2  2003/10/15 14:11:33  colin
// fixing for XDoclet
//
// Revision 1.34  2003/07/06 17:51:07  colin
// Fixed for jetty - need to reset folder name after sent folder
//
// Revision 1.33  2003/04/01 18:13:45  colin
// changed hasScrollbars to hasScrollBars
//
// Revision 1.32  2003/03/26 15:47:32  peter
// fixed links for messages with empty subjects
//
// Revision 1.31  2003/03/14 10:50:41  peter
// sent folder shows recipients. not senders
//
// Revision 1.30  2003/03/04 14:30:37  colin
// fixed javascript
//
// Revision 1.29  2003/03/04 11:14:54  peter
// fixed javascript in message pop-up links
//
// Revision 1.28  2003/03/04 09:22:50  jano
// missing ">" at end of tag
//
// Revision 1.27  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.26  2003/02/28 13:31:39  jano
// fixing : noSelected messges for forward and delete create window.alert with message
//
// Revision 1.25  2003/02/25 17:02:04  colin
// fixed display, recipients
//
// Revision 1.24  2003/02/25 07:30:42  colin
// restructured java file paths
//
// Revision 1.23  2003/02/19 07:50:19  colin
// bugfixes for new imhtml tags
//
// Revision 1.22  2003/02/18 10:55:14  colin
// change popUp attributes to double quotes
// fixed invalid forward
//
// Revision 1.21  2003/01/27 07:24:17  colin
// changed links to pop-ups
//
// Revision 1.20  2003/01/18 20:38:58  colin
// added help buttons
//
// Revision 1.19  2002/11/17 19:23:26  colin
// restructured around struts
// implemented check/uncheck icon
// moved over to message id based folders
// major speed improvements
//
// Revision 1.18  2002/11/12 14:33:27  colin
// bugfixes in subject, sent columns
//
// Revision 1.17  2002/11/12 10:26:02  colin
// removed mail tags
// extended list to include pages and sorting
//
// Revision 1.16  2002/10/24 15:52:22  colin
// converted to use list tags instead of folder iterator
//
// Revision 1.15  2002/10/10 14:03:00  peter
// changes due to demo version
//
// Revision 1.14  2002/09/25 11:47:00  colin
// improved layout and general appearance
//
// Revision 1.13  2002/09/17 07:19:22  peter
// formatter to be fixed
//
// Revision 1.12  2002/09/11 15:58:48  peter
// modified for changes in mail tags
//
// Revision 1.11  2002/09/09 13:27:23  peter
// changed for updated and new tags (move to server side)
//
// Revision 1.10  2002/08/27 08:55:01  peter
// conflict resolved
//
// Revision 1.9  2002/08/27 08:44:29  colin
// split tags and themes into two separate includes
//
// Revision 1.8  2002/07/01 08:07:40  colin
// moved SetPropertyTag to util from webgui
//
// Revision 1.7  2002/06/21 12:00:31  colin
// added superclass MailDisplayTag to the display tag classes
// restructured com.ivata.groupware.web into separate subcategories: fornat, javascript, theme and tree.
//
// Revision 1.6  2002/06/17 07:35:13  colin
// improved and extended documentation
//
// Revision 1.5  2002/06/13 15:45:32  peter
// brought over to peter, fixed bugs in webgui property-settings
//
// Revision 1.4  2002/06/13 11:11:00  colin
// first version with rose model and jboss  integration
//
// Revision 1.3  2002/04/30 15:14:27  colin
// added context paths and tidied up the tag heirarchy
//
// Revision 1.2  2002/04/27 17:40:49  colin
// first compliling version in EJB/JBuilder project
//
// Revision 1.1  2002/04/26 13:17:34  colin
// moved over to the EJB/JBuilder project
//
// Revision 1.19  2002/02/05 19:49:03  colin
// updated for changes to settings class
//
// Revision 1.18  2002/01/22 18:06:46  colin
// udpdated toolbar with slim 3D spacers and changed the order of the toolbar buttons
//
// Revision 1.17  2002/01/22 17:22:26  colin
// Added delete (move to trash) funtionality
// added the folder name to the window title of index.jsp
// added a list of bugs in mailbugs.html
//
// Revision 1.16  2002/01/21 22:17:28  colin
// updated toolbar so buttons are affected by mouseover event
//
// Revision 1.15  2002/01/21 00:51:44  colin
// fixed bug in window width
//
// Revision 1.14  2002/01/21 00:24:26  colin
// reversed the order of reply and forward icons in the toolbar
//
// Revision 1.13  2002/01/20 23:47:29  colin
// changes coding style to match core product
// added toolbar
//
// Revision 1.12  2002/01/20 20:38:31  colin
// updated theme changes from core project
// removed _ from tag names and changed capitalisation
// updated the tag libraries to the latest versions
//
// Revision 1.11  2002/01/20 19:44:59  colin
// consolidated work done on www.ivata.com and on the laptop
//
// Revision 1.10  2002/01/20 19:43:30  colin
// handled the case when the id is outwith bounds in display/compose
// included underscore _ in permitted link characters
//
// Revision 1.6  2001/12/02 21:25:47  colin
// fixed bug when viewing the sent mail folder
// changed caveat on login screen
//
// Revision 1.5  2001/11/24 13:49:00  colin
// display, reply and forward all working
// caveat placed on the login page
//
// Revision 1.4  2001/11/05 22:10:19  colin
// first version with working themes, imutil:setProperty tag
//
// Revision 1.3  2001/10/22 21:02:52  colin
// move from monifieth to lucenec
//
// Revision 1.2  2001/10/09 20:04:18  colin
// adapted tag libraries for shop project
//
// Revision 1.1.1.1  2001/10/06 15:48:03  colin
// initial import of jsp version into cvs
//
////////////////////////////////////////////////////////////////////////////////
--%>

<%@include file='/include/tags.jspf'%>
<igw:checkLogin forward='mailIndex'/>
<%@include file='/include/theme.jspf'%>

<%-- get the messages we will parse thro' --%>
<%
  List messages = (List) session.getAttribute("mailIndexMessages");
  IndexForm mailIndexForm = (IndexForm) request.getAttribute("mailIndexForm");
%>
<%-- if there are no messages in the session, forward to the index action --%>
<c:if test='<%=(messages == null)
                || (mailIndexForm == null)%>'>
  <logic:forward name='mailIndexAction'/>
</c:if>

<igw:bean id='selectedMessageIds' scope='session' type='java.util.HashSet'/>

<igw:bean id='dateFormatter' name="mailIndexForm" property="dateFormatter" type='com.ivata.mask.web.format.DateFormatter'/>

<%-- formatter - subject --%>
<igw:bean id='formatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter' />
<igw:bean id='maximumLength' scope='page' type='com.ivata.mask.web.format.MaximumLengthFormat' />
<jsp:setProperty name='maximumLength' property='maximumLength' value='70'/>
<imformat:addFormat formatter='<%=formatter%>' format='<%=maximumLength%>'/>

<igw:bean id='nonBreaking' scope='page' type='com.ivata.mask.web.format.NonBreakingSpaceFormat' />
<imformat:addFormat formatter='<%=formatter%>' format='<%=nonBreaking%>'/>

<%-- formatter - email addresses --%>
<igw:bean id='addressFormatter' scope='page' type='com.ivata.groupware.web.format.EmailAddressFormatter' />
<jsp:setProperty name='addressFormatter' property='pageContext' value='<%=pageContext%>'/>

<igw:bean id='addressLength' scope='page' type='com.ivata.mask.web.format.MaximumLengthFormat' />
<jsp:setProperty name='addressLength' property='maximumLength' value='20'/>
<imformat:addFormat formatter='<%=addressFormatter%>' format='<%=addressLength%>'/>

<%-- formatter - size --%>
<igw:bean id='sizeFormatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter' />
<igw:bean id='byteSize' scope='page' type='com.ivata.groupware.web.format.ByteSizeFormat' />
<imformat:addFormat formatter='<%=sizeFormatter%>' format='<%=byteSize%>'/>

<%-- this include stores methods to speed up the string concatenation --%>
<%@include file='/mail/include/indexSpeed.jspf'%>

<igw:bean id='folderName' name='mailIndexForm' property='folderName' type='java.lang.String'/>

<%-- pop-up window to display a message --%>
<igw:bean id='displayPopUp' scope='page' type='com.ivata.mask.web.javascript.JavaScriptWindow'/>
<jsp:setProperty name='displayPopUp' property='windowName' value='messageDisplay'/>
<jsp:setProperty name='displayPopUp' property='frameName' value='_blank'/>
<imutil:map id='displayPopUpParams'>
  <imutil:mapEntry name='folderName' value='<%=folderName%>'/>
</imutil:map>
<jsp:setProperty name='displayPopUp' property='page' value='/mail/display.action'/>
<jsp:setProperty name='displayPopUp' property='paramsName' value='displayPopUpParams'/>
<jsp:setProperty name='displayPopUp' property='height' value='600'/>
<jsp:setProperty name='displayPopUp' property='width' value='600'/>
<jsp:setProperty name='displayPopUp' property='hasScrollBars' value='false'/>
<jsp:setProperty name='displayPopUp' property='pageContext' value='<%=pageContext%>'/>

<igw:getSetting id="emailFolderSent" setting="emailFolderSent"/>
<igw:getSetting id="emailFolderTrash" setting="emailFolderTrash"/>

<%@include file='/mail/include/composePopUp.jspf'%>
<imhtml:html>
  <igw:head bundle='mail'
      titleKey='index.title'
      titleArgs='<%=Arrays.asList(new Object[] {folderName})%>'>
    <igw:bean id='alertDelete'><bean:message bundle='mail' key='index.alert.delete'/></igw:bean>
    <igw:bean id='alertNotSelected'><bean:message bundle='mail' key='index.alert.notSelected'/></igw:bean>

    <%-- I did take this from checkAll function document.mailIndexForm.selectAll.value = true; --%>

    <script language='javascript'>
      // <!--
      function checkAll() {

        for(i = 0; i < document.mailIndexForm.selectedMessageIds.length; ++i) {
          document.mailIndexForm.selectedMessageIds[i].checked = true;
        }
      }
      function sortAscending(sortBy) {
        document.mailIndexForm.sortBy.value = sortBy;
        document.mailIndexForm.sortAscending.value = true;
      }
      function sortDescending(sortBy) {
        document.mailIndexForm.sortBy.value = sortBy;
        document.mailIndexForm.sortAscending.value = false;
      }
      function submitDelete() {
        var ok=isSelected();
        if (ok) {
          document.mailIndexForm.moveTo.value='<%=emailFolderTrash%>';
          document.mailIndexForm.submit();
        } else {
          window.alert("<%=alertNotSelected%>");
        }
      }
      function submitDeleteTrash() {
        var ok=isSelected();
        if (ok) {
          var checkDelete = confirm("<%=alertDelete%>");
          if (checkDelete) {
            document.mailIndexForm.deleteTrash.value="true";
            document.mailIndexForm.submit();
          }
        } else {
          window.alert("<%=alertNotSelected%>");
        }
      }
      function submitForward() {
        var ok=isSelected();
        if (ok) {
          <%=composePopUp%>;
          document.mailIndexForm.forwardMessages.value="true";
          var oldTarget = document.mailIndexForm.target;
          document.mailIndexForm.target = "ivataCompose";
          document.mailIndexForm.submit();
          document.mailIndexForm.target = oldTarget;
        } else {
          window.alert("<%=alertNotSelected%>");
        }
      }
      function uncheckAll() {
        document.mailIndexForm.selectAll.value = false;
        document.mailIndexForm.retainPrevious.value = false;
        for(i = 0; i < document.mailIndexForm.selectedMessageIds.length; ++i) {
          document.mailIndexForm.selectedMessageIds[i].checked = false;
        }
      }
      function isSelected() {
        var ok=false;
        if (document.mailIndexForm.selectedMessageIds.checked==true) {
          ok=true;
        } else {
          for(i = 0; i < document.mailIndexForm.selectedMessageIds.length; ++i) {
            if (document.mailIndexForm.selectedMessageIds[i].checked==true) {
              ok=true;
              break;
            }
          }
        }
        return ok;
      }
      //-->
    </script>
    <imhtml:base/>
  </igw:head>
  <body>
    <div align='center'>
      <imhtml:form action='/mail/index'>
        <imtheme:window
            styleClass='mainWindow'
            bundle='mail'
            titleKey='index.title.window'
            titleArgs='<%=Arrays.asList(new Object[] {folderName})%>'>
          <%-- toolbar --%>
          <imtheme:toolBar>
            <imtheme:toolBarButton javaScript='<%=composePopUp.toString()%>' image='compose' bundle='mail' titleKey='title.toolbar.compose'/>
            <imtheme:toolBarSpacer/>

            <imutil:map id='toolbarParams'>
              <imutil:mapEntry name='folderName' value='inbox'/>
            </imutil:map>
            <imtheme:toolBarButton page='/mail/index.jsp' paramsName='toolbarParams' image='check' bundle='mail' titleKey='title.toolbar.check'/>
            <imutil:mapEntry mapName='toolbarParams' name='folderName' value='sent'/>
            <imtheme:toolBarButton page='/mail/index.jsp' paramsName='toolbarParams' image='sent' bundle='mail' titleKey='title.toolbar.sent'/>
            <%-- for jetty, we need to set it back to the folder name for the body !! --%>
            <imutil:mapEntry mapName='toolbarParams' name='folderName' value='<%=folderName%>'/>
            <imtheme:toolBarSpacer/>
            <%-- TODO: this button is not working right now
            <imtheme:toolBarButton javaScript='submitForward()' image='forward' bundle='mail' titleKey='index.title.toolbar.forward'/>
            <imtheme:toolBarSpacer/>
            --%>
            <%--
              if this is the trash folder, then the icon gives you the option
              to completely delete the message, not just move them to  trash
            --%>
            <c:choose>
              <c:when test='<%=mailIndexForm.getFolderName().equals(emailFolderTrash)%>'>
                <imtheme:toolBarButton javaScript='submitDeleteTrash()' image='trash' bundle='mail' titleKey='index.title.toolbar.delete' />
              </c:when>
              <c:otherwise>
                <imtheme:toolBarButton javaScript='submitDelete()' image='delete' bundle='mail' titleKey='index.title.toolbar.trash' />
              </c:otherwise>
            </c:choose>
          </imtheme:toolBar>

          <%-- list starts here --%>
          <imtheme:listFrame sortInternally='false' defaultSortColumn='4'
                  defaultSortAscending='false' formName='document.mailIndexForm'>
            <imtheme:listHeading>
              <%--
                need to add this style to ensure there is no padding around
                the image
              --%>
              <imtheme:listColumnHeading
                  link='false'
                  style='padding: 0px 0px 0px 0px;'
                  styleClass='hilight'><html:img page='/images/list/checkUncheck.gif' width='27' height='22' border='0' usemap='#map'/></imtheme:listColumnHeading>
              <igw:bean id='titleCheckAll'><bean:message bundle='mail' key='index.title.check.all'/></igw:bean>
              <igw:bean id='titleUncheckAll'><bean:message bundle='mail' key='index.title.uncheck.all'/></igw:bean>
              <map NAME="map">
                <area shape='POLY' coords='26,0,0,21,0,0' href='' onclick='checkAll();return false' title='<%=titleCheckAll%>'/>
                <area shape='POLY' coords='26,0,0,21,26,21,26,20' href='' onclick='uncheckAll();return false' title='<%=titleUncheckAll%>'/>
              </map>
              <c:choose>
                <c:when test='<%=mailIndexForm.getFolderName().equals(emailFolderSent)%>'>
                  <imtheme:listColumnHeading
                      ascendingScript='<%=getSortAscending(MailConstants.SORT_RECIPIENTS)%>'
                      descendingScript='<%=getSortDescending(MailConstants.SORT_RECIPIENTS)%>'
                      ><bean:message bundle='mail' key='index.column.to'/></imtheme:listColumnHeading>
                </c:when>
                <c:otherwise>
                  <imtheme:listColumnHeading
                      ascendingScript='<%=getSortAscending(MailConstants.SORT_SENDERS)%>'
                      descendingScript='<%=getSortDescending(MailConstants.SORT_SENDERS)%>'
                      ><bean:message bundle='mail' key='index.column.from'/></imtheme:listColumnHeading>
                </c:otherwise>
              </c:choose>
              <imtheme:listColumnHeading
                  ascendingScript='<%=getSortAscending(MailConstants.SORT_SUBJECT)%>'
                  descendingScript='<%=getSortDescending(MailConstants.SORT_SUBJECT)%>'
                  ><bean:message bundle='mail' key='index.column.subject'/></imtheme:listColumnHeading>
              <imtheme:listColumnHeading
                  ascendingScript='<%=getSortAscending(MailConstants.SORT_SIZE)%>'
                  descendingScript='<%=getSortDescending(MailConstants.SORT_SIZE)%>'
                  ><bean:message bundle='mail' key='index.column.size'/></imtheme:listColumnHeading>
              <imtheme:listColumnHeading
                  ascendingScript='<%=getSortAscending(MailConstants.SORT_SENT)%>'
                  descendingScript='<%=getSortDescending(MailConstants.SORT_SENT)%>'
                  ><bean:message bundle='mail' key='index.column.sent'/></imtheme:listColumnHeading>
            </imtheme:listHeading>

            <imtheme:listBody var='message' rows='<%=messages%>'>
              <igw:bean id='message' type='com.ivata.groupware.business.mail.message.MessageDO'/>
              <imtheme:listColumn>
                <html:multibox property='selectedMessageIds' ><%=message.getMessageID()%></html:multibox>
                <html:hidden property='messageIds' value='<%=message.getMessageID()%>'/>
              </imtheme:listColumn>
              <imtheme:listColumn>
                <c:choose>
                  <c:when test='<%=mailIndexForm.getFolderName().equals(emailFolderSent)%>'>
                    <%=addressFormatter.format(message.getRecipients())%>
                  </c:when>
                  <c:otherwise>
                    <%=addressFormatter.format(message.getSenders())%>
                  </c:otherwise>
                </c:choose>
              </imtheme:listColumn>
              <imtheme:listColumn>
                <nobr>
                  <imutil:mapEntry mapName='displayPopUpParams' name='id' value='<%=message.getMessageID()%>'/>
                  <a href='' onclick='<%=displayPopUp%>return false'>
                    <%=formatter.format(StringHandling.isNullOrEmpty(message.getSubject())?"[none]":message.getSubject())%>
                  </a>
                </nobr>
              </imtheme:listColumn>
              <%-- TODO: implement different sort types here --%>
              <imtheme:listColumn><%=sizeFormatter.format(message.getSize().toString())%></imtheme:listColumn>
              <% String dateDisplay="[none]";%>
              <c:if test='<%=message.getSent() != null%>'>
                <%dateDisplay = dateFormatter.format(message.getSent().getTime());%>
              </c:if>
              <imtheme:listColumn><%=dateDisplay%></imtheme:listColumn>
            </imtheme:listBody>
            <imtheme:listEmpty>
              <bean:message bundle='mail' key='index.label.list.empty'/>
            </imtheme:listEmpty>
          </imtheme:listFrame>
          <imtheme:buttonFrame>
            <imhtml:help key='mail.index'/>
          </imtheme:buttonFrame>
        </imtheme:window>
        <html:hidden property='folderName'/>
        <html:hidden property='moveTo'/>
        <html:hidden property='deleteTrash'/>
        <%--
          this flag is picked up by index action and tells it to keep the
          selected ids. good when we are changing pages, for example
        --%>
        <html:hidden property='retainPrevious' value='true'/>
        <html:hidden property='selectAll' value='false'/>
        <html:hidden property='sortAscending'/>
        <html:hidden property='sortBy'/>
        <%-- the following is a boolean for the thread action --%>
        <html:hidden property='forwardMessages'/>
      </imhtml:form>
    </div>
  </body>
</imhtml:html>
