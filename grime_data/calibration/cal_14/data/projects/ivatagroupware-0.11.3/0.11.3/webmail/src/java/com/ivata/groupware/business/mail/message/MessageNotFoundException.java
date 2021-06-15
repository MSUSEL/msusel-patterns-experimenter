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
package com.ivata.groupware.business.mail.message;

import com.ivata.mask.util.SystemException;


/**
 * <p>Thrown by {@link com.ivata.groupware.business.mail.MailBean MailBean} if there is
 * a requested mail cannot be located.</p>
 *
 * @since 2002-11-10
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 */
public class MessageNotFoundException extends SystemException {
    /**
     * <p>Creates a new <code>MailNotFoundException</code> using the given
     * details about the message which could not be found.</p>
     *
     * @param folderName the name of the folder where the message was being
     *     looked for.
     * @param messageId the unique identifier of the message which could not be
     *     found.
     */
    public MessageNotFoundException(String folderName, String messageId) {
        this (folderName, messageId, (Throwable)null);
    }
    /**
     * <p>Creates a new <code>MailNotFoundException</code> using the given
     * details about the message which could not be found.</p>
     *
     * @param folderName the name of the folder where the message was being
     *     looked for.
     * @param messageId the unique identifier of the message which could not be
     *     found.
     * @param cause root exception which caused this to happen.
     */
    public MessageNotFoundException(String folderName, String messageId,
            Throwable cause) {
        super("ERROR in Mail: no message with id '"
                + messageId
                + "' in folder '"
                + folderName
                + "'.",
                cause);
    }

    /**
     * <p>Creates a new <code>MailNotFoundException</code> using the given
     * details about the message which could not be found. This constructor
     * provides extra information about a message which could not be located due
     * to programming or configuration errors.</p>
     *
     * @param folderName the name of the folder where the message was being
     *     looked for.
     * @param messageId the unique identifier of the message which could not be
     *     found.
     * @param additionalInformation a clear message indicating an additional
     *     cause of the exception (such as "this folder does not exist").
     */
    public MessageNotFoundException(String folderName, String messageId,
            String additionalInformation) {
        this (folderName, messageId, additionalInformation, null);
    }
    /**
     * <p>Creates a new <code>MailNotFoundException</code> using the given
     * details about the message which could not be found. This constructor
     * provides extra information about a message which could not be located due
     * to programming or configuration errors.</p>
     *
     * @param folderName the name of the folder where the message was being
     *     looked for.
     * @param messageId the unique identifier of the message which could not be
     *     found.
     * @param additionalInformation a clear message indicating an additional
     *     cause of the exception (such as "this folder does not exist").
     */
    public MessageNotFoundException(String folderName, String messageId,
            String additionalInformation, Throwable cause) {
        super("ERROR in Mail: could not retrieve message '"
                + messageId
                + "' from folder '"
                + folderName
                + "': "
                + additionalInformation
                + ".",
                cause);
    }
}
