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

import org.apache.mailet.MailAddress;

/**
 * Interface for objects representing users of an email/ messaging system.
 *
 *
 * @version $Revision: 1.4.4.3 $
 */

public interface JamesUser extends User {

    /**
     * Change password to pass. Return true if successful.
     *
     * @param pass the new password
     * @return true if successful, false otherwise
     */
    boolean setPassword(String pass);

    /**
     * Indicate if mail for this user should be forwarded to some other mail
     * server.
     *
     * @param forward whether email for this user should be forwarded
     */
    void setForwarding(boolean forward);

    /** 
     * Return true if mail for this user should be forwarded
     */
    boolean getForwarding();

    /**
     * <p>Set destination for forwading mail</p>
     * <p>TODO: Should we use a MailAddress?</p>
     *
     * @param address the forwarding address for this user
     */
    boolean setForwardingDestination(MailAddress address);

    /**
     * Return the destination to which email should be forwarded
     */
    MailAddress getForwardingDestination();

    /**
     * Indicate if mail received for this user should be delivered locally to
     * a different address.
     */
    void setAliasing(boolean alias);

    /**
     * Return true if emails should be delivered locally to an alias.
     */
    boolean getAliasing();

    /**
     * Set local address to which email should be delivered.
     *
     * @return true if successful
     */
    boolean setAlias(String address);

    /**
     * Get local address to which mail should be delivered.
     */
    String getAlias();


}
