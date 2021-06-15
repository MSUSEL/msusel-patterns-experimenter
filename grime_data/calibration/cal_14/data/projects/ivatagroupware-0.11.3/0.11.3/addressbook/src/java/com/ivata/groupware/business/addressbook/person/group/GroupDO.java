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
package com.ivata.groupware.business.addressbook.person.group;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.container.persistence.NamedDO;
import com.ivata.groupware.web.tree.TreeNode;

/**
 * <p>Represents a group of people. This can be a company, a department or
 * a team within the addressbook. On the basis of these groupings, access
 * rights are assigned throughout the system.</p>
 *
 * @since 2002-05-15
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 *
 * @hibernate.class
 *      table="person_group"
 * @hibernate.cache
 *      usage="read-write"
 */
public class GroupDO extends NamedDO implements TreeNode {
    /**
     * Refer to {@link Logger}.
     */
    private static Logger logger = Logger.getLogger(GroupDO.class);
    /**
     * <p>Store a clear-text description of the function and purpose of this
     * group.</p>
     */
    private String description;

    /**
     * <p>Store the the person who is head of this group. In the case
     * where the group represents a company department, this should be the
     * manager of that department.</p>
     */
    private PersonDO head;

    /**
     * <p>Store the name of the group. This name should uniquely identify the
     * group in clear text.</p>
     */
    private String name;
    /**
     * <p>Parent group - group which contains this group.</p>
     */
    private GroupDO parent;

    /**
     * <p>All the people in this group.</p>
     */
    private Set people = new HashSet();

    /**
     * <p>All the users in this group.</p>
     */
    private Set users = new HashSet();

    /**
     * <p>
     * Address book group - used to reference the address book this group is in.
     * </p>
     *
     * @return Address book group - used to reference the address book this
     * group is in.
     */
    public final GroupDO getAddressBook() {
        // check this is not an address book itself
        assert (parent != null);
        if (GroupConstants.equals(getId(), GroupConstants.ADDRESS_BOOK)
                || GroupConstants.equals(GroupConstants.ADDRESS_BOOK,
                        parent.getId())) {
            return this;
        }
        GroupDO addressBook = this;
        assert (addressBook.parent.parent != null);
        
        // go thro' all parents till we find the address book.
        while (!GroupConstants.equals(
                        addressBook.parent.parent.getId(),
                        GroupConstants.ADDRESS_BOOK)) {
            addressBook = addressBook.parent;
            // if you get this, it is because the group tree has become unstable
            // : this group has no parent which is an address book anywhere up
            // the tree.
            if (addressBook.parent.parent == null) {
                throw new NullPointerException("ERROR in GroupDO: the group "
                        + "hierarchy is invalid: group '"
                        + name
                        + "' ("
                        + getId()
                        + ") has no address book as a parent.");
            }
        }
        return addressBook;
    }

    /**
     * <p>Get  a clear-text description of the function and purpose of this
     * group.</p>
     *
     * @return  a clear-text description of the function and purpose of this
     *     group.
     * @hibernate.property
     */
    public final String getDescription() {
        return description;
    }

    /**
     * <p>
     * For a group, the value displayed in a choice box is just the name,
     * </p>
     *
     * @see com.ivata.mask.valueobject.ValueObject#getDisplayValue()
     */
    public final String getDisplayValue() {
        return name;
    }

    /**
     * <p>Get the the person who is head of this group. In the case where
     * the group represents a company department, this should be the manager of
     * that department.</p>
     *
     * @return the the person who is head of this group.
     * @hibernate.many-to-one
     */
    public PersonDO getHead() {
        return head;
    }
    /**
     * <p>Get the name of the group. This name should uniquely identify the
     * group within its place in the heirarchy in clear text.</p>
     *
     * @return the name of this group, a clear-text string which identifies the
     *     group uniquely within its place in the group heirarchy.
     * @hibernate.property
     *      column="name"
     */
    public final String getName() {
        return name;
    }

    /**
     * <p>
     * Get the parent group of this group - the group which contains this one.
     * </p>
     *
     * @return the parent group of this group.
     * @hibernate.many-to-one
     *      column="parent"
     */
    public final GroupDO getParent () {
        return parent;
    }

    /**
     * <p>All the people in this group.</p>
     *
     * @return the people in this group.
     *
     * @hibernate.set
     * @hibernate.collection-key
     *      column="person_group"
     * @hibernate.collection-one-to-many
     *      cascade="all"
     *      class="com.ivata.groupware.business.addressbook.person.PersonDO"
     */
    public final Set getPeople() {
        return people;
    }

    /**
     * <p>All the users in this group.</p>
     *
     * @return the users in this group.
     *
     * @hibernate.set
     *      role="person_user"
     *      table="person_group_member"
     * @hibernate.collection-key
     *      column="person_group"
     * @hibernate.collection-many-to-many
     *      class="com.ivata.groupware.admin.security.user.UserDO"
     *      column="person_user"
     */
    public final Set getUsers() {
        return users;
    }

    /**
     * <p>Set a clear-text description of the function and purpose of this
     * group.</p>
     *
     * @param description  a clear-text description of the function and purpose
     *     of this group.
     */
    public final void setDescription(final String description) {
        this.description = description;
    }
    /**
     * <p>Set the person who is head of this group. In the case where
     * the group represents a company department, this should be the manager of
     * that department.</p>
     *
     * @param headParam the person who is head of this group.
     */
    public void setHead(PersonDO headParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("setHead before: '" + head + "', after: '"
                    + headParam + "'");
        }

        head = headParam;
    }
    /**
     * <p>Set the name of the group. This name should uniquely identify the
     * group in clear text.</p>
     *
     * @param nameParam the new name to set for this group.
     */
    public void setName(String nameParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("setName before: '" + name + "', after: '"
                    + nameParam + "'");
        }

        name = nameParam;
    }
    /**
     * <p>Set the parent of this group.</p>
     *
     * @param parentParam the parent of this group.
     */
    public void setParent(GroupDO parentParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("setParent before: '" + parent
                    + "', after: '" + parentParam + "'");
        }

        parent = parentParam;
    }
    /**
     * <p>All the people in this group.</p>
     *
     * @param peopleParam the people in this group.
     */
    public void setPeople(Set peopleParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("setPeople before: '" + people
                    + "', after: '" + peopleParam + "'");
        }

        people = peopleParam;
    }
    /**
     * <p>All the users in this group.</p>
     *
     * @param usersParam the users in this group.
     */
    public void setUsers(Set usersParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("setUsers before: '" + users
                    + "', after: '" + usersParam+ "'");
        }

        users = usersParam;
    }
}
