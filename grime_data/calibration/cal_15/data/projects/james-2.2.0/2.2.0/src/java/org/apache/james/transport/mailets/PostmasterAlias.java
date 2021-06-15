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
import org.apache.mailet.MailetContext;

import javax.mail.MessagingException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
//import com.workingdogs.town.*;

/**
 * Rewrites recipient addresses to make sure email for the postmaster is
 * always handled.  This mailet is silently inserted at the top of the root
 * spool processor.  All recipients mapped to postmaster@<servernames> are
 * changed to the postmaster account as specified in the server conf.
 *
 */
public class PostmasterAlias extends GenericMailet {

    /**
     * Make sure that a message that is addressed to a postmaster alias is always
     * sent to the postmaster address, regardless of delivery to other recipients.
     *
     * @param mail the mail to process
     *
     * @throws MessagingException if an error is encountered while modifying the message
     */
    public void service(Mail mail) throws MessagingException {
        Collection recipients = mail.getRecipients();
        Collection recipientsToRemove = null;
        MailetContext mailetContext = getMailetContext();
        boolean postmasterAddressed = false;

        for (Iterator i = recipients.iterator(); i.hasNext(); ) {
            MailAddress addr = (MailAddress)i.next();
            if (addr.getUser().equalsIgnoreCase("postmaster") &&
                mailetContext.isLocalServer(addr.getHost())) {
                //Should remove this address... we want to replace it with
                //  the server's postmaster address
                if (recipientsToRemove == null) {
                    recipientsToRemove = new Vector();
                }
                recipientsToRemove.add(addr);
                //Flag this as having found the postmaster
                postmasterAddressed = true;
            }
        }
        if (postmasterAddressed) {
            recipients.removeAll(recipientsToRemove);
            recipients.add(getMailetContext().getPostmaster());
        }
    }

    /**
     * Return a string describing this mailet.
     *
     * @return a string describing this mailet
     */
    public String getMailetInfo() {
        return "Postmaster aliasing mailet";
    }
}
