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
<%@page import="com.ivata.groupware.navigation.menu.MenuConstants" %>
<%@page import="com.ivata.groupware.business.calendar.struts.IndexFormConstants" %>

<%@page import="java.util.*" %>
<%@page import="org.apache.struts.Globals" %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: dateTime.jsp,v 1.4 2005/04/28 18:47:08 colinmacleod Exp $
//
// Preferences for calendar : Timezone and date/time formats.
//
// Since: ivata groupware 0.9 (2003-01-29)
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
// $Log: dateTime.jsp,v $
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
// Revision 1.6  2004/11/12 15:57:11  colinmacleod
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
// Revision 1.18  2003/06/10 06:03:13  peter
// img and html:img replaced with size-aware imhtml:img
//
// Revision 1.17  2003/03/13 07:08:31  peter
// fixed format template selectors with missing visible info
//
// Revision 1.16  2003/03/05 17:59:14  peter
// fixed conditional display of used and system hidden fields in setting jsps put to iFrames
//
// Revision 1.15  2003/03/05 11:50:52  colin
// fixed fieldSmall -> fieldNarrow
//
// Revision 1.14  2003/03/05 11:49:14  colin
// changed field class to fieldNarrow
//
// Revision 1.13  2003/03/05 11:04:50  peter
// styleIds for override checkboxes put back
//
// Revision 1.12  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.11  2003/02/25 07:30:55  colin
// restructured java file paths
//
// Revision 1.10  2003/02/24 15:39:22  peter
// converted to imhtml, added empty images to textareas to keep their minimal height
//
// Revision 1.9  2003/02/21 15:06:13  peter
// fixed the constants for calendarDefaultView
//
// Revision 1.8  2003/02/19 18:17:05  peter
// fixed window size and text input titles
//
// Revision 1.7  2003/02/18 17:05:43  peter
// template selectors do work, format templates to be added
//
// Revision 1.6  2003/02/14 17:07:14  peter
// started to convert to imhtml
//
// Revision 1.5  2003/02/12 14:24:09  peter
// now works both in user and administrator mode
//
// Revision 1.4  2003/02/06 16:49:31  peter
// tidied up
//
// Revision 1.3  2003/02/04 10:27:33  peter
// functionality ok, more settings to be added
//
// Revision 1.2  2003/02/03 17:36:15  peter
// works on JavaScript
//
// Revision 1.1  2003/01/30 16:45:51  peter
// first , draft version
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

    <%--the pairs of hidden fields are here, they'll be always referred from
        the iframe as fields in the parent --%>
    <logic:notEmpty name='settingForm' property='settingType(i18nTimeZone)'>
      <html:hidden styleId='system_i18nTimeZone' property='settingSystem(i18nTimeZone)' />
      <html:hidden styleId='user_i18nTimeZone' property='settingUser(i18nTimeZone)' />
    </logic:notEmpty>

    <logic:notEmpty name='settingForm' property='settingType(calendarDefaultView)'>
      <html:hidden styleId='system_calendarDefaultView' property='settingSystem(calendarDefaultView)' />
      <html:hidden styleId='user_calendarDefaultView' property='settingUser(calendarDefaultView)' />
    </logic:notEmpty>

    <logic:notEmpty name='settingForm' property='settingType(calendarFirstWeekDay)'>
      <html:hidden styleId='system_calendarFirstWeekDay' property='settingSystem(calendarFirstWeekDay)' />
      <html:hidden styleId='user_calendarFirstWeekDay' property='settingUser(calendarFirstWeekDay)' />
    </logic:notEmpty>

    <logic:notEmpty name='settingForm' property='settingType(i18nDateInputDisplay)'>
      <html:hidden styleId='system_i18nDateInputDisplay' property='settingSystem(i18nDateInputDisplay)' />
      <html:hidden styleId='user_i18nDateInputDisplay' property='settingUser(i18nDateInputDisplay)' />
    </logic:notEmpty>

    <logic:notEmpty name='settingForm' property='settingType(i18nTimeInputDisplay)'>
      <html:hidden styleId='system_i18nTimeInputDisplay' property='settingSystem(i18nTimeInputDisplay)' />
      <html:hidden styleId='user_i18nTimeInputDisplay' property='settingUser(i18nTimeInputDisplay)' />
    </logic:notEmpty>

    <logic:notEmpty name='settingForm' property='settingType(i18nDateLong)'>
      <html:hidden styleId='system_i18nDateLong' property='settingSystem(i18nDateLong)' />
      <html:hidden styleId='user_i18nDateLong' property='settingUser(i18nDateLong)' />
    </logic:notEmpty>

    <logic:notEmpty name='settingForm' property='settingType(i18nDateLongDay)'>
      <html:hidden styleId='system_i18nDateLongDay' property='settingSystem(i18nDateLongDay)' />
      <html:hidden styleId='user_i18nDateLongDay' property='settingUser(i18nDateLongDay)' />
    </logic:notEmpty>

    <logic:notEmpty name='settingForm' property='settingType(i18nDateLongYear)'>
      <html:hidden styleId='system_i18nDateLongYear' property='settingSystem(i18nDateLongYear)' />
      <html:hidden styleId='user_i18nDateLongYear' property='settingUser(i18nDateLongYear)' />
    </logic:notEmpty>

    <logic:notEmpty name='settingForm' property='settingType(i18nDateShort)'>
      <html:hidden styleId='system_i18nDateShort' property='settingSystem(i18nDateShort)' />
      <html:hidden styleId='user_i18nDateShort' property='settingUser(i18nDateShort)' />
    </logic:notEmpty>

    <logic:notEmpty name='settingForm' property='settingType(i18nDateShortYear)'>
      <html:hidden styleId='system_i18nDateShortYear' property='settingSystem(i18nDateShortYear)' />
      <html:hidden styleId='user_i18nDateShortYear' property='settingUser(i18nDateShortYear)' />
    </logic:notEmpty>

    <logic:notEmpty name='settingForm' property='settingType(i18nDateWeekDay)'>
      <html:hidden styleId='system_i18nDateWeekDay' property='settingSystem(i18nDateWeekDay)' />
      <html:hidden styleId='user_i18nDateWeekDay' property='settingUser(i18nDateWeekDay)' />
    </logic:notEmpty>

    <logic:notEmpty name='settingForm' property='settingType(i18nTimeLong24H)'>
      <html:hidden styleId='system_i18nTimeLong24H' property='settingSystem(i18nTimeLong24H)' />
      <html:hidden styleId='user_i18nTimeLong24H' property='settingUser(i18nTimeLong24H)' />
    </logic:notEmpty>

    <logic:notEmpty name='settingForm' property='settingType(i18nTimeLong)'>
      <html:hidden styleId='system_i18nTimeLong' property='settingSystem(i18nTimeLong)' />
      <html:hidden styleId='user_i18nTimeLong' property='settingUser(i18nTimeLong)' />
    </logic:notEmpty>

    <logic:notEmpty name='settingForm' property='settingType(i18nTimeShort)'>
      <html:hidden styleId='system_i18nTimeShort' property='settingSystem(i18nTimeShort)' />
      <html:hidden styleId='user_i18nTimeShort' property='settingUser(i18nTimeShort)' />
    </logic:notEmpty>

    <logic:notEmpty name='settingForm' property='settingType(i18nTimeShort24H)'>
      <html:hidden styleId='system_i18nTimeShort24H' property='settingSystem(i18nTimeShort24H)' />
      <html:hidden styleId='user_i18nTimeShort24H' property='settingUser(i18nTimeShort24H)' />
    </logic:notEmpty>

    <logic:notEmpty name='settingForm' property='settingType(i18nDateShortToday)'>
      <html:hidden styleId='system_i18nDateShortToday' property='settingSystem(i18nDateShortToday)' />
      <html:hidden styleId='user_i18nDateShortToday' property='settingUser(i18nDateShortToday)' />
    </logic:notEmpty>

    <logic:notEmpty name='settingForm' property='settingType(i18nDateShortYesterday)'>
      <html:hidden styleId='system_i18nDateShortYesterday' property='settingSystem(i18nDateShortYesterday)' />
      <html:hidden styleId='user_i18nDateShortYesterday' property='settingUser(i18nDateShortYesterday)' />
    </logic:notEmpty>

    <logic:notEmpty name='settingForm' property='settingType(i18nDateThisAfternoon)'>
      <html:hidden styleId='system_i18nDateThisAfternoon' property='settingSystem(i18nDateThisAfternoon)' />
      <html:hidden styleId='user_i18nDateThisAfternoon' property='settingUser(i18nDateThisAfternoon)' />
    </logic:notEmpty>

    <logic:notEmpty name='settingForm' property='settingType(i18nDateThisMorning)'>
      <html:hidden styleId='system_i18nDateThisMorning' property='settingSystem(i18nDateThisMorning)' />
      <html:hidden styleId='user_i18nDateThisMorning' property='settingUser(i18nDateThisMorning)' />
    </logic:notEmpty>

    <logic:notEmpty name='settingForm' property='settingType(i18nDateThisEvening)'>
      <html:hidden styleId='system_i18nDateThisEvening' property='settingSystem(i18nDateThisEvening)' />
      <html:hidden styleId='user_i18nDateThisEvening' property='settingUser(i18nDateThisEvening)' />
    </logic:notEmpty>

    <logic:notEmpty name='settingForm' property='settingType(i18nDateYesterdayMorning)'>
      <html:hidden styleId='system_i18nDateYesterdayMorning' property='settingSystem(i18nDateYesterdayMorning)' />
      <html:hidden styleId='user_i18nDateYesterdayMorning' property='settingUser(i18nDateYesterdayMorning)' />
    </logic:notEmpty>

    <logic:notEmpty name='settingForm' property='settingType(i18nDateYesterdayAfternoon)'>
      <html:hidden styleId='system_i18nDateYesterdayAfternoon' property='settingSystem(i18nDateYesterdayAfternoon)' />
      <html:hidden styleId='user_i18nDateYesterdayAfternoon' property='settingUser(i18nDateYesterdayAfternoon)' />
    </logic:notEmpty>

    <logic:notEmpty name='settingForm' property='settingType(i18nDateYesterdayEvening)'>
      <html:hidden styleId='system_i18nDateYesterdayEvening' property='settingSystem(i18nDateYesterdayEvening)' />
      <html:hidden styleId='user_i18nDateYesterdayEvening' property='settingUser(i18nDateYesterdayEvening)' />
    </logic:notEmpty>

    <imhtml:iframe style='border: 0px;' formName='settingForm' frameName='ivataSettingIFrame' width='100%' height='100%' align='block'>

      <igw:bean id='dateScript'><imhtml:rewrite page='/include/script/date.jsp'/></igw:bean>
      <igw:bean id='iFrameScript'><imhtml:rewrite page='/setting/include/script/iFrame.jsp'/></igw:bean>
      <script language='javascript' src='<%=dateScript%>'></script>
      <script language='javascript' src='<%=iFrameScript%>'></script>
      <script language='javascript'>
        <%--
          // Invoked on <code>onChange</code> event of a format template select,
          // copies the newly selected value to the textField.
          // Then onFieldChangeIFrame() is called to copy values to parent.
        --%>
        function onTemplateSelect(fieldName) {
          var templateSelect = document.getElementById('template_' + fieldName);
          var textField = document.getElementById(fieldName);
          if (templateSelect.value!='custom') {
            textField.value=templateSelect.value;
          }
          onFieldChangeIFrame(fieldName);
        }

        <%--
          // The method changes the title of an element to show how a date will
          // look in this format and saves the data to the hidden field.
        --%>
        function onFormatFieldChange(fieldName) {
            showDemo(fieldName);
            onFieldChangeIFrame(fieldName);
        }

        <%--
          // The method changes the title of an element to show how a date will
          // look in this format.
        --%>
        function showDemo(fieldName) {
            var textField = document.getElementById(fieldName);
            var format = textField.value;
            var demoDate = new Date(2003, 12, 24, 15, 49, 25);
            textField.title = formatDate(demoDate, format);
        }

        <%--
          // Invoked when a textField with suggested options was changed by user,
          // the custom option will become the current one in its selector.
        --%>
        function onCustomChange(fieldName) {
          var templateSelect = document.getElementById('template_' + fieldName);
          templateSelect.value='custom';
          onFormatFieldChange(fieldName);
        }


      </script>

        <table border='0' cellpadding='0' cellspacing='0' width='100%' height='100%' class='hilight'>
          <tr>
            <td colspan='3'/>
            <logic:notEmpty name='settingForm' property='administrator'>
              <td width='1'><imhtml:img page='/setting/images/override.gif' /></td>
            </logic:notEmpty>
          </tr>

          <logic:notEmpty name='settingForm' property='settingType(i18nTimeZone)'>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='i18nTimeZone'/></td>
              <td class='fieldNarrow' colspan='2'>
                <imhtml:select fieldName='i18nTimeZone' property='setting(i18nTimeZone)' mandatory='true' onchange="onFieldChangeIFrame('i18nTimeZone')">
                  <% TreeSet timeZoneIds = new TreeSet(Arrays.asList(TimeZone.getAvailableIDs())); %>
                  <c:forEach var='timeZoneId' items='<%= timeZoneIds %>' >
                    <igw:bean id='timeZoneId' type='java.lang.String' />
                    <% SimpleTimeZone timeZone = new SimpleTimeZone(0, timeZoneId); %>
                    <imhtml:option value='<%= timeZoneId %>'
                        title='<%= timeZone.getDisplayName((Locale) session.getAttribute(Globals.LOCALE_KEY)) %>' >
                      <%= timeZoneId %>
                    </imhtml:option>
                  </c:forEach>
                </imhtml:select>
              </td>
              <td width='1'>
                <logic:notEmpty name='settingForm' property='administrator'>
                  <imhtml:checkbox fieldName='override' property='settingOverride(i18nTimeZone)' styleId='override_i18nTimeZone'/>
                </logic:notEmpty>
              </td>
              <td width='1'>
                <imhtml:img page='/setting/images/revert.gif' bundle='admin'
                      titleKey='index.img.revertToSystem.title'
                      onclick="revertToSystemIFrame('i18nTimeZone')" />
              </td>
            </tr>
          </logic:notEmpty>

          <logic:notEmpty name='settingForm' property='settingType(calendarDefaultView)'>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='calendarDefaultView'/></td>
              <td class='fieldNarrow' colspan='2'>
                <imhtml:select property='setting(calendarDefaultView)' fieldName='calendarDefaultView' mandatory='true' onchange="onFieldChangeIFrame('calendarDefaultView');">
                  <imhtml:option value='<%= IndexFormConstants.VIEW_DAY.toString() %>'>
                    <bean:message key='dateTime.label.calendarViews.dayView' bundle='admin'/>
                  </imhtml:option>
                  <imhtml:option value='<%= IndexFormConstants.VIEW_WORK_WEEK.toString() %>'>
                    <bean:message key='dateTime.label.calendarViews.5dayView' bundle='admin'/>
                  </imhtml:option>
                  <imhtml:option value='<%= IndexFormConstants.VIEW_WEEK.toString() %>'>
                    <bean:message key='dateTime.label.calendarViews.weekView' bundle='admin'/>
                  </imhtml:option>
                  <imhtml:option value='<%= IndexFormConstants.VIEW_MONTH.toString() %>'>
                    <bean:message key='dateTime.label.calendarViews.monthView' bundle='admin'/>
                  </imhtml:option>
                </imhtml:select>
              </td>
              <td width='1'>
                <logic:notEmpty name='settingForm' property='administrator'>
                  <imhtml:checkbox fieldName='override' property='settingOverride(calendarDefaultView)' styleId='overide_calendarDefaultView'/>
                </logic:notEmpty>
              </td>
              <td width='1'><imhtml:img page='/setting/images/revert.gif' bundle='admin'
                      onclick="revertToSystemIFrame('calendarDefaultView')"
                      titleKey='index.img.revertToSystem.title' /></td>
            </tr>
          </logic:notEmpty>

          <logic:notEmpty name='settingForm' property='settingType(calendarFirstWeekDay)'>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='calendarFirstWeekDay'/></td>
              <td class='fieldNarrow' colspan='2'>
                <imhtml:select property='setting(calendarFirstWeekDay)' fieldName='calendarFirstWeekDay' mandatory='true' onchange="onFieldChangeIFrame('calendarFirstWeekDay');">
                  <imhtml:option value='0'>
                    <bean:message key='dateTime.label.calendarFirstWeekDay.sunday' bundle='admin'/>
                  </imhtml:option>
                  <imhtml:option value='1'>
                    <bean:message key='dateTime.label.calendarFirstWeekDay.monday' bundle='admin'/>
                  </imhtml:option>
                </imhtml:select>
              </td>
              <td width='1'>
                <logic:notEmpty name='settingForm' property='administrator'>
                  <imhtml:checkbox fieldName='override' property='settingOverride(calendarFirstWeekDay)' styleId='override_calendarFirstWeekDay' />
                </logic:notEmpty>
              </td>
              <td width='1'><imhtml:img page='/setting/images/revert.gif' bundle='admin'
                      onclick="revertToSystemIFrame('calendarFirstWeekDay')"
                      titleKey='index.img.revertToSystem.title' /></td>
            </tr>
          </logic:notEmpty>



