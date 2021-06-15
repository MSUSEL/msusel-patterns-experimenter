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

<%@page import="com.ivata.mask.util.StringHandling" %>
<%@page import="com.ivata.mask.web.RewriteHandling"%>
<%@page import="com.ivata.groupware.navigation.*" %>
<%@page import="com.ivata.groupware.navigation.menu.*" %>

<%@page import="java.io.IOException" %>
<%@page import="java.net.URLEncoder" %>
<%@page import="java.util.*" %>

<%@page import="javax.servlet.jsp.JspWriter" %>
<%@page import="javax.servlet.http.HttpSession" %>
<%@page import="javax.naming.NamingException" %>

<%@page import="org.apache.struts.taglib.TagUtils" %>
<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: left.jsp,v 1.9.2.1 2005/10/08 17:29:56 colinmacleod Exp $
//
// <p>This is the left most panel which contains menues, if you use the
// frames version of ivata groupware.</p>
//
// <p><strong>Note:</strong> this page still contains a good old-fashioned mixture of Java
// and HTML. It also contains dynamically-generated javascript which has matured
// for a fair old while now, so it is not for the faint-hearted.</p>
//
// Since: ivata groupware 0.9 (2001-09-09)
// Author: Colin MacLeod <colin.macleod@ivata.com>
// $Revision: 1.9.2.1 $
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
// $Log: left.jsp,v $
// Revision 1.9.2.1  2005/10/08 17:29:56  colinmacleod
// Replaced use of JSPPicoContainerFactory with igw:bean tag.
//
// Revision 1.9  2005/04/29 02:48:20  colinmacleod
// Data bugfixes.
// Changed primary key back to Integer.
//
// Revision 1.8  2005/04/28 18:47:06  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.7  2005/04/27 20:20:57  colinmacleod
// Fixed issues with IDs that were still handled
// as integers (should be now strings).
//
// Revision 1.6  2005/04/10 20:10:11  colinmacleod
// Added new themes.
// Changed id type to String.
// Changed i tag to em and b tag to strong.
// Improved PicoContainerFactory with NanoContainer scripts.
//
// Revision 1.5  2005/04/09 17:19:54  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.4  2005/03/18 08:14:08  colinmacleod
// Cosmetic change. Reordered imports.
//
// Revision 1.3  2005/03/17 11:21:02  colinmacleod
// Fixed bugs in JSPContainerFactory calls.
//
// Revision 1.2  2005/03/17 10:56:42  colinmacleod
// Added checkLogin.
//
// Revision 1.1.1.1  2005/03/10 17:49:26  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.10  2004/12/31 19:20:21  colinmacleod
// Removed unused ExceptionHandling import.
//
// Revision 1.9  2004/12/27 14:52:02  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.8  2004/11/12 16:03:00  colinmacleod
// Removed dependencies on SSLEXT.
// Moved Persistence classes to ivata masks.
//
// Revision 1.7  2004/07/18 22:01:48  colinmacleod
// Wrapped collection in a new vector to prevent concurrency probs.
//
// Revision 1.6  2004/07/14 22:50:26  colinmacleod
// Replaced bean:define with extended igw:bean.
//
// Revision 1.5  2004/07/14 20:59:54  colinmacleod
// Changed all occurrences of jsp:useBean to igw:bean (better tomcat compatibility).
//
// Revision 1.4  2004/07/13 19:48:09  colinmacleod
// Moved project to POJOs from EJBs.
// Applied PicoContainer to services layer (replacing session EJBs).
// Applied Hibernate to persistence layer (replacing entity EJBs).
//
// Revision 1.3  2004/03/21 21:16:33  colinmacleod
// Shortened name to ivata op.
//
// Revision 1.2  2004/02/06 14:59:32  janboros
// new verison of sslext
//
// Revision 1.1.1.1  2004/01/27 20:59:19  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.2  2003/10/17 14:14:21  jano
// changed names intranet -> portal
//
// Revision 1.1.1.1  2003/10/13 20:47:25  colin
// Restructured portal into subprojects
//
// Revision 1.28  2003/07/06 20:37:05  colin
// Made main pages secure to stop IE warning
//
// Revision 1.27  2003/06/10 05:56:38  peter
// img and html:img replaced with size-aware imhtml:img
//
// Revision 1.26  2003/06/06 13:35:24  jano
// fixing image size
//
// Revision 1.25  2003/03/04 14:28:18  colin
// moved favorite adding to struts
//
// Revision 1.24  2003/03/03 16:34:37  jano
// fixing stlyeClass for link in shortCut window
//
// Revision 1.23  2003/02/28 15:55:18  jano
// fixing javascript
// and
// graphics
//
// Revision 1.22  2003/02/25 09:27:17  colin
// added context path for pop ups
//
// Revision 1.21  2003/02/25 09:18:38  colin
// added full server path for local links (to fix ssl issues)
//
// Revision 1.20  2003/02/25 07:32:10  colin
// restructured java file paths
//
// Revision 1.19  2003/02/04 17:00:50  peter
// fixed for sslext
//
// Revision 1.18  2003/01/27 07:19:39  colin
// updated copyright notice
//
// Revision 1.17  2003/01/23 17:50:58  colin
// fixed double context path in links
//
// Revision 1.16  2003/01/23 17:34:02  colin
// converted to sslext
//
// Revision 1.15  2003/01/14 11:30:47  colin
// fixed error in html:base tag
//
// Revision 1.14  2003/01/14 10:31:29  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.13  2002/11/28 16:56:26  peter
// i18n finished
//
// Revision 1.12  2002/11/13 12:22:14  jano
// we want wider window for editing favorites
//
// Revision 1.11  2002/09/30 15:07:35  peter
// the favorites amend/removal implemented
//
// Revision 1.10  2002/09/30 07:55:07  peter
// the menuItem change logic will be moved to the tool window
//
// Revision 1.9  2002/09/30 06:03:39  peter
// add to favorites implemented
//
// Revision 1.8  2002/09/16 14:20:18  jano
// we want widther popUpWindow
//
// Revision 1.7  2002/09/11 15:13:43  jano
// in case when we want open popUpWindow I am creating difrent link with onClick event
//
// Revision 1.6  2002/09/11 09:10:01  colin
// added more menu styles and options, chosen via settings
//
// Revision 1.5  2002/08/27 08:43:40  colin
// split tags and themes into two separate includes
//
// Revision 1.4  2002/08/19 07:44:52  colin
// moved over to resin for development
//
// Revision 1.3  2002/07/01 08:06:47  colin
// bugfixes
// started conversion to imtheme tags
//
// Revision 1.2  2002/06/21 12:06:20  colin
// tidied up documentation
//
// Revision 1.1  2002/06/13 11:10:59  colin
// first version with rose model and jboss  integration
//
// Revision 1.4  2002/02/03 19:54:04  colin
// linked settings to the database, rather than hard-coding in com.ivata.groupware.admin.settings
//
// Revision 1.3  2002/01/22 22:26:32  colin
// changed site_user to person_user
//
// Revision 1.2  2002/01/20 12:29:25  colin
// moved properties over to separate jsp
//
// Revision 1.1  2001/12/12 20:13:27  colin
// initial import into CVS
//
///////////////////////////////////////////////////////////////////////
--%>

