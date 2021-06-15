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

<%@page import="com.ivata.mask.web.format.FormatConstants" %>
<%@page import="com.ivata.mask.web.RewriteHandling"%>
<%@page import="com.ivata.groupware.business.drive.file.FileDO" %>
<%@page import="com.ivata.groupware.business.mail.*" %>
<%@page import="com.ivata.groupware.business.mail.struts.DisplayForm" %>
<%@page import="com.ivata.mask.util.*" %>
<%@page import="java.util.*" %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: display.jsp,v 1.5 2005/04/28 18:46:15 colinmacleod Exp $
//
// Display an existing message, with attachments underneath in 'icon view'
// layout.
//
// Since: ivata groupware 0.9 (2001-09-30)
// Author: Colin MacLeod <colin.macleod@ivata.com>
// $Revision: 1.5 $
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
// $Log: display.jsp,v $
// Revision 1.5  2005/04/28 18:46:15  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.4  2005/04/22 11:00:56  colinmacleod
// Removed mail.jspf include.
//
// Revision 1.3  2005/04/10 19:46:08  colinmacleod
// Removed mail.jspf include.
// Removed nobr tags.
//
// Revision 1.2  2005/04/09 17:20:01  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:51:25  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.7  2004/11/12 15:57:26  colinmacleod
// Removed dependencies on SSLEXT.
// Moved Persistence classes to ivata masks.
//
// Revision 1.6  2004/11/03 17:16:13  colinmacleod
// Changed todo comments to TODO: all caps.
//
// Revision 1.5  2004/09/30 15:10:37  colinmacleod
// Changed syntax of santizer format.
//
// Revision 1.4  2004/07/14 22:50:27  colinmacleod
// Replaced bean:define with extended igw:bean.
//
// Revision 1.3  2004/07/14 20:59:56  colinmacleod
// Changed all occurrences of jsp:useBean to igw:bean (better tomcat compatibility).
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
// Revision 1.3  2003/10/28 13:27:51  jano
// commiting webmail,
// still fixing compile and building openPortal project
//
// Revision 1.2  2003/10/15 14:11:33  colin
// fixing for XDoclet
//
// Revision 1.40  2003/06/20 18:43:39  peter
// fixed conditions for image parsing and iframes have unique keys
//
// Revision 1.39  2003/06/10 05:56:13  peter
// img and html:img replaced with size-aware imhtml:img
//
// Revision 1.38  2003/05/27 17:16:08  peter
// fixed to reflect tchanges in FileDO - attachment
//
// Revision 1.37  2003/05/14 11:23:24  peter
// mimeImages made full size again
//
// Revision 1.36  2003/05/13 15:26:04  peter
// compose attachment changes
//
// Revision 1.35  2003/05/12 09:46:42  peter
// attachments display works with new DO structure
//
// Revision 1.34  2003/05/06 13:32:20  peter
// added embedded IMG attachments functionality
//
// Revision 1.33  2003/03/25 08:18:31  jano
// fixing bug with no message found
//
// Revision 1.32  2003/03/19 14:48:00  peter
// fixed delete javascript
//
// Revision 1.31  2003/03/05 19:00:12  colin
// handled null subject
//
// Revision 1.30  2003/03/04 18:00:25  colin
// added fieldName to close button
//
// Revision 1.29  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.28  2003/02/28 14:54:59  peter
// mail links target to self instead of _blank
//
// Revision 1.27  2003/02/27 17:30:55  peter
// attachment links fixed
//
// Revision 1.26  2003/02/25 17:02:04  colin
// fixed display, recipients
//
// Revision 1.25  2003/02/25 07:30:42  colin
// restructured java file paths
//
// Revision 1.24  2003/02/18 10:55:38  colin
// added pageContext to emailAddressFormatter
//
// Revision 1.23  2003/01/27 08:03:54  colin
// added missing mail bundle to toolbar entries
//
// Revision 1.22  2003/01/27 07:23:17  colin
// made pop-up windows
//
// Revision 1.21  2003/01/18 20:38:58  colin
// added help buttons
//
// Revision 1.20  2002/11/17 19:24:12  colin
// added forward/reply only for plain text
//
// Revision 1.19  2002/11/12 10:25:08  colin
// removed mail tags
// temporarily disabled reply, forward, trash
//
// Revision 1.18  2002/10/10 14:03:00  peter
// changes due to demo version
//
// Revision 1.17  2002/09/25 15:29:00  colin
// added title/tooltip attributes to buttons/submits
//
// Revision 1.16  2002/09/25 11:46:34  colin
// improved toolbar
// fixed close button
// improved email address formatting
//
// Revision 1.15  2002/09/17 07:37:00  peter
// bug found in the page's form
//
// Revision 1.14  2002/09/17 07:19:22  peter
// formatter to be fixed
//
// Revision 1.13  2002/09/13 13:58:32  peter
// FIXED formatter and tag parameter bugs
//
// Revision 1.12  2002/09/11 15:58:48  peter
// modified for changes in mail tags
//
// Revision 1.11  2002/09/09 13:27:23  peter
// changed for updated and new tags (move to server side)
//
// Revision 1.10  2002/08/29 15:17:56  peter
// page updated for new mail tags (EJB...)
//
// Revision 1.9  2002/08/27 08:44:29  colin
// split tags and themes into two separate includes
//
// Revision 1.8  2002/08/02 12:15:43  peter
// added attachment listing
//
// Revision 1.6  2002/06/21 12:00:31  colin
// added superclass MailDisplayTag to the display tag classes
// restructured com.ivata.groupware.web into separate subcategories: fornat, javascript, theme and tree.
//
// Revision 1.5  2002/06/17 07:35:13  colin
// improved and extended documentation
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
// Revision 1.1  2002/04/26 13:17:32  colin
// moved over to the EJB/JBuilder project
//
// Revision 1.16  2002/02/05 19:49:03  colin
// updated for changes to settings class
//
// Revision 1.15  2002/01/22 18:51:21  colin
// fixed bugs in toolbar
//
// Revision 1.14  2002/01/22 18:06:46  colin
// udpdated toolbar with slim 3D spacers and changed the order of the toolbar buttons
//
// Revision 1.13  2002/01/22 17:22:26  colin
// Added delete (move to trash) funtionality
// added the folder name to the window title of index.jsp
// added a list of bugs in mailbugs.html
//
// Revision 1.12  2002/01/21 00:24:26  colin
// reversed the order of reply and forward icons in the toolbar
//
// Revision 1.11  2002/01/20 23:47:29  colin
// changes coding style to match core product
// added toolbar
//
// Revision 1.10  2002/01/20 20:38:31  colin
// updated theme changes from core project
// removed _ from tag names and changed capitalisation
// updated the tag libraries to the latest versions
//
// Revision 1.9  2002/01/20 19:44:59  colin
// consolidated work done on www.ivata.com and on the laptop
//
// Revision 1.8  2001/12/26 23:37:03  colin
// improved link handling
//
// Revision 1.7  2001/12/20 17:12:33  colin
// tidied up the HTMLFormatter code, and improved InternetAddress handling
// truncated email addresses which are too long in index.jsp
// changed non-alphanumeric symbols (such as apostrophe ') to their html equivalents in compose and display
// made error messages in the compose window limited in width (word-wrap)
// converted URLs into links
//
// Revision 1.6  2001/12/12 20:26:25  colin
// fixed the following bugs:
// made display window same width as index
// made leading spaces non-breaking
// put a little header with from/to/subject information before forwarded and replied to mails
// stopped append re: to messages which are already re: (same applies to forward)
// convert people's email addresses into links (mailto)
// word wrap the messages (replies) in compose
// make the subject line spread right over the whole space in display
//
// Revision 1.5  2001/12/05 17:14:43  colin
// fixed bug in iterator which did not display last message in the folder-R .
// first attempt at getting message deletion to work
// first attempt at getting stack based cancel buttons implemented
//
// Revision 1.4  2001/11/24 13:49:00  colin
// display, reply and forward all working
// caveat placed on the login page
//
// Revision 1.3  2001/11/05 22:10:19  colin
// first version with working themes, imutil:setProperty tag
//
// Revision 1.2  2001/10/22 21:01:50  colin
// move from monifieth to lucenec
//
// Revision 1.1.1.1  2001/10/06 15:48:04  colin
// initial import of jsp version into cvs
//
////////////////////////////////////////////////////////////////////////////////
--%>

