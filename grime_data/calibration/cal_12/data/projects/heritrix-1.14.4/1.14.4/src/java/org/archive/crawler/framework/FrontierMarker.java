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
package org.archive.crawler.framework;


/**
 * A marker is a pointer to a place somewhere inside a frontier's list of
 * pending URIs. URIFrontiers use them to allow outside classes (UI for
 * example) to hold (effectively) pointers into the abstract list of pending
 * URIs inside the frontier. If the crawl is not paused (i.e. running) the
 * marker will instantly become out of date.
 *
 * @author Kristinn Sigurdsson
 */
public interface FrontierMarker {

    /**
     * Returns the regular expression that this marker uses.
     * @return the regular expression that this marker uses
     */
    public String getMatchExpression();

    /**
     * Returns the number of the next match after the marker.
     * Alternatively this can be viewed as n-1, where n is the number of items
     * found before the marker.
     * @return the number of the next match after the marker
     */
    public long getNextItemNumber();

    /**
     * Returns false if no more URIs can be found matching the expression
     * beyond those already covered. True otherwise.
     * @return Are there any more matches.
     */
    public boolean hasNext();
}
