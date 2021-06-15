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
package com.ivata.groupware.business.addressbook.person;


import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.business.addressbook.address.AddressDO;
import com.ivata.groupware.business.addressbook.person.employee.EmployeeDO;
import com.ivata.groupware.business.addressbook.person.group.GroupDO;
import com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressConstants;
import com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressDO;
import com.ivata.groupware.container.persistence.NamedDO;
import com.ivata.mask.util.StringHandling;


/**
 * <p>Represents a single person within the intranet system. This person can
 * be simply within the address book, or can be a user by having a
 * {@link UserBean user} associated with it.</p>
 *
 * <p>This is a dependent value class, used to pass data back from the.</p>
 * {@link PersonBean PersonBean} to a client application.</p>
 *
 * <p><strong>Note:</strong> This class provides data from {@link PersonBean PersonBean}.
 * This is no local copy of the bean class, however, and changes here
 * will not be automatically reflected in {@link PersonBean PersonBean}.</p>
 *
 * @since 2002-05-12
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 *
 * @hibernate.class
 *      table="person"
 * @hibernate.cache
 *      usage="read-write"
 */
public class PersonDO extends NamedDO implements Comparable {
    /**
     * <p>Store this person's address.</p>
     */
    private AddressDO address;

    /**
     * <p>Store the company the person works for.</p>
     */
    private String company;

    /**
     * <p>Store the person's date of birth.</p>
     */
    private Date dateOfBirth;

    /**
     * <p>
     * Stores whether or not this person is logically deleted. If a person
     * is a user and is deleted, then (s)he will only be logically deleted.
     * </p>
     */
    private boolean deleted;

    /**
     * <p>Stores the employee record for this person as a data object.</p>
     */
    private EmployeeDO employee = null;

    /**
     * <p>Store the string to file the person under in the address book.</p>
     */
    private String fileAs;

    /**
     * <p>Store the person's first names.</p>
     */
    private String firstNames;

    /**
     * <p>Stores the group of this person.</p>
     */
    private GroupDO group;

    /**
     * <p>Store the job title for this person.</p>
     */
    private String jobTitle;

    /**
     * <p>Store the person's last name.</p>
     */
    private String lastName;

    /**
     * <p>Store the salutation with which this person likes to be greeted by
     * post or email.</p>
     */
    private String salutation;

    /**
     * <p>Store the communication addresses themeselves.</p>
     */
    private Set telecomAddresses = new HashSet();

    /**
     * <p>Stores the user for this person.</p>
     */
    private UserDO user;

    /**
     * Get all the addresses associated with this person.
     *
     * @return all addresses associated with this person.
     *
     * @hibernate.one-to-one
     *      cascade="all"
     */
    public AddressDO getAddress() {
        return address;
    }

    /**
     * <p>Get the company the person works for.</p>
     *
     * @return the company the person works for.
     * @hibernate.property
     *      column="company"
     */
    public final String getCompany() {
        return company;
    }

    /**
     * <p>Get the person's date of birth.</p>
     *
     * @return the person's date of birth.
     * @hibernate.property
     *      column="date_of_birth"
     */
    public final Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * <p>
     * Return the first name(s), followed by a space, followed by the last name.
     * </p>
     *
     * @see com.ivata.mask.valueobject.ValueObject#getDisplayValue()
     */
    public final String getDisplayValue() {
        StringBuffer displayValue = new StringBuffer();
        if (!StringHandling.isNullOrEmpty(firstNames)) {
            displayValue.append(firstNames);
        }
        if (!StringHandling.isNullOrEmpty(firstNames)) {
            if (displayValue.length() > 0) {
                displayValue.append(" ");
            }
            displayValue.append(lastName);
        }
        return displayValue.toString();
    }

    /**
     * <p>Get the person's email address, formatted according to <a
     * href='http://www.faqs.org/rfcs/rfc822.HTML'>RFC822</a>.</p>
     *
     *
     * <p>This gives you the form:<br>
     * <blockquote>&quot;<em>{firstName} {lastName}</em>&quot;&nbsp;&lt;<em>{email}</em>&gt;
     * </blockquote></p>
     *
     * <p>If the person has multiple email addresses, the returned one is the
     * first one found, as ordered by the address number.</p>
     *
     * @return formatted email address, or <code>null</code> if this person does
     *    not have an email address in the system.
     */
    public final String getEmailAddress() {
        String firstEmailAddress = null;
        for (Iterator telecomAddressIterator = telecomAddresses.iterator(); telecomAddressIterator.hasNext();) {
            TelecomAddressDO address = (TelecomAddressDO) telecomAddressIterator.next();
            if (address.getType() == TelecomAddressConstants.TYPE_EMAIL) {
                firstEmailAddress = address.getAddress();
                break;
            }
        }
        // if no addresses found, return null
        if (firstEmailAddress == null) {
            return null;
        }

        // first prepend the name if there is one for this person
        StringBuffer formattedEmailAddress = new StringBuffer();
        if (!StringHandling.isNullOrEmpty(firstNames)) {
            formattedEmailAddress.append(firstNames);
        }
        if (!StringHandling.isNullOrEmpty(lastName)) {
            if (formattedEmailAddress.length() > 0) {
                formattedEmailAddress.append(" ");
            }
            formattedEmailAddress.append(lastName);
        }
        if (formattedEmailAddress.length() > 0) {
            formattedEmailAddress.append(" ");
        }
        formattedEmailAddress.append("<");
        formattedEmailAddress.append(firstEmailAddress);
        formattedEmailAddress.append(">");

        return formattedEmailAddress.toString();
    }

