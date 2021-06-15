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
import java.util.HashMap;
import java.util.Map;

import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.user.UserDO;

/**
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since   Jun 2, 2004
 * @version $Revision: 1.2 $
 */
public class TestSecuritySession implements SecuritySession {
    int access;
    Map attributes = new HashMap();
    UserDO user;

    /**
     */
    public TestSecuritySession(UserDO user) {
        super();
        this.user = user;
    }

    /**
     * @see com.ivata.groupware.admin.security.server.SecuritySession#getAttribute(String)
     */
    public final Serializable getAttribute(final String name) {
        return (Serializable) attributes.get(name);
    }

    /**
     * @see com.ivata.groupware.admin.security.server.SecuritySession#setAttribute(String, java.io.Serializable)
     */
    public final void setAttribute(final String name,
            final Serializable value) {
        attributes.put(name, value);
    }

    /**
     * @see com.ivata.groupware.admin.security.server.SecuritySession#getUser()
     */
    public UserDO getUser() {
        return user;
    }

    /**
     * @see com.ivata.groupware.admin.security.server.SecuritySession#getAccess()
     */
    public final int getAccess() {
        return access;
    }

    /**
     * @see com.ivata.groupware.admin.security.server.SecuritySession#setAccess(int)
     */
    public final void setAccess(final int access) {
        this.access = access;
    }

    /**
     * @see com.ivata.groupware.admin.security.server.SecuritySession#getContainer()
     */
    public final PicoContainer getContainer() {
        return null;
    }

    /**
     * Refer to {@link }.
     * @see com.ivata.groupware.admin.security.server.SecuritySession#isGuest()
     * @return
     */
    public boolean isGuest() {
        return false;
    }

    /**
     * Refer to {@link SecuritySession#getPassword}.
     * @return Refer to {@link SecuritySession#getPassword}.
     */
    public String getPassword() {
        return null;
    }

}
