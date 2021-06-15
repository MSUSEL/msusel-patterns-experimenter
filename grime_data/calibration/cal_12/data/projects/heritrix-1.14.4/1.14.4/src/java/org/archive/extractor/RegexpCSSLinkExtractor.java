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
package org.archive.extractor;

import java.util.regex.Matcher;

import org.apache.commons.httpclient.URIException;
import org.archive.crawler.extractor.Link;
import org.archive.net.UURIFactory;
import org.archive.util.DevUtils;
import org.archive.util.TextUtils;

/**
 * This extractor is parsing URIs from CSS type files.
 * The format of a CSS URL value is 'url(' followed by optional white space
 * followed by an optional single quote (') or double quote (") character
 * followed by the URL itself followed by an optional single quote (') or
 * double quote (") character followed by optional white space followed by ')'.
 * Parentheses, commas, white space characters, single quotes (') and double
 * quotes (") appearing in a URL must be escaped with a backslash:
 * '\(', '\)', '\,'. Partial URLs are interpreted relative to the source of
 * the style sheet, not relative to the document. <a href="http://www.w3.org/TR/REC-CSS1#url">
 * Source: www.w3.org</a>
 *
 * ROUGH DRAFT IN PROGRESS / incomplete... untested... major changes likely
 *
 * @author igor gojomo
 *
 **/

public class RegexpCSSLinkExtractor extends CharSequenceLinkExtractor {

    // private static Logger logger =
    //    Logger.getLogger(RegexpCSSLinkExtractor.class.getName());

    private static String ESCAPED_AMP = "&amp";
    // CSS escapes: "Parentheses, commas, whitespace characters, single
    // quotes (') and double quotes (") appearing in a URL must be
    // escaped with a backslash"
    static final String CSS_BACKSLASH_ESCAPE = "\\\\([,'\"\\(\\)\\s])";

    protected Matcher uris;

    /**
     *  CSS URL extractor pattern.
     *
     *  This pattern extracts URIs for CSS files
     **/
    static final String CSS_URI_EXTRACTOR =
    "(?:@import (?:url[(]|)|url[(])\\s*([\\\"\']?)([^\\\"\'].*?)\\1\\s*[);]";

    protected boolean findNextLink() {
        if (uris == null) {
            uris = TextUtils.getMatcher(CSS_URI_EXTRACTOR, sourceContent);
            // NOTE: this matcher can't be recycled in this method because
            // it is reused on rentry
        }
        String cssUri;
        try {
            while (uris.find()) {
                cssUri = uris.group(2);
                // TODO: Escape more HTML Entities.
                cssUri = TextUtils.replaceAll(ESCAPED_AMP, cssUri, "&");
                // Remove backslashes when used as escape character in CSS URL
                cssUri = TextUtils.replaceAll(CSS_BACKSLASH_ESCAPE, cssUri, "$1");
                // TODO: handle relative URIs?
                try {
                    Link link = new Link(source, UURIFactory.getInstance(base,
                            cssUri), Link.EMBED_MISC, Link.EMBED_HOP);
                    next.addLast(link);
                } catch (URIException e) {
                    extractErrorListener.noteExtractError(e, source, cssUri);
                }
                return true;
            }
        } catch (StackOverflowError e) {
            DevUtils.warnHandle(e, "RegexpCSSLinkExtractor StackOverflowError");
        }
        return false;
    }

    public void reset() {
        super.reset();
        TextUtils.recycleMatcher(uris);
        uris = null;
    }
    
    protected static CharSequenceLinkExtractor newDefaultInstance() {
        return new RegexpCSSLinkExtractor();
    }
}
