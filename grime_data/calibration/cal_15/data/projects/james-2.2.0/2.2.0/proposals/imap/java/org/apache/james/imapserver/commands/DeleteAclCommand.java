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
import org.apache.james.imapserver.ACLMailbox;
import org.apache.james.imapserver.ImapRequest;
import org.apache.james.imapserver.ImapSession;

class DeleteAclCommand extends AbstractAclCommand
{
    protected boolean checkUsage( int arguments, ImapSession session )
    {
        if ( arguments != 4 ) {
            session.badResponse( "Command should be <tag> <DELETEACL> <mailbox> <identity>" );
            return false;
        }
        return true;
    }

    protected void doAclCommand( ImapRequest request, ImapSession session,
                                 ACLMailbox target, String folder )
            throws AccessControlException, AuthorizationException
    {
        String command = request.getCommand();
        String identity = request.getCommandLine().nextToken();
        String changes = "";
            
        if ( target.setRights( session.getCurrentUser(), identity, changes ) ) {
            session.okResponse( command );
            session.getSecurityLogger().info( "ACL rights for " + identity + " in "
                                 + folder + " deleted by " + session.getCurrentUser() );
        }
        else {
            session.noResponse( command );
            session.getSecurityLogger().info( "Failed attempt to delete ACL rights for "
                                 + identity + " in " + folder + " by "
                                 + session.getCurrentUser() );
        }
    }
}
