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
package org.apache.james.imapserver.commands;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.james.imapserver.AuthorizationException;
import org.apache.james.imapserver.ImapConstants;
import org.apache.james.imapserver.store.ImapMailbox;
import org.apache.james.imapserver.ImapRequestLineReader;
import org.apache.james.imapserver.ImapResponse;
import org.apache.james.imapserver.ImapSession;
import org.apache.james.imapserver.ImapSessionState;
import org.apache.james.imapserver.store.MailboxException;
import org.apache.james.imapserver.ProtocolException;

/**
 * Base class for all command implementations. This class provides common
 * core functionality useful for all {@link org.apache.james.imapserver.commands.ImapCommand} implementations.
 *
 *
 * @version $Revision: 1.4.2.3 $
 */
abstract class CommandTemplate
        extends AbstractLogEnabled
        implements ImapCommand, ImapConstants
{
    protected CommandParser parser = new CommandParser();

    /**
     * By default, valid in any state (unless overridden by subclass.
     * @see org.apache.james.imapserver.commands.ImapCommand#validForState
     */
    public boolean validForState( ImapSessionState state )
    {
        return true;
    }

    /**
     * Template methods for handling command processing. This method reads
     * argument values (validating them), and checks the request for correctness.
     * If correct, the command processing is delegated to the specific command
     * implemenation.
     *
     * @see ImapCommand#process
     */
    public void process( ImapRequestLineReader request,
                         ImapResponse response,
                         ImapSession session )
    {
        try {
            doProcess( request, response, session );
        }
        catch ( MailboxException e ) {
            response.commandFailed( this, e.getResponseCode(), e.getMessage() );
        }
        catch ( AuthorizationException e ) {
            String msg = "Authorization error: Lacking permissions to perform requested operation.";
            response.commandFailed( this, msg );
        }
        catch ( ProtocolException e ) {
            String msg = e.getMessage() + " Command should be '" +
                    getExpectedMessage() + "'";
            response.commandError( msg );
        }
    }

    /**
     * This is the method overridden by specific command implementations to
     * perform commend-specific processing.
     *
     * @param request The client request
     * @param response The server response
     * @param session The current client session
     */
    protected abstract void doProcess( ImapRequestLineReader request,
                                       ImapResponse response,
                                       ImapSession session )
            throws ProtocolException, MailboxException, AuthorizationException;

    /**
     * Provides a message which describes the expected format and arguments
     * for this command. This is used to provide user feedback when a command
     * request is malformed.
     *
     * @return A message describing the command protocol format.
     */
    protected String getExpectedMessage()
    {
        StringBuffer syntax = new StringBuffer( "<tag> " );
        syntax.append( getName() );

        String args = getArgSyntax();
        if ( args != null && args.length() > 0 ) {
            syntax.append( " " );
            syntax.append( args );
        }

        return syntax.toString();
    }

    /**
     * Provides the syntax for the command arguments if any. This value is used
     * to provide user feedback in the case of a malformed request.
     *
     * For commands which do not allow any arguments, <code>null</code> should
     * be returned.
     *
     * @return The syntax for the command arguments, or <code>null</code> for
     *         commands without arguments.
     */
    protected abstract String getArgSyntax();

    protected ImapMailbox getMailbox( String mailboxName,
                                      ImapSession session,
                                      boolean mustExist )
            throws MailboxException
    {
        return session.getHost().getMailbox( session.getUser(), mailboxName, mustExist );
    }

    protected ImapMailbox getMailbox( String mailboxName,
                                      ImapSession session )
    {
        try {
            return session.getHost().getMailbox( session.getUser(), mailboxName, false );
        }
        catch ( MailboxException e ) {
            throw new RuntimeException( "Unexpected error in CommandTemplate.java" );
        }
    }

    public CommandParser getParser()
    {
        return parser;
    }
}
