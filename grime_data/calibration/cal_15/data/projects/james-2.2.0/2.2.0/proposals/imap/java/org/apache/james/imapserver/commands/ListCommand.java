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
import org.apache.james.imapserver.ImapRequest;
import org.apache.james.imapserver.ImapSession;
import org.apache.james.imapserver.ImapSessionState;
import org.apache.james.imapserver.MailboxException;

import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.List;

/**
 * List Command for listing some mailboxes. 
 * See RFC 2060 for more details.
 *
 * @version 0.2 on 04 Aug 2002
 */

class ListCommand extends AuthenticatedSelectedStateCommand
{
    public ListCommand()
    {
        this.commandName = "LIST";

        this.getArgs().add( new AstringArgument( "reference name" ) );
        this.getArgs().add( new AstringArgument( "mailbox" ) );
    }

    protected boolean doProcess( ImapRequest request, ImapSession session, List argValues )
    {
        String command = this.commandName;

        boolean subscribeOnly;
        if ( command.equalsIgnoreCase( "LIST" ) ) {
            subscribeOnly = false;
        }
        else {
            subscribeOnly = true;
        }

        String reference = (String) argValues.get( 0 );
        String folder = (String) argValues.get( 1 );

        Collection list = null;
        try {
        System.out.println("getImapHost: "+session.getImapHost().getClass().getName());
            list = session.getImapHost().listMailboxes( session.getCurrentUser(), reference, folder,
                                                        subscribeOnly );
            if ( list == null ) {
                session.noResponse( command, " unable to interpret mailbox" );
            }
            else if ( list.size() == 0 ) {
                getLogger().debug( "List request matches zero mailboxes: " + request.getCommandRaw() );
                session.okResponse( command );
            }
            else {
                Iterator it = list.iterator();
                while ( it.hasNext() ) {
                    String listResponse = (String) it.next();
                    session.getOut().println( UNTAGGED + SP + command.toUpperCase()
                                               + SP + listResponse );
                    getLogger().debug( UNTAGGED + SP + command.toUpperCase()
                                       + SP + listResponse );
                }
                session.okResponse( command );
            }
        }
        catch ( MailboxException mbe ) {
            if ( mbe.isRemote() ) {
                session.noResponse( command, "[REFERRAL "
                                           + mbe.getRemoteServer() + "]"
                                           + SP + "Wrong server. Try remote." );
            }
            else {
                session.noResponse( command, "No such mailbox" );
            }
            return true;
        }
        catch ( AccessControlException ace ) {
            session.noResponse( command, "No such mailbox" );
            session.logACE( ace );
            return true;
        }

        if ( session.getState() == ImapSessionState.SELECTED ) {
            session.checkSize();
            session.checkExpunge();
        }
        return true;
    }
}
