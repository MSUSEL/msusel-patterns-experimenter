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
<%@page import="com.ivata.groupware.admin.setting.struts.*" %>
<%@page import="java.util.*" %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: index.jsp,v 1.4 2005/04/28 18:47:08 colinmacleod Exp $
//
// <p>Maintain your user preferences and site settings.</p>
//
// Since: ivata groupware 0.9 (2002-06-28)
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
// $Log: index.jsp,v $
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
// Revision 1.2  2004/02/10 22:09:28  colinmacleod
// Turned off SSL
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
// Revision 1.16  2003/06/10 06:03:13  peter
// img and html:img replaced with size-aware imhtml:img
//
// Revision 1.15  2003/04/15 12:48:26  peter
// fixed helpKey
//
// Revision 1.14  2003/03/05 11:20:06  peter
// put a condition to copyField for checkboxes
//
// Revision 1.13  2003/03/04 14:26:58  colin
// moved window.resizeTo (now in head tag)
//
// Revision 1.12  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.11  2003/02/25 17:29:09  peter
// fixed the tab include
//
// Revision 1.10  2003/02/25 08:55:03  peter
// fixed the buttons, added help
//
// Revision 1.9  2003/02/25 07:30:55  colin
// restructured java file paths
//
// Revision 1.8  2003/02/19 18:17:05  peter
// fixed window size and text input titles
//
// Revision 1.7  2003/02/14 17:07:14  peter
// started to convert to imhtml
//
// Revision 1.6  2003/02/12 14:24:09  peter
// now works both in user and administrator mode
//
// Revision 1.5  2003/02/06 16:49:31  peter
// tidied up
//
// Revision 1.4  2003/02/04 10:26:42  peter
// javaScript finished
//
// Revision 1.3  2003/02/03 17:36:15  peter
// works on JavaScript
//
// Revision 1.2  2003/01/30 16:46:18  peter
// javascript for reverting to system values added
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

<%-- these are used to see if the year matches this year --%>
<igw:bean id='settingsTab' scope='session' type='java.lang.String'/>
<igw:bean id='settingTabUser' type='java.util.Vector'/>

<% SettingForm settingForm = (SettingForm) session.getAttribute("settingForm"); %>
<%-- if there is no setting form in the session, forward to the index action --%>
<c:if test='<%=(settingForm == null)%>'>
  <jsp:forward page='/setting/index.action'/>
</c:if>

<%
   int tabNumber = settingForm.getSettingTab_activeTab().intValue();
%>


<igw:bean id='tabName'>
  <%= (String) settingForm.getTabName().get(tabNumber) %>
</igw:bean>




