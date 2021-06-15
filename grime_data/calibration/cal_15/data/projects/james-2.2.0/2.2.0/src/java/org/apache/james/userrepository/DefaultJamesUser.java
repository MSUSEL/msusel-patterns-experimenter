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
package org.apache.james.userrepository;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.james.services.JamesUser;
import org.apache.mailet.MailAddress;

/**
 * Implementation of User Interface.
 *
 *
 * @version $Revision: 1.6.4.3 $
 */

public class DefaultJamesUser 
        extends DefaultUser
        implements JamesUser, Initializable {

    /**
     * Whether forwarding is enabled for this user.
     */
    private boolean forwarding;

    /**
     * The mail address to which this user's email is forwarded.
     */
    private MailAddress forwardingDestination;

    /**
     * Is this user an alias for another username on the system.
     */
    private boolean aliasing;


    /**
     * The user name that this user name is aliasing.
     */
    private String alias;

    public DefaultJamesUser(String name, String alg) {
        super(name, alg);
    }

    public DefaultJamesUser(String name, String passwordHash, String hashAlg) {
        super(name, passwordHash, hashAlg);
    }


    /**
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() {
        forwarding = false;
        forwardingDestination = null;
        aliasing = false;
        alias = "";
    }

    /**
     * Set whether mail to this user is to be forwarded to another
     * email address
     *
     * @param forward whether mail is forwarded
     */
    public void setForwarding(boolean forward) {
        forwarding = forward;
    }

    /**
     * Get whether mail to this user is to be forwarded to another
     * email address.
     *
     * @return forward whether mail is forwarded
     */
    public boolean getForwarding() {
        return forwarding;
    }

    
    /**
     * Set the destination address to which mail to this user
     * will be forwarded.
     *
     * @param address the forward-to address
     */
    public boolean setForwardingDestination(MailAddress address) {
        /* TODO: Some verification would be good */
        forwardingDestination = address;
        return true;
    }

    /**
     * Get the destination address to which mail to this user
     * will be forwarded.
     *
     * @return the forward-to address
     */
    public MailAddress getForwardingDestination() {
        return forwardingDestination;
    }

    /**
     * Set whether this user id is an alias.
     *
     * @param alias whether this id is an alias
     */
    public void setAliasing(boolean alias) {
        aliasing = alias;
    }

    /**
     * Get whether this user id is an alias.
     *
     * @return whether this id is an alias
     */
    public boolean getAliasing() {
        return aliasing;
    }

    /**
     * Set the user id for which this id is an alias.
     *
     * @param address the user id for which this id is an alias
     */
    public boolean setAlias(String address) {
        /* TODO: Some verification would be good */
        alias = address;
        return true;
    }

    /**
     * Get the user id for which this id is an alias.
     *
     * @return the user id for which this id is an alias
     */
    public String getAlias() {
        return alias;
    }
}
