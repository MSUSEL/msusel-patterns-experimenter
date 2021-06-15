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
<%@page import='com.ivata.groupware.business.addressbook.person.PersonDO'%>
<%@page import='com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressConstants'%>
<%@page import='com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressDO'%>

<%@page import='com.ivata.mask.util.StringHandling' %>
<%@page import='com.ivata.mask.util.ThrowableHandling' %>
<%@page import="com.ivata.mask.web.RewriteHandling"%>

<%@page import='java.util.*' %>
<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: recipientsEmail.jsp,v 1.5 2005/04/30 13:10:24 colinmacleod Exp $
//
// If you choose a recipient who doesn't have an email address, this will pop up
// to let you enter one. It also stores that address in the address book, if
// you're entitled to change the person involved.
//
// Since: ivata groupware 0.9 (2002-09-14)
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
// $Log: recipientsEmail.jsp,v $
// Revision 1.5  2005/04/30 13:10:24  colinmacleod
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
// Revision 1.5  2005/01/01 12:47:50  colinmacleod
// Renamed from ExceptionHandling to ThrowableHandling.
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
// Revision 1.10  2003/07/05 21:54:49  colin
// changed attendee to person in personTree
//
// Revision 1.9  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.8  2003/02/25 17:33:21  colin
// removed debug alerts
//
// Revision 1.7  2003/02/25 17:02:04  colin
// fixed display, recipients
//
// Revision 1.6  2003/02/25 07:30:42  colin
// restructured java file paths
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
<igw:checkLogin forward='mailRecipients'/>
<%@include file='/include/theme.jspf'%>
<%@include file='/addressBook/include/addressBook.jspf'%>

<igw:bean id='people' scope='page' type='java.util.Vector'/>
<%
  String[] emailAddresses = request.getParameterValues("emailAddress");
  String[] ids = request.getParameterValues("id");
  String textAreaId = request.getParameter("textAreaId");
  int updateIndex = 0;
%>
<igw:bean id='securitySession' scope='session' type='com.ivata.groupware.admin.security.server.SecuritySession'/>
<c:catch var='fieldException'>
  <c:forEach var='id' items='<%=ids%>'>
    <igw:bean id='id' scope='page' type='java.lang.String'/>
    <%--
     only update people if there are _no_ email addresses at all or if this one
     has a non-empty address
    --%>
    <%PersonDO person = addressBook.findPersonByPrimaryKey(securitySession, id);%>
    <c:choose>
      <c:when test='<%=emailAddresses == null%>'>
        <imutil:collectionAdd name='people' value='<%=person%>'/>
      </c:when>
      <c:when test='<%=emailAddresses != null  &&
                      !emailAddresses[updateIndex].trim().equals("")%>'>
        <%
          Set telecomAddresses = person.getTelecomAddresses();
          if (telecomAddresses == null) {
            person.setTelecomAddresses(telecomAddresses = new HashSet());
          }
          TelecomAddressDO telecomAddress = new TelecomAddressDO();
          telecomAddress.setAddress(emailAddresses[updateIndex].trim());
          telecomAddress.setType(TelecomAddressConstants.TYPE_EMAIL);
          telecomAddresses.add(telecomAddress);
          addressBook.amendPerson(securitySession, person);
        %>
      </c:when>
    </c:choose>
    <%++updateIndex;%>
  </c:forEach>
</c:catch>

