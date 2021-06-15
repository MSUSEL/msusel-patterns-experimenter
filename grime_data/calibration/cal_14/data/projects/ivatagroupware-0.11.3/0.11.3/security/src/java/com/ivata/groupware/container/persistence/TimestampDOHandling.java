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
package com.ivata.groupware.container.persistence;

import java.sql.Timestamp;
import java.util.Date;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;

/**
 * <p>
 * This class helps you assign users and time stamps for created by and modified
 * by times
 * </p>
 *
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since ivata groupware 0.9 (2004-06-03)
 * @version $Revision: 1.2 $
 */
public final class TimestampDOHandling {
    public static void add(SecuritySession securitySession, TimestampDO timestampDO) {
        assert (securitySession != null);
        assert (timestampDO != null);

        Timestamp now = new Timestamp(new Date().getTime());
        UserDO user = securitySession.getUser();
        timestampDO.setCreated(now);
        timestampDO.setCreatedBy(user);
        timestampDO.setModified(now);
        timestampDO.setModifiedBy(user);
    }
    public static void amend(SecuritySession securitySession, TimestampDO timestampDO) {
        Timestamp now = new Timestamp(new Date().getTime());
        UserDO user = securitySession.getUser();
        // modified is automagically updated timestampDO.setModified(now);
        timestampDO.setModifiedBy(user);
    }
}
