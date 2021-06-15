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
package org.apache.mailet;

import javax.mail.MessagingException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * GenericMatcher makes writing recipient based matchers easier. It provides
 * simple versions of the lifecycle methods init and destroy and of the methods
 * in the MatcherConfig interface. GenericMatcher also implements the log method,
 * declared in the MatcherContext interface.
 *
 * @version 1.0.0, 24/04/1999
 */
public abstract class GenericRecipientMatcher extends GenericMatcher {

    /**
     * Matches each recipient one by one through matchRecipient(MailAddress
     * recipient) method.  Handles splitting the recipients Collection
     * as appropriate.
     *
     * @param mail - the message and routing information to determine whether to match
     * @return Collection the Collection of MailAddress objects that have been matched
     */
    public final Collection match(Mail mail) throws MessagingException {
        Collection matching = new Vector();
        for (Iterator i = mail.getRecipients().iterator(); i.hasNext(); ) {
            MailAddress rec = (MailAddress) i.next();
            if (matchRecipient(rec)) {
                matching.add(rec);
            }
        }
        return matching;
    }

    /**
     * Simple check to match exclusively on the email address (not
     * message information).
     *
     * @param recipient - the address to determine whether to match
     * @return boolean whether the recipient is a match
     */
    public abstract boolean matchRecipient(MailAddress recipient) throws MessagingException;
}
