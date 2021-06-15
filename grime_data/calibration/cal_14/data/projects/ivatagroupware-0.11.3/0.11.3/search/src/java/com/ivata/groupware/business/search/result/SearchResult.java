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
package com.ivata.groupware.business.search.result;

import com.ivata.groupware.business.search.item.SearchItemDO;

/**
 * <p>
 * Represents a single result as returned by the search engine.
 * </p>
 *
 * @since Jul 31, 2004
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 */

public class SearchResult implements Comparable {
    /**
     * <p>
     * The search item which matched a query.
     * </p>
     */
    private SearchItemDO item;

    /**
     * <p>
     * Indicates how well the item matched the query.
     * </p>
     */
    private float weight;
    /**
     * <p>
     * <code>Comparable</code> method used to sort items. This compares the
     * weight of the two results.
     * </p>
     *
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final Object compare) {
        SearchResult otherResult = (SearchResult) compare;
        if (otherResult.weight == weight) {
            return 0;
        }
        if (otherResult.weight > weight) {
            return 1;
        }
        return -1;
    }

    /**
     * <p>
     * The search item which matched a query.
     * </p>
     *
     * @return Returns the item.
     */
    public final SearchItemDO getItem() {
        return item;
    }
    /**
     * <p>
     * Indicates how well the item matched the query.
     * </p>
     *
     * @return Returns the weight.
     */
    public final float getWeight() {
        return weight;
    }
    /**
     * <p>
     * The search item which matched a query.
     * </p>
     *
     * @param item The item to set.
     */
    public final void setItem(final SearchItemDO item) {
        this.item = item;
    }
    /**
     * <p>
     * Indicates how well the item matched the query.
     * </p>
     *
     * @param weight The weight to set.
     */
    public final void setWeight(final float weight) {
        this.weight = weight;
    }

}
