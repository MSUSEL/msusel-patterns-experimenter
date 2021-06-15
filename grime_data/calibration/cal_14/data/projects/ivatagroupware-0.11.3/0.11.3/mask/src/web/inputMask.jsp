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
<%@ page contentType='text/html;charset=UTF-8'%>
<%@ page import="com.ivata.mask.valueobject.ValueObject"%>
<%@ page import="com.ivata.mask.util.StringHandling"%>
<%@ page import="com.ivata.mask.web.RewriteHandling"%>
<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: inputMask.jsp,v 1.4 2005/04/28 18:47:06 colinmacleod Exp $
//
// This page presents a mask to edit or value objects. This is an example only -
// you should make a similar input maks which displays value objects in a way
// which suits your application.
//
// since: ivata op 0.9 (2004-05-09)
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
// $Log: inputMask.jsp,v $
// Revision 1.4  2005/04/28 18:47:06  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.3  2005/04/10 20:23:54  colinmacleod
// Surrounded popup script with condition.
//
// Revision 1.2  2005/04/09 17:19:51  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:49:57  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.2  2004/12/31 18:54:09  colinmacleod
// First working version. Now displays address book DOs.
//
// Revision 1.1  2004/12/29 14:09:34  colinmacleod
// Changed subproject name from masks to mask
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@include file='/include/tags.jspf'%>

<igw:checkLogin />
<%@include file='/include/theme.jspf'%>


<igw:bean id="maskForm" name="InputMaskForm" type="com.ivata.mask.web.struts.InputMaskForm"/>
<igw:bean id="baseClassName" name="InputMaskForm" property="baseClass.name" type="java.lang.String"/>
<igw:bean id="menuFrameName" type="java.lang.String"/>
<igw:bean id="menuFrameURI" type="java.lang.String"/>
<igw:bean id="resourceFieldPath" type="java.lang.String"/>
<igw:bean id="deleteKey" type="java.lang.String"/>


<igw:bean id='persistenceRights' type="com.ivata.mask.persistence.right.PersistenceRights"/>
<igw:bean id='userName' name="securitySession" property="user.name" type="java.lang.String"/>
<igw:bean id='formatter' type='com.ivata.mask.web.format.HTMLFormatter'/>
<igw:bean id='caseFormat' type='com.ivata.mask.web.format.CaseFormat'/>
<imformat:addFormat formatter='<%=formatter%>' format='<%=caseFormat%>'/>

