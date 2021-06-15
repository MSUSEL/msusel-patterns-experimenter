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

import java.io.Serializable;

import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.user.UserDO;

/**
 * <p>Represents a user's session, and is used to authenticate her actions
 * throughout the system. You must define a class which implements
 * this interface and which will be returned by <code>SessionServer.login</code>.</p>
 *
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 */
public interface SecuritySession extends Serializable {
    /**
     * <p>
     * Get the access level for this next task to be performed.
     * </p>
     *
     * @return access level
     */
    int getAccess();
    /**
     * <p>
     * The security session can also be used as a container for items which
     * should persist as long as the user is logged in.
     * </p>
     *
     * @param name name of the attribute to retrieve.
     * @return value for this attribute.
     */
    Serializable getAttribute(final String name);

    /**
     * <p>
     * Get the pico container used to access objects for this session.
     * </p>
     *
     * @return valid pico container.
     */
    PicoContainer getContainer();
    /**
     * <p>Get the password associated with this session.</p>
     * <p>
     * (I'd rather not store
     * the password in the sesssion, but <strong>Cyrus IMAP</strong> needs
     * it.)
     * </p>
     *
     * @return password.
     */
    String getPassword();
    /**
     * <p>
     * Get the user who logged in to this security session.
     * </p>
     *
     * @return user who logged in to this security session.
     */
    UserDO getUser();
    /**
     * <p>
     * Find out whether or not this is the guest user.
     * </p>
     * @return <code>true</code> if this is a guest, otherwise
     * <code>false</code>.
     */
    boolean isGuest();
    /**
     * <p>
     * Set the access level for subsequent tasks to be performed.
     * </p>
     *
     * @param accesss access level
     */
    void setAccess(final int access);
    /**
     * <p>
     * The security session can also be used as a container for items which
     * should persist as long as the user is logged in.
     * </p>
     *
     * @param name name of the attribute to set.
     * @param value value for this attribute.
     */
    void setAttribute(final String name,
            final Serializable value);
}
