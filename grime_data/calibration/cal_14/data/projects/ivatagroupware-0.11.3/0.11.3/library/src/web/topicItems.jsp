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
<%@page import='com.ivata.groupware.business.library.topic.TopicDO' %>
<%@page import='com.ivata.groupware.business.search.*' %>
<%@page import='com.ivata.mask.util.StringHandling' %>

<%@page import='java.util.*' %>

<%@page import='org.apache.struts.Globals' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: topicItems.jsp,v 1.5 2005/04/30 13:04:14 colinmacleod Exp $
//
// List all items for a given topic, or for all topics. Adapted from the search
// results page - TODO: - investigate reuniting this with the search page.
//
// Since: ivata groupware 0.9 (2003-01-20)
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
// $Log: topicItems.jsp,v $
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
// Revision 1.9  2004/12/27 14:52:01  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.8  2004/11/12 15:57:17  colinmacleod
// Removed dependencies on SSLEXT.
// Moved Persistence classes to ivata masks.
//
// Revision 1.7  2004/11/03 15:55:47  colinmacleod
// Changed todo comments to TODO: all caps.
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
// Revision 1.1.1.1  2004/01/27 20:58:47  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.3  2003/10/28 13:16:15  jano
// commiting library,
// still fixing compile and building openPortal project
//
// Revision 1.2  2003/10/15 14:16:54  colin
// fixing for XDoclet
//
// Revision 1.10  2003/06/10 06:01:53  peter
// img and html:img replaced with size-aware imhtml:img
//
// Revision 1.9  2003/04/14 07:26:39  peter
// fixed helpkey
//
// Revision 1.8  2003/03/07 15:53:45  peter
// fixed: action for edit
//
// Revision 1.7  2003/03/07 15:37:12  peter
// fixed popUp parameters for new item
//
// Revision 1.6  2003/03/04 14:25:22  colin
// cosmetic changes
//
// Revision 1.5  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.4  2003/02/25 07:28:15  colin
// converted display & submission to struts
// started converting comments to popup
// restructured java paths
//
// Revision 1.3  2003/01/31 13:05:28  jano
// fixing bug
//
// Revision 1.2  2003/01/27 16:12:53  peter
// basic functionality, finished
//
// Revision 1.1  2003/01/27 07:21:03  colin
// first version of topicItems - doesnt work yet :-)
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<%@include file='/include/theme.jspf'%>


<%--<igw:bean id='themeName' scope='page' type='java.lang.String'/>--%>
<igw:bean id='titleFormatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter'/>
<igw:bean id='nonBreaking' scope='page' type='com.ivata.mask.web.format.NonBreakingSpaceFormat' />
<imformat:addFormat formatter='<%=titleFormatter%>' format='<%=nonBreaking%>'/>
<igw:bean id='summaryFormatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter'/>

<igw:bean id='dateFormatter' type='com.ivata.mask.web.format.DateFormatter'/>
<jsp:setProperty name='dateFormatter' property='dateFormat' value='<%=com.ivata.mask.web.format.DateFormatterConstants.DATE_RELATIVE%>'/>
<igw:bean id='stripStyle' scope='page' type='com.ivata.mask.web.format.StripTagFormat'/>
<jsp:setProperty name='stripStyle' property='tagName' value='style'/>
<imformat:addFormat formatter='<%=summaryFormatter%>' format='<%=stripStyle%>'/>
<igw:bean id='maximumLengthTitle' type='com.ivata.mask.web.format.MaximumLengthFormat'/>
<jsp:setProperty name='maximumLengthTitle' property='maximumLength' value='62'/>
<imformat:addFormat formatter='<%=summaryFormatter%>' format='<%=maximumLengthTitle%>'/>
<imformat:addFormat formatter='<%=summaryFormatter%>' format='<%=nonBreaking%>'/>
<%-- error text for passing from other screens --%>
<igw:bean id='errorText' scope='session' type='java.lang.String'/>
<igw:bean id='securitySession' scope='session' type='com.ivata.groupware.admin.security.server.SecuritySession'/>

<igw:bean id='library' type='com.ivata.groupware.business.library.Library'/>
<igw:bean id='libraryRights' type='com.ivata.groupware.business.library.right.LibraryRights'/>
<%
  Integer topicId = StringHandling.integerValue(request.getParameter("topicId"));
  // this id is passed to the server method
  Integer purposeTopicId;

  if (topicId==null || topicId.equals(new Integer(0))) {
    purposeTopicId=null;
    topicId = new Integer(0);

  } else {
    purposeTopicId=topicId;
  }

  List topics = library.findTopics(securitySession);
  TopicDO thisTopic;
  try {
    thisTopic = library.findTopicByPrimaryKey(securitySession, topicId);
  } catch(Exception e) {
    thisTopic = null;
  }
  request.setAttribute("topics", topics);
  List items = (List) library.findRecentItems(securitySession, null,  RightConstants.ACCESS_VIEW, purposeTopicId);
%>

<igw:getSetting id='pathTopics' setting='pathTopics' type='java.lang.String'/>



<%@include file='/library/include/topicPopUp.jspf'%>

