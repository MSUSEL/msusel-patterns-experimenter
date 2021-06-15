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
// Source file: h:/cvslocal/ivata groupware/src/com/ivata/intranet/business/addressbook/struts/FindPersonAction.java

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
 * $Log: FindPersonAction.java,v $
 * Revision 1.3  2005/04/10 20:32:02  colinmacleod
 * Added new themes.
 * Changed id type to String.
 * Changed i tag to em and b tag to strong.
 * Improved PicoContainerFactory with NanoContainer scripts.
 *
 * Revision 1.2  2005/04/09 17:19:08  colinmacleod
 * Changed copyright text to GPL v2 explicitly.
 *
 * Revision 1.1.1.1  2005/03/10 17:50:25  colinmacleod
 * Restructured ivata op around Hibernate/PicoContainer.
 * Renamed ivata groupware.
 *
 * Revision 1.9  2004/12/31 18:27:42  colinmacleod
 * Added MaskFactory to constructor of MaskAction.
 *
 * Revision 1.8  2004/12/23 21:01:25  colinmacleod
 * Updated Struts to v1.2.4.
 * Changed base classes to use ivata masks.
 *
 * Revision 1.7  2004/11/12 18:19:13  colinmacleod
 * Change action and form classes to extend MaskAction, MaskForm respectively.
 *
 * Revision 1.6  2004/11/12 15:57:06  colinmacleod
 * Removed dependencies on SSLEXT.
 * Moved Persistence classes to ivata masks.
 *
 * Revision 1.5  2004/11/03 15:31:50  colinmacleod
 * Change method interfaces to remove log.
 *
 * Revision 1.4  2004/07/13 19:41:14  colinmacleod
 * Moved project to POJOs from EJBs.
 * Applied PicoContainer to services layer (replacing session EJBs).
 * Applied Hibernate to persistence layer (replacing entity EJBs).
 *
 * Revision 1.3  2004/03/21 21:16:08  colinmacleod
 * Shortened name to ivata op.
 *
 * Revision 1.2  2004/02/01 22:00:33  colinmacleod
 * Added full names to author tags
 *
 * Revision 1.1.1.1  2004/01/27 20:57:53  colinmacleod
 * Moved ivata openportal to SourceForge..
 *
 * Revision 1.4  2003/11/13 16:03:16  jano
 * commitng everything to CVS
 * can deploy and application is ruuning, can login into
 *
 * Revision 1.3  2003/10/17 12:36:12  jano
 * fixing problems with building
 * converting intranet -> portal
 * Eclipse building
 *
 * Revision 1.2  2003/10/15 13:18:02  colin
 * fixing for XDoclet
 *
 * Revision 1.5  2003/08/21 09:49:32  jano
 * fixing for new addressBook extension
 *
 * Revision 1.4  2003/08/20 16:24:15  jano
 * fixing addressBook extension
 *
 * Revision 1.3  2003/04/14 12:21:12  peter
 * helpKey initialisation added
 *
 * Revision 1.2  2003/02/28 09:36:38  jano
 * RuntimeException(e) -> IntrnetRuntimeException
 *
 * Revision 1.1  2003/02/24 19:09:21  colin
 * moved to business
 *
 * Revision 1.4  2003/02/21 16:17:03  peter
 * added initialisation of the birthDate of personForm
 *
 * Revision 1.3  2003/02/18 11:10:27  colin
 * first release of address book with Struts
 *
 * Revision 1.2  2003/02/14 08:59:48  colin
 * changed findParentGroups... to findGroups...
 *
 * Revision 1.1  2003/02/04 17:40:18  colin
 * first version in CVS
 *
 * Revision 1.6  2003/01/30 09:02:19  colin
 * updates for struts conversion
 *
 * Revision 1.5  2003/01/18 20:12:36  colin
 * fixes and changes to override new MaskAction method
 *
 * Revision 1.4  2003/01/10 10:29:46  jano
 * we need information about user who created group
 *
 * Revision 1.3  2003/01/09 10:50:54  jano
 * I need only one method for finding right for group
 *
 * Revision 1.2  2003/01/08 17:16:21  jano
 * We will use new methods for finding and changing rights for
 * GROUP of AddressBookRightsBean
 * -----------------------------------------------------------------------------
 */
package com.ivata.groupware.business.addressbook.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.Security;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.groupware.admin.setting.SettingsInitializationException;
import com.ivata.groupware.business.addressbook.AddressBook;
import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.business.addressbook.person.employee.EmployeeDO;
import com.ivata.groupware.business.addressbook.right.AddressBookRights;
import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.groupware.util.SettingDateFormatter;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.format.DateFormatterConstants;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;


