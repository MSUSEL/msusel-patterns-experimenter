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
<%@page import='com.ivata.groupware.business.calendar.event.publicholiday.PublicHolidayDO' %>
<%@page import='com.ivata.mask.util.StringHandling' %>
<%@page import='java.util.Collection' %>

<%@page import='org.apache.struts.Globals' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: eventDetails.jsp,v 1.2 2005/04/09 17:19:18 colinmacleod Exp $
//
// Contains the details of the main event page, shared by all event types (event,
// meeting and public holiday.)
//
// Since: ivata groupware 0.9 (2003-02-10)
// Author: Colin MacLeod <colin.macleod@ivata.com>
// $Revision: 1.2 $
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
// $Log: eventDetails.jsp,v $
// Revision 1.2  2005/04/09 17:19:18  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:47:48  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.7  2004/12/27 14:52:00  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
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
// Revision 1.12  2003/05/15 09:57:11  peter
// replaced wrong define for useBean (country)
//
// Revision 1.11  2003/05/08 10:33:15  colin
// changes for jetty
//
// Revision 1.10  2003/04/03 12:38:50  jano
// using sorting JSP for sort Countries
//
// Revision 1.9  2003/03/20 15:21:05  jano
// fixing textarea size for IE
//
// Revision 1.8  2003/03/18 10:02:32  peter
// added value for no country selected
//
// Revision 1.7  2003/03/06 17:28:12  peter
// fixed js frame references and location updates
//
// Revision 1.6  2003/03/03 18:45:16  colin
// added missing fieldName attribute in text field
//
// Revision 1.5  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.4  2003/02/26 13:56:58  peter
// added public holiday date checking with javaScript
//
// Revision 1.3  2003/02/25 17:28:33  peter
// fixed date javascript method calls and date-time field sizes
//
// Revision 1.2  2003/02/25 07:30:03  colin
// restructured java file paths
//
// Revision 1.1  2003/02/13 08:45:25  colin
// conversion to Struts/popups
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>

<igw:bean id='calendarEventForm' scope='session' type='com.ivata.groupware.business.calendar.struts.EventForm'/>
<igw:bean id='event' name='calendarEventForm' property='event' type='com.ivata.groupware.business.calendar.event.EventDO'/>
<%
  boolean isPublicHoliday = event instanceof PublicHolidayDO;
  boolean isMeeting = event instanceof MeetingDO;
  boolean isPlainEvent = !(isPublicHoliday || isMeeting);
%>
<%-- meetings appear in tabs - TODO: change this when other types also have tabs --%>
<igw:bean id='frameStyle'><c:choose>
  <c:when test='<%=isMeeting%>'>tabContent</c:when>
  <c:otherwise>dialog</c:otherwise>
</c:choose></igw:bean>


