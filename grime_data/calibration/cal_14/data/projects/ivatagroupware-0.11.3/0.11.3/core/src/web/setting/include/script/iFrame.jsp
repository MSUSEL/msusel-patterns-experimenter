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

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: iFrame.jsp,v 1.2 2005/04/09 17:19:39 colinmacleod Exp $
//
// JavaScript methods used in those tabs of /setting/index.jsp that use
// IFrames.
//
// Since: ivata groupware 0.9 (2003-02-14)
// Author: Peter Illes
// $Revision: 1.2 $
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
// $Log: iFrame.jsp,v $
// Revision 1.2  2005/04/09 17:19:39  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1  2005/03/10 19:23:46  colinmacleod
// Moved to ivata groupware.
//
// Revision 1.2  2004/03/21 21:16:24  colinmacleod
// Shortened name to ivata op.
//
// Revision 1.1  2004/03/03 21:32:35  colinmacleod
// Moved core files to core subproject
//
// Revision 1.1.1.1  2004/01/27 20:58:18  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.1.1.1  2003/10/13 20:50:52  colin
// Restructured portal into subprojects
//
// Revision 1.1  2003/03/04 15:44:52  colin
// renamed javaScriptFrame.jsp to script/iFrame.jsp
//
// Revision 1.3  2003/02/19 18:17:45  peter
// added methods to handle value copy to parent
//
// Revision 1.2  2003/02/14 17:07:14  peter
// started to convert to imhtml
//
// Revision 1.1  2003/02/14 14:56:49  peter
// added to cvs
//
////////////////////////////////////////////////////////////////////////////////
--%>


<%--
  // the function is called whenever the user changes a field.
  // The same method in the parent is called, as the new content was
  // already copied to the hidden fields in the parent
--%>
function onFieldChangeIFrame(fieldName) {
    parent.onFieldChange(fieldName);
}

<%--
  // the function is called when the user clicks on the revert button, the
  // the system setting is copied to the user
--%>
function revertToSystemIFrame(fieldName) {
    var visibleField = document.getElementById(fieldName);
    var hiddenSystemField = parent.document.getElementById("system_" + fieldName);
    var hiddenUserField = parent.document.getElementById("user_" + fieldName);
    parent.copyField(hiddenSystemField, hiddenUserField);
    <%-- show him the setting, when he is in user area --%>
    if (parent.detectSettingArea()=='user') {
        parent.copyField(hiddenUserField, visibleField);
    }
}
