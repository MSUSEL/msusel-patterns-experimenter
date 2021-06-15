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
<%@page import="com.ivata.groupware.business.addressbook.person.PersonDO" %>
<%@page import="com.ivata.groupware.business.addressbook.person.group.GroupConstants" %>
<%@page import="com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressConstants" %>
<%@page import="com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressDO" %>
<%@page import="java.util.Collection" %>
<%@page import="java.util.HashMap" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Vector" %>

<%@page import='org.apache.commons.beanutils.PropertyUtils' %>
<%@page import='org.apache.struts.Globals' %>
<%@page import="org.apache.struts.taglib.TagUtils" %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: index.jsp,v 1.5.2.1 2005/10/08 17:14:15 colinmacleod Exp $
//
// Creates a list of all the people with a given first initial.
//
// Since: ivata groupware 0.9 (2001-12-24)
// Author: Colin MacLeod <colin.macleod@ivata.com>
// $Revision: 1.5.2.1 $
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
// $Log: index.jsp,v $
// Revision 1.5.2.1  2005/10/08 17:14:15  colinmacleod
// Replaced use of JSPPicoContainerFactory with igw:bean tag.
//
// Revision 1.5  2005/04/30 13:04:12  colinmacleod
// Fixes reverting id type from String to Integer.
//
// Revision 1.4  2005/04/28 18:47:09  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.3  2005/04/11 09:17:04  colinmacleod
// Added new themes.
// Changed id type to String.
// Changed i tag to em and b tag to strong.
// Improved PicoContainerFactory with NanoContainer scripts.
//
// Revision 1.2  2005/04/09 17:19:12  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:50:47  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.8  2004/12/27 14:51:59  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.7  2004/11/03 15:42:31  colinmacleod
// Changed fieldPath to resourceFieldPath.
//
// Revision 1.6  2004/07/19 21:58:51  colinmacleod
// Changed Vector to List where possible.
//
// Revision 1.5  2004/07/14 22:50:21  colinmacleod
// Replaced bean:define with extended igw:bean.
//
// Revision 1.4  2004/07/14 20:59:44  colinmacleod
// Changed all occurrences of jsp:useBean to igw:bean (better tomcat compatibility).
//
// Revision 1.3  2004/07/13 19:41:18  colinmacleod
// Moved project to POJOs from EJBs.
// Applied PicoContainer to services layer (replacing session EJBs).
// Applied Hibernate to persistence layer (replacing entity EJBs).
//
// Revision 1.2  2004/03/21 21:16:20  colinmacleod
// Shortened name to ivata op.
//
// Revision 1.1  2004/03/03 20:27:38  colinmacleod
// Updated webapp structure. Moved core items to core subproject.
//
// Revision 1.2  2004/02/10 15:12:41  janboros
// fixing bug
//
// Revision 1.1.1.1  2004/01/27 20:58:01  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.5  2003/12/12 11:08:58  jano
// fixing aaddressbook functionaality
//
// Revision 1.4  2003/11/13 16:03:16  jano
// commitng everything to CVS
// can deploy and application is ruuning, can login into
//
// Revision 1.3  2003/11/07 14:54:15  jano
// commitng after fixing some bugs
//
// Revision 1.23  2003/08/05 14:54:26  jano
// addressBook extension
//
// Revision 1.22  2003/07/25 11:45:40  jano
// adding functionality for addressBook extension
//
// Revision 1.21  2003/07/22 10:15:56  jano
// fixing colums of contacts
//
// Revision 1.20  2003/06/10 10:02:32  jano
// fixing frames
//
// Revision 1.19  2003/06/10 05:57:38  peter
// img and html:img replaced with size-aware imhtml:img
//
// Revision 1.18  2003/06/02 23:39:44  colin
// changed forwards to redirects
//
// Revision 1.17  2003/04/14 07:30:04  peter
// fixed helpKey
//
// Revision 1.16  2003/03/04 17:19:09  colin
// fixed key for all groups
//
// Revision 1.15  2003/03/04 14:23:06  colin
// fixed javascript
//
// Revision 1.14  2003/03/03 17:29:43  colin
// removed misplaced bundle from html:html
//
// Revision 1.13  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.12  2003/02/25 12:10:05  peter
// removed clear parameter for person detail pop-ups
//
// Revision 1.11  2003/02/25 07:28:54  colin
// restructured java file paths
//
// Revision 1.10  2003/02/20 11:06:03  colin
// bugfix in form name at foot of page
//
// Revision 1.9  2003/02/20 08:29:46  colin
// cleared results to force a reload
//
// Revision 1.8  2003/02/18 13:13:55  colin
// reverted to single quotes for popups
//
// Revision 1.7  2003/02/18 10:38:26  colin
// conversion to Struts and pop-ups
//
// Revision 1.6  2003/01/27 12:22:38  colin
// renamed from personList.jsp to index.jsp (overwriting old frames index.jsp)
//
// Revision 1.24  2003/01/23 16:52:21  peter
// links and forms changed to sslext, checkLogin added
//
// Revision 1.23  2003/01/18 20:38:58  colin
// added help buttons
//
// Revision 1.22  2003/01/14 10:38:56  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.21  2002/12/18 14:31:18  jano
// fixing bug
//
// Revision 1.20  2002/12/16 14:58:45  jano
// go button
//
// Revision 1.19  2002/12/16 13:40:51  jano
// we need userName when calling findAllPeopleInGroup
//
// Revision 1.18  2002/12/16 08:20:27  jano
// fixing localization
//
// Revision 1.17  2002/12/13 12:45:56  jano
// we want difrent way of displaying title of index
//
// Revision 1.16  2002/12/12 15:10:12  jano
// we wnat to see groups with ACCESS_VIEW
//
// Revision 1.15  2002/11/22 16:44:38  peter
// localisation
//
// Revision 1.14  2002/10/16 14:34:51  colin
// made layout neater.
// limit number of telecom addresses to 5
//
// Revision 1.13  2002/10/03 11:59:21  jano
// default values for personTreeModel
//
// Revision 1.12  2002/09/30 13:16:53  jano
// we want to see all group
//
// Revision 1.11  2002/09/26 13:46:29  peter
// buggy jstl ForEachTags replaced by imutil ForEachTags
//
// Revision 1.10  2002/09/26 12:11:04  peter
// button title and tabs Capitalisation lost when resolving, ok now
//
// Revision 1.9  2002/09/26 11:13:08  peter
// fixed: filtering by index and group, tab capitalisation, forEach tag bugs
//
// Revision 1.8  2002/09/25 11:43:48  colin
// implemented new email address formatting
//
// Revision 1.7  2002/09/19 14:40:22  colin
// got the page to work in resin
// made the telecom addresses non-breaking
//
// Revision 1.6  2002/07/10 07:34:35  jano
// there was 'i' more so I have deleted it
//
// Revision 1.5  2002/07/01 08:07:56  colin
// moved SetPropertyTag to util from webgui
//
// Revision 1.4  2002/06/28 13:15:58  colin
// first addressbook release
//
// Revision 1.2  2002/06/13 15:45:21  peter
// brought over to peter, fixed bugs in webgui property-settings
//
// Revision 1.1  2002/06/13 11:10:59  colin
// first version with rose model and jboss  integration
//
// Revision 1.2  2002/02/10 14:33:24  colin
// corrections made whilst adding topic group rights
//
// Revision 1.1  2002/01/24 13:54:40  colin
// renamed:
// group_list.jsp to groupList.jsp
// group_frame.jsp to groupFrame.jsp
// person_list.jsp to personList.jsp
// person_list_item.jsp to personListItem.jsp
//
// Revision 1.4  2002/01/22 21:55:06  colin
// changed the object model path from com.ivata.groupware.address to com.ivata.groupware.business.addressbook.person
//
// Revision 1.3  2002/01/20 12:22:45  colin
// reformatted addressbook without frames
//
// Revision 1.2  2002/01/03 12:57:47  colin
// added group membership functionality
//
// Revision 1.1  2001/12/26 21:34:35  colin
// first working version
//
////////////////////////////////////////////////////////////////////////////////
--%>

