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
// Source file: h:/cvslocal/ivata groupware/src/com.ivata.groupware/admin/security/struts/LoginAction.java

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
 * $Log: LoginGuestAction.java,v $
 * Revision 1.3  2005/04/10 19:38:21  colinmacleod
 * Updated login pages to change theme.
 *
 * Revision 1.2  2005/04/09 17:19:57  colinmacleod
 * Changed copyright text to GPL v2 explicitly.
 *
 * Revision 1.1.1.1  2005/03/10 17:51:40  colinmacleod
 * Restructured ivata op around Hibernate/PicoContainer.
 * Renamed ivata groupware.
 *
 * Revision 1.2  2004/12/31 18:27:44  colinmacleod
 * Added MaskFactory to constructor of MaskAction.
 *
 * Revision 1.1  2004/12/23 20:50:58  colinmacleod
 * Split off guest login from LoginAction into a new action.
 * -----------------------------------------------------------------------------
 */
package com.ivata.groupware.admin.security.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.ivata.groupware.admin.security.Security;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.browser.Browser;
import com.ivata.mask.web.struts.MaskAuthenticator;

/**
 * <p>This <code>Action</code> is invoked whenever you don't have a valid
 * session.</p>
 *
 * @since 2004-12-23
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 */
public class LoginGuestAction extends LoginAction {
    private Security security;

    /**
     * TODO
     * @param security
     * @param maskFactory This factory is needed to access the masks and groups
     * of masks.
     * @param authenticator used to confirm whether or not the
     * user should be allowed to continue, in the <code>execute</code> method.
     */
    public LoginGuestAction(final Security securityParam,
            final Settings settingsParam,
            final MaskFactory maskFactoryParam,
            final MaskAuthenticator authenticatorParam) {
        super(securityParam, settingsParam, maskFactoryParam,
                authenticatorParam);
        this.security = securityParam;
    }

    /**
     * Check the form is valid and, if not, return the action forward we should
     * go to, to sort it out.
     *
     * @param mapping current action mapping from <em>Struts</em> config.
     * @param formParam optional ActionForm bean for this request (if any).
     * @return <code>null</code> if the action should continue, otherwise the
     * name of a forward to pass control to.
     */
    protected String checkForm(final ActionMapping mappingParam,
            final ActionForm formParam) {
        if (!"loginForm".equals(mappingParam.getName())) {
            return "loginGuest";
        }
        return null;
    }

    /**
     * <p>Overridden from the default intranet implementation to
     * manipulate user login.</p>
     *
     * @param mapping current action mapping from <em>Struts</em> config.
     * @param log valid logging object to write messages to.
     * @param errors valid errors object to append errors to. If there are
     * any errors, the action will return to the input.
     * @param formParam optional ActionForm bean for this request (if any)
     * @param request non-HTTP request we are processing
     * @param response The non-HTTP response we are creating
     * @param session  returned from the <code>request</code> parameter.
     * @param guestUserName current user name from session. Not needed for
     * this action.
     * @param settings valid, non-null settings from session.
     * @exception SystemException if there is any problem which
     * prevents processing. It will result in the webapp being forwarded
     * to
     * the standard error page.
     * @return this method returns the string used to identify the correct
     * <em>Struts</em> <code>ActionForward</code> which should follow this
     * page, or <code>null</code> if it should return to the input.
     */
    public String execute(final ActionMapping mapping,
            final ActionErrors errors,
            final ActionForm formParam,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session) throws SystemException {
        // if this mapping is not _really_ for us, go 'round again...
        String checkForward = checkForm(mapping, formParam);
        if (checkForward != null) {
            return checkForward;
        }
        SecuritySession securitySession;
        ActionForm form = formParam;
        securitySession = security.loginGuest();
        session.setAttribute("securitySession", securitySession);
        // for now, create a browser with no javascript support
        Browser browser = new Browser(request.getHeader("User-Agent"), null);
        session.setAttribute("browser", browser);

        form = RequestUtils.createActionForm(request, mapping,
                mapping.getModuleConfig(), servlet);
        if (form == null) {
            request.setAttribute("exception",
                    new NullPointerException(
                            "createActionForm returned null form for mapping '"
                            + mapping.getName()
                            + "' in LoginGuestAction"));
            return "error";
        }
        session.setAttribute("loginForm", form);
        return "success";
    }


    /**
     * <p>Overrides and extends (calls) the super class implementation to
     * tell it not to check the session.</p>
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request (if any).
     * @param request The non-HTTP request we are processing.
     * @param response The non-HTTP response we are creating.
     * @exception Exception if the application business logic throws
     * an exception.
     * @return this method returns a <code>"success"</code>
     * <code>ActionForward</code> if the compose session is cancelled or
     * successfully sent, otherwise a <code>"failure"</code>
     * <code>ActionForward</code>.
     *
     */
    public ActionForward execute(final ActionMapping mapping,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        // this prevents us always going around in circles!!
        setLogin(true);
        return super.execute(mapping, form, request, response);
    }
}
