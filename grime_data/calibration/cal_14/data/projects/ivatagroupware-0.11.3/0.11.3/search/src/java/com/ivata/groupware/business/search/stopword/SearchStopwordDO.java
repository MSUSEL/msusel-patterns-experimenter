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
package com.ivata.groupware.business.search.stopword;

import org.apache.log4j.Logger;

import com.ivata.groupware.container.persistence.BaseDO;

/**
 * This table stores 'stopwords' which are treated as punctuation when
 * indexing the search engine.
 * 
 * @since ivata groupware 0.11 (29-Apr-2005)
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.1 $
 * 
 * @hibernate.class
 *      table="search_stopword"
 */
public class SearchStopwordDO extends BaseDO {
    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger
            .getLogger(SearchStopwordDO.class);

    /**
     * Refer to {@link #getWord}.
     */
    private String word;
    /**
     * The stopword.
     *
     * @return Returns the stopword.
     * @hibernate.property
     */
    public String getWord() {
        return word;
    }
    /**
     * Refer to {@link #getWord}.
     * @param wordParam Refer to {@link #getWord}.
     */
    public void setWord(String wordParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting word. Before '" + word + "', after '"
                    + wordParam + "'");
        }
        word = wordParam;
    }
}
