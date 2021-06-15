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
package org.apache.james.nntpserver.repository;

import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;

/**
 * Abstraction of entire NNTP Repository.
 */
public interface NNTPRepository {

    /**
     * Gets the group with the specified name from within the repository.
     *
     * @param groupName the name of the group to retrieve
     *
     * @return the group
     */
    NNTPGroup getGroup(String groupName);

    /**
     * Gets the article with the specified id from within the repository.
     *
     * @param id the id of the article to retrieve
     *
     * @return the article
     */
    NNTPArticle getArticleFromID(String id);

    /**
     * Creates an article in the repository from the data in the reader.
     * TODO: Change this to be more OO and pass in a MimeMessage
     *
     * @param in the InputStream that serves as a source for the message data.
     */
    void createArticle(InputStream in);

    /**
     * Gets all groups that match the wildmat string
     *
     * @param wildmat the wildmat parameter
     *
     * @return an iterator containing the groups retrieved
     */
    Iterator getMatchedGroups(String wildmat);

    /**
     * Gets all groups added since the specified date
     *
     * @param dt the Date that serves as a lower bound
     *
     * @return an iterator containing the groups retrieved
     */
    Iterator getGroupsSince(Date dt);

    /**
     * Gets all articles posted since the specified date
     *
     * @param dt the Date that serves as a lower bound
     *
     * @return an iterator containing the articles retrieved
     */
    Iterator getArticlesSince(Date dt);

    /**
     * Returns whether this repository is read only.
     *
     * @return whether this repository is read only
     */
    boolean isReadOnly();

    /**
     * Returns the ordered array of header names (including the trailing colon on each)
     * returned in overview format for articles stored in this repository.
     */
    String[] getOverviewFormat();

}
