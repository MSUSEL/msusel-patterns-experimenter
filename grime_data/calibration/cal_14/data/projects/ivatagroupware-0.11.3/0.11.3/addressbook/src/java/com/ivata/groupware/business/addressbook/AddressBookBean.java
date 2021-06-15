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
package com.ivata.groupware.business.addressbook;

import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.addressbook.address.AddressDO;
import com.ivata.groupware.business.addressbook.address.country.CountryDO;
import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.business.addressbook.person.group.GroupDO;
import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.validation.ValidationErrors;


/**
 * <p>This address book provides clients with access to the register of people
 * within the ivata groupware system.</p>
 *
 * <p>Actions are here to retrieve people, change their details and enter new
 * people into the system. This bean manages people's street and
 * telecommunications addresses, together with which people are users and what
 * group(s) each person is a member of.</p>
 *
 * <p>Furthermore, this bean is responsible for assigning and maintaining
 * employee information.</p>
 *
 * <p><strong>Note</strong>, however that whilst the addressbook may react on which
 * people are users (it checks those people always have an email address), this
 * <em>EJB</em> is not responsible for adding, removing or amending users or their
 * passwords. For that functionality, see the {@link com.ivata.groupware.admin.security.SecurityBean
 * SecurityBean}.</p>
 *
 * @since 2002-05-12
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 * TODO: the employee and user methods need to check the user rights
 *
 * @ejb.bean
 *      name="AddressBook"
 *      display-name="AddressBook"
 *      type="Stateless"
 *      view-type="remote"
 *      jndi-name="AddressBookRemote"
 *
 * @ejb.transaction
 *      type = "Required"
 *
 *  @ejb.home
 *      generate="false"
 *      remote-class="com.ivata.groupware.business.addressbook.AddressBookRemoteHome"
 *
 *  @ejb.interface
 *      remote-class="com.ivata.groupware.business.addressbook.AddressBookRemote"
 */
public class AddressBookBean implements SessionBean, AddressBook {
    /**
     * <p>Provides the session bean with container-specific information.</p>
     */
    SessionContext sessionContext;


    /**
     * <p>Add bew AddressBook.</p>
     * @param userName
     * @param groupDO
     * @return
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public GroupDO addAddressBook(final SecuritySession securitySession,
            final GroupDO groupDO)
            throws SystemException {
        return getAddressBook().addAddressBook(securitySession, groupDO);
    }

    /**
     * <p>Add a new group to the address book.</p>
     *
     * @param userName the name of the user who wants to add the group. This is
     *     used to check user rights.
     * @param groupDO a data object containing all the details
     *     of the group to add.
     * @exception com.ivata.groupware.ejb.entity.UserRightException if the user
     *     provided is not entitled to add this group
     * @exception com.ivata.groupware.ejb.entity.InvalidFieldValueException
     *     if a field value in the new group contains an invalid value
     * @return the new group data object, with the details as they
     *     now are in the adressbook.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public GroupDO addGroup(final SecuritySession securitySession,
            final GroupDO groupDO)
            throws SystemException {
        return getAddressBook().addGroup(securitySession, groupDO);
    }

    /**
     * <p>Add a new person to the address book.</p>
     *
     * @param personDO data object containing the details of the
     *     person you want to add.
     * @return the new person data object, with the details as they
     *     now are in the adressbook.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public PersonDO addPerson(final SecuritySession securitySession,
            final PersonDO personDO)
            throws SystemException {
        return getAddressBook().addPerson(securitySession, personDO);
    }

    /**
     * <p>add new userGroup.</p>
     * @param userName
     * @param groupDO
     * @return
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public GroupDO addUserGroup(final SecuritySession securitySession,
            final GroupDO groupDO)
            throws SystemException {
        return getAddressBook().addUserGroup(securitySession, groupDO);
    }

    /**
     * <p>Amend the details of a group in the public address book.</p>
     *
     * @param userName the name of the user who wants to amend the group. This
     *     is used to check user rights.
     * @param groupDO a data object containing all the details
     *     of the group to amend.
     * @exception com.ivata.groupware.ejb.entity.UserRightException if the user
     *     provided is not entitled to amend this group.
     * @exception com.ivata.groupware.ejb.entity.InvalidFieldValueException
     *     if a field value in the new group contains an invalid value.
     * @return the new group data object, with the details as they
     *     now are in the adressbook.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public GroupDO amendGroup(final SecuritySession securitySession,
            final GroupDO groupDO)
            throws SystemException {
        return getAddressBook().amendGroup(securitySession, groupDO);
    }

    /**
     * <p>Change/update a person's details in the addressbook.</p>
     *
     * @param personDO data object containing the details of the
     *     person you want to amend.
     * @return the new person data object, with the details as they
     *     now are in the adressbook.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public PersonDO amendPerson(final SecuritySession securitySession,
            final PersonDO personDO)
            throws SystemException {
        return getAddressBook().amendPerson(securitySession, personDO);
    }

    /**
     * <p>Called by the container to notify an entity object it has been
     * activated.</p>
     */
    public void ejbActivate() {}

