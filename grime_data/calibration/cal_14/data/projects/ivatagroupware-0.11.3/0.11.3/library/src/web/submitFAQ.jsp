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
<%@page import='java.util.Vector' %>
<%@page import='java.util.Arrays' %>
<%@page import='com.ivata.mask.util.StringHandling' %>

<%@page import='org.apache.struts.Globals' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: submitFAQ.jsp,v 1.6 2005/04/27 14:46:39 colinmacleod Exp $
//
// Submit a collection of frequently asked questions.
//
// Since: ivata groupware 0.9 (2003-02-26)
// Author: Colin MacLeod <colin.macleod@ivata.com>
// $Revision: 1.6 $
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
// $Log: submitFAQ.jsp,v $
// Revision 1.6  2005/04/27 14:46:39  colinmacleod
// Fixed summary comment.
//
// Revision 1.5  2005/04/26 17:29:23  colinmacleod
// Tried to change Faq to FAQ.
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
// Revision 1.6  2004/12/27 14:52:01  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.5  2004/11/03 15:55:47  colinmacleod
// Changed todo comments to TODO: all caps.
//
// Revision 1.4  2004/07/18 16:20:05  colinmacleod
// Fixed library bundle name.
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
// Revision 1.4  2003/03/20 15:22:11  jano
// fixing textarea size for IE
//
// Revision 1.3  2003/03/12 14:07:13  jano
// fixing bug with new agenda point and new question
// renaming newHeding - > newPoint
//
// Revision 1.2  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.1  2003/02/28 07:32:06  colin
// implemented editing/displaying of faqs & notes
//
////////////////////////////////////////////////////////////////////////////////
--%>

<%@include file='/include/tags.jspf'%>
<%@include file='/include/theme.jspf'%>

<bean:size id='categoriesSize' name='libraryItemForm' property='item.FAQCategories'/>
<imhtml:form refersTo='librarySubmitForm' resourceFieldPath='submitFaq'>
  <logic:iterate indexId='categoryIndex' id='category' name='libraryItemForm' property='item.FAQCategories' type='com.ivata.groupware.business.library.faq.category.FAQCategoryDO'>
    <%String categoryProperty = "item.FAQCategories[" + categoryIndex + "]";%>
    <imtheme:window
        styleClass='mainWindow'
        bundle='library' titleKey='submitFaq.title'>
      <imtheme:frame>
        <imtheme:framePane>
          <table border='0' cellpadding='3' cellspacing='0' width='100%'>
            <tr>
              <td class='fieldCaption'><imhtml:label fieldName='category' forField='<%="category" + categoryIndex%>'/></td>
              <td class='field'><imhtml:text fieldName='category' styleId='<%="category" + categoryIndex%>' property='<%=categoryProperty + ".name"%>' maxlength='60'/></td>
            </tr>
          </table>
        </imtheme:framePane>
      </imtheme:frame>
      <logic:iterate indexId='fAQIndex' id='question' name='libraryItemForm' property='<%=categoryProperty + ".FAQs"%>' type='com.ivata.groupware.business.library.faq.FAQDO'>
        <%-- agenda point --%>
        <imtheme:frame>
          <imtheme:framePane>
            <table border='0' cellpadding='3' cellspacing='0' width='100%'>
              <tr>
                <td class='fieldCaption'><imhtml:label fieldName='question' forField='<%="question" + fAQIndex%>'/></td>
                <td class='field'><imhtml:text fieldName='question' styleId='<%="question" + fAQIndex%>' property='<%=categoryProperty + ".FAQs[" + fAQIndex + "].question"%>' size='40' maxlength='150'/></td>
              </tr>
              <tr>
                <td colspan='2' class='fieldCaption'><imhtml:label fieldName='answer' forField='<%="answer" + fAQIndex%>'/></td>
              </tr>
              <tr>
                <td colspan='2' height='180' class='field'><imhtml:textarea fieldName='answer' styleId='<%="answer" + fAQIndex%>'property='<%=categoryProperty + ".FAQs[" + fAQIndex + "].answer"%>' cols='75'/></td>
              </tr>
            </table>
          </imtheme:framePane>
        </imtheme:frame>
      </logic:iterate>

      <%-- button for adding new categories  --%>
      <imtheme:buttonFrame>
        <%-- only add a new category button on the last one --%>
        <c:if test='<%=categoryIndex.intValue() + 1 == categoriesSize.intValue()%>'>
          <imhtml:submit fieldName='newPage' bundle='library' valueKey='submitFaq.submit.newCategory.value' titleKey='submitFaq.submit.newCategory.title' />
        </c:if>
        <imhtml:submit fieldName='newPoint' property='<%="newPoint[" + categoryIndex + "]"%>' bundle='library' valueKey='submitFaq.submit.newQuestion.value' titleKey='submitFaq.submit.newQuestion.title' />
      </imtheme:buttonFrame>
    </imtheme:window>
    <br/>
  </logic:iterate>
</imhtml:form>