<%@include file='/include/tags.jspf'%>
<igw:checkLogin/>
<%@include file='/include/theme.jspf'%>

<igw:bean id='user' name='securitySession' property='user' type='com.ivata.groupware.admin.security.user.UserDO'/>

<igw:getSetting id='guiShortcutStyle' setting='guiShortcutStyle' type='java.lang.Integer'/>
<igw:getSetting id='guiShortcutPause' setting='guiShortcutPause' type='java.lang.Integer'/>
<igw:getSetting id='pathMenuItemImages' setting='pathMenuItemImages'type='java.lang.String'/>

<igw:bean id='securitySession' name='securitySession' type='com.ivata.groupware.admin.security.server.SecuritySession'/>

<igw:bean id='formatterTitle' type='com.ivata.mask.web.format.HTMLFormatter'/>
<igw:bean id='maximumLengthTitle' type='com.ivata.mask.web.format.MaximumLengthFormat'/>
<jsp:setProperty name='maximumLengthTitle' property='maximumLength' value='15'/>
<imformat:addFormat formatter='<%=formatterTitle%>' format='<%=maximumLengthTitle%>'/>

<igw:bean id='favoritePopUp' scope='page' type='com.ivata.mask.web.javascript.JavaScriptWindow'/>
<jsp:setProperty name='favoritePopUp' property='frameName' value='favoriteUpdateWindow'/>
<jsp:setProperty name='favoritePopUp' property='page' value='/navigation/favoriteFind.action'/>
<jsp:setProperty name='favoritePopUp' property='pageContext' value='<%=pageContext%>'/>
<jsp:setProperty name='favoritePopUp' property='width' value='655'/>
<jsp:setProperty name='favoritePopUp' property='height' value='205'/>

