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

<%@page import='com.ivata.groupware.business.library.faq.category.FAQCategoryDO' %>
<%@page import='com.ivata.mask.util.StringHandling' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: displayFAQ.jsp,v 1.4 2005/04/30 13:04:14 colinmacleod Exp $
//
// Display a single "Frequently Asked Questions" document item from the library.
//
// Since: ivata groupware 0.9 (2002-07-02)
// Author: Laco Kacani
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
// $Log: displayFAQ.jsp,v $
// Revision 1.4  2005/04/30 13:04:14  colinmacleod
// Fixes reverting id type from String to Integer.
//
// Revision 1.3  2005/04/28 18:47:07  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.2  2005/04/27 14:46:39  colinmacleod
// Fixed summary comment.
//
// Revision 1.1  2005/04/26 17:02:19  colinmacleod
// Renamed Faq to FAQ.
//
// Revision 1.3  2005/04/22 09:59:32  colinmacleod
// Cleaned up (simplified) FAQ theme.
// Changed abbreviation from faq to fAQ.
//
// Revision 1.2  2005/04/09 17:19:48  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:52:12  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.6  2004/11/12 15:57:17  colinmacleod
// Removed dependencies on SSLEXT.
// Moved Persistence classes to ivata masks.
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
// Revision 1.7  2003/02/28 07:31:52  colin
// implemented editing/displaying of faqs & notes
//
// Revision 1.6  2003/02/25 07:28:15  colin
// converted display & submission to struts
// started converting comments to popup
// restructured java paths
//
// Revision 1.5  2003/01/24 19:35:18  peter
// changes to sslext
//
// Revision 1.4  2003/01/14 10:42:19  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.3  2002/09/25 11:30:14  colin
// implemented themes
//
// Revision 1.2  2002/08/27 08:44:20  colin
// split tags and themes into two separate includes
//
// Revision 1.1  2002/07/04 15:21:47  laco
// added checking to see which document type is being displayed
// added functionality to display FAQ types
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>
<%@include file='/library/theme/fAQ.jspf'%>
<%@include file='/include/theme.jspf'%>

<igw:bean id='libraryItemForm' scope='session' type='com.ivata.groupware.business.library.struts.ItemForm'/>
<igw:bean id='displayPage' name='libraryItemForm' property='displayPage' type='java.lang.Integer'/>
<igw:bean id='item' name='libraryItemForm' property='item' type='com.ivata.groupware.business.library.item.LibraryItemDO'/>
<igw:bean id='linkPage' name='libraryItemForm' property='linkPage' type='java.lang.String'/>
<%
int displayPageInt = displayPage.intValue();
boolean print = (request.getParameter("print") != null);
%>
<imtheme:frame themeName='<%=libraryItemForm.getThemeName()%>'>
  <imtheme:framePane>
    <c:choose>
      <%-- either print or display contents --%>
      <c:when test='<%=print || (displayPageInt <= 0) || (displayPageInt > item.getFAQCategories().size())%>'>
        <imutil:map id='displayParameters' />
        <logic:iterate indexId='categoryIndex' id='category' name='libraryItemForm' property='item.FAQCategories' type='com.ivata.groupware.business.library.faq.category.FAQCategoryDO'>
          <h2><bean:write name='category' property='name' filter='false'/></h2>
          <ol>
	          <imutil:mapEntry mapName='displayParameters' name='page' value='<%=Integer.toString(categoryIndex.intValue() + 1)%>'/>
            <logic:iterate indexId='fAQIndex' id='fAQ' name='libraryItemForm' property='<%="item.FAQCategories[" + categoryIndex + "].FAQs"%>' type='com.ivata.groupware.business.library.faq.FAQDO'>
              <%-- if we are printing, this will display all faqs --%>
              <%-- otherwise, display a link to another page --%>
              <c:choose>
                <c:when test='<%=print%>'>
                  <logic:iterate indexId='fAQIndexPrint' id='fAQPrint' name='libraryItemForm' property='<%="item.FAQCategories[" + categoryIndex + "].FAQs"%>' type='com.ivata.groupware.business.library.faq.FAQDO'>
                   <li type='l'><h3><bean:write name='category' property='<%="FAQs[" + fAQIndexPrint + "].question"%>' filter='false'/><br></h3></li>
                   <bean:write name='category' property='<%="FAQs[" + fAQIndexPrint + "].answer"%>' filter='false'/>
                  </logic:iterate>
                </c:when>
                <c:otherwise>
                  <igw:bean id='fAQ' type='com.ivata.groupware.business.library.faq.FAQDO'/>
                  <imutil:mapEntry mapName='displayParameters' name='id' value='<%=StringHandling.getNotNull(fAQ.getId(),"none")%>'/>
                  <li><imhtml:link page='<%=linkPage%>' name='displayParameters' anchor='<%= ("fAQ" + fAQIndex) %>'><bean:write name='fAQ' property='question' filter='false'/><br/></imhtml:link></li>
                </c:otherwise>
              </c:choose>
            </logic:iterate>
          </ol>
        </logic:iterate>
      </c:when>
      <%-- print one specific page --%>
      <c:otherwise>
        <%int categoryIndex2 = libraryItemForm.getDisplayPage() - 1;%>
        <igw:bean id='specificCategory' name='libraryItemForm' property='<%="item.FAQCategories[" + categoryIndex2 + "]"%>' type='com.ivata.groupware.business.library.faq.category.FAQCategoryDO'/>
        <ol>
          <h2><bean:write name='specificCategory' property='name' filter='false'/></h2><br>
          <logic:iterate indexId='fAQIndex2' id='fAQ2' name='libraryItemForm' property='<%="item.FAQCategories[" + categoryIndex2 + "].FAQs"%>' type='com.ivata.groupware.business.library.faq.FAQDO'>
            <li type='l'><h3><a name ='<%="fAQ" + fAQIndex2%>'></a><bean:write name='specificCategory' property='<%="FAQs[" + fAQIndex2 + "].question"%>' filter='false'/><br></h3></li>
            <bean:write name='specificCategory' property='<%="FAQs[" + fAQIndex2 + "].answer"%>' filter='false'/>
          </logic:iterate>
        </ol>
      </c:otherwise>
    </c:choose>
    <br/>
  </imtheme:framePane>
</imtheme:frame>


