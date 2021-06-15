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

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import javax.mail.MessagingException;
import org.apache.mailet.GenericMatcher;
import org.apache.mailet.MailAddress;
import org.apache.mailet.Mail;

/**
 * <P>Abstract matcher checking whether a recipient has exceeded a maximum allowed quota.</P>
 * <P>"Quota" at this level is an abstraction whose specific interpretation
 * will be done by subclasses.</P> 
 * <P>Although extending GenericMatcher, its logic is recipient oriented.</P>
 *
 * @version CVS $Revision: 1.1.2.3 $ $Date: 2004/02/26 20:37:00 $
 * @since 2.2.0
 */
abstract public class AbstractQuotaMatcher extends GenericMatcher { 

    /**
     * Standard matcher entrypoint.
     * First of all, checks the sender using {@link #isSenderChecked}.
     * Then, for each recipient checks it using {@link #isRecipientChecked} and
     * {@link #isOverQuota}.
     *
     * @throws MessagingException if either <CODE>isSenderChecked</CODE> or isRecipientChecked throw an exception
     */    
    public final Collection match(Mail mail) throws MessagingException {
        Collection matching = null;
        if (isSenderChecked(mail.getSender())) {
            matching = new ArrayList();
            for (Iterator i = mail.getRecipients().iterator(); i.hasNext(); ) {
                MailAddress recipient = (MailAddress) i.next();
                if (isRecipientChecked(recipient) && isOverQuota(recipient, mail)) {
                    matching.add(recipient);
                }
            }
        }
        return matching;
    }

    /**
     * Does the quota check.
     * Checks if {@link #getQuota} < {@link #getUsed} for a recipient.
     * Catches any throwable returning false, and so should any override do.
     *
     * @param address the recipient addresss to check
     * @param mail the mail involved in the check
     * @return true if over quota
     */
    protected boolean isOverQuota(MailAddress address, Mail mail) {
        String user = address.getUser();
        try {
            boolean over = getQuota(address, mail) < getUsed(address, mail);
            if (over) log(address + " is over quota.");
            return over;
        } catch (Throwable e) {
            log("Exception checking quota for: " + address, e);
            return false;
        }
    }

    /** 
     * Checks the sender.
     * The default behaviour is to check that the sender <I>is not</I> null nor the local postmaster.
     * If a subclass overrides this method it should "and" <CODE>super.isSenderChecked</CODE>
     * to its check.
     *
     * @param sender the sender to check
     */    
    protected boolean isSenderChecked(MailAddress sender) throws MessagingException {
        return !(sender == null || getMailetContext().getPostmaster().equals(sender));
    }

    /** 
     * Checks the recipient.
     * The default behaviour is to check that the recipient <I>is not</I> the local postmaster.
     * If a subclass overrides this method it should "and" <CODE>super.isRecipientChecked</CODE>
     * to its check.
     *
     * @param recipient the recipient to check
     */    
    protected boolean isRecipientChecked(MailAddress recipient) throws MessagingException {
        return !(getMailetContext().getPostmaster().equals(recipient));
    }

    /** 
     * Gets the quota to check against.
     *
     * @param address the address holding the quota if applicable
     * @param mail the mail involved if needed
     */    
    abstract protected long getQuota(MailAddress address, Mail mail) throws MessagingException;
    
    /**
     * Gets the used amount to check against the quota.
     *
     * @param address the address involved
     * @param mail the mail involved if needed
     */
    abstract protected long getUsed(MailAddress address, Mail mail) throws MessagingException;

    /**
     * Utility method that parses an amount string.
     * You can use 'k' and 'm' as optional postfixes to the amount (both upper and lowercase).
     * In other words, "1m" is the same as writing "1024k", which is the same as
     * "1048576".
     *
     * @param amount the amount string to parse
     */
    protected long parseQuota(String amount) throws MessagingException {
        long quota;
        try {
            if (amount.endsWith("k")) {
                amount = amount.substring(0, amount.length() - 1);
                quota = Long.parseLong(amount) * 1024;
            } else if (amount.endsWith("m")) {
                amount = amount.substring(0, amount.length() - 1);
                quota = Long.parseLong(amount) * 1024 * 1024;
            } else {
                quota = Long.parseLong(amount);
            }
            return quota;
        }
        catch (Exception e) {
            throw new MessagingException("Exception parsing quota", e);
        }
    }
}
