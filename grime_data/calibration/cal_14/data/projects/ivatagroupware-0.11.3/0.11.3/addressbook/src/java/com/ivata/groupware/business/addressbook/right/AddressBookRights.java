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
package com.ivata.groupware.business.addressbook.right;

import java.util.Collection;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.business.addressbook.person.employee.EmployeeDO;
import com.ivata.groupware.business.addressbook.person.group.GroupDO;
import com.ivata.mask.util.SystemException;

/**
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since   Apr 18, 2004
 * @version $Revision: 1.2 $
 */
public interface AddressBookRights {
    /**
     * <p>Change user rights for group.</p>
     *
     * @param id of group
     * @param rights collection of group ids which will have ACCESS right to that group
     * @param set to one of the <code>ACCESS_...</code> constants in <code>RightConstants</code>.
     */
    public abstract void amendRightsForGroup(
        SecuritySession securitySession,
        GroupDO group,
        Collection rights,
        Integer access)
        throws SystemException;
    /**
     * <p>TODO: add a comment here.</p>
     */
    public abstract boolean canAddEmployeeToPerson(
        SecuritySession securitySession,
        PersonDO person)
        throws SystemException;
    /**
     * <p>Find out if a used is allowed to add entries to a given group.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param groupId the unique identifier of the group to check.
     * @return <code>true</code> if the user is entitled to add to the group,
     *     otherwise <code>false</code>.
     */
    public abstract boolean canAddToGroup(
        SecuritySession securitySession,
        GroupDO group)
        throws SystemException;
    /**
     * <p>TODO: add a comment here.</p>
     */
    public abstract boolean canAmendEmployee(
        SecuritySession securitySession,
        EmployeeDO employeeDO)
        throws SystemException;
    /**
     * <p>Find out if a used is allowed to amend entries in a given group.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param groupId the unique identifier of the group to check.
     * @return <code>true</code> if the user is entitled to amend entries in the
     *      group, otherwise <code>false</code>.
     */
    public abstract boolean canAmendInGroup(
        SecuritySession securitySession,
        GroupDO group)
        throws SystemException;
    /**
     * <p>TODO: add a comment here.</p>
     */
    public abstract boolean canRemoveEmployee(
        SecuritySession securitySession,
        EmployeeDO employeeDO)
        throws SystemException;
    /**
     * <p>Find out if a used is allowed to remove entries from a given group.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param groupId the unique identifier of the group to check.
     * @return <code>true</code> if the user is entitled to remove from the
     *     group, otherwise <code>false</code>.
     */
    public abstract boolean canRemoveFromGroup(
        SecuritySession securitySession,
        GroupDO group)
        throws SystemException;
    /**
     * <p>Internal helper method. Find out if a user is allowed to access
     * entries in a given group.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param groupId the unique identifier of the group to check.
     * @param access the access level as defined in {@link
     *      com.ivata.groupware.business.addressbook.person.group.right.RightConstants
     *      RightConstants}.
     * @return <code>true</code> if the user is entitled to access entries in the
     *      group, otherwise <code>false</code>.
     */
    public abstract boolean canUser(
        SecuritySession securitySession,
        GroupDO group,
        Integer access)
        throws SystemException;
    /**
     * <p>Find the unique identifiers of all addressBooks which can be accessed by the
     * group specified, with the access level given.</p>
     *
     * @param groupId unique identifier of the group for which to search for
     *    other groups.
     * @param access the access level as defined in {@link
     *      com.ivata.groupware.business.addressbook.person.group.right.RightConstants
     *      RightConstants}.
     * @return a <code>Collection</code> of <code>Integer</code> instances,
     *      matching all groups which can be access with this level of access
     *      by the group specified.
     */
    public abstract Collection findAddressBooksByGroupAccess(
        SecuritySession securitySession,
        GroupDO group,
        Integer access)
        throws SystemException;
    /**
     * <p>Find groups which have <code>access</code> to group.
     * Return only those groups which can be see by that user.</p>
     *
     * @param userName user which is trying find rights
     * @param id of group which we are interesting
     * @param access find rights with this access
     * @return Collection of IDS of groups which have <code>access</code> to that group
     */
    public abstract Collection findRightsForGroup(
        SecuritySession securitySession,
        GroupDO group,
        Integer access)
        throws SystemException;
}