    /**
     * <p>Called by the container just after the bean has been created.</p>
     *
     * @exception CreateException if any error occurs. Never thrown by this
     *     class.
     *
     * @ejb.create-method
     */
    public void ejbCreate() throws CreateException {}

    /**
     * <p>Called by the container to notify the entity object it will be
     * deactivated. Called just before deactivation.</p>
     */
    public void ejbPassivate() {}

    /**
     * <p>This method is called by the container when the bean is about
     * to be removed.</p>
     *
     * <p>This method will be called after a client calls the <code>remove</code>
     * method of the remote/local home interface.</p>
     *
     * @throws RemoveException if any error occurs. Currently never thrown by
     *     this class.
     */
    public final void ejbRemove() {}

    /**
     * <p>get Map of all address book names which are allowed with specific access.</p>
     * @param userName
     * @param access
     * @return
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public List findAddressBooks(final SecuritySession securitySession,
            final boolean includePersonal)
            throws SystemException {
        return getAddressBook().findAddressBooks(securitySession, includePersonal);
    }

    /**
     * <p>Find all of the countries in the system.</p>
     *
     * @return all of the coutries in the system as a <code>List</code>
     * of {@link com.ivata.groupware.business.addressbook.address.country.CountryDO CountryDO}
     * instances.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public List findAllCountries(final SecuritySession securitySession)
            throws SystemException {
        return getAddressBook().findAllCountries(securitySession);
    }


    /**
     * <p>Find All People in a specific address book.</p
     *
     * @see com.ivata.groupware.business.addressbook.AddressBook#findAllPeople(com.ivata.groupware.admin.security.server.SecuritySession, Integer, Integer, String)
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public List findAllPeopleInGroup(final SecuritySession securitySession,
            final GroupDO group,
            final String initialLetter) throws SystemException {
        return getAddressBook().findAllPeopleInGroup(securitySession, group,
            initialLetter);
    }

    /**
     * <p>Find a single county identified by its two-letter country code.</p>
     *
     * @param countryCode two-letter internet country code.
     * @return details of the country as an instance of
     * {@link com.ivata.groupware.business.addressbook.address.country.CountryDO CountryDO),
     * or <code>null</code> if no such country exists.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public CountryDO findCountryByCode(final SecuritySession securitySession,
            final String countryCode)
            throws SystemException {
        return getAddressBook().findCountryByCode(securitySession, countryCode);
    }

    /**
     * <p>Find a group in the address book by its unique identifier.</p>
     *
     * @param id the unique identifier of the group to find.
     * @return the data object of the group which matches this unique
     *      identifier.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public GroupDO findGroupByPrimaryKey(final SecuritySession securitySession,
            final Integer id)
            throws SystemException {
        return getAddressBook().findGroupByPrimaryKey(securitySession, id);
    }

    /**
     * <p>
     * Find all groups which are siblings, identified by the parent group.
     * </p>
     * @see com.ivata.groupware.business.addressbook.AddressBook#findGroupsByParent(com.ivata.groupware.admin.security.server.SecuritySession, Integer)
     */
    public List findGroupsByParent(final SecuritySession securitySession,
            final Integer parentId) throws SystemException {
        return getAddressBook().findGroupsByParent(securitySession, parentId);
    }

