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
<%@page import='com.ivata.groupware.business.library.struts.TopicForm'%>
<%@page import='com.ivata.groupware.business.addressbook.person.group.GroupConstants'%>
<%@page import='java.io.File'%>

<%@page import='org.apache.struts.Globals' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: topicModify.jsp,v 1.4 2005/04/28 18:47:07 colinmacleod Exp $
//
// <p>This page lets you modify or add a topic, and also acts as a menu for
// the user right options on topics.</p>
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
// $Log: topicModify.jsp,v $
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
// Revision 1.8  2004/12/27 14:52:01  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.7  2004/11/03 15:55:47  colinmacleod
// Changed todo comments to TODO: all caps.
//
// Revision 1.6  2004/07/29 20:53:15  colinmacleod
// Changed groupTreeModel to personTreeModel.
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
// Revision 1.27  2003/06/11 06:28:59  jano
// fixing image tag
//
// Revision 1.26  2003/06/11 06:28:29  jano
// fixing image tag
//
// Revision 1.25  2003/06/10 06:01:53  peter
// img and html:img replaced with size-aware imhtml:img
//
// Revision 1.24  2003/04/14 07:28:25  peter
// helpKey logic, todo: move this logic to form
//
// Revision 1.23  2003/04/07 08:28:16  jano
// remove Syste.err debug
//
// Revision 1.22  2003/03/07 15:36:57  peter
// the hidden field with property topic.id taken off
//
// Revision 1.21  2003/03/04 17:51:29  colin
// fixed imhtml tags
//
// Revision 1.20  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.19  2003/02/25 07:28:15  colin
// converted display & submission to struts
// started converting comments to popup
// restructured java paths
//
// Revision 1.18  2003/01/31 12:19:24  peter
// fixed with the new TreeSelectTag
//
// Revision 1.17  2003/01/24 19:35:18  peter
// changes to sslext
//
// Revision 1.16  2003/01/18 20:38:58  colin
// added help buttons
//
// Revision 1.15  2003/01/14 10:42:19  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.14  2003/01/07 11:01:12  jano
// we want to show only those groups which user can see
//
// Revision 1.13  2002/12/18 09:02:40  jano
// xixing bug
//
// Revision 1.12  2002/12/17 14:46:58  jano
// we we want elete topic whos message before
//
// Revision 1.11  2002/12/13 14:52:13  jano
// we have some ne ptompts
//
// Revision 1.10  2002/12/12 16:35:23  jano
// fixing group tree
//
// Revision 1.9  2002/12/12 09:49:55  jano
// we want to show all groups in View Add Amend Remove tabs
//
// Revision 1.8  2002/12/05 13:59:43  jano
// we changing the title of window
//
// Revision 1.7  2002/12/04 15:03:13  jano
// buttons are depended on rights
//
// Revision 1.6  2002/12/03 16:52:18  jano
// set up ilter to see groups for user
//
// Revision 1.5  2002/12/03 12:43:44  jano
// we want wider window
//
// Revision 1.4  2002/12/03 09:45:25  jano
// I added rights for TOPIC, tabs changed
//
// Revision 1.3  2002/11/28 14:11:15  jano
// we are using STRUTS, we have tabs ....
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
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<%@include file='/include/theme.jspf'%>

<%@include file='/addressBook/include/tree.jspf'%>
<%-- allow to user to see groups which he has rights to --%>
<jsp:setProperty name='filter' property='access' value='<%=com.ivata.groupware.business.addressbook.person.group.right.RightConstants.ACCESS_VIEW%>'/>

<igw:bean id='libraryRights' type='com.ivata.groupware.business.library.right.LibraryRights'/>
<igw:getSetting id='pathTopics' setting='pathTopics' type='java.lang.String'/>
<%
  File topicDirectory = new File(pageContext.getServletContext().getRealPath(pathTopics));
  File []  imageFiles = topicDirectory.listFiles();
%>

<igw:bean id='helpKey' scope='page' type='java.lang.String'/>
<igw:bean id='libraryTopicForm' scope='session' type='com.ivata.groupware.business.library.struts.TopicForm'/>
<igw:bean id='topic' name='libraryTopicForm' property='topic' type='com.ivata.groupware.business.library.topic.TopicDO'/>
<igw:bean id='defaultGroupTreeCaption'>
  <bean:message bundle='library' key='topicModify.label.allGroups' />
</igw:bean>
<igw:bean id='securitySession' scope='session' type='com.ivata.groupware.admin.security.server.SecuritySession'/>

