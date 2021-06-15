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
<%@page import='com.ivata.groupware.business.search.*' %>
<%@page import='com.ivata.mask.util.StringHandling' %>
<%@page import='java.util.*' %>

<%@page import='com.ivata.groupware.business.library.*' %>
<%@page import='com.ivata.groupware.business.library.right.*' %>
<%@page import='com.ivata.groupware.business.library.item.*' %>

<%@page import='org.apache.struts.Globals' %>
<%@page import="org.apache.struts.taglib.TagUtils" %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: search.jsp,v 1.4 2005/04/28 18:47:06 colinmacleod Exp $
//
// <p>Listing of search results -> library items.</p>
//
// Since: ivata groupware 0.9 (2002-10-25)
// Author: Peter Illes, colin, laco
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
// $Log: search.jsp,v $
// Revision 1.4  2005/04/28 18:47:06  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.3  2005/04/10 20:10:11  colinmacleod
// Added new themes.
// Changed id type to String.
// Changed i tag to em and b tag to strong.
// Improved PicoContainerFactory with NanoContainer scripts.
//
// Revision 1.2  2005/04/09 17:19:56  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:52:41  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.7  2004/12/27 14:52:02  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.6  2004/11/12 16:03:00  colinmacleod
// Removed dependencies on SSLEXT.
// Moved Persistence classes to ivata masks.
//
// Revision 1.5  2004/10/07 14:18:09  colinmacleod
// Removed old method names from sanitizer format.
//
// Revision 1.4  2004/07/14 22:50:26  colinmacleod
// Replaced bean:define with extended igw:bean.
//
// Revision 1.3  2004/07/14 20:59:54  colinmacleod
// Changed all occurrences of jsp:useBean to igw:bean (better tomcat compatibility).
//
// Revision 1.2  2004/03/21 21:16:34  colinmacleod
// Shortened name to ivata op.
//
// Revision 1.1.1.1  2004/01/27 20:59:20  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.2  2003/10/17 14:14:22  jano
// changed names intranet -> portal
//
// Revision 1.1.1.1  2003/10/13 20:47:29  colin
// Restructured portal into subprojects
//
// Revision 1.15  2003/07/04 06:51:12  peter
// fixed summary formatting
//
// Revision 1.14  2003/04/14 06:29:03  peter
// help added
//
// Revision 1.13  2003/03/19 08:40:02  peter
// fixed: search result points to display.action not .jsp
//
// Revision 1.12  2003/02/25 07:32:11  colin
// restructured java file paths
//
// Revision 1.11  2003/02/21 11:16:10  colin
// added userName attribute to the date formatter
//
// Revision 1.10  2003/01/27 07:16:04  colin
// update copyright notice
// convert to sslext
//
// Revision 1.9  2003/01/18 20:38:58  colin
// added help buttons
//
// Revision 1.8  2003/01/14 10:31:46  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.7  2002/11/28 16:56:26  peter
// i18n finished
//
// Revision 1.6  2002/11/22 10:11:02  peter
// fixed ListFrame's sorting - now turned off explicitly, we have it sorted server-side
//
// Revision 1.5  2002/11/12 16:39:22  peter
// applied colin's ListFrameTag changes
//
// Revision 1.4  2002/10/31 16:48:06  peter
// tidied up, formatting
//
// Revision 1.3  2002/10/29 16:52:45  peter
// search theme filename changed
//
// Revision 1.2  2002/10/29 16:37:18  peter
// works with ListFrameTag
//
//
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<%@include file='/include/theme.jspf'%>

<igw:bean id='library' scope='page' type='com.ivata.groupware.business.library.Library'/>
<igw:bean id='search' scope='page' type='com.ivata.groupware.business.search.SearchEngine'/>
<igw:bean id='securitySession' scope='page' type='com.ivata.groupware.admin.security.server.SecuritySession'/>

<%-- these are used to see if the year matches this year --%>
<igw:bean id='titleFormatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter'/>

<igw:bean id='nonBreaking' scope='page' type='com.ivata.mask.web.format.NonBreakingSpaceFormat' />
<imformat:addFormat formatter='<%=titleFormatter%>' format='<%=nonBreaking%>'/>

