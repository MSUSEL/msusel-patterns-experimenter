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

<%@page import="com.ivata.groupware.admin.setting.Settings" %>

<%@page import="com.ivata.mask.util.StringHandling"%>
<%@page import="com.ivata.mask.web.tag.theme.ThemeTag"%>
<%@page import="java.util.*"%>
<%@page import="javax.servlet.http.*"%>

<%@page import='org.apache.struts.Globals' %>
<%@ page import="com.ivata.groupware.container.persistence.hibernate.HibernateSetupConstants" %>
<%@ page import="com.ivata.groupware.business.mail.struts.MailSetupForm" %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: index.jsp,v 1.4 2005/04/28 18:47:08 colinmacleod Exp $
//
// If you start ivata groupware with the default db, this page appears to let
// you configure the database connection and mail server.
//
// Since: ivata groupware 0.11 (2005-03-25)
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
// $Log: index.jsp,v $
// Revision 1.4  2005/04/28 18:47:08  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.2  2005/04/22 09:43:59  colinmacleod
// Fixed mail setup form path.
//
// Revision 1.1  2005/04/11 10:40:41  colinmacleod
// Added setup feature.
//
////////////////////////////////////////////////////////////////////////////////
--%>

<%@include file='/include/tags.jspf'%>
<logic:notPresent name='securitySession'>
  <logic:redirect action='/setup'/>
</logic:notPresent>
<logic:notPresent name='setupForm'>
  <logic:redirect action='/setup'/>
</logic:notPresent>
<jsp:include page="/generateCSS"/>
<c:catch>
  <%@include file='/include/theme.jspf'%>
</c:catch>

<igw:getSetting id='siteName' setting='siteName' type='java.lang.String'/>
<igw:bean id='setupForm' name='setupForm' scope='request' type='com.ivata.groupware.business.mail.struts.MailSetupForm'/>

<igw:bean id='windows' name='setupForm' property='windows' type='java.lang.Boolean'/>

