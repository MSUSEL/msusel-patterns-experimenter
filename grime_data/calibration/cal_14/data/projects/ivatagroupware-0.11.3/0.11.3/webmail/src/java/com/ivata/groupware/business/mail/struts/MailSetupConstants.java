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
package com.ivata.groupware.business.mail.struts;

/**
 * Utility class to store literals we need for setup (in
 * <code>SetupAction</code> and <code>SetupForm</code>).
 *
 * @since ivata groupware 0.11 (2005-04-07)
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.1 $
 */
public final class MailSetupConstants {
    /**
     * Email folder namespace to use for Courier.
     */
    public static final String COURIER_FOLDER_NAMESPACE = "INBOX.";
    /**
     * This string in the output of port 143 identifies Courier.
     */
    public static final String COURIER_SIGNATURE = "Courier-IMAP";
    /**
     * Email folder namespace to use for Cyrus.
     */
    public static final String CYRUS_FOLDER_NAMESPACE = "INBOX.";
    /**
     * This string in the output of port 143 identifies Cyrus.
     */
    public static final String CYRUS_SIGNATURE = "Cyrus IMAP";
    /**
     * Mail domain to give you the right idea.
     */
    public static final String DEFAULT_MAIL_DOMAIN = "mycompany.com";
    /**
     * Default path for scripts.
     */
    public static final String DEFAULT_SCRIPTS_PATH = "/usr/local/ivatagroupware";
    /**
     * IMAP port - for receiving mails.
     */
    public static final int IMAP_PORT = 143;
    /**
     * This is the path of the <strong>EXIM</strong> scripts we use by default,
     * relative to the <strong>ivata groupware</strong> scripts' path.
     */
    public static final String SCRIPT_PATH_EXIM = "/mailserver/exim";
    /**
     * This is the path of the <strong>SUDO</strong> scripts we use by default,
     * relative to the <strong>ivata groupware</strong> scripts' path.
     */
    public static final String SCRIPT_PATH_SUDO = "/mailserver/sudo";
    /**
     * IMAP port - for sending mails.
     */
    public static final int SMTP_PORT = 25;
    /**
     * Interval to wait before re-checking <code>available</code> on a socket
     * input stream. The process will wait a total of
     * <code>SOCKET_WAIT_NUMBER</code> times  <code>SOCKET_WAIT_INTERVAL</code>
     * milliseconds before giving up on any more socket output.
     */
    public static final long SOCKET_WAIT_INTERVAL = 50;
    /**
     * Number of times to wait before deciding a socket input stream has no more
     * information. The process will wait a total of
     * <code>SOCKET_WAIT_NUMBER</code> times  <code>SOCKET_WAIT_INTERVAL</code>
     * milliseconds before giving up on any more socket output.
     */
    public static final int SOCKET_WAIT_NUMBER = 20;
    /**
     * Private default constructor enforces utility class behavior.
     */
    private MailSetupConstants() {
    }
}
