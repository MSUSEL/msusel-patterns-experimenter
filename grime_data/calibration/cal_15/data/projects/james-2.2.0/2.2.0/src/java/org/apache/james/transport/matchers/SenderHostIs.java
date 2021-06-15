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

import java.util.Collection;
import java.util.Collections;
import java.util.StringTokenizer;

/**
 * Checkes the sender's displayed domain name against a supplied list.
 *
 * Sample configuration:
 *
 * <mailet match="SenderHostIs=domain.com" class="ToProcessor">
 *   <processor> spam </processor>
 * </mailet>
 *
 * @version 1.0.0, 2002-09-10
 */
public class SenderHostIs extends GenericMatcher {
    /**
     * The collection of host names to match against.
     */
    private Collection senderHosts;

    /**
     * Initialize the mailet.
     */
    public void init()  {
        //Parse the condition...
        StringTokenizer st = new StringTokenizer(getCondition(), ", ", false);

        //..into a vector of domain names.
        senderHosts = new java.util.HashSet();
        while (st.hasMoreTokens()) {
            senderHosts.add(((String)st.nextToken()).toLowerCase());
        }
        senderHosts = Collections.unmodifiableCollection(senderHosts);
    }

    /**
     * Takes the message and checks the sender (if there is one) against
     * the vector of host names.
     *
     * Returns the collection of recipients if there's a match.
     *
     * @param mail the mail being processed
     */
    public Collection match(Mail mail) {
        try {
            if (mail.getSender() != null && senderHosts.contains(mail.getSender().getHost().toLowerCase())) {
                return mail.getRecipients();
            }
        } catch (Exception e) {
            log(e.getMessage());
        }

        return null;    //No match.
    }
}
