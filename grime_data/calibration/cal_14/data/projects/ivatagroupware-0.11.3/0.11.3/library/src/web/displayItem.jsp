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
<%@page import='com.ivata.groupware.business.addressbook.person.group.right.RightConstants' %>
<%@page import='com.ivata.groupware.business.library.item.LibraryItemConstants' %>
<%@page import='com.ivata.mask.util.StringHandling' %>
<%@page import="com.ivata.mask.web.RewriteHandling"%>
<%@page import='java.util.Collection' %>

<%@page import='org.apache.struts.Globals' %>
<%@page import="org.apache.struts.taglib.TagUtils" %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: displayItem.jsp,v 1.5 2005/04/28 18:47:07 colinmacleod Exp $
//
// Display a single item from the library. This file actually handles the
// display and is reused for article previews.
//
// Since: ivata groupware 0.9 (2002-08-28)
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
// $Log: displayItem.jsp,v $
// Revision 1.5  2005/04/28 18:47:07  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.4  2005/04/22 10:00:34  colinmacleod
// Changed abbreviation from faq to fAQ.
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
// Revision 1.1.1.1  2005/03/10 17:52:12  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.8  2004/12/31 18:49:17  colinmacleod
// Fixed typos in TagUtils conversion from RequestUtils.
//
// Revision 1.7  2004/12/27 14:52:01  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.6  2004/09/30 14:59:06  colinmacleod
// Added methods to sanitize the entire library and update the search index.
//
// Revision 1.5  2004/07/14 22:50:25  colinmacleod
// Replaced bean:define with extended igw:bean.
//
// Revision 1.4  2004/07/14 20:59:53  colinmacleod
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
// Revision 1.1.1.1  2004/01/27 20:58:45  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.3  2003/10/28 13:16:15  jano
// commiting library,
// still fixing compile and building openPortal project
//
// Revision 1.2  2003/10/15 14:16:54  colin
// fixing for XDoclet
//
// Revision 1.29  2003/07/10 10:04:23  peter
// title parameter for revisionControl added
//
// Revision 1.28  2003/07/09 16:14:57  jano
// we have history button,
// doing reverting feature
//
// Revision 1.27  2003/07/08 15:11:36  jano
// we need popUp fo history
//
// Revision 1.26  2003/07/08 14:50:25  jano
// we have library items in CVS
// new "history" button in displayItem
//
// Revision 1.25  2003/07/08 07:19:48  peter
// html upload and parser changes
//
// Revision 1.24  2003/06/11 06:41:10  peter
// added logic for referral form name, as this page is used both in display and submit (preview)
//
// Revision 1.23  2003/06/03 05:07:15  peter
// changes due to posibility to change filelist from display mode
//
// Revision 1.22  2003/03/12 14:07:57  jano
// taking away nobraeking spaces from title of item
//
// Revision 1.21  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.20  2003/02/28 07:31:52  colin
// implemented editing/displaying of faqs & notes
//
// Revision 1.19  2003/02/25 07:28:15  colin
// converted display & submission to struts
// started converting comments to popup
// restructured java paths
//
// Revision 1.18  2003/01/24 19:35:18  peter
// changes to sslext
//
// Revision 1.17  2003/01/18 20:38:58  colin
// added help buttons
//
// Revision 1.16  2003/01/14 10:42:19  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.15  2003/01/08 13:21:29  jano
// fixing bug with getCreated and getCreatedBy
//
// Revision 1.14  2002/12/20 10:11:32  jano
// fixing slovak "at" and "on" with date time
//
// Revision 1.13  2002/11/28 15:16:39  peter
// fixed 2 message keys
//
// Revision 1.12  2002/11/27 18:00:35  peter
// i18n finished
//
// Revision 1.11  2002/11/15 17:08:30  peter
// no edit and delete button for FAQ
//
// Revision 1.10  2002/11/15 15:07:20  jano
// when displaying start always with first page
//
// Revision 1.9  2002/10/14 15:56:47  colin
// added handling for meeting agenda page
//
// Revision 1.8  2002/10/08 16:22:03  colin
// added meeting theme
//
// Revision 1.7  2002/09/25 16:25:35  colin
// added more title/tootips to submit buttons
//
// Revision 1.6  2002/09/25 11:30:14  colin
// implemented themes
//
// Revision 1.5  2002/09/23 11:56:27  colin
// date formatter constants now in a separate class
//
// Revision 1.4  2002/09/13 08:28:07  jano
// corecting set up indexCat
//
// Revision 1.3  2002/09/12 16:01:05  jano
// don't check topic -1 for userRight
//
// Revision 1.2  2002/09/06 14:18:24  jano
// adding meeeting stuff
//
// Revision 1.1  2002/09/04 16:12:05  colin
// split off from display.jsp to let the functionality be shared with submission previews
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<%@include file='/library/theme/documentSummary.jspf'%>
<%@include file='/library/theme/fAQ.jspf'%>
<%@include file='/library/theme/meetingSummary.jspf'%>
<%@include file='/library/theme/note.jspf'%>
<%@include file='/include/theme.jspf'%>

