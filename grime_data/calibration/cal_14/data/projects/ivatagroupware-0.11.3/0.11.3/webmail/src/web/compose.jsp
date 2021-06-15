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

<%@page import="java.util.*" %>

<%@page import='com.ivata.groupware.business.addressbook.person.PersonDO' %>
<%@page import='com.ivata.groupware.business.mail.*' %>

<%@page import='com.ivata.mask.web.format.FormatConstants' %>
<%@page import='com.ivata.mask.web.format.HTMLFormatter' %>
<%@page import='com.ivata.mask.util.*' %>

<%--
///////////////////////////////////////////////////////////////////////
// $Id: compose.jsp,v 1.4 2005/04/28 18:46:15 colinmacleod Exp $
//
// Compose a new email on this page.
//
// Since: ivata groupware 0.9 (2001-09-29)
// Author: Colin MacLeod <colin.macleod@ivata.com>, peter
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
// $Log: compose.jsp,v $
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
// Revision 1.1.1.1  2005/03/10 17:51:23  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.5  2004/11/03 17:16:13  colinmacleod
// Changed todo comments to TODO: all caps.
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
// Revision 1.1.1.1  2004/01/27 21:00:00  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.3  2003/10/28 13:27:51  jano
// commiting webmail,
// still fixing compile and building openPortal project
//
// Revision 1.2  2003/10/15 14:11:33  colin
// fixing for XDoclet
//
// Revision 1.43  2003/06/10 05:56:13  peter
// img and html:img replaced with size-aware imhtml:img
//
// Revision 1.42  2003/05/27 17:16:08  peter
// fixed to reflect tchanges in FileDO - attachment
//
// Revision 1.41  2003/05/20 06:11:27  peter
// cosmetic change
//
// Revision 1.40  2003/05/20 05:50:55  peter
// cosmetic changes
//
// Revision 1.39  2003/05/16 12:10:26  peter
// styleIds were missing from checkboxes in iFrame
//
// Revision 1.38  2003/05/14 08:31:56  peter
// signature append logic removed
//
// Revision 1.37  2003/05/13 15:26:04  peter
// compose attachment changes
//
// Revision 1.36  2003/05/08 11:01:17  peter
// works on attachments, not finished yet
//
// Revision 1.35  2003/03/25 10:16:48  jano
// character " is problem for javaScript so replace with \"
//
// Revision 1.34  2003/03/25 07:38:44  jano
// fixing bug when signature add each submit loop
//
// Revision 1.33  2003/03/20 15:22:22  jano
// fixing textarea size for IE
//
// Revision 1.32  2003/03/16 20:07:38  peter
// fixed js string definitions format
//
// Revision 1.31  2003/03/14 11:48:38  peter
// td field added for recipients
//
// Revision 1.30  2003/03/14 10:50:57  peter
// fixed js for recipient selector
//
// Revision 1.29  2003/03/04 17:54:10  colin
// added forward to action if form not present
//
// Revision 1.28  2003/03/04 14:25:53  colin
// added signatures
//
// Revision 1.27  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.26  2003/02/25 17:02:04  colin
// fixed display, recipients
//
// Revision 1.25  2003/02/25 11:53:45  colin
// bugfixes and minor restructuring
//
// Revision 1.24  2003/02/25 07:30:42  colin
// restructured java file paths
//
// Revision 1.23  2003/02/19 07:50:19  colin
// bugfixes for new imhtml tags
//
// Revision 1.22  2003/02/18 13:15:24  colin
// reverted to single quotes for popups
//
// Revision 1.21  2003/02/18 10:55:54  colin
// added to request parameter for EmailAddressFormatter
//
// Revision 1.20  2003/01/27 07:23:17  colin
// made pop-up windows
//
// Revision 1.19  2003/01/18 20:38:58  colin
// added help buttons
//
// Revision 1.18  2002/11/17 19:25:06  colin
// changed 'folder' to 'folderName'
//
// Revision 1.17  2002/11/12 10:24:24  colin
// moved structure over to Struts
// removed business logic and mail tags
//
// Revision 1.16  2002/10/10 14:03:00  peter
// changes due to demo version
//
// Revision 1.15  2002/09/26 08:45:23  colin
// bugfixes when there is no MessageDO in pageContext
//
// Revision 1.14  2002/09/25 15:29:00  colin
// added title/tooltip attributes to buttons/submits
//
// Revision 1.13  2002/09/25 11:45:09  colin
// improved layout
// added popup to choose recipients
//
// Revision 1.12  2002/09/17 07:19:22  peter
// formatter to be fixed
//
// Revision 1.11  2002/09/13 13:58:32  peter
// FIXED formatter and tag parameter bugs
//
// Revision 1.10  2002/09/11 15:58:48  peter
// modified for changes in mail tags
//
// Revision 1.9  2002/09/09 13:27:23  peter
// changed for updated and new tags (move to server side)
//
// Revision 1.8  2002/08/27 08:44:29  colin
// split tags and themes into two separate includes
//
// Revision 1.7  2002/07/01 08:07:40  colin
// moved SetPropertyTag to util from webgui
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
// Revision 1.1  2002/04/26 13:17:29  colin
// moved over to the EJB/JBuilder project
//
// Revision 1.15  2002/02/05 19:49:03  colin
// updated for changes to settings class
//
// Revision 1.14  2002/01/22 18:51:21  colin
// fixed bugs in toolbar
//
// Revision 1.13  2002/01/22 18:06:46  colin
// udpdated toolbar with slim 3D spacers and changed the order of the toolbar buttons
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
///////////////////////////////////////////////////////////////////////
--%>

