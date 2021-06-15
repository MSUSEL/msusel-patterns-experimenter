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
package com.ivata.groupware.business.search.item.content;

import com.ivata.groupware.business.search.item.SearchItemDO;
import com.ivata.groupware.container.persistence.BaseDO;

/**
 * <p>
 * Each item searched for in <strong>ivata groupware</strong> can have different
 * content associated with it. Whether it is a page in a library item, or a
 * comment about a library item, for example, it is more efficient to index
 * these separately so they can be updated separately when they change.
 * </p>
 *
 * @since Aug 1, 2004
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 *
 * @hibernate.class
 *      table="search_item_content"
 * @hibernate.cache
 *      usage="read-write"
 */
public class SearchItemContentDO extends BaseDO {

    /**
     * <p>
     * This refers to the item which contains this content.
     * </p>
     */
    private SearchItemDO item;
    /**
     * <p> Unique identifier of the object being indexed. </p>
     */
    private Integer targetId;

    /**
     * <p> This is a text to describe the type of the object being indexed. In
     * <strong>ivata groupware</strong>, this always refers to the class of the
     * indexed object. </p>
     */
    private String type;

    /**
     * <p>
     * This refers to the item which contains this content.
     * </p>
     *
     * @return Returns the item.
     * @hibernate.many-to-one
     *      column="search_item"
     */
    public final SearchItemDO getItem() {
        return item;
    }

    /**
     * <p>
     * Unique identifier of the object being indexed.
     * </p>
     *
     * @return Returns the targetId.
     * @hibernate.property
     *      column="target_id"
     */
    public final Integer getTargetId() {
        return targetId;
    }

    /**
     * <p>
     * This is a text to describe the type of the object being indexed. In
     * <strong>ivata groupware</strong>, this always refers to the class of the
     * indexed object.
     * </p>
     *
     * @return Returns the type.
     * @hibernate.property
     */
    public final String getType() {
        return type;
    }
    /**
     * <p>
     * This refers to the item which contains this content.
     * </p>
     *
     * @param item The item to set.
     */
    public final void setItem(final SearchItemDO item) {
        this.item = item;
    }

    /**
     * <p>
     * Unique identifier of the object being indexed.
     * </p>
     *
     * @param targetId The targetId to set.
     */
    public final void setTargetId(final Integer targetId) {
        this.targetId = targetId;
    }

    /**
     * <p>
     * This is a text to describe the type of the object being indexed. In
     * <strong>ivata groupware</strong>, this always refers to the class of the
     * indexed object.
     * </p>
     *
     * @param type The type to set.
     */
    public final void setType(final String type) {
        this.type = type;
    }
}
