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
<%@page import='com.ivata.mask.web.format.HTMLFormatter' %>
<%@page import='com.ivata.groupware.business.library.*' %>
<%@page import='com.ivata.groupware.business.library.item.*' %>
<%@page import='com.ivata.groupware.business.library.right.*' %>
<%@page import='com.ivata.groupware.business.search.*' %>
<%@page import='com.ivata.mask.util.StringHandling' %>

<%@page import='java.util.*' %>

<%@page import='org.apache.struts.Globals' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: openComments.jsp,v 1.4 2005/04/28 18:47:07 colinmacleod Exp $
//
// List all open comments, They are sorted by date.
//
// Since: ivata groupware 0.9 (2003-07-18)
// Author: Jan Boros
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
// $Log: openComments.jsp,v $
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
// Revision 1.1.1.1  2005/03/10 17:52:12  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.9  2004/12/27 14:52:01  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.8  2004/11/12 15:57:17  colinmacleod
// Removed dependencies on SSLEXT.
// Moved Persistence classes to ivata masks.
//
// Revision 1.7  2004/07/19 22:02:13  colinmacleod
// Changes to the CommentDO interface.
//
// Revision 1.6  2004/07/18 16:20:05  colinmacleod
// Fixed library bundle name.
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
// Revision 1.1.1.1  2004/01/27 20:58:45  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.1  2004/01/12 14:03:31  jano
// caming to repository
//
// Revision 1.1  2003/07/21 14:17:46  jano
// caming to repository
//
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<%@include file='/include/theme.jspf'%>

<igw:bean id='themeName' scope='page' type='java.lang.String'/>

<igw:bean id='titleFormatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter'/>
<igw:bean id='nonBreaking' scope='page' type='com.ivata.mask.web.format.NonBreakingSpaceFormat' />
<imformat:addFormat formatter='<%=titleFormatter%>' format='<%=nonBreaking%>'/>
<igw:bean id='maximumLengthTitle' type='com.ivata.mask.web.format.MaximumLengthFormat'/>
<jsp:setProperty name='maximumLengthTitle' property='maximumLength' value='45'/>
<imformat:addFormat formatter='<%=titleFormatter%>' format='<%=maximumLengthTitle%>'/>

<igw:bean id='dateFormatter' type='com.ivata.mask.web.format.DateFormatter'/>
<jsp:setProperty name='dateFormatter' property='dateFormat' value='<%=com.ivata.mask.web.format.DateFormatterConstants.DATE_RELATIVE%>'/>

<imutil:map id='commentParams'/>
<igw:bean id='commentPopUp' scope='page' type='com.ivata.mask.web.javascript.JavaScriptWindow'/>
<jsp:setProperty name='commentPopUp' property='windowName' value='ivataComment'/>
<jsp:setProperty name='commentPopUp' property='frameName' value='ivataComment'/>
<jsp:setProperty name='commentPopUp' property='page' value='/library/comment.action'/>
<jsp:setProperty name='commentPopUp' property='paramsName' value='commentParams'/>
<jsp:setProperty name='commentPopUp' property='pageContext' value='<%=pageContext%>'/>
<jsp:setProperty name='commentPopUp' property='height' value='480'/>
<jsp:setProperty name='commentPopUp' property='width' value='900'/>
<jsp:setProperty name='commentPopUp' property='hasScrollBars' value='no'/>

<%-- error text for passing from other screens --%>
<igw:bean id='errorText' scope='session' type='java.lang.String'/>

<igw:bean id='library' type='com.ivata.groupware.business.library.Library'/>
<igw:bean id='securitySession' scope='session' type='com.ivata.groupware.admin.security.server.SecuritySession'/>

<% Vector comments = new Vector(library.findUnacknowledgedComments(securitySession,null)); %>

