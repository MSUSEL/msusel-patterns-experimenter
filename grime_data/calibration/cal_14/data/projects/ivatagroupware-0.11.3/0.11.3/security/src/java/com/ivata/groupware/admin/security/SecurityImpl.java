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

import com.ivata.groupware.admin.security.server.SecurityServer;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.business.BusinessLogic;
import com.ivata.groupware.container.persistence.QueryPersistenceManager;
import com.ivata.mask.Mask;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.persistence.FinderException;
import com.ivata.mask.persistence.PersistenceSession;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.validation.ValidationError;
import com.ivata.mask.validation.ValidationException;


/**
 * <p>The security bean is responsible for adding, removing and amending users
 * to the system, and for logging in in the first place.</p>
 *
 * @since 2002-09-08
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 * TODO: finish user right implementation
 */
public class SecurityImpl extends BusinessLogic implements Security {
    private MaskFactory maskFactory;

    /**
     * Persistence manger used to store/retrieve data objects, or retrieve a
     * new persistence session.
     */
    private QueryPersistenceManager persistenceManager;

    /**
     * Security server - used for logging in users.
     */
    private SecurityServer securityServer;

    /**
     * All security features are disabled in demo mode. This is determined
     * by the site setting <code>demoVersion</code>.
     */
    private boolean demoVersion;

    /**
     * Construct and initialize the Security implementation.
     *
     * @param persistenceManager persistence manager used to store/retrieve data
     *     objects.
     */
    public SecurityImpl(final QueryPersistenceManager persistenceManager,
            final SecurityServer securityServer,
            final MaskFactory maskFactory,
            final Boolean demoVersion) {
        this.persistenceManager = persistenceManager;
        this.securityServer = securityServer;
        this.maskFactory = maskFactory;
        this.demoVersion = demoVersion.booleanValue();
    }
    /**
     * <p>Add a new user to the system.</p>
     * <p>
     * <strong>Note:</strong> if you are using an address book, do not use this method!!!
     * Use <code>AddressBookSecurity.addUserToPerson</code> instead.
     * </p>
     *
     * @param securitySession checks the current site user is allowed to perform
     *   the action
     * @param user the user to be amended.
     */
    public UserDO addUser(final SecuritySession securitySession,
            final UserDO user)
                throws SystemException {
        // not in demo mode!
        if (demoVersion) {
            return user;
        }

        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        Mask mask = maskFactory.getMask(UserDO.class);

        // if the user is deleted, it is naturally also disabled!
        if (user.isDeleted()) {
            user.setEnabled(false);
        }
        // first check this user name is not already taken
        if (isUser(securitySession, user.getName())) {
            throw new ValidationException(
                        new ValidationError(
                                "user",
                                Security.BUNDLE_PATH,
                                mask.getField("name"),
                                "errors.unique"
                                        ));
        }

        try {
            persistenceManager.add(persistenceSession, user);
            // if the user is enabled, add it to the security server
            if (user.isEnabled()) {
                securityServer.addUser(securitySession,
                        user.getName(), getRealName(
                        persistenceSession, user));
            }
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }

        return user;
    }

