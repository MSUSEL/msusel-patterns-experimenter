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

<%@page import='com.ivata.mask.web.format.FormatConstants' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: email.jsp,v 1.4 2005/04/28 18:47:08 colinmacleod Exp $
//
// Preferences which control the email connection.
//
// Since: ivata groupware 0.9 (2002-07-01)
// Author: Colin MacLeod <colin.macleod@ivata.com>
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
// $Log: email.jsp,v $
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
// Revision 1.7  2004/11/12 15:57:12  colinmacleod
// Removed dependencies on SSLEXT.
// Moved Persistence classes to ivata masks.
//
// Revision 1.6  2004/11/03 15:53:20  colinmacleod
// Changed todo comments to TODO: all caps.
//
// Revision 1.5  2004/07/14 22:50:23  colinmacleod
// Replaced bean:define with extended igw:bean.
//
// Revision 1.4  2004/07/14 20:59:52  colinmacleod
// Changed all occurrences of jsp:useBean to igw:bean (better tomcat compatibility).
//
// Revision 1.3  2004/03/21 21:16:24  colinmacleod
// Shortened name to ivata op.
//
// Revision 1.2  2004/03/07 23:00:18  colinmacleod
// Fixed bug in checkbox properties.
//
// Revision 1.1  2004/03/03 21:32:34  colinmacleod
// Moved core files to core subproject
//
// Revision 1.1.1.1  2004/01/27 20:58:17  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.2  2003/10/17 12:36:13  jano
// fixing problems with building
// converting intranet -> portal
// Eclipse building
//
// Revision 1.1.1.1  2003/10/13 20:50:51  colin
// Restructured portal into subprojects
//
// Revision 1.14  2003/06/10 06:03:13  peter
// img and html:img replaced with size-aware imhtml:img
//
// Revision 1.13  2003/03/20 15:22:38  jano
// fixing textarea size for IE
//
// Revision 1.12  2003/03/14 09:06:49  peter
// fixed onChange handlers for signatures
//
// Revision 1.11  2003/03/05 17:59:15  peter
// fixed conditional display of used and system hidden fields in setting jsps put to iFrames
//
// Revision 1.10  2003/03/05 11:30:18  colin
// removed extra textareas
//
// Revision 1.9  2003/03/05 11:26:33  colin
// fixed text areas with 100%
//
// Revision 1.8  2003/03/05 11:01:19  peter
// styleIds for override checkboxes put back
//
// Revision 1.7  2003/03/04 14:26:38  colin
// added emailDefaultForward
//
// Revision 1.6  2003/03/03 16:57:13  colin
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
// Revision 1.3  2003/02/14 14:21:33  peter
// added emailSignature
//
// Revision 1.2  2003/02/12 14:24:09  peter
// now works both in user and administrator mode
//
// Revision 1.1  2003/01/29 09:18:23  peter
// added to cvs
//
// Revision 1.1  2003/01/27 13:04:07  colin
// very old version of settings to use as a basis
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<%@include file='/include/theme.jspf'%>

<igw:bean id="formatter" scope="page" type="com.ivata.mask.web.format.HTMLFormatter"/>
<igw:bean id="nonBreakingSpaces" scope="page" type="com.ivata.mask.web.format.NonBreakingSpaceFormat"/>
<imformat:addFormat formatter="<%=formatter%>" format="<%=nonBreakingSpaces%>"/>