<imhtml:html locale='true'>
  <igw:head topLevel='true' bundle='mail' titleKey='recipientsEmail.title'>
    <imhtml:base/>
    <script language='JavaScript' type='text/javascript'>
      var textArea = opener.document.getElementById("<%=textAreaId%>");
      <%-- the first time, create a method for updating --%>
      <c:choose>
        <c:when test='<%=request.getParameterValues("emailAddress") == null%>'>
          <%@include file='/include/script/trim.jsp'%>
          <%-- javascript method to add text to the original text area --%>
          function addToTextArea(fileAs, email) {
            if(textArea.value != "") {
              textArea.value += "\n";
            }
            textArea.value += "\"" + fileAs + "\" <" + email + ">";
          }
          <%-- add all non-empty values to the text area and submit this page --%>
          function submitPage() {
            <%int personIndex = 0;%>
            <c:forEach var='personScript' items='<%=people%>'>
              <%--
                if there are more than one person, we need to use javascript
                arrays to get the values for the people
              --%>
              <%String arrayIndex = "";%>
              <c:if test='<%=people.size() > 1%>'>
                <%arrayIndex = "[" + personIndex + "]";%>
              </c:if>
              <igw:bean id='personScript' scope='page' type='com.ivata.groupware.business.addressbook.person.PersonDO'/>
              <%-- ignore empty email addresses --%>
              if(trim(document.emailForm.emailAddress<%=arrayIndex%>.value) != "") {
                addToTextArea(document.emailForm.fileAs<%=arrayIndex%>.value, document.emailForm.emailAddress<%=arrayIndex%>.value);
              }
              <%++personIndex;%>
            </c:forEach>
            document.emailForm.submit();
          }
        </c:when>
        <c:otherwise>
          var ids = new Array(<%=ids.length%>);

          var emails = new Array(<%=ids.length%>);
          <%int addIndex = 0;%>
          <c:forEach var='idAdd' items='<%=ids%>'>
            <igw:bean id='idAdd' scope='page' type='java.lang.String'/>
            ids[<%=addIndex%>] = <%=idAdd%>;
            emails[<%=addIndex%>] = "<%=emailAddresses[addIndex]%>";
            <%++addIndex;%>
          </c:forEach>

          <%-- work out whether or not an iframe was used on the opener --%>
          var treeForm;
          if(opener.frames['personTree'] != null) {
            treeForm = opener.frames['personTree'].document.personTreeForm;
          } else {
            treeForm = opener.document.recipientForm;
          }

          <%-- now go thro' all the ids, locate each one and change the email --%>
          for(idIndex = 0; idIndex < ids.length; ++idIndex) {
            for(treeIndex = 0; treeIndex < treeForm.person.length; ++treeIndex) {
              if(ids[idIndex] == treeForm.person[treeIndex].value) {
                <%--alert("Setting mail for " + ids[idIndex] + ", " + treeForm.fileAs[treeIndex].value + " to "  + emails[idIndex]);--%>
                treeForm.email[treeIndex].value = "\"" + treeForm.fileAs[treeIndex].value + "\" <" + emails[idIndex] + ">";
              }
            }
          }

          window.close();
        </c:otherwise>
      </c:choose>
    </script>
  </igw:head>

  <body class='dialog'>
    <%-- the body is only displayed the first time --%>
    <c:if test='<%=request.getParameterValues("emailAddress") == null%>'>
      <imtheme:window>
        <form name='emailForm' method='post' action='<%=response.encodeURL(RewriteHandling.getContextPath(request) + "/mail/recipientsEmail.jsp")%>'>
          <c:if test='<%=pageContext.getAttribute("fieldException", pageContext.PAGE_SCOPE) != null%>'>
            <igw:bean id='fieldException' scope='page' type='java.lang.Exception'/>
            <imtheme:frame>
              <imtheme:framePane styleClass='error'>
                Please check:<br/>
                <%=ThrowableHandling.getCause(fieldException)%>
              </imtheme:framePane>
            </imtheme:frame>
          </c:if>
          <imtheme:frame>
            <imtheme:framePane styleClass='hilight'>
              <c:choose>
                <c:when test='<%=people.size() == 1%>'>
                  <bean:message bundle='mail' key='recipientsEmail.label.one'/>
                </c:when>
                <c:otherwise>
                  <bean:message bundle='mail' key='recipientsEmail.label.many'/>
                </c:otherwise>
              </c:choose>
              <table cellpadding='4'>
                <c:forEach var='thisPerson' items='<%=people%>'>
                  <igw:bean id='thisPerson' scope='page' type='com.ivata.groupware.business.addressbook.person.PersonDO'/>
                  <tr>
                    <td class='fieldCaption'><%=thisPerson.getFileAs()%>:</td>
                    <td class='field'>
                      <input type='text' name='emailAddress'/>
                      <input type='hidden' name='id' value='<%=thisPerson.getId()%>'/>
                      <input type='hidden' name='fileAs' value='<%=thisPerson.getFileAs()%>'/>
                    </td>
                  </tr>
                </c:forEach>
              </table>
            </imtheme:framePane>
          </imtheme:frame>
          <imtheme:buttonFrame>
            <igw:bean id='okTitle'><bean:message bundle='mail' key='recipientsEmail.submit.ok.title'/></igw:bean>
            <igw:bean id='okValue'><bean:message key='submit.ok.value'/></igw:bean>
            <igw:bean id='cancelTitle'><bean:message bundle='mail' key='recipientsEmail.submit.cancel.title'/></igw:bean>
            <igw:bean id='cancelValue'><bean:message key='submit.cancel.value'/></igw:bean>
            <input type='button' onClick='submitPage()' value='<%=okValue%>' title='<%=okTitle%>'/>
            <input type='button' onClick='window.close()' value='<%=cancelValue%>' title='<%=cancelTitle%>'/>
            <imhtml:help key='mail.recipients.email'/>
          </imtheme:buttonFrame>
        </form>
      </imtheme:window>
    </c:if>
  </body>
</imhtml:html>
