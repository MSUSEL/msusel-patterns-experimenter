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

<%@page import='com.ivata.groupware.business.calendar.event.EventDO' %>
<%@page import='com.ivata.groupware.business.calendar.event.meeting.MeetingDO' %>
<%@page import='com.ivata.groupware.business.calendar.event.publicholiday.PublicHolidayDO' %>
<%@page import='com.ivata.groupware.business.calendar.struts.IndexFormConstants' %>
<%@page import='com.ivata.mask.util.StringHandling' %>
<%@page import='org.apache.struts.Globals' %>
<%@page import='java.util.*' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: weekview.jsp,v 1.4 2005/04/28 18:47:08 colinmacleod Exp $
//
// <p>Weekview of calendar events.</p>
//
// Since: ivata groupware 0.9 (2002-07-10)
// Author: Jan Boros
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
// $Log: weekview.jsp,v $
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
// Revision 1.2  2005/04/09 17:19:18  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:47:51  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.7  2004/11/12 16:08:12  colinmacleod
// Removed dependencies on SSLEXT.
// Moved Persistence classes to ivata masks.
//
// Revision 1.6  2004/11/03 15:49:51  colinmacleod
// Changed todo comments to TODO: all caps.
//
// Revision 1.5  2004/07/14 22:50:22  colinmacleod
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
// Revision 1.1.1.1  2004/01/27 20:58:23  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.3  2003/10/28 13:10:23  jano
// commiting calendar,
// still fixing compile and building openPortal project
//
// Revision 1.2  2003/10/15 13:57:23  colin
// fixing for XDoclet
//
// Revision 1.30  2003/04/03 13:25:32  jano
// fixing currentDay , again
//
// Revision 1.29  2003/03/18 09:59:42  jano
// fixing displaying events at hour of week view
//
// Revision 1.28  2003/03/17 10:10:00  jano
// fixing:
// -TimeZone bug
// -displaying today day - red color, current day - dark orange
//
// Revision 1.27  2003/03/04 19:06:31  colin
// fixed prompt keys
//
// Revision 1.26  2003/03/04 14:24:19  colin
// fixed javascript
//
// Revision 1.25  2003/03/04 02:11:50  colin
// split up dayview and weekview labels for enteredBy
//
// Revision 1.24  2003/03/04 01:44:36  colin
// fixed enteredBy key
//
// Revision 1.23  2003/03/03 16:43:37  colin
// incorporated suggestions from Thomas Van Hare
//
// Revision 1.22  2003/02/25 07:30:03  colin
// restructured java file paths
//
// Revision 1.21  2003/02/13 08:45:25  colin
// conversion to Struts/popups
//
// Revision 1.20  2003/01/27 07:22:54  colin
// updated copyright notice
//
// Revision 1.19  2003/01/23 16:52:36  peter
// links and forms changed to sslext, checkLogin added
//
// Revision 1.18  2002/12/18 13:21:32  peter
// names of days locale-aware
//
// Revision 1.17  2002/11/25 08:54:38  peter
// internationalisation
//
// Revision 1.16  2002/10/18 09:23:23  jano
// fixing widt of week view
//
// Revision 1.15  2002/09/24 13:32:05  jano
// if Saturday or Sunday and click 5 days view go to 7 days view
//
// Revision 1.14  2002/09/24 12:20:47  jano
// today has diffrent background
// ficing names of class in CSS
//
// Revision 1.13  2002/09/23 11:53:58  colin
// date formatter constants now in a separate class
//
// Revision 1.12  2002/09/23 11:28:03  jano
// last changes with images and CSS
//
// Revision 1.11  2002/09/20 14:37:21  jano
// tidy up images
//
// Revision 1.10  2002/09/17 08:46:55  jano
// I need to put images for difrent events
//
// Revision 1.9  2002/08/27 08:44:09  colin
// split tags and themes into two separate includes
//
// Revision 1.8  2002/08/13 09:13:53  jano
// change algorimtus of including views and mask
//
--%>

<%@include file='/include/tags.jspf'%>
<igw:checkLogin />

<%@include file='/calendar/include/calendar.jspf'%>
<%@include file='/calendar/include/views.jspf'%>

<%-- format names of days --%>
<%
  java.text.SimpleDateFormat nameOfDay = new java.text.SimpleDateFormat("EEEEE d ",
                (java.util.Locale)session.getAttribute(Globals.LOCALE_KEY));
  nameOfDay.setTimeZone(TimeZone.getTimeZone(i18nTimeZone));
%>

