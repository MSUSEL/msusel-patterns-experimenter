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

import com.ivata.groupware.admin.security.Security;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.groupware.business.addressbook.AddressBook;
import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.business.addressbook.right.AddressBookRights;
import com.ivata.groupware.business.addressbook.struts.FindPersonAction;
import com.ivata.groupware.business.mail.Mail;
import com.ivata.groupware.business.mail.session.MailSession;
import com.ivata.groupware.util.SettingDateFormatter;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.struts.MaskAuthenticator;


/**
 * <p>Extends the find person action from the standard address book class to add
 * in user alias and other mail user settings.</p>
 *
 * <p>To use this class rather than the standard AddressBook class, you need to
 * change the struts config file to specify this class instead of
 * <code>FindPersonAction</code>.</p>
 *
 * TODO: this class needs to be substituted for the FindPersonAction in the
 *     struts file.
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 */
public class FindMailUserAction extends FindPersonAction {
    AddressBook addressBook;
    Mail mail;
    Security security;
    /**
     * TODO
     * @param addressBook
     * @param addressBookRights
     * @param mail
     * @param dateFormatter
     * @param security
     * @param settings
     * @param maskFactory This factory is needed to access the masks and groups
     * of masks.
     * @param authenticator used to confirm whether or not the
     * user should be allowed to continue, in the <code>execute</code> method.
     */
    public FindMailUserAction(AddressBook addressBook, AddressBookRights
            addressBookRights, Mail mail, SettingDateFormatter dateFormatter,
            Security security, Settings settings,
            MaskFactory maskFactory, MaskAuthenticator authenticator) {
        super(addressBook, addressBookRights, dateFormatter, security, settings,
                maskFactory, authenticator);
        this.addressBook = addressBook;
        this.mail = mail;
        this.security = security;
    }

    /**
     * <p>Overridden to set the vacation message and user aliases.</p>
     *
     * @see com.ivata.mask.web.struts.MaskAction#execute
     */
    public String execute(final ActionMapping mapping,
            final ActionErrors errors,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session) throws SystemException {
        String returnValue = super.execute(mapping, errors, form, request,
                response, session);

        MailUserForm mailUserForm = (MailUserForm) session.getAttribute(
                "addressBookPersonForm");

        // set up the user
        String id = request.getParameter("id");

        if (id == null) {
            throw new SystemException("ERROR in FindPersonAction: id is null",
                null);
        }

        PersonDO person;
        MailSession mailSession = (MailSession) session.getAttribute("securitySession");
        person = addressBook.findPersonByPrimaryKey(mailSession, id);

        String userNameSet = person.getUser().getName();

        // only get the email aliases and vacation message if the user name is
        // set to something
        if (!StringHandling.isNullOrEmpty(userNameSet)) {
            mailUserForm.setUserAliases(new Vector(mail.getUserAliases(
                mailSession, userNameSet)));
            mailUserForm.setVacationMessage(mail.getVacationMessage(
                mailSession, userNameSet));
        }

        mailUserForm.setUserName(userNameSet);
        mailUserForm.setEnableUser((userNameSet != null) &&
            security.isUserEnabled(mailSession, userNameSet));

        return returnValue;
    }
}
