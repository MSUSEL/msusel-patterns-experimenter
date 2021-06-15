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

import org.apache.mailet.Mail;
import org.apache.mailet.MailetContext;
import org.apache.mailet.MailAddress;
import javax.mail.MessagingException;
import java.util.Locale;

/**
 * <P>Checks whether a recipient has exceeded a maximum allowed quota for messages
 * standing in his inbox. Such quota is <I>the same</I> for all users.</P>
 * <P>Will check if the total size of all his messages in the inbox are greater
 * than a certain number of bytes.  You can use 'k' and 'm' as optional postfixes.
 * In other words, "1m" is the same as writing "1024k", which is the same as
 * "1048576".</P>
 * <P>Here follows an example of a config.xml definition:</P>
 * <PRE><CODE>
 * &lt;processor name="transport"&gt;
 * .
 * .
 * .
 *    &lt;mailet match=match="RecipientIsOverFixedQuota=40M" class="ToProcessor"&gt;
 *       &lt;processor&gt; error &lt;/processor&gt;
 *       &lt;notice&gt;The recipient has exceeded maximum allowed size quota&lt;/notice&gt;
 *    &lt;/mailet&gt;
 * .
 * .
 * .
 * &lt;/processor&gt;
 * </CODE></PRE>
 *
 * @version 1.0.0, 2003-05-11
 */

public class RecipientIsOverFixedQuota extends AbstractStorageQuota {
    private long quota = 0;

    /**
     * Standard matcher initialization.
     * Does a <CODE>super.init()</CODE> and parses the common storage quota amount from
     * <I>config.xml</I> for later use.
     */
    public void init() throws MessagingException {
        super.init();
        quota = parseQuota(getCondition().trim().toLowerCase(Locale.US));
    }

    protected long getQuota(MailAddress recipient, Mail _) throws MessagingException {
        return quota;
    }
}