<%-- there are always as many days as there are elements in the day events map array --%>
<bean:size id='numberOfDays' name='calendarIndexForm' property='events'/>
<%-- column width is based on the number of days --%>
<% int columnWidth = 100 / numberOfDays.intValue(); %>

<%-- we use the character formatter to format titles - remove new lines --%>
<igw:bean id='lineFeeds' scope='page' type='com.ivata.mask.web.format.SearchReplaceFormat'/>
<jsp:setProperty name='lineFeeds' property='search' value='<%="\n"%>'/>
<jsp:setProperty name='lineFeeds' property='replace' value=' - '/>
<imformat:addFormat formatter='<%=characterFormatter%>' format='<%=lineFeeds%>'/>
<igw:bean id='carriageReturns' scope='page' type='com.ivata.mask.web.format.SearchReplaceFormat'/>
<jsp:setProperty name='carriageReturns' property='search' value='<%="\r"%>'/>
<jsp:setProperty name='carriageReturns' property='replace' value=''/>
<imformat:addFormat formatter='<%=characterFormatter%>' format='<%=carriageReturns%>'/>

<imtheme:frame>
  <%-- pane with text which week is display --%>
  <imtheme:framePane>
  <div class='calendartitle'>
    <%thisDay.setTime(calendarIndexForm.getFirstWeekDay().getTime());%>
    <%=dateFormatter.format(thisDay.getTime())%> -
    <%
      thisDay.set(GregorianCalendar.DAY_OF_YEAR,
          thisDay.get(GregorianCalendar.DAY_OF_YEAR) + numberOfDays.intValue() - 1);
    %>
    <%=dateFormatter.format(thisDay.getTime())%>
  </div>
  </imtheme:framePane>


  <%-- pane with all days and events for those days --%>
  <imtheme:framePane>
    <table border='0' cellpadding='0' cellspacing='0' align="center" >
      <%-- first line of the table with DAYS --%>
      <tr>
        <td class='eventTime' width='40'>&nbsp;</td>

        <% thisDay.setTime(calendarIndexForm.getFirstWeekDay().getTime()); %>
        <c:forEach var='weekday' begin='1' end='<%=numberOfDays.intValue()%>'>
          <igw:bean id='weekday' type='java.lang.Integer'/>
          <igw:bean id='dayName'><%=nameOfDay.format(thisDay.getTime()).toString()%></igw:bean>
          <igw:bean id='dayTitle'><%=dateFormatter.format(thisDay.getTime()).toString()%></igw:bean>
          <c:choose>
            <%-- today is marked with a sort of redish color --%>
            <c:when test='<%=thisDay.equals(today)%>'>
              <igw:bean id='titleToday'><bean:message bundle='calendar' key='weekview.title.goto.today' arg0='<%=dayTitle%>'/></igw:bean>
              <td class='eventTodayDate' width='<%=columnWidth%>%' title='<%=titleToday%>'>
            </c:when>
            <%-- the current day is marked with a dark orange color --%>
            <c:when test='<%=calendarIndexForm.getCurrentDay().equals(thisDay)%>'>
              <igw:bean id='titleCurrentDay'><bean:message bundle='calendar' key='weekview.title.goto.current' arg0='<%=dayTitle%>'/></igw:bean>
              <td class='eventCurrentDate' width='<%=columnWidth%>%' title='<%=titleCurrentDay%>'>
            </c:when>
            <c:otherwise>
              <igw:bean id='titleDay'><bean:message bundle='calendar' key='weekview.title.goto.day' arg0='<%=dayTitle%>'/></igw:bean>
              <td class='eventDate' width='<%=columnWidth%>%' title='<%=titleDay%>'>
            </c:otherwise>
          </c:choose>
          <imutil:map id='indexParams'>
            <imutil:mapEntry name='view' value='<%=IndexFormConstants.VIEW_DAY.toString()%>'/>
            <imutil:mapEntry name='day' value='<%=new Integer(thisDay.get(GregorianCalendar.DATE)).toString()%>'/>
            <imutil:mapEntry name='month' value='<%=new Integer(thisDay.get(GregorianCalendar.MONTH)).toString()%>'/>
            <imutil:mapEntry name='year' value='<%=new Integer(thisDay.get(GregorianCalendar.YEAR)).toString()%>'/>
          </imutil:map>
          <imhtml:link page='/calendar/index.action' name='indexParams' styleClass='calendarDayTitle'><%=dayName%></imhtml:link>
          <% thisDay.set(GregorianCalendar.DATE, thisDay.get(GregorianCalendar.DATE) + 1); %>
          </td>
          <c:if test='<%=weekday.compareTo(numberOfDays) < 0%>'>
            <td class='eventTitleLine'  width='1'><html:img page='/images/empty.gif' width='1' height='1'/></td>
          </c:if>
        </c:forEach>
      </tr>

      <%-- second line of the table with all day events for each day --%>
      <tr class='eventAllDay'>
        <td class='eventTime' width='40'><bean:message bundle='calendar' key='weekview.label.allDay' /></td>

        <%thisDay.setTime(calendarIndexForm.getFirstWeekDay().getTime());%>
        <c:forEach var='allDayEventsThisDay' items='<%=calendarIndexForm.getAllDayEvents()%>' varStatus='allDayStatus'>
          <igw:bean id='allDayEventsThisDay' type='java.util.Vector'/>
          <igw:bean id='allDayStatus' type='javax.servlet.jsp.jstl.core.LoopTagStatus'/>

          <td width='<%=columnWidth%>%'>
            <c:forEach var='allDayEvent' items='<%=allDayEventsThisDay%>'>
              <igw:bean id='allDayEvent' type='com.ivata.groupware.business.calendar.event.EventDO'/>
              <table border='0' cellpadding='0' cellspacing='0' width='100%'>
                <tr>
                  <c:choose>
                    <c:when test='<%=allDayEvent instanceof MeetingDO%>'>
                      <td class='eventAllDay' width='12'><html:img page='/calendar/images/meeting.gif' width='12' height='23'/></td>
                    </c:when>
                    <c:when test='<%=allDayEvent instanceof PublicHolidayDO%>'>
                      <td class='eventAllDay' width='12'><html:img page='/calendar/images/publicHoliday.gif' width='12' height='23'/></td>
                    </c:when>
                    <c:otherwise>
                      <td class='eventAllDay' width='12'><html:img page='/calendar/images/event.gif' width='12' height='6'/></td>
                    </c:otherwise>
                  </c:choose>
                  <td class='eventAllDay'>
                    <imutil:mapEntry mapName='eventParams' name='id' value='<%=allDayEvent.getId().toString()%>'/>
                    <a href='' onclick='<%=eventPopUp.toString()%>return false' title='<%=characterFormatter.format(StringHandling.getNotNull(allDayEvent.getDescription(), ""))%>'><%org.apache.struts.util.ResponseUtils.write(pageContext, characterFormatter.format(allDayEvent.getSubject()));%></a>
                  </td>
                </tr>
              </table>
            </c:forEach>
            <%-- put out an empty space if there are no all day events --%>
            <c:if test='<%=allDayEventsThisDay.size() == 0%>'>&nbsp;</c:if>
          </td>
          <%-- there is a line between each day --%>
          <c:if test='<%=!allDayStatus.isLast()%>'>
            <td class='eventLine' width='1'><html:img page='/images/empty.gif' width='1' height='1'/></td>
          </c:if>
          <%-- go forward one day --%>
          <% thisDay.set(GregorianCalendar.DAY_OF_YEAR, thisDay.get(GregorianCalendar.DAY_OF_YEAR) + 1); %>
        </c:forEach>
      </tr>

      <%-- display events for each Hour In Day in next lines--%>
      <%int startHour, finishHour;%>
      <c:forEach var='hourOfDay' begin='<%=startHour = calendarIndexForm.getDayStartHour()%>'
                                  end='<%=finishHour = calendarIndexForm.getDayFinishHour()%>'>
        <igw:bean id='hourOfDay' type='java.lang.Integer'/>

        <%-- alternate rows get different shading --%>
        <c:choose>
          <c:when test='<%=((hourOfDay.intValue() - startHour)%2) == 1 %>'>
            <tr class='eventShadow'>
          </c:when>
          <c:otherwise>
            <tr class='eventNormal'>
          </c:otherwise>
        </c:choose>

        <%-- TODO: we should take the user's time format into consideration here!! --%>
        <td class='eventTime' width='40'><%=hourOfDay%>:00</td>

        <%-- go thro' each day and display the events for this hour in each one --%>
        <%
          thisDay.setTime(calendarIndexForm.getFirstWeekDay().getTime());
          theDayAfter.setTime(thisDay.getTime());
          theDayAfter.set(GregorianCalendar.DAY_OF_YEAR,
              theDayAfter.get(GregorianCalendar.DAY_OF_YEAR) + 1);
        %>

        <bean:define id='events' name='calendarIndexForm' property='events'/>
        <c:forEach var='eventsForDay' items='<%=events%>' varStatus='dayStatus'>
          <igw:bean id='dayStatus' type='javax.servlet.jsp.jstl.core.LoopTagStatus'/>
          <igw:bean id='eventsForDay' type='java.util.Map'/>
          <%-- are there any events for this day at this hour? --%>
          <%TreeSet eventsForHour = (TreeSet) eventsForDay.get(hourOfDay);%>
          <%-- this hour, this day has events. let's show them all --%>
          <td class='eventNormal' width='<%=columnWidth%>%'>
            <%-- if there are no events, just show a space --%>
            <c:if test='<%=eventsForHour == null%>'>
              <%-- stop the loop crashing --%>
              <%eventsForHour = new TreeSet();%>
              &nbsp;
            </c:if>

            <c:forEach var='event' items='<%=eventsForHour%>'>
              <igw:bean id='event' type='com.ivata.groupware.business.calendar.event.EventDO'/>
              <table border='0' cellpadding='0' cellspacing='0' width='100%'>
                <tr>
                  <%-- first column is the image (if we have one) --%>
                  <%
                    boolean hasImage = false;
                    boolean finishesAfterThisDay = (event.getFinish() != null)  && event.getFinish().after(theDayAfter);
                  %>

                  <c:choose>
                    <c:when test='<%=event instanceof MeetingDO%>'>
                      <td class='eventsAtHour' width='12'><html:img page='/calendar/images/meeting.gif' width='12' height='23'/></td>
                      <%hasImage = true;%>
                    </c:when>

                    <%-- if starting and finishing this day --%>
                    <c:when test='<%=!finishesAfterThisDay && event.getStart().after(thisDay)%>'>
                      <td width='12' class='eventsAtHour'><html:img page='/calendar/images/event.gif' width='12' height='6'/></td>
                    </c:when>

                    <%-- starting befor this day and finishing this day --%>
                    <c:when test='<%=!finishesAfterThisDay && event.getStart().before(thisDay)%>'>
                      <td width='12' class='eventsAtHour'><html:img page='/calendar/images/ciel.gif' width='10' height='10'/></td>
                      <%hasImage = true;%>
                    </c:when>

                    <%-- starting this day but finishing after this day--%>
                    <c:when test='<%=finishesAfterThisDay && event.getStart().after(thisDay)%>'>
                      <td width='12' class='eventsAtHour'><html:img page='/calendar/images/start.gif' width='10' height='10'/></td>
                      <%hasImage = true;%>
                    </c:when>
                  </c:choose>

                  <td colspan='2' class='eventsAtHour'>
                    <c:if test='<%=!hasImage%>'>
                      <%=timeFormatter.format(event.getStart().getTime())%>
                      <c:if test='<%=event.getFinish() != null%>'>
                        &minus; <%=timeFormatter.format(event.getFinish().getTime())%>
                      </c:if>
                    </c:if>
                    <imutil:mapEntry mapName='eventParams' name='id' value='<%=event.getId().toString()%>'/>
                    <a href='' onclick='<%=eventPopUp.toString()%>return false' title='<%=characterFormatter.format(StringHandling.getNotNull(event.getDescription(), ""))%>'><%=characterFormatter.format(event.getSubject())%></a>
                  </td>
                </tr>
                <%-- who entered the event? --%>
                <tr>
                  <td colspan='99' class='eventEnteredBy'><bean:message bundle='calendar' key='weekview.label.enteredBy' arg0='<%=dateTimeFormatter.format(event.getCreated())%>' arg1='<%=event.getCreatedBy().getName()%>'/></td>
                </tr>
              </table>
            </c:forEach><%-- events of hour --%>
          </td>
          <%-- if there are more days, then draw a line between this one and the next --%>
          <c:if test='<%=!dayStatus.isLast()%>'>
            <td class='eventLine' width='1'><html:img page='/images/empty.gif' width='1' height='1'/></td>
          </c:if>
          <%-- move this day and the day after forward one --%>
          <%
            theDayAfter.set(GregorianCalendar.DAY_OF_YEAR,
              theDayAfter.get(GregorianCalendar.DAY_OF_YEAR) + 1);
            thisDay.set(GregorianCalendar.DAY_OF_YEAR,
              thisDay.get(GregorianCalendar.DAY_OF_YEAR) + 1);
          %>
        </c:forEach><%-- events of day --%>
        </tr>
      </c:forEach> <%-- hours of the day --%>
    </table>
  </imtheme:framePane>
</imtheme:frame>

