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
package com.ivata.groupware.business.addressbook.struts;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;

import com.ivata.groupware.admin.security.Security;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.admin.setting.SettingsInitializationException;
import com.ivata.groupware.business.addressbook.AddressBook;
import com.ivata.groupware.business.addressbook.address.AddressDO;
import com.ivata.groupware.business.addressbook.address.country.CountryDO;
import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.business.addressbook.person.employee.EmployeeDO;
import com.ivata.groupware.business.addressbook.person.group.GroupDO;
import com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressConstants;
import com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressDO;
import com.ivata.groupware.util.SettingDateFormatter;
import com.ivata.mask.Mask;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.validation.ValidationError;
import com.ivata.mask.validation.ValidationErrors;
import com.ivata.mask.web.format.DateFormatterConstants;
import com.ivata.mask.web.struts.DialogForm;


/**
 * <p>This form is wrapper for <code>PersonDO</code>. It is used in
 * <code>person.jsp</code> and the tabs from that page</p>
 *
 * @since 2003-01-31
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 */
public class PersonForm extends DialogForm {
    /**
     * <p>This log provides tracing and debugging information.</p>
     */
    private static Logger log = Logger.getLogger(PersonForm.class);

    /**
     * <p>
     * Implementation of the address book services layer.
     * </p>
     */
    private AddressBook addressBook;

    /**
     * <p>
     * Unique identifier of the address book group. This dictates which groups
     * to choose from.
     * </p>
     */
    private Integer addressBookGroupId;

    /**
     * <p>
     * Name of the address book group. This dictates which groups
     * to choose from.
     * </p>
     */
    private String addressBookGroupName;

    /**
     * <p>
     * Map of all address book names available. This is used to construct a combo
     * list for the user to choose from.
     * </p>
     */
    private Map addressBookGroupNames;

    /**
     * <p>
     * Defines the base class of all objects in the value object list.
     * </p>
     */
    private Class baseClass;

    /**
     *<p>in case that userName can remove this contact it's true</p>
     */
    private boolean canRemove;
    /**
     * <p>if user chenged addressBook combo box it's true othrewise it's false.</p>
     */
    private String changedAddressBook;
    private SettingDateFormatter dateFormatter;
    /**
     * <p>Stores the person's date of birth in the format specified by
     * setting 'i18nDateInputDisplay'.</p>
     */
    private String dateOfBirthString;
    /**
     * <p>Set to <code>true</code> if this person is an employee.
     * Otherwise, <code>false</code>.</p>
     */
    private boolean employee;

    /**
     * <p>Users can be enabled and disabled. This lets the system retain
     * user information such as comments whilst locking the user out.</p>
     */
    private boolean enableUser;
    /**
     * <p>Stores the index of the last telecom address being displayed.
     * This is used to increase the list by 3 every time the "show more"
     * button is clicked.</p>
     */
    private Integer lastTelecomAddress;

    /**
     * <p>
     * Mask containing all the field definitions for this list.
     * </p>
     */
    private Mask mask;
    /**
     * <p>If non-<code>null</code> and not empty, indicates we should
     * display more telecom address fields.</p>
     */
    private String moreTelecomAddresses;

    /**
     * <p>Instance of person data object, containing all values
     * of person being submitted.</p>
     */
    private PersonDO person;
    /**
     * <p>
     * Indicates which TAB is active. This name is generated by the tab control
     * and depends on the name of the form !!!!!
     * </p>
     */
    private Integer personTab_activeTab;
    /**
     * <p>
     * Indicates the localization key of the active tab. This name is generated
     * by the tab control and depends on the name of the form !!!!!
     * </p>
     */
    private String personTab_activeTabKey;
    /**
     *<p>if contact is readOnly it's true</p>
     */
    private boolean readOnly;
    /**
     * <p>If <code>true</code> then the main frame is loaded
     * with the address book list when the dialog closes.</p>
     */
    private boolean refreshOnExit;
    /**
     * <p>Button for restoring removed user.</p>
     */
    private String restoreUser;
    private Security security;

