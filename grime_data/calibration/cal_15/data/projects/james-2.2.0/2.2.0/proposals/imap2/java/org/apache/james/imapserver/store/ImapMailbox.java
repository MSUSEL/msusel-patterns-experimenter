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
package org.apache.james.imapserver.store;

import org.apache.james.services.MailRepository;
import org.apache.james.core.MailImpl;

import javax.mail.internet.MimeMessage;
import javax.mail.search.SearchTerm;
import java.util.Date;
import java.util.Collection;

/**
 * Represents a mailbox within an {@link org.apache.james.imapserver.store.ImapStore}.
 * May provide storage for MailImpl objects, or be a non-selectable placeholder in the
 * Mailbox hierarchy.
 * TODO this is a "grown" interface, which needs some more design and thought re:
 * how it will fit in with the other mail storage in James.
 *
 *
 * @version $Revision: 1.4.2.3 $
 */
public interface ImapMailbox
{
    String getName();

    String getFullName();

    MessageFlags getAllowedFlags();

    int getMessageCount();

    int getRecentCount();

    long getUidValidity();

    int getFirstUnseen();

    int getMsn( long uid ) throws MailboxException;

    boolean isSelectable();

    long getUidNext();

    int getUnseenCount();

    SimpleImapMessage createMessage( MimeMessage message, MessageFlags flags, Date internalDate );

    void updateMessage( SimpleImapMessage message ) throws MailboxException;

    void store( MailImpl mail) throws Exception;

    SimpleImapMessage getMessage( long uid );

    long[] getMessageUids();

    void deleteMessage( long uid );
}
