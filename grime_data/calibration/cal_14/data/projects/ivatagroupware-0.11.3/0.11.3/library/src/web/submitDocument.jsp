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
<%@page import='com.ivata.mask.util.StringHandling' %>

<%@page import='org.apache.struts.Globals' %>
<%@page import='java.util.Arrays' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: submitDocument.jsp,v 1.3 2005/04/10 20:10:11 colinmacleod Exp $
//
// Submit a new document library item into the intranet, or edit an existing one.
//
// Since: ivata groupware 0.9 (2002-07-15)
// Author: Laco Kacani, colin
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
// $Log: submitDocument.jsp,v $
// Revision 1.3  2005/04/10 20:10:11  colinmacleod
// Added new themes.
// Changed id type to String.
// Changed i tag to em and b tag to strong.
// Improved PicoContainerFactory with NanoContainer scripts.
//
// Revision 1.2  2005/04/09 17:19:48  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:52:08  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.8  2004/12/27 14:52:01  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.7  2004/11/03 15:55:47  colinmacleod
// Changed todo comments to TODO: all caps.
//
// Revision 1.6  2004/07/18 16:20:05  colinmacleod
// Fixed library bundle name.
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
// Revision 1.16  2003/07/08 07:19:48  peter
// html upload and parser changes
//
// Revision 1.15  2003/04/14 07:26:14  peter
// removed Help
//
// Revision 1.14  2003/03/20 15:22:11  jano
// fixing textarea size for IE
//
// Revision 1.13  2003/03/03 19:06:45  colin
// i18n corrections
//
// Revision 1.12  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.11  2003/02/25 07:28:15  colin
// converted display & submission to struts
// started converting comments to popup
// restructured java paths
//
// Revision 1.10  2003/01/18 20:38:58  colin
// added help buttons
//
// Revision 1.9  2002/12/05 15:20:37  jano
// we want wider input box for page content
//
// Revision 1.8  2002/11/27 18:00:35  peter
// i18n finished
//
// Revision 1.7  2002/09/25 09:39:19  jano
// bug with add new blank PAGE
//
// Revision 1.6  2002/09/24 08:49:43  peter
// the button is provided with  title
//
// Revision 1.5  2002/09/24 08:38:25  peter
// changed the long 'Add New Page' button-caption to New
//
// Revision 1.4  2002/09/23 16:19:07  peter
// removed the insert buttons, new page apperars only after it's wanted
//
// Revision 1.2  2002/09/13 08:37:35  jano
// corecting insertPageBefore
//
// Revision 1.1  2002/09/06 14:19:52  jano
// *** empty log message ***
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>

<%-- upload html popup --%>
<igw:bean id='uploadHtmlPopUp' scope='page' type='com.ivata.mask.web.javascript.JavaScriptWindow'/>
<jsp:setProperty name='uploadHtmlPopUp' property='frameName' value='ivataLibraryHtmlUpload'/>
<jsp:setProperty name='uploadHtmlPopUp' property='page' value='/library/uploadHtml.jsp'/>
<jsp:setProperty name='uploadHtmlPopUp' property='pageContext' value='<%=pageContext%>'/>
<jsp:setProperty name='uploadHtmlPopUp' property='width' value='630'/>
<jsp:setProperty name='uploadHtmlPopUp' property='height' value='400'/>

<%-- Page Text WINDOW ............................................. --%>
<imhtml:form refersTo='librarySubmitForm' resourceFieldPath='submitDocument'>
  <imtheme:window bundle='library' titleKey='submitDocument.title' styleClass='mainWindow'>
    <logic:iterate indexId='index' id='pageText' name='libraryItemForm' property='item.pages'>
      <imtheme:frame>
        <imtheme:framePane>
          <table cellpadding='1' cellspacing='0' border='0' width='100%'>
            <tr>
              <td class='fieldCaption'><imhtml:label
                  fieldName='pages'
                  args='<%=
                    Arrays.asList(new Object[] {Integer.toString(index.intValue() + 1)})
                    %>'/></td>
            </tr>
              <td
                  class='field'
                  height='220'><imhtml:textarea
                      fieldName='pages'
                      property='<%="item.pages[" + index + "].text"%>'
                      cols='75'/></td>
            </tr>
          </table>
        </imtheme:framePane>
      </imtheme:frame>
    </logic:iterate>

    <imtheme:buttonFrame>
      <%-- TODO
      <imhtml:button fieldName='uploadDocument' onclick='<%= uploadHtmlPopUp.toString() %>' />
      --%>
      <imhtml:submit fieldName='newPage'/>
    </imtheme:buttonFrame>
  </imtheme:window>
</imhtml:form>
<br/>
