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
package com.ivata.groupware.business.search.index;


import com.ivata.groupware.business.search.item.content.SearchItemContentDO;
import com.ivata.groupware.container.persistence.BaseDO;


/**
 * <p>
 * Each word within an item has an index associated with it. This data object
 * stores that index.
 * </p>
 *
 * @since 2004-07-31
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 *
 * @hibernate.class
 *      table="search_index"
 * @hibernate.cache
 *      usage="read-write"
 */
public class SearchIndexDO  extends BaseDO {
    /**
     * <p>
     * This is the content in the item which this index matches.
     * </p>
     */
    private SearchItemContentDO content;
    /**
     * <p>
     * Indicates how well the query applies to this result.
     * </p>
     */
    private float weight;
    /**
     * <p>
     * Word within the object which is indexed.
     * </p>
     */
    private String word;
    /**
     * <p>
     * This is the content in the item which this index matches.
     * </p>
     *
     * @return Returns the item.
     * @hibernate.many-to-one
     */
    public final SearchItemContentDO getContent() {
        return content;
    }

    /**
     * <p>
     * Indicates how well the query applies to this result.
     * </p>
     *
     * @return Returns the weight.
     * @hibernate.property
     */
    public final float getWeight() {
        return weight;
    }

    /**
     * <p>
     * Word within the object which is indexed.
     * </p>
     *
     * @return Returns the word.
     * @hibernate.property
     */
    public final String getWord() {
        return word;
    }
    /**
     * <p>
     * This is the content in the item which this index matches.
     * </p>
     *
     * @param content Content of the item which this index matches.
     */
    public final void setContent(final SearchItemContentDO content) {
        this.content = content;
    }

    /**
     * <p>
     * Indicates how often the word occurs within this object.
     * </p>
     *
     * @param weight The weight to set.
     */
    public final void setWeight(final float weight) {
        this.weight = weight;
    }
    /**
     * <p>
     * Word within the object which is indexed.
     * </p>
     *
     * @param word The word to set.
     */
    public final void setWord(final String word) {
        this.word = word;
    }
}
