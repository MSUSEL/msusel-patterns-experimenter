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
<%@page import="com.ivata.mask.web.RewriteHandling"%>

<%@page import='java.util.Arrays' %>
<%@page import='java.util.Collection' %>

<%@page import='org.apache.struts.Globals' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: displayComment.jsp,v 1.6 2005/04/28 18:47:07 colinmacleod Exp $
//
// <p>Display all the comments for a single library item.</p>
//
// Since: ivata groupware 0.9 (2002-07-05)
// Author: Colin MacLeod <colin.macleod@ivata.com>
// $Revision: 1.6 $
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
// $Log: displayComment.jsp,v $
// Revision 1.6  2005/04/28 18:47:07  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.5  2005/04/26 16:33:03  colinmacleod
// Removed defaultComment.jspf include.
//
// Revision 1.4  2005/04/22 09:55:47  colinmacleod
// Removed dependency on classic
// comment theme.
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
// Revision 1.8  2004/12/31 18:48:38  colinmacleod
// Cosmetic indentation improvements.
//
// Revision 1.7  2004/12/27 14:52:01  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.6  2004/07/18 16:20:05  colinmacleod
// Fixed library bundle name.
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
// Revision 1.4  2003/11/18 09:58:15  jano
// commiting all forgoten staff
//
// Revision 1.3  2003/10/28 13:16:15  jano
// commiting library,
// still fixing compile and building openPortal project
//
// Revision 1.2  2003/10/15 14:16:54  colin
// fixing for XDoclet
//
// Revision 1.21  2003/06/30 05:21:58  peter
// userName at the end of cache key, to match other cache namings
//
// Revision 1.20  2003/06/30 04:53:47  peter
// comment tree is cached for each user separately now
//
// Revision 1.19  2003/06/10 11:10:45  peter
// changed cache key naming to suit  multiapp
//
// Revision 1.18  2003/06/09 12:04:30  peter
// cache for comment tree
//
// Revision 1.17  2003/04/01 18:11:20  colin
// changed hasScrollbars to hasScrollBars
//
// Revision 1.16  2003/02/28 13:43:50  colin
// made comments work with popups
//
// Revision 1.15  2003/02/28 07:31:52  colin
// implemented editing/displaying of faqs & notes
//
// Revision 1.14  2003/02/25 07:28:15  colin
// converted display & submission to struts
// started converting comments to popup
// restructured java paths
//
// Revision 1.13  2003/01/24 19:35:18  peter
// changes to sslext
//
// Revision 1.12  2003/01/24 07:31:51  peter
// forgotten i18n
//
// Revision 1.11  2003/01/18 20:38:58  colin
// added help buttons
//
// Revision 1.10  2003/01/14 10:42:19  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.9  2002/11/27 18:00:35  peter
// i18n finished
//
// Revision 1.8  2002/10/29 16:31:24  peter
// /library/theme/classic.jspf include changed to /library/theme/classicComment.jspf
//
// Revision 1.7  2002/09/25 16:25:35  colin
// added more title/tootips to submit buttons
//
// Revision 1.6  2002/09/25 11:29:51  colin
// added relative anchor called 'comments'
//
// Revision 1.5  2002/09/24 07:56:22  peter
// a  'New' comment button added to the top of the comments,
// if there's a comment
//
// Revision 1.4  2002/09/23 14:47:20  peter
// a  'New' comment button added to the top of the comments
//
// Revision 1.3  2002/09/04 16:10:36  colin
// minor cosmetic fixes
//
// Revision 1.2  2002/08/27 08:44:20  colin
// split tags and themes into two separate includes
//
// Revision 1.1  2002/08/11 12:07:20  colin
// First version with basic functionality.
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<igw:checkLogin/>

<igw:bean id='securitySession' scope='session' type='com.ivata.groupware.admin.security.server.SecuritySession'/>