<imhtml:html locale='true'>
  <igw:head topLevel='true' keepOnTop='false'/>

  <body class='dialog'>
    <igw:bean id='windowTitle'>enter <imformat:format formatter='<%=formatter%>'><imhtml:label className="<%=baseClassName%>" afterText=''/></imformat:format> values</igw:bean>
    <imtheme:window title='<%=windowTitle%>'>
      <%--
        form action is taken from the mask or group.
        for masks, the type tells you the Struts ActionForward
        for groups, the id tells you the Struts ActionForward
      --%>
      <imhtml:form mask="<%=maskForm.getMask()%>">
        <html:hidden name='InputMaskForm' property='applyButtonHidden'/>
        <html:hidden name='InputMaskForm' property='clearButtonHidden'/>
        <html:hidden name='InputMaskForm' property='deleteButtonHidden'/>

        <html:hidden name='InputMaskForm' property='defaultForwardApply'/>
        <html:hidden name='InputMaskForm' property='defaultForwardDelete'/>
        <html:hidden name='InputMaskForm' property='defaultForwardOk'/>

        <html:hidden name='InputMaskForm' property='baseClass.name'/>
        <html:hidden name='InputMaskForm' property='mask.name'/>
        <html:hidden name='InputMaskForm' property='refreshOpener'/>
        <imhtml:hidden parameter='bundle'/>
        <imhtml:hidden parameter='deleteKey'/>
        <imhtml:hidden parameter='menuFrameName'/>
        <imhtml:hidden parameter='menuFrameURI'/>
        <imhtml:hidden parameter='resourceFieldPath'/>

        <input type='hidden' name='baseClass' value='<%=baseClassName%>'/>
        <input type='hidden' name='refresh' value='true'/>
        <%@include file='/include/errorFrame.jspf'%>
        <logic:notPresent name='InputMaskForm' property='valueObject'>
          <html:hidden name='InputMaskForm' property='valueObject.class.name' value='<%=baseClassName%>'/>
          <imtheme:frame>
            <imtheme:framePane styleClass='hilight'>
              <em>
                This object could no longer be found in the system. Please
                refresh the list page in your browser.
              </em>
            </imtheme:framePane>
          </imtheme:frame>
          <imtheme:buttonFrame>
            <imhtml:cancel fieldName='close'/>
          </imtheme:buttonFrame>
        </logic:notPresent>
        <logic:present name='InputMaskForm' property='valueObject'>
          <igw:bean id="valueObject" name="InputMaskForm" property="valueObject" type="com.ivata.mask.valueobject.ValueObject"/>
          <imtheme:frame>
            <imtheme:framePane styleClass='hilight'>
              <%--
                see the comment above the JavaScript at the bottom of the page
              --%>
              <igw:bean id="uRL"><imhtml:rewrite page="<%=menuFrameURI%>"/></igw:bean>
              <%--
                whether we start with ? or & depends on whether or not there are
                parameters already
              --%>
              <%uRL += (menuFrameURI.indexOf("?") == -1) ? "?" : "&";%>
              <html:hidden name='InputMaskForm' property='valueObject.class.name'/>
              <igw:bean id="selectedId" name="InputMaskForm" property="valueObject.idString" type="java.lang.String"/>
              <input type='hidden' name='menuFrameFullURL' value='<%=uRL
                  + "resourceFieldPath=" + resourceFieldPath
                  + "&deleteKey=" + deleteKey
                  // don't want to put out the selected id if we are deleting!
                  + ((request.getParameter("deleteWarn") != null)
                          ? "" : "&selectedId=" + selectedId)
                  + "&inputMaskInvoked=true"%>'/>
              <%--
                you only need an id if this object is not new
              --%>
              <c:if test='<%=!StringHandling.isNullOrEmpty(valueObject.getIdString())%>'>
                <html:hidden name='InputMaskForm' property='valueObject.idString'/>
              </c:if>
              <%int fieldNumber = 0;%>
              <table style='height: auto;'>
                <%--
                  if there are any pages of fields to be included, they come
                  first: note the strange workaround for jsp:include, which can't
                  'find' the Struts bean - at least in resin/jikes, it can't (not
                  sure why)
                --%>
                <logic:iterate id="actionForward" name="InputMaskForm" property="mask.includePaths" type="java.lang.String">
                  <igw:bean id='includePath'><imhtml:rewrite forward='<%=actionForward%>'/></igw:bean>
                  <%--
                    ok - this is a total hack :-)
                    is there a better way to convert Struts forward --> page?
                    if you know of one, answers please on a postcard
                    to colin.macleod@ivata.com
                  --%>
                  <%includePath = includePath.substring(RewriteHandling.getContextPath(request).length());%>
                  <jsp:include page='<%=includePath%>'/>
                </logic:iterate>
                <logic:iterate id="field" name="InputMaskForm" property="mask.fields" type="com.ivata.mask.field.Field">
                  <c:choose>
                    <c:when test='<%=field.isHidden()%>'>
                      <mask:field field="<%=field%>" valueObject="<%=valueObject%>"/>
                    </c:when>
                    <%--
                     sublists are treated differently, if this is not display-only
                    --%>
                    <c:when test='<%=!maskForm.isDisplayOnly() && ("sublist".equals(field.getType()))%>'>
                      <tr>
                        <th colspan='2' class='sublistHeading'><masks:fieldLabel field="<%=field%>"/></th>
                      </tr>
                      <tr>
                        <td colspan='2'>
                          <table class='sublist'>
                            <tr class='sublist'>
                              <td class='sublist' colspan='<%=field.getValueObjectMask().getFields().size() + 1%>'><masks:field field="<%=field%>" valueObject="<%=valueObject%>"/></td>
                            </tr>
                            <tr class='sublist'>
                              <logic:iterate id="subFieldForLabel" name="InputMaskForm" property='<%="mask.fields[" + fieldNumber + "].valueObjectMask.fields"%>'  type="com.ivata.mask.field.Field">
                                <th class='sublist'><masks:fieldLabel field="<%=field%>" subField="<%=subFieldForLabel%>" /></th>
                              </logic:iterate>
                              <%-- extra one for the buttons --%>
                              <th class='sublist'></th>
                            </tr>
                            <tr class='sublist'>
                              <logic:iterate id="subFieldForField" name="InputMaskForm" property='<%="mask.fields[" + fieldNumber + "].valueObjectMask.fields"%>'  type="com.ivata.mask.field.Field">
                                <td class='sublist'><masks:field field="<%=field%>" subField="<%=subFieldForField%>" valueObject="<%=valueObject%>"/></td>
                              </logic:iterate>
                              <%--
                                disabled buttons are enabled using JavaScript - makes sure
                                they are disabled if you don't have JavaScript :-)
                              --%>
                              <td class='sublist'><input onclick='<%="onConfirm(\"" + field.getName() + "\");return true"%>' disabled="disabled" id='<%=field.getName() + "_confirm"%>' type="button" value="Add"/><input onclick='<%="onRemove(\"" + field.getName() + "\");return true"%>' disabled="disabled" id='<%=field.getName() + "_remove"%>' type="button" value="Remove"/></td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <script language="JavaScript">
                        var addButton = document.getElementById('<%=field.getName() + "_confirm"%>');
                        addButton.disabled = false;
                      </script>
                    </c:when>
                    <%--
                      I want text areas to get the whole field row, other fields
                      appear on the right.
                      I would love to avoid this extra condition, maybe by using
                      divs with CSS, but I played with it for a day or two, and
                      couldn't it to work as consistently as these plain old table
                      definitions.
                      if anyone out there knows a better way of doing this (say
                      with CSS), a mail to colin.macleod@ivata.com would be
                      MAJORLY appreciated.
                    --%>
                    <c:when test='<%="textarea".equals(field.getType())%>'>
                      <tr class="fieldCaption <%=field.getType()%>">
                        <td class='fieldCaption <%=field.getType()%>' colspan='2'><imhtml:label fieldName="<%=field.getPath()%>"/></td>
                      </tr>
                      <tr class='field <%=field.getType()%>'>
                        <td class='field <%=field.getType()%>' colspan='2'><mask:field field="<%=field%>" valueObject="<%=valueObject%>" disabled="<%=!persistenceRights.canAmend(null, valueObject, field.getName())%>"/></td>
                      </tr>
                    </c:when>
                    <c:otherwise>
                      <tr class="fieldGroup <%=field.getType()%>">
                        <td class='fieldCaption <%=field.getType()%>'><imhtml:label fieldName="<%=field.getPath()%>"/></td>
                        <td class='field <%=field.getType()%>'><mask:field field="<%=field%>" valueObject="<%=valueObject%>" disabled="<%=!persistenceRights.canAmend(null, valueObject, field.getName())%>"/></td>
                      </tr>
                    </c:otherwise>
                  </c:choose>
                  <%++fieldNumber;%>
                </logic:iterate>
              </table>
            </imtheme:framePane>
          </imtheme:frame>
          <imtheme:buttonFrame>
            <%-- Reset button --%>
            <c:if test='<%=!maskForm.isClearButtonHidden()%>'>
              <imhtml:clear asNewButton='<%=!StringHandling.isNullOrEmpty(valueObject.getIdString())%>'/>
            </c:if>

            <%--
              only show the delete button if it is not a new value object
            --%>
            <c:if test='<%=!(maskForm.isDeleteButtonHidden() || StringHandling.isNullOrEmpty(valueObject.getIdString()))%>'>
              <%--
                only enable it if we are allowed to delete it (horrible double
                negative logic)
              --%>
              <imhtml:delete
                  disabled="<%=
                    !persistenceRights.canRemove(userName, valueObject)%>"/>
            </c:if>
            <imtheme:buttonSpacer/>
            <imhtml:ok/>
            <imhtml:cancel/>
            <c:if test='<%=!maskForm.isApplyButtonHidden()%>'>
              <imhtml:apply/>
            </c:if>
          </imtheme:buttonFrame>
        </logic:present>
      </imhtml:form>
    </imtheme:window>
    <%--
      request parameter set as hidden above - indicates we should refresh the
      menu frame
    --%>
    <logic:present parameter="refresh">
      <logic:notPresent parameter="deleteWarn">
        <%--
          this is funky - the URI of the menu (left) frame is set from a field
          in the form, so you can change the value using JavaScript in included
          pages
        --%>
        <script language="JavaScript" type="text/javascript">
          <!--
            window.top.<%=menuFrameName%>.location.href='<%=request.getParameter("menuFrameFullURL")%>';
        // -->
        </script>
      </logic:notPresent>
    </logic:present>
    <logic:present name='popUp'>
      <jsp:include page='/include/script/fixPopUp.jspf'/>
    </logic:present>
  </body>
</imhtml:html>
