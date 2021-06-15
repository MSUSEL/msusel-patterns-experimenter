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
package com.ivata.groupware.business.mail.server;

import java.util.Collection;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Store;

import com.ivata.groupware.admin.security.server.SecurityServer;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.mail.session.MailSession;
import com.ivata.mask.util.SystemException;


/**
 * <p>Extends the standard security server to add mail-specific features.</p>
 *
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 */
public interface MailServer extends SecurityServer {
    /**
     * <p>Helper. Get the store from the mail session and connect it.</p>
     */
    public Store connectStore(final MailSession mailSession)
            throws SystemException;
    /**
     * Wrapper for <code>Store.getFolder</code> to get around the fact that
     * courier/cyrus imap prefixes all "personal namespace" folder names with
     * "INBOX".
     *
     * @param securitySession used to access the settings.
     * @param store valid, connected store.
     * @param name name of the folder you want to open.
     * @return folder for the name you passed.
     * @throws SystemException if the settings cannot be accessed, or there is
     * any exception accessing the store.
     */
    public Folder getFolder(final SecuritySession securitySession,
            final Store store, final String name)
            throws SystemException;
    /**
     * <p>Adds or amends a mailing list with the name and users
     * provided.</p>
     *
     * <p>This method calls the <code>setList</code> script with the name
     * as the first parameter and the user names of each of the users as
     * following parameters.</p>
     *
     * @param name the name of the list to be added.
     * @param users <code>java.util.Collection</code> containing
     * <code>UserLocal</code> instances of each of the users in the list.
     * Note that if you are changing an existing list, this array should
     * contain all of the users, not just the new ones.
     * @throws BusinessException containing the content of the standard
     * error stream, if the script returns non-zero.
     */
    void setList(final SecuritySession securitySession,
            final String name,
            final Collection users)
        throws SystemException;

    /**
     * <p>Removes an existing mailing list from the system.</p>
     *
     * <p>The <code>removeList</code> script is called with
     * <code>name</code> as its only parameter.</p>
     *
     * @param name the name of the list to remove.
     * @throws BusinessException containing the content of the standard
     * error stream, if the script returns non-zero.
     */
    void removeList(final SecuritySession securitySession,
            final String name) throws SystemException;

    /**
     * <p>Gets all of the email aliases for the user provided.</p>
     *
     * <p>This method calls the script <code>getUserAliases</code>.</p>
     *
     * @param userName the user for whom to get the email aliases.
     * @return a <code>java.util.Collection</code> containing
     * <code>String</code> values for each of the aliases.
     * @throws BusinessException containing the content of the standard
     * error stream, if the script returns non-zero.
     */
    List getUserAliases(final SecuritySession securitySession,
            final String userName)
        throws SystemException;

    /**
     * <p>Gets all of the email aliases for the user provided.</p>
     *
     * <p>This method calls the script <code>setUserAliases</code>.</p>
     *
     * @param userName the user for whom to get the email aliases.
     * @param aliases a <code>java.util.Collection</code> containing
     * <code>String</code> values for each of the aliases.
     * @throws BusinessException containing the content of the standard
     * error stream, if the script returns non-zero.
     */
    void setUserAliases(final SecuritySession securitySession,
            final String userName,
            final Collection aliases)
        throws SystemException;

    /**
     * <p>Get the email addresss this user's mail is forwarded to.</p>
     *
     * <p>This method calls the script <code>getUserForwarding</code>.</p>
     *
     * @param userName the user for whom to activate/deactive email
     * forwarding.
     * @return email address all email for this user is forwarded to, or
     * <code>null</code> if there is no forwarding for this user.
     * @throws BusinessException containing the content of the standard
     * error stream, if the script returns non-zero.
     */
    String getUserForwarding(final SecuritySession securitySession,
            final String userName)
        throws SystemException;

    /**
     * <p>Set an email addresss to forward this user's mail to. If
     * <code>address</code> is set to <code>null</code> then any previous
     * email
     * forwarding is removed.
     *
     * @param userName the user for whom to activate/deactive email
     * forwarding.
     * @param address email address to forward all email for this user to.
     * If this
     * address is set to <code>null</code> then any previous forwarding is
     * removed.
     * @throws BusinessException containing the content of the standard
     * error
     * stream, if the script returns non-zero.
     *
     *
     */
    void setUserForwarding(final SecuritySession securitySession,
            final String userName,
            final String address)
        throws SystemException;

    /**
     * <p>Gets the current vacation method for the user provided. </p>
     *
     * <p>This method calls the script
     * <code>getVacationMessage</code>.</p>
     *
     * @param userName the user for whom to set the vacation message.
     * @return the current vacation message for this user, or
     * <code>null</code> if
     * the user does not have a vacation message.
     * @throws BusinessException containing the content of the standard
     * error stream, if the script returns non-zero.
     */
    String getVacationMessage(final SecuritySession securitySession,
            final String userName)
        throws SystemException;

    /**
     * <p>Sets the vacation method for the user provided. This message
     * will be sent
     * to all mails received at this address until it has been cleared, by
     * calling
     * this method again with a <code>null</code> value for the
     * <code>message</code>
     * parameter.</p>
     *
     * <p>This method calls the <code>setVacationMessage</code> script
     * with <code>user name</code> and <code>message</code> as parameters.
     * If the <code>message</code> parameter is <code>null</code>, then
     * the
     * script is called with just the
     * <code>user name</code> parameter.</p>
     *
     * @param userName the user for whom to set the vacation message.
     * @param message the new vacation message for this user. Set to
     * <code>null</code> to remove any existing vacation message.
     * @throws BusinessException containing the content of the standard
     * error stream, if the script returns non-zero.
     */
    void setVacationMessage(final SecuritySession securitySession,
            final String userName,
            final String message)
        throws SystemException;

    /**
     * <p>Get the time the specified mail folder was last modified as a
     * <code>long</code>. This can then be saved and compared to
     * subsequent
     * calls of this method to see if the folder has changed.</p>
     *
     * @param userName the name of the user for whom to locate the folder.
     * @param folderName the name of the folder to locate.
     * @return operating system specific timestamp indicating when the
     * folder was last changed.
     * @throws BusinessException if the folder doesn't exists or there
     * is an application problem retrieving the modified time.
     */
    boolean hasNewMessages(final SecuritySession securitySession,
            final String userName,
            final String folderName)
        throws SystemException;
}
