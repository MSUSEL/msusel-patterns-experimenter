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
package com.ivata.groupware.business.mail.struts;

import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.ivata.groupware.admin.security.Security;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.groupware.business.addressbook.AddressBook;
import com.ivata.groupware.business.addressbook.struts.PersonForm;
import com.ivata.groupware.business.mail.Mail;
import com.ivata.groupware.util.SettingDateFormatter;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.CollectionHandling;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;

/**
 * <p>
 * Extends the standard ivata groupware person form to add functionality for a mail
 * user, such as user aliases.
 * </p>
 *
 * @since Nov 2, 2004
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 */

public class MailUserForm extends PersonForm {

    /**
     * <p>
     * Stores the email address host to append to user aliases to make new
     * email addresses. This is read from the settings.
     * </p>
     */
    String emailAddressHost = "localhost";

    /**
     * <p>
     * System mail implemenation. Used to add email addresses based on aliases.
     * </p>
     */
    private Mail mail;
    /**
     * <p>
     * Stores the security session of the current user.
     * </p>
     */
    private SecuritySession securitySession;
    /**
     * <p>
     * Settings implementation. Used to retrieve the email address host.
     * </p>
     */
    private Settings settings;
    /**
     * <p>Contains 'local parts' (i.e. 'johnny.tester', not
     * 'johnny.tester@acme.com') of the email addresses which will be sent
     * to this user.</p>
     */
    private List userAliases;
    /**
     * <p>If non-<code>null</code>, indicates a message which should be
     * sent to users who send this user an email, to indicate (s)he is on
     * holiday or otherwise unavailable.</p>
     */
    private String vacationMessage;

    /**
     * <p>
     * Constructor - necessary to initialize <code>PersonForm</code>. Called
     * by <code>PicoContainer</code>.
     * </p>
     *
     * @param addressBook address book implementation.
     * @param settings settings implementation. Used to retrieve the email
     *   address host.
     * @param dateFormatter date formatter for the current session.
     */
    public MailUserForm(final Mail mail,
            final AddressBook addressBook,
            final Settings settings,
            final SettingDateFormatter dateFormatter,
            final Security security,
            final MaskFactory maskFactory) {
        super(addressBook, dateFormatter, security, maskFactory);
        this.mail = mail;
        this.settings = settings;
    }
    /**
     * <p>
     * Return all form state to initial values.
     * </p>
     *
     * @see com.ivata.mask.web.struts.MaskForm#clear()
     */
    protected void clear() {
        super.clear();
        userAliases = null;
        vacationMessage = null;
    }

    /**
     * <p>Contains 'local parts' (i.e. 'johnny.tester', not
     * 'johnny.tester@acme.com') of the email addresses which will be sent
     * to this user.</p>
     *
     * @return the current value of userAliases.
     */
    public List getUserAliases() {
        return userAliases;
    }

    /**
     * <p>Contains 'local parts' (i.e. 'johnny.tester', not
     * 'johnny.tester@acme.com') of the email addresses which will be sent
     * to this user. This version of the getter reutrns the local parts
     * separated by new lines.</p>
     *
     * @return current value of userAliases.
     */
    public final String getUserAliasesAsLines() {
        return CollectionHandling.convertToLines(userAliases);
    }

    /**
     * <p>If non-<code>null</code>, indicates a message which should be
     * sent to users who send this user an email, to indicate (s)he is on
     * holiday or otherwise unavailable.</p>
     *
     * @return the current value of vacationMessage.
     */
    public final String getVacationMessage() {
        return vacationMessage;
    }
    /**
     * <p>Reset all bean properties to their default state.  This method
     * is called before the properties are repopulated by the controller
     * servlet.</p>
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void reset(final ActionMapping mapping,
            final HttpServletRequest request) {
        super.reset(mapping, request);
        securitySession = (SecuritySession) request.getSession().getAttribute("securitySesssion");
        if (!StringHandling.isNullOrEmpty(getUserName())) {
            // if the user has not been updated yet, use the site default
            // for the email host
            UserDO user = getPerson().getUser().getId() == null ?
                    null : getPerson().getUser();
            try {
                emailAddressHost =
                    settings.getStringSetting(
                        securitySession,
                        "emailAddressHost",
                        user);
            } catch (SystemException e) {
                throw new RuntimeException(e);
            }
        }

        // we're only interested in extending functionality for the user tab
        if ("/addressBook/user.jsp".equals(getTabPage())) {
            userAliases = null;
            vacationMessage = "";
        }
    }
    /**
     * <p>
     * Update the person to show all the addresses currently in the form. This
     * method has been overridden to add a default email address for users.
     * </p>
     *
     * @see com.ivata.groupware.business.addressbook.struts.PersonForm#setPersonFromTelecomAddresses()
     */
    public void setPersonFromTelecomAddresses() {
        try {
            String userName = getUserName();
            if (!StringHandling.isNullOrEmpty(userName)) {
                mail.addUserAliasEmailAddresses(securitySession, userName,
                        userAliases, getTelecomAddresses(), emailAddressHost);
            }
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
        super.setPersonFromTelecomAddresses();
    }

    /**
     * <p>Contains 'local parts' (i.e. 'johnny.tester', not
     * 'johnny.tester@acme.com') of the email addresses which will be sent
     * to this user.</p>
     *
     * @param userAliases the new value of userAliases.
     */
    public final void setUserAliases(final List userAliases) {
        this.userAliases = userAliases;
    }

    /**
     * <p>Contains 'local parts' (i.e. 'johnny.tester', not
     * 'johnny.tester@acme.com') of the email addresses which will be sent
     * to this user. This version of the setter contains the local parts
     * separated by new lines.</p>
     *
     * @param userAliases the new value of userAliases.
     */
    public final void setUserAliasesAsLines(final String userAliases) {
        this.userAliases = new Vector(CollectionHandling.convertFromLines(userAliases));
    }

    /**
     * <p>If non-<code>null</code>, indicates a message which should be
     * sent to users who send this user an email, to indicate (s)he is on
     * holiday or otherwise unavailable.</p>
     *
     * @param vacationMessage the new value of vacationMessage.
     */
    public final void setVacationMessage(final String vacationMessage) {
        this.vacationMessage = vacationMessage;
    }
}
