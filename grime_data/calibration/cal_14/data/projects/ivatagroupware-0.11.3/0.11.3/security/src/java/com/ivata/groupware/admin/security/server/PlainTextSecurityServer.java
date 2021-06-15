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

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.groupware.container.persistence.QueryPersistenceManager;
import com.ivata.mask.persistence.PersistenceSession;
import com.ivata.mask.util.SystemException;

/**
 * Simple security server which compares passwords against plain text values in
 * the CMP layer.
 *
 * <p>
 * This security server is not very secure! You are advised not to use this but
 * to set up an <strong>IMAP</strong> server with the  <code>MailServer</code>
 * class from the <code>webmail</code> subproject.
 * </p>
 *
 * @since 2004-05-11
 * @version $Revision: 1.3 $
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 */
public class PlainTextSecurityServer implements SecurityServer {
    /**
     * Persistence manger used to store/retrieve data objects, or retrieve a
     * new persistence session.
     */
    private QueryPersistenceManager persistenceManager;

    /**
     * Construct and initialize the Securtiy Server implementation.
     *
     * @param persistenceManager persistence manager used to store/retrieve data
     *     objects.
     */
    public PlainTextSecurityServer(QueryPersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    /**
     * Add a new user to the system.
     *
     * @param userName user name to add.
     * @param fullName full name under which the user will be filed.
     * @throws BusinessException if this user cannot be added.
     */
    public void addUser(final SecuritySession securitySession,
            final String userName,
            final String fullName) throws SystemException {
        // this server does not need to do anything additional to add a user
    }
    /**
     * Check the password for a user is correct.
     *
     * @param userName name of the user for whom to check the password.
     * @param password the new password value to check against the system.
     * @throws BusinessException if the password cannot be checked for any
     *     reason.
     */
    public void checkPassword(final SecuritySession securitySession,
            final String userName,
            final String password) throws SystemException {
        PersistenceSession persistenceSession =
            persistenceManager.openSession();
        try {

            UserDO user = (UserDO) persistenceManager.findInstance(persistenceSession,
                "securityUserByName",
                new Object[] { userName });

            String userPassword = user.getPassword();
            if (password == null) {
                if (userPassword != null) {
                    throw new SystemException("Null password specified - "
                        + "not null in data store for user '"
                        + userName
                        + "'.");
                }
            } else if (!password.equals(userPassword)) {
                throw new SystemException("Passwords do not match for "
                    + "user '"
                    + userName
                    + "'.");
            }
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }
    /**
     * @see com.ivata.groupware.admin.security.server.SecurityServer#getSystemUserName(String)
     */
    public final String getSystemUserName(final SecuritySession securitySession,
            final String userName) {
        return userName;
    }

    /**
     * @see com.ivata.groupware.admin.security.server.SecurityServer#getUserNameFromSystemUserName(String)
     */
    public final String getUserNameFromSystemUserName(
            final SecuritySession securitySession,
            final String systemUserName) {
        return systemUserName;
    }

    /**
     * Refer to {@link }.
     *
     * @param userNameParam
     * @return
     * @see com.ivata.groupware.admin.security.server.SecurityServer#isUser(java.lang.String)
     */
    public boolean isUser(final SecuritySession securitySession,
            String userNameParam) {
        return false;
    }
    /**
     * <p>Login to an authentication server using the user name and password
     * provided.</p>
     *
     * @param user user to login to the server.
     * @param password used to login to the server
     * @return valid session for this username password combination.
     * @throws BusinessException if this user cannot be authenticated.
     */
    public SecuritySession login(final UserDO user,
            final String password) throws SystemException {
        checkPassword(loginGuest(), user.getName(), password);
        PicoContainer globalContainer = PicoContainerFactory.getInstance()
            .getGlobalContainer();
        MutablePicoContainer sessionContainer = new DefaultPicoContainer(globalContainer);
        PlainTextSecuritySession session =
            new PlainTextSecuritySession(sessionContainer, user);
        sessionContainer.registerComponentInstance(SecuritySession.class, session);
        session.setPassword(password);
        return session;
    }

    /**
     * @see com.ivata.groupware.admin.security.server.SecurityServer#login()
     */
    public SecuritySession loginGuest() throws SystemException {
        PicoContainer globalContainer = PicoContainerFactory.getInstance()
            .getGlobalContainer();
        UserDO guestUser = new UserDO();
        guestUser.setDeleted(false);
        guestUser.setEnabled(true);
        guestUser.setName("guest");
        MutablePicoContainer sessionContainer = new DefaultPicoContainer(globalContainer);
        SecuritySession session = new PlainTextSecuritySession(sessionContainer, guestUser);
        sessionContainer.registerComponentInstance(SecuritySession.class, session);
        return session;
    }

    /**
     * <p>Remove the user with the given name from the system.</p>
     *
     * @param userName name of the user to be removed.
     * @throws BusinessException if this user cannot be removed.
     */
    public void removeUser(final SecuritySession securitySession,
            final String userName) throws SystemException {
        // don't need to do anything additional to remove a user for this server
    }

    /**
     * <p>Set the password for a user.</p>
     *
     * @param userName name of the user for whom to set the password.
     * @param password the new password value to set.
     * @throws BusinessException if the password cannot be set for any
     *     reason.
     */
    public final void setPassword(final SecuritySession securitySession,
            final String userName,
            final String password) throws SystemException {
        PersistenceSession persistenceSession;
        persistenceSession = persistenceManager.openSession();
        try {
            UserDO user = (UserDO) persistenceManager.findInstance(persistenceSession,
                "securityUserByName",
                new Object[] { userName });
            user.setPassword(password);
            persistenceManager.amend(persistenceSession, user);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    };

}