    /**
     * <p>
     * Contains the localization key for each of the tabs to be shown.
     * </p>
     */
    private List tabKeys = new Vector();
    /**
     * <p>Absolute (to application context) location of the page to
     * include for the tab body.</p>
     */
    private String tabPage;
    /**
     * <p>
     * This list representation of the telecom addresses in person are easier
     * for <strong>Struts</strong> to manipulate.
     * </p>
     */
    private List telecomAddresses;
    /**
     * <p>The title we will display depends on the action we're taking -
     * add, amend or removed.</p>
     */
    private String titleKey;
    /**
     * <p>
     * Map of all user group names available. This is used to construct a
     * list to choose groups the user is a member of.
     * </p>
     */
    private List userGroups;
    /**
     * <p>Stores the new user name of the person. This is done so we can
     * compare with before to see if the person is a new user, has had
     * his/her user name changed or has lost the right to be a user.</p>
     */
    private String userName;
    /**
     * <p>
     * If <code>true</code> then the user tab is shown but cannot be changed.
     * </p>
     */
    private boolean userTabReadOnly;
    /**
     * <p>
     * Constructor - called by <strong>PicoContainer</strong>.
     * </p>
     *
     * @param addressBook addresss book implementation.
     * @param dateFormatter date formatter for the current session.
     * @param maskParam Refer to {@link DialogForm#DialogForm}.
     * @param baseClassParam Refer to {@link DialogForm#DialogForm}.
     */
    public PersonForm(final AddressBook addressBook,
            final SettingDateFormatter dateFormatter,
            final Security security,
            final MaskFactory maskFactory) {
        this.addressBook = addressBook;
        this.dateFormatter = dateFormatter;
        this.security = security;
        mask = maskFactory.getMask(PersonDO.class);
    }

    /**
     * <p>
     * Return all form state to initial values.
     * </p>
     *
     * @see com.ivata.mask.web.struts.MaskForm#clear()
     */
    protected void clear() {
        addressBookGroupId = null;
        addressBookGroupName = "";
        canRemove = false;
        changedAddressBook = null;
        dateOfBirthString = null;
        enableUser = false;
        employee = false;
        lastTelecomAddress = null;
        moreTelecomAddresses = null;
        person = new PersonDO();
        personTab_activeTab = null;
        personTab_activeTabKey = null;
        readOnly = false;
        refreshOnExit = true;
        restoreUser = null;
        tabKeys = new Vector();
        tabPage = "/addressBook/personDetails.jsp";
        telecomAddresses = new Vector();
        titleKey = "person.title.new";
        userName = null;
        userGroups = null;
        userTabReadOnly = false;
    }