<igw:bean id='libraryItemForm' scope='session' type='com.ivata.groupware.business.library.struts.ItemForm'/>
<igw:bean id='item' name='libraryItemForm' property='item' type='com.ivata.groupware.business.library.item.LibraryItemDO'/>

<igw:bean id='securitySession' scope='session' type='com.ivata.groupware.admin.security.server.SecuritySession'/>
<igw:bean id='titleFormatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter'/>
<%--igw:bean id='nonBreaking' scope='page' type='com.ivata.mask.web.format.NonBreakingSpaceFormat' />
<imformat:addFormat formatter='<%=titleFormatter%>' format='<%=nonBreaking%>'/--%>

<% String formatDateTime = "{0}, ''" + TagUtils.getInstance().message(pageContext, null, Globals.LOCALE_KEY, "time.at") + "'' {1}"; %>

<igw:bean id='dateFormatter' type='com.ivata.mask.web.format.DateFormatter'/>
<jsp:setProperty name='dateFormatter' property='dateFormat' value='<%=com.ivata.mask.web.format.DateFormatterConstants.DATE_RELATIVE%>'/>
<jsp:setProperty name='dateFormatter' property='dateTimeText' value='<%=formatDateTime%>'/>

<%-- Revision Control popUp --%>
<igw:bean id='historyPopUp' scope='page' type='com.ivata.mask.web.javascript.JavaScriptWindow'/>
<jsp:setProperty name='historyPopUp' property='frameName' value='ivataRevisionControlPopUp'/>
<jsp:setProperty name='historyPopUp' property='pageContext' value='<%=pageContext%>'/>
<jsp:setProperty name='historyPopUp' property='width' value='550'/>
<jsp:setProperty name='historyPopUp' property='height' value='400'/>

<%-- this formatted is for the embedded images, to display them from downloader --%>
<igw:bean id='embeddedImagesFormatter' scope='request' type='com.ivata.mask.web.format.HTMLFormatter'/>

<igw:bean id='embeddedImages' type='com.ivata.groupware.web.format.SanitizerFormat' />
<jsp:setProperty name='embeddedImages' property='onlyBodyContents' value='true' />
<c:choose>
  <c:when test='<%= item.getId()==null %>'>
    <jsp:setProperty name='embeddedImages' property='imageUri' value='<%= RewriteHandling.getContextPath(request) + "/download/drive/UPLOAD/drive/files/" %>' />
  </c:when>
  <c:otherwise>
    <jsp:setProperty name='embeddedImages' property='imageUri' value='<%= RewriteHandling.getContextPath(request) + "/download/drive/HEAD/library/" + item.getId().toString() + "/" %>' />
  </c:otherwise>
</c:choose>
<c:if test='<%= request.isRequestedSessionIdFromURL() %>'>
  <jsp:setProperty name='embeddedImages' property='imageUriAppend' value='<%= ";jsessionid=" + request.getRequestedSessionId() %>' />
</c:if>
<imformat:addFormat formatter='<%=embeddedImagesFormatter%>' format='<%=embeddedImages%>'/>

