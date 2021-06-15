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
package org.apache.james.imapserver;

import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.component.Component;
import org.apache.avalon.framework.context.Contextualizable;

/**
 * Interface for objects representing an IMAP4rev1 mailbox (folder) with
 * embedded Access Control List.
 *
 * Reference: RFC 2060
 * @version 0.1 on 14 Dec 2000
 * @see Mailbox
 * @see ACL
 */
public interface ACLMailbox
    extends ACL, Mailbox, Component, Contextualizable, Initializable, Disposable {

    /**
     * Set the details particular to this Mailbox. Should only be called once,
     * at creation, and not when restored from storage.
     *
     * @param user String email local part of owner of a personal mailbox.
     * @param abName String absolute, ie user-independent, name of mailbox.
     * @param initialAdminUser String email local-part of a user who will be assigned admin rights on this mailbox
     */
    void prepareMailbox( String user, String absName, String initialAdminUser, int uidValidity );

    /**
     * Re-initialises mailbox when restored from storage. Must be called after
     * setConfiguration, setContext, setComponentManager, if they are called,
     * but before any opertional methods are called.
     */
    void reinitialize()
        throws Exception;

    /**
     * Permanently deletes the mailbox.
     */
    void removeMailbox();
}


