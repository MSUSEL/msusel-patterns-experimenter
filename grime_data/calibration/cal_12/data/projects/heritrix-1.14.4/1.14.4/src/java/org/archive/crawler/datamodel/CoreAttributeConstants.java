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
 * CrawlURI attribute keys used by the core crawler
 * classes.
 *
 * @author gojomo
 *
 */
public interface CoreAttributeConstants {

    /**
     * Extracted MIME type of fetched content; should be
     * set immediately by fetching module if possible
     * (rather than waiting for a later analyzer)
     */
    public static String A_CONTENT_TYPE = "content-type";

    /**
     * Multiplier of last fetch duration to wait before
     * fetching another item of the same class (eg host)
     */
    public static String A_DELAY_FACTOR = "delay-factor";
    /**
     * Minimum delay before fetching another item of th
     * same class (eg host). Even if lastFetchTime*delayFactor
     * is less than this, this period will be waited.
     */
    public static String A_MINIMUM_DELAY = "minimum-delay";

    public static String A_RRECORD_SET_LABEL = "dns-records";
    public static String A_DNS_FETCH_TIME    = "dns-fetch-time";
    public static String A_DNS_SERVER_IP_LABEL = "dns-server-ip";
    public static String A_FETCH_BEGAN_TIME= "fetch-began-time";
    public static String A_FETCH_COMPLETED_TIME = "fetch-completed-time";
    public static String A_HTTP_TRANSACTION = "http-transaction";
    public static String A_FTP_CONTROL_CONVERSATION = "ftp-control-conversation";
    public static String A_FTP_FETCH_STATUS = "ftp-fetch-status";

    public static String A_RUNTIME_EXCEPTION = "runtime-exception";
    public static String A_LOCALIZED_ERRORS = "localized-errors";

    /** shorthand string tokens indicating notable occurences,
     * separated by commas */
    public static String A_ANNOTATIONS = "annotations";

    public static String A_PREREQUISITE_URI = "prerequisite-uri";
    public static String A_DISTANCE_FROM_SEED = "distance-from-seed";
    public static String A_HTML_BASE = "html-base-href";
    public static String A_RETRY_DELAY = "retry-delay";

    public static String A_META_ROBOTS = "meta-robots";
    /** 
     * Define for org.archive.crawler.writer.MirrorWriterProcessor.
     */
    public static String A_MIRROR_PATH = "mirror-path";

    /**
     * Key to get credential avatars from A_LIST.
     */
    public static final String A_CREDENTIAL_AVATARS_KEY =
        "credential-avatars";
    
    /** a 'source' (usu. URI) that's inherited by discovered URIs */
    public static String A_SOURCE_TAG = "source";
    
    /**
     * Key to (optional) attribute specifying a list of keys that
     * are passed to CandidateURIs that 'descend' (are discovered 
     * via) this URI. 
     */
    public static final String A_HERITABLE_KEYS = "heritable";
    
    /** flag indicating the containing queue should be retired */ 
    public static final String A_FORCE_RETIRE = "force-retire";
    
    /** local override of proxy host */ 
    public static final String A_HTTP_PROXY_HOST = "http-proxy-host";
    /** local override of proxy port */ 
    public static final String A_HTTP_PROXY_PORT = "http-proxy-port";

    /** local override of origin bind address */ 
    public static final String A_HTTP_BIND_ADDRESS = "http-bind-address";
    
    /**
     * Fetch truncation codes present in {@link CrawlURI} annotations.
     * All truncation annotations have a <code>TRUNC_SUFFIX</code> suffix (TODO:
     * Make for-sure unique or redo truncation so definitive flag marked
     * against {@link CrawlURI}).
     */
    public static final String TRUNC_SUFFIX = "Trunc";
    // headerTrunc
    public static final String HEADER_TRUNC = "header" + TRUNC_SUFFIX; 
    // timeTrunc
    public static final String TIMER_TRUNC = "time" + TRUNC_SUFFIX;
    // lenTrunc
    public static final String LENGTH_TRUNC = "len" + TRUNC_SUFFIX;

    /* Duplication-reduction / recrawl / history constants */
    
    /** fetch history array */ 
    public static final String A_FETCH_HISTORY = "fetch-history";
    /** content digest */
    public static final String A_CONTENT_DIGEST = "content-digest";
	/** header name (and AList key) for last-modified timestamp */
    public static final String A_LAST_MODIFIED_HEADER = "last-modified";
	/** header name (and AList key) for ETag */
    public static final String A_ETAG_HEADER = "etag"; 
    /** key for status (when in history) */
    public static final String A_STATUS = "status"; 
    /** reference length (content length or virtual length */
    public static final String A_REFERENCE_LENGTH = "reference-length";

}
