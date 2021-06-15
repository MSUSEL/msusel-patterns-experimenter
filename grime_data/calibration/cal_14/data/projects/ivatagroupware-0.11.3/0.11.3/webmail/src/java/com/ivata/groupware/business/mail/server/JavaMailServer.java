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

import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.log4j.Logger;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import com.ivata.groupware.admin.security.server.PlainTextSecuritySession;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.groupware.business.addressbook.AddressBook;
import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.business.mail.session.MailSession;
import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.mask.util.SystemException;


/**
 * <p>Uses <strong>JavaMail</strong> methods to actually log into the mail server.</p>
 *
 * @since ivata groupware 0.10 (2005-02-21)
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 */
public abstract class JavaMailServer implements MailServer {
    private static Logger log = Logger.getLogger(JavaMailServer.class);
    private AddressBook addressBook;
    private Settings settings;
    public JavaMailServer(final AddressBook addressBookParam,
            final Settings settingsParam) {
        this.addressBook = addressBookParam;
        this.settings = settingsParam;
    }
    /**
     * <p>Check the password for a user. Throws {@link GroupwareException
     * GroupwareException} if the password is incorrect.</p>
     *
     * @param userName the user for whom to set/change the password.
     * @param password the password to check. If this password is
     * incorrect, an
     * <code>AuthenticationException</code> is thrown.
     */
    public void checkPassword(final SecuritySession securitySession,
            final String userName, final String password) throws SystemException {
        PicoContainer globalContainer = PicoContainerFactory.getInstance()
            .getGlobalContainer();
        MutablePicoContainer sessionContainer = new DefaultPicoContainer(globalContainer);
        SecuritySession guestSession = loginGuest();
        PersonDO person = addressBook.findPersonByUserName(guestSession, userName);
        UserDO user = person.getUser();
        MailSession mailSession = new MailSession(sessionContainer, user);
        user.setName(user.getName().toLowerCase());

        Properties mailProperties = new Properties();
        String hostName;

        try {
            mailProperties.setProperty("mail.host",
                (hostName = settings.getStringSetting(mailSession,
                    "emailHost", null)));
            mailProperties.setProperty("mail.smtp.host",
                settings.getStringSetting(mailSession,
                    "emailHostSmtp", null));
        } catch (Exception e) {
            throw new SystemException("ERROR: Exception in ScriptMailServer",
                e);
        }
        mailProperties.setProperty("mail.user",
                getSystemUserName(securitySession, user.getName()));

        if (user.getName().equals("emergency")) {
            mailProperties.setProperty("mail.from", "emergency@ivata.com");
        } else {
            mailProperties.setProperty("mail.from", person.getEmailAddress());
        }


        boolean isConnected = false;
        try {
            mailSession.login(password, mailProperties);
            isConnected = true;
        } catch (AuthenticationFailedException e) {
            throw new SystemException(e);
        } catch (NoSuchProviderException e) {
            throw new SystemException(
                "ERROR: "
                + e.getClass().getName()
                + " connecting using imap to "
                + mailProperties.getProperty("mail.host"),
                e);
        } catch (MessagingException e) {
            throw new SystemException(
                "ERROR: "
                + e.getClass().getName()
                + " connecting using imap to "
                + mailProperties.getProperty("mail.host"),
                e);
        } catch (Exception e) {
            throw new SystemException(
                "ERROR: "
                + e.getClass().getName()
                + " connecting using imap to "
                + mailProperties.getProperty("mail.host"),
                e);
        }
    }

