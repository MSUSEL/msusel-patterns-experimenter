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
// Source file: h:/cvslocal/ivata groupware/src/com.ivata.groupware/admin/security/struts/JavaScriptAction.java

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
 * $Log: JavaScriptAction.java,v $
 * Revision 1.3  2005/04/10 18:47:41  colinmacleod
 * Changed i tag to em and b tag to strong.
 *
 * Revision 1.2  2005/04/09 17:19:57  colinmacleod
 * Changed copyright text to GPL v2 explicitly.
 *
 * Revision 1.1.1.1  2005/03/10 17:51:40  colinmacleod
 * Restructured ivata op around Hibernate/PicoContainer.
 * Renamed ivata groupware.
 *
 * Revision 1.5  2004/12/31 18:27:44  colinmacleod
 * Added MaskFactory to constructor of MaskAction.
 *
 * Revision 1.4  2004/12/23 21:01:30  colinmacleod
 * Updated Struts to v1.2.4.
 * Changed base classes to use ivata masks.
 *
 * Revision 1.3  2004/11/12 18:19:16  colinmacleod
 * Change action and form classes to extend MaskAction, MaskForm respectively.
 *
 * Revision 1.2  2004/11/03 15:31:51  colinmacleod
 * Change method interfaces to remove log.
 *
 * Revision 1.1  2004/09/30 15:15:59  colinmacleod
 * Split off addressbook elements into security subproject.
 *
 * Revision 1.4  2004/07/13 19:41:12  colinmacleod
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
 * Revision 1.1.1.1  2004/01/27 20:57:45  colinmacleod
 * Moved ivata openportal to SourceForge..
 *
 * Revision 1.3  2003/11/13 16:03:15  jano
 * commitng everything to CVS
 * can deploy and application is ruuning, can login into
 *
 * Revision 1.2  2003/10/17 12:36:12  jano
 * fixing problems with building
 * converting intranet -> portal
 * Eclipse building
 *
 * Revision 1.1.1.1  2003/10/13 20:50:06  colin
 * Restructured portal into subprojects
 *
 * Revision 1.1  2003/02/24 18:53:57  colin
 * added to admin
 *
 * Revision 1.2  2003/02/04 17:38:13  colin
 * updated for new execute interface
 *
 * Revision 1.1  2003/01/18 20:29:42  colin
 * converted login process to struts
 * added checking for javascript at login
 * -----------------------------------------------------------------------------
 */
package com.ivata.groupware.admin.security.struts;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ivata.groupware.admin.security.Security;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.struts.MaskAuthenticator;

/**
 * <p>Detects whether or not the browser has <em>JavaScript</em>.</p>
 *
 * @since 2003-01-18
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 */
public class JavaScriptAction extends LoginAction {
    /**
     * <p>
     * Construct the javascript action.
     * </p>
     *
     * @param security see {@link LoginAction}
     * @param settings see {@link LoginAction}
     * @param maskFactory This factory is needed to access the masks and groups
     * of masks.
     * @param authenticator used to confirm whether or not the
     * user should be allowed to continue, in the <code>execute</code> method.
     */
    public JavaScriptAction(Security security, Settings settings,
            MaskFactory maskFactory, MaskAuthenticator authenticator) {
        super(security, settings, maskFactory, authenticator);
    }

    /**
     * <p>Overridden from the default intranet implementation to
     * detect <em>JavaScript</em>.</p>
     *
     * @param mapping current action mapping from <em>Struts</em> config.
     * @param log valid logging object to write messages to.
     * @param errors valid errors object to append errors to. If there are
     * any errors, the action will return to the input.
     * @param form optional ActionForm bean for this request (if any)
     * @param request non-HTTP request we are processing
     * @param response The non-HTTP response we are creating
     * @param session  returned from the <code>request</code> parameter.
     * @param guestUserName current user name from session. Not needed for
     * this aciton.
     * @param settings valid, non-null settings from session.
     * @exception SystemException if there is any problem which
     * prevents processing. It will result in the webapp being forwarded
     * to
     * the standard error page.
     * @return this method returns the string used to identify the correct
     * <em>Struts</em> <code>ActionForward</code> which should follow this
     * page, or <code>null</code> if it should return to the input.
     *
     */
    public String execute(final ActionMapping mapping,
            final ActionErrors errors,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session)
            throws SystemException {

        // if we don't have a form or the form doesn't have java script set
        // then just forward to the intranet login - that's our input screen
        if (form == null) {
            return null;
        }
        String javaScriptVersion = null;
        ActionForm loginForm = (ActionForm) session.getAttribute("loginForm");
        try {
            javaScriptVersion = (String) PropertyUtils.getSimpleProperty(form, "javaScriptVersion");
        } catch (NoSuchMethodException e) {
            throw new SystemException(e);
        } catch (InvocationTargetException e) {
            throw new SystemException(e);
        } catch (IllegalAccessException e) {
            throw new SystemException(e);
        }
        if (StringHandling.isNullOrEmpty(javaScriptVersion)) {
            return super.execute(mapping, errors, loginForm, request, response, session);
        }

        // if it gets here, set the javascript version in the login form
        if (loginForm == null) {
            return super.execute(mapping, errors, loginForm, request, response, session);
        }
        try {
            PropertyUtils.setSimpleProperty(loginForm, "javaScriptVersion",
                javaScriptVersion);
        } catch (NoSuchMethodException e) {
            throw new SystemException(e);
        } catch (InvocationTargetException e) {
            throw new SystemException(e);
        } catch (IllegalAccessException e) {
            throw new SystemException(e);
        }
        return super.execute(mapping, errors, loginForm, request, response, session);
    }
}