<%@include file='/include/tags.jspf'%>
<igw:checkLogin />
<%@include file='/include/theme.jspf'%>

<%-- get the addressBook object --%>
<%@include file='/addressBook/include/addressBook.jspf'%>

<%-- we need to have a form for this page... --%>
<logic:notPresent name='addressBookIndexForm'>
  <logic:redirect forward='addressBookIndexAction'/>
</logic:notPresent>
<%-- this one forces an action reload every time - it is cleared at the bottom of the page --%>
<logic:notPresent name='addressBookIndexForm' property='results'>
  <logic:redirect forward='addressBookIndexAction'/>
</logic:notPresent>


<igw:bean id='emailAddressFormatter' scope='page' type='com.ivata.groupware.web.format.EmailAddressFormatter'/>
<jsp:setProperty name='emailAddressFormatter' property='pageContext' value='<%=pageContext%>'/>
<jsp:setProperty name='emailAddressFormatter' property='showAddress' value='true'/>

<%-- the default values of index and group --%>
<igw:bean id='addressBookIndexForm' scope='session' type='org.apache.struts.action.ActionForm'/>
<igw:bean id='comboGroup' name='addressBookIndexForm' property='comboGroup' type='java.lang.String'/>
<igw:bean id='comboAddressBook' name='addressBookIndexForm' property='comboAddressBook' type='java.lang.String'/>
<igw:bean id='index' name='addressBookIndexForm' property='index' type='java.lang.String'/>
<% List results = (List) PropertyUtils.getSimpleProperty(addressBookIndexForm, "results"); %>
<%--
<% HashMap addressBooks = (HashMap) PropertyUtils.getSimpleProperty(addressBookIndexForm, "addressBooks"); %>
--%>

