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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.mask.util.SystemException;

/**
 * This absrtact class is a wrapper for <code>Map</code>, used to store items
 * in the session.
 *
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since   Mar 29, 2004
 * @version $Revision: 1.3.2.1 $
 */
public abstract class AbstractSecuritySession implements SecuritySession {

    /**
     * Access level.
     */
    private int access;
    /**
     * Stores all attributes which are persisted in this session.
     */
    private Map attributes = new HashMap();
    /**
     * <p>
     * Container used throughout this session.
     * </p>
     */
    private PicoContainer container;
    private String password;

    /**
     * User who is logged in to this session.
     */
    private UserDO user;

    /**
     * Construct a security session for the given user.
     *
     * @param container container used throughout this session.
     * @param user user who is logged in to this session.
     */
    public AbstractSecuritySession(PicoContainer container, UserDO user)
            throws SystemException {
        MutablePicoContainer mutableParent =
            new DefaultPicoContainer(container);
        mutableParent.registerComponentInstance(
            SecuritySession.class, this);
        MutablePicoContainer childContainer = PicoContainerFactory
            .getInstance().override(mutableParent);
        childContainer.registerComponentInstance(SecuritySession.class,
            this);

        this.container = childContainer;
        this.user = user;
    }

    /**
     * <p>
     * Get the access level for the next commands.
     * </p>
     *
     * @return access level for the next commands.
     */
    public final int getAccess() {
        return access;
    }

    /**
     * The security session can also be used as a container for items which
     * should persist as long as the user is logged in.
     *
     * @param name name of the attribute to retrieve.
     * @return value for this attribute.
     * @see com.ivata.groupware.admin.security.server.SecuritySession#getAttribute(String)
     */
    public final Serializable getAttribute(final String name) {
        return (Serializable) attributes.get(name);
    }

    /**
     * <p>
     * Container used throughout this session.
     * </p>
     * @return container used throughout this session.
     */
    public final PicoContainer getContainer() {
        return container;
    }


    /**
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @see com.ivata.groupware.admin.security.server.SecuritySession#getUser()
     */
    public UserDO getUser() {
        return user;
    }

    /**
     * Refer to {@link SecuritySession#isGuest}.
     * @return Refer to {@link SecuritySession#isGuest}.
     */
    public boolean isGuest() {
        return ((user == null)
                || "guest".equals(user.getName()));
    }

    /**
     * <p>Serialize the object from the input stream provided.</p>
     * @exception ClassNotFoundException thrown by
     * <code>ObjectInputStream.defaultReadObject(  )</code>.
     * @exception IOException thrown by
     * <code>ObjectInputStream.defaultReadObject(  )</code>.
     * @param ois the input stream to serialize the object from
     *
     */
    private void readObject(final ObjectInputStream ois) throws ClassNotFoundException,IOException {
        ois.defaultReadObject();
    }

    /**
     * <p>
     * Set the access level for the next commands.
     * </p>
     *
     * @param access access level for the next commands.
     */
    public final void setAccess(final int access) {
        this.access = access;
    }

    /**
     * The security session can also be used as a container for items which
     * should persist as long as the user is logged in.
     *
     * @param name name of the attribute to set.
     * @param value value for this attribute.
     * @see com.ivata.groupware.admin.security.server.SecuritySession#setAttribute(String, java.io.Serializable)
     */
    public final void setAttribute(final String name,
            final Serializable value) {
        attributes.put(name, value);
    }
    /**
     * Refer to {@link #getPassword}.
     * @param passwordParam Refer to {@link #getPassword}.
     */
    public void setPassword(String passwordParam) {
        password = passwordParam;
    }
    /**
     * <p>Serialize the object to the output stream provided.</p>
     * @exception IOException thrown by
     * <code>ObjectOutputStream.defaultWriteObject(  )</code>
     * @param oos the output stream to serialize the object to
     *
     */
    private void writeObject(final ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }
}