<%!
  // used to create links
  TagUtils tagUtils = TagUtils.getInstance();

  // id for the favorites menu, the only when items can be changed (removed)
  Integer favoritesId = new Integer(0);

  // include any preliminary stuff here...
  int topMargin = 5;      // gap before the first menu
  int menuSpacing = 42;      // this is the size of menues without items
  int menuItemSpacing = 30;  // this is the size of an individual item
  int menuExpandedHeight = 35;  // this is the extra height to allow for the expansion offset
%>

<%
  ArrayList idList = new ArrayList();
  ArrayList heightList = new ArrayList();
  ArrayList textList = new ArrayList();
  int topPosition = topMargin;
  int topCount = 0;

  String visibility;
  String linkVisibility;

  int highestId = 0;
  int idCount = 0;
  Integer id;
  int itemCount = 0;
  String text = "";

  // get the initial id
  Integer initialId = StringHandling.integerValue(request.getParameter("id"));

  // store the syle of menues
%>
<igw:bean id='navigation' type='com.ivata.groupware.navigation.Navigation'/>

<%
  // now get the menues from that bean - make a copy of the vector to avoid
  // concurrency probs...
  Collection menues = new Vector(navigation.findMenues(securitySession));
  Collection menuesToRemove = new Vector();
  int visibleMenu = 0;
%>


<%-- go through the menues once to set up the heights and delete any empty ones --%>
<c:forEach var='menuForSize' items='<%=menues%>'>
  <igw:bean id='menuForSize' type='com.ivata.groupware.navigation.menu.MenuDO'/>
  <c:choose>
    <c:when test='<%=menuForSize.getItems().size() == 0%>'>
      <%-- we're not interested in empty menu items --%>
      <%
        menuesToRemove.add(menuForSize);
      %>
    </c:when>
    <c:otherwise>
      <%-- is this the visible one? --%>
      <c:if test='<%=menuForSize.getId().equals(initialId)%>'>
        <%visibleMenu = idCount;%>
      </c:if>
      <%
        itemCount = menuForSize.getItems().size();
        idList.add(menuForSize.getId());

        // calculate the height of the menu
        Integer height = new Integer(menuItemSpacing * ++itemCount + menuExpandedHeight);
        heightList.add(height);

        textList.add(menuForSize.getText());
        ++idCount;
      %>
    </c:otherwise>
  </c:choose>
</c:forEach>
<%-- remove the empty ones here --%>
<c:forEach var='menuToRemove' items='<%=menuesToRemove%>'>
  <igw:bean id='menuToRemove' type='com.ivata.groupware.navigation.menu.MenuDO'/>
  <%menues.remove(menuToRemove);%>
</c:forEach>

