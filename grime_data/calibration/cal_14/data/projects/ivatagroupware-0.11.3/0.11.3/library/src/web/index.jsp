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
<%@page import='java.util.Collection' %>
<%@page import='com.ivata.groupware.business.addressbook.person.group.right.RightConstants' %>
<%@page import='com.ivata.groupware.business.library.item.LibraryItemConstants' %>
<%@page import='com.ivata.mask.util.StringHandling' %>
<%@page import='com.ivata.mask.util.ThrowableHandling' %>
<%@page import="com.ivata.mask.web.format.FormatConstants" %>
<%@page import="com.ivata.mask.web.RewriteHandling"%>
<%@page import='java.util.*' %>

<%@page import='org.apache.struts.Globals' %>
<%@page import="org.apache.struts.taglib.TagUtils" %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: index.jsp,v 1.7 2005/04/30 13:04:14 colinmacleod Exp $
//
// The main listing of the library, or 'noticeboard'. This page shows the
// summary for the five most recent library items, and a list of other recent
// items on the right.
//
// Since: ivata groupware 0.9 (2002-06-16)
// Author: Colin MacLeod <colin.macleod@ivata.com>, laco
// $Revision: 1.7 $
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
// $Log: index.jsp,v $
// Revision 1.7  2005/04/30 13:04:14  colinmacleod
// Fixes reverting id type from String to Integer.
//
// Revision 1.6  2005/04/28 18:47:07  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.5  2005/04/27 15:12:09  colinmacleod
// Changed faq to fAQ.
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
// Revision 1.1.1.1  2005/03/10 17:52:16  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.11  2004/12/27 14:52:01  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.10  2004/11/12 15:57:17  colinmacleod
// Removed dependencies on SSLEXT.
// Moved Persistence classes to ivata masks.
//
// Revision 1.9  2004/11/03 15:55:46  colinmacleod
// Changed todo comments to TODO: all caps.
//
// Revision 1.8  2004/09/30 14:59:06  colinmacleod
// Added methods to sanitize the entire library and update the search index.
//
// Revision 1.7  2004/07/18 22:03:13  colinmacleod
// Wrapped collection in a new vector to prevent concurrency probs.
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
// Revision 1.36  2003/07/08 06:48:36  peter
// parser changes - embedded images in summaries too
//
// Revision 1.35  2003/07/04 06:04:05  jano
// we want see last 10 unacknowledged comments
//
// Revision 1.34  2003/07/01 10:42:09  peter
// fixed the unacknowledged comments formatting
//
// Revision 1.33  2003/07/01 09:32:49  jano
// rem out sanitizer for title in unacknowledged comments
//
// Revision 1.32  2003/06/30 07:51:10  peter
// the formatter for unacknowledged comments works
//
// Revision 1.31  2003/06/26 14:49:21  jano
// unacknowledged comments
//
// Revision 1.30  2003/06/26 09:15:12  jano
// unacknowledgedComments
//
// Revision 1.29  2003/06/10 06:01:52  peter
// img and html:img replaced with size-aware imhtml:img
//
// Revision 1.28  2003/03/25 08:18:49  jano
// fixing width of note Item
//
// Revision 1.27  2003/03/20 08:08:21  jano
// fixing recent items style sheet
//
// Revision 1.26  2003/03/12 14:06:13  jano
// taking away nobraeking spaces from title of items
// fixing width of main and recent items cell
//
// Revision 1.25  2003/02/28 07:31:52  colin
// implemented editing/displaying of faqs & notes
//
// Revision 1.24  2003/02/25 07:28:15  colin
// converted display & submission to struts
// started converting comments to popup
// restructured java paths
//
// Revision 1.23  2003/01/27 07:18:46  peter
// fixed miswritten no-body html:link tags
//
// Revision 1.22  2003/01/24 19:35:18  peter
// changes to sslext
//
// Revision 1.21  2003/01/24 07:29:07  colin
// added checkLogin tag
//
// Revision 1.20  2003/01/14 10:42:19  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.19  2002/12/20 10:11:10  jano
// fixing slovak "at" and "on" with date time
//
// Revision 1.18  2002/12/04 08:22:46  jano
// we are not going use right.jsp, so we can read all libraryItems at one time
//
// Revision 1.17  2002/11/27 18:00:35  peter
// i18n finished
//
// Revision 1.16  2002/11/13 14:36:43  jano
// if thera are not documents display a message
//
// Revision 1.15  2002/10/14 13:55:38  jano
// theme for summary
//
// Revision 1.14  2002/10/08 16:22:03  colin
// added meeting theme
//
// Revision 1.13  2002/09/25 11:30:27  colin
// implemented themes
//
// Revision 1.12  2002/09/23 12:39:59  jano
// now we don't need reresh right frame
//
// Revision 1.11  2002/09/23 12:31:57  jano
// right.jsp is now including in library/index.jsp
//
// Revision 1.10  2002/09/23 11:56:50  colin
// date formatter constants now in a separate class
//
// Revision 1.9  2002/09/12 16:02:54  jano
// fixing bug with session.removeAtribute(errorText)
//
// Revision 1.8  2002/09/05 10:23:54  colin
// added javascript to refresh the right pane
//
// Revision 1.7  2002/09/04 16:07:44  colin
// moved the igw:head tag
//
// Revision 1.6  2002/08/27 08:44:20  colin
// split tags and themes into two separate includes
//
// Revision 1.5  2002/07/01 08:07:56  colin
// moved SetPropertyTag to util from webgui
//
// Revision 1.4  2002/06/28 13:16:59  colin
// first library release
//
// Revision 1.3  2002/06/19 14:08:56  laco
// implemented basic layout
//
// Revision 1.2  2002/06/18 08:42:54  colin
// first basic working version of library
//
// Revision 1.1  2002/06/17 07:35:01  colin
// improved and extended documentation
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<%--
 uncomment the following scriptlet to force a theme to reload - change the
 attribute as appropriate: documentTheme, documentSummaryTheme, faqTheme,
 noteTheme, etc.
