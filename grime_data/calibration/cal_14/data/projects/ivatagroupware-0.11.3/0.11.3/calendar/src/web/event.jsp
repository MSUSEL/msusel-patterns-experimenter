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

<%@page import='com.ivata.mask.web.format.HTMLFormatter' %>
<%@page import='com.ivata.groupware.business.addressbook.address.country.CountryDO' %>
<%@page import='com.ivata.groupware.business.calendar.event.*' %>
<%@page import='com.ivata.groupware.business.calendar.event.meeting.MeetingDO' %>
<%@page import='com.ivata.groupware.business.calendar.struts.*' %>
<%@page import='java.util.Arrays' %>
<%@page import='java.util.Collection' %>

<%@page import='org.apache.struts.Globals' %>


<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: event.jsp,v 1.4 2005/04/28 18:47:09 colinmacleod Exp $
//
// Add, amend, remove mask for event, public holiday and meeting.
//
//
// Since: ivata groupware 0.9 (2002-07-07)
// Author: Jan Boros, colin
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
// $Log: event.jsp,v $
// Revision 1.4  2005/04/28 18:47:09  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.3  2005/04/10 20:10:09  colinmacleod
// Added new themes.
// Changed id type to String.
// Changed i tag to em and b tag to strong.
// Improved PicoContainerFactory with NanoContainer scripts.
//
// Revision 1.2  2005/04/09 17:19:18  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:47:46  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.9  2004/12/31 18:44:13  colinmacleod
// Cosmetic indentation improvements.
//
// Revision 1.8  2004/12/27 14:52:00  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.7  2004/11/12 16:08:11  colinmacleod
// Removed dependencies on SSLEXT.
// Moved Persistence classes to ivata masks.
//
// Revision 1.6  2004/11/03 15:49:51  colinmacleod
// Changed todo comments to TODO: all caps.
//
// Revision 1.5  2004/07/14 22:50:21  colinmacleod
// Replaced bean:define with extended igw:bean.
//
// Revision 1.4  2004/07/14 20:59:51  colinmacleod
// Changed all occurrences of jsp:useBean to igw:bean (better tomcat compatibility).
//
// Revision 1.3  2004/07/13 19:42:15  colinmacleod
// Moved project to POJOs from EJBs.
// Applied PicoContainer to services layer (replacing session EJBs).
// Applied Hibernate to persistence layer (replacing entity EJBs).
//
// Revision 1.2  2004/03/21 21:16:22  colinmacleod
// Shortened name to ivata op.
//
// Revision 1.1  2004/03/03 21:15:38  colinmacleod
// Moved core files to core subproject
//
// Revision 1.3  2004/02/10 22:09:28  colinmacleod
// Turned off SSL
//
// Revision 1.2  2004/02/06 11:20:01  janboros
// dependecy problem
//
// Revision 1.1.1.1  2004/01/27 20:58:22  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.3  2003/10/28 13:10:23  jano
// commiting calendar,
// still fixing compile and building openPortal project
//
// Revision 1.2  2003/10/15 13:57:23  colin
// fixing for XDoclet
//
// Revision 1.38  2003/06/16 08:11:49  peter
// events with minutes have editing disabled
//
// Revision 1.37  2003/04/14 07:24:47  peter
// helpKey is taken from form
//
// Revision 1.36  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.35  2003/02/26 13:50:01  peter
// added javaScript for public holiday date checking
//
// Revision 1.34  2003/02/25 17:28:33  peter
// fixed date javascript method calls and date-time field sizes
//
// Revision 1.33  2003/02/25 07:30:03  colin
// restructured java file paths
//
// Revision 1.32  2003/02/21 18:34:09  peter
// javascript fixes, some methods moved to include/script/date.jsp and trim.jsp
//
// Revision 1.31  2003/02/18 13:15:04  colin
// reverted to single quotes for popups
//
// Revision 1.30  2003/02/14 17:07:59  peter
// input formats taken from settings
//
// Revision 1.29  2003/02/13 08:45:25  colin
// conversion to Struts/popups
//
// Revision 1.28  2003/01/27 07:22:54  colin
// updated copyright notice
//
// Revision 1.27  2003/01/23 16:52:35  peter
// links and forms changed to sslext, checkLogin added
//
// Revision 1.26  2003/01/18 20:38:58  colin
// added help buttons
//
// Revision 1.25  2003/01/14 10:39:53  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.24  2003/01/10 14:26:29  jano
// fixing country in diffrent language
//
// Revision 1.23  2002/12/20 08:57:18  jano
// fixing localization
//
// Revision 1.22  2002/11/25 08:54:38  peter
// internationalisation
//
// Revision 1.21  2002/11/15 15:21:07  jano
// we have changed format to date_short_year
//
// Revision 1.20  2002/09/30 15:32:23  colin
// missing > in buttonFrame tag
//
// Revision 1.19  2002/09/25 16:25:35  colin
// added more title/tootips to submit buttons
//
// Revision 1.18  2002/09/25 08:25:58  jano
// javascript to check times
//
// Revision 1.17  2002/09/23 13:05:23  jano
// subject of publicHolidai is a name
//
// Revision 1.16  2002/09/23 11:49:55  colin
// date formatter constants now in a separate class
//
// Revision 1.15  2002/09/20 16:25:36  jano
// (colin)
// commit changes to make tabs function properly
//
// Revision 1.14  2002/09/13 08:17:42  jano
// fixing set up startday
//
// Revision 1.13  2002/09/10 13:49:01  jano
// displaing errors when you write wrong date
//
// Revision 1.12  2002/09/09 12:52:39  colin
// changed window width and added country combo
//
// Revision 1.11  2002/09/05 12:43:56  jano
// last changes
//
// Revision 1.10  2002/09/03 12:10:19  jano
// moving igw:headtag to begining of page
//
// Revision 1.9  2002/08/27 08:44:09  colin
// split tags and themes into two separate includes
//
// Revision 1.8  2002/08/23 07:45:43  jano
// added cancel button
//
// Revision 1.7  2002/08/15 11:33:34  jano
// *** empty log message ***
//
// Revision 1.6  2002/08/13 12:43:45  jano
// *** empty log message ***
//
// Revision 1.5  2002/08/13 09:12:48  jano
// change algorimtus of including views and mask
//
--%>

