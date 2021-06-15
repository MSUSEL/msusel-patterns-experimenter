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
package org.apache.james.transport.mailets;

import org.apache.mailet.GenericMailet;
import org.apache.mailet.Mail;
import org.apache.mailet.MailAddress;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.util.Collection;
import java.util.HashSet;

/**
 * <P>Replaces incoming recipients with those specified, and resends the message unaltered.</P>
 * <P>Can be totally replaced by an equivalent usage of {@link Resend} (see below),
 * simply replacing <I>&lt;forwardto&gt;</I> with <I>&lt;recipients&gt</I>.
 *
 * <P>Sample configuration:</P>
 * <PRE><CODE>
 * &lt;mailet match="All" class="Forward">
 *   &lt;forwardTo&gt;<I>comma delimited list of email addresses</I>&lt;/forwardTo&gt;
 *   &lt;passThrough&gt;<I>true or false, default=false</I>&lt;/passThrough&gt;
 *   &lt;fakeDomainCheck&gt;<I>true or false, default=true</I>&lt;/fakeDomainCheck&gt;
 *   &lt;debug&gt;<I>true or false, default=false</I>&lt;/debug&gt;
 * &lt;/mailet&gt;
 * </CODE></PRE>
 *
 * <P>The behaviour of this mailet is equivalent to using Resend with the following
 * configuration:</P>
 * <PRE><CODE>
 * &lt;mailet match="All" class="Resend">
 *   &lt;recipients&gt;comma delimited list of email addresses&lt;/recipients&gt;
 *   &lt;passThrough&gt;true or false&lt;/passThrough&gt;
 *   &lt;fakeDomainCheck&gt;<I>true or false</I>&lt;/fakeDomainCheck&gt;
 *   &lt;debug&gt;<I>true or false</I>&lt;/debug&gt;
 * &lt;/mailet&gt;
 * </CODE></PRE>
 * <P><I>forwardto</I> can be used instead of
 * <I>forwardTo</I>; such name is kept for backward compatibility.</P>
 *
 * @version CVS $Revision: 1.6.4.14 $ $Date: 2004/03/15 03:54:19 $
 */
public class Forward extends AbstractRedirect {

    /**
     * Return a string describing this mailet.
     *
     * @return a string describing this mailet
     */
    public String getMailetInfo() {
        return "Forward Mailet";
    }

    /** Gets the expected init parameters. */
    protected  String[] getAllowedInitParameters() {
        String[] allowedArray = {
//            "static",
            "debug",
            "passThrough",
            "fakeDomainCheck",
            "forwardto",
            "forwardTo"
        };
        return allowedArray;
    }

    /* ******************************************************************** */
    /* ****************** Begin of getX and setX methods ****************** */
    /* ******************************************************************** */

    /**
     * @return UNALTERED
     */
    protected int getInLineType() throws MessagingException {
        return UNALTERED;
    }

    /**
     * @return NONE
     */
    protected int getAttachmentType() throws MessagingException {
        return NONE;
    }

    /**
     * @return ""
     */
    protected String getMessage() throws MessagingException {
        return "";
    }

    /**
     * @return the <CODE>recipients</CODE> init parameter or null if missing
     */
    protected Collection getRecipients() throws MessagingException {
        Collection newRecipients = new HashSet();
        boolean error = false;
        String addressList = getInitParameter("forwardto");
        if (addressList == null) {
            addressList = getInitParameter("forwardTo");
        }
        
        // if nothing was specified, throw an exception
        if (addressList == null) {
            throw new MessagingException("Failed to initialize \"recipients\" list: no <forwardTo> or <forwardto> init parameter found");
        }

        try {
            InternetAddress[] iaarray = InternetAddress.parse(addressList, false);
            for (int i = 0; i < iaarray.length; i++) {
                String addressString = iaarray[i].getAddress();
                MailAddress specialAddress = getSpecialAddress(addressString,
                new String[] {"postmaster", "sender", "from", "replyTo", "reversePath", "unaltered", "recipients", "to", "null"});
                if (specialAddress != null) {
                    newRecipients.add(specialAddress);
                } else {
                    newRecipients.add(new MailAddress(iaarray[i]));
                }
            }
        } catch (Exception e) {
            throw new MessagingException("Exception thrown in getRecipients() parsing: " + addressList, e);
        }
        if (newRecipients.size() == 0) {
            throw new MessagingException("Failed to initialize \"recipients\" list; empty <recipients> init parameter found.");
        }

        return newRecipients;
    }

    /**
     * @return null
     */
    protected InternetAddress[] getTo() throws MessagingException {
        return null;
    }

    /**
     * @return null
     */
    protected MailAddress getReplyTo() throws MessagingException {
        return null;
    }

    /**
     * @return null
     */
    protected MailAddress getReversePath() throws MessagingException {
        return null;
    }

    /**
     * @return null
     */
    protected MailAddress getSender() throws MessagingException {
        return null;
    }

    /**
     * @return null
     */
    protected String getSubject() throws MessagingException {
        return null;
    }

    /**
     * @return ""
     */
    protected String getSubjectPrefix() throws MessagingException {
        return null;
    }

    /**
     * @return false
     */
    protected boolean attachError() {
        return false;
    }

    /**
     * @return false
     */
    protected boolean isReply() throws MessagingException {
        return false;
    }

    /* ******************************************************************** */
    /* ******************* End of getX and setX methods ******************* */
    /* ******************************************************************** */

}

