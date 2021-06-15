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
package org.apache.james.services;

import org.apache.james.core.MailImpl;

import javax.mail.MessagingException;

import java.util.Collection;
import java.util.Iterator;

/**
 * Interface for a Repository to store Mails.
 * @version 1.0.0, 24/04/1999
 */
public interface MailRepository {

    /**
     * Define a MAIL repository. MAILS are stored in the specified
     * destination.
     */
    String MAIL = "MAIL";


    /**
     * Stores a message in this repository. Shouldn't this return the key
     * under which it is stored?
     *
     * @param mc the mail message to store
     */
    void store(MailImpl mc) throws MessagingException;

    /**
     * List string keys of messages in repository.
     *
     * @return an <code>Iterator</code> over the list of keys in the repository
     *
     */
    Iterator list() throws MessagingException;

    /**
     * Retrieves a message given a key. At the moment, keys can be obtained
     * from list() in superinterface Store.Repository
     *
     * @param key the key of the message to retrieve
     * @return the mail corresponding to this key, null if none exists
     */
    MailImpl retrieve(String key) throws MessagingException;

    /**
     * Removes a specified message
     *
     * @param mail the message to be removed from the repository
     */
    void remove(MailImpl mail) throws MessagingException;

    /**
     * Remove an Collection of mails from the repository
     *
     * @param mails The Collection of <code>MailImpl</code>'s to delete
     * @since 2.2.0
     */
     void remove(Collection mails) throws MessagingException;

    /**
     * Removes a message identified by key.
     *
     * @param key the key of the message to be removed from the repository
     */
    void remove(String key) throws MessagingException;

    /**
     * Obtains a lock on a message identified by key
     *
     * @param key the key of the message to be locked
     *
     * @return true if successfully obtained the lock, false otherwise
     */
    boolean lock(String key) throws MessagingException;

    /**
     * Releases a lock on a message identified the key
     *
     * @param key the key of the message to be unlocked
     *
     * @return true if successfully released the lock, false otherwise
     */
    boolean unlock(String key) throws MessagingException;
}
