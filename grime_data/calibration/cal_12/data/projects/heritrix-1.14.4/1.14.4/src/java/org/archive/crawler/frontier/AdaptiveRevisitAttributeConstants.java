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
package org.archive.crawler.frontier;

import org.archive.crawler.datamodel.CoreAttributeConstants;

/**
 * Defines static constants for the Adaptive Revisiting module defining data
 * keys in the CrawlURI AList. 
 *
 * @author Kristinn Sigurdsson
 * 
 * @see org.archive.crawler.datamodel.CoreAttributeConstants
 */
public interface AdaptiveRevisitAttributeConstants
extends CoreAttributeConstants {

    /** Designates a field in the CrawlURIs AList for the content digest of
     *  an earlier visit. */
    public static final String A_LAST_CONTENT_DIGEST = "last-content-digest";
    public static final String A_TIME_OF_NEXT_PROCESSING = 
        "time-of-next-processing";
    public static final String A_WAIT_INTERVAL = "wait-interval";
    public static final String A_NUMBER_OF_VISITS = "number-of-visits";
    public static final String A_NUMBER_OF_VERSIONS = "number-of-versions";
    public static final String A_FETCH_OVERDUE = "fetch-overdue";
    
    public static final String A_LAST_ETAG = "last-etag";
    public static final String A_LAST_DATESTAMP = "last-datestamp";
    
    public static final String A_WAIT_REEVALUATED = "wait-reevaluated";
    
    /** Mark a URI to be dropped from revisit handling. Used for custom 
     * processors that want to implement more selective revisiting. 
     * Actual effect depends on whether an alreadyIncluded structure
     * is used. If an alreadyIncluded is used, dropping the URI from 
     * revisit handling means it won't be visited again. If an
     * alreadyIncluded is not used, this merely drops one discovery of 
     * the URI, and it may be rediscovered and thus revisited that way.
     */
    public static final String A_DISCARD_REVISIT = "no-revisit";
    
    /** No knowledge of URI content. Possibly not fetched yet, unable
     *  to check if different or an error occurred on last fetch attempt. */
    public static final int CONTENT_UNKNOWN = -1;
    
    /** URI content has not changed between the two latest, successfully
     *  completed fetches. */
    public static final int CONTENT_UNCHANGED = 0;
    
    /** URI content had changed between the two latest, successfully completed
     *  fetches. By definition, content has changed if there has only been one
     *  successful fetch made. */
    public static final int CONTENT_CHANGED = 1;

    /**
     * Key to use getting state of crawluri from the CrawlURI alist.
     */
    public static final String A_CONTENT_STATE_KEY = "ar-state";
}
