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

import org.apache.james.imapserver.ImapRequestLineReader;
import org.apache.james.imapserver.ImapResponse;
import org.apache.james.imapserver.ImapSession;
import org.apache.james.imapserver.ProtocolException;
import org.apache.james.imapserver.store.MessageFlags;
import org.apache.james.imapserver.store.ImapMailbox;
import org.apache.james.imapserver.store.SimpleImapMessage;
import org.apache.james.imapserver.store.MailboxException;

/**
 * Handles processeing for the STORE imap command.
 *
 *
 * @version $Revision: 1.1.2.3 $
 */
class StoreCommand extends SelectedStateCommand implements UidEnabledCommand
{
    public static final String NAME = "STORE";
    public static final String ARGS = "<Message-set> ['+'|'-']FLAG[.SILENT] <flag-list>";

    private final StoreCommandParser parser = new StoreCommandParser();

    /** @see CommandTemplate#doProcess */
    protected void doProcess( ImapRequestLineReader request,
                              ImapResponse response,
                              ImapSession session )
            throws ProtocolException, MailboxException
    {
        doProcess( request, response, session , false );
    }

    public void doProcess( ImapRequestLineReader request,
                              ImapResponse response,
                              ImapSession session,
                              boolean useUids )
            throws ProtocolException, MailboxException
    {
        IdSet idSet = parser.set( request );
        StoreDirective directive = parser.storeDirective( request );
        MessageFlags flags = parser.flagList( request );
        parser.endLine( request );

        ImapMailbox mailbox = session.getSelected();
        long[] uids = mailbox.getMessageUids();
        for ( int i = 0; i < uids.length; i++ ) {
            long uid = uids[i];
            int msn = mailbox.getMsn( uid );

            if ( ( useUids && idSet.includes( uid ) ) ||
                 ( !useUids && idSet.includes( msn ) ) )
            {
                SimpleImapMessage imapMessage = mailbox.getMessage( uid );
                storeFlags( imapMessage, directive, flags );
                mailbox.updateMessage( imapMessage );

                if ( ! directive.isSilent() ) {
                    StringBuffer out = new StringBuffer( "FLAGS " );
                    out.append( imapMessage.getFlags().format() );
                    response.fetchResponse( msn, out.toString() );
                }
            }
        }

        session.unsolicitedResponses( response );
        response.commandComplete( this );
    }

    private void storeFlags( SimpleImapMessage imapMessage, StoreDirective directive, MessageFlags newFlags )
    {
        MessageFlags messageFlags = imapMessage.getFlags();
        if ( directive.getSign() == 0 ) {
            messageFlags.setAll( newFlags );
        }
        else if ( directive.getSign() < 0 ) {
            messageFlags.removeAll( newFlags );
        }
        else if ( directive.getSign() > 0 ) {
            messageFlags.addAll( newFlags );
        }
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

    private class StoreCommandParser extends CommandParser
    {
        StoreDirective storeDirective( ImapRequestLineReader request ) throws ProtocolException
        {
            int sign = 0;
            boolean silent = false;

            char next = request.nextWordChar();
            if ( next == '+' ) {
                sign = 1;
                request.consume();
            }
            else if ( next == '-' ) {
                sign = -1;
                request.consume();
            }
            else {
                sign = 0;
            }

            String directive = consumeWord( request, new NoopCharValidator() );
            if ( "FLAGS".equalsIgnoreCase( directive ) ) {
                silent = false;
            }
            else if ( "FLAGS.SILENT".equalsIgnoreCase( directive ) ) {
                silent = true;
            }
            else {
                throw new ProtocolException( "Invalid Store Directive: '" + directive + "'" );
            }
            return new StoreDirective( sign, silent );
        }
    }

    private class StoreDirective
    {
        private int sign;
        private boolean silent;

        public StoreDirective( int sign, boolean silent )
        {
            this.sign = sign;
            this.silent = silent;
        }

        public int getSign()
        {
            return sign;
        }

        public boolean isSilent()
        {
            return silent;
        }
    }
}
/*
6.4.6.  STORE Command

   Arguments:  message set
               message data item name
               value for message data item

   Responses:  untagged responses: FETCH

   Result:     OK - store completed
               NO - store error: can't store that data
               BAD - command unknown or arguments invalid

      The STORE command alters data associated with a message in the
      mailbox.  Normally, STORE will return the updated value of the
      data with an untagged FETCH response.  A suffix of ".SILENT" in
      the data item name prevents the untagged FETCH, and the server
      SHOULD assume that the client has determined the updated value
      itself or does not care about the updated value.

         Note: regardless of whether or not the ".SILENT" suffix was
         used, the server SHOULD send an untagged FETCH response if a
         change to a message's flags from an external source is
         observed.  The intent is that the status of the flags is
         determinate without a race condition.

      The currently defined data items that can be stored are:

      FLAGS <flag list>
                     Replace the flags for the message with the
                     argument.  The new value of the flags are returned
                     as if a FETCH of those flags was done.

      FLAGS.SILENT <flag list>
                     Equivalent to FLAGS, but without returning a new
                     value.

      +FLAGS <flag list>
                     Add the argument to the flags for the message.  The
                     new value of the flags are returned as if a FETCH
                     of those flags was done.

      +FLAGS.SILENT <flag list>
                     Equivalent to +FLAGS, but without returning a new
                     value.

      -FLAGS <flag list>
                     Remove the argument from the flags for the message.
                     The new value of the flags are returned as if a
                     FETCH of those flags was done.

      -FLAGS.SILENT <flag list>
                     Equivalent to -FLAGS, but without returning a new
                     value.

   Example:    C: A003 STORE 2:4 +FLAGS (\Deleted)
               S: * 2 FETCH FLAGS (\Deleted \Seen)
               S: * 3 FETCH FLAGS (\Deleted)
               S: * 4 FETCH FLAGS (\Deleted \Flagged \Seen)
               S: A003 OK STORE completed

*/
