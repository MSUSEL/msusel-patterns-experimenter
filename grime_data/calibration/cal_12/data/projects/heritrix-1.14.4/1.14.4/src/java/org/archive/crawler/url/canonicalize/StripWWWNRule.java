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
package org.archive.crawler.url.canonicalize;

import java.util.regex.Pattern;



/**
 * Strip any 'www[0-9]*' found on http/https URLs IF they have some
 * path/query component (content after third slash). Top 'slash page' 
 * URIs are left unstripped: we prefer crawling redundant
 * top pages to missing an entire site only available from either
 * the www-full or www-less hostname, but not both. 
 * @author stack
 * @version $Date: 2006-09-18 20:32:47 +0000 (Mon, 18 Sep 2006) $, $Revision: 4634 $
 */
public class StripWWWNRule extends BaseRule {
    private static final long serialVersionUID = 3619916990307308590L;

    private static final String DESCRIPTION = "Strip any 'www[0-9]*' found. " +
        "Use this rule to equate 'http://www.archive.org/index.html' and " +
        "'http://www0001.archive.org/index.html' with " +
        "'http://archive.org/index.html'.  The resulting canonicalization " +
        "returns 'http://archive.org/index.html'.  It removes any www's " +
        "or wwwNNN's found, where 'N' is one or more numerics, EXCEPT " +
        "on URIs that have no path/query component " +
        ". Top-level 'slash page' URIs are left unstripped: we prefer " +
        "crawling redundant top pages to missing an entire site only " +
        "available from either the www-full or www-less hostname, but not " +
        "both.  Operates on http and https schemes only. " +
        "Use StripWWWRule to strip a lone 'www' only (This rule is a " +
        "more general version of StripWWWRule).";
    
    private static final Pattern REGEX =
        Pattern.compile("(?i)^(https?://)(?:www[0-9]*\\.)([^/]*/.+)$");

    public StripWWWNRule(String name) {
        super(name, DESCRIPTION);
    }

    public String canonicalize(String url, Object context) {
        return doStripRegexMatch(url, REGEX.matcher(url));
    }
}