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

import org.apache.log4j.Logger;

import com.ivata.groupware.admin.security.user.UserDO;

/**
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since   Mar 28, 2004
 * @version $Revision: 1.3.2.1 $
 */
public abstract class TimestampDO extends BaseDO {
    private static Logger log = Logger.getLogger(TimestampDO.class);
    /**
     * <p>The date and time when the DO was created.</p>
     */
    Timestamp created;

    /**
     * <p>User who created this DO.</p>
     */
    private UserDO createdBy;

    /**
     * <p>The date and time when the DO was last modified.</p>
     */
    Timestamp modified;

    /**
     * <p>User who last modified this DO.</p>
     */
    private UserDO modifiedBy;
    /**
     * <p>Get the date and time when the DO was created.</p>
     *
     * @return the date and time when the DO was created.
     * @hibernate.property
     */
    public Timestamp getCreated() {
        return created;
    }

    /**
     * <p>Get the user who created this DO.</p>
     *
     * @return the user who created this DO.
     * @hibernate.many-to-one
     *      column="created_by"
     */
    public UserDO getCreatedBy() {
        return createdBy;
    }
    /**
     * <p>Get the date and time when the DO was last modified.</p>
     *
     * @return the date and time when the DO was last modified.
     * @hibernate.version
     */
    public Timestamp getModified() {
        return modified;
    }

    /**
     * <p>Get the username of the user who last modified this DO.</p>
     *
     * @return the user name for the user who last modified this DO.
     * @hibernate.many-to-one
     *      column="modified_by"
     */
    public UserDO getModifiedBy() {
        return modifiedBy;
    }

    /**
     * Refer to {@link #getCreated}.
     * @param createdParam The created to set.
     */
    public void setCreated(Timestamp createdParam) {
        if (log.isDebugEnabled()) {
            log.debug("setCreated before: '" + created
                    + "', after: '" + createdParam + "': "
                    + toString());
        }
        assert (createdParam != null);
        created = createdParam;
    }
    /**
     * Refer to {@link #getCreatedBy}.
     * @param createdByParam The createdBy to set.
     */
    public void setCreatedBy(UserDO createdByParam) {
        if (log.isDebugEnabled()) {
            log.debug("setCreatedBy before: '" + createdBy
                    + "', after: '" + createdByParam + "': "
                    + toString());
        }
        assert (createdByParam != null);
        createdBy = createdByParam;
    }
    /**
     * Refer to {@link #getModified}.
     * @param modifiedParam The modified to set.
     */
    public void setModified(Timestamp modifiedParam) {
        if (log.isDebugEnabled()) {
            log.debug("setModified before: '" + modified
                    + "', after: '" + modifiedParam + "': "
                    + toString());
        }
        assert (modifiedParam != null);
        modified = modifiedParam;
    }
    /**
     * Refer to {@link #getModifiedBy}.
     * @param modifiedByParam The modifiedBy to set.
     */
    public void setModifiedBy(UserDO modifiedByParam) {
        if (log.isDebugEnabled()) {
            log.debug("setModifiedBy before: '" + modifiedBy
                    + "', after: '" + modifiedByParam + "': "
                    + toString());
        }
        assert (modifiedByParam != null);
        modifiedBy = modifiedByParam;
   }
    /**
     * Refer to {@link }.
     * Overridden to output current time and user info - useful for debugging.
     *
     * @return string with current class, id and user/time information.
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return super.toString()
                + "[ Created "
                + created
                + " by "
                + ((createdBy == null)
                        ? "[nobody]"
                        : createdBy.getDisplayName())
                + ", Modified "
                + modified
                + " by "
                + ((modifiedBy == null)
                        ? "[nobody]"
                                : modifiedBy.getDisplayName())
                + " ]";
    }
}
