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

import org.apache.james.imapserver.*;
import org.apache.james.util.Assert;

import java.util.StringTokenizer;
import java.util.List;

/**
 * Implements the UID Command for calling Commands with the fixed UID
 *
 * @version 0.2 on 04 Aug 2002
 */
class UidCommand implements ImapCommand
{
    public boolean validForState( ImapSessionState state )
    {
        return ( state == ImapSessionState.SELECTED );
    }

    public boolean process( ImapRequest request, ImapSession session )
    {
       // StringTokenizer commandLine = new java.util.StringTokenizer(request.getCommandRaw());
        StringTokenizer commandLine = request.getCommandLine();
        int arguments = commandLine.countTokens();
       // StringTokenizer commandLine = request.getCommandLine();
        String command = request.getCommand();

        StringTokenizer txt = new java.util.StringTokenizer(request.getCommandRaw());
        System.out.println("UidCommand.process: #args="+txt.countTokens());
        while (txt.hasMoreTokens()) {
            System.out.println("UidCommand.process: arg='"+txt.nextToken()+"'");
        }
        if ( arguments < 3 ) {
            session.badResponse( "rawcommand='"+request.getCommandRaw()+"' #args="+request.arguments()+" Command should be <tag> <UID> <command> <command parameters>" );
            return true;
        }
        String uidCommand = commandLine.nextToken();
        System.out.println("UidCommand.uidCommand="+uidCommand);
        System.out.println("UidCommand.session="+session.getClass().getName());
        ImapCommand cmd = session.getImapCommand( uidCommand );
        System.out.println("UidCommand.cmd="+cmd);
        System.out.println("UidCommand.cmd="+cmd.getClass().getName());
        if ( cmd instanceof CommandFetch || cmd instanceof CommandStore  || cmd instanceof CopyCommand) {
            // As in RFC2060 also the COPY Command is valid for UID Command
            request.setCommand( uidCommand );
            ((ImapRequestImpl)request).setUseUIDs( true );
            cmd.process( request, session );
        } else {
            session.badResponse( "Invalid UID secondary command." );
        }
        return true;
    }
}