<%-- groupModel for addressBooks tree >
<igw:bean id='addressBookTreeModel' scope='page' type='com.ivata.groupware.business.addressbook.person.group.tree.PersonTreeModel'/>
<jsp:setProperty name='addressBookTreeModel' property='overRideRoot' value='<%=GroupConstants.ADDRESS_BOOK%>'/>
<jsp:setProperty name='addressBookTreeModel' property='includePeople' value='false'/>


<igw:bean id='filter' scope='page' type='com.ivata.groupware.business.addressbook.person.group.right.UserRightFilter'/>
<%-- only show groups we can change >
<jsp:setProperty name='filter' property='userName' value='<%=(String) session.getAttribute("userName")%>'/>
<jsp:setProperty name='filter' property='access' value='<%=com.ivata.groupware.business.addressbook.person.group.right.RightConstants.ACCESS_VIEW%>'/>
<jsp:setProperty name='filter' property='detail' value='<%=com.ivata.groupware.business.addressbook.person.group.right.RightConstants.DETAIL_PERSON_GROUP_MEMBER%>'/>
<jsp:setProperty name='addressBookTreeModel' property='filter' value='<%=filter%>'/--%>

<%-- groupModel for groups in addressBook tree --%>
<igw:bean id='securitySession' scope='session' type='com.ivata.groupware.admin.security.server.SecuritySession'/>
<igw:bean id='personTreeModel' type='com.ivata.groupware.business.addressbook.person.group.tree.PersonTreeModel'/>
<jsp:setProperty name='personTreeModel' property='includePeople' value='false'/>
<%
TagUtils tagUtils = TagUtils.getInstance();
%>


<igw:bean id='title'><bean:message bundle='addressBook' key='index.title'/></igw:bean>

<igw:bean id='text' scope='page' type='java.lang.String'/>
<igw:bean id='textNext' scope='page' type='java.lang.String'/>

