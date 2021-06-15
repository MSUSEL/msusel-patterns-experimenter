/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
// Source file: h:/cvslocal/ivata groupware/src/com/ivata/intranet/admin/setting/SettingsDataTypeException.java

/*
 * Copyright (c) 2001 - 2005 ivata limited.
 * All rights reserved.
 * -----------------------------------------------------------------------------
 * ivata groupware may be redistributed under the GNU General Public
 * License as published by the Free Software Foundation;
 * version 2 of the License.
 *
 * These programs are free software; you can redistribute them and/or
 * modify them under the terms of the GNU General Public License
 * as published by the Free Software Foundation; version 2 of the License.
 *
 * These programs are distributed in the hope that they will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU General Public License in the file LICENSE.txt for more
 * details.
 *
 * If you would like a copy of the GNU General Public License write to
 *
 * Free Software Foundation, Inc.
 * 59 Temple Place - Suite 330
 * Boston, MA 02111-1307, USA.
 *
 *
 * To arrange commercial support and licensing, contact ivata at
 *                  http://www.ivata.com/contact.jsp
 * -----------------------------------------------------------------------------
 * $Log: SettingsDataTypeException.java,v $
 * Revision 1.2  2005/04/09 17:19:57  colinmacleod
 * Changed copyright text to GPL v2 explicitly.
 *
 * Revision 1.1.1.1  2005/03/10 17:51:36  colinmacleod
 * Restructured ivata op around Hibernate/PicoContainer.
 * Renamed ivata groupware.
 *
 * Revision 1.2  2004/11/12 15:57:19  colinmacleod
 * Removed dependencies on SSLEXT.
 * Moved Persistence classes to ivata masks.
 *
 * Revision 1.1  2004/09/30 15:15:59  colinmacleod
 * Split off addressbook elements into security subproject.
 *
 * Revision 1.4  2004/07/13 19:54:31  colinmacleod
 * Moved project to POJOs from EJBs.
 * Applied PicoContainer to services layer (replacing session EJBs).
 * Applied Hibernate to persistence layer (replacing entity EJBs).
 *
 * Revision 1.3  2004/03/21 21:16:05  colinmacleod
 * Shortened name to ivata op.
 *
 * Revision 1.2  2004/02/01 22:00:32  colinmacleod
 * Added full names to author tags
 *
 * Revision 1.1.1.1  2004/01/27 20:57:46  colinmacleod
 * Moved ivata openportal to SourceForge..
 *
 * Revision 1.3  2003/10/17 12:36:12  jano
 * fixing problems with building
 * converting intranet -> portal
 * Eclipse building
 *
 * Revision 1.2  2003/10/15 13:04:21  colin
 * fixing for XDoclet
 *
 * Revision 1.1  2003/02/24 18:56:15  colin
 * added to admin
 *
 * Revision 1.4  2003/02/04 17:43:50  colin
 * copyright notice
 *
 * Revision 1.3  2002/06/21 12:10:03  colin
 * cosmetic changes. tidied up documentation
 *
 * Revision 1.2  2002/06/13 11:22:10  colin
 * first version with rose model integration.
 *
 * Revision 1.1  2002/04/27 17:36:44  colin
 * first compliling version in EJB/JBuilder project
 *
 * Revision 1.1.1.1  2002/04/22 13:51:47  colin
 * EJB version of the intranet project
 *
 * Revision 1.1  2002/02/03 13:30:08  colin
 * first version; used in Settings class to indicate that the wrong database data type was set
 * -----------------------------------------------------------------------------
 */
package com.ivata.groupware.admin.setting;

import com.ivata.mask.util.SystemException;


/**
 * <p>An instance of this class gets thrown by com.ivata.groupware.admin.settingsBean
 * if you try to access getStringSetting, getBooleanSetting or getIntegerSetting
 * and the datatype stored doesn't match the type requested.</p>
 *
 * @since 2002-02-03
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @see com.ivata.groupware.web.theme.Theme
 * @version $Revision: 1.2 $
 */
public class SettingsDataTypeException extends SystemException {

    /**
     * <p>Creates a new instance of SettingDataTypeException.</p>
     *
     * @param message a clear error Message indicating what should be
     * done to
     * resolve this exception.
     *
     */
    public SettingsDataTypeException(String message) {
        super(message);
    }
}
