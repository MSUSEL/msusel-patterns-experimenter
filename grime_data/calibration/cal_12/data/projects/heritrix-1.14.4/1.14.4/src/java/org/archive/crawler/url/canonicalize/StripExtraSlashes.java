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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StripExtraSlashes extends BaseRule {

    private static final String DESCRIPTION = 
        "Strip any extra slashes, '/', found in the path. " +
        "Use this rule to equate 'http://www.archive.org//A//B/index.html' and " +
        "'http://www.archive.org/A/B/index.html'.";
        
    private static final Pattern REGEX = Pattern.compile("(^https?://.*?)//+(.*)");

    public StripExtraSlashes(String name) {
        super(name, DESCRIPTION);
    }

    public String canonicalize(String url, Object context) {
        Matcher matcher = REGEX.matcher(url);
        while (matcher.matches()) {
            url = matcher.group(1) + "/" + matcher.group(2);
            matcher = REGEX.matcher(url);
        }
        return url;
    }
}
