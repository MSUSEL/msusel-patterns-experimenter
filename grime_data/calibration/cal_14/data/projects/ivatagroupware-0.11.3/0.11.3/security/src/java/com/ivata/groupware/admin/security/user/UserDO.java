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
package com.ivata.groupware.admin.security.user;


import java.util.Set;

import com.ivata.groupware.container.persistence.BaseDO;


/**
 * <p>Each user within the system has a user name, and activation information
 * such as a password. This class maintains this information.</p>
 *
 * @since 2002-05-05
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 *
 * @hibernate.class
 *      table="person_user"
 * @hibernate.cache
 *      usage="read-write"
 */
public class UserDO  extends BaseDO {
    /**
     * <p><code>true</code> if user was deleted, and created some data in system
     * which we don't want to remove.</p>
     */
    private boolean deleted;

    /**
     * <p>Find out if a user is enabled or disabled.</p>
     */
    private boolean enabled;

    /**
     * <p>Each user is a member of several groups. By default, each user is a
     * member of the group called "everyOne" </p>
     */
    private Set groups;

    /**
     * <p>The user name. This name must uniquely identify the user
     * within the system, and should only contain alphanumeric characters.</p>
     */
    private String name;

    /**
     * <p>The user's password, if the authentication is not done through the
     * <code>IMAP</code> webmail interface.</p>
     */
    private String password;

    /**
     * <p>If the user is deleted we will make new name.</p>
     * @return userName
     */
    public final String getDisplayName() {
        if (isDeleted()) {
            return this.getName() + " (deleted)";
        } else {
            return this.getName();
        }
    }
    /**
     * <p>
     * For a user, the value displayed in a choice box is just the user name,
     * </p>
     *
     * @see com.ivata.mask.valueobject.ValueObject#getDisplayValue()
     */
    public final String getDisplayValue() {
        return name;
    }
    /**
     * <p>Each user is a member of several groups. By default, each user is a
     * member of the group called "everyOne" </p>
     *
     * @hibernate.set
     *      role="person_group"
     *      table="person_group_member"
     * @hibernate.collection-key
     *      column="person_user"
     * @hibernate.collection-many-to-many
     *      class="com.ivata.groupware.business.addressbook.person.group.GroupDO"
     *      column="person_group"
     */
    public final Set getGroups() {
        return groups;
    }
    /**
     * <p>
     * Unique identifier of this data object.
     * </p>
     *
     * NOTE: this is a hibernate one-to-one relationship with person, so we
     *       need no generator here...
     * @hibernate.id
     *      generator-class = "assigned"
     */
    public final Integer getId() {
       return super.getId();
    }
    /**
     * <p>Get the user name. This name must uniquely identify the user
     * within the system, and should only contain alphanumeric characters.</p>
     *
     * @return new value for the user name. Should be unique within the intranet
     *      system.
     *
     * @hibernate.property
     */
    public final String getName() {
        return name;
    }
    /**
     * <p>Get the user's password, if the authentication is not done through the
     * <code>IMAP</code> webmail interface.</p>
     *
     * @return new value of the user's password (encrypted).
     *
     * @hibernate.property
     */
    public final String getPassword() {
        return password;
    }
    /**
     * <p>True if user was deleted, and he did created some date in system which we don't want to remove.</p>
     * @return
     *
     * @hibernate.property
     */
    public boolean isDeleted() {
        return deleted;
    }
    /**
     * <p>Find out if a user is enabled or disabled.</p>
     *
     * @return <code>true</code> if the user is currently enabled, or
     *     <code>false</code> if the user is prevented from accessing the
     *     system.
     *
     * TODO: change column to enabled
     * @hibernate.property
     *      column="enable"
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * <p>Set to <code>true</code> if user was deleted, and created some data
     * in the system which we don't want to remove.</p>
     */
    public final void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * <p>Enable/disable this user. This prevents the usr from being able to
     * login and access the system.</p>
     *
     * @param enable set to <code>true</code> to the enable the user, or
     *     <code>false</code> to prevent this user from accessing the system.
     */
    public final void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * <p>Each user is a member of several groups. By default, each user is a
     * member of the group called "everyOne" </p>
     */
    public final void setGroups(final Set groups) {
        this.groups = groups;
    }

    /**
     * <p>Sets the user name. This name must uniquely identify the user
     * within the system, and should only contain alphanumeric characters.</p>
     *
     * @param name new value for the user name. Should be unique within the
     *     intranet system.
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * <p>Set the user's password, if the authentication is not done through the
     * <code>IMAP</code> webmail interface.</p>
     *
     * @param password new value of the user's password, already encrypted.
     */
    public final void setPassword(final String password) {
        this.password = password;
    }

}
