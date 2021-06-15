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

<%@page import='com.ivata.groupware.business.calendar.struts.*' %>
<%@page import='java.util.*' %>
<%@page import='org.apache.struts.Globals' %>
<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: monthview.jsp,v 1.4 2005/04/28 18:47:09 colinmacleod Exp $
//
// Monthview of calendar events.
//
// Since: ivata groupware 0.9 (2002-07-15)
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
// $Log: monthview.jsp,v $
// Revision 1.4  2005/04/28 18:47:09  colinmacleod
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
// Revision 1.1.1.1  2005/03/10 17:47:47  colinmacleod
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
// Revision 1.24  2003/05/06 13:42:45  jano
// I forgot remove systemout.println
//
// Revision 1.23  2003/05/06 13:34:11  jano
// we have to take thisMonth from currentDay not from firstWeekDay
//
// Revision 1.22  2003/04/03 13:25:32  jano
// fixing currentDay , again
//
// Revision 1.21  2003/03/17 10:10:00  jano
// fixing:
// -TimeZone bug
// -displaying today day - red color, current day - dark orange
//
// Revision 1.20  2003/03/04 19:06:31  colin
// fixed prompt keys
//
// Revision 1.19  2003/02/25 07:30:03  colin
// restructured java file paths
//
// Revision 1.18  2003/02/13 08:45:25  colin
// conversion to Struts/popups
//
// Revision 1.17  2003/01/27 07:22:54  colin
// updated copyright notice
//
// Revision 1.16  2003/01/23 16:52:36  peter
// links and forms changed to sslext, checkLogin added
//
// Revision 1.15  2002/12/18 14:40:20  peter
// format for week-days fixed
//
// Revision 1.14  2002/12/18 14:33:12  peter
// added   import org.apache.struts.Globals
//
// Revision 1.13  2002/12/18 13:54:25  peter
// names of days locale-aware
//
// Revision 1.12  2002/11/25 08:54:38  peter
// internationalisation
//
// Revision 1.11  2002/10/18 09:49:12  jano
// fixing with of colums
//
// Revision 1.10  2002/09/24 12:20:14  jano
// today has diffrent backgound
// fixing names of class in CSS
//
// Revision 1.9  2002/09/23 11:35:28  jano
// last changes with images and CSS
//
// Revision 1.8  2002/08/27 08:44:09  colin
// split tags and themes into two separate includes
//
// Revision 1.7  2002/08/13 09:13:19  jano
// change algorimtus of including views and mask
//
--%>

<%@include file='/include/tags.jspf'%>

<%@include file='/calendar/include/calendar.jspf'%>
<%@include file='/calendar/include/views.jspf'%>

<igw:bean id='formatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter'/>
<igw:bean id='maximumLengthMonth' scope='page' type='com.ivata.mask.web.format.MaximumLengthFormat'/>
<jsp:setProperty name='maximumLengthMonth' property='maximumLength' value='25'/>
<imformat:addFormat formatter='<%=formatter%>' format='<%=maximumLengthMonth%>'/>

<%-- format names of months --%>
<% java.text.SimpleDateFormat nameOfMonth = new java.text.SimpleDateFormat("MMMMM yyyy",
                (java.util.Locale)session.getAttribute(Globals.LOCALE_KEY));
  nameOfMonth.setTimeZone(TimeZone.getTimeZone(i18nTimeZone));
%>
<%-- format names of days --%>
<% java.text.SimpleDateFormat nameOfDay = new java.text.SimpleDateFormat("EEEE",
                (java.util.Locale)session.getAttribute(Globals.LOCALE_KEY));
  nameOfDay.setTimeZone(TimeZone.getTimeZone(i18nTimeZone));
%>

<%
  thisDay.setTime(calendarIndexForm.getCurrentDay().getTime());
  int thisMonth = thisDay.get(java.util.Calendar.MONTH);
%>