<%
  session.removeAttribute("documentTheme");
  session.removeAttribute("documentSummaryTheme");
  session.removeAttribute("fAQTheme");
  session.removeAttribute("noteTheme");
%>
--%>

<%@include file='/library/theme/note.jspf'%>
<%@include file='/library/theme/documentSummary.jspf'%>
<%@include file='/library/theme/fAQ.jspf'%>
<%@include file='/library/theme/meetingSummary.jspf'%>
<%@include file='/include/theme.jspf'%>

<igw:checkLogin/>

<logic:notPresent name='libraryIndexForm'>
  <logic:redirect forward='libraryIndexAction'/>
</logic:notPresent>

<imhtml:html locale='true'>
  <igw:head bundle='library' titleKey='index.title'><imhtml:base/></igw:head>

  <%-- these are used to see if the year matches this year --%>
  <igw:bean id='themeName' scope='page' type='java.lang.String'/>
  <igw:bean id='library' type='com.ivata.groupware.business.library.Library'/>
  <igw:bean id='item' type='com.ivata.groupware.business.library.item.LibraryItemDO'/>

  <igw:bean id='titleFormatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter'/>
  <%--igw:bean id='nonBreaking' scope='page' type='com.ivata.mask.web.format.NonBreakingSpaceFormat' />
  <imformat:addFormat formatter='<%=titleFormatter%>' format='<%=nonBreaking%>'/--%>

  <% String formatDateTime = "{0}, ''" + TagUtils.getInstance().message(pageContext, null, Globals.LOCALE_KEY, "time.at") + "'' {1}"; %>

  <igw:bean id='dateFormatter' type='com.ivata.mask.web.format.DateFormatter'/>
  <jsp:setProperty name='dateFormatter' property='dateFormat' value='<%=com.ivata.mask.web.format.DateFormatterConstants.DATE_RELATIVE%>'/>
  <jsp:setProperty name='dateFormatter' property='dateTimeText' value='<%=formatDateTime%>'/>

  <igw:bean id='summaryFormatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter'/>
  <igw:bean id='embeddedImages' type='com.ivata.groupware.web.format.SanitizerFormat' />
  <jsp:setProperty name='embeddedImages' property='onlyBodyContents' value='true' />
  <c:if test='<%= request.isRequestedSessionIdFromURL() %>'>
    <jsp:setProperty name='embeddedImages' property='imageUriAppend' value='<%= ";jsessionid=" + request.getRequestedSessionId() %>' />
  </c:if>
  <imformat:addFormat formatter='<%=summaryFormatter%>' format='<%=embeddedImages%>'/>
  <igw:bean id='stripStyle' scope='page' type='com.ivata.mask.web.format.StripTagFormat'/>
  <jsp:setProperty name='stripStyle' property='tagName' value='style'/>
  <imformat:addFormat formatter='<%=summaryFormatter%>' format='<%=stripStyle%>'/>

  <%-- error text for passing from other screens --%>
  <igw:bean id='errorText' scope='session' type='java.lang.String'/>
  <imutil:map id='displayLocationParameters' />

  <igw:getSetting id='libraryHome' setting='libraryHome' type='java.lang.Integer'/>
  <igw:getSetting id='libraryRecent' setting='libraryRecent' type='java.lang.Integer'/>
  <%
    int numberOfItems = libraryHome.intValue();
    int numberOfRecent = libraryRecent.intValue();
  %>
  <igw:bean id='items' name='libraryIndexForm' property='items' type='java.util.Collection'/>
  <% // hack to avoid concurrency
    items = new Vector(items);
  %>
  <igw:bean id='unacknowledgedComments' name='libraryIndexForm' property='unacknowledgedComments' type='java.util.Collection'/>
  <igw:bean id='commentsForItem' name='libraryIndexForm' property='commentsForItem' type='java.util.Map'/>

  <body>
    <%@include file='/include/errorWindow.jspf'%>
    <c:choose>
      <c:when test='<%=items.size()==0%>'>
        <imtheme:window bundle='library' titleKey='index.label.noDocuments'>
          <imtheme:frame>
            <imtheme:framePane>
            <p><bean:message bundle='library' key='index.alert.noDocuments' /></p>
            </imtheme:framePane>
          </imtheme:frame>
        </imtheme:window>
        <br/>
      </c:when>
      <%-- right pane with unacknowledged comments and recentItems --%>
      <c:otherwise>
        <%-- unacknowledged comments --%>
        <c:if test='<%=!unacknowledgedComments.isEmpty()%>'>
          <igw:bean id='formatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter'/>
          <igw:bean id='sanitizer' type='com.ivata.groupware.web.format.SanitizerFormat' />
          <jsp:setProperty name='sanitizer' property='textOnly' value='true' />
          <jsp:setProperty name='sanitizer' property='onlyBodyContents' value='true' />
          <imformat:addFormat formatter='<%=formatter%>' format='<%=sanitizer%>'/>

          <imtheme:window bundle='library' titleKey='unacknowledged.title' styleClass='rightWindow'>
            <imtheme:frame>
              <imtheme:framePane styleClass='hilight'>
                <table cellpadding='0' cellspacing='0' border='0' width = '100%'>
                  <c:forEach var='comment' items='<%=unacknowledgedComments%>'>
                    <igw:bean id='comment' type='com.ivata.groupware.business.library.comment.CommentDO'/>
                    <tr>
                      <td style='width: 180px' title='<%= (comment.getFormat()==FormatConstants.FORMAT_HTML)?formatter.format(comment.getText()):comment.getText() %>'>
                        <imutil:mapEntry mapName='displayLocationParameters' name='id' value='<%=StringHandling.getNotNull(comment.getItem().getId(), "")%>'/>
                        <imhtml:link page='/library/display.action'
                                     name='displayLocationParameters'
                                     anchor='<%="comment"+comment.getId().toString()%>'>
                          <%=comment.getSubject()%>
                        </imhtml:link>
                      </td>
                      <igw:bean id='hideComment'><bean:message bundle='library' key='unacknowledged.hide'/></igw:bean>
                      <td style='width: 30px' title='<%=hideComment%>'>
                        <imutil:map id='hideUnacknowledgedCommentsParam' />
                        <imutil:mapEntry mapName='hideUnacknowledgedCommentsParam' name='hide' value='<%=comment.getId().toString()%>'/>
                        <imhtml:link forward='libraryUnacknowledgedComments' name='hideUnacknowledgedCommentsParam'>
                          <html:img page='/images/cross.gif' width='15' height='13' border='0'/>
                        </imhtml:link>
                      </td>
                    <tr>
                  </c:forEach>
                </table>
              </imtheme:framePane>
            </imtheme:frame>
            <imtheme:buttonFrame>
              <imhtml:link page='/library/openComments.jsp'><bean:message bundle='library' key='index.label.moreComments'/></imhtml:link>
            </imtheme:buttonFrame>
          </imtheme:window>
          <br/>
        </c:if>

        <%-- recentItems --%>
        <imtheme:window bundle='library' titleKey='right.title' styleClass='rightWindow'>
          <imtheme:frame>
            <imtheme:framePane styleClass='hilight'>
              <ul class='recentItems'>

                <% Iterator r = items.iterator(); %>
                <c:forEach var='whichRecent' begin='0' end='<%=numberOfRecent-1%>'>

                  <c:if test='<%=r.hasNext()%>'>
                    <% item = (com.ivata.groupware.business.library.item.LibraryItemDO) r.next(); %>

                  <imutil:mapEntry mapName='displayLocationParameters' name='id' value='<%=StringHandling.getNotNull(item.getId(), "")%>'/>
                    <%-- TODO: add a count of comments where the minus is now--%>
                    <li class='recentItems'>
                      <imhtml:link page='/library/display.action' name='displayLocationParameters'>
                        <%=item.getTitle()%> (<%=commentsForItem.get(item.getId())%>)
                      </imhtml:link>

                  </c:if>

                </c:forEach>
              </ul>
            </imtheme:framePane>
          </imtheme:frame>
          <imtheme:buttonFrame>
            <imhtml:link page='/library/topicItems.jsp'><bean:message bundle='library' key='index.label.moreItems'/></imhtml:link>
          </imtheme:buttonFrame>
        </imtheme:window>
      </c:otherwise>
    </c:choose>

    <div class='libraryContainer'>
      <%-- next section puts out a message when the users could not be notified after an item was added or changed --%>
      <logic:present name='libraryNotificationException'>
        <igw:bean id='notificationException' name='libraryNotificationException' type='com.ivata.groupware.business.library.NotificationException'/>
        <%session.removeAttribute("libraryNotificationException");%>

        <igw:bean id='notificationFormatter' type='com.ivata.mask.web.format.HTMLFormatter'/>
        <igw:bean id='characterEntityFormat' type='com.ivata.mask.web.format.CharacterEntityFormat'/>
        <imformat:addFormat formatter='<%=notificationFormatter%>' format='<%=characterEntityFormat%>'/>
        <igw:bean id='lineBreaks' type='com.ivata.mask.web.format.LineBreakFormat'/>
        <jsp:setProperty name='lineBreaks' property='convertLineBreaks' value='true'/>
        <imformat:addFormat formatter='<%=notificationFormatter%>' format='<%=lineBreaks%>'/>
        <%Throwable notificationCause = ThrowableHandling.getCause(notificationException);%>

        <igw:bean id='notificationMessage'><imformat:format formatter='<%=notificationFormatter%>'><%=notificationCause.getMessage()%></imformat:format><br/></igw:bean>
        <igw:bean id='notificationSender'><imformat:format formatter='<%=notificationFormatter%>'><%=notificationException.getSenderEmailAddress()%></imformat:format><br/></igw:bean>
        <igw:bean id='notificationRecipients'><imformat:format formatter='<%=notificationFormatter%>'><%=StringHandling.getNotNull(notificationException.getRecipientEmailAddresses(), "[None]")%></imformat:format><br/></igw:bean>

        <imtheme:window bundle='library' titleKey='index.label.notification'>
          <imtheme:frame>
            <imtheme:framePane>
            <p><bean:message bundle='library' key='index.alert.notification' arg0='<%=notificationMessage%>' arg1='<%=notificationSender%>' arg2='<%=notificationRecipients%>'/></p>
            </imtheme:framePane>
          </imtheme:frame>
        </imtheme:window>
        <br/>
      </logic:present>
      <% Iterator i = items.iterator(); %>
      <c:forEach var='whichItem' begin='0' end='<%=numberOfItems-1%>'>

        <c:if test='<%=i.hasNext()%>'>
          <% item = (com.ivata.groupware.business.library.item.LibraryItemDO) i.next(); %>
          <jsp:setProperty name='embeddedImages' property='imageUri' value='<%= RewriteHandling.getContextPath(request) + "/download/drive/HEAD/library/" + item.getId().toString() + "/" %>' />
          <c:choose>
            <c:when test='<%=item.getType().equals(LibraryItemConstants.ITEM_MEETING)%>'>
              <% themeName="meetingSummary"; %>
            </c:when>
            <c:when test='<%=item.getType().equals(LibraryItemConstants.ITEM_FAQ)%>'>
              <% themeName="fAQ"; %>
            </c:when>
            <c:when test='<%=item.getType().equals(LibraryItemConstants.ITEM_NOTE)%>'>
              <% themeName="note"; %>
            </c:when>
            <c:when test='<%=item.getType().equals(LibraryItemConstants.ITEM_NEWS)%>'>
              <% themeName="documentSummary"; %>
            </c:when>
            <c:otherwise>
              <% themeName="documentSummary"; %>
            </c:otherwise>
          </c:choose>
          <imtheme:window
               themeName='<%=themeName%>'
               title='<%=titleFormatter.format(item.getTitle())%>'>
            <%@include file='/library/include/summary.jspf'%>
            <imtheme:buttonFrame themeName='<%=themeName%>'>
              <table cellpadding='0' cellspacing='0' border='0' width = '95%' align='center'>
                <tr>
                  <imutil:mapEntry mapName='displayLocationParameters' name='id' value='<%=StringHandling.getNotNull(item.getId(), "")%>'/>
                  <td align='left'><imhtml:link page='/library/display.action' name='displayLocationParameters' ><bean:message bundle='library' key='index.label.readMore' /></imhtml:link></td>
                  <%int commentCount = ((Integer) commentsForItem.get(item.getId())).intValue();%>
                  <c:if test='<%=commentCount > 0%>'>
                  <td align='right'>
                    <imhtml:link page='/library/display.action' name='displayLocationParameters' anchor='comments'>
                      <bean:message bundle='library' key='index.label.commentNo'/>&nbsp;<%=commentCount%>
                    </imhtml:link>
                  </td>
                  </c:if>
                </tr>
              </table>
            </imtheme:buttonFrame>
          </imtheme:window>
          <br/>

        </c:if>
      </c:forEach>
    </div>
  </body>
</imhtml:html>