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

import org.apache.james.imapserver.AuthenticationException;
import org.apache.james.imapserver.ImapRequest;
import org.apache.james.imapserver.ImapSession;
import org.apache.james.imapserver.ImapSessionState;

import java.util.StringTokenizer;
import java.util.List;

class LoginCommand extends NonAuthenticatedStateCommand
{
    LoginCommand()
    {
        this.commandName = "LOGIN";

        this.getArgs().add( new AstringArgument( "username" ) );
        this.getArgs().add( new AstringArgument( "password" ) );
    }

    protected boolean doProcess( ImapRequest request, ImapSession session, List argValues )
    {
        String userName = (String) argValues.get(0);
        String password = (String) argValues.get(1);

        session.setCurrentUser( userName );
        if ( session.getUsers().test( session.getCurrentUser(), password ) ) {
            session.getSecurityLogger().info( "Login successful for " + session.getCurrentUser() + " from  "
                                 + session.getRemoteHost() + "(" + session.getRemoteIP() + ")" );
            // four possibilites handled:
            // private mail: isLocal, is Remote
            // other mail (shared, news, etc.) is Local, is Remote

            if ( session.getImapHost().isHomeServer( session.getCurrentUser() ) ) {
                session.okResponse( request.getCommand() );
                session.setState( ImapSessionState.AUTHENTICATED );

            }
            else {
                String remoteServer = null;
                try {
                    remoteServer
                            = session.getImapSystem().getHomeServer( session.getCurrentUser() );
                }
                catch ( AuthenticationException ae ) {
                    session.setConnectionClosed( session.closeConnection( TAGGED_NO,
                                               " cannot find your inbox, closing connection",
                                               "" ) );
                    return false;
                }

                if ( session.getImapHost().hasLocalAccess( session.getCurrentUser() ) ) {
                    session.okResponse( "[REFERRAL "
                                               + remoteServer + "]" + SP
                                               + "Your home server is remote, other mailboxes available here" );
                    session.setState( ImapSessionState.AUTHENTICATED );

                }
                else {
                    session.closeConnection( TAGGED_NO, " [REFERRAL" + SP
                                                + remoteServer + "]" + SP
                                                + "No mailboxes available here, try remote server", "" );
                    return false;
                }
            }
            session.setCurrentNamespace( session.getImapHost().getDefaultNamespace( session.getCurrentUser() ) );
            session.setCurrentSeperator( session.getImapSystem().getHierarchySeperator( session.getCurrentNamespace() ) );
            // position at root of default Namespace,
            // which is not actually a folder
            session.setCurrentFolder( session.getCurrentNamespace() + session.getCurrentSeperator() + "" );
            getLogger().debug( "Current folder for user " + session.getCurrentUser() + " from "
                               + session.getRemoteHost() + "(" + session.getRemoteIP() + ") is "
                               + session.getCurrentFolder() );
            return true;


        } // failed password test

        // We should add ability to monitor attempts to login
        session.noResponse( request.getCommand() );
        session.getSecurityLogger().error( "Failed attempt to use Login command for account "
                              + session.getCurrentUser() + " from " + session.getRemoteHost() + "(" + session.getRemoteIP()
                              + ")" );
        return true;
    }
}
