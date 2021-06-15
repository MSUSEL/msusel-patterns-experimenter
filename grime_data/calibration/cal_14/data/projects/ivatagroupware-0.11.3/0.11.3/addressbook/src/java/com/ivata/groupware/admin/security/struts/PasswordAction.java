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
package com.ivata.groupware.admin.security.struts;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.ivata.groupware.admin.security.Security;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.addressbook.struts.PersonForm;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.util.ThrowableHandling;
import com.ivata.mask.validation.ValidationException;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;
import com.ivata.mask.web.struts.ValidationErrorsConvertor;


/**
 * <p>Invoked when the user changes a password for
 * him/herself or any other user.</p>
 *
 * @since 2003-01-26
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 */
public class PasswordAction extends MaskAction {
    /**
     * Refer to {@link Logger}.
     */
    private static Logger log = Logger.getLogger(PasswordAction.class);
    /**
     * Refer to {@link Security}.
     */
    private Security security;

    /**
     * <p>
     * Constructor - invoked by <strong>PicoContainer</strong>.
     * </p>
     *
     * @param security valid security implementation.
     * @param maskFactory This factory is needed to access the masks and groups
     * of masks.
     * @param authenticator used to confirm whether or not the
     * user should be allowed to continue, in the <code>execute</code> method.
     */
    public PasswordAction(Security security, MaskFactory maskFactory,
            MaskAuthenticator authenticator) {
        super(maskFactory, authenticator);
        this.security = security;
    }

