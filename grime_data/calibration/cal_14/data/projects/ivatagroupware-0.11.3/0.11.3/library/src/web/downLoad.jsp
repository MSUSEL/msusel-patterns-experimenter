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
<%@page import='com.ivata.mask.util.StringHandling' %>
<%@page import='com.ivata.mask.web.format.DateFormatterConstants' %>
<%@page import="com.ivata.mask.web.RewriteHandling"%>
<%@page import='com.ivata.groupware.business.library.item.LibraryItemConstants'%>

<%@page import='org.apache.struts.Globals' %>
<%@page import="org.apache.struts.taglib.TagUtils" %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: downLoad.jsp,v 1.5 2005/04/28 18:47:07 colinmacleod Exp $
//
// Create ONE page document for download all LibraryItem as html documnet
//
// Since: ivata groupware 0.9 (2003-06-27)
// Author: Jan Boros
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
// $Log: downLoad.jsp,v $
// Revision 1.5  2005/04/28 18:47:07  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.4  2005/04/27 15:12:09  colinmacleod
// Changed faq to fAQ.
//
// Revision 1.3  2005/04/22 10:00:34  colinmacleod
// Changed abbreviation from faq to fAQ.
//
// Revision 1.2  2005/04/09 17:19:48  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:52:16  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
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
// Revision 1.4  2003/11/13 16:11:08  jano
// commitng everything to CVS
// can deploy and application is ruuning, can login into
//
// Revision 1.3  2003/10/28 13:16:15  jano
// commiting library,
// still fixing compile and building openPortal project
//
// Revision 1.2  2003/10/15 14:16:54  colin
// fixing for XDoclet
//
// Revision 1.3  2003/07/08 14:48:40  jano
// we have library items in CVS
//
// Revision 1.2  2003/07/03 06:26:37  peter
// embeddedImagesFormatter
//
// Revision 1.1  2003/06/30 13:10:44  jano
// caming to repository
//
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<%@include file='/library/theme/documentSummary.jspf'%>
<%@include file='/library/theme/fAQ.jspf'%>
<%@include file='/library/theme/meetingSummary.jspf'%>
<%@include file='/library/theme/note.jspf'%>
<%@include file='/include/theme.jspf'%>

<igw:checkLogin forward='libraryIndex'/>
<igw:bean id='library' type='com.ivata.groupware.business.library.Library'/>

<igw:bean id='item' scope='session' type='com.ivata.groupware.business.library.item.LibraryItemDO'/>
<igw:bean id='securitySession' type='com.ivata.groupware.admin.security.server.SecuritySession'/>

<igw:bean id='titleFormatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter'/>
<%--igw:bean id='nonBreaking' scope='page' type='com.ivata.mask.web.format.NonBreakingSpaceFormat' />
<imformat:addFormat formatter='<%=titleFormatter%>' format='<%=nonBreaking%>'/--%>

<igw:bean id='summaryFormatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter'/>

<% String formatDateTime = "{0}, ''" + TagUtils.getInstance().message(pageContext, null, Globals.LOCALE_KEY, "time.at") + "'' {1}"; %>

<igw:bean id='dateFormatter' type='com.ivata.mask.web.format.DateFormatter'/>
<jsp:setProperty name='dateFormatter' property='dateFormat' value='<%=com.ivata.mask.web.format.DateFormatterConstants.DATE_RELATIVE%>'/>
<jsp:setProperty name='dateFormatter' property='dateTimeText' value='<%=formatDateTime%>'/>

<igw:bean id='stripStyle' scope='page' type='com.ivata.mask.web.format.StripTagFormat'/>
<jsp:setProperty name='stripStyle' property='tagName' value='style'/>
<imformat:addFormat formatter='<%=summaryFormatter%>' format='<%=stripStyle%>'/>
<igw:bean id='emailAddressFormatter' scope='session' type='com.ivata.groupware.web.format.EmailAddressFormatter'/>
<jsp:setProperty name='emailAddressFormatter' property='pageContext' value='<%=pageContext%>'/>

<c:choose>
  <c:when test='<%=request.getParameter("id") != null && request.getParameter("revision") != null%>'>
    <c:catch var='itemException'>
      <%
// TODO        item = library.findItemByRevision(StringHandling.integerValue(request.getParameter("id")),request.getParameter("revision"),userName);
        session.setAttribute("item", item);
      %>
    </c:catch>
  </c:when>
  <c:when test='<%=request.getParameter("id") != null && request.getParameter("revision") == null%>'>
    <c:catch var='itemException'>
      <%
        item = library.findItemByPrimaryKey(securitySession, StringHandling.integerValue(request.getParameter("id")));
        session.setAttribute("item", item);
      %>
    </c:catch>
  </c:when>
</c:choose>

<c:if test='<%=(item == null) || (item.getTopic().getId() == null)%>'>
  <%session.setAttribute("errorText", "The requested item (id: " +
                                      request.getParameter( "id" ) +
                                      ") could not be located.");%>
  <%--jsp:forward page='/'/--%>
</c:if>

<igw:bean id='embeddedImagesFormatter' scope='request' type='com.ivata.mask.web.format.HTMLFormatter'/>
<igw:bean id='embeddedImages' type='com.ivata.groupware.web.format.SanitizerFormat' />
<jsp:setProperty name='embeddedImages' property='onlyBodyContents' value='true' />
<jsp:setProperty name='embeddedImages' property='imageUri' value='<%= RewriteHandling.getContextPath(request) + "/download/drive/LIBRARY/" + item.getId() + "/" %>' />
<c:if test='<%= request.isRequestedSessionIdFromURL() %>'>
  <jsp:setProperty name='embeddedImages' property='imageUriAppend' value='<%= ";jsessionid=" + request.getRequestedSessionId() %>' />
</c:if>
<imformat:addFormat formatter='<%=embeddedImagesFormatter%>' format='<%=embeddedImages%>'/>


<igw:checkLogin forward='libraryIndex'/>

<imhtml:html locale='true'>
  <igw:head topLevel='true'><imhtml:base/></igw:head>
  <body>
    <div align='center'>
      <%
        String themeName="documentSummaryTheme";
        String includeFile="downloadDocument.jsp";
      %>
      <c:choose>
        <c:when test='<%=item.getType().equals(LibraryItemConstants.ITEM_MEETING)%>'>
          <%
            themeName="meetingSummaryTheme";
            includeFile="downloadMeeting.jsp";
          %>
        </c:when>
        <c:when test='<%=item.getType().equals(LibraryItemConstants.ITEM_NOTE)%>'>
          <%
            themeName="noteTheme";
            includeFile="";
          %>
        </c:when>
        <c:when test='<%=item.getType().equals(LibraryItemConstants.ITEM_FAQ)%>'>
          <%
            themeName="fAQTheme";
            includeFile="downloadFAQ.jsp";
          %>
        </c:when>
      </c:choose>

      <imtheme:window title='<%=titleFormatter.format(item.getTitle())%>'
                       themeName='<%=themeName%>'>
        <%@include file='/library/include/summary.jspf'%>
        <%-- PAGES --%>
        <c:if test='<%=!StringHandling.isNullOrEmpty(includeFile)%>'>
          <jsp:include page='<%=includeFile%>' />
        </c:if>
      </imtheme:window>
    </div>
  </body>
</imhtml:html>
