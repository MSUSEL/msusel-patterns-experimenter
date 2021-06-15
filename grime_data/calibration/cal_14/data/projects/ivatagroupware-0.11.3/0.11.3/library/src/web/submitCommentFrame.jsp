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
<%@page contentType='text/html;charset=UTF-8'%>

<%@page import='java.util.Arrays' %>
<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: submitCommentFrame.jsp,v 1.3 2005/04/28 18:47:07 colinmacleod Exp $
//
// Display a frame around the submit comment editing fields and the submit
// comment summary.
//
// Since: ivata groupware 0.9 (2003-02-23)
// Author: Colin MacLeod <colin.macleod@ivata.com>
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
// $Log: submitCommentFrame.jsp,v $
// Revision 1.3  2005/04/28 18:47:07  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.2  2005/04/09 17:19:48  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:52:07  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.6  2004/11/12 15:57:17  colinmacleod
// Removed dependencies on SSLEXT.
// Moved Persistence classes to ivata masks.
//
// Revision 1.5  2004/07/31 11:14:00  colinmacleod
// Updated to Pico/Hibernate.
//
// Revision 1.4  2004/07/14 22:50:25  colinmacleod
// Replaced bean:define with extended igw:bean.
//
// Revision 1.3  2004/07/14 20:59:54  colinmacleod
// Changed all occurrences of jsp:useBean to igw:bean (better tomcat compatibility).
//
// Revision 1.2  2004/03/21 21:16:30  colinmacleod
// Shortened name to ivata op.
//
// Revision 1.1  2004/03/03 21:50:02  colinmacleod
// Updated webapp structure. Moved core files to core subproject.
//
// Revision 1.2  2004/02/10 22:09:29  colinmacleod
// Turned off SSL
//
// Revision 1.1.1.1  2004/01/27 20:58:46  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.4  2004/01/12 14:01:13  jano
// fixing bugs
//
// Revision 1.3  2003/10/28 13:16:15  jano
// commiting library,
// still fixing compile and building openPortal project
//
// Revision 1.2  2003/10/15 14:16:54  colin
// fixing for XDoclet
//
// Revision 1.2  2003/02/28 13:43:50  colin
// made comments work with popups
//
// Revision 1.1  2003/02/25 07:28:15  colin
// converted display & submission to struts
// started converting comments to popup
// restructured java paths
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<igw:checkLogin />
<%@include file='/include/theme.jspf'%>



<%-- if the form has not been set, then go off to the action to set it --%>
<logic:notPresent name='libraryCommentForm'>
  <logic:redirect forward='libraryCommentAction'/>
</logic:notPresent>

<igw:bean id='libraryCommentForm' scope='session' type='com.ivata.groupware.business.library.struts.CommentForm'/>
<igw:bean id='comment' name='libraryCommentForm' property='comment' type='com.ivata.groupware.business.library.comment.CommentDO'/>

<%-- if the item has not been set, then go off to the action to set it --%>
<logic:notPresent name='comment' property='item'>
  <logic:redirect forward='libraryCommentAction'/>
</logic:notPresent>

<igw:bean id='itemTitle' name='comment' property='item.title' type='java.lang.String'/>

<imhtml:html locale='true'>
  <imhtml:base/>
  <igw:head bundle='library'
             titleKey='submitCommentFrame.title'
             titleArgs='<%=Arrays.asList(new Object[] {itemTitle})%>'
             topLevel='true'>
    <imhtml:base/>
  </igw:head>
  <igw:bean id='commentUri'><imhtml:rewrite page='/library/submitComment.jsp'/></igw:bean>
  <igw:bean id='commentPreviewUri'><imhtml:rewrite page='/library/submitCommentPreview.jsp'/></igw:bean>
  <frameset cols="*, 300">
    <frame src="<%=commentUri%>" name="ivataComment" border='0' marginheight='0' marginwidth='0' frameborder='yes' scrolling='no'/>
    <frame src='<%=commentPreviewUri%>' name='ivataCommentPreview' border='0' marginheight='0' marginwidth='10' frameborder='no' scrolling='yes'/>
    <noframes>
      <bean:message key='alert.noframes' />
    </noframes>
  </frameset>
</imhtml:html>