<imtheme:frame>

  <%-- pane with text which month is display --%>
  <imtheme:framePane styleClass='hilight'>
  <div class='calendartitle'>
    <%=nameOfMonth.format(thisDay.getTime()).toString()%>
    <%--=day.get(GregorianCalendar.YEAR)--%>
  </div>
  </imtheme:framePane>

  <%-- pane with all days and events for those days in month --%>
  <imtheme:framePane styleClass='hilight'>
    <table border='0' cellpadding='0' cellspacing='0' align="center" width='100%'>
      <%-- first line of the table with DAYS --%>
      <tr>
        <td class='eventTitleLine'  width='1'><html:img page='/images/empty.gif' width='1' height='1'/></td>
        <td class='eventDate' width='40'><bean:message bundle='calendar' key='monthview.label.week' /></td>
        <td class='eventTitleLine'  width='1'><html:img page='/images/empty.gif' width='1' height='1'/></td>

        <%
          thisDay.setTime(calendarIndexForm.getFirstWeekDay().getTime());
        %>

        <c:forEach var='weekday' begin='0' end='6'>
           <td class='eventDate' width='14%'><%=nameOfDay.format(thisDay.getTime()).toString()%></td>
           <td class='eventTitleLine'  width='1'><html:img page='/images/empty.gif' width='1' height='1'/></td>
           <% thisDay.set(java.util.Calendar.DAY_OF_YEAR,
                  thisDay.get(java.util.Calendar.DAY_OF_YEAR) + 1); %>
        </c:forEach>
      </tr>
      <tr><td class='eventLine' colspan='17' height='1'><html:img page='/images/empty.gif' width='1' height='1'/></td></tr>



      <%-- set up the first day at view to monday --%>
      <% thisDay.setTime(calendarIndexForm.getFirstWeekDay().getTime()); %>

      <% nameOfDay.applyPattern("d"); %>

      <c:forEach var='eventsForWeek' items='<%=calendarIndexForm.getEvents()%>'>
        <igw:bean id='eventsForWeek' type='java.util.Map'/>
        <%-- at on row I will display 7 days --%>
        <tr height='80'>
          <td class='eventLine'  width='1'><html:img page='/images/empty.gif' width='1' height='1'/></td>
          <td class='eventWeek' width='40'>
            <imutil:map id='indexParams'>
              <imutil:mapEntry name='view' value='<%=IndexFormConstants.VIEW_WEEK.toString()%>'/>
              <imutil:mapEntry name='day' value='<%=new Integer(thisDay.get(java.util.Calendar.DATE)).toString()%>'/>
              <imutil:mapEntry name='month' value='<%=new Integer(thisDay.get(java.util.Calendar.MONTH)).toString()%>'/>
              <imutil:mapEntry name='year' value='<%=new Integer(thisDay.get(java.util.Calendar.YEAR)).toString()%>'/>
            </imutil:map>
            <imhtml:link page='/calendar/index.action' name='indexParams' styleClass='calendarWeek'><%=thisDay.get(java.util.Calendar.WEEK_OF_YEAR)%></imhtml:link>
          </td>
          <td class='eventLine'  width='1'><html:img page='/images/empty.gif' width='1' height='1'/></td>

          <c:forEach var='dayOfWeek' begin='0' end='6'>
            <igw:bean id='dayOfWeek' type='java.lang.Integer'/>
            <%TreeSet eventsForDay = (TreeSet) eventsForWeek.get(dayOfWeek);%>
            <c:choose>
              <%--is thisDay in the previous or next month? --%>
              <c:when test='<%=eventsForDay == null%>'>
                <td class='eventOtherMonthDay' >&nbsp</td>
              </c:when>
              <%-- thisDay is in this month and we have valid events --%>
              <c:otherwise>
                <%-- this is inside of rectangle ********************************************* --%>
                <c:choose>
                  <c:when test='<%=thisDay.equals(today)%>'>
                    <td class='eventToday' width='14%'>
                  </c:when>
                  <c:when test='<%=calendarIndexForm.getCurrentDay().equals(thisDay)%>'>
                    <td class='eventCurrentDay' width='14%'>
                  </c:when>
                  <c:otherwise>
                    <td class='eventDay' width='14%'>
                  </c:otherwise>
                </c:choose>
                <%--td INDENT FROM HERE BECAUSE OF td ABOVE --%>
                  <imutil:map id='indexParams'>
                    <imutil:mapEntry name='view' value='<%=IndexFormConstants.VIEW_DAY.toString()%>'/>
                    <imutil:mapEntry name='day' value='<%=new Integer(thisDay.get(java.util.Calendar.DATE)).toString()%>'/>
                    <imutil:mapEntry name='month' value='<%=new Integer(thisDay.get(java.util.Calendar.MONTH)).toString()%>'/>
                    <imutil:mapEntry name='year' value='<%=new Integer(thisDay.get(java.util.Calendar.YEAR)).toString()%>'/>
                  </imutil:map>
                  <imhtml:link page='/calendar/index.action' name='indexParams' styleClass='calendarDay'><%=nameOfDay.format(thisDay.getTime()).toString()%></imhtml:link>
                  <c:forEach var='event' items='<%=eventsForDay%>'>
                    <igw:bean id='event' type='com.ivata.groupware.business.calendar.event.EventDO'/>
                    <br/>
                    <imformat:format formatter='<%=formatter%>'><%=event.getSubject()%></imformat:format>
                  </c:forEach>
                </td>
                <%-- ************************************************************************* --%>
              </c:otherwise>
            </c:choose>
            <td class='eventLine'  width='1'><html:img page='/images/empty.gif' width='1' height='1'/></td>

            <%-- go forward one day --%>
            <% thisDay.set(java.util.Calendar.DAY_OF_YEAR,
                            thisDay.get(java.util.Calendar.DAY_OF_YEAR)+1); %>
          </c:forEach><%-- day of week --%>
        </tr>
        <tr><td class='eventLine' colspan='17' height='1'><html:img page='/images/empty.gif' width='1' height='1'/></td></tr>
      </c:forEach><%-- events of week --%>
    </table>
  </imtheme:framePane>
</imtheme:frame>
