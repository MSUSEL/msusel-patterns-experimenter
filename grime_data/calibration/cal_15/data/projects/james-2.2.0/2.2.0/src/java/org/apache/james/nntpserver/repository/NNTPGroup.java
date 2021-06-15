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
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

/** 
 * Contract exposed by a NewsGroup
 *
 */
public interface NNTPGroup {

    /**
     * Gets the name of the newsgroup
     *
     * @return the newsgroup name
     */
    String getName();

    /**
     * Gets the description of the newsgroup
     *
     * @return the newsgroup description
     */
    String getDescription();

    /**
     * Returns whether posting is allowed to this newsgroup
     *
     * @return whether posting is allowed to this newsgroup
     */
    boolean isPostAllowed();

    /**
     * Gets the number of articles in the group.
     *
     * @return the number of articles in the group.
     */
    int getNumberOfArticles();

    /**
     * Gets the first article number in the group.
     *
     * @return the first article number in the group.
     */
    int getFirstArticleNumber();

    /**
     * Gets the last article number in the group.
     *
     * @return the last article number in the group.
     */
    int getLastArticleNumber();

    /**
     * Gets the article with the specified article number.
     *
     * @param number the article number
     *
     * @return the article
     */
    NNTPArticle getArticle(int number);

    /**
     * Retrieves an iterator of articles in this newsgroup that were
     * posted on or after the specified date.
     *
     * @param dt the Date that acts as a lower bound for the list of
     *           articles
     *
     * @return the article iterator
     */
    Iterator getArticlesSince(Date dt);

    /**
     * Retrieves an iterator of all articles in this newsgroup
     *
     * @return the article iterator
     */
    Iterator getArticles();

    /**
     * Retrieves the group information in a format consistent with
     * a LIST or LIST ACTIVE return line
     *
     * @return the properly formatted string
     */
    String getListFormat();

    /**
     * Retrieves the group information in a format consistent with
     * a LIST NEWSGROUPS return line
     *
     * @return the properly formatted string
     */
    String getListNewsgroupsFormat();

    /**
     * Adds an article to the group based on the data in the
     * stream.
     *
     * @param newsStream the InputStream containing the article data
     *
     * @return the newly created article
     */
    NNTPArticle addArticle(InputStream newsStream) throws IOException;
}
