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
<%@page import='com.ivata.groupware.business.addressbook.person.group.right.RightConstants' %>
<%@page import='com.ivata.mask.web.format.HTMLFormatter' %>
<%@page import='com.ivata.groupware.business.library.item.LibraryItemDO' %>
<%@page import='com.ivata.groupware.business.library.item.LibraryItemConstants' %>
<%@page import='com.ivata.groupware.business.library.topic.TopicDO' %>
<%@page import='com.ivata.mask.util.StringHandling' %>
<%@page import="com.ivata.mask.web.RewriteHandling"%>
<%@page import='com.ivata.groupware.business.calendar.*'%>

<%@page import='java.util.Collection' %>
<%@page import='java.util.List' %>
<%@page import='java.util.Map' %>
<%@page import='java.util.Set' %>
<%@page import='java.util.Vector' %>
<%@page import='java.util.Arrays' %>

<%@page import='org.apache.struts.Globals' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: submit.jsp,v 1.5 2005/04/30 13:04:14 colinmacleod Exp $
//
// Submit a new document library item into the intranet, or change an existing
// one.
//
// Since: ivata groupware 0.9 (2002-06-05)
// Author: Laco Kacani, colin
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
// $Log: submit.jsp,v $
// Revision 1.5  2005/04/30 13:04:14  colinmacleod
// Fixes reverting id type from String to Integer.
//
// Revision 1.4  2005/04/28 18:47:07  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.3  2005/04/10 20:10:11  colinmacleod
// Added new themes.
// Changed id type to String.
// Changed i tag to em and b tag to strong.
// Improved PicoContainerFactory with NanoContainer scripts.
//
// Revision 1.2  2005/04/09 17:19:48  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:52:10  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.9  2004/12/31 18:50:32  colinmacleod
// Removed unused ExceptionHandling import.
//
// Revision 1.8  2004/12/27 14:52:01  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.7  2004/11/03 15:55:47  colinmacleod
// Changed todo comments to TODO: all caps.
//
// Revision 1.6  2004/07/29 20:52:35  colinmacleod
// Handled case when item topic is null.
//
// Revision 1.5  2004/07/14 22:50:25  colinmacleod
// Replaced bean:define with extended igw:bean.
//
// Revision 1.4  2004/07/14 20:59:54  colinmacleod
// Changed all occurrences of jsp:useBean to igw:bean (better tomcat compatibility).
//
// Revision 1.3  2004/07/13 19:47:30  colinmacleod
// Moved project to POJOs from EJBs.
// Applied PicoContainer to services layer (replacing session EJBs).
// Applied Hibernate to persistence layer (replacing entity EJBs).
//
// Revision 1.2  2004/03/21 21:16:30  colinmacleod
// Shortened name to ivata op.
//
// Revision 1.1  2004/03/03 21:50:02  colinmacleod
// Updated webapp structure. Moved core files to core subproject.
//
// Revision 1.1.1.1  2004/01/27 20:58:46  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.4  2004/01/12 14:01:13  jano
// fixing bugs
//
// Revision 1.3  2003/10/28 13:16:15  jano
// commiting library,
// still fixing compile and building openPortal project
//
// Revision 1.2  2003/10/15 14:16:54  colin
// fixing for XDoclet
//
// Revision 1.44  2003/07/10 10:04:23  peter
// title parameter for revisionControl added
//
// Revision 1.43  2003/07/09 16:14:57  jano
// we have history button,
// doing reverting feature
//
// Revision 1.42  2003/06/20 13:21:20  jano
// we want deleteFile button in list of attached files
//
// Revision 1.41  2003/06/11 06:40:36  peter
// displayItem include moved inside form
//
// Revision 1.40  2003/06/10 06:01:52  peter
// img and html:img replaced with size-aware imhtml:img
//
// Revision 1.39  2003/06/03 05:07:15  peter
// changes due to posibility to change filelist from display mode
//
// Revision 1.38  2003/05/21 09:32:56  peter
// uploadPopUp added
//
// Revision 1.37  2003/05/20 08:13:18  jano
// maintaing attaching files to libray item
//
// Revision 1.36  2003/05/14 06:52:46  jano
// comenting inclue submitFile.jsp just for now
//
// Revision 1.35  2003/05/12 10:29:45  jano
// addiding files list
//
// Revision 1.34  2003/04/27 19:16:14  peter
// added defaultButton to form
//
// Revision 1.33  2003/04/14 07:25:55  peter
// helpKey is taken from form
//
// Revision 1.32  2003/03/20 15:22:11  jano
// fixing textarea size for IE
//
// Revision 1.31  2003/03/03 19:06:45  colin
// i18n corrections
//
// Revision 1.30  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.29  2003/02/28 11:06:21  colin
// fixed spacing on summary table cells
//
// Revision 1.28  2003/02/28 07:31:52  colin
// implemented editing/displaying of faqs & notes
//
// Revision 1.27  2003/02/25 07:28:15  colin
// converted display & submission to struts
// started converting comments to popup
// restructured java paths
//
// Revision 1.26  2003/01/24 19:35:18  peter
// changes to sslext
//
// Revision 1.25  2003/01/24 07:29:07  colin
// added checkLogin tag
//
// Revision 1.24  2003/01/18 20:38:58  colin
// added help buttons
//
// Revision 1.23  2003/01/14 10:42:19  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.22  2003/01/08 13:22:03  jano
// fixing bug with getCreated and getCreatedBy
// show only topic where user can Add
//
// Revision 1.21  2002/12/04 09:12:06  jano
// when you want amend item, we will display tpocis which are for amend items
//
// Revision 1.20  2002/11/28 14:03:37  jano
// I chaneged findTopics because we need detail of right too
//
// Revision 1.19  2002/11/27 18:00:35  peter
// i18n finished
//
// Revision 1.18  2002/10/14 15:57:39  colin
// bugfixes in meeting handling
//
// Revision 1.17  2002/10/08 16:22:36  colin
// improved title attribute
//
// Revision 1.16  2002/09/25 11:48:48  jano
// forget bug fixed
//
// Revision 1.15  2002/09/25 11:31:31  colin
// added the context path to the email address formatter
//
// Revision 1.14  2002/09/25 09:39:00  jano
// click on Submit item in menu starting with NEW document
// fixing bug with add new blank page
//
// Revision 1.13  2002/09/24 13:04:56  peter
// formatting type input commented out
//
// Revision 1.12  2002/09/24 11:28:47  peter
// removed the insert buttons and functionality in write minutes, adding new instead
//
// Revision 1.11  2002/09/23 12:39:50  jano
// now we don't need reresh right frame
//
// Revision 1.10  2002/09/13 11:05:05  jano
// fixing submiting document and meeting
//
// Revision 1.9  2002/09/11 11:00:47  jano
// corecting reading meetingDO when you want write new minutes
//
// Revision 1.8  2002/09/10 09:41:47  jano
// fixing bugs
//
// Revision 1.7  2002/09/06 14:18:24  jano
// adding meeeting stuff
//
// Revision 1.6  2002/09/05 10:24:17  colin
// lots of bugfixes to improve inserting of pages
//
// Revision 1.5  2002/09/04 16:12:45  colin
// added article previews
// bugfixes
//
// Revision 1.4  2002/08/27 08:44:20  colin
// split tags and themes into two separate includes
//
// Revision 1.3  2002/08/15 09:15:48  peter
// bugfixes (wouldn't compile)
//
// Revision 1.2  2002/08/11 12:09:26  colin
// Added mail bean parameter to server-side calls.
//
// Revision 1.1  2002/06/28 13:16:59  colin
// first library release
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<%@include file='/include/theme.jspf'%>

