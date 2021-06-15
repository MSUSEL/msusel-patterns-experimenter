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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import javax.ejb.EJBException;

import com.ivata.groupware.admin.security.Security;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.admin.security.user.UserConstants;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.groupware.business.BusinessLogic;
import com.ivata.groupware.business.addressbook.address.AddressDO;
import com.ivata.groupware.business.addressbook.address.country.CountryDO;
import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.business.addressbook.person.group.GroupConstants;
import com.ivata.groupware.business.addressbook.person.group.GroupDO;
import com.ivata.groupware.business.addressbook.person.group.right.RightConstants;
import com.ivata.groupware.business.addressbook.right.AddressBookRights;
import com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressConstants;
import com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressDO;
import com.ivata.groupware.container.persistence.QueryPersistenceManager;
import com.ivata.groupware.container.persistence.TimestampDOHandling;
import com.ivata.groupware.container.persistence.listener.RemovePersistenceListener;
import com.ivata.mask.Mask;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.persistence.PersistenceException;
import com.ivata.mask.persistence.PersistenceSession;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.validation.ValidationError;
import com.ivata.mask.validation.ValidationErrors;
import com.ivata.mask.validation.ValidationException;
import com.ivata.mask.valueobject.ValueObject;

/**
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since Mar 22, 2004
 * @version $Revision: 1.5 $
 */
