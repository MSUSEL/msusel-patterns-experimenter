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

import org.apache.mailet.GenericRecipientMatcher;
import org.apache.mailet.MailAddress;

import javax.mail.MessagingException;

/**
 * CommandListservMatcher is the matcher that pairs with the {@link org.apache.james.transport.mailets.CommandListservManager}
 * It checks to see if the request is intended for the ListservManager, but doesn't guarantee that it is a valid command.
 * <br />
 * To configure, insert this into the config.xml inside of the root processor block.
 * <pre>
 * &lt;mailet match="CommandListservMatcher=announce@localhost" class="CommandListservManager"&gt;
 * ...
 * &lt;/mailet&gt;
 * </pre>
 *
 * @version CVS $Revision: 1.1.2.3 $ $Date: 2004/03/15 03:54:21 $
 * @since 2.2.0
 * @see org.apache.james.transport.mailets.CommandListservManager
 */
public class CommandListservMatcher extends GenericRecipientMatcher {

    private MailAddress listservAddress;

    public void init() throws MessagingException {
        listservAddress = new MailAddress(getCondition());
    }

    /**
     * This doesn't perform an exact match, but checks to see if the request is at lesast
     * intended to go to the list serv manager.
     * @param recipient
     * @return true if matches, false otherwise
     */
    public boolean matchRecipient(MailAddress recipient) {
        if (recipient.getHost().equals(listservAddress.getHost())) {
            if (recipient.getUser().startsWith(listservAddress.getUser() + "-")) {
                return true;
            }
        }
        return false;
    }
}