<igw:checkLogin forward='libraryIndex'/>

<logic:notPresent name='libraryItemForm' property='item'>
  <logic:forward name='librarySubmitAction'/>
</logic:notPresent>

<igw:bean id='libraryItemForm' scope='session' type='com.ivata.groupware.business.library.struts.ItemForm'/>
<igw:bean id='item' name='libraryItemForm' property='item' type='com.ivata.groupware.business.library.item.LibraryItemDO'/>

<igw:bean id='emailAddressFormatter' scope='session' type='com.ivata.groupware.web.format.EmailAddressFormatter'/>
<jsp:setProperty name='emailAddressFormatter' property='pageContext' value='<%=pageContext%>'/>

<%-- Revision Control popUp --%>
<igw:bean id='historyPopUp' scope='page' type='com.ivata.mask.web.javascript.JavaScriptWindow'/>
<jsp:setProperty name='historyPopUp' property='frameName' value='ivataRevisionControlPopUp'/>
<jsp:setProperty name='historyPopUp' property='pageContext' value='<%=pageContext%>'/>
<jsp:setProperty name='historyPopUp' property='width' value='550'/>
<jsp:setProperty name='historyPopUp' property='height' value='400'/>

<igw:getSetting id='pathTopics' setting='pathTopics' type='java.lang.String'/>

