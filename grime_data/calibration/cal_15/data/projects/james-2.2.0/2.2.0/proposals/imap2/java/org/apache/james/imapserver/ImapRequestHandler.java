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

import org.apache.james.imapserver.commands.ImapCommandFactory;
import org.apache.james.imapserver.commands.CommandParser;
import org.apache.james.imapserver.commands.ImapCommand;

import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 *
 * @version $Revision: 1.2.2.3 $
 */
public final class ImapRequestHandler
{
    private ImapCommandFactory imapCommands = new ImapCommandFactory();
    private CommandParser parser = new CommandParser();
    private static final String REQUEST_SYNTAX = "Protocol Error: Was expecting <tag SPACE command [arguments]>";

    /**
     * This method parses POP3 commands read off the wire in handleConnection.
     * Actual processing of the command (possibly including additional back and
     * forth communication with the client) is delegated to one of a number of
     * command specific handler methods.  The primary purpose of this method is
     * to parse the raw command string to determine exactly which handler should
     * be called.  It returns true if expecting additional commands, false otherwise.
     *
     * @return whether additional commands are expected.
     */
    public boolean handleRequest( InputStream input,
                                  OutputStream output,
                                  ImapSession session )
            throws ProtocolException
    {
        ImapRequestLineReader request = new ImapRequestLineReader( input, output );
        try {
            request.nextChar();
        }
        catch ( ProtocolException e ) {
            return false;
        }

        ImapResponse response = new ImapResponse( output );

        doProcessRequest( request, response, session );

        // Consume the rest of the line, throwing away any extras. This allows us
        // to clean up after a protocol error.
        request.consumeLine();

        return true;
    }

    private void doProcessRequest( ImapRequestLineReader request,
                                   ImapResponse response,
                                   ImapSession session)
    {
        String tag = null;
        String commandName = null;

        try {
            tag = parser.tag( request );
        }
        catch ( ProtocolException e ) {
            response.badResponse( REQUEST_SYNTAX );
            return;
        }

//        System.out.println( "Got <tag>: " + tag );
        response.setTag( tag );
        try {
            commandName = parser.atom( request );
        }
        catch ( ProtocolException e ) {
            response.commandError( REQUEST_SYNTAX );
            return;
        }

//        System.out.println( "Got <command>: " + commandName );
        ImapCommand command = imapCommands.getCommand( commandName );
        if ( command == null )
        {
            response.commandError( "Invalid command.");
            return;
        }

        if ( !command.validForState( session.getState() ) ) {
            response.commandFailed( command, "Command not valid in this state" );
            return;
        }

        command.process( request, response, session );
    }


}
