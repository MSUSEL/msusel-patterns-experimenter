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
package org.apache.james.transport.mailets;

import org.apache.mailet.GenericMailet;
import org.apache.mailet.Mail;
import org.apache.mailet.MailAddress;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashSet;
import java.util.Set;

/**
 * Returns the current time for the mail server.  Sample configuration:
 * <mailet match="RecipientIs=time@cadenza.lokitech.com" class="ServerTime">
 * </mailet>
 *
 */
public class ServerTime extends GenericMailet {
    /**
     * Sends a message back to the sender indicating what time the server thinks it is.
     *
     * @param mail the mail being processed
     *
     * @throws MessagingException if an error is encountered while formulating the reply message
     */
    public void service(Mail mail) throws javax.mail.MessagingException {
        MimeMessage response = (MimeMessage)mail.getMessage().reply(false);
        response.setSubject("The time is now...");
        StringBuffer textBuffer =
            new StringBuffer(128)
                    .append("This mail server thinks it's ")
                    .append((new java.util.Date()).toString())
                    .append(".");
        response.setText(textBuffer.toString());

        // Someone manually checking the server time by hand may send
        // an formatted message, lacking From and To headers.  If the
        // response fields are null, try setting them from the SMTP
        // MAIL FROM/RCPT TO commands used to send the inquiry.

        if (response.getFrom() == null) {
            response.setFrom(((MailAddress)mail.getRecipients().iterator().next()).toInternetAddress());
        }

        if (response.getAllRecipients() == null) {
            response.setRecipients(MimeMessage.RecipientType.TO, mail.getSender().toString());
        }

        response.saveChanges();
        getMailetContext().sendMail(response);
    }

    /**
     * Return a string describing this mailet.
     *
     * @return a string describing this mailet
     */
    public String getMailetInfo() {
        return "ServerTime Mailet";
    }
}