<igw:checkLogin forward='libraryTopic' />
<imhtml:html locale='true'>
  <igw:head bundle='library' titleKey='topicModify.title' topLevel='true'>
    <imhtml:base/>
    <script language="JavaScript" type="text/javascript">
      <!--
        function updateTopicImage() {
          var image = document.getElementById("image");
          var imgTopic = document.getElementById("imgTopic");
          imgTopic.src = "<%=RewriteHandling.getContextPath(request) +
                             pathTopics%>" +
                             image.value;
        }
      // -->
    </script>
  </igw:head>

  <body class='dialog'>

    <% boolean readOnly = ! ((topic.getId()==null) || ( (topic.getId()!=null) && (libraryRights.canAmendTopic(securitySession,topic.getId())) ) ); %>
    <imtheme:window>
      <imhtml:form action='/library/topic' bundle='library' deleteKey='topicModify.alert.deleteEntry' resourceFieldPath='topicModify'>
        <%@include file='/include/errorFrame.jspf'%>

<%--
        <imtheme:tabFrame>
          <imtheme:tabControl name='topicTab' formName='document.libraryTopicForm'>
            <imtheme:tab><bean:message bundle='library' key='topicModify.tab.general' /></imtheme:tab>
            <imtheme:tab><bean:message bundle='library' key='topicModify.tab.view' /></imtheme:tab>
            <imtheme:tab><bean:message bundle='library' key='topicModify.tab.add' /></imtheme:tab>
            <imtheme:tab><bean:message bundle='library' key='topicModify.tab.amend' /></imtheme:tab>
            <imtheme:tab><bean:message bundle='library' key='topicModify.tab.delete' /></imtheme:tab>
          </imtheme:tabControl>

          <imtheme:tabContent>

            <% int activeTab = (libraryTopicForm==null ? 0 :
                               (libraryTopicForm.getTopicTab_activeTab()==null ? 0 : libraryTopicForm.getTopicTab_activeTab().intValue())); %>
            <c:choose>
              <c:when test='<%=activeTab==0%>'>
                <% helpKey="general"; %>
--%>
                <imtheme:frame>
                  <imtheme:framePane>
                    <table border='0' width='100%' cellpadding='0' cellspacing='0'>
                      <tr>
                        <td class='fieldCaption'><imhtml:label fieldName='caption'/></td>
                        <td class='field'><imhtml:text fieldName='caption' property='topic.caption'/></td>
                        <td rowspan='3'>
                          <img id='imgTopic' src='<%=RewriteHandling.getContextPath(request)
                            + (topic.getImage()==null
                            ? "/images/empty.gif"
                            : pathTopics + topic.getImage())%>'/>
                        </td>
                      </tr>
                      <tr>
                        <td class='fieldCaption'><imhtml:label fieldName='image'/></td>
                        <td class='field'>
                          <imhtml:select fieldName='image' property='topic.image' onchange='updateTopicImage()'>
                            <imhtml:option value='null'><bean:message key='topicModify.field.image.choose' bundle='library' /></imhtml:option>
                            <c:forEach var='imageFile' items='<%=imageFiles%>'>
                              <igw:bean id='imageFile' type='java.io.File'/>
                              <c:if test='<%=!imageFile.isDirectory()%>'>
                                <imhtml:option value='<%=imageFile.getName()%>'><%=imageFile.getName()%></imhtml:option>
                              </c:if>
                            </c:forEach>
                          </imhtml:select>
                        </td>
                      </tr>
                      <tr height='100%'><td>&nbsp;</td><td>&nbsp;</td></tr>
                    </table>
                  </imtheme:framePane>
                </imtheme:frame>