<%-- if we're in pop-up mode, don't show a title --%>
<igw:bean id='title' type='java.lang.String'><bean:message key='setup.title'/></igw:bean>
<imhtml:html locale='true'>
  <igw:head title='<%=title%>' login='true' topLevel='true'>
    <%-- this javascript ensures that this window is not within an HTML frame--%>
    <script type='text/javascript'>
      <%--
      // if you select to created the DB automatically, then that limits what
      // you can change. the db type is set to HypersonicSQL with the
      // appropriate driver, and a default URL
      --%>
      function onChangeCreateDatabaseAutomatically() {
        var setupForm = document.getElementById("setupForm");
        if (setupForm.createDatabaseAutomatically.checked) {
          setupForm.hiddenDatabaseDialect.value = setupForm.databaseDialect.value;
          setupForm.hiddenDatabaseDriver.value = setupForm.databaseDriver.value;
          setupForm.hiddenDatabasePassword.value = setupForm.databasePassword.value;
          setupForm.hiddenDatabaseUserName.value = setupForm.databaseUserName.value;
          setupForm.hiddenDatabaseURL.value = setupForm.databaseURL.value;

          setupForm.databaseDialect.value = "<%=HibernateSetupConstants.AUTOMATIC_DATABASE_DIALECT%>";
          setupForm.databaseDriver.value = "<%=HibernateSetupConstants.AUTOMATIC_DATABASE_DRIVER%>";
          setupForm.databasePassword.value = "<%=HibernateSetupConstants.AUTOMATIC_DATABASE_PASSWORD%>";
          setupForm.databaseUserName.value = "<%=HibernateSetupConstants.AUTOMATIC_DATABASE_USER_NAME%>";
          setupForm.databaseURL.value = "<%=HibernateSetupConstants.AUTOMATIC_DATABASE_URL%>";
          disableDatabaseFields(true);
        } else {
          setupForm.databaseDialect.value = setupForm.hiddenDatabaseDialect.value;
          setupForm.databaseDriver.value = setupForm.hiddenDatabaseDriver.value;
          setupForm.databasePassword.value = setupForm.hiddenDatabasePassword.value;
          setupForm.databaseUserName.value = setupForm.hiddenDatabaseUserName.value;
          setupForm.databaseURL.value = setupForm.hiddenDatabaseURL.value;
          disableDatabaseFields(false);
        }
      }
      <%--
      // enable/disable the database fields depending on whether or not the
      // settings are user-chosen or automatic
      --%>
      function disableDatabaseFields(disabled) {
          var setupForm = document.getElementById("setupForm");
          setupForm.databaseDialect.disabled = disabled;
          setupForm.databaseDriver.disabled = disabled;
          setupForm.databasePassword.disabled = disabled;
          setupForm.databaseUserName.disabled = disabled;
      }

      <%--
      // when the dialect is changed, we load the default driver and URL
      --%>
      function onChangeDatabaseDialect() {
        var setupForm = document.getElementById("setupForm");
        if (setupForm.databaseDrivers[setupForm.databaseDialect.selectedIndex].value != "") {
          setupForm.databaseDriver.value = setupForm.databaseDrivers[setupForm.databaseDialect.selectedIndex].value;
          setupForm.databaseURL.value = setupForm.databaseURLs[setupForm.databaseDialect.selectedIndex].value;
        }
      }
      <%--
      // when the URL is changed, and the database should be created
      // automatically, we check that the format is still a HypersonicSQL file
      --%>
      function onChangeDatabaseURL() {
        var setupForm = document.getElementById("setupForm");
        if (setupForm.createDatabaseAutomatically.checked
            && (setupForm.databaseURL.value.indexOf("<%=HibernateSetupConstants.AUTOMATIC_DATABASE_URL_START%>") != 0)) {
          alert ("If you want to create a database automatically, the URL must start with '<%=HibernateSetupConstants.AUTOMATIC_DATABASE_URL_START%>', followed by valid path on the local filesystem.");
          setupForm.databaseURL.value = "<%=HibernateSetupConstants.AUTOMATIC_DATABASE_URL%>";
        }
      }
      <%--
      // keep this as the top-level frame
      --%>
      if (opener != null && opener != self ) {
        opener.location.href = self.location.href;
        self.close();
      }
    </script>
    <link rel='stylesheet' href='<%=request.getContextPath()%>/style/login.css' type='text/css' />
    <html:base/>
  </igw:head>
  <body>
    <imhtml:form styleId='setupForm'
        action='/setup'
        resourceFieldPath='setup'>
      <imtheme:window
          styleClass='loginWindow'
          titleKey='setup.title.window'
          titleArgs='<%=Arrays.asList(new Object[] {siteName})%>'>
        <%@include file='/include/errorFrame.jspf'%>
        <imtheme:frame>
          <imtheme:framePane>
            <bean:message key="setup.label.intro" arg0="<%=siteName%>"/>
          </imtheme:framePane>
        </imtheme:frame>
        <imtheme:frame>
          <imtheme:framePane>
            <table style='fieldTable'>
              <tr>
                <td class='fieldCaption'></td>
                <td class='field'><imhtml:checkbox fieldName="createDatabaseAutomatically" onchange="onChangeCreateDatabaseAutomatically(); return true"/></td>
              </tr>
              <tr>
                <td class='fieldCaption'><imhtml:label fieldName="databaseType"/></td>
                <td class='field'>
                  <imhtml:select fieldName="databaseDialect" onchange='onChangeDatabaseDialect(); return true'>
                    <% int i =0;%>
                    <logic:iterate id="databaseType" name="setupForm" property="databaseTypes">
                      <igw:bean id='databaseType' type='java.lang.Object'/>
                      <igw:bean id='databaseDialect' name='setupForm' property='<%="databaseDialects[" + i + "]"%>' type='java.lang.Object'/>
                      <html:option value='<%=databaseDialect.toString()%>'><%=databaseType%></html:option>
                      <%++i;%>
                    </logic:iterate>
                  </imhtml:select>
                  <imhtml:hidden name='setupForm' property='databaseURLs'/>
                  <imhtml:hidden fieldName='windows'/>
                  <imhtml:hidden name='setupForm' property='<%="databaseDrivers"%>'/>
                  <input type='hidden' name='hiddenDatabaseDialect'/>
                </td>
              </tr>
              <tr>
                <td class='fieldCaption'><imhtml:label fieldName="databaseURL"/></td>
                <td class='field'>
                  <imhtml:text fieldName="databaseURL" onchange='onChangeDatabaseURL(); return true'/>
                  <input type='hidden' name='hiddenDatabaseURL'/>
                </td>
              </tr>
              <tr>
                <td class='fieldCaption'><imhtml:label fieldName="databaseDriver"/></td>
                <td class='field'>
                  <imhtml:text fieldName="databaseDriver"/>
                  <input type='hidden' name='hiddenDatabaseDriver'/>
                </td>
              </tr>
              <tr>
                <td class='fieldCaption'><imhtml:label fieldName="databaseUserName"/></td>
                <td class='field'>
                  <imhtml:text fieldName="databaseUserName"/>
                  <input type='hidden' name='hiddenDatabaseUserName'/>
                </td>
              </tr>
              <tr>
                <td class='fieldCaption'><imhtml:label fieldName="databasePassword"/></td>
                <td class='field'>
                  <imhtml:password fieldName="databasePassword"/>
                  <input type='hidden' name='hiddenDatabasePassword'/>
                </td>
              </tr>
            </table>
          </imtheme:framePane>
        </imtheme:frame>
        <imtheme:frame>
          <imtheme:framePane>
            <table style='fieldTable'>
              <tr>
                <td class='fieldCaption'><imhtml:label fieldName="mailDomain"/></td>
                <td class='field'>
                  <%-- show a combo box for hMailServer on Windows --%>
                  <c:choose>
                    <c:when test='<%=windows.booleanValue()%>'>
                      <imhtml:select fieldName='mailDomain'>
                        <logic:iterate id="mailDomain" name="setupForm" property="mailDomains">
                          <igw:bean id='mailDomain' type='java.lang.Object'/>
                          <html:option value='<%=mailDomain.toString()%>'><%=mailDomain%></html:option>
                        </logic:iterate>
                      </imhtml:select>
                    </c:when>
                    <c:otherwise>
                      <imhtml:text fieldName="mailDomain"/>
                    </c:otherwise>
                  </c:choose>
                </td>
              </tr>
              <tr>
                <td class='fieldCaption'><imhtml:label fieldName="mailHostSMTP"/></td>
                <td class='field'><imhtml:text fieldName="mailHostSMTP"/></td>
              </tr>
              <tr>
                <td class='fieldCaption'><imhtml:label fieldName="mailHostIMAP"/></td>
                <td class='field'><imhtml:text fieldName="mailHostIMAP"/></td>
              </tr>
              <%-- you don't need the scripts with hMailServer on Windows --%>
              <c:if test='<%=!windows.booleanValue()%>'>
                <tr>
                  <td class='fieldCaption'><imhtml:label fieldName="scriptsPath"/></td>
                  <td class='field'><imhtml:text fieldName="scriptsPath"/></td>
                </tr>
              </c:if>
            </table>
          </imtheme:framePane>
        </imtheme:frame>
        <imtheme:buttonFrame>
          <imhtml:ok/>
        </imtheme:buttonFrame>
      </imtheme:window>
    </imhtml:form>
    <%@ include file='/theme/include/standards.jspf' %>
    <script type='text/javascript'>
      var setupForm = document.getElementById("setupForm");
      if (setupForm.createDatabaseAutomatically.checked) {
        onChangeCreateDatabaseAutomatically();
      }
    </script>
  </body>
</imhtml:html>