<imhtml:html locale='true'>
  <igw:head bundle='library' titleKey='search.title'>
    <imhtml:base/>
  </igw:head>
  <body>
    <div align='center'>

    <igw:bean id='actionEdit'><imhtml:rewrite page='/library/topicFind.action' /></igw:bean>
    <imtheme:window
        styleClass='mainWindow'
        bundle='library'
        titleKey='topicItems.title'>
      <igw:bean id='actionSelect'><imhtml:rewrite forward='libraryTopicItems'/></igw:bean>
      <form name='selectTopicForm' method='post' action='<%=actionSelect%>'>
        <imtheme:frame>
          <imtheme:framePane>

            <table cellpadding='0' cellspacing='0' border='0' width='100%'>
              <tr>
                <td class='fieldCaption' valign='top'>
                  <bean:message bundle='library' key='topicItems.field.topic' />:
                </td>

                <td class='field' valign='top'>
                  <table cellpadding='0' cellspacing='0' border='0'>
                    <tr>
                      <td class='field'>

                        <select name='topicId' onChange='document.selectTopicForm.submit()' >
                          <option value='0'><bean:message bundle='library' key='topicItems.field.topic.allItems' /></option>

                          <logic:iterate id='topic' name='topics' type='com.ivata.groupware.business.library.topic.TopicDO'>
                            <option value='<%=topic.getId()%>' <%=HTMLFormatter.getBooleanAttribute("selected", topicId.equals(topic.getId()))%> />
                              <%= topic.getCaption() %>
                            </option>
                          </logic:iterate>
                        </select>

                      </td class='field'>
                    </tr>
                  </table>
                </td>
                <td align='right'>
                  <c:if test='<%= thisTopic !=null %>'>
                    <imhtml:img page='<%= pathTopics + thisTopic.getImage() %>' />
                  </c:if>
                </td>
              </tr>
            </table>
          </imtheme:framePane>
        </imtheme:frame>

        <imutil:map id='displayParams'/>
        <imtheme:listFrame sortInternally='false' formName='document.selectTopicForm'>
          <imtheme:listHeading>
            <imtheme:listColumnHeading>&nbsp;</imtheme:listColumnHeading>
            <imtheme:listColumnHeading><bean:message key='search.label.docTitle'/></imtheme:listColumnHeading>
            <imtheme:listColumnHeading><bean:message key='search.label.lastModified' /></imtheme:listColumnHeading>
            <imtheme:listColumnHeading><bean:message key='search.label.author' /></imtheme:listColumnHeading>
          </imtheme:listHeading>
          <imtheme:listBody var='item' rows='<%=items%>'>
            <igw:bean id='item' type='com.ivata.groupware.business.library.item.LibraryItemDO'/>

              <imtheme:listColumn>
                PROPERTY(currentItemIndex).
              </imtheme:listColumn>

              <imtheme:listColumn>
                <imutil:mapEntry mapName='displayParams' name='id'><bean:write name='item' property='id'/></imutil:mapEntry>
                <imhtml:link page='/library/display.action' name='displayParams'>
                  <strong><%=titleFormatter.format(item.getTitle())%></strong><br />
                </imhtml:link>
              </imtheme:listColumn>

              <imtheme:listColumn>
                <imformat:formatDate formatter='<%=dateFormatter%>' date='<%=item.getModified()%>'/>
              </imtheme:listColumn>

              <imtheme:listColumn>
                <bean:write name='item' property='createdBy.name'/>
              </imtheme:listColumn>

            </imtheme:listBody>

            <imtheme:listEmpty>
              <bean:message bundle='library' key='topicItems.label.noItems' />
            </imtheme:listEmpty>

          </imtheme:listFrame>

        </form>

        <imtheme:buttonFrame>
          <c:if test='<%=libraryRights.canAddTopic(securitySession)%>'>
            <imutil:mapClear mapName='topicParams'/>
            <imutil:mapEntry mapName='topicParams' name='clear' value='true'/>
            <jsp:setProperty name='topicPopUp' property='page' value='/library/topic.action'/>
            <%-- TODO: new topic disabled for ivata groupware 0.10
            <imhtml:button bundle='library'
                   onclick='<%=topicPopUp.toString()%>'
                   valueKey='topicItems.submit.new.value'
                   titleKey='topic.submit.new.title'/>
            --%>
          </c:if>
          <%-- if there is no topic, don' show the edit button --%>
          <c:if test='<%=(topicId != null) && !topicId.equals("0") && libraryRights.canAddTopic(securitySession)%>'>
            <imutil:mapClear mapName='topicParams'/>
            <imutil:mapEntry mapName='topicParams' name='id' value='<%=topicId.toString()%>'/>
            <jsp:setProperty name='topicPopUp' property='page' value='/library/topicFind.action'/>
            <imhtml:button bundle='library' titleKey='topicItems.submit.edit.title'
                    onclick='<%=topicPopUp.toString()%>' valueKey='topicItems.submit.edit.value'/>
          </c:if>
          <imtheme:buttonSpacer/>
          <imhtml:help key='library.topicItems'/>
        </imtheme:buttonFrame>
      </imtheme:window>
    </div>

  </body>
</imhtml:html>
