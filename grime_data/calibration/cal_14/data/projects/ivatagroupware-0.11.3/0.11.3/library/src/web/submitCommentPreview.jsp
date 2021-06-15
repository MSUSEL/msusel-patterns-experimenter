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
<%@page import='com.ivata.mask.web.format.FormatConstants' %>
<%@page import='com.ivata.mask.web.format.HTMLFormatter' %>
<%@page import='com.ivata.groupware.business.library.comment.CommentDO' %>
<%@page import='com.ivata.mask.util.StringHandling' %>

<%@page import='org.apache.struts.Globals' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: submitCommentPreview.jsp,v 1.4 2005/04/28 18:47:07 colinmacleod Exp $
//
// Preview a new comment or reply to existing comment.
//
// Since: ivata groupware 0.9 (2003-02-23)
// Author: Colin MacLeod <colin.macleod@ivata.com>
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
// $Log: submitCommentPreview.jsp,v $
// Revision 1.4  2005/04/28 18:47:07  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.3  2005/04/10 18:47:40  colinmacleod
// Changed i tag to em and b tag to strong.
//
// Revision 1.2  2005/04/09 17:19:48  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:52:13  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.7  2004/12/27 14:52:01  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.6  2004/07/31 11:14:00  colinmacleod
// Updated to Pico/Hibernate.
//
// Revision 1.5  2004/07/14 22:50:25  colinmacleod
// Replaced bean:define with extended igw:bean.
//
// Revision 1.4  2004/07/14 20:59:54  colinmacleod
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
// Revision 1.1.1.1  2004/01/27 20:58:46  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.3  2003/10/28 13:16:15  jano
// commiting library,
// still fixing compile and building openPortal project
//
// Revision 1.2  2003/10/15 14:16:54  colin
// fixing for XDoclet
//
// Revision 1.3  2003/02/28 14:01:24  colin
// fixed html formatting for parent comments
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
<igw:checkLogin/>
<%@include file='/include/theme.jspf'%>

<igw:bean id='libraryCommentForm' scope='session' type='com.ivata.groupware.business.library.struts.CommentForm'/>

<%-- make the title look nice --%>
<igw:bean id='titleFormatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter'/>
<igw:bean id='characterEntities' scope='page' type='com.ivata.mask.web.format.CharacterEntityFormat'/>
<imformat:addFormat formatter='<%=titleFormatter%>' format='<%=characterEntities%>'/>
<igw:bean id='nonBreaking' scope='page' type='com.ivata.mask.web.format.NonBreakingSpaceFormat' />
<imformat:addFormat formatter='<%=titleFormatter%>' format='<%=nonBreaking%>'/>
<igw:bean id='lineBreaks' scope='page' type='com.ivata.mask.web.format.LineBreakFormat' />
<jsp:setProperty name='lineBreaks' property='convertLineBreaks' value='true'/>

<%-- make the comment text look nice --%>
<igw:bean id='textFormatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter'/>

<%-- if it is not html, convert the line breaks and character entities --%>
<logic:equal name='libraryCommentForm' property="comment.format" value='<%=Integer.toString(FormatConstants.FORMAT_TEXT)%>'>
  <imformat:addFormat formatter='<%=textFormatter%>' format='<%=characterEntities%>'/>
  <imformat:addFormat formatter='<%=textFormatter%>' format='<%=lineBreaks%>'/>
</logic:equal>

<imhtml:html locale='true'>
  <igw:head title='preview - comment on a library item' topLevel='true' keepOnTop='false'><imhtml:base/></igw:head>

  <body>
    <h2><bean:message bundle='library' key='submitCommentPreview.title'/></h2>
    <%--
      only show a preview if there is an original comment, or this comment
      has something to show
    --%>
    <logic:present name='libraryCommentForm' property='comment.text'>
      <logic:present name='libraryCommentForm' property='comment.parent'>
        <logic:present name='libraryCommentForm' property='comment.parent.text'>
          <em><bean:message bundle='library' key='submitComment.label.yourReply' /></em><br/><br/>
        </logic:present>
      </logic:present>
      <strong><imformat:format formatter='<%=titleFormatter%>'><bean:write name='libraryCommentForm' property='comment.subject' filter='false'/></imformat:format></strong><br/>
        <imformat:format formatter='<%=textFormatter%>'><bean:write name='libraryCommentForm' property='comment.text' filter='false'/></imformat:format>
      <logic:present name='libraryCommentForm' property='comment.parent'>
        <logic:present name='libraryCommentForm' property='comment.parent.text'>
        <br/>
        <br/>
        <hr/>
        <br/>
        </logic:present>
      </logic:present>
    </logic:present>
    <logic:present name='libraryCommentForm' property='comment.parent'>
      <logic:present name='libraryCommentForm' property='comment.parent.text'>

        <%-- make the parent comment text look nice --%>
        <igw:bean id='parentTextFormatter' scope='page' type='com.ivata.mask.web.format.HTMLFormatter'/>
        <%-- if it is not html, convert the line breaks and character entities --%>
        <logic:equal name='comment.parent.format' value='<%=Integer.toString(FormatConstants.FORMAT_TEXT)%>'>
          <imformat:addFormat formatter='<%=parentTextFormatter%>' format='<%=characterEntities%>'/>
          <imformat:addFormat formatter='<%=parentTextFormatter%>' format='<%=lineBreaks%>'/>
        </logic:equal>

        <em><bean:message bundle='library' key='submitComment.label.originalComment' arg0='<%=libraryCommentForm.getComment().getParent().getCreatedBy().getName()%>'/></em>
        <br/><br/>
          <strong><imformat:format formatter='<%=titleFormatter%>'><bean:write name='libraryCommentForm' property='comment.parent.subject' filter='false'/></imformat:format></strong><br/>
          <imformat:format formatter='<%=parentTextFormatter%>'><bean:write name='libraryCommentForm' property='comment.parent.text' filter='false'/></imformat:format>
      </logic:present>
    </logic:present>
  </body>
</imhtml:html>