<imhtml:form refersTo='eventForm' resourceFieldPath='event'>
  <imtheme:frame styleClass='<%=frameStyle%>' style='height: 240px;' styleId='none'>
    <imtheme:framePane styleClass='hilight'>
      <table border='0' cellpadding='3' cellspacing='0' width='100%' height='100%'>

        <%-- SUBJECT --%>
        <tr>
          <c:choose>
            <c:when test='<%=isPublicHoliday%>'>
              <td class='fieldCaption'><imhtml:label fieldName='subject' keySuffix='publicHoliday'/></td>
            </c:when>
            <c:otherwise>
              <td class='fieldCaption'><imhtml:label fieldName='subject'/></td>
            </c:otherwise>
          </c:choose>
          <td class='field' colspan='3'>
            <imhtml:text fieldName='subject' mandatory='true' property="event.subject" size='31' maxlength='30'/>
          </td>
        </tr>

        <%-- START DATE/TIME --%>
        <tr>
          <c:choose>
            <c:when test='<%=isPublicHoliday%>'>
              <td class='fieldCaption'><imhtml:label fieldName='startDate' keySuffix='publicHoliday'/></td>
              <td class='field'>
                <imhtml:text fieldName='holidayDate' mandatory='true' property='startDate' onblur='onHolidayDate();' size='12' maxlength='11'/>
              </td>
            </c:when>
            <c:otherwise>
              <td class='fieldCaption'><imhtml:label fieldName='startDate'/></td>
              <td class='field'>
                <imhtml:text fieldName='startDate' mandatory='true' onblur='onStartDate();' size='12' maxlength='11'/>
              </td>
            </c:otherwise>
          </c:choose>
          <c:if test='<%=!isPublicHoliday%>'>
              <td class='fieldCaption'><imhtml:label fieldName='startTime'/></td>
              <td class='field'>
                <input type='hidden' id='tmpStartTime' name='tmpStartTime' value='<%=calendarEventForm.getStartTime()%>'/>
                <imhtml:text fieldName='startTime' onblur='onStartTime();' size='9' maxlength='8'/>
              </td>
          </c:if>
        </tr>

        <c:choose>
          <%-- input for COUNTRY and REGION CODE of public holiday --%>
          <%-- public holidays have no times and are always all day events --%>
          <c:when test='<%=isPublicHoliday%>'>
            <html:hidden property='event.allDayEvent' value='true'/>
            <%-- get the addressBook object for country combo --%>
            <%@include file='/addressBook/include/addressBook.jspf'%>

            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='regionCode'/></td>
              <td class='field' colspan='3'>
                <imhtml:text fieldName='regionCode' property='event.regionCode' size='11' maxlength='10'/>
              </td>
            </tr>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='countryCode'/></td>
              <td class='field' colspan='3'>
                <%--Collection countries = addressBook.findAllCountries();--%>

                <%--
                this is not used
                c:if test='<%= !StringHandling.isNullOrEmpty(event.getCountryCode()) %>'>
                  <%CountryDO titleCountry = addressBook.findCountryByCode(event.getCountryCode());%>
                </c:if--%>

                <%@include file='/addressBook/include/sortCountryKeys.jspf'%>


                <imhtml:select fieldName='countryCode' property='event.countryCode'>
                  <imhtml:option value=''><bean:message key='default.all'/></imhtml:option>
                  <% int i=0; %>
                  <c:forEach var='country' items='<%=countryCodes%>'>
                    <igw:bean id='country' type='java.lang.String'/>
                    <imhtml:option value='<%=country%>'>
                    <bean:message bundle='addressBook' key='<%=(String) countryKeys.get(i)%>'/></imhtml:option>
                    <% i++; %>
                  </c:forEach>
                </imhtml:select>
              </td>
            </tr>
          </c:when>
          <%-- NOT PUBLIC HOLIDAY --%>
          <c:otherwise>
              <%-- END DATE/TIME --%>
              <tr>
                <td class='fieldCaption'><imhtml:label fieldName='finishDate'/></td>
                <td class='field'>
                  <imhtml:text fieldName='finishDate' onblur='onFinishDate();' size='12' maxlength='11'/>
                </td>
                <td class='fieldCaption'><imhtml:label fieldName='finishTime'/></td>
                <td class='field'>
                  <imhtml:text fieldName='finishTime' onblur='onFinishTime()' size='9' maxlength='8' />
                  <input type='hidden' id='tmpFinishTime' name='tmpFinishTime' value='<%=calendarEventForm.getFinishTime()%>'/>
                </td>
              </tr>

              <%-- MEETING LOCATION --%>
              <c:if test='<%=isMeeting%>'>
                <tr>
                    <td class='fieldCaption'><imhtml:label fieldName='location'/></td>
                    <td colspan='3' class='field'><imhtml:text mandatory='true' fieldName='location' property='meeting.location' size='20' maxlength='49'/></td>
                </tr>
              </c:if>

              <%-- ALL DAY EVENT --%>
              <tr>
                <td class='fieldCaption' colspan='4'>
                  <imhtml:checkbox fieldName='allDayEvent' property='event.allDayEvent' onchange='onAllDayEvent();'/>
                </td>
              </tr>
              <script language='javascript'>
                // <!--
                onAllDayEvent();
                // -->
              </script>
          </c:otherwise>
        </c:choose>

        <%-- DESCRIPTION --%>
        <tr>
          <td class='fieldCaption' colspan='4'><br/><imhtml:label fieldName='description'/></td>
        </tr>
        <tr>
          <td class='field' colspan='4' height='100%'>
              <imhtml:textarea fieldName='description' property='event.description'/>
          </td>
        </tr>
      </table>
    </imtheme:framePane>
  </imtheme:frame>
</imhtml:form>