<%--
              </c:when>
              <c:otherwise>

                <imtheme:frame propertiesName='propertiesTabContent'>
                  <imtheme:framePane propertiesName='propertiesHilight'>
                    <c:choose>
                      <c:when test='<%=activeTab==1%>'>
                        <bean:message bundle='library' key='topicModify.label.view.item'/>
                        <% helpKey="view"; %>
                      </c:when>
                      <c:when test='<%=activeTab==2%>'>
                        <bean:message bundle='library' key='topicModify.label.add.item'/>
                        <% helpKey="add"; %>
                      </c:when>
                      <c:when test='<%=activeTab==3%>'>
                        <bean:message bundle='library' key='topicModify.label.amend.item'/>
                        <% helpKey="amend"; %>
                      </c:when>
                      <c:when test='<%=activeTab==4%>'>
                        <bean:message bundle='library' key='topicModify.label.remove.item'/>
                        <% helpKey="remove"; %>
                      </c:when>
                    </c:choose>

                    <div align='center'>
                      <%-- don't show the administrator group - - %>
                      <jsp:setProperty name='personTreeModel' property='excludeBranch' value='<%=GroupConstants.GROUP_ADMINISTRATOR%>'/>
                      <c:choose>
                        <c:when test='<%=activeTab==1%>'>
                          <igwtree:treeSelect listSize='7' model='<%=personTreeModel%>' treeName='group' defaultCaption='<%= defaultGroupTreeCaption %>' treeStyle='list' controlName='rightsViewItem' selectedList='<%=java.util.Arrays.asList(libraryTopicForm.getRightsViewItem())%>'/>
                        </c:when>
                        <c:when test='<%=activeTab==2%>'>
                          <igwtree:treeSelect listSize='7' model='<%=personTreeModel%>' treeName='group' defaultCaption='<%= defaultGroupTreeCaption %>' treeStyle='list' controlName='rightsAddItem' selectedList='<%=java.util.Arrays.asList(libraryTopicForm.getRightsAddItem())%>'/>
                        </c:when>
                        <c:when test='<%=activeTab==3%>'>
                          <igwtree:treeSelect listSize='7' model='<%=personTreeModel%>' treeName='group' defaultCaption='<%= defaultGroupTreeCaption %>' treeStyle='list' controlName='rightsAmendItem' selectedList='<%=java.util.Arrays.asList(libraryTopicForm.getRightsAmendItem())%>'/>
                        </c:when>
                        <c:when test='<%=activeTab==4%>'>
                          <igwtree:treeSelect listSize='7' model='<%=personTreeModel%>' treeName='group' defaultCaption='<%= defaultGroupTreeCaption %>' treeStyle='list' controlName='rightsRemoveItem' selectedList='<%=java.util.Arrays.asList(libraryTopicForm.getRightsRemoveItem())%>'/>
                        </c:when>
                      </c:choose>
                    </div>
                  <c:if test='<%=activeTab!=2%>'>
                    <imtheme:frameBreak propertiesName='propertiesHilight'/>
                      <c:choose>
                        <c:when test='<%=activeTab==1%>'>
                          <bean:message bundle='library' key='topicModify.label.view.topic'/>
                        </c:when>
                        <c:when test='<%=activeTab==3%>'>
                          <bean:message bundle='library' key='topicModify.label.amend.topic'/>
                        </c:when>
                        <c:when test='<%=activeTab==4%>'>
                          <bean:message bundle='library' key='topicModify.label.remove.topic'/>
                        </c:when>
                      </c:choose>

                      <div align='center'>
                        <%-- don't show the administrator group - - %>
                        <jsp:setProperty name='personTreeModel' property='excludeBranch' value='<%=GroupConstants.GROUP_ADMINISTRATOR%>'/>
                        <c:choose>
                          <c:when test='<%=activeTab==1%>'>
                            <igwtree:treeSelect listSize='7'  model='<%=personTreeModel%>' treeName='group' defaultCaption='<%= defaultGroupTreeCaption %>' treeStyle='list' controlName='rightsView' selectedList='<%=java.util.Arrays.asList(libraryTopicForm.getRightsView())%>'/>
                          </c:when>
                          <c:when test='<%=activeTab==3%>'>
                            <igwtree:treeSelect listSize='7' model='<%=personTreeModel%>' treeName='group' defaultCaption='<%= defaultGroupTreeCaption %>' treeStyle='list' controlName='rightsAmend' selectedList='<%=java.util.Arrays.asList(libraryTopicForm.getRightsAmend())%>'/>
                          </c:when>
                          <c:when test='<%=activeTab==4%>'>
                            <igwtree:treeSelect listSize='7' model='<%=personTreeModel%>' treeName='group' defaultCaption='<%= defaultGroupTreeCaption %>' treeStyle='list' controlName='rightsRemove' selectedList='<%=java.util.Arrays.asList(libraryTopicForm.getRightsRemove())%>'/>
                          </c:when>
                        </c:choose>
                      </div>
                    </c:if>

                  </imtheme:framePane>
                </imtheme:frame>

              </c:otherwise>
            </c:choose>

          </imtheme:tabContent>
        </imtheme:tabFrame>
--%>
        <imtheme:buttonFrame>
          <%-- if you have right to remove show the button --%>
          <%-- TODO: disabled for ivata groupware 0.10
          <c:if test='<%=(topic.getId()!=null) && (libraryRights.canRemoveTopic(securitySession, topic.getId()))%>'>
            <imhtml:delete/>
          </c:if>
          <imtheme:buttonSpacer/>
          --%>
          <%-- if you have right to amend OR it's new topic show the button --%>
          <c:if test='<%=!readOnly%>'>
            <imhtml:ok/>
          </c:if>
          <imhtml:cancel/>
          <imhtml:help key='<%= "library.topicmodify." + helpKey %>'/>
        </imtheme:buttonFrame>
      </imhtml:form>
    </imtheme:window>
  </body>
  <%@include file='/include/script/fixPopUp.jspf'%>
</imhtml:html>
