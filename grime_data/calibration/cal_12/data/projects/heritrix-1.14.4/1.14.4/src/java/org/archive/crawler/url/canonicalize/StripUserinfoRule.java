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
 * Strip any 'userinfo' found on http/https URLs.
 * @author stack
 * @version $Date: 2006-09-25 20:27:35 +0000 (Mon, 25 Sep 2006) $, $Revision: 4655 $
 */
public class StripUserinfoRule extends BaseRule {

    private static final long serialVersionUID = -4271062607638914996L;

    private static final String DESCRIPTION = "Strip any 'userinfo' found. " +
        "Use this rule to equate 'http://stack:psswrd@archive.org/index.htm'" + 
        " and 'http://archive.org/index.htm'. The resulting canonicalization" +
        " returns 'http://archive.org/index.htm'. Removes any userinfo" +
        " found.  Operates on http/https/ftp/ftps schemes only.";
    
    /**
     * Strip userinfo.
     */
    private static final Pattern REGEX =
        Pattern.compile("^((?:(?:https?)|(?:ftps?))://)(?:[^/]+@)(.*)$",
            Pattern.CASE_INSENSITIVE);

    public StripUserinfoRule(String name) {
        super(name, DESCRIPTION);
    }

    public String canonicalize(String url, Object context) {
        return doStripRegexMatch(url, REGEX.matcher(url));
    }
}
