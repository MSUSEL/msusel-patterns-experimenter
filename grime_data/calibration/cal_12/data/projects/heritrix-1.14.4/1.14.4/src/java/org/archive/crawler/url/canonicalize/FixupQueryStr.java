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




/**
 * Strip any trailing question mark.
 * @author stack
 * @version $Date: 2006-09-25 20:27:35 +0000 (Mon, 25 Sep 2006) $, $Revision: 4655 $
 */
public class FixupQueryStr
extends BaseRule {

    private static final long serialVersionUID = 3169526832544474794L;

    private static final String DESCRIPTION =
        "Fixup the question mark that leads off the query string. " +
        "This rule returns 'http://www.archive.org/index.html' if passed" +
        " 'http://www.archive.org/index.html?'.  It will also strip '?&'" +
        " if '?&' is all that comprises the query string.  Also strips" +
        " extraneous leading '&': Returns 'http://archive.org/index.html?x=y" +
        " if passed 'http://archive.org/index.html?&x=y." +
        " Will also strip '&' if last thing in query string." +
        " Operates on all schemes.  This is a good rule to run toward the" +
        " end of canonicalization processing.";

    public FixupQueryStr(String name) {
        super(name, DESCRIPTION);
    }

    public String canonicalize(String url, Object context) {
        if (url == null || url.length() <= 0) {
            return url;
        }
        
        int index = url.lastIndexOf('?');
        if (index > 0) {
            if (index == (url.length() - 1)) {
                // '?' is last char in url.  Strip it.
                url = url.substring(0, url.length() - 1);
            } else if (url.charAt(index + 1) == '&') {
                // Next char is '&'. Strip it.
                if (url.length() == (index + 2)) {
                    // Then url ends with '?&'.  Strip them.
                    url = url.substring(0, url.length() - 2);
                } else {
                    // The '&' is redundant.  Strip it.
                    url = url.substring(0, index + 1) +
                    url.substring(index + 2);
                }
            } else if (url.charAt(url.length() - 1) == '&') {
                // If we have a lone '&' on end of query str,
                // strip it.
                url = url.substring(0, url.length() - 1);
            }
        }
        return url;
    }
}