    /**
     * <p>Overridden from the default intranet implementation to
     * initialize the dialog.</p>
     *
     * @param mapping current action mapping from <em>Struts</em> config.
     * @param log valid logging object to write messages to.
     * @param errors valid errors object to append errors to. If there
     * are any errors, the action will return to the input.
     * @param form optional ActionForm bean for this request (if any)
     * @param request non-HTTP request we are processing
     * @param response The non-HTTP response we are creating
     * @param session  returned from the <code>request</code> parameter.
     * @param userName current user name from session. .
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
            final HttpSession session) throws SystemException {
        SecuritySession securitySession = (SecuritySession)
                session.getAttribute("securitySession");
        // by default, we don't want to just close the window!
        boolean justClose = false;
        // first look out for null or empty people or user names!!
        PersonForm personForm = (PersonForm)
                session.getAttribute("addressBookPersonForm");
        if ((personForm == null)
                || StringHandling.isNullOrEmpty(personForm.getUserName())) {
            errors.add(Globals.ERROR_KEY,
                    new ActionMessage(
                            "errors.addressBook.password.noUserName"));
            justClose =  true;
        } else if (!personForm.getUserName().equals(
                personForm.getPerson().getUser().getName())) {
            // you can't change the password for a new user until that user is
            // added first
            errors.add(Globals.ERROR_KEY,
                    new ActionMessage(
                            "errors.addressBook.password.userNameApply"));
            justClose = true;
        } else if (!security.isUserEnabled(securitySession,
                personForm.getPerson().getUser().getName())) {
            // if user is disabled
            errors.add(Globals.ERROR_KEY,
                    new ActionMessage(
                            "errors.addressBook.password.userIsDisable"));
            justClose = true;
        }


        // save justClose in the form
        try {
            PropertyUtils.setSimpleProperty(form, "justClose",
                    new Boolean(justClose));
        } catch (NoSuchMethodException e) {
            throw new SystemException(e);
        } catch (InvocationTargetException e) {
            throw new SystemException(e);
        } catch (IllegalAccessException e) {
            throw new SystemException(e);
        }
        return null;
    }

    /**
     * <p>This method is called if the ok or apply buttons are pressed.</p>
     *
     * @param mapping current action mapping from <em>Struts</em> config.
     * @param log valid logging object to write messages to.
     * @param errors valid errors object to append errors to. If there are
     * any errors, the action will return to the input.
     * @param form optional ActionForm bean for this request (if any)
     * @param request non-HTTP request we are processing
     * @param response The non-HTTP response we are creating
     * @param session  returned from the <code>request</code> parameter.
     * @param userName valid, non-null user name from session.
     * @param settings valid, non-null settings from session.
     * @param defaultForward &quot;ok&quot; if the <code>Ok</code> button
     * was pressed, otherwise &quot;apply&quot; if the <code>Apply</code> button
     * was pressed.
     * @exception SystemException if there is any problem which
     * prevents processing. It will result in the webapp being forwarded
     * to
     * the standard error page.
     * @return this method returns the string used to identify the correct
     * <em>Struts</em> <code>ActionForward</code> which should follow this
     * page, or <code>null</code> if it should return to the input.
     */
    public String onConfirm(final ActionMapping mapping,
            final ActionErrors errors,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session,
            final String defaultForward) throws SystemException {
        String oldPassword, newPassword, confirmPassword;
        boolean justClose = false;

        try {
            oldPassword = (String) PropertyUtils.getSimpleProperty(form,
                    "oldPassword");
            newPassword = (String) PropertyUtils.getSimpleProperty(form,
                    "newPassword");
            confirmPassword = (String) PropertyUtils.getSimpleProperty(form,
                    "confirmPassword");
        } catch (NoSuchMethodException e) {
            throw new SystemException(e);
        } catch (InvocationTargetException e) {
            throw new SystemException(e);
        } catch (IllegalAccessException e) {
            throw new SystemException(e);
        }
        // check none of the passwords is null or empty
        if (StringHandling.isNullOrEmpty(oldPassword)
                || StringHandling.isNullOrEmpty(newPassword)
                || StringHandling.isNullOrEmpty(confirmPassword)) {
            errors.add(Globals.ERROR_KEY,
                    new ActionMessage(
                            "errors.addressBook.password.notComplete"));
        } else if (!newPassword.equals(confirmPassword)) {
            errors.add(Globals.ERROR_KEY,
                    new ActionMessage(
                            "errors.addressBook.password.notMatching"));
        } else if (errors.isEmpty()) {
            PersonForm personForm
                = (PersonForm) session.getAttribute("addressBookPersonForm");

            // check the old password matches this user
            SecuritySession securitySession
                = (SecuritySession) session.getAttribute("securitySession");
            try {
                security.checkPassword(securitySession, oldPassword);
            } catch (SystemException e) {
                log.warn("security.checkPassword threw an exception.", e);

                if (personForm.getUserName().equals(
                        securitySession.getUser().getName())) {
                    errors.add(
                            ActionMessages.GLOBAL_MESSAGE,
                            new ActionMessage("password.error.badOldPassword"));
                } else {
                    errors.add(
                            ActionMessages.GLOBAL_MESSAGE,
                            new ActionMessage("password.error."
                                    + "badYoursPassword"));
                }
            }
            if (errors.isEmpty()) {
                // now change the password
                if ((personForm == null)
                        || StringHandling.isNullOrEmpty(
                                personForm.getUserName())) {
                    errors.add(Globals.ERROR_KEY,
                            new ActionMessage(
                                    "errors.addressBook.password.noUserName"));
                } else {
                    try {
                        security.setPassword(securitySession,
                                personForm.getUserName(), newPassword);
                        // if it is successful, just close the dialog with a
                        // happy message
                        justClose = true;
                        // confirm this back to the user - ok, I've cheated
                        // - this is not _really_ an error
                        errors.add(Globals.ERROR_KEY,
                                new ActionMessage(
                                        "errors.addressBook.password."
                                        + "passwordUpdated"));
                    } catch (Exception e) {
                        Throwable cause = ThrowableHandling.getCause(e);
                        // if this is caused by a validation exception, try to
                        // get the real cause of the error out (if it is
                        // password-related)
                        if (cause instanceof ValidationException) {
                            ValidationException validationException =
                                (ValidationException) cause;
                            Locale locale =
                                (Locale) session.getAttribute(
                                        Globals.LOCALE_KEY);
                            ActionMessages allErrors = ValidationErrorsConvertor
                                .toActionErrors(validationException.getErrors(),
                                        locale);
                            Iterator allErrorsIterator = allErrors.get();
                            while (allErrorsIterator.hasNext()) {
                                ActionMessage actionError = (ActionMessage)
                                    allErrorsIterator.next();
                                String key = actionError.getKey();
                                if (key.startsWith("password.error")) {
                                    log.debug("Adding action error: "
                                            + actionError.getKey());
                                    errors.add(ActionMessages.GLOBAL_MESSAGE,
                                            actionError);
                                } else if ("errors.admin.script".equals(key)) {
                                    log.debug("Adding script error: "
                                            + actionError.getKey());
                                    errors.add(ActionMessages.GLOBAL_MESSAGE,
                                            actionError);
                                } else {
                                    log.error(
                                            "Invalid password action error "
                                            + "key: "
                                            + key);
                                    errors.add(ActionMessages.GLOBAL_MESSAGE,
                                            new ActionMessage(
                                                    "password.error."
                                                    + "passwordCouldNotBe"
                                                    + "Changed"));
                                }
                            }
                        } else {
                            log.warn("security.setPassword threw an exception.",
                                    e);
                            errors.add(ActionMessages.GLOBAL_MESSAGE,
                                    new ActionMessage("password.error."
                                            + "passwordCouldNotBeChanged"));
                        }
                    }
                }
            }
        }
        // save justClose in the form
        try {
            PropertyUtils.setSimpleProperty(form, "justClose",
                    new Boolean(justClose));
        } catch (NoSuchMethodException e) {
            throw new SystemException(e);
        } catch (InvocationTargetException e) {
            throw new SystemException(e);
        } catch (IllegalAccessException e) {
            throw new SystemException(e);
        }
        return "addressBookPassword";
    }
}