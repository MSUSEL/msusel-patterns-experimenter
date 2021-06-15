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

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.addressbook.address.AddressDO;
import com.ivata.groupware.business.addressbook.address.country.CountryDO;
import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.business.addressbook.person.group.GroupDO;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.validation.ValidationErrors;


/**
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since Mar 22, 2004
 * @version $Revision: 1.4 $
 */
public interface AddressBook {
    public final static String BUNDLE_PATH = "addressBook";
    /**
     * <p>Add bew AddressBook.</p>
     * @param userName
     * @param groupDO
     * @return
     */
    GroupDO addAddressBook(final SecuritySession securitySession,
            final GroupDO groupDO)
        throws SystemException;

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
     */
    GroupDO addGroup(final SecuritySession securitySession,
            final GroupDO groupDO)
            throws SystemException;

    /**
     * <p>Add a new person to the address book.</p>
     *
     * @param personDO data object containing the details of the
     *     person you want to add.
     * @return the new person data object, with the details as they
     *     now are in the adressbook.
     */
    PersonDO addPerson(final SecuritySession securitySession,
            final PersonDO personDO)
            throws SystemException;
    /**
     * <p>add new userGroup.</p>
     * @param userName
     * @param groupDO
     * @return
     */
    GroupDO addUserGroup(final SecuritySession securitySession,
            final GroupDO groupDO)
        throws SystemException;

    /**
     * <p>Amend the details of a group in the address book.</p>
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
     */
    GroupDO amendGroup(final SecuritySession securitySession,
            final GroupDO groupDO)
        throws SystemException;

    /**
     * <p>Change/update a person's details in the addressbook.</p>
     *
     * @param personDO data object containing the details of the
     *     person you want to amend.
     * @return the new person data object, with the details as they
     *     now are in the adressbook.
     */
    PersonDO amendPerson(final SecuritySession securitySession,
            final PersonDO personDO)
        throws SystemException;

    /**
     * <p>get Map of all address book names which are allowed with specific access.</p>
     * @param userName
     * @param access
     * @return
     */
    List findAddressBooks(final SecuritySession securitySession,
            final boolean includePersonal)
        throws SystemException;

    /**
     * <p>Find all of the countries in the system.</p>
     *
     * @return all of the coutries in the system as a <code>List</code>
     * of {@link com.ivata.groupware.business.addressbook.address.country.CountryDO CountryDO}
     * instances.
     */
    List findAllCountries(final SecuritySession securitySession)
        throws SystemException;

    /**
     * <p>Find All People in a specific address book.</p
     *
     * @see com.ivata.groupware.business.addressbook.AddressBook#findAllPeople(com.ivata.groupware.admin.security.server.SecuritySession, Integer, Integer, String)
     */
    List findAllPeopleInGroup(final SecuritySession securitySession,
            final GroupDO group,
            final String initialLetter) throws SystemException;

    /**
     * <p>Find a single county identified by its two-letter country code.</p>
     *
     * @param countryCode two-letter internet country code.
     * @return details of the country as an instance of
     * {@link com.ivata.groupware.business.addressbook.address.country.CountryDO CountryDO),
     * or <code>null</code> if no such country exists.
     */
    CountryDO findCountryByCode(final SecuritySession securitySession,
            final String countryCode)
        throws SystemException;

    /**
     * <p>Find a group in the address book by its unique identifier.</p>
     *
     * @param id the unique identifier of the group to find.
     * @return the data object of the group which matches this unique
     *      identifier.
     */
    GroupDO findGroupByPrimaryKey(final SecuritySession securitySession,
            final Integer id)
        throws SystemException;

    /**
     * <p>
     * Find all groups which are siblings, identified by the parent group.
     * </p>
     */
    List findGroupsByParent(final SecuritySession securitySession,
            final Integer parentId)
            throws SystemException;

    /**
     * <p>Find Id of personal AddressBook.</p>
     * @param userName
     * @return
     */
    GroupDO findPersonalAddressBook(final SecuritySession securitySession)
        throws SystemException;

    /**
     * <p>Find a person in the address book by their unique identifier.</p>
     *
     * @param id the unique identifier of the person to find.
     * @return the person data object which matches this id, with the
     *      details as they now are in the adressbook.
     */
    PersonDO findPersonByPrimaryKey(final SecuritySession securitySession,
            final String id)
        throws SystemException;

    /**
     * <p>Find a person in the address book by their user name.</p>
     *
     * @param userName Name of the user to find.
     * @return the person data object which matches this user name.
     */
    PersonDO findPersonByUserName(final SecuritySession securitySession,
            final String userName)
        throws SystemException;

    /**
     * <p>get Map of all usergroup names which are allowed with specific access.</p>
     * @param userName
     * @param access
     * @return
     */
    List findUserGroups(final SecuritySession securitySession,
            final boolean includeAdministrator)
        throws SystemException;

    /**
     * <p>Remove a group from the address book.</p>
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
     */
    void removeGroup(final SecuritySession securitySession,
            final Integer id)
        throws SystemException;

    /**
     * <p>Remove a person from the address book.</p>
     *
     * @param personDO data object containing the details of the
     *     person you want to remove. The id is used to locate and remove the
     *     person.
     */
    void removePerson(final SecuritySession securitySession,
            final Integer id)
            throws SystemException;

    /**
     * <p>Confirm all of the elements of the group are present and valid,
     * before the message is sent.</p>
     *
     * @param groupDO data object to check for consistency and
     *     completeness.
     * @return a collection of validation errors if any of the
     *     mandatory fields are missing, or if fields contain invalid values.
     */
    ValidationErrors validate(final SecuritySession securitySession,
            final AddressDO addressDO)
        throws SystemException;

     /**
      * <p>Confirm all of the elements of the group are present and valid,
      * before the message is sent.</p>
      *
      * @param groupDO data object to check for consistency and
      *     completeness.
      * @return a collection of validation errors if any of the
      *     mandatory fields are missing, or if fields contain invalid values.
      */
     ValidationErrors validate(final SecuritySession securitySession,
            final GroupDO groupDO)
         throws SystemException;

    /**
     * <p>Confirm all of the elements of the person are present and valid,
     * before the message is sent.</p>
     *
     * @param personDO data object to check for consistency and
     *     completeness.
     * @return a collection of validation errors if any of the
     *     mandatory fields are missing, or if fields contain invalid values.
     */
    ValidationErrors validate(final SecuritySession securitySession,
            final PersonDO personDO)
        throws SystemException;
}
