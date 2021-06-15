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
package org.archive.crawler.extractor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.httpclient.URIException;
import org.archive.crawler.datamodel.CoreAttributeConstants;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.net.LaxURLCodec;
import org.archive.net.UURI;
import org.archive.util.TextUtils;

/**
 * An extractor for finding URIs inside other URIs. Unlike most other
 * extractors, this works on URIs discovered by previous extractors. Thus 
 * it should appear near the end of any set of extractors.
 *
 * Initially, only finds absolute HTTP(S) URIs in query-string or its 
 * parameters.
 *
 * TODO: extend to find URIs in path-info
 *
 * @author Gordon Mohr
 *
 **/

public class ExtractorURI extends Extractor implements CoreAttributeConstants {

    private static final long serialVersionUID = -6273897743240970822L;

    private static Logger LOGGER =
        Logger.getLogger(ExtractorURI.class.getName());

    static final String ABS_HTTP_URI_PATTERN = "^https?://[^\\s<>]*$";
    
    // FIXME: these counters are not incremented atomically; totals may not
    // be correct
    private long numberOfCURIsHandled = 0;
    private long numberOfLinksExtracted = 0;

    /**
     * Constructor
     * 
     * @param name
     */
    public ExtractorURI(String name) {
        super(name, "URI Extractor. Extracts links inside other " +
                "discovered URIs. Should appear last among extractors.");
    }

    /**
     * Perform usual extraction on a CrawlURI
     * 
     * @param curi Crawl URI to process.
     */
    public void extract(CrawlURI curi) {

        this.numberOfCURIsHandled++;
        // use array copy because discoveriess will add to outlinks
        Collection<Link> links = curi.getOutLinks();
        Link[] sourceLinks = links.toArray(new Link[links.size()]);
        for (Link wref: sourceLinks) {
            extractLink(curi,wref);
        }
    }

    /**
     * Consider a single Link for internal URIs
     * 
     * @param curi CrawlURI to add discoveries to 
     * @param wref Link to examine for internal URIs
     */
    protected void extractLink(CrawlURI curi, Link wref) {
        UURI source = UURI.from(wref.getDestination());
        if(source == null) {
            // shouldn't happen
            return; 
        }
        List<String> found = extractQueryStringLinks(source);
        for (String uri : found) {
            try {
                curi.createAndAddLink(
                        uri, 
                        Link.SPECULATIVE_MISC,
                        Link.SPECULATIVE_HOP);
                numberOfLinksExtracted++;
            } catch (URIException e) {
                LOGGER.log(Level.FINE, "bad URI", e);
            }
        }
        // TODO: consider path URIs too
        
    }

    /**
     * Look for URIs inside the supplied UURI.
     * 
     * Static for ease of testing or outside use. 
     * 
     * @param source UURI to example
     * @return List of discovered String URIs.
     */
    protected static List<String> extractQueryStringLinks(UURI source) {
        List<String> results = new ArrayList<String>(); 
        String decodedQuery;
        try {
            decodedQuery = source.getQuery();
        } catch (URIException e1) {
            // shouldn't happen
            return results;
        }
        if(decodedQuery==null) {
            return results;
        }
        // check if full query-string appears to be http(s) URI
        Matcher m = TextUtils.getMatcher(ABS_HTTP_URI_PATTERN,decodedQuery);
        if(m.matches()) {
            TextUtils.recycleMatcher(m);
            results.add(decodedQuery);
        }
        // split into params, see if any param value is http(s) URI
        String rawQuery = new String(source.getRawQuery());
        String[] params = rawQuery.split("&");
        for (String param : params) {
            String[] keyVal = param.split("=");
            if(keyVal.length==2) {
                String candidate;
                try {
                    candidate = LaxURLCodec.DEFAULT.decode(keyVal[1]);
                } catch (DecoderException e) {
                    continue;
                }
                // TODO: use other non-UTF8 codecs when appropriate
                m.reset(candidate);
                if(m.matches()) {
                    results.add(candidate);
                }
            }
        }
        return results;
    }

    public String report() {
        StringBuffer ret = new StringBuffer();
        ret.append("Processor: "+ExtractorURI.class.getName()+"\n");
        ret.append("  Function:          Extracts links inside other URIs\n");
        ret.append("  CrawlURIs handled: " + numberOfCURIsHandled + "\n");
        ret.append("  Links extracted:   " + numberOfLinksExtracted + "\n\n");

        return ret.toString();
    }
}
