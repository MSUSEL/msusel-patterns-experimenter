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
<%@ include file="/include/tags.jspf"%>
<%@ page import="com.ivata.mask.valueobject.ValueObject"%>
<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: list.jsp,v 1.3 2005/04/10 20:23:23 colinmacleod Exp $
//
// This page presents a list of value objects.
//
// Since: ivata groupware 0.9 (2004-05-09)
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
// $Log: list.jsp,v $
// Revision 1.3  2005/04/10 20:23:23  colinmacleod
// First version of list working for third party
// value objects.
//
// Revision 1.2  2005/04/09 17:19:51  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:49:57  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.1  2004/12/29 14:09:34  colinmacleod
// Changed subproject name from masks to mask
//
// Revision 1.3  2004/11/11 13:25:39  colinmacleod
// Added new menu (for the various data objects).
// Split off introduction for input masks and lists.
//
// Revision 1.2  2004/05/19 20:29:40  colinmacleod
// Added methods to add, amend and remove value objects.
//
// Revision 1.1.1.1  2004/05/16 20:40:08  colinmacleod
// Ready for 0.1 release
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%@ include file="/include/tags.jspf"%>
<bean:define id="mask" name="ListForm" property="mask" type="com.ivata.mask.Mask"/>
<bean:define id="listForm" name="ListForm" type="com.ivata.mask.web.struts.ListForm"/>
<bean:define id="baseClass" name="ListForm" property="baseClass" type="java.lang.Class"/>
<igw:bean id="valueObjects" name="ListForm" property="valueObjects" type="java.util.List"/>

<igw:bean id='formatter' type='com.ivata.mask.web.format.HTMLFormatter'/>
<igw:bean id='caseFormat' type='com.ivata.mask.web.format.CaseFormat'/>
<imformat:addFormat formatter='<%=formatter%>' format='<%=caseFormat%>'/>

<igw:bean id='title'><imformat:format formatter='<%=formatter%>'><imhtml:label className="<%=baseClass.getName()%>" afterText=''/> list</imformat:format></igw:bean>

<imutil:map id='inputParams'>
  <imutil:mapEntry name='baseClass' value='<%=baseClass.getName()%>'/>
</imutil:map>

<imtheme:window title='<%=title%>'>
  <imhtml:form action="/mask/list">
    <input type='hidden' name='baseClass' value='<%=baseClass.getName()%>'/>
    <imtheme:listFrame sortInternally='false' formName='document.genericForm'>
      <imtheme:listHeading>
        <imtheme:listColumnHeading>&nbsp;</imtheme:listColumnHeading>
        <logic:iterate id="field" name="ListForm" property="mask.fields"  type="com.ivata.mask.field.Field">
          <imtheme:listColumnHeading><imhtml:label fieldName="<%=field.getName()%>" afterText=''/></imtheme:listColumnHeading>
        </logic:iterate>
        <imtheme:listColumnHeading>&nbsp;</imtheme:listColumnHeading>
      </imtheme:listHeading>
      <imtheme:listBody var='valueObject' rows='<%=valueObjects%>'>
        <igw:bean id='valueObject' type='com.ivata.mask.valueobject.ValueObject'/>
        <imtheme:listColumn>
          PROPERTY(currentItemIndex).
        </imtheme:listColumn>
        <logic:iterate id="field2" name="ListForm" property="mask.fields"  type="com.ivata.mask.field.Field">
          <imtheme:listColumn>
            <mask:field field="<%=field2%>" valueObject="<%=valueObject%>"/>
          </imtheme:listColumn>
        </logic:iterate>
        <imutil:mapEntry mapName='inputParams' name='idString' value='<%=valueObject.getIdString()%>'/>
        <imtheme:listColumn><imhtml:link page="/mask/find.action" name="inputParams">view/amend/remove</imhtml:link></imtheme:listColumn>
      </imtheme:listBody>
      <imtheme:listEmpty>
        There are no items of this type.
      </imtheme:listEmpty>
    </imtheme:listFrame>
    <imtheme:buttonFrame>
      <imhtml:clear asNewButton='true'/>
    </imtheme:buttonFrame>
  </imhtml:form>
</imtheme:window>
