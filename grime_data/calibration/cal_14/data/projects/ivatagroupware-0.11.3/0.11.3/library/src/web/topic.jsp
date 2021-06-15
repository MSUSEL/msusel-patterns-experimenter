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

<%@page import='java.util.*' %>

<%@page import='org.apache.struts.Globals' %>
<%@page import="org.apache.struts.taglib.TagUtils" %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: topic.jsp,v 1.4 2005/04/28 18:47:07 colinmacleod Exp $
//
// <p>This is the index page for topics section. It displays a list of all the
// topics in the system and lets you choose which topic you would like to
// edit.</p>
//
// Since: ivata groupware 0.9 (2002-01-22)
// Author: Colin MacLeod <colin.macleod@ivata.com>
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
// $Log: topic.jsp,v $
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
// Revision 1.7  2004/07/19 22:03:42  colinmacleod
// Fixed bugs in the library rights and changed the interface to match the new library.
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
// Revision 1.16  2003/06/10 13:12:17  jano
// fixing imhtml:image tag
//
// Revision 1.15  2003/06/10 06:01:53  peter
// img and html:img replaced with size-aware imhtml:img
//
// Revision 1.14  2003/03/07 01:26:27  colin
// fixed new button
//
// Revision 1.13  2003/03/04 18:53:59  colin
// changed prompt to field
//
// Revision 1.12  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.11  2003/02/25 07:28:15  colin
// converted display & submission to struts
// started converting comments to popup
// restructured java paths
//
// Revision 1.10  2003/01/27 16:12:23  peter
// changed:the user gets to topicItems when clicking on a topic
//
// Revision 1.9  2003/01/24 19:35:18  peter
// changes to sslext
//
// Revision 1.8  2003/01/18 20:38:58  colin
// added help buttons
//
// Revision 1.7  2003/01/14 10:42:19  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.6  2002/12/16 12:21:24  peter
// fixed i18n message for noTopics
//
// Revision 1.5  2002/12/13 14:01:41  jano
// using application resource
//
// Revision 1.4  2002/11/29 15:19:50  jano
// if you have not rights to add new topic don't whow ADD button
//
// Revision 1.3  2002/11/28 14:10:44  jano
// we are starting to use STRUTS ...
//
// Revision 1.2  2002/08/27 08:44:20  colin
// split tags and themes into two separate includes
//
// Revision 1.1  2002/06/28 13:16:59  colin
// first library release
//
// Revision 1.3  2002/02/10 14:32:57  colin
// implemented topic group rights
//
// Revision 1.2  2002/02/03 19:54:04  colin
// linked settings to the database, rather than hard-coding in com.ivata.groupware.admin.settings
//
// Revision 1.1  2002/01/24 19:13:33  colin
// created files in new location /article
//
// Revision 1.1  2002/01/24 15:41:15  colin
// first version with image changing javascript; data persistence not yet implemented
//
///////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<%@include file='/include/theme.jspf'%>

<%@include file='/library/include/topicPopUp.jspf'%>
<igw:bean id='securitySession' scope='session' type='com.ivata.groupware.admin.security.server.SecuritySession'/>

<igw:bean id='library' type='com.ivata.groupware.business.library.Library'/>
<igw:bean id='libraryRights' type='com.ivata.groupware.business.library.right.LibraryRights'/>

<%-- get all the topics we can view --%>
<%
  List topics = library.findTopics(securitySession);
%>



<igw:getSetting id='siteTitle' setting='siteTitle' type='java.lang.String'/>
<igw:getSetting id='pathTopics' setting='pathTopics' type='java.lang.String'/>

<igw:checkLogin />
<imhtml:html locale='true'>
  <igw:head title='topics'><imhtml:base/></igw:head>
  <body class='blank'>

      <div align='center'>
        <c:choose>
          <c:when test='<%=topics.size()==0%>'>
            <imtheme:window
                styleClass='mainWindow'
                title='no documents'>
              <imtheme:frame>
                <imtheme:framePane>
                  <p><bean:message bundle='library' key='topic.alert.noTopics'/></p>
                </imtheme:framePane>
              </imtheme:frame>
            </imtheme:window>
          </c:when>

          <c:otherwise>
            <imtheme:window
                styleClass='mainWindow'
                title='<%= siteTitle  + " - " +
                          TagUtils.getInstance().message(pageContext, "library", Globals.LOCALE_KEY, "topic.field.title")%>'>

              <imtheme:frame>
                <imtheme:framePane>
                  <% int nCell = 0; %>
                  <table cellpadding='5' cellspacing='0' border='0' width='100%'>

                    <c:forEach var='topic' items='<%=topics%>' varStatus='topicStatus'>
                      <igw:bean id='topic' type='com.ivata.groupware.business.library.topic.TopicDO'/>
                      <igw:bean id='topicStatus' type='javax.servlet.jsp.jstl.core.LoopTagStatus'/>

                      <c:if test='<%=(nCell++ % 4) == 0%>'>
                        <tr>
                      </c:if>
                      <td valign='center' width='25%'>
                        <table cellpadding='2' cellspacing='0' border='0' align='center'>
                          <tr>
                            <imutil:map id='topicFindParameters' >
                              <imutil:mapEntry name='topicId' value='<%= topic.getId().toString()%>' />
                            </imutil:map>

                            <td align='center'>
                              <imhtml:link page='/library/topicItems.jsp' name='topicFindParameters'>
                                <imhtml:img border='0'
                                    page='<%= pathTopics + topic.getImage()%>'/>
                              </imhtml:link>
                            </td>
                          </tr>
                          <tr>
                            <td align='center'>
                              <imhtml:link page='/library/topicItems.jsp' name='topicFindParameters'>
                                <%= topic.getCaption()%>
                              </imhtml:link>
                            </td>
                          </tr>
                          <c:if test='<%=libraryRights.canAddTopic(securitySession)%>'>
                            <tr>
                              <td align='center'>
                                <imutil:mapEntry mapName='topicParams' name='id' value='<%=topic.getId().toString()%>'/>
                                <imhtml:button bundle='library' titleKey='topicItems.submit.edit.title'
                                        onclick='<%=topicPopUp.toString()%>' valueKey='submit.edit.value'/>
                              </td>
                            </tr>
                          </c:if>
                        </table>
                      </td>
                      <c:if test='<%=((nCell % 4) == 0) || topicStatus.isLast()%>'>
                        </tr>
                      </c:if>
                    </c:forEach>

                  </table>
                </imtheme:framePane>
              </imtheme:frame>
              <imtheme:buttonFrame>
                <c:if test='<%=libraryRights.canAddTopic(securitySession)%>'>
                  <imutil:mapClear mapName='topicParams'/>
                  <imutil:mapEntry mapName='topicParams' name='clear' value='true'/>
                  <jsp:setProperty name='topicPopUp' property='page' value='/library/topic.action'/>
                  <%-- TODO: new topic disabled for ivata groupware 0.10
                  <imhtml:button bundle='library'
                         onclick='<%=topicPopUp.toString()%>'
                         valueKey='submit.new.value'
                         titleKey='topic.submit.new.title'/>
                  --%>
                </c:if>
                <imtheme:buttonSpacer/>
                <imhtml:help key='library.topic'/>
              </imtheme:buttonFrame>
            </imtheme:window>
          </c:otherwise>
        </c:choose>
      </div>
  </body>
</imhtml:html>
