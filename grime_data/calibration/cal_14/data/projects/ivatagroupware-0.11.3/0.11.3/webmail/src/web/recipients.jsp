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
<%@page contentType="text/html;charset=UTF-8"%>
<%@page import='com.ivata.groupware.business.addressbook.person.group.GroupDO' %>
<%@page import='com.ivata.mask.util.StringHandling' %>
<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: recipients.jsp,v 1.5 2005/04/30 13:10:08 colinmacleod Exp $
//
// Choose people from the addressbook as recipents of an email.
//
// Since: ivata groupware 0.9 (2002-09-12)
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
// $Log: recipients.jsp,v $
// Revision 1.5  2005/04/30 13:10:08  colinmacleod
// Fixes reverting id type from String to Integer.
//
// Revision 1.4  2005/04/28 18:46:15  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.3  2005/04/10 19:46:08  colinmacleod
// Removed mail.jspf include.
// Removed nobr tags.
//
// Revision 1.2  2005/04/09 17:20:02  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:51:25  colinmacleod
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
// Revision 1.13  2003/07/05 21:54:49  colin
// changed attendee to person in personTree
//
// Revision 1.12  2003/03/14 10:50:57  peter
// fixed js for recipient selector
//
// Revision 1.11  2003/03/06 17:31:55  peter
// fixed js frame references and location updates
//
// Revision 1.10  2003/02/25 17:33:21  colin
// removed debug alerts
//
// Revision 1.9  2003/02/25 17:02:04  colin
// fixed display, recipients
//
// Revision 1.8  2003/02/25 07:30:42  colin
// restructured java file paths
//
// Revision 1.7  2003/02/19 07:50:19  colin
// bugfixes for new imhtml tags
//
// Revision 1.6  2003/01/28 16:01:10  peter
// fixed the form field names in javascript
//
// Revision 1.5  2003/01/27 07:23:39  colin
// modified style to new pop-up style
//
// Revision 1.4  2003/01/18 20:38:58  colin
// added help buttons
//
// Revision 1.3  2002/10/08 16:23:33  colin
// fixed top level handling
//
// Revision 1.2  2002/09/25 15:29:00  colin
// added title/tooltip attributes to buttons/submits
//
// Revision 1.1  2002/09/25 11:45:53  colin
// first version of recipients chooser
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<igw:checkLogin forward='mailIndex'/>
<%@include file='/include/theme.jspf'%>

<igw:bean id='emailPopUp' scope='page' type='com.ivata.mask.web.javascript.JavaScriptWindow'/>
<jsp:setProperty name='emailPopUp' property='windowName' value='emailChooser'/>
<jsp:setProperty name='emailPopUp' property='frameName' value='_blank'/>
<jsp:setProperty name='emailPopUp' property='height' value='420'/>
<jsp:setProperty name='emailPopUp' property='width' value='420'/>
<jsp:setProperty name='emailPopUp' property='page' value='/mail/recipientsEmail.jsp?" + URLParameters + "'/>
<jsp:setProperty name='emailPopUp' property='pageContext' value='<%=pageContext%>'/>

<%@include file='/addressBook/include/personPopUp.jspf'%>

<%-- these beans are used for storing the field values when a new contact is entered --%>
<igw:bean id='recipientTo' scope='session' type='java.lang.String'/>
<igw:bean id='recipientCc' scope='session' type='java.lang.String'/>
<igw:bean id='recipientBcc' scope='session' type='java.lang.String'/>

<%-- actions go here --%>
<%Boolean newContact = StringHandling.booleanValue(request.getParameter("newContact"));%>
<c:if test='<%=(newContact != null) && newContact.booleanValue()%>'>
  <%-- store the field values for our return --%>
  <%
    session.setAttribute("recipientTo", request.getParameter("to"));
    session.setAttribute("recipientCc", request.getParameter("cc"));
    session.setAttribute("recipientBcc", request.getParameter("bcc"));
   %>
   <jsp:forward page='/addressBook/person.jsp'>
     <jsp:param name='retrieveFromSession' value='true'/>
     <jsp:param name='returnTo' value='/mail/recipients.jsp'/>
   </jsp:forward>
</c:if>

<igw:bean id='browser' scope='session' type='com.ivata.mask.web.browser.Browser'/>
<%
  String formName = browser.canDisplayIFrames()
            ?  "window.personTree.document.personTreeForm"
            : "document.mailRecipientForm";
