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
package org.archive.crawler.datamodel;

/**
 * Constant flag codes to be used, in lieu of per-protocol
 * codes (like HTTP's 200, 404, etc.), when network/internal/
 * out-of-band conditions occur.
 *
 * The URISelector may use such codes, along with user-configured
 * options, to determine whether, when, and how many times
 * a CrawlURI might be reattempted.
 *
 * @author gojomo
 *
 */
public interface FetchStatusCodes {
    /** fetch never tried (perhaps protocol unsupported or illegal URI) */
    public static final int S_UNATTEMPTED = 0;
    /** DNS lookup failed */
    public static final int S_DOMAIN_UNRESOLVABLE = -1;  //
    /** HTTP connect failed */
    public static final int S_CONNECT_FAILED = -2;       //
    /** HTTP connect broken */
    public static final int S_CONNECT_LOST = -3;         //
    /** HTTP timeout (before any meaningful response received) */
    public static final int S_TIMEOUT = -4;              //
    /** Unexpected runtime exception; see runtime-errors.log */
    public static final int S_RUNTIME_EXCEPTION = -5;    //
    /** DNS prerequisite failed, precluding attempt */
    public static final int S_DOMAIN_PREREQUISITE_FAILURE = -6; //
    /** URI recognized as unsupported or illegal)  */
    public static final int S_UNFETCHABLE_URI = -7;      //
    /** multiple retries all failed */
    public static final int S_TOO_MANY_RETRIES = -8;     //

    /** temporary status assigned URIs awaiting preconditions; appearance in
     *  logs is a bug */
    public static final int S_DEFERRED = -50;
    /** URI could not be queued in Frontier; when URIs are properly
     * filtered for format, should never occur */
    public static final int S_UNQUEUEABLE = -60;
    
    /** Robots prerequisite failed, precluding attempt */
    public static final int S_ROBOTS_PREREQUISITE_FAILURE = -61; //
    /** DNS prerequisite failed, precluding attempt */
    public static final int S_OTHER_PREREQUISITE_FAILURE = -62; //
    /** DNS prerequisite failed, precluding attempt */
    public static final int S_PREREQUISITE_UNSCHEDULABLE_FAILURE = -63; //
    
    /** synthetic status, used when some other status (such as connection-lost)
     * is considered by policy the same as a document-not-found */
    public static final int S_DEEMED_NOT_FOUND = -404; //

    /** severe java 'Error' conditions (OutOfMemoryError, StackOverflowError,
     *  etc.) during URI processing */
    public static final int S_SERIOUS_ERROR = -3000;     //

    /** 'chaff' detection of traps/content of negligible value applied */
    public static final int S_DEEMED_CHAFF = -4000;
    /** overstepped link hops */
    public static final int S_TOO_MANY_LINK_HOPS = -4001;
    /** overstepped embed/trans hops */
    public static final int S_TOO_MANY_EMBED_HOPS = -4002;
    /** out-of-scope upoin reexamination (only when scope changes during
     *  crawl) */
    public static final int S_OUT_OF_SCOPE = -5000;
    /** blocked from fetch by user setting. */
    public static final int S_BLOCKED_BY_USER = -5001;
    /**
     * Blocked by custom prefetcher processor.
     * A check against scope or against filters in a custom prefetch
     * processor rules CrawlURI should not be crawled.
     * TODO: Add to documentation and help page.
     */
    public static final int S_BLOCKED_BY_CUSTOM_PROCESSOR = -5002;
    /**
     * Blocked due to exceeding an established quota.
     * TODO: Add to documentation and help page.
     */
    public static final int S_BLOCKED_BY_QUOTA = -5003;
    /**
     * Blocked due to exceeding an established runtime.
     * TODO: Add to documentation and help page.
     */
    public static final int S_BLOCKED_BY_RUNTIME_LIMIT = -5004;
    /** deleted from frontier by user */
    public static final int S_DELETED_BY_USER = -6000;

    /** Processing thread was killed */
    public static final int S_PROCESSING_THREAD_KILLED = -7000;

    /** robots rules precluded fetch */
    public static final int S_ROBOTS_PRECLUDED = -9998;

    /** DNS success */
    public static final int S_DNS_SUCCESS = 1;
    /** InetAddress.getByName success */
    public static final int S_GETBYNAME_SUCCESS = 1001;
}