<igw:bean id='summaryFormatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter'/>
<igw:bean id='textOnly' type='com.ivata.groupware.web.format.SanitizerFormat' />
<jsp:setProperty name='textOnly' property='textOnly' value='true' />
<imformat:addFormat formatter='<%=summaryFormatter%>' format='<%=textOnly%>'/>
<igw:bean id='maximumLengthTitle' type='com.ivata.mask.web.format.MaximumLengthFormat'/>
<jsp:setProperty name='maximumLengthTitle' property='maximumLength' value='62'/>
<imformat:addFormat formatter='<%=summaryFormatter%>' format='<%=maximumLengthTitle%>'/>
<imformat:addFormat formatter='<%=summaryFormatter%>' format='<%=nonBreaking%>'/>

<igw:bean id='dateFormatter' scope='page' type='com.ivata.mask.web.format.DateFormatter'/>
<jsp:setProperty name='dateFormatter' property='dateFormat' value='<%=com.ivata.mask.web.format.DateFormatterConstants.DATE_RELATIVE%>'/>


<%!
TagUtils tagUtils = TagUtils.getInstance();
%>

<imhtml:html locale='true'>
  <igw:bean id='searchTitle'><bean:message key="search.title"/></igw:bean>
  <igw:head title='<%=searchTitle%>'><imhtml:base/></igw:head>

  <div align='center'>
    <%-- if there is an error text, display a window with the error before all others --%>
    <igw:bean id='errorText' scope='session' type='java.lang.String'/>
    <c:if test='<%=!StringHandling.isNullOrEmpty(errorText)%>'>
      <igw:bean id='errorsTitle'><bean:message key="errors.header"/></igw:bean>
      <imtheme:window title='<%=errorsTitle%>'>
        <imtheme:frame>
          <imtheme:framePane>
            <%=errorText%>
            <%
              session.removeAttribute("errorText");
              session.removeAttribute("preview");
            %>
          </imtheme:framePane>
        </imtheme:frame>
      </imtheme:window>
      <br/>
    </c:if>

    <%-- the search itself --%>
    <% List items = new Vector(search.search(securitySession, request.getParameter("query"))); %>
    <imtheme:window title='<%=searchTitle%>'>
      <imtheme:listFrame sortInternally='<%=false%>'>
        <imtheme:listHeading>
          <imtheme:listColumnHeading>&nbsp;</imtheme:listColumnHeading>
          <imtheme:listColumnHeading><bean:message key='search.label.docTitle' /></imtheme:listColumnHeading>
          <imtheme:listColumnHeading><bean:message key='search.label.lastModified' /></imtheme:listColumnHeading>
          <imtheme:listColumnHeading><bean:message key='search.label.author' /></imtheme:listColumnHeading>
        </imtheme:listHeading>
        <imtheme:listBody var='item' rows='<%=items%>'>
          <igw:bean id='item' type='com.ivata.groupware.business.library.item.LibraryItemDO'/>

            <imtheme:listColumn>
              PROPERTY(currentItemIndex).
            </imtheme:listColumn>

            <imtheme:listColumn>
              <imhtml:link page='<%="/library/display.action?id=" + item.getId()%>'>
                <strong><%=titleFormatter.format(item.getTitle())%></strong><br />
                <%=summaryFormatter.format(item.getSummary())%>
              </imhtml:link>
            </imtheme:listColumn>

            <imtheme:listColumn>
              <imformat:formatDate formatter='<%=dateFormatter%>' date='<%=item.getCreated()%>'/>
            </imtheme:listColumn>

            <imtheme:listColumn>
              <%=item.getCreatedBy()%>
            </imtheme:listColumn>

          </imtheme:listBody>

          <imtheme:listEmpty>
            <bean:message key='search.label.noResults' />
          </imtheme:listEmpty>

        </imtheme:listFrame>
        <imtheme:buttonFrame>
          <imhtml:help key='search.list'/>
        </imtheme:buttonFrame>
      </imtheme:window>
    </div>

  </body>
</imhtml:html>
