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
package com.ivata.groupware.business.search.item;


import java.util.Set;

import com.ivata.groupware.container.persistence.BaseDO;


/**
 * <p>
 * Each item which can be queried as a result of a search is abstracted by an
 * instance of this class.
 * </p>
 *
 * @since 2004-07-31
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 *
 * @hibernate.class
 *      table="search_item"
 * @hibernate.cache
 *      usage="read-write"
 */
public class SearchItemDO  extends BaseDO {
    /**
     * <p>
     * Extra string to describe the category or topic associated with the indexed
     * object. Used in conjunction with <code>type</code> and
     * <code>targetId</code> to uniquely identify the target.
     * </p>
     */
    private String category;

    /**
     * <p>
     * Refers to all the indexed contents of this item.
     * </p>
     */
    private Set contents;

    /**
     * <p>
     * Unique identifier of the object being indexed.
     * </p>
     */
    private Integer targetId;
    /**
     * <p>
     * This is a text to describe the type of the object being indexed. In
     * <strong>ivata groupware</strong>, this always refers to the class of the
     * indexed object.
     * </p>
     */
    private String type;

    /**
     * <p>
     * Extra string to describe the category or topic associated with the indexed
     * object. Used in conjunction with <code>type</code> and
     * <code>targetId</code> to uniquely identify the target.
     * </p>
     *
     * @return Returns the category.
     * @hibernate.property
     */
    public final String getCategory() {
        return category;
    }
    /**
     * <p>
     * Refers to all the indexed contents of this item.
     * </p>
     *
     * @hibernate.set
     *      cascade="all"
     * @hibernate.collection-key
     *      column="search_item"
     * @hibernate.collection-one-to-many
     *      class="com.ivata.groupware.business.library.page.PageDO"
     * @return Returns the contents.
     */
    public final Set getContents() {
        return contents;
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
     * Extra string to describe the category or topic associated with the indexed
     * object. Used in conjunction with <code>type</code> and
     * <code>targetId</code> to uniquely identify the target.
     * </p>
     *
     * @param category The category to set.
     */
    public final void setCategory(final String category) {
        this.category = category;
    }
    /**
     * <p>
     * Refers to all the indexed contents of this item.
     * </p>
     *
     * @param contents The contents to set.
     */
    public final void setContents(final Set contents) {
        this.contents = contents;
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
