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
 * $Log: LoginAction.java,v $
 * Revision 1.5  2005/04/22 10:27:23  colinmacleod
 * Changed to using hibernate properties
 * rather than the hibernate configuration
 * instance directly.
 *
 * Revision 1.4  2005/04/10 19:38:22  colinmacleod
 * Updated login pages to change theme.
 *
 * Revision 1.3  2005/04/09 17:19:57  colinmacleod
 * Changed copyright text to GPL v2 explicitly.
 *
 * Revision 1.2  2005/03/16 15:55:43  colinmacleod
 * Changed debug logging for no java script to info.
 *
 * Revision 1.1.1.1  2005/03/10 17:51:40  colinmacleod
 * Restructured ivata op around Hibernate/PicoContainer.
 * Renamed ivata groupware.
 *
 * Revision 1.6  2004/12/31 18:27:44  colinmacleod
 * Added MaskFactory to constructor of MaskAction.
 *
 * Revision 1.5  2004/12/23 20:50:58  colinmacleod
 * Split off guest login from LoginAction into a new action.
 *
 * Revision 1.4  2004/11/12 18:19:16  colinmacleod
 * Change action and form classes to extend MaskAction, MaskForm respectively.
 *
 * Revision 1.3  2004/11/12 15:57:19  colinmacleod
 * Removed dependencies on SSLEXT.
 * Moved Persistence classes to ivata masks.
 *
 * Revision 1.2  2004/11/03 15:31:51  colinmacleod
 * Change method interfaces to remove log.
 *
 * Revision 1.1  2004/09/30 15:15:59  colinmacleod
 * Split off addressbook elements into security subproject.
 *
 * Revision 1.7  2004/07/19 21:58:35  colinmacleod
 * Changed Vector to List where possible.
 *
 * Revision 1.6  2004/07/18 16:16:55  colinmacleod
 * Added checking that the form is present, and of the right class.
 *
 * Revision 1.5  2004/07/13 19:41:12  colinmacleod
 * Moved project to POJOs from EJBs.
 * Applied PicoContainer to services layer (replacing session EJBs).
 * Applied Hibernate to persistence layer (replacing entity EJBs).
 *
 * Revision 1.4  2004/03/21 21:16:05  colinmacleod
 * Shortened name to ivata op.
 *
 * Revision 1.3  2004/03/21 20:20:54  colinmacleod
 * Changed session variable called mailSession to securityServerSession.
 *
 * Revision 1.2  2004/02/01 22:00:32  colinmacleod
 * Added full names to author tags
 *
 * Revision 1.1.1.1  2004/01/27 20:57:45  colinmacleod
 * Moved ivata openportal to SourceForge..
 *
 * Revision 1.5  2003/11/13 16:03:15  jano
 * commitng everything to CVS
 * can deploy and application is ruuning, can login into
 *
 * Revision 1.4  2003/11/07 14:54:15  jano
 * commitng after fixing some bugs
 *
 * Revision 1.3  2003/10/24 13:18:12  jano
 * fixing some bugs
 *
 * Revision 1.2  2003/10/17 12:36:12  jano
 * fixing problems with building
 * converting intranet -> portal
 * Eclipse building
 *
 * Revision 1.1.1.1  2003/10/13 20:50:07  colin
 * Restructured portal into subprojects
 *
 * Revision 1.7  2003/07/05 15:57:25  colin
 * Added possibility for empty jndiPrefix = root default context
 *
 * Revision 1.6  2003/03/14 10:26:06  jano
 * adding backdoor man functionality
 * backdoor man = briezky
 *
 * Revision 1.5  2003/03/12 14:31:53  peter
 * fixed jndiPrefix handling - one webapp problem
 *
 * Revision 1.4  2003/03/11 18:25:45  colin
 * changes to allow just one webapp
 *
 * Revision 1.3  2003/03/05 15:16:48  colin
 * fixed jndiPrefix to request.getContextPath
 *
 * Revision 1.2  2003/02/27 12:03:16  colin
 * changed forward to redirect to avoid displaying the parameters in the browser
 *
 * Revision 1.1  2003/02/24 18:53:57  colin
 * added to admin
 *
 * Revision 1.5  2003/02/06 12:41:22  colin
 * changed checks for null to checks for isNullOrEmpty (login, forward)
 *
 * Revision 1.4  2003/02/06 12:22:25  colin
 * changed from errors() to isErrors() (a la struts 1.1c)
 *
 * Revision 1.3  2003/02/04 17:38:14  colin
 * updated for new execute interface
 *
 * Revision 1.2  2003/01/27 08:59:12  colin
 * simplified version of login with keepAlive.jsp
 *
 * Revision 1.1  2003/01/18 20:29:42  colin
 * converted login process to struts
 * added checking for javascript at login
 * -----------------------------------------------------------------------------
 */