<%@include file='/include/tags.jspf'%>
<igw:checkLogin forward='mailIndex'/>
<%@include file='/include/theme.jspf'%>
<%@include file='/addressBook/include/addressBook.jspf'%>

<logic:notPresent name='mailComposeForm'>
  <logic:forward name='mailComposeAction'/>
</logic:notPresent>

<igw:bean id='popUp' scope='page' type='com.ivata.mask.web.javascript.JavaScriptWindow'/>
<jsp:setProperty name='popUp' property='frameName' value='ivataRecipients'/>
<jsp:setProperty name='popUp' property='page' value='/mail/recipients.jsp'/>
<jsp:setProperty name='popUp' property='pageContext' value='<%=pageContext%>'/>
<jsp:setProperty name='popUp' property='width' value='750'/>
<jsp:setProperty name='popUp' property='height' value='570'/>

<igw:bean id='uploadPopUp' scope='page' type='com.ivata.mask.web.javascript.JavaScriptWindow'/>
<jsp:setProperty name='uploadPopUp' property='frameName' value='ivataMailAttachments'/>
<jsp:setProperty name='uploadPopUp' property='page' value='/mail/upload.action'/>
<jsp:setProperty name='uploadPopUp' property='pageContext' value='<%=pageContext%>'/>
<jsp:setProperty name='uploadPopUp' property='width' value='500'/>
<jsp:setProperty name='uploadPopUp' property='height' value='400'/>

<igw:bean id='message' name='mailComposeForm' property='message' type='com.ivata.groupware.business.mail.message.MessageDO'/>

<igw:bean id='sizeFormatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter' />
<igw:bean id='byteSize' scope='page' type='com.ivata.groupware.web.format.ByteSizeFormat' />
<imformat:addFormat formatter='<%=sizeFormatter%>' format='<%=byteSize%>'/>

