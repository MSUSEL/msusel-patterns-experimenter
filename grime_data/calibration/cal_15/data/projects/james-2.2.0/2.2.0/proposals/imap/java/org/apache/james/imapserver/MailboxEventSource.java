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

/**
 * Interface for objects that are sources for Mailbox Events. Mailbox Events
 * are used to inform registered listeners of events in this Source. For
 * example, if mail is delivered to an Inbox or if another user appends or 
 * deletes a message.
 *
 * <p>Not currently active in this implementation
 *
 * @version 0.1 on 14 Dec 2000
 */
public interface MailboxEventSource  {
  
    /**
     * Registers a MailboxEventListener.
     *
     * @param mel MailboxEventListener to be registered with this source.
     */
    void addMailboxEventListener( MailboxEventListener mel );

    /**
     * Deregisters a MailboxEventListener.
     *
     * @param mel MailboxEventListener to be deregistered from this source.
     */
    void removeMailboxEventListener( MailboxEventListener mel );
}
 

