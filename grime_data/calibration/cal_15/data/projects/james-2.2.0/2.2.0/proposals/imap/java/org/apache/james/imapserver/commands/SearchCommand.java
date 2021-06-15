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
 * Implements the command for searching after Mails.
 *
 * @version 0.2 on 04 Aug 2002
 */

class SearchCommand extends AuthenticatedSelectedStateCommand
{
    public SearchCommand()
    {
        System.out.println("*SEARCH*: <init>");
        this.commandName = "SEARCH";

        this.getArgs().add( new AstringArgument( "search1" ) );
        this.getArgs().add( new AstringArgument( "search2" ) );
    }

    protected boolean doProcess( ImapRequest request, ImapSession session, List argValues )
    {
        String command = this.commandName;

        String search1 = (String) argValues.get( 0 );
        String search2 = (String) argValues.get( 1 );

        System.out.println("*SEARCH*: got arg1: "+ search1);
        System.out.println("*SEARCH*: got arg2: "+ search2);
        
        System.out.println("*SEARCH*: currentMailbox:"+request.getCurrentMailbox().getName());
        
        session.getOut().print( UNTAGGED + SP + command.toUpperCase());
        getLogger().debug( UNTAGGED + SP + command.toUpperCase());
        if (request.getCurrentMailbox().matchesName("inbox")) {
            session.getOut().print( SP + "1" );
            getLogger().debug( SP + "1"  );
        }
        session.getOut().println();

        session.okResponse( command );

        if ( session.getState() == ImapSessionState.SELECTED ) {
            session.checkSize();
            session.checkExpunge();
        }
        return true;
    }
}
