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

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ivata.groupware.admin.security.addressbook.AddressBookSecurity;
import com.ivata.groupware.admin.security.right.SecurityRights;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.groupware.business.addressbook.AddressBook;
import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.business.addressbook.right.AddressBookRights;
import com.ivata.groupware.business.addressbook.struts.PersonAction;
import com.ivata.groupware.business.mail.Mail;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.struts.MaskAuthenticator;


/**
 * <p>Extends the person action from the standard address book class to add
 * in mail user settings.</p>
 *
 * <p>To use this class rather than the standard AddressBook class, you need to
 * change the struts config file to specify this class instead of
 * <code>PersonAction</code>.</p>
 *
 * TODO: this class needs to be substituted for the PersonAction in the
 *     struts file.
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 */
public class MailUserAction extends PersonAction {
    Mail mail;
    /**
     * TODO
     * @param mail
     * @param addressBook
     * @param addressBookRights
     * @param security
     * @param securityRights
     * @param settings
     * @param maskFactory This factory is needed to access the masks and groups
     * of masks.
     * @param authenticator used to confirm whether or not the
     * user should be allowed to continue, in the <code>execute</code> method.
     */
    public MailUserAction(Mail mail, AddressBook addressBook,
            AddressBookRights addressBookRights,
            AddressBookSecurity security, SecurityRights securityRights,
            Settings settings,
            MaskFactory maskFactory,
            MaskAuthenticator authenticator) {
        super(addressBook, addressBookRights, security, securityRights, settings,
                maskFactory, authenticator);
        this.mail = mail;
    }

    /* (non-Javadoc)
     * @see com.ivata.groupware.business.struts.MaskAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionMessages, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.http.HttpSession)
     */
    public String execute(final ActionMapping mapping,
            final ActionErrors errors,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session)
            throws SystemException {
        MailUserForm mailUserForm = (MailUserForm) form;
        if (mailUserForm.getUserAliases() == null) {
            UserDO user = null;
            if (mailUserForm.getPerson() != null) {
                user = mailUserForm.getPerson().getUser();
            }
            // if there is no user, just create an empty user aliases list
            if ((user == null)
                    || StringHandling.isNullOrEmpty(user.getName())) {
                mailUserForm.setUserAliases(new Vector());
            }
            // otherwise get the user aliases for this user
            else {
                SecuritySession securitySession = (SecuritySession) session.getAttribute("securitySession");
                mailUserForm.setUserAliases(new Vector(mail.getUserAliases(
                        securitySession, user.getName())));
            }
        }

        return super.execute(mapping, errors, form, request, response, session);
    }
    public String onConfirm(final ActionMapping mapping,
            final ActionErrors errors,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session,
            final String defaultForward) throws SystemException {
        // first process the default implementation of person action
        String returnValue = super.onConfirm(mapping, errors, form, request, response,
                session, defaultForward);
        MailUserForm mailUserForm = (MailUserForm) form;
        SecuritySession securitySession = (SecuritySession) session.getAttribute("securitySession");
        PersonDO person = mailUserForm.getPerson();
        UserDO user = person.getUser();

        if ((user != null)
                && !StringHandling.isNullOrEmpty(user.getName())) {
            // TODO: - vacation message not currently implemented
            // mail.setVacationMessage(personForm.getUserName(), personForm.getVacationMessage());
            // NOTE: This MUST come after updating the user
            mail.setUserAliases(securitySession, user.getName(),
                mailUserForm.getUserAliases());
        }

        return returnValue;
    }
}