public class AddressBookImpl extends BusinessLogic implements AddressBook,
        RemovePersistenceListener {

    /**
     * <p>This class is used to sort the countries returned.</p>
     */
    private class CountryComparator implements java.util.Comparator {

        /**
         * <p>Compare two objects (in this case, both are instances of
         * {@link com.ivata.groupware.business.addressbook.address.country.CountryDO
         * CountryDO}) and return which is the higher priority.</p>
         *
         * @param object1 first country to be compared.
         * @param object2 second country to be compared
         * @return a negative integer, zero, or a positive integer as the first
         * argument is less than, equal to, or greater than the second.
         */
        public final int compare(final Object object1,
            final Object object2) {
            CountryDO country1 = (CountryDO) object1;
            CountryDO country2 = (CountryDO) object2;
            return new Integer(country1.getPriority()).compareTo(new Integer(country2.getPriority()));
        }
    }

    /**
     * <p>This class is used to sort the people returned.</p>
     */
    private class PersonComparator implements java.util.Comparator {

        /**
         * <p>Compare two objects (in this case, both are instances of
         * {@link com.ivata.groupware.business.addressbook.person.PersonDO PersonDO}) and
         * return which is the higher priority.</p>
         *
         * @param object1 First person to be compared.
         * @param object2 Second person to be compared.
         * @return a negative integer, zero, or a positive integer as the first
         * argument is less than, equal to, or greater than the second.
         */
        public int compare(final Object object1,
            final Object object2) {
            PersonDO person1 = (PersonDO) object1;
            PersonDO person2 = (PersonDO) object2;
            String fileAs1 = person1.getFileAs();
            String fileAs2 = person2.getFileAs();

            // watch for null values. treat them as less than zero
            if (fileAs1 == null) {
                return (fileAs2 == null) ? 0 : 1;
            } else if (fileAs2 == null) {
                return 1;
            } else {
                // add in the id in case the fileAs matches - it is no longer
                // unique
                fileAs1 += person1.getId();
                fileAs2 += person2.getId();
                return fileAs1.toLowerCase().compareTo(fileAs2.toLowerCase());
            }
        }
    }
    private MaskFactory maskFactory;

    /**
     * Persistence manger used to store/retrieve data objects.
     */
    private QueryPersistenceManager persistenceManager;
    private AddressBookRights rights;
    private Settings settings;
    /**
     * Construct a new address book.
     *
     * @param persistenceManager used to store objects in db.
     */
    public AddressBookImpl(final QueryPersistenceManager persistenceManager,
            final MaskFactory maskFactoryParam,
            final AddressBookRights rights,
            final Settings settings) {
        this.persistenceManager = persistenceManager;
        this.maskFactory = maskFactoryParam;
        persistenceManager.addRemoveListener(GroupDO.class, this);
        this.rights = rights;
        this.settings = settings;
    }

    /**
     * @see com.ivata.groupware.business.addressbook.AddressBook#addAddressBook(String, com.ivata.groupware.business.addressbook.person.group.GroupDO)
     */
    public GroupDO addAddressBook(final SecuritySession securitySession,
            final GroupDO groupDO)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            // before creating the group, check we have reasonable data
            ValidationErrors errors = validate(securitySession, groupDO);
            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }
            GroupDO parent = (GroupDO) persistenceManager.findByPrimaryKey(
                    persistenceSession,
                    GroupDO.class, GroupConstants.ADDRESS_BOOK_PUBLIC);
            groupDO.setParent(parent);

            TimestampDOHandling.add(securitySession, groupDO);
            persistenceManager.add(persistenceSession, groupDO);
            return groupDO;
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * @see com.ivata.groupware.business.addressbook.AddressBook#addGroup(String, com.ivata.groupware.business.addressbook.person.group.GroupDO)
     */
    public GroupDO addGroup(final SecuritySession securitySession,
            final GroupDO groupDO)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            // before creating the group, check we have reasonable data
            ValidationErrors errors = validate(securitySession, groupDO);
            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }

            TimestampDOHandling.add(securitySession, groupDO);
            return (GroupDO) persistenceManager.add(persistenceSession, groupDO);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * @see com.ivata.groupware.business.addressbook.AddressBook#addPerson(String, com.ivata.groupware.business.addressbook.person.PersonDO)
     */
    public PersonDO addPerson(final SecuritySession securitySession,
            final PersonDO person)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            // before creating the person, check we have reasonable data
            ValidationErrors errors = validate(securitySession, person);
            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }
            TimestampDOHandling.add(securitySession, person);
            if (person.getTelecomAddresses() != null) {
                Iterator telecomAddressIterator = person.getTelecomAddresses().iterator();
                while (telecomAddressIterator.hasNext()) {
                    TelecomAddressDO telecomAddress = (TelecomAddressDO) telecomAddressIterator.next();
                    // only new addressses allowed!
                    telecomAddress.setId(null);
                    TimestampDOHandling.add(securitySession, telecomAddress);
                }
            }
            // not sure why I need to do this? should be automatic?
            if (person.getAddress() != null) {
                person.getAddress().setPerson(person);
            }
            return (PersonDO) persistenceManager.add(persistenceSession,
                    person);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * @see com.ivata.groupware.business.addressbook.AddressBook#addUserGroup(String, com.ivata.groupware.business.addressbook.person.group.GroupDO)
     */
    public GroupDO addUserGroup(final SecuritySession securitySession,
            final GroupDO groupDO)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            // before creating the group, check we have reasonable data
            ValidationErrors errors = validate(securitySession, groupDO);
            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }

            GroupDO parent = (GroupDO) persistenceManager.findByPrimaryKey(
                    persistenceSession,
                    GroupDO.class, GroupConstants.USER_GROUP);
            groupDO.setParent(parent);

            TimestampDOHandling.add(securitySession, groupDO);
            return (GroupDO) persistenceManager.add(persistenceSession, groupDO);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * @see com.ivata.groupware.business.addressbook.AddressBook#amendGroup(String, com.ivata.groupware.business.addressbook.person.group.GroupDO)
     */
    public GroupDO amendGroup(final SecuritySession securitySession,
            final GroupDO groupDO)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            // before changing the group, check we have reasonable data
            ValidationErrors errors = validate(securitySession, groupDO);
            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }

            TimestampDOHandling.amend(securitySession, groupDO);
            persistenceManager.amend(persistenceSession, groupDO);
            return groupDO;
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * @see com.ivata.groupware.business.addressbook.AddressBook#amendPerson(String, com.ivata.groupware.business.addressbook.person.PersonDO)
     */
    public PersonDO amendPerson(final SecuritySession securitySession,
            final PersonDO person)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            // before creating the person, check we have reasonable data
            ValidationErrors errors = validate(securitySession, person);
            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }
            TimestampDOHandling.amend(securitySession, person);
            if (person.getTelecomAddresses() != null) {
                Iterator telecomAddressIterator = person.getTelecomAddresses().iterator();
                while (telecomAddressIterator.hasNext()) {
                    TelecomAddressDO telecomAddress = (TelecomAddressDO) telecomAddressIterator.next();
                    if (telecomAddress.getId() == null) {
                        TimestampDOHandling.add(securitySession, telecomAddress);
                    } else {
                        TimestampDOHandling.amend(securitySession, telecomAddress);
                    }
                }
            }
            // not sure why I need to do this? should be automatic, shurely?
            if (person.getAddress() != null) {
                person.getAddress().setPerson(person);
            }
            // if the person is an employee but the country hasn't been set,
            // remove the country object
            if ((person.getEmployee() != null)
                    && (person.getEmployee().getCountry() != null)
                    && (person.getEmployee().getCountry().getId() == null)) {
                person.getEmployee().setCountry(null);
            }
            // if this person is a user, (s)he must be in the public address
            // book
            if ((person.getUser() != null)
                    && !StringHandling.isNullOrEmpty(person.getUser()
                            .getName())) {
                GroupDO addressBook = person.getGroup().getAddressBook();
                if (!GroupConstants.equals(
                        addressBook.getId(),
                        GroupConstants.ADDRESS_BOOK_DEFAULT)) {
                    GroupDO defaultAddressBook = findGroupByPrimaryKey(
                            securitySession,
                            GroupConstants.ADDRESS_BOOK_DEFAULT);
                    person.setGroup(defaultAddressBook);
                }
            }
            persistenceManager.amend(persistenceSession, person);

            return person;
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * @see com.ivata.groupware.business.addressbook.AddressBook#getAddressBookNames(String, Integer, boolean)
     */
    public List findAddressBooks(final SecuritySession securitySession,
            final boolean includePersonal)
            throws SystemException {
        List addressBooks = new ArrayList();
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            GroupDO addressBookParent = (GroupDO) persistenceManager
                .findByPrimaryKey(
                        persistenceSession,
                        GroupDO.class,
                        GroupConstants.ADDRESS_BOOK_PUBLIC);
            List children = persistenceManager.find(persistenceSession,
                "addressBookGroupsInGroup", new Object[]{addressBookParent.getId()});
            addressBooks.addAll(children);

            // find personal addressBook for that user
            if (includePersonal) {
                GroupDO personalAddressBook = findPersonalAddressBook(securitySession);
                addressBooks.add(personalAddressBook);
            }
            return Collections.synchronizedList(addressBooks);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * @see com.ivata.groupware.business.addressbook.AddressBook#findAllCountries()
     */
    public List findAllCountries(final SecuritySession securitySession)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            return Collections.synchronizedList(persistenceManager.findAll(persistenceSession, CountryDO.class));
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }
    /**
     * <p>
     * Recursive implementation to find all people in child groups.
     * </p>
     *
     * @see com.ivata.groupware.business.addressbook.AddressBook#findCountryByCode(com.ivata.groupware.admin.security.server.SecuritySession, String)
     */
    private void findAllPeopleInGroup(final GroupDO group,
            final String lowerCaseInitialLetter,
            final List totalResults,
            final Stack parentGroups) throws SystemException {
        // go through the children first
        PersistenceSession persistenceSession = persistenceManager.openSession();
        parentGroups.push(group);

        Mask groupMask = maskFactory.getMask(GroupDO.class);
        try {
            List children = persistenceManager.find(persistenceSession,
                "addressBookGroupsInGroup", new Object[]{group.getId()});
            Iterator childIterator = children.iterator();
            while(childIterator.hasNext()) {
                GroupDO child = (GroupDO) childIterator.next();
                if (parentGroups.contains(child)) {
                    throw new ValidationException(
                            new ValidationError(
                                    "errors.addressbook.group.parent",
                                    Arrays.asList(new Object[] {
                                            group,
                                            child
                                    }))
                            );
                }
                findAllPeopleInGroup(child, lowerCaseInitialLetter, totalResults,
                        parentGroups);
            }
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            parentGroups.pop();
            persistenceSession.close();
        }

        // iterate through all the people
        // in this group and only add those which match the first letter
        Iterator personIterator = group.getPeople().iterator();
        while(personIterator.hasNext()) {
            PersonDO person = (PersonDO) personIterator.next();
            String fileAs = person.getFileAs();
            if (StringHandling.isNullOrEmpty(fileAs)) {
                continue;
            }
            if ((lowerCaseInitialLetter != null)
                            && !lowerCaseInitialLetter
                                .equals(fileAs.substring(0, 1).toLowerCase())) {
                continue;
            }
            totalResults.add(person);
        }
    }

    /**
     * <p>Find All People in a specific address book group.</p
     *
     * @see com.ivata.groupware.business.addressbook.AddressBook#findAllPeople(com.ivata.groupware.admin.security.server.SecuritySession, Integer, Integer, String)
     */
    public List findAllPeopleInGroup(final SecuritySession securitySession,
            final GroupDO group,
            final String initialLetterParam) throws SystemException {
        boolean demoVersion = settings.getBooleanSetting(securitySession,
                "demoVersion", null).booleanValue();
        List results = new Vector();
        String initialLetterLowerCase = initialLetterParam;
        if (initialLetterLowerCase != null) {
            initialLetterLowerCase = initialLetterLowerCase.toLowerCase();
        }
        findAllPeopleInGroup(group, initialLetterLowerCase, results,
                new Stack());

        // this filters out all the people which are in private address
        // books - this should be removed when real user rights are used again
        // this filtering stage also removes logically deleted people - that
        // can probably stay in (depending on how the rights are implemented)
        List filteredResults = new Vector();
        Iterator peopleIterator = results.iterator();
        while (peopleIterator.hasNext()) {
            PersonDO person = (PersonDO) peopleIterator.next();
            // don't show the administator in the demo
            // TODO: this should really be handled via user rights
            if (demoVersion && UserConstants
                    .ADMINISTRATOR.equals(person.getId())) {
                continue;
            }
            if (person.isDeleted()) {
                continue;
            }
            if (!rights.canUser(securitySession, person.getGroup(),
                                    RightConstants.ACCESS_VIEW)) {
                continue;
            }
            filteredResults.add(person);
        }

        return Collections.synchronizedList(filteredResults);
    }

    /**
     * @see com.ivata.groupware.business.addressbook.AddressBook#findCountryByCode(String)
     */
    public CountryDO findCountryByCode(final SecuritySession securitySession,
            final String countryCode)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            return (CountryDO) persistenceManager.findInstance(persistenceSession,
                "addressBookCountryByCode", new Object[] {countryCode});
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * @see com.ivata.groupware.business.addressbook.AddressBook#findGroupByPrimaryKey(Integer)
     */
    public GroupDO findGroupByPrimaryKey(final SecuritySession securitySession,
            final Integer id)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            return (GroupDO) persistenceManager.findByPrimaryKey(persistenceSession,
                GroupDO.class, id);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * <p>
     * Find all groups which are siblings, identified by the parent group.
     * </p>
     * @see com.ivata.groupware.business.addressbook.AddressBook#findGroupByPrimaryKey(Integer)
     */
    public List findGroupsByParent(final SecuritySession securitySession,
            final Integer parentId)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            return Collections.synchronizedList(persistenceManager.find(persistenceSession,
                "addressBookGroupsInGroup", new Object[]{parentId}));
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * @see com.ivata.groupware.business.addressbook.AddressBook#findPersonalAddressBook(String)
     */
    public GroupDO findPersonalAddressBook(final SecuritySession securitySession)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            return (GroupDO) persistenceManager.findInstance(persistenceSession,
                "addressBookGroupsInGroupByName",
                new Object[] {GroupConstants.ADDRESS_BOOK_PRIVATE,
                    securitySession.getUser().getName()});
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * @see com.ivata.groupware.business.addressbook.AddressBook#findPersonByPrimaryKey(Integer)
     */
    public PersonDO findPersonByPrimaryKey(final SecuritySession securitySession,
            final String id)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            return (PersonDO) persistenceManager.findByPrimaryKey(persistenceSession,
                PersonDO.class, id);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
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
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            return (PersonDO) persistenceManager.findInstance(persistenceSession,
                "addressBookPersonByUserName", new Object [] {userName});
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * @see com.ivata.groupware.business.addressbook.AddressBook#getUserGroupNames(String, Integer, boolean)
     */
    public List findUserGroups(final SecuritySession securitySession,
            final boolean includeAdministrator)
            throws SystemException {
        List userGroups = new ArrayList();
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            GroupDO userGroupParent = (GroupDO)
                persistenceManager.findByPrimaryKey(
                        persistenceSession,
                        GroupDO.class,
                        GroupConstants.USER_GROUP);
            List children = persistenceManager.find(persistenceSession,
                "addressBookGroupsInGroup", new Object[]{userGroupParent.getId()});
            userGroups.addAll(children);

            // if we should not include the administrator group, then excluded
            if (!includeAdministrator) {
                Iterator iterator = userGroups.iterator();
                while(iterator.hasNext()) {
                    GroupDO userGroup = (GroupDO) iterator.next();
                    if (GroupConstants.equals(userGroup.getId(),
                            GroupConstants.GROUP_ADMINISTRATOR)) {
                        userGroups.remove(userGroup);
                        break;
                    }
                }
            }
            // add "everyone" GROUP to the list
            userGroups.add(userGroupParent);
            return Collections.synchronizedList(userGroups);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * Refer to {@link RemovePersistenceListener#onRemove}.
     *
     * @param session Refer to {@link RemovePersistenceListener#onRemove}.
     * @param valueObject Refer to {@link RemovePersistenceListener#onRemove}.
     * @throws PersistenceException
     * Refer to {@link RemovePersistenceListener#onRemove}.
     */
    public void onRemove(final PersistenceSession persistenceSession,
            final ValueObject valueObject) throws PersistenceException {
        // only interested in deleting groups - at least for now!
        if (!(valueObject instanceof GroupDO)) {
            return;
        }
        GroupDO groupDO = (GroupDO) valueObject;

        // relocate everything in this group to the parent
        GroupDO parent = groupDO.getParent();

        List children = persistenceManager.find(persistenceSession,
            "addressBookGroupsInGroup", new Object[]{groupDO.getId()});

        // if this is an addressbook group, see if the group has any members
        if (GroupConstants.equals(groupDO.getParent().getId(),
                    GroupConstants.ADDRESS_BOOK_PRIVATE)
                || GroupConstants.equals(groupDO.getParent().getId(),
                        GroupConstants.ADDRESS_BOOK_PRIVATE)) {
            assert (children.size() > 0);
            assert (groupDO.getPeople().size() == 0);
        }
        // move all people and subgroups up a level
        Iterator childIterator = children.iterator();
        while (childIterator.hasNext()) {
            GroupDO child = (GroupDO) childIterator.next();
            child.setParent(parent);
            persistenceManager.amend(persistenceSession, child);
        }
        Iterator personIterator = groupDO.getPeople().iterator();
        while (personIterator.hasNext()) {
            PersonDO person = (PersonDO) personIterator.next();
            person.setGroup(parent);
            persistenceManager.amend(persistenceSession, person);
        }
    }

    /**
     * @see com.ivata.groupware.business.addressbook.AddressBook#removeGroup(String, Integer)
     */
    public void removeGroup(final SecuritySession securitySession,
            final Integer id)
            throws SystemException {
        // check we have an addressBookID and Id of group
        assert (id != null);
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            persistenceManager.remove(persistenceSession, GroupDO.class, id);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * @see com.ivata.groupware.business.addressbook.AddressBook#removePerson(String, Integer)
     */
    public void removePerson(final SecuritySession securitySession,
            final Integer id)
            throws SystemException {
        // check we have an id
        if (id == null) {
            throw new EJBException("ERROR in AddressBookBean: cannot remove a person with a null id");
        }
        PersistenceSession persistenceSession =
            persistenceManager.openSession(securitySession);
        PersonDO person;
        try {
            person = (PersonDO)persistenceManager.findByPrimaryKey(
                    persistenceSession, PersonDO.class, id);
        } finally {
            persistenceSession.close();
        }
        persistenceSession = persistenceManager.openSession(securitySession);
        try {
            // if this person has a user, only logically remove
            if (person.getUser() == null) {
                persistenceManager.remove(persistenceSession, person);
            } else {
                person.setDeleted(true);
                person.getUser().setDeleted(true);
                persistenceManager.amend(persistenceSession, person);
            }
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * Check the address data object is complete and represents a valid address.
     *
     * @param addressDO address to check.
     * @return all errors found when validating this data object. If no errors
     * are found, the returned instance is empty.
     */
    public ValidationErrors validate(final SecuritySession securitySession,
            final AddressDO addressDO) {
        ValidationErrors errors = new ValidationErrors();
        Mask mask = maskFactory.getMask(AddressDO.class);

        if ((addressDO.getCountry() == null)
                || StringHandling.isNullOrEmpty(addressDO.getCountry().getCode())) {
            errors.add(new ValidationError(
                    "person.address",
                    AddressBook.BUNDLE_PATH,
                    mask.getField("country"),
                    "errors.required"));
        }
        if (StringHandling.isNullOrEmpty(addressDO.getStreetAddress())) {
            errors.add(new ValidationError(
                    "person.address",
                    AddressBook.BUNDLE_PATH,
                    mask.getField("streetAddress"),
                    "errors.required"));
        }
        if (StringHandling.isNullOrEmpty(addressDO.getTown())) {
            errors.add(new ValidationError(
                    "person.address",
                    AddressBook.BUNDLE_PATH,
                    mask.getField("town"),
                    "errors.required"));
        }
        return errors;
    }

    /**
     * @see com.ivata.groupware.business.addressbook.AddressBook#validate(com.ivata.groupware.business.addressbook.person.group.GroupDO)
     */
    public ValidationErrors validate(final SecuritySession securitySession,
            final GroupDO groupDO) {
        ValidationErrors errors = new ValidationErrors();
        Mask mask = maskFactory.getMask(GroupDO.class);

        // name is mandatory
        if (StringHandling.isNullOrEmpty(groupDO.getName())) {
            errors.add(new ValidationError(
                    "group",
                    AddressBook.BUNDLE_PATH,
                    mask.getField("name"),
                    "errors.required"));
        }
        return errors;
    }

    /**
     * Check the person data object is complete and represents a valid person.
     *
     * @param personDO person to check.
     * @return all errors found when validating this data object. If no errors
     * are found, the returned instance is empty.
     */
    public ValidationErrors validate(final SecuritySession securitySession,
            final PersonDO personDO) {
        ValidationErrors errors = new ValidationErrors();
        Mask mask = maskFactory.getMask(PersonDO.class);
        Mask userMask = maskFactory.getMask(UserDO.class);

        if (StringHandling.isNullOrEmpty(personDO.getFileAs())) {
            errors.add(new ValidationError(
                    "personDetails",
                    AddressBook.BUNDLE_PATH,
                    mask.getField("fileAs"),
                    "errors.required"));
        }
        if (StringHandling.isNullOrEmpty(personDO.getFirstNames())) {
            errors.add(new ValidationError(
                    "personDetails",
                    AddressBook.BUNDLE_PATH,
                    mask.getField("firstNames"),
                    "errors.required"));
        }
        if (StringHandling.isNullOrEmpty(personDO.getLastName())) {
            errors.add(new ValidationError(
                    "personDetails",
                    AddressBook.BUNDLE_PATH,
                    mask.getField("lastName"),
                    "errors.required"));

        // contact has to belong to some group not root node of group tree !!!!
        }
        if (personDO.getGroup() == null) {
            errors.add(new ValidationError(
                    "personDetails",
                    AddressBook.BUNDLE_PATH,
                    mask.getField("addressBookName"),
                    "errors.required"));
        }
        // if this is the default administrator, you can't stop that person
        // being a user
        if (UserConstants.ADMINISTRATOR.equals(personDO.getId())) {
            if ((personDO.getUser() == null)
                    || !personDO.getUser().isEnabled()
                    || personDO.getUser().isDeleted()) {
                errors.add(new ValidationError(
                        "user",
                        Security.BUNDLE_PATH,
                        userMask.getField("name"),
                        "errors.addressBook.user.administrator"));

            }
        }


        if (personDO.getUser() != null) {
            if (personDO.getUser().getName() == null) {
                errors.add(new ValidationError(
                        "user",
                        Security.BUNDLE_PATH,
                        userMask.getField("name"),
                        "errors.required"));
            }

            // every user _must_ have at least one email address
            // go thro' all the telecom addresses and look for an email address
            boolean foundEmail = false;

            for (Iterator i = personDO.getTelecomAddresses().iterator(); i.hasNext();) {
                int type = ((TelecomAddressDO) i.next()).getType();
                if (type == TelecomAddressConstants.TYPE_EMAIL) {
                    foundEmail = true;
                    break;
                }
            }
            if (!foundEmail) {
                errors.add(new ValidationError(
                        "personDetails",
                        AddressBook.BUNDLE_PATH,
                        mask.getField("emailAddress"),
                        "errors.required"));
            }
        }
        // if any the mandatory address fields are there, see if they all are
        if (personDO.getAddress() != null) {
            ValidationErrors addressErrors = validate(securitySession,
                    personDO.getAddress());
            if (addressErrors != null) {
                errors.addAll(addressErrors);
            }
        }

        return errors;
    }
}