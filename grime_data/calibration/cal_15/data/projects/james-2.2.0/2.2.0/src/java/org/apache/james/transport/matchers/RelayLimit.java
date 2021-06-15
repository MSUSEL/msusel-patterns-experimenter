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
package org.apache.james.transport.matchers;

import org.apache.james.util.RFC2822Headers;
import org.apache.mailet.GenericMatcher;
import org.apache.mailet.Mail;

import javax.mail.Header;
import javax.mail.internet.MimeMessage;
import java.util.Collection;
import java.util.Enumeration;

/**
 *
 * @version 1.0.0, 1/5/2000
 */

public class RelayLimit extends GenericMatcher {
    int limit = 30;

    public void init() {
        limit = Integer.parseInt(getCondition());
    }

    public Collection match(Mail mail) throws javax.mail.MessagingException {
        MimeMessage mm = mail.getMessage();
        int count = 0;
        for (Enumeration e = mm.getAllHeaders(); e.hasMoreElements();) {
            Header hdr = (Header)e.nextElement();
            if (hdr.getName().equals(RFC2822Headers.RECEIVED)) {
                count++;
            }
        }
        if (count >= limit) {
            return mail.getRecipients();
        } else {
            return null;
        }
    }
}