/**
 * <p><code>FindPersonAction</code> invoked from the index page. This
 * action locates the person and prepares the form for
 * <code>person.jsp</code>.</p>
 *
 * @since 2003-02-01
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 *
 * @TODO dependency AddressbookAction on WebMail
 */
public class FindPersonAction extends MaskAction {
    /**
     * Address book implementation.
     */
    private AddressBook addressBook;
    /**
     * Address book rights implementation.
     */
    private AddressBookRights addressBookRights;
    /**
     * Used to, well, format dates!
     */
    private SettingDateFormatter dateFormatter;
    /**
     * Settings implemntation.
     */
    private Settings settings;
    private Security security;

    /**
     * Construct the action.
     *
     * @param addressBook address book implementation.
     * @param addressBookRights address book rights implementation.
     * @param settings settings implemntation.
     * @param maskFactory This factory is needed to access the masks and groups
     * of masks.
     * @param authenticator used to confirm whether or not the
     * user should be allowed to continue, in the <code>execute</code> method.
     */
    public FindPersonAction(AddressBook addressBook, AddressBookRights
            addressBookRights, SettingDateFormatter dateFormatter,
            Security security, Settings settings,
            MaskFactory maskFactory, MaskAuthenticator authenticator) {
        super(maskFactory, authenticator);
        this.addressBook = addressBook;
        this.addressBookRights = addressBookRights;
        this.dateFormatter = dateFormatter;
        this.security = security;
        this.settings = settings;
    }

    /**
     * <p>Invoked when the user clicks on a person in the address
     * list.</p>
     *
     * @param mapping current action mapping from <em>Struts</em> config.
     * @param log valid logging object to write messages to.
     * @param errors valid errors object to append errors to. If there are
     * any errors, the action will return to the input.
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
        // find the person and set him/her in the form
        session.removeAttribute("personTab_activeTab");
        String id = request.getParameter("id");

        if (id == null) {
            throw new SystemException("ERROR in FindPersonAction: id is null");
        }
        SecuritySession securitySession = (SecuritySession) session.getAttribute("securitySession");
        PersonDO person = addressBook.findPersonByPrimaryKey(securitySession, id);
        PicoContainer container = securitySession.getContainer();
        PersonForm personForm = (PersonForm)
            PicoContainerFactory.getInstance().instantiateOrOverride(container,
                    PersonForm.class);
        personForm.clear();
        personForm.setPerson(person);

        // set Up READ ONLY flag
        personForm.setReadOnly(!addressBookRights.canAmendInGroup(securitySession,
            person.getGroup().getAddressBook()));
        String userName = securitySession.getUser().getName();
        if (personForm.getReadOnly() && person.getCreatedBy().equals(securitySession.getUser())) {
            personForm.setReadOnly(false);
        }
        // set Up CAN REMOVE flag
        personForm.setCanRemove(addressBookRights.canRemoveFromGroup(securitySession,
            person.getGroup().getAddressBook()));

        // date of birth
        java.util.Date birthDate = person.getDateOfBirth();
        if (birthDate != null) {
            try {
                dateFormatter.setUserName(userName);
                dateFormatter.setDateFormat(DateFormatterConstants.DATE_INPUT_DISPLAY);
                dateFormatter.setDateTimeText("{0}");

                personForm.setDateOfBirthString(dateFormatter.format(birthDate));
            } catch (SettingsInitializationException e) {
                throw new RuntimeException(e);
            }
        }

        session.setAttribute("addressBookPersonForm", personForm);

        // set up the employee
        personForm.setEmployee(person.getEmployee() != null);
        // if the person is not yet an employee, make a new employee record
        // (we'll remove this at the end unless the 'isEmployee' is set)
        if (!personForm.isEmployee()) {
            person.setEmployee(new EmployeeDO());
        }
        // set up the user
        UserDO userSet = person.getUser();

        personForm.setUserName(userSet == null ? null : userSet.getName());
        personForm.setEnableUser((userSet != null)
                && security.isUserEnabled(securitySession, userSet.getName()));
        // only get the email aliases and vacation message if the user name is
        // set to something
//            if (!StringHandling.isNullOrEmpty(userNameSet)) {
//                MailRemote mail = getMail();

//                personForm.setUserAliases(new Vector(mail.getUserAliases(userNameSet)));
//                personForm.setVacationMessage(mail.getVacationMessage(userNameSet));
//            }
        // TODO: check for user rights here
        personForm.setTitleKey("person.title.amend");
        // TODO: personForm.setHelpKey("addressbook.person");
        return "addressBookPersonAction";
    }
}
