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
<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: library.jsp,v 1.3 2005/04/10 20:10:10 colinmacleod Exp $
//
// Preferences for library subsystem.
//
// Since: ivata groupware 0.9 (2002-07-01)
// Author: Peter Illes
// $Revision: 1.3 $
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
// $Log: library.jsp,v $
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
// Revision 1.7  2003/06/10 06:03:13  peter
// img and html:img replaced with size-aware imhtml:img
//
// Revision 1.6  2003/03/05 11:24:27  peter
// resourceFieldPath in forms
//
// Revision 1.5  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.4  2003/02/25 07:30:55  colin
// restructured java file paths
//
// Revision 1.3  2003/02/24 15:39:22  peter
// converted to imhtml, added empty images to textareas to keep their minimal height
//
// Revision 1.2  2003/02/14 17:07:14  peter
// started to convert to imhtml
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

<igw:bean id='formatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter'/>
<igw:bean id='nonBreakingSpaces' scope='page' type='com.ivata.mask.web.format.NonBreakingSpaceFormat'/>
<imformat:addFormat formatter='<%=formatter%>' format='<%=nonBreakingSpaces%>'/>

<imhtml:form refersTo='ivataSettingForm' resourceFieldPath='setting'>
  <imtheme:frame>
    <imtheme:framePane>
      <table border='0' cellpadding='0' cellspacing='0' width='100%'>
        <tr>
          <td colspan='2'/>
            <logic:notEmpty name='settingForm' property='administrator'>
              <td><imhtml:img page='/setting/images/override.gif' /></td>
            </logic:notEmpty>
        </tr>

        <logic:notEmpty name='settingForm' property='settingType(libraryHome)'>
          <tr>
            <td class='fieldCaption'><imhtml:label fieldName='libraryHome'/></td>
            <td class='field'>
              <html:hidden styleId='system_libraryHome' property='settingSystem(libraryHome)' />
              <html:hidden styleId='user_libraryHome' property='settingUser(libraryHome)' />
              <imhtml:text fieldName='libraryHome' property='setting(libraryHome)' mandatory='true' /></td>
            <td>
              <logic:notEmpty name='settingForm' property='administrator'>
                <imhtml:checkbox fieldName='override' bundle='admin' property='settingOverride(libraryHome)' />
              </logic:notEmpty>
            </td>
            <td>
              <imhtml:img page='/setting/images/revert.gif' bundle='admin'
                    titleKey='index.img.revertToSystem.title'
                    onclick="revertToSystem('libraryHome')" />
            </td>
          </tr>
        </logic:notEmpty>

        <logic:notEmpty name='settingForm' property='settingType(libraryRecent)'>
          <tr>
            <td class='fieldCaption'><imhtml:label fieldName='libraryRecent'/></td>
            <td class='field'>
              <html:hidden styleId='system_libraryRecent' property='settingSystem(libraryRecent)' />
              <html:hidden styleId='user_libraryRecent' property='settingUser(libraryRecent)' />
              <imhtml:text fieldName='libraryRecent' property='setting(libraryRecent)' mandatory='true' />
            </td>
            <td>
              <logic:notEmpty name='settingForm' property='administrator'>
                <imhtml:checkbox fieldName='override' bundle='admin' property='settingOverride(libraryRecent)' />
              </logic:notEmpty>
            </td>
            <td>
              <imhtml:img page='/setting/images/revert.gif' bundle='admin'
                    titleKey='index.img.revertToSystem.title'
                    onclick="revertToSystem('libraryRecent')" />
            </td>
          </tr>
        </logic:notEmpty>

        <logic:notEmpty name='settingForm' property='settingType(libraryCommentSpacer)'>
          <tr>
            <td class='fieldCaption'><imhtml:label fieldName='libraryCommentSpacer'/></td>
            <td class='field'>
              <html:hidden styleId='system_libraryCommentSpacer' property='settingSystem(libraryCommentSpacer)' />
              <html:hidden styleId='user_libraryCommentSpacer' property='settingUser(libraryCommentSpacer)' />
              <imhtml:text fieldName='libraryCommentSpacer' property='setting(libraryCommentSpacer)' mandatory='true' />
            </td>
            <td>
              <logic:notEmpty name='settingForm' property='administrator'>
                <imhtml:checkbox fieldName='override' bundle='admin' property='settingOverride(libraryCommentSpacer)' />
              </logic:notEmpty>
            </td>
            <td>
              <imhtml:img page='/setting/images/revert.gif' bundle='admin'
                    titleKey='index.img.revertToSystem.title'
                    onclick="revertToSystem('libraryCommentSpacer')" />
            </td>
          </tr>
        </logic:notEmpty>

      </table>
    </imtheme:framePane>
  </imtheme:frame>
</imhtml:form>