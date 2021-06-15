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
// $Id: submitComment.jsp,v 1.3 2005/04/28 18:47:07 colinmacleod Exp $
//
// Submit a new comment or reply to existing comment.
//
// Since: ivata groupware 0.9 (2002-07-10)
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
// $Log: submitComment.jsp,v $
// Revision 1.3  2005/04/28 18:47:07  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.2  2005/04/09 17:19:48  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:52:13  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.9  2004/12/27 14:52:01  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.8  2004/11/12 15:57:17  colinmacleod
// Removed dependencies on SSLEXT.
// Moved Persistence classes to ivata masks.
//
// Revision 1.7  2004/11/03 15:55:47  colinmacleod
// Changed todo comments to TODO: all caps.
//
// Revision 1.6  2004/07/31 11:13:40  colinmacleod
// Fixed page title.
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
// Revision 1.17  2003/03/28 11:19:43  jano
// trick with wraping text in textarea
//
// Revision 1.16  2003/03/20 15:22:11  jano
// fixing textarea size for IE
//
// Revision 1.15  2003/03/07 16:25:18  peter
// comment subject length limited here
//
// Revision 1.14  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.13  2003/02/28 13:43:50  colin
// made comments work with popups
//
// Revision 1.12  2003/02/25 07:28:15  colin
// converted display & submission to struts
// started converting comments to popup
// restructured java paths
//
// Revision 1.11  2003/01/24 19:35:18  peter
// changes to sslext
//
// Revision 1.10  2003/01/18 20:38:58  colin
// added help buttons
//
// Revision 1.9  2003/01/14 10:42:19  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.8  2002/12/16 12:58:16  peter
// fixed i18n in title
//
// Revision 1.7  2002/11/27 18:00:35  peter
// i18n finished
//
// Revision 1.6  2002/10/24 09:25:23  peter
// fixed bug with comment amend (parent null check and passing the comment id)
//
// Revision 1.5  2002/09/25 15:29:00  colin
// added title/tooltip attributes to buttons/submits
//
// Revision 1.4  2002/09/23 11:59:07  colin
// changed mail session handling
//
// Revision 1.3  2002/09/05 10:20:57  colin
// lots of bugfixes
//
// Revision 1.2  2002/08/27 08:44:20  colin
// split tags and themes into two separate includes
//
// Revision 1.1  2002/08/11 12:10:18  colin
// First working (sort of) version with basic functionality.
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<%@include file='/include/theme.jspf'%>

<igw:bean id='libraryCommentForm' scope='session' type='com.ivata.groupware.business.library.struts.CommentForm'/>

<imhtml:html locale='true'>
  <igw:head bundle='library' titleKey='submitComment.title' topLevel='true' keepOnTop='false'>
    <imhtml:base/>
    <igw:bean id='framesUri'><imhtml:rewrite page='/library/submitCommentFrame.jsp'/></igw:bean>
    <igw:bean id='previewUri'><imhtml:rewrite page='/library/submitCommentPreview.jsp'/></igw:bean>
    <script language='javascript'>
      <!--
      if (window == parent) {
          window.location.href = "<%=framesUri%>";
      } else {
          window.open ("<%=previewUri%>", "ivataCommentPreview");
      }
      // -->
    </script>
 </igw:head>

  <body class='dialog'>
    <imtheme:window>
      <imhtml:form action='/library/comment' bundle='library' resourceFieldPath='submitComment'>
        <%@include file='/include/errorFrame.jspf'%>
        <imtheme:frame>
          <imtheme:framePane styleClass='hilight'>
            <table border='0' cellpadding='0' cellspacing='0' width='100%' height='100%'>
              <tr>
                <td class='fieldCaption'><imhtml:label fieldName='subject'/></td>
                <td class='field'><imhtml:text mandatory='true' fieldName='subject' property='comment.subject' maxlength='60' /></td>
              </tr>
              <tr>
                <td class='fieldCaption'><imhtml:label fieldName='text'/></td>
              </tr>
              <tr>
                <td class='field' height='100%' colspan='2'><imhtml:textarea fieldName='text' mandatory='true' property='comment.text' cols='40'/></td>
              </tr>
            </table>
          </imtheme:framePane>
        </imtheme:frame>
        <imtheme:buttonFrame>
          <imhtml:label fieldName='format'/>
          <imhtml:select fieldName='format' property='comment.format'>
              <imhtml:option value='<%=Integer.toString(FormatConstants.FORMAT_TEXT)%>'><bean:message bundle='library' key='submitComment.select.format.option.plainText' /></imhtml:option>
              <imhtml:option value='<%=Integer.toString(FormatConstants.FORMAT_HTML)%>'>HTML</imhtml:option>
          </imhtml:select>
          <imhtml:submit fieldName='preview'/>
          <imtheme:buttonSpacer/>
          <logic:present name='libraryCommentForm' property='preview'>
            <imhtml:ok/>
          </logic:present>
          <imhtml:cancel/>
          <imhtml:help key='library.comment.submit'/>
        </imtheme:buttonFrame>
      </imhtml:form>
    </imtheme:window>
  </body>
</imhtml:html>
