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

import java.io.OutputStream;

/** 
 * Contract exposed by a NewsGroup Article
 */
public interface NNTPArticle {

    /**
     * Gets the group containing this article.
     *
     * @return the group
     */
    NNTPGroup getGroup();

    /**
     * Gets the article number for this article.
     *
     * @return the article number
     */
    int getArticleNumber();

    /**
     * Gets the unique message id for this article.
     *
     * @return the message id
     */
    String getUniqueID();

    /**
     * Writes the whole article to a writer.
     *
     * @param wrt the OutputStream to which the article is written.
     */
    void writeArticle(OutputStream wrt);

    /**
     * Writes the article headers to a writer.
     *
     * @param wrt the OutputStream to which the article is written.
     */
    void writeHead(OutputStream wrt);

    /**
     * Writes the article body to a writer.
     *
     * @param wrt the OutputStream to which the article is written.
     */
    void writeBody(OutputStream wrt);

    /**
     * Writes the article overview to a writer.
     *
     * @param wrt the OutputStream to which the article is written.
     */
    void writeOverview(OutputStream wrt);

    /**
     * Gets the header with the specified headerName.  Returns null
     * if the header doesn't exist.
     *
     * @param headerName the name of the header being retrieved.
     */
    String getHeader(String headerName);
}
