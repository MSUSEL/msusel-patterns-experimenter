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
package com.ivata.groupware.admin.security.server;

import com.ivata.mask.util.SystemException;


/**
 * <p>Thrown by subclasses of <code>Session</code> if a user cannot be
 * authenticated.</p>
 *
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 */
public class SecurityServerException extends SystemException {

    /**
     * <p>Create a new exception which wraps the one supplied.</p>
     *
     * @param cause specific exception which caused this one.
     */
    public SecurityServerException(Exception cause) {
        super(cause);
    }
    /**
     * Create a new exception with the causual text as a string message.
     *
     * @param message information about when the error happened.
     */
    public SecurityServerException(String message) {
        super(message);
    }

    /**
     * Create a new exception, giving additional information to the supplied
     * exception which caused this to happen.
     *
     * @param message additional information about when the error happened.
     * @param cause specific exception which caused this one.
     */
    public SecurityServerException(String message, Exception cause) {
        super(message, cause);
    }
}