<%-- this popup also used in defaultComment.jsp = must come here --%>
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
<%@include file='/include/theme.jspf'%>
<%@include file='/library/theme/defaultComment.jspf'%>

<igw:bean id='item' name='libraryItemForm' property='item' type='com.ivata.groupware.business.library.item.LibraryItemDO'/>
<igw:bean id='library' type='com.ivata.groupware.business.library.Library'/>

<%-- oscache:cache key='<%= RewriteHandling.getContextPath(request) + "_itemCommentTree_" + item.getId().toString() + "_" + userName %>' time='2000000' --%>

  <%-- make the title look nice --%>
  <igw:bean id='titleFormatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter'/>
  <igw:bean id='characterEntities' scope='page' type='com.ivata.mask.web.format.CharacterEntityFormat'/>
  <imformat:addFormat formatter='<%=titleFormatter%>' format='<%=characterEntities%>'/>
  <igw:bean id='nonBreaking' scope='page' type='com.ivata.mask.web.format.NonBreakingSpaceFormat' />
  <imformat:addFormat formatter='<%=titleFormatter%>' format='<%=nonBreaking%>'/>
  <%-- make the text look nice --%>
  <igw:bean id='textFormatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter'/>

  <div align='center'>
      <a name='comments'></a>
    <igw:bean id='libraryCommentForm' scope='session' type='com.ivata.groupware.business.library.struts.CommentForm'/>
    <imhtml:form action='/library/comment'>
      <imtheme:window
          styleClass='mainWindow'
          bundle='library'
          titleKey='displayComment.title'
          titleArgs='<%=Arrays.asList(new Object[] {titleFormatter.format(item.getTitle())})%>'>

        <%-- show the first 'NEW' button only if there are some comments already--%>
        <c:if test='<%=library.countCommentsForItem(securitySession, item.getId()) != 0%>'>
          <imtheme:buttonFrame>
            <imutil:mapClear mapName='commentParams'/>
            <imutil:mapEntry mapName='commentParams' name='clear' value='true'/>
            <imhtml:button onclick='<%=commentPopUp.toString()%>' valueKey='submit.new.value'
                bundle='library' titleKey='displayComment.submit.asNewButton.title'/>
          </imtheme:buttonFrame>
        </c:if>


        <imtheme:frame>
          <imtheme:framePane>
            <%-- first check we have some comments to show --%>
            <c:choose>
              <c:when test='<%=library.countCommentsForItem(securitySession, item.getId()) == 0%>'>
                <bean:message bundle='library' key='displayComment.label.noComments' />
              </c:when>
              <c:otherwise>
                <%-- construct the tree --%>
                <igw:bean id='commentTreeModel' scope='session' type='com.ivata.groupware.business.library.comment.tree.CommentTreeModel'/>
                <igw:bean id='commentTreeNodeRenderer' scope='page' type='com.ivata.groupware.web.tree.comment.CommentTreeNodeRenderer'/>
                <%-- only show comments for this item --%>
                <jsp:setProperty name='commentTreeModel' property='item' value='<%=item%>'/>
                <igwtree:tree model='<%=commentTreeModel%>'
                               renderer='<%=commentTreeNodeRenderer%>'
                               defaultOpen='true'
                               treeName='<%="commentForItem" + item.getId()%>'
                               themeName='comment'/>
              </c:otherwise>
            </c:choose>
          </imtheme:framePane>
        </imtheme:frame>
        <imtheme:buttonFrame>
          <imutil:mapClear mapName='commentParams'/>
          <imutil:mapEntry mapName='commentParams' name='clear' value='true'/>
          <imhtml:button onclick='<%=commentPopUp.toString()%>' valueKey='submit.new.value'
              bundle='library' titleKey='displayComment.submit.asNewButton.title'/>
          <imhtml:help key='library.comment.display'/>
        </imtheme:buttonFrame>
      </imtheme:window>
    </imhtml:form>
  </div>

<%-- /oscache:cache --%>