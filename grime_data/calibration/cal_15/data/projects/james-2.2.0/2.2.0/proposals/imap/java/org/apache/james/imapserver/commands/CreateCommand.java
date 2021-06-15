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
import org.apache.james.imapserver.*;

import java.util.StringTokenizer;
import java.util.List;

/**
 * Create Command for creating some new mailboxes.
 *
 * @version 0.2 on 04 Aug 2002
 */

class CreateCommand extends AuthenticatedSelectedStateCommand
{
    public CreateCommand()
    {
        this.commandName = "CREATE";
        this.getArgs().add( new AstringArgument( "mailbox" ) );
    }

    protected boolean doProcess( ImapRequest request, ImapSession session, List argValues )
    {
        String command = this.commandName;
        String folder = (String) argValues.get( 0 );

        try {
            if ( session.getCurrentFolder() == folder ) {
                session.noResponse( command, "Folder exists and is selected." );
                return true;
            }
            System.out.println("CreteCommand FOLDERNAME: "+folder);
            ACLMailbox target = session.getImapHost().createMailbox( session.getCurrentUser(), folder );
            session.okResponse( command );
            session.getImapHost().releaseMailbox( session.getCurrentUser(), target );
        }
        catch ( AccessControlException ace ) {
            session.noResponse( command, "No such mailbox." );
            session.logACE( ace );
            return true;
        }
        catch ( MailboxException mbe ) {
            if ( mbe.isRemote() ) {
                session.noResponse( "[REFERRAL "
                            + mbe.getRemoteServer() + "]"
                            + SP + "Wrong server. Try remote." );
            }
            else {
                session.noResponse( mbe.getStatus() );
            }
            return true;
        }
        catch ( AuthorizationException aze ) {
            session.noResponse( command, "You do not have the rights to create mailbox: "
                                 + folder );
            return true;
        }
        if ( session.getState() == ImapSessionState.SELECTED ) {
            session.checkSize();
            session.checkExpunge();
        }
        return true;
    }
}