<%@include file='/include/tags.jspf'%>
<igw:checkLogin forward='mailIndex'/>
<%@include file='/include/theme.jspf'%>

<%-- if we don't have a  --%>
<logic:notPresent name='mailDisplayForm'>
  <logic:forward name='mailIndex'/>
</logic:notPresent>

<%-- create a new formatter object for the email addresses --%>
<igw:bean id='emailAddressFormatter' scope='page' type='com.ivata.groupware.web.format.EmailAddressFormatter' />
<jsp:setProperty name='emailAddressFormatter' property='pageContext' value='<%=pageContext%>'/>

<igw:bean id='formatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter'/>

<igw:bean id='sizeFormatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter' />
<igw:bean id='byteSize' scope='page' type='com.ivata.groupware.web.format.ByteSizeFormat' />
<imformat:addFormat formatter='<%=sizeFormatter%>' format='<%=byteSize%>'/>

<igw:bean id='mailDisplayForm' scope='request' type='com.ivata.groupware.business.mail.struts.DisplayForm'/>
<igw:bean id='message' name='mailDisplayForm' property='message' type='com.ivata.groupware.business.mail.message.MessageDO'/>
<%String subject = StringHandling.getNotNull(message.getSubject(), "[none]");%>

<c:choose>
  <c:when test='<%= message.getFormat() == FormatConstants.FORMAT_TEXT%>'>
    <igw:bean id='characterEntities' scope='page' type='com.ivata.mask.web.format.CharacterEntityFormat' />
    <imformat:addFormat formatter='<%=formatter%>' format='<%=characterEntities%>'/>
    <igw:bean id='lineBreaks' scope='page' type='com.ivata.mask.web.format.LineBreakFormat' />
    <jsp:setProperty name='lineBreaks' property='convertLineBreaks' value='true'/>
    <imformat:addFormat formatter='<%=formatter%>' format='<%=lineBreaks%>'/>
  </c:when>
  <c:when test='<%= !message.getAttachments().isEmpty() && (FormatConstants.FORMAT_HTML == message.getFormat())%>'>
      <igw:bean id='embeddedImages' type='com.ivata.groupware.web.format.SanitizerFormat' />
      <jsp:setProperty name='embeddedImages' property='imageUri' value='<%= RewriteHandling.getContextPath(request) + "/download/mail/" + message.getFolderName() + "/" + message.getId() + "/" %>' />
      <c:if test='<%= request.isRequestedSessionIdFromURL() %>'>
        <jsp:setProperty name='embeddedImages' property='imageUriAppend' value='<%= ";jsessionid=" + request.getRequestedSessionId() %>' />
      </c:if>
      <imformat:addFormat formatter='<%=formatter%>' format='<%=embeddedImages%>'/>
  </c:when>
