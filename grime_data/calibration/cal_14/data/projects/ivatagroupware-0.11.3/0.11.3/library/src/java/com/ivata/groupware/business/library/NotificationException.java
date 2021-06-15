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
package com.ivata.groupware.business.library;

import java.util.List;

import com.ivata.mask.util.SystemException;

/**
 * An instance of this class is thrown whenever the notification for a library
 * item cannot be sent out.
 *
 * @since ivata groupware 0.10 (2005-01-11)
 * @author Colin MacLeod
 * <a href="mailto:colin.macleod@ivata.com">colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 */
public class NotificationException extends SystemException {
    private List recipientEmailAddresses;
    private String senderEmailAddress;
    /**
     * @param messageParam clear text message l
     */
    public NotificationException(final Throwable causeParam,
            final String senderEmailAddressParam,
            final List recipientEmailAddressesParam) {
        super(causeParam);
        this.recipientEmailAddresses = recipientEmailAddressesParam;
        this.senderEmailAddress = senderEmailAddressParam;
    }

    /**
     * @return Returns the recipientEmailAddresses.
     */
    public List getRecipientEmailAddresses() {
        return recipientEmailAddresses;
    }
    /**
     * @return Returns the senderEmailAddress.
     */
    public String getSenderEmailAddress() {
        return senderEmailAddress;
    }
}
