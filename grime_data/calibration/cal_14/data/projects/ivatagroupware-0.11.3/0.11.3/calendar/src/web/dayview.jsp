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
<%@page import='com.ivata.mask.util.StringHandling' %>
<%@page import='java.util.*' %>
<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: dayview.jsp,v 1.3 2005/04/10 20:10:09 colinmacleod Exp $
//
// Day view for actual date or for the date which is coming in parameters.
//
//
// Since: ivata groupware 0.9 (2002-07-7)
// Author: Jan Boros
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
// $Log: dayview.jsp,v $
// Revision 1.3  2005/04/10 20:10:09  colinmacleod
// Added new themes.
// Changed id type to String.
// Changed i tag to em and b tag to strong.
// Improved PicoContainerFactory with NanoContainer scripts.
//
// Revision 1.2  2005/04/09 17:19:18  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:47:48  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
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
// Revision 1.1.1.1  2004/01/27 20:58:22  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.4  2003/12/08 15:52:53  jano
// fixing calendar functionaality
//
// Revision 1.3  2003/10/28 13:10:23  jano
// commiting calendar,
// still fixing compile and building openPortal project
//
// Revision 1.2  2003/10/15 13:57:23  colin
// fixing for XDoclet
//
// Revision 1.30  2003/03/04 19:06:31  colin
// fixed prompt keys
//
// Revision 1.29  2003/03/04 14:23:44  colin
// fixed javascript
//
// Revision 1.28  2003/03/04 02:11:50  colin
// split up dayview and weekview labels for enteredBy
//
// Revision 1.27  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.26  2003/02/26 13:48:29  peter
// fixed displaying of country and region text
//
// Revision 1.25  2003/02/25 07:30:03  colin
// restructured java file paths
//
// Revision 1.24  2003/02/13 08:45:25  colin
// conversion to Struts/popups
//
// Revision 1.23  2003/01/27 07:22:54  colin
// updated copyright notice
//
// Revision 1.22  2003/01/23 16:52:35  peter
// links and forms changed to sslext, checkLogin added
//
// Revision 1.21  2003/01/15 08:27:47  jano
// fixing bug
//
// Revision 1.20  2002/12/20 08:10:52  jano
// fixing localization
//
// Revision 1.19  2002/11/25 08:54:38  peter
// internationalisation
//
// Revision 1.18  2002/09/24 12:19:30  jano
// ficing names of class in CSS
//
// Revision 1.17  2002/09/23 11:49:40  colin
// date formatter constants now in a separate class
//
// Revision 1.16  2002/09/23 11:27:36  jano
// last changes with images and CSS
//
// Revision 1.15  2002/09/20 14:36:56  jano
// tidy up images
//
// Revision 1.14  2002/09/10 08:22:18  jano
// fixing getCountryCode
//
// Revision 1.13  2002/08/27 08:44:09  colin
// split tags and themes into two separate includes
//
// Revision 1.12  2002/08/15 13:46:51  jano
// <No Comment Entered>
//
// Revision 1.11  2002/08/13 09:11:33  jano
// tidy comments
//
////////////////////////////////////////////////////////////////////////////////
--%>

<%@include file='/include/tags.jspf'%>
<igw:checkLogin />
<%@include file='/calendar/include/calendar.jspf'%>
<%@include file='/calendar/include/views.jspf'%>

<igw:bean id='eventsForDay' name='calendarIndexForm' property='events[0]' type='java.util.Map'/>
<%
  thisDay = calendarIndexForm.getCurrentDay();
  theDayAfter.setTime(thisDay.getTime());
  theDayAfter.set(GregorianCalendar.DAY_OF_YEAR, theDayAfter.get(GregorianCalendar.DAY_OF_YEAR) + 1);
%>

