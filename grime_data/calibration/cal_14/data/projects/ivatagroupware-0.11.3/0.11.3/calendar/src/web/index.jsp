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

<%@page import='org.apache.struts.Globals' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: index.jsp,v 1.4 2005/04/28 18:47:09 colinmacleod Exp $
//
// The main listing of the calendar. It's including views jsp files depend on
// view parameter.
//
// Since: ivata groupware 0.9 (2002-06-19)
// Author: Colin MacLeod <colin.macleod@ivata.com>,jano
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
// Revision 1.1.1.1  2005/03/10 17:47:49  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.5  2004/12/27 14:52:00  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.4  2004/07/14 22:50:22  colinmacleod
// Replaced bean:define with extended igw:bean.
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
// Revision 1.30  2003/04/04 11:27:20  jano
// itle for favorities
//
// Revision 1.29  2003/03/04 19:06:31  colin
// fixed prompt keys
//
// Revision 1.28  2003/02/25 07:30:03  colin
// restructured java file paths
//
// Revision 1.27  2003/02/13 08:45:25  colin
// conversion to Struts/popups
//
// Revision 1.26  2003/01/27 07:22:54  colin
// updated copyright notice
//
// Revision 1.25  2003/01/23 16:52:35  peter
// links and forms changed to sslext, checkLogin added
//
// Revision 1.24  2003/01/18 20:38:58  colin
// added help buttons
//
// Revision 1.23  2003/01/14 10:39:53  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.22  2002/11/25 08:54:38  peter
// internationalisation
//
// Revision 1.21  2002/09/24 13:30:41  jano
// fixing bug with lastview
//
// Revision 1.20  2002/09/13 08:18:39  jano
// when you first time calling index.jsp with view parameter equal to "lastview"
//
// Revision 1.19  2002/09/03 12:08:32  jano
// moving igw:headtag to begining of page
//
// Revision 1.18  2002/08/27 08:44:09  colin
// split tags and themes into two separate includes
//
// Revision 1.17  2002/08/16 11:08:47  jano
// *** empty log message ***
//
// Revision 1.16  2002/08/15 11:33:34  jano
// *** empty log message ***
//
// Revision 1.15  2002/08/13 09:12:58  jano
// change algorimtus of including views and mask
//
// Revision 1.14  2002/08/07 16:16:20  jano
// *** empty log message ***
//
// Revision 1.13  2002/08/06 06:09:14  jano
// fixing public_holiday
//
// Revision 1.12  2002/08/02 08:32:26  jano
// fixing problems and tidy code
//
// Revision 1.11  2002/08/01 14:39:47  jano
// fixing bugs and added Masks(add amend/remove)
//
// Revision 1.10  2002/07/25 05:50:24  jano
// fixing bugs
//
// Revision 1.9  2002/07/22 13:00:10  jano
// changes DATE to GregorianCalendar in EventDO
//
// Revision 1.8  2002/07/19 11:42:19  jano
// fixing BUG and make logic for first day at week (SUNDAY, MONDAY)
//
// Revision 1.7  2002/07/19 09:00:53  jano
// split code too dayview.jsp and weekview.jsp
//
// Revision 1.6  2002/07/15 12:33:18  jano
// finish DayView but I have to tidy the code
//
// Revision 1.5  2002/07/12 15:30:49  jano
// finishing with day view, next / previous day
//
// Revision 1.4  2002/07/10 05:53:49  jano
// start of making day view for calendar
//
// Revision 1.3  2002/07/04 12:09:43  jano
// *** empty log message ***
//
// Revision 1.2  2002/07/03 15:26:06  jano
// litle corection and one test
//
// Revision 1.1  2002/07/01 11:17:57  colin
// first basic framework with toolbar
//
////////////////////////////////////////////////////////////////////////////////
--%>

<%@include file='/include/tags.jspf'%>
<igw:checkLogin />
<%@include file='/include/theme.jspf'%>

<%-- if there is no view, this is no use to us! --%>
<logic:notPresent name='calendarIndexForm'>
  <logic:forward name='calendarIndexAction'/>
</logic:notPresent>
<logic:notPresent name='calendarIndexForm' property='view'>
  <logic:forward name='calendarIndexAction'/>
</logic:notPresent>
<igw:bean id='calendarIndexForm' name='calendarIndexForm' scope='session' type='com.ivata.groupware.business.calendar.struts.IndexForm'/>

<imhtml:html locale='true'>
  <igw:head title='calendar'><imhtml:base/></igw:head>

  <body>
    <div align='center'>
      <imtheme:window
          styleClass='mainWindow'
          bundle='calendar'
          titleKey='index.title'>
        <%@include file='/calendar/include/toolbar.jspf'%>
        <jsp:include page='<%=calendarIndexForm.getViewPage()%>'/>

        <imtheme:buttonFrame>
          <imhtml:help key='<%=calendarIndexForm.getHelpKey()%>'/>
        </imtheme:buttonFrame>
      </imtheme:window>
    </div>
  </body>
</imhtml:html>