%>
<imhtml:html locale='true'>
  <igw:head  title='choose mail recipients' topLevel='true'>
    <imhtml:base/>
    <script language='JavaScript' type='text/javascript'>
      <!--
      <%--// bring up a pop-up to enter a new contact --%>
      function enterNewContact() {
        <%=personPopUp%>
      }
      <%--
      //  add recipients to the textarea provided - uses java to add in the frame
      //  name, if this browser uses iframes (see below)
      --%>
      function addRecipients(textAreaId) {
        var treeForm = <%=formName%>;
        var textArea = document.getElementById(textAreaId);
        <%--// find out which recipients have email addresses --%>
        var URLParameters = "";
        for (var index = 0; index < treeForm.person.length; ++index) {
          <%--// we are only interested in the checked ones --%>
          if(!treeForm.person[index].checked) {
            continue;
          }
          <%--// does this person have an email address? --%>
          if(treeForm.email[index].value == "[none]") {
            <%--// if not, store the index for later use--%>
            if(URLParameters != "") {
              URLParameters += "&";
            }
            URLParameters += "id=" + treeForm.person[index].value;
          } else {
            <%--// otherwise, we'll add the email address to the text area --%>
            if(textArea.value != "") {
              textArea.value = textArea.value + "\n";
            }
            textArea.value = textArea.value + treeForm.email[index].value;
          }
          treeForm.person[index].checked = false;
        }
        <%--// if some people did not have email addresses, do the popup thing
        --%>
        if (URLParameters != "") {
          URLParameters += "&textAreaId=" + textAreaId;
          <%=emailPopUp%>
        }
      }
      <%--
        update the window which opened this one and close the window
      --%>
      function ok() {
          copyTextAreaToField(document.mailRecipientForm.to, opener.document.mailComposeForm.recipients);
          copyTextAreaToField(document.mailRecipientForm.cc, opener.document.mailComposeForm.recipientsCC);
          copyTextAreaToField(document.mailRecipientForm.bcc, opener.document.mailComposeForm.recipientsBCC);
          window.close();
      }
      <%@include file='/include/script/trim.jsp'%>
      <%--
        convert all of the carriage returns in the string provided into
        semicolons
      --%>
      function copyTextAreaToField(textArea, field) {
          field.value = trim(field.value);
          <%--// if the field already has contents, add a semicolon, space --%>
          if ((field.value != "")
                && (trim(textArea.value) != "")) {
            field.value += "; ";
          }
          for(var i=0; i < textArea.value.length; ++i) {
              var ch = textArea.value.charAt(i);
              if(ch == '\r') {
                <%-- do nothing --%>
              } else if(ch == '\n') {
                field.value += "; "
              } else {
                field.value += ch;
              }
          }
      }
      // -->
    </script>
  </igw:head>

  <%-- only show groups we can see --%>
  <%@include file='/addressBook/include/tree.jspf'%>
  <jsp:setProperty name='filter' property='access' value='<%=com.ivata.groupware.business.addressbook.person.group.right.RightConstants.ACCESS_VIEW%>'/>
  <jsp:setProperty name='personTreeModel' property='filter' value='<%=filter%>'/>
  <jsp:setProperty name='personTreeModel' property='includePeople' value='true'/>

  <body class='dialog'>
    <imtheme:window>
      <imhtml:form action='/mail/recipients'>
        <%--
          this is used as a flag to indicate we should go to the new contact page
          it is filled out by recipientsNew.jsp
        --%>
        <input type='hidden' name='newContact'/>
        <imtheme:frame>
          <imtheme:framePane styleClass='hilight'>
            <table cellpadding='4' cellspacing='0' border='0' height='100%' width='100%'>
              <tr>
                <td style='width: 300px;'>
                  <imutil:map id='frameParams'>
                    <imutil:mapEntry name='page' value='/addressBook/personTree.jsp'/>
                  </imutil:map>
                  <imhtml:iframe
                      name='frameParams'
                      frameName='personTree'
                      page='/util/loading.jsp'
                      styleClass='watermark'
                      styleId='stretchIFrame'/>
                </td>
                <td valign='top' style='width: 250px;'>
                  <table cellpadding='4' cellspacing='0' border='0' height='100%'>
                    <%--
                      TODO: don't specify cols in these textareas - it overrides
                      the wrap attribute!!
                    --%>
                    <tr>
                      <td valign='top' align='right'><imhtml:button
                                                titleKey='recipients.submit.recipients.title'
                                                 onclick='addRecipients("to")'
                                                  bundle='mail'
                                                valueKey='compose.submit.button_recipients.value'/></td>
                      <td><textArea id='to' wrap="off" class='mandatory' name='to' rows='7'><%=recipientTo%></textArea></td>
                    </tr>
                    <tr>
                      <td valign='top' align='right'><imhtml:button
                                                titleKey='recipients.submit.recipientsCC.title'
                                                 onclick='addRecipients("cc")'
                                                  bundle='mail'
                                                valueKey='compose.submit.button_recipientsCC.value'/></td>
                      <td><textArea id='cc' wrap='off' name='cc' rows='7'><%=recipientCc%></textArea></td>
                    </tr>
                    <tr>
                      <td valign='top' align='right'><imhtml:button
                                                titleKey='recipients.submit.recipientsBCC.title'
                                                 onclick='addRecipients("bcc")'
                                                  bundle='mail'
                                                valueKey='compose.submit.button_recipientsBCC.value'/></td>
                      <td><textArea id='bcc' wrap='off' name='bcc' rows='7'><%=recipientBcc%></textArea></td>
                    </tr>
                  </table>
                </td>
              </tr>
            </table>
          </imtheme:framePane>
        </imtheme:frame>
        <imtheme:buttonFrame styleId='buttonFrame'>
          <%--
          <imhtml:button onclick='enterNewContact()'
                          bundle='mail'
                        titleKey='recipients.submit.new.title'
                        valueKey='recipients.submit.new.value'/>
          <imtheme:buttonSpacer/>
          --%>
          <imhtml:button onclick='ok()'
                        valueKey='submit.ok.value'
                          bundle='mail'
                        titleKey='recipients.submit.ok.title'/>
          <imhtml:button onclick='window.close()'
                        valueKey='submit.cancel.value'
                          bundle='mail'
                        titleKey='recipients.submit.cancel.title'/>
          <imhtml:help key='mail.recipients'/>
        </imtheme:buttonFrame>
      </imhtml:form>
    </imtheme:window>
    <%@include file='/include/script/fixPopUp.jspf'%>
  </body>
</imhtml:html>