<igw:bean id='summaryFormatter' scope='request' type='com.ivata.mask.web.format.HTMLFormatter'/>
<jsp:setProperty name='embeddedImages' property='onlyBodyContents' value='true' />
<imformat:addFormat formatter='<%=summaryFormatter%>' format='<%=embeddedImages%>'/>
<igw:bean id='stripStyle' scope='page' type='com.ivata.mask.web.format.StripTagFormat'/>
<jsp:setProperty name='stripStyle' property='tagName' value='style'/>
<imformat:addFormat formatter='<%=summaryFormatter%>' format='<%=stripStyle%>'/>


<igw:bean id='emailAddressFormatter' scope='session' type='com.ivata.groupware.web.format.EmailAddressFormatter'/>
<jsp:setProperty name='emailAddressFormatter' property='pageContext' value='<%=pageContext%>'/>

<igw:bean id='libraryRights' type='com.ivata.groupware.business.library.right.LibraryRights'/>

<%-- When preview was set, that should mean we are included in submit... --%>
<imhtml:form refersTo='<%= libraryItemForm.getPreview()!=null?"librarySubmitForm":"refLibraryItemForm" %>'>
  <div align='center'>
    <%String themeName=libraryItemForm.getSummaryThemeName();%>
    <imtheme:window
        styleClass='mainWindow'
        title='<%=titleFormatter.format(item.getTitle()!=null?item.getTitle():"")%>'
        titleRight='<%=libraryItemForm.getPageLinks()%>'
        themeName='<%=themeName%>'>
      <%@include file='/library/include/summary.jspf'%>
      <logic:present name='libraryItemForm' property='displayIncludePage'>
          <jsp:include page='<%=libraryItemForm.getDisplayIncludePage()%>' />
      </logic:present>

      <%-- no buttons for submit preview --%>
      <logic:notPresent name='libraryItemForm' property='preview'>
        <imtheme:buttonFrame>
          <%-- only display the edit button if the user created this item or has permission--%>
          <c:if test='<%=libraryRights.canAmendInTopic(securitySession, item.getTopic().getId())%>'>
            <imhtml:submit fieldName='edit'/>
          </c:if>
          <%-- likewise only display the delete button if the user created this item or has permission--%>
          <c:if test='<%= libraryRights.canRemoveFromTopic(securitySession, item.getTopic().getId())%>'>
            <imhtml:delete/>
          </c:if>
          <imtheme:buttonSpacer/>

          <imutil:map id='fileParams'>
            <imutil:mapEntry name='fileName' value='<%="document.xml"%>'/>
            <imutil:mapEntry name='filePath' value='<%="library"+java.io.File.separator+item.getId()+java.io.File.separator+"versions"+java.io.File.separator%>'/>
            <imutil:mapEntry name='windowTitle' value='<%= item.getTitle() %>'/>
            <%-- if user can amend he can revert --%>
            <c:choose>
              <c:when test='<%=libraryRights.canAmendInTopic(securitySession, item.getTopic().getId())%>'>

                <imutil:mapEntry name='display' value='false'/>
              </c:when>
              <c:otherwise>
                <imutil:mapEntry name='display' value='true'/>
              </c:otherwise>
            </c:choose>
            <imutil:mapEntry name='libraryItemId' value='<%=item.getId().toString()%>'/>
          </imutil:map>
          <jsp:setProperty name='historyPopUp' property='page' value='/drive/fileRevisions.action'/>
          <jsp:setProperty name='historyPopUp' property='paramsName' value='fileParams' />
<%-- TODO
          <imhtml:button fieldName='history'
                          bundle='library'
                          titleKey='displayItem.submit.revisionButton.title'
                          onclick='<%= historyPopUp.toString() %>' />
--%>
          <imtheme:buttonSpacer/>
          <imhtml:cancel fieldName='close'/>
          <imhtml:help key='library.display'/>
        </imtheme:buttonFrame>
      </logic:notPresent>
    </imtheme:window>
  </div>
</imhtml:form>