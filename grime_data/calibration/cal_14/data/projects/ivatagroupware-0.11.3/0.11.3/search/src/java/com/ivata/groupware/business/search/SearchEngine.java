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
package com.ivata.groupware.business.search;

import java.util.SortedSet;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.mask.util.SystemException;

/**
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since   Apr 9, 2004
 * @version $Revision: 1.4 $
 */
public interface SearchEngine {
    /**
     * <p>removes an item or part of an item from index</p>
     * @param type one of {@link: SearchConstant SearchContant}s values
     * @param id the id of the document, when null, all entries for {@see:item
     * item } will be removed
     * @param item the item id of the library item which this document relates to
     */
    public void removeFromIndex(SecuritySession securitySession,
            final String type,
            final String id,
            final String category)
        throws SystemException;
    /**
     * <p>searches the index for the given query, returns appropriate DOs as a
     * <code>Vector</code>, most relevant documents first</p>
     * @param query the query of one or more space-separated words
     * @param topic the id of the topic of the wanted documents, <code>null</code>
     * when the all topics should be searched
     * @param filter <code>Collection</code> of {@link: SearchConstant SearchContant}s,
     * describing what parts of the system to search, when <code>null</code>,
     * search is performed on all kinds of documents
     * @return <code>Vector</code> of appropriate DOs,
     * most relevant documents first
     *
     * @ejb.interface-method
     *      view-type = "both"
     */
    public SortedSet search(final SecuritySession securitySession,
            final String query)
        throws SystemException;
    /**
     * <p>stems the text content and indexes the document with the provided type
     * {@see SearchConstants} and id</p>
     * @param integerParam the id of the document - subpart of a library item,
     * used as reference when searching
     * @param type one of {@link: SearchConstant SearchContant}s values
     * @param integer2Param TODO
     * @param contentType TODO
     * @param text the content (HTML is converted to plain text) of the document
     * @param topic the id of the topic of this document, <code>null</code> when
     * this document has no topic asscociated
     * @param item the item id of the library item which this document relates to
     * @param textType format of the text, see
     * {@link com.ivata.intranet.jsp.format.FormatConstants}
     */
    public void updateIndex(SecuritySession securitySession,
            Integer integerParam,
            final String type,
            final String category,
            Integer integer2Param,
            final String contentType,
            final String text, int format)
            throws SystemException;
}