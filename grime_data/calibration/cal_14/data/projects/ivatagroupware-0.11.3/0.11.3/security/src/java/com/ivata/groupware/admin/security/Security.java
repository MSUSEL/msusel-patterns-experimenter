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
package com.ivata.groupware.admin.security;

import javax.ejb.EJBException;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.mask.util.SystemException;

/**
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since   Mar 29, 2004
 * @version $Revision: 1.3 $
 */
public interface Security {
    public String BUNDLE_PATH = "security";
    /**
     * <p>Add a new user to the system.</p>
     *
     * @param securitySession checks the current site user is allowed to perform
     *   the action
     * @param user the user to be amended.
     */
    UserDO addUser(SecuritySession securitySession,
            UserDO user)
                throws SystemException;

    /**
     * <p>Amend a user in the system.</p>
     *
     * @param securitySession checks the current site user is allowed to perform
     *   the action
     * @param user the user to be amended.
     */
    void amendUser(SecuritySession securitySession,
            UserDO user)
                throws SystemException;

    /**
     * <p>Check a password is correct for a user.</p>
     *
     * @param userName
     * @param password
     * @throws InvalidFieldValueException if any of the parameters are
     *     <code>null</code>.
     * @throws InvalidFieldValueException if any of the parameters are
     *     <code>null</code>.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    void checkPassword(SecuritySession securitySession, String password)
        throws SystemException;
    /**
     * Find a user given the user name.
     *
     * @param securitySession valid security session.
     * @param userName name of the user to find.
     */
    UserDO findUserByName(SecuritySession securitySession, String userName)
            throws SystemException;

    /**
     * <p>This method add prefix to username.</p>
     *
     * @param userName
     * @return prefix_userName
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    String getSystemUserName(final SecuritySession securitySession,
            String userName)
        throws SystemException;

    /**
     * <p>This method is converting SystemUserName to userName, it's oposite method to getSystemUserName.</p>
     * @param systemUserName
     * @return
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    String getUserNameFromSystemUserName(final SecuritySession securitySession,
            String systemUserName)
        throws SystemException;

    boolean isUser(SecuritySession securitySession,
            String userNameParam) throws SystemException;
    /**
     * <p>Find out if a user is currently enabled
     * @param userName
     * @throws InvalidFieldValueException if <code>userName</code> is
     *     <code>null</code>.
     * @return <code>true</code> if the user is currently enabled, otherwise
     * <code>false</code>.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    boolean isUserEnabled(SecuritySession securitySession,
        String userName)
        throws SystemException;
    /**
     * <p>Login to the system. This method confirms the user name and password
     * against system settings and logs the user into the mail server, if this
     * is the desired form of authentication.</p>
     *
     * @param user to log into the remote system.
     * @param password the clear-text password to log into the remote system.
     * @throws EJBException if the person cannot log in.
     * @return the mail server used to access the mail system, or
     * <code>null</code> if another form of authentication is being used.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    SecuritySession login(UserDO user, String password)
        throws SystemException;

    /**
     * @param userName
     * @return
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    String loginAgain(SecuritySession securitySession, String userName)
        throws SystemException;

    SecuritySession loginGuest()
        throws SystemException;

    /**
     * <p>Remove a user from the system. <strong>Note:</strong> this can have dire
     * consequences and will delete all entries this user has ever made in the
     * system. Consider using <code>enableUser</code> instead.</p>
     *
     * @param userName the name of the user who is doing the removing. <strong>This
     *     is not the name of the user to be removed!</strong>
     * @param userNameRemove the name of the user to be removed.
     * @see #enableUser
     * @throws InvalidFieldValueException if any of the parameters are
     *     <code>null</code>.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    void removeUser(SecuritySession securitySession,
        String userNameRemove)
        throws SystemException;

    /**
     * <p>Restore user is he was delted.</p>
     * @param userName who is doing this operation
     * @param restoreUserName who is going to be restored
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    void restoreUser(SecuritySession securitySession,
        String restoreUserName)
        throws SystemException;
    /**
     * <p>Set the password of a user.</p>
     *
     * @param userName the name of the user who is changing the password in the
     *     system. <strong>This is not be the user name whose password is to be
     *     changed!</strong>
     * @param userNamePassword the name of the user for whom to change the
     *     password.
     * @param password the new value of the password (unencrypted) for the user.
     * @throws InvalidFieldValueException if any of the parameters are
     *     <code>null</code>.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    void setPassword(
        SecuritySession securitySession,
        String userNamePassword,
        String password)
        throws SystemException;

}