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
package com.ivata.groupware.admin.security.right;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.mask.util.SystemException;

/**
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since   Apr 18, 2004
 * @version $Revision: 1.2 $
 */
public interface SecurityRights {
    /**
     * <p>See if a user has sufficient rights to add user to the system - it's meen to everyOne group.</p>
     *
     * @param userName the user who wants to add another user.
     * @param personId the unique identifier of the person who will be added.
     * @return <code>true</code> if this action is authorized by the system,
     * otherwise <code>false</code>.
     *
     * @ejb.interface-method
     *      view-type = "both"
     */
    public boolean canAddUser(SecuritySession securitySession)
        throws SystemException;
    /**
     * <p>See if a user has sufficient rights to amend user in the
     * system - it's meen in everyone group.</p>
     *
     * @param userName the user who wants to add another user.
     * @param userNameAmend the user who should be amended.
     * @return <code>true</code> if this action is authorized by the system,
     * otherwise <code>false</code>.
     *
     * @ejb.interface-method
     *      view-type = "both"
     */
    public boolean canAmendUser(SecuritySession securitySession)
        throws SystemException;
    /**
     * <p>See if a user has sufficient rights to remove user from the
     * system - it's meen from everone group.</p>
     *
     * @param userName the user who wants to add another user.
     * @param userNameRemove the user who should be removed.
     * @return <code>true</code> if this action is authorized by the system,
     * otherwise <code>false</code>.
     *
     * @ejb.interface-method
     *      view-type = "both"
     */
    public boolean canRemoveUser(SecuritySession securitySession)
        throws SystemException;
    /**
     * <p>Internal helper method. Find out if a user is allowed to access
     * entries in a given group.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param groupId the unique identifier of the group to check.
     * @param access the access level as defined in {@link
     *      com.ivata.groupware.security.person.group.right.RightConstants
     *      RightConstants}.
     * @return <code>true</code> if the user is entitled to access entries in the
     *      group, otherwise <code>false</code>.
     */
    public boolean canUser(
        SecuritySession securitySession,
        Integer access)
        throws SystemException;
}