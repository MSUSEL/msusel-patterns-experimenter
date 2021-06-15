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

import junit.framework.TestCase;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import java.util.Properties;

public final class InitialMail extends TestCase
        implements IMAPTest
{
    private Session _session;
    private InternetAddress _fromAddress;
    private InternetAddress _toAddress;

    public InitialMail( String name )
    {
        super( name );
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        Properties props = new Properties();
        props.setProperty("mail.debug","true");
        _session = Session.getDefaultInstance( props );

        _fromAddress = new InternetAddress( FROM_ADDRESS );
        _toAddress = new InternetAddress( TO_ADDRESS );
    }

    public void testSendInitialMessages() throws Exception
    {
        sendMessage( "Message 1", "This is the first message." );
        sendMessage( "Message 2", "This is the second message." );
        sendMessage( "Message 3", "This is the third message." );
        sendMessage( "Message 4", "This is the fourth message." );
    }

    private void sendMessage( String subject, String body )
            throws Exception
    {
        MimeMessage msg = new MimeMessage(_session);
        msg.setFrom( _fromAddress );
        msg.addRecipient(Message.RecipientType.TO, _toAddress );
        msg.setSubject( subject );
        msg.setContent( body, "text/plain" );

        Transport.send( msg );
        System.out.println( "Sending message: " + subject );
        
        Thread.sleep( 1000 );
    }
}