    /**
     * <p>Get the employee record for this person as a data object.</p>
     *
     * @return employee record, or <code>null</code> if this person is not an
     * employee.
     * @hibernate.one-to-one
     *      cascade="all"
     *      property-ref="person"
     */
    public final EmployeeDO getEmployee() {
        return employee;
    }

    /**
     * <p>Get the string to file the person under in the address book.</p>
     *
     * @return the string to file the person under in the address book.
     * @hibernate.property
     *      column="file_as"
     */
    public final String getFileAs() {
        return fileAs;
    }

    /**
     * <p>Get the person's first names.</p>
     *
     * @return the person's first names, separated by a comma.
     * @hibernate.property
     *      column="first_names"
     */
    public final String getFirstNames() {
        return firstNames;
    }

    /**
     * <p>Get the group of this person.</p>
     *
     * @return the group this person is in.
     *
     * @hibernate.many-to-one
     *      column="person_group"
     */
    public final GroupDO getGroup() {
        return group;
    }
    /**
     * <p>Get the job title for this person.</p>
     *
     * @return the job title for this person.
     * @hibernate.property
     *      column="job_title"
     */
    public final String getJobTitle() {
        return  jobTitle;
    }

    /**
     * <p>Get the person's last name.</p>
     *
     * @return the person's last name.
     * @hibernate.property
     *      column="last_name"
     */
    public final String getLastName() {
        return lastName;
    }

    /**
     * <p>
     * Get the full name of this person. This is the same as the
     * <code>fileAs</code> value.
     * </p>
     *
     * @return fileAs value for this person
     */
    public final String getName() {
        return fileAs;
    }

    /**
     * <p>Get the saluation with which this person likes to be greeted by mail
     * or email.</p>
     *
     * @return the saluation with which this person likes to be greeted by mail
     *     or email.
     * @hibernate.property
     *      column="salutation"
     */
    public final String getSalutation() {
        return salutation;
    }

    /**
     * <p>Get the communication addresses themeselves.</p>
     *
     * @return a set containing the communication addresses themeselves.
     *
     * @hibernate.set
     *      cascade="all"
     *      role="telecom-addresses"
     * @hibernate.collection-key
     *      column="person"
     * @hibernate.collection-one-to-many
     *      class="com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressDO"
     */
    public final Set getTelecomAddresses() {
        return telecomAddresses;
    }

    /**
     * <p>Get the user for this person.</p>
     *
     * @return user name for this person, or <code>null</code> if this person is
     * not a user.
     * @hibernate.one-to-one
     *      cascade="all"
     */
    public UserDO getUser() {
        return user;
    }

    /**
     * @return Returns the deleted.
     * @hibernate.property
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * @param address The address to set.
     */
    public final void setAddress(final AddressDO address) {
        this.address = address;
    }
    /**
     * <p>Set the company the person works for.</p>
     *
     * @param company the company the person works for.
     */
    public final void setCompany(final String company) {
        this.company = company;
    }

    /**
     * <p>Set the person's date of birth.</p>
     *
     * @param dateOfBirth the new value supplied to person's date of birth.
     */
    public final void setDateOfBirth(final java.util.Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    /**
     * @param deletedParam The deleted to set.
     */
    public void setDeleted(boolean deletedParam) {
        deleted = deletedParam;
    }

    /**
     * <p>Get the employee record for this person as a data object.</p>
     *
     * @param employee employee record, or <code>null</code> if this person is
     * not an employee.
     */
    public final void setEmployee(final com.ivata.groupware.business.addressbook.person.employee.EmployeeDO employee) {
        this.employee = employee;
    }

    /**
     * <p>Set the string to file the person under in the address book.</p>
     *
     * @param fileAs the string to file the person under in the address book.
     */
    public final void setFileAs(final String fileAs) {
        this.fileAs = fileAs;
    }

    /**
     * <p>Set the person's first names.</p>
     *
     * @param firstNames the person's first names, separated by a comma.
     */
    public final void setFirstNames(final String firstNames) {
        this.firstNames = firstNames;
    }

    /**
     * <p>Set the group of this person.</p>
     *
     * @param group new value of the group.
     */
    public final void setGroup(final GroupDO group) {
        this.group = group;
    }

    /**
     * <p>Set the job title for this person.</p>
     *
     * @param jobTitle the job title for this person.
     */
    public final void setJobTitle(final String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * <p>Set the person's last name.</p>
     *
     * @param lastName the person's last name.
     */
    public final void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * <p>Set the saluation with which this person likes to be greeted by mail or
     * email.</p>
     *
     * @param salutation the saluation with which this person likes to be
     *     greeted by mail or email.
     */
    public final void setSalutation(final String salutation) {
        this.salutation = salutation;
    }

    /**
     * @param set
     */
    public final void setTelecomAddresses(final Set set) {
        telecomAddresses = set;
    }

    /**
     * <p>Set the user for this person.</p>
     *
     * @param user new user for this person, or <code>null</code> if
     * this person is not a user.
     */
    public final void setUser(final UserDO user) {
        this.user = user;
    }
}