<igw:bean id='jsFormatter' type='com.ivata.mask.web.format.HTMLFormatter'/>
<igw:bean id='replaceQuotes' type='com.ivata.mask.web.format.SearchReplaceFormat'/>
<jsp:setProperty name='replaceQuotes' property='search' value='\''/>
<jsp:setProperty name='replaceQuotes' property='replace' value='\\\''/>
<imformat:addFormat formatter='<%=jsFormatter%>' format='<%=replaceQuotes%>'/>
<igw:bean id='replaceLines' type='com.ivata.mask.web.format.SearchReplaceFormat'/>
<jsp:setProperty name='replaceLines' property='search' value='<%="\n"%>'/>
<jsp:setProperty name='replaceLines' property='replace' value='<%="\\\n"%>'/>
<imformat:addFormat formatter='<%=jsFormatter%>' format='<%=replaceLines%>'/>
<igw:bean id='replaceReturns' type='com.ivata.mask.web.format.SearchReplaceFormat'/>
<jsp:setProperty name='replaceReturns' property='search' value='<%="\r"%>'/>
<jsp:setProperty name='replaceReturns' property='replace' value='<%=""%>'/>
<imformat:addFormat formatter='<%=jsFormatter%>' format='<%=replaceReturns%>'/>
<igw:bean id='replaceDA' type='com.ivata.mask.web.format.SearchReplaceFormat'/>
<%String tmp="\""; %>
<jsp:setProperty name='replaceDA' property='search' value='<%=tmp%>'/>
<%String tmp1="\\\"";%>
<jsp:setProperty name='replaceDA' property='replace' value='<%=tmp1%>'/>
<imformat:addFormat formatter='<%=jsFormatter%>' format='<%=replaceDA%>'/>

<igw:getSetting id='textSignature' setting='emailSignatureText' type='java.lang.String'/>
<igw:bean id='textSignatureOut'><%=jsFormatter.format(textSignature)%></igw:bean>
<igw:getSetting id='htmlSignature' setting='emailSignatureHtml' type='java.lang.String'/>
<igw:bean id='htmlSignatureOut'><%=jsFormatter.format(htmlSignature)%></igw:bean>
<igw:getSetting id='replyPrefix' setting='emailSubjectReplyPrefix' type='java.lang.String'/>
<igw:getSetting id='forwardPrefix' setting='emailSubjectForwardPrefix' type='java.lang.String'/>
<%
  String useSignature = (FormatConstants.FORMAT_HTML == message.getFormat() ? htmlSignature : textSignature);
%>
<igw:getSetting id='replySignature' setting='emailSignatureReply' type='java.lang.Boolean'/>
<igw:getSetting id='forwardSignature' setting='emailSignatureReply' type='java.lang.Boolean'/>
<%--
TODO: this should definitely be in an action
--%>
<igw:bean id='securitySession' scope='session' type='com.ivata.groupware.admin.security.server.SecuritySession'/>
<igw:bean id='user' name='securitySession' property="user" type='com.ivata.groupware.admin.security.user.UserDO'/>
<%
PersonDO person = addressBook.findPersonByUserName(securitySession, user.getName());
%>
<igw:getSetting id='emailEnable' setting='emailEnable' type='java.lang.Boolean'/>

<%--
  if it is reply and reply signature is off, or it is forward and forward
  signature is off, make them empty
--%>

<c:choose>
  <c:when test='<%=(message.getSubject() != null)
                  && ((message.getSubject().startsWith(replyPrefix) && !replySignature.booleanValue())
                      || (message.getSubject().startsWith(forwardPrefix) && !forwardSignature.booleanValue()))%>'>
      <% textSignature = htmlSignature = "";%>
  </c:when>
  <c:when test='<%=StringHandling.isNullOrEmpty(message.getText())%>'>
    <%message.setText("\n\n\n"+useSignature);%>
  </c:when>
</c:choose>

<igw:bean id='javaScript'>
  function selectRecipients() {
      <%=popUp%>
  }
  function onChangeFormat() {
      var format = document.getElementById("format");
      var text = document.getElementById("text");
      var textSignature = "<%=textSignatureOut%>";
      var htmlSignature = "<%=htmlSignatureOut%>";
      if (format.value == '<%=FormatConstants.FORMAT_HTML%>') {
          var positionText = text.value.lastIndexOf(textSignature);
          if (positionText == (text.value.length - textSignature.length)) {
              text.value = text.value.substring(0, positionText) + htmlSignature;
          }
      } else {
          var positionHtml = text.value.lastIndexOf(htmlSignature);
          if (positionHtml == (text.value.length - htmlSignature.length)) {
              text.value = text.value.substring(0, positionHtml) + textSignature;
          }
      }
  }
</igw:bean>