<%-- the functions that maintain all the actions in the page --%>
<igw:bean id='javaScriptSource'>

  <%-- the arrays of elementId patterns used in more functions --%>
  var systemFieldNames = new Array(<%= settingForm.getSettingTabSystemFields() %>);
  var userFieldNames = new Array(<%= settingForm.getSettingTabUserFields() %>);
  var allFieldNames = catenateArrays(userFieldNames, systemFieldNames);

  function catenateArrays(array1, array2) {
    var i;
    var counter = 0;
    var newArray = new Array(array1.length + array2.length);
    for (i=0; i<array1.length; i++, counter++) {
      newArray[counter]=array1[i];
    }
    for (i=0; i<array2.length; i++, counter++) {
      newArray[counter]=array2[i];
    }
    return newArray;
  }



  <%-- helper function, copies value of source element to target element --%>
  function copyField(sourceField, targetField) {
    <%-- both input fields are checkboxes --%>
    if (sourceField.type == 'checkbox' && targetField.type == 'checkbox') {
      targetField.checked = sourceField.checked;
    <%-- one of the input fields is a checkbox --%>
    } else if (sourceField.type == 'checkbox' || targetField.type == 'checkbox') {
      if (sourceField.type == 'checkbox') {
        if (sourceField.checked) {
          targetField.value = sourceField.value;
        } else {
          targetField.value = '';
        }
      } else {
        if (sourceField.value == '' || sourceField.value == 'false') {
          targetField.checked = false;
        } else {
          targetField.checked = true;
        }
      }
    <%-- no one is a checkbox --%>
    } else {
      targetField.value = sourceField.value;
    }
  }



  <%-- the function returns the settingArea that can be "user" or "system" from
        testing the states of the settingArea radio buttons --%>
  function detectSettingArea() {
    var radioUser = document.getElementById("radioUser");
    if (radioUser == null) {
      return "user";
    } else {
      if (radioUser.checked==true) {
        return "user";
      } else {
        return "system";
      }
    }
  }



  <%--the function is called whenever the user changes a field.
      the contents are immediately copied to the appropriate hidden field --%>
  function onFieldChange(fieldName) {
    var mode = detectSettingArea();
    var visibleField = document.getElementById(fieldName);
    var hiddenField = document.getElementById(mode + "_" + fieldName);
    copyField(visibleField, hiddenField);
  }



  <%--the function is called when the user clicks on the revert button, the
      the system setting is copied to the user --%>
  function revertToSystem(fieldName) {
    var visibleField = document.getElementById(fieldName);
    var hiddenSystemField = document.getElementById("system_" + fieldName);
    var hiddenUserField = document.getElementById("user_" + fieldName);
    copyField(hiddenSystemField, hiddenUserField);
    <%-- show him the setting, when he is in user area --%>
    if (detectSettingArea()=='user') {
      copyField(hiddenUserField, visibleField);
    }
  }



  <%-- the function goes through all the fields on the tab and sets user or
      system values to the visible fields depending on settingArea parameter
      that can have values "user" or "system" --%>
  function showSettings(mode) {
    var i;

    <%-- look for iframe, we have to access the visible fields there... --%>
    if (window.length == 1) {

      if (mode=='system') {

        for (i=0; i<allFieldNames.length; i++) {
          copyField(document.getElementById(mode + "_" + allFieldNames[i]), window.frames[0].document.getElementById(allFieldNames[i]));
          window.frames[0].document.getElementById(allFieldNames[i]).disabled=false;
        }

      } else if (mode=='user') {
        <%-- disable all the fields that have no effect as user settings --%>
        for (i=0; i<systemFieldNames.length; i++) {
          copyField(document.getElementById("system_" + systemFieldNames[i]), window.frames[0].document.getElementById(systemFieldNames[i]));
          window.frames[0].document.getElementById(systemFieldNames[i]).disabled=true;
        }

        for (i=0; i<userFieldNames.length; i++) {
          copyField(document.getElementById("user_" + userFieldNames[i]), window.frames[0].document.getElementById(userFieldNames[i]));
        }
      }

    <%-- the same thing, for plain case, the visibles arent in an iframe --%>
    } else {

      if (mode=='system') {

        for (i=0; i<allFieldNames.length; i++) {
          copyField(document.getElementById(mode + "_" + allFieldNames[i]), document.getElementById(allFieldNames[i]));
          document.getElementById(allFieldNames[i]).disabled=false;
        }

      } else if (mode=='user') {
        <%-- disable all the fields that have no effect as user settings --%>
        for (i=0; i<systemFieldNames.length; i++) {
          copyField(document.getElementById("system_" + systemFieldNames[i]), document.getElementById(systemFieldNames[i]));
          document.getElementById(systemFieldNames[i]).disabled=true;
        }

        for (i=0; i<userFieldNames.length; i++) {
          copyField(document.getElementById("user_" + userFieldNames[i]), document.getElementById(userFieldNames[i]));
        }
      }
    }
  }



  <%-- the function saves the values from the visible fields to the hidden ones
    depending on settingArea parameter that can have values "user" or "system"
  --%>
  function saveSettings(mode) {
    var i;

    <%-- look for iframe, we have to access the visible fields there... --%>
    if (window.length == 1) {
      if (mode=='system') {
        for (i=0; i<allFieldNames.length; i++) {
          copyField(window.frames[0].document.getElementById(allFieldNames[i]), document.getElementById("system_" + allFieldNames[i]));
        }
      } else if (mode=='user') {
        <%-- copy the values of visible fields to the user hidden fields --%>
        for (i=0; i<userFieldNames.length; i++) {
          copyField(window.frames[0].document.getElementById(userFieldNames[i]), document.getElementById("user_" + userFieldNames[i]));
        }
      }

      <%-- we always want to have the system-only field values in the corresponding user ones too --%>
      for (i=0; i<systemFieldNames.length; i++) {
        copyField(document.getElementById("system_" + systemFieldNames[i]), document.getElementById("user_" + systemFieldNames[i]));
      }


    <%-- the same thing, for plain case, the visibles arent in an iframe --%>
    } else {
      if (mode=='system') {
        for (i=0; i<allFieldNames.length; i++) {
          copyField(document.getElementById(allFieldNames[i]), document.getElementById("system_" + allFieldNames[i]));
        }
      } else if (mode=='user') {
        <%-- copy the values of visible fields to the user hidden fields --%>
        for (i=0; i<userFieldNames.length; i++) {
          copyField(document.getElementById(userFieldNames[i]), document.getElementById("user_" + userFieldNames[i]));
        }
      }

      <%-- we always want to have the system-only field values in the corresponding user ones too --%>
      for (i=0; i<systemFieldNames.length; i++) {
        copyField(document.getElementById("system_" + systemFieldNames[i]), document.getElementById("user_" + systemFieldNames[i]));
      }
    }
  }

