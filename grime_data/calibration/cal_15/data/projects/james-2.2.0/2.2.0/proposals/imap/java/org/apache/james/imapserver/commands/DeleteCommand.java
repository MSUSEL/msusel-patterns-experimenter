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

import org.apache.james.imapserver.AccessControlException;
import org.apache.james.imapserver.AuthorizationException;
import org.apache.james.imapserver.ImapRequest;
import org.apache.james.imapserver.ImapSession;
import org.apache.james.imapserver.ImapSessionState;
import org.apache.james.imapserver.MailboxException;

import java.util.StringTokenizer;
import java.util.List;

class DeleteCommand extends AuthenticatedSelectedStateCommand
{
    public DeleteCommand()
    {
        this.commandName = "DELETE";

        this.getArgs().add( new AstringArgument( "mailbox" ) );
    }

    public boolean doProcess( ImapRequest request, ImapSession session, List argValues )
    {
        String command = this.getCommand();

        String folder = (String) argValues.get( 0 );

        if ( session.getCurrentFolder().equals( folder ) ) {
            session.noResponse( command, "You can't delete a folder while you have it selected." );
            return true;
        }
        try {
            if ( session.getImapHost().deleteMailbox( session.getCurrentUser(), folder ) ) {
                session.okResponse( command );
            }
            else {
                session.noResponse( command );
                getLogger().info( "Attempt to delete mailbox " + folder
                                  + " by user " + session.getCurrentUser() + " failed." );
            }
            if ( session.getState() == ImapSessionState.SELECTED ) {
                session.checkSize();
                session.checkExpunge();
            }
            return true;
        }
        catch ( MailboxException mbe ) {
            session.noResponse( command, mbe.getMessage() );
            return true;
        }
        catch ( AuthorizationException aze ) {
            session.noResponse( command, "You do not have the rights to delete mailbox: " + folder );
            session.logAZE( aze );
            return true;
        }
        catch ( AccessControlException ace ) {
            session.noResponse( command, ace.getMessage() );
            session.logACE( ace );
            return true;
        }
    }
}
