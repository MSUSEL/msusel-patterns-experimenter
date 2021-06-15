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
<%@page import="com.ivata.mask.web.format.HTMLFormatter" %>
<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: notification.jsp,v 1.4 2005/04/28 18:47:08 colinmacleod Exp $
//
// Preferences tab with library notification messages.
//
// Since: ivata groupware 0.9 (2003-02-12)
// Author: Peter Illes
// $Revision: 1.4 $
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
// $Log: notification.jsp,v $
// Revision 1.4  2005/04/28 18:47:08  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.3  2005/04/10 20:10:10  colinmacleod
// Added new themes.
// Changed id type to String.
// Changed i tag to em and b tag to strong.
// Improved PicoContainerFactory with NanoContainer scripts.
//
// Revision 1.2  2005/04/09 17:19:39  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1  2005/03/10 19:23:45  colinmacleod
// Moved to ivata groupware.
//
// Revision 1.6  2004/11/12 15:57:12  colinmacleod
// Removed dependencies on SSLEXT.
// Moved Persistence classes to ivata masks.
//
// Revision 1.5  2004/11/03 15:53:20  colinmacleod
// Changed todo comments to TODO: all caps.
//
// Revision 1.4  2004/07/14 22:50:23  colinmacleod
// Replaced bean:define with extended igw:bean.
//
// Revision 1.3  2004/07/14 20:59:52  colinmacleod
// Changed all occurrences of jsp:useBean to igw:bean (better tomcat compatibility).
//
// Revision 1.2  2004/03/21 21:16:24  colinmacleod
// Shortened name to ivata op.
//
// Revision 1.1  2004/03/03 21:32:34  colinmacleod
// Moved core files to core subproject
//
// Revision 1.1.1.1  2004/01/27 20:58:18  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.2  2003/10/17 12:36:13  jano
// fixing problems with building
// converting intranet -> portal
// Eclipse building
//
// Revision 1.1.1.1  2003/10/13 20:50:52  colin
// Restructured portal into subprojects
//
// Revision 1.9  2003/03/05 17:59:15  peter
// fixed conditional display of used and system hidden fields in setting jsps put to iFrames
//
// Revision 1.8  2003/03/05 11:31:20  colin
// remvoed colon after amend label
//
// Revision 1.7  2003/03/05 11:24:27  peter
// resourceFieldPath in forms
//
// Revision 1.6  2003/03/03 16:57:14  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.5  2003/02/25 07:30:55  colin
// restructured java file paths
//
// Revision 1.4  2003/02/24 15:39:22  peter
// converted to imhtml, added empty images to textareas to keep their minimal height
//
// Revision 1.3  2003/02/19 18:17:05  peter
// fixed window size and text input titles
//
// Revision 1.2  2003/02/14 17:07:14  peter
// started to convert to imhtml
//
// Revision 1.1  2003/02/13 17:10:34  peter
// iframe version
//
////////////////////////////////////////////////////////////////////////////////
--%>

<%@include file='/include/tags.jspf'%>
<%@include file='/include/theme.jspf'%>

<igw:bean id='formatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter'/>
<igw:bean id='nonBreakingSpaces' scope='page' type='com.ivata.mask.web.format.NonBreakingSpaceFormat'/>


<igw:bean id='settingForm' scope='session' type='com.ivata.groupware.admin.setting.struts.SettingForm' />

<imformat:addFormat formatter='<%=formatter%>' format='<%=nonBreakingSpaces%>'/>

