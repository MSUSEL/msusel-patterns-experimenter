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

import org.apache.james.imapserver.store.ImapStore;
import org.apache.james.imapserver.store.InMemoryStore;
import org.apache.james.imapserver.store.ImapMailbox;
import org.apache.james.imapserver.store.MailboxException;
import org.apache.james.imapserver.store.MessageFlags;
import org.apache.james.imapserver.store.SimpleImapMessage;
import org.apache.james.core.MimeMessageSource;
import org.apache.james.core.MimeMessageWrapper;
import org.apache.james.core.MailImpl;

import junit.framework.TestCase;

import javax.mail.internet.MimeMessage;
import javax.mail.Address;
import java.util.Date;
import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.net.InetAddress;

/**
 *
 *
 * @version $Revision: 1.2.2.3 $
 */
public class ImapMailboxTest extends TestCase
        implements ImapConstants
{
    public ImapMailboxTest( String s )
    {
        super( s );
    }

    public void testAppend() throws Exception
    {
        ImapMailbox mailbox = getMailbox();

        MessageFlags flags = new MessageFlags();
        flags.setFlagged( true );

        Date datetime = new Date();
        String message =
        "Date: Mon, 7 Feb 1994 21:52:25 -0800 (PST)\r\n" +
        "From: Fred Foobar <foobar@Blurdybloop.COM>\r\n" +
        "Subject: afternoon meeting\r\n" +
        "To: mooch@owatagu.siam.edu\r\n" +
        "Message-Id: <B27397-0100000@Blurdybloop.COM>\r\n" +
        "MIME-Version: 1.0\r\n" +
        "Content-Type: TEXT/PLAIN; CHARSET=US-ASCII\r\n" +
        "\r\n" +
        "Hello Joe, do you think we can meet at 3:30 tomorrow?\r\n" +
        "\r\n";
        long uid = appendMessage( message, flags, datetime, mailbox );

        SimpleImapMessage imapMessage = mailbox.getMessage( uid );

        assertEquals( 1, mailbox.getMessageCount() );
        assertTrue( imapMessage.getFlags().isFlagged() );
        assertTrue( ! imapMessage.getFlags().isAnswered() );

        MimeMessage mime = imapMessage.getMimeMessage();
        assertEquals( "TEXT/PLAIN; CHARSET=US-ASCII", mime.getContentType() );
        assertEquals( "afternoon meeting", mime.getSubject() );
        assertEquals( "Fred Foobar <foobar@Blurdybloop.COM>",
                      mime.getFrom()[0].toString() );

    }

    private long appendMessage( String messageContent, MessageFlags flags,
                                Date datetime, ImapMailbox mailbox )
    {
        MimeMessageSource source =
                new MimeMessageByteArraySource( "messageContent:" + System.currentTimeMillis(),
                                                messageContent.getBytes());
        MimeMessage message = new MimeMessageWrapper( source );
        SimpleImapMessage imapMessage = mailbox.createMessage( message, flags, datetime );
        return imapMessage.getUid();
    }

    private ImapMailbox getMailbox() throws MailboxException
    {
        ImapStore store = new InMemoryStore();
        ImapMailbox root = store.getMailbox( ImapConstants.USER_NAMESPACE );
        ImapMailbox test = store.createMailbox( root, "test", true );
        return test;
    }

    class MimeMessageByteArraySource extends MimeMessageSource
    {
        private String sourceId;
        private byte[] byteArray;

        public MimeMessageByteArraySource( String sourceId, byte[] byteArray )
        {
            this.sourceId = sourceId;
            this.byteArray = byteArray;
        }

        public String getSourceId()
        {
            return sourceId;
        }

        public InputStream getInputStream() throws IOException
        {
            return new ByteArrayInputStream( byteArray );
        }
    }


}
