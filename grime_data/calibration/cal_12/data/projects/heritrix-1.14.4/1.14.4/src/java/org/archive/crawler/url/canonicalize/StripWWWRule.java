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
 * Strip any 'www' found on http/https URLs, IF they have some
 * path/query component (content after third slash). (Top 'slash page' 
 * URIs are left unstripped, so that we prefer crawling redundant
 * top pages to missing an entire site only available from either
 * the www-full or www-less hostname, but not both). 
 * @author stack
 * @version $Date: 2006-09-25 20:27:35 +0000 (Mon, 25 Sep 2006) $, $Revision: 4655 $
 */
public class StripWWWRule extends BaseRule {

    private static final long serialVersionUID = -5416391108485746976L;

    private static final String DESCRIPTION = "Strip any 'www' found. " +
        "Use this rule to equate 'http://www.archive.org/index.html' and" +
        " 'http://archive.org/index.html'. The resulting canonicalization" +
        " returns 'http://archive.org/index.html'.  It removes any www's " +
        "found, except on URIs that have no path/query component " +
        "('slash' pages).  Operates on http and https schemes only. " +
        "Use the more general StripWWWNRule if you want to strip both 'www' " +
        "and 'www01', 'www02', etc.";
    
    private static final Pattern REGEX =
        Pattern.compile("(?i)^(https?://)(?:www\\.)([^/]*/.+)$");

    public StripWWWRule(String name) {
        super(name, DESCRIPTION);
    }

    public String canonicalize(String url, Object context) {
        return doStripRegexMatch(url, REGEX.matcher(url));
    }
}