    /**
     * <p>Find Id of personal AddressBook.</p>
     * @param userName
     * @return
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public GroupDO findPersonalAddressBook(final SecuritySession securitySession)
            throws SystemException {
        return getAddressBook().findPersonalAddressBook(securitySession);
    }

    /**
     * <p>Find a person in the address book by their unique identifier.</p>
     *
     * @param id the unique identifier of the person to find.
     * @return the person data object which matches this id, with the
     *      details as they now are in the adressbook.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public PersonDO findPersonByPrimaryKey(final SecuritySession securitySession,
            final String id)
            throws SystemException {
        return getAddressBook().findPersonByPrimaryKey(securitySession, id);
    }

    /**
     * <p>Find a person in the address book by their user name.</p>
     *
     * @param userName Name of the user to find.
     * @return the person data object which matches this user name.
     * @see com.ivata.groupware.business.addressbook.AddressBook#findPersonByUserName(com.ivata.groupware.admin.security.server.SecuritySession, String)
     */
    public PersonDO findPersonByUserName(final SecuritySession securitySession,
            final String userName) throws SystemException {
        return getAddressBook().findPersonByUserName(securitySession, userName);
    }

    /**
     * <p>get Map of all usergroup names which are allowed with specific access.</p>
     * @param userName
     * @param access
     * @return
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public List findUserGroups(final SecuritySession securitySession,
            final boolean includeAdministrator)
            throws SystemException {
        return getAddressBook().findUserGroups(securitySession, includeAdministrator);
    }

    /**
     * Get the addressbook implementation from the <code>PicoContainer</code>.
     */
    private AddressBook getAddressBook()
            throws SystemException {
        PicoContainer container = PicoContainerFactory.getInstance()
            .getGlobalContainer();
        return (AddressBook) container.getComponentInstance(AddressBook.class);
    }

    /**
     * <p>Remove a group from the public address book.</p>
     *
     * @param userName the name of the user who wants to remove the group. This
     *     is used to check user rights.
     * @param groupDO a data object containing all the details
     *     of the group to remove. The id of the group is used to identify which
     *      group to remove.
     * @exception com.ivata.groupware.ejb.entity.UserRightException if the user
     *     provided is not entitled to remvoe this group.
     * @exception com.ivata.groupware.ejb.entity.InvalidFieldValueException
     *     if the id of the group contains an invalid value.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public void removeGroup(final SecuritySession securitySession,
            final Integer id)
            throws SystemException {
        getAddressBook().removeGroup(securitySession, id);
    }

    /**
     * <p>Remove a person from the address book.</p>
     *
     * @param personDO data object containing the details of the
     *     person you want to remove. The id is used to locate and remove the
     *     person.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public void removePerson(final SecuritySession securitySession,
            final Integer id)
            throws SystemException {
        getAddressBook().removePerson(securitySession, id);
    }

    /**
     * <p>Set up the context for this entity object. The session bean stores the
     * context for later use.</p>
     *
     * @param sessionContext the new context which the session object should
     *     store.
     */
    public final void setSessionContext(final SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    /**
     * <p>Confirm all of the elements of the person are address and valid,
     * before the message is sent.</p>
     *
     * @param addressDO data object to check for consistency and
     *     completeness.
     * @return a collection of validation errors if any of the
     *     mandatory fields are missing, or if fields contain invalid values.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public ValidationErrors validate(final SecuritySession securitySession,
            final AddressDO addressDO)
            throws SystemException {
        return getAddressBook().validate(securitySession, addressDO);
    }

    /**
     * <p>Confirm all of the elements of the group are present and valid,
     * before the message is sent.</p>
     *
     * @param groupDO data object to check for consistency and
     *     completeness.
     * @return a collection of validation errors if any of the
     *     mandatory fields are missing, or if fields contain invalid values.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public ValidationErrors validate(final SecuritySession securitySession,
            final GroupDO groupDO)
            throws SystemException {
        return getAddressBook().validate(securitySession, groupDO);
    }

    /**
     * <p>Confirm all of the elements of the person are present and valid,
     * before the message is sent.</p>
     *
     * @param personDO data object to check for consistency and
     *     completeness.
     * @return a collection of validation errors if any of the
     *     mandatory fields are missing, or if fields contain invalid values.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public ValidationErrors validate(final SecuritySession securitySession,
            final PersonDO personDO)
            throws SystemException {
        return getAddressBook().validate(securitySession, personDO);
    }

}
