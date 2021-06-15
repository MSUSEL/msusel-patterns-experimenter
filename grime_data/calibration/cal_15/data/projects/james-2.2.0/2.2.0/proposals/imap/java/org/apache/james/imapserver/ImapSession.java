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

import org.apache.avalon.framework.logger.Logger;
import org.apache.james.imapserver.AccessControlException;
import org.apache.james.imapserver.AuthorizationException;
import org.apache.james.imapserver.commands.ImapCommand;
import org.apache.james.services.UsersRepository;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.List;

public interface ImapSession extends MailboxEventListener
{
    void okResponse( String command );

    void noResponse( String command );

    void noResponse( String command, String msg );

    void badResponse( String badMsg );

    void notImplementedResponse( String command );

    void taggedResponse( String msg );

    void untaggedResponse( String msg );

    ImapSessionState getState();

    void setState( ImapSessionState state );

    BufferedReader getIn();

    void setIn( BufferedReader in );

    PrintWriter getOut();

    void setOut( PrintWriter out );
    
    void setCanParseCommand(boolean canParseCommand);
    
    boolean getCanParseCommand();

    void checkSize();
    
    void checkExpunge();
    
    String getRemoteHost();
    
    String getRemoteIP();
    
    Logger getSecurityLogger();
    
    UsersRepository getUsers();
    
    Host getImapHost();
    
    IMAPSystem getImapSystem();
    
    String getCurrentNamespace();
    void setCurrentNamespace( String currentNamespace );
    String getCurrentSeperator();
    void setCurrentSeperator( String currentSeperator );
    String getCurrentFolder();
    void setCurrentFolder( String currentFolder );
    ACLMailbox getCurrentMailbox();
    void setCurrentMailbox( ACLMailbox currentMailbox );
    boolean isCurrentIsReadOnly();
    void setCurrentIsReadOnly( boolean currentIsReadOnly );
    boolean isConnectionClosed();
    void setConnectionClosed( boolean connectionClosed );
    String getCurrentUser();
    void setCurrentUser( String user );
    void setSequence( List sequence );

    ImapCommand getImapCommand( String command );
    
    boolean closeConnection(int exitStatus,
                                     String message1,
                                     String message2 );
    
    void logACE( AccessControlException ace );
    void logAZE( AuthorizationException aze );
    
//    ACLMailbox getBox( String user, String mailboxName );
    
    List decodeSet( String rawSet, int exists ) throws IllegalArgumentException;
}
