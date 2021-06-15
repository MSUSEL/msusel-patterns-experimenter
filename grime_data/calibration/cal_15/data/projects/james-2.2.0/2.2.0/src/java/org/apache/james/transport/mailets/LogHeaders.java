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

import java.util.Enumeration;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.james.core.MailImpl;
import org.apache.mailet.GenericMailet;
import org.apache.mailet.Mail;

/**
 * Logs Message Headers.
 * If the "passThrough" in confs is true the mail will be left untouched in
 * the pipe. If false will be destroyed.  Default is true.
 *
 * @version This is $Revision: 1.8.4.2 $
 */
public class LogHeaders extends GenericMailet {

    /**
     * Whether this mailet should allow mails to be processed by additional mailets
     * or mark it as finished.
     */
    private boolean passThrough = true;

    /**
     * Initialize the mailet, loading configuration information.
     */
    public void init() {
        try {
            passThrough = (getInitParameter("passThrough") == null) ? true : new Boolean(getInitParameter("debug")).booleanValue();
        } catch (Exception e) {
            // Ignore exception, default to true
        }
    }

    /**
     * Log a particular message
     *
     * @param mail the mail to process
     */
    public void service(Mail genericmail) {
        MailImpl mail = (MailImpl)genericmail;
        log(new StringBuffer(160).append("Logging mail ").append(mail.getName()).toString());
        try {
            log(getMessageHeaders(mail.getMessage()));
        }
        catch (MessagingException e) {
            log("Error logging headers.");
        }
        if (!passThrough) {
            mail.setState(Mail.GHOST);
        }
    }

    /**
     * Utility method for obtaining a string representation of a
     * Message's headers
     */
    private String getMessageHeaders(MimeMessage message) throws MessagingException {
        Enumeration heads = message.getAllHeaderLines();
        StringBuffer headBuffer = new StringBuffer(1024).append("\n");
        while(heads.hasMoreElements()) {
            headBuffer.append(heads.nextElement().toString()).append("\n");
        }
        return headBuffer.toString();
    }

    /**
     * Return a string describing this mailet.
     *
     * @return a string describing this mailet
     */
    public String getMailetInfo() {
        return "LogHeaders Mailet";
    }
}
