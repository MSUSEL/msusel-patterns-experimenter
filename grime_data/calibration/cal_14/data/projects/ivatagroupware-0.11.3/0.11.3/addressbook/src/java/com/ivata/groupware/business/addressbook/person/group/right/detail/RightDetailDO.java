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
package com.ivata.groupware.business.addressbook.person.group.right.detail;

import com.ivata.groupware.container.persistence.BaseDO;


/**
 * <p>Represents details a right for group of people to do something. This
 * class provides detail explaining in clear text what the right allows.</p>
 *
 * @since 2002-05-15
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 * @see com.ivata.groupware.adddressbook.person.group.right.RightBean
 *
 * @hibernate.class
 *      table="person_group_right_detail"
 * @hibernate.cache
 *      usage="read-write"
 */
public class RightDetailDO  extends BaseDO {

    /**
     * <p>Full and detailed description of the entitlements the right
     * offers.</p>
     */
    private String description;
    /**
     * <p>Name of the right is a clear-text field which uniquely which
     * identifies what it is the group will be allowed to do. This name can
     * include spaces.</p>
     */
    private String name;
    /**
     * <p>Get full and detailed description of the entitlements the right
     * offers.</p>
     *
     * @return full and detailed description of the entitlements the right
     *     offers.
     * @hibernate.property
     */
    public final String getDescription() {
        return description;
    }
    /**
     * <p>Get the name of the right is a clear-text field which uniquely which
     * identifies what it is the group will be allowed to do. This name can
     * include spaces.</p>
     *
     * @return clear text name of the right, possibly including spaces.
     * @hibernate.property
     */
    public final String getName() {
        return name;
    }

    /**
     * <p>Set full and detailed description of the entitlements the right offers.</p>
     *
     * @param description full and detailed description of the entitlements the
     *     right offers.
     */
    public final void setDescription(final String description) {
        this.description = description;
    }

    /**
     * <p>Set the name of the right is a clear-text field which uniquely which identifies what it is the group will be allowed to do. This name can include spaces.</p>
     *
     * @param name clear text name of the right, possibly including spaces.
     */
    public final void setName(final String name) {
        this.name = name;
    }
}
