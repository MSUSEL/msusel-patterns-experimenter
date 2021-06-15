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

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.logger.Logger;
import org.apache.james.services.User;
import org.apache.james.services.UsersRepository;
import org.apache.james.imapserver.store.ImapMailbox;

/**
 *
 *
 * @version $Revision: 1.1.2.3 $
 */
public final class ImapSessionImpl implements ImapSession
{
    private ImapSessionState state = ImapSessionState.NON_AUTHENTICATED;
    private User user = null;
    private ImapMailbox selectedMailbox = null;
    // TODO: Use a session-specific wrapper for user's view of mailbox
    // this wrapper would provide access control and track if the mailbox
    // is opened read-only.
    private boolean selectedIsReadOnly = false;

    private String clientHostName;
    private String clientAddress;

    // TODO these shouldn't be in here - they can be provided directly to command components.
    private ImapHandler handler;
    private ImapHost imapHost;
    private UsersRepository users;

    public ImapSessionImpl( ImapHost imapHost,
                            UsersRepository users,
                            ImapHandler handler,
                            String clientHostName,
                            String clientAddress )
    {
        this.imapHost = imapHost;
        this.users = users;
        this.handler = handler;
        this.clientHostName = clientHostName;
        this.clientAddress = clientAddress;
    }

    public ImapHost getHost()
    {
        return imapHost;
    }

    public void unsolicitedResponses( ImapResponse request )
    {
    }

    public void closeConnection()
    {
        handler.resetHandler();
    }

    public UsersRepository getUsers()
    {
        return users;
    }

    public String getClientHostname()
    {
        return clientHostName;
    }

    public String getClientIP()
    {
        return clientAddress;
    }

    public void setAuthenticated( User user )
    {
        this.state = ImapSessionState.AUTHENTICATED;
        this.user = user;
    }

    public User getUser()
    {
        return this.user;
    }

    public void deselect()
    {
        this.state = ImapSessionState.AUTHENTICATED;
    }

    public void setSelected( ImapMailbox mailbox, boolean readOnly )
    {
        this.state = ImapSessionState.SELECTED;
        this.selectedMailbox = mailbox;
        this.selectedIsReadOnly = readOnly;
    }

    public ImapMailbox getSelected()
    {
        return this.selectedMailbox;
    }

    public boolean selectedIsReadOnly()
    {
        return this.selectedIsReadOnly;
    }

    public ImapSessionState getState()
    {
        return this.state;
    }
}