<imhtml:html locale='true'>
  <igw:head bundle='addressBook' titleKey='index.title'>
    <imhtml:base/>
    <script language="javascript">
      <!--
        function refreshComborGroup() {
          document.addressBookIndexForm.comboGroup.selectedIndex=0;
          document.addressBookIndexForm.submit();
        }
      // -->
    </script>
  </igw:head>
  <body class='blank' id='body1'>
    <imhtml:form action='/addressBook/index' bundle='addressBook' resourceFieldPath='index'>
      <div align='center'>

                                <input name='index' value='' type='hidden'>

        <imutil:map id='tabParams'>
          <imutil:mapEntry name='index' value='all' />
        </imutil:map>
        <imtheme:window
            styleClass='mainWindow'
            title='<%=title%>'>
          <%@include file='/addressBook/include/toolbar.jspf'%>
          <imutil:map id='findPersonParams'/>
          <imutil:mapClear mapName='findPersonParams' />
          <jsp:setProperty name='personPopUp' property='page' value='/addressBook/personFind.action'/>
          <jsp:setProperty name='personPopUp' property='paramsName' value='findPersonParams'/>

          <imtheme:tabFrame>
            <imtheme:tabControl name='addressBookTab'>
              <imtheme:tab paramsName='tabParams' page='/addressBook/index.action'><%=tagUtils.message(pageContext, "addressBook", Globals.LOCALE_KEY , "index.tab.all") %></imtheme:tab>
              <imutil:mapEntry mapName='tabParams' name='index' value='number'/>
              <imtheme:tab paramsName='tabParams' page='/addressBook/index.action'>#</imtheme:tab>
              <% textNext = "a"; %>
              <c:forEach var='counter' begin='0' end='25'>
                <igw:bean id='counter' type='java.lang.Integer'/>
                <%-- if this is the last tab, set the textNext to null --%>
                <% text = textNext; textNext = (counter.intValue() == 25) ? null :
                   new Character((char)('b' + counter.intValue())).toString(); %>
                <imutil:mapEntry mapName='tabParams' name='index' value='<%=text%>'/>
                <imtheme:tab paramsName='tabParams' page='/addressBook/index.action'><%=text%></imtheme:tab>
              </c:forEach>
            </imtheme:tabControl>
            <imtheme:tabContent>
              <imtheme:frame>
                <imtheme:framePane styleClass='text' style='padding: 0px; border: 0px;'>
                  <%! int nRow = 0; %>
                  <table style=''>
                    <tr>
                      <%int i = 0;%>
                      <c:if test='<%=results.isEmpty()%>'>
                        <td align='center'><em><bean:message bundle='addressBook' key='index.label.noResults'/></em></td>
                      </c:if>
                      <c:forEach var='person' items='<%=results%>' varStatus='status'>
                        <igw:bean id='person' type='com.ivata.groupware.business.addressbook.person.PersonDO'/>
                        <igw:bean id='status' type='javax.servlet.jsp.jstl.core.LoopTagStatus'/>
                        <td width='50%' style='border: 1px solid #aaaaaa; height: 60px; padding: 12px;'>
                          <imutil:mapEntry mapName='<%="findPersonParams"%>' name='<%="id"%>' value='<%=person.getId().toString()%>'/>
                          <igw:bean id='findPersonParams' name='findPersonParams' type='java.util.Map'/>
                          <div style='float: left;'>
                            <a href='#' onclick='<%=personPopUp%>return false;'><%=person.getFileAs()%></a>
                            <c:if test='<%=!StringHandling.isNullOrEmpty(person.getJobTitle())%>'>
                              <br/><em><%=person.getJobTitle()%></em>
                            </c:if>
                            <c:if test='<%=!StringHandling.isNullOrEmpty(person.getCompany())%>'>
                              <br/><strong><%=person.getCompany()%></strong>
                            </c:if>
                          </div>
                          <%
                            List telecomAddressList = new Vector();
                            telecomAddressList.addAll(person.getTelecomAddresses());
                          %>
                          <c:if test='<%=telecomAddressList.size() > 0 %>'>
                            <div style='float: right;'>
                              <c:forEach var='telecomAddress' items='<%=telecomAddressList%>'>
                                <igw:bean id='telecomAddress' name='telecomAddress' type='com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressDO'/>
                                <%-- don't show the email address here --%>
                                <c:if test='<%=!(telecomAddress.getType() ==
                                                  TelecomAddressConstants.TYPE_EMAIL)%>'>
                                    <em><bean:message bundle='addressBook' key='<%=TelecomAddressConstants.getTypeName(telecomAddress.getType())%>'/>:</em>
                                    &nbsp;<%=telecomAddress.getAddress().trim()%><br/>
                                </c:if>
                              </c:forEach>
                            </div>
                          </c:if>
                          <c:if test='<%=person.getEmailAddress() != null%>'>
                            <div style='text-align: center;'>
                              <%=emailAddressFormatter.format(person.getEmailAddress())%>
                            </div>
                          </c:if>
                      </td>
                      <%-- new line only on even cells, apart from the last one --%>
                      <c:if test='<%=((i++ % 2) != 0) && !status.isLast()%>'>
                        </tr><tr>
                      </c:if>
                    </c:forEach>
                    </tr>
                  </table>
                </imtheme:framePane>
              </imtheme:frame>
            </imtheme:tabContent>
          </imtheme:tabFrame>
          <imtheme:buttonFrame>
            <imhtml:help key='addressbook'/>
          </imtheme:buttonFrame>
        </imtheme:window>
      </div>
    </imhtml:form>
  </body>
</imhtml:html>
<%-- force it always to go thro' the action before the jsp --%>
<%PropertyUtils.setSimpleProperty(addressBookIndexForm, "results", null);%>