<%-- settings with suggested formats --%>


          <logic:notEmpty name='settingForm' property='settingType(i18nDateInputDisplay)'>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='i18nDateInputDisplay'/></td>
              <td class='fieldNarrow'>
                <select id='template_i18nDateInputDisplay' onChange="onTemplateSelect('i18nDateInputDisplay')">
                  <option value='custom'><bean:message bundle='admin' key='dateTime.label.custom' /></option>
                  <option value='MM/dd/yyyy'>MM/dd/yyyy</option>
                  <option value='dd/MM/yyyy'>dd/MM/yyyy</option>
                  <option value='MM.dd.yyyy'>MM.dd.yyyy</option>
                </select>
              </td>
              <td class='field'>
                <imhtml:text fieldName='i18nDateInputDisplay' property='setting(i18nDateInputDisplay)' mandatory='true'
                  onkeydown="onCustomChange('i18nDateInputDisplay');" onchange="onFormatFieldChange('i18nDateInputDisplay')" onmouseover="showDemo('i18nDateInputDisplay')"/>
              </td>
              <logic:notEmpty name='settingForm' property='administrator'>
                <td width='1'><imhtml:checkbox fieldName='override' property='settingOverride(i18nDateInputDisplay)' styleId='override_i18nDateInputDisplay' /></td>
              </logic:notEmpty>
              <td width='1'>
                <imhtml:img page='/setting/images/revert.gif' bundle='admin'
                      titleKey='index.img.revertToSystem.title'
                      onclick="revertToSystemIFrame('i18nDateInputDisplay')" />
              </td>
            </tr>
          </logic:notEmpty>


          <logic:notEmpty name='settingForm' property='settingType(i18nTimeInputDisplay)'>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='i18nTimeInputDisplay'/></td>
              <td class='fieldNarrow'>
                <select id='template_i18nTimeInputDisplay' onChange="onTemplateSelect('i18nTimeInputDisplay')" width='100%'>
                  <option value='custom'><bean:message bundle='admin' key='dateTime.label.custom' /></option>
                  <option value='HH:mm'>HH:mm</option>
                  <option value='KK:mm a'>KK:mm a</option>
                  <option value='hh.mm a'>hh.mm a</option>
                </select>
              </td>
              <td class='field'>
                <imhtml:text fieldName='i18nTimeInputDisplay' property='setting(i18nTimeInputDisplay)' mandatory='true'
                  onkeydown="onCustomChange('i18nTimeInputDisplay')" onchange="onFormatFieldChange('i18nTimeInputDisplay');" onmouseover="showDemo('i18nTimeInputDisplay');"/>
              </td>
              <logic:notEmpty name='settingForm' property='administrator'>
                <td width='1'><imhtml:checkbox fieldName='override' property='settingOverride(i18nTimeInputDisplay)' styleId='override_i18nTimeInputDisplay' /></td>
              </logic:notEmpty>
              <td width='1'>
                <imhtml:img page='/setting/images/revert.gif' bundle='admin'
                      titleKey='index.img.revertToSystem.title'
                      onclick="revertToSystemIFrame('i18nTimeInputDisplay')" />
              </td>
            </tr>
          </logic:notEmpty>


          <logic:notEmpty name='settingForm' property='settingType(i18nDateLong)'>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='i18nDateLong'/></td>
              <td class='fieldNarrow'>
                <select id='template_i18nDateLong' onChange="onTemplateSelect('i18nDateLong')">
                  <option value='custom'><bean:message bundle='admin' key='dateTime.label.custom' /></option>
                  <option value='d MMMM'>d MMMM</option>
                  <option value='MMMM d.'>MMMM d.</option>
                  <option value='d. MMM'>d. MMM</option>
                </select>
              </td>
              <td class='field'>
                <imhtml:text fieldName='i18nDateLong' property='setting(i18nDateLong)' mandatory='true'
                  onkeydown="onCustomChange('i18nDateLong')" onchange="onFormatFieldChange('i18nDateLong');" onmouseover="showDemo('i18nDateLong');"/>
              </td>
              <logic:notEmpty name='settingForm' property='administrator'>
                <td width='1'><imhtml:checkbox fieldName='override' property='settingOverride(i18nDateLong)' styleId='override_i18nDateLong' /></td>
              </logic:notEmpty>
              <td width='1'>
                <imhtml:img page='/setting/images/revert.gif' bundle='admin'
                      titleKey='index.img.revertToSystem.title'
                      onclick="revertToSystemIFrame('i18nDateLong')" />
              </td>
            </tr>
          </logic:notEmpty>


          <logic:notEmpty name='settingForm' property='settingType(i18nDateLongDay)'>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='i18nDateLongDay'/></td>
              <td class='fieldNarrow'>
                <select id='template_i18nDateLongDay' onChange="onTemplateSelect('i18nDateLongDay')">
                  <option value='custom'><bean:message bundle='admin' key='dateTime.label.custom' /></option>
                  <option value='EEEE, d MMMM'>EEEE, d MMMM</option>
                  <option value='EEE, MMMM d.'>EEE, MMMM d.</option>
                  <option value='d. MMM, EEEE'>d. MMM, EEEE</option>
                </select>
              </td>
              <td class='field'>
                <imhtml:text fieldName='i18nDateLongDay' property='setting(i18nDateLongDay)' mandatory='true'
                  onkeydown="onCustomChange('i18nDateLongDay')" onchange="onFormatFieldChange('i18nDateLongDay');" onmouseover="showDemo('i18nDateLongDay');"/>
              </td>
              <logic:notEmpty name='settingForm' property='administrator'>
                <td width='1'><imhtml:checkbox fieldName='override' property='settingOverride(i18nDateLongDay)' styleId='override_i18nDateLongDay'/></td>
              </logic:notEmpty>
              <td width='1'>
                <imhtml:img page='/setting/images/revert.gif' bundle='admin'
                      titleKey='index.img.revertToSystem.title'
                      onclick="revertToSystem('i18nDateLongDay')" />
              </td>
            </tr>
          </logic:notEmpty>


          <logic:notEmpty name='settingForm' property='settingType(i18nDateLongYear)'>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='i18nDateLongYear'/></td>
              <td class='fieldNarrow'>
                <select id='template_i18nDateLongYear' onChange="onTemplateSelect('i18nDateLongYear')">
                  <option value='custom'><bean:message bundle='admin' key='dateTime.label.custom' /></option>
                  <option value='d MMMM yyyy'>d MMMM yyyy</option>
                  <option value='d MMM yy'>d MMM yy</option>
                  <option value='d. MMM yyyy'>d. MMM yyyy</option>
                </select>
              </td>
              <td class='field'>
                <imhtml:text fieldName='i18nDateLongYear' property='setting(i18nDateLongYear)' mandatory='true'
                  onkeydown="onCustomChange('i18nDateLongYear')" onchange="onFormatFieldChange('i18nDateLongYear');" onmouseover="showDemo('i18nDateLongYear');"/>
              </td>
              <logic:notEmpty name='settingForm' property='administrator'>
                <td width='1'><imhtml:checkbox fieldName='override' property='settingOverride(i18nDateLongYear)' styleId='override_i18nDateLongYear' /></td>
              </logic:notEmpty>
              <td width='1'>
                <imhtml:img page='/setting/images/revert.gif' bundle='admin'
                      titleKey='index.img.revertToSystem.title'
                      onclick="revertToSystemIFrame('i18nDateLongYear')" />
              </td>
            </tr>
          </logic:notEmpty>


          <logic:notEmpty name='settingForm' property='settingType(i18nDateShort)'>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='i18nDateShort'/></td>
              <td class='fieldNarrow'>
                <select id='template_i18nDateShort' onChange="onTemplateSelect('i18nDateShort')">
                  <option value='custom'><bean:message bundle='admin' key='dateTime.label.custom' /></option>
                  <option value='MM/dd'>MM/dd</option>
                  <option value='dd-MM'>dd-MM</option>
                  <option value='M.d.'>M.d.</option>
                </select>
              </td>
              <td class='field'>
                <imhtml:text fieldName='i18nDateShort' property='setting(i18nDateShort)' mandatory='true'
                  onkeydown="onCustomChange('i18nDateShort')" onchange="onFormatFieldChange('i18nDateShort')" onmouseover="showDemo('i18nDateShort')"/>
              </td>
              <logic:notEmpty name='settingForm' property='administrator'>
                <td width='1'><imhtml:checkbox fieldName='override' property='settingOverride(i18nDateShort)' styleId='override_i18nDateShort' /></td>
              </logic:notEmpty>
              <td width='1'>
                <imhtml:img page='/setting/images/revert.gif' bundle='admin'
                      titleKey='index.img.revertToSystem.title'
                      onclick="revertToSystemIFrame('i18nDateShort')" />
              </td>
            </tr>
          </logic:notEmpty>

          <logic:notEmpty name='settingForm' property='settingType(i18nDateShortYear)'>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='i18nDateShortYear'/></td>
              <td class='fieldNarrow'>
                <select id='template_i18nDateShortYear' onChange="onTemplateSelect('i18nDateShortYear')">
                  <option value='custom'><bean:message bundle='admin' key='dateTime.label.custom' /></option>
                  <option value='MM/dd/yyyy'>MM/dd/yyyy</option>
                  <option value='dd-MM-yyyy'>dd-MM-yyyy</option>
                  <option value='yyyy.M.d.'>yyyy.M.d.</option>
                </select>
              </td>
              <td class='field'>
                <imhtml:text fieldName='i18nDateShortYear' property='setting(i18nDateShortYear)' mandatory='true'
                  onkeydown="onCustomChange('i18nDateShortYear')" onchange="onFormatFieldChange('i18nDateShortYear');" onmouseover="showDemo('i18nDateShortYear');"/>
              </td>
              <logic:notEmpty name='settingForm' property='administrator'>
                <td width='1'><imhtml:checkbox fieldName='override' property='settingOverride(i18nDateShortYear)' styleId='override_i18nDateShortYear' /></td>
              </logic:notEmpty>
              <td width='1'>
                <imhtml:img page='/setting/images/revert.gif' bundle='admin'
                      titleKey='index.img.revertToSystem.title'
                      onclick="revertToSystemIFrame('i18nDateShortYear')" />
              </td>
            </tr>
          </logic:notEmpty>

          <logic:notEmpty name='settingForm' property='settingType(i18nDateWeekDay)'>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='i18nDateWeekDay'/></td>
              <td class='fieldNarrow'>
                <select id='template_i18nDateWeekDay' onChange="onTemplateSelect('i18nDateWeekDay')">
                  <option value='custom'><bean:message bundle='admin' key='dateTime.label.custom' /></option>
                  <option value='EEEE'>EEEE</option>
                  <option value='EEE'>EEE</option>
                </select>
              </td>
              <td class='field'>
                <imhtml:text fieldName='i18nDateWeekDay' property='setting(i18nDateWeekDay)' mandatory='true'
                  onkeydown="onCustomChange('i18nDateWeekDay')" onchange="onFormatFieldChange('i18nDateWeekDay');" onmouseover="showDemo('i18nDateWeekDay');"/>
              </td>
              <logic:notEmpty name='settingForm' property='administrator'>
                <td width='1'><imhtml:checkbox fieldName='override' property='settingOverride(i18nDateWeekDay)' styleId='override_i18nDateWeekDay' /></td>
              </logic:notEmpty>
              <td width='1'>
                <imhtml:img page='/setting/images/revert.gif' bundle='admin'
                      titleKey='index.img.revertToSystem.title'
                      onclick="revertToSystemIFrame('i18nDateWeekDay')" />
              </td>
            </tr>
          </logic:notEmpty>


          <logic:notEmpty name='settingForm' property='settingType(i18nTimeLong)'>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='i18nTimeLong'/></td>
              <td class='fieldNarrow'>
                <select id='template_i18nTimeLong' onChange="onTemplateSelect('i18nTimeLong')">
                  <option value='custom'><bean:message bundle='admin' key='dateTime.label.custom' /></option>
                  <option value='HH:mm:ss'>HH:mm:ss</option>
                  <option value='hh:mm:ss a'>hh:mm:ss a</option>
                  <option value='hh.mm.ss'>hh.mm.ss</option>
                </select>
              </td>
              <td class='field'>
                <imhtml:text fieldName='i18nTimeLong' property='setting(i18nTimeLong)' mandatory='true'
                  onkeydown="onCustomChange('i18nTimeLong')" onchange="onFormatFieldChange('i18nTimeLong');" onmouseover="showDemo('i18nTimeLong');"/>
              </td>
              <logic:notEmpty name='settingForm' property='administrator'>
                <td width='1'><imhtml:checkbox fieldName='override' property='settingOverride(i18nTimeLong)' styleId='override_i18nTimeLong' /></td>
              </logic:notEmpty>
              <td width='1'>
                <imhtml:img page='/setting/images/revert.gif' bundle='admin'
                      titleKey='index.img.revertToSystem.title'
                      onclick="revertToSystemIFrame('i18nTimeLong')" />
              </td>
            </tr>
          </logic:notEmpty>


          <logic:notEmpty name='settingForm' property='settingType(i18nTimeShort)'>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='i18nTimeShort'/></td>
              <td class='fieldNarrow'>
                <select id='template_i18nTimeShort' onChange="onTemplateSelect('i18nTimeShort')">
                  <option value='custom'><bean:message bundle='admin' key='dateTime.label.custom' /></option>
                  <option value='H:mm'>H:mm</option>
                  <option value='h:mm a'>h:mm a</option>
                  <option value='h.mm'>h.mm</option>
                </select>
              </td>
              <td class='field'>
                <imhtml:text fieldName='i18nTimeShort' property='setting(i18nTimeShort)' mandatory='true'
                  onkeydown="onCustomChange('i18nTimeShort')" onchange="onFormatFieldChange('i18nTimeShort');" onmouseover="showDemo('i18nTimeShort');"/>
              </td>
              <logic:notEmpty name='settingForm' property='administrator'>
                <td width='1'><imhtml:checkbox fieldName='override' property='settingOverride(i18nTimeShort)' styleId='override_i18nTimeShort' /></td>
              </logic:notEmpty>
              <td width='1'>
                <imhtml:img page='/setting/images/revert.gif' bundle='admin'
                      titleKey='index.img.revertToSystem.title'
                      onclick="revertToSystemIFrame('i18nTimeShort')" />
              </td>
            </tr>
          </logic:notEmpty>