<imhtml:html locale='true'>
  <igw:head>
    <imhtml:base/>
    <script language="javascript">
      <!--
      var active_popup = 'popup_main';
      var menuSwitchAllowed = true;
      <%-- this function expands the dynamic menu --%>
      function expand(popup) {
        if(menuSwitchAllowed) {
          <%
            // ok. this is a bit tricky. the javascript is dynamically generated by
            // the jsp code to check which popup is active and to place them all in
            // the right position for this case
            boolean bStart = true;
            for(int idNumber = 0; idNumber < idList.size() ; ++idNumber) {
              id = (Integer) idList.get(idNumber);
              // check out whether this is the first one
              // if it isn't, put out an else
             if(bStart) {
                bStart = false;
              } else {
                out.print("} else ");
              }
              // echo the javascript if corresponding to this id
              out.println("if(popup == '" + id + "') {");
              // now go thro' all the ids again, setting up each popup
              topPosition = topMargin;
              for(int nAssign = 0; nAssign < idList.size(); ++nAssign) {
                Integer idAssign = (Integer) idList.get(nAssign);
                visibility = idAssign.equals(id) ? "\"hidden\"" : "\"visible\"";
                linkVisibility = idAssign.equals(id) ? "\"visible\"" : "\"hidden\"";
                out.println("document.getElementById('popup_" + idAssign.toString() + "').style.visibility = " + visibility + ";");
                out.println("document.getElementById('popup_links_" +  idAssign.toString() + "').style.visibility = " + linkVisibility + ";");
                out.println("document.getElementById('popup_"  + idAssign.toString() + "').style.top = " + new Integer(topPosition).toString() + ";");
                out.println("document.getElementById('popup_links_"  + idAssign.toString() + "').style.top = " + new Integer(topPosition).toString() + ";");
                out.println("menuSwitchAllowed = false;");
                // put in a time out to limit the chance of you hitting the wrong menu
                out.println("setTimeout(\"menuSwitchAllowed=true;\", "+guiShortcutPause.toString()+");");


                // add in the height of this menu
                // if this is the active menu, then the height depends on the number of items
                // otherwise it is constant
                if(idAssign.equals(id)) {
                  topPosition += ((Integer) heightList.get(nAssign)).intValue();
                } else {
                  topPosition += menuSpacing;
                }
              }
            }
            // if we found ids, output the final brace
            if(!bStart) {
              out.print("}");
            }
          %>
          // disable popups for a short time, to make sure you chose the right menu
          bEnablePopup = false;
          setTimeout("enablePopup();", 300);
        } else {
        }
      }
      function enablePopup() {
        bEnablePopup = true;
      }
      // by default, popups are enabled
      bEnablePopup = true;
      // -->
    </script>
  </igw:head>
    <body>
      <%
        // go thro' all the menues and show...
        int menuCount = 0;
        topPosition = topMargin;
      %>
      <igw:bean id='formatter' type='com.ivata.mask.web.format.HTMLFormatter'/>
      <igw:bean id='maximumLength' type='com.ivata.mask.web.format.MaximumLengthFormat'/>
      <jsp:setProperty name='maximumLength' property='maximumLength' value='15'/>
      <imformat:addFormat formatter='<%=formatter%>' format='<%=maximumLength%>'/>

      <imutil:map id='mapUpdateFavorite' />
      <c:forEach var='menu' items='<%=menues%>'>
        <igw:bean id='menu' type='com.ivata.groupware.navigation.menu.MenuDO'/>
        <%
          id = menu.getId();
          text = menu.getText();

          visibility = (menuCount == visibleMenu) ? "hidden" : "visible";
          linkVisibility = (menuCount == visibleMenu) ? "visible" : "hidden";

          // enable the following lines to read the error messages
          // (which may be hidden behind layers)
          //$visibility = "hidden";
          //$linkVisibility = "hidden";
        %>
        <imutil:map id='params'>
          <imutil:mapEntry name='id' value='<%=menu.getId().toString()%>'/>
          <c:if test='<%=guiShortcutStyle.equals(MenuConstants.SHORTCUT_STYLE_DYNAMIC)%>'>
            <imutil:mapEntry name='onmouseover' value='<%="expand("
                               + id.toString()
                               + ")"%>'/>
          </c:if>
        </imutil:map>
        <igw:bean id='title'>
          <%--imhtml:link styleClass='menu' page='/left.jsp' name='params'><%=text%></imhtml:link--%>
          <c:choose>
            <c:when test='<%=guiShortcutStyle.equals(MenuConstants.SHORTCUT_STYLE_CLICK)%>'>
              <a class='menuShortCut' href='' onclick='<%="expand("+String.valueOf(id)+");return false"%>'><%=text%></a>
            </c:when>
            <c:when test='<%=guiShortcutStyle.equals(MenuConstants.SHORTCUT_STYLE_DYNAMIC)%>'>
              <a class='menuShortCut' href='javascript:;' onMouseOver='<%="expand("+String.valueOf(id)+");return false"%>'><%=text%></a>
            </c:when>
            <c:otherwise>
              <%=text%>
            </c:otherwise>
          </c:choose>
        </igw:bean>
        <%-- put out the layers unless we should show all the menu items --%>
        <c:if test='<%=!guiShortcutStyle.equals(MenuConstants.SHORTCUT_STYLE_OPEN)%>'>
          <div id="<%= "popup_" + id.toString() %>" style="<%= "position: absolute; z-index: 20; visibility: " + visibility + "; top: " + new Integer(topPosition).toString() + "px; left: 2px;" %>">
            <imtheme:window title='<%=title%>' styleClass='window'>
            </imtheme:window>
          </div>
          <div id="<%= "popup_links_" + id.toString() %>" style="<%= "position: absolute; z-index: 20; visibility: " + linkVisibility + "; top: " + new Integer(topPosition).toString() + "px; left: 2px;" %>">
        </c:if>
        <imtheme:window title='<%=title%>' styleClass='window'>
          <imtheme:frame>
            <imtheme:framePane>
              <table cellpadding='0' cellspacing='0' border='0'>
                <c:forEach var='item' items='<%=menu.getItems()%>'>
                  <igw:bean id='item' type='com.ivata.groupware.navigation.menu.item.MenuItemDO'/>
                  <%-- only show items for this user --%>
                  <c:if test='<%=(item.getUser() == null) || user.equals(item.getUser())%>'>
                    <%
                      String link = item.getURL();
                    %>
                    <%--
                      if the link doesn't start with a protocol and it doesn't
                      start with /, append the context path
                    --%>
                    <c:if test='<%=(link.indexOf(':') == -1) && !link.startsWith("/")%>'>
                      <%link = RewriteHandling.getContextPath(request) + "/" + link;%>
                    </c:if>
                    <% Integer currentItemId = item.getId(); %>
                    <% String currentItemText = item.getText(); %>
                    <% String itemImage = item.getImage(); %>
                    <tr>
                      <td class='menuItem' valign='middle'>
                        <c:choose>

                          <%-- this section is for favorites --%>
                          <c:when test='<%= favoritesId.equals(id) %>'>
                            <imutil:mapEntry name='idString' mapName='mapUpdateFavorite'
                                value='<%= currentItemId.toString() %>' />
                            <jsp:setProperty name='favoritePopUp' property='paramsName'
                                value='mapUpdateFavorite' />
                            <imhtml:img page='<%= pathMenuItemImages + "updateFavorite.gif" %>' border='0'
                                  onclick='<%= favoritePopUp.toString() %>'
                                  titleKey='left.favoriteImg.title' />

                          </c:when>

                          <c:when test='<%=StringHandling.isNullOrEmpty(itemImage)%>' />

                          <c:otherwise>
                            <c:choose>
                              <c:when test='<%=link.startsWith("popUpWindow")%>'>
                                <%
                                  int where = link.indexOf(":");
                                  link = link.substring(where+1);
                                %>
                                <%-- prepend context path --%>
                                <c:if test='<%=(link.indexOf(':') == -1) && !link.startsWith("/")%>'>
                                  <%link = RewriteHandling.getContextPath(request) + "/" + link;%>
                                </c:if>