<imhtml:html locale='true'>
  <igw:head bundle='library' titleKey='submit.title'>
    <imhtml:base/>
    <igw:bean id='commentPrompt'><bean:message bundle='library' key='submit.alert.ok.label' /></igw:bean>
    <igw:bean id='commentTitle'><bean:message bundle='library' key='submit.alert.ok.title' /></igw:bean>
    <script language="JavaScript" type="text/javascript">
      <!--

        function versionControlComment() {
            var returnValue = false;
            var comment = prompt("<%=commentPrompt%>", "<%=commentTitle%>' />");
            if (comment==null) {
              returnValue = false;
            } else {
              document.libraryItemForm.comment.value = comment;
              returnValue = true;
            }
            return returnValue;
        }


        function updateTopicImage() {
            var topicIdField = document.getElementById("topicId");
            var currentId = topicIdField.value;
          <%-- go through all the topic ids and match each with its topic image--%>
          <c:forEach var='topicIdJs' items='<%=libraryItemForm.getTopicIds()%>'>
            <igw:bean id='topicIdJs' type='java.lang.Integer'/>
            if(currentId == '<%=topicIdJs.toString()%>') {
              var topicImage = document.getElementById("topicImage");
              topicImage.value = "<%=libraryItemForm.getTopicImages().get(topicIdJs)%>"
              var topicImagePicture = document.getElementById("topicImagePicture");
              topicImagePicture.src = "<%=RewriteHandling.getContextPath(request)
                                          + pathTopics
                                          + libraryItemForm.getTopicImages().get(topicIdJs)%>";
            } else
          </c:forEach>
          {
            topicImage.src = "<%=RewriteHandling.getContextPath(request)%>/images/empty.gif";
          }
        }
      // -->
    </script>
  </igw:head>
  <body>
    <div align='center'>
      <igw:bean id='errorMessage' scope='page' type='java.lang.String'/>
      <%-- was there an exception submitting the form? --%>
      <%@include file='/include/errorWindow.jspf'%>

      <imhtml:form action='/library/submit' referralName='librarySubmitForm' bundle='library'
            resourceFieldPath='submit' defaultButton='<%= (libraryItemForm.getPreview()!=null)?"ok":"preview" %>'>

      <%-- show the preview if the buttton has already been pressed --%>
      <c:if test='<%=(libraryItemForm.getPreview() != null)%>'>
        <jsp:include page='/library/displayItem.jsp'/>
        <br/>
      </c:if>

        <%-- Summary WINDOW ............................................... --%>
        <imtheme:window
            styleClass='mainWindow'
            bundle='library'
            titleKey='<%=libraryItemForm.getSummaryTitleKey()%>'>
          <imtheme:frame>
            <imtheme:framePane styleClass='hilight'>
              <table cellpadding='1' width='100%' cellspacing='0' border='0'>
                <tr>
                  <td class='fieldCaption'><imhtml:label fieldName='title'/></td>
                  <td class='field'><imhtml:text fieldName='title' mandatory='true' property='item.title'/></td>
                  <td class='field' rowspan='4' width='1'>
                    <%-- show an empty image if the topic has not yet been chosen --%>
                    <c:choose>
                      <c:when test='<%=item.getTopic()==null%>'>
                        <html:img page='/images/empty.gif' styleId='topicImagePicture'/>
                        <%-- prevent errors later --%>
                        <%item.setTopic(new TopicDO());%>
                      </c:when>
                      <c:otherwise>
                        <imhtml:img page='<%=pathTopics
                              + item.getTopic().getImage()%>' styleId='topicImagePicture'/>
                      </c:otherwise>
                    </c:choose>
                  </td>
                </tr>
                <tr>
                  <td class='fieldCaption'><imhtml:label fieldName='topicId'/></td>
                  <td>
                    <html:hidden property='comment'/>

                    <html:hidden property='item.topic.image' styleId='topicImage'/>
                    <imhtml:select fieldName='topicId' property='item.topic.id' mandatory='true' onchange='updateTopicImage()'>
                      <%-- add in a 'choose one...' option, if there is no topic --%>
                      <c:if test='<%=(item.getTopic().getId()==null) || (item.getTopic().getId()!=null && item.getTopic().getId() == new Integer(-1))%>'>
                        <imhtml:option value='-1'><bean:message bundle='library' key='submit.topicId.noTopic' /></imhtml:option>
                      </c:if>
                      <c:forEach var='topicId' items='<%=libraryItemForm.getTopicIds()%>'>
                        <igw:bean id='topicId' type='java.lang.Integer'/>
                        <imhtml:option value='<%=topicId.toString()%>'><%=libraryItemForm.getTopicCaptions().get(topicId)%></imhtml:option>
                      </c:forEach>
                    </imhtml:select>
                  </td>
                <tr>
                  <td class='fieldCaption' colspan='2'>
                  <imhtml:label fieldName='summary' keySuffix='<%=libraryItemForm.getSummaryPromptKey()%>'/>
                  </td>
                </tr>
                <tr>
                  <td colspan='2' height='100%'><imhtml:textarea fieldName='summary' mandatory='true' property='item.summary' cols='50'/></td>
                </tr>
              </table>
            </imtheme:framePane>
          </imtheme:frame>
        </imtheme:window>
        <br/>
        <logic:present name='libraryItemForm' property='submitIncludePage'>
            <jsp:include page='<%=libraryItemForm.getSubmitIncludePage()%>' />
        </logic:present>

        <%-- FILE Control Window .......................................... --%>
        <%-- TODO
        <jsp:include page='submitFile.jsp' />
        --%>
        <%-- Controls WINDOW .............................................. --%>
        <imtheme:window>
          <imtheme:buttonFrame>

            <%--
            <select name='format'>
              <option value='<%=FormatConstants.FORMAT_TEXT%>'<%=
                    HTMLFormatter.getBooleanAttribute("selected",
                    item.getFormat().equals(FormatConstants.FORMAT_TEXT))
                    %>>Plain text</option>
              <option value='<%=FormatConstants.FORMAT_HTML%>' <%=
                    HTMLFormatter.getBooleanAttribute("selected",
                    item.getFormat().equals(FormatConstants.FORMAT_HTML))
                    %>>HTML</option>
            </select>
            --%>
            <imhtml:submit fieldName='preview'/>
            <imhtml:clear asNewButton='<%=item.getId() != null%>'/>

            <c:if test='<%=item.getId() != null%>'>
              <imtheme:buttonSpacer/>

              <imutil:map id='fileParams'>
                <imutil:mapEntry name='fileName' value='document.xml'/>
                <imutil:mapEntry name='filePath' value='<%="library"+java.io.File.separator+item.getId()+java.io.File.separator+"versions"+java.io.File.separator%>'/>
                <imutil:mapEntry name='windowTitle' value='<%= item.getTitle() %>'/>
                <imutil:mapEntry name='display' value='false'/>
                <imutil:mapEntry name='libraryItemId' value='<%=item.getId().toString()%>'/>
              </imutil:map>
              <jsp:setProperty name='historyPopUp' property='page' value='/drive/fileRevisions.action'/>
              <jsp:setProperty name='historyPopUp' property='paramsName' value='fileParams' />
              <%-- TODO
              <imhtml:button fieldName='history'
                              bundle='library'
                              titleKey='displayItem.submit.revisionButton.title'
                              onclick='<%= historyPopUp.toString() %>' />
              <imtheme:buttonSpacer/>
              --%>
            </c:if>

            <c:if test='<%=libraryItemForm.getPreview() != null%>'>
              <%-- TODO
              <imhtml:ok onclick='return (versionControlComment())'/>
              --%>
              <imhtml:ok/>
            </c:if>
            <imhtml:cancel/>
            <imhtml:help />
          </imtheme:buttonFrame>
        </imtheme:window>

      </imhtml:form>
    </div>
  </body>
</imhtml:html>
