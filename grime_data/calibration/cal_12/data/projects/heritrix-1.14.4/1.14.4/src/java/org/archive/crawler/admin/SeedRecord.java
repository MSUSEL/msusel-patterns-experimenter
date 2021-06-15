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
package org.archive.crawler.admin;

import java.io.Serializable;
import java.util.logging.Logger;

import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.datamodel.CoreAttributeConstants;
import org.archive.crawler.datamodel.CrawlURI;

/**
 * Record of all interesting info about the most-recent
 * processing of a specific seed.
 * 
 * @author gojomo
 */
public class SeedRecord implements CoreAttributeConstants, Serializable {
    private static final long serialVersionUID = -8455358640509744478L;
    private static Logger logger =
        Logger.getLogger(SeedRecord.class.getName());
    private final String uri;
    private int statusCode;
    private String disposition;
    private String redirectUri;
    
    /**
     * Create a record from the given CrawlURI and disposition string
     * 
     * @param curi CrawlURI, already processed as reported to StatisticsTracker
     * @param disposition descriptive disposition string
     * 
     */
    public SeedRecord(CrawlURI curi, String disposition) {
        super();
        this.uri = curi.toString();
        this.statusCode = curi.getFetchStatus();
        this.disposition = disposition;
        if (statusCode==301 || statusCode == 302) {
            for (CandidateURI cauri: curi.getOutCandidates()) {
                if("location:".equalsIgnoreCase(cauri.getViaContext().
                		toString())) {
                    redirectUri = cauri.toString();
                }
            }
        }
    }
    
    /**
     * Constructor for when a CrawlURI is unavailable; such
     * as when considering seeds not yet passed through as
     * CrawlURIs. 
     * 
     * @param uri
     * @param disposition
     */
    public SeedRecord(String uri, String disposition) {
    	this(uri, disposition, -1, null);
    }

    /**
     * Create a record from the given URI, disposition, HTTP status code,
     * and redirect URI.
     * @param uri
     * @param disposition
     * @param statusCode
     * @param redirectUri
     */
    public SeedRecord(String uri, String disposition, int statusCode,
    		String redirectUri) {
        super();
        this.uri = uri;
        this.statusCode = statusCode;
        this.disposition = disposition;
        this.redirectUri = redirectUri;        
    }

    
    /**
     * A later/repeat report of the same seed has arrived; update with
     * latest. Should be rare/never?
     * 
     * @param curi
     */
    public void updateWith(CrawlURI curi,String disposition) {
        if(!this.uri.equals(curi.toString())) {
            logger.warning("SeedRecord URI changed: "+uri+"->"+curi.toString());
        }
        this.statusCode = curi.getFetchStatus();
        this.disposition = disposition;
        if (statusCode==301 || statusCode == 302) {
            for (CandidateURI cauri: curi.getOutCandidates()) {
                if("location:".equalsIgnoreCase(cauri.getViaContext().
                        toString())) {
                    redirectUri = cauri.toString();
                }
            }
        } else {
            redirectUri = null; 
        }
    }
    
    /**
     * @return Returns the disposition.
     */
    public String getDisposition() {
        return disposition;
    }
    /**
     * @return Returns the redirectUri.
     */
    public String getRedirectUri() {
        return redirectUri;
    }
    /**
     * @return Returns the statusCode.
     */
    public int getStatusCode() {
        return statusCode;
    }
    /**
     * @return Returns the uri.
     */
    public String getUri() {
        return uri;
    }
}