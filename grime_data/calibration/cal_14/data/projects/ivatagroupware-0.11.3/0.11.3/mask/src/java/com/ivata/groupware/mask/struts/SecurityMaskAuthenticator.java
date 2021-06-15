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
package com.ivata.groupware.mask.struts;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.mask.web.struts.MaskAuthenticator;

/**
 * <p>
 * Authenticates the current user against the <strong>ivata groupware</strong> security
 * session.
 * </p>
 *
 * @since Nov 12, 2004
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 */

public class SecurityMaskAuthenticator implements MaskAuthenticator, Serializable {

    /**
     * <p>
     * Authenticates the current user against the <strong>ivata groupware</strong> security
     * session.
     * </p>
     *
     * @return <code>null</code>, if this is a valid, logged-in user, otherwise
     * &quot;<code>login</code>&quot;.
     * @see com.ivata.mask.web.struts.MaskAuthenticator#authenticate(javax.servlet.http.HttpSession, boolean)
     */
    public String authenticate(final HttpSession session,
            final boolean login) {
        SecuritySession securitySession = (SecuritySession)
            session.getAttribute("securitySession");

        // if we're logging in, or there is a valid security session, that means
        // carry on as normal...
        if (login
                || ((securitySession != null)
                        && !securitySession.isGuest())) {
            return null;
        }
        // otherwise login...
        return "login";
    }

}