</c:choose>
<%@include file='/mail/include/composePopUp.jspf'%>

<imhtml:html>
  <igw:head bundle='mail'
      titleKey='display.title'
      titleArgs='<%=Arrays.asList(new Object[] {subject})%>'
      topLevel='true'>
    <imhtml:base/>
    <igw:bean id='alertDelete'><bean:message bundle='mail' key='display.alert.delete'/></igw:bean>
    <script language='javascript'>
      // <!--
      function submitDelete() {
        var deleteField = document.getElementById("delete");
        deleteField.value="true";
        document.mailDisplayForm.submit();
      }
      function submitDeleteTrash() {
        var checkDelete = confirm("<%=alertDelete%>");
        if (checkDelete) {
          document.mailDisplayForm.deleteTrash.value="true";
          document.mailDisplayForm.submit();
        }
      }
      // <%-- refresh the opener if we have just deleted this mail (moved to trash)--%>
      <%if (mailDisplayForm.getDelete()) {%>
          opener.location.href = opener.location.href;
      <%}%>
      // <%-- close this window if the message was just deleted from trash --%>
      <%if (mailDisplayForm.getDeleteTrash()) {%>
          window.close();
      <%}%>
      //-->
    </script>
  </igw:head>
  <body class='dialog'>
    <imtheme:window>
      <imhtml:form action='/mail/display' resourceFieldPath='display' bundle='mail'>
        <html:hidden property='folderName'/>
        <html:hidden property='id'/>
        <html:hidden styleId='delete' property='delete' value='false'/>
        <html:hidden property='deleteTrash' value='false'/>
        <imtheme:toolBar>
          <imutil:map id='toolbarParams'>
            <imutil:mapEntry name='folderName' value='inbox'/>
          </imutil:map>
          <imtheme:toolBarButton javaScript='<%=composePopUp.toString()%>' image='compose' bundle='mail' titleKey='title.toolbar.compose'/>
          <imtheme:toolBarSpacer/>

          <imutil:mapEntry mapName='toolbarParams' name='folderName' value='<%=request.getParameter("folderName")%>'/>
          <imutil:mapEntry mapName='toolbarParams' name='id' value='<%=request.getParameter("id")%>'/>
          <imutil:mapEntry mapName='toolbarParams' name='thread' value='<%=MailConstants.THREAD_REPLY.toString()%>'/>
          <imtheme:toolBarButton page='/mail/display.action' paramsName='toolbarParams' image='reply' bundle='mail' titleKey='display.title.toolbar.reply'/>
          <imutil:mapEntry mapName='toolbarParams' name='thread' value='<%=MailConstants.THREAD_REPLY_ALL.toString()%>'/>
          <imtheme:toolBarButton page='/mail/display.action' paramsName='toolbarParams' image='replyAll' bundle='mail' titleKey='display.title.toolbar.reply.all'/>
          <imutil:mapEntry mapName='toolbarParams' name='thread' value='<%=MailConstants.THREAD_FORWARD.toString()%>'/>
          <imtheme:toolBarButton page='/mail/display.action' paramsName='toolbarParams' image='forward' bundle='mail' titleKey='display.title.toolbar.forward'/>
          <imtheme:toolBarSpacer/>

          <%--
            if this is the trash folder, then the icon gives you the option
            to completely delete the message, not just move them to  trash
          --%>
          <igw:getSetting id="emailFolderTrash" setting="emailFolderTrash" type="java.lang.String"/>
          <c:choose>
            <c:when test='<%=mailDisplayForm.getFolderName().equals(emailFolderTrash)%>'>
              <imtheme:toolBarButton javaScript='submitDeleteTrash()' image='trash' bundle='mail' titleKey='display.title.toolbar.delete' />
            </c:when>
            <c:otherwise>
              <imtheme:toolBarButton javaScript='submitDelete()' image='delete' bundle='mail' titleKey='display.title.toolbar.trash' />
            </c:otherwise>
          </c:choose>
