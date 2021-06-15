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

import org.apache.james.imapserver.AuthorizationException;
import org.apache.james.imapserver.ImapRequestLineReader;
import org.apache.james.imapserver.ImapResponse;
import org.apache.james.imapserver.ImapSession;
import org.apache.james.imapserver.store.MailboxException;
import org.apache.james.imapserver.ProtocolException;

/**
 * Handles processeing for the DELETE imap command.
 *
 *
 * @version $Revision: 1.3.2.3 $
 */
class DeleteCommand extends AuthenticatedStateCommand
{
    public static final String NAME = "DELETE";
    public static final String ARGS = "<mailbox>";

    /** @see CommandTemplate#doProcess */
    protected void doProcess( ImapRequestLineReader request,
                              ImapResponse response,
                              ImapSession session )
            throws ProtocolException, MailboxException, AuthorizationException
    {

        String mailboxName = parser.mailbox( request );
        parser.endLine( request );

        session.getHost().deleteMailbox( session.getUser(), mailboxName );

        session.unsolicitedResponses( response );
        response.commandComplete( this );
    }

    /** @see ImapCommand#getName */
    public String getName()
    {
        return NAME;
    }

    /** @see CommandTemplate#getArgSyntax */
    public String getArgSyntax()
    {
        return ARGS;
    }
}

/*
6.3.4.  DELETE Command

   Arguments:  mailbox name

   Responses:  no specific responses for this command

   Result:     OK - delete completed
               NO - delete failure: can't delete mailbox with that name
               BAD - command unknown or arguments invalid

      The DELETE command permanently removes the mailbox with the given
      name.  A tagged OK response is returned only if the mailbox has
      been deleted.  It is an error to attempt to delete INBOX or a
      mailbox name that does not exist.

      The DELETE command MUST NOT remove inferior hierarchical names.
      For example, if a mailbox "foo" has an inferior "foo.bar"
      (assuming "." is the hierarchy delimiter character), removing
      "foo" MUST NOT remove "foo.bar".  It is an error to attempt to
      delete a name that has inferior hierarchical names and also has
      the \Noselect mailbox name attribute (see the description of the
      LIST response for more details).

      It is permitted to delete a name that has inferior hierarchical
      names and does not have the \Noselect mailbox name attribute.  In
      this case, all messages in that mailbox are removed, and the name
      will acquire the \Noselect mailbox name attribute.

      The value of the highest-used unique identifier of the deleted
      mailbox MUST be preserved so that a new mailbox created with the
      same name will not reuse the identifiers of the former
      incarnation, UNLESS the new incarnation has a different unique
      identifier validity value.  See the description of the UID command
      for more detail.


   Examples:   C: A682 LIST "" *
               S: * LIST () "/" blurdybloop
               S: * LIST (\Noselect) "/" foo
               S: * LIST () "/" foo/bar
               S: A682 OK LIST completed
               C: A683 DELETE blurdybloop
               S: A683 OK DELETE completed
               C: A684 DELETE foo
               S: A684 NO Name "foo" has inferior hierarchical names
               C: A685 DELETE foo/bar
               S: A685 OK DELETE Completed
               C: A686 LIST "" *
               S: * LIST (\Noselect) "/" foo
               S: A686 OK LIST completed
               C: A687 DELETE foo
               S: A687 OK DELETE Completed


               C: A82 LIST "" *
               S: * LIST () "." blurdybloop
               S: * LIST () "." foo
               S: * LIST () "." foo.bar
               S: A82 OK LIST completed
               C: A83 DELETE blurdybloop
               S: A83 OK DELETE completed
               C: A84 DELETE foo
               S: A84 OK DELETE Completed
               C: A85 LIST "" *
               S: * LIST () "." foo.bar
               S: A85 OK LIST completed
               C: A86 LIST "" %
               S: * LIST (\Noselect) "." foo
               S: A86 OK LIST completed
*/