<imtheme:frame>

  <%-- pane with date --%>
  <imtheme:framePane>
  <div class='calendartitle'>
  <%=dateFormatter.format(thisDay.getTime())%>
  </div>
  </imtheme:framePane>

  <%-- pane with allday events --%>
  <imtheme:framePane>
    <table border='0' cellpadding='0' cellspacing='0' width='100%'>
      <tr>
        <td class='eventDate' colspan='3'><bean:message bundle='calendar' key='dayview.label.allDayEvent' /></td>
      </tr>
      <%-- go thro' all the all-day events --%>
      <c:forEach var='allDayEvent' items='<%=calendarIndexForm.getAllDayEvents()[0]%>'>
        <igw:bean id='allDayEvent' type='com.ivata.groupware.business.calendar.event.EventDO'/>
        <tr>
          <%-- choose the logo to suit the type --%>
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
            <a href='' onclick='<%=eventPopUp.toString() + "return false"%>'><%=characterFormatter.format(allDayEvent.getSubject())%></a>
          </td>
          <td class='eventAllDay'>
          <c:choose>
            <%-- extra info for public holidays goes here --%>
            <c:when test ='<%=allDayEvent instanceof PublicHolidayDO%>'>
               <%PublicHolidayDO publicHoliday = (PublicHolidayDO) allDayEvent;%>
               <bean:message bundle='calendar' key='dayview.label.publicHoliday'/>
               <c:if test='<%=publicHoliday.getCountry() != null %>'>
                  <bean:message bundle='calendar' key='dayview.label.forCountry'/><%=publicHoliday.getCountry().getCode()%>
                  <c:if test='<%= !StringHandling.isNullOrEmpty(publicHoliday.getRegionCode()) %>'>
                     , <%=publicHoliday.getRegionCode()%>
                  </c:if>
               </c:if>
            </c:when>
            <%-- time info for normal events which just overrun this day goes here --%>
            <c:when test ='<%=allDayEvent.getClass().getName().equals(EventDO.class.getName()) && !allDayEvent.isAllDayEvent()%>'>
              <%=dateFormatter.format(allDayEvent.getStart().getTime())%> <%=(allDayEvent.getFinish()!=null) ? (" -- " + dateFormatter.format(allDayEvent.getFinish().getTime())) : "" %>
            </c:when>
          </c:choose>
          </td>
        </tr>
        <%-- description takes up the full next row --%>
        <c:if test='<%=!StringHandling.isNullOrEmpty(allDayEvent.getDescription())%>'>
          <tr>
            <td colspan='3' class='eventAllDay'><%=characterFormatter.format(allDayEvent.getDescription())%></td>
          </tr>
        </c:if>
      </c:forEach>
    </table>
  </imtheme:framePane>

  <%-- pane with day events --%>
  <imtheme:framePane>
    <table border='0' cellpadding='2' cellspacing='0' width='100%'>
      <c:forEach var='hourOfDay' begin='<%=calendarIndexForm.getDayStartHour()%>'
              end='<%=calendarIndexForm.getDayFinishHour()%>'>
        <igw:bean id='hourOfDay' type='java.lang.Integer'/>

        <%-- alternate lines get a different color --%>
        <c:choose>
          <c:when test='<%=((hourOfDay.intValue() - starthour) % 2) == 1 %>'>
            <tr class='eventShadow'>
          </c:when>
          <c:otherwise>
            <tr class='eventNormal'>
          </c:otherwise>
        </c:choose>
        <%--tr INDENT FROM HERE BECAUSE OF tr ABOVE!! --%>
          <%-- TODO: we should take the user's time format into consideration here!! --%>
          <td class='eventTime' width='40'><%=hourOfDay%>:00</td>

          <%-- are there any events for this day at this hour? --%>
          <%TreeSet eventsForHour = (TreeSet) eventsForDay.get(hourOfDay);%>


          <%-- go thro' each day and display the events for this hour in each one --%>
          <td class='eventNormal' width='100%'>
            <%-- if there are no events, just show a space --%>
            <c:if test='<%=eventsForHour == null%>'>
              <%-- stop the loop crashing --%>
              <%eventsForHour = new TreeSet();%>
              &nbsp;
            </c:if>
            <c:forEach var='event' items='<%=eventsForHour%>'>
              <igw:bean id='event' type='com.ivata.groupware.business.calendar.event.EventDO'/>
              <table cellpadding='0' cellspacing='0' border='0'>
                <tr>

                  <%-- before first put a icon of event --%>
                  <c:choose>
                      <%-- Meeting --%>
                      <c:when test='<%=event instanceof MeetingDO%>'>
                        <td class='eventsAtHour' width='20' align='left'><html:img page='/calendar/images/meeting.gif' width='12' height='23'/></td>
                      </c:when>

                      <%-- Event --%>
                      <%-- if starts today --%>
                      <c:when test='<%=(event.getFinish() != null) && !event.getFinish().before(theDayAfter)%>'>
                        <td width='20' class='eventsAtHour' align='left'><html:img page='/calendar/images/start.gif' width='10' height='10'/></td>
                      </c:when>

                      <%-- if ends today --%>
                      <c:when test='<%=event.getStart().before(thisDay)%>'>
                        <td width='20' class='eventsAtHour' align='left'><html:img page='/calendar/images/ciel.gif' width='10' height='10'/></td>
                      </c:when>

                      <c:otherwise>
                        <td width='20' class='eventsAtHour' align='left'><html:img page='/calendar/images/event.gif' width='12' height='6'/></td>
                      </c:otherwise>
                  </c:choose>

                  <%-- first cell contains the time --%>
                  <td>
                    <c:choose>
                      <%--
                        does this event start before the current day? if so just show
                        the time it ends at
                      --%>
                      <c:when test='<%=event.getStart().before(thisDay)%>'>
                        <bean:message bundle='calendar' key='dayview.label.endsAt' />
                      </c:when>
                      <c:otherwise>
                        <%--
                          if this events finishes after today, show a label to say when
                          it starts
                        --%>
                        <c:if test='<%=(event.getFinish() != null)
                                && !event.getFinish().before(theDayAfter)%>'>
                          <bean:message bundle='calendar' key='dayview.label.startsAt' />
                        </c:if>
                        <%=timeFormatter.format(event.getStart().getTime())%>
                        <%--
                          if this events finishes today, show a dash between the times
                        --%>
                        <c:if test='<%=(event.getFinish() != null)
                                && event.getFinish().before(theDayAfter)%>'>&minus;</c:if>
                      </c:otherwise>
                    </c:choose>
                    <%-- only show finish times for events which finish today --%>
                    <c:if test='<%=(event.getFinish()!=null)
                            && event.getFinish().before(theDayAfter)%>'>
                      <%=timeFormatter.format(event.getFinish().getTime())%>
                    </c:if>
                  </td>
                  <%-- second cell contains the link --%>
                  <imutil:mapEntry mapName='eventParams' name='id' value='<%=event.getId().toString()%>'/>
                  <td><a href='' onclick='<%=eventPopUp.toString()%>return false'><%=characterFormatter.format(event.getSubject())%></a></td>
                </tr>
                <%-- who entered the event? --%>
                <tr>
                <c:choose>
                  <%-- if the event was modified, show that too --%>
                  <c:when test='<%!event.getCreatedBy().equals(event.getModifiedBy())%>'>
                    <td colspan='3' class='eventEnteredBy'><bean:message bundle='calendar' key='dayview.label.enteredBy.modified' arg0='<%=dateTimeFormatter.format(event.getCreated())%>' arg1='<%=event.getCreatedBy().getName()%>' arg2='<%=dateTimeFormatter.format(event.getModified())%>' arg3='<%=event.getModifiedBy().getName()%>'/></td>
                  </c:when>
                  <c:otherwise>
                    <td colspan='3' class='eventEnteredBy'><bean:message bundle='calendar' key='dayview.label.enteredBy' arg0='<%=dateTimeFormatter.format(event.getCreated())%>' arg1='<%=event.getCreatedBy().getName()%>'/></td>
                  </c:otherwise>
                </c:choose>
                </tr>
                <%-- show the description row if the description has been entered --%>
                <c:if test='<%=!StringHandling.isNullOrEmpty(event.getDescription())%>'>
                  <tr>
                    <td colspan='3'><%=characterFormatter.format(event.getDescription())%></td>
                  </tr>
                </c:if>
              </table>
            </c:forEach>
          </td>
        </tr>
      </c:forEach>
    </table>
  </imtheme:framePane>
</imtheme:frame>
