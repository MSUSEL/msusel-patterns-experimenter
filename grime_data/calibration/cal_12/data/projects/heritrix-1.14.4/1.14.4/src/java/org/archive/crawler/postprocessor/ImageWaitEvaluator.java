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
package org.archive.crawler.postprocessor;

/**
 * A specialized ContentBasedWaitEvaluator. Comes preset with a regular 
 * expression that matches text documents. <code>^image/.*$</code>
 *
 * @author Kristinn Sigurdsson
 * 
 * @see org.archive.crawler.postprocessor.ContentBasedWaitEvaluator
 */
public class ImageWaitEvaluator extends ContentBasedWaitEvaluator {

    private static final long serialVersionUID = -2762377129860398333L;

    protected final static Long DEFAULT_INITIAL_WAIT_INTERVAL =
        new Long(172800); // 2 days

    protected final static String DEFAULT_CONTENT_REGEXPR = "^image/.*$"; //Text

    /**
     * Constructor
     * 
     * @param name The name of the module
     */
    public ImageWaitEvaluator(String name) {
        super(name,"Evaluates how long to wait before fetching a URI again. " +
                "Only handles CrawlURIs whose content type indicates a " +
                "image document (^image/.*$). " +
                "Typically, this processor should be in the post processing " +
                "chain. It will pass if another wait evaluator has already " +
                "processed the CrawlURI.", 
                DEFAULT_CONTENT_REGEXPR,
                DEFAULT_INITIAL_WAIT_INTERVAL,
                DEFAULT_MAX_WAIT_INTERVAL,
                DEFAULT_MIN_WAIT_INTERVAL,
                DEFAULT_UNCHANGED_FACTOR,
                DEFAULT_CHANGED_FACTOR);
    }


}
