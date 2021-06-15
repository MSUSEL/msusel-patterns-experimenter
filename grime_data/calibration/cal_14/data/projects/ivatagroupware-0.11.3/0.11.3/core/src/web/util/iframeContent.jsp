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

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: iframeContent.jsp,v 1.3 2005/04/28 18:47:07 colinmacleod Exp $
//
// Simple, blank page which displays a text from the request. This is done so
// the page can used as contents for an iframe tag.
//
// Since: ivata groupware 0.9 (2003-01-25)
// Author: Colin MacLeod <colin.macleod@ivata.com>
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
// $Log: iframeContent.jsp,v $
// Revision 1.3  2005/04/28 18:47:07  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.2  2005/04/09 17:19:40  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1  2005/03/10 19:24:02  colinmacleod
// Moved to ivata groupware.
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
// Revision 1.1  2004/03/03 21:32:35  colinmacleod
// Moved core files to core subproject
//
// Revision 1.1.1.1  2004/01/27 20:58:18  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.2  2003/10/17 12:36:13  jano
// fixing problems with building
// converting intranet -> portal
// Eclipse building
//
// Revision 1.1.1.1  2003/10/13 20:50:50  colin
// Restructured portal into subprojects
//
// Revision 1.5  2003/02/25 07:31:16  colin
// restructured java file paths
//
// Revision 1.4  2003/02/19 10:31:38  peter
// added condition for checkboxes to updateParentField method
//
// Revision 1.3  2003/02/13 08:53:16  colin
// added javascript for copying values from fields on the iframe to the parent form
//
// Revision 1.2  2003/02/11 07:23:36  colin
// restructured content around a session attribute
//
// Revision 1.1  2003/01/27 07:10:24  colin
// first version of iframe handling routines
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>

<igw:checkLogin/>

<igw:bean id='browser' scope='session' type='com.ivata.mask.web.browser.Browser'/>

<%-- only display the full html page if this will be _inside_ an iframe --%>
<igw:bean id='text' type='java.lang.String'/>
<c:if test='<%=request.getParameter("sessionAttribute") != null%>'>
  <%text = (String) session.getAttribute(request.getParameter("sessionAttribute"));%>
</c:if>
<%
  String align = "center";
  String bodyClass = "normal";
%>
<logic:present parameter="align">
  <%align = request.getParameter("align");%>
  <c:if test='<%=align.equals("block")%>'>
    <%bodyClass="dialog";%>
  </c:if>
</logic:present>
<logic:present parameter="class">
    <%bodyClass=request.getParameter("class");%>
</logic:present>
<c:choose>
  <c:when test='<%=browser.canDisplayIFrames()%>'>
    <imhtml:html locale='true'>
      <igw:head>
        <imhtml:base/>
        <script language='javascript'>
          <!--
          <%--
          // This method is used to update hidden fields on the parent page to
          // 'transparently' display fields.
          --%>
          function updateParentField(styleId) {
              var field = document.getElementById(styleId);
              var parentField = parent.document.getElementById(styleId);

              if (field.type == 'checkbox') {
                  if (field.checked==true) {
                    parentField.value = field.value;
                  } else {
                    parentField.value = '';
                  }
              } else {;
                  parentField.value = field.value;
              }
          }
          // -->
        </script>
      </igw:head>
      <body class='iframe <%=bodyClass%>'>
        <%
          String formName = request.getParameter("formName");
          String formActionPage = request.getParameter("formAction");
        %>
        <c:if test='<%=formName != null%>'>
          <igw:bean id='formAction'><c:choose>
            <c:when test='<%=formActionPage != null%>'><imhtml:rewrite page='<%=formActionPage%>'/></c:when>
            <c:otherwise>nothing</c:otherwise>
          </c:choose></igw:bean>
          <form method='post' <%=HTMLFormatter.getAttributeNotNull("name", formName)%> <%=HTMLFormatter.getAttributeNotNull("action", formAction)%> <%=HTMLFormatter.getAttributeNotNull("target", request.getParameter("formTarget"))%>>
        </c:if>
        <%=text%>
        <c:if test='<%=formName != null%>'>
          </form>
        </c:if>
      </body>
    </imhtml:html>
  </c:when>
  <c:otherwise><%=text%></c:otherwise>
</c:choose>
