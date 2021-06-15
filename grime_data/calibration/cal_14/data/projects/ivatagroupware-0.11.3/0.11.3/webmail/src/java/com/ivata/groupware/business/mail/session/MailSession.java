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
package com.ivata.groupware.business.mail.session;

import java.security.NoSuchProviderException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.naming.AuthenticationException;

import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.server.AbstractSecuritySession;
import com.ivata.groupware.admin.security.server.SecurityServerException;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.mask.util.SystemException;


/**
 * <p><code>javax.mail.Session</code> is not serializable and cannot
 * be passed from client to server. This class stores the login details
 * an provides a new session instance when required, by logging in
 * again.</p>
 *
 * @since 2002-09-08
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 */
public class MailSession extends AbstractSecuritySession {
    /**
     * <p>Contains all of the properties necessary to log into the
     * <code>javax.mail.Session</code>.</p>
     */
    private Properties mailProperties = null;

    /**
     * <p>Used to autheticate this mail session, whenever the
     * <code>javax.mail.Session</code> is recreated.</p>
     */
    public MailAuthenticator authenticator;

    /**
     * Construct a new mail session.
     */
    public MailSession(PicoContainer container, UserDO user)
            throws SystemException {
        super(container, user);
    }

    /**
     * <p>Logs into the mail session for the first time. This stores the
     * user name and password so that the session can be continued again
     * later.</p>
     *
     * @userName name of the user to log in.
     * @param password clear-text user's password to log into the mail
     * server.
     * @param mailProperties all of the mail properties necessary to login
     * to the system.
     * @throws AuthenticationException thrown by JavaMail if the user cannot
     *      login.
     * @return newly created mail session.
     */
    public Session login(final String password,
            final Properties mailProperties)
        throws SecurityServerException, NoSuchProviderException,
            MessagingException {
        authenticator = new MailAuthenticator(
                mailProperties.getProperty("mail.user"), password);
        this.mailProperties = mailProperties;

        return getJavaMailSession();
    }

    /**
     * <p>Return the current mail session as a
     * <code>javax.mail.Session</code> instance. This involves logging in
     * again.</p>
     *
     * @throws AuthenticationException thrown by JavaMail if the user cannot login.
     * @throws NoSuchProviderException thrown by JavaMail if the user cannot login.
     * @throws MessagingException thrown by JavaMail if the user cannot login.
     * @return the current mail session as a
     * <code>javax.mail.Session</code> instance.
     */
    public Session getJavaMailSession()
        throws SecurityServerException, NoSuchProviderException,
            MessagingException {
        Session javaSession = Session.getInstance(mailProperties, authenticator);
        Store store = javaSession.getStore("imap");
        if (!store.isConnected()) {
            store.connect();
        }
        store.close();
        return javaSession;
    }
}