<imhtml:html locale='true'>
  <igw:head bundle='library' titleKey='search.title'>
    <imhtml:base/>
  </igw:head>

  <div align='center'>
    <imtheme:window bundle='library' titleKey='openComments.title'>

      <imutil:map id='displayParameters' />

      <imtheme:listFrame sortInternally='false'>
        <imtheme:listHeading>
          <imtheme:listColumnHeading>&nbsp;</imtheme:listColumnHeading>
          <imtheme:listColumnHeading><bean:message bundle='library' key='openComments.label.document'/></imtheme:listColumnHeading>
          <imtheme:listColumnHeading><bean:message bundle='library' key='openComments.label.subject'/></imtheme:listColumnHeading>
          <imtheme:listColumnHeading><bean:message bundle='library' key='openComments.label.text'/></imtheme:listColumnHeading>
          <imtheme:listColumnHeading><bean:message bundle='library' key='openComments.label.date'/></imtheme:listColumnHeading>
          <imtheme:listColumnHeading><bean:message bundle='library' key='openComments.label.author'/></imtheme:listColumnHeading>
          <imtheme:listColumnHeading>&nbsp;</imtheme:listColumnHeading>
          <imtheme:listColumnHeading>&nbsp;</imtheme:listColumnHeading>
        </imtheme:listHeading>
        <imtheme:listBody var='comment' rows='<%=comments%>' rowsPerPage='10'>
          <igw:bean id='comment' type='com.ivata.groupware.business.library.comment.CommentDO'/>

            <imtheme:listColumn>
              PROPERTY(currentItemIndex).
            </imtheme:listColumn>

            <imtheme:listColumn>
              <%--b><%=titleFormatter.format(comment.getItemTitle())%></b--%>
              <imutil:mapEntry mapName='displayParameters' name='id' value='<%=StringHandling.getNotNull(comment.getItem().getId(), "")%>'/>
              <imhtml:link page='/library/display.action'
                           name='displayParameters'>
                <%=titleFormatter.format(comment.getItem().getTitle())%>
              </imhtml:link>
            </imtheme:listColumn>

            <imtheme:listColumn>
              <imutil:mapEntry mapName='displayParameters' name='id' value='<%=StringHandling.getNotNull(comment.getItem().getId(), "")%>'/>
              <imhtml:link page='/library/display.action'
                           name='displayParameters'
                           anchor='<%="comment"+comment.getId().toString()%>'>
                <bean:write name='comment' property='subject'/>
              </imhtml:link>
            </imtheme:listColumn>

            <imtheme:listColumn>
              <%=comment.getText()%>
            </imtheme:listColumn>

            <imtheme:listColumn>
              <imformat:formatDate formatter='<%=dateFormatter%>' date='<%=comment.getModified()%>'/>
            </imtheme:listColumn>

            <imtheme:listColumn>
              <bean:write name='comment' property='createdBy.name'/>
            </imtheme:listColumn>

            <imtheme:listColumn>
              <imutil:mapClear mapName='commentParams'/>
              <imutil:mapEntry mapName='commentParams' name='parentId' value='<%=comment.getId().toString()%>'/>
              <imutil:mapEntry mapName='commentParams' name='list' value='true'/>
              <imhtml:button onclick='<%=commentPopUp.toString()%>' bundle='library' valueKey='treeComment.submit.replyButton.value' titleKey='treeComment.submit.replyButton.title' />
            </imtheme:listColumn>

            <imtheme:listColumn>
              <igw:bean id='hideComment'><bean:message bundle='library' key='unacknowledged.hide'/></igw:bean>
              <imutil:map id='hideUnacknowledgedCommentsParam' />
              <imutil:mapEntry mapName='hideUnacknowledgedCommentsParam' name='hide' value='<%=comment.getId().toString()%>'/>
              <imutil:mapEntry mapName='hideUnacknowledgedCommentsParam' name='list' value='true'/>
              <imhtml:link forward='libraryUnacknowledgedComments' name='hideUnacknowledgedCommentsParam'>
                <html:img page='/images/cross.gif' width='15' height='13' border='0' title='<%=hideComment%>'/>
              </imhtml:link>
            </imtheme:listColumn>

          </imtheme:listBody>

          <imtheme:listEmpty>
            <bean:message bundle='library' key='openComments.list.empty'/>
          </imtheme:listEmpty>

        </imtheme:listFrame>
      </imtheme:window>
    </div>
  </body>
</imhtml:html>
