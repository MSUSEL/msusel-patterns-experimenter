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

import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import org.apache.commons.httpclient.URIException;
import org.archive.crawler.datamodel.CoreAttributeConstants;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.framework.CrawlController;
import org.archive.io.ReplayCharSequence;
import org.archive.net.UURI;
import org.archive.util.DevUtils;
import org.archive.util.TextUtils;
import org.archive.util.UriUtils;

/**
 * Processes Javascript files for strings that are likely to be
 * crawlable URIs.
 *
 * @contributor gojomo
 * @contributor szznax
 *
 */
public class ExtractorJS extends Extractor implements CoreAttributeConstants {

    private static final long serialVersionUID = -2231962381454717720L;

    private static Logger LOGGER =
        Logger.getLogger("org.archive.crawler.extractor.ExtractorJS");

    // finds whitespace-free strings in Javascript
    // (areas between paired ' or " characters, possibly backslash-quoted
    // on the ends, but not in the middle)
    static final String JAVASCRIPT_STRING_EXTRACTOR =
        "(\\\\{0,8}+(?:\"|\'))(\\S{0,"+UURI.MAX_URL_LENGTH+"}?)(?:\\1)";
    // GROUPS:
    // (G1) ' or " with optional leading backslashes
    // (G2) whitespace-free string delimited on boths ends by G1


    protected long numberOfCURIsHandled = 0;
    protected static long numberOfLinksExtracted = 0;

    
    // URIs known to produce false-positives with the current JS extractor.
    // e.g. currently (2.0.3) the JS extractor produces 13 false-positive 
    // URIs from http://www.google-analytics.com/urchin.js and only 2 
    // good URIs, which are merely one pixel images.
    // TODO: remove this blacklist when JS extractor is improved 
    protected final static String[] EXTRACTOR_URI_EXCEPTIONS = {
        "http://www.google-analytics.com/urchin.js"
        };
    
    /**
     * @param name
     */
    public ExtractorJS(String name) {
        super(name, "JavaScript extractor. Link extraction on JavaScript" +
                " files (.js).");
    }

    /* (non-Javadoc)
     * @see org.archive.crawler.framework.Processor#process(org.archive.crawler.datamodel.CrawlURI)
     */
    public void extract(CrawlURI curi) {
        // special-cases, for when we know our current JS extractor does poorly.
        // TODO: remove this test when JS extractor is improved 
        for (String s: EXTRACTOR_URI_EXCEPTIONS) {
            if (curi.toString().equals(s))
                return;
        }
            
        if (!isHttpTransactionContentToProcess(curi)) {
            return;
        }
        String contentType = curi.getContentType();
        if ((contentType == null)) {
            return;
        }
        // If content type is not js and if the viaContext
        // does not begin with 'script', return.
        if((contentType.indexOf("javascript") < 0) &&
            (contentType.indexOf("jscript") < 0) &&
            (contentType.indexOf("ecmascript") < 0) &&
            (!curi.toString().toLowerCase().endsWith(".js")) &&
            (curi.getViaContext() == null || !curi.getViaContext().
                toString().toLowerCase().startsWith("script"))) {
            return;
        }

        this.numberOfCURIsHandled++;

        ReplayCharSequence cs = null;
        try {
            cs = curi.getHttpRecorder().getReplayCharSequence();
        } catch (IOException e) {
            curi.addLocalizedError(this.getName(), e,
            	"Failed get of replay char sequence.");
        }
        if (cs == null) {
            LOGGER.warning("Failed getting ReplayCharSequence: " +
                curi.toString());
            return;
        }

        try {
            try {
                numberOfLinksExtracted += considerStrings(curi, cs,
                        getController(), true);
            } catch (StackOverflowError e) {
                DevUtils.warnHandle(e, "ExtractorJS StackOverflowError");
            }
            // Set flag to indicate that link extraction is completed.
            curi.linkExtractorFinished();
        } finally {
            // Done w/ the ReplayCharSequence. Close it.
            if (cs != null) {
                try {
                    cs.close();
                } catch (IOException ioe) {
                    LOGGER.warning(TextUtils.exceptionToString(
                        "Failed close of ReplayCharSequence.", ioe));
                }
            }
        }
    }

    public static long considerStrings(CrawlURI curi, CharSequence cs,
            CrawlController controller, boolean handlingJSFile) {
        long foundLinks = 0;
        Matcher strings =
            TextUtils.getMatcher(JAVASCRIPT_STRING_EXTRACTOR, cs);
        while(strings.find()) {
            CharSequence subsequence =
                cs.subSequence(strings.start(2), strings.end(2));

            if(UriUtils.isLikelyUriJavascriptContextLegacy(subsequence)) {
                String string = subsequence.toString();
                string = UriUtils.speculativeFixup(string, curi.getUURI());
                foundLinks++;
                try {
                    if (handlingJSFile) {
                        curi.createAndAddLinkRelativeToVia(string,
                            Link.JS_MISC, Link.SPECULATIVE_HOP);
                    } else {
                        curi.createAndAddLinkRelativeToBase(string,
                            Link.JS_MISC, Link.SPECULATIVE_HOP);
                    }
                } catch (URIException e) {
                    // There may not be a controller (e.g. If we're being run
                    // by the extractor tool).
                    if (controller != null) {
                        controller.logUriError(e, curi.getUURI(), string);
                    } else {
                        LOGGER.info(curi + ", " + string + ": " +
                            e.getMessage());
                    }
                }
            } else {
               foundLinks += considerStrings(curi, subsequence,
                   controller, handlingJSFile);
            }
        }
        TextUtils.recycleMatcher(strings);
        return foundLinks;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.archive.crawler.framework.Processor#report()
     */
    public String report() {
        StringBuffer ret = new StringBuffer();
        ret.append("Processor: org.archive.crawler.extractor.ExtractorJS\n");
        ret.append("  Function:          Link extraction on JavaScript code\n");
        ret.append("  CrawlURIs handled: " + numberOfCURIsHandled + "\n");
        ret.append("  Links extracted:   " + numberOfLinksExtracted + "\n\n");

        return ret.toString();
    }
}