<imhtml:form refersTo="ivataSettingForm" resourceFieldPath="setting">
  <imtheme:frame>
    <imtheme:framePane>

      <logic:notEmpty name="settingForm" property="settingType(emailHost)">
        <html:hidden styleId="system_emailHost" property="settingSystem(emailHost)"/>
        <html:hidden styleId="user_emailHost" property="settingUser(emailHost)"/>
      </logic:notEmpty>

      <logic:notEmpty name="settingForm" property="settingType(emailPort)">
        <html:hidden styleId="system_emailPort" property="settingSystem(emailPort)"/>
        <html:hidden styleId="user_emailPort" property="settingUser(emailPort)"/>
      </logic:notEmpty>

      <logic:notEmpty name="settingForm" property="settingType(emailHostSmtp)">
        <html:hidden styleId="system_emailHostSmtp" property="settingSystem(emailHostSmtp)"/>
        <html:hidden styleId="user_emailHostSmtp" property="settingUser(emailHostSmtp)"/>
      </logic:notEmpty>

      <logic:notEmpty name="settingForm" property="settingType(emailReplyIndent)">
        <html:hidden styleId="system_emailReplyIndent" property="settingSystem(emailReplyIndent)"/>
        <html:hidden styleId="user_emailReplyIndent" property="settingUser(emailReplyIndent)"/>
      </logic:notEmpty>

      <logic:notEmpty name="settingForm" property="settingType(emailFolderInbox)">
        <html:hidden styleId="system_emailFolderInbox" property="settingSystem(emailFolderInbox)"/>
        <html:hidden styleId="user_emailFolderInbox" property="settingUser(emailFolderInbox)"/>
      </logic:notEmpty>

      <logic:notEmpty name="settingForm" property="settingType(emailFolderSent)">
        <html:hidden styleId="system_emailFolderSent" property="settingSystem(emailFolderSent)"/>
        <html:hidden styleId="user_emailFolderSent" property="settingUser(emailFolderSent)"/>
      </logic:notEmpty>

      <logic:notEmpty name="settingForm" property="settingType(emailFolderDrafts)">
        <html:hidden styleId="system_emailFolderDrafts" property="settingSystem(emailFolderDrafts)"/>
        <html:hidden styleId="user_emailFolderDrafts" property="settingUser(emailFolderDrafts)"/>
      </logic:notEmpty>

      <logic:notEmpty name="settingForm" property="settingType(emailFolderTrash)">
        <html:hidden styleId="system_emailFolderTrash" property="settingSystem(emailFolderTrash)"/>
        <html:hidden styleId="user_emailFolderTrash" property="settingUser(emailFolderTrash)"/>
      </logic:notEmpty>

      <logic:notEmpty name="settingForm" property="settingType(emailDefaultFormat)">
        <html:hidden styleId="system_emailDefaultFormat" property="settingSystem(emailDefaultFormat)"/>
        <html:hidden styleId="user_emailDefaultFormat" property="settingUser(emailDefaultFormat)"/>
      </logic:notEmpty>

      <logic:notEmpty name="settingForm" property="settingType(emailSignatureForward)">
        <html:hidden styleId="system_emailSignatureForward" property="settingSystem(emailSignatureForward)"/>
        <html:hidden styleId="user_emailSignatureForward" property="settingUser(emailSignatureForward)"/>
      </logic:notEmpty>

      <logic:notEmpty name="settingForm" property="settingType(emailSignatureReply)">
        <html:hidden styleId="system_emailSignatureReply" property="settingSystem(emailSignatureReply)"/>
        <html:hidden styleId="user_emailSignatureReply" property="settingUser(emailSignatureReply)"/>
      </logic:notEmpty>

      <logic:notEmpty name="settingForm" property="settingType(emailSignatureHtml)">
        <html:hidden styleId="system_emailSignatureHtml" property="settingSystem(emailSignatureHtml)"/>
        <html:hidden styleId="user_emailSignatureHtml" property="settingUser(emailSignatureHtml)"/>
      </logic:notEmpty>

      <logic:notEmpty name="settingForm" property="settingType(emailSignatureText)">
        <html:hidden styleId="system_emailSignatureText" property="settingSystem(emailSignatureText)"/>
        <html:hidden styleId="user_emailSignatureText" property="settingUser(emailSignatureText)"/>
      </logic:notEmpty>

      <imhtml:iframe style="border: 0px;" formName="settingForm" frameName="ivataSettingIFrame" width="100%" height="100%" align="block">
        <igw:bean id="iFrameScript">
          <imhtml:rewrite page="/setting/include/script/iFrame.jsp"/>
        </igw:bean>
        <script language="javascript" src="<%=iFrameScript%>"></script>

        <table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%" class="hilight">
          <tr>
            <td colspan="2">&nbsp;</td>
            <logic:notEmpty name="settingForm" property="administrator">
              <td colspan="2">
                <imhtml:img page="/setting/images/override.gif"/>
              </td>
            </logic:notEmpty>
          </tr>

          <logic:notEmpty name="settingForm" property="settingType(emailHost)">
            <tr>
              <td class="fieldCaption">
                <imhtml:label fieldName="emailHost"/>
              </td>
              <td class="field">
                <imhtml:text fieldName="emailHost" property="setting(emailHost)" mandatory="true" onchange="onFieldChangeIFrame('emailHost')"/>
              </td>
              <td width="1">
                <html:img page="/images/empty.gif" height="21"/>
              </td>
            </tr>
          </logic:notEmpty>

          <logic:notEmpty name="settingForm" property="settingType(emailPort)">
            <tr>
              <td class="fieldCaption">
                <imhtml:label fieldName="emailPort"/>
              </td>
              <td class="field">
                <imhtml:text fieldName="emailPort" property="setting(emailPort)" mandatory="true" onchange="onFieldChangeIFrame('emailPort')"/>
              </td>
              <td width="1">
                <html:img page="/images/empty.gif" height="21"/>
              </td>
            </tr>
          </logic:notEmpty>

          <logic:notEmpty name="settingForm" property="settingType(emailHostSmtp)">
            <tr>
              <td class="fieldCaption">
                <imhtml:label fieldName="emailHostSmtp"/>
              </td>
              <td class="field">
                <imhtml:text fieldName="emailHostSmtp" property="setting(emailHostSmtp)" mandatory="true" onchange="onFieldChangeIFrame('emailHostSmtp')"/>
              </td>
              <td width="1">
                <html:img page="/images/empty.gif" height="21"/>
              </td>
            </tr>
          </logic:notEmpty>

          <logic:notEmpty name="settingForm" property="settingType(emailReplyIndent)">
            <tr>
              <td class="fieldCaption">
                <imhtml:label fieldName="emailReplyIndent"/>
              </td>
              <td class="field">
                <imhtml:text fieldName="emailReplyIndent" property="setting(emailReplyIndent)" mandatory="true" onchange="onFieldChangeIFrame('emailReplyIndent')"/>
              </td>
              <td width="1">
                <logic:notEmpty name="settingForm" property="administrator">
                  <imhtml:checkbox fieldName="override" styleId="override_emailReplyIndent" property="settingOverride(emailReplyIndent)"/>
                </logic:notEmpty>
              </td>
              <td width="1">
                <imhtml:img page="/setting/images/revert.gif" bundle="admin" titleKey="index.img.revertToSystem.title" onclick="revertToSystemIFrame('emailReplyIndent')"/>
              </td>
            </tr>
          </logic:notEmpty>

          <logic:notEmpty name="settingForm" property="settingType(emailFolderInbox)">
            <tr>
              <td class="fieldCaption">
                <imhtml:label fieldName="emailFolderInbox"/>
              </td>
              <td class="field">
                <imhtml:text fieldName="emailFolderInbox" property="setting(emailFolderInbox)" mandatory="true" onchange="onFieldChangeIFrame('emailFolderInbox')"/>
              </td>
              <td width="1">
                <logic:notEmpty name="settingForm" property="administrator">
                  <imhtml:checkbox fieldName="override" styleId="override_emailFolderInbox" property="settingOverride(emailFolderInbox)"/>
                </logic:notEmpty>
              </td>
              <td width="1">
                <imhtml:img page="/setting/images/revert.gif" bundle="admin" titleKey="index.img.revertToSystem.title" onclick="revertToSystemIFrame('emailFolderInbox')"/>
              </td>
            </tr>
          </logic:notEmpty>

          <logic:notEmpty name="settingForm" property="settingType(emailFolderSent)">
            <tr>
              <td class="fieldCaption">
                <imhtml:label fieldName="emailFolderSent"/>
              </td>
              <td class="field">
                <imhtml:text fieldName="emailFolderSent" property="setting(emailFolderSent)" mandatory="true" onchange="onFieldChangeIFrame('emailFolderSent')"/>
              </td>
              <td width="1">
                <logic:notEmpty name="settingForm" property="administrator">
                  <imhtml:checkbox fieldName="override" styleId="override_emailFolderSent" property="settingOverride(emailFolderSent)"/>
                </logic:notEmpty>
              </td>
              <td width="1">
                <imhtml:img page="/setting/images/revert.gif" bundle="admin" titleKey="index.img.revertToSystem.title" onclick="revertToSystemIFrame('emailFolderSent')"/>
              </td>
            </tr>
          </logic:notEmpty>

          <logic:notEmpty name="settingForm" property="settingType(emailFolderDrafts)">
            <tr>
              <td class="fieldCaption">
                <imhtml:label fieldName="emailFolderDrafts"/>
              </td>
              <td class="field">
                <imhtml:text fieldName="emailFolderDrafts" property="setting(emailFolderDrafts)" mandatory="true" onchange="onFieldChangeIFrame('emailFolderDrafts')"/>
              </td>
              <td width="1">
                <logic:notEmpty name="settingForm" property="administrator">
                  <imhtml:checkbox fieldName="override" styleId="override_emailFolderDrafts" property="settingOverride(emailFolderDrafts)"/>
                </logic:notEmpty>
              </td>
              <td width="1">
                <imhtml:img page="/setting/images/revert.gif" bundle="admin" titleKey="index.img.revertToSystem.title" onclick="revertToSystemIFrame('emailFolderDrafts')"/>
              </td>
            </tr>
          </logic:notEmpty>

          <logic:notEmpty name="settingForm" property="settingType(emailFolderTrash)">
            <tr>
              <td class="fieldCaption">
                <imhtml:label fieldName="emailFolderTrash"/>
              </td>
              <td class="field">
                <imhtml:text fieldName="emailFolderTrash" property="setting(emailFolderTrash)" mandatory="true" onchange="onFieldChangeIFrame('emailFolderTrash')"/>
              </td>
              <td width="1">
                <logic:notEmpty name="settingForm" property="administrator">
                  <imhtml:checkbox fieldName="override" styleId="override_emailFolderTrash" property="settingOverride(emailFolderTrash)"/>
                </logic:notEmpty>
              </td>
              <td width="1">
                <imhtml:img page="/setting/images/revert.gif" bundle="admin" titleKey="index.img.revertToSystem.title" onclick="revertToSystemIFrame('emailFolderTrash')"/>
              </td>
            </tr>
          </logic:notEmpty>

          <logic:notEmpty name="settingForm" property="settingType(emailDefaultFormat)">
            <tr>
              <td class="fieldCaption">
                <imhtml:label fieldName="emailDefaultFormat"/>
              </td>
              <td class="field">
                <imhtml:select fieldName="emailDefaultFormat" property="setting(emailDefaultFormat)" mandatory="true" onchange="onFieldChangeIFrame('emailDefaultFormat')">
                  <imhtml:option value="<%=new Integer(FormatConstants.FORMAT_TEXT).toString()%>">
                    <bean:message key="html.format.constant.text"/>
                  </imhtml:option>
                  <imhtml:option value="<%=new Integer(FormatConstants.FORMAT_HTML).toString()%>">
                    <bean:message key="html.format.constant.html"/>
                  </imhtml:option>
                </imhtml:select>
              </td>
              <td width="1">
                <logic:notEmpty name="settingForm" property="administrator">
                  <imhtml:checkbox fieldName="override" styleId="override_emailDefaultFormat" property="settingOverride(emailDefaultFormat)"/>
                </logic:notEmpty>
              </td>
              <td width="1">
                <imhtml:img page="/setting/images/revert.gif" bundle="admin" titleKey="index.img.revertToSystem.title" onclick="revertToSystemIFrame('emailDefaultFormat')"/>
              </td>
            </tr>
          </logic:notEmpty>

          <logic:notEmpty name="settingForm" property="settingType(emailSignatureForward)">
            <tr>
              <td class="fieldCaption">&nbsp;</td>
              <td class="field">
                <imhtml:checkbox fieldName="emailSignatureForward" property="setting(emailSignatureForward)" onchange="onFieldChangeIFrame('emailSignatureForward')"/>
              </td>
              <td>
                <logic:notEmpty name="settingForm" property="administrator">
                  <imhtml:checkbox fieldName="override" styleId="override_emailSignatureForward" property="settingOverride(emailSignatureForward)"/>
                </logic:notEmpty>
              </td>
              <td width="1">
                <imhtml:img page="/setting/images/revert.gif" bundle="admin" titleKey="index.img.revertToSystem.title" onclick="revertToSystemIFrame('emailSignatureForward')"/>
              </td>
            </tr>
          </logic:notEmpty>

          <logic:notEmpty name="settingForm" property="settingType(emailSignatureReply)">
            <tr>
              <td class="fieldCaption">&nbsp;</td>
              <td class="field">
                <imhtml:checkbox fieldName="emailSignatureReply" property="setting(emailSignatureReply)" onchange="onFieldChangeIFrame('emailSignatureReply')"/>
              </td>
              <td>
                <logic:notEmpty name="settingForm" property="administrator">
                  <imhtml:checkbox fieldName="override" styleId="override_emailSignatureReply" property="settingOverride(emailSignatureReply)"/>
                </logic:notEmpty>
              </td>
              <td>
                <imhtml:img page="/setting/images/revert.gif" bundle="admin" titleKey="index.img.revertToSystem.title" onclick="revertToSystemIFrame('emailSignatureReply')"/>
              </td>
            </tr>
          </logic:notEmpty>

          <logic:notEmpty name="settingForm" property="settingType(emailSignatureHtml)">
            <tr>
              <td colspan="4">
                <table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
                  <tr>
                    <td colspan="2" class="fieldCaption" style="width: 100%; vertical-align: bottom;">
                      <imhtml:label fieldName="emailSignatureHtml"/>
                    </td>
                    <td width="1" style="vertical-align: bottom">
                      <logic:notEmpty name="settingForm" property="administrator">
                        <html:img page="/images/empty.gif" height="1" width="24"/>
                        <imhtml:checkbox fieldName="override" styleId="override_emailSignatureHtml" property="settingOverride(emailSignatureHtml)"/>
                      </logic:notEmpty>
                    </td>
                    <td width="1" style="vertical-align: bottom">
                      <imhtml:img page="/setting/images/revert.gif" bundle="admin" titleKey="index.img.revertToSystem.title" onclick="revertToSystem('emailSignatureHtml')"/>
                    </td>
                  </tr>
                  <tr>
                    <td colspan="4" class="field">
                      <imhtml:textarea rows="5" cols="40" fieldName="emailSignatureHtml" property="setting(emailSignatureHtml)" mandatory="true" onchange="onFieldChangeIFrame('emailSignatureHtml')"/>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </logic:notEmpty>

          <logic:notEmpty name="settingForm" property="settingType(emailSignatureText)">
            <tr>
              <td colspan="4">
                <table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
                  <tr>
                    <td colspan="2" class="fieldCaption" style="width: 100%; vertical-align: bottom;">
                      <imhtml:label fieldName="emailSignatureText"/>
                    </td>
                    <td width="1" style="vertical-align: bottom">
                      <logic:notEmpty name="settingForm" property="administrator">
                        <html:img page="/images/empty.gif" height="1" width="24"/>
                        <imhtml:checkbox fieldName="override" styleId="override_emailSignatureText" property="settingOverride(emailSignatureText)"/>
                      </logic:notEmpty>
                    </td>
                    <td width="1" style="vertical-align: bottom">
                      <imhtml:img page="/setting/images/revert.gif" bundle="admin" titleKey="index.img.revertToSystem.title" onclick="revertToSystem('emailSignatureText')"/>
                    </td>
                  </tr>
                  <tr>
                    <td colspan="4" class="field">
                      <imhtml:textarea rows="5" cols="40" fieldName="emailSignatureText" property="setting(emailSignatureText)" mandatory="true" onchange="onFieldChangeIFrame('emailSignatureText')"/>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </logic:notEmpty>
        </table>
      </imhtml:iframe>
    </imtheme:framePane>
  </imtheme:frame>
</imhtml:form>