<%--
          <imtheme:toolBarButton URL='<%= RewriteHandling.getContextPath(request) + "/mail/display.jsp?trash=true&folder=" + request.getParameter( "folder" ) + "&id=" + request.getParameter( "id" ) %>' image='delete' title='Trash the Message' />
--%>
        </imtheme:toolBar>
          <%-- if there are attachments, go thro' each one and create a download link --%>
          <c:if test='<%= message.getAttachments()!=null && !message.getAttachments().isEmpty() %>'>
            <imtheme:frame styleId='attachmentsFrame' style='height: auto;'>
              <imtheme:framePane style='padding: 5px 5px 5px 5px;' propertiesName='propertiesNormal'>
                  <small><strong>Note:</strong> This message has attachments, but mail attachments are not supported in this version of ivata groupware!<br/>
                  You will need to use another web client to read attachments from this message.</small>
                  <%--
                <imhtml:iframe style='border: 0px;' formName='mailDisplayForm' frameName='<%= "ivataMailDisplayAttachmentsIFrame_" + message.getMessageID() %>' align='block'>
                  <table border='0' cellpadding='0' cellspacing='0' width='100%' height='100%' class='dialog' valign='top'>
                    <imtheme:listFrame  sortInternally='false' formName='document.mailDisplayForm' styleClass='normal'>
                      <imtheme:listHeading>
                        <imtheme:listColumnHeading link='false' width='5%'>&nbsp;</imtheme:listColumnHeading>
                        <imtheme:listColumnHeading width='80%' align='left'><html:img page='images/empty.gif' width='55' height='1' /><bean:message bundle='mail'  key='label.attachmentName' /></imtheme:listColumnHeading>
                        <imtheme:listColumnHeading width='20%' align='center'><bean:message bundle='mail'  key='label.size' /></imtheme:listColumnHeading>
                      </imtheme:listHeading>
                      <imtheme:listBody var='attachment' rows='<%= message.getAttachments() %>'>
                        <igw:bean id='attachment' type='com.ivata.groupware.business.drive.file.FileDO'/>
                        <igw:bean id='downloadAddress'>
                          <imhtml:rewrite page='<%= "/download/mail/" + message.getFolderName() + "/" + message.getMessageID() + "/" + attachment.getFileName() + "/" + attachment.getComment() %>' />
                        </igw:bean>

                        <imtheme:listColumn align='right'>
                          PROPERTY(currentItemIndex).
                        </imtheme:listColumn>

                        <imtheme:listColumn align='left'>
                          <table border='0' cellspacing='0' cellpadding='0'>
                            <tr>
                              <td><a href='<%= downloadAddress %>' target='_parent'><imhtml:img page='<%= MimeTypesHandling.mimeType2Icon(attachment.getMimeType()) %>' border='0' /></a></td>
                              <td valign='center'><a href='<%= downloadAddress %>' target='_parent'><jsp:getProperty name="attachment" property="comment" /></a></td>
                            </tr>
                          </table>
                        </imtheme:listColumn>

                        <imtheme:listColumn align='center'>
                          <%=sizeFormatter.format(attachment.getSize().toString())%>
                        </imtheme:listColumn>

                      </imtheme:listBody>

                      <imtheme:listEmpty>
                        <bean:message bundle='mail' key='display.label.noAttachments' />
                      </imtheme:listEmpty>

                    </imtheme:listFrame>
                  </table>
                </imhtml:iframe>
                    --%>

              </imtheme:framePane>
            </imtheme:frame>
          </c:if>


        <imtheme:frame>
          <%@include file='/include/errorFrame.jspf'%>
          <imtheme:framePane styleClass='hilight'>
            <div align='center'>
              <table cellpadding='0' cellspacing='5' border='0' width='95%'>
                <tr>
                  <td class='fieldCaption'><bean:message bundle='mail' key='field.to'/></td>
                  <td class='field'><%=StringHandling.getNotNull(emailAddressFormatter.format(message.getRecipients()), "[none]")%></td>
                </tr>
                <logic:present name='message' property='recipientsCC'>
                  <tr>
                    <td class='fieldCaption'><bean:message bundle='mail' key='field.cc'/></td>
                    <td class='field'><%=emailAddressFormatter.format(message.getRecipientsCC())%></td>
                  </tr>
                </logic:present>
                <logic:present name='message' property='recipientsBCC'>
                  <tr>
                    <td class='fieldCaption'><bean:message bundle='mail' key='field.bcc'/></td>
                    <td class='field'><%=emailAddressFormatter.format(message.getRecipientsBCC())%></td>
                  </tr>
                </logic:present>
                <tr>
                  <td class='fieldCaption'><bean:message bundle='mail' key='field.from'/></td>
                  <td class='field'><%=StringHandling.getNotNull(emailAddressFormatter.format(message.getSenders()), "[none]")%></td>
                </tr>
                <tr>
                  <igw:bean id='emptyGif'><imhtml:rewrite page='/images/empty.gif'/></igw:bean>
                  <td class='fieldCaption'><img src='<%=emptyGif%>' width='1' height='20'/></td>
                  <td class='field'><img src='<%=emptyGif%>' width='1' height='20'/></td>
                </tr>
                <tr>
                  <td class='fieldCaption'><bean:message bundle='mail' key='field.subject'/></td>
                  <td class='field'><%=subject%></td>
                </tr>
              </table>
            </div>
          </imtheme:framePane>
          <imtheme:framePane styleClass='normal' style='padding: 2px 2px 2px 2px;'>
            <imhtml:iframe styleClass='watermark' frameName='<%= "ivataMailDisplayFrame" + message.getMessageID() %>'><%=formatter.format(StringHandling.isNullOrEmpty(message.getText())?"[This message has no body.]":message.getText())%></imhtml:iframe>
          </imtheme:framePane>
        </imtheme:frame>
        <imtheme:buttonFrame>
          <imhtml:cancel fieldName='close' onclick='window.close()'/>
          <c:if test='<%=message != null%>'>
<%-- TODO:              <input type='submit' name='editButton' value='Edit' title='Create New Message With Same Contents'/> --%>
          </c:if>
          <imhtml:help key='mail.display'/>
        </imtheme:buttonFrame>
      </imhtml:form>
    </imtheme:window>
  </body>

</imhtml:html>