<%@include file='/include/tags.jspf'%>
<%-- you should only come here from the action, not directly --%>
<logic:notPresent name='calendarEventForm'>
  <logic:forward name='calendarEventAction'/>
</logic:notPresent>


<igw:checkLogin />
<%@include file='/include/theme.jspf'%>

<igw:bean id='userName' scope='session' type='java.lang.String'/>

<igw:bean id='calendarEventForm' scope='session' type='com.ivata.groupware.business.calendar.struts.EventForm'/>
<igw:bean id='event' name='calendarEventForm' property='event' type='com.ivata.groupware.business.calendar.event.EventDO'/>

<igw:getSetting id='dateInputFormatString' setting='i18nDateInput' type='java.lang.String'/>
<igw:getSetting id='dateDisplayFormatString' setting='i18nDateInputDisplay' type='java.lang.String'/>
<igw:getSetting id='timeInputFormatString' setting='i18nTimeInput' type='java.lang.String'/>
<igw:getSetting id='timeDisplayFormatString' setting='i18nTimeInputDisplay' type='java.lang.String'/>
<%
  pageContext.setAttribute("event", event);
%>

<imhtml:html locale='true'>
  <igw:head bundle='calendar' titleKey='<%=calendarEventForm.getTitleKey()%>'
          titleArgs='<%=Arrays.asList(new Object[] {event.getSubject()})%>'
          topLevel='true'>
    <imhtml:base/>
    <igw:bean id='alertHolidayDate'><bean:message bundle='calendar' key='event.alert.holidayDate' arg0='<%=dateInputFormatString%>'/></igw:bean>
    <igw:bean id='alertStartDate'><bean:message bundle='calendar' key='event.alert.startDate' arg0='<%=dateInputFormatString%>'/></igw:bean>
    <igw:bean id='alertStartDate'><bean:message bundle='calendar' key='event.alert.startDate' arg0='<%=dateInputFormatString%>'/></igw:bean>
    <igw:bean id='alertStartDatePast'><bean:message bundle='calendar' key='event.alert.startDate.past'/></igw:bean>
    <igw:bean id='alertFinishDate'><bean:message bundle='calendar' key='event.alert.finishDate' arg0='<%=dateInputFormatString%>'/></igw:bean>
    <igw:bean id='alertFinishDateAfter'><bean:message bundle='calendar' key='event.alert.finishDate.after' arg0='<%=dateInputFormatString%>'/></igw:bean>
    <igw:bean id='alertStartTime'><bean:message bundle='calendar' key='event.alert.startTime' arg0='<%=timeInputFormatString%>'/></igw:bean>
    <igw:bean id='alertStartTimePast'><bean:message bundle='calendar' key='event.alert.startTime.past' arg0='<%=timeInputFormatString%>'/></igw:bean>
    <igw:bean id='alertFinishTime'><bean:message bundle='calendar' key='event.alert.finishTime' arg0='<%=timeInputFormatString%>'/></igw:bean>
    <igw:bean id='alertFinishTimeAfter'><bean:message bundle='calendar' key='event.alert.finishTime.after' arg0='<%=timeInputFormatString%>'/></igw:bean>
    <igw:bean id='dateScript'><imhtml:rewrite page='/include/script/date.jsp'/></igw:bean>
    <igw:bean id='trimScript'><imhtml:rewrite page='/include/script/trim.jsp'/></igw:bean>
    <script language='javascript' src='<%=dateScript%>'></script>
    <script language='javascript' src='<%=trimScript%>'></script>
    <script language='javascript'>
      // <!--
      var dateInputFormat = "<%=dateInputFormatString%>";
      var dateDisplayFormat = "<%=dateDisplayFormatString%>";
      var timeInputFormat = "<%=timeInputFormatString%>";
      var timeDisplayFormat = "<%=timeDisplayFormatString%>";

      function onAllDayEvent() {
        var allDayEvent = trimElementById("allDayEvent");
        var tmpStartTime = trimElementById("tmpStartTime");
        var startTime = trimElementById("startTime");
        var tmpFinishTime = trimElementById("tmpFinishTime");
        var finishTime = trimElementById("finishTime");

        if (allDayEvent.checked) {
          tmpStartTime.value = startTime.value;
          startTime.value = "";
          if (finishTime.value.length > 0) {
            tmpFinishTime.value = finishTime.value;
            finishTime.value = "";
          }
          startTime.disabled = true;
          finishTime.disabled = true;
        } else {
          startTime.value = tmpStartTime.value;
          if (tmpFinishTime.value.length > 0) {
            finishTime.value = tmpFinishTime.value;
            tmpFinishTime.value = "";
          }
          startTime.disabled = false;
          finishTime.disabled = false;
        }
      }


      function onHolidayDate() {
          var holidayDate = trimElementById("holidayDate");

          if (holidayDate.value.length == 0) {
              return;
          }

          if (!isValidDate(holidayDate.value, dateInputFormat)) {
              alert("<%=alertHolidayDate%>");
              return;
          }
          reformatDateField(holidayDate, dateInputFormat, dateDisplayFormat);
          var dateNow = new Date();
      }


      function onStartDate() {
          var startDate = trimElementById("startDate");
          var finishDate = trimElementById("finishDate");
          if (startDate.value.length == 0) {
              return;
          }

          if (!isValidDate(startDate.value, dateInputFormat)) {
              alert("<%=alertStartDate%>");
              return;
          }
          reformatDateField(startDate, dateInputFormat, dateDisplayFormat);
          var dateNow = new Date();
          <%--
          // if the start date is in the past, warn about that
          --%>
          if (compareDates(startDate.value, dateDisplayFormat,
                  formatDate(dateNow, dateDisplayFormat), dateDisplayFormat) < 0) {
              alert("<%=alertStartDatePast%>");
          }
          <%--
          // if the finish date is before start date, make it the same
          --%>
          if ((finishDate.value.length != 0)
                      && (compareDates(startDate.value, dateDisplayFormat,
                              finishDate.value, dateDisplayFormat) > 0)) {
              finishDate.value = startDate.value;
          }

      }
      function onStartTime() {
          var startDate = trimElementById("startDate");
          var startTime = trimElementById("startTime");
          var finishDate = trimElementById("finishDate");
          var finishTime = trimElementById("finishTime");

          <%--
          // check this is a valid time
          --%>
          if (startTime.value.length == 0) {
              return;
          }
          if (!isValidDate(startTime.value, timeInputFormat)) {
              alert("<%=alertStartTime%>");
              return;
          }
          reformatTimeField(startTime, timeInputFormat, timeDisplayFormat);
          <%--
          // if the start date is empty, make it today
          --%>
          if (startDate.value.length == 0) {
              startDate.value = formatDate(new Date(), dateDisplayFormat);
          }
          <%--
          // if the start date is in not in the past, but the combination of
          // date and time is, warn about that
          --%>
          var dateTimeFormat = dateDisplayFormat + " " + timeDisplayFormat;
          var dateNow = new Date();
          if ((compareDates(startDate.value, dateDisplayFormat, formatDate(dateNow,
                      dateDisplayFormat), dateDisplayFormat) >= 0)
                  && (compareDates(startDate.value + " " + startTime.value,
                      dateTimeFormat,
                      formatDate(dateNow, dateTimeFormat), dateTimeFormat) < 0)) {
              alert("<%=alertStartTimePast%>");
          }
      }
      function onFinishDate() {
          var startDate = trimElementById("startDate");
          var finishDate = trimElementById("finishDate");
          if (finishDate.value.length == 0) {
              return;
          }
          if (!isValidDate(finishDate.value, dateInputFormat)) {
              alert("<%=alertFinishDate%>");
              return;
          }
          reformatDateField(finishDate, dateInputFormat, dateDisplayFormat);
          <%--
          // if there is no start date, set it to the same date
          --%>
          if (startDate.value.length == 0) {
              startDate.value = finishDate.value;
          } else if (compareDates(startDate.value, dateInputFormat, finishDate.value, dateInputFormat) > 0) {
              alert("<%=alertFinishDateAfter%>");
          }
      }
      function onFinishTime() {
          var finishTime = trimElementById("finishTime");
          if (finishTime.value.length == 0) {
              return;
          }
          if (!isValidDate(finishTime.value, timeInputFormat)) {
              alert("<%=alertFinishTime%>");
              finishTime.focus();
              return;
          }
          reformatTimeField(finishTime, timeInputFormat, timeDisplayFormat);
          <%--
          // if the finish time is not empty and the finish date is, set it
          // to the same date as the start date
          ---%>
          var startDate = trimElementById("startDate");
          var startTime = trimElementById("startTime");
          var finishDate = trimElementById("finishDate");
          if (finishDate.value.length == 0) {
              finishDate.value = startDate.value;
          }

          <%--
          // check the combination of finish date and time comes after the
          // combination of start date and time
          --%>
          var dateTimeFormat = dateInputFormat + " " + timeInputFormat;
          var startDateTime = getDateFromFormat(startDate.value + " " + startTime.value, dateTimeFormat);
          var finishDateTime = getDateFromFormat(finishDate.value + " " + finishTime.value, dateTimeFormat);
          if (startDateTime > finishDateTime) {
              alert("<%=alertFinishTimeAfter%>");
          }
      }
      //-->
    </script>
  </igw:head>
  <body class='dialog'>
    <imtheme:window>
      <%@include file='/include/errorFrame.jspf'%>

      <igw:bean id='deleteKey'><c:choose>
        <%-- a meeting with a library item for minutes --%>
        <%--

        @TODO
        DEPENDENCES

        c:when test='<%=(calendarEventForm.getMeeting() != null) && calendarEventForm.getMeeting().getHasLibraryItem()%>'>event.alert.delete.meeting.hasItem</c:when--%>

        <%-- a meeting with no library item for minutes --%>
        <c:when test='<%=calendarEventForm.isMeeting()%>'>event.alert.delete.meeting</c:when>
        <%-- just an ordinary event --%>
        <c:otherwise>event.alert.delete</c:otherwise>
      </c:choose></igw:bean>
      <imhtml:form action='/calendar/event' referralName='eventForm' bundle='calendar' deleteKey='<%=deleteKey%>' resourceFieldPath='event'>
        <c:choose>
          <%-- meetings have got tabs - TODO: add recurring event tab --%>
          <c:when test='<%=event instanceof MeetingDO%>'>
            <imtheme:tabFrame>
              <imtheme:tabControl name='eventTab' formName='document.calendarEventForm'>
                <imtheme:tab><bean:message bundle='calendar' key='meeting.tab.event' /></imtheme:tab>
                <imtheme:tab><bean:message bundle='calendar' key='meeting.tab.people' /></imtheme:tab>
                <imtheme:tab><bean:message bundle='calendar' key='meeting.tab.agenda' /></imtheme:tab>
              </imtheme:tabControl>

              <imtheme:tabContent>
                <jsp:include page='<%=calendarEventForm.getTabPage()%>'/>
              </imtheme:tabContent>

            </imtheme:tabFrame>
          </c:when>
          <%-- other events have no tabs - TODO: add recurring event tab --%>
          <c:otherwise>
            <jsp:include page='/calendar/eventDetails.jsp'/>
          </c:otherwise>
        </c:choose>

        <imtheme:buttonFrame>
          <imhtml:clear asNewButton='<%= event.getId() != null%>'
                  onclick='window.document.addressBookGroupForm.groupTab_activeTab.value=0;'/>

          <c:choose>
            <%--

            @TDOO
            DEPENDENCES

            c:when test='<%=(calendarEventForm.getMeeting() != null) && calendarEventForm.getMeeting().getHasLibraryItem()%>'--%>

            <c:when test='<%=calendarEventForm.isMeeting() %>'>
              <c:if test='<%=event.getId() != null%>'>
                <html:hidden property='event.id'/>
              </c:if>
              <imhtml:cancel/>
            </c:when>
            <c:otherwise>
              <c:choose>
                <c:when test='<%=event.getId() != null%>'>
                  <imhtml:delete/>
                  <imtheme:buttonSpacer/>
                  <imhtml:ok titleKey='event.submit.amendEvent.title'/>
                  <html:hidden property='event.id'/>
                </c:when>
                <c:otherwise>
                  <imtheme:buttonSpacer/>
                  <imhtml:ok titleKey='event.submit.addNewEvent.title'/>
                </c:otherwise>
              </c:choose>
              <imhtml:cancel/>
              <imhtml:apply/>
            </c:otherwise>
          </c:choose>
          <imhtml:help />
        </imtheme:buttonFrame>
      </imhtml:form>
    </imtheme:window>
    <%@include file='/include/script/fixPopUp.jspf'%>
  </body>
</imhtml:html>