<%-- settings without suggested formats --%>

          <logic:notEmpty name='settingForm' property='settingType(i18nDateShortToday)'>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='i18nDateShortToday'/></td>
              <td class='field' colspan='2'>
                <imhtml:text fieldName='i18nDateShortToday' property='setting(i18nDateShortToday)' mandatory='true'
                    onchange="onFormatFieldChange('i18nDateShortToday')" onmouseover="showDemo('i18nDateShortToday')"/>
              </td>
              <logic:notEmpty name='settingForm' property='administrator'>
                <td width='1'><imhtml:checkbox fieldName='override' property='settingOverride(i18nDateShortToday)' styleId='override_i18nDateShortToday' /></td>
              </logic:notEmpty>
              <td width='1'>
                <imhtml:img page='/setting/images/revert.gif' bundle='admin'
                      titleKey='index.img.revertToSystem.title'
                      onclick="revertToSystemIFrame('i18nDateShortToday')" />
              </td>
            </tr>
          </logic:notEmpty>


          <logic:notEmpty name='settingForm' property='settingType(i18nDateShortYesterday)'>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='i18nDateShortYesterday'/></td>
              <td class='field' colspan='2'>
                <imhtml:text fieldName='i18nDateShortYesterday' property='setting(i18nDateShortYesterday)' mandatory='true'
                    onchange="onFormatFieldChange('i18nDateShortYesterday')" onmouseover="showDemo('i18nDateShortYesterday')"/>
              </td>
              <logic:notEmpty name='settingForm' property='administrator'>
                <td width='1'><imhtml:checkbox fieldName='override' property='settingOverride(i18nDateShortYesterday)' styleId='override_i18nDateShortYesterday'/></td>
              </logic:notEmpty>
              <td width='1'>
                <imhtml:img page='/setting/images/revert.gif' bundle='admin'
                      titleKey='index.img.revertToSystem.title'
                      onclick="revertToSystemIFrame('i18nDateShortYesterday')" />
              </td>
            </tr>
          </logic:notEmpty>


          <logic:notEmpty name='settingForm' property='settingType(i18nDateThisAfternoon)'>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='i18nDateThisAfternoon'/></td>
              <td class='field' colspan='2'>
                <imhtml:text fieldName='i18nDateThisAfternoon' property='setting(i18nDateThisAfternoon)'  mandatory='true'
                    onchange="onFormatFieldChange('i18nDateThisAfternoon')" onmouseover="showDemo('i18nDateThisAfternoon')"/>
              </td>
              <logic:notEmpty name='settingForm' property='administrator'>
                <td width='1'><imhtml:checkbox fieldName='override' property='settingOverride(i18nDateThisAfternoon)' styleId='override_i18nDateThisAfternoon' /></td>
              </logic:notEmpty>
              <td width='1'>
                <imhtml:img page='/setting/images/revert.gif' bundle='admin'
                      titleKey='index.img.revertToSystem.title'
                      onclick="revertToSystemIFrame('i18nDateThisAfternoon')" />
              </td>
            </tr>
          </logic:notEmpty>


          <logic:notEmpty name='settingForm' property='settingType(i18nDateThisMorning)'>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='i18nDateThisMorning'/></td>
              <td class='field' colspan='2'>
                <imhtml:text fieldName='i18nDateThisMorning' property='setting(i18nDateThisMorning)' mandatory='true'
                    onchange="onFormatFieldChange('i18nDateThisMorning')" onmouseover="showDemo('i18nDateThisMorning')"/>
              </td>
              <logic:notEmpty name='settingForm' property='administrator'>
                <td width='1'><imhtml:checkbox fieldName='override' property='settingOverride(i18nDateThisMorning)' styleId='override_i18nDateThisMorning' /></td>
              </logic:notEmpty>
              <td width='1'>
                <imhtml:img page='/setting/images/revert.gif' bundle='admin'
                      titleKey='index.img.revertToSystem.title'
                      onclick="revertToSystemIFrame('i18nDateThisMorning')" />
              </td>
            </tr>
          </logic:notEmpty>


          <logic:notEmpty name='settingForm' property='settingType(i18nDateThisEvening)'>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='i18nDateThisEvening'/></td>
              <td class='field' colspan='2'>
                <imhtml:text fieldName='i18nDateThisEvening' property='setting(i18nDateThisEvening)' mandatory='true'
                    onchange="onFormatFieldChange('i18nDateThisEvening')" onmouseover="showDemo('i18nDateThisEvening')"/>
              </td>
              <logic:notEmpty name='settingForm' property='administrator'>
                <td width='1'><imhtml:checkbox fieldName='override' property='settingOverride(i18nDateThisEvening)' styleId='override_i18nDateThisEvening' /></td>
              </logic:notEmpty>
              <td width='1'>
                <imhtml:img page='/setting/images/revert.gif' bundle='admin'
                      titleKey='index.img.revertToSystem.title'
                      onclick="revertToSystemIFrame('i18nDateThisEvening')" />
              </td>
            </tr>
          </logic:notEmpty>


          <logic:notEmpty name='settingForm' property='settingType(i18nDateYesterdayMorning)'>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='i18nDateYesterdayMorning'/></td>
              <td class='field' colspan='2'>
                <imhtml:text fieldName='i18nDateYesterdayMorning' property='setting(i18nDateYesterdayMorning)' mandatory='true'
                    onchange="onFormatFieldChange('i18nDateYesterdayMorning')" onmouseover="showDemo('i18nDateYesterdayMorning')"/>
              </td>
              <logic:notEmpty name='settingForm' property='administrator'>
                <td width='1'><imhtml:checkbox fieldName='override' property='settingOverride(i18nDateYesterdayMorning)' styleId='override_i18nDateYesterdayMorning' /></td>
              </logic:notEmpty>
              <td width='1'>
                <imhtml:img page='/setting/images/revert.gif' bundle='admin'
                      titleKey='index.img.revertToSystem.title'
                      onclick="revertToSystemIFrame('i18nDateYesterdayMorning')" />
              </td>
            </tr>
          </logic:notEmpty>


          <logic:notEmpty name='settingForm' property='settingType(i18nDateYesterdayAfternoon)'>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='i18nDateYesterdayAfternoon'/></td>
              <td class='field' colspan='2'>
                <imhtml:text fieldName='i18nDateYesterdayAfternoon' property='setting(i18nDateYesterdayAfternoon)' mandatory='true'
                    onchange="onFormatFieldChange('i18nDateYesterdayAfternoon')" onmouseover="showDemo('i18nDateYesterdayAfternoon')"/>
              </td>
              <logic:notEmpty name='settingForm' property='administrator'>
                <td width='1'><imhtml:checkbox fieldName='override' property='settingOverride(i18nDateYesterdayAfternoon)' styleId='override_i18nDateYesterdayAfternoon' /></td>
              </logic:notEmpty>
              <td width='1'>
                <imhtml:img page='/setting/images/revert.gif' bundle='admin'
                      titleKey='index.img.revertToSystem.title'
                      onclick="revertToSystemIFrame('i18nDateYesterdayAfternoon')" />
              </td>
            </tr>
          </logic:notEmpty>


          <logic:notEmpty name='settingForm' property='settingType(i18nDateYesterdayEvening)'>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='i18nDateYesterdayEvening'/></td>
              <td class='field' colspan='2'>
                <imhtml:text fieldName='i18nDateYesterdayEvening' property='setting(i18nDateYesterdayEvening)' mandatory='true'
                    onchange="onFormatFieldChange('i18nDateYesterdayEvening')" onmouseover="showDemo('i18nDateYesterdayEvening')"/>
              </td>
              <logic:notEmpty name='settingForm' property='administrator'>
                <td width='1'><imhtml:checkbox fieldName='override' property='settingOverride(i18nDateYesterdayEvening)' styleId='override_i18nDateYesterdayEvening' /></td>
              </logic:notEmpty>
              <td width='1'>
                <imhtml:img page='/setting/images/revert.gif' bundle='admin'
                      titleKey='index.img.revertToSystem.title'
                      onclick="revertToSystemIFrame('i18nDateYesterdayEvening')" />
              </td>
            </tr>
          </logic:notEmpty>
          <tr height='100%'><td>&nbsp;</td></tr>
        </table>
    </imhtml:iframe>
  </imtheme:framePane>
</imtheme:frame>

</imhtml:form>