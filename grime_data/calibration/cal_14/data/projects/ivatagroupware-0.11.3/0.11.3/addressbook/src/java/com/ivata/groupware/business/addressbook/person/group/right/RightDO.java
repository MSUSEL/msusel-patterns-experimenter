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
package com.ivata.groupware.business.addressbook.person.group.right;

import com.ivata.groupware.business.addressbook.person.group.GroupDO;
import com.ivata.groupware.business.addressbook.person.group.right.detail.RightDetailDO;
import com.ivata.groupware.container.persistence.BaseDO;


/**
 * <p>Assigns the right to perform a specific action to a group of people. Each
 * right allows a group to add, amend, view or delete entries from the intranet.
 * The element which is allowed to be changed by this right is described by the
 * {@link com.ivata.groupware.adddressbook.person.group.right.detail.RightDetailBean
 * detail EJB} associated with it.</p>
 *
 * @since 2002-05-19
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @see
 *     com.ivata.groupware.adddressbook.person.group.right.detail.RightDetailBean
 * @see RightConstants
 * @version $Revision: 1.4 $
 *
 * @hibernate.class
 *      table="person_group_right"
 * @hibernate.cache
 *      usage="read-write"
 */
public class RightDO  extends BaseDO {
    /**
     * <p>Access level allowed. This will give the group the right to
     * add, amend, view or delete the associated data, as defined in
     * {@link RightConstants RightConstants}.</p>
     */
    private Integer access;

    /**
     * <p>The detail, which provides clear text information about this
     * right.</p>
     */
    private RightDetailDO detail;

    /**
     * <p>Group who is entitled by this right. Rights in ivata groupware
     * are allocated on a per-group basis.</p>
     */
    private GroupDO group;

    /**
     * <p>Id of the DO this right targets. Some rights allow a group
     * access to a specific instance of an EJB. This id is the id of that
     * instance.</p>
     */
    private Integer targetId;

    /**
     * <p>Get the access level allowed. This will give the group the right to
     * add, amend, view or delete the associated data, as defined in
     * {@link RightConstants RightConstants}.</p>
     *
     * @return access level allowed, as defined in {@link RightConstants
     *     RightConstants}.
     *
     * @hibernate.property
     *      column="access"
     */
    public final Integer getAccess() {
        return access;
    }

    /**
     * <p>Get the detail, which provides clear text information about this
     * right.</p>
     *
     * @return instance providing clear text information about this right
     *
     * @hibernate.many-to-one
     *      column="detail"
     */
    public final RightDetailDO getDetail() {
        return detail;
    }
    /**
     * <p>Get the group who is entitled by this right. Rights in ivata groupware
     * are allocated on a per-group basis.</p>
     *
     * @return group entitled to this right.
     *
     * @hibernate.many-to-one
     *      column="person_group"
     */
    public final GroupDO getGroup() {
        return group;
    }
    /**
     * <p>Get the id of the DO this right targets. Some rights allow a group
     * access to a specific instance of an EJB. This id is the id of that
     * instance.</p>
     *
     * @return the id of the EJB the right targets, or null if this right is not
     *      specific to a single instance.
     *
     * @hibernate.property
     *      column="target_id"
     */
    public final Integer getTargetId() {
        return targetId;
    }

    /**
     * <p>Set the access level allowed. This will give the group the right to
     * add, amend, view or delete the associated data, as defined in
     * {@link RightConstants RightConstants}.</p>
     *
     * @param access access level allowed, as defined in {@link RightConstants
     *     RightConstants}
     */
    public final void setAccess(final Integer access) {
        this.access = access;
    }

    /**
     * <p>Set the detail, which provides clear text information about this
     * right.</p>
     *
     * @param detail instance providing clear text information about this right
     */
    public final void setDetail(final RightDetailDO detail) {
        this.detail = detail;
    }

    /**
     * <p>Set the group who is entitled by this right. Rights in ivata groupware
     * are allocated on a per-group basis.</p>
     *
     * @param group group bean entitled to this right.
     */
    public final void setGroup(final GroupDO group) {
        this.group = group;
    }

    /**
     * <p>Set the id of the DO this right targets. Some rights allow a group
     * access to a specific instance of an EJB. This id is the id of that
     * instance.</p>
     *
     * @param targetId the id of the EJB the right targets, or null if this
     *     right is not specific to a single instance.
     */
    public final void setTargetId(final Integer targetId) {
        this.targetId = targetId;
    }
}