</igw:bean>

<igw:checkLogin />
<imhtml:html locale='true'>
  <igw:head bundle='admin' titleKey='index.title' javaScript='<%= javaScriptSource %>'
    topLevel='true'>
    <imhtml:base/>
  </igw:head>

  <body class='dialog' onLoad="showSettings('<bean:write name='settingForm' property='settingArea' />')">

    <imtheme:window>
      <imhtml:form action='/setting/index' method='post' referralName='ivataSettingForm'
                                onsubmit="saveSettings(detectSettingArea())" bundle='admin' resourceFieldPath='index'>

          <%@include file='/include/errorFrame.jspf'%>

          <imtheme:tabFrame>
            <imtheme:tabControl name='settingTab' formName='document.settingForm'>
              <logic:iterate name='settingForm' property='tabName' id='tabNameString' type='java.lang.String'>
                <imtheme:tab><bean:message bundle='admin' key='<%= "index.tab." + tabNameString %>' /></imtheme:tab>
              </logic:iterate>
            </imtheme:tabControl>
            <imtheme:tabContent>


              <jsp:include page='<%="/setting/" + tabName + ".jsp" %>'/>


            </imtheme:tabContent>
          </imtheme:tabFrame>
          <imtheme:buttonFrame>
            <table width='100%' cellpadding='0' cellspacing='0' border='0'>
              <tr>
                <logic:notEmpty name='settingForm' property='administrator'>
                  <td align='left'>
                    <igw:bean id='systemTitle'><bean:message bundle='admin' key='field.system.title'/></igw:bean>
                    <input type='radio' name='settingArea' value='system'
                        title='<%=systemTitle%>'
                        id='radioSystem' onchange="saveSettings('user'); showSettings('system');"
                        <%=HTMLFormatter.getBooleanAttribute("checked", settingForm.getSettingArea().equals("system"))%>/>
                    <imhtml:img page='/setting/images/system.gif'
                      title='<%=systemTitle%>'
                      onclick="if (detectSettingArea()=='user') {document.getElementById('radioSystem').checked='checked'; saveSettings('user'); showSettings('system');}" />

                    <igw:bean id='userTitle'><bean:message bundle='admin' key='field.user.title'/></igw:bean>
                    <input type='radio' name='settingArea' value='user' id='radioUser'
                        title='<%=userTitle%>'
                        onchange="saveSettings('system'); showSettings('user');"
                        <%=HTMLFormatter.getBooleanAttribute("checked", settingForm.getSettingArea().equals("user"))%>/>
                    <imhtml:img page='/setting/images/user.gif'
                      title='<%=userTitle%>'
                      onclick="if (detectSettingArea()=='system') {document.getElementById('radioUser').checked='checked'; saveSettings('system'); showSettings('user');}" />
                  </td>
                </logic:notEmpty>
                <td width='100%' align='right'>
                  <imhtml:ok />
                  <imhtml:cancel />
                  <imhtml:apply />
                  <imhtml:help key='setting' />
                </td>
              </tr>
            </table>
          </imtheme:buttonFrame>

      </imhtml:form>
    </imtheme:window>
  </body>
  <%-- fixes the buttons in Mozilla & co. --%>
  <%@include file='/include/script/fixPopUp.jspf'%>
</imhtml:html>