<%
  link = "window.open(\"" + tagUtils.computeURL(pageContext, null, link, null, null, null, null, null, true)  + "\", \"popUpWindow\",\"location=no,status=no,toolbar=no,width=500,height=600\"); return false";
%>
                                <a class='menu' href='' onclick='<%=link%>'>
                                  <imhtml:img page='<%= pathMenuItemImages + itemImage %>' border='0' />
                                </a>

                              </c:when>
                              <c:otherwise>
                                <imhtml:link styleClass='menu' href='<%=link%>' target='ivataMain'>
                                  <imhtml:img page='<%= pathMenuItemImages + itemImage %>'
                                        border='0' width='24' height='24' />
                                </imhtml:link>

                              </c:otherwise>
                            </c:choose>
                          </c:otherwise>

                        </c:choose>
                      </td>
                      <td class='menuItem' valign='middle' width='8'><imhtml:img page='/images/empty.gif' height='<%=String.valueOf(menuItemSpacing)%>' width='1'/></td>
                      <td class='menuItem' valign='middle'>
                        <c:choose>
                          <c:when test='<%=link.startsWith("window")%>'>
                            <a class='menu' href='' onClick='<%=link%>'><imformat:format formatter='<%=formatter%>'><%= currentItemText %></imformat:format></a>
                          </c:when>
                          <%-- favorite section again --%>
                          <c:when test='<%= favoritesId.equals(id) %>'>
                            <imhtml:link styleClass='menu' href='<%=link%>' target='ivataMain'><font title='<%= currentItemText %>'><imformat:format formatter='<%=formatterTitle%>'><%= currentItemText %></imformat:format></font></imhtml:link>
                          </c:when>
                          <c:otherwise>
                            <imhtml:link styleClass='menu' href='<%=link%>' target='ivataMain'><imformat:format formatter='<%=formatter%>'><%= currentItemText %></imformat:format></imhtml:link>
                          </c:otherwise>
                        </c:choose>
                      </td>
                    </tr>
                  </c:if>
                </c:forEach>
              </table>
            </imtheme:framePane>
          </imtheme:frame>
          <%-- close the layer unless we should show all the menu items --%>
          <c:if test='<%=!guiShortcutStyle.equals(MenuConstants.SHORTCUT_STYLE_OPEN)%>'>
            <c:choose>
              <c:when test='<%=menuCount == visibleMenu%>'>
                <%topPosition += heightList.size() > 0 ?
                               ((Integer) heightList.get(menuCount)).intValue():
                               0;%>
              </c:when>
              <c:otherwise>
                <%topPosition += menuSpacing;%>
              </c:otherwise>
            </c:choose>
            <%++menuCount;%>
          </c:if>
        </imtheme:window>
      <%-- close the layer unless we should show all the menu items --%>
      <c:if test='<%=!guiShortcutStyle.equals(MenuConstants.SHORTCUT_STYLE_OPEN)%>'>
        </div>
      </c:if>
      <br/>
    </c:forEach>
  </body>
</imhtml:html>

