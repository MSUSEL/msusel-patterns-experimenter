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

/**
 * Does a DNS lookup (MX and A/CNAME records) on the sender's domain.  If
 * there are no entries, the domain is considered fake and the match is 
 * successful.
 *
 */
public class SenderInFakeDomain extends AbstractNetworkMatcher {

    public Collection match(Mail mail) {
        if (mail.getSender() == null) {
            return null;
        }
        String domain = mail.getSender().getHost();
        //DNS Lookup for this domain
        Collection servers = getMailetContext().getMailServers(domain);
        if (servers.size() == 0) {
            //No records...could not deliver to this domain, so matches criteria.
            log("No MX, A, or CNAME record found for domain: " + domain);
            return mail.getRecipients();
        } else if (matchNetwork(servers.iterator().next().toString())){
            /*
             * It could be a wildcard address like these:
             *
             * 64.55.105.9/32          # Allegiance Telecom Companies Worldwide (.nu)
             * 64.94.110.11/32         # VeriSign (.com .net)
             * 194.205.62.122/32       # Network Information Center - Ascension Island (.ac)
             * 194.205.62.62/32        # Internet Computer Bureau (.sh)
             * 195.7.77.20/32          # Fredrik Reutersward Data (.museum)
             * 206.253.214.102/32      # Internap Network Services (.cc)
             * 212.181.91.6/32         # .NU Domain Ltd. (.nu)
             * 219.88.106.80/32        # Telecom Online Solutions (.cx)
             * 194.205.62.42/32        # Internet Computer Bureau (.tm)
             * 216.35.187.246/32       # Cable & Wireless (.ws)
             * 203.119.4.6/32          # .PH TLD (.ph)
             *
             */
            log("Banned IP found for domain: " + domain);
            log(" --> :" + servers.iterator().next().toString());
            return mail.getRecipients();
        } else {
            // Some servers were found... the domain is not fake.

            return null;
        }
    }
}
