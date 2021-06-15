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

import java.util.Date;

/**
 * Interface for objects holding IMAP4rev1 Message Attributes. Message
 * Attributes should be set when a message enters a mailbox. Implementations
 * are encouraged to implement and store MessageAttributes apart from the
 * underlying message. This allows the Mailbox to respond to questions about
 * very large message without needing to access them directly.
 * <p> Note that the message in a mailbox have the same order using either
 * Message Sequence Numbers or UIDs.
 *
 * Reference: RFC 2060 - para 2.3
 * @version 0.1 on 14 Dec 2000
 */
public interface MessageAttributes  {

    /**
     * Provides the current Message Sequence Number for this message. MSNs
     * change when messages are expunged from the mailbox.
     *
     * @return int a positive non-zero integer
     */
    int getMessageSequenceNumber();

    /**
     * Provides the unique identity value for this message. UIDs combined with
     * a UIDValidity value form a unique reference for a message in a given
     * mailbox. UIDs persist across sessions unless the UIDValidity value is
     * incremented. UIDs are not copied if a message is copied to another
     * mailbox.
     *
     * @return int a 32-bit value
     */
    int getUID();

    /**
     * Provides the date and time at which the message was received. In the
     * case of delivery by SMTP, this SHOULD be the date and time of final
     * delivery as defined for SMTP. In the case of messages copied from
     * another mailbox, it shuld be the internalDate of the source message. In
     * the case of messages Appended to the mailbox, example drafts,  the
     * internalDate is either specified in the Append command or is the
     * current dat and time at the time of the Append.
     *
     * @return Date imap internal date
     */
    Date getInternalDate();

    /**
     * Returns IMAP formatted String representation of Date
     */
    String getInternalDateAsString();

    /**
     * Provides the sizeof the message in octets.
     *
     * @return int number of octets in message.
     */
    int getSize();

    /**
     * Provides the Envelope structure information for this message. 
     * This is a parsed representation of the rfc-822 envelope information. 
     * This is not to be confused with the SMTP envelope!
     *
     * @return String satisfying envelope syntax in rfc 2060.
     */
    String getEnvelope();

    /**
     * Provides the Body Structure information for this message. 
     * This is a parsed representtion of the MIME structure of the message.
     *
     * @return String satisfying body syntax in rfc 2060.
     */
    String getBodyStructure();
}