<imhtml:html>
  <igw:head bundle='mail' titleKey='compose.title' topLevel='true' javaScript='<%=javaScript%>'>
    <imhtml:base/>
  </igw:head>



  <body class='dialog'>
    <imtheme:window>
      <imhtml:form action='/mail/compose' bundle='mail' resourceFieldPath='compose'>
        <html:hidden property='folderName' value='<%= StringHandling.getNotNull(pageContext.getAttribute("folderName")) %>' />
        <html:hidden property='attach' />
        <%-- next section puts out a message when sending failed --%>
        <logic:present name='mailComposeForm' property='messagingException'>
          <igw:bean id='messagingException' name='mailComposeForm' property='messagingException' type='javax.mail.MessagingException'/>

          <igw:bean id='exceptionFormatter' type='com.ivata.mask.web.format.HTMLFormatter'/>
          <igw:bean id='characterEntityFormat' type='com.ivata.mask.web.format.CharacterEntityFormat'/>
          <imformat:addFormat formatter='<%=exceptionFormatter%>' format='<%=characterEntityFormat%>'/>
          <igw:bean id='lineBreaks' type='com.ivata.mask.web.format.LineBreakFormat'/>
          <jsp:setProperty name='lineBreaks' property='convertLineBreaks' value='true'/>
          <imformat:addFormat formatter='<%=exceptionFormatter%>' format='<%=lineBreaks%>'/>

          <igw:bean id='exceptionMessage'><imformat:format formatter='<%=exceptionFormatter%>'><bean:write name='mailComposeForm' property='messagingException.message'/></imformat:format><br/></igw:bean>

          <imtheme:frame>
            <imtheme:framePane propertiesName='propertiesError'>
              <p><bean:message bundle='mail' key='compose.alert.send' arg0='<%=exceptionMessage%>'/></p>
            </imtheme:framePane>
          </imtheme:frame>
        </logic:present>

        <%@include file='/include/errorFrame.jspf'%>
        <imtheme:frame>
          <imtheme:framePane propertiesName='propertiesHilight'>
            <table cellpadding='0' cellspacing='4' border='0' height='100%' width='100%'>
              <tr>
                <td class='fieldCaption'><imhtml:button fieldName='button_recipients'
                                                         onclick='selectRecipients()'
                                                          bundle='mail'/></td>
                <%-- check for 'to' request parameter - used in EmailAddressFormatter --%>
                <c:choose>
                  <c:when test='<%=request.getParameter("to") != null%>'>
                    <td class='field'><imhtml:text fieldName='recipients' mandatory='true' value='<%=request.getParameter("to")%>' size='30'/></td>
                  </c:when>
                  <c:otherwise>
                    <td class='field'><imhtml:text fieldName='recipients' mandatory='true' size='30'/></td>
                  </c:otherwise>
                </c:choose>
              </tr>
              <tr>
                <td class='fieldCaption'><imhtml:button fieldName='button_recipientsCC'
                                                         onclick='selectRecipients()'
                                                          bundle='mail'/></td>
                <td class='field'><imhtml:text fieldName='recipientsCC' size='30'/></td>
              </tr>
              <tr>
                <td class='fieldCaption'><imhtml:button fieldName='button_recipientsBCC'
                                                         onclick='selectRecipients()'
                                                          bundle='mail'/></td>
                <td class='field'><imhtml:text fieldName='recipientsBCC' size='30'/></td>
              </tr>
              <tr>
                <td colspan='2' class='fieldCaption'>&nbsp;</td>
              </tr>
              <tr>
                <td class='fieldCaption'><imhtml:label fieldName='subject'/></td>
                <td class='field'><imhtml:text fieldName='subject' property='message.subject' mandatory='true' size='30'/></td>
              </tr>
              <tr>
                <td colspan='2' class='fieldCaption'>&nbsp;</td>
              </tr>
              <tr height='100%'>
                <td colspan='2' height='100%'>
                  <imhtml:textarea mandatory='true' fieldName='text' property='message.text' cols='30'/>
                </td>
              </tr>
            </table>
          </imtheme:framePane>
        </imtheme:frame>


        <c:if test='<%= message.getAttachments()!=null && !message.getAttachments().isEmpty() %>'>
          <imtheme:frame>
            <imtheme:framePane style='padding: 5px 5px 5px 5px;' propertiesName='propertiesNormal'>

              <imhtml:iframe style='border: 0px;' formName='mailComposeForm' frameName='ivataMailComposeAttachmentsIFrame' align='block'>
                <table border='0' cellpadding='0' cellspacing='0' width='100%' height='100%' class='dialog' valign='top'>
                  <imtheme:listFrame  sortInternally='false' formName='document.mailComposeForm' styleClass='normal'>  <%-- defaultSortColumn='2' --%>
                    <imtheme:listHeading>
                      <imtheme:listColumnHeading link='false'>&nbsp;</imtheme:listColumnHeading>
                      <imtheme:listColumnHeading link='false'>&nbsp;</imtheme:listColumnHeading>
                      <imtheme:listColumnHeading><html:img page='images/empty.gif' width='55' height='1' /><bean:message bundle='mail'  key='label.attachmentName' /></imtheme:listColumnHeading>
                      <imtheme:listColumnHeading><bean:message bundle='mail'  key='label.size' /></imtheme:listColumnHeading>
                    </imtheme:listHeading>
                    <imtheme:listBody var='file' rows='<%= message.getAttachments() %>'>
                      <igw:bean id='file' type='com.ivata.groupware.business.drive.file.FileDO'/>

                      <imtheme:listColumn>
                        <imhtml:checkbox styleId='<%= file.getName() %>' property='<%= "attachmentRemove(" + Integer.toString((file.getName()).hashCode()) + ")" %>' fieldName='attachmentRemove' />
                      </imtheme:listColumn>

                      <imtheme:listColumn>
                        PROPERTY(currentItemIndex).
                      </imtheme:listColumn>

                      <imtheme:listColumn>
                        <table border='0' cellspacing='0' cellpadding='0'>
                          <tr>
                            <td><imhtml:img page='<%= MimeTypesHandling.mimeType2Icon(file.getMimeType()) %>'/></td>
                            <td valign='center'><jsp:getProperty name="file" property="comment" /></td>
                          </tr>
                        </table>
                      </imtheme:listColumn>

                      <imtheme:listColumn>
                        <%=sizeFormatter.format(file.getSize().toString())%>
                      </imtheme:listColumn>

                    </imtheme:listBody>

                    <imtheme:listEmpty>
                      <bean:message bundle='mail' key='compose.label.noAttachments' />
                    </imtheme:listEmpty>

                  </imtheme:listFrame>
                </table>
              </imhtml:iframe>

            </imtheme:framePane>
          </imtheme:frame>

        </c:if>

        <imtheme:buttonFrame>
          <html:hidden property='senders' value='<%=person.getEmailAddress()%>'/>

            <imhtml:label fieldName='format'/>
            <%pageContext.setAttribute("formatOptions", FormatConstants.getLabelValues());%>
            <html:select styleId='format' onchange='onChangeFormat();return false' styleClass='mandatory' property="message.format">
              <html:options collection="formatOptions" property="value"
                       labelProperty="label"/>
            </html:select>

            <%-- TODO: no attachments in this release
              <imhtml:button fieldName='button_upload'
                onclick='<%= uploadPopUp.toString() %>' />
            --%>
            <%--TODO: <html:submit property='saveDraftButton' value='Save Draft' title='Save This Message In Drafts Folder'/>--%>

            <imtheme:buttonSpacer/>
            <imhtml:ok/>
            <imhtml:cancel />
            <imhtml:help key='mail.compose' />

        </imtheme:buttonFrame>
      </imhtml:form>
    </imtheme:window>
    <%@include file='/include/script/fixPopUp.jspf'%>
    <c:if test='<%=!emailEnable.booleanValue()%>'>
      <script language='JavaScript' type='text/JavaScript'>
        alert("This is a demo system, so outgoing email has been disabled.\nAny e-mail you write here will not really be sent out.");
      </script>
    </c:if>
  </body>
</imhtml:html>