<imhtml:form refersTo='ivataSettingForm' resourceFieldPath='setting'>

  <imtheme:frame styleClass='tabContent'>
    <imtheme:framePane style='padding: 0px 0px 0px 0px;'>

      <%--  the pairs of hidden fields are here, they'll be always referred from
            the iframe as fields in the parent --%>
      <logic:notEmpty name='settingForm' property='settingType(libraryNotificationContentComment)'>
        <html:hidden styleId='system_libraryNotificationContentComment' property='settingSystem(libraryNotificationContentComment)' />
        <html:hidden styleId='user_libraryNotificationContentComment' property='settingUser(libraryNotificationContentComment)' />
      </logic:notEmpty>

      <logic:notEmpty name='settingForm' property='settingType(libraryNotificationContentCommentAmend)'>
        <html:hidden styleId='system_libraryNotificationContentCommentAmend' property='settingSystem(libraryNotificationContentCommentAmend)' />
        <html:hidden styleId='user_libraryNotificationContentCommentAmend' property='settingUser(libraryNotificationContentCommentAmend)' />
      </logic:notEmpty>

      <logic:notEmpty name='settingForm' property='settingType(libraryNotificationContentItem)'>
        <html:hidden styleId='system_libraryNotificationContentItem' property='settingSystem(libraryNotificationContentItem)' />
        <html:hidden styleId='user_libraryNotificationContentItem' property='settingUser(libraryNotificationContentItem)' />
      </logic:notEmpty>

      <logic:notEmpty name='settingForm' property='settingType(libraryNotificationContentItemAmend)'>
        <html:hidden styleId='system_libraryNotificationContentItemAmend' property='settingSystem(libraryNotificationContentItemAmend)' />
        <html:hidden styleId='user_libraryNotificationContentItemAmend' property='settingUser(libraryNotificationContentItemAmend)' />
      </logic:notEmpty>

      <logic:notEmpty name='settingForm' property='settingType(libraryNotificationContentItemReply)'>
        <html:hidden styleId='system_libraryNotificationContentItemReply' property='settingSystem(libraryNotificationContentItemReply)' />
        <html:hidden styleId='user_libraryNotificationContentItemReply' property='settingUser(libraryNotificationContentItemReply)' />
      </logic:notEmpty>

      <logic:notEmpty name='settingForm' property='settingType(libraryNotificationContentItemReplyAmend)'>
        <html:hidden styleId='system_libraryNotificationContentItemReplyAmend' property='settingSystem(libraryNotificationContentItemReplyAmend)' />
        <html:hidden styleId='user_libraryNotificationContentItemReplyAmend' property='settingUser(libraryNotificationContentItemReplyAmend)' />
      </logic:notEmpty>

      <imhtml:iframe style='border: 0px; width: 100%;' formName='settingForm' frameName='ivataSettingIFrame'>
          <igw:bean id='iFrameScript'><imhtml:rewrite page='/setting/include/script/iFrame.jsp'/></igw:bean>
          <script language='javascript' src='<%=iFrameScript%>'></script>

          <logic:notEmpty name='settingForm' property='settingType(libraryNotificationContentComment)'>
            <table width='100%' height='17%' class='hilight'>
              <tr>
                <td class='fieldCaption'><imhtml:label fieldName='libraryNotificationContentComment'/></td>
              </tr>
              <tr height='100%'>
                <td colspan='5' class='field'>
                  <imhtml:textarea rows='5' cols='40' mandatory='true' fieldName='libraryNotificationContentComment' property='setting(libraryNotificationContentComment)' onchange="onFieldChangeIFrame('libraryNotificationContentComment')"/>
                </td>
              </tr>
            </table>
          </logic:notEmpty>

          <logic:notEmpty name='settingForm' property='settingType(libraryNotificationContentCommentAmend)'>
            <table width='100%' height='17%' class='hilight'>
              <tr>
                <td class='fieldCaption'><imhtml:label fieldName='libraryNotificationContentCommentAmend'/></td>
              </tr>
              <tr height='100%'>
                <td colspan='5' class='field'>
                  <imhtml:textarea rows='5' cols='40' mandatory='true' fieldName='libraryNotificationContentCommentAmend' property='setting(libraryNotificationContentCommentAmend)' onchange="onFieldChangeIFrame('libraryNotificationContentCommentAmend')"/>
                </td>
              </tr>
            </table>
          </logic:notEmpty>

          <logic:notEmpty name='settingForm' property='settingType(libraryNotificationContentItem)'>
            <table width='100%' height='17%' class='hilight'>
              <tr>
                <td class='fieldCaption'><imhtml:label fieldName='libraryNotificationContentItem'/></td>
              </tr>
              <tr height='100%'>
                <td colspan='5' class='field'>
                  <imhtml:textarea rows='5' cols='40' mandatory='true' fieldName='libraryNotificationContentItem' property='setting(libraryNotificationContentItem)' onchange="onFieldChangeIFrame('libraryNotificationContentItem')"/>
                </td>
              </tr>
            </table>
          </logic:notEmpty>

          <logic:notEmpty name='settingForm' property='settingType(libraryNotificationContentItemAmend)'>
            <table width='100%' height='17%' class='hilight'>
              <tr>
                <td class='fieldCaption'><imhtml:label fieldName='libraryNotificationContentItemAmend'/></td>
              </tr>
              <tr height='100%'>
                <td colspan='5' class='field'>
                  <imhtml:textarea rows='5' cols='40' mandatory='true' fieldName='libraryNotificationContentItemAmend' property='setting(libraryNotificationContentItemAmend)' onchange="onFieldChangeIFrame('libraryNotificationContentItemAmend')"/>
                </td>
              </tr>
            </table>
          </logic:notEmpty>

          <logic:notEmpty name='settingForm' property='settingType(libraryNotificationContentItemReply)'>
            <table width='100%' height='17%' class='hilight'>
              <tr>
                <td class='fieldCaption'><imhtml:label fieldName='libraryNotificationContentItemReply'/></td>
              </tr>
              <tr height='100%'>
                <td colspan='5' class='field'>
                  <imhtml:textarea rows='5' cols='40' mandatory='true' fieldName='libraryNotificationContentItemReply' property='setting(libraryNotificationContentItemReply)' onchange="onFieldChangeIFrame('libraryNotificationContentItemReply')"/>
                </td>
              </tr>
            </table>
          </logic:notEmpty>

          <logic:notEmpty name='settingForm' property='settingType(libraryNotificationContentItemReplyAmend)'>
            <table width='100%' height='17%' class='hilight'>
              <tr>
                <td class='fieldCaption'><imhtml:label fieldName='libraryNotificationContentItemReplyAmend'/></td>
              </tr>
              <tr height='100%'>
                <td colspan='5' class='field'>
                  <imhtml:textarea rows='5' cols='40' mandatory='true' fieldName='libraryNotificationContentItemReplyAmend' property='setting(libraryNotificationContentItemReplyAmend)' onchange="onFieldChangeIFrame('libraryNotificationContentItemReplyAmend')" />
                </td>
              </tr>
            </table>
          </logic:notEmpty>

      </imhtml:iframe>

    </imtheme:framePane>
  </imtheme:frame>

</imhtml:form>