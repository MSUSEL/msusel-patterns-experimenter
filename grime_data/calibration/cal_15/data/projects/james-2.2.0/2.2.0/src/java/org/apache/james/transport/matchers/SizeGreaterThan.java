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

import org.apache.mailet.GenericMatcher;
import org.apache.mailet.Mail;

import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;

/**
 * Checks whether the message (entire message, not just content) is greater
 * than a certain number of bytes.  You can use 'k' and 'm' as optional postfixes.
 * In other words, "1m" is the same as writing "1024k", which is the same as
 * "1048576".
 *
 */
public class SizeGreaterThan extends GenericMatcher {

    int cutoff = 0;

    public void init() {
        String amount = getCondition().trim().toLowerCase(Locale.US);
        if (amount.endsWith("k")) {
            amount = amount.substring(0, amount.length() - 1);
            cutoff = Integer.parseInt(amount) * 1024;
        } else if (amount.endsWith("m")) {
            amount = amount.substring(0, amount.length() - 1);
            cutoff = Integer.parseInt(amount) * 1024 * 1024;
        } else {
            cutoff = Integer.parseInt(amount);
        }
    }

    public Collection match(Mail mail) throws MessagingException {
        MimeMessage message = mail.getMessage();
        //Calculate the size
        int size = message.getSize();
        Enumeration e = message.getAllHeaders();
        while (e.hasMoreElements()) {
            size += ((Header)e.nextElement()).toString().length();
        }
        if (size > cutoff) {
            return mail.getRecipients();
        } else {
            return null;
        }
    }
}
