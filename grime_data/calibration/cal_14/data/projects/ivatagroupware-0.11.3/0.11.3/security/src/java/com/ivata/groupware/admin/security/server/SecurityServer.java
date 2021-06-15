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
package com.ivata.groupware.admin.security.server;

import java.io.Serializable;

import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.mask.util.SystemException;

/**
 * You must define a class which implements this interface, and set it as
 * in the setting <code>securitySessionServer</code>.
 *
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 */
public interface SecurityServer extends Serializable {
    /**
     * Add a new user to the system.
     *
     * @param userName user name to add.
     * @param fullName full name under which the user will be filed.
     * @throws BusinessException if this user cannot be added.
     */
    void addUser(final SecuritySession securitySession,
            final String userName,
            final String fullName)
        throws SystemException;

    /**
     * Check the password for a user is correct.
     *
     * @param userName name of the user for whom to check the password.
     * @param password the new password value to check against the system.
     * @throws BusinessException if the password cannot be checked for any
     *     reason.
     */
    void checkPassword(final SecuritySession securitySession,
            final String userName,
            final String password)
        throws SystemException;

    String getSystemUserName(final SecuritySession securitySession,
            final String userName);
    String getUserNameFromSystemUserName(final SecuritySession securitySession,
            final String systemUserName);

    /**
     * Find out if a user name is used or not.
     * @param user user to check
     * @throws SystemException if the user name cannot be checked for any
     * reason.
     */
    boolean isUser(final SecuritySession securitySession,
            final String userName)
            throws SystemException;

    /**
     * Login to an authentication server using the user name and password
     * provided.
     *
     * @param user user to login to the server.
     * @param password used to login to the server
     * @return valid session for this username password combination.
     * @throws BusinessException if this user cannot be authenticated.
     */
    SecuritySession login(final UserDO user,
            final String password)
        throws SystemException;

    /**
     * Login as a guest user to an authentication server.
     *
     * @return valid session for the guest user.
     * @throws BusinessException if this user cannot be authenticated.
     */
    SecuritySession loginGuest()
        throws SystemException;

    /**
     * <p>Remove the user with the given name from the system.</p>
     *
     * @param userName name of the user to be removed.
     * @throws BusinessException if this user cannot be removed.
     */
    void removeUser(final SecuritySession securitySession,
            final String userName)
        throws SystemException;

    /**
     * Set the password for a user.
     *
     * @param userName name of the user for whom to set the password.
     * @param password the new password value to set.
     * @throws BusinessException if the password cannot be set for any
     *     reason.
     */
    void setPassword(final SecuritySession securitySession,
            final String userName,
            final String password)
        throws SystemException;

}
