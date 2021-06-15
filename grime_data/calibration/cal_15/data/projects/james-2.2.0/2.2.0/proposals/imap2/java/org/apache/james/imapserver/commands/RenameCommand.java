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
 * Handles processeing for the RENAME imap command.
 *
 *
 * @version $Revision: 1.3.2.3 $
 */
class RenameCommand extends AuthenticatedStateCommand
{
    public static final String NAME = "RENAME";
    public static final String ARGS = "existing-mailbox-name SPACE new-mailbox-name";

    /** @see CommandTemplate#doProcess */
    protected void doProcess( ImapRequestLineReader request,
                              ImapResponse response,
                              ImapSession session )
            throws ProtocolException, MailboxException, AuthorizationException
    {
        String existingName = parser.mailbox( request );
        String newName = parser.mailbox( request );
        parser.endLine( request );

        session.getHost().renameMailbox( session.getUser(), existingName, newName );

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
6.3.5.  RENAME Command

   Arguments:  existing mailbox name
               new mailbox name

   Responses:  no specific responses for this command

   Result:     OK - rename completed
               NO - rename failure: can't rename mailbox with that name,
                    can't rename to mailbox with that name
               BAD - command unknown or arguments invalid

      The RENAME command changes the name of a mailbox.  A tagged OK
      response is returned only if the mailbox has been renamed.  It is
      an error to attempt to rename from a mailbox name that does not
      exist or to a mailbox name that already exists.  Any error in
      renaming will return a tagged NO response.

      If the name has inferior hierarchical names, then the inferior
      hierarchical names MUST also be renamed.  For example, a rename of
      "foo" to "zap" will rename "foo/bar" (assuming "/" is the
      hierarchy delimiter character) to "zap/bar".

      The value of the highest-used unique identifier of the old mailbox
      name MUST be preserved so that a new mailbox created with the same
      name will not reuse the identifiers of the former incarnation,
      UNLESS the new incarnation has a different unique identifier
      validity value.  See the description of the UID command for more
      detail.

      Renaming INBOX is permitted, and has special behavior.  It moves
      all messages in INBOX to a new mailbox with the given name,
      leaving INBOX empty.  If the server implementation supports
      inferior hierarchical names of INBOX, these are unaffected by a
      rename of INBOX.

   Examples:   C: A682 LIST "" *
               S: * LIST () "/" blurdybloop
               S: * LIST (\Noselect) "/" foo
               S: * LIST () "/" foo/bar
               S: A682 OK LIST completed
               C: A683 RENAME blurdybloop sarasoop
               S: A683 OK RENAME completed
               C: A684 RENAME foo zowie
               S: A684 OK RENAME Completed
               C: A685 LIST "" *
               S: * LIST () "/" sarasoop
               S: * LIST (\Noselect) "/" zowie
               S: * LIST () "/" zowie/bar
               S: A685 OK LIST completed

               C: Z432 LIST "" *
               S: * LIST () "." INBOX
               S: * LIST () "." INBOX.bar
               S: Z432 OK LIST completed
               C: Z433 RENAME INBOX old-mail
               S: Z433 OK RENAME completed
               C: Z434 LIST "" *
               S: * LIST () "." INBOX
               S: * LIST () "." INBOX.bar
               S: * LIST () "." old-mail
               S: Z434 OK LIST completed
*/
