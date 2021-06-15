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

<%@page import='com.ivata.groupware.business.library.page.PageDO' %>
<%@page import='com.ivata.mask.util.StringHandling' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: displayDocument.jsp,v 1.3 2005/04/27 15:11:28 colinmacleod Exp $
//
// Display a single document item from the library.
//
// Since: ivata groupware 0.9 (2002-07-02)
// Author: Laco Kacani
// $Revision: 1.3 $
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
// $Log: displayDocument.jsp,v $
// Revision 1.3  2005/04/27 15:11:28  colinmacleod
// Fixed for printing pages.
//
// Revision 1.2  2005/04/09 17:19:48  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:52:12  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.6  2004/07/29 20:52:01  colinmacleod
// Fixed case when page text is null.
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
// Revision 1.13  2003/07/08 07:19:48  peter
// html upload and parser changes
//
// Revision 1.12  2003/07/02 11:32:26  peter
// embedded images
//
// Revision 1.11  2003/05/20 08:13:18  jano
// maintaing attaching files to libray item
//
// Revision 1.10  2003/02/28 07:31:52  colin
// implemented editing/displaying of faqs & notes
//
// Revision 1.9  2003/02/25 07:28:15  colin
// converted display & submission to struts
// started converting comments to popup
// restructured java paths
//
// Revision 1.8  2003/01/24 19:35:18  peter
// changes to sslext
//
// Revision 1.7  2003/01/14 10:42:19  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.6  2002/11/27 18:00:35  peter
// i18n finished
//
// Revision 1.5  2002/10/14 15:56:10  colin
// made the next/previous page use getServletPath (instead of hard-coded display.jsp)
//
// Revision 1.4  2002/09/25 11:30:14  colin
// implemented themes
//
// Revision 1.3  2002/08/27 08:44:20  colin
// split tags and themes into two separate includes
//
// Revision 1.2  2002/08/11 12:08:04  colin
// Included properties.jsp.
//
// Revision 1.1  2002/07/04 15:21:47  laco
// added checking to see which document type is being displayed
// added functionality to display FAQ types
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<%@include file='/library/theme/document.jspf'%>
<%@include file='/include/theme.jspf'%>


<igw:bean id='libraryItemForm' scope='session' type='com.ivata.groupware.business.library.struts.ItemForm'/>
<igw:bean id='item' name='libraryItemForm' property='item' type='com.ivata.groupware.business.library.item.LibraryItemDO'/>
<igw:bean id='fileList' name='libraryItemForm' property='fileList' type='java.util.List'/>

<%-- set up in display.jsp --%>
<igw:bean id='embeddedImagesFormatter' scope='request' type='com.ivata.mask.web.format.HTMLFormatter'/>

<%int pageNumber = libraryItemForm.getDisplayPage();%>

<c:choose>
  <%-- if we are printing, put all pages into the text --%>
  <c:when test='<%=request.getParameter("print") != null%>'>
	  <logic:iterate indexId='thisIndex' id='thisPage' name='libraryItemForm' property='<%="item.pages"%>' type='com.ivata.groupware.business.library.page.PageDO'>
      <igw:bean id='thisPage' type='com.ivata.groupware.business.library.page.PageDO'/>
      <c:if test='<%=thisIndex.intValue() != 0%>'>
        <hr/>
      </c:if>
	    <div class='pageContents'>
		    <h3>Page <%=thisIndex.intValue() + 1%></h3>
	      <bean:write name='thisPage' property='text' filter='false'/>
	    </div>
    </logic:iterate>  
  </c:when>
  <%-- only show the page text if there is a text to show --%>
  <c:when test='<%=(item.getPages() != null)
          && (item.getPages().size() > 0)
          && !StringHandling.isNullOrEmpty(((PageDO) (item.getPages().get(pageNumber))).getText())%>'>
	  <imtheme:frame themeName='<%=libraryItemForm.getThemeName()%>'>
	    <imtheme:framePane styleClass='normal'>
        <div class='pageContents'>
	        <bean:write name='libraryItemForm'
	          property='<%="item.pages[" + pageNumber + "].text"%>'
	          filter='false'/>
        </div>
        <div class='pageLinks'>
  	      <div class='pageLinksLeft'><%=libraryItemForm.getPreviousPageLink()%></div>
          <div class='pageLinksRight'><%=libraryItemForm.getNextPageLink()%></div>
        </div>
	      </table>
	    </imtheme:framePane>
	  </imtheme:frame>
  </c:when>
</c:choose>


