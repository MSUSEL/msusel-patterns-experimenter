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

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.mask.util.SystemException;

/**
 * <p>The security bean is responsible for adding, removing and amending users
 * to the system, and for logging in in the first place.</p>
 *
 * @since 2002-09-08
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 *
 * @ejb.bean
 *      name="Security"
 *      display-name="Security"
 *      type="Stateless"
 *      view-type="remote"
 *      jndi-name="SecurityRemote"
 *
 * @ejb.transaction
 *      type = "Required"
 *
 *  @ejb.home
 *      generate="false"
 *      remote-class="com.ivata.groupware.admin.security.SecurityRemoteHome"
 *
 *  @ejb.interface
 *      remote-class="com.ivata.groupware.admin.security.SecurityRemote"
 */
public class SecurityBean implements SessionBean {
    /**
     * <p>Provides the session bean with container-specific information.</p>
     */
    SessionContext sessionContext;

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
    public void checkPassword(final SecuritySession securitySession,
            final String password) throws SystemException {
        getSecurity().checkPassword(securitySession, password);
    }

    /**
     * <p>Called by the container to notify an entity object it has been
     * activated.</p>
     */
    public void ejbActivate() {}

    /**
     * <p>Called by the container just after the bean has been created.</p>
     *
     * @exception CreateException if any error occurs. Never thrown by this
     *     class.
     *
     * @ejb.create-method
     */
    public void ejbCreate() throws CreateException {}

    /**
     * <p>Called by the container to notify the entity object it will be
     * deactivated. Called just before deactivation.</p>
     */
    public void ejbPassivate() {}

    /**
     * <p>This method is called by the container when the bean is about
     * to be removed.</p>
     *
     * <p>This method will be called after a client calls the <code>remove</code>
     * method of the remote/local home interface.</p>
     *
     * @throws RemoveException if any error occurs. Currently never thrown by
     *     this class.
     */
    public void ejbRemove() {}

    /**
     * Find a user given the user name.
     *
     * @param securitySession valid security session.
     * @param userName name of the user to find.
     */
    public UserDO findUserByName(final SecuritySession securitySession,
            final String userName)
            throws SystemException {
        return getSecurity().findUserByName(securitySession, userName);
    }

    /**
     * Get the security implementation.
     *
     * @return valid security implementation.
     */
    private Security getSecurity() throws SystemException {
        PicoContainer container = PicoContainerFactory.getInstance()
            .getGlobalContainer();
        return (Security) container.getComponentInstance(Security.class);
    }

    /**
     * <p>This method add prefix to username.</p>
     *
     * @param userName
     * @return prefix_userName
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public final String getSystemUserName(final SecuritySession securitySession,
            final String userName)
            throws SystemException {
        return getSecurity().getSystemUserName(securitySession, userName);
    }

    /**
     * <p>This method is converting SystemUserName to userName, it's oposite method to getSystemUserName.</p>
     * @param systemUserName
     * @return
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public final String getUserNameFromSystemUserName(
            final SecuritySession securitySession,
            final String systemUserName)
            throws SystemException {
        return getSecurity().getUserNameFromSystemUserName(securitySession,
                systemUserName);
    }

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
    public boolean isUserEnabled(final SecuritySession securitySession,
            final String userName)
            throws SystemException {
        return getSecurity().isUserEnabled(securitySession, userName);
    }

    /**
     * <p>Login to the system. This method confirms the user name and password
     * against system settings and logs the user into the mail server, if this
     * is the desired form of authentication.</p>
     *
     * @param userName this user name is used to log into the remote system.
     * @param password the clear-text password to log into the remote system.
     * @throws EJBException if the person cannot log in.
     * @return the mail server used to access the mail system, or
     * <code>null</code> if another form of authentication is being used.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public SecuritySession login(final UserDO user,
            final String password)
            throws SystemException {
        return getSecurity().login(user, password);
    }

    /**
     * <p>if userName briezky is trying login -> find first admin .</p>
     * @param userName
     * @return
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public String loginAgain(final SecuritySession securitySession,
            final String userName)
            throws SystemException {
        return getSecurity().loginAgain(securitySession, userName);
    }

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
    public void removeUser(final SecuritySession securitySession,
            final String userNameRemove) throws SystemException {
        getSecurity().removeUser(securitySession, userNameRemove);
    }

    /**
     * <p>Restore user is he was delted.</p>
     * @param userName who is doing this operation
     * @param restoreUserName who is going to be restored
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public void restoreUser(final SecuritySession securitySession,
            final String restoreUserName)
            throws SystemException {
        getSecurity().restoreUser(securitySession, restoreUserName);
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
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public final void setPassword(final SecuritySession securitySession,
            final String userNamePassword,
            final String password) throws SystemException {
        getSecurity().setPassword(securitySession, userNamePassword, password);
    }

    /**
     * <p>Set up the context for this entity object. The session bean stores the
     * context for later use.</p>
     *
     * @param sessionContext the new context which the session object should
     *     store.
     */
    public final void setSessionContext(final SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }
}