    /**
     * <p>
     * Helper to find and set the country on a changed address.
     * </p>
     *
     * @param securitySession valid security sesion, used to look up the country.
     * @param address the address for which to find the country. The country
     *  code should be set.
     */
    public void findAndSetCountry(final SecuritySession securitySession) {
        // first update the country in the person's address
        AddressDO address = person.getAddress();
        CountryDO addressCountry = new CountryDO();
        if (address != null) {
            if ((address.getCountry() != null)
                    && !StringHandling.isNullOrEmpty(address.getCountry().getCode())) {
                try {
                    addressCountry = addressBook.findCountryByCode(securitySession, address.getCountry().getCode());
                } catch (SystemException e) {
                    throw new RuntimeException(e);
                }
            }
            address.setCountry(addressCountry);
        }
        // now update the country in the person's employee record
        EmployeeDO employee = person.getEmployee();
        if (employee != null) {
            CountryDO employeeCountry = new CountryDO();
            if ((employee.getCountry() != null)
                    && !StringHandling.isNullOrEmpty(employee.getCountry().getCode())) {
                try {
                    employeeCountry = addressBook.findCountryByCode(securitySession, employee.getCountry().getCode());
                } catch (SystemException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // if the employee country is empty, default it to the address
                // country
                employeeCountry = addressCountry;
            }
            employee.setCountry(employeeCountry);
        }
    }
    /**
     * <p>
     * Unique identifier of the address book group. This dictates which groups
     * to choose from.
     * </p>
     *
     * @return current value of addressBookId.
     */
    public final Integer getAddressBookGroupId() {
        return addressBookGroupId;
    }
    /**
     * <p>
     * Name of the address book group. This dictates which groups
     * to choose from.
     * </p>
     *
     * @return current value of addressBookGroupName.
     */
    public final String getAddressBookGroupName() {
        return addressBookGroupName;
    }
    /**
     * <p>
     * Map of all address books available. This is used to construct a combo
     * list for the user to choose from.
     * </p>
     *
     * @return current value of addressBookGroups.
     */
    public final Map getAddressBookGroupNames() {
        return addressBookGroupNames;
    }

    /**
     * <p>
     * Defines the base class of all objects in the value object list.
     * </p>
     *
     * @return base class of all objects in the value object list.
     */
    public final Class getBaseClass() {
        return baseClass;
    }

    /**
     *<p>in case that userName can remove this contact it's true</p>
     *
     * @return the current value of canRemove.
     */
    public final boolean getCanRemove() {
        return this.canRemove;
    }

    /**
     * <p>if user chenged addressBook combo box it's true othrewise it's false.</p>
     *
     * @return the current value of changedAddressBook.
     */
    public final String getChangedAddressBook() {
        return this.changedAddressBook;
    }

    /**
     * <p>returns the person's date of birth in the format specified by
     * setting 'i18nDateInputDisplay'.</p>
     * @return the person's date of birth in the format specified by
     * setting 'i18nDateInputDisplay'
     */
    public final String getDateOfBirthString() {
        return dateOfBirthString;
    }

    /**
     * <p>Users can be enabled and disabled. This lets the system retain
     * user information such as comments whilst locking the user out.</p>
     *
     * @return the current value of enableUser.
     */
    public final boolean getEnableUser() {
        return enableUser;
    }

    /**
     * <p>Stores the index of the last telecom address being displayed.
     * This is used to increase the list by 3 every time the "show more"
     * button is clicked.</p>
     *
     * @return the current value of lastTelecomAddress.
     */
    public final Integer getLastTelecomAddress() {
        return lastTelecomAddress;
    }

    /**
     * <p>
     * Mask containing all the field definitions for this list.
     * </p>
     *
     * @return mask containing all the field definitions for this list.
     */
    public final Mask getMask() {
        return mask;
    }

    /**
     * <p>If non-<code>null</code> and not empty, indicates we should
     * display more telecom address fields.</p>
     *
     * @return the current value of moreTelecomAddresses.
     */
    public final String getMoreTelecomAddresses() {
        return moreTelecomAddresses;
    }

    /**
     * <p>Get the values of the person this form refers to as a dependent
     * value object.</p>
     *
     * @return person values this form refers to.
     *
     */
    public PersonDO getPerson() {
        return person;
    }

    /**
     * <p>
     * Indicates which TAB is active. This name is generated by the tab control
     * and depends on the name of the form !!!!!
     * </p>
     *
     * @return the current value of personTab_activeTab.

     */
    public final Integer getPersonTab_activeTab() {
        return personTab_activeTab;
    }
    /**
     * <p>
     * Indicates the localization key of the active tab. This name is generated
     * by the tab control and depends on the name of the form !!!!!
     * </p>
     *
     * @return Returns the personTab_activeTabKey.
     */
    public final String getPersonTab_activeTabKey() {
        return personTab_activeTabKey;
    }

    /**
     *<p>if contact is readOnly it's true</p>
     *
     * @return the current value of readOnly.
     */
    public final boolean getReadOnly() {
        return this.readOnly;
    }

    /**
     * <p>If <code>true</code> then the main frame is loaded
     * with the address book list when the dialog closes.</p>
     *
     * @return the current value of refreshOnExit.
     */
    public final boolean getRefreshOnExit() {
        return refreshOnExit;
    }

    /**
     * <p>Button for restoring removed user.</p>
     *
     * @return the current value of restoreUser.
     */
    public final String getRestoreUser() {
        return this.restoreUser;
    }
    /**
     * <p>
     * Contains the localization key for each of the tabs to be shown.
     * </p>
     *
     * @return Returns the tab keys.
     */
    public List getTabKeys() {
        return tabKeys;
    }

    /**
     * <p>Absolute (to application context) location of the page to
     * include for the tab body.</p>
     *
     * @return the current value of tabPage.
     */
    public final String getTabPage() {
        return tabPage;
    }
    /**
     * @return Returns the telecomAddresses.
     */
    public List getTelecomAddresses() {
        return telecomAddresses;
    }
    /**
     * <p>The title we will display depends on the action we're taking -
     * add, amend or removed.</p>
     *
     * @return the current value of titleKey.
     */
    public final String getTitleKey() {
        return titleKey;
    }

    /**
     * <p>
     * List of all user groups available.
     * </p>
     *
     * @return current value of userGroups.
     */
    public List getUserGroups() {
        return userGroups;
    }

    /**
     * <p>Stores the new user name of the person. This is done so we can
     * compare with before to see if the person is a new user, has had
     * his/her user name changed or has lost the right to be a user.</p>
     *
     * @return the current value of userName.
     */
    public final String getUserName() {
        return userName;
    }

    /**
     * <p>Set to <code>true</code> if this person is an employee.
     * Otherwise, <code>false</code>.</p>
     *
     * @return the current value of employee.
     */
    public boolean isEmployee() {
        return employee;
    }
    /**
     * <p>
     * If <code>true</code> then the user tab is shown but cannot be changed.
     * </p>
     *
     * @return current value of userTabReadOnly.
     */
    public boolean isUserTabReadOnly() {
        return userTabReadOnly;
    }

    /**
     * <p>Reset all bean properties to their default state.  This method
     * is called before the properties are repopulated by the controller
     * servlet.</p>
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(final ActionMapping mapping,
            final HttpServletRequest request) {
        // get the email address host which matches the current user
        if (request.getParameter("changedAddressBook")==null) {
            this.setChangedAddressBook("false");
        }
        // find out which tab has just been clicked
        if ("/addressBook/address.jsp".equals(tabPage)) {
            AddressDO address = new AddressDO();
            address.setCountry(new CountryDO());
            person.setAddress(address);
        } else if ("/addressBook/telecomAddress.jsp".equals(tabPage)) {
            moreTelecomAddresses = null;
            Iterator telecomAddressIterator = telecomAddresses.iterator();
            while (telecomAddressIterator.hasNext()) {
                TelecomAddressDO telecomAddress = (TelecomAddressDO) telecomAddressIterator.next();
                telecomAddress.setAddress("");
            }
        } else if ("/addressBook/member.jsp".equals(tabPage)) {
            // no nothing
        } else if ("/addressBook/user.jsp".equals(tabPage)) {
            enableUser = false;
            userName = "";
        } else if ("/addressBook/restoreUser.jsp".equals(tabPage)) {
            // do nothing
            int i = 0;
        } else if ("/addressBook/employee.jsp".equals(tabPage)) {
            employee = false;
            EmployeeDO employee = person.getEmployee();
            employee.setCountry(new CountryDO());
            employee.setNumber("");
            employee.setVacationDays(null);

        } else if (request.getParameter("person.company") != null) {
            person.setCompany("");
            person.setDateOfBirth(null);
            person.setFileAs("");
            person.setFirstNames("");
            person.setJobTitle("");
            person.setLastName("");
            person.setSalutation("");
        }
        personTab_activeTab = null;
        personTab_activeTabKey = "";

        super.reset(mapping, request);
    }
    /**
     * <p>
     * Unique identifier of the address book group. This dictates which groups
     * to choose from.
     * </p>
     *
     * @param addressBookId new value of addressBookId.
     */
    public final void setAddressBookGroupId(final Integer addressBookId) {
        this.addressBookGroupId = addressBookId;
    }
    /**
     * <p>
     * Name of the address book group. This dictates which groups
     * to choose from.
     * </p>
     *
     * @param addressBookGroupName new value of addressBookGroupName.
     */
    public final void setAddressBookGroupName(final String addressBookGroupName) {
        this.addressBookGroupName = addressBookGroupName;
    }
    /**
     * <p>
     * Map of all address books available. This is used to construct a combo
     * list for the user to choose from.
     * </p>
     *
     * @param addressBookGroups new value of addressBookGroups.
     */
    public final void setAddressBookGroupNames(final Map addressBookGroups) {
        this.addressBookGroupNames = addressBookGroups;
    }

    /**
     *<p>in case that userName can remove this contact it's true</p>
     *
     * @param canRemove the new value of canRemove.
     */
    public final void setCanRemove(final boolean canRemove) {
        this.canRemove = canRemove;
    }

    /**
     * <p>if user chenged addressBook combo box it's true othrewise it's false.</p>
     *
     * @param changedAddressBook the new value of changedAddressBook.
     */
    public final void setChangedAddressBook(final String changedAddressBook) {
        this.changedAddressBook = changedAddressBook;
    }

    /**
     * <p>Set the person's date of birth in the format 'dd.MM.YYYY'.</p>
     *
     * @param dateOfBirthString the person's date of birth in the format
     * 'dd.MM.YYYY'.
     *
     */
    public final void setDateOfBirthString(final String dateOfBirthString) {
        this.dateOfBirthString = dateOfBirthString;
    }
    /**
     * <p>Set to <code>true</code> if this person is an employee.
     * Otherwise, <code>false</code>.</p>
     *
     * @param employee the new value of employee.
     */
    public final void setEmployee(final boolean employee) {
        this.employee = employee;
    }

    /**
     * <p>Users can be enabled and disabled. This lets the system retain
     * user information such as comments whilst locking the user out.</p>
     *
     * @param enableUser the new value of enableUser.
     */
    public final void setEnableUser(final boolean enableUser) {
        this.enableUser = enableUser;
    }

    /**
     * <p>Stores the index of the last telecom address being displayed.
     * This is used to increase the list by 3 every time the "show more"
     * button is clicked.</p>
     *
     * @param lastTelecomAddress the new value of lastTelecomAddress.
     */
    public final void setLastTelecomAddress(final Integer lastTelecomAddress) {
        this.lastTelecomAddress = lastTelecomAddress;
    }

    /**
     * <p>If non-<code>null</code> and not empty, indicates we should
     * display more telecom address fields.</p>
     *
     * @param moreTelecomAddresses the new value of moreTelecomAddresses.
     */
    public final void setMoreTelecomAddresses(final String moreTelecomAddresses) {
        this.moreTelecomAddresses = moreTelecomAddresses;
    }

    /**
     * <p>Get the values of the person this form refers to as a dependent
     * value object.</p>
     *
     * @param person person values this form refers to.
     *
     */
    public final void setPerson(final PersonDO person) {
        this.person = person;
        // set all the telecom addresses
        telecomAddresses = new Vector();
        telecomAddresses.addAll(person.getTelecomAddresses());
        // set the user name
        userName = "";
        if (person.getUser() != null) {
            userName = person.getUser().getName();
        }

        // set the address book name and id
        if ((person.getGroup() != null)
                && (person.getGroup().getAddressBook() != null)) {
            GroupDO addressBook = person.getGroup().getAddressBook();
            addressBookGroupId = addressBook.getId();
            addressBookGroupName = addressBook.getName();
        }
    }
    /**
     * <p>
     * Update the person to show all the addresses currently in the form.
     * </p>
     */
    public void setPersonFromTelecomAddresses() {
        // remove empty telecom addresses
        Iterator formTelecomAddressIterator = telecomAddresses.iterator();
        Set personTelecomAddresses = new HashSet();
        UserDO user = person.getUser();

        // remove empty addresses and find out if person has any email address
        Iterator telecomAddressIterator = personTelecomAddresses.iterator();
        while (formTelecomAddressIterator.hasNext()) {
            TelecomAddressDO thisTelecomAddress = (TelecomAddressDO)
                formTelecomAddressIterator.next();
            if (!StringHandling.isNullOrEmpty(thisTelecomAddress.getAddress())) {
                personTelecomAddresses.add(thisTelecomAddress);
            }
        }
        person.setTelecomAddresses(personTelecomAddresses);
    }

    /**
     * <p>
     * Indicates which TAB is active. This name is generated by the tab control
     * and depends on the name of the form !!!!!
     * </p>
     *
     * @param personTab_activeTab the new value of personTab_activeTab.

     */
    public final void setPersonTab_activeTab(final Integer personTab_activeTab) {
        this.personTab_activeTab = personTab_activeTab;
    }
    /**
     * <p>
     * Indicates the localization key of the active tab. This name is generated
     * by the tab control and depends on the name of the form !!!!!
     * </p>
     *
     * @param personTab_activeTabKey The personTab_activeTabKey to set.
     */
    public final void setPersonTab_activeTabKey(final String personTab_activeTabKey) {
        this.personTab_activeTabKey = personTab_activeTabKey;
    }

    /**
     *<p>if contact is readOnly it's true</p>
     *
     * @param readOnly the new value of readOnly.
     */
    public final void setReadOnly(final boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * <p>If <code>true</code> then the main frame is loaded
     * with the address book list when the dialog closes.</p>
     *
     * @param refreshOnExit the new value of refreshOnExit.
     */
    public final void setRefreshOnExit(final boolean refreshOnExit) {
        this.refreshOnExit = refreshOnExit;
    }

    /**
     * <p>Button for restoring removed user.</p>
     *
     * @param restoreUser the new value of restoreUser.
     */
    public final void setRestoreUser(final String restoreUser) {
        this.restoreUser = restoreUser;
    }
    /**
     * <p>
     * Contains the localization key for each of the tabs to be shown.
     * </p>
     *
     * @param tabKeys New value of tab keys.
     */
    public final void setTabKeys(final List tabKeys) {
        this.tabKeys = tabKeys;
    }

    /**
     * <p>Absolute (to application context) location of the page to
     * include for the tab body.</p>
     *
     * @param tabPage the new value of tabPage.
     */
    public final void setTabPage(final String tabPage) {
        this.tabPage = tabPage;
    }
    /**
     * @param telecomAddresses The telecomAddresses to set.
     */
    public final void setTelecomAddresses(final List telecomAddresses) {
        this.telecomAddresses = telecomAddresses;
    }

    /**
     * <p>
     * Initialize the telecom addresses on the form from the person object.
     * </p>
     */
    public final void setTelecomAddressesFromPerson() {
        // first remove the blank telecom addresses
        Iterator personTelecomAddressIterator = person.getTelecomAddresses().iterator();
        telecomAddresses = new Vector();
        while (personTelecomAddressIterator.hasNext()) {
            TelecomAddressDO thisTelecomAddress = (TelecomAddressDO)
                personTelecomAddressIterator.next();
            if (!StringHandling.isNullOrEmpty(thisTelecomAddress.getAddress())) {
                telecomAddresses.add(thisTelecomAddress);
            }
        }
        // show at least 5 telecom addresses in total, at least 3 blank ones
        int totalTelecomAddresses = telecomAddresses.size();
        int totalToShow = totalTelecomAddresses < 3 ? 5 : totalTelecomAddresses + 3;
        // if you wanted more telecom address fields, I'll give you even more
        if (!StringHandling.isNullOrEmpty(moreTelecomAddresses)) {
            totalToShow += 3;
        }
        for(int i=totalTelecomAddresses; i<totalToShow; ++i) {
            TelecomAddressDO newTelecomAddress = new TelecomAddressDO();
            telecomAddresses.add(newTelecomAddress);
            // iterate thro' all the types
            newTelecomAddress.setType(i % TelecomAddressConstants.countTypes());
        }
    }

    /**
     * <p>The title we will display depends on the action we're taking -
     * add, amend or removed.</p>
     *
     * @param titleKey the new value of titleKey.
     */
    public final void setTitleKey(final String titleKey) {
        this.titleKey = titleKey;
    }

    /**
     * <p>
     * List of all user groups available.
     * </p>
     *
     * @param userGroupNames new value of userGroupNames.
     */
    public final void setUserGroups(final List userGroups) {
        this.userGroups = userGroups;
    }

    /**
     * <p>Stores the new user name of the person. This is done so we can
     * compare with before to see if the person is a new user, has had
     * his/her user name changed or has lost the right to be a user.</p>
     *
     * @param userName the new value of userName.
     */
    public final void setUserName(final String userName) {
        this.userName = userName;
    }
    /**
     * <p>
     * If <code>true</code> then the user tab is shown but cannot be changed.
     * </p>
     *
     * @param userTabReadOnly new value of userTabReadOnly.
     */
    public final void setUserTabReadOnly(final boolean userTabReadOnly) {
        this.userTabReadOnly = userTabReadOnly;
    }

    /**
     * <p>Validates the form contents.</p>
     */
    public ValidationErrors validate(final HttpServletRequest request,
            final HttpSession session) {
        ValidationErrors validationErrors = super.validate(request, session);
        SecuritySession securitySession = (SecuritySession)
            session.getAttribute("securitySession");

        // if we have userName -> correct
        if (!StringHandling.isNullOrEmpty(userName)) {
            userName = userName.toLowerCase();
            userName = userName.trim();
            // remove any spaces
            if (userName.indexOf(" ")!=-1) {
                userName = userName.replaceAll(" ","");
                validationErrors.add(new ValidationError(
                        "user",
                        Security.BUNDLE_PATH,
                        mask.getField("name"),
                        "errors.addressBook.user.userName"));
            }
            if ((person.getUser() == null)
                    || !userName.equals(person.getUser().getName())) {
                try {
                    if (security.isUser(securitySession, userName)) {
                        validationErrors.add(new ValidationError(
                                "user",
                                Security.BUNDLE_PATH,
                                mask.getField("name"),
                                "errors.unique"));
                    }
                } catch (SystemException e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
        // update the person to reflect the current email addresses
        setPersonFromTelecomAddresses();
        // now update the form again
        setTelecomAddressesFromPerson();

        // find the address country, if an address was specified, or delete the
        // address if it is empty
        findAndSetCountry(securitySession);
        AddressDO address = person.getAddress();
        if (StringHandling.isNullOrEmpty(address.getPostCode())
                && StringHandling.isNullOrEmpty(address.getRegion())
                && StringHandling.isNullOrEmpty(address.getStreetAddress())
                && StringHandling.isNullOrEmpty(address.getTown())) {
            // if there is no address, ignore the address elements for
            // validation purpses (we'll set it back again at the end of this
            // method
            person.setAddress(null);
        }

        EmployeeDO employee = person.getEmployee();
        if (!this.employee) {
            // if this is not an employee, ignore the employee elements for
            // validation purpses (we'll set it back again at the end of this
            // method
            person.setEmployee(null);
        }


        // store the 'real' user for later
        UserDO user = person.getUser();
        try {
            // if user is going to remove from person
            if ((person.getUser()!= null)
                    && StringHandling.isNullOrEmpty(userName)) {
                person.setUser(null);
            } else {
                // this is just to test against server-side validation of user
                // name and enabled state
                UserDO tmpUser = new UserDO();
                tmpUser.setDeleted(user.isDeleted());
                tmpUser.setEnabled(enableUser);
                tmpUser.setGroups(user.getGroups());
                tmpUser.setName(userName);
                person.setUser(tmpUser);
            }
            validationErrors.addAll(addressBook.validate(securitySession,
                    person));

            // see if there is a date format error setting the date of birth
            // note: if dateOfBirth ever becomes mandatory, this will need to
            //       be set & checked before the validate above
            if (StringHandling.isNullOrEmpty(dateOfBirthString)) {
                person.setDateOfBirth(null);
            } else {
                try {
                    dateFormatter.setUserName((String) session.getAttribute("userName"));
                    dateFormatter.setDateFormat(DateFormatterConstants.DATE_INPUT);
                    dateFormatter.setDateTimeText("{0}");

                    person.setDateOfBirth(dateFormatter.parse(dateOfBirthString));
                } catch (SettingsInitializationException e) {
                    throw new RuntimeException(e);
                } catch (ParseException e) {
                    if (validationErrors == null) {
                        validationErrors = new ValidationErrors();
                    }
                    String[] parameters = {dateOfBirthString};

                    validationErrors.add(new ValidationError(
                            "personDetails",
                            AddressBook.BUNDLE_PATH,
                            mask.getField("dateOfBirthString"),
                            "errors.date",
                            Arrays.asList(parameters)));
                }
            }
        } catch (SystemException e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }
        // remove the active tab to return us to the start, if this is ok
        if (!StringHandling.isNullOrEmpty(getOk())) {
            session.removeAttribute("personTab_activeTab");
        }

        // return the address & employee data object to the person, if either
        // was set to null
        person.setAddress(address);
        person.setEmployee(employee);
        // likewise, the user
        person.setUser(user);
        return validationErrors;
    }
}