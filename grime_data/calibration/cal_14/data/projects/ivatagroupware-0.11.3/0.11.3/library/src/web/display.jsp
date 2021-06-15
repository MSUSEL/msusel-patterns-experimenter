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
<%@page import='com.ivata.groupware.business.addressbook.person.group.right.RightConstants' %>
<%@page import='com.ivata.groupware.business.library.item.LibraryItemConstants' %>
<%@page import='com.ivata.mask.util.StringHandling' %>
<%@page import='java.util.Collection' %>

<%@page import='org.apache.struts.Globals' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: display.jsp,v 1.5 2005/04/28 18:47:07 colinmacleod Exp $
//
// Display a single item from the library.
//
// Since: ivata groupware 0.9 (2002-06-22)
// Author: Colin MacLeod <colin.macleod@ivata.com>, laco
// $Revision: 1.5 $
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
// $Log: display.jsp,v $
// Revision 1.5  2005/04/28 18:47:07  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.4  2005/04/26 15:28:15  colinmacleod
// Removed the line resetting the FAQ theme.
//
// Revision 1.3  2005/04/10 20:10:11  colinmacleod
// Added new themes.
// Changed id type to String.
// Changed i tag to em and b tag to strong.
// Improved PicoContainerFactory with NanoContainer scripts.
//
// Revision 1.2  2005/04/09 17:19:48  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:52:12  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.8  2004/12/31 18:50:32  colinmacleod
// Removed unused ExceptionHandling import.
//
// Revision 1.7  2004/12/27 14:52:01  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.6  2004/11/03 15:55:46  colinmacleod
// Changed todo comments to TODO: all caps.
//
// Revision 1.5  2004/07/14 22:50:25  colinmacleod
// Replaced bean:define with extended igw:bean.
//
// Revision 1.4  2004/07/14 20:59:53  colinmacleod
// Changed all occurrences of jsp:useBean to igw:bean (better tomcat compatibility).
//
// Revision 1.3  2004/07/13 19:47:30  colinmacleod
// Moved project to POJOs from EJBs.
// Applied PicoContainer to services layer (replacing session EJBs).
// Applied Hibernate to persistence layer (replacing entity EJBs).
//
// Revision 1.2  2004/03/21 21:16:30  colinmacleod
// Shortened name to ivata op.
//
// Revision 1.1  2004/03/03 21:50:02  colinmacleod
// Updated webapp structure. Moved core files to core subproject.
//
// Revision 1.1.1.1  2004/01/27 20:58:45  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.3  2003/10/28 13:16:15  jano
// commiting library,
// still fixing compile and building openPortal project
//
// Revision 1.2  2003/10/15 14:16:54  colin
// fixing for XDoclet
//
// Revision 1.26  2003/07/08 07:19:48  peter
// html upload and parser changes
//
// Revision 1.25  2003/07/03 09:21:41  peter
// fixed embeddedImagesFormatter
//
// Revision 1.24  2003/07/02 11:31:10  peter
// added embeddedImages formatter
//
// Revision 1.23  2003/06/03 05:07:15  peter
// changes due to posibility to change filelist from display mode
//
// Revision 1.22  2003/05/20 08:13:18  jano
// maintaing attaching files to libray item
//
// Revision 1.21  2003/05/12 14:52:42  jano
// comenting displayFile for this moment
//
// Revision 1.20  2003/05/07 14:00:19  jano
// caming back to files attachents of libraryItem
//
// Revision 1.19  2003/04/01 11:33:07  jano
// set title of page to title of item
//
// Revision 1.18  2003/02/28 07:31:52  colin
// implemented editing/displaying of faqs & notes
//
// Revision 1.17  2003/02/25 07:28:15  colin
// converted display & submission to struts
// started converting comments to popup
// restructured java paths
//
// Revision 1.16  2003/01/24 19:35:18  peter
// changes to sslext
//
// Revision 1.15  2003/01/24 07:29:07  colin
// added checkLogin tag
//
// Revision 1.14  2003/01/22 07:09:39  peter
// displaying the filelist remmed out
//
// Revision 1.13  2003/01/14 10:42:19  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.12  2003/01/07 13:09:51  peter
// added include: displayFile.jsp
//
// Revision 1.11  2002/11/27 18:00:35  peter
// i18n finished
//
// Revision 1.10  2002/10/14 15:55:27  colin
// added a warning message for meeting minutes on deletion
//
// Revision 1.9  2002/10/08 16:21:52  colin
// corrected delete handling
//
// Revision 1.8  2002/09/11 10:59:42  jano
// small change in name of parameter meeting => meetingId
//
// Revision 1.7  2002/09/10 09:41:28  jano
// fixing ling from calendar - meeting
//
// Revision 1.6  2002/09/04 16:08:23  colin
// split off display functionality into displayItem.jsp so it can be used for previuews
//
// Revision 1.5  2002/08/27 08:44:20  colin
// split tags and themes into two separate includes
//
// Revision 1.4  2002/08/11 12:06:34  colin
// Included displayComment.jsp.
//
// Revision 1.3  2002/07/04 15:21:47  laco
// added checking to see which document type is being displayed
// added functionality to display FAQ types
//
// Revision 1.2  2002/07/01 08:07:56  colin
// moved SetPropertyTag to util from webgui
//
// Revision 1.1  2002/06/28 13:16:59  colin
// first library release
//
// Revision 1.3  2002/06/19 14:08:56  laco
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<%@include file='/include/theme.jspf'%>

<igw:checkLogin forward='libraryIndex'/>
<logic:notPresent name='libraryItemForm'>
  <logic:forward name='libraryDisplayAction'/>
</logic:notPresent>
<logic:notPresent name='libraryItemForm' property='item'>
  <logic:forward name='libraryDisplayAction'/>
</logic:notPresent>
<logic:notPresent name='libraryItemForm' property='item.id'>
  <logic:forward name='libraryDisplayAction'/>
</logic:notPresent>

<igw:bean id='libraryItemForm' scope='session' type='com.ivata.groupware.business.library.struts.ItemForm'/>
<igw:bean id='item' name='libraryItemForm' property='item' type='com.ivata.groupware.business.library.item.LibraryItemDO'/>


<imhtml:html locale='true'>
  <igw:head title='<%=item.getTitle()!=null?item.getTitle():""%>'><imhtml:base/></igw:head>
  <body>
    <div align='center'>
      <imhtml:form action='/library/display' bundle='library' resourceFieldPath='displayItem' referralName='refLibraryItemForm'>
        <input type='hidden' name='attach' value='' />
        <jsp:include page='/library/displayItem.jsp'/>

        <%-- TODO
        <c:if test='<%=request.getParameter("deleteWarn") == null%>'>
          <br/>
          <jsp:include page='/library/displayFile.jsp'/>
        </c:if>
        --%>
      </imhtml:form>

      <logic:notPresent parameter='deleteWarn'>
        <br/>
        <jsp:include page='/library/displayComment.jsp'/>
      </logic:notPresent>
    </div>
    <logic:present parameter='print'>
      <script type='text/javascript'>
        window.print();
        window.close();
      </script>
    </logic:present>
  </body>
</imhtml:html>
