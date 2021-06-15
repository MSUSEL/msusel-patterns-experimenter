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
package org.apache.james.imapserver;

/**
 * Thrown on an inappropriate attempt to reference a mailbox.
 * Includes attempting to create a mailbox that already exists and attempting
 * to open a mailbox that does not exist.
 * If status is ALREADY_EXISTS_REMOTELY or IF_CREATED_REMOTE then field
 * remoteServer should be set to the url of the remote server, formatted for
 * Mailbox Referral.
 *
 * @version 0.1 on 14 Dec 2000
 */
public class MailboxException extends Exception {

    public final static String ALREADY_EXISTS_LOCALLY
        = "Already exists locally";
    public final static String ALREADY_EXISTS_REMOTELY
        = "Already exists remotely";
    public final static String IF_CREATED_LOCAL
        = "If created, mailbox would be local";
    public final static String IF_CREATED_REMOTE
        = "If created, mailbox would be remote";
    public final static String NOT_LOCAL
        = "Does not exist locally, no further information available";
    public final static String LOCAL_BUT_DELETED
        = "Was local but has been deleted.";

    protected String status = null; 
    protected String remoteServer = null;

    /**
     * Construct a new <code>MailboxException</code> instance.
     *
     * @param message The detail message for this exception (mandatory).
     */
    public MailboxException(String message) {
        super(message);
    }

    /**
     * Construct a new <code>MailBoxException</code> instance.
     *
     * @param message The detail message for this exception (mandatory).
     * @param aStatus String constant indicating condition
     */
    public MailboxException(String message, String aStatus) {
        super(message);
        this.status = aStatus;
    }

    /**
     * Construct a new <code>MailBoxException</code> instance.
     *
     * @param message The detail message for this exception (mandatory).
     * @param aStatus String constant indicating condition
     * @param sServer String indicating another server where Mailbox should be.
     */
    public MailboxException(String message, String aStatus, String aServer) {
        super(message);
        this.status = aStatus;
        this.remoteServer = aServer;
    }

    public String getStatus() {
        return status;
    }

    public String getRemoteServer() {
        return remoteServer;
    }

    public boolean isRemote() {
        return ( status.equals(ALREADY_EXISTS_REMOTELY)
                 || status.equals(IF_CREATED_REMOTE) ) ;
    }
}
