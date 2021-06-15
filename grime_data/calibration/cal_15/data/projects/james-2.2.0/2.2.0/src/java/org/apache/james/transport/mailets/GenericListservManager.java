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

import javax.mail.MessagingException;

/**
 * An abstract implementation of a listserv manager.  This mailet reads the
 * address to find the command.
 */
public abstract class GenericListservManager extends GenericMailet {

    /**
     * Adds an address to the listserv.  Returns whether command was
     * successful.
     */
    public abstract boolean addAddress(MailAddress address);

    /**
     * Removes an address from the listserv.  Returns whether command
     * was successful.
     */
    public abstract boolean removeAddress(MailAddress address);


    /**
     * Indicates whether an address already exists on the listserv. Returns
     * whether the address exists.
     */
    public abstract boolean existsAddress(MailAddress address);

    /**
     * Processes the message.  Checks which command was sent based on the
     * recipient address, and does the appropriate action.
     */
    public final void service(Mail mail) throws MessagingException {
        if (mail.getRecipients().size() != 1) {
            getMailetContext().bounce(mail, "You can only send one command at a time to this listserv manager.");
            return;
        }
        MailAddress address = (MailAddress)mail.getRecipients().iterator().next();
        if (address.getUser().endsWith("-off")) {
            if (existsAddress(mail.getSender())) {
                if (removeAddress(mail.getSender())) {
                    getMailetContext().bounce(mail, "Successfully removed from listserv.");
                } else {
                    getMailetContext().bounce(mail, "Unable to remove you from listserv for some reason.");
                }
            } else {
                getMailetContext().bounce(mail, "You are not subscribed to this listserv.");
            }
        } else if (address.getUser().endsWith("-on")) {
            if (existsAddress(mail.getSender())) {
                getMailetContext().bounce(mail, "You are already subscribed to this listserv.");
            } else {
                if (addAddress(mail.getSender())) {
                    getMailetContext().bounce(mail, "Successfully added to listserv.");
                } else {
                    getMailetContext().bounce(mail, "Unable to add you to the listserv for some reason");
                }
            }
        } else {
            getMailetContext().bounce(mail, "Could not understand the command you sent to this listserv manager.\r\n"
                                      + "Valid commands are <listserv>-on@domain.com and <listserv>-off@domain.com");
        }
        //Kill the command message
        mail.setState(Mail.GHOST);
    }
}