package com.ivata.groupware.admin.security.struts;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.RequestUtils;
import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.Security;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.admin.setting.SettingNullException;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.groupware.admin.setting.SettingsDataTypeException;
import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.groupware.container.persistence.hibernate.HibernateSetupConstants;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.persistence.FinderException;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.browser.Browser;
import com.ivata.mask.web.browser.BrowserConstants;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;
import com.ivata.mask.web.tag.theme.ThemeConstants;

/**
 * <p>This <code>Action</code> is invoked whenever someone tries to
 * login to the intranet system.</p>
 *
 * @since 2003-01-15
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.5 $
 */
public class LoginAction extends MaskAction {
    private Security security;
    private Settings settings;
    /**
     * <p>
     * Log4j logger for, well, logging.
     * </p>
     */
    private Logger log = Logger.getLogger(LoginAction.class);

    /**
     * TODO
     * @param security
     * @param settings
     * @param maskFactory This factory is needed to access the masks and groups
     * of masks.
     * @param authenticator used to confirm whether or not the
     * user should be allowed to continue, in the <code>execute</code> method.
     */
    public LoginAction(Security security, Settings settings,
            MaskFactory maskFactory, MaskAuthenticator authenticator) {
        super(maskFactory, authenticator);
        this.security = security;
        this.settings = settings;
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
     * this aciton.
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
        ActionForm form = formParam;
        String forward = null;
        String help = null;
        String javaScriptVersion = null;
        String jndiPrefix = null;
        String login = null;
        String password = null;

        String userName = null;
        SecuritySession securitySession = (SecuritySession) session.getAttribute("securitySession");

        // the value for the forward changes if there is no javascript
        String errorForward = "login";
        if ((form == null)
                || (securitySession == null)) {
            return "loginGuestAction";
        }

        // if the URL is still set to the default database (in memory), check
        // the user has confirmed this should be so and forward to the setup
        // page, if not
        PicoContainerFactory factory = PicoContainerFactory.getInstance();
        assert (factory != null);
        PicoContainer container = factory.getGlobalContainer();
        assert (container != null);
        Properties hibernateProperties = (Properties) container
            .getComponentInstance("hibernateProperties");
        assert (hibernateProperties != null);
        Boolean setupConfirmed = (Boolean) servlet.getServletContext()
            .getAttribute(HibernateSetupConstants.CONFIRM_ATTRIBUTE);
        String uRL = hibernateProperties.getProperty(
                HibernateSetupConstants
                .HIBERNATE_PROPERTY_DATABASE_URL);
        assert (uRL != null);
        if (HibernateSetupConstants.AUTOMATIC_DATABASE_MEMORY_URL
                    .equals(uRL)
                && !Boolean.TRUE.equals(setupConfirmed)) {
            return "setupAction";
        }

        try {
            forward = (String) PropertyUtils.getSimpleProperty(form, "forward");
            help = (String) PropertyUtils.getSimpleProperty(form, "help");
            javaScriptVersion = (String) PropertyUtils.getSimpleProperty(form, "javaScriptVersion");
            jndiPrefix = (String) PropertyUtils.getSimpleProperty(form, "jndiPrefix");
            login = (String) PropertyUtils.getSimpleProperty(form, "login");
            password = (String) PropertyUtils.getSimpleProperty(form, "password");
            userName = ((String) PropertyUtils.getSimpleProperty(form, "userName")).toLowerCase();
        } catch (InvocationTargetException e) {
            throw new SystemException(e);
        } catch (IllegalAccessException e) {
            throw new SystemException(e);
        } catch (NoSuchMethodException e) {
            // if any of the methods are missing, it must be the wrong form...
            form = RequestUtils.createActionForm(request, mapping, mapping.getModuleConfig(), servlet);
        }
        // we can't do anything if we don't even have guest access yet
        if (securitySession == null) {
            return "loginGuestAction";
        }

        String defaultForward = "";
        // try to find from setting defaultForwaard
        UserDO user = null;
        if (!StringHandling.isNullOrEmpty(userName)) {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Looking for entity for user name '" + userName + "'");
                }
                user = security.findUserByName(securitySession, userName);
                defaultForward = settings.getStringSetting(securitySession,
                    "siteDefaultForward",
                    user);
            } catch (SettingsDataTypeException e) {
                throw new SystemException(e);
            } catch (SettingNullException e) {
                throw new SystemException(e);
            } catch (FinderException e) {
                log.warn("No user found called '" + userName + "'", e);
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("login.error.login"));
                // set login null to login as guest
                login = null;
            }
        }
        // choose the theme
        String siteTheme = request.getParameter("theme");
        // go back to the last used?
        boolean changeTheme = true;
        if ("last".equals(siteTheme)) {
            changeTheme = false;
            siteTheme = settings.getStringSetting(securitySession,
                    "siteTheme", user);
        }
        session.setAttribute(ThemeConstants.ATTRIBUTE_THEME_NAME, siteTheme);
        // if the user just changed the theme, use the one specified and go back
        // again
        if(StringHandling.isNullOrEmpty(login)) {
            return "login";
        }

        // forward was not sent from the form
        if (StringHandling.isNullOrEmpty(forward)) {
            forward = defaultForward;
        }

        try {
            PropertyUtils.setSimpleProperty(form, "forward", forward);
        } catch (NoSuchMethodException e) {
            // send the user back to the login page - this form is no use!
            form = RequestUtils.createActionForm(request, mapping, mapping.getModuleConfig(), servlet);
            try {
                PropertyUtils.setSimpleProperty(form, "forward", forward);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            }
            return null;
        } catch (InvocationTargetException e) {
            throw new SystemException(e);
        } catch (IllegalAccessException e) {
            throw new SystemException(e);
        }
        Browser browser = null;

        // initialize the browser object
        if ((javaScriptVersion != null) &&
            javaScriptVersion.trim().equals("")) {
            javaScriptVersion = null;
        }
        browser = new Browser(request.getHeader("User-Agent"), javaScriptVersion);
        session.setAttribute("browser", browser);
        // this is where we actually try to login
        try {
            if (log.isInfoEnabled()) {
                log.info("Logging in user '" + userName + "' for real (not guest).");
            }
            session.setAttribute("securitySession", securitySession =
                security.login(user, password));
        } catch (Exception e) {
            // show the password only if the setting allows us to
            String logPassword = "**********";
            try {
                securitySession = security.loginGuest();
                Boolean debugPassword = settings.getBooleanSetting(
                        securitySession,
                        "siteLoginDebugPassword", null);
                if ((debugPassword != null)
                        && debugPassword.booleanValue()) {
                    logPassword = password;
                }
            } catch (Exception e1) {
                // nothing we can do - just log the error
                log.error("Failed to access setting 'siteLoginDebugPassword'.",
                        e1);
            }
            log.warn ("Failed to login user '"
                    + userName
                    + "', password '"
                    + logPassword
                    + "'", e);
            errors.add(ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage("login.error.login"));
        }
        // if there is no javascript on the browser, handle that
        // TODO: - javascript could be made optional with just a warning by
        //         placing the if(!errors.empty()) { section above this comment
        if (javaScriptVersion == null) {
            if (log.isInfoEnabled()) {
                log.info("Javascript is unavailable on user's browser.");
            }
            // if you don't have javascript, we show a special login designed
            // to help you activate (and check it is working)
            errorForward = "loginJavaScript";
            Integer browserType = browser.getType();
            String errorKey;

            if (BrowserConstants.TYPE_INTERNET_EXPLORER.equals(browserType)) {
                if (browser.getVersion().compareTo("5") >= 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("Identified IE5 browser.");
                    }
                    // they changed the place for javascript preferences. Grrr.
                    errorKey = "login.error.javascript.iexplore5";
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("Identified IE browser.");
                    }
                    errorKey = "login.error.javascript.iexplore";
                }
            } else if (BrowserConstants.TYPE_MOZILLA.equals(browserType) ||
                BrowserConstants.TYPE_NETSCAPE.equals(browserType)) {
                if (log.isDebugEnabled()) {
                    log.debug("Identified Netscape/Mozilla browser.");
                }
                errorKey = "login.error.javascript.netscape";
            } else if (BrowserConstants.TYPE_OPERA.equals(browserType)) {
                if (browser.getVersion().compareTo("6") >= 0) {
                    // they changed the place for javascript preferences. Grrr.
                    errorKey = "login.error.javascript.opera6";
                    if (log.isDebugEnabled()) {
                        log.debug("Identified Opera 6 browser.");
                    }
                } else {
                    errorKey = "login.error.javascript.opera";
                    if (log.isDebugEnabled()) {
                        log.debug("Identified Opera browser.");
                    }
                }
            } else {
                errorKey = "login.error.javascript.unknown";
                if (log.isDebugEnabled()) {
                    log.debug("Could not identify browser.");
                }
            }
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(errorKey));
        }
        // if there are errors, you can't get in!
        if (!errors.isEmpty()) {
            if (log.isInfoEnabled()) {
                log.info("Errors found for user '" + userName + "'.");
            }
            return errorForward;
        }
        if (log.isInfoEnabled()) {
            log.info("Login successful for user '" + userName + "'.");
        }
        // if we got here, that means we now have a logged in user
        session.setAttribute("userName", userName);
        request.setAttribute("loginForward", forward);

        // if the user changed the theme, save it in the settings for the next
        // time
        if (changeTheme) {
            settings.amendSetting(securitySession, "siteTheme", siteTheme,
                    user);
        }
        return forward;
    }
}
