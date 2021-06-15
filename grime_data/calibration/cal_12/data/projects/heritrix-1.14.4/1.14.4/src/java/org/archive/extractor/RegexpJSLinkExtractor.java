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

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.URIException;
import org.archive.crawler.extractor.Link;
import org.archive.net.UURI;
import org.archive.net.UURIFactory;
import org.archive.util.TextUtils;

/**
 * Uses regular expressions to find likely URIs inside Javascript.
 *
 * ROUGH DRAFT IN PROGRESS / incomplete... untested...
 * 
 * @author gojomo
 */
public class RegexpJSLinkExtractor extends CharSequenceLinkExtractor {
//    private static Logger logger =
//        Logger.getLogger(RegexpJSLinkExtractor.class.getName());

    static final String AMP = "&";
    static final String ESCAPED_AMP = "&amp;";
    static final String WHITESPACE = "\\s";

    // finds whitespace-free strings in Javascript
    // (areas between paired ' or " characters, possibly backslash-quoted
    // on the ends, but not in the middle)
    static final Pattern JAVASCRIPT_STRING_EXTRACTOR = Pattern.compile(
        "(\\\\{0,8}+(?:\"|\'))(.+?)(?:\\1)");

    // determines whether a string is likely URI
    // (no whitespace or '<' '>',  has an internal dot or some slash,
    // begins and ends with either '/' or a word-char)
    static final Pattern STRING_URI_DETECTOR = Pattern.compile(
        "(?:\\w|[\\.]{0,2}/)[\\S&&[^<>]]*(?:\\.|/)[\\S&&[^<>]]*(?:\\w|/)");

    Matcher strings;
    LinkedList<Matcher> matcherStack = new LinkedList<Matcher>();

    protected boolean findNextLink() {
        if(strings==null) {
             strings = JAVASCRIPT_STRING_EXTRACTOR.matcher(sourceContent);
        }
        while(strings!=null) {
            while(strings.find()) {
                CharSequence subsequence =
                    sourceContent.subSequence(strings.start(2), strings.end(2));
                Matcher uri = STRING_URI_DETECTOR.matcher(subsequence);
                if ((subsequence.length() <= UURI.MAX_URL_LENGTH) && uri.matches()) {
                    String string = uri.group();
                    string = TextUtils.replaceAll(ESCAPED_AMP, string, AMP);
                    try {
                        Link link = new Link(source, UURIFactory.getInstance(
                                source, string), Link.JS_MISC, Link.SPECULATIVE_HOP);
                        next.add(link);
                        return true;
                    } catch (URIException e) {
                        extractErrorListener.noteExtractError(e,source,string);
                    }
                } else {
                   //  push current range
                   matcherStack.addFirst(strings);
                   // start looking inside string
                   strings = JAVASCRIPT_STRING_EXTRACTOR.matcher(subsequence);
                }
            }
            // continue at enclosing range, if available
            strings = (Matcher) (matcherStack.isEmpty() ? null : matcherStack.removeFirst());
        }
        return false;
    }


    /* (non-Javadoc)
     * @see org.archive.extractor.LinkExtractor#reset()
     */
    public void reset() {
        super.reset();
        matcherStack.clear();
        strings = null;
    }

    protected static CharSequenceLinkExtractor newDefaultInstance() {
        return new RegexpJSLinkExtractor();
    }
}