    /**
     * <p>Helper. Get the store from the mail session and connect it.</p>
     */
    public Store connectStore(final MailSession mailSession)
            throws SystemException {
        try {
            Session javaMailSession;
            try {
                javaMailSession = mailSession.getJavaMailSession();
            } catch (java.security.NoSuchProviderException e1) {
                throw new SystemException(e1);
            }
            Store store = javaMailSession.getStore("imap");
            if (store == null) {
                throw new SystemException(
                    "ERROR in MailBean: could not access the mail store");
            }
            if (!store.isConnected()) {
                store.connect();
            }
            return store;
        } catch (MessagingException e) {
            throw new SystemException(e);
        }
    }
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
            throws SystemException {
        assert (securitySession != null);
        assert (store != null);
        assert (store.isConnected());
        assert (name != null);
        String prefix;
        if ("inbox".equalsIgnoreCase(name)) {
            prefix = "";
        } else {
            prefix = settings.getStringSetting(securitySession,
                    "emailFolderNamespace", securitySession.getUser());
        }
        String fullFolderName = prefix + name;
        try {
            return store.getFolder(fullFolderName);
        } catch (MessagingException e) {
            throw new SystemException(e);
        }
    }
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
     * @throws SystemException if the folder doesn't exists or there
     * is an application problem retrieving the modified time.
     */
    public boolean hasNewMessages(final SecuritySession securitySession,
            final String userName,
            final String folderName)
            throws SystemException {
        assert (securitySession instanceof MailSession);
        MailSession mailSession = (MailSession) securitySession;

        Store store = connectStore(mailSession);
        try {
            String sentFolderName = settings.getStringSetting(
                    mailSession,
                    "emailFolderSent",
                    mailSession.getUser());
            Folder folder = getFolder(mailSession, store, folderName);
            return folder.hasNewMessages();
        } catch(MessagingException e) {
            throw new SystemException(e);
        } finally {
            try {
                store.close();
            } catch(MessagingException e) {
                log.error("Closing store.", e);
            }
        }
    }

    /**
     * <p>Login to the mail server.</p>
     *
     * @see com.ivata.groupware.admin.security.session.SessionServer#login(String, String)
     */
    public SecuritySession login(final UserDO user,
            final String password)
            throws SystemException {
        PicoContainer globalContainer = PicoContainerFactory.getInstance()
            .getGlobalContainer();
        MutablePicoContainer sessionContainer = new DefaultPicoContainer(globalContainer);
        MailSession mailSession = new MailSession(sessionContainer, user);
        sessionContainer.registerComponentInstance(SecuritySession.class, mailSession);

        // all user names are lower case
        if (user == null) {
            throw new SystemException("ERROR in ScriptMailServer: user is null");
        }
        if (user.getName() == null) {
            throw new SystemException("ERROR in ScriptMailServer: user name is null");
        }
        user.setName(user.getName().toLowerCase());

        Properties mailProperties = new Properties();
        String hostName;

        try {
            mailProperties.setProperty("mail.host",
                (hostName = settings.getStringSetting(mailSession,
                    "emailHost", null)));
            mailProperties.setProperty("mail.smtp.host",
                settings.getStringSetting(mailSession,
                    "emailHostSmtp", null));
        } catch (Exception e) {
            throw new SystemException("ERROR: Exception in ScriptMailServer",
                e);
        }
        mailProperties.setProperty("mail.user",
                getSystemUserName(mailSession, user.getName()));

        if (user.getName().equals("emergency")) {
            mailProperties.setProperty("mail.from", "emergency@ivata.com");
        } else {
            // now check out this user's name and email address
            PersonDO person = addressBook.findPersonByUserName(mailSession,
                    user.getName());
            mailProperties.setProperty("mail.from", person.getEmailAddress());
        }


        boolean isConnected = false;
        try {
            try {
                mailSession.login(password, mailProperties);
                isConnected = true;
            } catch (AuthenticationFailedException e) {
                String logPassword = "**********";
                Boolean showPassword = settings.getBooleanSetting(mailSession,
                        "siteLoginDebugPassword", null);
                if ((showPassword != null)
                        && showPassword.booleanValue()) {
                    logPassword = password;
                }
                throw new SystemException("Warning: could not connect user '"
                    + mailProperties.getProperty("mail.user")
                    + ", password '"
                    + logPassword
                    + "' to server '"
                    + hostName
                    + "'",
                    e);
            }
        } catch (NoSuchProviderException e) {
            throw new SystemException(
                "ERROR: "
                + e.getClass().getName()
                + " connecting using imap to "
                + mailProperties.getProperty("mail.host"),
                e);
        } catch (MessagingException e) {
            throw new SystemException(
                "ERROR: "
                + e.getClass().getName()
                + " connecting using imap to "
                + mailProperties.getProperty("mail.host"),
                e);
        } catch (Exception e) {
            throw new SystemException(
                "ERROR: "
                + e.getClass().getName()
                + " connecting using imap to "
                + mailProperties.getProperty("mail.host"),
                e);
        }
        mailSession.setPassword(password);
        return mailSession;
    }

    /**
     * @see com.ivata.groupware.admin.security.server.SecurityServer#loginGuest()
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
}
