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

import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.mask.util.SystemException;

/**
 * <p>
 * TODO
 * </p>
 *
 * @since Nov 2, 2004
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 */

public class TestSecurityServer implements SecurityServer {

    /* (non-Javadoc)
     * @see com.ivata.groupware.admin.security.server.SecurityServer#addUser(String, String)
     */
    public void addUser(final SecuritySession securitySession,
            final String userName,
            final String fullName)
            throws SystemException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.ivata.groupware.admin.security.server.SecurityServer#checkPassword(String, String)
     */
    public void checkPassword(final SecuritySession securitySession,
            final String userName,
            final String password)
            throws SystemException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.ivata.groupware.admin.security.server.SecurityServer#getSystemUserName(String)
     */
    public final String getSystemUserName(final SecuritySession securitySession,
            final String userName) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.ivata.groupware.admin.security.server.SecurityServer#getUserNameFromSystemUserName(String)
     */
    public final String getUserNameFromSystemUserName(final SecuritySession securitySession,
            final String systemUserName) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Refer to {@link }.
     *
     * @param userNameParam
     * @return
     * @see com.ivata.groupware.admin.security.server.SecurityServer#isUser(java.lang.String)
     */
    public boolean isUser(final SecuritySession securitySession,
            final String userNameParam) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.ivata.groupware.admin.security.server.SecurityServer#login(com.ivata.groupware.admin.security.user.UserDO, String)
     */
    public SecuritySession login(final UserDO user,
            final String password)
            throws SystemException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.ivata.groupware.admin.security.server.SecurityServer#loginGuest()
     */
    public SecuritySession loginGuest() throws SystemException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.ivata.groupware.admin.security.server.SecurityServer#removeUser(String)
     */
    public void removeUser(final SecuritySession securitySession,
            final String userName) throws SystemException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.ivata.groupware.admin.security.server.SecurityServer#setPassword(String, String)
     */
    public final void setPassword(final SecuritySession securitySession,
            final String userName,
            final String password)
            throws SystemException {
        // TODO Auto-generated method stub

    }

}