    /**
     * <p>Amend a user in the system.</p>
     *
     * @param securitySession checks the current site user is allowed to perform
     *   the action
     * @param user the user to be amended.
     */
    public void amendUser(final SecuritySession securitySession,
            final UserDO user)
                throws SystemException {
        if (demoVersion) {
            return;
        }
        Mask mask = maskFactory.getMask(UserDO.class);
        // check we have field values
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        UserDO oldUser = null;

        try {
            oldUser = (UserDO) persistenceManager.findByPrimaryKey(persistenceSession,
                    UserDO.class, user.getId());
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
        persistenceSession = persistenceManager.openSession(securitySession);
        try {

            // if the user is deleted, it is naturally also disabled!
            if (user.isDeleted()) {
                user.setEnabled(false);
            }

            // remove the user name from the mail system -
            // only if the old user is enabled
            if (!user.isEnabled()) {
                if (oldUser.isEnabled()) {
                    securityServer.removeUser(securitySession,
                            oldUser.getName());
                }
            } else {
                // likewise if the old user is disabled and it is now enabled,
                // recreate the user
                if (!oldUser.isEnabled()) {
                    securityServer.addUser(securitySession,
                            user.getName(), user.getDisplayName());
                }
                if(!oldUser.getName().equals(user.getName())) {
                    try {
                        UserDO sameNameUser =
                            (UserDO) persistenceManager.findInstance(persistenceSession,
                            "securityUserByName",
                            new Object[] { user.getName() });
                        throw new ValidationException(
                                new ValidationError(
                                        "user",
                                        Security.BUNDLE_PATH,
                                        mask.getField("name"),
                                        "errors.unique"
                                        ));
                    } catch (FinderException thatsGood) { // that's good!
                        persistenceManager.amend(persistenceSession, user);

                        // if the name changed, there are special
                        // considerations...
                        onAmendUserName(securitySession, persistenceSession,
                                user, oldUser.getName());
                    }
                } else {
                    persistenceManager.amend(persistenceSession, user);
                }
            }
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }
    /**
     * <p>Check a password is correct for a user.</p>
     *
     * @param userName
     * @param password
     * @throws InvalidFieldValueException if any of the parameters are
     *     <code>null</code>.
     * @throws InvalidFieldValueException if any of the parameters are
     *     <code>null</code>.
     */
    public void checkPassword(final SecuritySession securitySession,
            final String password)
            throws SystemException {
        if (demoVersion) {
            return;
        }
        UserDO user = securitySession.getUser();

        // check the password is correct
        securityServer.checkPassword(securitySession,
                user.getName(), password);
    }

    /**
     * <p>Enable/disable a user from logging into the system.</p>
     *
     * @param userName the name of the user who is changing the access of
     *     another user. <strong>This is the not the user to enable/disable!</strong>
     * @param userNameEnable the name of the user to enable or disable.
     * @param enable set to <code>true</code> if the user should be allowed to
     *     log into the system, otherwise <code>false</code>.
     * @throws InvalidFieldValueException if any of the parameters are
     *     <code>null</code>.
     */
    public void enableUser(final SecuritySession securitySession,
            final String userNameEnable,
            final boolean enable)
            throws SystemException {
        if (demoVersion) {
            return;
        }
        Mask mask = maskFactory.getMask(UserDO.class);
        // check we have field values
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            if (userNameEnable == null) {
                throw new ValidationException(
                        new ValidationError(
                                "user",
                                Security.BUNDLE_PATH,
                                mask.getField("name"),
                                "errors.required"));
            }

            // find the user
            UserDO user = (UserDO) persistenceManager.findInstance(persistenceSession,
                    "securityUserByName",
                    new Object[] { userNameEnable });
            user.setEnabled(enable);
            amendUser(securitySession, user);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }    }

    /**
     * Find a user given the user name.
     *
     * @param securitySession valid security session.
     * @param userName name of the user to find.
     */
    public UserDO findUserByName(final SecuritySession securitySession,
            final String userName)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            return (UserDO) persistenceManager.findInstance(persistenceSession,
                    "securityUserByName", new Object[] {userName});
        } catch (Exception e) {
            persistenceSession.cancel();
            if (e instanceof SystemException) {
                throw (SystemException) e;
            } else {
                throw new SystemException(e);
            }
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * <p>
     * Override this to provide a better string as the 'real' user name. This
     * is usually a name such as "Paul Smith". The default implementation just
     * returns the user name.
     * </p>
     *
     * @param persistenceSession Valid persistence session.
     * @param user The user for whom to return the name.
     * @return Real-life name for this user.
     * @throws SystemException
     */
    protected String getRealName(final PersistenceSession persistenceSession,
            final UserDO user)
            throws SystemException {
        return user.getName();
    }

    /**
     * <p>This method add prefix to username.</p>
     *
     * @param userName
     * @return prefix_userName
     */
    public final String getSystemUserName(final SecuritySession securitySession,
            final String userName)  {
        return securityServer.getSystemUserName(securitySession,
                userName);
    }

    /**
     * <p>This method is converts the system username back into a plain old
     * username, it's the opposite method to getSystemUserName.</p>
     */
    public final String getUserNameFromSystemUserName(
            final SecuritySession securitySession,
            final String systemUserName) {
        return securityServer.getUserNameFromSystemUserName(securitySession,
                systemUserName);
    }
    /**
     * @param userNameParam
     * @return
     * @throws SystemException
     */
    public boolean isUser(SecuritySession securitySession,
            String userNameParam) throws SystemException {
        return securityServer.isUser(securitySession,
                userNameParam);
    }

    /**
     * <p>Find out if a user is currently enabled
     * @param userName
     * @throws InvalidFieldValueException if <code>userName</code> is
     *     <code>null</code>.
     * @return <code>true</code> if the user is currently enabled, otherwise
     * <code>false</code>.
     */
    public boolean isUserEnabled(final SecuritySession securitySession,
            final String userName)
            throws SystemException {
        if (demoVersion) {
            return false;
        }
        assert (userName != null);

        // check we have field values
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {

            // find the user
            UserDO user = (UserDO) persistenceManager.findInstance(persistenceSession,
                    "securityUserByName",
                    new Object[] { userName });

            return user.isEnabled();
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * <p>Login to the system. This method confirms the user name and password
     * against system settings and logs the user into the mail server, if this
     * is the desired form of authentication.</p>
     *
     * @param user user to log into the remote system.
     * @param password the clear-text password to log into the remote system.
     * @throws EJBException if the person cannot log in.
     * @return the mail server used to access the mail system, or
     * <code>null</code> if another form of authentication is being used.
     */
    public SecuritySession login(final UserDO user,
            final String password) throws SystemException {
        SecuritySession session;
        session = securityServer.login(user, password);

        return session;
    }

    /**
     * <p>if userName emergency is trying login -> find first admin .</p>
     * @param userName
     * @return
     */
    public String loginAgain(final SecuritySession securitySession,
            final String userName)
            throws SystemException {
        return "guest";
    }

    /**
     * <p>Login as a guest to the system. </p>
     *
     * @throws EJBException if the person cannot log in.
     * @return the mail server used to access the mail system, or
     * <code>null</code> if another form of authentication is being used.
     */
    public SecuritySession loginGuest() throws SystemException {
        SecuritySession session;

        session = securityServer.loginGuest();

        return session;
    }

    /**
     * Called when the user name is changed, so it can be overridden to change
     * address book groups accordingly.
     * @param persistenceSession valid, open session for the current data store
     * transaction.
     * @param user user with the new name set.
     * @param oldName user name before the change.
     *
     * @throws SystemException if the name cannot be changed for any reason
     */
    protected void onAmendUserName(
            final SecuritySession securitySession,
            final PersistenceSession persistenceSession,
            final UserDO user,
            final String oldName)
            throws SystemException {
        if (demoVersion) {
            return;
        }
        Mask mask = maskFactory.getMask(UserDO.class);
        // if the user names have changed, remove the old user
        // from the system and add the new one
        // find the user
        // check the new username is not already taken
        if (securityServer.isUser(securitySession, user.getName())) {
            throw new ValidationException(
                    new ValidationError(
                            "user",
                            Security.BUNDLE_PATH,
                            mask.getField("name"),
                            "errors.unique"));
        }
        securityServer.removeUser(securitySession, oldName);
        securityServer.addUser(securitySession, user.getName(),
                getRealName(persistenceSession, user));
    }

    /**
     * <p>Remove a user from the system. The user is marked as logically deleted.</p>
     *
     * @param userName the name of the user who is doing the removing. <strong>This
     *     is not the name of the user to be removed!</strong>
     * @param userNameRemove the name of the user to be removed.
     * @see #enableUser
     * @throws InvalidFieldValueException if any of the parameters are
     *     <code>null</code>.
     */
    public void removeUser(final SecuritySession securitySession,
            final String userNameRemove)
            throws SystemException {
        if (demoVersion) {
            return;
        }
        // check we have field values
        Mask mask = maskFactory.getMask(UserDO.class);
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);

        try {
            if (userNameRemove == null) {
                throw new ValidationException(
                        new ValidationError(
                                "user",
                                Security.BUNDLE_PATH,
                                mask.getField("name"),
                                "errors.required"));
            }

            // find the user
            UserDO user = (UserDO) persistenceManager.findInstance(persistenceSession,
                    "securityUserByName",
                    new Object[] { userNameRemove });
            // now mark the entity as deleted
            user.setDeleted(true);
            amendUser(securitySession, user);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * <p>Restore a deleted user.</p>
     * @param userName who is doing this operation
     * @param restoreUserName who is going to be restored
     */
    public void restoreUser(final SecuritySession securitySession,
            final String restoreUserName) throws SystemException {
        if (demoVersion) {
            return;
        }
        assert (restoreUserName != null);
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            // find the user
            UserDO user = (UserDO) persistenceManager.findInstance(persistenceSession,
                "securityUserByName",
                new Object[] { restoreUserName });
            user.setDeleted(false);
            amendUser(securitySession, user);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

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
     */
    public final void setPassword(final SecuritySession securitySession,
            final String userNamePassword,
            final String password)
            throws SystemException {
        if (demoVersion) {
            return;
        }
        // check we have field values
        Mask mask = maskFactory.getMask(UserDO.class);
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            if (userNamePassword == null) {
                throw new ValidationException(
                        new ValidationError(
                                "user",
                                Security.BUNDLE_PATH,
                                mask.getField("name"),
                                "errors.required"));
            }

            // find the user
            UserDO user = (UserDO) persistenceManager.findInstance(persistenceSession,
                    "securityUserByName",
                    new Object[] { userNamePassword });

            // set password on mailserver
            securityServer.setPassword(securitySession,
                    user.getName(), password);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }
    /**
     * @return Returns the demoVersion.
     */
    protected boolean isDemoVersion() {
        return demoVersion;
    }
}
